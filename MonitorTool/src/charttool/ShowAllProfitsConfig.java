package charttool;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.UnitType;

import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.Ea;
import data.GlobalVar;
import data.Metaconfig;
import data.Trade;
import data.Tradeliste;
import hiflsklasse.DemoPanel;

public class ShowAllProfitsConfig extends ApplicationFrame
{
	private static Tableview tv_glob = null;
	
	public ShowAllProfitsConfig(String title, Tableview tv, ArrayList<Tradeliste> alltradelist, Brokerview bv)
	{
		
		super(title);
		tv_glob = tv;
		JFrame frame = new JFrame("indicator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(5, 5));
		
		DemoPanel panel = new DemoPanel(new GridLayout(100, 6));
		
		int anz = alltradelist.size();
		
		for (int i = 0; i < anz; i++)
		{
			Tradeliste eatradeliste = alltradelist.get(i);
			Trade trade = eatradeliste.getelem(0);
			int magic = trade.getMagic();
			String broker = trade.getBroker();
			String comment = trade.getComment();
			
			// meconf wird im augenblick noch nicht gebraucht
			Metaconfig meconf = bv.getMetaconfigByBrokername(broker);
			
			Ea ea = tv.getEaliste().getEa(magic, broker);
			int on = tv.getEaliste().getOn(magic, broker);
			
			String eainf = "";
			if (ea.getInfo() != null)
				eainf = ea.getInfo();
			String eaname = "<" + broker + "> <" + trade.getSymbol() + "> <" + magic + "> <" + comment + "> <" + eainf
					+ "tp<"+ea.getTp()+"> sl<"+ea.getSl()+"> on<" + on + ">";
			eatradeliste.calcSummengewinne();
			XYDataset dataset = createDataset(eatradeliste);
			JFreeChart chart = createChart(dataset, eaname);
			if (on == 1)
			addSubtitle(chart);
			
			panel.add(new ChartPanel(chart));
			
			JPanel panel2 = new JPanel();
			JTextArea txtfeld = new JTextArea(10, 15);
			txtfeld.setText("Hallo");
			panel2.add(txtfeld);
			JTextArea txtfeld2 = new JTextArea(10, 25);
			txtfeld2.setText("Hallo2");
			panel2.add(txtfeld2);
			
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.WEST);
			frame.getContentPane().add(panel2, BorderLayout.EAST);
			
		}
		// http://java-tutorial.org/jscrollpane.html
		JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// JScrollPane wird dem Dialog hinzugefügt
		frame.getContentPane().add(scrollPane);
		
		frame.pack();
		
		frame.setVisible(true);
	}
	
	private void addSubtitle(JFreeChart chart)
	{
		TextTitle subtitle = new TextTitle("****** Connected to Realaccount !!! *******");
		subtitle.setFont(new Font("SansSerif", Font.BOLD, 30));
		subtitle.setPosition(RectangleEdge.TOP);
		subtitle.setPadding(new RectangleInsets(UnitType.RELATIVE, 0.05, 0.05, 0.05, 0.05));
		subtitle.setVerticalAlignment(VerticalAlignment.CENTER);
		chart.addSubtitle(subtitle);
		
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
		{
			
			series1.add((i + 1), tl.get_tsumx(i));
		}
		/*
		 * // den gd5 eintragen XYSeries series2 = new XYSeries("GD"+gd5period); for
		 * (int i = 0; i < anz; i++) series2.add(i + 1, tl.calc_gdx(i, gd5period));
		 * 
		 * 
		 * // den gd9 eintragen XYSeries series3 = new XYSeries("GD"+gd9period); for
		 * (int i = 0; i < anz; i++) series3.add(i + 1, tl.calc_gdx(i, gd9period));
		 */
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		/*
		 * dataset.addSeries(series2); dataset.addSeries(series3);
		 */
		return dataset;
	}
	
	private static JFreeChart createChart(XYDataset dataset, String titel)
	{
		
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(titel, // chart
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
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		
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
	public static JPanel createDemoPanel(Tradeliste eatradeliste, String titel)
	{
		JFreeChart chart = createChart(createDataset(eatradeliste), titel);
		return new ChartPanel(chart);
	}
	
	public static void start(ArrayList<Tradeliste> alltradelist, Brokerview bv)
	{
		
		ShowAllProfitsConfig demo = new ShowAllProfitsConfig("Profitanzeige", tv_glob, alltradelist, bv);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		
	}
	
}
