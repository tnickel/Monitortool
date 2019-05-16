package wartung;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;

import java.io.File;

import mainPackage.GC;

public class SplittVerzeichniss
{
	public SplittVerzeichniss()
	{
	}
	
	public void start_verschiebe_verzeichnisse(String root)
	{
		//Verschiebe alle Verzeichnisse in dem Rootpath von $root nach $root//@[Anfangsbustaben]
		//Der erste Verzeichnisscharakter wird als Verzeichnissnamen des Unterverzeichnisses gewählt.
		
		//Bsp: $root//Altos wird verschoben nach $root//@A//Altos
		
		// erstelle Liste der Verzeichnisse
		FileAccess.initFileSystemList(root, 0);
		int anz=FileAccess.holeFileAnz();
		
		for(int i=0; i<anz; i++)
		{
			String nam=FileAccess.holeFileSystemName();
			char first=nam.charAt(0);
			if(first=='@')
				continue;
			
			String dirnam="@"+first;
			String verz=root+"\\"+dirnam;
			if(FileAccess.checkgenDirectory(verz))
			{
				//System.out.println("Verzeichniss <"+verz+"> pos<"+i+"> existiert");
			}
				else
				{
					System.out.println("Verzeichniss <"+verz+"> pos<"+i+"> wurde erstellt");
				}
		
			File  source = new File(root+"\\"+nam);
			File destination = new File(verz+"\\"+nam);
			
			System.out.println("Verschiebe Verzeichniss pos<"+i+"> <"+source.toString()+">");
			
			if(!source.renameTo(destination))
			{
			
				Tracer.WriteTrace(20,"Fehler beim Umbenennen der Datei: " + source.getName());
			}
		}
	}
	public void start_verschiebe_namen(String root)
	{
		//Verschiebe alle Verzeichnisse in dem Rootpath von $root nach $root//@[Anfangsbustaben]
		//Der erste Verzeichnisscharakter wird als Verzeichnissnamen des Unterverzeichnisses gewählt.
		
		//Bsp: $root//Altos wird verschoben nach $root//@A//Altos
		
		// erstelle Liste der dateien
		FileAccess.initFileSystemList(root, 1);
		int anz=FileAccess.holeFileAnz();
		
		for(int i=0; i<anz; i++)
		{
			String nam=FileAccess.holeFileSystemName();
			char first=nam.charAt(0);
			
			String dirnam="@"+first;
			String verz=root+"\\"+dirnam;
			if(FileAccess.checkgenDirectory(verz))
			{
				//System.out.println("Verzeichniss <"+verz+"> pos<"+i+"> existiert");
			}
				else
				{
					System.out.println("Verzeichniss <"+verz+"> pos<"+i+"> wurde erstellt");
				}
		
			File  source = new File(root+"\\"+nam);
			File destination = new File(verz+"\\"+nam);
			
			System.out.println("Verschiebe Verzeichniss pos<"+i+"> <"+source.toString()+">");
			
			if(!source.renameTo(destination))
			{
				Tracer.WriteTrace(20,"Fehler beim Umbenennen der Datei: " + source.getName());
			}
		}
	}
	public void loescheVerzeichnissinhalt(String root)
	{
		// erstelle Liste der dateien
		FileAccess.initFileSystemList(root, 1);
		int anz=FileAccess.holeFileAnz();
		
		for(int i=0; i<anz; i++)
		{
			String nam=FileAccess.holeFileSystemName();
			FileAccess.FileDelete(GC.rootpath+"\\db\\Attribute\\"+nam,0);
			Tracer.WriteTrace(20, "<"+i+">Info: delete file<"+nam+">");
		}
	}

}
