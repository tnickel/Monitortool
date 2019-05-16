package comperatoren;

import hilfsklasse.Tools;

import java.util.Comparator;

import objects.ThreadDbObj;

public class ThreadsSliderComparator implements Comparator
{
	// sortiere die threadsdb so das vorne die alten slider stehen
	public int compare(Object o1, Object o2)
	{
		//falls o1<o2 dann gib 1 zurück
		//falls o1>o2 dann gib 0 zurück
		String lastUpdate1 = ((ThreadDbObj) o1).getLastsliderupdateSORT();
		String lastUpdate2 = ((ThreadDbObj) o2).getLastsliderupdateSORT();

		if ((lastUpdate2 == null)||(lastUpdate2.contains("null")))
		{
			
			return 1;
		}
		if ((lastUpdate1 == null)||(lastUpdate1.contains("null")))
		{
		
			return 0;
		}
		if (Tools.datum_ist_aelter(lastUpdate1, lastUpdate2)==true)
		{
			
			return 1;
		} else
		{
			
			return 0;
		}
	}

}
