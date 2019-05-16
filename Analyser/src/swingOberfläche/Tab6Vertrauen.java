package swingOberfläche;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kurse.KursDbObj;
import kurse.KurseDB;
import objects.ThreadDbObj;

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

import stores.ThreadsDB;
import swingtest.DemoPanel;

public class Tab6Vertrauen
{
	static protected JTextArea textArea;

	public Tab6Vertrauen()
	{
	}

	private static JFreeChart createChart(int pos)
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

	private static XYDataset createPriceDataset()
	{
		// create dataset 1...
		TimeSeries series1 = new TimeSeries("Price");

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

		series1.add(new Day(4, MonthConstants.FEBRUARY, 2002), 96.140);
		series1.add(new Day(5, MonthConstants.FEBRUARY, 2002), 96.195);
		series1.add(new Day(6, MonthConstants.FEBRUARY, 2002), 96.245);
		series1.add(new Day(7, MonthConstants.FEBRUARY, 2002), 96.220);
		series1.add(new Day(8, MonthConstants.FEBRUARY, 2002), 96.280);

		series1.add(new Day(11, MonthConstants.FEBRUARY, 2002), 96.265);
		series1.add(new Day(12, MonthConstants.FEBRUARY, 2002), 96.160);
		series1.add(new Day(13, MonthConstants.FEBRUARY, 2002), 96.120);
		series1.add(new Day(14, MonthConstants.FEBRUARY, 2002), 96.125);
		series1.add(new Day(15, MonthConstants.FEBRUARY, 2002), 96.265);

		series1.add(new Day(19, MonthConstants.FEBRUARY, 2002), 96.290);
		series1.add(new Day(20, MonthConstants.FEBRUARY, 2002), 96.275);
		series1.add(new Day(21, MonthConstants.FEBRUARY, 2002), 96.280);
		series1.add(new Day(22, MonthConstants.FEBRUARY, 2002), 96.305);

		series1.add(new Day(25, MonthConstants.FEBRUARY, 2002), 96.265);
		series1.add(new Day(26, MonthConstants.FEBRUARY, 2002), 96.185);
		series1.add(new Day(27, MonthConstants.FEBRUARY, 2002), 96.305);
		series1.add(new Day(28, MonthConstants.FEBRUARY, 2002), 96.215);
		series1.add(new Day(1, MonthConstants.MARCH, 2002), 96.015);

		series1.add(new Day(4, MonthConstants.MARCH, 2002), 95.970);
		series1.add(new Day(5, MonthConstants.MARCH, 2002), 95.935);
		series1.add(new Day(6, MonthConstants.MARCH, 2002), 95.935);
		series1.add(new Day(7, MonthConstants.MARCH, 2002), 95.705);
		series1.add(new Day(8, MonthConstants.MARCH, 2002), 95.4850);

		series1.add(new Day(11, MonthConstants.MARCH, 2002), 95.505);
		series1.add(new Day(12, MonthConstants.MARCH, 2002), 95.540);
		series1.add(new Day(13, MonthConstants.MARCH, 2002), 95.675);
		series1.add(new Day(14, MonthConstants.MARCH, 2002), 95.510);
		series1.add(new Day(15, MonthConstants.MARCH, 2002), 95.500);

		series1.add(new Day(18, MonthConstants.MARCH, 2002), 95.500);
		series1.add(new Day(19, MonthConstants.MARCH, 2002), 95.535);
		series1.add(new Day(20, MonthConstants.MARCH, 2002), 95.420);
		series1.add(new Day(21, MonthConstants.MARCH, 2002), 95.400);
		series1.add(new Day(22, MonthConstants.MARCH, 2002), 95.375);

		series1.add(new Day(25, MonthConstants.MARCH, 2002), 95.350);
		series1.add(new Day(26, MonthConstants.MARCH, 2002), 95.505);
		series1.add(new Day(27, MonthConstants.MARCH, 2002), 95.550);
		series1.add(new Day(28, MonthConstants.MARCH, 2002), 95.485);

		series1.add(new Day(1, MonthConstants.APRIL, 2002), 95.485);
		series1.add(new Day(2, MonthConstants.APRIL, 2002), 95.630);
		series1.add(new Day(3, MonthConstants.APRIL, 2002), 95.735);
		series1.add(new Day(4, MonthConstants.APRIL, 2002), 95.695);
		series1.add(new Day(5, MonthConstants.APRIL, 2002), 95.810);

		series1.add(new Day(8, MonthConstants.APRIL, 2002), 95.810);
		series1.add(new Day(9, MonthConstants.APRIL, 2002), 95.865);
		series1.add(new Day(10, MonthConstants.APRIL, 2002), 95.885);
		series1.add(new Day(11, MonthConstants.APRIL, 2002), 95.900);
		series1.add(new Day(12, MonthConstants.APRIL, 2002), 95.980);

		series1.add(new Day(15, MonthConstants.APRIL, 2002), 96.035);
		series1.add(new Day(16, MonthConstants.APRIL, 2002), 96.000);
		series1.add(new Day(17, MonthConstants.APRIL, 2002), 96.035);
		series1.add(new Day(18, MonthConstants.APRIL, 2002), 96.085);
		series1.add(new Day(19, MonthConstants.APRIL, 2002), 96.0750);

		series1.add(new Day(22, MonthConstants.APRIL, 2002), 96.105);
		series1.add(new Day(23, MonthConstants.APRIL, 2002), 96.075);
		series1.add(new Day(24, MonthConstants.APRIL, 2002), 96.210);
		series1.add(new Day(25, MonthConstants.APRIL, 2002), 96.255);
		series1.add(new Day(26, MonthConstants.APRIL, 2002), 96.310);

		series1.add(new Day(29, MonthConstants.APRIL, 2002), 96.310);
		series1.add(new Day(30, MonthConstants.APRIL, 2002), 96.325);
		series1.add(new Day(1, MonthConstants.MAY, 2002), 96.345);
		series1.add(new Day(2, MonthConstants.MAY, 2002), 96.285);
		series1.add(new Day(3, MonthConstants.MAY, 2002), 96.385);

		series1.add(new Day(6, MonthConstants.MAY, 2002), 96.380);
		series1.add(new Day(7, MonthConstants.MAY, 2002), 96.485);
		series1.add(new Day(8, MonthConstants.MAY, 2002), 96.230);
		series1.add(new Day(9, MonthConstants.MAY, 2002), 96.310);
		series1.add(new Day(10, MonthConstants.MAY, 2002), 96.445);

		series1.add(new Day(13, MonthConstants.MAY, 2002), 96.355);
		series1.add(new Day(14, MonthConstants.MAY, 2002), 96.180);
		series1.add(new Day(15, MonthConstants.MAY, 2002), 96.240);
		series1.add(new Day(16, MonthConstants.MAY, 2002), 96.325);
		series1.add(new Day(17, MonthConstants.MAY, 2002), 96.200);

		series1.add(new Day(20, MonthConstants.MAY, 2002), 96.305);
		series1.add(new Day(21, MonthConstants.MAY, 2002), 96.385);
		series1.add(new Day(22, MonthConstants.MAY, 2002), 96.445);
		series1.add(new Day(23, MonthConstants.MAY, 2002), 96.385);
		series1.add(new Day(24, MonthConstants.MAY, 2002), 96.390);

		series1.add(new Day(28, MonthConstants.MAY, 2002), 96.390);
		series1.add(new Day(29, MonthConstants.MAY, 2002), 96.475);
		series1.add(new Day(30, MonthConstants.MAY, 2002), 96.555);
		series1.add(new Day(31, MonthConstants.MAY, 2002), 96.500);

		series1.add(new Day(3, MonthConstants.JUNE, 2002), 96.540);
		series1.add(new Day(4, MonthConstants.JUNE, 2002), 96.605);
		series1.add(new Day(5, MonthConstants.JUNE, 2002), 96.580);
		series1.add(new Day(6, MonthConstants.JUNE, 2002), 96.610);
		series1.add(new Day(7, MonthConstants.JUNE, 2002), 96.600);

		series1.add(new Day(10, MonthConstants.JUNE, 2002), 96.615);
		series1.add(new Day(11, MonthConstants.JUNE, 2002), 96.705);
		series1.add(new Day(12, MonthConstants.JUNE, 2002), 96.750);
		series1.add(new Day(13, MonthConstants.JUNE, 2002), 96.830);
		series1.add(new Day(14, MonthConstants.JUNE, 2002), 96.965);

		series1.add(new Day(17, MonthConstants.JUNE, 2002), 96.945);
		series1.add(new Day(18, MonthConstants.JUNE, 2002), 96.990);
		series1.add(new Day(19, MonthConstants.JUNE, 2002), 97.165);
		series1.add(new Day(20, MonthConstants.JUNE, 2002), 97.030);
		series1.add(new Day(21, MonthConstants.JUNE, 2002), 97.145);

		series1.add(new Day(24, MonthConstants.JUNE, 2002), 97.120);
		series1.add(new Day(25, MonthConstants.JUNE, 2002), 97.175);
		series1.add(new Day(26, MonthConstants.JUNE, 2002), 97.365);
		series1.add(new Day(27, MonthConstants.JUNE, 2002), 97.245);
		series1.add(new Day(28, MonthConstants.JUNE, 2002), 97.245);

		series1.add(new Day(1, MonthConstants.JULY, 2002), 97.290);
		series1.add(new Day(2, MonthConstants.JULY, 2002), 97.380);
		series1.add(new Day(3, MonthConstants.JULY, 2002), 97.380);

		series1.add(new Day(5, MonthConstants.JULY, 2002), 97.220);

		series1.add(new Day(8, MonthConstants.JULY, 2002), 97.325);
		series1.add(new Day(9, MonthConstants.JULY, 2002), 97.455);
		series1.add(new Day(10, MonthConstants.JULY, 2002), 97.580);
		series1.add(new Day(11, MonthConstants.JULY, 2002), 97.605);
		series1.add(new Day(12, MonthConstants.JULY, 2002), 97.690);

		series1.add(new Day(15, MonthConstants.JULY, 2002), 97.730);
		series1.add(new Day(16, MonthConstants.JULY, 2002), 97.580);
		series1.add(new Day(17, MonthConstants.JULY, 2002), 97.640);
		series1.add(new Day(18, MonthConstants.JULY, 2002), 97.680);
		series1.add(new Day(19, MonthConstants.JULY, 2002), 97.715);

		series1.add(new Day(22, MonthConstants.JULY, 2002), 97.815);
		series1.add(new Day(23, MonthConstants.JULY, 2002), 97.875);
		series1.add(new Day(24, MonthConstants.JULY, 2002), 97.835);
		series1.add(new Day(25, MonthConstants.JULY, 2002), 97.925);
		series1.add(new Day(26, MonthConstants.JULY, 2002), 97.960);

		series1.add(new Day(29, MonthConstants.JULY, 2002), 97.745);
		series1.add(new Day(30, MonthConstants.JULY, 2002), 97.710);
		series1.add(new Day(31, MonthConstants.JULY, 2002), 97.930);
		series1.add(new Day(1, MonthConstants.AUGUST, 2002), 98.000);
		series1.add(new Day(2, MonthConstants.AUGUST, 2002), 98.170);

		series1.add(new Day(5, MonthConstants.AUGUST, 2002), 98.225);
		series1.add(new Day(6, MonthConstants.AUGUST, 2002), 98.115);
		series1.add(new Day(7, MonthConstants.AUGUST, 2002), 98.265);
		series1.add(new Day(8, MonthConstants.AUGUST, 2002), 98.180);
		series1.add(new Day(9, MonthConstants.AUGUST, 2002), 98.185);

		series1.add(new Day(12, MonthConstants.AUGUST, 2002), 98.150);
		series1.add(new Day(13, MonthConstants.AUGUST, 2002), 98.290);
		series1.add(new Day(14, MonthConstants.AUGUST, 2002), 98.155);
		series1.add(new Day(15, MonthConstants.AUGUST, 2002), 98.075);
		series1.add(new Day(16, MonthConstants.AUGUST, 2002), 98.000);

		series1.add(new Day(19, MonthConstants.AUGUST, 2002), 98.040);
		series1.add(new Day(20, MonthConstants.AUGUST, 2002), 98.135);
		series1.add(new Day(21, MonthConstants.AUGUST, 2002), 98.110);
		series1.add(new Day(22, MonthConstants.AUGUST, 2002), 98.005);
		series1.add(new Day(23, MonthConstants.AUGUST, 2002), 98.055);

		series1.add(new Day(26, MonthConstants.AUGUST, 2002), 98.065);
		series1.add(new Day(27, MonthConstants.AUGUST, 2002), 97.980);
		series1.add(new Day(28, MonthConstants.AUGUST, 2002), 98.035);
		series1.add(new Day(29, MonthConstants.AUGUST, 2002), 98.095);
		series1.add(new Day(30, MonthConstants.AUGUST, 2002), 98.060);

		series1.add(new Day(3, MonthConstants.SEPTEMBER, 2002), 98.250);
		series1.add(new Day(4, MonthConstants.SEPTEMBER, 2002), 98.245);
		series1.add(new Day(5, MonthConstants.SEPTEMBER, 2002), 98.315);
		series1.add(new Day(6, MonthConstants.SEPTEMBER, 2002), 98.170);

		series1.add(new Day(9, MonthConstants.SEPTEMBER, 2002), 98.080);
		series1.add(new Day(10, MonthConstants.SEPTEMBER, 2002), 98.090);
		series1.add(new Day(11, MonthConstants.SEPTEMBER, 2002), 98.030);
		series1.add(new Day(12, MonthConstants.SEPTEMBER, 2002), 98.105);
		series1.add(new Day(13, MonthConstants.SEPTEMBER, 2002), 98.135);

		series1.add(new Day(16, MonthConstants.SEPTEMBER, 2002), 98.115);
		series1.add(new Day(17, MonthConstants.SEPTEMBER, 2002), 98.125);
		series1.add(new Day(18, MonthConstants.SEPTEMBER, 2002), 98.130);
		series1.add(new Day(19, MonthConstants.SEPTEMBER, 2002), 98.255);
		series1.add(new Day(20, MonthConstants.SEPTEMBER, 2002), 98.255);

		series1.add(new Day(23, MonthConstants.SEPTEMBER, 2002), 98.280);
		series1.add(new Day(24, MonthConstants.SEPTEMBER, 2002), 98.310);
		series1.add(new Day(25, MonthConstants.SEPTEMBER, 2002), 98.250);
		series1.add(new Day(26, MonthConstants.SEPTEMBER, 2002), 98.300);
		series1.add(new Day(27, MonthConstants.SEPTEMBER, 2002), 98.410);

		series1.add(new Day(30, MonthConstants.SEPTEMBER, 2002), 98.495);
		series1.add(new Day(1, MonthConstants.OCTOBER, 2002), 98.440);
		series1.add(new Day(2, MonthConstants.OCTOBER, 2002), 98.440);
		series1.add(new Day(3, MonthConstants.OCTOBER, 2002), 98.440);
		series1.add(new Day(4, MonthConstants.OCTOBER, 2002), 98.380);

		series1.add(new Day(7, MonthConstants.OCTOBER, 2002), 98.385);
		series1.add(new Day(8, MonthConstants.OCTOBER, 2002), 98.340);
		series1.add(new Day(9, MonthConstants.OCTOBER, 2002), 98.420);
		series1.add(new Day(10, MonthConstants.OCTOBER, 2002), 98.375);
		series1.add(new Day(11, MonthConstants.OCTOBER, 2002), 98.275);

		series1.add(new Day(14, MonthConstants.OCTOBER, 2002), 98.275);
		series1.add(new Day(15, MonthConstants.OCTOBER, 2002), 98.135);
		series1.add(new Day(16, MonthConstants.OCTOBER, 2002), 98.165);
		series1.add(new Day(17, MonthConstants.OCTOBER, 2002), 98.170);
		series1.add(new Day(18, MonthConstants.OCTOBER, 2002), 98.165);

		series1.add(new Day(21, MonthConstants.OCTOBER, 2002), 98.105);
		series1.add(new Day(22, MonthConstants.OCTOBER, 2002), 98.125);
		series1.add(new Day(23, MonthConstants.OCTOBER, 2002), 98.185);
		series1.add(new Day(24, MonthConstants.OCTOBER, 2002), 98.245);
		series1.add(new Day(25, MonthConstants.OCTOBER, 2002), 98.320);

		series1.add(new Day(28, MonthConstants.OCTOBER, 2002), 98.420);
		series1.add(new Day(29, MonthConstants.OCTOBER, 2002), 98.540);
		series1.add(new Day(30, MonthConstants.OCTOBER, 2002), 98.545);
		series1.add(new Day(31, MonthConstants.OCTOBER, 2002), 98.560);

		return new TimeSeriesCollection(series1);
	}

