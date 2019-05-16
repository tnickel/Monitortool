package charttool;

import gui.Mbox;
import hiflsklasse.DemoPanel;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import data.Tradeliste;

public class ProfitPanel
{
	public ProfitPanel()
	{}
	
	public JPanel genPanel(String title, int gdx,Tradeliste eatradeliste, String headline)
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 1));
		XYDataset dataset = createDataset(eatradeliste,gdx);
		JFreeChart chart = createChart(dataset, headline);
		panel.add(new ChartPanel(chart));

		return panel;
	}

	private static XYDataset createDataset(Tradeliste tl,int gdx)
	{
		XYSeries series1 = new XYSeries("summ");
		int gd5period = gdx;
		int gd9period = 9;
		// die Preise im array eintragen

		if(tl==null)
			Mbox.Infobox("error tradeliste empty");
		
		int anz = tl.getsize();

		if(anz<=0)
		Mbox.Infobox("Error tradelist empty size<"+anz+">");
		
		// gesammtsumme der Gewinne in der graphik eintragen
		for (int i = 0; i < anz; i++)
			series1.add(i + 1, tl.get_tsumx(i));

		// den gd5 eintragen
		XYSeries series2 = new XYSeries("GD" + gd5period);
		for (int i = 0; i < anz; i++)
			series2.add(i + 1, tl.calc_gdx(i, gd5period));

		// den gd5 eintragen
		XYSeries series3 = new XYSeries("GD" + gd9period);
		for (int i = 0; i < anz; i++)
			series3.add(i + 1, tl.calc_gdx(i, gd9period));

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset,String headline)
	{

		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(headline, // chart
																	// title
				"Trade", // x axis label
				"Equity", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setBaseShapesFilled(false);

		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}
	
	public  JPanel createDemoPanel(Tradeliste eatradeliste,
			String headline,int gdx)
	{
		JFreeChart chart = createChart(createDataset(eatradeliste,gdx),headline);
		
		return new ChartPanel(chart);
	}

}
