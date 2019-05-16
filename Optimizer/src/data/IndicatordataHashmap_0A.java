package data;

import gui.Mbox;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import jhilf.JProgressWin;

public class IndicatordataHashmap_0A extends IndiGrundfunkt
{
	// diese Klasse speichert alle Zeilen der indikatoren.
	// eine Zeile hat n Attribute

	private Date mindate = null;
	private Date maxdate = null;
	Indidat in = null;
	private int anzindi = 0;
	private String timeframe = null;

	// datum, indi1, indi2, indi3.... indiN
	private HashMap<Date, Indidat> datemap = new HashMap<Date, Indidat>();

	public IndicatordataHashmap_0A(String fnam)
	{
		String[] timechar =
		{ "M15", "M5", "M30", "H1", "H4", "D1", "M1" };
		// hier wird das indicatorfile eingelesen
		String zeile = null;
		Inf inf = new Inf();
		inf.setFilename(fnam);
		int count = 0, progresscount = 0;

		File fname = new File(fnam);
		long flen = fname.length();
		JProgressWin jp = new JProgressWin("load data <" + fnam + ">", 0,
				(int) (flen));
		jp.update(0);
		int anz = timechar.length;

		for (int i = 0; i < anz; i++)
		{
			if (fnam.contains(timechar[i]) == true)
			{
				timeframe = timechar[i];
				break;
			}
		}
		if (timeframe == null)
			Mbox.Infobox("timeframe <" + fnam + "> not known");

		while ((zeile = inf.readZeile()) != null)
		{

			if (count % 100 == 0)
			{
				System.out.println("count=" + count);
				jp.update(progresscount);

			}

			// in ist der vektor mit 99 attributen
			in = new Indidat(zeile);
			count++;
			progresscount = progresscount + zeile.length();
			if (mindate == null)
				mindate = in.getDt();

			datemap.put(in.getDt(), in);

		}
		anzindi = in.calcAnzDat();
		maxdate = in.getDt();
		jp.end();
	}

	public Date getMindate()
	{
		return mindate;
	}

	public void setMindate(Date mindate)
	{
		this.mindate = mindate;
	}

	public Date getMaxdate()
	{
		return maxdate;
	}

	public void setMaxdate(Date maxdate)
	{
		this.maxdate = maxdate;
	}

	public int getAnzIndi()
	{
		return anzindi;
	}

	public int isMarked(Trade tr,String tradedate, int index)
	{
		// kleinster index ist 0

		// return: 1 falls markiert also true
		// 0 falls nicht markiert also false
		// -1 falls im Indikator kein daten vorhanden

		// zeigt an das zum ersten male ein datum in der indikatordatei gefunden
		// wurde
		int firstvalidfoundflag = 0;

		// 1970.01.01 00:00
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt1 = null;
		try
		{
			dt1 = df.parse(tradedate);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jetzt wird das korregierte Timeframedatum ermittelt
		// bsp: im H4 ist 2.30 Uhr ==> 0 Uhr
		// im M15 ist 2.17 ==> 2.15 Uhr

		Date dt2lower = calcTimeframeDate(timeframe, dt1);
		
		//setze das lowerdate in dem trade
		tr.setIndikatorcheckdate(dt2lower.toString());
		
		if (dt2lower == null)
			Mbox.Infobox("internal error dt2 = null");

		//den indidat für das lowerdate ermitteln
		Indidat idat = datemap.get(dt2lower);

		// falls datum gefunden wurde
		if (idat != null)
		{
			firstvalidfoundflag = 1;

			// index-1 da im boolarray bei 0 angefangen wird die indikatoren
			// aber bei 1 anfangen
			boolean ma = idat.getArrayPos(index - 2);
			if (ma == true)
				return 1;
			else if (ma == false)
				return 0;

		} else
		{
			if (firstvalidfoundflag == 1)
			{
				// mitten drin wurde ein datum nicht gefunden warum nur ?
				Tracer.WriteTrace(20, "middle-date<" + dt2lower.toString()
						+ "> not found in indicator");
			}

			System.out.println("missing time<" + dt2lower.toString()
					+ "> in indidat");
			return -1;
		}

		// nix gefunden also false
		return 0;
	}
	
	private Date calcTimeframeDate(String timeframe, Date din)
	{
		//din: Eingangsdatum, für dies eingangsdatum soll die abgerundete Zeit ermittelt werden
		//
		Calendar myCal = new GregorianCalendar(); 
		int gmt = 0;
		Date dateout = din;
		myCal.setTime( din );  
		// die sekunden immer löschen
		myCal.set(Calendar.SECOND, 0);
		
		int minuten = myCal.get(Calendar.MINUTE);
		int hours = myCal.get(Calendar.HOUR_OF_DAY);
		
		
		// H1
		if (timeframe.equals("H1"))
		{
			// minute =0
			myCal.set(Calendar.MINUTE, 0);
			myCal.add(Calendar.HOUR_OF_DAY, -1);
			dateout=myCal.getTime();
			return dateout;
		}
		// D1
		if (timeframe.equals("D1"))
		{
			// shift-tage von dateout abziehen
			//shift tage abziehen, minute =0
			myCal.set(Calendar.MINUTE, 0);
			myCal.set(Calendar.HOUR_OF_DAY, 0);
			
			//shift abziehen
			myCal.add(Calendar.DAY_OF_MONTH, -1);
			dateout=myCal.getTime();
			return dateout;
		}
		// H4
		if (timeframe.equals("H4"))
		{
			//stunden auf einen modulo4 bringen
			int hours2 = ((4 * (hours / 4)) + gmt) % 24;
			myCal.set(Calendar.HOUR_OF_DAY,hours2);
			
			//minute =0, 4 stunden abziehen
			myCal.set(Calendar.MINUTE, 0);
			myCal.add(Calendar.HOUR_OF_DAY, -4);
			
			dateout=myCal.getTime();
			return dateout;
		}
		// M1
		if (timeframe.equals("M1"))
		{
			//eine Minute abziehen
			myCal.add(Calendar.MINUTE, -1);
			dateout=myCal.getTime();
			return dateout;
		}
		if (timeframe.equals("M30"))
		{
			//die minuten auf modulo30 synchronisierten
			int minutes2 = (30 * (minuten / 30)) % 60;
			myCal.set(Calendar.MINUTE, minutes2);
			//30 Minuten abziehen
			myCal.add(Calendar.MINUTE, -30);
			
			dateout=myCal.getTime();
			return dateout;
		}

		if (timeframe.equals("M15"))
		{
			//die Minuten auf modolu15 synchronisieren
			int minutes2 = (15 * (minuten / 15)) % 60;
			myCal.set(Calendar.MINUTE, minutes2);
			//15 Minuten abziehen
			myCal.add(Calendar.MINUTE, -15);
			
			dateout=myCal.getTime();
			return dateout;
		}
		if (timeframe.equals("M5"))
		{
			//die mintuen auf M5 synchronisieren
			int minutes2 = (5 * (minuten / 5)) % 60;
			myCal.set(Calendar.MINUTE, minutes2);
			
			//shift abziehen 5 minuten
			myCal.add(Calendar.MINUTE, -5);
			
			return dateout;
		}
		Mbox.Infobox("unknown timeframe <" + timeframe + ">");
		return null;
	}
}
