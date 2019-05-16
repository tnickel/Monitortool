package network;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

import java.io.File;

import mtools.MboxQuest;
import Metriklibs.FileAccessDyn;
import data.GlobalVar;
import data.Rootpath;

public class Updater
{
	static public int GetVersionFilelength()
	{
		// schickt die länge des jar-files
		// falls bereits ein updatefile da ist wir dessen länge zurückgegeben
		File javaupdatefile = new File(Rootpath.getRootpath()
				+ "\\bin\\monitortool_update.jar");
		if (javaupdatefile.exists() == true)
		{
			int len = (int) javaupdatefile.length();
			return len;
		} else
		{
			String javafile = Rootpath.getRootpath() + "\\bin\\monitortool.jar";
			File jarfile = new File(javafile);
			int len = (int) jarfile.length();
			return len;
		}
	}

	static private void updateMonitortool()
	{
		FileAccessDyn fd= new FileAccessDyn();
		// das quellfile bestimmen
		String quellfile = Rootpath.getRootpath()
				+ "\\bin\\monitortool_update.jar";
		String zielfile = Rootpath.getRootpath() + "\\bin\\monitortool.jar";

		// check quell und zielfile
		if (FileAccess.FileAvailable(quellfile) == false)
		{
			Mbox.Infobox("Update error quellfile <" + quellfile
					+ "> not available ");
			return;
		}
		if (FileAccess.FileAvailable(zielfile) == false)
			Tracer.WriteTrace(20, "Update error destfile <" + zielfile
					+ "> not available");


		// sicherung erstellen
		fd.copyFile2(zielfile, zielfile + ".save");

		// meldung ausgeben
		if (fd.copyFile2(quellfile, zielfile) == true)
		{
			File qfile = new File(quellfile);
			qfile.delete();
			Mbox.Infobox("I copied update from <" + quellfile + "> to <"
					+ zielfile + "> please restart");
			System.exit(0);
		}
	}

	static public boolean checkNewUpdate()
	{
		String quellfile = Rootpath.getRootpath()
				+ "\\bin\\monitortool_update.jar";

		File quell = new File(quellfile);

		//falls quellfile da ist und filelänge =0, dann lösche
		if(quell.exists()&&quell.length()==0)
		{
			Tracer.WriteTrace(20, "Warning: updatefile <"+quellfile+"> has length zero, I delete it");
			quell.delete();
			return false;
		}
		
		if(GlobalVar.getAskforUpdateflag()==1)
		if (quell.exists())
		{
			int installflag = 0;
			installflag = MboxQuest
					.Questbox("New update is ready, do you want to update ?");

			if (installflag == 1)
				updateMonitortool();
		}

		return true;
	}
}
