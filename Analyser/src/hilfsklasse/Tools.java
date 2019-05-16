package hilfsklasse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import mainPackage.GC;

public class Tools extends SG
{
	static public void showPdf(String fnam)
	{
		try
		{
			String cmd = "\"" + GC.acroreaderbase + "\" \"" + fnam + "\"";
			String line = null;

			System.out.println("zeile<" + cmd + ">");
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader lsOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = lsOut.readLine()) != null)
			{
				System.out.println(line);
			}
		} catch (Exception e)
		{
			System.err.println("ls error " + e);
		}
	}
	
	static public void showText(String fnam)
	{
		try
		{
			String cmd = "\"" + GC.notepadbase + "\" \"" + fnam + "\"";
			String line = null;

			System.out.println("zeile<" + cmd + ">");
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader lsOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = lsOut.readLine()) != null)
			{
				System.out.println(line);
			}
		} catch (Exception e)
		{
			System.err.println("ls error " + e);
		}
	}
	
	static public boolean checkFilesystem()
	{
		String fnam = GC.rootpath
				+ "\\db\\Userthreadvirtualkonto\\testfile.html";

		if (FileAccess.FileAvailable0(fnam))
			FileAccess.FileDelete(fnam, 1);

		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.writezeile("testdaten");
		inf.close();
		return true;
	}

	static public boolean istKursKorrekt(float a, float b,
			float prozentabweichung)
	{
		// prüft ob dies ein korrekter kurs ist
		// d.h. abweichung von a und b ist nicht zu stark
		Float min = 0f, max = 0f;

		if (a < b)
		{
			min = a;
			max = b;
		} else
		{
			min = b;
			max = a;
		}

		float istprozentabweichung = 100 - ((100f / min) * max);
		if (istprozentabweichung > prozentabweichung)
		{
			System.out.println("istprozentabweichung<" + istprozentabweichung
					+ "> prozentabweichung<" + prozentabweichung + "> min<"
					+ min + "> max<" + max + ">");
			return false;
		} else
		{
			return true;
		}
	}

	static public String convDatumKomma(String suchdatum)
	{
		//umwandlung von
		//2009.01.02,19:48->24.03.06 14:00:59 
		String part1=suchdatum.substring(0,suchdatum.indexOf(","));
		
		String tag = null, monat = null, jahr = null;
		String sd=new String(part1);
		if (sd.contains("."))
		{
			try
			{
				jahr = Tools.nteilstring(sd, ".", 1);
				jahr=jahr.substring(2);
				monat = Tools.nteilstring(sd, ".", 2);
				tag = Tools.nteilstring(sd, ".", 3);
				int j = Integer.parseInt(jahr);

				

			} catch (ToolsException e)
			{
				e.printStackTrace();
				Tracer.WriteTrace(10, "Error:interal datumsformat<" + suchdatum
						+ ">");
			}
			sd = tag + "." + monat + "." + jahr;
		}
		
		String part2=suchdatum.substring(suchdatum.indexOf(",")+1);


		
		String newdatum=sd+ " "+part2+":00";
		return newdatum;
		
	}
	
	static public String convDatumPunktStrich(String suchdatum)
	{
		// wandelt 10.10.04 in 2004-10-10 um
		// 10.10.70 in 2070-10-10
		// 10.10.71 in 1971-10-10
		// 10.10.72 in 1972-10-10
		// 10.10.99 in 1999-10-10

		String tag = null, monat = null, jahr = null;
		if (suchdatum.contains("."))
		{
			try
			{
				tag = Tools.nteilstring(suchdatum, ".", 1);
				monat = Tools.nteilstring(suchdatum, ".", 2);
				jahr = Tools.nteilstring(suchdatum, ".", 3);
				int j = Integer.parseInt(jahr);

				if (j > 70)
				{
					jahr = "19" + jahr;
				} else if (j <= 70)
				{
					jahr = "20" + jahr;
				} else
				{
					Tracer.WriteTrace(10, "Error: internal datumsfehler jahr<"
							+ jahr + "> j<" + j + ">");
				}

			} catch (ToolsException e)
			{
				e.printStackTrace();
				Tracer.WriteTrace(10, "Error:interal datumsformat<" + suchdatum
						+ ">");
			}
			suchdatum = jahr + "-" + monat + "-" + tag;
			return suchdatum;
		} else
			return suchdatum;

	}

	static public String convDatumStrichPunkt(String suchdatum)
	{
		// wandelt
		// 2004-10-10 nach 10.10.04
		// 2070-10-10 nach 10.10.70
		// 1971-10-10 nach 10.10.71
		// 1972-10-10 nach 10.10.72
		// 1999-10-10 nach 10.10.99

		String tag = null, monat = null, jahr = null, jstr = null;
		;

		if (suchdatum.contains("-"))
		{
			try
			{
				tag = Tools.nteilstring(suchdatum, "-", 3);
				monat = Tools.nteilstring(suchdatum, "-", 2);
				jahr = Tools.nteilstring(suchdatum, "-", 1);

				if (jahr.length() == 4)
				{
					jstr = jahr.substring(2, 4);
				} else
					Tracer.WriteTrace(10, "Error:interal datumsformat<"
							+ suchdatum + ">");
			} catch (ToolsException e)
			{
				e.printStackTrace();
				Tracer.WriteTrace(10, "Error:interal datumsformat<" + suchdatum
						+ ">");
			}
			suchdatum = tag + "." + monat + "." + jstr;
			return suchdatum;
		} else
			return suchdatum;

	}

	static public String convDatumTextStrich(String dat)
	{
		// 1 Apr 2008 wird umgewandet in 2008-04-01

		int tag = 0, mon = 0, year = 0;
		try
		{
			tag = get_day_int(dat);
			mon = get_month_int(dat);
			year = get_year_int(dat);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String out = new String(year + "-" + mon + "-" + tag);
		return out;

	}

	static public String calcSekundenString(String date)
	{
		// Wandelt das datum in einen sekunden-String um
		long dsec = get_date_milisec_lo(date) / 1000;
		String str = new String(String.valueOf(dsec));
		return str;
	}

	static public String cutStringlaenge(String str, int laenge)
	{
		if (str == null)
			return null;

		if (str.length() < laenge)
			return str;
		else
			return (str.substring(0, laenge));
	}

	static public boolean checkKursVerfügbar(String symb)
	{
		if (isKorrektSymbol(symb) == false)
		{
			Tracer.WriteTrace(20, "Warning:checkKurs ungültiges symbol <"
					+ symb + ">");
			return false;
		}
		String fnam = GC.rootpath + "\\db\\kurse\\" + symb + ".csv";
		if (FileAccess.FileLength(fnam) > 0)
			return true;
		else
			return false;
	}

	static public String holeSubstring(String eingang, int maxlen)
	{
		int len = eingang.length();
		if (maxlen >= len)
			return eingang;
		else
			return (eingang.substring(0, maxlen));

	}

	static public boolean isKorrektSymbol(String symb)
	{
		if (symb == null)
			return false;
		if (symb.length() < 2)
			return false;
		if (SG.is_zahl(symb) == true)
			return false;

		return true;
	}

	public static boolean isDate(String date)
	{
		if (date == null)
			return false;

		if (date.length() == 10)
			return true;

		if ((date.length() < 8) || (date.length() > 19))
			return false;
		return true;
	}

	public static long get_mindate_lo()
	{
		long d1_sec = 0;
		GregorianCalendar d1_date = new GregorianCalendar();
		d1_date.set(1970, 0, 1);
		d1_sec = d1_date.getTime().getTime();
		return (d1_sec);
	}

	public static long get_aktdate_lo()
	{
		long d1_sec = 0;
		GregorianCalendar d1_date = new GregorianCalendar();
		d1_sec = d1_date.getTime().getTime();
		return (d1_sec);
	}

	public static String get_aktdate_str(String formater)
	{
		long d1_sec = 0;
		String akt_date_str = null;

		d1_sec = get_aktdate_lo();
		akt_date_str = get_date_string(d1_sec, formater);
		return (akt_date_str);
	}

	public static String get_date_string(long milisecs, String formater)
	{// wandelt aus den milisec nach 1970 in einen datumsstring um
		SimpleDateFormat dateFormatter;
		String dateString;

		if (formater == null)
			formater = "yyyy-MM-dd";

		GregorianCalendar d1_date = new GregorianCalendar();

		d1_date.setTimeInMillis(milisecs);
		dateFormatter = new SimpleDateFormat(formater);
		dateString = dateFormatter.format(d1_date.getTime());
		return (dateString);
	}

	public static String get_aktdatetime_str()
	{// wandelt aus den milisec nach 1970 in einen datumsstring um
		SimpleDateFormat dateFormatter;
		String dateString;
		long milisecs = get_aktdate_lo();
		GregorianCalendar d1_date = new GregorianCalendar();

		d1_date.setTimeInMillis(milisecs);
		dateFormatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		dateString = dateFormatter.format(d1_date.getTime());
		return (dateString);
	}
	public static String get_akttime_str()
	{// wandelt aus den milisec nach 1970 in einen datumsstring um
		SimpleDateFormat dateFormatter;
		String dateString;
		long milisecs = get_aktdate_lo();
		GregorianCalendar d1_date = new GregorianCalendar();

		d1_date.setTimeInMillis(milisecs);
		dateFormatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		dateString = dateFormatter.format(d1_date.getTime());
		return (dateString);
	}
	public static String modifziereDatum(String quelldatum, int jahroffset,
			int monatoffset, int tagoffset, int plausiflag)
	{
		// Konvertiert das quelldatum zum Zieldatum
		// es wird hierbei das jahr, monat und der Tag addiert/subtrahiert
		SimpleDateFormat dateFormatter;
		long dmil = get_date_milisec_lo(quelldatum);
		long offset_tag = 0, offset_monat = 0, offset_jahr = 0;

		try
		{
			FormatCheckTime(quelldatum);
		} catch (DateExcecption e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: date format2 <" + quelldatum + ">");
		}
		if (plausiflag == 1)
			if ((jahroffset > 10) || (jahroffset < -10) || (monatoffset > 12)
					|| (monatoffset < -12) || (tagoffset > 31)
					|| (tagoffset < -31))
			{
				Tracer.WriteTrace(10, "Error: offset groesse fehler <"
						+ quelldatum + "> jahroffset<" + jahroffset
						+ "> monatoffset<" + monatoffset + "> tagoffset<"
						+ tagoffset + ">");
			}

		// hier findet die Umrechnung Statt
		long hours_ms = 60 * 60 * 1000; // eine stunde hat soviel millisekunden
		offset_tag = (Long.valueOf(tagoffset)) * 24 * hours_ms;
		offset_monat = (Long.valueOf(monatoffset)) * 30 * 24 * hours_ms;
		offset_jahr = (Long.valueOf(jahroffset)) * 12 * 30 * 24 * hours_ms;

		dmil = dmil + offset_tag + offset_monat + offset_jahr;

		GregorianCalendar d1_date = new GregorianCalendar();
		d1_date.setTimeInMillis(dmil);
		dateFormatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String time_str = dateFormatter.format(d1_date.getTime());

		return (time_str);
	}

	public static String get_aktWochentag_str()
	{
		SimpleDateFormat dateFormatter;
		String dateString;
		long milisecs = get_aktdate_lo();
		GregorianCalendar d1_date = new GregorianCalendar();

		d1_date.setTimeInMillis(milisecs);
		dateFormatter = new SimpleDateFormat("E");
		dateString = dateFormatter.format(d1_date.getTime());
		return (dateString);

	}

	public static String addTimeHours(String datestring, int hours)
			throws DateExcecption
	{
		SimpleDateFormat dateFormatter;
		long dmil = get_date_milisec_lo(datestring);
		long offset = 0;

		FormatCheckTime(datestring);
		offset = hours * 60 * 60 * 1000;
		dmil = dmil + offset;

		GregorianCalendar d1_date = new GregorianCalendar();
		d1_date.setTimeInMillis(dmil);
		dateFormatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String time_str = dateFormatter.format(d1_date.getTime());

		return (time_str);
	}

	public static int zeitdifferenz_tage(String suchdatum, String kursdatum)
	{
		try
		{
			FormatCheckTime(suchdatum);
			FormatCheckTime(kursdatum);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			Tracer.WriteTrace(20, "Error: date format3<" + suchdatum
					+ "> + kursdatum<" + kursdatum + ">");
			// e.printStackTrace();
			return 0;
		}

		long dmilsuch = get_date_milisec_lo(suchdatum);
		long dmilkurs = get_date_milisec_lo(kursdatum);
		long dmildiff = Math.abs(dmilsuch - dmilkurs);
		int tagdiff = (int) (dmildiff / (1000 * 60 * 60 * 24));
		return tagdiff;
	}

	public static float zeitdifferenz_minuten(String suchdatum, String kursdatum)
	{
		if ((suchdatum.contains("null")) || (kursdatum.contains("null"))
				|| (suchdatum == null) || (kursdatum == null))
			return 9999999;

		try
		{
			FormatCheckTime(suchdatum);
			FormatCheckTime(kursdatum);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			Tracer.WriteTrace(20, "Error: date format4 <" + suchdatum
					+ "> + kursdatum<" + kursdatum + ">");
			e.printStackTrace();
		}

		long dmilsuch = get_date_milisec_lo(suchdatum);
		long dmilkurs = get_date_milisec_lo(kursdatum);
		long dmildiff = Math.abs(dmilsuch - dmilkurs);

		float minutendiff = ((float) dmildiff / (float) 60000);
		return minutendiff;
	}

	public static int zeitdifferenz_stunden(String suchdatum, String kursdatum)
	{
		if ((suchdatum == null) || (kursdatum == null))
			return 9999999;

		try
		{
			FormatCheckTime(suchdatum);
			FormatCheckTime(kursdatum);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			Tracer.WriteTrace(20, "Error: date format4 <" + suchdatum
					+ "> + kursdatum<" + kursdatum + ">");
			e.printStackTrace();
		}

		long dmilsuch = get_date_milisec_lo(suchdatum);
		long dmilkurs = get_date_milisec_lo(kursdatum);
		long dmildiff = Math.abs(dmilsuch - dmilkurs);

		int stundendiff = (int) ((float) dmildiff / (float) 3600000);
		return stundendiff;
	}

	public static Boolean CheckDateIsOlder(String datestring, int hours,
			int mins, int secs)
	{
		// prüft ob das datestring +hours+mins+secs älter als das aktuelle datum
		// ist.
		String aktdate = get_aktdatetime_str();
		long dmil_akt = get_date_milisec_lo(aktdate);
		long dmil_date = get_date_milisec_lo(datestring);

		dmil_date = dmil_date + hours * 60 * 60 * 1000 + mins * 60 * 1000
				+ secs * 1000;
		try
		{
			FormatCheckTime(datestring);
		} catch (DateExcecption e)
		{
			Tracer.WriteTrace(10, "Error: date format5 <" + datestring + ">");
			e.printStackTrace();
		}

		if (dmil_akt > dmil_date)
			return true;
		else
			return false;

	}

	public static String subTimeHours(String datestring, int hours)
			throws DateExcecption
	{
		SimpleDateFormat dateFormatter;
		long dmil = get_date_milisec_lo(datestring);

		FormatCheckTime(datestring);
		long offset = hours * 60 * 60 * 1000;

		if (offset < 0)
			Tracer.WriteTrace(10, "Error:internal überlauf offset<" + offset
					+ "> hours<" + hours + "> hours zu gross");

		dmil = dmil - offset;

		GregorianCalendar d1_date = new GregorianCalendar();
		d1_date.setTimeInMillis(dmil);
		dateFormatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String time_str = dateFormatter.format(d1_date.getTime());

		return (time_str);
	}

	public static int get_date_days(String datestring)
	{
		// berechnet die angemeldeten Tage
		if ((datestring == null) || (datestring.equals("0"))
				|| (datestring.contains(".") == false))
		{
			Tracer.WriteTrace(20, "Error: date format6 <" + datestring + ">");
			return 0;
		}
		try
		{
			FormatCheckTime(datestring);
		} catch (DateExcecption e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: date format7 <" + datestring + ">");
			return 0;
		}
		long dmil_date = get_date_milisec_lo(datestring);
		final int umrechn_i = 1000 * 60 * 60 * 24;

		// aktuelles datum ermitteln
		String aktdate = get_aktdatetime_str();
		long dmil_akt = get_date_milisec_lo(aktdate);

		long dmil_dif = dmil_akt - dmil_date;

		long umrechn_l = new Long(umrechn_i);
		int days = Math.round(dmil_dif / umrechn_l);

		return days;
	}

	public static long get_date_milisec_lo(String datestring)
	{// wandelt einen Datumsstring in milisec um
		int year = 0, month = 0, day = 0, hour = 0, min = 0, sec = 0;
		long d1_sec = 0;

		if ((datestring.contains("null")) || (datestring == null)
				|| (datestring.equals("0")))
		{
			Tracer.WriteTrace(20, "Error: date format8 <" + datestring + ">");
			return 0;
		}

		GregorianCalendar d1_date = new GregorianCalendar();

		try
		{
			year = get_year_int(datestring);
			month = get_month_int(datestring);
			day = get_day_int(datestring);
			if (datestring.length() > 10)
			{
				hour = get_hour_int(datestring, ":");
				min = get_min_int(datestring, ":");
				sec = get_sec_int(datestring, ":");
			}
			d1_date.set(Calendar.YEAR, year);
			d1_date.set(Calendar.MONTH, month - 1);
			d1_date.set(Calendar.DAY_OF_MONTH, day);
			if (datestring.length() > 10)
			{
				d1_date.set(Calendar.HOUR_OF_DAY, hour);
				d1_date.set(Calendar.MINUTE, min);
				d1_date.set(Calendar.SECOND, sec);
			}
			// d1_date.set(year, month, day);
			// d1_date.
			d1_sec = d1_date.getTime().getTime();

			return (d1_sec);
		} catch (DateExcecption d)
		{
			d.printStackTrace();
			Tracer.WriteTrace(10, "Error: date format9 <" + datestring + ">");
			return 0;
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(20, "Error: date format10 <" + datestring + ">");
			return 0;
		}

	}

	private static void FormatCheckTime(String zeile) throws DateExcecption
	{
		if (zeile == null)
			throw new DateExcecption("Error: Date/Time format:<" + zeile + ">");

		// make a date-time format check
		if (zeile.length() == 10)
			return;

		if ((zeile.length() < 6) || (zeile.length() > 19))
			throw new DateExcecption("Error: Date/Time format:<" + zeile + ">");

	}

	private static String ExtractTime(String zeile) throws DateExcecption
	{
		// extract the time of the date-time stirng
		// input:05.09.08 17:26:34
		// output:17:26:34

		FormatCheckTime(zeile);

		String sub = null;
		sub = zeile.substring(zeile.indexOf(" ") + 1);
		return sub;
	}

	public static int get_day_int(String zeile) throws DateExcecption
	{// 2008-04-22
		String sub = null;
		int ret = 0;

		FormatCheckTime(zeile);

		// 2008-04-22
		if (zeile.contains("-") == true)
		{
			try
			{
				sub = nteilstring(zeile, "-", 3);
				if (sub.contains(" "))
					sub = sub.substring(0, sub.indexOf(" "));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}
		} else if ((zeile.contains(".") == true))
		{
			try
			{
				sub = nteilstring(zeile, ".", 1);

			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}

			if (is_zahl(sub) == true)
				return (Integer.valueOf(sub));
			else
				return 0;
		} else
		{
			// 1 Apr 2001
			try
			{
				sub = nteilstring(zeile, " ", 1);

			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}
		}
		if (is_zahl(sub) == true)
			return (Integer.valueOf(sub));
		else
			return 0;

	}

	public static int get_month_int(String zeile) throws DateExcecption
	{
		String sub = null;
		int ret = 0;

		FormatCheckTime(zeile);
		// 2008-04-22
		if (zeile.contains("-") == true)
			try
			{
				sub = nteilstring(zeile, "-", 2);
				if (sub.contains(" "))
					sub = sub.substring(0, sub.indexOf(" "));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}
		else if (zeile.contains(".") == true)
		{
			try
			{
				sub = nteilstring(zeile, ".", 2);

			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block

				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}
		} else
		{
			// 1 Apr 2001
			try
			{
				sub = nteilstring(zeile, " ", 2);
				if (sub.equalsIgnoreCase("jan"))
					return 1;
				if (sub.equalsIgnoreCase("feb"))
					return 2;
				if (sub.equalsIgnoreCase("mar"))
					return 3;
				if (sub.equalsIgnoreCase("apr"))
					return 4;
				if (sub.equalsIgnoreCase("may"))
					return 5;
				if (sub.equalsIgnoreCase("jun"))
					return 6;
				if (sub.equalsIgnoreCase("jul"))
					return 7;
				if (sub.equalsIgnoreCase("aug"))
					return 8;
				if (sub.equalsIgnoreCase("sep"))
					return 9;
				if (sub.equalsIgnoreCase("oct"))
					return 10;
				if (sub.equalsIgnoreCase("nov"))
					return 11;
				if (sub.equalsIgnoreCase("dec"))
					return 12;

			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block

				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}

		}
		if (is_zahl(sub) == true)
			return (Integer.valueOf(sub));
		else
			return 0;
	}

	public static int get_year_int(String zeile) throws DateExcecption
	{
		String sub = null;
		int ret = 0;

		if (zeile == null)
			throw new DateExcecption("Error: Date/Time format null!!:" + zeile);

		FormatCheckTime(zeile);

		// 2008-04-22
		if (zeile.contains("-") == true)
			try
			{
				sub = nteilstring(zeile, "-", 1);
				if (sub.contains(" "))
					sub = sub.substring(0, sub.indexOf(" "));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}
		else if (zeile.contains(".") == true)
		{
			// 04.12.06
			try
			{
				sub = nteilstring(zeile, ".", 3);
				if (sub.contains(" "))
					sub = sub.substring(0, sub.indexOf(" "));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}

			int y = Integer.valueOf(sub);
			if (y < 1000)
			{// falls 2 stellig
				if (y < 50)
					y = y + 2000;
				else
					y = y + 1900;
			}
			sub = Integer.toString(y);
		} else
		{
			// 1 Apr 2001
			try
			{
				sub = nteilstring(zeile, " ", 3);
				if (sub.contains(" "))
					sub = sub.substring(0, sub.indexOf(" "));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new DateExcecption("Error: Date/Time format:" + zeile);
			}

			int y = Integer.valueOf(sub);
			if (y < 1000)
			{// falls 2 stellig
				if (y < 50)
					y = y + 2000;
				else
					y = y + 1900;
			}
			sub = Integer.toString(y);
		}

		if (is_zahl(sub) == true)
			return (Integer.valueOf(sub));
		else
			return 0;
	}

	public static int get_hour_int(String zeile, String trenner)
			throws DateExcecption, ToolsException
	{// 05.09.08 17:26:34
		FormatCheckTime(zeile);

		String sub = ExtractTime(zeile);

		sub = nteilstring(sub, trenner, 1);
		if (is_zahl(sub) == true)
			return (Integer.valueOf(sub));
		else
			throw new DateExcecption("Error: Date/Time format:" + zeile);
	}

	public static int get_min_int(String zeile, String trenner)
			throws DateExcecption, ToolsException
	{// 05.09.08 17:26:34
		FormatCheckTime(zeile);

		String sub = ExtractTime(zeile);

		sub = nteilstring(sub, trenner, 2);
		if (is_zahl(sub) == true)
			return (Integer.valueOf(sub));
		else
			throw new DateExcecption("Error: Date/Time format:" + zeile);
	}

	public static int get_sec_int(String zeile, String trenner)
			throws DateExcecption, ToolsException
	{// 05.09.08 17:26:34
		FormatCheckTime(zeile);

		String sub = ExtractTime(zeile);

		sub = nteilstring(sub, trenner, 3);
		if (is_zahl(sub) == true)
			return (Integer.valueOf(sub));
		else
			throw new DateExcecption("Error: Date/Time format:" + zeile);
	}

	public static boolean datum_ist_aelter_gleich(String alt, String neu)
	{
		if ((alt == null) || (neu == null))
			Tracer.WriteTrace(10, "datumsfehler datum alt<" + alt + "> neu<"
					+ neu + "> muss != null sein, sonst kein Vergleich");

		Long talt = get_date_milisec_lo(alt);
		Long tneu = get_date_milisec_lo(neu);
		if (tneu >= talt)
			return true;
		else
		{
			Tracer.WriteTrace(50, "Datumsvergleich(ist neuer= false) alt<"
					+ alt + "> neu<" + neu + "> talt<" + talt + "> tneu<"
					+ tneu + ">");
			return false;
		}
	}

	public static boolean datum_ist_aelter(String alt, String neu)
	{

		if ((alt == null) || (neu == null))
			return true;
		if ((alt.contains("null")) || (neu.contains("null")))
			return true;

		//evtl datumsanpassung
		//2009.01.02,19:48  -> 24.03.06 14:00:59 
		if((alt.contains(",")==true)&&(alt.length()==16))
			alt=Tools.convDatumKomma(alt);
		if((neu.contains(",")==true)&&(neu.length()==16))
			neu=Tools.convDatumKomma(neu);
		
		
		
		
		// falls das alte datum noch nicht gesetzt ist das neue auf jeden fall
		// neuer
		if (alt.equalsIgnoreCase("0") == true)
			return true;

		Long talt = get_date_milisec_lo(alt);
		Long tneu = get_date_milisec_lo(neu);

		// falls beide daten gleich sind
		if (alt.equalsIgnoreCase(neu) == true)
			return false;

		if (tneu > talt)
			return true;
		else
		{
			Tracer.WriteTrace(50, "Datumsvergleich(ist neuer= false) alt<"
					+ alt + "> neu<" + neu + "> talt<" + talt + "> tneu<"
					+ tneu + ">");
			return false;
		}
	}

	public static boolean datum_im_intervall(String datum, String start,
			String end)
	{

		if ((datum == null) || (start == null) || (end == null))
		{
			Tracer.WriteTrace(10, "Error Datumsfehler: datum<" + datum
					+ "> start<" + start + "> end<" + end + ">");
		}

		Long tdat = get_date_milisec_lo(datum);
		Long tstart = get_date_milisec_lo(start);
		Long tend = get_date_milisec_lo(end);

		if ((tdat >= tstart) && (tdat <= tend))
			return true;
		else
			return false;
	}

	public static int getDateInt(String refdatum, String date)
	{
		// rechnet ein Datum in integer um, also tage nach refdatum
		// also es wird die Zeitdifferenz zwischen refdatum und date in Tagen
		// ermittelt
		Long t = get_date_milisec_lo(date);
		Long reft = get_date_milisec_lo(refdatum);
		Long tdiff = t - reft;
		return (int) (tdiff / (1000 * 60 * 60 * 24));
	}

	public static String getDateString(String refdatum, int tagezahl)
	{
		// rechnet aus einer tagezahl ein Datum
		SimpleDateFormat dateFormatter;
		String dateString;

		long refmil = get_date_milisec_lo(refdatum);
		// umrechnung tage in millisekunden

		long datmil = refmil + ((long) tagezahl * 1000 * 60 * 60 * 24);

		GregorianCalendar d1_date = new GregorianCalendar();

		d1_date.setTimeInMillis(datmil);
		dateFormatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		dateString = dateFormatter.format(d1_date.getTime());
		return (dateString);
	}

	public static String entferneZeit(String datum)
	{
		if (datum == null)
			return null;
		if (datum.contains(" "))
		{
			String dat = datum.substring(0, datum.indexOf(" "));
			return new String(dat);
		} else
		{
			return datum;
		}
	}

	public static HashSet<Integer> MengenDifferenz(HashSet<Integer> menge1,
			HashSet<Integer> menge2)
	{

		HashSet<Integer> menge3 = new HashSet<Integer>();

		/* Differenz */

		menge3.clear();
		menge3.addAll(menge1);
		menge3.removeAll(menge2);
		/*
		 * System.out.print(" Differenz m1 - m2: "); for (Iterator
		 * it=menge3.iterator();it.hasNext();) System.out.print(it.next()+" ");
		 * System.out.println();
		 */

		return menge3;
	}

	public static String EncodeStringUtf8(String eingabe)
	{
		String[] umlaute =
		{ "ä", "ö", "ü", "ß", "§", "€", "½", "²" };
		String[] code =
		{ "%C3%A4", "%C3%B6", "%C3%BC", "%C3%9F", "%C2%A7", "%E2%82%AC",
				"%C2%BD", "%C2%B2" };

		// wandelt ä in %E4 um etc....

		int anz = umlaute.length;
		for (int i = 0; i < anz; i++)
			eingabe = eingabe.replaceAll(umlaute[i], code[i]);

		// filtere den : raus bei user-info.html (wo die userinfos abgeholt
		// werden)
		if (eingabe.contains("search_nick"))
		{
			String part1 = eingabe
					.substring(0, eingabe.indexOf("?search_nick"));
			String part2 = eingabe.substring(eingabe.indexOf("?search_nick"));
			eingabe = part1 + part2.replace(":", "%3A");
		}
		return eingabe;
	}

	static public String calcTargetnameSimple(int tid)
	{
		// http://www.wallstreet-online.de/diskussion/1137546-691-700/ksh-nach-dem-aktiensplit

		int postend = 1 + 9;
		String targetlink = "http://www.wallstreet-online.de/diskussion/" + tid
				+ "-" + 1 + "-" + postend;
		return targetlink;
		// http://www.wallstreet-online.de/diskussion/1137546-691-700
	}
}
