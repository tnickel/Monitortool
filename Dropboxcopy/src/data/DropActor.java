package data;

import ftool.DropFile;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import mtools.Mlist;
import datefunkt.Mondate;

public class DropActor extends Drop
{
	// pfad wo sich das metatraderroot befindet

	int activeflag;
	// zeit wann das logfile das letzte mal kopiert wurde
	private Date lastlogcopytime = null;

	// 1: system is valid
	// 0: system is deleted
	int validflag = 0;
	// type=1 dann metatrader
	// type=2 dann Fst
	int type = 0;

	public DropActor()
	{

	}

	public String getTraderexe()
	{
		return traderexe;
	}

	public void setTraderexe(String metatraderexe)
	{
		this.traderexe = metatraderexe;
	}

	public int getActiveflag()
	{
		return activeflag;
	}

	public void setActiveflag(int activeflag)
	{
		this.activeflag = activeflag;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getValidflag()
	{
		return validflag;
	}

	public void setValidflag(int validflag)
	{
		this.validflag = validflag;
	}

	public void initdropbox()
	{

		if (type == 1)
		{
			String droprootdir = getDropMTRootdir();
			InitVerz(droprootdir);
			InitVerz(droprootdir + "\\experts");
			InitVerz(droprootdir + "\\logs");
			InitVerz(droprootdir + "\\profiles");
			InitVerz(droprootdir + "\\experts\\files");
			InitVerz(droprootdir + "\\experts\\indicators");
			InitVerz(droprootdir + "\\experts\\include");
			InitVerz(droprootdir + "\\profiles\\default");

		} else if (type == 2)
		{
			String droprootdir = getDropFSTRootdir();
			InitVerz(droprootdir);
			InitVerz(droprootdir + "\\Strategies");
		} else
			Tracer.WriteTrace(10, "Error: internal error 12344");
	}

	public void copyHistoryexporter()
	{
		if (type != 1)
			return;

		String fnamquell1 = getTraderroot() + "\\experts\\files\\history.txt";
		String fnamziel1 = getDropMTRootdir() + "\\experts\\files\\history.txt";
		String fnamquell2 = getTraderroot()
				+ "\\experts\\files\\history_small.txt";
		String fnamziel2 = getDropMTRootdir()
				+ "\\experts\\files\\history_small.txt";
		String fnamquell3 = getTraderroot()
				+ "\\experts\\files\\history_open.txt";
		String fnamziel3 = getDropMTRootdir()
				+ "\\experts\\files\\history_open.txt";

		String exporterlockfile = getTraderroot()
				+ "\\experts\\files\\exporter.lock";
		String monitorlockfile = getTraderroot()
				+ "\\experts\\files\\monitor.lock";

		// schaut nach ob der historyexporter gerade schreibt, wartet max 240
		// sekunden
		if (DropFile.waitFreeLock(exporterlockfile, 240) == false)
			return;

		DropFile.setLock(monitorlockfile);

		CopyFileLencheck(fnamquell1, fnamziel1);
		CopyFileLencheck(fnamquell2, fnamziel2);
		CopyFileLencheck(fnamquell3, fnamziel3);

		DropFile.freeLock(monitorlockfile);
	}

	public void copyOldHistoryexporter()
	{
		if (type != 1)
			return;

		String exporterlockfile = getTraderroot()
				+ "\\experts\\files\\exporter.lock";
		String monitorlockfile = getTraderroot()
				+ "\\experts\\files\\monitor.lock";

		String fnamquelldir = getTraderroot() + "\\experts\\files";

		FileAccess.initFileSystemList(fnamquelldir, 1);

		// schaut nach ob der historyexporter gerade schreibt, wartet max 240
		// sekunden
		if (DropFile.waitFreeLock(exporterlockfile, 240) == false)
			return;

		DropFile.setLock(monitorlockfile);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.contains("history_expired") == false)
				continue;

			String fnamziel = getDropMTRootdir() + "\\experts\\files\\" + fnam;
			CopyFileLencheck(getTraderroot() + "\\experts\\files\\" + fnam,
					fnamziel);

		}
		DropFile.freeLock(monitorlockfile);

	}

	public void exportEas()
	{
		// hier werden die ea´s in die dropbox zurückexportiert
		// es wird die dateilänge verglichen
		// ist die gleich wird nix gemacht
		if (type == 1)
		{
			// export geht nur beim metatrader

			// die experts werden in die dropbox kopiert
			String zielpath = getDropMTRootdir() + "\\experts";
			String quellpath = getTraderroot() + "\\experts";
			DropboxCopyDelExe(quellpath, zielpath, ".mq4", null);

			// kopiere die indikatoren
			zielpath = getDropMTRootdir() + "\\experts\\indicators";
			quellpath = getTraderroot() + "\\experts\\indicators";
			DropboxCopyDelExe(quellpath, zielpath, ".mq4", null);

			// kopiere die include
			zielpath = getDropMTRootdir() + "\\experts\\include";
			quellpath = getTraderroot() + "\\experts\\include";
			DropboxCopyDelExe(quellpath, zielpath, ".mqh", "");

			// kopiere die profiles
			zielpath = getDropMTRootdir() + "\\profiles\\default";
			quellpath = getTraderroot() + "\\profiles\\default";
			DropboxCopyDelExe(quellpath, zielpath, ".chr", null);
			
			// kopiere die *.txt files
			// dies sind die historyfiles incl. der history expired
			zielpath = getDropMTRootdir() + "\\experts\\files";
			quellpath = getTraderroot() + "\\experts\\files";
			DropboxCopyDelExe(quellpath, zielpath, ".txt", null);
		}

	}

	public void installEas(boolean cleanflag)
	{
		// das alte löschen
		if (cleanflag == true)
		{
			cleanMetatraderDir(getTraderroot() + "\\experts");
			cleanProfileDir(getTraderroot() + "\\profiles\\default");
		}

		cleanMqlCache(getTraderroot() + "\\experts");

		if (type == 1)// Metatrader
		{
			// installiere die *.mq4
			String quellpath = getDropMTRootdir() + "\\experts";
			String zielpath = getTraderroot() + "\\experts";
			DropboxCopyDelExe(quellpath, zielpath, ".mq4", ".ex4");

			// installiere die *.ex4
			DropboxCopyDelExe(quellpath, zielpath, ".ex4", null);
			
			// installiere die indikatoren
			quellpath = getDropMTRootdir() + "\\experts\\indicators";
			zielpath = getTraderroot() + "\\experts\\indicators";
			DropboxCopyDelExe(quellpath, zielpath, ".mq4", ".ex4");

			// installiere include
			quellpath = getDropMTRootdir() + "\\experts\\include";
			zielpath = getTraderroot() + "\\experts\\include";
			DropboxCopyDelExe(quellpath, zielpath, ".mqh", ".ex4");

			// installiere profiles
			quellpath = getDropMTRootdir() + "\\profiles\\default";
			zielpath = getTraderroot() + "\\profiles\\default";
			DropboxCopyDelExe(quellpath, zielpath, null, null);

			//installiere die history, history expired
			quellpath = getDropMTRootdir() + "\\experts\\files";
			zielpath = getTraderroot() + "\\experts\\files";
			DropboxCopyDelExe(quellpath, zielpath, ".txt", null);
			
			
			// installiere RestartTerminal.bat
			quellpath = getDropMTRootdir();
			zielpath = getTraderroot();
			DropboxCopyDelExe(quellpath, zielpath, ".bat", null);
			
			//kopiere monitor.info
			quellpath = getDropMTRootdir();
			zielpath = getTraderroot() ;
			DropboxCopyDelExe(quellpath, zielpath, ".info", null);
			
		} else if (type == 2)// FSB
		{
			// die xml-files kopieren
			String quellpath = getDropFSTRootdir() + "\\Strategies";
			String zielpath = getTraderroot() + "\\Strategies";
			DropboxCopyDelExe(quellpath, zielpath, ".xml", "");

			// das startbatch kopieren
			quellpath = getDropFSTRootdir();
			zielpath = getTraderroot();
			DropboxCopyDelExe(quellpath, zielpath, ".bat", "");
		} else if (type == 3)
		{
			return;
		} else
			Tracer.WriteTrace(10, "Error: internal error 123344");
	}
	public void deleteEas()
	{
	
		//mqlcache löschen
		cleanMqlCache(getTraderroot() + "\\experts");

		//deletedir erzeugen
		String deletedir=getTraderroot() + "\\deleted";
		File deletedirf = new File(deletedir);
		if(deletedirf.exists()==false)
			deletedirf.mkdir();
		
	
		
		if (type == 1)// Metatrader
		{
			//löscht die files in der dropbox und im metatrader
			accessDeleteFiles(getDropMTRootdir() + "\\experts",getTraderroot() + "\\experts",deletedir);
			//löscht die files in der Dropbox und im metatrader
			accessDeleteFiles(getDropMTRootdir() + "\\profiles\\default",getTraderroot() + "\\profiles\\default",deletedir);
		}  
	}
	
	private void accessDeleteFiles(String quellpath, String zielpath,String deletedir)
	{
		//quellpath: dies ist das dropboxverzeichniss
		//zielpath: dies ist das Metatraderverzeichniss
		//deletedir: dies ist das verzeichniss wo das backup angelegt wird, es wird also nix gelöscht nur verschoben
		
	
	
		DropMagicliste dropmagicliste_dropbox= new DropMagicliste(quellpath);
		
		
	

		//wende diese magicliste auf den Metatrader an
		FileAccess.initFileSystemList(zielpath, 1);
		int anz=FileAccess.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			//den filenamen ohne pfad holen
			String fnam=FileAccess.holeFileSystemName();
			
			if(fnam==null)
				return;

			//falls file gelöscht werden soll
			if(dropmagicliste_dropbox.checkDeleteFile(fnam)==true)
			{
				//das file in dem metatrader in den deleted ordner moven
				FileAccess.FileMove(zielpath+"\\"+fnam, deletedir+"\\"+fnam);
				Mlist.add("i: delete file<"+fnam+">");
			}
		}
		
		
		
		
		//wende diese magicliste auf die Dropbox an
		FileAccess.initFileSystemList(quellpath, 1);
		 anz=FileAccess.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			//den filenamen ohne pfad holen
			String fnam=FileAccess.holeFileSystemName();
			
			if(fnam==null)
				return;

			//falls file gelöscht werden soll
			if(dropmagicliste_dropbox.checkDeleteFile(fnam)==true)
			{
				Mlist.add("i: delete file<"+fnam+">");
				
				//dann muss noch das file aus der dropbox entfernt werden
				File fnamf1 = new File(quellpath+"\\"+fnam);
				fnamf1.delete();
			}
			
		}
		
	}
	

	

	public void IoHinrichtung()
	{
		if (type != 1)
			return;

		// kopiere
		// a) *.on ->(lösche *.off und *.off.ok)
		// b) *.off ->(lösche *.on und *.on.ok)
		// c) closeaccount.start ->
		String quellpath = getDropMTRootdir() + "\\experts\\files";
		String zielpath = getTraderroot() + "\\experts\\files";

		FileAccess.initFileSystemList(quellpath, 1);
		int anz = FileAccess.holeFileAnz();
		// betrachte bestimmte files in quellverzeichniss
		for (int i = 0; i < anz; i++)
		{

			String dropnam = FileAccess.holeFileSystemName();

			if (dropnam.toLowerCase().contains("closeaccount.start"))
			{
				String zielnam = zielpath + "\\" + dropnam;
				String quellnam = quellpath + "\\" + dropnam;
				MoveFile(quellnam, zielnam);
				continue;
			}
			if (DropGlobalVar.getDropcontrol())
				if (dropnam.endsWith(".on"))
				{
					String zielnam = zielpath + "\\" + dropnam;
					String quellnam = quellpath + "\\" + dropnam;
					CopyFileLencheck(quellnam, zielnam);
					DelDropEndingFile(quellnam, ".on", ".off", ".off.ok");
				}
			if (DropGlobalVar.getDropcontrol())
				if (dropnam.endsWith(".off"))
				{
					String zielnam = zielpath + "\\" + dropnam;
					String quellnam = quellpath + "\\" + dropnam;
					CopyFileLencheck(quellnam, zielnam);
					DelDropEndingFile(quellnam, ".off", ".on", ".on.ok");
				}
		}
	}

	public void copyLotfiles()
	{
		if (type != 1) // nur metatraderaccounts bearbeiten
			return;

		// kopiere
		// a) *.lot von Dropbox in den Metatrader (wenn dropbox neuer)
		String quellpath = getDropMTRootdir() + "\\experts\\files";
		String zielpath = getTraderroot() + "\\experts\\files";
		lotcopy(quellpath, zielpath);

		// b) vom Meatrader in die Dropbox (wenn metatrader neuer)
		// und die Rückrichtung
		lotcopy(zielpath, quellpath);
	}

	public void copyLogfiles(int minsekunden)
	{
		if (type != 1) // nur metatraderaccounts bearbeiten
			return;

		Date dt = Mondate.getAktDate();

		// mache den vergleich nur jede x sekunden
		if ((lastlogcopytime != null)
				&& (Mondate.SekundenVergangen(lastlogcopytime) < minsekunden))
			return;
		else
			lastlogcopytime = dt;

		// logfiles vom Metatrader in die dropbox kopieren
		String quellpath = getTraderroot() + "\\logs";
		String zielpath = getDropMTRootdir() + "\\logs";
		logcopy(quellpath, zielpath);
	}

	private void lotcopy(String quellpath, String zielpath)
	{
		// die lotfiles werden von der quelle zum ziel kopiert
		FileAccess.initFileSystemList(quellpath, 1);
		int anz = FileAccess.holeFileAnz();
		// Phase 1) dropbox in den Metatrader
		for (int i = 0; i < anz; i++)
		{
			String dropnam = FileAccess.holeFileSystemName();

			if (dropnam.endsWith(".lot"))
			{
				String zielnam = zielpath + "\\" + dropnam;
				String quellnam = quellpath + "\\" + dropnam;

				File quellnamf = new File(quellnam);
				File zielnamf = new File(zielnam);

				// falls das quellfile neuer ist

				int quellmodified = (int) ((quellnamf.lastModified()) / 1000);
				int zielmodified = (int) ((zielnamf.lastModified()) / 1000);

				if (quellmodified > zielmodified)
					CopyFileLastAccesscheck(quellnam, zielnam);
				else
				// falls das zielfile neuer ist
				if (quellmodified < zielmodified)
					CopyFileLastAccesscheck(zielnam, quellnam);
			}
		}

	}

	public void logcopy(String quellpath, String zielpath)
	{
		Date aktdat = Mondate.getAktDate();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

		String aktdatstring = sdf.format(aktdat);

		// die logfiles werden von der quelle zum ziel kopiert
		FileAccess.initFileSystemList(quellpath, 1);
		int anz = FileAccess.holeFileAnz();
		// Phase Metatrader in die Dropbox
		for (int i = 0; i < anz; i++)
		{
			String dropnam = FileAccess.holeFileSystemName();

			// nur das aktuelle datum kopieren
			if (dropnam.contains(aktdatstring) == false)
				continue;

			// kopiere nur wenn die datei neuer ist
			if (dropnam.endsWith(".log"))
			{
				String zielnam = zielpath + "\\" + dropnam;
				String quellnam = quellpath + "\\" + dropnam;

				File quellnamf = new File(quellnam);
				File zielnamf = new File(zielnam);

				// falls das quellfile neuer ist
				int quellmodified = (int) ((quellnamf.lastModified()) / 1000);
				int zielmodified = (int) ((zielnamf.lastModified()) / 1000);

				if (quellmodified > zielmodified)
					CopyFileLastAccesscheck(quellnam, zielnam);

			}
		}
		
		//altes zeug aus der Dropbox löschen
		FileAccess.initFileSystemList(zielpath, 1);
		anz = FileAccess.holeFileAnz();
		
		for (int i = 0; i < anz; i++)
		{
			String dropnam = FileAccess.holeFileSystemName();
			//das aktuelle datum wird nicht gelöscht
			if (dropnam.contains(aktdatstring) == true)
				continue;
			
			if(dropnam.contains(".log")==true)
			{
				File fnam=new File(zielpath+"\\"+dropnam);
				fnam.delete();
				Mlist.add("delete <" +fnam.getAbsolutePath()+">", 1);
			}
		}
		
	}

	public void IoRueckrichtung()
	{
		if (type != 1)
			return;
		// kopiere
		// a) <magic>.ok
		// b) closeaccount.ok (lösche closeaccount.start auf dropbox)

		String quellpath = getTraderroot() + "\\experts\\files";
		String zielpath = getDropMTRootdir() + "\\experts\\files";

		FileAccess.initFileSystemList(quellpath, 1);
		int anz = FileAccess.holeFileAnz();
		// betrachte bestimmte files in quellverzeichniss
		for (int i = 0; i < anz; i++)
		{

			String fnami = FileAccess.holeFileSystemName();
			String quellnam = quellpath + "\\" + fnami;
			if (quellnam.endsWith(".ok"))
			{
				String zielnam = zielpath + "\\" + fnami;
				CopyFileLencheck(quellnam, zielnam);

				if (quellnam.endsWith("closeaccount.ok"))
				{

					// lösche closeaccount.start
					zielnam = zielnam.replace("closeaccount.ok",
							"closeaccount.start");
					File fna = new File(zielnam);
					fna.delete();
				}
			}
		}
	}

	public void restartcheck()
	{
		String quellpath = getDropMTRootdir() + "\\experts\\files";
		String zielpath = getTraderroot() + "\\experts\\files";

		FileAccess.initFileSystemList(quellpath, 1);
		int anz = FileAccess.holeFileAnz();
		// betrachte bestimmte files in quellverzeichniss
		for (int i = 0; i < anz; i++)
		{

			String fnami = FileAccess.holeFileSystemName();
			String quellnam = quellpath + "\\" + fnami;
			if (quellnam.contains("restart.start") == true)
			{
				String zielnam = zielpath + "\\" + fnami;
				if (CopyFileLencheck(quellnam, zielnam) == true)
				{
					Mlist.add("***RESTART <" + getTraderroot() + "> ***");
					// lösche den restart.start damit das nur einmal gemacht
					// wird
					FileAccess.FileDelete(quellnam, 0);
				}
			}
		}

	}

	public void restartMetatrader()
	{
		String zielpath = getTraderroot() + "\\experts\\files";
		Inf inf = new Inf();
		inf.setFilename(zielpath + "\\restart.start");
		inf.writezeile("restart");
		inf.close();
	}
}
