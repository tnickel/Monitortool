package swingtest;

import hilfsklasse.KeyReader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import mainPackage.GC;

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

public class MainW extends JPanel
{
	protected JTextArea textArea;

	public MainW()
	{
		ImageIcon icon = new ImageIcon("java-swing-tutorial.JPG");
		JTabbedPane jtbExample = new JTabbedPane();
		JPanel jplInnerPanel1 = createTextFeldUser();
		jtbExample.addTab("User.db", icon, jplInnerPanel1, "Tab 1");
		jtbExample.setSelectedIndex(0);
		JPanel jplInnerPanel2 = createTextFeldThreads();
		jtbExample.addTab("Threads.db", icon, jplInnerPanel2);
		JPanel jplInnerPanel3 = createDemoPanel();
		jtbExample.addTab("Akt.db", icon, jplInnerPanel3, "Tab 3");
		// JPanel jplInnerPanel4 = JGraphAdapterDemo();
		// jtbExample.addTab("Observeuser", jplInnerPanel4);
		JPanel jplInnerPanel5 = createInnerPanel("Tab 5 Contains Text only");
		jtbExample.addTab("Consolewindow", jplInnerPanel5);
		JPanel jplInnerPanel6 = createTraceFeld();
		jtbExample.addTab("Logfile", jplInnerPanel6);
		JPanel jplInnerPanel7 = createTraceFeld();
		jtbExample.addTab("Neu", jplInnerPanel7);
		// Add the tabbed pane to this panel.
		setLayout(new GridLayout(1, 1));
		add(jtbExample);
	}

	protected JPanel createInnerPanel(String text)
	{
		JPanel jplPanel = new JPanel();
		JLabel jlbDisplay = new JLabel(text);
		jlbDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		jplPanel.setLayout(new GridLayout(1, 1));
		jplPanel.add(jlbDisplay);
		return jplPanel;
	}

	protected JPanel createTraceFeld()
	{
		String line;
		int i = 0;
		textArea = new JTextArea(5, 60);
		KeyReader kr = new KeyReader();
		kr.SetReader(GC.rootpath + "\\db\\trace.txt");
		while ((line = kr.GetLine()) != null)
		{
			i++;
			if (i > 100000)
				break;
			// System.out.println("add line<"+line+">");
			textArea.append(line);
			textArea.append("\n");
		}
		JScrollPane jsPane = new JScrollPane(textArea);
		// Lay out the content pane.
		JPanel jplContentPane = new JPanel();
		jplContentPane.setLayout(new BorderLayout());
		jplContentPane.setPreferredSize(new Dimension(200, 100));
		jplContentPane.add(jsPane, BorderLayout.WEST);
		return (jplContentPane);
	}

	protected JPanel createTextFeldUser()
	{
		String line;
		int i = 0;
		textArea = new JTextArea(5, 60);
		KeyReader kr = new KeyReader();
		kr.SetReader(GC.rootpath + "\\db\\userstore.db");
		while ((line = kr.GetLine()) != null)
		{
			i++;
			if (i > 100000)
				break;
			// System.out.println("add line<"+line+">");
			textArea.append(line);
			textArea.append("\n");
		}
		JScrollPane jsPane = new JScrollPane(textArea);
		// Lay out the content pane.
		JPanel jplContentPane = new JPanel();
		jplContentPane.setLayout(new BorderLayout());
		jplContentPane.setPreferredSize(new Dimension(200, 100));
		jplContentPane.add(jsPane, BorderLayout.WEST);
		return (jplContentPane);
	}

