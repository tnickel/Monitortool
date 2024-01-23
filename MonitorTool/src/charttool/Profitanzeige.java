package charttool;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.eclipse.swt.widgets.Text;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import data.GlobalVar;
import data.Rootpath;
import data.Trade;
import data.Tradeliste;
import hiflsklasse.DemoPanel;
import hiflsklasse.FileAccess;

public class Profitanzeige extends ApplicationFrame
{
	public Profitanzeige(String title)
	{
		super(title);
	}
	
	public Profitanzeige(String title, Tradeliste eatradeliste, String headline, String storefile, Text prof7,
			Text prof30, Text profall, int anzEas)
	{
		super(title);
		
		JFrame frame = new JFrame("indicator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1250, 500);
		JTextArea txtfeld = new JTextArea(10, 25);
		
		if (prof7 != null)
		{
			// get the current font
			Font f = txtfeld.getFont();
			Font f2 = new Font(f.getFontName(), f.getStyle(), f.getSize() + 15);
			txtfeld.setFont(f2);
			txtfeld.setText("#Eas=" + anzEas + "\n" + "#Trades=" + +eatradeliste.getsize() + "\n" + "Weekly Profit="
					+ prof7.getText() + "\n" + "30 Days Profit=" + prof30.getText() + "\n" + "All Days Profit="
					+ profall.getText());
			
		}
		
		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));
		XYDataset dataset = createDataset(eatradeliste);
		JFreeChart chart = createChart(dataset, headline);
		panel.add(new ChartPanel(chart));
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.WEST);
		panel.add(txtfeld);
		
		// evtl. speichern
		if (storefile != null)
			try
			{
				ChartUtilities.saveChartAsJPEG(new File(storefile), chart, 1100, 350);
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		frame.setVisible(true);
	}
	
	private static XYDataset createDataset(Tradeliste tl)
	{
		XYSeries series1 = new XYSeries("summ");
		int gd5period = GlobalVar.getDefaultGd();
		int gd9period = GlobalVar.getSecondGd();
		// die Preise im array eintragen
		
		int anz = tl.getsize();
		
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
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setBaseShapesFilled(false);
		
		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		return chart;
		
	}
	
	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 * 
	 * @return A panel.
	 */
	public JPanel createDemoPanel(Tradeliste eatradeliste, String headline)
	{
		JFreeChart chart = createChart(createDataset(eatradeliste), headline);
		return new ChartPanel(chart);
	}
	
	public static String createProfitpicture(Tradeliste eatradeliste, String headline, int forceflag)
	{
		// falls forceflag==1, dann wird das Bild auf jeden Fall geschrieben
		
		Trade trade = eatradeliste.getelem(0);
		int magic = trade.getMagic();
		
		String broker = trade.getBroker();
		String fdir = Rootpath.getRootpath() + "\\picture";
		String fnam = fdir + "\\tradepic_" + broker + "_" + trade.getSymbol() + "_" + magic + ".jpg";
		
		if (fnam.contains("???"))
			fnam = fnam.replace("???", "null");
		
		// falls file schon da, dann mache nix
		if (forceflag == 0)
			if (FileAccess.FileAvailable(fnam) == true)
				return fnam;
			else // forceflag==1 => lösche altes bild
				FileAccess.FileDelete(fnam, 0);
			
		File fd = new File(fdir);
		fd.mkdir();
		
		JFreeChart chart = createChart(createDataset(eatradeliste), headline);
		try
		{
			ChartUtilities.saveChartAsJPEG(new File(fnam), chart, 640, 320);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fnam;
	}
	
	public static void start(Tradeliste eatradeliste, String headline, String storefile, Text prof7, Text prof30,
			Text profall,int anzEas)
	{
		
		Profitanzeige demo = new Profitanzeige("Profitanzeige", eatradeliste, headline, storefile, prof7, prof30,
				profall,anzEas);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		
	}
	
}
