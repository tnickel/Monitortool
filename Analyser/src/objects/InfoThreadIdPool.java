package objects;

import hilfsklasse.KeyReader;
import hilfsklasse.SG;
import hilfsklasse.Tracer;

import java.util.HashSet;

import mainPackage.GC;
import stores.UserDB;
import userHilfObj.UserPostingListe;

public class InfoThreadIdPool
{

	private KeyReader kr = new KeyReader();
	private HashSet<Integer> tidmenge = new HashSet<Integer>();

	public InfoThreadIdPool(UserDB udb, String observefile)
	// baue den ThreadIdPool auf
	{
		kr.SetReader(GC.rootpath + "\\db\\" + observefile);
		while (5 == 5)
		{
			String line = kr.GetNextLine();
			if (line == null)
				return;

			if ((SG.is_zahl(line) == true))
				tidmenge.add(SG.get_zahl(line));
			else
			{
				// wenn es keine Zahl ist dann ist es wohl ein Username
				UserDbObj udbo = udb.getUserobj(line);

				// falls der user nicht in der userstore.db dann gib warnung aus
				if (udbo == null)
				{
					Tracer.WriteTrace(20, "Warning username<" + line
							+ "> aus file<" + observefile
							+ "> ist nicht in userstore.db");
					return;
				}
				UserPostingListe upl = udbo.getUserPostingListe();
				upl.ReadUserInfoListe(udbo);
				int size = upl.getSize();
				for (int i = 0; i < size; i++)
				{
					UserThreadPostingObj postobj = upl.getObjIDX(i);
					int tid = postobj.getThreadid();
					tidmenge.add(tid);
				}
				// lösche die Postingliste wieder
				upl.deleteUserPostingListe();
			}
		}

	}

	public int getNextThreadId()
	{
		// hole die nächste tid aus der Menge
		if (tidmenge.size() > 0)
		{
			// wandel die menge in ein array um
			Integer[] tidarray = tidmenge.toArray(new Integer[0]);
			// nimm das erste element
			int tid = tidarray[0];
			// entferne die tid aus der tidmenge

			if (tidmenge.contains(tid) == false)
				Tracer.WriteTrace(10, "Error: internal error 1511");
			tidmenge.remove(tid);
			return tid;
		}
		return 0;
	}
}
