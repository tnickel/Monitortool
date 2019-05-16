package data;

import javax.swing.JFrame;

public class Config
{
	private static String rootdir = null;
	private static JFrame jframeroot=null;
	
	public Config(String dir)
	{
		String userdir = null;

		if (dir == null)
			userdir = System.getProperty("user.dir");
		else
			userdir = dir;

		rootdir = new String(userdir);

	}

	public static String getRootdir()
	{
		return rootdir;
	}

	public static void setRootdir(String rootdir)
	{
		Config.rootdir = rootdir;
	}

	public static JFrame getJframeroot()
	{
		return jframeroot;
	}

	public static void setJframeroot(JFrame jframeroot)
	{
		Config.jframeroot = jframeroot;
	}

	public static void repaint()
	{
		jframeroot.repaint();
		jframeroot.repaint(50000);
	
	}
}
