package charttool;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.UnitType;

import Panels.EaConfigDis;
import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.Ea;
import data.GlobalVar;
import data.Metaconfig;
import data.Trade;
import data.Tradeliste;

public class ShowConfigAllP2 extends ApplicationFrame {
	// diese Klasse zeigt alles an in einer scollbaren liste an,
	// man kann hier die lostsize und das mm konfigurieren
	// hier möchte man nur schauen wie gut die EAs und der backtest laufen
	// vergleichen und die Lotsize einstellen.
	private static Tableview tv_glob = null;
	public ShowConfigAllP2(String title, Tableview tv, ArrayList<Tradeliste> alltradelist, Brokerview bv) 
	{
		super("title");
		int anz=5;
		JFrame frame = new JFrame("frame");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2, 10));
		frame.setSize(250, 150);
		//frame.setPreferredSize(new java.awt.Dimension(600, 1000));
		anz=alltradelist.size();
		
		//das ganze hat ein grid layout mit 2 Spalten
		DemoPanelX panelx = new DemoPanelX(new GridLayout(0, 2));
	    panelx.setPreferredSize(new java.awt.Dimension(1500, anz*350));

	    
		//for (int i = 0; i < anz; i++) 
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
			
			//spalte 1 ist der aktuelle chart
			panelx.add(new ChartPanel(chart));
			frame.getContentPane().setLayout(new BorderLayout());
			
			//spalte 2 ist die config
			String filedata=meconf.getFiledata();
			EaConfigDis cp = new EaConfigDis(magic,trade.getSymbol(), filedata,on,broker,bv,tv,tv.getEaliste(),eatradeliste);
			panelx.add(cp);
			frame.getContentPane().add(panelx);// , BorderLayout.WEST);
		}

		// http://java-tutorial.org/jscrollpane.html
		JScrollPane scrollPane = new JScrollPane(panelx, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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
	private static XYDataset createDataset(Tradeliste tl) {
		XYSeries series1 = new XYSeries("summ");
		
		// die Preise im array eintragen
		int anz = tl.getsize();
		
		// gesammtsumme der Gewinne in der graphik eintragen
		for (int i = 0; i < anz; i++)
		  series1.add((i + 1), tl.get_tsumx(i));
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset, String titel) {

		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(titel, // chart
																	// title
				"tr", // x axis label
				"eq", // y axis label
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
	public static JPanel createDemoPanel(Tradeliste eatradeliste, String titel) {
		JFreeChart chart = createChart(createDataset(eatradeliste), "titel");
		return new ChartPanel(chart);
	}

}
