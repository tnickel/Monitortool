package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.DB;
import html.ThreadsPostElem;
import internetPackage.DownloadManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import mainPackage.GC;
import objects.ThreadDbObj;
import objects.UserDbObj;

//postbasis, hier in dieser Datenbank werden die postings gespeichert
//Dies ist nur eine Unterklasse !!!, der datenbankname wird übergeben
//beim Schreiben wird ein Sequencecheck durchgeführt, da wird also geprüft ob alle
//postings in der dbx sind
public class PostDBx
{

	static private int dbopenflag = 0;
	// userid

	// max 7000 user pro thread
	static private List<Integer>[] userid_aktthreadpos = new List[7000];
	// der sequencecheck wird benötigt um festzustellen ob alle postings
	// aufgenommen wurden
	static private Boolean[] checksequence;
	static private int maxsequence = 0;
	static private String fname = null;

	// das breakdate gibt das datumschrank an, die die ersten 20 % signalisiert
	// der breakdatepostindex ist der zugehörige index zum breakdate
	static private String breakdate = null;
	static private int breakdatePostindex = 0;

	// username wird hier mit arraypos indentifiziert (erste Dimension)
	static HashMap<String, Integer> userpos = new HashMap<String, Integer>();
	static HashMap<Integer, String> userpos_reverse = new HashMap<Integer, String>();

	private int nextfreeuserid = 0;
	private SG sg = new SG();

	// hilfsvariablen
	private int anzvalidpostingdates_glob = -99;

	public boolean PostDB()
	{

		ReadDB(dbopenflag);

		return true;
	}

	public boolean ReadDB(int openflag)
	{
		dbopenflag = openflag;

		// System.out.println("constructor PostDB");
		return true;
	}

	public boolean Reset()
	{

		return true;

	}

	private boolean READPOSTDBX(ThreadDbObj tdbo, UserDB udb)
	{

		String filedbnam = GC.rootpath + "\\db\\threaddata\\"
				+ tdbo.getThreadname() + "_" + tdbo.getThreadid() + ".db";
		// falls schon geladen
		if (userpos.size() != 0)
			return true;

		Boolean postdbavailable = ReadPostDB(filedbnam);

		// Wenn keine Postdb aus irgendwelchen Gründen nicht da ist wird sie
		// aufgebaut
		// Im Normalfall sollte aber die Postdb verfügbar sein
		if (postdbavailable == false)
		{
			// übergib ein unmögliches Breakdate damit alles gespeichert wird
			// Das Breakdate wir ja hier überhaupt nicht gesucht

			Tracer.WriteTrace(20, "Warning: Postdbx<" + filedbnam
					+ "> nicht verfügbar, baue sie neu auf");
			BuildPostinglistEinThread(0, tdbo.getPageanz() * 10, "2040-08-01",
					0, udb, tdbo, 0);
			WritePostDB(filedbnam, udb);
		}
		return true;
	}

	public int calcAnzUser(ThreadDbObj tdbo, UserDB udb)
	{
		// ermittelt die anzahl der verschiedenen user für eine Postdbx
		// wenn die postdbx noch nicht da ist dann muss die erst mal geladen
		// werden
		READPOSTDBX(tdbo, udb);
		int anz = userpos.size();
		return anz;
	}

	public int calcAnzPostings(ThreadDbObj tdbo, UserDB udb,int flag, String uname)
	{
		// ermittelt die anzahl der verschiedenen Postings in der Postdbx
		// wenn die postdbx noch nicht da ist dann muss die erst mal geladen
		// werden
		READPOSTDBX(tdbo, udb);
		if(flag==GC.PDBALLPOST)
		{
			//ermittle die gesammtpostanzahl
			int anz = maxsequence;
			return anz;
		}
		else if(flag==GC.PDBUSER)
		{
			//ermittle die postanzahl für einen user
			if(uname==null)
				Tracer.WriteTrace(10, "Error: internal username muss != null");

			int anz=getUserPostinganz(uname);
			return anz;
		}
		else
			Tracer.WriteTrace(10, "Error: internal nicht erlaubtes flag");
		return 0;
	}

