package fileActorPack;

import gui.Mbox;
import hilfsklasse.FileAccessDyn;

import java.io.File;

import Metriklibs.Metriktabellen;
import data.Stratliste;

public class Filemanager
{
	// kopiert die selektierten Endfiles vom full in das selected directory
	static public void kopiereEndfiles(Stratliste stratliste, Metriktabellen met)
	{
		// str__all_endtestfiles
		// str__selected_endtestfiles
		String endfile = met.holeEndtestFilnamen();
		String endtestdir_sq4_all = endfile.substring(0, endfile.lastIndexOf("\\") + 1) + "str__all_sq4_endtestfiles";
		String endtestdir_sq4_sel = endfile.substring(0, endfile.lastIndexOf("\\") + 1) + "str__selected_sq4_endtestfiles";
		String endtestdir_sq3_all = endfile.substring(0, endfile.lastIndexOf("\\") + 1) + "str__all_sq3_endtestfiles";
		String endtestdir_sq3_sel = endfile.substring(0, endfile.lastIndexOf("\\") + 1) + "str__selected_sq3_endtestfiles";
		
		// beim zielverzeichniss alle *.str files löschen
		cleanDirAll(endtestdir_sq3_sel,".str");
		cleanDirAll(endtestdir_sq4_sel,".sqx");
		
		// hier wird vom quellverzeichniss ins zielverzeichniss kopiert
		copyEndtest(endtestdir_sq3_all,endtestdir_sq3_sel,stratliste,".str");
		copyEndtest(endtestdir_sq4_all,endtestdir_sq4_sel,stratliste,".sqx");
		
	}
	static private void copyEndtest(String endtestdir_all,String endtestdir_sel,Stratliste stratliste,String postfix)
	{
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(endtestdir_all, 1);
		int anz = fdyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn.holeFileSystemName();
			if (fnam.contains(postfix) == false)
				continue;
			
			// prüft nach ob str in der endliste vorhanden ist, Nur strategien in der
			// endliste werden kopiert

			if (stratliste.checkStrAvailable(fnam) == false)
				continue;
			
			String zielfile = endtestdir_sel + "\\" + fnam;
			File zielfilef = new File(zielfile);
			if (zielfilef.exists())
				zielfilef.delete();
			
			String quellfile = endtestdir_all + "\\" + fnam;
			File quellfilef = new File(endtestdir_all + "\\" + fnam);
			
			if (quellfilef.exists() == false)
				continue;
			fdyn.copyFile2(quellfile, zielfile);
		}
		
	}
	static private void cleanDirAll(String cleandir,String postfix)
	{
		// beim zielverzeichniss alle *.str files löschen
		FileAccessDyn fdyn2 = new FileAccessDyn();
		fdyn2.initFileSystemList(cleandir, 1);
		int anz2 = fdyn2.holeFileAnz();
		if (anz2 != 0)
		{
			for (int i = 0; i < anz2; i++)
			{
				String fnam = fdyn2.holeFileSystemName();
				if (fnam.contains(postfix) == false)
					continue;
				File fnamdel = new File(cleandir + "\\" + fnam);
				fnamdel.delete();
			}
		}
	}
}
