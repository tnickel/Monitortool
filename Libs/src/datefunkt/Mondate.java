package datefunkt;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mondate
{
	public static SimpleDateFormat getSimpleDateFormat()
	{
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S" );
		return df;
	}
	static public long SekundenVergangen(Date lasttime)
	{
		//prüft ob die letzte ausschaltung schon lange genug her ist
		
		Date akttime= new java.util.Date();
		long zeitdiff=akttime.getTime()-lasttime.getTime();
		long sekundendiff=Math.round(zeitdiff/1000);
		return sekundendiff;
	}
	static public long Zeitdiff(Date time1, Date time2)
	{
		
		long zeitdiff=Math.abs(time1.getTime()-time2.getTime());
		long sekundendiff=Math.round(zeitdiff/1000);
		return sekundendiff;
	}
	static public Date convTradezeit(String datum)
	{
		 //2013-04-11 18:13:31
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     	 try
		{
			return(sdf.parse(datum));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	 return null;
	}
	
	static public Date convTradezeitFormat(String datum,String formatstring)
	{
		 //2013-04-11 18:13:31
		 SimpleDateFormat sdf = new SimpleDateFormat(formatstring);
     	 try
		{
			return(sdf.parse(datum));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	 return null;
	}
	static public String reconvertTradezeitFormat(Long ld,String formatstring)
	{
		Date d= new Date();
		d.setTime(ld);
		
		//konvertiert das datum wieder in einem string
		//Ziel: 2012.09.24 08:56:14
		SimpleDateFormat df = new SimpleDateFormat( formatstring );
		String reportDate = df.format(d);
		return reportDate;
	}
	
	static public Date getAktDate()
	{
		return( new Date());
	}
	
	static public int Filealterhour(String fnam)
	{
		File fi= new File(fnam);
		Date filedate = new Date(fi.lastModified());
		Date aktdate=(new Date());
		long zeitdiff=Zeitdiff(filedate,aktdate);
		int stunden=(int)(zeitdiff/3600);
		return stunden;
	}
	static public int FilealterSec(String fnam)
	{
		File fi= new File(fnam);
		Date filedate = new Date(fi.lastModified());
		Date aktdate=(new Date());
		long zeitdiff=Zeitdiff(filedate,aktdate);
		int sekunden=(int)(zeitdiff);
		return sekunden;
	}
	
}