	public int calcAnzValidPostings(ThreadDbObj tdbo, UserDB udb)
	{
		// ermittelt die anzahl der verschiedenen 'valid'Postings in der Postdbx
		// -Ein Posting ist valid wenn das regdatum des user im korrekten format
		// ist
		// -Wenn die postdbx noch nicht da ist dann muss die erst mal geladen
		// werden

		if (maxsequence == 0)
		{
			Tracer
					.WriteTrace(
							20,
							"Warning: Ablauffehler, erst a)calcDurchschnittAktienerfahrungTage dann b)calcAnzValidPostings ");
		}

		return anzvalidpostingdates_glob;
	}

	public float calcDurchschnittAktienerfahrungTage(ThreadDbObj tdbo,
			UserDB udb)
	{
		// ermittelt die durchschnittliche Aktienerfahrung in Tagen in der
		// Postdbx
		String regstr = null;
		CleanPostDB();
		anzvalidpostingdates_glob = 0;

		READPOSTDBX(tdbo, udb);
		int anzuser = userpos.size();
		// hier wird gezaehlt ob dies Posting auch einen postenden user mit
		// korrekten
		// datum hat, wenn nein wird dieser nicht betrachtet.

		int anzpostings = maxsequence;
		float sumregtage = 0;
		float aktienerfahrung = 0;

		// hier wird die durchschnittliche Aktienerfahrung ermittelt
		for (int i = 0; i < anzuser; i++)
		{
			String unam = userpos_reverse.get(i);
			UserDbObj udbo = udb.getUserobj(unam);

			if (udbo != null)
				regstr = udbo.getRegistriert();
			else
			{
				Tracer.WriteTrace(20, "Warning: regstrg==null, unam<" + unam
						+ ">");
				continue;

			}
			if (Tools.isDate(regstr) == true)
			{

				int userregtage = Tools.zeitdifferenz_tage(Tools
						.get_aktdatetime_str(), regstr);
				int anzuserpostings = userid_aktthreadpos[i].size();
				sumregtage = sumregtage + (userregtage * anzuserpostings);
				anzvalidpostingdates_glob = anzvalidpostingdates_glob
						+ anzuserpostings;
			}
		}
		if (anzvalidpostingdates_glob != anzpostings)
		{
			Tracer.WriteTrace(20, "Warning: CalcThreadranking Threadpostings<"
					+ anzpostings + "> != validPostingdates<"
					+ anzvalidpostingdates_glob + "> [ungültige?, alte user?]");
		}

		if (anzvalidpostingdates_glob > 0)
		{
			aktienerfahrung = sumregtage / anzvalidpostingdates_glob;
			return aktienerfahrung;
		}
		return 0;
	}

	private Boolean PostNummernCheck(int aktpostid, String fnam,
			DB<ThreadsPostElem> html, ThreadDbObj tdbo)
	{
		// überprüfe den wertebereich der postnummer zur seitenzahl
		int seitenzaehler = 0;
		int minpost = 0, maxpost = 0;

		if (fnam.contains(".html") == false)
			Tracer.WriteTrace(10, "Error:Pagezahl kann nicht aus <" + fnam
					+ "> extrahiert werden .html.gzip fehlt");

		String zahlstr = fnam.substring(0, fnam.indexOf(".html"));
		zahlstr = zahlstr.substring(zahlstr.lastIndexOf("_") + 1);
		if ((seitenzaehler = SG.get_zahl(zahlstr)) < 0)
			Tracer.WriteTrace(10, "Error:Pagezahl kann nicht aus <" + fnam
					+ "> extrahiert werden");

		// überprüfe ob die postid im wertebereich der Pageseite ist
		maxpost = seitenzaehler * 10;
		minpost = (seitenzaehler * 10) - 9;
		if ((aktpostid <= maxpost) && (aktpostid >= minpost))
		{
			// Der Wertebereich stimmt
			// Bsp:
			// Pageseite = 6685
			// Posting von 66841..... 66850
			return true;
		} else
		{
			// Wertebereich der Postid fehlerhaft, lösche die htmlseite
			Tracer.WriteTrace(20, "Error:fnam <" + fnam
					+ ">Wertebereich der postid<" + aktpostid
					+ "> muss zwischen <" + minpost + "> <" + maxpost
					+ "> liegen");
			html.CloseHtmlPage();

			FileAccess.FileMoveBrokenPages(fnam);
			return false;
		}

	}

