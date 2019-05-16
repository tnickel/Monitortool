package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.date.MonthConstants;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import data.GewinnjahrStategienSum;

/**
 * A demonstration application showing how to create a price-volume chart.
 */
public class GesammtSumChart2
{
	
	private static GewinnjahrStategienSum gewsum_glob = null;

	public GesammtSumChart2(String title, GewinnjahrStategienSum gewsum)
	{
		JFrame frame = new JFrame("indicator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1600, 500);
		
		
		gewsum_glob = gewsum;
		JFreeChart chart = createChart(title);
		ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
		panel.setPreferredSize(new java.awt.Dimension(500, 270));
	
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel);

		frame.setVisible(true);
		gewsum.showResult();
	}

	/**
	 * Creates a chart.
	 * 
	 * @return a chart.
	 */
	private static JFreeChart createChart(String title)
	{
		XYDataset priceData = createSumDataset();

		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date",
				"Equity-Sum", priceData, true, true, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
		rangeAxis1.setLowerMargin(0.40); // to leave room for volume bars
		DecimalFormat format = new DecimalFormat("00.00");
		rangeAxis1.setNumberFormatOverride(format);

		XYItemRenderer renderer1 = plot.getRenderer();
		renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));
		renderer1.setSeriesPaint(0, Color.red);
		
		NumberAxis rangeAxis2 = new NumberAxis("Mon Prof");
		rangeAxis2.setUpperMargin(1.00); // to leave room for price line
		plot.setRangeAxis(1, rangeAxis2);
		plot.setDataset(1, createVolumeDataset());
		plot.setRangeAxis(1, rangeAxis2);
		plot.mapDatasetToRangeAxis(1, 1);
		XYBarRenderer renderer2 = new XYBarRenderer(0.20);
		renderer2.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat(
						"0,000.00")));
		plot.setRenderer(1, renderer2);
		ChartUtilities.applyCurrentTheme(chart);
		renderer2.setBarPainter(new StandardXYBarPainter());
		renderer2.setShadowVisible(false);
		renderer2.setSeriesPaint(0, Color.blue);
		return chart;
	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return A sample dataset.
	 */
	private static XYDataset createSumDataset()
	{
		// create dataset 1... Hier wird die Summe dargestellt
		TimeSeries series1 = new TimeSeries("Equity-Sum", Day.class);

		int firstjahrindex = gewsum_glob.getFirstJahresIndex();
		int lastjahrindex = gewsum_glob.calcLastJahresIndex();
		float sum = 0;

		for (int j = firstjahrindex; j <= lastjahrindex; j++)
			for (int i = 1; i <= 12; i++)
			{
				Day d = new Day(20, i, 2000 + j);
				float monprof = gewsum_glob.getMonatsgewinn(j, i - 1);
				sum = sum + monprof;
				if(monprof!=0)
				  series1.add(d, sum);
			}
		return new TimeSeriesCollection(series1);
	}
	/**
	 * Creates a sample dataset.
	 * 
	 * @return A sample dataset.
	 */
	private static IntervalXYDataset createVolumeDataset()
	{
		// create dataset 2...Hier werden die monatlichen balken berechnet
		TimeSeries series1 = new TimeSeries("MonProf", Day.class);

		int firstjahrindex = gewsum_glob.getFirstJahresIndex();
		int lastjahrindex = gewsum_glob.calcLastJahresIndex();
		float sum = 0;

		for (int j = firstjahrindex; j <= lastjahrindex; j++)
			for (int i = 1; i <= 12; i++)
			{
				for (int x = 1; x < 27; x++)
				{
					Day d = new Day(x, i, 2000 + j);
					float monprof = gewsum_glob.getMonatsgewinn(j, i - 1);
					if(monprof!=0)
					series1.add(d, monprof);
				}
			}
		return new TimeSeriesCollection(series1);
	}

	
}
