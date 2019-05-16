package stores;

import hilfsklasse.Tracer;

import java.util.HashSet;

import objects.AktDbObj;
import objects.Obj;
import db.DB;

public class AktDB extends DB
{
	private HashSet<Integer> tidmengecheck = new HashSet<Integer>();
	
	
	public AktDB()
	{
		tidmengecheck.clear();
		Tracer.WriteTrace(20, "Info:Kontstruktor AktDB");
		LoadDB("aktdb", null, 0);

	}
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		//Dies ist ein plausicheck der bei der neuaufnahme von
		//neuen objekten durchgeführt wird
		
		AktDbObj aktneu=(AktDbObj)obj;

		
		int tidneu=aktneu.getThreadid();
		
		
			
		if(tidmengecheck.contains(tidneu)==true)
		{
			Tracer.WriteTrace(20, "Error: aktdb.db inkonsistenz, möchte tid<"+tidneu+"> aufnehmen, aber schon vorhanden =>drop it");
			return false;
		}
		tidmengecheck.add(tidneu);
		
		return true;
	}
	public int SearchSymbIDX(String symb)
	{
		// sucht ein Symbol und gibt den index oder -99 zurück
		int maxcounter = GetanzObj();

		for (int i = 0; i < maxcounter; i++)
		{
			AktDbObj aktdbobj = (AktDbObj) GetObjectIDX(i);
			if (aktdbobj.getSymbol().equals(symb) == true)
				return (i);

		}
		return -99;
	}
	public int SearchWknIDX(String wkn)
	{
		// sucht eine wkn und gibt den index oder -99 zurück
		int maxcounter = GetanzObj();

		for (int i = 0; i < maxcounter; i++)
		{
			AktDbObj aktdbobj = (AktDbObj) GetObjectIDX(i);
			if (aktdbobj.getWkn().equals(wkn) == true)
				return (i);

		}
		return -99;
	}
	public AktDbObj getWknIDX(String wkn)
	{
		// sucht eine wkn und gibt den index oder -99 zurück
		int maxcounter = GetanzObj();

		for (int i = 0; i < maxcounter; i++)
		{
			AktDbObj aktdbobj = (AktDbObj) GetObjectIDX(i);
			if (aktdbobj.getWkn().equals(wkn) == true)
				return aktdbobj;

		}
		return null;
	}
	public AktDbObj SearchSymbObj(String symb)
	{
		// sucht ein Symbol und gibt den index oder -99 zurück
		int maxcounter = GetanzObj();

		for (int i = 0; i < maxcounter; i++)
		{
			AktDbObj aktdbobj = (AktDbObj) GetObjectIDX(i);
			if (aktdbobj.getSymbol().equals(symb) == true)
				return aktdbobj;

		}
		return null;
	}
	
	
	
	
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public void postprocess()
	{}
}