	public HashSet<Integer> getPostidMenge( String usernam)
	{
		// ermittelt die Menge aller postid´s
		HashSet<Integer> postmenge = new HashSet<Integer>();

		// suche den index des users
	

		int userpos = this.getUserpos(usernam);
		if (userpos < 0)
			return null;

		if (userid_aktthreadpos[userpos] != null)
			if (userid_aktthreadpos[userpos].size() > 0)
			{
				int anz = userid_aktthreadpos[userpos].size();

				for (int i = 0; i < anz; i++)
				{
					// holt sich die postnummer
					int postnum = userid_aktthreadpos[userpos].get(i);
					postmenge.add(postnum);
				}
			}

		return postmenge;
	}

	public Boolean BuildPostinglistEinThread(int startpage, int endpage,
			String breakdate_str, int find_breakdate_flag, UserDB udb,
			ThreadDbObj tdbo, int updateflag)
	{
		// startpage= ab hier wird mit der Analyse begonnen (update)
		// endpage=bis zu dieser Page muss analysiert werden
		// falls find_breakdate_flag==1 dann wird breakdate_str ausgewertet
		//
		// baut für einen Thread die postings auf
		// voraussetzung: tdb muss auf den richtigen Thread gesetzt werden
		// Anschlissend kann mit get_nextPosting alles ausgelesen werden
		// falls find_breakdate_flag==1 dann wird nur die postid gesucht und
		// gespeichert und danach sofort beendet.

		// Wird auch beim update aufgerufen

		ThreadsPostElem postobj = new ThreadsPostElem();
		DownloadManager dm = new DownloadManager(GC.MAXLOW);

		int i = 0, breakfoundflag = 0, fehlladezaehler = 0;
		String fname = null, fnamz = null, lastfnamz = null;
		String aktpostdatumzeit = null;
		long breakdate_long = 0, postdate_long = 0;
		int seitenpostzaehler = 0;

		if (find_breakdate_flag == 1)
			breakdate_long = Tools.get_date_milisec_lo(breakdate_str);

		// mache postdb reset damit lastposting auf null
		this.Reset();

		Tracer.WriteTrace(40, "I:Build Posting Liste ein Thread startpage<"
				+ startpage + "> endpage<" + endpage + "> ");
		for (i = startpage; i <= endpage; i++)
		// Webseiten
		{
			fname = tdbo.GetSavePageName_genDir(i);
			fnamz = fname + ".gzip";

			Tracer.WriteTrace(40, "I:Untersuche page<" + i + "> name<" + fnamz
					+ "> ");
			int aktpostid = 0;
			// Holt sich das nächste Posting also username id etc...

			if (FileAccess.FileAvailable(fnamz) == false)
			{

				reloadWebpage(dm, tdbo.GetUrlName(i), fnamz, "w:pagename <"
						+ fnamz + "> missing I load it");
			}
			DB<ThreadsPostElem> html = new DB<ThreadsPostElem>(fnamz);

			seitenpostzaehler = 0;
			while ((postobj = html.GetNextPosting()) != null)
			{
				seitenpostzaehler++;
				aktpostid = postobj.get_postid();

				// prüft ob die aktpostid zur Seitenzahl passt
				if (PostNummernCheck(aktpostid, fname, html, tdbo) == false)
				{
					// falls die seite fehlerhaft ist dann lade neu
					reloadWebpage(dm, tdbo.GetUrlName(i), fnamz,
							"W:Postnummerncheck");
					html = new DB<ThreadsPostElem>(fname);
					html.HtmlReadPage(fnamz);
					continue; // gehe weiter
				}

				postobj.set_threadid(tdbo.getThreadid());
				addPosting(postobj, tdbo, udb, updateflag);

				// suche das breakdate und die Id
				aktpostdatumzeit = postobj.get_datetime();
				if (aktpostdatumzeit.length() < 17)
					reloadWebpage(dm, tdbo.GetUrlName(i), fnamz,
							"W:incomplete htmlpage (date/time-error) <" + fnamz
									+ ">");

				// System.out.println("postdatum=" + aktpostdatum);
				postdate_long = Tools.get_date_milisec_lo(aktpostdatumzeit);

				// vergleiche aktpostdatum mit dem Breakdate
				if (postdate_long < 0)
					Tracer.WriteTrace(20, "W:Fehler im Breakdate !!!");

				if (breakfoundflag == 0)// noch kein Breakdate gefunden
					if (postdate_long > breakdate_long)
					{

						// Breakdate gefunden
						Tracer.WriteTrace(30, "I:<" + tdbo.getThreadname()
								+ "><" + tdbo.getSymbol()
								+ ">Found Breakdate ->Setze Breakindex <"
								+ postobj.get_postid() + ">!!!");

						tdbo.setBreakid(postobj.get_postid());
						breakfoundflag = 1;
					}
			}
			// überprüfe ob die Seite 10 Postings beinhaltet
			if ((seitenpostzaehler < 10) && (i < endpage))
			{
				// die seite hat zu wenig postings
				// lösche die Seite und lade neu
				if (fnamz.equals(lastfnamz) == true)
					fehlladezaehler++;
				else
					fehlladezaehler = 0;

				// hier ist ein massives problem da eine webseite nicht geladen
				// werden konnte
				if (fehlladezaehler >= 2)
				{
					Tracer
							.WriteTrace(
									20,
									"Error: Webpage<"
											+ fnamz
											+ "> konnte trotz 3 Versuche nicht geladen werden !!");
					return false;
				}
				reloadWebpage(dm, tdbo.GetUrlName(i), fnamz,
						"W:Seite hat weniger als 10 Postings-> lade neu");
				lastfnamz = fnamz;
				i--;
				continue;
			}

			if (i % 100 == 0)
				System.out.println("Analysiere <" + fnamz + ">");
		}
		if (breakfoundflag == 0)
			tdbo.setBreakid(0);

		return true; // alles ok
	}

