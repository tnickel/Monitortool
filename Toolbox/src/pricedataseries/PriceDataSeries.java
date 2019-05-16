package pricedataseries;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.util.Date;

import tools.Kurs1;
import zeitmessung.Zeitmessung;
import datefunkt.Mondate;

public class PriceDataSeries
{
	PriceDataSeries()
	{}
	static public void PriceGmtConvertion(int gmt, String strfile,String destfile)
	{
		//line of SQ data
		//1986.12.01,01:00,1.0278,1.0278,1.0278,1.0278,4
		//load file line by line
		//convert line by line
		//save file line by line
		//make the timeshift
		
		String line=null;
		
		Inf inf=new Inf();
		inf.setFilename(strfile);
		File dfile=new File(destfile);
		if(dfile.exists())
			dfile.delete();

		Zeitmessung z=new Zeitmessung(1);
		
		Inf ouf=new Inf();
		ouf.setFilename(destfile);
		while(5==5)
		{
			
			line=inf.readZeile();
			
			if(line==null)
				break;
			Kurs1 k1= new Kurs1(line);
		
			String datstring=k1.getDatum();
			
			//convert sting of the inputfile into a date
			Date d1=Mondate.convTradezeitFormat(datstring, "yyyy.MM.dd,HH:mm");
			Long time_long=d1.getTime();
			Long timediff=((long)gmt*3600*1000);
			//time correction
			time_long=time_long+timediff;
			
			//reconvert date to the datestring for the outputfile
			String outstring=Mondate.reconvertTradezeitFormat(time_long,"yyyy.MM.dd,HH:mm");
			k1.setDatum(outstring);
			
			ouf.writezeile(k1.getZeile());
			
		}
		inf.close();
		ouf.close();
		
	}
}
