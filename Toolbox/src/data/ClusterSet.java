package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;

public class ClusterSet
{
	//hier befindet sich die ganze Clustermenge
	static ArrayList<ClusterStrategy> stratliste = new ArrayList<ClusterStrategy>();
	
	public boolean passtStrat(String fnam)
	{
		//wird geschaut ob Strategie in den Cluster passt
		ClusterStrategy cl=new ClusterStrategy(fnam);
		
		ClusterStrategy strat1=stratliste.get(0);
		double corel_f=strat1.calcCorel(cl);
		
		//falls strategie ähnlich ist, dann passt die zum cluster
		if(corel_f>0.3)
			return true;
		
		
		return false;
	}
	
	public boolean add(String fnam)
	{
		//nimmt strategie in den Cluster auf
		ClusterStrategy cl=new ClusterStrategy(fnam);
		stratliste.add(cl);
		return true;
	}
	
	public boolean storeCluster(String quelle, String ziel)
	{
		//eine cluster beinhaltet eine menge von strategien mit den gleichen
		//eigenschaften
		//ein cluster wird nur in einem einzigen verzeichniss gspeichert
		
		if(quelle.equalsIgnoreCase(ziel))
		{
			Tracer.WriteTrace(10, "E: it is not allowed src<"+quelle+"> should != dest<"+ziel+">");
			return false;
		}
		
		if(ziel.toLowerCase().contains("output")==false)
		{
			Tracer.WriteTrace(10, "E: outputpath should contains `output`");
			return false;
		}
		
		int anz=0;
		
		//alte clusterverzeichnisse mit strategien löschen
		//also alles was da ist weg
		FileAccess.initFileSystemList(ziel, 0);
		anz=FileAccess.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String fnam=FileAccess.holeFileSystemName();
			if(fnam.contains("CLUST_")==false)
				continue;
			//delete file in the directory
			FileAccess.deleteDirectoryContentPostfix(new File(ziel+"\\"+fnam), ".str");

			//delete Directory
			File f= new File(ziel+"\\"+fnam);
			if(f.isDirectory()==true)
			{
				f.delete();
			}
		}
		
		//dann erzeuge die verzeichnisse
		anz=stratliste.size();
		for(int i=0; i<anz; i++)
		{
			ClusterStrategy cl=stratliste.get(i);
			String clustname=cl.getClustername();
			File f= new File(ziel+"\\"+clustname);
			if(f.exists()==false)
				f.mkdir();
		}
		
		anz=stratliste.size();
		for(int i=0; i<anz; i++)
		{
			ClusterStrategy cl=stratliste.get(i);
			String stratname=cl.getStratname();
			String clustname=cl.getClustername();
			Tracer.WriteTrace(20, "I:stratname=<"+stratname+"> cluster<"+cl.getClustername()+">");
			
			//verzeichnisse sind da, dann kann jetzt umkopiert werden
			stratname=stratname.substring(stratname.lastIndexOf("\\")+1);
			String zielfile=ziel+"\\"+clustname+"\\"+stratname;
			FileAccess.copyFile(quelle+"\\"+stratname, zielfile);
		}
		
		return true;
	}
	
	
	
}
