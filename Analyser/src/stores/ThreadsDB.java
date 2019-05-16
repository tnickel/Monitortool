package stores;

import hilfsklasse.Archive;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.Threads;
import internetPackage.DownloadManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import kurse.KursDbObj;
import kurse.KurseDB;
import mainPackage.GC;
import objects.Obj;
import objects.ThreadDbObj;
import objects.UserDbObj;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import ranking.RangObj;
import ranking.ThreadStatObj;
import attribute.ThreadAttribStoreI;

import comperatoren.ThreadSteigungsfaktorComparator;
import comperatoren.ThreadsSliderComparator;

import db.DB;
import filter.ThreadsDbFilter;

public class ThreadsDB extends DB
{
	static ThreadsDbFilter tdbFilter=new ThreadsDbFilter();
	static Archive ar = new Archive();
	static private HashMap<Integer, Integer> threadidpos = new HashMap<Integer, Integer>();
	public ThreadsDbFilter filter= new ThreadsDbFilter();
	private MidDB middb_glob=null;
	int durchlaufmode = 0;

	private HashSet<Integer> tidmengecheck = new HashSet<Integer>();
	private HashSet<String> namenmengecheck = new HashSet<String>();

	public ThreadsDB()
	{
		// Tracer.WriteTrace(10,
		// "static lade threadsdb ? möchten wir das ? klick ");
		System.out.println("Info:ThreadsDB construktor");

		tidmengecheck.clear();
		namenmengecheck.clear();

		LoadDB("threads", null, 0);
		plausicheck();
		middb_glob = new MidDB(this);
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		// Dies ist ein plausicheck der bei der neuaufnahme von
		// neuen objekten durchgeführt wird
		ThreadDbObj tdboneu = (ThreadDbObj) obj;

		String tnamneu = tdboneu.getThreadname();
		int tidneu = tdboneu.getThreadid();

		if (namenmengecheck.contains(tnamneu) == true)
			Tracer.WriteTrace(10,
					"Error: threads.db inkonsistenz, möchte tnam<" + tnamneu
							+ "> aufnehmen pos<" + namenmengecheck.size()
							+ ">, aber schon vorhanden");

		if (tidmengecheck.contains(tidneu) == true)
			Tracer.WriteTrace(10, "Error: threads.db inkonsistenz, möchte tid<"
					+ tidneu + "> aufnehmen, aber schon vorhanden");

		tidmengecheck.add(tidneu);
		namenmengecheck.add(tnamneu);
		return true;
	}