	private void reloadWebpage(DownloadManager dm, String url, String fnamez,
			String fehlermeldung)
	{
		// läd eine webpage neu, sichert aber das alte im müllcontainer
		if (fehlermeldung != null)
			Tracer.WriteTrace(30, fehlermeldung);
		FileAccess.FileMoveBrokenPages(fnamez);
		dm.DownloadHtmlPage(url, fnamez, 0, 50000, 1, 1, 0);

	}

	public void BuildPostDBalleThreads(ThreadsDB tdb, UserDB udb,
			String breakdate_str)
	{
		// Hier wird die Postdb aktuallisiert, es werden die Postinformationen
		// für alle Threads aktualisiert
		// Ausserdem wird geschaut welche User gepostet haben, falls neue
		// User gefunden wurden, wird diese info in userdb aufgenommen.
		// Die Postinformation wird für den jeweiligen Thread in db/threaddata
		// abgespeichert.
		// Neue User werden in der userstore.db abgespeichert
		// a) existiert ein threads_<threadname>.db -Auswertung bereits wird
		// fortgefahren
		// Weiterhin wird die Postnummer für das breakdate ermittelt

		String filedbnam = null, fnam = null, aktpostdatum = null;
		int i = 0;
		Boolean postflag = false;
		tdb.ResetDB();
		int tdblimit = tdb.GetanzObj();
		breakdate = new String(breakdate_str);

		// Analysiert alle Threads
		for (int j = 0; j < tdblimit; j++) // maximal 1xxx threads
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetAktObject();
			filedbnam = GC.rootpath + "\\db\\threaddata\\"
					+ tdb.getAktThreadName() + "_" + tdb.GetThreadid() + ".db";
			if (FileAccess.FileAvailable(filedbnam) == true)
			{
				System.out.println("<" + j + "|" + tdblimit + ">Datenbasis <"
						+ filedbnam + "> schon vorhanden => gehe weiter");
				if (tdb.SetNextObject() == false)
					break;
				else
					continue;
			}

			postflag = BuildPostinglistEinThread(1, tdbo.getPageanz(),
					breakdate_str, 0, udb, tdbo, 0);

			if (postflag == true)
			{
				WritePostDB(GC.rootpath + "\\db\\threaddata\\"
						+ tdbo.getThreadname() + "_" + tdbo.getThreadid()
						+ ".db", udb);

				udb.WriteDB();
				CleanPostDB(); // lösche die Postliste
			}
			if (tdb.SetNextObject() == false)
				break;
		}
		tdb.WriteDB();
	}

	public void UpdatePostDB(int startpos, int endpos, UserDB udb,
			ThreadDbObj tdbo)
	// die Postdb für ein Thread wird geupdatet
	// threadname: name der db
	// startpos: ab dieser htmlseite wird neu hinzugefügt
	// endpos: bis zu dieser htmlseite wird hinzugefügt
	{
		String filename = null;
		int updateflag = 0;
		boolean postdbavailable = false;
		// baue den filenamen
		String filedbnam = GC.rootpath + "\\db\\threaddata\\"
				+ tdbo.getThreadname() + "_" + tdbo.getThreadid() + ".db";
		// zur sicherheit
		if (startpos < 0)
			startpos = 0;

		// Die Poststruktur löschen
		CleanPostDB();
		// die aktuelle postdb lesen
		postdbavailable = ReadPostDB(filedbnam);

		if ((postdbavailable == false) && (startpos > 0))
		{
			// falls die postdb nicht vorhanden aber postings da sind, dann
			// erstelle sie bei position 1
			System.out.println("Info: postdb-nicht verfügbar, startpos=<"
					+ startpos + ">");
			System.out.println("Erstelle neue postdb mit startpos=1");
			startpos = 1;
		}
		// falls die postdb verfügbar ist, dann ist dies nur ein update
		if (postdbavailable == true)
			updateflag = 1;

		// übergib ein unmögliches Breakdate damit alles gespeichert wird
		// Das Breakdate wir ja hier überhaupt nicht gesucht
		BuildPostinglistEinThread(startpos, endpos, "2040-08-01", 0, udb, tdbo,
				updateflag);
		WritePostDB(filedbnam, udb);
		CleanPostDB();
	}

	private int getUserpos(String unam)
	{
		int userp = 0;

		if (userpos == null)
			return -99;
		if (userpos.containsKey(unam) == true)
		{
			userp = userpos.get(unam);
			return userp;
		} else
			return -99;
	}

	public void CleanPostDB()
	{
		// alles wird gelöscht !
		int i, j;

		userpos.clear();
		userpos_reverse.clear();
		nextfreeuserid = 0;

		// maximal 7000 user
		for (i = 0; i < 7000; i++)
			if (userid_aktthreadpos[i] != null)
				userid_aktthreadpos[i].clear();
	}

	public Boolean CheckSequence()
	{
		// Es wird für die Postdbx ein sequenzencheck durchgeführt
		// D.h. es wird überprüft ob alle postnummern in der postdbx von 1 bis
		// maxpostnr aufgenommen wurden
		// Wenn dieser Test fehlschlägt ist die postdbx für den Aktienwert
		// fehlerhaft
		if (checksequence == null)
			System.out.println("da ist nix");

		for (int i = 1; i <= maxsequence; i++)
		{
			// System.out.println("überprüfe seq="+i);
			if ((checksequence[i] == null) || (checksequence[i] == false))
			{
				Tracer.WriteTrace(20, "Error: Sequence check error postid<" + i
						+ "> is missing, maxsequence=" + maxsequence
						+ "filename <" + fname + "> move bad file");

				FileAccess.FileMoveBadPages(fname);

				return false;
			}

		}
		if (maxsequence == 0)
			return false;
		else
			return true;
	}

	public void AddSequence(int postid)
	{
		if (checksequence == null)
		{
			System.out.println("leer");
			// max 500000 postings
			checksequence = new Boolean[500000];
		}

		checksequence[postid] = true;
		if (postid >= maxsequence)
			maxsequence = postid;
	}

	public void addPosting(ThreadsPostElem posting, ThreadDbObj tdbo,
			UserDB udb, int updateflag)
	{
		// das posting wird in der sammelliste aufgenommen
		// (betrifft nur einen Thread)
		// Es wird aber überprüft ob das Posting schon drin ist, wenn ja
		// wird eine Warnung ausgegeben

		int userp = 0;
		int threadp = 0;
		int firstflag = 1; // liste schon vorhanden
		String username = null;
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\NeueUserInThreads.txt");
		int aktthreadid = posting.get_threadid();

		// user ermitteln
		username = posting.get_username();
		int postid = posting.get_postid();
		AddSequence(postid);

		// prüft nach ob der user ganz neu ist
		if (userpos.containsKey(username) == false)
		{
			// user ganz neu in diesem thread
			UserDbObj udbo = udb.getUserobj(username);
			if (updateflag == 1)
			{// im updatefall wird gelogged
				if (udbo != null)
					inf.writezeile(Tools.get_aktdatetime_str() + ":<"
							+ tdbo.getThreadname() + ">:user <" + username
							+ "> seit<" + udbo.getRegistriert() + "> postings<"
							+ udbo.getAntworten() + "|" + udbo.getThemen()
							+ ">neu in thread<" + posting.get_threadid()
							+ "> <" + tdbo.getUrlaktname() + "> <br>");
				else
					inf.writezeile(Tools.get_aktdatetime_str() + ":<"
							+ tdbo.getThreadname() + ">:user <" + username
							+ "> keine info !!");
			}
			userpos.put(username, nextfreeuserid); // hashtable
			userpos_reverse.put(nextfreeuserid, username); // rückverknüpf
			userp = nextfreeuserid;
			nextfreeuserid++;

			firstflag = 0;// neue liste
			// System.out.println("add new user <"+nextfreeuserid+"|" + username
			// + ">");
		} else
		{// user schon da hole position
			userp = userpos.get(username);
		}

		if (firstflag == 0)
		{
			// der user ist ganz neu
			userid_aktthreadpos[userp] = new ArrayList<Integer>();
		}

		// Das neue Posting kommt zu der userpostingliste hinzu wenn es noch
		// nicht drin ist

		if (userid_aktthreadpos[userp].contains(postid) == true)
			Tracer.WriteTrace(40, "W:updatePostDB username=<" + username
					+ "> postid<" + postid + "> schon vorhanden");
		else
		{
			Tracer.WriteTrace(40, "I:updatePostDB username=<" + username
					+ "> postid<" + postid + "> neu aufgenommen");
			userid_aktthreadpos[userp].add(postid);
		}
	}

	public int getUserPostinganz(String username_str)
	// zaehlt wie oft ein bestimmter User in einem thread gepostet hat
	{
		int postanz = 0, userp = 0;
		if (userpos.containsKey(username_str) == true)
		{
			userp = userpos.get(username_str);
			postanz = userid_aktthreadpos[userp].size();
		}
		return (postanz);
	}

	
	public int getUserPostnummerIDX(String username_str, int index)
	// hole für einen Usernamen für den index=i die postnummer
	// wieviele postings ein user abgesetzt hat das kann man mit
	// getUserPostinganz ermitteln
	// man möchte mit dieser Funktion die exaxten postings für einen
	// user holen, dies ist ja in der db gespeichert
	{
		int postnum = 0, userp = 0;
		if (userpos.containsKey(username_str) == true)
		{
			userp = userpos.get(username_str);
			postnum = userid_aktthreadpos[userp].get(index);
			return postnum;
		}
		return -1; // user nicht gefunden
	}

	public void WritePostDB(String filename, UserDB udb)
	{
		// die postdb für einen thread wird geschrieben
		// vor dem Schreiben wird aber noch überprüft ob hierdrin neue usernamen
		// sind, ist dies der fall werden neue Usernamen in userstore.db
		// aufgenommen

		String zeile = null;
		int i = 0, j = 0;
		int threadid = 0;
		String usernam = null;
		int adduserflag = 0;

		if (dbopenflag != 0)
			return;

		try
		{
			Tracer.WriteTrace(30, PostDBx.class.getName() + "Untersuche<"
					+ filename + ">");
			BufferedWriter ouf = FileAccess.WriteFileOpen(filename, "UTF-8");

			ouf.write("**********");
			ouf.newLine();

			for (i = 0; i < 7000; i++)
			{ // maximal 7000 user
				if (userpos_reverse.containsKey(i) == false)
					break;

				usernam = userpos_reverse.get(i);
				zeile = "#" + "\"" + usernam + "\" "
						+ userid_aktthreadpos[i].size() + " "
						+ userid_aktthreadpos[i];
				ouf.write(zeile);
				ouf.newLine();

				if (usernam.length() == 0)
				{
					Tracer.WriteTrace(20, PostDBx.class.getName()
							+ "Warning:drop usernam <" + usernam + ">");
					continue;
				}
				if (udb.checkUsername(usernam) < 0)
				{
					Tracer
							.WriteTrace(30, PostDBx.class.getName()
									+ "Nimm neuen user <" + usernam
									+ "> in userdb auf");
					adduserflag = 1;
					UserDbObj udbo = new UserDbObj();
					udbo.setUsername(usernam);
					udb.AddObject(udbo);

					int anz = udb.GetanzObj();
					System.out.println("Info:neuen user<" + anz + "|" + usernam
							+ "> in userstore.db aufgenommen");
				}
			}
			if (adduserflag == 1)
				udb.WriteDB();
			ouf.close();

			if (CheckSequence() == false)
			{
				Tracer.WriteTrace(20, "W:Sequencecheck für <" + filename
						+ "> nicht bestanden maxpost<" + maxsequence + ">");
				return;
			}

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean ReadPostDB(String filename)
	{
		// liest die postdb unter threaddata\thread_x.db wieder ein
		int i, j, anz, min = 0, max = 0, links = 0, rechts = 0, aktzahlpointer = 0;
		String username = null, cut = null;
		BufferedReader inf = null;
		CleanPostDB();
		// max 200000 postings
		checksequence = new Boolean[500000];
		maxsequence = 0;
		fname = filename;
		if (FileAccess.FileAvailable(filename) == false)
			return false;

		try
		{
			inf = FileAccess.ReadFileOpen(filename);
			i = 0;

			String zeile = "";
			int anfuehrzeich_pos = 0;

			while ((zeile = inf.readLine()) != null)
			{
				// System.out.println(zeile);
				if (zeile.contains("*****") == true)
					continue;

				username = new String(zeile.substring(2, zeile
						.lastIndexOf("\"")));
				if (userpos.containsKey(username) == false)
				{// user neu
					userpos.put(username, i); // hashtable
					userpos_reverse.put(i, username); // rückverknüpf
				}

				anz = Integer.parseInt(zeile.substring(username.length() + 4,
						zeile.indexOf(" [")));
				userid_aktthreadpos[i] = new ArrayList<Integer>();

				// sucht nach Anführungszeichen leerpos
				zeile = zeile
						.substring(anfuehrzeich_pos = zeile.indexOf("\" "));
				zeile = zeile.substring(zeile.indexOf("["));
				zeile = zeile.substring(1, zeile.lastIndexOf("]"));

				for (j = 0; j < anz; j++)
				{
					links = 0;
					if (j < anz - 1)
						rechts = zeile.indexOf(",");
					else
						rechts = zeile.length();

					cut = zeile.substring(links, rechts);
					// System.out.println("j=" + j + " cut=" + cut);
					int postid = Integer.parseInt(cut);
					userid_aktthreadpos[i].add(postid);
					if (j < anz - 1)
						zeile = zeile.substring(rechts + 2);

					// den sequencecheck aktualisieren
					AddSequence(postid);
				}
				// System.out.println("hab alles zeile=" +
				// userid_aktthreadpos[i]);
				i++;
			}
			nextfreeuserid = i;
			inf.close();
			return true;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, PostDBx.class.getName()
					+ ":ERROR: file not found <" + filename + ">");
			return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, PostDBx.class.getName()
					+ ":ERROR: read error <" + filename + ">");
			return false;
		} catch (StringIndexOutOfBoundsException e)
		{
			Tracer.WriteTrace(20, PostDBx.class.getName()
					+ ":ERROR: datensatz im file index out of bound<"
					+ filename + "> fehlerhaft");
			Tracer.WriteTrace(20, "E: lösche defekten datensatz <" + filename
					+ ">");
			if (inf != null)
				try
				{
					inf.close();

				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			FileAccess.FileDelete(filename,0);
			return false;
		}

	}
	public boolean checkOfflineUsername(String uname,String fname)
	{
		
		boolean ret=false;
		//prüft ob der Username im Datensatz ist
		ret=FileAccess.CheckFileKeyword(fname, uname,0);
		return ret;
	}
	
	public String getUnam(int index)
	{
		//int pos=userpos.get(index);
		String unam=userpos_reverse.get(index);
		return unam;
	}
	public int getPostanz(String unam)
	{
		int pos =userpos.get(unam);
		int postanz=userid_aktthreadpos[pos].size();
		return postanz;
	}
}
