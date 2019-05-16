package objects;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.IsValid;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import html.DB;
import html.Htmlcompress;
import html.Threads;
import html.ThreadsPostElem;
import interfaces.DBObject;
import internetPackage.DownloadManager;

import java.io.File;

import kurse.KursValueDB;
import kurse.Kursvalue;
import mainPackage.GC;
import stores.MidDB;
import stores.PostDBx;
import stores.SeitenzaehlerDBx;
import stores.ThreadsDB;
import stores.UserDB;

import compressor.CompressedThreadDBx;

import db.CompressedPostingObj;
import except.CompressedThreadException;

public class ThreadDbObj extends Obj implements DBObject
{
	// dieses Objekt repräsentiert einen Thread, sämtliche Infos die für
	// diesen Thread relevant sind werden hier gespeichert

	private static int onlinemode_glob = -99;
	private String anames = null;
	//startdatum=datum wo das erste posting abgesetzt wurde
	private String startdatum = null;
	private int threadid = 0;
	private int masterid = 0;
	private int state = 0;
	//lastpagevorh= ist die letzte page mit nummer die in dem thread ist
	private int lastpagevorh = 0;
	private String urlaktname = null;
	private String symbol = null;
	private int imarker = 0;
	private int breakid = 0;
	private String eroeffnet = null;

	private static int trennercounter = 0;

	// dies ist der Rankingstring für den Thread
	private String threadrankingstring = null;
	// mode ist für den Objektdurchlauf wichtig, falls mode 'all' werden alle
	// Objekte durchlaufen
	private int mode = 0;

	//lastthreadposttime=datum des letzten postings in dem thread
	private String lastthreadposttime = null;
	//lastthreaddownloadtime=datum wann der thread das letzte mal
	//aktualisiert wurde
	private String lastdownloadtime = null;

	// hier wird vermerkt wann der benutzer den Thread zuletzt gelesen hat
	private String lastreadtime = null;
	static DownloadManager Wpage = new DownloadManager(GC.MAXLOW);

	// für next posting einige variablen
	private int lastpage = 0;
	private int lastpostid = 0;
	private int symbolflag = 0;
	// lastsliderupdate_sort wird nur für sortierzwecke und abfragezwecke
	// benutzt
	private String lastsliderupdate_SORT = null;
	private String errorcode = "";

	// dies ist der steigungsfaktor der als Sortierkriterium genutzt werdern
	// kann
	// der Steigungsfaktor gibt an wie stark der kurs(für das Symbol) in letzter
	// Zeit gestiegen ist.
	private float steigungsfaktor = 0;
	private String masterstring = null;
	private String woboerplatz = null;
	private int prognoseanzahl = 0;
	private int puscherflag = 0;

	//gibt an wann das letzte Mal gepusched wurde
	private String BBlastPuschdate = null;

	// private int reboundflag = 0;

	public ThreadDbObj()
	{
	}

	private void checkAnames(String anames, String errormsg)
	{
		if ((anames == null) || (anames.length() == 0)
				|| (anames.equals("null")))
		{
			if (errormsg != null)
				Tracer.WriteTrace(10,
						"Error:internal anames not allowed error<" + errormsg
								+ ">");
		}
	}

