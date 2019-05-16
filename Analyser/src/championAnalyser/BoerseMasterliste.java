package championAnalyser;

import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.HashMap;

import stores.AktDB;
import stores.ChampionDB;

public class BoerseMasterliste
{
	//die bliste ist die Liste über alle Boersenblätter von Boerse De
	private ArrayList<BoerseDe> bliste = new ArrayList<BoerseDe>();
	private AktDB aktdb_glob = null;
	private ChampionDB champdb= new ChampionDB();
	
	public BoerseMasterliste(AktDB aktdbx)
	{
		aktdb_glob = aktdbx;
	}
	
	public void readAll(HashMap<Integer, String> bmap)
	{
		//Hier werden alle Börsenblätter eingelesen
		if (bliste.size() != 0)
			return;

		int anz = bmap.size();

		for (int i = 0; i < anz; i++)
		{

			String fname = bmap.get(i);
			BoerseDe bd = new BoerseDe(fname, aktdb_glob);
			bliste.add(bd);
		}
	}

	public int getanz()
	{
		return bliste.size();
	}
	public BoerseDe getBoerblattIdx(int i)
	{
		return(bliste.get(i));
	}
	
	public BoerseDe holeBoersenblatt(String fname)
	{
		int anz = bliste.size();
		for (int i = 0; i < anz; i++)
		{
			BoerseDe bd = bliste.get(i);
			if (bd.getFnameFull().equalsIgnoreCase(fname))
				return bd;
		}
		Tracer.WriteTrace(10, "Error: internal börsenblatt <"+fname+"> nicht gefunden");
		return null;
	}

	
	
	public String getVorgaengerTrend(BoerseDe boersenblatt, int caindex)
	{
		//Holt das Vorgängerbösenblatt, das ist das Blatt mir einem Datum vor 2 Wochen
		//Da die liste absteigend sortiert ist, ist dies position i+1
		String fname=boersenblatt.getFnameFull();
		
		int anz = bliste.size();
		for (int i = 0; i < anz; i++)
		{
			BoerseDe bd = bliste.get(i);
			if (bd.getFnameFull().equalsIgnoreCase(fname))
			{
				// i+1 ist der vorgaenger;
				// hole vom vorgänger den Trend
				BoerseDe bd2 = bliste.get(i+1);
				
				Championaktie ca = bd2.getChampionaktie(caindex);
				String trend = ca.getTrend();
				return trend;
			}
		}
		return "nix";

	}
}
