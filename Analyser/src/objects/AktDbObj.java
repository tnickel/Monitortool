package objects;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import html.Threads;
import interfaces.DBObject;
import internetPackage.DownloadManager;
import kurse.LadeKurs;
import mainPackage.GC;
import stores.MidDB;
import stores.ThreadsDB;

public class AktDbObj extends Obj implements DBObject
{

	private int threadid = 0;
	private String symbol = null;
	private String boerse = null;
	private String aktname = null;
	private String wkn = null;
	private int masterid = 0;
	private int status = 0;
	private String threadfullname = null;
	private LadeKurs kurs = null;

	// newflag
	private int newflag = 0;

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public int getNewflag()
	{
		return newflag;
	}

	public void setNewflag(int newflag)
	{
		this.newflag = newflag;
	}

	public AktDbObj()
	{
	}

	public AktDbObj(int tid, String symb, String boer, String aktnam,
			String wk, int masteri, int sta, String threadfulln)
	{
		// threadid,symbol,boerse, aktname, wkn, masterid,
		// status, threadfullname
		threadid = tid;
		symbol = new String(symb);
		boerse = new String(boer);
		aktname = new String(aktnam);
		wkn = new String(wk);
		masterid = masteri;
		status = sta;
		threadfullname = new String(threadfulln);

		if (aktname.contains("Gold PM MiniFuture"))
			System.out.println("found gold");

		aktname = SG.convFilename(aktname);

		if (aktname.contains("/") == true)
			Tracer.WriteTrace(10,
					"ERROR: internal error in construktor aktname=<" + aktname
							+ ">");
	}

