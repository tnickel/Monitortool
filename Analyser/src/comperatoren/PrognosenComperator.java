package comperatoren;

import hilfsklasse.Tools;

import java.util.Comparator;

import objects.PrognoseDbObj;

public class PrognosenComperator implements Comparator
{

	public int compare(Object o1, Object o2)
	{
		// falls o1<o2 dann gib 1 zurück
		// falls o1>o2 dann gib 0 zurück
		String info1 = ((PrognoseDbObj) o1).getInfo();
		String info2 = ((PrognoseDbObj) o2).getInfo();

		// a) Neustes Datum oben
		String dat1 = ((PrognoseDbObj) o1).getAufnahmedatum();
		String dat2 = ((PrognoseDbObj) o2).getAufnahmedatum();

		if (dat1.equals(dat2) == false)
		{
			if (Tools.datum_ist_aelter(dat1, dat2) == true)
				return 1;
			else
				return -1;
		}
		// b) Threadbewertung
		if ((info1.contains(":s=") == false) && (info2.contains(":s=") == true))
		{
			return -1;
		} else if ((info1.contains(":s=") == true)
				&& (info2.contains(":s=") == false))
		{
			return 1;
		} else if ((info1.contains(":s=") == false)
				&& (info2.contains(":s=") == false))
		{

			if (((PrognoseDbObj) o1).getMittlerRang() < ((PrognoseDbObj) o2)
					.getMittlerRang())
				return -1;
			else
				return 1;
		}

		// c)Steigungsfaktor s
		String ss1 = info1.substring(info1.indexOf(":s=") + 3);
		String ss2 = info1.substring(info1.indexOf(":s=") + 3);
		float s1 = Float.valueOf(ss1);
		float s2 = Float.valueOf(ss2);
		if (s1 < s2)
			return 1;
		else
			return -1;

	}
}
