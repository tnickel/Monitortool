package basis;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.util.HashSet;

import mainPackage.GC;
import objects.ThreadDbObj;
import stores.MidDB;
import stores.PostDBx;
import stores.ThreadsDB;

abstract public class BAWorkThreads extends BAWorkHtml
{
	DownloadManager Wpage_glob = new DownloadManager(GC.MAXLOW);
	int timeout_glob = 200000;
	private int aktBreakid = 0;
	// durchlaufmode bestimmt wie die threads durchlaufen werden
	// =>0 nicht gesetzt, 5=>druchlaufe nur threads mit breakid
	private int durchlaufmode = 0;
	private String username = null;
	private int SetNextThread_flag = 0;
	PostDBx pdb = new PostDBx();
	private int lastposition = 0;
	public ThreadsDB tdb = new ThreadsDB();
	private int threadwriteflag = 0;
	Inf minf = new Inf();

	//falls ein tidset gesetzt ist werden nur threads
	//mit passender tid angefahren
	private HashSet tidset_glob=null;
	
	public BAWorkThreads()
	{
	}

	// in dieser Klasse steht eine Webseite zum Bearbeiten bereit
	

	public abstract void BAworkOneThreadPageAbstract(String Threadname, String pagename,
			int pos, int posmax, int pcount, int pcountmax, ThreadDbObj tdbo_x,
			ThreadsDB tdb,MidDB middb);
	
	public int BAgetaktThreadId()
	{
		return (tdb.GetThreadid());
	}

	public int BAgetaktBreakid()
	{
		return aktBreakid;
	}

	public String BAgetThreadname()
	{
		return (tdb.GetThreadName(tdb.GetThreadid()));
	}

	public int BAgetanzElements()
	{
		return (tdb.GetanzObj());
	}
	
	// Mit dem Durchlaufmode wird ein Kriterium gesetzt nach dem die
	// Seiten ausgewählt werden
	public boolean BAsetDurchlaufmode(int mode, String unam)
	{
		durchlaufmode = mode;
		username = new String(unam);
		return true;
	}

	public boolean BAsetTidMenge(HashSet tidset)
	{
		//Wenn eine tidmenge gesetzt ist, dann werden nur die threads mit der passenden
		//tid angefahren
		tidset_glob=tidset;
		return true;
	}
	
	public boolean BAsetLastposition(int pos)
	{
		lastposition = pos;
		return true;
	}

	public boolean BAsetNextThread()
	{
		// wenn dies Flag gesetzt wird dann wird zum nächsten Thread gesprungen
		// es wird zum nächsten Thread gesprungen
		SetNextThread_flag = 1;
		return true;
	}

