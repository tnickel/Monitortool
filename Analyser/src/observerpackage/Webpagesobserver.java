package observerpackage;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import internetPackage.DownloadManager;
import mainPackage.GC;
import objects.AktDbObj;
import objects.ThreadDbObj;
import stores.AktDB;
import stores.ThreadsDB;
import db.DB;
import db.WebInfoObj;

// Diese Observerklasse überwacht Webseiten auf Veränderungen.
// Hierzu wird eine Datenbank benötigt. In dieser Datenbank werden
// die einzelnen Objekte (WebobserverObj) gespeichert. Die Objekte 
// präsentieren einzelne Webseiten
//
// Die Obeserverklasse übernimmt das ganze Handling um einzelne Webseiten zu
// überwachen.
// Es wird also eine ganze Datenbank von Webseiten aufgebaut und gespeichert.
// Auch Werden Funktionen angeboten die feststellen ob sich diese Webseiten geändert haben.
//
// 
// Es folgen einige Überlegungen:
// Was wird zum Überwachen benötigt
// a) Rootpage
// Bsp:http://www.pinksheets.com/pink/quote/quote.jsp?symbol=apio#getInsiderTrans
// Diese Rootpage beinhaltet immer einen variablen Anteil hier in dem
// Beispiel
// "apio"
// b) Speicherort der Webseiten
// Bsp: c:/offline/downloadpages/insider
// c)Soll nur Veränderung überprüft werden, oder soll die ganze Webseite
// auch gespeichert werden ? (Im ersten Schritt nur Veränderung)
// Wird die Webseite überhaupt auf platte abgelegt? oder nur die Kennung ?
// (Im ersten Schritt die Webseite, dient zum Überprüfen)
// d) Hier werden Schlüsselworte angegeben die eine bestimmte Stelle der
// Webseite identifizieren, man muss ja feststellen ob sich
// die seite Verändert hat. Entspricht der Konfiguration
// e)db-Kernspeicher: Dieser Speicher beinhaltet relevante
// Webseiteninformation und das letzte Änderungsdatum
// Beispiel:
// Symbol#status#Ladedatum1#Webstring1#Ladedatum2#Webstring2
// Symbol#status#Ladedatum1#Webstring1#Ladedatum2#Webstring2

public class Webpagesobserver
{
	static private DB webpages = null;
	private String url = null, speicherort = null, kernspeicher = null;
	private String schluesselstr1 = null, schluesselstr2 = null;

	public Webpagesobserver(String url1, String speicherort1,
			String kernspeicher1, String schluesselstr1a, String schluesselstr2a)
	{
		url = url1;
		speicherort = speicherort1;
		kernspeicher = kernspeicher1;
		schluesselstr1 = schluesselstr1a;
		schluesselstr2 = schluesselstr2a;
		// initialsiert die Klasse, baut auch den Kernspeicher auf
		// webpages = new DB(speicherort, kernspeicher);
		// webpages.ReadDB(0);

	}

	private String genUrl(String symbol)
	{
		String urlstring = null;

		return urlstring;
	}

	private int AddWebpage(String Symbol)
	{
		// Nimmt eine neue Webpage in den Kernspeicher auf
		// Schaut nach ob das Symbol schon im Kernspeicher ist
		// Wenn schon vorhanden dann mache nix.
		// Wenn das Symbol noch nicht vorhanden dann nimm dieses Symbol/Webpage
		// auf.
		// Return: -1, wenn symbol schon vorhanden
		// pos>=0, wenn das symbol neu ist

		// aufnahme mit initialen Daten
		WebInfoObj webinfobj = new WebInfoObj(Symbol
				+ "#0#1970-08-14#0#1970-08-14#0");

		int aufflag = webpages.AddObject(webinfobj);
		return (aufflag);
	}

	public boolean BuildWebpagesDB()
	// baut die observe db auf
	// Die DB muss aus irgendwelchen Grunddaten aufgebaut werden
	// Hier in diesem Fall wird die DB mit den Symboldaten aus aktdb.db und
	// threads.db aufgebaut
	{
		int tid = 0, neuaufnahmecounter = 0;
		String symbol = null;
		AktDB aktdb = new AktDB();
		aktdb.ResetDB();

		// holt die Symbole aus aktdb
		for (int i = 0; i < 10000000; i++)
		{
			AktDbObj aktdbobj = (AktDbObj) aktdb.GetObjectIDX(i);

			tid = aktdbobj.getThreadid();
			if (tid < 0)
				break;

			symbol = aktdbobj.getSymbol();

			if (symbol.length() > 2)
			{
				if (AddWebpage(symbol) >0)
					neuaufnahmecounter++;
			} else
				continue;
		}
		webpages.WriteDB();
		System.out.println("Fertig anz obj <" + neuaufnahmecounter
				+ "> aus aktdb in insider.db aufgenommen");

		ThreadsDB tdb = new ThreadsDB();
		neuaufnahmecounter = 0;

		tdb.ResetDB();
		int anz = tdb.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetAktObject();
			symbol = tdbo.getSymbol();
			if (symbol.length() > 2)
			{
				if (AddWebpage(symbol) >0)
					neuaufnahmecounter++;
			}
			tdb.SetNextObject();
		}
		webpages.WriteDB();
		System.out.println("Fertig anz obj <" + neuaufnahmecounter
				+ "> aus threadsdb in insider.db aufgenommen");

		return true;
	}

	public boolean CheckAllWebpagesChanged()
	// prüft nach ob sich die webseiten geändert haben
	// maximal 1 mal pro Tag wird geprüft
	{
		String date;

		date = Tools.get_aktdate_str(null);
		webpages.ResetDB();
		DownloadManager dm = new DownloadManager(GC.MAXLOW);

		// prüft ob directory vorhanden ist
		FileAccess.checkgenDirectory(FileAccess.convsonderz(GC.rootpath
				+ speicherort));

		while (5 == 5)
		{
			WebInfoObj webinfobj = (WebInfoObj) webpages.GetAktObject();
			String symbol = webinfobj.getSymbol();
			// falls heute noch nicht geladen wurde, dann lade
			if (webinfobj.Ladedatum1.equalsIgnoreCase(date) == false)
			{
				dm.DownloadHtmlPage("urlname", GC.rootpath + speicherort + "\\"
						+ symbol + ".html", 0, 5000000, 1, 1, 1);

			}

			// falls das Ende erreicht ist
			if (webpages.SetNextObject() == false)
				break;
		}
		return true;
	}
}
