package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

public class Metrikglobalconf
{
	private static Properties prop = new Properties();

	//userdir ist das root/hauptverzeichniss wo sich alle unterverzeichnisse vom metrikanalyser befinden
	static String userdir_glob=null;

	//filterpath=dies ist das verzeichniss wo die einzelnen filterfiles drin sind
	//z.B. analyse1
	static String filterpath_glob=null;
	//propfile= ist das propertyfile
	static String propfilename_glob=null;
	//endtestpath ist das Verzeichniss wo sich das Endtestfile drin befindet
	static String endtestpath_glob=null;
	static int stopflag_glob=0;
	

	
	
	static public String getFilterpath()
	{
		if(filterpath_glob==null)
			return ("please set path");
			else
		return filterpath_glob;
	}

	static public void setFilterpath(String filterpath)
	{
		filterpath_glob = filterpath;
		prop.setProperty("filterpath", filterpath_glob);
		
		save();
	}
	static public void setEndtestpath(String path)
	{
		endtestpath_glob=path;
	}
	static public String getEndtestpath()
	{
		return endtestpath_glob;
	}
	
	

	public static int getStopflag_glob()
	{
		return stopflag_glob;
	}

	public static void setStopflag_glob(int stopflag_glob)
	{
		Metrikglobalconf.stopflag_glob = stopflag_glob;
	}

	public Metrikglobalconf(String userdir)
	{
		
	
		userdir_glob=userdir;
		propfilename_glob=userdir_glob
				+ "\\bin\\conf\\conf.prop";

		File pfile= new File(propfilename_glob);
		if(pfile.exists()==false)
			return;
		try
		{
			prop.load(new FileInputStream(propfilename_glob));
			filterpath_glob = prop.getProperty("filterpath");

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
	static public void save()
	{
		//		Mbox.Infobox("Möchte die config unter <"+propfilename_glob+"> speichern");
		
		Writer writer = null;
		 try
		{
			writer = new FileWriter( propfilename_glob );
			prop.store(writer,null);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
