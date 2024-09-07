package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShowGoodBadCounter extends JPanel
{
	
	private final ArrayList<Integer> periodNumbers = new ArrayList<>();
	private final ArrayList<Integer> goodCounters = new ArrayList<>();
	private final ArrayList<Integer> badCounters = new ArrayList<>();
	private final ArrayList<Double> cumulativeRatios = new ArrayList<>();
	private double minRatio = Double.MAX_VALUE;
	private double maxRatio = Double.MIN_VALUE;
	
	public ShowGoodBadCounter(String filePath)
	{
		readDataFromFile(filePath);
		calculateCumulativeRatios();
		
		setLayout(new BorderLayout());
		
		// Erstellen des Balkendiagramms
		JFreeChart barChart = createBarChart();
		ChartPanel barChartPanel = new ChartPanel(barChart);
		
		// Erstellen des Liniendiagramms
		JFreeChart lineChart = createLineChart();
		ChartPanel lineChartPanel = new ChartPanel(lineChart);
		
		// Hinzufügen der Diagramme zum Panel
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, barChartPanel, lineChartPanel);
		splitPane.setResizeWeight(0.9); // Verhältnis 75% für Balkendiagramm, 25% für Liniendiagramm
		splitPane.setDividerLocation(0.9); // Festlegen der Divider-Position
		add(splitPane, BorderLayout.CENTER);
	}
	
	private void readDataFromFile(String filePath)
	{
		try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				if (line.contains("goodcounter") || line.contains("###"))
				{
					continue;
				}
				
				String[] parts = line.split("#");
				if (parts.length == 3)
				{
					int periodNumber = Integer.parseInt(parts[0]);
					int goodCounter = Integer.parseInt(parts[1]);
					int badCounter = Integer.parseInt(parts[2]);
					
					periodNumbers.add(periodNumber);
					goodCounters.add(goodCounter);
					badCounters.add(badCounter);
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void calculateCumulativeRatios()
	{
		int cumulativeGood = 0;
		int cumulativeBad = 0;
		
		System.out.println("Period | Cumulative Good | Cumulative Bad | Cumulative Ratio");
		
		for (int i = 0; i < goodCounters.size(); i++)
		{
			cumulativeGood += goodCounters.get(i);
			cumulativeBad += badCounters.get(i);
			
			double ratio = (cumulativeGood + cumulativeBad) == 0 ? 0.0
					: (double) cumulativeGood / (cumulativeGood + cumulativeBad);
			cumulativeRatios.add(ratio);
			
			// Aktualisiere Min- und Max-Werte
			if (ratio < minRatio)
				minRatio = ratio;
			if (ratio > maxRatio)
				maxRatio = ratio;
			
			// Ausgabe zur Überprüfung
			System.out.println(periodNumbers.get(i) + " | " + cumulativeGood + " | " + cumulativeBad + " | " + ratio);
		}
	}
	
	public double calculateTotalRatio()
	{
		int totalGood = 0;
		int totalBad = 0;
		
		for (int good : goodCounters)
		{
			totalGood += good;
		}
		
		for (int bad : badCounters)
		{
			totalBad += bad;
		}
		
		return (totalGood + totalBad) == 0 ? 0.0 : (double) totalGood / (totalGood + totalBad);
	}
	
	private JFreeChart createBarChart()
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for (int i = 0; i < periodNumbers.size(); i++)
		{
			dataset.addValue(goodCounters.get(i), "Good", periodNumbers.get(i));
			dataset.addValue(badCounters.get(i), "Bad", periodNumbers.get(i));
		}
		
		JFreeChart barChart = ChartFactory.createBarChart("Good vs Bad Counters", // Chart title
				"Period", // X-Axis Label
				"Count", // Y-Axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot orientation
				true, // Show legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
		);
		
		CategoryPlot plot = (CategoryPlot) barChart.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		
		// Entfernen des Schattens von den Balken
		renderer.setShadowVisible(false);
		
		// Farben für die Balken festlegen
		renderer.setSeriesPaint(0, Color.GREEN); // Good -> Grün
		renderer.setSeriesPaint(1, Color.RED); // Bad -> Rot
		
		// Abstand zwischen den Balken, die zur gleichen Kategorie gehören, verringern
		renderer.setItemMargin(0.1); // 10% Abstand zwischen den Balken in einer Kategorie
		
		// X-Achse Labels besser lesbar machen
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2.0) // 90
																													// Grad
		);
		
		// Schriftgröße der Labels erhöhen
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		
		return barChart;
	}
	
	private JFreeChart createLineChart()
	{
		XYSeries series = new XYSeries("Cumulative Good/Bad Ratio");
		
		for (int i = 0; i < periodNumbers.size(); i++)
		{
			double ratio = cumulativeRatios.get(i);
			series.add(i, ratio); // Verwenden des Index als X-Wert für die Synchronisation
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Cumulative Good/Bad Ratio Over Time", "Period", "Ratio",
				dataset, PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(minRatio, maxRatio); // Dynamisch basierend auf den Min/Max-Ratios
		
		return chart;
	}
	
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Usage: java ShowGoodBadCounter <file_path>");
			return;
		}
		
		String filePath = args[0];
		
		// Erstellen und Anzeigen des Fensters
		JFrame frame = new JFrame("Show Good and Bad Counter");
		
		// Erstellen des Panels mit den Diagrammen
		ShowGoodBadCounter chartPanel = new ShowGoodBadCounter(filePath);
		
		// Summenverhältnis berechnen und in der Überschrift anzeigen
		double totalRatio = chartPanel.calculateTotalRatio();
		frame.setTitle("Show Good and Bad Counter - Good/Bad Ratio = " + String.format("%.6f", totalRatio));
		
		// Hinzufügen des Panels zum Frame
		frame.setContentPane(chartPanel);
		frame.setSize(1600, 1200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
