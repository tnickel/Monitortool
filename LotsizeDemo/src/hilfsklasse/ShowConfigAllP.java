package hilfsklasse;

import java.awt.BorderLayout;
import java.awt.GridLayout;

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
import org.jfree.ui.ApplicationFrame;

import Panels.EaConfigDis;

public class ShowConfigAllP extends ApplicationFrame {
	// diese Klasse zeigt alles an in einer scollbaren liste an,
	// man kann hier die lostsize und das mm konfigurieren
	// hier möchte man nur schauen wie gut die EAs und der backtest laufen
	// vergleichen und die Lotsize einstellen.
	public ShowConfigAllP() {
		super("title");

		JFrame frame = new JFrame("frame");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2, 10));
		frame.setSize(350, 150);
		// frame.setPreferredSize(new java.awt.Dimension(600, 1000));

		
		//das ganze hat ein grid layout mit 2 Spalten
		DemoPanelX panelx = new DemoPanelX(new GridLayout(0, 3));
	    panelx.setPreferredSize(new java.awt.Dimension(800, 800));

		for (int i = 0; i < 3; i++) {

			int magic = 5;
			String br = "namebr";
			String comment = "comment";
			String eainf = "hallo";
			String eaname = "name";

			XYDataset dataset = createDataset();
			JFreeChart chart = createChart(dataset, eaname);
			
			//spalte 1 ist der aktuelle chart
			panelx.add(new ChartPanel(chart));
			frame.getContentPane().setLayout(new BorderLayout());

			//spalte 2 ist der Backtest falls vorhanden
			panelx.add(new ChartPanel(chart));
			frame.getContentPane().setLayout(new BorderLayout());
			
			//spalte 3 ist die config
			EaConfigDis cp = new EaConfigDis(i, "XXX", "path");
			
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

	private static XYDataset createDataset() {
		XYSeries series1 = new XYSeries("summ");
		int gd5period = 5;
		int gd9period = 9;

		int anz = 15;
		for (int i = 0; i < anz; i++) {
			series1.add((i + 1), 2);
		}
		XYSeries series2 = new XYSeries("GD" + gd5period);
		for (int i = 0; i < anz; i++)
			series2.add(i + 1, 8);
		XYSeries series3 = new XYSeries("GD" + gd9period);
		for (int i = 0; i < anz; i++)
			series3.add(i + 1, 12);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		// dataset.addSeries(series2);
		// dataset.addSeries(series3);
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
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart(createDataset(), "titel");
		return new ChartPanel(chart);
	}

}
