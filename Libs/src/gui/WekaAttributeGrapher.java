package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class WekaAttributeGrapher extends ApplicationFrame {

    private Map<String, Map<String, JFreeChart>> attributeChartsMap;
    private JPanel chartsContainer;

    public WekaAttributeGrapher(String title, String rootPath) {
        super(title);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);

        // Obere Liste der Attribute
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        attributeChartsMap = new LinkedHashMap<String, Map<String, JFreeChart>>();
        List<File> csvFiles = findCSVFiles(new File(rootPath));
        for (File csvFile : csvFiles) {
            List<String[]> data = readCSV(csvFile.getAbsolutePath());
            if (data != null && data.size() > 1) {
                String[] headers = data.get(0);
                String directoryName = csvFile.getParentFile().getName();

                List<List<Double>> positiveValues = new ArrayList<List<Double>>();
                List<List<Double>> negativeValues = new ArrayList<List<Double>>();

                for (int i = 1; i < headers.length - 1; i++) {
                    positiveValues.add(new ArrayList<Double>());
                    negativeValues.add(new ArrayList<Double>());
                }

                for (int i = 1; i < data.size(); i++) {
                    String[] row = data.get(i);
                    if (row.length < headers.length) {
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
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }

                for (int i = 1; i < headers.length - 1; i++) {
                    String attributeName = headers[i];
                    JFreeChart chart = createHistogramWithLines(attributeName, positiveValues.get(i - 1), negativeValues.get(i - 1));

                    if (!attributeChartsMap.containsKey(attributeName)) {
                        attributeChartsMap.put(attributeName, new LinkedHashMap<String, JFreeChart>());
                        listModel.addElement(attributeName);
                    }
                    attributeChartsMap.get(attributeName).put(directoryName, chart);
                }
            }
        }

        final JList<String> attributeList = new JList<String>(listModel);
        attributeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attributeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedAttribute = attributeList.getSelectedValue();
                    updateChartsContainer(selectedAttribute);
                }
            }
        });
        JScrollPane listScrollPane = new JScrollPane(attributeList);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Untere Anzeige der Diagramme
        chartsContainer = new JPanel();
        chartsContainer.setLayout(new BoxLayout(chartsContainer, BoxLayout.Y_AXIS));
        JScrollPane chartsScrollPane = new JScrollPane(chartsContainer);
        chartsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        chartsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chartsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        splitPane.setTopComponent(listScrollPane);
        splitPane.setBottomComponent(chartsScrollPane);

        setContentPane(splitPane);
    }

    private void updateChartsContainer(String attributeName) {
        chartsContainer.removeAll();
        if (attributeName != null && attributeChartsMap.containsKey(attributeName)) {
            Map<String, JFreeChart> charts = attributeChartsMap.get(attributeName);
            for (Map.Entry<String, JFreeChart> chartEntry : charts.entrySet()) {
                String directoryName = chartEntry.getKey();
                JFreeChart chart = chartEntry.getValue();

                ChartPanel chartPanelForAttribute = new ChartPanel(chart);
                chartPanelForAttribute.setPreferredSize(new Dimension(400, 200));
                chartPanelForAttribute.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), directoryName, 0, 0, new java.awt.Font("Dialog", java.awt.Font.BOLD, 24)));
                chartsContainer.add(chartPanelForAttribute);
            }
        }
        chartsContainer.revalidate();
        chartsContainer.repaint();
    }

    private JFreeChart createHistogramWithLines(String attributeName, List<Double> positiveValues, List<Double> negativeValues) {
        HistogramDataset dataset = new HistogramDataset();

        double[] positiveArray = listToPrimitiveArray(positiveValues);
        dataset.addSeries("Positive Values", positiveArray, 20);

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

    private double[] listToPrimitiveArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

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
        grapher.setSize(1200, 800);
        grapher.setVisible(true);
        grapher.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
