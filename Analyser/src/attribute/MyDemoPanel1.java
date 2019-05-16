package attribute;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

import swingtest.DemoPanel;

class MyDemoPanel1 extends DemoPanel implements ChangeListener
{
	JScrollBar scroller;
	SlidingCategoryDataset dataset_g;

	public MyDemoPanel1(SlidingCategoryDataset datas)
	{
		super(new BorderLayout());
		//dataset_g = new SlidingCategoryDataset(createDataset(), 0,	 30);
		dataset_g=datas;
		//this.dataset = new SlidingCategoryDataset(dataset, 0, 30);

		// get data for diagrams
		JFreeChart chart = createChart(dataset_g);
		addChart(chart);
		ChartPanel cp1 = new ChartPanel(chart);
		cp1.setPreferredSize(new Dimension(400, 400));
		this.scroller = new JScrollBar(SwingConstants.VERTICAL, 0, 10, 0, 500);
		add(cp1);
		this.scroller.getModel().addChangeListener(this);
		JPanel scrollPanel = new JPanel(new BorderLayout());
		scrollPanel.add(this.scroller);
		scrollPanel.setBorder(BorderFactory.createEmptyBorder(66, 2, 2, 2));
		scrollPanel.setBackground(Color.white);
		add(scrollPanel, BorderLayout.EAST);
	}

	public void addData(float val)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// value 0...100
		dataset.addValue(val, "mmm", "xxxx ");
	}

	private CategoryDataset createDataset()
	{
		System.out.println("hallo");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < 500; i++)
		{
			// dataset.addValue(Math.random() * 100.0, "S1", "Series " + i);
			dataset.addValue(0.5 * 100.0, "mmm", "xxxx " + i);
		}
		return dataset;
	}

	private JFreeChart createChart(CategoryDataset dataset)
	{
		JFreeChart chart = ChartFactory.createBarChart(
				"SlidingCategoryDatasetDemo1", // chart title
				"Series", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // orientation
				false, // include legend
				true, // tooltips?
				false // URLs?
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setMaximumCategoryLabelWidthRatio(0.8f);
		domainAxis.setLowerMargin(0.02);
		domainAxis.setUpperMargin(0.02);

		// set the range axis to display integers only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setRange(0.0, 100.0);

		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f,
				0.0f, new Color(0, 0, 64));
		renderer.setSeriesPaint(0, gp0);

		return chart;
	}

	public void stateChanged(ChangeEvent e)
	{
		dataset_g.setFirstCategoryIndex(this.scroller.getValue());
	}
}
