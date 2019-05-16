package objects;

import hilfsklasse.DateExcecption;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.IsValid;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import html.Userhtmlpage;
import html.UserhtmlpagesBeitraege;
import html.UserhtmlpagesDiskussionen;
import interfaces.DBObject;
import internetPackage.DownloadManager;

import java.io.File;

import mainPackage.GC;

import org.eclipse.swt.widgets.Display;

import ranking.Rangparameter;
import stores.AktDB;
import stores.MidDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;
import userHilfObj.UserPostingListe;
import db.DbException;

public class UserDbObj extends Obj implements DBObject, Comparable<UserDbObj>
{
	// Dieses Userobjekt Speichert alle relavanten Infos für einen USER
	// Beinhaltet die infos die für userdb benötigt werden + weitere daten

	// UserLastPostings beinhaltet die infos wo ein user in letzter Zeit
	// gepostet hat
	// static private List<UserInfPostingObj> UserLastPostings = new
	// private Random generator = new Random();
	private int postinglistecounter = 0;
	private int UserId = 0;
	private int Themen = 0;
	private int Antworten = 0;
	private int Threads = 0;
	private int Postings = 0;
	private int Blogs = 0;
	private int state = 0;
	private int mode = 0;
	private int anzpostingliste = 0;
	private float beitraegetag = 0;
	private int rang = -99;
	private int dummy1 = 0;
	private int rangpoints = -99;
	private String username_glob = null;
	private String registriert = null;
	private String letzteAktualisierung = null;
	private int woexpertenflag = 0;
	// trefferwkeit =wahrscheinlichkeit der bewerteten postings auf einen gewinn
	// Mindestanzahl postings ==100
	private float trefferwkeit = 0;
	// Manuelle Bewertung von 0---1000
	private float manbewertung = 0;

	// static private RankingUser ru = null; new RankingUser();
	// static private RankingUser ru = null;
	String filename = null;
	private UserPostingListe upl = new UserPostingListe();
	private int boostrang_t = 0;
	// prio=0=keine, prio1=sehr gut, prio2=gut....
	private int prio = 0;

	// temporäre variablen die nicht in der DB gespeichert werden
	private int neusteThreadTage = 999999999;
	private int neusteThreadId = 0;
	// Dieser Infostring wird nur benutzt um im infofile weitere infos bzgl. des
	// Rankings zu
	// Verfügung zu stellen
	private String rankinginfostring = null;
	private String rankinginfostring2 = null;

	// variablen für der Ranking

	public UserDbObj()
	{

	}

	public UserDbObj(String name, String Regdatum, int uid, int th, int antw,
			int thr, int post, int blog, int sta, int m)
	{
		username_glob = new String(name);
		if (Regdatum.contains("vor April") == true)
			Regdatum = GC.startdatum;
		registriert = new String(Regdatum);
		UserId = uid;
		Themen = th;
		Antworten = antw;
		Threads = thr;
		Postings = post;
		Blogs = blog;
		state = sta;
		mode = m;
	}

