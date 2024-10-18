package fileActorPack;

import java.io.File;

import data.Metrikglobalconf;
import hiflsklasse.Tracer;
import hilfsklasse.FileAccess;

public class AutoGenVerzeichnisse
{
	// Hier werden im Metrikanalyser in ..AnalysesRoot die Verzeichnisse erstellt
	// Die Verzeichnissnamen werden aus den Workflownamen generiert
	static public void GenVerzeichnisse(String sqxworkflowrootdir,int anzdirn)
	{
		
		String filterpath = Metrikglobalconf.getFilterpath();
		
		if (sqxworkflowrootdir.toLowerCase().contains("master") == true)
			Tracer.WriteTrace(10,
					"E: Don´t use 1 Master, please use Generatordirectorys for example 'C:\\forex\\Toolbox\\SQ\\2 Generator\\user\\projects'");
		
		if (sqxworkflowrootdir.endsWith("projects") == false)
			Tracer.WriteTrace(10,
					"The directory should set to SQX-project-dir for example 'C:\\forex\\Toolbox\\SQ\\2 Generator\\user\\projects'");
		
		File directory = new File(sqxworkflowrootdir);
		
		if (directory.exists() && directory.isDirectory())
		{
			// Liste der Inhalte des Ausgangsverzeichnisses abrufen
			File[] contents = directory.listFiles();
			
			if (contents != null)
			{
				// Durch alle Inhalte iterieren
				for (File file : contents)
				{
					String filePath = file.getAbsolutePath().toLowerCase();
					// Bedingungen zum Überspringen bestimmter Verzeichnisse
					if (filePath.endsWith("builder") || filePath.endsWith("retester") || filePath.endsWith("optimizer")
							|| filePath.endsWith("portfoliomaster"))
					{
						continue;
					}
					
					// Nur Verzeichnisse berücksichtigen und deren Namen ausgeben
					if (file.isDirectory())
					{
						String dirName = file.getName();
						String targetDir = filterpath + "\\" + dirName;
						
						// Verzeichnisstruktur erstellen
						genStructure(targetDir,anzdirn);
					}
				}
			} else
			{
				System.out
						.println("Das angegebene Verzeichnis ist leer oder der Inhalt konnte nicht abgerufen werden.");
			}
			
			genStructure(filterpath + "\\WORKFLOWNAME",anzdirn);
		}
	}
	
	private static void genStructure(String zieldir,int anzdirn)
	{
		File zieldir_f = new File(zieldir);
		if (zieldir_f.exists() == false)
		{
			
			if (zieldir_f.mkdir() == false)
				Tracer.WriteTrace(10, "E:Error can´t generate <" + zieldir_f.getAbsolutePath() + ">");
			
			
			
			for(int i=0; i<anzdirn; i++)
			{
				FileAccess.checkgenDirectory(zieldir + "\\_"+i+"_dir");
				
			}
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir");
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\best100dir");
			//FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\str__all_sq3_endtestfiles");
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\str__all_sq4_endtestfiles");
			//FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\str__selected_sq3_endtestfiles");
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\str__selected_sq4_endtestfiles");
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\badcounter");
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\goodcounter");
			FileAccess.checkgenDirectory(zieldir + "\\_99_dir\\Multilearn");
		}
		
	}
}
