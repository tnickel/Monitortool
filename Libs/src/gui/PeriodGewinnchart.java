package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class PeriodGewinnchart {

    public JPanel createChartPanel(String filePath) {
        // Load the data
        List<String[]> data = loadData(filePath);

        // Create datasets for the charts
        DefaultCategoryDataset lineDataset = createAccumulatedDataset(data);
        DefaultCategoryDataset barDataset = createBarDataset(data);

        // Create line chart for accumulated profits
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Accumulated Profits over Periods",  // Chart title
                "Period",                            // X-axis label
                "Accumulated Profit",                // Y-axis label
                lineDataset,                         // Data
                PlotOrientation.VERTICAL,
                true,                                // Include legend
                true,
                false
        );

        // Create bar chart for individual profits
        JFreeChart barChart = ChartFactory.createBarChart(
                "Profits (Bar Chart)",               // Chart title
                "Period",                            // X-axis label
                "Profit",                            // Y-axis label
                barDataset,                          // Data
                PlotOrientation.VERTICAL,
                true,                                // Include legend
                true,
                false
        );

        // Set the layout of the panel and add both charts
        JPanel chartPanel = new JPanel(new GridLayout(2, 1));

        // Create chart panels and add them to the panel
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        ChartPanel barChartPanel = new ChartPanel(barChart);

        lineChartPanel.setPreferredSize(new java.awt.Dimension(800, 300));
        barChartPanel.setPreferredSize(new java.awt.Dimension(800, 300));

        chartPanel.add(lineChartPanel);
        chartPanel.add(barChartPanel);

        return chartPanel;
    }

    private List<String[]> loadData(String filePath) {
        List<String[]> data = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("#");
                data.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reverse the order of the data
        Collections.reverse(data);

        return data;
    }

    private DefaultCategoryDataset createAccumulatedDataset(List<String[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double accumulatedProfit = 0;

        for (String[] entry : data) {
            int period = Integer.parseInt(entry[0].trim());
            double profit = Double.parseDouble(entry[1].trim());
            accumulatedProfit += profit;
            dataset.addValue(accumulatedProfit, "Accumulated Profit", String.valueOf(period));
        }

        return dataset;
    }

    private DefaultCategoryDataset createBarDataset(List<String[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String[] entry : data) {
            int period = Integer.parseInt(entry[0].trim());
            double profit = Double.parseDouble(entry[1].trim());
            dataset.addValue(profit, "Profit", String.valueOf(period));
        }

        return dataset;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the file path as a command-line argument.");
            System.exit(1);
        }

        String filePath = args[0];  // Get the file path from the command-line argument

        JFrame frame = new JFrame("Line Chart and Bar Chart Example");
        PeriodGewinnchart chartCreator = new PeriodGewinnchart();
        JPanel chartPanel = chartCreator.createChartPanel(filePath);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void show(String filePath)
    {
    	  JFrame frame = new JFrame("Line Chart and Bar Chart Example");
          PeriodGewinnchart chartCreator = new PeriodGewinnchart();
          JPanel chartPanel = chartCreator.createChartPanel(filePath);

          frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
          frame.add(chartPanel);
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
    	
    }
}
