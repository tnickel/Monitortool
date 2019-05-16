package internetPackage;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.ToolsDyn;
import hilfsklasse.Tracer;

import java.util.Properties;
import java.util.Timer;

import mainPackage.GC;

public class DownloadManager
{
	// static private String htmlfile;
	private static int onlineflag = 99;
	static int maxthreads = 200;
	static String m_syncflag = "Downloadmanager";

	// x Threads maximal
	private static LoadWebpageThread tpointer[] = new LoadWebpageThread[200];
	private static Inf inf = new Inf();
	private ToolsDyn to = new ToolsDyn();

	protected static Timer timer[] = new Timer[200];
	// threadpos muss static sein da diese variable nur einmal vorkommen darf
	protected static int threadpos = 0;

	public DownloadManager(int maxth)
	{
		maxthreads = maxth;
		// System.out.println("construktor DownloadManager");
		// Falls schon initialisiert
		if (onlineflag != 99)
			return;

		inf.setFilename(GC.rootpath + "\\db\\downloadinfo.txt");
		// hier initialisiere den Freethread demon
		// der Freethreaddemon schaut nach ob die threads fertig sind
		// und stoppt dann auch die timer.

		String str = null;
		if (GC.ONLINEMODE == 1)
			onlineflag = 1;
		else
			onlineflag = 0;

		if ((FileAccess.FileAvailable("F:\\set_offlinemode.txt") == true)
				&& (onlineflag == 1))
		{
			Tracer.WriteTrace(10, "Achtung Onlinemode !!!");
			onlineflag = 0;
		}

		// proxy holen
		String fnam = "proxy";
		if (fnam != null)
		{
			System.out.println("debug setze proxy");
			Properties p = System.getProperties();
			p.put("proxySet", "true");
			p.put("proxyHost", "proxy");
			p.put("proxyPort", "8080");
		}
	}

