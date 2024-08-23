package sq4xWorkflow;

import java.io.File;
import java.io.IOException;

import FileTools.Filefunkt;
import FileTools.SqConfXml;
import FileTools.SqDate;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqGenerateWorkflowMain
{
	// diese Klasse muss jetzt alle Projektfiles bearbeiten
	
	private String projectdir_g = "";
	
	String[] memstring_g = new String[100];
	String[] memstring_backup_g = null;
	String[] name_g = new String[100];
	String[] name_backup_g = null;
	
	int anzfiles_g = 0;
	
	public SqGenerateWorkflowMain(String directory)
	{
		projectdir_g = directory;
		loadProjectfiles();
	}
	
	void Reset()
	{
		// Restore the old data
		memstring_g = memstring_backup_g.clone();
		name_g = name_backup_g.clone();
	}
	
	void loadProjectfiles()
	{
		int index = 0;
		File[] LIST = new File(projectdir_g).listFiles();
		for (File FILE : LIST)
		{
			
			String fnam = FILE.getAbsolutePath();
			// load the config.sml in memory
			// config.xml is the projektfile
			
			// Read project.cfx into memory
			Inf inf = new Inf();
			inf.setFilename(fnam);
			
			memstring_g[index] = new String(inf.readMemFileDelimter(200000));
			name_g[index] = new String(fnam);
			index++;
			inf.close();
		}
		anzfiles_g = index;
		memstring_backup_g = memstring_g.clone();
		name_backup_g = name_g.clone();
	}
	
	public void saveToTmpDir()
	{
		// this function write config exists in memory to harddisk
		for (int i = 0; i < anzfiles_g; i++)
		{
			String fnam = name_g[i];
			//Tracer.WriteTrace(20, "I:working with file <"+fnam+">");
			// delete the original file
			File fnam_f = new File(fnam);
			if (fnam_f.exists() == false)
				Tracer.WriteTrace(10, "Error: internal, file not exists<" + fnam + ">");
			else if (fnam_f.delete() == false)
				Tracer.WriteTrace(10, "Error1530: can´t delete file<" + fnam_f.getAbsolutePath() + ">");
			
			Inf inf2 = new Inf();
			inf2.setFilename(fnam);
			
			// String[] parts = memstring_g[i].split("@@@@@");
			// hier holt er sich den speicher der ein file representiert
			String mem = memstring_g[i];
			
			
			int offset=0;
			
			// schreib das File auf platte
			int anz = 100000; // max 100000 zeilen
			for (int j = 0; j < anz; j++)
			{
				String zeile = null;
				
				
				
				//int foundpos = mem.indexOf("@@@@@");
				
				int foundpos=mem.indexOf("@@@@@",offset);
				//Tracer.WriteTrace(20, "I:foundpos=<"+foundpos+"> offset=<"+offset+">");
				
				if(foundpos>mem.length())
					break;
				
				if (foundpos == -1)
				{ // the end has reached, write the rest
					//Tracer.WriteTrace(20, "I:filelen="+mem.length()+" foundpos=<"+foundpos+">");
					
					zeile=mem.substring(offset);
					inf2.writeFastZeile(zeile);
			
					break;
					
				} else
				{
					//take the line
					zeile = mem.substring(offset, foundpos);
					inf2.writeFastZeile(zeile);
					//increase offset
					offset=foundpos+5;
				}
				
				
			}
			inf2.close();
		}
		
	}
	
	public void copyToSq(String sqrootdir, String tmpcfx, String destname,String Enddate)
	{
		// das fertig generierte wird in die sq verzeichnisse kopiert
		String newdir = sqrootdir + "\\user\\projects\\" + destname;
		File dirname_f = new File(newdir);
		if (dirname_f.isDirectory() == false)
		{
			if (dirname_f.mkdir() == false)
				Tracer.WriteTrace(10, "E:can´t create directory <" + dirname_f.getAbsolutePath() + ">");
		}
		File tmpcfx_f = new File(tmpcfx);
		File destfile_f = new File(newdir + "\\project.cfx");
		try
		{
			// overwrite existing file
			if (destfile_f.exists())
				destfile_f.delete();
			Tracer.WriteTrace(20,
					"I:I will copy <" + tmpcfx_f.getAbsolutePath() + "> to <" + destfile_f.getAbsolutePath() + ">");
			Filefunkt.copyFileUsingChannel(tmpcfx_f, destfile_f);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File destinfofile=new File(newdir+"\\enddate.txt");
		if(destinfofile.exists())
			if(destinfofile.delete()==false)
				Tracer.WriteTrace(10, "E:cant delete file <"+destinfofile.getAbsolutePath()+">54542211");
		Inf inf=new Inf();
		inf.setFilename(destinfofile.getAbsolutePath());
		inf.writezeile(Enddate);
		inf.close();
		
	}
	
	public void cleanLogfiles(String sqrootdir, String destname)
	{
		File logdir_f = new File(sqrootdir + "\\user\\projects\\" + destname + "\\log");
		FileAccess.deleteDirectoryContentPostfix(logdir_f, ".log");
	}
	
	public String modifyProject(int daysoffset_i,String EndtestDatabaseName,String workflowname,String projectdiri)
	{
		
		String projectdir=projectdiri;
		//workflowname=24.2.24prod[ 1100-1500 ] 1.05 4M_Metrikanalyser_+00000
		String FoundEndtestdate=null;
		for (int i = 0; i < anzfiles_g; i++)
		{
			
			// jetzt wird das datum gesucht und entsprechend modifiziert
			// Suche <Project name="EURUSD - H1 -2year NEW" version="126.2189">
			
			SqConfXml sxml = new SqConfXml(memstring_g[i]);
			
			//hole das Endtestdate
			String Endtestdate=sxml.getEndtestdate(EndtestDatabaseName,daysoffset_i);
			if(Endtestdate!=null)
				FoundEndtestdate=Endtestdate;
			
			
			sxml.setSearchPattern("<Project name=\"", "\"");
			String projektname = sxml.getProjectName();
			Tracer.WriteTrace(20, "projektname=" + projektname);
			
			sxml.setSearchPattern("<Setup dateFrom=", " test");
			memstring_g[i] = sxml.modifyAllPatterns(daysoffset_i);
			
			sxml.setSearchPattern("<Range dateFrom=", " />");
			memstring_g[i] = sxml.modifyAllPatterns(daysoffset_i);
			
			{
				
				if(memstring_g[i].contains("PROJECTDIR")==true)
				{
					Tracer.WriteTrace(50, "Found PROJECTDIR");
					memstring_g[i]=memstring_g[i].replace("PROJECTDIR", projectdir);
				}
					//Tracer.WriteTrace(10, "I:Found projectdir in <"+EndtestDatabaseName+">");
			}
			{
				
				if(memstring_g[i].contains("WORKFLOWNAME")==true)
					
				{
					Tracer.WriteTrace(50, "Found WORKFLOWNAME");
					memstring_g[i]=memstring_g[i].replace("WORKFLOWNAME", workflowname);
				}
					//Tracer.WriteTrace(10, "I:Found workflowname in <"+EndtestDatabaseName+">");
			}
			// replace back X@X
			memstring_g[i] = SqDate.replaceBack(memstring_g[i]);
			
		}
		return FoundEndtestdate;
	}
	
	
}
