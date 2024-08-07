package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
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

public class HeatMapLine extends ApplicationFrame
{
	String title_glob="";
	public  HeatMapLine(String title,String filename)
	{
		
		super(title);
		title_glob=title;
		JFreeChart heatmapChart = createChart(createDataset(filename));
		ChartPanel chartPanel = new ChartPanel(heatmapChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1500, 400));
		setContentPane(chartPanel);
	}
	
	private DefaultXYZDataset createDataset(String filename)
	{
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		ArrayList<Double> values = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			String line = br.readLine();
			String[] tokens = line.split("[\\t ]+");
			for (String token : tokens)
			{
				values.add(Double.parseDouble(token));
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int n = values.size();
		double[] xValues = new double[n];
		double[] yValues = new double[n];
		double[] zValues = new double[n];
		
		for (int i = 0; i < n; i++)
		{
			xValues[i] = i;
			yValues[i] = 0;
			zValues[i] = values.get(i);
		}
		
		double[][] data =
		{ xValues, yValues, zValues };
		dataset.addSeries("Heatmap", data);
		return dataset;
	}
	
	private JFreeChart createChart(DefaultXYZDataset dataset)
	{
		JFreeChart chart = ChartFactory.createScatterPlot(title_glob, "X", "Y", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		
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
		yAxis.setRange(-0.5, 0.5);
		
		// Add annotations for each data point
		int seriesCount = dataset.getSeriesCount();
		for (int series = 0; series < seriesCount; series++)
		{
			int itemCount = dataset.getItemCount(series);
			for (int item = 0; item < itemCount; item++)
			{
				double x = dataset.getXValue(series, item);
				double y = dataset.getYValue(series, item);
				double z = dataset.getZValue(series, item);
				XYTextAnnotation annotation = new XYTextAnnotation(String.format("%.2f", z), x, y);
				annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
				annotation.setPaint(Color.black);
				plot.addAnnotation(annotation);
			}
		}
		
		return chart;
	}
	
	public void start(String filename)
	{
		HeatMapLine example = new HeatMapLine(title_glob, filename);
		example.pack();
		RefineryUtilities.centerFrameOnScreen(example);
		example.setVisible(true);
	}
	
	static class GradientPaintScale implements org.jfree.chart.renderer.PaintScale
	{
		
		private final double lowerBound;
		private final double upperBound;
		
		public GradientPaintScale()
		{
			this.lowerBound = -0.1;
			this.upperBound = 0.33;
		}
		
		@Override
		public double getLowerBound()
		{
			return lowerBound;
		}
		
		@Override
		public double getUpperBound()
		{
			return upperBound;
		}
		
		@Override
		public Paint getPaint(double value)
		{
			float scale = (float) ((value - lowerBound) / (upperBound - lowerBound));
			return new Color(Color.HSBtoRGB(0.5f - scale * 0.5f, 1.0f, 1.0f));
		}
	}
}
