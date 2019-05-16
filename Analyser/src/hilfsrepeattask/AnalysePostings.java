package hilfsrepeattask;

import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import mainPackage.GC;
import objects.AktDbObj;
import objects.UserDbObj;
import stores.AktDB;
import stores.UserDB;

public class AnalysePostings
{
	// hier werden die postingsthreads aller benutzer verwaltet und
	// verarbeitet
	int useranz = 0;
	static int threadanzahl = 0;
	final static int maxthreadanz = 16000;

	private static DownloadManager dm = new DownloadManager(GC.MAXLOW);

	// 16000 threads maximal
	static private String[] PostingThreadName = new String[maxthreadanz]; // voller
	// threadname
	static private int[] ThreadId = new int[maxthreadanz];
	static private int[] ThreadIdZugriffe = new int[maxthreadanz];

	public AnalysePostings()
	{
	}

	private boolean checkAvailableCount(String name)
	{
		// prüft ob der thread schon gespeichert ist
		for (int i = 0; i < threadanzahl; i++)
		{
			if (PostingThreadName[i].equals(name) == true)
			{
				ThreadIdZugriffe[i]++;
				return true;
			}
		}
		return false;
	}

	private void addPostingThreads(String filename, AktDB aktdb)
	// addiert die postings aus ..db\\postingliste\\<username>.db
	// und fügt den Thread evtl. in die aktdb ein
	{
		BufferedReader br;
		String zeile = null;
		String threadname = null;
		String threadid = null;
		threadanzahl=0;
		
		if (FileAccess.FileAvailable(filename))
		{
			br = FileAccess.ReadFileOpen(filename);

			while (true)
			{
				zeile = FileAccess.ReadFileZeile(br);
				if (zeile == null)
					break;

				// wenn der user nix gepostet hat steht "#postings" in dem file
				if ((zeile.equalsIgnoreCase("#postings")))
					break;
				if (zeile.contains("*******") == true)
					continue;
				if (zeile.contains("ehrlichkeit") == true)
					break;

				// holt sich threadname und threadid aus dem jeweiligen
				// userprofil

				try
				{
					threadid = SG.nteilstring(zeile, "#", 1);
					threadname = SG.nteilstring(zeile, "#", 2);
				} catch (ToolsException e)
				{
					Tracer.WriteTrace(10, "E:Error= internal error 134343456");
					e.printStackTrace();
				}

				// schaut nach ob dieser threadname schon gespeichert
				// wenn nein speichere in temporärer postingliste und aktdb
				// und erhöhe den threadanzahl
				if (checkAvailableCount(threadname) == false)
				{
					System.out.println("threadid<" + threadid + "> added #<"
							+ threadanzahl + "> to Aktdb");
					PostingThreadName[threadanzahl] = new String(threadname);
					ThreadId[threadanzahl] = Integer.valueOf(threadid);
					AktDbObj aktdbobj = new AktDbObj();
					aktdbobj.setThreadid(ThreadId[threadanzahl]);

					aktdb.AddObject(aktdbobj);
					threadanzahl++;
				}
			}
			FileAccess.ReadFileClose(br);
		}
	}

	private void writePostingThreads(String file, int minzugriffe)
	/*
	 * Das Analyiserte wird in einem Datenfile geschrieben. Es wird eine neue
	 * Postingliste erstellt. Eine Postingliste beinhaltet
	 * Threadnamen#Threadid#x#0 Es werden nur threads in die Datenbasis(file)
	 * aufgenommen die eine Mindestzugriffszahl haben.
	 */
	{
		BufferedWriter bw;
		String zeile;

		bw = FileAccess.WriteFileOpen(file, "UTF-8");
		for (int i = 0; i < threadanzahl; i++)
		{
			if (ThreadIdZugriffe[i] > minzugriffe)
			{// FILTER
				zeile = new String(ThreadId[i] + "#" + ThreadIdZugriffe[i]
						+ "#" + PostingThreadName[i]);
				FileAccess.WriteFileZeile(bw, zeile);
			}
		}
		FileAccess.WriteFileClose(bw);

		bw = FileAccess.WriteFileOpen(file + "2", "UTF-8");
		for (int i = 0; i < threadanzahl; i++)
		{
			if (ThreadIdZugriffe[i] > minzugriffe)
			{// FILTER
				zeile = new String(PostingThreadName[i] + "#" + ThreadId[i]
						+ "#x#0");
				FileAccess.WriteFileZeile(bw, zeile);
			}
		}
		FileAccess.WriteFileClose(bw);
	}

