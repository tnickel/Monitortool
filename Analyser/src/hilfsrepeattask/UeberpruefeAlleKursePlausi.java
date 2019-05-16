package hilfsrepeattask;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.ThreadsPostElem;
import internetPackage.DownloadManager;
import kurse.KursValueDB;
import kurse.Kursvalue;
import kurse.LadeKurs;
import mainPackage.GC;
import objects.ThreadDbObj;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import statistikobjekte.Kursbewert;
import stores.MidDB;
import stores.ThreadsDB;
import basis.BAWorkThreads;

public class UeberpruefeAlleKursePlausi extends BAWorkThreads
{
	// Hier wird überprüft ob für alle Aktien die Kursdaten da sind
	// Wenn nicht vorhanden wird dies in einem entsprechenden File geloggt
	String symb_glob = null, lastthreadname_glob = null;
	private Kursbewert kbewert = new Kursbewert(GC.rootpath
			+ "\\db\\reporting\\Kursverfügbarkeit.txt");
	int checkonlyflag_g = 0;
	int threadpos_glob = 0, posmax_glob = 0, kursanzahl_glob = 0,
			minpagepostnr_glob = 0, maxpagepostnr_glob = 0;
	String threadurl_glob = null;
	String pagename_glob = null;
	int tid_glob = 0, lastpostid_glob = 0;
	int last_masterid_glob = 0, lastpostnr_glob = 0;
	String last_symbol_glob = null;

	float prozok_glob = 0, prozokges_glob = 0;
	String threadname_glob = null;
	int threadid_glob = 0, lasttid = 0, threadpageanz_glob = 0;
	String aktthreadnamz_glob = null, lastnam = null;
	Inf inf = new Inf();
	static int loggerstop_glob = 0, loggerstop_datumssequence_glob = 0;
	private float lastkurs_glob = 0;
	// 100% Kursschwankungen sind erlaubt sonst gibt es eine Warnung !!
	final float maxprozabweichung_glob = 100;

	// variablen für den Plausicheck
	static int lastpostidplausi_glob = 0;
	static String lastdateplausi_glob = null;

	// variablen für die graphische Ausgabe
	ProgressBar progressBar_glob = null;
	Display display_glob = null;

	KursValueDB kvdba = null;

	private void clean_glob()
	{
		loggerstop_glob = 0;
		loggerstop_datumssequence_glob = 0;
		lastpostidplausi_glob = 0;
		lastdateplausi_glob = null;
		last_symbol_glob = null;
		lastkurs_glob = 0;
		lastpostnr_glob = 0;
		minpagepostnr_glob = 0;
		maxpagepostnr_glob = 0;
	}

	@Override
	public void BAworkOnePostingAbstract(ThreadsPostElem threadpostelem)
	{

		String kdate = new String(threadpostelem.get_datetime());
		int postid = 0;

		// CHECK: Sequence
		// mache einen postnummerncheck (Die Nummern müssen in der Sequenz
		// passend steigen
		// System.out.println("überprüfe pagename<" + pagename_glob + ">");
		postid = threadpostelem.get_postid();

		if (postid != lastpostidplausi_glob + 1)
		{
			String msg = "Error: postidsequence fehler lastposid<"
					+ lastpostidplausi_glob + "> postid<"
					+ threadpostelem.get_postid() + "> date<" + kdate
					+ "> symb<" + symb_glob + "> tname<" + threadname_glob
					+ "> pagename<" + pagename_glob + ">";
			// fehler, postidsequence verletzt
			Tracer.WriteTrace(20, msg);
			inf.appendzeile(GC.rootpath
					+ "\\db\\reporting\\plausicheckpostid.txt", msg, true);
			lastpostidplausi_glob = threadpostelem.get_postid();
		} else
			// alles ok
			lastpostidplausi_glob = threadpostelem.get_postid();

		// checkt ob die postnummer in der Schranke ist, wenn nein wird das file
		// gelöscht

		if ((postid < minpagepostnr_glob) || (postid > maxpagepostnr_glob))
		{

			String str = "Error:postschranke verletzt postid<" + postid
					+ "> minpost<" + minpagepostnr_glob + "> maxpost<"
					+ maxpagepostnr_glob + "> lösche file fnam<"
					+ pagename_glob + ">";
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath + "\\db\\reporting\\postschranke.txt");
			inf.writezeile(str);
			Tracer.WriteTrace(20, str);
			FileAccess.FileDelete(pagename_glob + ".gzip",0);
			System.out.println("del file");
			return;
		}

		// CHECK: Datumsfolge (3 Monate)
		// mache einen Datumscheck, das Datum darf maximal um 24Monate
		// abweichen,
		// d.h. jede 24 Monate muss mindestens
		// ein Posting erfolgen sonst ist was faul

		// a)erster vergleich
		if (lastdateplausi_glob == null)
			lastdateplausi_glob = kdate;

		int zeitdiff_tage = Tools
				.zeitdifferenz_tage(kdate, lastdateplausi_glob);

