package comperatoren;

import hilfsklasse.Tools;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class FnamComperator implements Comparator
{
	// Der FnamComperator vergleicht das Datum von zwei Filenamen
	public int compare(Object o1, Object o2)
	{
		String s1 = o1.toString();
		String s2 = o2.toString();
		String date1=s1.substring(0,s1.indexOf("_"));
		String date2=s2.substring(0,s2.indexOf("_"));
		
		if (Tools.datum_ist_aelter_gleich(date1, date2))
			return 1;
		else 
			return -1;
		
	}
}
