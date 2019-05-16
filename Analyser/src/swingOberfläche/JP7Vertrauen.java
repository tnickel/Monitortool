package swingOberfläche;

import hilfsklasse.DateExcecption;
import hilfsklasse.Tools;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JPanel;
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
import swingtest.DemoPanel;
import attribute.ThreadAttribStoreI;

public class JP7Vertrauen
{
	static protected JTextArea textArea;

	public JP7Vertrauen()
	{
	}

	private static JFreeChart createChart(ThreadsDB tdb, ThreadDbObj tdbo,
			JTextArea txtfeld)
	{
		// tidmenge ermitteln
		ArrayList<Integer> tidArray = tdb.calcTidArray(tdbo.getMasterid());
		int tidanz = tidArray.size();

		KursValueDB kvdb = new KursValueDB(tdbo.getSymbol(), 1,tdb);

		XYDataset priceData = createPriceDataset(tdbo.getSymbol(), txtfeld,tdb);
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
		NumberAxis rangeAxis2 = new NumberAxis("Vertrauen(gewinn)");
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
		NumberAxis rangeAxis3 = new NumberAxis("Vertrauen(verlust)");
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

		ChartUtilities.applyCurrentTheme(chart);
		renderer2.setBarPainter(new StandardXYBarPainter());
		// renderer3.setBarPainter(new StandardXYBarPainter());
		renderer2.setShadowVisible(false);
		// renderer3.setShadowVisible(false);

		return chart;
	}

	private static XYDataset createPriceDataset(String symbol, JTextArea txtfeld,ThreadsDB tdb)
	{
		String laufdatum = null;
		// create dataset 1...
		TimeSeries series1 = new TimeSeries("Kurs");

		// Hier wird der Kurs für ein symbol aufbereitet
		KursValueDB kvdb = new KursValueDB(symbol, 0,tdb);

		// läuft das Datum von mindate bis jetzt durch
		String mindate = kvdb.calcMindate();
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

		TimeSeries series2 = new TimeSeries("Vertrauen " + msg);

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
		// gewinnflag=1=> nur gewinne sammeln
		// gewinnflag=0=> nur verluste sammeln
		// return: anz handlungen gewinn/verlust

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

	public static JPanel createDemoPanel(ThreadsDB tdb, ThreadDbObj tdbo,
			JTextArea txtfeld)
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));

		// Den Kurs-chart als Panel aufbauen
		JFreeChart chart = createChart(tdb, tdbo, txtfeld);
		panel.add(new ChartPanel(chart));
		panel.addChart(chart);

		return panel;
	}
}
