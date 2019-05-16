package swtOberfläche;

import hilfsklasse.DateExcecption;
import hilfsklasse.Ma;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
import attribute.ThreadAttribObjI;
import attribute.ThreadAttribStoreI;



public class SwingVertrau1
{
	public static boolean RIGHT_TO_LEFT = false;
	static protected JTextArea textArea;

	public SwingVertrau1(String title, ThreadsDB tdb, ThreadDbObj tdbo,
			int vertrauflag, int allethreadsflag)
	{
		// allethreadsflag=1, dann werden für eine mid alle threads gesucht und
		// dieses Ergebniss bei der Darstellung der Vertrauenswerte
		// berücksichtigt

		JFrame frame = new JFrame("Vertrauensindikator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(850, 500);
		JTextArea txtfeld = new JTextArea(10, 25);

		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));
		JFreeChart chart = createChart(tdb, tdbo, txtfeld, vertrauflag,
				allethreadsflag);
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
		JFreeChart chart = createChart(tdb, tdbo, textArea, vertrauflag,
				allethreadsflag);
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
			JTextArea txtfeld, int vertrauflag, int allethreadsflag)
	{
		// Baut das Mainfenster auf
		// vertrauflag: falls vertrauflag = 1 ist wird auch der Vertrauenswert
		// mit ausgegeben

		// tidmenge ermitteln
		ArrayList<Integer> tidArray = tdb.calcTidArray(tdbo.getMasterid());
		int tidanz = tidArray.size();

		if (tidanz == 0)
			Tracer.WriteTrace(10, "Error: internal mid<" + tdbo.getMasterid()
					+ "> hat keine tid´s");

		if (tdbo.getSymbol().length() < 2)
			Tracer.WriteTrace(10, "Error: symbol<" + tdbo.getSymbol()
					+ "> nicht erlaubt ");

		KursValueDB kvdb = new KursValueDB(tdbo.getSymbol(), 1,tdb);

		XYDataset priceData = createPriceDataset(tdbo.getSymbol(),tdb);
		String title = tdbo.getThreadname() + " Tidanz=" + tidanz;
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

		// Axis 2 (Kaufsignale/gewinne)
		NumberAxis rangeAxis2 = new NumberAxis("Prognosen +");
		rangeAxis2.setUpperMargin(1.00); // to leave room for price line
		plot.setRangeAxis(2, rangeAxis2);
		plot.setDataset(2, createKaufsignaleDataset(tdb, tdbo, 1, kvdb,
				tidArray, txtfeld));
		plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_LEFT);
		plot.setRangeAxis(2, rangeAxis2);
		plot.mapDatasetToRangeAxis(2, 2);
		XYBarRenderer renderer2 = new XYBarRenderer(0.20);
		renderer2.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat(
						"0,000.00")));
		plot.setRenderer(2, renderer2);

		// Kennlinie 3 (Kaufsignale/verluste)
		NumberAxis rangeAxis3 = new NumberAxis("Prognosen -");
		rangeAxis3.setUpperMargin(1.00); // to leave room for price line
		plot.setRangeAxis(3, rangeAxis3);
		plot.setDataset(3, createKaufsignaleDataset(tdb, tdbo, 0, kvdb,
				tidArray, txtfeld));
		plot.setRangeAxisLocation(3, AxisLocation.BOTTOM_OR_RIGHT);
		plot.setRangeAxis(3, rangeAxis3);
		plot.mapDatasetToRangeAxis(3, 3);
		XYBarRenderer renderer3 = new XYBarRenderer(0.20);

		renderer3.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat(
						"0,000.00")));
		plot.setRenderer(3, renderer3);

		// Kennlinie 4 (Vertrauen)
		if (vertrauflag == 1)
		{
			NumberAxis rangeAxis4 = new NumberAxis("Vertrauen");
			rangeAxis4.setUpperMargin(1.00); // to leave room for price line
			plot.setRangeAxis(4, rangeAxis4);
			plot.setDataset(4, createVertrauenDataset(tdb, tdbo, 0, kvdb,
					tidArray, txtfeld, allethreadsflag));
			plot.setRangeAxisLocation(4, AxisLocation.BOTTOM_OR_RIGHT);
			plot.setRangeAxis(4, rangeAxis4);
			plot.mapDatasetToRangeAxis(4, 4);
			XYBarRenderer renderer4 = new XYBarRenderer(0.20);

			renderer4.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
					StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
					new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat(
							"0,000.00")));
			plot.setRenderer(4, renderer4);
		}

		ChartUtilities.applyCurrentTheme(chart);
		renderer2.setBarPainter(new StandardXYBarPainter());
		// renderer3.setBarPainter(new StandardXYBarPainter());
		renderer2.setShadowVisible(false);
		// renderer3.setShadowVisible(false);

		return chart;
	}

	private static XYDataset createPriceDataset(String symbol,ThreadsDB tdb)
	{
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

	private static IntervalXYDataset createKaufsignaleDataset(ThreadsDB tdb,
			ThreadDbObj tdbo, int gewinnflag, KursValueDB kvdb,
			ArrayList<Integer> tidArray, JTextArea txtfeld)
	{
		String laufdatum = null;
		String msg = null, msg2 = null;
		int gewinnprognosenanz = 0;
		int verlustprognosenanz = 0;
		// gewinnflag:1=> sammle nur die gewinne
		// gewinnflag=0=> sammle nur die verluste

		// für eine Masterid werden sämtliche Threads gesucht (threadid) so das
		// sämtliche prognosen aufgelistet werden
		HashSet<String> datumsset = new HashSet<String>();

		if (tdbo.getThreadid() == 0)
			return null;

		String aktdate = Tools.entferneZeit(Tools.get_aktdatetime_str());
		// create dataset x...
		if (gewinnflag == 1)
			msg = "+";
		else
			msg = "-";

		TimeSeries series2 = new TimeSeries("Prognosen " + msg);

		// durchlaufe alle tid´s
		int anz = tidArray.size();
		for (int i = 0; i < anz; i++)
		{
			ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tidArray
					.get(i),"\\db\\Attribute\\Threads");
			String mindat = attribstore.calcMindatum();
			laufdatum = mindat;
			int anzahlhandlungen = fuelleDatenserie(laufdatum, aktdate,
					series2, attribstore, datumsset, kvdb, gewinnflag);

			if (gewinnflag == 1)
				gewinnprognosenanz = gewinnprognosenanz + anzahlhandlungen;
			else
				verlustprognosenanz = verlustprognosenanz + anzahlhandlungen;
		}
		if (gewinnprognosenanz > 0)
			msg2 = "Gewinnhandlungen<" + gewinnprognosenanz + ">\n";
		else if (verlustprognosenanz > 0)
			msg2 = "Verlusthandlungen<" + verlustprognosenanz + "> \n";
		else
			msg2 = "-----\n";
		txtfeld.append(msg2);

		return new TimeSeriesCollection(series2);
	}

	private static IntervalXYDataset createVertrauenDataset(ThreadsDB tdb,
			ThreadDbObj tdbo, int gewinnflag, KursValueDB kvdb,
			ArrayList<Integer> tidArray, JTextArea txtfeld, int allethreadsflag)
	{

		int tag = 0, monat = 0, jahr = 0;
		String msg = null;
		Float vval = 0f;
		int anzahlval = 0;
		// allethreadsflag==1, dann werden für eine mid alle threads gesucht und
		// dies bei der Darstellung berücksichtigt

		// speichert Datum und Summe der Vertrauenswerte
		HashMap<String, Float> vertrausum = new HashMap<String, Float>();
		// speichert datum und anzahl der vertrauenswerte
		HashMap<String, Integer> anzvertrausum = new HashMap<String, Integer>();

		// für eine Masterid werden sämtliche Threads gesucht (threadid) so das
		// sämtliche prognosen aufgelistet werden

		if (tdbo.getThreadid() == 0)
			return null;

		msg = "";

		TimeSeries series2 = new TimeSeries("Vertrauen" + msg);

		// durchlaufe alle tid´s
		int anztids = tidArray.size();

		for (int i = 0; i < anztids; i++)
		{
			int tid = tidArray.get(i);

			// falls nur die eine tid ausgwertet werden soll
			if ((allethreadsflag == 0) && (tid != tdbo.getThreadid()))
				continue;
			txtfeld.append("tid=" + tid + "\n");

			ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tidArray
					.get(i),"\\db\\Attribute\\Threads");

			int anz = attribstore.GetanzObj();
			for (int k = 0; k < anz; k++)
			{
				ThreadAttribObjI attribobj = (ThreadAttribObjI) attribstore
						.GetObjectIDX(k);

				// hier werden die daten generiert
				String datum = Tools.entferneZeit(attribobj.getDatum());
				Float vertrauval = Ma.scaleVertrauen(attribobj
						.getAttribvalue(GC.ATTRIB_MITTLERERRANK), 50000);

				if ((vertrausum.size() > 0) && (vertrausum.containsKey(datum)))
					vval = vertrausum.get(datum);
				else
					vval = 0f;

				if ((anzvertrausum.size() > 0)
						&& (anzvertrausum.containsKey(datum)))
					anzahlval = anzvertrausum.get(datum);
				else
					anzahlval = 0;

				// addiere die einzelen vertrauenswerte
				vval = vval + vertrauval;
				anzahlval = anzahlval + 1;
				vertrausum.put(datum, vval);
				anzvertrausum.put(datum, anzahlval);
			}
		}

		// normierung der vertrauensumme
		int anz = vertrausum.size();
		int anz2 = anzvertrausum.size();

		if (anz != anz2)
			Tracer.WriteTrace(10, "internal Error: Längenfehler anz<" + anz
					+ "> anz2<" + anz2 + ">");

		// Die Hashmap wird durchlaufen und Normiert
		for (Map.Entry<String, Float> e : vertrausum.entrySet())
		{
			String datum = e.getKey();
			Float f = e.getValue();

			// stützstellenanzahl
			int vanz = anzvertrausum.get(datum);

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
			f = f / vanz;
			series2.add(new Day(tag, monat, jahr), f);
		}

		return new TimeSeriesCollection(series2);
	}

	private static float calcGewinnFuerDatum(KursValueDB kvdb,
			String laufdatum, String handelshinweis)
	{
		// Handelshinweis z.B.="Kaufe fuer <2000006.0>Euro"
		laufdatum = Tools.entferneZeit(laufdatum);
		String enddatum10 = Tools.modifziereDatum(laufdatum, 0, 0, 10, 0);

		// Extrahiere Eurosumme Bsp: Kaufe fuer <2000006.0>Euro
		String einsatztext = handelshinweis.substring(handelshinweis
				.indexOf("<") + 1, handelshinweis.indexOf("."));
		float einsatz = Float.valueOf(einsatztext);
		Kursvalue kurs = kvdb.holeKurswert(laufdatum, 0);
		Kursvalue kurs10 = kvdb.holeKurswert(enddatum10, 0);
		float k = kurs.getKv();
		float k10 = kurs10.getKv();

		// falls kein Kurs dann kein Gewinn
		if ((k == 0) || (k10 == 0))
			return 0;

		// anzahl der gekauften Aktien
		float anz = einsatz / k;
		float wert10 = anz * k10;
		float gewinn = wert10 - einsatz;
		return gewinn;
	}

	private static int fuelleDatenserie(String laufdatum, String aktdate,
			TimeSeries series2, ThreadAttribStoreI attribstore,
			HashSet<String> datumsset, KursValueDB kvdb, int gewinnflag)
	{
		// a)gewinnflag=1=> nur gewinne sammeln
		// b)gewinnflag=0=> nur verluste sammeln
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

				float vertrau = attribstore.getAttrib(laufdatum,
						GC.ATTRIB_MITTLERERRANK);
				String kaufsignal = attribstore.getHandelshinweis(laufdatum);

				if ((kaufsignal != null) && (kaufsignal.contains("Kaufe")))
				{

					// Nimm kein Datum doppelt auf
					if (datumsset.contains(Tools.entferneZeit(laufdatum)) == false)
					{
						// ermittle den Gewinn bei einer 10Tage Prognose
						Float gewinn = calcGewinnFuerDatum(kvdb, laufdatum,
								kaufsignal);

						// Prüfe das gewinnflag (Gewinne oder verluste
						// vermerken)
						if (((gewinn > 0) && (gewinnflag == 1)) || (gewinn < 0)
								&& (gewinnflag == 0))
						{
							handlungen++;
							series2.add(new Day(tag, monat, jahr), 200);
							datumsset.add(Tools.entferneZeit(laufdatum));
						}
					}
				}
				System.out.println("datensatz datum<" + laufdatum + ">tag<"
						+ tag + "> monat<" + monat + "> jahr<" + jahr
						+ "> vertrauen<" + vertrau + ">");
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
		JFreeChart chart = createChart(tdb, tdbo, txtfeld, vertrauflag,
				allethreadsflag);
		return new ChartPanel(chart);
	}

}
