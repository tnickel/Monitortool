package comperatoren;

import java.util.Comparator;

import objects.UserGewStrategieObjII;

@SuppressWarnings("unchecked")
public class StabilComparator implements Comparator
{
	// Wird für das Sortieren verwendet
	public int compare(Object o1, Object o2)
	{
		float rp1=((UserGewStrategieObjII)o1).getStabilfaktor_g();
		float rp2=((UserGewStrategieObjII)o2).getStabilfaktor_g();
		
		if(rp1==0)
			return 1;
		if(rp2==0)
			return -1;

		if (rp1 > rp2)
			return -1;
		if (rp2 < rp1)
			return 1;

		return 0;
	}
}
