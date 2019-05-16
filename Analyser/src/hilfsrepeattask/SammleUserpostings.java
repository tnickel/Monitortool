package hilfsrepeattask;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Sys;
import hilfsklasse.TDate;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.Htmlcompress;
import html.ThreadsPostElem;

import java.util.HashSet;

import mainPackage.GC;
import objects.Htmlpage;
import objects.ThreadDbObj;
import objects.UserDbObj;
import ranking.UserStatistik;
import stores.MidDB;
import stores.PostDBx;
import stores.ThreadsDB;
import stores.UserDB;
import basis.BAWorkThreads;

public class SammleUserpostings extends BAWorkThreads
{
	// Hier werden alle Postings aller user für alle Threads durchlaufen
	UserDB udb = null;
	PostDBx pdb = new PostDBx();

	String username = null;
	String postfilename_glob = null;
	String postverzeichniss_glob = null;
	String aktthreadnam = null, lastnam = null;
	String Threadnametext = null;
	String threadurl = null;
	int threadid = 0, threadpageanz = 0;
	int firstpostingflag = 1;
	String postinfname = null;
	int tid = 0, postid = 0, lastpostid = 0, breakindex = 0, aktpostanz = 0;

	String lastcreatedatetime = null, aktdate = null;
	String startzeit = null;

	Htmlcompress hw = new Htmlcompress(super.tdb);
	UserDbObj userdbobj = null;
	ThreadDbObj tdbo_glob = null;
	Htmlpage htmlZahlen = null, htmlTexte = null, htmlUserinfo = null;
	UserStatistik ustat = null;

	private String CalcColor(UserDbObj userdbobj, int tid)
	{
		String color = null;

		// bestimme die farbe für die überschrift
		// falls tid!=0 dann prüfe ob die tid passt

		if (tid != 0)
			if (userdbobj.getNeusteThreadId() != tid)
				return "green";

		if (userdbobj.getNeusteThreadTage() <= 5)
			color = "red";
		else
			color = "green";
		return color;
	}

