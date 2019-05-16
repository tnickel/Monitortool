package swingHilfsfenster;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import StartFrame.Tableview;
import charttool.ShowAllProfits;
import data.Ea;
import data.GlobalVar;
import data.Trade;
import data.Tradeliste;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ShowUebersicht extends javax.swing.JInternalFrame
{
	private static Tableview tv_glob = null;

	public ShowUebersicht(String title, Tableview tv,
			ArrayList<Tradeliste> alltradelist)
	{
		super(title);
		tv_glob = tv;
		JFrame frame = new JFrame("indicator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new GridLayout(15,2));
		
		JPanel panel = new JPanel();
		
		int anz = alltradelist.size();

		for (int i = 0; i < anz; i++)
		{
			
			Tradeliste eatradeliste = alltradelist.get(i);
			Trade trade = eatradeliste.getelem(0);
			int magic = trade.getMagic();
			String broker = trade.getBroker();
			String comment = trade.getComment();

			Ea ea = tv.getEaliste().getEa(magic, broker);
			String eainf = "";
			if (ea.getInfo() != null)
				eainf = ea.getInfo();
			String eaname = broker + " " + magic + " " + comment + " " + eainf;
			eatradeliste.calcSummengewinne();
			XYDataset dataset = createDataset(eatradeliste);
			JFreeChart chart = createChart(dataset, eaname);
			panel.add(new ChartPanel(chart));
			
			frame.getContentPane().setLayout(new BorderLayout());
			frame.add(panel , BorderLayout.WEST);
		
			frame.add(new JButton("Button 1"),BorderLayout.EAST);
			
			
			   // frame.add(new ChartPanel(chart));
			   // frame.add(new ChartPanel(chart));
			
		}
		// http://java-tutorial.org/jscrollpane.html
		JScrollPane scrollPane = new JScrollPane(frame,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// JScrollPane wird dem Dialog hinzugefügt
		frame.getContentPane().add(scrollPane);

		// panel.add(txtfeld, BorderLayout.EAST);

		frame.pack();

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

		// den gd9 eintragen
		XYSeries series3 = new XYSeries("GD" + gd9period);
		for (int i = 0; i < anz; i++)
			series3.add(i + 1, tl.calc_gdx(i, gd9period));

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
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

		ShowAllProfits demo = new ShowAllProfits("Profitanzeige", tv_glob,
				alltradelist);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}
	
	private void initGUI() {
		try {
			{
				this.setPreferredSize(new java.awt.Dimension(1027, 612));
				this.setBounds(0, 0, 1027, 612);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
