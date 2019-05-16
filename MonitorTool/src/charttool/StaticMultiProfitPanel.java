package charttool;

import gui.Mbox;
import hiflsklasse.DemoPanel;

import java.awt.Color;
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

public class StaticMultiProfitPanel
{
	// das MultiprofitPanel besetzt eine statische datenstruktur die die
	// einzelnen tradelisten speichert
	// beim genPanel werden die daten zusammen mit einem index übergeben
	// die Farbe wird im MultiProfitPanel vorgegeben
	// die Farbe kann über eine Funktion abgefragt werden

	// hier werden die Tradelisten gespeichert
	private static Tradeliste tradelist[] = new Tradeliste[5];
	private static int gdxlist[] = new int[5];
	private static String titlelist[] = new String[5];

	 public StaticMultiProfitPanel()
	{
	}

	static public void cleanAll()
	{
		for (int i = 0; i < 5; i++)
		{
			tradelist[i] = null;
			gdxlist[i] = 0;
			titlelist[i]=null;
		}
	}

	public JPanel genPanel(String title, int gdx, Tradeliste eatradeliste,
			String headline, int index)
	{
		tradelist[index] = eatradeliste;
		gdxlist[index] = gdx;
		titlelist[index] = title;

		DemoPanel panel = new DemoPanel(new GridLayout(1, 1));
		XYDataset dataset = createDataset();
		
		JFreeChart chart = createChart(dataset, headline);
		panel.add(new ChartPanel(chart));

		return panel;
	}

	private static XYDataset createDataset()
	{
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (int y = 0; y < 5; y++)
		{
			if (tradelist[y] == null)
				continue;

			XYSeries series1 = new XYSeries(titlelist[y]);
			int gd5period = gdxlist[y];
			int gd9period = 9;
			// die Preise im array eintragen

			Tradeliste tl = tradelist[y];
			if (tl == null)
				Mbox.Infobox("error tradeliste empty");

			int anz = tl.getsize();

			if (anz <= 0)
				Mbox.Infobox("Error tradelist empty size<" + anz + ">");

			// gesammtsumme der Gewinne in der graphik eintragen
			for (int i = 0; i < anz; i++)
				series1.add(i + 1, tl.get_tsumx(i));

			

			if (gd5period > 0)
			{
				// den gd5 eintragen
				XYSeries series2 = new XYSeries("GD" + gd5period);
				for (int i = 0; i < anz; i++)
					series2.add(i + 1, tl.calc_gdx(i, gd5period));

				// den gd9 eintragen
				XYSeries series3 = new XYSeries("GD" + gd9period);
				for (int i = 0; i < anz; i++)
					series3.add(i + 1, tl.calc_gdx(i, gd9period));
				dataset.addSeries(series2);
				dataset.addSeries(series3);
			}
			
			dataset.addSeries(series1);
		}

		
		
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset, String headline)
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

		
		plot.getRenderer().setSeriesPaint(0, Color.blue);
		plot.getRenderer().setSeriesPaint(1, Color.green);
		plot.getRenderer().setSeriesPaint(2, Color.red);
		plot.getRenderer().setSeriesPaint(3, Color.black);
		
		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}

	public JPanel createDemoPanel(Tradeliste eatradeliste, String headline,
			int gdx,int index)
	{
		tradelist[index] = eatradeliste;
		gdxlist[index] = gdx;
		titlelist[index] = headline;
		JFreeChart chart = createChart(createDataset(),
				headline);

		return new ChartPanel(chart);
	}

}
