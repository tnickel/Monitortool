package charttool;

import hiflsklasse.DemoPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import StartFrame.Tableview;
import data.Ea;
import data.GlobalVar;
import data.Trade;
import data.Tradeliste;

public class ShowAllProfitsConfig extends ApplicationFrame
{
	private static Tableview tv_glob=null;
	public ShowAllProfitsConfig(String title, Tableview tv,ArrayList<Tradeliste> alltradelist)
	{
		
		super(title);
		tv_glob=tv;
		JFrame frame = new JFrame("indicator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(5,5));
		
		DemoPanel panel = new DemoPanel(new GridLayout(100, 6));

		
		
		int anz = alltradelist.size();

		for (int i = 0; i < anz; i++)
		{
			Tradeliste eatradeliste = alltradelist.get(i);
			Trade trade=eatradeliste.getelem(0);
			int magic=trade.getMagic();
			String broker=trade.getBroker();
			String comment=trade.getComment();
			
			Ea ea=tv.getEaliste().getEa(magic, broker);
			String eainf="";
			if(ea.getInfo()!=null)
				eainf=ea.getInfo();
			String eaname = broker+" "+trade.getSymbol()+" "+magic+" "+comment+" "+eainf;
			eatradeliste.calcSummengewinne();
			XYDataset dataset = createDataset(eatradeliste);
			JFreeChart chart = createChart(dataset, eaname);
			panel.add(new ChartPanel(chart));
		
			 JPanel panel2 = new JPanel();
			JTextArea txtfeld = new JTextArea(10, 25);
			txtfeld.setText("Hallo");
			panel2.add(txtfeld);
			JTextArea txtfeld2 = new JTextArea(30, 25);
			txtfeld2.setText("Hallo2");
			panel2.add(txtfeld2);
			
			
			
			
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel , BorderLayout.WEST);
			frame.getContentPane().add(panel2 , BorderLayout.EAST);
			
		}
		// http://java-tutorial.org/jscrollpane.html
		JScrollPane scrollPane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// JScrollPane wird dem Dialog hinzugefügt
		frame.getContentPane().add(scrollPane);

		

		frame.pack();

		frame.setVisible(true);
	}

	private static XYDataset createDataset(Tradeliste tl)
	{
		XYSeries series1 = new XYSeries("summ");
		int gd5period=GlobalVar.getDefaultGd();
		int gd9period=GlobalVar.getSecondGd();
		// die Preise im array eintragen

		int anz = tl.getsize();
		
		// gesammtsumme der Gewinne in der graphik eintragen
		for (int i = 0; i < anz; i++)
		{
			
			series1.add((i + 1), tl.get_tsumx(i));
		}
		/*// den gd5 eintragen
		XYSeries series2 = new XYSeries("GD"+gd5period);
		for (int i = 0; i < anz; i++)
			series2.add(i + 1, tl.calc_gdx(i, gd5period));

		
		// den gd9 eintragen
				XYSeries series3 = new XYSeries("GD"+gd9period);
				for (int i = 0; i < anz; i++)
					series3.add(i + 1, tl.calc_gdx(i, gd9period));*/
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		/*dataset.addSeries(series2);
		dataset.addSeries(series3);*/
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

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
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

	public static void start(ArrayList<Tradeliste> alltradelist)
	{

		ShowAllProfitsConfig demo = new ShowAllProfitsConfig("Profitanzeige",tv_glob, alltradelist);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

}
