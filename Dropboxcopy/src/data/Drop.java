package data;

import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Sys;
import hilfsklasse.Tracer;

import java.io.File;

import mtools.Mlist;

public class Drop
{
	protected String traderexe = null;

	public Drop()
	{
		super();
	}

	protected void cleanMqlCache(String path)
	{
		if(FileAccess.FileAvailable(path+"\\mqlcache.dat")==true)
			FileAccess.FileDelete(path+"\\mqlcache.dat", 0);
	}
	
	protected void cleanMetatraderDir(String path)
	{
		FileAccessDyn fdyn= new FileAccessDyn();
		fdyn.initFileSystemList(path, 1);
		
		int anz=fdyn.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String fnam=path+"\\"+fdyn.holeFileSystemName();
			if(	(fnam.endsWith(".ex4"))||
				(fnam.endsWith(".mq4"))||
				(fnam.endsWith(".log"))||
				(fnam.endsWith(".dat")))
			{
				FileAccess.FileDelete(fnam, 0);
			}
			
		}
	}
	
	protected void cleanProfileDir(String path)
	{
		FileAccessDyn fdyn= new FileAccessDyn();
		fdyn.initFileSystemList(path, 1);
		
		int anz=fdyn.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String fnam=path+"\\"+fdyn.holeFileSystemName();
			if(	(fnam.endsWith(".chr")))
			{
				FileAccess.FileDelete(fnam, 0);
			}
			
		}
	}
	
	private String getTradername()
	{
		// c:\\forex\\"metatrader1"\\terminal.exe
		// c:\\forex\\Mtx\\Verz1\\"Metatrader1"\\terminal.exe
		String metaname = "";

		if (traderexe.contains("terminal.exe") == true)
		{
			// schneide terminal.exe ab
			metaname = traderexe.substring(0,
					traderexe.lastIndexOf("\\terminal.exe"));
			// schneide die pfade davor ab
			metaname = metaname.substring(metaname.lastIndexOf("\\") + 1);
		} else if (traderexe.contains("Forex Strategy Trader.exe"))
		{
			metaname = traderexe.substring(0,
					traderexe.lastIndexOf("\\Forex Strategy Trader.exe"));
			metaname = metaname.substring(metaname.lastIndexOf("\\") + 1);
		} else
			Tracer.WriteTrace(10, "Error: trader.exe missing traderexe<"
					+ traderexe + ">");

		return metaname;
		// bsp: return "metatrader1"
	}

	protected String getTraderroot()
	{
		// metatraderexe=c:\\forex\\metatrader1\\terminal.exe
		// rootpath=c:\\forex\\metatrader1
		String rootpath = traderexe.substring(0, traderexe.lastIndexOf("\\"));
		return rootpath;
	}

	protected String getDropFSTRootdir()
	{
		// return:
		// "C:\Users\tnickel\Dropbox\MonitorCom\tnickel-PC_MT4_ActiveTrades1"
		String getDropRootdir = DropGlobalVar.getDropboxdir() + "\\"
				+ Sys.getHostname() + "_FST_" + getTradername();
		return getDropRootdir;
	}

	protected String getDropMTRootdir()
	{
		// return:
		// "C:\Users\tnickel\Dropbox\MonitorCom\tnickel-PC_MT4_ActiveTrades1"
		String getDropRootdir = DropGlobalVar.getDropboxdir() + "\\"
				+ Sys.getHostname() + "_MT4_" + getTradername();
		return getDropRootdir;
	}

	
	
	protected boolean CopyFileLencheck(String fnamquell, String fnamziel)
	{
		// prüft nach ob kopiert werden sollte
		File fquell = new File(fnamquell);
		File fziel = new File(fnamziel);
		FileAccessDyn fd= new FileAccessDyn();

		// änderungsdatum vergleichen und wenn ungleich
		// den historyexporter in die dropbox kopieren

		// Falls Quelle existiert und ziel nicht
		Boolean condA = ((fquell.exists()) && (fziel.exists() == false));
		// falls beide existieren aber länge unterschiedlich
		Boolean condB = ((fquell.exists() && fziel.exists()) && (fquell
				.length() != fziel.length()));

		if (condA || condB)
		{
			Mlist.add("quellexits<"+fquell.exists()+"> zielexits <"+fziel.exists()+">");
			Mlist.add("copy <" + fnamquell + "> q<"+fquell.length()+"> z<"+fnamziel+"> <"+fziel.length()+">", 1);
			fd.copyFile2(fnamquell, fnamziel);
			return true;
		}
		return false;
	}
	protected boolean CopyFileLastAccesscheck(String fnamquell, String fnamziel)
	{
		FileAccessDyn fd= new FileAccessDyn();
		// prüft nach ob kopiert werden sollte
		File fquell = new File(fnamquell);
		File fziel = new File(fnamziel);

		// änderungsdatum vergleichen und wenn ungleich
		// den historyexporter in die dropbox kopieren

		// Falls Quelle existiert und ziel nicht
		Boolean condA = ((fquell.exists()) && (fziel.exists() == false));
		// falls beide existieren aber unterschiedliches last access
		Boolean condB = (fquell.lastModified()!=fziel.lastModified());

		if (condA || condB)
		{
			Mlist.add("copy <" + fnamquell + "> q<"+fquell.length()+"> z<"+fnamziel+"> <"+fziel.length()+">", 1);
			fd.copyFile2(fnamquell, fnamziel);
			return true;
		}
		return false;
	}
	protected void MoveFile(String fnamquell, String fnamziel)
	{
		FileAccessDyn fd= new FileAccessDyn();

		Mlist.add("move <" + fnamquell + ">", 1);
		fd.copyFile2(fnamquell, fnamziel);
		FileAccess.FileDelete(fnamquell, 0);
	}

	protected void DropboxCopyDelExe(String quellpath, String zielpath,
			String postfix, String delpostfix)
	{
		// kopiert alle mql-files vom quellpath zum zielpath
		// Es werden anschliessend alle del-postfixe im ziel gelöscht
		// ("bsp. *.ex4")

		FileAccess.initFileSystemList(quellpath, 1);
		int anz = FileAccess.holeFileAnz();
		// betrachte bestimmte files in quellverzeichniss
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			// falls der falsche Name, dann weiter
			if (postfix != null)
				if (fnam.endsWith(postfix) == false)
					continue;

			String zielnam = zielpath + "\\" + fnam;
			String quellnam = quellpath + "\\" + fnam;
			if (CopyFileLencheck(quellnam, zielnam) == true)
			{
				if ((postfix == null) || (delpostfix == null))
					continue;
				// es wurde kopiert, also lösche das ex4
				File fzielexe = new File(zielnam.replace(postfix, delpostfix));
				fzielexe.delete();
			}
		}

	}

	protected void DelDropEndingFile(String fnam, String postfix,
			String delfix1, String delfix2)
	{
		// für das file mit postfix werden zwei namensgleiche files mit
		// unterschiedlichen postfixes (delfixes) gelöscht
		// Beispiel:
		// fnam:c:\\metatrader\\experts\\files\\5005.on, ".on", ".off",
		// ".off.ok"
		// fnam=c:\\metatrader\\experts\\files\\5005.on
		// Die files
		// c:\\metatrader\\experts\\files\\5005.off
		// und
		// c:\\metatrader\\experts\\files\\5005.off.ok
		// werden gelöscht

		String tmpnam1 = fnam.replace(postfix, delfix1);
		File ftemp = new File(tmpnam1);
		if (ftemp.exists() == true)
			ftemp.delete();

		String tmpnam2 = fnam.replace(postfix, delfix2);
		File ftemp2 = new File(tmpnam2);
		if (ftemp2.exists() == true)
			ftemp2.delete();
	}

	protected void InitVerz(String path)
	{
		// das Hauptverzeichniss anlegen
		File fna = new File(path);
		if (fna.exists() == false)
			fna.mkdir();
	}
}