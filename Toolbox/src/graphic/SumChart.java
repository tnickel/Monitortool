package graphic;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import hiflsklasse.DemoPanel;
import sq4xWorkflow.SqBaseElem;

/**
 * A demo showing a line chart drawn using spline curves.
 */
public class SumChart extends ApplicationFrame
{
	
	static class MyDemoPanel extends DemoPanel
	{
		
		/** Dataset 1. */
		private XYDataset data1;
		
		/**
		 * Creates a new instance.
		 */
		public MyDemoPanel(ArrayList<SqBaseElem> baselist, String dbn,int normf)
		{
			super(new BorderLayout());
			this.data1 = createSampleData(baselist,normf);
			add(createContent(dbn));
		}
		
		/**
		 * Creates and returns a sample dataset. The data was randomly generated.
		 *
		 * @return a sample dataset.
		 */
		private XYDataset createSampleData(ArrayList<SqBaseElem> baselist,int normf)
		{
			int anz = baselist.size();
			double sumprof = 0;
			XYSeries seriesSum = new XYSeries("Sum Profit");
			XYSeries seriesProfit = new XYSeries("Profit");
			
			XYSeriesCollection result = new XYSeriesCollection(seriesSum);
			for (int i = anz - 1; i >= 0; i--)
			{
				
				SqBaseElem be = baselist.get(i);
				double neprof = be.getNetprofit(normf);
				int index = be.getIndexVal();
			
				seriesSum.add(index, neprof + sumprof);
				seriesProfit.add(index, neprof);
				sumprof = sumprof + neprof;
			}
			result.addSeries(seriesProfit);
			return result;
			
		}
		
		/**
		 * Creates a tabbed pane for displaying sample charts.
		 *
		 * @return the tabbed pane.
		 */
		private JTabbedPane createContent(String dbn)
		{
			JTabbedPane tabs = new JTabbedPane();
			tabs.add("Splines:", createChartPanel1(dbn));
			tabs.add("Lines:", createChartPanel2(dbn));
			return tabs;
		}
		
		/**
		 * Creates a chart based on the first dataset, with a fitted linear regression
		 * line.
		 *
		 * @return the chart panel.
		 */
		private ChartPanel createChartPanel1(String databankname)
		{
			
			// create plot...
			NumberAxis xAxis = new NumberAxis("Days in the Past");
			xAxis.setAutoRangeIncludesZero(false);
			NumberAxis yAxis = new NumberAxis("Profit in Euro");
			yAxis.setAutoRangeIncludesZero(false);
			
			XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
			XYPlot plot = new XYPlot(this.data1, xAxis, yAxis, renderer1);
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
			
			// horizontal line to mark 0
			ValueMarker mark = new ValueMarker(0, Color.YELLOW, new BasicStroke(3), Color.YELLOW, null, 0.5f);
			plot.addRangeMarker(mark);
			
			// create and return the chart panel...
			JFreeChart chart = new JFreeChart(databankname, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			addChart(chart);
			ChartUtilities.applyCurrentTheme(chart);
			ChartPanel chartPanel = new ChartPanel(chart, false);
			return chartPanel;
			
		}
		
		/**
		 * Creates a chart based on the second dataset, with a fitted power regression
		 * line.
		 *
		 * @return the chart panel.
		 */
		private ChartPanel createChartPanel2(String databankname)
		{
			
			// create subplot 1...
			NumberAxis xAxis = new NumberAxis("Days in the Past");
			xAxis.setAutoRangeIncludesZero(false);
			NumberAxis yAxis = new NumberAxis("Profit in Euro");
			yAxis.setAutoRangeIncludesZero(true);
			
				
			XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
			XYPlot plot = new XYPlot(this.data1, xAxis, yAxis, renderer1);
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
			 plot.setRangeAxis(yAxis);
			 
			// create and return the chart panel...
			JFreeChart chart = new JFreeChart(databankname, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

			
	    
	       
		
			
			addChart(chart);
			ChartUtilities.applyCurrentTheme(chart);
			ChartPanel chartPanel = new ChartPanel(chart, false);
			return chartPanel;
			
		}
		
	}
	
	/**
	 * Creates a new instance of the demo application.
	 *
	 * @param title
	 *            the frame title.
	 */
	public SumChart(String title, ArrayList<SqBaseElem> baselist, String databankname,int normf)
	{
		super(title);
		JPanel content = createDemoPanel(baselist, databankname,normf);
		getContentPane().add(content);
	}
	
	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel(ArrayList<SqBaseElem> baselist, String databankname,int normf)
	{
		return new MyDemoPanel(baselist, databankname, normf);
	}
	
	/**
	 * The starting point for the regression demo.
	 *
	 * @param args
	 *            ignored.
	 */
	
	public static void ShowChart(ArrayList<SqBaseElem> baselist, String databankname,int normf)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		
		SumChart appFrame = new SumChart("Profit Overview", baselist, databankname,normf);
		
		appFrame.setPreferredSize(
				new java.awt.Dimension((int) (screenSize.getWidth() * 0.5), (int) (screenSize.getHeight() * 0.5)));
		appFrame.pack();
		RefineryUtilities.centerFrameOnScreen(appFrame);
		appFrame.setVisible(true);
	}
	
	public static void ShowChart2(ArrayList<SqBaseElem> baselist, String databankname)
	{
	        XYSeries series = new XYSeries("First");
	        series.add(1.0, 1.0);
	        series.add(2.0, 10.0);
	        series.add(3.0, 100.0);
	        series.add(4.0, 1000.0);
	        series.add(5.0, 10000.0);
	        series.add(6.0, 100000.0);
	        series.add(7.0, 1000000.0);
	        series.add(8.0, 10000000.0);

	        XYSeriesCollection dataset = new XYSeriesCollection();
	        dataset.addSeries(series);

	        JFreeChart chart = ChartFactory.createXYLineChart(
	            "XY Chart",
	            "x-axis",
	            "y-axis",
	            dataset,
	            PlotOrientation.VERTICAL,
	            false,
	            false,
	            false
	            );
	    
	        LogarithmicAxis yAxis = new LogarithmicAxis("Y");
	    
	        XYPlot plot = chart.getXYPlot();
	        plot.setRangeAxis(yAxis);
	    
	        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
	        renderer.setSeriesShapesVisible(0, true);
	    
	        ChartFrame frame = new ChartFrame("My Chart", chart);
	        frame.pack();
	        frame.setVisible(true);
	   
	}
	
}
