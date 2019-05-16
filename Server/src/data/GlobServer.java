package data;

public class GlobServer
{

	static int serverport = 0;
	static String rootdir = null;

	public static int getServerport()
	{
		return serverport;
	}

	public static void setServerport(int serverport)
	{
		GlobServer.serverport = serverport;
	}

	public static String getRootdir()
	{
		return rootdir;
	}

	public static void setRootdir(String rootdir)
	{
		GlobServer.rootdir = rootdir;
	}

}