	public void waitEnd()
	{
		int i = 0, anz = 0;
		int sleepcount = 0;
		// wartet bis alle downloads abgeschlossen sind
		while (5 == 5)
		{
			// System.out.println("Sync download end");
			for (i = 0, anz = 0; i < maxthreads; i++)
			{
				// prüft nach ob alle downloads beendet sind
				if (tpointer[i] != null)
				{
					// prüfe ob schon terminiert, wenn ja setze null
					if ((tpointer[i].getState().name().contains("TERMINATED") == true)
							|| (tpointer[i].startzeit.contains("Beendet")))
					{
						Tracer.WriteTrace(20, "Thread Terminated<" + i + "> <"
								+ tpointer[i].getName() + "> startzeit <"
								+ tpointer[i].startzeit + ">");
						tpointer[i] = null;
						timer[i].cancel();
					} else
					{
						// ansonsten läuft wohl noch was
						// System.out.println("tpointer="+tpointer[i].getState().name());
						// wenn status ==1, dann ist der thread mit laden fertig
						// System.out.println("tpointer status="
						// + tpointer[i].status);
						// System.out.println("tpointer
						// getName="+tpointer[i].getName());
						// System.out.println("Startzeit="+tpointer[i].startzeit);
						Tracer.WriteTrace(40, "Thread i<" + i + "> <"
								+ tpointer[i].getName()
								+ ">läuft noch, startzeit <"
								+ tpointer[i].startzeit + ">");

						// Der Thread ist noch nicht gestartet, dann wird er
						// aber noch starten
						if (tpointer[i].startzeit
								.contains("Waiting for first start"))
							anz++;
						// er ist bereits beendet, dann lösche
						else if (tpointer[i].startzeit.contains("Beendet"))
						{
							tpointer[i] = null;
							timer[i].cancel();
						} else
						// er läuft gerade, aber er läuft schon über 10 Minuten,
						// das ist zu lange, dann lösche
						if (to.CheckDateIsOlder(tpointer[i].startzeit, 0, 10, 0) == true)
						{
							tpointer[i] = null;
							timer[i].cancel();
						}
						// Er läuft gerade und die laufzeit ist noch keine 10
						// Minuten
						else
							anz++;
					}

				}
			}
			if (anz == 0)
			{
				// System.out.println("anz=0 nix läd mehr !!!!");
				return;
			} else
			{
				try
				{
					Thread.sleep(1000);
					sleepcount++;
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (sleepcount >= 5)
				{
					sleepcount = 0;
					System.out.println(" waiting for download ending, anz=<"
							+ anz + "> threads are running ");
				}
			}
		}
	}

	public int getfreethread()
	{
		// getfreethread sollte im eigenen Task laufen !!!

		// schaut nach ob noch was frei ist
		// falls was terminiert und nicht freigegeben, dann wird dies
		// freigegeben
		int i = 0;
		// return: threadid falls was gefunden wurde
		// -1: alles belegt
		while (5 == 5)
		{
			for (i = 0; i < maxthreads; i++)
			{

				// falls schon 10 belegt sind dann verzögere 50 ms if(i>10)
				/*
				 * if (i > 10) { try { Thread.sleep(50); } catch
				 * (InterruptedException e) { // TODO // Auto-generated catch
				 * block e.printStackTrace(); } }
				 */

				// falls was freies gefunden wurde dann nimm dies
				if (tpointer[i] == null)
					return i;

				// thread ist schon terminiert, dann setzte thread pointer und
				// timer auf zu sicherheit auf null
				if (tpointer[i].getState().name().contains("TERMINATED") == true)
				{
					tpointer[i] = null;
					timer[i].cancel();
					return i; // was gefunden
				}
			}
			// nix gefunden
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}
	}

	public int DownloadHtmlPage(String urlname, String filename,
			int umbruchflag, int timeout, int singlethreadflag, int forceflag,
			int showflag)
	// wenn das singlethreadflag==1 ist,
	// wird erst die webseite geladen bevor weitergemacht wird
	// falls forceflag = 1 wird die webseite auf jeden Fall geladen, auch wenn
	// sie schon da ist
	// falls showflag==1 dann wird der webseitenname ausgegeben
	{
		// die umlaute umwandeln
		urlname = Tools.EncodeStringUtf8(urlname);
		inf.writezeile(urlname + "#" + filename);

		if (singlethreadflag == 1)
		{ // nur eine webseite laden
			int trys = 5;
			LoadWebpageThread lwt = new LoadWebpageThread(urlname, filename,
					umbruchflag, timeout, 0, null, forceflag, showflag);

			lwt.putOnlineflag(onlineflag);
			while (lwt.LoadWebpage(urlname, filename, umbruchflag, timeout,
					forceflag, showflag) != 0)
			{
				Tracer.WriteTrace(20, "I: webladefehler <" + urlname
						+ "> lade neu versuch<" + (5 - trys) + ">");
				trys--;
				if (trys == 0)
				{
					Tracer.WriteTrace(
							20,
							"Error: Webseite<"
									+ urlname
									+ "> file <"
									+ filename
									+ "> konnte nach 5 ladeversuchen nicht geladen werden ");
					break;
				}
			}
			//setze page als bad, bzw. den thread
			return 88;
		}

		// Es ist kein Singlethreadloadmode dann lade parallel
		// static LoadWebpageThread thread1,thread2,thread3;

		while (5 == 5)
		{
			synchronized (m_syncflag)
			{
				threadpos = getfreethread();

				// wenn nix gefunde dann probiere es in 100ms nocheinmal
				if (threadpos < 0)
					continue;

				// Hole threadpos
				// einen neuen Zeitüberschreitungstimer zuweisen
				timer[threadpos] = new Timer();

				// Den 300sek timer setzten, dieser überwacht das was
				// geladen wurde
				TimeControlTask tc = new TimeControlTask();
				// tc.SetTPointer(tpointer[threadpos], threadpos);
				tc.SetTPointer(tpointer[threadpos], threadpos);
				timer[threadpos].schedule(new TimeControlTask(), 300000);

				// initialisiere den Laderthread
				tpointer[threadpos] = new LoadWebpageThread(urlname, filename,
						umbruchflag, timeout, threadpos, timer[threadpos],
						forceflag, showflag);
				// starte den Laderthread
				tpointer[threadpos].putOnlineflag(onlineflag);
			}
			tpointer[threadpos].start();
			break;
		}
		return (0);
	}

}
