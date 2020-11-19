package sq4xWorkflow;

import java.io.File;
import java.io.IOException;

import FileTools.Filefunkt;
import FileTools.ProjZipper;
import FileTools.Zipper;
import hiflsklasse.Inf;
import hiflsklasse.InfFast;
import hiflsklasse.Tracer;

public class SqGenerateWorkflowMain
{
	// diese Klasse kümmert sich um ein einzelnes Projektfile, lesen, modifizieren und
	// speichern
	// das File wird im tmp-verzeichniss abgelegt.
	ProjZipper pz = null;
	private String projectfile_g = "";
	// private String membin_glob="";
	private String memstring_org_g = null;
	private String memstring_mod_g = null;
	private Inf inf_glob = new Inf();
	private String configxml = "c:\\tmp\\config.xml";
	private String configxml2 = "c:\\tmp\\config2.xml";
	private String tmpcfx = "c:\\tmp\\project.cfx";
	
	public SqGenerateWorkflowMain()
	{
		
	}
	
	public SqGenerateWorkflowMain(String fnam)
	{
		projectfile_g = fnam;
		loadProjectfile();
	}
	
	private void loadProjectfile()
	{
		// load the config.sml in memory
		// config.xml is the projektfile
		File ofile = new File(configxml);
		if (ofile.exists())
			ofile.delete();
		
		// Read project.cfx into memory
		pz = new ProjZipper(projectfile_g);
		pz.ShowFiles();
		
		// Write file on harddisk in tmp-directory
		// write c:\tmp\config.xml
		pz.writeFile(configxml);
		Inf inf = new Inf();
		inf.setFilename(configxml);
		memstring_org_g = inf.readMemFileDelimter(60000);
		inf.close();
		
	}
	
	public void modifyProject(int daysoffset_i)
	{
		memstring_mod_g = new String(memstring_org_g);
		
		// jetzt wird das datum gesucht und entsprechend modifiziert
		// Suche <Project name="EURUSD - H1 -2year NEW" version="126.2189">
		
		SqConfXml sxml = new SqConfXml(memstring_mod_g);
		sxml.setSearchpattern("<Project name=\"", "\"");
		String projektname = sxml.getProjectName();
		Tracer.WriteTrace(20, "projektname=" + projektname);
		
		sxml.setSearchpattern("<Setup dateFrom=", " test");
		memstring_mod_g = sxml.modifyAllPatterns(daysoffset_i);
		
		sxml.setSearchpattern("<Range dateFrom=", " />");
		memstring_mod_g = sxml.modifyAllPatterns(daysoffset_i);
		
		// replace back X@X
		memstring_mod_g = SqDate.replaceBack(memstring_mod_g);
		
	}
	
	public void saveTmpProjectfile()
	{
		// this function write config exists in memory to harddisk
		Inf inf2 = new Inf();
		inf2.setFilename(configxml2);
		
		String[] parts = memstring_mod_g.split("@@@@@");
		
		int anz = parts.length;
		for (int i = 0; i < anz; i++)
			inf2.writezeile(parts[i]);
		
		inf2.close();
		
		File conf1 = new File(configxml);
		if (conf1.exists())
			conf1.delete();
		
		File conf2 = new File(configxml2);
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
	
	public void copyToSq(String sqrootdir, String destname)
	{
		// das fertig generierte wird in die sq verzeichnisse kopiert
		String newdir = sqrootdir + "\\user\\projects\\" + destname;
		File dirname_f = new File(newdir);
		
		
		if (dirname_f.isDirectory() == false)
		{
			if (dirname_f.mkdir() == false)
				Tracer.WriteTrace(10, "E:can´t create directory <"+dirname_f.getAbsolutePath()+">");
		}
		File tmpcfx_f = new File(tmpcfx);
		File destfile_f = new File(newdir + "\\project.cfx");
		try
		{
			//overwrite existing file
			if(destfile_f.exists())
				destfile_f.delete();
			Tracer.WriteTrace(20, "I:I will copy <"+tmpcfx_f.getAbsolutePath()+"> to <"+destfile_f.getAbsolutePath()+">");
			Filefunkt.copyFileUsingChannel(tmpcfx_f, destfile_f);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
