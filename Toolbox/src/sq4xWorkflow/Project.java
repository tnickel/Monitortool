package sq4xWorkflow;

import FileTools.ProjZipper;
import hiflsklasse.Inf;

public class Project
{
	private String projectfile_glob="";
	private String mem_glob="";
	private Inf inf_glob=new Inf();
	
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
		//Read project.cfx into memory
		
		ProjZipper pz=new ProjZipper(projectfile_glob);
		pz.ShowFiles();
		pz.writeFile("c:\\tmp\\output.xml");
		
	}
	public void saveProjektfile(String destpath)
	{
		//inf_glob.writeMemFileDelimter();
		
	}
}
