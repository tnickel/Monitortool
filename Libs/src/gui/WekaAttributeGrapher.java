package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WekaAttributeGrapher extends ApplicationFrame {

    private int lastScrollPosition = 0;

    public WekaAttributeGrapher(String title, String rootPath) {
        super(title);
        JTabbedPane tabbedPane = new JTabbedPane();

        // Alle unterverzeichnisse durchsuchen und CSV-Dateien finden
        List<File> csvFiles = findCSVFiles(new File(rootPath));

        for (File csvFile : csvFiles) {
            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));

            List<String[]> data = readCSV(csvFile.getAbsolutePath());
            if (data != null && data.size() > 1) {
                // Die erste Zeile enthält die Attributnamen
                String[] headers = data.get(0);

                // Liste für positive und negative Werte
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
                }

                // Erstellen und Hinzufügen von Diagrammen für jedes Attribut
                for (int i = 1; i < headers.length - 1; i++) {
                    String attributeName = headers[i];

                    // Erstellen des Histogramms für positive und negative Werte
                    JFreeChart chart = createHistogramWithLines(attributeName, positiveValues.get(i - 1), negativeValues.get(i - 1));
                    ChartPanel chartPanelForAttribute = new ChartPanel(chart);
                    chartPanelForAttribute.setPreferredSize(new Dimension(600, 300)); // Kleinere Diagrammgröße für bessere Darstellung
                    chartPanel.add(chartPanelForAttribute);
                }
            }

            JScrollPane scrollPane = new JScrollPane(chartPanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            String directoryName = csvFile.getParentFile().getName();
            String tabName = directoryName.substring(directoryName.indexOf('_') + 1);
            tabbedPane.addTab(tabName, scrollPane);
        }

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                JScrollPane selectedScrollPane = (JScrollPane) sourceTabbedPane.getSelectedComponent();
                if (selectedScrollPane != null) {
                    selectedScrollPane.getVerticalScrollBar().setValue(lastScrollPosition);
                    selectedScrollPane.getVerticalScrollBar().addAdjustmentListener(new java.awt.event.AdjustmentListener() {
                        @Override
                        public void adjustmentValueChanged(java.awt.event.AdjustmentEvent adjustmentEvent) {
                            lastScrollPosition = adjustmentEvent.getValue();
                        }
                    });
                }
            }
        });

        setContentPane(tabbedPane);
    }

    // Methode zum Erstellen eines Histogramms mit Linien
    private JFreeChart createHistogramWithLines(String attributeName, List<Double> positiveValues, List<Double> negativeValues) {
        HistogramDataset dataset = new HistogramDataset();

        // Hinzufügen der positiven Werte
        double[] positiveArray = new double[positiveValues.size()];
        for (int i = 0; i < positiveValues.size(); i++) {
            positiveArray[i] = positiveValues.get(i);
        }
        dataset.addSeries("Positive Values", positiveArray, 20);

        // Hinzufügen der negativen Werte
        double[] negativeArray = new double[negativeValues.size()];
        for (int i = 0; i < negativeValues.size(); i++) {
            negativeArray[i] = negativeValues.get(i);
        }
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

    // Methode zum Lesen der CSV-Datei
    private List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<String[]>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Methode zum Finden aller CSV-Dateien im Verzeichnis
    private List<File> findCSVFiles(File root) {
        List<File> csvFiles = new ArrayList<>();
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    csvFiles.addAll(findCSVFiles(file));
                } else if (file.getName().equals("exported_for_weka.csv")) {
                    csvFiles.add(file);
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
        grapher.setSize(grapher.getWidth(), grapher.getHeight() - 5);
        grapher.setVisible(true);
        grapher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
