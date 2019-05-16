package comperatoren;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.Comparator;

import objects.UserDbGewinnZeitraumObjI;
@SuppressWarnings("unchecked")
public class BesteAlgo implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		String date1=((UserDbGewinnZeitraumObjI)o1).getStartdatum();
		String date2=((UserDbGewinnZeitraumObjI)o2).getStartdatum();
		
		if ((date1 == null) || (date2 == null))
			Tracer.WriteTrace(10, this.getClass().getName()
					+ "ERROR: No date comp possible unam<" + date1
					+ "> rp1<" + date1 + "> unam<" + date2
					+ "> rp2<"+date2+">");
		
		if (Tools.datum_ist_aelter_gleich(date1, date2))
			return -1;
		else 
			return 1;
	}
}
