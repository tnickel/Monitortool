package Metriklibs;

import java.util.Locale;

import hiflsklasse.Tracer;

public class Metrikentry
{
	// 0= nicht aktiv, 1=string, 2=float
	int attributflag = 0;
	String attributname = null;
	String value = null;
	
	public String getAttributName()
	{
		return attributname;
	}
	
	public void setAttributName(String attributname)
	{
		this.attributname = attributname;
	}
	
	public String getValue()
	{
		if(value==null)
			Tracer.WriteTrace(10, "E: nullstring 1100");
		
		if(value.equals("N/A")==true)
			return ("0.0");
		
		// Überprüfe, ob der Wert "Strategy" enthält
		if (value.contains("Strategy"))
		{
			return value;
		} else
		{
			try
			{
				// Versuche, den String in ein Double zu konvertieren
				
				double v = Double.valueOf(value);
				
				// Formatiere das Double auf zwei Dezimalstellen mit Punkt als
				// Dezimaltrennzeichen
				String retstring = String.format(Locale.US, "%.2f", v);
				return retstring;
			} catch (NumberFormatException e)
			{
				// Wenn eine Ausnahme auftritt, gib den Originalwert oder einen geeigneten
				// Fehler zurück
				Tracer.WriteTrace(10, "E:Invalid number format string<"+value+">"); 
				return null;
			}
		}
	}
	
	public void setValue(String strvalue)
	{
		
		this.value = new String(strvalue.replace("\"", ""));
		
		try
		{
			float a = Float.valueOf(value);
			attributflag = 2; // zahl ist float
		} catch (NumberFormatException e)
		{
			if (value.length() > 0)
				attributflag = 1; // zahl ist string
		}
		
	}
	
	public int getAttributflag()
	{
		return attributflag;
	}
	
	public void setAttributflag(int attributflag)
	{
		this.attributflag = attributflag;
	}
	
}