	private static IntervalXYDataset createVolumeDataset()
	{
		// create dataset 2...
		TimeSeries series1 = new TimeSeries("Volume");

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

		series1.add(new Day(18, MonthConstants.MARCH, 2002), 66048);
		series1.add(new Day(19, MonthConstants.MARCH, 2002), 87079);
		series1.add(new Day(20, MonthConstants.MARCH, 2002), 116084);
		series1.add(new Day(21, MonthConstants.MARCH, 2002), 113206);
		series1.add(new Day(22, MonthConstants.MARCH, 2002), 68326);

		series1.add(new Day(25, MonthConstants.MARCH, 2002), 34340);
		series1.add(new Day(26, MonthConstants.MARCH, 2002), 104413);
		series1.add(new Day(27, MonthConstants.MARCH, 2002), 57277);
		series1.add(new Day(28, MonthConstants.MARCH, 2002), 69936);

		series1.add(new Day(1, MonthConstants.APRIL, 2002), 57282);
		series1.add(new Day(2, MonthConstants.APRIL, 2002), 74686);
		series1.add(new Day(3, MonthConstants.APRIL, 2002), 108601);
		series1.add(new Day(4, MonthConstants.APRIL, 2002), 123381);
		series1.add(new Day(5, MonthConstants.APRIL, 2002), 106691);

		series1.add(new Day(13, MonthConstants.MAY, 2002), 78208);
		series1.add(new Day(14, MonthConstants.MAY, 2002), 212048);
		series1.add(new Day(15, MonthConstants.MAY, 2002), 145643);
		series1.add(new Day(16, MonthConstants.MAY, 2002), 121520);
		series1.add(new Day(17, MonthConstants.MAY, 2002), 147820);

		series1.add(new Day(20, MonthConstants.MAY, 2002), 75969);
		series1.add(new Day(21, MonthConstants.MAY, 2002), 118970);
		series1.add(new Day(22, MonthConstants.MAY, 2002), 131013);
		series1.add(new Day(23, MonthConstants.MAY, 2002), 141100);
		series1.add(new Day(24, MonthConstants.MAY, 2002), 63606);

		series1.add(new Day(28, MonthConstants.MAY, 2002), 78687);
		series1.add(new Day(29, MonthConstants.MAY, 2002), 86743);
		series1.add(new Day(30, MonthConstants.MAY, 2002), 164376);
		series1.add(new Day(31, MonthConstants.MAY, 2002), 150108);

		series1.add(new Day(3, MonthConstants.JUNE, 2002), 132363);
		series1.add(new Day(4, MonthConstants.JUNE, 2002), 144902);
		series1.add(new Day(5, MonthConstants.JUNE, 2002), 123834);
		series1.add(new Day(6, MonthConstants.JUNE, 2002), 125004);
		series1.add(new Day(7, MonthConstants.JUNE, 2002), 165049);

		series1.add(new Day(24, MonthConstants.JUNE, 2002), 199052);
		series1.add(new Day(25, MonthConstants.JUNE, 2002), 191804);
		series1.add(new Day(26, MonthConstants.JUNE, 2002), 384936);
		series1.add(new Day(27, MonthConstants.JUNE, 2002), 313065);
		series1.add(new Day(28, MonthConstants.JUNE, 2002), 169963);

		series1.add(new Day(1, MonthConstants.JULY, 2002), 109906);
		series1.add(new Day(2, MonthConstants.JULY, 2002), 140644);
		series1.add(new Day(3, MonthConstants.JULY, 2002), 150898);

		series1.add(new Day(5, MonthConstants.JULY, 2002), 181355);

		series1.add(new Day(8, MonthConstants.JULY, 2002), 155042);
		series1.add(new Day(9, MonthConstants.JULY, 2002), 204305);
		series1.add(new Day(10, MonthConstants.JULY, 2002), 300113);
		series1.add(new Day(11, MonthConstants.JULY, 2002), 338948);
		series1.add(new Day(12, MonthConstants.JULY, 2002), 281325);

		series1.add(new Day(15, MonthConstants.JULY, 2002), 256101);
		series1.add(new Day(16, MonthConstants.JULY, 2002), 348164);
		series1.add(new Day(17, MonthConstants.JULY, 2002), 242995);
		series1.add(new Day(18, MonthConstants.JULY, 2002), 200744);
		series1.add(new Day(19, MonthConstants.JULY, 2002), 181071);

		series1.add(new Day(19, MonthConstants.AUGUST, 2002), 96437);
		series1.add(new Day(20, MonthConstants.AUGUST, 2002), 151735);
		series1.add(new Day(21, MonthConstants.AUGUST, 2002), 167390);
		series1.add(new Day(22, MonthConstants.AUGUST, 2002), 127184);
		series1.add(new Day(23, MonthConstants.AUGUST, 2002), 80205);

		series1.add(new Day(26, MonthConstants.AUGUST, 2002), 79893);
		series1.add(new Day(27, MonthConstants.AUGUST, 2002), 201723);
		series1.add(new Day(28, MonthConstants.AUGUST, 2002), 114001);
		series1.add(new Day(29, MonthConstants.AUGUST, 2002), 188389);
		series1.add(new Day(30, MonthConstants.AUGUST, 2002), 162801);

		series1.add(new Day(30, MonthConstants.SEPTEMBER, 2002), 199615);
		series1.add(new Day(1, MonthConstants.OCTOBER, 2002), 170771);
		series1.add(new Day(2, MonthConstants.OCTOBER, 2002), 138498);
		series1.add(new Day(3, MonthConstants.OCTOBER, 2002), 154774);
		series1.add(new Day(4, MonthConstants.OCTOBER, 2002), 287154);

		return new TimeSeriesCollection(series1);
	}

