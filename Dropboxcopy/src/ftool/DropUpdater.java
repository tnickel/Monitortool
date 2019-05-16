package ftool;

import gui.Mbox;
import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;

import java.io.File;

import data.DropGlobalVar;
import data.DropRootpath;

public class DropUpdater
{
	static public void updateDropboxtool()
	{
		//das quellfile bestimmen
		String quellfile=DropGlobalVar.getUpdatedir()+"\\bin\\dropboxtool.jar";
		String zielfile=DropRootpath.getRootpath()+"\\bin\\dropboxtool.jar";
		FileAccessDyn fd= new FileAccessDyn();
		//check quell und zielfile
		if(FileAccess.FileAvailable(quellfile)==false)
		{
			Mbox.Infobox("Update error quellfile <"+quellfile+"> not available please make correct config");
			Mbox.Infobox("for example set dropbox\\dropboxtool   as quellpath  ");
			return;
		}
		if(FileAccess.FileAvailable(zielfile)==false)
		{
			Mbox.Infobox("Update error destfile <"+zielfile+"> not available");
			return;
		}
		
		if(checkNewUpdate()==false)
		{
			Mbox.Infobox("system already up to date");
			return;
		}
		
		fd.copyFile2(zielfile, zielfile+".save");
		
		if(fd.copyFile2(quellfile, zielfile)==true)
		{
			Mbox.Infobox("I copied update from <"+quellfile+"> to <"+zielfile+"> please restart");
		}
	}
	static public boolean checkNewUpdate()
	{
		String quellfile=DropGlobalVar.getUpdatedir()+"\\bin\\dropboxtool.jar";
		String zielfile=DropRootpath.getRootpath()+"\\bin\\dropboxtool.jar";
		File quell=new File(quellfile);
		File ziel=new File(zielfile);
		
		if(quell.length()!=ziel.length())
			return true;
		else 
			return false;
	}
}
