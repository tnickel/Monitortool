package sq4xWorkflow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hiflsklasse.Tracer;

public class SqDate
{
	//klasse für die datumsverarbeitung
	private String date1_g=null,date2_g=null;
	private String keyword_g="Setup";//kann Range oder Setup heissen
	
	SqDate(String datestring)
	
	{
		//Der Setupstring kann Range oder Setup sein
		//datestring=<Range dateFrom="2017.03.18" dateTo="2018.03.13" />
		//datestring=<Setup dateFrom="2003.5.5" dateTo="2018.03.19"
		if(datestring.contains("Range"))
			keyword_g="Range";
		
		date1_g=datestring.substring(datestring.indexOf("<"+keyword_g+" dateFrom=\"")+17,datestring.indexOf("\" "));
		date2_g=datestring.substring(datestring.indexOf("dateTo=")+8,datestring.lastIndexOf("\""));
		Tracer.WriteTrace(20, "date1<"+date1_g+"> date2<"+date2_g+">");
	}
	//hier werden n tage vom datum abgezogen
	public String StringGetModDateString(int subdays)
	{
		//original=<Setup dateFrom="2003.5.5" dateTo="2018.03.19"
		//returned=<Setup dX@XFrom="2003.5.5" dateTo="2018.03.19"
		//ausserdem werden vom Datum jeweils n monate abgezogen, für n=2 sieht das so aus
		//returned=<Setup dX@XFrom="2003.3.5" dateTo="2018.01.19"
		
		String d1=new String(calcMonths(date1_g, subdays));
		String d2=new String(calcMonths(date2_g, subdays));
		
		return("\"<"+keyword_g+" dX@XFrom=\""+d1+"\" dateTo=\""+d2+"\"");
	
	}
	
	//interne funktion
	private String calcMonths(String datestring,int subndays)
	{
		Date date=null;
		//wandelt string in date um
		DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		try {
		  date = formatter.parse(datestring);
		} catch (ParseException e) {
		  e.printStackTrace();
		  Tracer.WriteTrace(10, "date format error");
		}
		
		//zeit setzen;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		//n tage abziehen
		c.add(Calendar.DAY_OF_MONTH, -subndays);
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		System.out.println(df.format(c.getTime()));
		return((String)df.format(c.getTime()));
	}
	static public String  replaceBack(String mem)
	{
		
		return(mem.replace("X@X", "ate"));
		
	}
}
