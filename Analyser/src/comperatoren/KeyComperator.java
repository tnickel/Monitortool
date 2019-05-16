package comperatoren;

import java.util.Comparator;

import objects.KeyDbObj;
@SuppressWarnings("unchecked")
public class KeyComperator implements Comparator
{
	public int compare(Object o1, Object o2)
	{

		if (((KeyDbObj)o1).getHitcounter() > ((KeyDbObj)o2).getHitcounter())
			return -1;
		if (((KeyDbObj)o1).getHitcounter() < ((KeyDbObj)o2).getHitcounter())
			return 1;

		return 0;
	}
}
