package tools;

import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;


public class Kurs1
{
	String datum=null;
	float open=0;
	float max=0;
	float min=0;
	float close=0;
	float ask=0;
	float bid=0;
	int vol=0;
	float floatvol=0;
	String dat=null;
	String uhr=null;
	
	public Kurs1(String zeile)
	{
		
		
		int anz = Tools.countZeichen(zeile, ",");

	    //Zeilentype prüfen

		//Prüft ob standdartkurszeile
		if (anz == 6)
				{
					//Standart Candle
					LeseStandartKerze(zeile);
				}
		else if(anz ==3)
		{
			//Metatrader Tickzeile		
			LeseMetatraderTickzeile(zeile);
		
		}
		else
			Tracer.WriteTrace(10, "Zeilenaufbaufehler trennerist<" + anz
					+ "> trenner erw<,> zeile<"
					+ zeile + ">");
			
		
	}
	
	private void LeseMetatraderTickzeile(String zeile)
	{
		int pos = 1;
		try
		{
			// Time,Ask,Bid,Volume
			// 2012.09.24 08:56:14,1.29048,1.29048,31
			String dattime = new String(Tools.nteilstring(zeile, ",", pos)
					.toUpperCase());

			dat=dattime.substring(0,dattime.indexOf(" "));
			uhr=dattime.substring(dattime.indexOf(" ")+1);
			
			
			pos++;
			
			bid = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			ask = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			floatvol = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			
			
			datum=dat+","+uhr;
		}
		catch (ToolsException e)
		{
			Tracer.WriteTrace(10,":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void LeseStandartKerze(String zeile)
	{
		int pos = 1;
		try
		{
			// datum, open,max,min,close,vol
		
			dat = new String(Tools.nteilstring(zeile, ",", pos)
					.toUpperCase());
			pos++;
			uhr = new String(Tools.nteilstring(zeile, ",", pos)
					.toUpperCase());
			pos++;
			
			open = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			max = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			min = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			close = Float.valueOf(Tools.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			vol = Integer.valueOf(Tools.nteilstring(zeile, ",", pos));
			
			datum=dat+","+uhr;
		}
		catch (ToolsException e)
		{
			Tracer.WriteTrace(10,":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public String getZeile()
	{
		return(datum+","+open+","+max+","+min+","+close+","+vol);
		
	}
	public String getFsbZeile()
	{
		//datum umwandeln von 2009.07.06 --> 2001-05-07
		String fsbdat =dat;
		fsbdat=fsbdat.replace(".", "-");
		fsbdat=fsbdat.replace(".", "-");
		return(fsbdat+"\t"+uhr+"\t"+open+"\t"+max+"\t"+min+"\t"+close+"\t"+vol);
		
	}
	public String getDukaTickZeile(int rand)
	{
		//Eine DukaTickzeile hat den Aufbau
		/* 
		 Time,Ask,Bid,AskVolume,BidVolume 
		 2011.01.02 22:00:26.739,1.3348,1.3345,1.5,1.5
		 */
		String out=dat+" "+uhr+"."+rand+","+ask+","+bid+","+floatvol+","+floatvol;
		
		
		return(out);
	}
	public String getDatum()
	{
		return datum;
	}

	public void setDatum(String datum)
	{
		this.datum = datum;
	}

	public float getOpen()
	{
		return open;
	}

	public void setOpen(float open)
	{
		this.open = open;
	}

	public float getMax()
	{
		return max;
	}

	public void setMax(float max)
	{
		this.max = max;
	}

	public float getMin()
	{
		return min;
	}

	public void setMin(float min)
	{
		this.min = min;
	}

	public float getClose()
	{
		return close;
	}

	public void setClose(float close)
	{
		this.close = close;
	}

	public int getVol()
	{
		return vol;
	}

	public void setVol(int vol)
	{
		this.vol = vol;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Kurs1 other = (Kurs1) obj;
		if (this.getDatum().equalsIgnoreCase(other.getDatum()) == false)
			return false;
		return true;
	}

}
