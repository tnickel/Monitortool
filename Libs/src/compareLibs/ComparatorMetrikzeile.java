package compareLibs;

import java.util.Comparator;

import Metriklibs.Metrikzeile;

public class ComparatorMetrikzeile implements Comparator<Metrikzeile>
{
	@Override
	public int compare(Metrikzeile m1, Metrikzeile m2)
	{
		String name1 = m1.holeEntry(0).getValue();
		String name2 = m2.holeEntry(0).getValue();
		
		name1 = name1.replace("Strategy", "");
		name2 = name2.replace("Strategy", "");
		name1 = name1.replace(" ", "");
		name2 = name2.replace(" ", "");
		name1 = name1.replace(".", "");
		name2 = name2.replace(".", "");
		
		
	
		 return name1.compareToIgnoreCase(name2);
		 
	}
}