	public static JList listeSymbole_dep(ThreadsDB tdb)
	{
		String[] listentry = new String[80000];
		KurseDB kdb = new KurseDB(tdb);
		int anz = kdb.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			KursDbObj kdbo = (KursDbObj) kdb.GetObjectIDX(i);
			listentry[i] = new String("pos<" + i + "> symb<" + kdbo.getSymbol()
					+ ">");
		}
		JList jlist = new JList(listentry);
		MouseListener ma1= new MA6Vertrauen();
		
		jlist.addMouseListener(ma1);
		jlist.setVisibleRowCount(20);

		return jlist;
	}

	public static JList listeThreads()
	{
		String[] listentry = new String[80000];
		ThreadsDB tdb = new ThreadsDB();
		int anz = tdb.GetanzObj();
		int listcounter=0;

		tdb.refreshPrognoseanzahl();

		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			
			if(tdbo.getPrognoseanzahl()>20)
			{
			listentry[listcounter] = new String("pos<" + i + "> tid<"
					+ tdbo.getThreadid() + "> name<" + tdbo.getThreadname()
					+ "> Prognanz<" + tdbo.getPrognoseanzahl() + ">");
			listcounter++;
			}
		}
		JList jlist = new JList(listentry);

		jlist.addMouseListener(new MA6Vertrauen());
		jlist.setVisibleRowCount(20);

		return jlist;
	}

	public static JPanel container()
	{
		JPanel p = new JPanel();

		p.setLayout(new FlowLayout());
		p.add(new JButton("Einfacher Text"));
		p.add(new JButton("<html><table border=\"1\">"
				+ "<tr><td>Button</td><td>mit</td></tr>"
				+ "<tr><td>HTML-</td><td>Text</td></tr>" + "</table></html>"));
		return p;
	}

	public static JPanel createThreadUebersicht()
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));

		// Den chart als Panel aufbauen
		JFreeChart chart = createChart(1);
		panel.add(new ChartPanel(chart));
		panel.addChart(chart);

		// Die Selektionsliste aufbauen
		JList jl = Tab6Vertrauen.listeThreads();
		panel.add(jl);
		jl.setSelectedIndex(1);

		// Den Seitlichen Scroller generieren
		JScrollPane scrollPane = new JScrollPane(jl);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}
}
