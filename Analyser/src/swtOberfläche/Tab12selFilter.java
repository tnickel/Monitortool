package swtOberfläche;

import hilfsklasse.Tools;
import objects.EventDbObj;

public class Tab12selFilter
{
	String Startdate=null;

	public String getStartdate()
	{
		return Startdate;
	}

	public void setStartdate(String startdate)
	{
		Startdate = startdate;
	}
	public Boolean check(EventDbObj eobj)
	{
		//true: der Filter sagt das was angzeigt wird
		if((Startdate==null)||(Startdate.equals("")))
				return true;
		
		//im Zweifellsfall true
		if(eobj==null)
			return true;
		
		String date=eobj.getAusloesedate();
		
		if(Tools.datum_ist_aelter_gleich(Startdate, date))
			return true;
		else
			return false;
	}
}
