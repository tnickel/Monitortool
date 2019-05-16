package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;

import java.util.ArrayList;
import java.util.Collections;

import objects.ThreadDbObj;

public class Rank extends Object
{
	ArrayList<ThreadStatObj> rankingliste = new ArrayList<ThreadStatObj>();

	public static boolean isGuterUser(int rank)
	{
		if (rank < 1000)
			return true;
		else
			return false;
	}

	public static boolean isSchlechterUser(int rank)
	{
		if(rank>25000)
			return true;
		else
			return false;
	}

	public static boolean isBadUser(int rank)
	{
		if(rank<0)
			return true;
		else
			return false;
	}

	public void add(ThreadStatObj elem)
	{
		rankingliste.add(elem);
	}

	public void sort()
	{
		Collections.sort(rankingliste);
	}

	public void showliste()
	{
		int anz = rankingliste.size();
		for (int i = 0; i < anz; i++)
		{
			ThreadStatObj t = rankingliste.get(i);
			String infostring = i + ":" + t.getInfoString();
			System.out.println(infostring);
		}
	}

	public void writeliste(String fnam)
	{
		Inf inf = new Inf();

		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam,0);

		inf.setFilename(fnam);
		int anz = rankingliste.size();
		for (int i = 0; i < anz; i++)
		{
			ThreadStatObj t = rankingliste.get(i);
			String outstr = i + ":" + t.getSaveString();
			inf.writezeile(outstr);
		}
	}

	public void writeRankingpointsToThreaddb()
	// hier werden alle Rankingpoints für die Threads in der threadsdb abgelegt
	{
		int anz = rankingliste.size();
		for (int i = 0; i < anz; i++)
		{
			ThreadStatObj t = rankingliste.get(i);
			String tinfostr = t.getInfoString();
			ThreadDbObj tdbo = t.getTdbo();
			tdbo.setThreadrankingstring(tinfostr);
		}
	}
}
