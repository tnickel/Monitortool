package hilfsklasse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TDate
{
	static SG sg = new SG();

	Calendar calobj = null;

	public TDate()
	{
		// aktuelles Datum setzen
		calobj = new GregorianCalendar();

	}

	public String GetaktDatestring()
	{
		SimpleDateFormat dateFormatter;
		String dateString;

		String formater = "yyyy-MM-dd";
		dateFormatter = new SimpleDateFormat(formater);
		dateString = dateFormatter.format(calobj.getTime());
		return (dateString);

	}

	private Calendar genCalenderobj(String datum)
	{
		// generiert ein Callenderobjekt
		Calendar cal = new GregorianCalendar();
		try
		{
			int year = Tools.get_year_int(datum);
			int month = Tools.get_month_int(datum);
			int day = Tools.get_day_int(datum);
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DAY_OF_MONTH, day);
			return cal;
		} catch (DateExcecption d)
		{
			d.printStackTrace();
			Tracer.WriteTrace(10, this.getClass().getSimpleName()
					+ "Error: date format <" + datum + ">");
			return null;
		}

	}

	private long holeMiliSekunden()
	{
		// holt aus dem tdate die milisekunden
		return (calobj.getTime().getTime());
	}

	public TDate(String datum)
	{
		//1955-01-01
		
		if(datum.contains(",00"))
			datum=datum.substring(0,datum.indexOf(",00"));
		
		if (datum == null)
		{
			calobj = genCalenderobj("1955-01-01");
			return;
		}
		calobj = genCalenderobj(datum);
	}

	public int Tdiff(TDate vergleichdat, String flag)
	{
		// gibt die zeitdifferenz in tagen an
		// flag = "t" tage
		// flag = "m" minuten
		// flag = "s" sekunden

		if (flag.equals("t") == false)
			Tracer.WriteTrace(10, this.getClass().getSimpleName()
					+ "Error: Nur Tage erlaubt keyword 't' ");

		long difftime = vergleichdat.holeMiliSekunden()
				- this.holeMiliSekunden();
		int retdays = (int) Math.round(difftime / (24. * 60. * 60. * 1000.));

		return retdays;
	}

	public int getVal(String formater)
	{
		// holt aus dem datum einen bestimmten Wert
		// z.B. Formater = y,m,d,wo
		if (formater.equals("y"))
			return (calobj.get(Calendar.YEAR));
		if (formater.equals("m"))
			return (calobj.get(Calendar.MONTH) + 1);
		if (formater.equals("d"))
			return (calobj.get(Calendar.DAY_OF_MONTH));
		if (formater.equals("wo"))
			return (calobj.get(Calendar.DAY_OF_WEEK));
		Tracer.WriteTrace(10, this.getClass().getSimpleName()
				+ "Error: formater <" + formater + ">");
		return 0;
	}

}
