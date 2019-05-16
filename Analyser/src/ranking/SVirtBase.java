package ranking;

import hilfsklasse.Tools;
import db.CompressedPostingObj;

public class SVirtBase
{
	String lastdate=null;
	
	public boolean istNeuesDatum(CompressedPostingObj copost)
	{
		//a)falls lastdate==0 dann => false (Da anfang)
		//b)falls lastdate!=0 dann schaue ob datum != lastdate
		//b1) falls datum != lastdate => true
		//b2) falls datum ==lastdate =>false
		
		String date = Tools.entferneZeit(copost.getDatetime());
		// falls datum schon mal gesehen, dann wurde heute schon ausgefuehrt

		//a)
		if(lastdate==null)
		{
			lastdate=date;
			return true;
		}
		//b2) datum ist gleich
		if (date.equals(lastdate) == true)
			return false;
		else
		{ // b1) datum ist neu
			lastdate = date;
			return true;
		}
	}
}