	public AktDbObj(String zeile) throws BadObjectException
	{
		try
		{
			threadid = Integer.valueOf(SG.nteilstring(zeile, "#", 1));
			symbol = new String(SG.nteilstring(zeile, "#", 2));
			boerse = new String(SG.nteilstring(zeile, "#", 3));
			aktname = new String(SG.nteilstring(zeile, "#", 4));
			aktname = SG.convFilename(aktname);

			wkn = new String(SG.nteilstring(zeile, "#", 5));
			masterid = Integer.valueOf(SG.nteilstring(zeile, "#", 6));
			status = Integer.valueOf(SG.nteilstring(zeile, "#", 7));
			threadfullname = new String(SG.nteilstring(zeile, "#", 8));
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String GetSaveInfostring()
	{
		return "threadid#symbol#boerse#aktname#wkn#masterid#status#threadfullname";
	}

	@Override
	public String toString()
	{
		return (threadid + "#" + symbol + "#" + boerse + "#" + aktname + "#"
				+ wkn + "#" + masterid + "#" + status + "#" + threadfullname);
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
		AktDbObj other = (AktDbObj) obj;
		if (threadid != other.threadid)
			return false;
		return true;
	}

	public int getThreadid()
	{
		return threadid;
	}

	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getBoerse()
	{
		return boerse;
	}

	public void setBoerse(String boerse)
	{
		this.boerse = boerse;
	}

	public String getAktname()
	{
		if (aktname.contains("/") == true)
			Tracer.WriteTrace(10,
					"ERROR: internal error in construktor aktname=<" + aktname
							+ ">");
		return aktname;
	}

	public void setAktname(String aktname)
	{
		aktname = SG.convFilename(aktname);
		this.aktname = aktname;
	}

	public String getWkn()
	{
		return wkn;
	}

	public void setWkn(String wkn)
	{
		this.wkn = wkn;
	}

	public int getMasterid()
	{
		return masterid;
	}

	public void setMasterid(int masterid)
	{
		this.masterid = masterid;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getThreadfullname()
	{
		return threadfullname;
	}

	public void setThreadfullname(String threadfullname)
	{
		this.threadfullname = threadfullname;
	}

	public void lade_aktname_symbol_wkn(MidDB middb,ThreadsDB tdb,DownloadManager downloader,int fehlerindex, int forceflag,
			ThreadDbObj tdbo)
	{

		// holt für eine threadid den aktnamen, symbol und wkn
		// a)erst wird geschaut ob diese info in der aktdb bereits vorhanden
		// ist.
		// b)Ist dies nicht der fall wird die erste threadseite geladen
		// und diese information aus dieser Seite geholt.
		// Die erste Threadseite wird in ein temporäres
		// Verzeichniss gespeichert. Aus dieser Threadseite werden 3 Keywörter
		// extrahiert.
		// return: void
		// falls die htmlseite nicht geladen werden konnte haben wir ein problem
		// !!
		String aktname = null, symbol = null, wkn = null;

		//DownloadManager downloader = new DownloadManager(GC.MAXLOW);

		int ran = (int) (Math.random()*1000+1);
		
		String filename = GC.rootpath + "\\tmp\\tmp_"+ran+".html";
		
		int masterid = 0;

		// a) schaue nach was in aktdb
		int mid = getMasterid();
		String symb = getSymbol();
		String aktnam = getAktname();
		if ((mid == 0) || (symb.length() < 2) || (aktnam.length() == 1)
				|| (forceflag == 1))
		{
			// aktdb-eintrag nicht vollständig

			// lösche tmp
			if (FileAccess.FileAvailable0(filename) == true)
				FileAccess.FileDelete(filename,1);
			// b2) lade erste page in tmp (in der ersten page steht dies)
			String urlstr = "http://www.wallstreet-online.de/community/thread/"
					+ getThreadid() + "-1.html";

			int errorcounter = 0;
			while ((FileAccess.FileLength(filename) < 5000)
					&& (errorcounter < 10))
			{
				
				downloader.DownloadHtmlPage(urlstr, filename, 0, 5000000, 1, 0,
						0);
				if (FileAccess.FileLength(filename) < 5000)
				{
					// im fehlerfall versuche nochmal
					try
					{
						Thread.sleep(30000);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					errorcounter++;
				}
				else
					break;
			}
			
			if (FileAccess.FileLength(filename) < 5000)
			{
				String msg = "Error:SCHLIESSE THREAD tid<"
						+ getThreadid()
						+ ">: Grunddaten mid,symb,wkn für url<"
						+ urlstr
						+ "> fnam<"
						+ filename
						+ "> konnten nach 10 Versuchen nicht geladen werden setze akdb state=10 und state=99 in threaddb";

				Inf inf = new Inf();
				inf.setFilename(GC.rootpath + "\\db\\reporting\\threadschliessung.txt");
				inf.writezeile(Tools.get_aktdatetime_str()+" "+msg);
				Tracer.WriteTrace(20, msg);
				FileAccess.FileDelete(filename,0);
				
				setStatus(10);
				if (tdbo != null)
					tdbo.setState(99);

				return;
			}
			// c) hole aus diesem File den AKT-Namen, Speichere diesen
			// AKT-Namen im Fehlerfile in ein Datenfile
			Threads html = new Threads(middb,tdb,filename, this.getThreadid()); // selektiert
																		// dieses
																		// File
			aktname = new String(html.GetAktName(GC.NOWARNING));
			symbol = new String(html.GetSymbol(GC.NOWARNING));
			wkn = new String(html.GetWkn());
			masterid = html.GetMasterId(GC.NOWARNING);
			setAktname(aktname);
			setSymbol(symbol);
			setWkn(wkn);
			setMasterid(masterid);

		}
		if ((getAktname().equalsIgnoreCase("0"))
				|| (getSymbol().equalsIgnoreCase("0")))
		{
			// Fehlerfall, ein Keyword nicht gefunden. Sichere datei
			FileAccess.copyFile(filename, GC.rootpath + "\\tmp\\errorpages\\"
					+ masterid + "_" + fehlerindex + ".html");

			FileAccess.FileDelete(filename, 0);
			setStatus(10);
			return;
		}
		FileAccess.FileDelete(filename, 0);
		return;
	}

	public boolean SucheYahooBoerse(DownloadManager dm,ThreadDbObj tdbo)
	{
		if (kurs == null)
			kurs = new LadeKurs();

		if (kurs.SucheBoerseLadeKurs2(this, dm,tdbo) == true)
			return true;

		// kein Kurs gefunden
		this.setStatus(55);
		this.setBoerse("NOKURS");
		return false; // ladefehler, keine börse gefunden

	}

	public boolean LadeKurs(ThreadDbObj tdbo, DownloadManager dm, int threshold)
			throws InterruptedException
	{
		if (kurs == null)
			kurs = new LadeKurs();

		kurs.LadeKurs(this, tdbo, dm, threshold);

		return true;
	}
	public void postprocess()
	{}
}
