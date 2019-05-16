package cluster;

import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

import java.util.ArrayList;

import work.JToolboxProgressWin;
import data.ClusterSet;
import data.ClusterStrategy;

public class Cluster
{
	// dies ist die hauptklasse die den ganzen Cluster mit den dazugehörigen
	// Funktionen beinhaltet.
	static ArrayList<ClusterSet> clusterliste = new ArrayList<ClusterSet>();
	static String sourcedir_glob=null, destdir_glob=null;

	public static void genCluster(String sourcedir, String destdir)
	{
		sourcedir_glob=sourcedir;
		destdir_glob=destdir;
		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(sourcedir, 1);

		int anz = FileAccess.holeFileAnz();
		JToolboxProgressWin jp = new JToolboxProgressWin("load data <calc>", 0,
				(int) anz);
		jp.update(0);
		// lade jede Strategie
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if (fnam0.contains(".str") == false)
				continue;
			String fnam = sourcedir + "\\" + fnam0;
			addStrategie(fnam);

		}
		jp.update(anz);
		jp.end();
		saveCluster();
	}

	private static void addStrategie(String fnam)
	{
		// die Strategie wird in den cluster eingeordnet
		int anzcluster= clusterliste.size();
		for(int i=0; i<anzcluster; i++)
		{
			ClusterSet cs=clusterliste.get(i);
			//falls ein passender cluster gefunden wurde, dann nimm auf
			if(cs.passtStrat(fnam)==true)
			{
				cs.add(fnam);
				Tracer.WriteTrace(20, "I:strat <"+fnam+"> wurde zum bekannten cluster<"+i+"> aufgenommen");
				return;
			}
		}
		//es wurde kein cluster gefunden, dann generiere einen neues clusterset und nimm in die liste auf
		ClusterSet csnew=new ClusterSet();
		Tracer.WriteTrace(20, "I:strat <"+fnam+"> in neuem Cluster <"+anzcluster+">");
		csnew.add(fnam);
		clusterliste.add(csnew);
	}
	private static void saveCluster()
	{
		//hier werden die ganzen Cluster auf platte gespeichert
		int anzcluster= clusterliste.size();
		for(int i=0; i<anzcluster; i++)
		{
			ClusterSet cs=clusterliste.get(i);
			cs.storeCluster(sourcedir_glob,destdir_glob);
		}
		
	}
}