	public ThreadDbObj(String a, int id, String dat, int st, int mid,
			String sym, int panz, String url, int im, int bi, int mo,
			String ll, String masterstr)
	{
		// threadname, id, date, status, masterid, symbol, pageanz, url,
		// imarker, breakid,
		// observerflag, lastloaded
		anames = new String(a);
		anames = SG.convFilename(anames);

		checkAnames(anames, null);

		startdatum = new String(dat);
		threadid = id;
		masterid = mid;
		state = st;
		lastpagevorh = panz;
		urlaktname = new String(url);
		symbol = new String(sym);
		imarker = im;
		breakid = bi;
		mode = mo;
		lastthreadposttime = new String(ll);
		masterstring = new String(masterstr);
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}
	public ThreadDbObj(String zeile)
	{
		int anz = Tools.countZeichen(zeile, "#");

		if (trennercounter == 0)
			trennercounter = anz;
		else
		{
			if (anz != trennercounter)
				Tracer.WriteTrace(10, "Zeilenaufbaufehler trennerist<" + anz
						+ "> trenner erw<" + trennercounter + "> zeile<"
						+ zeile + ">");
		}
		if ((anz < 11) || (anz > 24))
		{
			System.out.println(ThreadDbObj.class.getName()
					+ ":ERROR:threadname fehlerhaft in threads.db zeile=<"
					+ zeile + "> anz<" + anz + ">");
			Tracer.WriteTrace(10, ThreadDbObj.class.getName()
					+ ":ERROR:threadname fehlerhaft in threads.db zeile=<"
					+ zeile + ">");
			return;
		}
		int pos = 1;
		try
		{
			// Name, threadid, date, state, masterid, symbol, pageanz, url,
			// internmarker,breakid,observerflag,lastloaded

			if (zeile.contains("H5B5 MEDIA AG O.N1#492706#23.1"))
				Tracer.WriteTrace(20, "found Beru");

			anames = new String(Tools.nteilstring(zeile, "#", pos)
					.toUpperCase());
			anames = SG.convFilename(anames);
			checkAnames(anames, zeile);
			pos = pos + 1;
			threadid = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			startdatum = new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			state = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			masterid = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			symbol = new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			lastpagevorh = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			urlaktname = new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			imarker = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			breakid = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			mode = Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			lastthreadposttime = new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;

			if (anz >= 12)
			{
				threadrankingstring = new String(Tools.nteilstring(zeile, "#",
						pos));
				pos = pos + 1;
			}
			if (anz >= 13)
			{
				eroeffnet = new String(Tools.nteilstring(zeile, "#", pos));
				pos = pos + 1;
			}
			if (anz >= 14)
			{
				lastdownloadtime = new String(Tools
						.nteilstring(zeile, "#", pos));
				pos = pos + 1;
			}
			if (anz >= 15)
			{
				lastsliderupdate_SORT = new String(Tools.nteilstring(zeile,
						"#", pos));
				pos = pos + 1;
			}
			if (anz >= 16)
			{
				errorcode = new String(Tools.nteilstring(zeile, "#", pos));
				pos = pos + 1;
			}
			if (anz >= 17)
			{
				steigungsfaktor = Float.valueOf(Tools.nteilstring(zeile, "#",
						pos));
				pos++;
			}
			if (anz >= 18)
			{
				masterstring = new String(Tools.nteilstring(zeile, "#", pos));
				pos++;
			}
			if (anz >= 19)
			{
				woboerplatz = new String(Tools.nteilstring(zeile, "#", pos));
				pos++;
			}
			if (anz >= 20)
			{
				prognoseanzahl = Integer.valueOf(Tools.nteilstring(zeile, "#",
						pos));
				pos++;
			}
			if (anz >= 21)
			{
				lastreadtime = new String(Tools.nteilstring(zeile, "#", pos));
				pos++;
			}
			if (anz >= 22)
			{
				puscherflag = Integer.valueOf(Tools
						.nteilstring(zeile, "#", pos));
				pos++;
			}
			if (anz >= 23)
			{
				BBlastPuschdate = new String(Tools.nteilstring(zeile, "#", pos));
				pos++;
			}
		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, ThreadDbObj.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int is_onlinemode()
	{
		// falls mode noch nicht gesetzt dann setze
		if (onlinemode_glob < 0)
		{
			if (GC.ONLINEMODE == 1)
				onlinemode_glob = 1;
			else
				onlinemode_glob = 0;
		}
		return onlinemode_glob;
	}

	public String toString()
	{
		return (anames + "#" + threadid + "#" + startdatum + "#" + state + "#"
				+ masterid + "#" + symbol + "#" + lastpagevorh + "#"
				+ urlaktname + "#" + imarker + "#" + breakid + "#" + mode + "#"
				+ lastthreadposttime + "#" + threadrankingstring + "#"
				+ eroeffnet + "#" + lastdownloadtime + "#"
				+ lastsliderupdate_SORT + "#" + errorcode + "#"
				+ steigungsfaktor + "#" + masterstring + "#" + woboerplatz
				+ "#" + prognoseanzahl + "#" + lastreadtime + "#" + puscherflag
				+ "#" + BBlastPuschdate);
	}

	public String GetSaveInfostring()
	{
		return "name#threadid#date#state#masterid#symbol#pageanzsoll#url#marker#breakid#mode#lastthreadposttime#threadranking#eroeffnet#lastdownloadtime#lastsliderupdate#errorcode#steigungsfaktor#masterstring#boerplatz#prognoseanzahl#lastreadtime#puscherflag#BBlastPuschdate";
	}

	public int calcLetzteVorhThreadseite(int forceflag)
	{
		// ermittelt die Nummer der letzten vorhandenen Threadseite
		// also die Seite die wirklich auf der Platte ist
		// falls forceflag==1 wird auf jeden Fall die binäre suche gemacht
		String fnam = null;
		int pz = this.getPageanz();
		int seitenpagez = (int) ((float) pz * 1.4f);

		if (forceflag == 0)
		{

			// Prüft nach ob die letzte Seite und vorletzte vorhanden ist,
			// wenn das der Fall ist ist alles vollständig
			if (this.checkPageAvailable(seitenpagez) != null)
			{
				if (seitenpagez >= 2)
				{ // alles ok, die letzten beiden Seiten sind da
					if (this.checkPageAvailable(seitenpagez - 1) != null)
						return seitenpagez;
				} else
					// alles ok, die einzige Seite ist da
					return seitenpagez;
			}
		}
		// wenn ein Seitenfehler vorliegt mache binäre suche um die letzte noch
		// vollständige Seite zu finden

		int links = 1;
		int rechts = seitenpagez;
		int mitte = 0;

		// prüft ob letzte Seite vorhanden
		if (this.checkPageAvailable(seitenpagez) != null)
			return seitenpagez;

		do
		{
			mitte = (links + rechts) / 2;
			if (this.checkPageAvailable(mitte) != null)
			{
				// suche im hinteren teil
				links = mitte;
			} else
			{
				// suche im vorderen teil
				rechts = mitte;
			}
		} while (Math.abs(links - rechts) > 1);

		return links;
	}

	public void setMarker(int m)
	{
		imarker = m;
	}

	public int getMarker()
	{
		return imarker;
	}

	public String getThreadrankingstring()
	{
		return threadrankingstring;
	}

	public void setThreadrankingstring(String threadrankingstring)
	{
		this.threadrankingstring = threadrankingstring;
	}

	public int getThreadid()
	{
		return threadid;
	}

	public int getMasterindex()
	{
		// hier ist der Masterindex die threadid
		return threadid;
	}

	public String getThreadname()
	{
		anames = SG.convFilename(anames);
		return anames;
	}

	public void setThreadname(String tnam)
	{
		anames = new String(SG.convFilename(tnam).toUpperCase());
		checkAnames(anames, null);
	}

	public int getMasterid()
	{
		return masterid;
	}

	public void setMasterid(int i)
	{
		masterid = i;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int st)
	{
		state = st;
	}

	public int getMode()
	{
		return mode;
	}

	public void setMode(int fl)
	{
		mode = fl;
	}

	public int getPageanz()
	{
		return lastpagevorh;
	}

	public String getStartdatum()
	{
		return startdatum;
	}

	public void setStartdatum(String dat)
	{
		startdatum = new String(dat);
	}

	public String getLastThreadPosttime()
	{
		return lastthreadposttime;
	}

	public void setLastThreadPosttime(String str1)
	{
		lastthreadposttime = str1;
	}

	public String getUrlaktname()
	{
		return urlaktname;
	}

	public void setUrlaktname(String urlaktname)
	{
		this.urlaktname = urlaktname;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public void setPageanz(int pageanzsoll)
	{
		this.lastpagevorh = pageanzsoll;
	}

	public String getEroeffnet()
	{
		return eroeffnet;
	}

	public void setEroeffnet(String eroeffnet)
	{
		this.eroeffnet = eroeffnet;
	}

	public String getLastdownloadtime()
	{
		return lastdownloadtime;
	}

	public void setLastdownloadtime(String lastdownloadtime)
	{
		this.lastdownloadtime = lastdownloadtime;
	}

	public String getLastreadtime()
	{
		return lastreadtime;
	}

	public void setLastreadtime(String lastreadtime)
	{
		this.lastreadtime = lastreadtime;
	}

	public float getSortkriterium_steigungsfaktor()
	{
		return steigungsfaktor;
	}

	public void setSortkriterium_steigungsfaktor(
			float sortkriterium_steigungsfaktor)
	{
		this.steigungsfaktor = sortkriterium_steigungsfaktor;
	}

	public String getLastsliderupdateSORT()
	{
		// die gleiche updatezeit steht in der sliderdb,
		// diese variable wird nur für Sortierzwecke verwendet
		return lastsliderupdate_SORT;
	}

	public void setLastsliderupdateSORT(String lastsliderupdate)
	{ // die gleiche updatezeit steht in der sliderdb
		this.lastsliderupdate_SORT = lastsliderupdate;
	}

	public String getWoBoerplatz()
	{
		return woboerplatz;
	}

	public void setBoerplatz(String woboerplatz)
	{
		this.woboerplatz = woboerplatz;
	}

	public int getPrognoseanzahl()
	{
		return prognoseanzahl;
	}

	public void setPrognoseanzahl(int prognoseanzahl)
	{
		this.prognoseanzahl = prognoseanzahl;
	}

	public String getErrorcode()
	{
		return errorcode;
	}

	public void setErrorcode(String errorcode)
	{
		this.errorcode = errorcode;
	}

	public String getMasterstring()
	{
		return masterstring;
	}

	public void setMasterstring(String masterstring)
	{
		this.masterstring = masterstring;
	}

	public String getBBlastPuschdate()
	{
		// gibt das datum des letzten Pusches von einem Börsenblatt an
		return BBlastPuschdate;
	}

	public void setBBlastPuschdate(String bBlastPuschdate)
	{
		BBlastPuschdate = bBlastPuschdate;
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

		String uname = "http://www.wallstreet-online.de/diskussion/"
				+ getThreadid() + "-" + (1 + (index - 1) * 10) + "-" + index
				* 10 + "/" + getUrlaktname();
		/* 1-10 11-20 21-30 */

		// http://www.wallstreet-online.de/diskussion/998804-1-10/obducat-quo-vadis
		return uname;

	}

	public String GetSavePageName_genDir(int index)
	{
		// offline\threads\09\09_1.html
		// prüft ob directory vorhanden ist

		String tn = getThreadname();

		if ((tn == null) || (tn.contains("null")) || (tn.equals("")))
		{
			Tracer.WriteTrace(10,
					"Error: threads.db error fnam fehlerhaft tdbo tid<"
							+ getThreadid() + "> mid<" + getMasterid() + ">");
		}

		if (tn == null)
			return null;
		if (tn.length() == 0)
			return null;

		char pref = tn.charAt(0);

		
		File Dirnam=new File(GC.rootpath+ "\\downloaded\\threads\\@"+pref);
		if(Dirnam.exists()==false)
		{
			Tracer.WriteTrace(20, "I:create new Directory <"+Dirnam+">");
			Dirnam.mkdir();
		}
		
		
		String fn = FileAccess.convsonderz(GC.rootpath
				+ "\\downloaded\\threads" + "\\@" + pref + "\\" + tn);

		if(fn.toUpperCase().contains("JUBII"))
			System.out.println("found jubii");
		
		// erzeugt ggf das directory
		FileAccess.checkgenDirectory(fn);

		// generiert den filenamen
		String fname = getThreadDownloadSpeichername(index);
		return fname;
	}

	public String checkPageAvailable(int index)
	{
		// return: filename, falls da
		// return: null, falls nicht da

		String tnam = getThreadname();
		char first = tnam.charAt(0);

		String fn = FileAccess.convsonderz(GC.rootpath
				+ "\\downloaded\\threads" + "\\@" + first + "\\"
				+ getThreadname() + "\\" + getThreadname() + "_" + index
				+ ".html.gzip");
		if (FileAccess.FileAvailable(FileAccess.convsonderz(fn)) == true)
			return fn;
		else
			return null;
	}

	public String GetSavePageName2(int index)
	{

		// generiert den filenamen
		String fname = getThreadDownloadSpeichername(index);

		if ((FileAccess.FileAvailable(fname)) == false)
			if ((FileAccess.FileAvailable(fname + ".gzip")) == true)
				fname = fname + ".gzip";
		return fname;
	}

	public boolean ChangeSymbol(int mid, String alt_symb, String alt_boer,
			String neu_symb, String neu_boer, ThreadsDB tdb)
	{
		// welchselt mid und symbol da bei wo- diese in der threadseite neu ist

		// Von Zahl nach Symbol möglich
		if ((Tools.is_zahl(alt_symb) == true)
				&& (Tools.is_zahl(neu_symb) == false))
			Tracer.WriteTrace(20,
					"Info:WO-Symbol Korrektur:Changesymbol von Zahl nach Symbol, symb1<"
							+ alt_symb + "> symb2<" + neu_symb + ">");

		if (Tools.is_zahl(neu_symb) == true)
		{
			Tracer.WriteTrace(20,
					"Error:Symbolwechsel nicht möglich da Zahl, symbalt<"
							+ alt_symb + "> symbneu<" + neu_symb + ">");
			return false;
		}

		// plausi
		if ((alt_symb == null) || (alt_symb.length() < 2) || (neu_symb == null)
				|| (neu_symb.length() < 2))
		{
			Tracer.WriteTrace(20, "Error: Symbolwechsel nicht möglich alt<"
					+ alt_symb + "> neu<" + neu_symb + ">");
			return false;
		}

		KursValueDB kvdb = new KursValueDB(alt_symb, 0, tdb);
		Boolean ret = kvdb.erwSymbolersetzung(mid, alt_symb, alt_boer,
				neu_symb, neu_boer);
		return ret;

	}

	public boolean ChangeMasterid(int oldmasterid, int newmasterid,
			String symbol, ThreadsDB tdb)
	{
		if ((oldmasterid == 0) || (newmasterid == 0))
			Tracer.WriteTrace(10, "Error: kann nicht von masterid<"
					+ oldmasterid + "> nach masterid<" + newmasterid
					+ "> wechseln !!!");

		if (oldmasterid == newmasterid)
		{
			Tracer.WriteTrace(20,
					"Warning kein Masteridwechsel da masterids gleich, oldmid<"
							+ oldmasterid + "> newmid<" + newmasterid + ">");
			return false;
		}

		KursValueDB kvdb = new KursValueDB(symbol, 0, tdb);
		Boolean ret = kvdb.changeMaster(oldmasterid, newmasterid, symbol);
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\masteridwechsel.txt");
		inf.writezeile("Masteridwechsel im translator: old-mid<" + oldmasterid
				+ "> new-mid<" + newmasterid + ">");
		return ret;
	}

	public String getThreadDownloadSpeichername(int index)
	{
		String tn = getThreadname();
		char pref = tn.charAt(0);

		String fname = FileAccess.convsonderz(GC.rootpath
				+ "\\downloaded\\threads" + "\\@" + pref + "\\"
				+ getThreadname() + "\\" + getThreadname() + "_" + index
				+ ".html");
		return fname;
	}

	public int getPuscherflag()
	{
		return puscherflag;
	}

	public void setPuscherflag(int puscherflag)
	{
		this.puscherflag = puscherflag;
	}

	public Boolean ReloadPage(int seitennummer)
	{
		// läd eine defekte htmlseite nochmal
		String urlstr = GetUrlName(1);
		String fnam = GetSavePageName_genDir(1);
		FileAccess.FileDelete(fnam, 0);
		Wpage.DownloadHtmlPage(urlstr, fnam, 0, 5000000, 1, 1, 0);
		return true;
	}

	public boolean UpdatePrimePageStep1(DownloadManager dm, int thresholdhours)
	// lade die Primepagedaten vom Web
	{
		String fnam = GetSavePageName_genDir(1) + ".gzip";
		String urlstr = null;
		/* SeitenzaehlerDBx sz = new SeitenzaehlerDBx(anames); */

		// falls Webseite zu neu mache nix
		if (FileAccess.CheckIsFileOlderHours(fnam, thresholdhours) == false)
			return true;

		urlstr = GetUrlName(1);
		dm.DownloadHtmlPage(urlstr, fnam, 0, 8000000, 0, 1, 0);
		return true;

	}

	public boolean UpdatePrimePageStep2(MidDB middb,ThreadsDB tdb,Boolean fehlerstop)
	// werte die Primepagedaten aus
	{
		// rückgabe: true falls erfolgreich
		// rückgabe: false, falls dieser Thread überhaupt nicht mehr existiert
		// läd die erste Threadseite des Threads neu und
		// aktualsiert die daten.
		// x1)prüft nach ob sich beim Update der PrimePage das Symbol geändert
		// hat
		// Falls Symboländerungen [Aktiensplit, Wkn etc.] wird dies in der
		// Symbolersetzung vermerkt
		// Möglicherweise ist die Primepage auch defekt ?, z.b. es ist ein
		// Aktienthread, es wird aber
		// kein Symbol angezeigt !!... dann mache nix.
		// middb: hier wird die masterid und der masterstring gespeichert
		// fehlerstop: 	true, bei einem fehler wird angehalten
		// 				false,bei einem fehler wird nix gemacht
		
		// werte neue Daten aus
		String fnam = GetSavePageName_genDir(1) + ".gzip";
		Threads html = new Threads(middb,tdb,fnam, this.getThreadid());
		System.out.println("Betrachte file<"+fnam+">");
		
		if(html==null)
		{
			Tracer.WriteTrace(20,
					"Error: Htmlseite fehlerhaft<" + fnam
							+ ">");
			return false;
			
		}
		
		
		String mstring = null;
		int Masterid_html = html.GetMasterId(GC.DEFAULT);
		String symbol_old = this.getSymbol();
		// die alte masterid ist in der threads.db
		int mid_old = this.getMasterid();

		String eroeff = Tools.entferneZeit(html.GetEroeffnetAm());

		if ((eroeff == null) || (eroeff.equalsIgnoreCase("0") == true)
				|| (Tools.isDate(eroeff) == false))
		{
			Tracer.WriteTrace(20,
					"Error: Fehler beim updateprimepage eroeffnet<" + eroeff
							+ ">");
			return false;
		}
		this.setEroeffnet(eroeff);

		int Seitenanz = html.GetSeitenzahl();
		if (Seitenanz > 0)
			setPageanz(Seitenanz);

		String urlaktnamex = new String(html.GetUrlAktName());
		setUrlaktname(urlaktnamex);

		String boerplatz = new String(html.GetBoerplatz(0));
		this.setBoerplatz(boerplatz);

		String startdatum = new String(html.GetEroeffnetAm());
		this.setStartdatum(startdatum);

		String neusterBeitrag = new String(html.GetNeusterBeitrag());
		
		if(neusterBeitrag.equals("0"))
				Tracer.WriteTrace(20, "Warning: neuster beitrag = null urlaktnamex<"+urlaktnamex+"> tid<"+this.threadid+">");
		
		this.setLastThreadPosttime(neusterBeitrag);

		try
		{
			
			mstring = new String(html.GetMasterString(0).toUpperCase());
		} catch (NullPointerException e)
		{
			return false;
		}
		if (mstring == null)
			System.out.println("mstring=null");

		if ((mstring != null) && (mstring.length() > 2))
		{
			// falls in der threads.db noch nix gesetzt ist
			if ((this.getMasterstring() == null)
					|| (this.getMasterstring().equals("null")))
				this.setMasterstring(mstring);
			else
			{// falls in der threads.db schon was gesetzt ist

				// a) webseite hat einen masterstring, dann vergleiche mit
				// threadsdb
				if ((mstring != null) && (mstring.length() > 2))
					if (this.getMasterstring().toUpperCase().equals(mstring.toUpperCase()) == false)
					{
						Tracer
								.WriteTrace(
										20,
										"Error: internal masterstring webpage<"
												+ mstring
												+ "> has different masterstring against threadsdb<"
												+ this.getMasterstring()
												+ "> => webpage incorrekt");
						return false;
					}

			}

		}

		// **************************************************************
		// prüfe nach ob die neue Primepage ein korrekter Aktienthread ist
		if (html.holeSeitentype() != GC.AktienThread)
		{
			Tracer.WriteTrace(20, "Warning: neue Primepage fnam<" + fnam
					+ "> kein Aktienthread tid<" + this.getThreadid() + ">");
			return false;
		}

		if (Masterid_html != 0)
			setMasterid(Masterid_html);

		String symb = new String(html.GetSymbol(GC.DEFAULT));
		setSymbol(symb);

		// schaut nach ob symbol in der alten Webseite vorhanden
		if ((symbol_old != null) && (symbol_old.length() > 1)
				&& (symbol_old.equals(symb) == false))
		{
			Tracer.WriteTrace(20,
					"I:Das Symbol auf der Primepage hat sich geändert tid<"
							+ threadid + "> oldsymb<" + symbol_old
							+ "> newsymb<" + symb + "> newfnam<" + fnam + ">");
			// symbol hat sich geändert, das d.h. die neue primepage hat ein
			// altes symbol

			if (mid_old != Masterid_html)
			{
				Tracer.WriteTrace(20,
						"Warning: Masterid für das Symbol hat sich geändert, alt<"
								+ mid_old + "> neu<" + Masterid_html
								+ "> symb<" + symb
								+ "> WO fehler hat Datenbank geändert !!!");
				this.ChangeMasterid(mid_old, Masterid_html, symb, tdb);
			}
			// das Symbol für die Masterid hat sich geändert, dann passe an
			// der Boersenplatz ist nicht bekannt da dieser nicht in der
			// primepage steckt
			// der Boersenplatz muss erst gesucht werden
			this.ChangeSymbol(Masterid_html, symbol_old, "???", symb, "???",
					tdb);

		}
		String urlstr = new String(GetUrlName(1));
		if (startdatum.equals("0") == true)
		{
			String msg = "Error: Thread nicht verfügbar/geschlossen?? setze state ==99 oder entferne Thread von Hand !!fnam<"
					+ fnam
					+ ">  urlstr<"
					+ urlstr
					+ "> masterid<"
					+ Masterid_html
					+ "> seitenanz<"
					+ Seitenanz
					+ "> symb<"
					+ symb + "> startdatum<" + startdatum + ">";
			Tracer.WriteTrace(20, msg);

			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\ungueltige_threads.txt");
			inf.writezeile(msg);

			// stetze nur status 99 falls onlinemode gesetzt ist
			if (is_onlinemode() == 1)
				this.setState(99);

			return false;
		} else
			return true;

		/*
		 * int leserheuteanz=html.GetLeserHeute();
		 * sz.UpdateLeserHeute(leserheuteanz);
		 */
	}

	public boolean UpdatePrimePage_dep(MidDB middb,int thresholdhours, ThreadsDB tdb)
	{
		// rückgabe: true falls erfolgreich
		// rückgabe: false, falls dieser Thread überhaupt nicht mehr existiert
		// läd die erste Threadseite des Threads neu und
		// aktualsiert die daten.
		// x1)prüft nach ob sich beim Update der PrimePage das Symbol geändert
		// hat
		// Falls Symboländerungen [Aktiensplit, Wkn etc.] wird dies in der
		// Symbolersetzung vermerkt
		// Möglicherweise ist die Primepage auch defekt ?, z.b. es ist ein
		// Aktienthread, es wird aber
		// kein Symbol angezeigt !!... dann mache nix.

		String fnam = GetSavePageName_genDir(1) + ".gzip";
		String urlstr = null;
		/* SeitenzaehlerDBx sz = new SeitenzaehlerDBx(anames); */

		// falls Webseite zu neu mache nix
		if (FileAccess.CheckIsFileOlderHours(fnam, thresholdhours) == false)
			return true;

		// hole altes Symbol
		Threads html_old = new Threads(middb,tdb,fnam, this.getThreadid());

		String symb_old = new String(html_old.GetSymbol(GC.DEFAULT));
		int mid_old = html_old.GetMasterId(GC.DEFAULT);

		urlstr = GetUrlName(1);
		Wpage.DownloadHtmlPage(urlstr, fnam, 0, 8000000, 1, 1, 0);
		Wpage.waitEnd();

		// werte neue Daten aus
		Threads html = new Threads(middb,tdb,fnam, this.getThreadid());
		int Masterid = html.GetMasterId(GC.DEFAULT);

		String eroeff = Tools.entferneZeit(html.GetEroeffnetAm());
		if ((eroeff == null) || (eroeff.equalsIgnoreCase("0") == true)
				|| (Tools.isDate(eroeff) == false))
		{
			Tracer.WriteTrace(20,
					"Error: Fehler beim updateprimepage eroeffnet<" + eroeff
							+ ">");
			return false;
		}
		this.setEroeffnet(eroeff);

		
		
		int Seitenanz = html.GetSeitenzahl();
		if (Seitenanz > 0)
			setPageanz(Seitenanz);
		
		if(Seitenanz==0)
			Tracer.WriteTrace(20, "Error: seitenaufbaufehler, keine seitenanzahl file<"+fnam+">");

		String urlaktnamex = new String(html.GetUrlAktName());
		setUrlaktname(urlaktnamex);

		String startdatum = new String(html.GetEroeffnetAm());
		if(startdatum.length()<6)
			Tracer.WriteTrace(20, "Error: seitenaufbaufehler, keine startdatum file<"+fnam+">");
		this.setStartdatum(startdatum);

		String neusterBeitrag = html.GetNeusterBeitrag();
		this.setLastThreadPosttime(neusterBeitrag);
		// **************************************************************
		// prüfe nach ob die neue Primepage ein korrekter Aktienthread ist
		if (html.holeSeitentype() != GC.AktienThread)
		{
			Tracer.WriteTrace(20, "Warning: neue Primepage fnam<" + fnam
					+ "> kein Aktienthread");
			return false;
		}
		if (Masterid != 0)
			setMasterid(Masterid);

		String symb = new String(html.GetSymbol(GC.DEFAULT));
		setSymbol(symb);

		// schaut nach ob symbol in der alten Webseite vorhanden
		if ((symb_old != null) && (symb_old.length() > 1)
				&& (symb_old.equals(symb) == false))
		{
			Tracer.WriteTrace(20,
					"I:Das Symbol auf der Primepage hat sich geändert oldsymb<"
							+ symb_old + "> newsymb<" + symb + "> newfnam<"
							+ fnam + ">");
			// symbol hat sich geändert, das d.h. die neue primepage hat ein
			// altes symbol

			if (mid_old != Masterid)
			{
				Tracer.WriteTrace(20,
						"Warning: Masterid für das Symbol hat sich geändert, alt<"
								+ mid_old + "> neu<" + Masterid + "> symb<"
								+ symb + "> WO hat Datenbank geändert !!!");
			}
			// das Symbol für die Masterid hat sich geändert, dann passe an
			// der Boersenplatz ist nicht bekannt da dieser nicht in der
			// primepage steckt
			// der Boersenplatz muss erst gesucht werden
			this.ChangeSymbol(Masterid, symb_old, "???", symb, "???", tdb);
			this.ChangeMasterid(mid_old, Masterid, symb, tdb);
		}

		if (startdatum.equals("0") == true)
		{
			String msg = "Error: Thread nicht verfügbar/geschlossen?? setze state ==99 oder entferne Thread von Hand !!fnam<"
					+ fnam
					+ ">  urlstr<"
					+ urlstr
					+ "> masterid<"
					+ Masterid
					+ "> seitenanz<"
					+ Seitenanz
					+ "> symb<"
					+ symb
					+ "> startdatum<" + startdatum + ">";
			Tracer.WriteTrace(20, msg);

			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\ungueltige_threads.txt");
			inf.writezeile(msg);

			// stetze nur status 99 falls onlinemode gesetzt ist
			if (is_onlinemode() == 1)
				this.setState(99);

			return false;
		} else
			return true;

		/*
		 * int leserheuteanz=html.GetLeserHeute();
		 * sz.UpdateLeserHeute(leserheuteanz);
		 */

	}

	public void UpdateThread(MidDB middb,UserDB udb, int threadupdateflag, int threshold,
			ThreadsDB tdb)
	{
		// hier wird ein ganzer Thread upgedated
		// daten werden also aus dem internet geladen
		// ausserdem wird db/threaddata aktualisiert (postdb)
		// threadupdateflag:sagt welche threadtypen upgedatet werden sollen
		// (z.b. diskussionsthread, aktienthread.....)
		// threadupdateflag 0, thread
		// 1, aktienthread
		
		
		if(this.getState()!=0)
		{
			Tracer.WriteTrace(20, "Info: Thread <"+this.getThreadname()+"> <"+this.getEroeffnet()+">has state <"+this.getState()+"> => bad thread, go on");
			return;
		}
		
		Htmlcompress hw = new Htmlcompress(tdb);

		String fname = null, fnamz = null, urlstr = null;
		int oldlastpage = 0, newlastpage = 0, i = 0;
		int timeout = 200000;
		int pcount = 0;
		PostDBx pdb = new PostDBx();

		if (threadupdateflag != GC.DEFAULT)
		{
			// update nur wenn dies ein aktienthread ist
			if ((this.symbol.length() < 2) && (this.masterid == 0))
			{
				Tracer
						.WriteTrace(
								20,
								"I:thread <"
										+ anames
										+ "> ist diskussionsthread und wird nicht upgedatet symb<"
										+ symbol + "> tid<" + threadid
										+ "> mid<" + masterid + ">");
				return;
			}

		}

		oldlastpage = this.calcLetzteVorhThreadseite(0);
		// oldlastpage muss mindestens bei 1 anfangen
		if (oldlastpage < 1)
			oldlastpage = 1;

		// 1)lade die erste Threadseite neu
		this.UpdatePrimePage_dep(middb,0, tdb);
		newlastpage = this.getPageanz();

		// löscht letzte unvollst Seite
		// xxx das geht so nicht mehr, die Primepage wurde schon upgedatet

		fname = GetSavePageName_genDir(oldlastpage);
		fnamz = fname + ".gzip";
		if (FileAccess.FileAvailable(fnamz))
			FileAccess.FileDelete(fnamz, 0);

		Tracer.WriteTrace(20, "I:Lade Seiten von<" + oldlastpage + "> bis<"
				+ newlastpage + ">");

		if (IsValid.Thread(fnamz) == false)
		{
			Tracer.WriteTrace(20,
					"W:Threadid 0279 not allowed go on !!! (do nothing)");
			return;
		}

		if (newlastpage > 10000)
		{
			Tracer.WriteTrace(20, "Warning: thread <" + fname
					+ "> is very long maxpage=<" + newlastpage + ">");
			// return;
		}
		// 2)dann lade sämtliche Seiten
		for (pcount = 0, i = oldlastpage; i <= newlastpage; i++, pcount++)
		{
			// baue url-string auf
			urlstr = GetUrlName(i);
			fname = GetSavePageName_genDir(i);
			fnamz = fname + ".gzip";
			Wpage.DownloadHtmlPage(urlstr, fnamz, 0, timeout, 0, 0, 0);

			System.out.print(i + ",");
			if ((pcount % 40) == 0)
				System.out.println();

			if (i == newlastpage)
			{
				Wpage.waitEnd();
				this.UpdateSeitenzaehler(middb,tdb);
			}
		}
		// warte bis alles geladen wurde
		this.setLastdownloadtime(Tools.get_aktdatetime_str());
		Wpage.waitEnd();

		// komprimiere
		for (pcount = 0, i = oldlastpage; i <= newlastpage; i++, pcount++)
		{
			fname = GetSavePageName_genDir(i);
			fnamz = fname + ".gzip";
			hw.cleanCompressHtmlFile(middb,fnamz, fnamz, this.getThreadid(),"NO1");
		}

		// die Postdb updaten
		pdb.UpdatePostDB(oldlastpage, newlastpage, udb, this);
		// die Slider updaten
		System.out.println();

	}

	public void CompressThread()
	{
		// hier werden die Postings eines einzelnen Threads komprimiert
		// und in db/threads/ abgespeichert
		int pageanz = this.getPageanz();
		for (int i = 0; i < pageanz; i++)
		{

		}
	}

	public void UpdateSeitenzaehler(MidDB middb,ThreadsDB tdb)
	{

		// den Seitenzaehler für den Thread updaten
		// aber aufpassen, die letzte Seite sollte neu sein
		// hier könnte man noch eine datumsüberprüfung einfügen
		// xxxxxxx todo
		Inf inf = new Inf();
		String fnam = GetSavePageName_genDir(lastpagevorh);
		String fnamz = fnam + ".gzip";
		Threads html = new Threads(middb,tdb,fnamz, this.getThreadid());
		SeitenzaehlerDBx sz = new SeitenzaehlerDBx(anames);
		int leserheuteanz = html.GetLeserHeute();
		sz.UpdateLeserHeute(leserheuteanz);
		inf.appendzeile(GC.rootpath
				+ "\\db\\seitenzaehler\\0000000Übersicht00000.db", Tools
				.get_aktdatetime_str()
				+ ":" + anames + ":" + leserheuteanz, true);

	}

	public int getBreakid()
	{
		return breakid;
	}

	public void setBreakid(int breakid)
	{
		this.breakid = breakid;
	}

	public boolean checkPostdbAvailable()
	{
		String nam = GC.rootpath + "\\db\\threaddata\\" + getThreadname() + "_"
				+ getThreadid() + ".db";
		return (FileAccess.FileAvailable(nam));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ThreadDbObj other = (ThreadDbObj) obj;
		if (threadid != other.threadid)
			return false;
		return true;
	}

	public void AppendCopyIconTags(Htmlpage htmlTexte, String iconzielpath)
	{
		// fügt für einen Thread die iconTags in die htmlseite ein
		String[] kuerzel =
		{ "1d", "5d", "1m", "3m", "1y", "5y" };
		String pageanf = null;
		String pageend = null;

		if (masterid == 0)
			return;
		for (int j = 5; j >= 0; j--)
		{
			pageanf = "";
			pageend = "";
			if (j == 5)
				pageanf = "<p>";
			if (j == 0)
				pageend = "</p>";

			// bsp: src="../../../db/kursicons/9092_1y.png" WIDTH=300 HEIGHT=255
			// >

			/*
			 * String htmlzeile = pageanf + "<img src=\"../../../" +
			 * "/db/kursicons/" + masterid + "_" + kuerzel[j] + ".png\"
			 * WIDTH=300 HEIGHT=255 >" + pageend;
			 */

			String htmlzeile = pageanf + "<img src=\"kursicons/" + masterid
					+ "_" + kuerzel[j] + ".png\" WIDTH=300 HEIGHT=255 >"
					+ pageend;

			// das Kursicon umkopieren
			FileAccess.checkgenDirectory(iconzielpath + "\\kursicons");

			String quellfile = GC.rootpath + "\\db\\kursicons\\" + masterid
					+ "_" + kuerzel[j] + ".png";
			String zielfile = iconzielpath + "\\kursicons\\" + masterid + "_"
					+ kuerzel[j] + ".png";

			// kopiere das icon
			if (FileAccess.FileAvailable(quellfile) == false)
				Tracer.WriteTrace(20, "Warning: file<" + quellfile
						+ "> nicht zum kopieren verfügbar");
			else if (FileAccess.copyFile2_dep(quellfile, zielfile) == false)
				Tracer.WriteTrace(20, "Warning kann file<" + quellfile
						+ "> nicht nach <" + zielfile + "> kopieren !!");

			// blende die Kursverläufe ein 5jahre, 1 Jahr, 3 monate.....
			htmlTexte.Append(htmlzeile);
		}
	}

	public ThreadsPostElem GetNextPosting(String mindate, Boolean firstflag)
	{
		// holt das nächste posting aus einer Htmlseite
		// mindate, postingdate >=mindate
		// action, falls action = 0, dann hole das erste posting

		// falls erster aufruf, dann suche den anfang der postingkette
		if (firstflag == true)
		{
			lastpage = 1;
			lastpostid = 1;
		}

		ThreadsPostElem einposting = new ThreadsPostElem();

		for (int i = lastpage; i < lastpagevorh; i++)
		{
			String fnam = GetSavePageName2(i);
			DB<ThreadsPostElem> posting = new DB<ThreadsPostElem>(fnam);

			while ((einposting = posting.GetNextPosting()) != null)
			{
				// prüft ob das postdate älter als mindate
				String postdate = einposting.get_datetime();
				if (Tools.datum_ist_aelter_gleich(mindate, postdate) == true)
				{
					if (firstflag == true)
					{
						// falls der anfang der squence gesucht wird
						lastpage = i;
						lastpostid = einposting.get_postid();
						return (einposting);
					}
					// falls passendes posting in der sequenz
					if (einposting.get_postid() > lastpostid)
					{
						// aufsteigendes posting gefunden
						lastpostid = einposting.get_postid();
						lastpage = i;
						return (einposting);
					}
				}
			}
		}
		return null;
	}

	public int getSymbolflag()
	{
		return symbolflag;
	}

	public void setSymbolflag(int symbolflag)
	{
		this.symbolflag = symbolflag;
	}

	public boolean isValidAktienthread(int kurscheckflag)
	{
		// falls kurscheckflag==1, dann wird auch überprüft ob der Kurs da ist
		// dann ist ein valid aktienthread ein thread mit korrekten kursen

		String symb = this.symbol;

		if (Tools.isKorrektSymbol(symb) == false)
			return false;

		if (kurscheckflag == 1)
			if (FileAccess.FileAvailable(GC.rootpath + "\\db\\kurse\\" + symb
					+ ".csv") == false)

				return false;

		return true;
	}

	private void addCompressedThreads(CompressedThreadDBx compt, String fnamz,
			Threads html, KursValueDB kvdb)
	{
		// hier wird die htmlseite zu den kompressed threads hinzugefügt
		// die Htmlseite besteht aus 10 Postings
		// Ausserdem wird der Kurs ermittelt und der Seite gleich mitgegeben

		// Achtung: Es wird nix doppelt eingefügt

		DB<ThreadsPostElem> posting = new DB<ThreadsPostElem>(fnamz);
		ThreadsPostElem einposting = new ThreadsPostElem();
		CompressedPostingObj co = new CompressedPostingObj();

		while ((einposting = posting.GetNextPosting()) != null)
		{

			co.setDatetime(einposting.get_datetime());
			co.setPostid(einposting.get_postid());
			co.setThreadid(einposting.get_threadid());
			String unam = einposting.get_username();
			if (IsValid.isValidUsername(unam) == true)
				co.setUsername(unam);
			else
				co.setUsername("dummy");

			String dat = Tools.convDatumPunktStrich(Tools
					.entferneZeit(einposting.get_datetime()));
			Kursvalue kv = kvdb.holeKurswert(dat, 0);
			co.setKursvalue(kv.getKv());

			// fügt element ein falls es noch nicht drin ist
			compt.addElem(co);
		}
	}

	private void checkCorrectPostinganzahl(MidDB middb,String fnamz, Threads html,
			String urlnam, int i, int j, int pageanz, Htmlcompress hw)
	{
		int loadcounter=0;
		int banz = html.GetPostinganzahl();
		if ((banz < 10) && (j < pageanz))
		{
			Tracer.WriteTrace(20, "W:i<" + i + "> Seite <" + fnamz
					+ "> postanz <" + banz + ">fehlerhaft lade neu");
			// Seite fehlerhaft
			if (FileAccess.FileAvailable(fnamz))
				FileAccess.FileDelete(fnamz, 0);
			Wpage.DownloadHtmlPage(urlnam, fnamz, 0, 50000, 1, 1, 0);

			while(hw.cleanCompressHtmlFile(middb,fnamz, fnamz, this.getThreadid(),urlnam)==false)
			{
				//bei fehlerhaft lade noch einmal
				Wpage.DownloadHtmlPage(urlnam, fnamz, 0, 50000, 1, 1, 0);
				Tracer.WriteTrace(20, "Warning: corrupt html download urlnam<"+urlnam+"> fnamz<"+fnamz+"> tid<"+this.getThreadid()+"> download again");
				loadcounter++;
				
				//Nach 3 Versuchen breche ab
				if(loadcounter>3)
					Tracer.WriteTrace(10, "Error: corrupt html download urlnam<"+urlnam+"> fnamz<"+fnamz+"> tid<"+this.getThreadid()+"> can´t download/compress file");
					
			}
			
		}
	}

	private void checkCorrectEroeffnetAm(MidDB middb,String fnamz, Threads html,
			String urlnam, int i, int j, int pageanz, Htmlcompress hw)
	{
		int ladeflag = 0;
		int tid = html.GetThreadId();
		String eroeffnet = Tools.entferneZeit(html.GetEroeffnetAm());

		if (tid != this.getThreadid())
		{
			Tracer.WriteTrace(20, "W:i<" + i + "> Seite <" + fnamz
					+ "> fehlerhaft, tid stimmt nicht tid<" + tid + "> erwtid<"
					+ this.getThreadid() + ">, lade neu");
			ladeflag = 1;
		}
		if (Tools.zeitdifferenz_minuten(eroeffnet, this.getEroeffnet()) > 10)
		{
			Tracer.WriteTrace(20, "W:i<" + i + "> Seite <" + fnamz
					+ "> fehlerhaft, eröffnet stimmt nicht eröffnet<"
					+ eroeffnet + "> erweroeffnet<" + this.getEroeffnet()
					+ ">, lade neu");
			ladeflag = 1;
		}
		if (ladeflag == 1)
		{
			// Seite fehlerhaft, lade neu
			if (FileAccess.FileAvailable(fnamz))
				FileAccess.FileDelete(fnamz, 0);
			Wpage.DownloadHtmlPage(urlnam, fnamz, 0, 50000, 1, 1, 0);
			hw.cleanCompressHtmlFile(middb,fnamz, fnamz, this.getThreadid(),urlnam);
		}
	}

	private void korrektFilename(String fnamz)
	{
		System.out.println("found ._ <" + fnamz + ">");
		String fnamez2 = fnamz.replaceAll("._", "_");
		if (FileAccess.FileAvailable(fnamz))
		{
			System.out.println("rename <" + fnamz + "> to <" + fnamez2 + ">");
			FileAccess.Rename(fnamz, fnamez2);
		}
	}

	private void ueberpruefePostdb(ThreadsDB tdb, ThreadDbObj tdbo,
			int pageanz, int i, UserDB udb)
	{
		String filedbnam = GC.rootpath + "\\db\\threaddata\\"
				+ tdb.getAktThreadName() + "_" + tdb.GetThreadid() + ".db";
		PostDBx pdb = new PostDBx();
		pdb.ReadPostDB(filedbnam);

		// versucht eine neue Sequence zu bilden
		int trycounter = 0;
		while (pdb.CheckSequence() == false)
		{
			trycounter++;
			// Sequencenfehler, erzeuge die db neu
			Tracer.WriteTrace(20, "W: i<" + i + "> Sequence failure <"
					+ filedbnam + " > not available 05");
			FileAccess.FileMoveBadPages(filedbnam);

			Tracer.WriteTrace(20, "I:build new .dbfile <" + filedbnam + " >");
			// kein update der postdb, da sequencen check fehler
			pdb.BuildPostinglistEinThread(1, pageanz, "x", 0, udb, tdbo, 0);
			pdb.WritePostDB(GC.rootpath + "\\db\\threaddata\\"
					+ tdb.getAktThreadName() + "_" + tdb.GetThreadid() + ".db",
					udb);
			udb.WriteDB();

			if (trycounter > 3)
			{
				Tracer
						.WriteTrace(5,
								"ERROR: Fatal Error Sequence konnte nach 3 Versuchen nicht erstellt werden");
				Tracer
						.WriteTrace(5,
								"ERROR: ********** Nacharbeitung Notwendig *******************");
				break;
			}
		}
	}

	
	
	public  void updateCompressedThread(MidDB middb,int threadindx, int tanz, ThreadsDB tdb,
			UserDB udb, int loeschflag)
	{
		// komprimiert die htmlseiten zu diesem thread
		// legt die kompressed-htmlseiten an und versieht diese auch mit einen
		// Kurs
		// threadindx: index des thread 0...n
		// tanz:anzahl der gesammten threads
		// tdb:
		// udb:
		// loeschflag: 1, dann wird compressed thread neu erstellt
		KursValueDB kvdb = new KursValueDB(this.getSymbol(), 0, tdb);
		Htmlcompress hw = new Htmlcompress(tdb);
		int fehlzaehl = 0;
		CompressedThreadDBx compt = null;

		while (fehlzaehl < 2)
		{
			try
			{
				// lade das bereits bestehende
				compt = new CompressedThreadDBx(Integer.toString(this
						.getThreadid()));
				break; // alles ok
			} catch (CompressedThreadException e)
			{
				Inf inf = new Inf();
				inf.setFilename(GC.rootpath
						+ "\\db\\reporting\\compressedThreadError_deleted.txt");
				inf.writezeile(Tools.get_aktdatetime_str() + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
				char first = String.valueOf(this.getThreadid()).charAt(0);
				// lösche compressed damit sie neu erstellt werden kann
				String fna = GC.rootpath + "\\db\\compressedthreads\\@" + first
						+ "\\" + this.getThreadid() + ".db";
				FileAccess.FileDelete(fna, 0);
				fehlzaehl++;
				continue;
			}
		}

		if (loeschflag == 1)
		{
			// hier wird die compressed db für den thread komplett gelöscht
			compt.cleanMemDB();
			compt.cleanfile();
		}

		// stelle fest welches die letzte Htmlseite in der compressed db ist
		int anzcompressed = compt.getanz();
		int lastpagecompr = anzcompressed / 10;
		if (lastpagecompr == 0)
			lastpagecompr = 1;

		int lastpagetdbo = this.getPageanz();

		lastpagevorh = calcLetzteVorhThreadseite(1);

		// falls zuviele Seiten im Verzeichniss, dann lösche die überflüssigen
		// es dürfen nur soviele threadseiten vorhanden sein wie es die
		// primepage(tdbo) erlaubt
		if (lastpagevorh > lastpagetdbo)
		{
			// lösche die überflüssigen threadseiten
			for (int i = lastpagetdbo + 1; i <= lastpagevorh; i++)
			{
				String fname = this.GetSavePageName_genDir(i) + ".gzip";
				if ((FileAccess.FileAvailable(fname)) == true)
					FileAccess.FileDelete(fname, 0);
			}
		}
		// gehe durch die Htmlseiten und baue die compressed.db weiter aus
		for (int j = lastpagecompr; j <= lastpagetdbo; j++)
		{
			String urlnam = this.GetUrlName(j);
			String fname = this.GetSavePageName_genDir(j);
			// falls was ungezipptes da ist lösche es
			if ((FileAccess.FileAvailable(fname)) == true)
				FileAccess.FileDelete(fname, 0);

			String fnamz = fname + ".gzip";

			//compresse: im Fehlerfall wiederhole max 3 mal
			
			while ((hw.cleanCompressHtmlFile(middb,fnamz, fnamz, this.getThreadid(),urlnam) == false) && (fehlzaehl<3))
			{
				fehlzaehl++;
				Tracer.WriteTrace(20, "Warning: Htmlseite verschwunden <"
						+ fnamz + ">??, lade neu url<"+urlnam+">");
				Wpage.DownloadHtmlPage(urlnam, fnamz, 0, 50000, 1, 1, 0);
				hw.cleanCompressHtmlFile(middb,fnamz, fnamz, this.getThreadid(),urlnam);

				String fnamez2 = fnamz.replaceAll("_", "._");
				if (FileAccess.FileAvailable(fnamez2) == true)
				{
					System.out.println("fnamez2 <" + fnamez2
							+ ">ist aber vorhanden");
					System.out.println("möchte umbenennen von <" + fnamez2
							+ "> nach <" + fnamz + ">");
					FileAccess.Rename(fnamez2, fnamz);
				}
			}

			if (fnamz.contains("._"))
				korrektFilename(fnamz);

			// komprimiert die htmlseite
			System.out.println("compressed <" + threadindx + "|" + tanz + "> <"
					+ j + "|" + lastpagevorh + "> <" + fnamz + ">");
			

			// zaehlt die Postings
			Threads html = new Threads(middb,tdb,fnamz, this.getThreadid());

			// überprüft die postingsanzahl auf dieser einen Threadseite und
			// korrigiert diese ggf.
			checkCorrectPostinganzahl(middb,fnamz, html, urlnam, threadindx, j,
					lastpagevorh, hw);

			// überprüft 'tid' und 'eröffnet am' und korrigiert diese ggf.
			checkCorrectEroeffnetAm(middb,fnamz, html, urlnam, threadindx, j,
					lastpagevorh, hw);

			// fuege die Htmlseite zur compressed db hinzu
			addCompressedThreads(compt, fnamz, html, kvdb);
		}
		// speichere kompressed threads
		compt.WriteDB();
		ueberpruefePostdb(tdb, this, lastpagevorh, threadindx, udb);
	}

	public Boolean hatNeuePostings()
	{
		// hier wird überprüft ob neue Postings im Thread sind
		// d.h. ob lastthreadposttime älter als lastthreadreadtime ist

		// im Zweifelsfall sind neue Postings da
		if (((lastthreadposttime == null) || (lastreadtime == null)
				|| (lastthreadposttime.contains("null")) || (lastreadtime
				.contains("null"))))
			return true;

		if (Tools.datum_ist_aelter(lastreadtime, lastthreadposttime) == true)
			return true;
		else
			return false;
	}

	public String calcTargetname(int postnr)
	{
		// http://www.wallstreet-online.de/diskussion/1137546-691-700/ksh-nach-dem-aktiensplit
		int tid = this.getThreadid();
		int postend = postnr + 9;
		String targetlink = "http://www.wallstreet-online.de/diskussion/" + tid
				+ "-" + postnr + "-" + postend + "/" + this.getUrlaktname();
		return targetlink;
		// http://www.wallstreet-online.de/diskussion/1137546-691-700/ksh-nach-dem-aktiensplit
	}

	private DB<ThreadsPostElem> holeWebseite(ThreadDbObj tdbo, int page)
	{
		// Holt die Postings für eine ganze Webseite(10 Postings)
		String fnam = tdbo.GetSavePageName_genDir(page);
		DB<ThreadsPostElem> posting = new DB<ThreadsPostElem>(fnam + ".gzip");
		return posting;
	}

	public int sucheSeiteStartdatum(ThreadsDB tdb,String suchdatum, int maxpage)
	{
		// Sucht den kleinsten Index der das erste mal das Startdatum
		// erreicht oder überschreitet
		// startdatum: die erste page mit dem datum wird gesucht
		// maxpage: bis zu dieser maximalen pagenummer wird gesucht
		// ret: nr
		// 0: im fehlerfall

		Htmlcompress hw = new Htmlcompress(tdb);
		// int startindex = 1;
		int i = 1;
		int foundindex = 0;
		String founddatum = null;
		suchdatum = Tools.entferneZeit(suchdatum);

		// Wende binäre suche an
		int a1 = 1;
		int a2 = maxpage;
		while (5 == 5)
		{
			i = (a2 + a1) / 2;
			System.out.println("Step1: BinärCheck a1<" + a1 + "> a2<" + a2
					+ "> wähle i<" + i + ">");

			DB<ThreadsPostElem> posting = this.holeWebseite(this, i);
			ThreadsPostElem obj = posting.GetNextPosting();
			if (obj == null)
			{
				a2 = a2 - 1;
				continue;
			}
			String pagedatum = Tools.entferneZeit(obj.get_datetime());

			// schutzabfrage
			if ((startdatum.equals("null")) || (startdatum == null))
			{
				a2 = a2 - 1;
				continue;
			}

			if (suchdatum.equals(pagedatum) == true)
			{
				// found
				foundindex = i;
				founddatum = pagedatum;
				break;
			}

			if (Tools.datum_ist_aelter_gleich(suchdatum, pagedatum) == false)
			{
				// gehe in den oberen bereich
				a1 = i;
			} else
				// gehe in den unteren bereich
				a2 = i;

			// abbruchbedinung falls nicht gefunden und alles durchsucht
			if (a2 - a1 == 1)
			{
				// setze auf das was da ist
				foundindex = i;
				break;
			}
		}

		if (foundindex == 0)
		{
			System.out.println("Error: suchdatum<" + suchdatum
					+ "> nicht gefunden");
			return 0;
		}

		// gehe jetzt solange rückwärts bis 1 bis ein anderes datum errreicht
		// wird
		for (i = foundindex; i > 1; i--)
		{
			System.out.println("Step2: BinärCheck page<" + i + "|" + maxpage
					+ ">");
			DB<ThreadsPostElem> posting = this.holeWebseite(this, i);
			ThreadsPostElem obj = posting.GetNextPosting();
			String pagedatum = Tools.entferneZeit(obj.get_datetime());

			if (pagedatum.equals(founddatum) == false)
			{
				Tracer.WriteTrace(20, "Step2: BinärCheck found date<"
						+ pagedatum + "> index<" + i + ">");
				return i;
			}
		}
		return i;
	}

	public int calcUserpostanzahl(String unam)
	{
		int postanz = 0;
		PostDBx pdb = new PostDBx();
		// Berechnet wie oft ein User in diesem Thread gepostet hat

		String filedbnam = GC.rootpath + "\\db\\threaddata\\" + this.anames
				+ "_" + this.getThreadid() + ".db";

		if (FileAccess.FileAvailable(filedbnam))
		{
			pdb.ReadPostDB(filedbnam);
			// ermittle wie oft ein user gepostet hat
			postanz = pdb.getUserPostinganz(unam);
		} else
			postanz = -99;

		return postanz;
	}

	public void postprocess()
	{
	}
	/****************************************************************/
	
}