		// b)datum ist ok
		if (zeitdiff_tage < 720)
			lastdateplausi_glob = kdate;
		else
		// c) Fehler datum ist zu alt !!
		{
			String msg = "Error: Datumssequence verletzt >720 Tage pagename<"
					+ pagename_glob + "> postid<" + threadpostelem.get_postid()
					+ "> date<" + kdate + ">  lastkdate<" + lastdateplausi_glob
					+ "> symb<" + symb_glob + "> tname<" + threadname_glob
					+ "> pagename<" + pagename_glob + ">";

			Inf inf720 = new Inf();
			inf720.setFilename(GC.rootpath
					+ "\\db\\reporting\\720tageCheckKursfehler.txt");
			inf720.writezeile(msg);
			if (loggerstop_datumssequence_glob == 0)
			{
				Tracer.WriteTrace(20, msg);
				loggerstop_datumssequence_glob = 1;

			} else
				Tracer.WriteTrace(20, msg);

			inf.appendzeile(GC.rootpath
					+ "\\db\\reporting\\plausicheckdatumsfolge.txt", msg, true);
		}
		// check Kursstabilität diff(lastkurs,kurs) <50 % (maximal 50%
		// Kursschwankung sonst ist was faul)

		// check postingsequence

		Kursvalue k1obj = null;

		k1obj = kvdba.holeKurswert(kdate, 0);
		if ((k1obj == null) || (k1obj.isValidflag() == false))
		{
			// bad
			kbewert.Bewerte(0, 1, 0);
		} else if (k1obj.isValidflag() == true)
		{
			// ok
			if (k1obj.getKv() > 0)
				kbewert.Bewerte(1, 0, 0);
			// neutral
			else
				kbewert.Bewerte(0, 0, 1);
		} else
			Tracer.WriteTrace(10, "Error: internal xxx");

		float maxdifferabs = Math.abs((lastkurs_glob / 100)
				* maxprozabweichung_glob);

		// Kurs plausicheck,die maximale Kursschwankung darf nux x % betragen !

		if ((Math.abs(k1obj.getKv() - lastkurs_glob) > maxdifferabs)
				&& ((lastkurs_glob != 0) && (k1obj.getKv() != 0)))
		{
			String errormessage = "Schwankung sy<" + symb_glob + "> tname <"
					+ threadname_glob + "> tid<" + tid_glob + "> kurs<"
					+ k1obj.getKv() + "> lastkurs<" + lastkurs_glob + "> max%<"
					+ maxprozabweichung_glob + "> Range <"
					+ (lastkurs_glob - maxdifferabs) + ">-<"
					+ (lastkurs_glob + maxdifferabs) + ">";

			Tracer.WriteTrace(20, errormessage);

			inf.appendzeile(GC.rootpath + "\\db\\reporting\\kursfehler.txt",
					errormessage, true);
		} else
		{
			int id = threadpostelem.get_postid();
			if (id % 5000 == 0)
				System.out.println("...Info: ID<" + id + ">tname<"
						+ threadname_glob + "> symb<" + symb_glob + "> date<"
						+ kdate + "> kurs<" + k1obj.getKv() + ">");

		}
		lastkurs_glob = k1obj.getKv();
	}

	@Override
	public void BAworkOneThreadPageAbstract(String Threadname, String pagename,
			int pos, int posmax, int pcount, int pcountmax, ThreadDbObj tdbo_x,
			ThreadsDB tdb,MidDB middb)
	{
		threadname_glob = new String(Threadname);
		symb_glob = tdbo_x.getSymbol();
		pagename_glob = pagename;
		String pref = "";

		if (threadname_glob.equals(lastthreadname_glob) == false)
		{
			if (progressBar_glob != null)
				progressBar_glob.setSelection(pos);
			if (display_glob != null)
				display_glob.readAndDispatch();
			// gib die Kursverfügbarkeit aus
			kbewert.GibZwischenstandAus(last_symbol_glob, lastthreadname_glob,
					last_masterid_glob);
			clean_glob();

			// Hier wird ein neuer Thread betrachtet !!!!
			kvdba = new KursValueDB(symb_glob, checkonlyflag_g,tdb);
			kursanzahl_glob = kvdba.calcAnzKurswerte();

			Tracer.WriteTrace(20, "I:<" + pos + "|" + posmax
					+ ">Ueberpruefe Kurswerte threadname <" + threadname_glob
					+ "> symbol<" + symb_glob + ">");

			lastthreadname_glob = threadname_glob;
			lasttid = tid_glob;
			last_masterid_glob = tdbo_x.getMasterid();
			last_symbol_glob = symb_glob;
			// lösche lastpostid =0 da neuer Thread
			lastpostid_glob = 0;
		}
		minpagepostnr_glob = ((pcount - 1) * 10) + 1;
		maxpagepostnr_glob = ((pcount - 1) * 10) + 11;
		BAsetHtml(pagename + ".gzip");
		BAdurchlaufHtml(middb,tdb,tdbo_x, pcount);
	}

	public void ladeNurKurs()
	{
		LadeKurs kurs = new LadeKurs();
		DownloadManager dm = new DownloadManager(2);
		String symb = "APIA";

		// ".F", ""
		String boerse = ".OB";
		kurs.LoadYahooKurs(symb, symb, boerse, dm);
	}

	public void Start(int checkonlyflag, ProgressBar progressBar1, Display dis)
	{
		// falls das checkonlyflag gesetzt ist werden nur die Kurse geprüft und
		// nix nachgeladen
		checkonlyflag_g = checkonlyflag;
		progressBar_glob = progressBar1;
		progressBar_glob.setMinimum(0);
		progressBar_glob.setMaximum(BAgetanzElements());
		display_glob = dis;
		BAsetDurchlaufmode(GC.MODE_ONLYAKTIENTHREADSKEINEDAX, "");
		BAsetLastposition(0);
		kbewert.SetStartindex(0);
		kbewert.SetNeuerThread();
		BAdurchlaufThreads();

		// alles fertig

	}
}
