package data;

import hiflsklasse.Tracer;

import java.util.ArrayList;
import java.util.Collections;

public class Metatraderlist
{
	private ArrayList<Metaconfig> metaliste = new ArrayList<Metaconfig>();

	public void deleteelem(String name)
	{
		int ind = findIndex(name);
		if (ind < 0)
			Tracer.WriteTrace(10, "Error: broker element<" + name
					+ "> nicht gefunden");
		metaliste.remove(ind);

	}

	private int findIndex(String name)
	{
		int anz = metaliste.size();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mconf = metaliste.get(i);
			if (mconf.brokername.equals(name))
				return i;

		}
		return -1;
	}

	public void changeItems(int pos1, int pos2)
	{

		Metaconfig mconf1 = metaliste.get(pos1);
		Metaconfig mconf2 = metaliste.get(pos2);

		metaliste.set(pos2, mconf1);
		metaliste.set(pos1, mconf2);
	}

	public void addelem(Metaconfig elem)
	{
		metaliste.add(elem);
	}

	public Metaconfig getelem(int index)
	{
		return (metaliste.get(index));
	}

	public void sortliste()
	{
		Collections.sort(metaliste);
	}

	public int getsize()
	{
		return (metaliste.size());
	}

	public void clear()
	{
		metaliste.clear();
	}

	public Metaconfig getMetaconfig(String expertdirectory)
	{
		// sucht sich die Konfig
		int anz = metaliste.size();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig metc = metaliste.get(i);

			if (metc.getMqldata() != null) // mt600+
			{
				if (metc.getMqldata().equalsIgnoreCase(expertdirectory) == true)
					return metc;
			} else
			// mt5xx alte version
			{
				if (metc.getNetworkshare_INSTALLDIR().equalsIgnoreCase(
						expertdirectory) == true)
					return metc;
			}
		}
		return null;
	}

	public Metaconfig getMetaconfigByBrokername(String expertname)
	{
		// sucht sich die Konfig
		int anz = metaliste.size();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig metc = metaliste.get(i);
			if (metc.getBrokername().equalsIgnoreCase(expertname) == true)
				return metc;
		}
		return null;
	}
	public Metaconfig getMetaconfigByIndex(int index)
	{
		// sucht sich die Konfig
		int anz = metaliste.size();
		
		if(index>anz)
			Tracer.WriteTrace(10, "E: internal index to big");

		Metaconfig metc = metaliste.get(index);
		return metc;
		
		
	}
}
