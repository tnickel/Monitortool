package data;

public class Rootpath
{
	//dies ist die globale definition für alle variablen
	//diese definitionen werden im main gesetzt
	static String rootpath=null;


	public Rootpath(String rp, String crp)
	{
		rootpath=rp;

	}
	
	public static String getRootpath()
	{
		return rootpath;
	}
	public static void setRootpath(String rootpath)
	{
		Rootpath.rootpath = rootpath;
	}

}
