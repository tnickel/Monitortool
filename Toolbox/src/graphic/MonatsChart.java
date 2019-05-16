package graphic;

import hiflsklasse.DemoPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import data.GewinnjahrStategienSum;

public class MonatsChart
{
	public MonatsChart(GewinnjahrStategienSum gewsum)
	{
		JFrame frame = new JFrame("indicator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1600, 500);

		DemoPanel panel = new DemoPanel(new BorderLayout());
		CategoryDataset dataset = createDataset(gewsum);
		JFreeChart chart = createChart(dataset, "Gewinnübersicht"+gewsum.getZeitraumname());
		panel.add(new ChartPanel(chart));

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel);

		frame.setVisible(true);
		gewsum.showResult();
	}

	private static CategoryDataset createDataset(GewinnjahrStategienSum gewsumme)
	{

		DefaultCategoryDataset result = new DefaultCategoryDataset();
		// wochentag/(+|-|0)

		// der wochenname ist der type(0-6) und die serie das Result (+,-,0)
		// result.addValue(1.0, series1, type1);
		String[] moname =
		{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };

		// 1) die Wochenmatrix berechnen
		// create the dataset...
		// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// value=1, (pos, neg, oder null), (wochentag 1-7) montag =1
		// dataset.addValue(1.0, series1, category1);

		int lastjahrindex = gewsumme.calcLastJahresIndex();

		for (int j = gewsumme.getFirstJahresIndex(); j < lastjahrindex; j++)
		{

			for (int i = 0; i < 12; i++)
			{
				float gewsum = gewsumme.getMonatsgewinn(j, i);

				if (gewsum > 0)
					result.addValue(gewsumme.getMonatsgewinn(j, i), "+", j+
							"|"+ moname[i]);
				else
					result.addValue(gewsumme.getMonatsgewinn(j, i), "-", j
							+ moname[i]);
			}
		}
		return result;
	}

	private static JFreeChart createChart(CategoryDataset dataset,
			String headline)
	{
		// create the chart...
		JFreeChart chart = ChartFactory.createStackedBarChart(headline, // chart
																		// title
				"Category", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDomainGridlinesVisible(true);
		plot.setRangeCrosshairVisible(true);
		plot.setRangeCrosshairPaint(Color.green);
		
		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRange(true);
		rangeAxis.setAutoRangeIncludesZero(true);
	

				
		StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(true);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

	
		renderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator(
				"Tooltip: {0}"));

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		
		plot.getRenderer().setSeriesPaint(0, Color.red);
		plot.getRenderer().setSeriesPaint(1, Color.green);
		return chart;
	}
}
