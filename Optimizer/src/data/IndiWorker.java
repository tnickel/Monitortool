package data;

import hiflsklasse.InfFast;

public class IndiWorker extends IndiGrundfunkt
{
	// diese Klasse arbeitet mit dem Indikator
	// ist im prinzip ne hilfsklasse
	// hier wird der indikatorsourcecode eingelesen und teile davon angezeigt
	private static InfFast inf_glob = null;
	public String zeilenspeicher[] = new String[20000];
	

	public IndiWorker(String filename)
	{
		inf_glob = new InfFast();
		inf_glob.setFilename(filename);
		readMemFile();
	}

	public void readMemFile()
	{
		// die datei in den speicher lesen
		int n = 0;
		StringBuffer zeile = null;
		while ((zeile = inf_glob.readZeile()) != null)
		{

			zeilenspeicher[n] = new String(zeile);
			n++;
		}
		return;
	}

	public String getTextSection(String kennung)
	{
		// suche bool Filter_0009b
		
		
		int startzeile = -1;
		int endzeile = -1;
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile != null)
				if (zeile.contains("bool Filter_" + kennung) == true)
				{
					startzeile = i;
					continue;
				}
			if (zeile != null)
				if ((startzeile>0)&&(zeile.contains("}") && (zeile.length()<2)))
						{
							endzeile=i;
							break;
						}	
		}
		String resultzeile="";
		for(int i=startzeile; i<=endzeile;i++)
		{
			String zeile = zeilenspeicher[i];
			resultzeile=resultzeile.concat(zeile+"\n");
		}
		return resultzeile;
	}


	
}