package sq4xWorkflow;

import hiflsklasse.Tracer;

public class SqConfXml extends SqString
{
	//klasse um das xml zu verarbeiten
	private String mem_g=null;
	
	SqConfXml(String mem)
	{ 
		mem_g=mem;
	}
	
	public void setSearchpattern(String pattern1, String pattern2)
	{
		ResetCounter();
		super.suchwort1=pattern1;
		super.suchwort2=pattern2;
	}
	
	public String getProjectName()
	{
		//Suche <Project name="EURUSD - H1 -2year NEW" version="126.2189">
		//Rückgabewert=EURUSD - H1 -2year NEW
		String projname=super.getSuchwort(mem_g);
		return projname;
	}
	
	
	
	public String modifyAllPatterns(int days)
	{
		//suche <Setup dateFrom="2003.5.5" dateTo="2018.03.19" testPrecision="2"
		//Rückgabewert="2003.5.5" dateTo="2018.03.19"

		while(5==5)
		{
			String setupdate=super.getNextSuchwort(mem_g);
			if(setupdate==null)
				break;
			SqDate sd=new SqDate(setupdate);
			String moddate=sd.StringGetModDateString(30);
			mem_g=mem_g.replace(setupdate, moddate);
		}
	
		return mem_g;
	}
	
	
	public void ResetCounter()
	{
		super.ResetCounter();
	}
	
	
	
}
