package stores;

import hilfsklasse.KeyReader;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.util.HashMap;

public class BoostraengeDBx
{
	private int anz_g = 0;
	private HashMap<String, Integer> boostmap_g = new HashMap<String, Integer>();
	

	public boolean getBoosterfile(String inputfilename)
	{
		// hier wird die liste der zu beobachteten objekte eingelesen
		String zeile = null,name=null;
		int value = 0;
		KeyReader kr = new KeyReader();
		kr.SetReader(inputfilename);

		// clean
		while ((zeile = kr.GetLine()) != null)
		{
			try
			{
				name = new String(SG.nteilstring(zeile, "#", 1));
				value = Integer.valueOf(SG.nteilstring(zeile, "#", 2));
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// schaue nach ob keyword schon drin
			if (boostmap_g.containsKey(name) == false)
			{
				boostmap_g.put(name, value);
			} else
				Tracer.WriteTrace(20, "Warning: Keyword<" + name
						+ "> doppelt in file<" + inputfilename + ">");
		}
		return true;
	}

	public int getBoosterVal(String name)
	{
		// schaut nach ob für den User ein Booster definiert ist,
		// wenn ja wird der value zurückgeliefert
		
		
		
		if(boostmap_g.containsKey(name)==true)
		{
			return (boostmap_g.get(name));
		}
		return 0;
	}
}
