package sq4xWorkflow;

import java.io.File;

import FileTools.ProjZipper;
import hiflsklasse.Inf;

public class Project
{
	//liest erst aus dem gezippten File das config.xml binär
	//und schreibt diese Datei unter c:\tmp\output.xml
	//dann wird output.xml mit inf-read mem in den speicher gelesen und
	//kann geändert werden.
	
	private String projectfile_glob="";
	private String membin_glob="";
	private String memstring_glob="";
	private Inf inf_glob=new Inf();
	private String configxml="c:\\tmp\\config.xml";
	ProjZipper pz= null;
	
	public Project(String projectfile)
	{
		projectfile_glob = projectfile;
		loadProjectfile();
		
	}
	
	public  String getProjectfile()
	{
		return projectfile_glob;
	}

	
	
	private void loadProjectfile( )
	{
		File ofile=new File(configxml);
		if(ofile.exists())
		   ofile.delete();
		
		//Read project.cfx into memory
		pz=new ProjZipper(projectfile_glob);
		pz.ShowFiles();
		
		// Write file on harddisk in tmp-directory
		pz.writeFile(configxml);
		Inf inf=new Inf();
		inf.setFilename(configxml);
		memstring_glob=inf.readMemFileDelimter(50000);
		inf.close();
		
		//System.out.println("memstring_glob="+memstring_glob);
	}
	public void saveProjektfile(String destpath)
	{
		//this function have to be written
		//inf_glob.writeMemFileDelimter();
		
	}
	public void modifyProject()
	//the currency paris can be changed
	{
		
	}
}