	protected JPanel createTextFeldThreads()
	{
		String line;
		int i = 0;
		textArea = new JTextArea(5, 60);
		KeyReader kr = new KeyReader();
		kr.SetReader(GC.rootpath + "\\db\\threads.db");
		while ((line = kr.GetLine()) != null)
		{
			i++;
			if (i > 100000)
				break;
			// System.out.println("add line<"+line+">");
			textArea.append(line);
			textArea.append("\n");
		}
		JScrollPane jsPane = new JScrollPane(textArea);
		// Lay out the content pane.
		JPanel jplContentPane = new JPanel();
		jplContentPane.setLayout(new BorderLayout());
		jplContentPane.setPreferredSize(new Dimension(200, 100));
		jplContentPane.add(jsPane, BorderLayout.WEST);
		return (jplContentPane);
	}
	private static XYDataset createPriceDataset()
	{

		// create dataset 1...
		TimeSeries series1 = new TimeSeries("Price", Day.class);

		series1.add(new Day(2, MonthConstants.JANUARY, 2002), 95.565);
		series1.add(new Day(3, MonthConstants.JANUARY, 2002), 95.640);
		series1.add(new Day(4, MonthConstants.JANUARY, 2002), 95.710);

		series1.add(new Day(7, MonthConstants.JANUARY, 2002), 95.930);
		series1.add(new Day(8, MonthConstants.JANUARY, 2002), 95.930);
		series1.add(new Day(9, MonthConstants.JANUARY, 2002), 95.960);
		series1.add(new Day(10, MonthConstants.JANUARY, 2002), 96.055);
		series1.add(new Day(11, MonthConstants.JANUARY, 2002), 96.335);

		series1.add(new Day(14, MonthConstants.JANUARY, 2002), 96.290);
		series1.add(new Day(15, MonthConstants.JANUARY, 2002), 96.275);
		series1.add(new Day(16, MonthConstants.JANUARY, 2002), 96.240);
		series1.add(new Day(17, MonthConstants.JANUARY, 2002), 96.080);
		series1.add(new Day(18, MonthConstants.JANUARY, 2002), 96.145);

		series1.add(new Day(22, MonthConstants.JANUARY, 2002), 96.120);
		series1.add(new Day(23, MonthConstants.JANUARY, 2002), 96.015);
		series1.add(new Day(24, MonthConstants.JANUARY, 2002), 95.890);
		series1.add(new Day(25, MonthConstants.JANUARY, 2002), 95.8650);

		series1.add(new Day(28, MonthConstants.JANUARY, 2002), 95.880);
		series1.add(new Day(29, MonthConstants.JANUARY, 2002), 96.050);
		series1.add(new Day(30, MonthConstants.JANUARY, 2002), 96.065);
		series1.add(new Day(31, MonthConstants.JANUARY, 2002), 95.910);
		series1.add(new Day(1, MonthConstants.FEBRUARY, 2002), 96.015);

		return new TimeSeriesCollection(series1);

	}

