package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
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
		public MyDemoPanel(ArrayList<SqBaseElem> baselist,String  dbn)
		{
			super(new BorderLayout());
			this.data1 = createSampleData(baselist);
			add(createContent(dbn));
		}
		
		/**
		 * Creates and returns a sample dataset. The data was randomly generated.
		 *
		 * @return a sample dataset.
		 */
		private XYDataset createSampleData(ArrayList<SqBaseElem> baselist)
		{
			int anz=baselist.size();
			double sumprof=0;
			XYSeries seriesSum = new XYSeries("Sum Profit");
			XYSeries seriesProfit = new XYSeries("Profit");
			
			XYSeriesCollection result = new XYSeriesCollection(seriesSum);
			for(int i=0; i<anz; i++)
			{
				
				SqBaseElem be=baselist.get(i);
				double neprof=be.getNetprofit();
				int index=be.getIndexVal();
				seriesSum.add(index,neprof+sumprof);
				seriesProfit.add(index,neprof);
				sumprof=sumprof+neprof;
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
			NumberAxis xAxis = new NumberAxis("X");
			xAxis.setAutoRangeIncludesZero(false);
			NumberAxis yAxis = new NumberAxis("Y");
			yAxis.setAutoRangeIncludesZero(false);
			
			XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
			XYPlot plot = new XYPlot(this.data1, xAxis, yAxis, renderer1);
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
			
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
	public SumChart(String title, ArrayList<SqBaseElem> baselist,String databankname)
	{
		super(title);
		JPanel content = createDemoPanel(baselist,databankname);
		getContentPane().add(content);
	}
	
	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel(ArrayList<SqBaseElem> baselist,String databankname)
	{
		return new MyDemoPanel(baselist,databankname);
	}
	
	/**
	 * The starting point for the regression demo.
	 *
	 * @param args
	 *            ignored.
	 */

	
	public static void ShowChart(ArrayList<SqBaseElem> baselist,String databankname)
	{
		SumChart appFrame = new SumChart("Profit Overview", baselist,databankname);
		
		appFrame.setPreferredSize(new java.awt.Dimension(3000, 1500));
		appFrame.pack();
		RefineryUtilities.centerFrameOnScreen(appFrame);
		appFrame.setVisible(true);
	}
}