	// Hier werden die Threads durchlaufen. Durchlaufmodi werden berücksichtigt
	public void BAdurchlaufThreads()
	{ // Hier gibt es 2 Modi
		// a) die seiten werden gezielt ausgewählt oder
		// b) es wird seite für Seite durchgelaufen
		int i = 0, j = 0;
		//Moni mon = new Moni();
		int maxthread = tdb.GetanzObj();
		MidDB middb=new MidDB(tdb);
		String fnam = null;
		tdb.ResetDB(lastposition);

		// a)gehe gezielt durch die seiten wo auch nur der Username vorkommt
		if (durchlaufmode == GC.MODE_ONLY_THREAPAGES_WITH_USERNAME)
		{
			// gehe durch alle threads wo der username vorkommt
			for (j = lastposition; j < maxthread; j++)
			{
				int postnummer = 0, lastpostnummer = 0, postanz = 0, threadseite = 0, lastthreadseite = 0;
				// hier wird gezielt durchlaufen, also nur wo der user vorkommt
				// es werden nur threadseiten geladen wo der username auch
				// vorkommt. Dies wird anhand db/threaddata/.... ermittelt
				int tid= tdb.GetThreadid();
				//falls thread invalid state!=0 dann gehe weiter
				ThreadDbObj tdbo=(ThreadDbObj)tdb.GetAktObject();
				if(tdbo.getState()!=0)
					SetNextThread_flag=1;
				
				String filedbnam = GC.rootpath + "\\db\\threaddata\\"
						+ tdb.getAktThreadName() + "_" + tid
						+ ".db";

				if((j%10000)==0)
					System.out.print("\ncheck<"+j+">");
				
				if(tidset_glob!=null)
					if(tidset_glob.contains(tid)==false)
					{
						if (tdb.SetNextObject() == false)
							break;
						continue;
					}
				if((j%100==0))
					System.out.print(".");
				if(pdb.checkOfflineUsername(username,filedbnam)==false)
				{
					if (tdb.SetNextObject() == false)
						break;
					continue;
				}
				pdb.ReadPostDB(filedbnam);
				// ermittle wie oft ein user gepostet hat
				postanz = pdb.getUserPostinganz(username);
				
			
				if (postanz == 0)
				{ // wenn nix gepostet gehe weiter !!
					if (tdb.SetNextObject() == false)
						break;
					continue;
				}
				// gehe zur jeder geposteten Seite
				Tracer.WriteTrace(40, "I:Untersuche Thread=" + filedbnam
						+ " usernam=" + username + " usergesthreadpostanz="
						+ postanz);
				for (i = 0; i < postanz; i++)
				{
					// hole die i-te postnummer
					postnummer = pdb.getUserPostnummerIDX(username, i);
					if (postnummer == -1)
						Tracer.WriteTrace(10, "Error:kann user <" + username
								+ "> nicht in <" + filedbnam + "> finden");
					if ((postnummer < lastpostnummer) && (lastpostnummer != 0))
						Tracer.WriteTrace(20,
								"W:BAdurchlaufT sequenz postnummer postnr<"
										+ postnummer + "> lastpostnr<"
										+ lastpostnummer + ">");
					lastpostnummer = postnummer;

					// aus der postnummer läst sich die Webseite berechnen
					// ermittle die NR der Webseite
					// 1..10 ist seite 1, 11..20 ist seite 2

					threadseite = ((postnummer - 1) / 10) + 1;
					Tracer.WriteTrace(40, "I:Betrachte Posting pindex=" + i
							+ " postnummer=" + postnummer + " threadseite="
							+ threadseite);

					if (threadseite != lastthreadseite)
					{ // lade nur wenn neue Seite
						Tracer
								.WriteTrace(40, "lade Threadseite="
										+ threadseite);

						lastthreadseite = threadseite;
						fnam = tdbo.GetSavePageName_genDir(threadseite);

						if (SetNextThread_flag == 1)
						{
							// geht zum nächsten Thread
							SetNextThread_flag = 0;
							break;
						}
						
						BAworkOneThreadPageAbstract(tdb.getAktThreadName(),
								fnam, j, maxthread, i, postanz, tdbo, tdb,middb);

						
					}
				}
				if (tdb.SetNextObject() == false)
					break;
			}
		} else
			// b)gehe durch alle Threads
			for (j = lastposition; j < maxthread; j++)
			{
				ThreadDbObj tdbo = (ThreadDbObj) tdb.GetAktObject();
				//falls thread invalid state!=0 dann gehe weiter
				if(tdbo.getState()!=0)
					SetNextThread_flag=1;
				
				
				if (durchlaufmode == GC.MODE_ONLYBREAKID)
				{
					// durchlaufe nur seiten wo breakid gesetzt ist
					aktBreakid = tdbo.getBreakid();
					if (aktBreakid == 0)
					{
						tdb.SetNextObject();
						continue;
					}
				}
				// durchlaufe für den thread alle webseiten
				int maxpage = tdbo.getPageanz();
				for (i = 1; i <= maxpage; i++)
				{
					tdbo = (ThreadDbObj) tdb.GetAktObject();
					fnam = tdbo.GetSavePageName_genDir(i);

					int tid=tdbo.getThreadid();
					if(tidset_glob!=null)
					{
						if (tdb.SetNextObject() == false)
							break;
						continue;
					}
					if ((fnam == null) || (fnam.contains("null"))
							|| (fnam.equals("")))
					{
						Tracer.WriteTrace(10,
								"Error: threads.db error fnam fehlerhaft tdbo tid<"
										+ tdbo.getThreadid() + "> mid<"
										+ tdbo.getMasterid() + ">");
						break;
					}
					if (SetNextThread_flag == 1)
					{
						// geht zum nächsten Thread
						SetNextThread_flag = 0;
						break;
					}

					if ((FileAccess.FileAvailable(tdbo
							.getThreadDownloadSpeichername(i)
							+ ".gzip") == false))
					{
						// threadseite nicht verfügbar obwohl sie da sein müsste
						// gib warnung aus und versuche sie neu zu laden
						// 
						// a) bei fehler, gehe zum nächsten Thread
						// b) bei erfolg mache mit der nächsten Seite weiter
						Tracer.WriteTrace(20, "W:Threadpage<"
								+ tdbo.getThreadDownloadSpeichername(i)
								+ ".gzip" + "> not available I load it");

						String urlstr = tdbo.GetUrlName(i);
						Wpage_glob.DownloadHtmlPage(urlstr, fnam + ".gzip", 0,
								timeout_glob, 1, 1, 0);

						// im Fehlerfall gehe zur nächsten Seite
						if (FileAccess.FileAvailable(fnam + ".gzip") == false)
							continue;
					}
					if (durchlaufmode == GC.MODE_ONLYAKTIENTHREADS)
					{
						if ((tdbo.getSymbol().length() < 2)
								|| (tdbo.getMasterid() == 0))
						{
							break;
						}
					}
					if (durchlaufmode == GC.MODE_ONLYAKTIENTHREADSKEINEDAX)
					{
						// kein Aktienthread dann gehe weiter
						if ((tdbo.getSymbol().length() < 2)
								|| (tdbo.getMasterid() == 0))
							break;
						// auch keinen dax-thread
						if (tdbo.getSymbol().equalsIgnoreCase("dax") == true)
							break;
					}
					// mon.begin("Eine Threadpage");
					BAworkOneThreadPageAbstract(tdb.getAktThreadName(), fnam,
							j, maxthread, i, maxpage, tdbo, tdb,middb);
					// mon.end(FileAccess.getFilenameEnd(fnam));
				}

				if (tdb.SetNextObject() == false)
					break;
			}
		System.out.println("fertig alle Threads durchlaufen");
	}

}