	private static IntervalXYDataset createVolumeDataset()
	{

		// create dataset 2...
		TimeSeries series1 = new TimeSeries("Volume", Day.class);

		series1.add(new Day(2, MonthConstants.JANUARY, 2002), 41020);
		series1.add(new Day(3, MonthConstants.JANUARY, 2002), 45586);
		series1.add(new Day(4, MonthConstants.JANUARY, 2002), 81672);

		series1.add(new Day(7, MonthConstants.JANUARY, 2002), 81975);
		series1.add(new Day(8, MonthConstants.JANUARY, 2002), 79692);
		series1.add(new Day(9, MonthConstants.JANUARY, 2002), 53187);
		series1.add(new Day(10, MonthConstants.JANUARY, 2002), 87929);
		series1.add(new Day(11, MonthConstants.JANUARY, 2002), 107047);

		series1.add(new Day(14, MonthConstants.JANUARY, 2002), 86276);
		series1.add(new Day(15, MonthConstants.JANUARY, 2002), 79005);
		series1.add(new Day(16, MonthConstants.JANUARY, 2002), 80632);
		series1.add(new Day(17, MonthConstants.JANUARY, 2002), 88797);
		series1.add(new Day(18, MonthConstants.JANUARY, 2002), 57179);

		series1.add(new Day(22, MonthConstants.JANUARY, 2002), 36611);
		series1.add(new Day(23, MonthConstants.JANUARY, 2002), 57063);
		series1.add(new Day(24, MonthConstants.JANUARY, 2002), 101938);
		series1.add(new Day(25, MonthConstants.JANUARY, 2002), 87177);

		series1.add(new Day(28, MonthConstants.JANUARY, 2002), 39831);
		series1.add(new Day(29, MonthConstants.JANUARY, 2002), 67654);
		series1.add(new Day(30, MonthConstants.JANUARY, 2002), 81162);
		series1.add(new Day(31, MonthConstants.JANUARY, 2002), 64923);
		series1.add(new Day(1, MonthConstants.FEBRUARY, 2002), 73481);

		series1.add(new Day(4, MonthConstants.FEBRUARY, 2002), 54723);
		series1.add(new Day(5, MonthConstants.FEBRUARY, 2002), 76708);
		series1.add(new Day(6, MonthConstants.FEBRUARY, 2002), 81281);
		series1.add(new Day(7, MonthConstants.FEBRUARY, 2002), 66553);
		series1.add(new Day(8, MonthConstants.FEBRUARY, 2002), 53592);

		series1.add(new Day(11, MonthConstants.FEBRUARY, 2002), 29410);
		series1.add(new Day(12, MonthConstants.FEBRUARY, 2002), 60345);
		series1.add(new Day(13, MonthConstants.FEBRUARY, 2002), 67339);
		series1.add(new Day(14, MonthConstants.FEBRUARY, 2002), 40057);
		series1.add(new Day(15, MonthConstants.FEBRUARY, 2002), 67865);

		series1.add(new Day(19, MonthConstants.FEBRUARY, 2002), 58628);
		series1.add(new Day(20, MonthConstants.FEBRUARY, 2002), 52109);
		series1.add(new Day(21, MonthConstants.FEBRUARY, 2002), 50195);
		series1.add(new Day(22, MonthConstants.FEBRUARY, 2002), 47806);

		series1.add(new Day(25, MonthConstants.FEBRUARY, 2002), 31711);
		series1.add(new Day(26, MonthConstants.FEBRUARY, 2002), 88328);
		series1.add(new Day(27, MonthConstants.FEBRUARY, 2002), 95805);
		series1.add(new Day(28, MonthConstants.FEBRUARY, 2002), 84035);
		series1.add(new Day(1, MonthConstants.MARCH, 2002), 113584);

		series1.add(new Day(4, MonthConstants.MARCH, 2002), 71872);
		series1.add(new Day(5, MonthConstants.MARCH, 2002), 83016);
		series1.add(new Day(6, MonthConstants.MARCH, 2002), 62273);
		series1.add(new Day(7, MonthConstants.MARCH, 2002), 138508);
		series1.add(new Day(8, MonthConstants.MARCH, 2002), 139428);

		series1.add(new Day(11, MonthConstants.MARCH, 2002), 80232);
		series1.add(new Day(12, MonthConstants.MARCH, 2002), 75693);
		series1.add(new Day(13, MonthConstants.MARCH, 2002), 104068);
		series1.add(new Day(14, MonthConstants.MARCH, 2002), 72171);
		series1.add(new Day(15, MonthConstants.MARCH, 2002), 117262);
		return new TimeSeriesCollection(series1);

	}

	private static JFreeChart createChart()
	{

		XYDataset priceData = createPriceDataset();
		String title = "Eurodollar Futures Contract (MAR03)";
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date",
				"Price", priceData, true, true, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
		rangeAxis1.setLowerMargin(0.40); // to leave room for volume bars
		DecimalFormat format = new DecimalFormat("00.00");
		rangeAxis1.setNumberFormatOverride(format);

		XYItemRenderer renderer1 = plot.getRenderer();
		renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

		NumberAxis rangeAxis2 = new NumberAxis("Volume");
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
		return chart;

	}


	
	public static JPanel createDemoPanel()
	{
		JFreeChart chart = createChart();
		return new ChartPanel(chart);
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("TabbedPane Source Demo");

		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		frame.getContentPane().add(new MainW(), BorderLayout.CENTER);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}