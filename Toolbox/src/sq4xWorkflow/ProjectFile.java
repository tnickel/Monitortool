package sq4xWorkflow;

import java.io.File;
import java.io.IOException;

import FileTools.Filefunkt;
import FileTools.ProjZipper;
import FileTools.Zipper;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class ProjectFile
{
	//diese Klasse k�mmert sich um ein einzelnes File, lesen, modifizieren und speichern
	//das File wird im tmp-verzeichniss abgelegt.
	ProjZipper pz= null;
	private String projectfile_g="";
	//private String membin_glob="";
	private String memstring_org_g="";
	private String memstring_mod_g="";
	private Inf inf_glob=new Inf();
	private String configxml="c:\\tmp\\config.xml";
	private String configxml2="c:\\tmp\\config2.xml";
	private String tmpcfx="c:\\tmp\\project.cfx";

	public ProjectFile()
	{
	
	}
	public ProjectFile(String fnam)
	{
		projectfile_g=fnam;
		loadProjectfile();
	}
	
	private void loadProjectfile( )
	{
		//load the config.sml in memory
		//config.xml is the projektfile
		File ofile=new File(configxml);
		if(ofile.exists())
		   ofile.delete();
		
		//Read project.cfx into memory
		pz=new ProjZipper(projectfile_g);
		pz.ShowFiles();
		
		// Write file on harddisk in tmp-directory
		// write c:\tmp\config.xml
		pz.writeFile(configxml);
		Inf inf=new Inf();
		inf.setFilename(configxml);
		memstring_org_g=inf.readMemFileDelimter(60000);
		inf.close();
		
		
	}
	
	public void modifyProject(String parameter)
	{
		memstring_mod_g=new String(memstring_org_g);
		
		//jetzt wird das datum gesucht und entsprechend modifiziert
		
	}
	
	public void saveTmpProjectfile()
	{
		//this function write config exists in memory to harddisk
		Inf inf2=new Inf();
		inf2.setFilename(configxml2);
		
		String[] parts = memstring_mod_g.split("@@@@@");
		
		int anz=parts.length;
		for(int i=0; i<anz; i++)
			inf2.writezeile(parts[i]);
		
		inf2.close();
		
		File conf1=new File(configxml);
		if(conf1.exists())
			conf1.delete();
		
		File conf2=new File(configxml2);
		conf2.renameTo(conf1);
		
		try
		{
			Zipper.ZipFileArchive(conf1.getAbsolutePath(), tmpcfx);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void copyToSq(String projroot,String destname) throws IOException
	{
		
		String newdir=projroot+"\\"+destname;
		File dirname_f=new File(newdir);
		if(dirname_f.isDirectory())
			Tracer.WriteTrace(10, "E:directory <"+dirname_f.getAbsolutePath()+">already exists");
		if(dirname_f.mkdir()==false)
			Tracer.WriteTrace(10, "E:can�t create directory <+dirname_f.getAbsolutePath()+>");
		
		File tmpcfx_f=new File(tmpcfx);
		File destfile_f=new File(newdir+"\\project.cfx");
		Filefunkt.copyFileUsingChannel(tmpcfx_f, destfile_f);
		
	}
	
}
