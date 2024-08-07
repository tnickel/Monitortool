package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.eclipse.swt.widgets.Shell;

import hiflsklasse.Tracer;

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
	static int percent_glob=50;
	static int fixed_seed_flag_glob=0;
	static int maxBestlist=100;
	static int CollectOnlyRobustStrategies_glob=1;
	static int minStratPortfolio_glob=5;
	static String wekafile_glob=null;
	
	//variables for the header
	static Shell shell_glob=null;
	static String version_glob=null;
	
	//temporary configurations
	static String sqworkflowdir=null;
	static String wekalearndatapath="";
	static String wekalearnlearnerpath="";
	static String wekaclassidatapath="";
	static String wekaclassilearnerpath="";
	static String wekabuildattributespath="";
	
	public Metrikglobalconf(String userdir)
	{
		
	
		userdir_glob=userdir;
		if((userdir_glob==null)||(userdir_glob.length()<3))
			Tracer.WriteTrace(10, "Error: userdir_glob<"+userdir_glob+">");
		propfilename_glob=userdir_glob
				+ "\\conf\\conf.prop";

		File pfile= new File(propfilename_glob);
		if(pfile.exists()==false)
			return;
		try
		{
			prop.load(new FileInputStream(propfilename_glob));
			filterpath_glob = prop.getProperty("filterpath");
			String percent=prop.getProperty("percent");
			if(percent!=null)
				percent_glob=Integer.valueOf(percent);
			else
				percent_glob=50;
			
			String fixedseed=prop.getProperty("fixedseedflag");
			
			if(fixedseed!=null)
				fixed_seed_flag_glob=Integer.valueOf(fixedseed);
			else
				fixed_seed_flag_glob=0;

			String mb=prop.getProperty("maxbestlist");
			if(mb==null)
				mb="1000";
			maxBestlist=Integer.valueOf(mb);
			
			if(prop.getProperty("collectonlyrobuststrategies")!=null)
				CollectOnlyRobustStrategies_glob=Integer.valueOf(prop.getProperty("collectonlyrobuststrategies"));
			if(prop.getProperty("minstratportfolio")!=null)
				minStratPortfolio_glob=Integer.valueOf(prop.getProperty("minstratportfolio"));
			prop.load(new FileInputStream(propfilename_glob));
			wekafile_glob = prop.getProperty("wekafile");
			
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
	static public String getFilterpath()
	{
		if(filterpath_glob==null)
			return ("please set path");
			else
		return filterpath_glob;
	}

	public static int getMinStratPortfolio()
	{
		return minStratPortfolio_glob;
	}

	public static void setMinStratPortfolio(int minStratPortfolio)
	{
		prop.setProperty("minstratportfolio", String.valueOf(minStratPortfolio));
		minStratPortfolio_glob = minStratPortfolio;
		save();
	}

	static public void setFilterpath(String filterpath)
	{
		filterpath_glob = filterpath;
		prop.setProperty("filterpath", filterpath_glob);
		
		save();
	}
	
	public static int getCollectOnlyRobustStrategies()
	{
		return CollectOnlyRobustStrategies_glob;
	}

	public static void setCollectOnlyRobustStrategies(int val)
	{
		Metrikglobalconf.CollectOnlyRobustStrategies_glob = val;
		prop.setProperty("collectonlyrobuststrategies", String.valueOf(val));
		save();
	}

	static public void setPercent(int i)
	{
		percent_glob=i;
		prop.setProperty("percent", String.valueOf(i));
	}
	static public int getPercent()
	{
		
		return percent_glob;
	}
	static public int getFixedSeedflag()
	{
		return fixed_seed_flag_glob;
		
	}
	static public void setFixedSeedflag(int i)
	{
		fixed_seed_flag_glob=i;
		prop.setProperty("fixedseedflag", String.valueOf(fixed_seed_flag_glob));
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

	public static void setMaxbestlist(int max)
	{
		Metrikglobalconf.maxBestlist=max;
		prop.setProperty("maxbestlist", String.valueOf(max));
	}
	public static int getmaxBestlist()
	{
		return Metrikglobalconf.maxBestlist;
	}
	
	
	public static String getWekafile_glob()
	{
		return wekafile_glob;
	}

	public static void setWekafile_glob(String wekafile_glob)
	{
		prop.setProperty("wekafile", wekafile_glob);
		save();
		Metrikglobalconf.wekafile_glob = wekafile_glob;
	}

	
	static public void save()
	{
		//		Mbox.Infobox("Möchte die config unter <"+propfilename_glob+"> speichern");
		
		if((propfilename_glob==null)||(propfilename_glob.length()<3))
			Tracer.WriteTrace(10, "Error: configfilename <"+propfilename_glob+">");
		
		Writer writer = null;
		 try
		{
			 
			 
			writer = new FileWriter( propfilename_glob );
			prop.store(writer,null);
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Tracer.WriteTrace(10, "Error: exception:configfilename <"+propfilename_glob+">");
			e.printStackTrace();
		}
	}

	static public void setShell(Shell shell)
	{
		shell_glob=shell;
	}

	

	public static void setVersion(String version_glob)
	{
		Metrikglobalconf.version_glob = version_glob;
	}
	public static void refreshHeader(String path)
	{
		shell_glob.setText("Metrikanalyser " + version_glob+ "       WorkDir="+path);
		
	}
	public static String getWekalearndatapath()
	{
		return wekalearndatapath;
	}
	public static void setWekalearndatapath(String wekalearndatapath)
	{
		Metrikglobalconf.wekalearndatapath = wekalearndatapath;
	}
	public static String getWekalearnmodelpath()
	{
		return wekalearnlearnerpath;
	}
	public static void setWekalearnlearnerpath(String wekalearnlearnerpath)
	{
		Metrikglobalconf.wekalearnlearnerpath = wekalearnlearnerpath;
	}
	public static String getWekaclassidatapath()
	{
		return wekaclassidatapath;
	}
	public static void setWekaclassidatapath(String wekaclassidatapath)
	{
		Metrikglobalconf.wekaclassidatapath = wekaclassidatapath;
	}
	public static String getWekaclassimodelpath()
	{
		return wekaclassilearnerpath;
	}
	public static void setWekaclassilearnerpath(String wekaclassilearnerpath)
	{
		Metrikglobalconf.wekaclassilearnerpath = wekaclassilearnerpath;
	}
	public static String getWekabuildattributespath()
	{
		return wekabuildattributespath;
	}
	public static void setWekabuildattributespath(String wekabuildattributespath)
	{
		Metrikglobalconf.wekabuildattributespath = wekabuildattributespath;
	}
	public static boolean isValidMasterRootpath()
	{
		String path=filterpath_glob;
		File dir_f= new File(filterpath_glob+"\\WORKFLOWNAME");
		if (dir_f.exists()==false)
			return false;
		else
			return true;
	}
	public static boolean isValidSubRootpath()
	{
		if(isValidMasterRootpath()==true)
			return false;
		
		
		String path=filterpath_glob;
		File dir_f= new File(filterpath_glob+"\\_0_dir");
		if (dir_f.exists()==true)
			return true;
		else
			return false;
	}
}
