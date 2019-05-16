package fileActorPack;

import gui.Mbox;
import hilfsklasse.FileAccessDyn;

import java.io.File;

import Metriklibs.Metriktabellen;
import data.Stratliste;

public class Filemanager
{
	//kopiert die selektierten Endfiles vom full in das selected directory
	static public void  kopiereEndfiles(Stratliste stratliste,Metriktabellen met)
	{
		//str__all_endtestfiles
		//str__selected_endtestfiles
		String endfile=met.holeEndtestFilnamen();
		String endtestdir_all=endfile.substring(0,endfile.lastIndexOf("\\")+1)+"str__all_endtestfiles";
		String endtestdir_sel=endfile.substring(0,endfile.lastIndexOf("\\")+1)+"str__selected_endtestfiles";
		
		//beim zielverzeichniss alle *.str files löschen
		FileAccessDyn fdyn2=new FileAccessDyn();
		fdyn2.initFileSystemList(endtestdir_sel, 1);
		int anz2=fdyn2.holeFileAnz();
		if(anz2==0)
		{
			Mbox.Infobox("I: no strfiles in <"+endtestdir_all+"> to copy and show");
			return;
		}
		for(int i=0; i<anz2; i++)
		{
			String fnam=fdyn2.holeFileSystemName();
			if(fnam.contains(".str")==false)
				continue;
			File fnamdel=new File(endtestdir_sel+"\\"+fnam);
			fnamdel.delete();
		}
		
		//liste der Verzeichnisse erstellen
		FileAccessDyn fdyn=new FileAccessDyn();
		fdyn.initFileSystemList(endtestdir_all, 1);
		int anz=fdyn.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String fnam=fdyn.holeFileSystemName();
			if(fnam.contains(".str")==false)
				continue;
			
			//prüft nach ob str in der endliste vorhanden ist
			if(stratliste.checkStrAvailable(fnam)==false)
				continue;
			
			String zielfile=endtestdir_sel+"\\"+fnam;
			File zielfilef=new File(zielfile);
			if(zielfilef.exists())
				zielfilef.delete();
			
			String quellfile=endtestdir_all+"\\"+fnam;
			File quellfilef=new File(endtestdir_all+"\\"+fnam);
			
			if(quellfilef.exists()==false)
				continue;
			fdyn.copyFile2(quellfile, zielfile);
		}
	}
}