	// Hier werden die Userpostings analysiert. Es wird in ..db//userdata
	// geschaut und
	// es werden hier die Postings-ids (threads) in einer liste gesammelt.
	// D.h. von allen useren werden alle posting-threads gesammelt.
	// anschlissend wird gefiltert und eine threadliste der postings wird unter
	// ..db//threadliste.txt abgespeichert.
	// Nur posting-threadlisten die den Filterkriterien genügen werden in dieser
	// threadliste gespeichert.
	// Die Filterkriterien werden in dem globalen propertie-file gespeichert.

	public void filtere_neue_threads_aus_postings(String file)
	{
		// Hier werden neue Threads gesucht die für weitere Useranalysen
		// verwendet werden
		// können. D.h. es wird davon ausgegangen das diese Threads komplett
		// runtergeladen
		// werden.
		// Die Threads werden anhand des Userverhaltens/Postverhaltens
		// ermittelt.
		// 1) Betrachte nur erfahrene User. Ein user ist erfahren wenn er 500
		// Postings abgesetzt hat und seit 2003 dabei ist
		// 2) Betrachte alle Posting-Threads dieser erfahrenen Benutzer
		// 3) Nimm einen Thread eines erfahrenen Benutzers hinzu wenn weitere 40
		// erfahrenen User auch in diesem sind
		// 4) Es wird eine Liste mit threadnamen#threadid.... etc generiert
		// 5) Diese List ist noch von Hand zu bearbeiten
		String username = null;
		int postanzahl = 0, filter_min_postings = 0, min_year = 0;
		String date = null;
		String min_posting_str = null;
		int foundcounter_level1 = 0;

		UserDB udb = new UserDB("observeuser.txt","boostraenge.txt");
		useranz = udb.GetanzObj();
		AktDB aktdb = new AktDB();
		DownloadManager dm = new DownloadManager(GC.MAXLOW);

		// Es werden nur threads in die Liste aufgenommen, die eine
		// Mindestzugriffszahl
		// bei 0 werden alle threads aufgenommen
		int mindestzugriffszahl = GC.FILTER_MIN_ZUGRIFFE;

		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) udb.GetObjectIDX(i);
			username = udbo.get_username();
			postanzahl = udbo.getAntworten();

			filter_min_postings = GC.FILTER_MIN_POSTINGS;
			if (postanzahl < filter_min_postings)
				continue;

			// das Mindestjahr FILTER_MIN_YEAR muss erreicht werden, ansonsten
			// betrachte user nicht
			date = udbo.getRegistriert();
			System.out.println("date<" + date + ">");
			date = date.substring(date.lastIndexOf(".") + 1, date.length());
			if (date.contains("?"))
				continue;
			min_year = GC.FILTER_MIN_YEAR;
			if ((Integer.valueOf(date)) > min_year)
				continue;

			System.out.println("check username<" + username + ">");
			foundcounter_level1++;

			udbo.WebUpdateStep3(dm, 1);

			// addiert die postings-threads des users
			addPostingThreads(GC.rootpath + "\\db\\postingliste\\" + username
					+ ".db", aktdb);
		}
		System.out.println("Analyse fertig, lev1_counter="
				+ foundcounter_level1);
		writePostingThreads(file, mindestzugriffszahl);
	}

}
