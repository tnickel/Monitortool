package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import mainPackage.GC;
import objects.Obj;
import objects.UserDbObj;
import objects.UserPostingverhaltenDbObj;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import ranking.Rangparameter;
import wartung.CompressUserhtmlpages;

import comperatoren.UserComperatorRang;
import comperatoren.UserComperatorRangpoints;

import db.DB;

public class UserDB extends DB
{
	// dient zum schnellen Auffinden der userposition
	// name->index
	private HashMap<String, Integer> hmap = new HashMap<String, Integer>();

	int durchlaufmode = 0;
	int[] hitpoints = new int[GC.USERLIMIT];
	// In dieser liste wird verwaltet wofür die user die hitpoints bekommen
	// haben
	List<Integer>[] hitthreads = new List[GC.USERLIMIT];
	String hitpointinfofile = GC.rootpath + "\\db\\hitpointinfo.txt";
	ObserveDBx obsdb = new ObserveDBx();
	BoostraengeDBx brdb = new BoostraengeDBx();

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public UserDB(String observefile, String boostfile)
	{
		Tracer.WriteTrace(20, "Info:Kontstruktor Userstore");
		if ((this.GetanzObj()) == 0)
			LoadDB("userstore", null, 0);

		InitHashmapIndex();

		obsdb.GetObservefile(GC.rootpath + "\\db\\" + observefile);
		int anz = GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			String unam=udbo.get_username();
			if (obsdb.CheckObserve(unam) == true)
			{
				udbo.setMode(8000);
				udbo.setPrio(obsdb.getPrio(unam));
			} else
			{
				udbo.setMode(0);
				udbo.setPrio(0);
			}
		}
		brdb.getBoosterfile(GC.rootpath + "\\db\\" + boostfile);
		int br = 0;

		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			br = brdb.getBoosterVal(udbo.get_username());
			if (br > 0)
			{
				udbo.setBoostrang(br);
			}
		}
		System.out.println("ende konsruktor userdb");
	}

	public void setState(int state)
	{
		int anz = GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			udbo.setState(state);
		}
	}

	public int checkUsername(String uname)
	{
		// prüft ob username schon in db, und gibt ggf die pos zurück
		int anz = GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			if (udbo.get_username().equalsIgnoreCase(uname) == true)
			{
				return i;
			}
		}
		return -99;
	}

	public UserDbObj getUserobj(String uname)
	{
		// prüft ob username schon in db, und gibt ggf die pos zurück
		// die hmap dient zum schnellen auffinden der user

		if (hmap.containsKey(uname) == true)
		{
			int pos = hmap.get(uname);
			UserDbObj udbo = (UserDbObj) GetObjectIDX(pos);

			if (udbo.get_username().equalsIgnoreCase(uname) == true)
				return udbo;
			else
				Tracer.WriteTrace(10,
						"Error: internal hmapping inkonsistent pos<" + pos
								+ "> gesucht-uname<" + uname + "> gef. uname<"
								+ udbo.get_username() + ">");
			return null;
		} else
			return null;

		/*
		 * int anz = GetanzObj(); for (int i = 0; i < anz; i++) { UserDbObj udbo
		 * = (UserDbObj) GetObjectIDX(i); if
		 * (udbo.get_username().equalsIgnoreCase(uname) == true) { return udbo;
		 * } } return null;
		 */
	}

	private void updateHmap(String username)
	{
		// der username 'username' wird in der hmap gespeichert

		int anz = GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			if (udbo.get_username().equalsIgnoreCase(username) == true)
			{
				if (hmap.containsKey(username) == false)
				{
					hmap.put(username, i);
					return;
				}
			}
		}
		Tracer.WriteTrace(20, "Error: internal username<" + username
				+ "> konnte nicht userstore gefunden werden");
	}

	private void InitHashmapIndex()
	// hier wird die ganze Hashmap aufgebaut, dies passiert nach dem ersten
	// einlesen der userdaten
	{

		int anz = GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			String username = udbo.get_username();

			if (hmap.containsKey(username) == false)
			{
				hmap.put(username, i);
			} else
			{
				Tracer.WriteTrace(10,
						"Error: datenfileerror userstore.db username <"
								+ username + "> ist doppelt");
			}
		}
	}

	public void AddNewUser(String username)
	{
		UserDbObj udbo = new UserDbObj();
		udbo.setUsername(username);
		this.AddObject(udbo);
		this.AktuallisiereEinenUserSchnell(username);
		this.WriteDB();
		updateHmap(username);

	}

	public void AktuallisiereEinenUserSchnell(String username)
	{
		// für diesen neuen user werden die Grunddaten ermittelt
		DownloadManager dm = new DownloadManager(5);
		this.ResetDB();

		UserDbObj udbo = this.getUserobj(username);
		if (udbo == null)
			return;

		String regdate = udbo.getRegistriert();
		if (Tools.isDate(regdate) == true)
			return;
		udbo.WebUpdateStep1(dm, 48, 0);
		dm.waitEnd();
		udbo.WebUpdateStep2();
		this.WriteDB();
		dm.waitEnd();

	}

	public void AktualliereAlleUserSchnell(int forceflag)
	{
		// Hier werden nur die User Grunddaten schnell aktuallisiert
		// d.h. Wenn regdatum ==null dann wird z.B. nur das Regdatum gesetzt
		// sonst nix.
		// falls forceflag==1 dann aktuallisierte auf jeden fall ohne
		// betrachtung des states
		// User die in die observerliste sind werden auf jeden Fall
		// aktuallisiert
		DownloadManager dm = new DownloadManager(50);
		int useranz = GetanzObj();
		this.ResetDB();
		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);

			// falls user Mode==8000 => user observable => auf jeden Fall update
			if (forceflag == 0)
				if ((udbo.getState() != 0) && (udbo.getMode() == 0))
					continue;
			String regdate = udbo.getRegistriert();

			// Falls das Datum schon gesetzt ist mache weiter
			if ((Tools.isDate(regdate) == true) && (udbo.getMode() == 0))
				continue;

			System.out.println("Step1<" + i + "|" + useranz
					+ "> Download 'user-info.html?search_nick='<"
					+ udbo.get_username() + ">");
			udbo.WebUpdateStep1(dm, 10, 1);
			udbo = null;
		}
		dm.waitEnd();
		this.ResetDB();

		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);
			// falls der user nicht valid ist gehe weiter

			if (forceflag == 0)
				if ((udbo.getState() != 0) && (udbo.getMode() == 0))
					continue;
			String regdate = udbo.getRegistriert();

			// Falls das Datum schon gesetzt ist mache weiter
			if ((Tools.isDate(regdate) == true) && (udbo.getMode() == 0))
				continue;
			System.out
					.println("Step2: <"
							+ i
							+ "|"
							+ useranz
							+ "> Webseite user -> userstore.db, lade username_beitraege... <"
							+ udbo.get_username() + ">");

			// falls user fehlerhaft oder nicht mehr vorhanden dann setze
			// userstate==99

			// werte die Userinfo aus und aktualisiere userstore.db
			udbo.WebUpdateStep2();
			regdate = udbo.getRegistriert();
			if (Tools.isDate(regdate) == true)
				udbo.setState(0);

			if (i % 100 == 0)
				this.WriteDB();
		}
		this.WriteDB();
		dm.waitEnd();
		this.ResetDB();

	}

	public void AktuallisiereAlleUserCalRang(AktDB aktdb,
			int usergewinnrangflag, int nurobserveuser_flag, int threshold,
			ProgressBar pb, Display display, Text aktion, String pref)
	{
		UserGewStrategieDB2 ugewinnedb = new UserGewStrategieDB2(
				"UserThreadVirtualKonto\\Summengewinnedb");
		DownloadManager dm = new DownloadManager(10);
		// hier wird erst alles runtergeladen
		// bei forceflag ==1 wird auf jeden fall runtergeladen
		// usergewinnrangflag: falls 1, dann fliessen in die userrangberechnung
		// die gewinnränge
		// observeuserflag: falls 1 werden nur die observeuser aktualisiert
		// Aktualisiert wird das folgende: a)-> c)
		// a)userinfo, b)userbeiträge, c)ReadUserInfoListeCalRank
		int useranz = GetanzObj();
		this.ResetDB();
		String rangfile = "rangfile";

		// roadhouse1 (gesperrt), setze bei gespeerten usern den threadstatus
		// auf 99
		if (aktion != null)
		{
			aktion.setText("Step1/3: Lade userinfo");
			pb.setMinimum(0);
			pb.setMaximum(useranz);
		}

		if (GC.ONLINEMODE == 1)
			for (int i = 0; i < useranz; i++)
			{
				if (pb != null)
					pb.setSelection(i);
				Swttool.wupdate(display);
				UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);
				// falls der user nicht valid ist gehe weiter
				if (udbo.getState() != 0)
					continue;
				// falls kein observeuser, dann weiter
				if ((udbo.getMode() == 0) && (nurobserveuser_flag == 1))
					continue;

				System.out.println("Step1<" + i + "|" + useranz
						+ "> Download 'user-info.html?search_nick='<"
						+ udbo.get_username() + ">");
				udbo.WebUpdateStep1(dm, threshold, 0);
				
				udbo = null;
				//Bei Testmode machen nur einen
				if(GC.TESTMODE==1)
					break;
				
				
			}

		dm.waitEnd();
		this.ResetDB();

		if (aktion != null)
			aktion.setText("Step2/3: Lade Userbeiträge");

		if (GC.ONLINEMODE == 1)
			for (int i = 0; i < useranz; i++)
			{
				if (pb != null)
					pb.setSelection(i);
				Swttool.wupdate(display);
				UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);
				// falls der user nicht valid ist gehe weiter
				if (udbo.getState() != 0)
					continue;
				if ((udbo.getMode() == 0) && (nurobserveuser_flag == 1))
					continue;
				System.out
						.println("Step2/3: <"
								+ i
								+ "|"
								+ useranz
								+ "> Webseite user -> userstore.db, lade username_beitraege... <"
								+ udbo.get_username() + ">");

				// falls user fehlerhaft oder nicht mehr vorhanden dann setze
				// userstate==99

				// werte die Userinfo aus und aktualisiere userstore.db
				udbo.WebUpdateStep2();
				// Lade noch 2 userwebseiten
				udbo.WebUpdateStep3(dm, threshold);

				if (i % 100 == 0)
					this.WriteDB();
				
				//Bei Testmode machen nur einen
				if(GC.TESTMODE==1)
					break;
			}
		this.WriteDB();
		dm.waitEnd();
		this.ResetDB();

		int gesanz_neuer_userthreads = 0;
		int anznthreads = 0, gesnthreads = 0;
		if (aktion != null)
			aktion.setText("Step3/3: Werte Userinfo aus");
		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);
			if (pb != null)
				pb.setSelection(i);
			Swttool.wupdate(display);
			// falls der user nicht valid ist gehe weiter
			if (udbo.getState() != 0)
				continue;
			if ((udbo.getMode() == 0) && (nurobserveuser_flag == 1))
				continue;
			// werte die beiden User-Webseiten aus, aktualisiere die
			// db/userinfoliste
			System.out.println("Step4a: unam <" + udbo.get_username() + ">");

			
			anznthreads = udbo.WebUpdateStep4(ugewinnedb, threshold, aktdb,
					usergewinnrangflag, pref, rangfile);
			gesnthreads = gesnthreads + anznthreads;

			gesanz_neuer_userthreads = gesanz_neuer_userthreads + anznthreads;
			System.out.println("Step4b: <" + i + "|" + useranz
					+ "> neueUthreads<" + anznthreads + "> gesNeueUthreads<"
					+ gesnthreads + ">   Webseiten -> username.db <"
					+ udbo.get_username() + ">");

			//Bei Testmode machen nur einen
			if(GC.TESTMODE==1)
				break;
		}
		Tracer.WriteTrace(20,
				"I:###Gesanz der neuen threads bei allen usern =<"
						+ gesanz_neuer_userthreads + ">");

		dm.waitEnd();

	}

	public void ImportNewThreadsFromUsers(int usergewinnrangflag,
			int observeuser_flag, ProgressBar pb, String pref,AktDB aktdb,ThreadsDB tdb,KeyDB keydb)
	{
		// läd alle Threads und schaut nach ob einige User neue Threads
		// aufgespürt
		// haben
		// observeuser_flag: falls dies gesetzt ist werden nur die user
		// betrachet die
		// das observeflag haben
		UserGewStrategieDB2 ugewinnedb = new UserGewStrategieDB2(
				"UserThreadVirtualKonto\\Summengewinnedb");
		// alle userdaten wurden runtergeladen und user.db aktualisiert
		String rangfile = "rangfile";

		
		DownloadManager dm = new DownloadManager(50);
		int useranz = GetanzObj();
		dm.waitEnd();
		this.ResetDB();
		
		
		MidDB middb= new MidDB(tdb);
		int anznthreads = 0;
		pb.setMaximum(useranz);

		for (int i = 0; i < useranz; i++)
		{
			pb.setSelection(i);
			UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);

			// falls der user nicht valid ist gehe weiter
			if (udbo.getState() != 0)
				continue;
			if ((observeuser_flag == 1) && (udbo.getMode() == 0))
				continue;

			System.out.println("Step5: <" + i + "|" + useranz
					+ "> Werte Postingliste aus (->aktdb.db ->threads.db) <"
					+ udbo.get_username() + ">");
			if (udbo.WorkUserPostingListe(middb,dm, ugewinnedb, aktdb, tdb, this, 5,
					GC.AktienThread, usergewinnrangflag, pref, null, rangfile) == true)
				anznthreads++;
			udbo.DeleteUserPostingListeMem();

			if (i % 100 == 0)
			{
				tdb.WriteDB();
				aktdb.WriteDB();
			}
		}
		tdb.WriteDB();
		aktdb.WriteDB();
		Tracer.WriteTrace(20,
				"I:###Gesanz aller neuen Threads (aktthreads, sofathreads) in aktdb.db =<"
						+ anznthreads + ">");

		CompressUserhtmlpages ch = new CompressUserhtmlpages();
		ch.startCompressAll(middb,tdb);
		System.out.println("fertig mit schauen nach neuen Threads");
	}

	private void Skaliere100000(UserDB udb,String fnam)
	{
		//Hier wird der Rang vergeben und protokolliert
		
		Inf inf = new Inf();
		String inffile = fnam;
		if (FileAccess.FileAvailable(inffile))
			FileAccess.FileDelete(inffile, 0);

		// sortiert und vergibt den Rang
		int anz = this.GetanzObj();
		float rankfaktor = (100000 / anz);
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo=(UserDbObj)this.GetObjectIDX(i);
			
			// setzt den Rank;
			int rangmod = (int) (i * rankfaktor);
			udbo.setRang(rangmod);
			
			inf.appendzeile(inffile, "<" + i + ">:unam<"
					+ udbo.get_username() + "> Rang<"+(udbo).getRang()+"> points<"
					+ udbo.getRangpoints() + "> Inf<"
					+ udbo.getRankinginfostring() + ">", true);
		}
		inf.close();
	}

	private void verschmelzeRangeUserpostverh(UserDB udb,
		
			UserPostingverhaltenDB userpostverhdb)
	{
		//hier wird der usercharakter mit in die Rangberechnung der
		//UserDB mit einbezogen
		//Es wird also aus 2 einzelnen Rangberechnungen eine neue Rangberechnung
		//gemacht
		//RangUserinfo: Rang aus udbo (Rang aus Wo-Parametern gebildet)
		//RangCharakter:Rang aus userpostverhalten (Hier wird das Postverhalten beurteilt: reaktiv, proaktiv, informativ etc.
		
		int rp2 = 0;
		String ristr=null;
		Inf inf= new Inf();
		
		String fnam=GC.rootpath+"\\db\\stores\\ranking\\RangberechnungInfo.txt";
		inf.setFilename(fnam);
		
		if(FileAccess.FileAvailable0(fnam))
			FileAccess.FileDelete(fnam, 0);
		
		// hier werden die Rangbewertung der Usercharakterbewertung mit
		// berücksichtigt
		int anz = udb.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj)udb.GetObjectIDX(i);
			String unam = udbo.get_username();
			UserPostingverhaltenDbObj upostverhobj = (UserPostingverhaltenDbObj) userpostverhdb
					.sucheUser(unam);

			int rp1 = udbo.getRang();
			if (upostverhobj == null)
			{
				Tracer
						.WriteTrace(
								20,
								"Info:user<"
										+ unam
										+ "> kein Postingverh. bekannt setze rp<"+rp1+60000+">=RangUser<"+rp1+">+60000");
				rp2 = 60000;
				ristr="kein postverh";
			} else
			{
				rp2 = upostverhobj.getRang();
				ristr=upostverhobj.getRanginfostring();
			}
			

			udbo.setRang(rp1 + rp2);
			
			String ri = udbo.getRankinginfostring2()
					+ ristr;
			udbo.setRankinginfostring2(ri);
			
			inf.writezeile("User<"+udbo.get_username()+"> RangUserinfo<"+rp1+"> RangCharakter<"+rp2+"> => GesRang<"+udbo.getRang()+">");
		}
		this.WriteDB();
		inf.close();
	}

	public void BaueRankingListe(int usergewinnflag,
			int fileausgabeflag, Rangparameter rp, AktDB aktdb,
			Boolean useUsercharakterflag, UserPostingverhaltenDB userpostverhdb)
	{
		// Einige Erklärungen
		// usergewinnflag=1, dann werden auch die einzelnen usergewinne mit in
		// das Ranking gezogen
		// a) Gewinnrang, ist der Rank (Skaliert auf 100000) des users der bei
		// Betrachtung
		// des naiven Handels erzielt werden konnte
		// b) Gewinnrangpunkte, diese Gewinnrangpunkte wurden durch naives
		// Handeln (10 Euro 10 Tage bei jedem
		// posting, ermittelt wurden (sind in diesem Fall der Gewinn)
		// c) Rang, dies ist der Gesammtrank des users (wie gut er ist)
		// d) Rangpunkte, die Rankpunkte werden ermittelt und anschliessend wird
		// hieraus ein Rang ermittelt.
		// Rankpunkte = diverse Punkte + Gewinnrankgpunkte

		// erst alle userdaten aktuallisieren

		// AktuallisiereAlleUser(aktdb);
		UserGewStrategieDB2 ugewinnedb = new UserGewStrategieDB2(
				"UserThreadVirtualKonto\\Summengewinnedb");

		// jetzt wird ein ranking gemacht
		// Das Ranking wird auf 100000 Skaliert. 1=bester, 100000=schlechtester
		int useranz = GetanzObj();
		this.ResetDB();
		

		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);

			// falls der user nicht valid ist gehe weiter
			if (udbo.getState() != 0)
				continue;

			if ((udbo.getRegistriert().contains("?"))
					|| (udbo.getRegistriert().contains("null")))
			{
				Tracer.WriteTrace(20, "W:kein user aufnahmedatum user<"
						+ udbo.get_username() + "> datum<"
						+ udbo.getRegistriert() + "> no ranking!");
				continue;
			}

			System.out.println(" <" + i + "|" + useranz + "> Add user <"
					+ udbo.get_username() + "> to Rankingliste");
			int rangpoints = udbo.ReadUserInfoListeCalRank(ugewinnedb, aktdb,
					usergewinnflag, rp);

			udbo.setRankpoints(rangpoints);
			//den rang erst mal löschen
			udbo.setRang(50000);
			udbo.DeleteUserPostingListeMem();
		}
		this.WriteDB();
		aktdb = null;
		
		//UserDB wird nach den "Rangpunkten" sortiert
		UserComperatorRangpoints c = new UserComperatorRangpoints();
		Collections.sort(this.dbliste,c);
		this.WriteDB();
		
		// sortiert und vergibt den Rang
		Skaliere100000(this, GC.rootpath
				+ "\\db\\rankingliste_1OhneUserchar.txt");

		this.WriteDB();
		/* rechne den userrang mit rein */
		if (useUsercharakterflag == true)
		{
			if (userpostverhdb == null)
				Tracer
						.WriteTrace(10,
								"Error: erst userpostverhalten bestimmen");

			verschmelzeRangeUserpostverh(this, 	userpostverhdb);

			//sortiere nach "Rang"
			UserComperatorRang cx = new UserComperatorRang();
			Collections.sort(this.dbliste,cx);
			this.WriteDB();
			
			//skalliere auf ränge 100000
			Skaliere100000(this, GC.rootpath
					+ "\\db\\rankingliste_2MitUserchar.txt");
			this.WriteDB();
		}
		this.WriteDB();
	}

	public void ShowUser()
	{
		int i = 0;
		int useranz = GetanzObj();
		for (i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			System.out.println("i=" + i + " user<" + udbo.get_username()
					+ "> date<" + udbo.getRegistriert() + "> uid<"
					+ udbo.getUserId() + ">");
		}
	}

	public void hit_user(String threadname, String username, int tid)
	// der user mit username bekommt einen hitpoint für den Thread mit der tid
	// für einen thread kann ein user nur einen hit bekommen
	{

		Inf inf = new Inf();

		int useranz = GetanzObj();
		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			if (udbo.get_username().equals(username) == true)
			{
				if (hitthreads[i] == null)
					hitthreads[i] = new ArrayList<Integer>();

				if (hitthreads[i].contains(tid) == false)
				{
					System.out.println("add hitpoint <" + threadname + "> <"
							+ username + ">");
					inf.appendzeile(hitpointinfofile, "user<" + username
							+ "> get hitpoint", true);
					hitthreads[i].add(tid);
					hitpoints[i]++;
				}
			}
		}
	}

	public void UpdateAllePostinglisten(int forceflag)
	{
		// wenn das forceflag =1 ist wird immer runtergeladen
		// wenn das forceflag =0 ist wird nur runtergeladen wenn überhaupt nix
		// da ist
		DownloadManager dm = new DownloadManager(GC.MAXLOW);
		int anz = GetanzObj();
		ResetDB();

		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetAktObject();
			System.out.println("update postingliste <" + i + "|" + anz + ">");
			char first = udbo.get_username().charAt(0);

			String filename = GC.rootpath + "\\downloaded\\userhtmlpages\\@"
					+ first + "\\" + udbo.get_username() + "_postings"
					+ ".html";
			// lade nur wenn forceflag =1 oder nix da ist
			if ((forceflag == 1)
					|| (FileAccess.FileAvailable(filename) == false))
				udbo.WebUpdateStep3(dm, 5);

			SetNextObject();
		}

	}

	public void writeHitpointstore()
	{
		// schreibt interne HitpointStruktur
		// tnickel_de#
		// ausserdem wird addobservethread.txt generiert
		String zeile = null;
		int i = 0, points = 0;
		String froot = null;

		Inf inf = new Inf();

		froot = GC.rootpath;
		String observenam = froot + "\\db\\addobservethreads.txt";
		String hitpointnam = froot + "\\db\\hitpoint.dbs";
		FileAccess.FileDelete(observenam, 0);
		FileAccess.FileDelete(hitpointnam, 0);
		int useranz = GetanzObj();
		try
		{
			BufferedWriter ouf = new BufferedWriter(new FileWriter(hitpointnam));

			ouf.write("###########Hitpointliste############### useranz="
					+ useranz
					+ " username,hitpoints,hitthreads anz, hitthreads");
			ouf.newLine();

			/*
			 * for (i = 0; i < useranz; i++) { if (hitthreads[i] == null) szeile
			 * = "0#0"; else szeile = hitthreads[i].size() + "#" +
			 * hitthreads[i];
			 * 
			 * zeile = pageusername[i] + "#" + hitpoints[i] + "#" + szeile;
			 * ouf.write(zeile); ouf.newLine(); }
			 */
			ouf
					.write("###########Hitpointliste nach Hitpoints sortiert############### ");
			ouf.newLine();
			for (points = 10; points > 0; points--)
			{
				ouf.write("********** user mit n=" + points
						+ " hitpoints **********");
				ouf.newLine();
				for (i = 0; i < useranz; i++)
				{
					UserDbObj udbo = (UserDbObj) GetObjectIDX(i);

					if (hitthreads[i] != null)
						if (hitthreads[i].size() == points)
						{
							// schreibe hitpoints.dbs
							zeile = udbo.get_username();// +"#"+RegistriertSeit[i]+"#"+Antworten[i];
							ouf.write(zeile);
							ouf.newLine();
							// schreibe addobservethreads.txt
							for (int j = 0; j < hitthreads[i].size(); j++)
							{
								inf.appendzeile(observenam, Integer
										.toString(hitthreads[i].get(j)), true);
							}
						}
				}
				System.out.println("fertig3567889");
			}
			ouf.close();
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

	public void postprocess()
	{
	}

	public void exportRanking(String fnam)
	{
		Inf inf = new Inf();
		FileAccess.FileDelete(fnam, 0);
		inf.setFilename(fnam);

		int useranz = this.GetanzObj();
		for (int i = 0; i < useranz; i++)
		{
			UserDbObj udbo = (UserDbObj) GetObjectIDX(i);
			inf
					.writezeile(udbo.get_username() + "#"
							+ udbo.getRangpoints() + "#" + udbo.getRang() + "#"
							+ udbo.getRankinginfostring2());
			System.out.println("i<" + i + "> export unam<" + udbo.getUsername()
					+ "> rang<" + udbo.getRang() + ">");
		}
		inf.close();
	}

	public void importRanking(String fnam)
	{
		String[] sParam = new String[]
		{};
		String zeile = null;
		int rzaehl = 0;

		Inf inf = new Inf();
		inf.setFilename(fnam);

		// löschen (Setze mittleren Rang)
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) this.GetObjectIDX(i);
			udbo.setRang(50000);
			udbo.setRankpoints(0);
			udbo.setRankinginfostring("unknown");
		}

		// einlesen und setzen
		while ((zeile = inf.readZeile()) != null)
		{
			sParam = zeile.split("#");
			String nam = sParam[0];
			int rpoint = Integer.valueOf(sParam[1]);
			int rang = Integer.valueOf(sParam[2]);
			String ri = String.valueOf(sParam[3]);

			UserDbObj udbo = (UserDbObj) this.getUserobj(nam);

			if (udbo == null)
			{
				Tracer.WriteTrace(20, "Warning: rangdaten user<" + nam
						+ "> nicht in user.db");
				rzaehl++;
				continue;
			}

			udbo.setRankpoints(rpoint);
			udbo.setRang(rang);
			udbo.setRankinginfostring2(ri);
			Tracer.WriteTrace(20, "<" + rzaehl + ">Import:unam<" + nam
					+ "> rang<" + rang + "> rangpoints<" + rpoint
					+ "> Ranginfostring<" + ri + ">");
			rzaehl++;
		}
		inf.close();

		// Speichern
		this.WriteDB();
	}
}