	@Override
	public void BAworkOnePostingAbstract(ThreadsPostElem obj)
	{
		// hier wird ein posting verarbeitet
		// falls username in der page, dann Sammle die Info
		postid = obj.get_postid();
		String pusername = obj.get_username();
		String postdatetime = obj.get_datetime();
		String zeile = null;

		Tracer.WriteTrace(40, "Postingvergl. erw_username<" + username
				+ "> postusername<" + pusername + "> vgldate<"
				+ lastcreatedatetime + "> postdate<" + postdatetime + ">");

		if ( // falls der username passt
		((pusername.equalsIgnoreCase(username)) == true) &&
		// und das posting neuer oder gleich ist als das letzte gemessene
				((Tools.datum_ist_aelter_gleich(startzeit, postdatetime) == true)))
		{
			if (aktthreadnam.equals(lastnam) == false)
			{
				// Neuer Thread, also das erste Posting in diesem Thread
				lastpostid = 0; // da neuer thread
				lastnam = aktthreadnam;
				System.out.println("Untersuche Thread <" + aktthreadnam + ">");

				// prüft nach wieviele Tage der user den Thread schon kennt
				String aufnahmedatum = userdbobj
						.GetAufnahmedatumFuerThreadid(threadid);

				// falls das aufnahmedatum ==null ist dann ist der thread sehr
				// alt und
				// nicht in <username>.db verzeichnet
				// Setze Aufnahmedatum als 11.11.55
				if (aufnahmedatum == null)
					aufnahmedatum = "11.11.55";

				TDate tdaufnahme = new TDate(aufnahmedatum);
				TDate tdaktdate = new TDate(aktdate.substring(0, aktdate
						.indexOf(" ")));
				int tagediff = tdaufnahme.Tdiff(tdaktdate, "t");

				// falls dieser thread neuer ist, dann merke dies
				if (tagediff < userdbobj.getNeusteThreadTage())
				{
					userdbobj.setNeusteThreadTage(tagediff);
					userdbobj.setNeusteThreadId(threadid);
				}
				// der Thread wird gestartet
				htmlZahlen
						.Append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
				if (firstpostingflag == 1)
				{
					htmlZahlen.AppendBalken("red", 5);
					firstpostingflag = 0;
					htmlZahlen
							.Append("<br> <font color=\"green  \"> Betrachtungszeitraum= ("
									+ startzeit
									+ " ->"
									+ aktdate
									+ " ) </font>");
					// fuegt einen link ein mit dem man für den user sämtliche
					// postings sehen kann

				}
				htmlZahlen.Append(" ");
				htmlZahlen.Append(" ");

				if (tdbo_glob.getMasterid() != 0)
					Threadnametext = aktthreadnam;
				else
					// falls nur diskussionsrunde dann ist keine masterid vorh.
				{
					Threadnametext = threadurl;
					
				}
				htmlZahlen.Append("<br> <br><font color=\""
						+ CalcColor(userdbobj, threadid)
						+ "\"> Untersuche Thread=" + Threadnametext
						+ "   ThreadRank<" + tdbo_glob.getThreadrankingstring()
						+ "> <br>" + ustat.printUserliste(threadid, username)
						+ "</font><br>");

				// bilde Kopfzeile in der userpostingzusammenstellung damit
				// besser lesbar
				// setze utf-8 damit umlaute besser angezeigt werden
				htmlTexte
						.Append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
				htmlTexte.AppendBalken("red", 10);

				htmlTexte.Append("<BLOCKQUOTE> <H1><font color=\"green\"> "
						+ Threadnametext + "</font></H1> <H3><font color=\""
						+ CalcColor(userdbobj, threadid) + "\">" + threadurl
						+ ": bekannt seit mind. Tagen:" + tagediff
						+ " pageanz:" + threadpageanz
						+ " </font></H3><font color=\""
						+ CalcColor(userdbobj, threadid) + "\">" + username
						+ "  tid:" + tdbo_glob.getThreadid() + " mid:"
						+ tdbo_glob.getMasterid() + "</font></BLOCKQUOTE>");

				// fügt die Kursübersicht ein, daychart, monatschart,
				// jahreschart
				tdbo_glob.AppendCopyIconTags(htmlTexte, postverzeichniss_glob);
				// fügt einen link für aktuelle postingübersicht ein
				htmlTexte.Append("<a href=\"../../..//db/threaddata/"
						+ tdbo_glob.getThreadname() + "_" + threadid
						+ ".db\" target=\"Postingfenster\">Thread '"
						+ tdbo_glob.getThreadname()
						+ "' Postingübersicht  </a>");
				htmlTexte
						.Append("<a href=\"../../..//db/seitenzaehler/"
								+ tdbo_glob.getThreadname()
								+ ".db\" target=\"Postingfenster\"> Seitenzaehler </a><br>");
			}
			int postid = obj.get_postid();

			// überprüfe ob postid doppelt ist
			if (postid > lastpostid)
			{
				lastpostid = postid;
				Tracer.WriteTrace(40, "Userposting wird registriert postid<"
						+ postid + ">");

				// drucke nur aus wenn die postid sich geändert hat und
				// aufsteigend ist
				htmlZahlen.Append(" " + postid + ",");
				System.out.print(" " + postid + ",");

				// System.out.println("postline="+obj.get_postingzeile());
				zeile = obj.get_postingzeile();
				htmlTexte.Append("<i>" + hw.cleanHtmlPostingLine(zeile));

				// der link zu dem Thread wird hinter jedem posting angefügt
				htmlTexte.AppendThreadLink(tdbo_glob, postid);
			} else
				Tracer.WriteTrace(20, "W: Postid doppelt lastpostid<"
						+ lastpostid + "> postid<" + postid + ">");
		}
	}

	@Override
	public void BAworkOneThreadPageAbstract(String Threadname, String pagename,
			int count, int max, int pcount, int maxpagecount,
			ThreadDbObj tdbo_x, ThreadsDB tdb,MidDB middb)
	{
		// hier wird ein Thread verarbeitet
		// Bei diesem Durchlaufmode ist fnam=threadname
		aktthreadnam = Threadname;
		tdbo_glob = tdbo_x;
		threadurl = tdbo_glob.getUrlaktname();
		threadid = tdbo_glob.getThreadid();
		threadpageanz = tdbo_glob.getPageanz();

		BAsetHtml(pagename + ".gzip");
		BAdurchlaufHtml(middb,tdb,tdbo_glob, pcount);

	}

