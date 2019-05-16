package swtOberfläche;

import hilfsklasse.DateExcecption;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import kurse.KursValueDB;
import kurse.Kursvalue;
import mainPackage.GC;
import objects.ThreadDbObj;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

import stores.ThreadsDB;
import swingOberfläche.Tab6Vertrauen;
import swingtest.DemoPanel;
import attribute.ThreadAttribObjI;
import attribute.ThreadAttribStoreI;

public class Tab11uiShowSliderSwing
{
	public static boolean RIGHT_TO_LEFT = false;
	static protected JTextArea textArea;

	public Tab11uiShowSliderSwing()
	{
	}

	public Tab11uiShowSliderSwing(String title, ThreadsDB tdb, ThreadDbObj tdbo)
	{
		// allethreadsflag=1, dann werden für eine mid alle threads gesucht und
		// dieses Ergebniss bei der Darstellung der Vertrauenswerte
		// berücksichtigt

		JFrame frame = new JFrame("Slider");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(850, 500);
		JTextArea txtfeld = new JTextArea(10, 25);

		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));
		JFreeChart chart = createChart(tdb, tdbo, txtfeld);
		panel.add(new ChartPanel(chart));
		panel.addChart(chart);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.WEST);

		panel.add(txtfeld, BorderLayout.EAST);
		frame.setVisible(true);
	}

	public static JPanel createJPanel(ThreadsDB tdb, ThreadDbObj tdbo)
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));

		// Den chart als Panel aufbauen
		JFreeChart chart = createChart(tdb, tdbo, textArea);
		panel.add(new ChartPanel(chart), BorderLayout.CENTER);
		panel.addChart(chart);

		// Die Selektionsliste aufbauen
		JList jl = Tab6Vertrauen.listeThreads();
		panel.add(jl, BorderLayout.WEST);
		jl.setSelectedIndex(1);

		// Den Seitlichen Scroller generieren
		JScrollPane scrollPane = new JScrollPane(jl);
		panel.add(scrollPane, BorderLayout.EAST);
		return panel;
	}

	private static JFreeChart createChart(ThreadsDB tdb, ThreadDbObj tdbo,
			JTextArea txtfeld)
	{
		// Baut das Mainfenster auf

		if (tdbo.getSymbol().length() < 2)
			Tracer.WriteTrace(10, "Error: symbol<" + tdbo.getSymbol()
					+ "> nicht erlaubt ");

		KursValueDB kvdb = new KursValueDB(tdbo.getSymbol(), 0,tdb);
		// AXIS 1 => Kurs
		XYDataset dataset1 = createPriceDataset(tdbo.getSymbol(),tdb);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Multiple Axis Demo 1", "Time of Day", "Primary Range Axis",
				dataset1, true, true, false);

		chart.addSubtitle(new TextTitle("Four datasets and four range axes."));
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.getRangeAxis().setFixedDimension(15.0);

		// AXIS 2 (Vertrauen)
		NumberAxis axis2 = new NumberAxis("Attribute");
		axis2.setFixedDimension(10.0);
		axis2.setAutoRangeIncludesZero(false);
		axis2.setLabelPaint(Color.red);
		axis2.setTickLabelPaint(Color.red);
		plot.setRangeAxis(1, axis2);
		plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
		XYDataset dataset2 = createAttributeDataset(tdbo, kvdb, textArea);
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);
		XYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setSeriesPaint(0, Color.red);
		plot.setRenderer(1, renderer2);

		// AXIS 3 (Attribute)
		NumberAxis axis3 = new NumberAxis("Attribute");
		axis3.setLabelPaint(Color.blue);
		axis3.setTickLabelPaint(Color.blue);
		plot.setRangeAxis(2, axis3);

		XYDataset dataset3 = createAttributeDataset(tdbo);
		plot.setDataset(2, dataset3);
		plot.mapDatasetToRangeAxis(2, 2);
		XYItemRenderer renderer3 = new StandardXYItemRenderer();
		renderer3.setSeriesPaint(0, Color.blue);
		plot.setRenderer(2, renderer3);
		ChartUtilities.applyCurrentTheme(chart);
		return chart;
	}

	private static XYDataset createPriceDataset(String symbol,ThreadsDB tdb)
	{
		// Hier wird das Dataset für den Kurs dargestellt
		String laufdatum = null;
		// create dataset 1...
		TimeSeries series1 = new TimeSeries("Kurs");

		// Hier wird der Kurs für ein symbol aufbereitet
		KursValueDB kvdb = new KursValueDB(symbol, 0,tdb);

		// läuft das Datum von mindate bis jetzt durch
		String mindate = kvdb.calcMindate();
		if (mindate == null)
		{
			Tracer.WriteTrace(10, "Warning: für Symbol <" + symbol
					+ "> ist kein Kurs vorhanden");
			return null;
		}
		String aktdate = Tools.entferneZeit(Tools.get_aktdatetime_str());
		laufdatum = mindate;
		Kursvalue kval = kvdb.holeKurswert(laufdatum, 0);
		float kv = kval.getKv();

		while (Tools.datum_ist_aelter(laufdatum, aktdate))
		{
			try
			{
				int tag = Tools.get_day_int(laufdatum);
				int monat = Tools.get_month_int(laufdatum);
				int jahr = Tools.get_year_int(laufdatum);
				if (jahr < 1900)
					jahr = jahr + 2000;

				laufdatum = Tools.modifziereDatum(laufdatum, 0, 0, 1, 0);
				kval = kvdb.holeKurswert(laufdatum, 0);
				kv = kval.getKv();

				series1.add(new Day(tag, monat, jahr), kv);
				System.out.println("datensatz datum<" + laufdatum + ">tag<"
						+ tag + "> monat<" + monat + "> jahr<" + jahr
						+ "> val<" + kv + ">");
			} catch (DateExcecption e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new TimeSeriesCollection(series1);
	}

	private static IntervalXYDataset createAttributeDataset(ThreadDbObj tdbo,
			KursValueDB kvdb, JTextArea txtfeld)
	{
		int tag = 0, monat = 0, jahr = 0;
		int tid = 0;
		String msg = null;

		if ((tid = tdbo.getThreadid()) == 0)
			return null;

		msg = "";
		TimeSeries series2 = new TimeSeries("Vertrauen" + msg);
		// txtfeld.append("tid=" + tid + "\n");
		ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tid, "\\db\\Attribute\\Threads");

		int anz = attribstore.GetanzObj();
		if (anz == 0)
			Tracer.WriteTrace(10, "Info: keine Attribute vorhanden tid<" + tid
					+ ">");
		for (int k = 0; k < anz; k++)
		{
			ThreadAttribObjI attribobj = (ThreadAttribObjI) attribstore
					.GetObjectIDX(k);

			// hier werden die daten generiert
			String datum = Tools.entferneZeit(attribobj.getDatum());
			Float vertrauval = attribobj
					.getAttribvalue(GC.ATTRIB_MITTLERERRANK);

			try
			{
				tag = Tools.get_day_int(datum);
				monat = Tools.get_month_int(datum);
				jahr = Tools.get_year_int(datum);
				if (jahr < 1900)
					jahr = jahr + 2000;
			} catch (DateExcecption e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			series2.add(new Day(tag, monat, jahr), vertrauval);
		}
		return new TimeSeriesCollection(series2);
	}

	private static XYDataset createAttributeDataset(ThreadDbObj tdbo)
	{
		// int ATTRIB_LASTPOSTID = 0;
		// int ATTRIB_USERIN20TAGEN = 1;
		// int ATTRIB_MITTLERERRANK = 2;
		// int ATTRIB_anzGuteUser = 3;
		// int ATTRIB_anzSchlechteUser = 4;
		// int ATTRIB_anzBadUser = 5;
		// int ATTRIB_anzSchlechtePostings = 6;
		// int ATTRIB_anzGutePostings = 7;
		// int ATTRIB_anzNeueGuteUser = 8;
		// int ATTRIB_anzNeueSchlechteUser = 9;
		// int ATTRIB_anzNeueBadUser = 10;

		ThreadAttribStoreI ts = new ThreadAttribStoreI(tdbo.getThreadid(), "\\db\\Attribute\\Threads");
		TimeSeries series1 = new TimeSeries("Useranzahl", Day.class);
		TimeSeries series2 = new TimeSeries("+User", Day.class);
		TimeSeries series3 = new TimeSeries("-User", Day.class);
		TimeSeries series4 = new TimeSeries("+Postings", Day.class);
		TimeSeries series5 = new TimeSeries("-Postings", Day.class);
		TimeSeries series6 = new TimeSeries("+Neu", Day.class);
		TimeSeries series7 = new TimeSeries("-Neu", Day.class);

		String mindate = ts.calcMindatum();
		if (mindate == null)
		{
			Tracer.WriteTrace(10, "Warning: für tid <" + tdbo.getThreadid()
					+ "> kein Attribut vorhanden");
			return null;
		}

		int anz = ts.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI to = (ThreadAttribObjI) ts.GetObjectIDX(i);
			String laufdatum = to.getDatum();

			int tag;
			try
			{
				tag = Tools.get_day_int(laufdatum);

				int monat = Tools.get_month_int(laufdatum);
				int jahr = Tools.get_year_int(laufdatum);
				if (jahr < 1900)
					jahr = jahr + 2000;

				series1.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_USERIN20TAGEN));
				series2.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_anzGuteUser));
				series3.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_anzSchlechteUser));
				series4.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_anzGutePostings));
				series5.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_anzSchlechtePostings));
				series6.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_anzNeueGuteUser));
				series7.add(new Day(tag, monat, jahr), ts.getAttrib(laufdatum,
						GC.ATTRIB_anzNeueSchlechteUser));
			} catch (DateExcecption e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);
		dataset.addSeries(series5);
		dataset.addSeries(series6);
		dataset.addSeries(series7);
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset)
	{
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Time Series Demo 3", "Time", "Value", dataset, true, true,
				false);
		XYPlot plot = (XYPlot) chart.getPlot();
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1,
				new SimpleDateFormat("MMM-yyyy")));
		axis.setVerticalTickLabels(true);

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		renderer.setBaseShapesVisible(true);
		renderer.setSeriesFillPaint(0, Color.red);
		renderer.setSeriesFillPaint(1, Color.white);
		renderer.setUseFillPaint(true);
		renderer
				.setLegendItemToolTipGenerator(new StandardXYSeriesLabelGenerator(
						"Tooltip {0}"));
		return chart;
	}

}
