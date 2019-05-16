package data;

import hiflsklasse.SG;
import hiflsklasse.ToolsException;

import java.util.TreeSet;

public class OneMagiclist
{
	//dies ist die übergeordnete magic
	protected int obermagic=0;
	//magicmenge
	TreeSet<Integer> magicmenge = new TreeSet<Integer>();
	
	protected OneMagiclist()
	{
		
	}
	
	protected OneMagiclist(String list)
	{
		//172=501,502,503,504
		
		if(list.contains("=")== false)
			return;
		String first=list.substring(0,list.indexOf("="));
		String rest=list.substring(list.indexOf("=")+1);
		
		obermagic=SG.get_zahl(first);
		
		int anz=SG.countZeichen(rest, ",");
		for(int i=0; i<=anz; i++)
		{
			int zahl=0;
			try
			{
				zahl = SG.get_zahl(SG.nteilstring(rest, ",", i+1));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			magicmenge.add(zahl);
		}
		
	}
	
	protected int holeObermagic(int magic)
	{
		//es wird geprüft ob Magic in der Menge der ersetzungsmagics ist
		//wenn ja wird die obermagic zurückgegeben
		
		if(magicmenge.contains(magic))
			return obermagic;
		else
			return -1;
	}
	
}
