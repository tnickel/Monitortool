package data;

import hiflsklasse.SG;
import hiflsklasse.ToolsException;

import java.util.ArrayList;

public class Magiclist extends OneMagiclist
{
	ArrayList<OneMagiclist> magiclist= new ArrayList<OneMagiclist>();
	public Magiclist(String gesstring)
	{
		
		System.out.println("parse alles");
		int i=0;

		if(gesstring.endsWith("\n")==false)
			gesstring=gesstring+"\n";
		
		int anz=SG.countZeichen(gesstring, "\n");
		
		while(i<anz)
		{
			i++;
			try
			{
				String teilstring = SG.nteilstring(gesstring, "\n", i);
				teilstring=teilstring.replace("\r", "");
				System.out.println(teilstring);
				OneMagiclist om= new OneMagiclist(teilstring);
				magiclist.add(om);
				
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public int getObermagic(int magic)
	{
		//hier wird geprüft ob die magic eine entsprechende ersatzmagic besitzt
		
		
		//gehe durch sämtliche magicmengen und prüfe nach ersatzmagic
		int anz=magiclist.size();
		for(int i=0; i<anz; i++)
		{
			//betrachte eine magicmenge
			OneMagiclist om= magiclist.get(i);
			int obermagic=om.holeObermagic(magic);
			
			//diese magic hat eine obermagic, dann gib die zurück
			if(obermagic>=0)
				return obermagic;
		}
		//nix gefunden
		return -1;
	}
	
}
