package data;

public class DropRootpath
{
	static String rootpath=null;


	public DropRootpath(String rp)
	{
		rootpath=rp;

	}
	
	public static String getRootpath()
	{
		return rootpath;
	}
	public static void setRootpath(String rootpath)
	{
		DropRootpath.rootpath = rootpath;
	}
}