	private void plausicheck()
	{
		
		
		int changeflag = 0;
		// a)überprüfe tid auf doppeltes vorkommen
		// b)überprüfe threadname auf doppeltes vorkommen
		// c)überprüfe ob der Threadname als Pfad verwendbar ist (Filesystem)
		// aber nur bei Neuaufnahmen
		// d) überprüft ob masterstring korrekt
		HashSet<Integer> tidmenge = new HashSet<Integer>();
		HashSet<String> namenmenge = new HashSet<String>();

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			int tid = tdbo.getThreadid();
			String tnam = tdbo.getThreadname();

			if (tidmenge.contains(tid) == true)
				Tracer.WriteTrace(10,
						"Error: Datenkonsistenz threadsdb hat tid<" + tid
								+ "> doppelt");

			if (namenmenge.contains(tnam.toUpperCase()) == true)
				Tracer.WriteTrace(10,
						"Error: Datenkonsistenz threadsdb hat namen<" + tnam
								+ "> doppelt tid1<" + tid + ">");
			tidmenge.add(tid);
			namenmenge.add(tnam.toUpperCase());

			String mstring = tdbo.getMasterstring();
			if (Tools.is_zahl(mstring))
			{
				Tracer.WriteTrace(20, "Warning: tid<" + tid + "> mstring<"
						+ mstring + "> nicht korrekt => lösche mstring");
				tdbo.setMasterstring(null);
				changeflag = 1;
			}
		}
		if (changeflag == 1)
			this.WriteDB();
	}

	
	
	public String getAktThreadName()
	{
		int id = GetThreadid();
		return (GetThreadName(id));
	}

	public String GetThreadName(int tid)
	{
		ThreadDbObj tdbo = (ThreadDbObj) GetAktObject();
		String name = tdbo.getThreadname();
		return name;
	}

	public void setAktState(int state)
	{
		ThreadDbObj tdbo = (ThreadDbObj) GetAktObject();
		tdbo.setState(state);
	}

	public void setAllState(int state)
	{
		ResetDB();
		while (5 == 5)
		{
			setAktState(state);
			if (SetNextObject() == false)
				break;
		}
	}

	public void setAllMarker(int state)
	{
		ThreadDbObj tdbo = null;
		ResetDB(-1);
		while ((tdbo = (ThreadDbObj) this.GetNextObject()) != null)
		{
			tdbo.setMarker(state);
		}
	}

	private int getAnzTnames(String tname)
	{
		int count = 0;
		int maxzaehler = GetanzObj();

		// xxxxxxxxxx hier sollte eine abfrage von tname==null eingefügt werden

		for (int i = 0; i < maxzaehler; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(i);
			if (tdbo.getThreadname().equalsIgnoreCase(tname) == true)
				count++;
		}
		return count;
	}

	public String calcTname(String tname)
	{
		
		
		if (tname == null)
			Tracer.WriteTrace(10, "E:tname=null ");

		if(tname.contains("?")==true)
			Tracer.WriteTrace(20, "found ?");
		
		//Sonderzeichenbehandlung
		
		tname=tname.replace("?", "X");
		
		
		int n = this.getAnzTnames(tname);
		int count = 0;
		while (n > 0)
		{
			count = count + 1;
			n = this.getAnzTnames(tname + String.valueOf(count));

			if (n == 0)
			{
				// name noch nicht in db
				tname = tname + String.valueOf(count);
				return tname;
			}

			if (count > 10000)
			{
				// Ein Threadname darf max 10000 unternamen haben
				// Bsp. Eon1 Eon2..... Eon10000
				System.out.println("internal error djfdjdkfdjf");
				Tracer.WriteTrace(10, "E:Max 10000 Diskussionsthreads");
				return null;
			}
		}
		return tname;
	}

	public int GetThreadNameIDX(String name)
	{
		int tdblimit = GetanzObj();
		for (int i = 0; i < tdblimit; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(i);

			if (tdbo.getThreadname().equals(name) == true)
				return (i);
		}
		return -1;
	}

	public ThreadDbObj SearchThreadid(int id)
	{
		// id ist in der map, dann hole schnell
		if (threadidpos.containsKey(id) == true)
		{
			int pos = threadidpos.get(id);
			ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(pos);
			// plausi
			if (tdbo.getThreadid() != id)
				Tracer.WriteTrace(10, "Error: internal plausi pos<" + pos
						+ "> id<" + id + "> ");
			return tdbo;
		}

		// wenn nicht in der map dann gehe durch den ganzen thread und speichere
		int maxzaehler = GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(i);
			if (tdbo.getThreadid() == id)
			{
				// speichere die position in der map
				threadidpos.put(id, i);
				return tdbo;
			}
		}
		return null;
	}

	public int getMarkedThread(int minIDX, int searchstate)
	{
		int maxzaehler = GetanzObj();
		for (int i = minIDX; i < maxzaehler; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(i);
			if (tdbo.getMarker() == searchstate)
				return i;
		}
		return -99;
	}

	public void CleanBreakid()
	{
		int maxzaehler = GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(i);
			tdbo.setBreakid(0);
		}
	}

	public String GetUrlName(int index)
	{
		// index ist die postion des objektes in der liste
		// urlstr="http://www.wallstreet-online.de/community/thread/990693-"+i+".html";
		// status ==0 dann nimm alle akt ausser status == 3

		/*
		 * uname = "http://www.wallstreet-online.de/community/thread/" +
		 * threadid[aktzaehler] + "-" + index + ".html";
		 */
		ThreadDbObj tdbo = (ThreadDbObj) GetObjectIDX(index);
		String uname = tdbo.GetUrlName(index);
		return uname;

	}

	public String GetSavePageName_genDir(int index)
	{
		ThreadDbObj tdbo = (ThreadDbObj) GetAktObject();
		String fname = tdbo.GetSavePageName_genDir(index);
		return fname;
	}
	
	
	
	
	public int updateAllPrimepagesParallel(int threshold, int minprio,
			int maxprio, SlideruebersichtDB sldb, float zeitlimitMinuten,
			Inf inf, ProgressBar pb)
	{
		// prio: nur die threads mit der passenden prio werden upgedatet
		// return: pagemax, es wird der Wert zurückgeliefert wie weit man beim
		// Primepage maximal gegangen ist
		DownloadManager Wpage = new DownloadManager(10);
		int threadanz = GetanzObj();
		int pagemax = 0;
		String zwischenzeit = "";
		String startzeit = Tools.get_aktdatetime_str();
		int aktionen = 0;
		float zeitdiffminuten = 0;

		pb.setMinimum(0);
		pb.setMaximum((int) zeitlimitMinuten);

		ResetDB();
		for (int i = 0; i < threadanz; i++)
		{
			zwischenzeit = Tools.get_aktdatetime_str();
			zeitdiffminuten = Tools.zeitdifferenz_minuten(startzeit,
					zwischenzeit);
			pb.setSelection((int) zeitdiffminuten);
			if (zeitdiffminuten > zeitlimitMinuten)
			{
				// zeit überschritten
				inf
						.writezeile(Tools.get_aktdatetime_str()
								+ ";Zeitlimit überschritten (Update Primepages Step1 (download)) ; zeitlimit<"
								+ zeitlimitMinuten + ">; startzeit<"
								+ startzeit + ">; endzeit<" + zwischenzeit
								+ "> ; dauer<" + zeitdiffminuten
								+ "> ;anz update Primepages<" + aktionen
								+ "> pagemax<" + pagemax + ">");
				break;
			}

			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);

			int tprio = sldb.calcPrio(tdbo, GC.ALTERFLAG);
			if ((tprio > maxprio) || (tprio < minprio))
				continue;

			// pagemax zaehlt wie weit der Step 1 gekommen ist, für die weitere
			// Auswertung
			// braucht man nur noch bis zum pageindex gehen
			pagemax = i;

			tdbo.UpdatePrimePageStep1(Wpage, threshold);
			System.out.println("<" + i + "|" + threadanz
					+ ">Update Step1 Prio(" + minprio + "->" + maxprio
					+ ")(Primepage download) worktime<" + zeitdiffminuten + "|"
					+ zeitlimitMinuten + ">");
			aktionen++;
		}
		Wpage.waitEnd();
		ResetDB();

		
		for (int i = 0; i < pagemax; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);

			int tprio = sldb.calcPrio(tdbo, GC.ALTERFLAG);

			if ((tprio > maxprio) || (tprio < minprio))
				continue;

			tdbo.UpdatePrimePageStep2(middb_glob,this,false);
			if (i % 100 == 0)
				this.WriteDB();
			System.out.println("<" + i + "|" + pagemax + ">Update Step2 Prio("
					+ minprio + "->" + maxprio + ")(Primepage auswertung)");
		}
		ResetDB();
		// speichere die db, da neue Masterstrings hinzugekommen sein können
		this.WriteDB();
		return pagemax;
	}

	public void updateAllThreads(int threshold, int aktienthreadflag,
			int minprio, int maxprio, int zeitlimitMinuten,
			int firstflagreport, ProgressBar pb, UserDB udb,SlideruebersichtDB sldb)
	{
		// falls loadmode=0 dann nimm nur aktien mit state ==0 (default)
		// falls loadmode=1 dann lade immer
		// zeitlimit= es wird nur solange geladen wie das zeitlimit
		// vorgibt(zeitlimit in minuten)
		// firstflag=1, => wird eine ___ zeile ausgegeben für das reporting

		String zwischenzeit = "";
		float zeitdiffminuten = 0;

		
		String startzeit = Tools.get_aktdatetime_str();
		float zeitlimit_minuten = (float) zeitlimitMinuten;
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\zeitlimit.csv");
		if (firstflagreport == 1)
			inf
					.writezeile("________________________________________________________");

		
		int j;
		int gelThreads = 0;
		int threadmax=0;

		ResetDB();

		// läd die ersten Threadseiten alle neu, threshold stunden sind erlaubt

		if (GC.ONLINEMODE == 1)
		{
			threadmax = this.updateAllPrimepagesParallel(threshold,
					minprio, maxprio, sldb, zeitlimitMinuten, inf, pb);
			this.WriteDB();
		}
		else
			threadmax=this.GetanzObj();
		
		pb.setMaximum(0);
		pb.setMaximum(threadmax);
		startzeit = Tools.get_aktdatetime_str();
		// läd alle threads runter
		for (j = 0; j < threadmax; j++)
		{ // maximal tdblimit Threads

			pb.setSelection(j);
			zwischenzeit = Tools.get_aktdatetime_str();
			zeitdiffminuten = Tools.zeitdifferenz_minuten(startzeit,
					zwischenzeit);
			if (zeitdiffminuten > zeitlimit_minuten)
			{
				// zeit überschritten
				inf.writezeile(Tools.get_aktdatetime_str()
						+ ";Zeitlimit (lade threads)überschritten;threshold<"
						+ threshold + ">; minprio<" + minprio + ">; maxprio<"
						+ maxprio + ">; limitMin<" + zeitlimit_minuten
						+ ">; startz<" + startzeit + ">; endz<" + zwischenzeit
						+ "> ; dauer<" + zeitdiffminuten + "> ; gel.Threads<"
						+ gelThreads + ">");

				return;
			}

			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(j);
			if (tdbo == null)
				break;

			if(tdbo.getState()!=0)
			{
				
				Tracer.WriteTrace(20, "Info: Thread <"+tdbo.getThreadname()+"> <"+tdbo.getEroeffnet()+">has state <"+tdbo.getState()+"> => bad thread, go on 454");
				j++;
				continue;
			}
			
			int tprio = sldb.calcPrio(tdbo, GC.ALTERFLAG);

			if ((tprio > maxprio) || (tprio < minprio))
				continue;

			if (aktienthreadflag == 1)
				if (tdbo.isValidAktienthread(0) == false)
					continue;

			if(GC.ONLINEMODE==1)
				tdbo.UpdateThread(middb_glob,udb, aktienthreadflag, threshold, this);
			gelThreads++;
			System.out.println("<" + j + "|" + threadmax
					+ ">Update Thread Akt-worktime prio(" + minprio + "->"
					+ maxprio + ")Minuten<" + zeitdiffminuten + "|"
					+ zeitlimit_minuten + ">");
		}
		// alles fertiggeladen
		inf.writezeile(Tools.get_aktdatetime_str() + ";Fertig; threshold<"
				+ threshold + ">; prio<" + minprio + "->" + maxprio
				+ ">; limitMin<" + zeitlimit_minuten + ">; end<" + zwischenzeit
				+ "> ; dauer<" + zeitdiffminuten + "> ; gel.Threads<"
				+ gelThreads + ">");
		this.WriteDB();
	}

	public void BuildOfflineThreadDB()
	{
		// die ThreadsDB wird normalerweise von Hand generiert
		// Falls die ThreadsDB kaputt ist kann diese aus den bereits
		// geladenen Verzeichnissen regeneriert werden.
		// Es wird in die Verzeichnisse von ..\offline\threads und
		// die threadsdb aufgebaut(bzw. erweitert)
		// ebenfalls wird überprüft ob ein thread schon geladen wurde
		// und der entsprechende Status wird gesetzt geladen =2

		String verz = null;
		int threadid = 0;
		FileAccess.initFileSystemList(GC.rootpath + "\\Threads", 0);

		while ((verz = FileAccess.holeFileSystemName()) != null)
		{// überprüft ob der verzeichnissname schon in der threadsdb

			if (getAnzTnames(verz) < 0)
			{// threadname ist nur als verzeichniss vorhanden und nicht in
				// der db
				// hole die threadid

				if ((threadid = HoleThreadIDausVerzeichniss(verz)) > 0)
				{
					// threadname, id, date, status, masterid, symbol, pageanz,
					// url, imarker, breakid,
					// observerflag, lastloaded
					ThreadDbObj tdbo = new ThreadDbObj(verz, threadid, "x", 0,
							0, "0", 0, "0", 0, 0, 0, "0", null);
					AddObject(tdbo);
				}
			}
		}
	}

	public boolean BuildCompressedThreadsPostings()
	{
		// holt die postings aus downloaded/threads und baut davon
		// die compressed postings auf

		int j;

		ResetDB();
		int tdblimit = GetanzObj();

		// gehe durch die Threads
		for (j = 0; j < tdblimit; j++)
		{ // maximal tdblimit Threads

			ThreadDbObj tdbo = (ThreadDbObj) this.GetAktObject();
			System.out.println("Betrachte Thread" + tdbo.getThreadname());
			tdbo.CompressThread();

			if (SetNextObject() == false)
				break;
		}
		System.out.println("fertig");

		return true;
	}

	public void analyseGetAllUsernamesFromThreadsDB(int maxpage)
	{
		PostDBx PoDB = new PostDBx();
		UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
		PoDB.BuildPostDBalleThreads(this, udb, "x");
	}

	public int HoleThreadIDausVerzeichniss(String threadname)
	{
		int threadid = 0;
		ResetDB();

		char pref = threadname.charAt(0);

		String filename = FileAccess.convsonderz(GC.rootpath
				+ "\\downloaded\\threads\\" + threadname + "\\@" + pref + "\\"
				+ threadname + "_1.html");

		Threads html = new Threads(middb_glob,this,filename, 0);

		threadid = html.GetThreadId();
		if (threadid > 0)
			return (threadid);
		else
			return 0; // keine HTML-Seite gefunden
	}

	public void GetAllPrimeDataFromFristThreadPage(int forceflag,
			int thresholdhours, int aktienthreadflag)
	// falls forceflag ==1 dann wird die erste seite auf jeden fall geladen
	// wird benötigt falls die seiten veraltet sind
	// falls aktientrheadflag==1, werden nur aktienthreads mit gültigen kursen
	// upgedatet
	{
		// holt a)masterid
		// b)symbol
		// c)anz Seiten
		// aus der ersten Seite eines Threads
		DownloadManager Wpage = new DownloadManager(GC.MAXLOW);
		int i, j, Masterid = 0, Seitenanz = 0;

		int aktThreadanz = GetanzObj();
		String fnam = null;
		String lastnam; // dies ist der name der letzten Page
		ResetDB();

		if (forceflag == 1)
			setAllState(0);

		// holt aus allen Threads die Masterid und das Symbol
		for (j = 0; j < aktThreadanz; j++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(j);
			fnam = tdbo.GetSavePageName_genDir(1);
			String symb = tdbo.getSymbol();

			if (aktienthreadflag == 1)
				if (tdbo.isValidAktienthread(0) == false)
					continue;

			// die primepage die die Seitenanzahl beinhaltet wird upgedatet
			tdbo.UpdatePrimePage_dep(middb_glob,thresholdhours, this);

			// Prüfe ob alles geladen wurde, wenn ja => state 2
			lastnam = tdbo.GetSavePageName_genDir(tdbo.getPageanz());
			if (FileAccess.FileAvailable(lastnam) == true)
				setAktState(2);

			System.out.println("(" + j + "|" + aktThreadanz + ")Masterid aus <"
					+ fnam + "> id=<" + Masterid + "> Seitenanz <" + Seitenanz
					+ "> Symb <" + tdbo.getSymbol() + "> urln<"
					+ tdbo.getUrlaktname() + ">");

			if (j % 1000 == 0)
				this.WriteDB();
		}

		WriteDB();
	}

	public void BaueThreadRankingListe(UserDB udb)
	{
		int ttage = 0;

		// Rankingliste die ganz neue Threads beinhaltet, die Liste ist max 3
		// Monate alt
		RangObj rankl_3monate = new RangObj();
		// diese Rankingliste ist schon alt und hat nur wenig postings
		RangObj rankl_wenigpost = new RangObj();
		// Rankingliste mit normalen Postverhalten
		RangObj rankl = new RangObj();

		// wie oft die einzelnen User gepostet haben das findet man in der
		// PostDBx
		// db/threaddata....
		ResetDB();
		int tdblimit = GetanzObj();
		for (int j = 0; j < tdblimit; j++)
		{
			System.out.println("i<" + j + ">");
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(j);
			if (tdbo.checkPostdbAvailable() == true)
			{
				PostDBx pdb = new PostDBx();
				float erf = pdb.calcDurchschnittAktienerfahrungTage(tdbo, udb);
				int anzuser = pdb.calcAnzUser(tdbo, udb);
				int anzpostings = pdb.calcAnzPostings(tdbo, udb, GC.PDBALLPOST,
						null);
				int anzvalidpostings = pdb.calcAnzValidPostings(tdbo, udb);

				ThreadStatObj tstat = new ThreadStatObj();
				tstat.setAnzuser(anzuser);
				tstat.setAnzpostings(anzpostings);
				tstat.setAnzvalidpostings(anzvalidpostings);
				tstat.setErf(erf);
				tstat.setTdbo(tdbo);

				// holt sich das Startdatum
				String fnam = tdbo.getThreadDownloadSpeichername(1);
				Threads html = new Threads(middb_glob,this,fnam + ".gzip", tdbo.getThreadid());
				String startdatum = html.GetEroeffnetAm();
				if (startdatum.equals("0") == true)
				{
					if (tdbo.UpdatePrimePage_dep(middb_glob,0, this) == false)
						continue;
					html = new Threads(middb_glob,this,fnam + ".gzip", tdbo.getThreadid());
					startdatum = html.GetEroeffnetAm();
				}

				// prüft ob der Thread älter als 90 Tage ist
				ttage = Tools.zeitdifferenz_tage(Tools.get_aktdatetime_str(),
						startdatum);
				tstat.setTtage(ttage);
				// pro Jahr sollten 100 postings rumkommen
				// 100/360=
				float postfaktor = anzpostings / ttage;

				if (ttage < 90)
				{
					// Thread ist juenger als 90 Tage
					rankl_3monate.add(tstat);
				} else if (postfaktor < 0.277)
				{
					rankl_wenigpost.add(tstat);
				} else
					rankl.add(tstat);
			}
		}

		rankl.sort();
		rankl.writeliste(GC.rootpath + "\\db\\reporting\\threadrank_rankl.txt");
		rankl.writeRankingpointsToThreaddb();

		rankl_wenigpost.sort();
		rankl_wenigpost.writeliste(GC.rootpath
				+ "\\db\\reporting\\threadrank_wenigpost.txt");
		rankl_wenigpost.writeRankingpointsToThreaddb();

		rankl_3monate.sort();
		rankl_3monate.writeliste(GC.rootpath
				+ "\\db\\reporting\\threadrank_3monate.txt");
		rankl_3monate.writeRankingpointsToThreaddb();

		// schreibe die tdb neu da die rankingpunkte verändert wurden
		this.WriteDB();
	}

	public void BesucheAlleThreadseiten()
	{
		int i, j, fehlzaehler = 0;
		String fnam = null;

		ResetDB();
		int tdblimit = GetanzObj();

		// gehe durch die Threads
		for (j = 0; j < tdblimit; j++)
		{ // maximal tdblimit Threads
			for (fehlzaehler = 0, i = 1; i < 120000; i++)// 120000 Webseiten
			// kann
			// ein thread haben
			{
				ThreadDbObj tdbo = (ThreadDbObj) GetAktObject();
				fnam = tdbo.GetSavePageName_genDir(i);
				if (fnam.contains("null"))
					break;

				// Aktion zippe Webseite
				System.out.println("betrachte <" + fnam + ">");

				if (FileAccess.FileAvailable(fnam) == true)// prüft ob die
				// sequence
				// vorhanden
				{
					/* falls *.html */
					if (FileAccess.FileAvailable(fnam) == true)
					{
						// dann zippe
						if (ar.gzipFile(fnam) == true)
						{
							System.out.println("File gezipped<" + fnam + ">");
							FileAccess.FileDelete(fnam, 0);
						}
					}
					// falls *.html.gzip.gzip
					if (FileAccess.FileAvailable(fnam + ".gzip.gzip") == true)
					{
						// dann entzippe
						if (ar.gzipUnzipFile(fnam + ".gzip.gzip") == true)
						{
							System.out.println("File unzipped<" + fnam + ">");
							FileAccess.FileDelete(fnam + ".gzip.gzip", 0);
						}

					}
				} else
					fehlzaehler++;

				// if (fehlzaehler>=100)
				// break;
			}

			if (SetNextObject() == false)
				break;
		}
		System.out.println("fertig");
	}

	public void LadeAlleKurse(AktDB aktdb, int threshold, boolean forceflag,
			boolean schnellflag, ProgressBar pb, Display dis)
	{
		// schnellflag==0, dann suche auch nach neuen Symbolen in aktdb und tdb
		// schnellflag==1, dann lade ganz schnell die Kurse
		KurseDB kurse = new KurseDB(this);
		kurse.ladeAlleKurseUpdateKurseDB(aktdb, this, threshold, forceflag,
				schnellflag, pb, dis);
	}

	public void CompressCheckAllHtmlFiles(int startthread)
	{
		// geht durch alle Threads und wandelt alle html-seiten in ein
		// kompressed format um.
		// legt gleichzeitig compressedthreads an

		UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");

		this.ResetDB();
		int tanz = this.GetanzObj();
		this.SetAktIDX(0);

		for (int i = startthread; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			int pageanz = tdbo.getPageanz();

			if (pageanz > 60000)
			{
				Tracer.WriteTrace(20, "I:Pageanz zu gross anz=<" + pageanz
						+ "> gehe weiter");
				this.SetNextObject();
				continue;
			}

			tdbo.updateCompressedThread(middb_glob,i, tanz, this, udb, 0);

			// gehe einen thread weiter
			System.out.println("gehe einen thread weiter");
			this.SetNextObject();
		}
	}

	public String sucheEinenThreadnamen(String symb)
	{
		// suche für das Symbol einen Threadnamen
		int tanz = this.GetanzObj();
		for (int i = 0; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
			{
				return tdbo.getThreadname();
			}
		}
		Tracer.WriteTrace(20, "E: Für das Symbol <" + symb
				+ "> konnte kein Threadname in threads.db ermittelt werden");
		return new String("unbekannt");
	}

	public ThreadDbObj sucheThread(String tnam)
	{
		// suche für das Symbol einen Threadnamen
		int tanz = this.GetanzObj();
		for (int i = 0; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getThreadname().equalsIgnoreCase(tnam))
			{
				return tdbo;
			}
		}
		Tracer.WriteTrace(10, "E: Es konnte kein Thread mit namen <" + tnam
				+ "> in threads.db ermittelt werden");
		return null;
	}

	public int sucheThreadid(String symb)
	{
		// suche für das Symbol einen Threadnamen
		int tanz = this.GetanzObj();
		for (int i = 0; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
			{
				return tdbo.getThreadid();
			}
		}
		Tracer.WriteTrace(20, "E: Für das Symbol <" + symb
				+ "> konnte keine threadid in threads.db ermittelt werden");
		return 0;
	}

	public ThreadDbObj sucheThreadObj(String symb)
	{
		// suche für das Symbol einen Threadnamen
		int tanz = this.GetanzObj();
		for (int i = 0; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
			{
				return tdbo;
			}
		}
		Tracer.WriteTrace(20, "E: Für das Symbol <" + symb
				+ "> konnte keine threadid in threads.db ermittelt werden");
		return null;
	}

	public void setSortkriterium_steigungsfaktor(String symb, float f)
	{
		// setzt für alle tdbo´s mit passenden symbol den steigungsfaktor f
		// der Steigungsfaktor gibt an wie der kurs in letzter Zeit gestiegen
		// ist
		// der Steigungsfaktor wird als Sortierkriterium verwendet
		int tanz = this.GetanzObj();
		for (int i = 0; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
			{
				tdbo.setSortkriterium_steigungsfaktor(f);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void SortSliderWriteDB()
	{
		// hier wird die ganze dbliste sortiert
		// Die threadliste wird so sortiert das beim nächsten Male
		// die Slider drankommen die schon länger nicht bearbeitet wurden
		ThreadsSliderComparator c = new ThreadsSliderComparator();
		Collections.sort(dbliste, c);

		// lösche die threadmap, da ja alles wieder neu sortiert wurde
		threadidpos.clear();

		// dann schreiben
		this.WriteDB();
	}

	@SuppressWarnings("unchecked")
	public void sortSteigungsfaktorWriteDB()
	{
		ThreadSteigungsfaktorComparator c = new ThreadSteigungsfaktorComparator();
		Collections.sort(dbliste, c);

		// lösche die threadmap, da ja alles wieder neu sortiert wurde
		threadidpos.clear();

		this.WriteDB();
	}

	public int holeMasterid(int tid)
	{

		ThreadDbObj tdbo = (ThreadDbObj) this.GetObject(tid);
		if (tdbo == null)
			return 0;
		else
			return tdbo.getMasterid();

	}

	public int holeTid(String symb)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
				return tdbo.getThreadid();
		}
		return 0;
	}

	public String holeMasterstring(String symb)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
				return tdbo.getMasterstring();
		}
		return null;
	}

	public int holeMasterId(String symb)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(symb))
				return tdbo.getMasterid();
		}
		return 0;
	}

	public ArrayList<Integer> calcTidArray(int mid)
	{
		ArrayList<Integer> tidliste = new ArrayList<Integer>();

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getMasterid() == mid)
			{
				tidliste.add(tdbo.getThreadid());
			}
		}
		return tidliste;
	}

	public ArrayList<Integer> calcTidArrayMaxDay(int mid, int maxday)
	{
		ArrayList<Integer> tidliste = new ArrayList<Integer>();

		String adate = Tools.entferneZeit(Tools.get_aktdatetime_str());

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getMasterid() == mid)
			{

				String lastpost = tdbo.getLastThreadPosttime();
				if (Tools.zeitdifferenz_tage(lastpost, adate) > maxday)
					continue;

				tidliste.add(tdbo.getThreadid());
			}
		}
		return tidliste;
	}

	public int calcBoersenId(String symb)
	{
		ThreadDbObj tdbo = null;
		String boerplatz = null;
		int i = 0;
		final Integer[] VAL =
		{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 18, 21, 22, 31, 33, 39,
				45, 47 };
		final String[] BOER =
		{ "Xetra (EUR)", "Frankfurt", "Berlin", "Düsseldorf (EUR)",
				"Hamburg (EUR)", "München", "Hannover (EUR)", "Stuttgart",
				"NASDAQ (USD)", "NYSE", "AMEX (USD)", "OTC",
				"DJ Indizes (USD)", "Euronext Paris", "TradeGate",
				"Lang & Schwarz", "Toronto (CAD)", "TSX Venture",
				"Euronext (EUR)", "SIX SWX (CHF)", "ICE Fut Eur (USD)" };

		if (VAL.length != BOER.length)
			Tracer.WriteTrace(10, "Error: internal val!= boernamelength");

		// maid ==0, dann gibt es keine börsenid
		// maid>0, dann wurde eine maid gefunden
		// aus einem symbol wird ein borsenplatz berechnet==mid==market id

		// market id
		// value="1" >Xetra,
		// value="2" >Frankfurt,
		// value="3" >Berlin,
		// value="4" >Düsseldorf (EUR)
		// value="5" Hamburg (EUR)
		// value="6" >München,
		// value="7" Hannover (EUR)
		// value="8" >Stuttgart
		// value="9" selected>NASDAQ
		// value="10" >NYSE</option><option
		// value="12" >OTC,value="21" >TradeGate,value="33" >TSX Venture
		// value="13" DJ Indizes (USD)
		// value="18" >Euronext Paris
		// value="21" >TradeGate</option><option
		// value="22" >Lang & Schwarz<
		// value="33" >TSX Venture
		// value="39" selected>Euronext
		// value="47" ICE Fut Eur (USD)

		// Suchen
		int maid = 0;

		int anz = this.GetanzObj();
		for (i = 0; i < anz; i++)
		{
			tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getSymbol().equalsIgnoreCase(symb))
			{
				if ((tdbo.getWoBoerplatz() != null)
						&& (tdbo.getWoBoerplatz().equals("0") == false)
						&& (tdbo.getWoBoerplatz().equals("null") == false))
				{
					// gefunden
					break;
				}
			} else
				tdbo = null;
		}

		// Auswertung
		if (tdbo == null)
		{
			// Nix gefunden
			Tracer.WriteTrace(20, "Warning: noch kein Börsenplatz für symbol <"
					+ symb + "> in threads.db gefunden  i<" + i + ">");
			return 0;
		} else
		{
			// börsenplatz ist gesetzt
			// dann suche die maid

			anz = BOER.length;
			boerplatz = tdbo.getWoBoerplatz();
			maid = 0;
			for (i = 0; i < anz; i++)
			{
				if ((boerplatz.contains(BOER[i]))
						|| (BOER[i].contains(boerplatz)))
				{
					maid = VAL[i];
					return maid;
				}
			}

			// Börsenplatz nicht gefunden !!!
			if (maid == 0)
				Tracer.WriteTrace(20, "Warning:unbekannter Börsenplatz <"
						+ boerplatz + "> tid<" + tdbo.getThreadid() + "> ");
			return maid;
		}
	}

	public void refreshPrognoseanzahl()
	{
		// hier wird die Prognoseanzahl refreshed d.h.
		// Aus sämtlichen Attributen wird gezählt wieviele male "Kaufe zu"
		// vorkommt
		// und dieser Wert wird für die einzelnen Aktien in der Threadsdb
		// vermerkt
		// Man kann also jetzt zu jedem Thread ablesen, wieviele Prognosen für
		// diesen
		// generiert wurden

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);

			ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tdbo
					.getThreadid(), "\\db\\Attribute\\Threads");
			int proganzahl = attribstore.calcPrognoseanzahl();
			tdbo.setPrognoseanzahl(proganzahl);
			Tracer.WriteTrace(20, "RefreshProganzahl i<" + i + "> tid<"
					+ tdbo.getThreadid() + "> tnam<" + tdbo.getThreadname()
					+ "> proganzahl<" + proganzahl + ">");
		}
		this.WriteDB();
	}

	public float holeAttrib(String datum, int attribid, int tid)
	{
		ThreadDbObj tdbo = (ThreadDbObj) this.GetObject(tid);
		ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tdbo
				.getThreadid(), "\\db\\Attribute\\Threads");
		float val = attribstore.getAttrib(datum, attribid);
		return val;
	}

	public void setPusherflag(int mid)
	{
		// für eine mid wird das pusherflag gesetzt
		// das Puscherflag gibt an das die aktie gepusched ist
		// im threadsinfo steht hierzu mehr
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);

			if (tdbo.getMasterid() == mid)
			{
				tdbo.setPuscherflag(1);
				Tracer.WriteTrace(20, "Info: für mid<" + mid
						+ "> wird Puscherflag bei <" + tdbo.getThreadname()
						+ "> gesetzt");
			}
		}
	}

	public void setReboundflag_dep(KurseDB kdb, ThreadDbObj tdbo, int mid,
			int reboundflag, float min, float max)
	{
		// funktion ist veraltet, das wird alles mit dem observermodul gemacht
		// !!!

		// hier werden die reboundwerte für einen mid gesetzt
		// beim reboundcheck möchte man wissen ob für eine mid bestimmte
		// kurswerte eingehalten wurden
		// bei überschreiten werden entsprechende Ereignisse ausgelöst
		// Die Reboundwerte werden einmal direkt in der kursdb gesetzt und das
		// entsprechende
		// reboundflag in der tdb gesetzt
		ArrayList<Integer> m = this.calcTidArray(mid);

		// plausi threadsdb: schaut nach ob für die mid überhaupt threadid´s
		// existieren
		if (m.isEmpty())
			Tracer.WriteTrace(10, "Error: für die mid<" + mid
					+ "> existieren keine tids in threads.db");

		String symb = tdbo.getSymbol();
		if (symb == null)
			Tracer.WriteTrace(10, "symb=null errror");

		KursDbObj kdbo = (KursDbObj) kdb.holeKursobj(tdbo.getSymbol());
		if (kdbo == null)
			Tracer.WriteTrace(10, "kdbo=null error");

		// kdbo.setReboundmin(min);
		// kdbo.setReboundmax(max);

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdboind = (ThreadDbObj) this.GetObjectIDX(i);

			if (tdboind.getMasterid() == mid)
			{
				// tdboind.setReboundflag(1);
				Tracer.WriteTrace(20, "Info: für mid<" + mid + "> tid<"
						+ tdboind.getThreadid() + "> > wird Reboundflag bei <"
						+ tdboind.getThreadname() + "> gesetzt");
			}
		}
		this.WriteDB();
		kdb.WriteDB();
	}

	private void genTidFileTmp(String zielfile, ThreadDbObj tdbo)
	{

		String nam = tdbo.getThreadname();
		String quell = GC.rootpath + "\\db\\threaddata\\" + nam + "_"
				+ tdbo.getThreadid() + ".db";
		String q1 = zielfile;
		String k1 = GC.rootpath + "\\tmp\\k1.txt";
		FileAccess.FileDelete(k1, 0);

		// kopiere das orginal ins tmp
		FileAccess.copyFile2_dep(quell, q1);

		// erstellt den Kopf für das erste file
		Inf inf = new Inf();
		inf.setFilename(k1);
		inf
				.writezeile("Name<"
						+ nam
						+ "> tid<"
						+ tdbo.getThreadid()
						+ "> mid<"
						+ tdbo.getMasterid()
						+ "> lastpost<"
						+ tdbo.getLastThreadPosttime()
						+ ">______________________________________________________________________________");
		inf.close();

		// hängt die Info dran
		FileAccess.ConcatFiles(k1, q1);
	}

	public void ZeigePostinglisten(int mid)
	{

		// hier werden die postinglisten für einen bestimmten Thread angezeigt.
		// Es werden alle tid´s gesammelt und gemeinsamm dargestellt

		// berücksichtigt nur Threads wo das letzte Posting max 50 Tage alt ist.
		// Das uralte zeug wird somit nicht berücksichtigt
		ArrayList<Integer> al = null;
		int suchtage = 50;
		int anz = 0;

		while (5 == 5)
		{
			al = this.calcTidArrayMaxDay(mid, suchtage);
			anz = al.size();

			// nix gefunden
			if (anz == 0)
			{
				suchtage = suchtage + 50;
				continue;
			}
			// gefunden
			else
			{
				// im normalen
				if (suchtage == 50)
					break;
				if (suchtage > 50)
				{
					// gefunden aber suchtage >50
					Tracer.WriteTrace(10, "Suchtage <" + suchtage
							+ "> >50, threads sehr alt");
					break;
				}

			}
		}

		// für die erste tid das datenfile genieriern
		int tid = al.get(0);
		String fnam1 = GC.rootpath + "\\tmp\\q0.txt";
		FileAccess.FileDelete(fnam1, 0);

		ThreadDbObj tdbo = this.SearchThreadid(tid);
		genTidFileTmp(fnam1, tdbo);

		for (int i = 1; i < anz; i++)
		{
			// für die zweite tid das Datenfile genieren
			tid = al.get(i);
			String fnam2 = GC.rootpath + "\\tmp\\q" + i + ".txt";
			FileAccess.FileDelete(fnam2, 0);
			tdbo = this.SearchThreadid(tid);
			genTidFileTmp(fnam2, tdbo);

			FileAccess.ConcatFiles(fnam1, fnam2);

			// loesche fnam1
			FileAccess.FileDelete(fnam1, 0);
			fnam1 = fnam2;
		}

		// fnam1 ist jetzt das Resultatfile und wird angezeigt
		try
		{
			Runtime.getRuntime().exec("notepad " + fnam1);
		} catch (Exception e)
		{
			System.err.println(e.toString());
		}
	}

	private void genThreadPostingliste(String outfile, PostDBx pdb,
			ThreadDbObj tdbo, UserDB udb)
	{
		// hier wird die Info für einen Thread aufgebaut
		int rang = 0;

		Inf inf = new Inf();
		inf.setFilename(outfile);
		inf
				.writezeile("--------------------------------------------------------------------------------------");
		inf.writezeile("tid<" + tdbo.getThreadid() + "> mid<"
				+ tdbo.getMasterid() + "> tname<" + tdbo.getThreadname() + ">");
		inf.writezeile("Eröffnet<" + tdbo.getEroeffnet() + "> Lastposttime<"
				+ Tools.entferneZeit(tdbo.getLastThreadPosttime())
				+ "> LastDownload<" + tdbo.getLastdownloadtime() + ">");
		inf.writezeile("Erfahrung in Jahre<"
				+ (pdb.calcDurchschnittAktienerfahrungTage(tdbo, udb)) / 365
				+ ">");

		// gehe durch die User
		int uanz = pdb.calcAnzUser(tdbo, udb);
		for (int i = 0; i < uanz; i++)
		{
			String username = pdb.getUnam(i);
			UserDbObj udbo = (UserDbObj) udb.getUserobj(username);

			if (udbo == null)
				rang = -99;
			else
				rang = udbo.getRang();

			int panz = pdb.getPostanz(username);
			inf.writezeile(i + ":unam<" + username + "> rang<" + rang
					+ "> tpostanz<" + panz + ">");
		}
		inf.close();
	}

	public void zeigePostinguser(int mid, UserDB udb, int maxdays)
	{
		// maxalter>0: dann werden nur threads berücksichtigt wo das letzte
		// Posting maximal
		// n tage alt ist
		String outfile = GC.rootpath + "\\tmp\\outpost.txt";
		FileAccess.FileDelete(outfile, 0);

		String adate = Tools.entferneZeit(Tools.get_aktdatetime_str());

		// pdb einlesen
		PostDBx pdb = new PostDBx();

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);

			if (tdbo.getMasterid() == mid)
			{
				// altes zeug rausfiltern
				String lastpost = tdbo.getLastThreadPosttime();
				if (Tools.zeitdifferenz_tage(lastpost, adate) > maxdays)
					continue;

				String filepostdbnam = GC.rootpath + "\\db\\threaddata\\"
						+ tdbo.getThreadname() + "_" + tdbo.getThreadid()
						+ ".db";
				pdb.ReadPostDB(filepostdbnam);
				genThreadPostingliste(outfile, pdb, tdbo, udb);
				pdb.CleanPostDB();
			}
		}

		try
		{
			Runtime.getRuntime().exec("notepad " + outfile);
		} catch (Exception e)
		{
			System.err.println(e.toString());
		}
	}

	public void postprocess()
	{
	}

	public void setBBlastPusch(int mid, String date)
	{
		// für eine bestimmte mid
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			if (tdbo.getMasterid() == mid)
				tdbo.setBBlastPuschdate(date);
		}
	}
	public String calcLastPosting()
	{
		// suche das letzte Posting über alle Threads
		
		String mindat=GC.startdatum;
	
		int tanz = this.GetanzObj();
		for (int i = 0; i < tanz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) this.GetObjectIDX(i);
			String lastpost=tdbo.getLastThreadPosttime();
			
			if(Tools.datum_ist_aelter_gleich(mindat, lastpost))
				mindat=lastpost;
		}
		return mindat;
	}

}
