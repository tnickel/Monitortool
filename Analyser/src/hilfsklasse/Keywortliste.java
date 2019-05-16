package hilfsklasse;

import java.util.ArrayList;

public class Keywortliste
{
	private String trenner_glob = null;
	public ArrayList<String> keylist = new ArrayList<String>();

	public Keywortliste(String trenner)
	{
		trenner_glob = trenner;
	}

	public void addFile(String nam)
	{
		// hier wird das keylist array anhand eines files auf platte aufgebaut
		String zeile = null;
		Inf inf = new Inf();
		inf.setFilename(nam);

		// jede Zeile darf ein Schlüsselwort beinhalten
		while ((zeile = inf.readZeile()) != null)
		{
			keylist.add(zeile);
		}
		inf.close();
	}

	public boolean addElem(String elems)
	{
		//ein Element hinzufügen
		if(elems.contains(trenner_glob)==false)
		{
			keylist.add(elems);
			return true;
		}
		
		//auseinanderstückeln und addn
		int anz=SG.countZeichen(elems, trenner_glob);
		
		for(int i=1; i<anz+2; i++)
		{
			
			try
			{
				String nelem=SG.nteilstring(elems, trenner_glob,i);
				keylist.add(nelem);
			}catch(Exception e)
			{
			
			
				e.printStackTrace();
				return false;
			}
				
		}
		return true;
		
	}

	public int getanz()
	{
		return keylist.size();
	}

	public String getObjIdx(int index)
	{
		return keylist.get(index);
	}
}
