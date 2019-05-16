package comperatoren;

import hilfsklasse.Tools;

import java.util.Comparator;

import kurse.KursvalueNativeDbObj;


@SuppressWarnings("unchecked")
public class KursvalueNativeComperator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		String d1=((KursvalueNativeDbObj)o1).getDate();
		String d2=((KursvalueNativeDbObj)o2).getDate();
		
		if(Tools.datum_ist_aelter(d1, d2))
			return 1;
		else
			return -1;

	}
}
