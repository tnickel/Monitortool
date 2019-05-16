package comperatoren;

import java.util.Comparator;

import objects.UserDbObj;

@SuppressWarnings("unchecked")
public class UserComperatorRangpoints implements Comparator
{
		
	// Wird für das Sortieren verwendet
	public int compare(Object o1, Object o2)
	{
		int rp1=((UserDbObj)o1).getRangpoints();
		int rp2=((UserDbObj)o2).getRangpoints();
		
		

		if (rp1 > rp2)
			return -1;
		if (rp1 < rp2)
			return 1;

		return 0;
	}
}
