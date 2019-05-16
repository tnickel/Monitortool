package work;

import Metriklibs.Metriktabelle;

public class ClusterWork
{
	String ClusterSourcedir=null;
	String ClusterDestDir=null;
	String ClusterCsvFile=null;
	
	public String getClusterSourcedir()
	{
		return ClusterSourcedir;
	}
	public void setClusterSourcedir(String clusterSourcedir)
	{
		ClusterSourcedir = clusterSourcedir;
	}
	public String getClusterDestDir()
	{
		return ClusterDestDir;
	}
	public void setClusterDestDir(String clusterDestDir)
	{
		ClusterDestDir = clusterDestDir;
	}
	public String getClusterCsvFile()
	{
		return ClusterCsvFile;
	}
	public void setClusterCsvFile(String clusterCsvFile)
	{
		ClusterCsvFile = clusterCsvFile;
	}
	public void startClustering()
	{
		//die metriktabelle einlesen
		Metriktabelle met= new Metriktabelle();
		met.readTabelleFile(ClusterCsvFile);
		
		//die strliste einlesen
		
	}
}