	public void StartSammleSchreibeUserpostings(UserDB tudb, UserDbObj uobjx,
			String startz, String jetztzeit1, int postcount,
			UserStatistik ustatx,String pref,HashSet tidset)
	{
		
		
		// hier werden alle threads verarbeitet, unam ist der username dessen
		// postings beobachtet werden
		// uname=username
		// lldat=die neuen Postings müssen neuer als dieses datum sein
		// postcount= index des Files
		// return: den Verzeichnisnamen wo alles reingeschrieben wurde
		// verzpref: Verzeichnissprefix
		firstpostingflag = 1;
		udb = tudb;
		userdbobj = uobjx;
		username = userdbobj.get_username();
		ustat = ustatx;
		// set global lastloaddatetime
		// we will count all postings who are newer than this lastloaddatetime
		lastcreatedatetime = startzeit;
		// will be used for info in the overview file
		aktdate = jetztzeit1;
		startzeit = startz;
		String verzdir = startzeit.substring(0, startzeit.indexOf(" "));

		// generate filename who contains the postings
		postfilename_glob = GC.rootpath + "\\info\\"+pref+"\\userpostings\\" + verzdir
				+ "\\" + username + "_postx" + postcount + ".html";
		postverzeichniss_glob = GC.rootpath + "\\info\\"+pref+"\\userpostings\\"
				+ verzdir;

		htmlTexte = new Htmlpage(postfilename_glob);

		// falls noch altes zeug da ist dann weg, durch den index sollte ja was
		// neueres generiert werden.
		FileAccess.checkgenDirectory(GC.rootpath + "\\info\\"+pref+"\\userpostings");
		FileAccess.checkgenDirectory(GC.rootpath + "\\info\\"+pref+"\\userpostings\\"
				+ verzdir);
		if (FileAccess.FileAvailable(postfilename_glob) == true)
			FileAccess.FileDelete(postfilename_glob,0);

		// generate filename who contains the postinfo
		postinfname = GC.rootpath + "\\info\\"+pref+"\\userpostings\\" + verzdir + "\\"
				+ username + "_infx" + postcount + ".html";
		htmlZahlen = new Htmlpage(postinfname);
		if (FileAccess.FileAvailable(postinfname) == true)
			FileAccess.FileDelete(postinfname,0);

		// summenfile löschen
		String postsumfilename = GC.rootpath + "\\info\\"+pref+"\\userpostings\\"
				+ verzdir + "\\" + username + "_post" + postcount + ".html";
		if (FileAccess.FileAvailable(postsumfilename) == true)
			FileAccess.FileDelete(postsumfilename,0);

		BAsetDurchlaufmode(GC.MODE_ONLY_THREAPAGES_WITH_USERNAME, username);
		BAsetTidMenge(tidset);
		BAdurchlaufThreads();
		
		String hostname = Sys.getHostname();
		// prüft ob ein handdata/userinfo vorhanden ist
		String userhanddata = GC.rootpath + "\\handdata\\Rechnername "+hostname+"\\userinfo\\" + username
				+ ".txt";
		if ((FileAccess.FileAvailable(userhanddata)) == false)
		{
			// falls das handdata nicht da ist, dann wird es angelegt
			Inf inf = new Inf();
			inf.appendzeile(userhanddata, "", false);
		}

		// füge inf über postfile zusammen, und lösche die einzelfiles
		if ((FileAccess.FileAvailable(postinfname))
				&& (FileAccess.FileAvailable(postfilename_glob)))
			FileAccess.MatchFiles(postinfname, postfilename_glob,
					postsumfilename, true);

		if ((FileAccess.FileAvailable(userhanddata))
				&& (FileAccess.FileAvailable(postsumfilename)))
			FileAccess.ConcatFiles(userhanddata, postsumfilename);

		String tmpfile = GC.rootpath + "\\tmp\\tmpuser.txt";
		htmlUserinfo = new Htmlpage(tmpfile);
		htmlUserinfo
				.Append("<BLOCKQUOTE>  <H1><font color=\"+CalcColor(userdbobj)+\">"
						+ username
						+ " regDatum:"
						+ userdbobj.getRegistriert()
						+ " Antworten:"
						+ userdbobj.getAntworten()
						+ " RP:"
						+ userdbobj.getRangpoints()
						+ " Rang:"
						+ userdbobj.getRang() + " </font></BLOCKQUOTE>");
		htmlUserinfo.Append("<a href=\"../../..//db/postingliste/" + username
				+ ".db\" target=\"Postingfenster\"> Postingliste </a><br>");
		// Baue link zum virtual Konto auf
		// Beispiel:
		// <a
		// href="../../..//db/UserThreadVirtualKonto/Einzelgewinne/no_brainer.txt"
		// target="Postingfenster"> Einzelgewinne</a><br>
		htmlUserinfo
				.Append("<a href=\"../../..//db/UserThreadVirtualKonto/Einzelgewinne/"
						+ username
						+ ".txt\" target=\"Postingfenster\"> Einzelgwinne </a><br>");

		htmlUserinfo
				.Append("<a href=\"../../..//db/UserThreadVirtualKonto/Summengewinne/"
						+ username
						+ ".txt\" target=\"Postingfenster\"> Summengewinne </a><br>");

		htmlUserinfo
				.Append("<a href=\"../../..//db/UserThreadVirtualKonto/Summengewinnedb/"
						+ username
						+ ".db\" target=\"Postingfenster\"> SummengewinneDB </a><br>");

		htmlUserinfo.Append("Rankingpos=" + userdbobj.getRang() + " points="
				+ userdbobj.getRangpoints() + "<br>");
		htmlUserinfo.Append("Rankinginfo=" + userdbobj.getRankinginfostring()
				+ "<br>");
		if (FileAccess.FileAvailable(postsumfilename))
			FileAccess.ConcatFiles(tmpfile, postsumfilename);

		FileAccess.FileDelete(postinfname,0);
		FileAccess.FileDelete(postfilename_glob,0);

	}
}