	public UserDbObj(String zeile) throws DbException
	{
		int n = Tools.countZeichen(zeile, "#");
		if ((n < 12) || (n > 16))
		{
			System.out.println(UserDbObj.class.getName()
					+ ":ERROR:username fehlerhaft in userstore.db zeile=<"
					+ zeile + "> ");
			Tracer.WriteTrace(10, UserDbObj.class.getName()
					+ ":ERROR:username fehlerhaft in userstore.db zeile=<"
					+ zeile + ">");
			throw new DbException("Fehler im Userdbobj" + zeile);
		}
		try
		{
			if (zeile.contains("vor April") == true)
				zeile = zeile.replace("vor April", GC.startdatum);

			// username,userid,datum,themen,antworten,state,mode
			username_glob = new String(Tools.nteilstring(zeile, "#", 1));
			// System.out.println("username="+username);
			UserId = Integer.valueOf(Tools.nteilstring(zeile, "#", 2));
			registriert = new String(Tools.nteilstring(zeile, "#", 3));
			if (registriert.contains("vor April") == true)
				registriert = new String(GC.startdatum);
			Themen = Integer.valueOf(Tools.nteilstring(zeile, "#", 4));
			Antworten = Integer.valueOf(Tools.nteilstring(zeile, "#", 5));
			Threads = 0;
			Postings = 0;
			Blogs = 0;
			state = Integer.valueOf(Tools.nteilstring(zeile, "#", 6));
			mode = Integer.valueOf(Tools.nteilstring(zeile, "#", 7));
			anzpostingliste = Integer.valueOf(Tools.nteilstring(zeile, "#", 8));
			beitraegetag = Float.valueOf(Tools.nteilstring(zeile, "#", 9));
			dummy1 = Integer.valueOf(Tools.nteilstring(zeile, "#", 10));
			rangpoints = Integer.valueOf(Tools.nteilstring(zeile, "#", 11));
			letzteAktualisierung = new String(Tools.nteilstring(zeile, "#", 12));
			rang = Integer.valueOf(Tools.nteilstring(zeile, "#", 13));

			if (registriert.contains("vor April"))
			{
				registriert = new String("01.01.1999");
			}
			if (n == 13)
			{
				woexpertenflag = Integer.valueOf(Tools.nteilstring(zeile, "#",
						14));
			}
			if (n == 14)
			{
				trefferwkeit = Float.valueOf(Tools.nteilstring(zeile, "#", 15));
			}
			if (n == 15)
			{
				manbewertung = Float.valueOf(Tools.nteilstring(zeile, "#", 16));
			}
			if (n == 13)
			{
				prio = Integer.valueOf(Tools.nteilstring(zeile, "#", 17));
			}

		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ResetPostingObj()
	{
		postinglistecounter = -1;
	}

	public UserThreadPostingObj getNextUserTheadPostingObj()
	{
		postinglistecounter++;
		if (postinglistecounter >= upl.getSize())
			return null;
		return upl.getObjIDX(postinglistecounter);
	}

	// Wird für das Sortieren verwendet
	public int compareTo(UserDbObj argument)
	{

		if (rangpoints > argument.rangpoints)
			return -1;
		if (rangpoints < argument.rangpoints)
			return 1;

		return 0;
	}

	public String GetAufnahmedatumFuerThreadid(int threadid)
	{
		// es wird rausgesucht wenn der user diese threadid das erste mal
		// in seiner Postingliste aufgenommen hat.
		// Also: Wann ist der User das erste mal in diesem Thread gekommen
		// return: null, der user kennt diese Threadid überhaupt nicht
		// datum: dies ist das Aufnahmedatum dieser Threadid
		if (upl == null)
			upl.ReadUserInfoListe(this);

		UserThreadPostingObj uipo = upl.getUserObj(threadid);
		if (uipo == null)
			return null;
		else
		{
			return (uipo.getAufnahmeDatum());
		}
	}

	public String getRankinginfostring()
	{
		return rankinginfostring;
	}

	public void setRankinginfostring(String rankinginfostring)
	{
		this.rankinginfostring = rankinginfostring;
	}

	public String getRankinginfostring2()
	{
		return rankinginfostring2;
	}

	public void setRankinginfostring2(String rankinginfostring)
	{
		this.rankinginfostring2 = rankinginfostring;
	}

	public float getBeitraegetag()
	{
		return beitraegetag;
	}

	public void setBeitraegetag(float beitraegetag)
	{
		this.beitraegetag = beitraegetag;
	}

	public String getLetzteAktualisierung()
	{
		return letzteAktualisierung;
	}

	public void setLetzteAktualisierung(String letzteAktualisierung)
	{
		this.letzteAktualisierung = letzteAktualisierung;
	}

	public int getRangpoints()
	{

		return rangpoints;
	}

	public void setRankpoints(int rankpoints)
	{
		this.rangpoints = rankpoints;
	}

	public int getThreadid()
	{
		return 0;
	}

	public int getUserId()
	{
		return UserId;
	}

	public void setUserId(int userId)
	{
		UserId = userId;
	}

	public int getThemen()
	{
		return Themen;
	}

	public void setThemen(int themen)
	{
		Themen = themen;
	}

	public int getAntworten()
	{
		return Antworten;
	}

	public void setAntworten(int antworten)
	{
		Antworten = antworten;
	}

	public int getThreads()
	{
		return Threads;
	}

	public void setThreads(int threads)
	{
		Threads = threads;
	}

	public int getPostings()
	{
		return Postings;
	}

	public void setPostings(int postings)
	{
		Postings = postings;
	}

	public int getBlogs()
	{
		return Blogs;
	}

	public void setBlogs(int blogs)
	{
		Blogs = blogs;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		// state== 99 fehlerhaft
		this.state = state;
	}

	public int getMode()
	{
		return mode;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	public String getUsername()
	{
		return username_glob;
	}

	public void setUsername(String username)
	{
		this.username_glob = username;
	}

	public String getRegistriert()
	{
		if (registriert != null)
		{
			if (registriert.contains("vor April") == true)
				registriert = registriert.replace("vor April", GC.startdatum);
		}
		return registriert;

	}

	public void setRegistriert(String registriert)
	{
		if (registriert != null)
		{
			if (registriert.contains("vor April") == true)
				registriert = registriert.replace("vor April", GC.startdatum);
		}
		this.registriert = registriert;
	}

	public String get_username()
	{
		return username_glob;
	}

	public int getAnzpostingliste()
	{
		return anzpostingliste;
	}

	public void setAnzpostingliste(int anzpostingliste)
	{
		this.anzpostingliste = anzpostingliste;
	}

	public int getWoexpertenflag()
	{
		return woexpertenflag;
	}

	public void setWoexpertenflag(int woexpertenflag)
	{
		this.woexpertenflag = woexpertenflag;
	}

	public float getTrefferwkeit()
	{
		return trefferwkeit;
	}

	public void setTrefferwkeit(float trefferwkeit)
	{
		this.trefferwkeit = trefferwkeit;
	}

	public String toString()
	{
		return (username_glob + "#" + UserId + "#" + registriert + "#" + Themen
				+ "#" + Antworten + "#" + state + "#" + mode + "#"
				+ anzpostingliste + "#" + beitraegetag + "#" + dummy1 + "#"
				+ rangpoints + "#" + letzteAktualisierung + "#" + rang + "#"
				+ woexpertenflag + "#" + trefferwkeit + "#" + manbewertung
				+ "#" + prio);
	}

	public String GetSaveInfostring()
	{
		return "username#userid#registriert#themen#antworten#state#mode#anzpostingliste#beitraegetag#dummy#rankpoints#letzteAktualisierung#rang#woexpertenflag#trefferwkeit#manbewertung#prio";
	}

	public int ReadUserInfoListeCalRank(UserGewStrategieDB2 ugewinnedb,
			AktDB aktdb, int usergewinnflag, Rangparameter rp)
	{
		// liest die Beitragsliste ein die schon da ist
		// Die Beitragsliste beinhaltet die Threads die ein user in letzter Zeit
		// besucht hatte
		// berechnet anschliessend das ranking
		// cleanflag: falls dies flag 1 ist dann wird der speicher nach der
		// berechnung wieder freigegeben
		// return: anz elemente in dieser Liste

		upl.ReadUserInfoListe(this);
		rangpoints = rp
				.CalcRanking(ugewinnedb, aktdb, usergewinnflag, this, rp);
		return rangpoints;

	}

	private boolean UeberpruefeAufNeuePostings(String dblastload,
			String userlastload, int mindestzeit)
	{
		// dblastload= ist die ladezeit der threads.db
		// userlastload= ist die ladezeit des threads im geladenen userprofil
		// (aktuelle zeit)
		// mindestzeit= ist die zeit die mindestens verstrichen sein muss damit
		// der thread neu upgedatet wird

		if ((dblastload == null) || (dblastload.equals("0"))
				|| (dblastload.equals("null")))
		{
			// in der DB ist ladezeit 0, dann lade immer da noch nix da ist
			return true;
		}

		String dbtime1 = null;
		try
		{
			dbtime1 = Tools.addTimeHours(dblastload, mindestzeit);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			Tracer.WriteTrace(10, "E:dblastload <" + dblastload
					+ "> userlastload <" + userlastload + "> mindestzeit<"
					+ mindestzeit + ">");
			e.printStackTrace();
		}

		// user hat neue Postings wenn die Zeiten im userprofil für den thread
		// neuer sind
		if (Tools.datum_ist_aelter(dbtime1, userlastload) == true)
			return true;
		else
			return false;
	}

	public AktDbObj bearbeiteAKTDB(MidDB middb, ThreadsDB tdb,
			DownloadManager dm, int tid, AktDB aktdb)
	{
		// Werte die neuen Aktien des Users aus
		// trage evtl. neue Aktien in der Aktdb ein
		AktDbObj aktdbobj = null;
		if (aktdb.CheckThreadid(tid) < 0)
		{
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath + "\\db\\reporting\\NeueThreads.txt");

			// aktie ist nicht in der aktdb.db dann nimm auf
			aktdbobj = new AktDbObj(tid, "0", "0", "0", "0", 0, 0, "0");
			aktdbobj.lade_aktname_symbol_wkn(middb, tdb, dm, 0, 0, null);

			aktdb.AddObject(aktdbobj);
			String msg = "I:Add threadname to AKTDB<" + aktdbobj.getAktname()
					+ "> tid<" + tid + "> mid<" + aktdbobj.getMasterid()
					+ "> wkn<" + aktdbobj.getWkn() + ">";
			Tracer.WriteTrace(30, msg);
			inf.writezeile(Tools.get_aktdatetime_str() + ":" + msg);
			inf.close();

			aktdbobj.setNewflag(aktdbobj.getNewflag() + 1);
			// aktdb.WriteDB();
			return aktdbobj;
		} else
			// ist drin dann hole masterid und symbol
			aktdbobj = (AktDbObj) aktdb.GetObject(tid);
		return aktdbobj;
	}

	private void prueftWorkAlteFragmente(ThreadDbObj tdbo)
	{
		// prüft nach ob im Filesystem auch alles für den neuen Thread
		// ok ist.
		// Es wird also überprüft ob sich auf Platte unter dem Thread.db
		// -pfad nichts altes mehr befindet
		// falls was altes da ist wird das alte Verzeichniss gelöscht

		if (tdbo == null)
			Tracer.WriteTrace(10, "internal tdbo==null");

		String tn = tdbo.getThreadname();
		char pref = tn.charAt(0);

		String fpath = GC.rootpath + "\\downloaded\\threads\\@" + pref + "\\"
				+ tdbo.getThreadname();
		if (FileAccess.checkDirectory(fpath) == true)
		{
			DownloadManager Wpage = new DownloadManager(1);
			Tracer.WriteTrace(
					20,
					"Warning: möchte einen neuen Thread<"
							+ tdbo.getThreadname()
							+ "> in thread.db aufnahmen aber schon Datenfiles auf platte!!=> lösche files ?");
			FileAccess.deleteDirectory(new File(fpath));
			FileAccess.checkgenDirectory(fpath);

			// Seitenzaehler löschen
			String zfile = GC.rootpath + "\\db\\seitenzaehler\\"
					+ tdbo.getThreadname() + ".db";
			FileAccess.FileAvailable(zfile);
			FileAccess.FileDelete(zfile, 0);

			// primepage laden
			String urlstr = tdbo.GetUrlName(1);
			String fnam = tdbo.GetSavePageName_genDir(1) + ".gzip";
			Wpage.DownloadHtmlPage(urlstr, fnam, 0, 8000000, 1, 1, 0);
			// mindestens eine webseite ist vorhanden
			tdbo.setPageanz(1);
			Wpage.waitEnd();
		}
	}

	private void bearbeiteTHREADSDB(MidDB middb, int tid, AktDbObj aktdbobj,
			ThreadsDB tdb, UserDB udb, Inf inf, String inffile,
			int mindestzeit, UserThreadPostingObj upostobj, int threadupdateflag)
	{
		// Werte die neuen Aktien des Users aus
		// trage evtl. neue Aktien in der threadsdb ein
		// threadupdateflag:sagt welche threadtypen upgedatet werden sollen z.b.
		// aktienthread,oder
		// diskussionsthread (GC.NUR_AKTIENTHREADS_UPDATEN)
		ThreadDbObj tdbo = null;
		String tname = aktdbobj.getAktname();
		Inf inf2 = new Inf();

		if (tname.equals("") == true)
			Tracer.WriteTrace(10,
					"internal akttnam<" + aktdbobj.getThreadfullname()
							+ "> not allowed tid<" + aktdbobj.getThreadid()
							+ "> mid<" + aktdbobj.getMasterid() + ">");

		if (tdb.CheckThreadid(tid) < 0)
		{
			// Tid noch nicht drin
			// Berechne einen Namen
			inf2.setFilename(GC.rootpath + "\\db\\reporting\\NeueThreads.txt");

			tname = tdb.calcTname(tname);

			// aktie noch nicht in der threadsdb
			String msg = "I:Thread<" + tname + "> tid<" + tid
					+ ">ganz neu  Threads.db wird erweitert !!!";
			Tracer.WriteTrace(20, msg);
			inf.appendzeile(inffile, msg, true);
			inf2.writezeile(Tools.get_aktdatetime_str() + ":" + msg);
			inf2.close();

			// hier wird ein unerwünschter Thread nicht aufgenommen
			if (upostobj.getThreadname().contains(
					"wallstreet-online User stellen Weltrekord auf"))
			{
				Tracer.WriteTrace(20,
						"I:Thread to big <" + upostobj.getThreadname()
								+ " -> I drop it");
				upostobj = upl.getNextPostingObj();
				return;
			}

			// prüft ob man mit dem tnamen auf dem Filesystem arbeiten kann
			if (IsValid.checkFilesystemname(tname) == false)
			{
				Tracer.WriteTrace(20, "W:invalid filename <" + tname + ">");
				return;
			}

			// falls die Threadid überhaupt nicht in der threadsdb, dann
			// nimm mit state 0 auf
			tdbo = new ThreadDbObj(tname, upostobj.getThreadid(), "0", 0,
					aktdbobj.getMasterid(), aktdbobj.getSymbol(), 1, "0", 0, 0,
					0, upostobj.getLastposting(), "0");

			// fügt object in der threadsdb ein
			tdb.AddObject(tdbo);

			// prüft ob auf platte noch alte fragmente
			// xxxxxxxx1111 temporär offline prueftWorkAlteFragmente(tdbo);

			// der thread wird auf den neusten stand gebracht
			tdbo.UpdateThread(middb, udb, threadupdateflag, mindestzeit, tdb);
			tdb.UpdateObject(tdbo);

			// xxxxxx1111 sofort schreiben auf Platte
			tdb.WriteDB();

		} else

		{
			tdbo = (ThreadDbObj) tdb.GetObject(tid);

			//falls thread blockiert ist dann mache nix
			if (tdbo.getState() == 0)
			{

				tdb.UpdateObject(tdbo);
				String dblastload = tdbo.getLastThreadPosttime();
				String userlastload = upostobj.getLastposting();

				if (UeberpruefeAufNeuePostings(dblastload, userlastload,
						mindestzeit) == true)
				{
					// falls ein thread neue postings hat
					// dann date up
					// *********************** ACHTUNG ****************
					// Achtung hier ist ein Bug bei WO: Es werden nur die
					// letzten 50 Threads des Users bei
					// wo-aktualisiert. D.h. evtl. ist im hier gespeicherten
					// Userprofil ein zu altes Datum für den
					// Thread. D.h. hier muss ein echter Datumsvergleich gemacht
					// werden damit geschaut wird ob in der
					// db was neueres ist.

					if (upostobj.getLastposting().equalsIgnoreCase("0") == true)
						System.out.println("leeres posting");

					Tracer.WriteTrace(
							20,
							"I:user<" + username_glob
									+ ">hat im thread Thread tid<" + tid
									+ "> name<" + tdbo.getThreadname()
									+ "> neue Postings old-tdbdate in db<"
									+ tdbo.getLastThreadPosttime()
									+ "> newpostdate im Userprofil<"
									+ upostobj.getLastposting() + ">");
					tdbo.UpdateThread(middb, udb, threadupdateflag,
							mindestzeit, tdb);
					tdbo.setLastThreadPosttime(upostobj.getLastposting());
					tdb.UpdateObject(tdbo);
				}
				// prüft ob die postdb für den thread vorhanden ist
				// wenn nein wird postdb erstellt und der thread upgedatet

				if (GC.ONLINEMODE == 1)
					if (tdbo.checkPostdbAvailable() == false)
						tdbo.UpdateThread(middb, udb, threadupdateflag,
								mindestzeit, tdb);
			} else
			{
				Tracer.WriteTrace(20,
						"Info: Thread has state <" + tdbo.getState()
								+ "> I need state 0 => do nothing !");

			}
		}
	}

	public boolean WorkUserPostingListe(MidDB middb, DownloadManager dm,
			UserGewStrategieDB2 ugewinnedb, AktDB aktdb, ThreadsDB tdb,
			UserDB udb, int mindestzeit, int threadupdateflag,
			int usergewinnrangflag, String pref, Display dis, String rangfile)
	{
		// a)überprüft ob alle Threads der Beitragsliste in aktdb.db threads.db
		// sind ?
		// liest die bereits 'bestehende' web-betragsliste
		// dated die threads up, also neue Webseiten und threaddata//...db
		// prüft auch nach ob die postdb vorhanden ist
		//
		// mindestzeit: wurde ein thread upgedatet muss anschliessend erst die
		// mindestzeit ablaufen damit er wieder upgedatet wird
		//
		// threadupdateflag:sagt welche threadtypen upgedatet werden sollen z.b.
		// aktienthread,oder
		// diskussionsthread (GC.NUR_AKTIENTHREADS_UPDATEN)
		// Wenn GC.NUR_AKTIENTHREADS_UPDATEN angegeben ist werden in threads.db
		// auch nur Aktienthreads neu aufgenommen
		//
		// return: true falls thread ganz neu ist und in der aktdb aufgenommen
		// worden ist

		AktDbObj aktdbobj = null;
		Rangparameter rp = new Rangparameter(rangfile);
		UserThreadPostingObj upostobj = new UserThreadPostingObj(username_glob);
		int tid, firstflag = 0;

		Inf inf = new Inf();
		String inffile = GC.rootpath + "\\info\\" + pref
				+ "\\userpostings\\observeinfo.txt";

		// liest threaddaten des users
		int anzuserthreads = ReadUserInfoListeCalRank(ugewinnedb, aktdb,
				usergewinnrangflag, rp);
		int userthcount = 0;
		int j = 1;

		// System.out.println("laufe von 1 bis maxanz="+anz);
		while (5 == 5)
		{
			Swttool.wupdate(dis);
			/*
			 * System.out.println("user <" + this.username + "> <" + userthcount
			 * + "|Rank:" + anzuserthreads + ">");
			 */
			userthcount++;

			if (firstflag == 0)
			{
				// beim ersten Aufruf das erste Threadobjekt holen
				upostobj = upl.getFirstPostingObj();
				firstflag = 1;
			}
			if (upostobj == null)
				break;

			j++;
			tid = upostobj.getThreadid();

			if (tid == 0)
				Tracer.WriteTrace(
						20,
						"W:neuer thread nicht alle daten vollständig tid<"
								+ tid + "> threadname<"
								+ upostobj.getThreadname() + ">");

			// schaut nach ob tid in aktdb, wenn nein nimm auf und date up
			aktdbobj = this.bearbeiteAKTDB(middb, tdb, dm, tid, aktdb);

			// schaue nach ob tid in threadsdb, wenn nein nimm auf und date up
			// betrachte aber das updateflag, falls aktienthreads=1, dann werden
			// nur
			// aktienthreads aufgenommen

			if (aktdbobj.getAktname().contains("._"))
			{
				System.out.println("name=" + aktdbobj.getAktname());
				System.out.println("warning ._ in name");
			}
			// nur aktienthreads kommen in die threadsdb
			if ((aktdbobj.getMasterid() != 0)
					&& (aktdbobj.getSymbol().length() >= 2))
			{
				this.bearbeiteTHREADSDB(middb, tid, aktdbobj, tdb, udb, inf,
						inffile, mindestzeit, upostobj, threadupdateflag);
			}
			// tdb.WriteDB();
			// Wenn der thread in der db ist dann muss er Status 8000 haben (xxx
			// warum status 80000 ????ß)

			upostobj = upl.getNextPostingObj();
			if (upostobj == null)
				break;
		}

		// der user hat keine postings
		if (aktdbobj == null)
			return false;

		if (aktdbobj.getNewflag() > 0)
			return true;
		else
			return false;

	}

	public boolean WebUpdateStep1(DownloadManager dm, int thresholdhours,
			int forceflag)
	{

		// Update der Userdaten
		// läd die userdaten userid, themen, antworten vom WEB
		// falls forceflag ==1 dann wird nicht auf den state geachtet
		String filename = null;
		String unameweb = null;
		String webnam = null;
		if (forceflag == 0)
			if (getState() != 0)
				return true;

		char first = username_glob.charAt(0);

		filename = GC.rootpath + "\\downloaded\\userhtmlpages\\@" + first
				+ "\\" + username_glob + ".html.gzip";
		unameweb = username_glob.replace(" ", "+");
		// download userinfo from webside and store file
		webnam = "http://www.wallstreet-online.de/dyn/userzentrum/user-info.html?search_nick="
				+ unameweb;

		if (FileAccess.FileLength(filename) < 50)
		{
			Tracer.WriteTrace(20, "I:file <" + filename
					+ "> zu kurz <50bytes -> gelöscht");
			FileAccess.FileDelete(filename, 0);
		}

		if (FileAccess.CheckIsFileOlderHours(filename, thresholdhours) == true)
		{
			dm.DownloadHtmlPage(webnam, filename, 0, 350000, 0, 1, 0);
			Tracer.WriteTrace(50, "I:hab userdaten fnam <" + filename
					+ "> geladen");
		} else
			System.out.println("I:File <" + filename
					+ "> wird nicht upgedatet da zu neu");

		return true;
	}

	public boolean WebUpdateStep2()
	{
		// Update der Userdaten
		// Die geladenen Webdaten werden in die user.db geschrieben
		// SYNCHONISIZE
		// Es werden ggf. userid,themen und Antworten aktualisiert

		String filenamz = null;
		String unam = get_username();
		char first = unam.charAt(0);

		filenamz = GC.rootpath + "\\downloaded\\userhtmlpages\\@" + first
				+ "\\" + unam + ".html.gzip";
		Userhtmlpage uhtml = new Userhtmlpage(filenamz);

		String regdatum = uhtml.getRegistriertSeit();

		// falls die mitgliedschaft des users beendet worden ist
		if (uhtml.checkUserErrorInformation() == true)
		{
			state = 99;
			setState(99);
			return false;
		}

		// Plausi Webseitenaufbau redatum

		setRegistriert(regdatum);

		try
		{
			setUserId(uhtml.getUserid());
			setThemen(uhtml.getThemen());
			setAntworten(uhtml.getAntworten());
			this.setWoexpertenflag(uhtml.getWoExpertenflag());
			this.setBeitraegetag(uhtml.getBeitraegeTag());
			this.setLetzteAktualisierung(Tools.get_aktdatetime_str());
			return true;

		} catch (Exception E)
		{
			Tracer.WriteTrace(20, "Error: Exception user<" + get_username()
					+ "> fehlerhaft fnam<" + filenamz + "> set state=99");
			E.printStackTrace();
			setState(99);
			return false;
		}
	}

	public void WebUpdateStep3(DownloadManager dm, int thresholdhours)
	{
		// Update der Userdaten
		// läd die posting und threadlisten vom web
		// es wird nur geladen wenn die zeit thresholdhours überschritten wurde
		String webnam = null;

		char first = username_glob.charAt(0);

		// Lade Beiträge vom web
		filename = GC.rootpath + "\\downloaded\\userhtmlpages\\@" + first
				+ "\\" + username_glob + "_beitraege" + ".html";
		webnam = "http://www.wallstreet-online.de/userinfo/"
				+ String.valueOf(UserId) + "-postings.html";

		if (FileAccess.CheckIsFileOlderHours(filename, thresholdhours) == true)
			dm.DownloadHtmlPage(webnam, filename, 0, 50000, 0, 1, 0);
		else
			System.out.println("I:File <" + filename
					+ "> wird nicht upgedatet da zu neu");

		// Lade Diskussionen vom web
		filename = GC.rootpath + "\\downloaded\\userhtmlpages\\@" + first
				+ "\\" + username_glob + "_diskussionen" + ".html";
		webnam = "http://www.wallstreet-online.de/userinfo/"
				+ String.valueOf(UserId) + "-threads.html";

		if (FileAccess.CheckIsFileOlderHours(filename, thresholdhours) == true)
			dm.DownloadHtmlPage(webnam, filename, 0, 50000, 0, 1, 0);
		else
			System.out.println("I:File <" + filename
					+ "> wird nicht upgedatet da zu neu");
	}

	public int WebUpdateStep4(UserGewStrategieDB2 ugewinnedb, int threshold,
			AktDB aktdb, int usergewinnrangflag, String pref, String rangfile)
	{
		// Update der Userdaten
		// Synchronizie
		// die daten der geladenen userdaten werden in userstore.db gespeichert
		// return: die anzahl der neuen Threads wird zurückgemeldet
		String threadname = null;
		String lastpost_str = null;
		int threadid = 0, countnewthreads = 0;
		Inf inf = new Inf();
		Rangparameter rp = new Rangparameter(rangfile);

		String inffile = GC.rootpath + "\\info\\" + pref
				+ "\\userpostings\\observeinfo.txt";
		if (filename == null)
			filename = GC.rootpath + "\\db\\postingliste\\" + username_glob
					+ ".db";

		// liest die bereits bestehende alte web-betragsliste und baut
		// eine Liste mit UserThreadPostingObj auf.
		ReadUserInfoListeCalRank(ugewinnedb, aktdb, usergewinnrangflag, rp);

		if (UserId == 0)
			Tracer.WriteTrace(10, "Error:Keine Daten für user<" + username_glob

			+ "> bitte erst alle userdaten runterladen");
		/*
		 * if (FileAccess.CheckIsFileOlderHours(filename, threshold) == false) {
		 * System.out.println("I:File <" + filename +
		 * "> wird nicht upgedatet da zu neu"); return 0; }
		 */

		char first = username_glob.charAt(0);
		// Werte Beiträge aus
		filename = GC.rootpath + "\\downloaded\\userhtmlpages\\@" + first
				+ "\\" + username_glob + "_beitraege" + ".html";

		UserhtmlpagesBeitraege uhtmlbeitraege = new UserhtmlpagesBeitraege(
				filename);

		// hole nächsten threadnamen aus der threadliste
		try
		{
			while (uhtmlbeitraege.getNextListeneintrag() != null)
			{
				threadname = new String(uhtmlbeitraege.getThreadname());
				threadid = uhtmlbeitraege.getThreadid();
				lastpost_str = new String(uhtmlbeitraege.getLastposting());

				if (upl.threadidAvailable(threadid) == false)
				{
					// ist der Thread ganz neu, dann nimm ihn in der Liste
					// der userthreads auf
					countnewthreads++;
					UserThreadPostingObj upost = new UserThreadPostingObj(
							username_glob);
					upost.setThreadname(threadname);
					upost.setThreadid(threadid);
					upost.setAufnahmeDatum(Tools.get_aktdate_str(null));
					upost.setLastposting(new String(uhtmlbeitraege
							.getLastposting()));
					upl.addUserObj(upost);

					Tracer.WriteTrace(5, "I:User<" + username_glob
							+ "> besucht neuen Thread<" + threadname + ">");
					inf.appendzeile(inffile, "User<" + username_glob
							+ "> besucht neuen Thread<" + threadname + ">",
							true);
				} else
				{
					// wenn das objekt in der userinfoliste da ist wird das
					// lastpost-datum in der
					// userinfo-liste upgedatet

					UserThreadPostingObj upost;
					// holt das bestehende objekt
					upost = upl.getUserObj(threadid);
					upost.setLastposting(lastpost_str);
				}
			}
		} catch (StringIndexOutOfBoundsException e)
		{
			Tracer.WriteTrace(10, "Webseite<" + filename
					+ "> fehlerhaft=> Bitte von Hand löschen und ok drücken");
			e.printStackTrace();
		}

		// werte Diskussionen aus
		filename = GC.rootpath + "\\downloaded\\userhtmlpages\\@" + first
				+ "\\" + username_glob + "_diskussionen" + ".html";

		UserhtmlpagesDiskussionen uhtmldiskussionen = new UserhtmlpagesDiskussionen(
				filename);
		try
		{
			// hole naechstes posting
			while (uhtmldiskussionen.getNextListeneintrag() != null)
			{
				threadname = new String(uhtmldiskussionen.getThreadname());
				// System.out.println("threadname=<" + threadname + ">");
				threadid = uhtmldiskussionen.getThreadid();

				if (username_glob.equals("nahost"))
					System.out.println("found nahost");

				lastpost_str = new String(uhtmldiskussionen.getLastposting());
				if (upl.threadidAvailable(threadid) == false)
				{
					countnewthreads++;
					// ist der Thread ganz neu, dann nimm ihn in der Liste
					// der userthreads auf
					UserThreadPostingObj upost = new UserThreadPostingObj(
							username_glob);
					upost.setThreadname(threadname);
					upost.setThreadid(threadid);
					upost.setAufnahmeDatum(Tools.get_aktdate_str(null));
					upost.setLastposting(new String(uhtmldiskussionen
							.getLastposting()));
					upl.addUserObj(upost);

					Tracer.WriteTrace(5, "I:User<" + username_glob
							+ "> besucht neuen Thread<" + threadname + ">");
					inf.appendzeile(inffile, "User<" + username_glob
							+ "> besucht neuen Thread<" + threadname + ">",
							true);
				} else
				{
					// wenn das objekt in der userinfoliste da ist wird das
					// lastpost-datum in der
					// userinfo-liste upgedatet

					UserThreadPostingObj upost;
					// holt das bestehende objekt
					upost = upl.getUserObj(threadid);
					upost.setLastposting(lastpost_str);
				}
			}
		} catch (StringIndexOutOfBoundsException e)
		{
			Tracer.WriteTrace(10, "Webseite<" + filename
					+ "> fehlerhaft=> Bitte von Hand löschen und ok drücken");
			e.printStackTrace();
		}
		// Schreibe die Liste neu
		upl.WriteUserInfoListe(this);
		upl.deleteUserPostingListe();
		return countnewthreads;
	}

	public UserPostingListe getUserPostingListe()
	{
		if (upl == null)
			upl = new UserPostingListe();
		return upl;
	}

	public void DeleteUserPostingListeMem()
	{
		upl.deleteUserPostingListe();

	}

	// temporäre Variablen
	public int getNeusteThreadTage()
	{
		// gibt an wieviele Tage der neuteste Thread dem user bekannt ist
		return neusteThreadTage;
	}

	public void setNeusteThreadTage(int neusteThreadTagen)
	{
		this.neusteThreadTage = neusteThreadTagen;
	}

	public int getNeusteThreadId()
	{
		// gibt die Threadid des neusten Thread des users bekannt
		return neusteThreadId;
	}

	public void setNeusteThreadId(int neusteThreadId)
	{
		this.neusteThreadId = neusteThreadId;
	}

	public int getRang()
	{
		return rang;
	}

	public void setRang(int rang)
	{
		this.rang = rang;
	}

	public int getBoostrang()
	{
		return boostrang_t;
	}

	public void setBoostrang(int boostrang_t)
	{
		this.boostrang_t = boostrang_t;
	}

	public float getManbewertung()
	{
		return manbewertung;
	}

	public void setManbewertung(float manbewertung)
	{
		this.manbewertung = manbewertung;
	}

	public int getPrio()
	{
		return prio;
	}

	public void setPrio(int prio)
	{
		this.prio = prio;
	}

	public void postprocess()
	{
	}
}
