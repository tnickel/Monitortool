package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SMglobalConfig
{
	private static String rootdir=null;
	private static Properties prop = new Properties();

	public SMglobalConfig(String rdir)
	{
		rootdir=rdir;
		//hier wird die globale config eingelesen
			try
			{
				prop.load(new FileInputStream(rootdir
						+ "\\conf\\conf.prop"));
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static String getRootdir()
	{
		return rootdir;
	}

	public static void setRootdir(String rootdirs)
	{
		rootdir = rootdirs;
	};
	public static String getTradelistendir()
	{
		String tdir=prop.getProperty("tradelistendir");
		return(tdir);
	}
	public static void setTradelistendir(String dir)
	{
	
		prop.setProperty("tradelistendir", dir);
		store();
	}

	public static void setCmddir(String dir)
	{
		prop.setProperty("commanddir", dir);
		store();
	}
	
	public static String getCmddir()
	{
		String cdir=prop.getProperty("commanddir");
		return(cdir);
	}
	
	public static void store()
	{
		FileOutputStream out;
		try
		{
			out = new FileOutputStream(rootdir+ "\\conf\\conf.prop");
			prop.store(out, null);
			out.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
