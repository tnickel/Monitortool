package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

public class HistoryExpired extends TableViewBasic
{
	private HashSet<String> trset = new HashSet<String>();
	
	
	public HistoryExpired(String expsetfile)
	{
		File fnam_f = new File(expsetfile);
		if (fnam_f.exists() == false)
			return;

		// lade schon mal das vorhandene set file
		addfileHashset(expsetfile);
	}

	public void CompressExpiredDatabase(String dir)
	{
		// baut die ExpiredDatabase des Verzeichnisses auf
		// 1) lese die ganzen Expired in einer Menge ein
		// 2) speichere das summierte expired als tmp-file
		// 3) Prüfe ob tmp-file ok
		// 4) move die anderen expired files ins wastedir
		// 5) Kopiere das tmp-file als neues history_expset_ - file
		int neuflag = 0;
		String wastedir = dir + "\\wastedir";
		File wastedir_f = new File(wastedir);

		// es wird nix gelöscht, alles gelöschte kommt ins wastedir
		if (wastedir_f.exists() == false)
			wastedir_f.mkdir();

		// 1) lese die ganze expired menge ein
		FileAccess.initFileSystemList(dir, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			String fnam4 = dir + "/" + fnam;
			if (fnam4.contains("history_expired_") == false)
				continue;

			// es kam was neues hinzu
			neuflag++;
			// Es wurde eine history_expired gefunden=> dann baue die Tradebasis
			// für
			// dieses file auf
			Tracer.WriteTrace(20, "I: compress expired file <" + fnam + ">");
			addfileHashset(fnam4);
		}
		// falls nix neues dann raus
		if (neuflag == 0)
			return;
		else
			Tracer.WriteTrace(20, "I:#<" + neuflag
					+ "> neue Dateien für Database<" + dir + "> hinzugefügt");

		// 2)speichere als tmp
		storefileHashset(dir + "\\history_expset_tmp");

		// 3)prüfe ob history_expset ok ist
		File fna = new File(dir + "\\history_expset");
		if ((fna.exists()) && (fna.length() < 10))
		{
			Tracer.WriteTrace(10, "E:file<" + fna.getName()
					+ "> to short/broken < 5 bytes");
			return;
		}

		// 4) move all expired to wastedir
		FileAccess.initFileSystemList(dir, 1);
		anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			String fnam4 = dir + "/" + fnam;
			if (fnam4.contains("history_expired_") == false)
				continue;

			// packe ins waste verzeichniss
			FileAccess.FileMove(dir + "\\" + fnam, wastedir + "\\" + fnam);

		}

		// 5) kopiere um
		String tfile = dir + "\\history_expset_tmp";
		File tfilef = new File(tfile);
		if (tfilef.exists())
			FileAccess.FileMove(dir + "\\history_expset_tmp", dir
					+ "\\history_expset_");
	}

	private void addfileHashset(String fnam)
	{
		int myfxbookflag=0;
		
		if(fnam.contains("myfxbook")==true)
			myfxbookflag=1;
		
		// läd das file in das hashset
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String line = null;

		while ((line = inf.readZeile()) != null)
		{
			if (line.contains("cancelled") == true)
				continue;
			
			if(myfxbookflag==0)
				trset.add(line);
			else
			{
				String convline=convertMyfxbookline(line);

				//falls man die line konvertieren konnte
				if(convline!=null)
					trset.add(convline);
			}
		}
		inf.close();
	}

	private String convertMyfxbookline(String line)
	{
		//convertiert eine myfxbookline in eine line vom historyexporter
		String convline="";

		
		convline=Trade.convMyfxbooktradezeile(line);
		return convline;
	}
	
	
	private void storefileHashset(String fnam)
	{
		// löscht altes Tmpfile falls vorhanden
		File inf_f = new File(fnam);
		if (inf_f.exists())
			inf_f.delete();

		// speichert das hashset auf platte
		Inf inf = new Inf();
		inf.setFilename(fnam);

		Iterator<String> it = trset.iterator();
		// HashSet wird mit dem Iterator durchlaufen
		while (it.hasNext())
		{
			// Next gibt das aktuelle HashSet-Objekt zurück
			// und geht zum nächsten über
			String setText = (String) it.next();
			// Ausgabe des jeweiligen HashSet-Elementes
			inf.writezeile(setText);
		}
		inf.close();
	}

}
