package comperatoren;

import java.util.Comparator;

import objects.ThreadDbObj;

public class ThreadSteigungsfaktorComparator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		//falls o1<o2 dann gib 1 zurück
		//falls o1>o2 dann gib 0 zurück
		float faktor1 = ((ThreadDbObj) o1).getSortkriterium_steigungsfaktor();
		float faktor2 = ((ThreadDbObj) o2).getSortkriterium_steigungsfaktor();

		if(faktor1==0)
		{
			return 1;
		}
		if (faktor2==0)
		{
			return 0;
		}
		if (faktor1<faktor2)
		{
			return 1;
		} else
		{
			return 0;
		}
	}
}
