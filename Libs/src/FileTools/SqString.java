package FileTools;

import hiflsklasse.Tracer;

public class SqString
{
	private int lastpos = 0;
	private int count = 0;
	protected String suchwort1=null, suchwort2=null;
	
	public String getSuchwort(String mem)
	{
		int index1 = mem.indexOf(suchwort1);
		if (index1 < 0)
			return null;
		int index2 = mem.indexOf(suchwort2, index1 + suchwort1.length());
		if (index2 == 0)
			return null;
		String suchwort = mem.substring(index1 + suchwort1.length(), index2);
		Tracer.WriteTrace(20, "suchwort=" + suchwort);
		return suchwort;
	}
	
	public String getNextSuchwort(String mem)
	{
		try
		{
			int index1 = mem.indexOf(suchwort1, lastpos);
			if (index1 == 0)
				return null;
			
			int index2 = mem.indexOf(suchwort2, index1 + suchwort1.length());
			if (index2 == 0)
				return null;
			lastpos = index2 + suchwort2.length();
			String suchwort = mem.substring(index1, index2);
			count++;
			if (suchwort.length() > 200)
				return null;
			Tracer.WriteTrace(20, "count<" + count + "> lastpos<" + lastpos + ">suchwort=" + suchwort);
			return suchwort;
			
		} catch (java.lang.StringIndexOutOfBoundsException e)
		{
			return null;
		}
	}

	protected void ResetCounter()
	{
		lastpos=0;
		count=0;
	}
	
}
