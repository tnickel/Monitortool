package stores;

import hilfsklasse.KeyReader;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.util.HashMap;

public class ObserveDBx
{
	// Observefile beinhaltet spezielle usernamen oder threads
	// wenn die datenbank nur zum lesen und mit observerflag geöffnet
	// wird dann werden nur spezielle user eingelesen. Das hat einen
	// Geschwindigkeitsvorteil.
	// Die datenbank kann anschliessend aber nicht gespeichert werden
	// man müsste vorher einen merge durchführen


	private HashMap<String,Integer>Observe = new HashMap<String,Integer>();

	public boolean GetObservefile(String inputfilename)
	{
		// hier wird die liste der zu beobachteten objekte eingelesen
		String line = null;
		String nam=null;
		int pri=0;
		KeyReader kr = new KeyReader();
		kr.SetReader(inputfilename);

		// clean
		while ((line = kr.GetLine()) != null)
		{
			
			
			// hinter der route könnnen kommentare folgen
			if (line.contains("#") == true)
			{
				int n = Tools.countZeichen(line, "#");
				if(n!=3)
					Tracer.WriteTrace(10, "def element line<"+line+"> file<"+inputfilename+">");
				
				try
				{
					nam=new String(Tools.nteilstring(line, "#", 1));
					pri=Integer.valueOf(Tools.nteilstring(line, "#", 2));
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// schaue nach ob keyword schon drin
			if(Observe.containsKey(nam)==true)
				continue;
			
			Observe.put(nam,pri);
			
		}
		System.out.println("<" + Observe.size()
				+ "> user/threads werden beobachtet");
		return true;
	}

	public boolean CheckObserve(String str)
	{
		if(Observe.containsKey(str)==true)
			return true;
		return false;
	}
	public int getPrio(String str)
	{
		if(Observe.containsKey(str)==true)
		{
			int prio=Observe.get(str);
			return prio;
		}
		else
			return 0;
	}	
}