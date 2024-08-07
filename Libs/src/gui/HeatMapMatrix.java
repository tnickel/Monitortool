package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HeatMapMatrix extends ApplicationFrame {
	private String title_glob="";
    public HeatMapMatrix(String title, String filename) {
        super(title);
        title_glob=title;
        JFreeChart heatmapChart = createChart(createDataset(filename));
        ChartPanel chartPanel = new ChartPanel(heatmapChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 800));
        setContentPane(chartPanel);
    }

    private DefaultXYZDataset createDataset(String filename) {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        ArrayList<ArrayList<Double>> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("[\\t ]+");
                ArrayList<Double> row = new ArrayList<>();
                for (String token : tokens) {
                    try {
                        row.add(Double.parseDouble(token));
                    } catch (NumberFormatException e) {
                        // Ignoriere das Token, wenn es kein numerischer Wert ist
                    }
                }
                if (!row.isEmpty()) {
                    values.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numRows = values.size();
        int numCols = values.get(0).size();
        double[] xValues = new double[numRows * numCols];
        double[] yValues = new double[numRows * numCols];
        double[] zValues = new double[numRows * numCols];

        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                xValues[index] = j;
                yValues[index] = i;
                zValues[index] = values.get(i).get(j);
                index++;
            }
        }

        double[][] data = { xValues, yValues, zValues };
        dataset.addSeries("Heatmap", data);
        return dataset;
    }

    private JFreeChart createChart(DefaultXYZDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
        		title_glob,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockWidth(1.0);
        renderer.setBlockHeight(1.0);
        renderer.setPaintScale(new GradientPaintScale());
        plot.setRenderer(renderer);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0);
        xAxis.setUpperMargin(0);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0);
        yAxis.setUpperMargin(0);
        yAxis.setRange(-0.5, yAxis.getUpperBound() - 0.5);

        // Add annotations for each data point
        int itemCount = dataset.getItemCount(0);
        for (int item = 0; item < itemCount; item++) {
            double x = dataset.getXValue(0, item);
            double y = dataset.getYValue(0, item);
            double z = dataset.getZValue(0, item);
            XYTextAnnotation annotation = new XYTextAnnotation(
                    String.format("%.2f", z), x, y);
            annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
            annotation.setPaint(Color.black);
            plot.addAnnotation(annotation);
        }

        return chart;
    }

    public void start(String filename) {
        HeatMapMatrix example = new HeatMapMatrix(title_glob, filename);
        example.pack();
        RefineryUtilities.centerFrameOnScreen(example);
        example.setVisible(true);
    }

    static class GradientPaintScale implements org.jfree.chart.renderer.PaintScale {

        private final double lowerBound;
        private final double upperBound;

        public GradientPaintScale() {
            this.lowerBound = -0.2;
            this.upperBound = 0.6;
        }

        @Override
        public double getLowerBound() {
            return lowerBound;
        }

        @Override
        public double getUpperBound() {
            return upperBound;
        }

        @Override
        public Paint getPaint(double value) {
            if (value < 0) {
                float scale = (float) ((value - lowerBound) / -lowerBound);
                return new Color(Color.HSBtoRGB(0.7f, scale, 1.0f)); // Shades of blue
            } else {
                float scale = (float) (value / upperBound);
                return new Color(Color.HSBtoRGB(0.0f, scale, 1.0f)); // Shades of red
            }
        }
    }
}
