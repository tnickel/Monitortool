package charttool;

/* ------------------
 * BarChartDemo1.java
 * ------------------
 * (C) Copyright 2002-2008, by Object Refinery Limited.
 *
 */

import hiflsklasse.DemoPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import data.Trade;
import data.Tradeliste;

/**
 * A simple demonstration application showing how to create a bar chart.
 */
public class HourChart
{
	/**
	 * 
	 */

	public HourChart()
	{

	}

	public JPanel genPanel(String title, Tradeliste eatradeliste,
			String headline)
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 1));
		CategoryDataset dataset = createDataset(eatradeliste);
		JFreeChart chart = createChart(dataset, headline);
		panel.add(new ChartPanel(chart));

		return panel;
	}

	private static CategoryDataset createDataset(Tradeliste tl)
	{

		DefaultCategoryDataset result = new DefaultCategoryDataset();
		// hour/(+|-|0)

		// die gewmatrix zählt die gewinne für jede Stunde
		double[] gewmatrix = new double[60];
		// die anzgewinne zählt die anzahl der gewinne je stunde
		int[] anzgewinne = new int[60];

		// der wochenname ist der type(0-6) und die serie das Result (+)
		// result.addValue(1.0, series1, type1);

		// 1) die Wochenmatrix berechnen
		// create the dataset...
		// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// value=1, (pos, neg, oder null), (wochentag 1-7)
		// dataset.addValue(1.0, series1, category1);

		int anz = tl.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tl.getelem(i);
			// weekday ermitteln
			Date d1 = tr.getOpentimeDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d1);

			int stunde = cal.get(Calendar.HOUR_OF_DAY);

			// das stundenarray berechnen

			if (stunde == 0)
				System.out.println("stunde=0");

			gewmatrix[stunde] = gewmatrix[stunde] + tr.getProfit();

			anzgewinne[stunde]++;

		}

		// 2)jetzt die resultmatrix füllen
		for (int i = 0; i < 24; i++)
		{
			// # positive results grün
			if (gewmatrix[i] > 0)
			{
				result.addValue(gewmatrix[i], "+", String.valueOf(i));
			} else
			{
				result.addValue(gewmatrix[i], "-", String.valueOf(i));
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

		StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));

		plot.getRenderer().setSeriesPaint(0, Color.green);
		plot.getRenderer().setSeriesPaint(1, Color.red);
	

		return chart;
	}

	public JPanel createDemoPanel(Tradeliste tl, String headline)
	{
		CategoryDataset dataset = createDataset(tl);
		JFreeChart chart = createChart(dataset, headline);
		return new ChartPanel(chart);
	}

}
