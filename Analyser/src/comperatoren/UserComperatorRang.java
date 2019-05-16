package comperatoren;

import java.util.Comparator;

import objects.UserDbObj;

@SuppressWarnings("unchecked")
public class UserComperatorRang implements Comparator
{
		
	// Wird für das Sortieren verwendet
	public int compare(Object o1, Object o2)
	{
		int r1=((UserDbObj)o1).getRang();
		int r2=((UserDbObj)o2).getRang();
		
		

		if (r1 > r2)
			return 1;
		if (r1 < r2)
			return -1;

		return 0;
	}
}
