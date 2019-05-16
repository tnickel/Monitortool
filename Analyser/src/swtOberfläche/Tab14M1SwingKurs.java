package swtOberfläche;

import hilfsklasse.DateExcecption;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;

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

import stores.ThreadsDB;
import swingOberfläche.Tab6Vertrauen;
import swingtest.DemoPanel;
import attribute.ThreadAttribStoreI;

public class Tab14M1SwingKurs
{
	public static boolean RIGHT_TO_LEFT = false;
	static protected JTextArea textArea;

	public Tab14M1SwingKurs(String title, ThreadsDB tdb, ThreadDbObj tdbo)
	{
		// allethreadsflag=1, dann werden für eine mid alle threads gesucht und
		// dieses Ergebniss bei der Darstellung der Vertrauenswerte
		// berücksichtigt

		JFrame frame = new JFrame("Vertrauensindikator");
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

	public static JPanel createJPanel(ThreadsDB tdb, ThreadDbObj tdbo,
			int vertrauflag, int allethreadsflag)
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

		KursValueDB kvdb = new KursValueDB(tdbo.getSymbol(), 1, tdb);

		XYDataset priceData = createPriceDataset(tdbo.getSymbol(), tdb);
		String title = tdbo.getThreadname();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date",
				"Price", priceData, true, true, false);

		XYPlot plot = (XYPlot) chart.getPlot();
		// Axis 1 (Kurs)
		NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
		rangeAxis1.setLowerMargin(0.1); // to leave room for volume bars
		DecimalFormat format = new DecimalFormat("00.00");
		rangeAxis1.setNumberFormatOverride(format);
		XYItemRenderer renderer1 = plot.getRenderer();
		renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

		// Axis 2 (Slider20)
		NumberAxis rangeAxis2 = new NumberAxis("Prognosen +");
		rangeAxis2.setUpperMargin(1.00); // to leave room for price line
		plot.setRangeAxis(2, rangeAxis2);
		plot.setDataset(2, createSlider20Dataset(tdb, tdbo, txtfeld));
		plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_LEFT);
		plot.setRangeAxis(2, rangeAxis2);
		plot.mapDatasetToRangeAxis(2, 2);
		XYBarRenderer renderer2 = new XYBarRenderer(0.20);
		renderer2.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat(
						"0,000.00")));
		plot.setRenderer(2, renderer2);

		ChartUtilities.applyCurrentTheme(chart);
		renderer2.setBarPainter(new StandardXYBarPainter());
		// renderer3.setBarPainter(new StandardXYBarPainter());
		renderer2.setShadowVisible(false);
		// renderer3.setShadowVisible(false);

		return chart;
	}

	private static XYDataset createPriceDataset(String symbol, ThreadsDB tdb)
	{
		int errzaehl=0;
		String laufdatum = null;
		// create dataset 1...
		TimeSeries series1 = new TimeSeries("Kurs");

		// Hier wird der Kurs für ein symbol aufbereitet
		KursValueDB kvdb = new KursValueDB(symbol, 0, tdb);

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

	private static IntervalXYDataset createSlider20Dataset(ThreadsDB tdb,
			ThreadDbObj tdbo, JTextArea txtfeld)
	{
		String laufdatum = null;
		

		// für eine Masterid werden sämtliche Threads gesucht (threadid) so das
		// sämtliche prognosen aufgelistet werden
		HashSet<String> datumsset = new HashSet<String>();

		if (tdbo.getThreadid() == 0)
			return null;

		String aktdate = Tools.entferneZeit(Tools.get_aktdatetime_str());
		// create dataset x...

		TimeSeries series2 = new TimeSeries("Slider20");

		ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tdbo
				.getThreadid(), "\\db\\Attribute\\Threads");
		String mindat = attribstore.calcMindatum();
		laufdatum = mindat;
		int anzahlhandlungen = fuelleDatenserie(laufdatum, aktdate, series2,
				attribstore, datumsset);
		txtfeld.append("xxx");
		return new TimeSeriesCollection(series2);
	}

	
	private static int fuelleDatenserie(String laufdatum, String aktdate,
			TimeSeries series2, ThreadAttribStoreI attribstore,
			HashSet<String> datumsset)
	{
		int handlungen = 0;

		// durchläuft die Zeitreihe bis zum aktuellen Datum
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

				float useranz = attribstore.getAttrib(laufdatum,
						GC.ATTRIB_USERIN20TAGEN);

				// Nimm kein Datum doppelt auf
				if (datumsset.contains(Tools.entferneZeit(laufdatum)) == false)
				{
					series2.add(new Day(tag, monat, jahr), useranz);
					datumsset.add(Tools.entferneZeit(laufdatum));
					handlungen++;
				}
				System.out.println("datensatz datum<" + laufdatum + ">tag<"
						+ tag + "> monat<" + monat + "> jahr<" + jahr
						+ "> useranz<" + useranz + ">");
			} catch (DateExcecption e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return handlungen;
	}

	public static JPanel createVertrauenChart(ThreadsDB tdb, ThreadDbObj tdbo,
			JTextArea txtfeld, int vertrauflag, int allethreadsflag)
	{
		JFreeChart chart = createChart(tdb, tdbo, txtfeld);
		return new ChartPanel(chart);
	}
}
