package comperatoren;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.Comparator;

import objects.ThreadTable;

public class TdboSortComp implements Comparator
{
	private static int colpos_glob = 2;

	public void setColpos(int i)
	{
		// 0|Pos#1|Threadname#2|tid#3|lastAttr.Date#4|nUser#5|+User#
		// 6|-User#7|Sl20_3T#8|Sl20akt#9|Sl20steig#10|Threadfullname";
		// 11|prio#12|slastupdate";
		colpos_glob = i;
		System.out.println("index =<" + colpos_glob + "> gesetzt");
	}

	public int compare(Object o1, Object o2)
	{
		// falls o1<o2 dann gib 1 zurück
		// falls o1>o2 dann gib 0 zurück

		// Lastdate 3
		if (colpos_glob == 3)
		{

			String lastUpdate1 = ((ThreadTable) o1).getAttLastdate();
			String lastUpdate2 = ((ThreadTable) o2).getAttLastdate();

			if ((lastUpdate2 == null) || (lastUpdate2.contains("null")))
				return 1;
			if ((lastUpdate1 == null) || (lastUpdate1.contains("null")))
				return 0;

			if (Tools.datum_ist_aelter(lastUpdate1, lastUpdate2) == true)

				return 1;
			else
				return 0;
		}
		// Anz neuer User
		else if (colpos_glob == 4)
		{
			float anz1 = ((ThreadTable) o1).getAnzneuUser();
			float anz2 = ((ThreadTable) o2).getAnzneuUser();
			if (anz1 < anz2)
				return 1;
			else
				return 0;
		}
		// anz neuer gute User
		else if (colpos_glob == 5)
		{
			float anz1 = ((ThreadTable) o1).getAnzguteuser();
			float anz2 = ((ThreadTable) o2).getAnzguteuser();
			if (anz1 < anz2)
				return 1;
			else
				return 0;
		}
		// Slider3Tage groesse
		else if (colpos_glob == 7)
		{
			float anz1 = ((ThreadTable) o1).getSlider20t3groesse();
			float anz2 = ((ThreadTable) o2).getSlider20t3groesse();
			if (anz1 < anz2)
				return 1;
			else
				return 0;
		}
		// slider 20groesse
		else if (colpos_glob == 8)
		{
			float anz1 = ((ThreadTable) o1).getSlider20groesse();
			float anz2 = ((ThreadTable) o2).getSlider20groesse();
			if (anz1 < anz2)
				return 1;
			else
				return 0;
		}
		// Slider Steigung
		else if (colpos_glob == 9)
		{
			float val1 = ((ThreadTable) o1).getSlider20Steigung();
			float val2 = ((ThreadTable) o2).getSlider20Steigung();
			if (val1 < val2)
				return 1;
			else
				return 0;
		} 
		//prio
		else if (colpos_glob == 11)
		{
			float val1 = ((ThreadTable) o1).getPrio();
			float val2 = ((ThreadTable) o2).getPrio();
			if (val1 < val2)
				return 1;
			else
				return 0;
		} 
		else if (colpos_glob == 12)
		{
		
		String lastUpdate1 = ((ThreadTable) o1).getLastSliderUpdate();
		String lastUpdate2 = ((ThreadTable) o2).getLastSliderUpdate();

		if ((lastUpdate2 == null) || (lastUpdate2.contains("null")))
			return 1;
		if ((lastUpdate1 == null) || (lastUpdate1.contains("null")))
			return 0;

		if (Tools.datum_ist_aelter(lastUpdate1, lastUpdate2) == true)

			return 1;
		else
			return 0;
		}
		else
			Tracer.WriteTrace(10, "Error: internal index colpos<" + colpos_glob
					+ "> not implemented");
		return 0;
	}
}
