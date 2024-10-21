package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class WekaAttributeGrapher extends ApplicationFrame {

    private int lastScrollPosition = 0;

    public WekaAttributeGrapher(String title, String rootPath) {
        super(title);
        JTabbedPane tabbedPane = new JTabbedPane();
tabbedPane.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 26));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel chartsContainer = new JPanel();
        chartsContainer.setLayout(new BoxLayout(chartsContainer, BoxLayout.Y_AXIS));

        JScrollPane chartsScrollPane = new JScrollPane(chartsContainer);
chartsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
chartsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chartsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Alle Unterverzeichnisse durchsuchen und CSV-Dateien finden
        List<File> csvFiles = findCSVFiles(new File(rootPath));

        Map<String, Map<String, JFreeChart>> attributeChartsMap = new LinkedHashMap<String, Map<String, JFreeChart>>();
        for (File csvFile : csvFiles) {
            List<String[]> data = readCSV(csvFile.getAbsolutePath());
            if (data != null && data.size() > 1) {
                // Die erste Zeile enthält die Attributnamen
                String[] headers = data.get(0);
                String directoryName = csvFile.getParentFile().getName();

                // Listen für positive und negative Werte initialisieren
                List<List<Double>> positiveValues = new ArrayList<List<Double>>();
                List<List<Double>> negativeValues = new ArrayList<List<Double>>();

                // Initialisieren von Listen für jedes numerische Attribut (Strategienamen überspringen)
                for (int i = 1; i < headers.length - 1; i++) {
                    positiveValues.add(new ArrayList<Double>());
                    negativeValues.add(new ArrayList<Double>());
                }

                // Verarbeiten der Datenzeilen
                for (int i = 1; i < data.size(); i++) {
                    String[] row = data.get(i);
                    if (row.length < headers.length) {
                        // Überspringen von Zeilen mit unzureichenden Spalten
                        continue;
                    }
                    try {
                        double wekaEndtestValue = Double.parseDouble(row[headers.length - 1]);

                        for (int j = 1; j < headers.length - 1; j++) {
                            try {
                                double attributeValue = Double.parseDouble(row[j]);

                                if (wekaEndtestValue >= 0) {
                                    positiveValues.get(j - 1).add(attributeValue);
                                } else {
                                    negativeValues.get(j - 1).add(attributeValue);
                                }
                            } catch (NumberFormatException e) {
                                // Überspringen von nicht-numerischen Attributen
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Überspringen der gesamten Zeile, wenn der Endtest-Wert nicht numerisch ist
                    }
                }

                // Erstellen und Hinzufügen von Diagrammen für jedes Attribut
                for (int i = 1; i < headers.length - 1; i++) {
                    String attributeName = headers[i];

                    // Erstellen des Histogramms für positive und negative Werte
                    JFreeChart chart = createHistogramWithLines(attributeName, positiveValues.get(i - 1), negativeValues.get(i - 1));

                    if (!attributeChartsMap.containsKey(attributeName)) {
                        attributeChartsMap.put(attributeName, new LinkedHashMap<String, JFreeChart>());
                    }
                    attributeChartsMap.get(attributeName).put(directoryName, chart);
                }
            }
        }

        // Erstellen der Tabs, wobei jedes Attribut in einem eigenen Scrollbereich dargestellt wird
        for (Map.Entry<String, Map<String, JFreeChart>> entry : attributeChartsMap.entrySet()) {
            String attributeName = entry.getKey();
            Map<String, JFreeChart> charts = entry.getValue();

            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
            for (Map.Entry<String, JFreeChart> chartEntry : charts.entrySet()) {
                String directoryName = chartEntry.getKey();
                JFreeChart chart = chartEntry.getValue();

                ChartPanel chartPanelForAttribute = new ChartPanel(chart);
                chartPanelForAttribute.setPreferredSize(new Dimension(800, 400)); // Kleinere Diagrammgröße für bessere Darstellung
                chartPanelForAttribute.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), directoryName, 0, 0, new java.awt.Font("Dialog", java.awt.Font.BOLD, 24)));
                chartPanel.add(chartPanelForAttribute);
            }

            JScrollPane scrollPane = new JScrollPane(chartPanel);
scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            tabbedPane.addTab(attributeName, scrollPane);
        }

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        chartsScrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
        setContentPane(mainPanel);
    }

    // Methode zum Erstellen eines Histogramms mit Linien
    private JFreeChart createHistogramWithLines(String attributeName, List<Double> positiveValues, List<Double> negativeValues) {
        HistogramDataset dataset = new HistogramDataset();

        // Hinzufügen der positiven Werte
        double[] positiveArray = listToPrimitiveArray(positiveValues);
        dataset.addSeries("Positive Values", positiveArray, 20);

        // Hinzufügen der negativen Werte
        double[] negativeArray = listToPrimitiveArray(negativeValues);
        dataset.addSeries("Negative Values", negativeArray, 20);

        JFreeChart chart = ChartFactory.createHistogram(
                "Distribution for " + attributeName,
                attributeName,
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Linien hinzufügen, um die Verteilungen besser sichtbar zu machen
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesPaint(1, Color.RED);
        plot.setRenderer(renderer);

        return chart;
    }

    // Hilfsmethode zum Konvertieren von List<Double> zu double[]
    private double[] listToPrimitiveArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    // Verbesserte Methode zum Lesen der CSV-Datei mit OpenCSV
    private List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<String[]>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath));
            data = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    // Methode zum Finden aller CSV-Dateien im Verzeichnis
    private List<File> findCSVFiles(File root) {
        List<File> csvFiles = new ArrayList<File>();
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        csvFiles.addAll(findCSVFiles(file));
                    } else if (file.getName().equalsIgnoreCase("exported_for_weka.csv")) {
                        csvFiles.add(file);
                    }
                }
            }
        }
        return csvFiles;
    }

    public static void main(String[] args) {
        String rootPath = "C:\\forex\\Metrikanalyser\\AR\\A02 EURUSD M15 Thomas Nikel Project";
        displayGraphs(rootPath);
    }

    public static void displayGraphs(String rootPath) {
        WekaAttributeGrapher grapher = new WekaAttributeGrapher("Weka Attribute Grapher", rootPath);
        grapher.pack();
        grapher.setSize(1200, 800); // Setzen Sie eine feste Größe für bessere Konsistenz
        grapher.setVisible(true);
        grapher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
