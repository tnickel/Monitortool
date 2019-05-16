package objects;

import hilfsklasse.DateExcecption;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import hilfsrepeattask.SammleUserpostings;
import html.ThreadsPostElem;

import java.util.HashSet;

import mainPackage.GC;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import ranking.Rangparameter;
import ranking.UserStatistik;
import stores.AktDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;

public class InfoUserpost
{
	private String BerechneAktTimeMinus1(ControllObj co)
	{
		// aktuelle zeit minus 1 Stunde
		String akttime1 = null;

		try
		{
			// ziehe eine stunde ab, da die analyse vor ca 1h begonnen wurde
			akttime1 = Tools.subTimeHours(Tools.get_aktdatetime_str(), 1);
			// add durration to the time

		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return akttime1;
	}

	private String CalcSetNewTimes(ControllObj co, String akttime1)
	{
		// die last creationtime wird zurückgegeben

		String lastcreatetime = co.getLastCreationDirTime();
		int postcounter = co.getPostcount();
		int durration = co.getDurration();
		// aktuelle zeit minus 1 Stunde

		// lctd = lastcreationtime+duration
		// also die Zeit bis ein neues Verzeichniss entstehen würde
		String lctd = null;
		try
		{
			lctd = Tools.addTimeHours(co.getLastCreationDirTime(), durration);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// falls das konfigurierte intervall überschritten is
		if (Tools.datum_ist_aelter_gleich(lctd, akttime1) == true)
		{
			// Falls AktuelleZeit > lastCreationTime+Duration
			// Dann erhöhe lastCreationTime um die durration
			// Setze lastCreationTime auf AktuelleZeit und erhöhe den
			// Postcounter

			Tracer.WriteTrace(20, "I:das Zeitintervall von <" + durration
					+ "> wurde ueberschritten lasttime <" + lctd
					+ ">setze neue lastcreatetime <" + akttime1 + ">");
			co.setPostcount(postcounter + 1);
			co.setLastCreationDirTime(akttime1);
			co.Write();
			return co.getLastAnalysingTime();
		}
		return lastcreatetime;
	}

	private String CalcPostverzeichniss(String lastcreatetime,String pref)
	{
		String lastcreatefile = lastcreatetime.substring(0, lastcreatetime
				.indexOf(" "));
		String postverzeichniss = GC.rootpath + "\\info\\"+pref+"\\userpostings\\"
				+ lastcreatefile;
		return postverzeichniss;
	}

	private void GenKursiconverzeichniss(String path)
	{
		FileAccess.checkgenDirectory(path + "\\kursicons");
	}

	private void BaueUserLinksAuf(Htmlpage hpage, UserDB udb, int userpos)
	{
		String rotfarbeb = "", rotfarbee = "";
		String filename = null;

		while ((filename = FileAccess.holeFileSystemName()) != null)
		{
			rotfarbeb = "";
			rotfarbee = "";

			if (filename.contains("_post") == false)
			{
				continue;
			}

			String username = filename.substring(0, filename.indexOf("_post"));
			if ((userpos = udb.checkUsername(username)) >= 0)
			{
				UserDbObj udbo = new UserDbObj();
				udbo = (UserDbObj) udb.GetObjectIDX(userpos);
				if (udbo.getNeusteThreadTage() <= 5)
				{
					rotfarbeb = "<font color=\"red\">";
					rotfarbee = "</font>";
				}

				// stelle filename rot dar falls bekannt seit tagen < 5
				hpage.Append("<p><a href=\"" + filename + "\""
						+ "title=\"Hier kann ein Text stehen\""
						+ " target=\"Postingfenster\"><b>" + rotfarbeb
						+ filename.substring(0, filename.indexOf("_post"))
						+ "(" + udbo.getRang() + ")" + rotfarbee + "</b></a>");
			} else
				Tracer
						.WriteTrace(
								20,
								"Warning username<"
										+ username
										+ "> nicht in user.db, user wird nicht in übersicht erscheinen !!");
			// hpage.AppendBalken("red", 2);
		}

	}

	private void BaueThreadLinksAuf(Htmlpage hpage, UserDB udb, int userpos)
	{
		String filename = null;
		FileAccess.holeFileSystemNameReset();
		while ((filename = FileAccess.holeFileSystemName()) != null)
		{
			if (filename.contains("_thread") == false)
				continue;

			// stelle filename rot dar falls bekannt seit tagen < 5
			hpage.Append("<p><a href=\"" + filename + "\""
					+ " target=\"Postingfenster\"><b>"
					+ Tools.cutStringlaenge(filename, 18) + "</b></a>");
		}
	}

	private void FuegeNeueUserInThreadsAn(Htmlpage hpage, UserDB udb,
			int userpos)
	{
		// <a href="..\..\..\db\NeueUserInThreads.txt" target="Postingfenster">
		// <b>Neue User </b> </a>
		hpage
				.Append("<p><a href=\"NeueUserInThreads.txt\" target=\"Postingfenster\"> <b>Neue User </b> </a>");

	}

	private void FuegeRankinglisteAn(Htmlpage hpage, UserDB udb, int userpos)
	{
		hpage
				.Append("<p><a href=\"..\\..\\..\\db\\Rankingliste.txt\" target=\"Postingfenster\"> <b>Rankingliste </b> </a>");
	}

	private void FuegeSlideruebersichtAn(Htmlpage hpage)
	{
		hpage
				.Append("<p><a href=\"UserThreadVirtualKonto\\slideruebersicht.html\" target=\"Postingfenster\"> <b>Slideruebersicht </b> </a>");
	}

	private void baueInfoseite(UserDB udb, String dir)
	{
		// baue das kopf-file auf
		if (FileAccess.FileAvailable(dir + "\\info.html"))
			FileAccess.FileDelete(dir + "\\info.html",0);
		FileAccess.initFileSystemList(dir, 1);

		String masterfile = dir + "\\info.html";
		Htmlpage hpage = new Htmlpage(masterfile);

		hpage
				.Append("	<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
		hpage.Append(" \"http://www.w3.org/TR/html4/loose.dtd\">");
		hpage.Append("<frameset cols=\"250,*\">");
		hpage.Append(" <frame src=\"userlinks.html\" name=\"links\">");
		hpage.Append(" <frame src=\"startseite.htm\" name=\"Postingfenster\">");
		hpage.Append("<noframes>");
		hpage.Append(" <body>");
		hpage.Append("<p>Alternativ-Inhalt (Zusammenfassung, Sitemap ...)</p>");
		hpage.Append("</body>");
		hpage.Append("</noframes>");
		hpage.Append("</frameset>");
	}

	private void baueUserlinks(UserDB udb, String dir)
	{
		int userpos = 0;

		String filename = null;
		// erstelle Liste aller Files im Verzeichnis

		if (FileAccess.FileAvailable(dir + "\\userlinks.html"))
			FileAccess.FileDelete(dir + "\\userlinks.html",0);
		FileAccess.initFileSystemList(dir, 1);

		String masterfile = dir + "\\userlinks.html";
		Htmlpage hpage = new Htmlpage(masterfile);

		hpage
				.Append("	<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
		hpage.Append(" \"http://www.w3.org/TR/html4/loose.dtd\">");
		hpage.Append("<html>");
		hpage.Append("<head>");
		hpage.Append("<p>");

		// hier wird für jeden User der spezielle infolink gesetzt
		BaueUserLinksAuf(hpage, udb, userpos);
		// hier wird für jeden Thread der spezielle infolink gesetzt
		BaueThreadLinksAuf(hpage, udb, userpos);

		if (FileAccess.FileAvailable(GC.rootpath
				+ "\\db\\NeueUserInThreads.txt"))
		{
			if (FileAccess.FileAvailable(dir + "\\NeueUserInThreads.txt"))
			{
				FileAccess.ConcatFiles(GC.rootpath
						+ "\\db\\NeueUserInThreads.txt", dir
						+ "\\NeueUserInThreads.txt");
			} else
			{
				FileAccess.copyFile(
						GC.rootpath + "\\db\\NeueUserInThreads.txt", dir
								+ "\\NeueUserInThreads.txt");
			}
			// Baue links für die neuen user auf (NeueUserInThreads.txt=
			FileAccess.FileDelete(GC.rootpath + "\\db\\NeueUserInThreads.txt",0);
		}
		//
		FuegeNeueUserInThreadsAn(hpage, udb, userpos);
		FuegeRankinglisteAn(hpage, udb, userpos);
		FuegeSlideruebersichtAn(hpage);
		hpage.Append("</p>");
		hpage.Append("</body>");
		hpage.Append("</html>");
	}

	public boolean SammleUserpostings(UserDB udb, AktDB aktdb,
			int alleuserflag, String observefile, String startzeit,
			String akttime1, int postcounter, UserStatistik ustat,
			ProgressBar pb, Text text, Display dis,String pref,HashSet tidset,String rangfile)
	{
		// Sucht alle postings für einen User, und stellt diese Information in
		// info/userpostings/<username>.postings zur Verfügung.
		// Bei der Suche wird erst eine Postdb in db/threaddata erstellt
		// Dann wird für jeden Thread die postliste geladen und geschaut ob der
		// User
		// dort gepostet hat.
		// Wenn ein User dort gepostet hat wird diese Info in
		// info/userpostings/<username>. abgelegt
		// alluserflag 0; dann nur die zu beobachtenden user
		// alluserflag 1; dann alle user

		UserGewStrategieDB2 ugewinnedb = new UserGewStrategieDB2(
				"UserThreadVirtualKonto\\Summengewinnedb");
		UserDbObj userobj = new UserDbObj();
		int maxuser = 0;
		SammleUserpostings samuser = new SammleUserpostings();
		Rangparameter rp = new Rangparameter(rangfile);
		String username = null;

		if(pb!=null)
		  pb.setMaximum(20);
		udb.ResetDB(-1);
		if (alleuserflag == 0)
			maxuser = udb.SetDurchlaufmode(GC.MODE_OBSERVE);
		else
			maxuser = udb.SetDurchlaufmode(GC.MODE_ALL);
		int i = 1;

		while ((userobj = (UserDbObj) udb.GetNextObject()) != null)
		{
			if(pb!=null)
			pb.setSelection(i);
			if(dis!=null)
				Swttool.wupdate(dis);
		
			// list die threadpostingliste für den User ein
			userobj.ReadUserInfoListeCalRank(ugewinnedb, aktdb, 0,rp);
			username = userobj.get_username();
			String msg = "untersuche user<" + i + "|" + maxuser + "><"
					+ username + "> von<" + startzeit + "> bis<" + akttime1
					+ ">";
			if (text!=null)
				text.setMessage(msg);
			if(dis!=null)
				Swttool.wupdate(dis);
		
			System.out.println(msg);

			samuser.StartSammleSchreibeUserpostings(udb, userobj, startzeit,
					akttime1, postcounter, ustat, pref, tidset);
			userobj.DeleteUserPostingListeMem();
			i++;
		}
		return true;
	}

	private Htmlpage baueHeaderpage(String lastcreatetime, ThreadDbObj tdbo,
			int postcount,String pref)
	{
		/*
		 * String threadpostfilename = GC.rootpath + "\\info\\userpostings\\" +
		 * lastcreatetime.substring(0, lastcreatetime.indexOf(" ")) + "\\" +
		 * threadid + "_thread_" + postcount + ".html";
		 */
		String tnam = Tools.cutStringlaenge(tdbo.getThreadname(), 18);

		String threadpostfilename = GC.rootpath + "\\info\\"+pref+"\\userpostings\\"
				+ lastcreatetime.substring(0, lastcreatetime.indexOf(" "))
				+ "\\" + tnam + "_thread" + tdbo.getThreadid() + ".html";

		Htmlpage htmlpage = new Htmlpage(threadpostfilename);

		if (FileAccess.FileAvailable(threadpostfilename) == true)
			FileAccess.FileDelete(threadpostfilename,0);
		return htmlpage;
	}

	private void calcPostinfo(Htmlpage htmlpage, int regtage, String unam,
			String regstr, ThreadsPostElem einpost)
	{
		// malt das postinfo in der entsprechenden farbe
		if (regtage > 1500)
			htmlpage.Append("<font color=\"green\">" + einpost.get_datetime()
					+ ":" + unam + ":" + regstr + "</font><br>");
		else if (regtage > 360)
			htmlpage.Append(einpost.get_datetime() + ":" + unam + ":" + regstr
					+ "<br>");
		else if (regtage > 30)
			htmlpage.Append("<font color=\"orange\">" + einpost.get_datetime()
					+ ":" + unam + ":" + regstr + "</font><br>");
		else
			htmlpage.Append("<font color=\"red\">" + einpost.get_datetime()
					+ ":" + unam + ":" + regstr + "</font><br>");
	}

	private Htmlpage BauePostinguebersichtauf1(String startzeit, int threadid,
			int postcount, Htmlpage htmlpage, ThreadDbObj tdbo, UserDB udb,String pref)
	{
		// Hier wird der Header aufgebaut
		ThreadsPostElem einpost = null;
		String regstr = null;
		int anzpostings = 0, regtage = 0;
		int sumregtage = 0;
		float durchtage = 0;
		// erstes posting
		einpost = tdbo.GetNextPosting(startzeit, true);
		if (einpost == null)
			return null;// nix neues
		else
		{
			// baue den header auf
			htmlpage = baueHeaderpage(startzeit, tdbo, postcount,pref);
			// hier werden die kursicons eingefügt
			tdbo.AppendCopyIconTags(htmlpage, GC.rootpath
					+ "\\info\\"+pref+"\\userpostings\\"
					+ startzeit.substring(0, startzeit.indexOf(" ")));
		}

		while (5 == 5)
		{
			String unam = einpost.get_username();
			UserDbObj udbo = udb.getUserobj(unam);
			if (udbo == null)
			{ // der Username ist nicht bekannt, dann nimm ihn auf
				Tracer.WriteTrace(20, "Warning username<" + unam
						+ "> nicht in userstore.db, username wird aufgenommen");
				udb.AddNewUser(unam);
				udbo = udb.getUserobj(unam);
				if (udbo == null)
					Tracer
							.WriteTrace(
									20,
									"Error username<"
											+ unam
											+ "> nicht in userstore.db, Aufnahmeversuch fehlgeschlagen");
				System.out.println("fehler");
				einpost = tdbo.GetNextPosting(startzeit, false);
				if (einpost == null)
					break;
				else
					continue;
			}

			// es werden nur korrekte user betrachtet
			if (udbo.getState() == 0)
			{
				regstr = udbo.getRegistriert();
				if (Tools.isDate(regstr) == false)
				{ // regstr fehlerhaft, versuche zu korrigieren
					udb.AktuallisiereEinenUserSchnell(unam);
					udbo = udb.getUserobj(unam);
					regstr = udbo.getRegistriert();
				}

				if (Tools.isDate(regstr) == true)
				{
					// regstring ist jetzt korrekt
					anzpostings++;
					regtage = Tools.zeitdifferenz_tage(Tools
							.get_aktdatetime_str(), regstr);
					sumregtage = sumregtage + regtage;

				} else
				// regstring konnte nicht korrigiert werden
				{
					Tracer.WriteTrace(20, "Warning: User <" + unam
							+ "> konnte nicht aktuallisert werden, regstr<"
							+ regstr + "> fehlerhaft");
					udbo.setState(99);
					udb.WriteDB();
				}
			}
			// falls ein Posting da dann füge es zu der Gesammtübersicht an
			calcPostinfo(htmlpage, regtage, unam, regstr, einpost);
			einpost = tdbo.GetNextPosting(startzeit, false);
			if (einpost == null)
				break;
		}
		// Berechne allegmeine Statistikdaten für diese Postingübersicht
		if (anzpostings == 0)
			durchtage = 0;
		else
			durchtage = (sumregtage / anzpostings);
		float durchjahre = (durchtage / 365);

		htmlpage.Append("____Userübersicht _________________________<br>");
		htmlpage.Append("Postinganz=" + anzpostings + " Aktienerfahrung=<"
				+ durchtage + ">tage|<" + durchjahre + ">jahre  <br>");
		htmlpage.Append("Allgemeine Threadinfo <"
				+ tdbo.getThreadrankingstring() + "> <br>");
		htmlpage.Append("threadname=" + tdbo.getThreadname() + " tid="
				+ tdbo.getThreadid());
		htmlpage.AppendBalken("red", 5);
		return htmlpage;
	}

	private void BauePostingsauf2(String lastcreatetime, int threadid,
			int postcount, Htmlpage htmlpage, ThreadDbObj tdbo, UserDB udb)
	{
		// Hier wird die liste mit den einzelnen Postings aufgebaut
		ThreadsPostElem einpost = null;
		UserDbObj udbo = null;
		String regstr;
		int regtage = 0;
		// erstes posting
		einpost = tdbo.GetNextPosting(lastcreatetime, true);
		if (einpost == null)
			return;// nix neues

		while (5 == 5)
		{
			// falls ein Posting da dann füge es zu der Gesammtübersicht an
			udbo = udb.getUserobj(einpost.get_username());

			if (udbo != null)
			{
				regstr = udbo.getRegistriert();
				if (Tools.isDate(regstr) == false)
					Tracer.WriteTrace(20, "Warning: Datumsformatfehler regstr<"
							+ regstr + "> htmlpage<" + htmlpage.zeigeFilename()
							+ ">");
				else
				{ // Datum ist korrekt
					regtage = Tools.zeitdifferenz_tage(Tools
							.get_aktdatetime_str(), regstr);
					htmlpage.AppendBalken("black", 1);
					calcPostinfo(htmlpage, regtage, einpost.get_username(),
							regstr, einpost);
				}
				htmlpage.Append(einpost.get_postingzeile());
				// System.out.println("postnr=" + einpost.get_postid());
			}
			if ((einpost = tdbo.GetNextPosting(lastcreatetime, false)) == null)
				break;
		}
	}

	public boolean SammleThreadinfos(UserDB udb, int alleuserflag,
			String observefile, String startzeit, String akttime1,
			int postcounter,String pref)
	{
		// hier werden die Threadinfos aufbereitet, d.h. die Threads die
		// beobachtet werden sollen werden
		// schön übersichtlich dargestellt
		int threadid = 0;
		Htmlpage htmlpage = null;

		ThreadsDB tdb = new ThreadsDB();
		ThreadDbObj tdbo = null;
		// im Threadpool werden die threadid´s gespeichert, id´s sollen nicht
		// doppelt vorkommen
		InfoThreadIdPool idpool = new InfoThreadIdPool(udb, observefile);

		// arbeitet alle Threads ab
		while ((threadid = idpool.getNextThreadId()) != 0)
		{
			// holt den Thread mit der passenden Threadid
			tdbo = tdb.SearchThreadid(threadid);

			if (tdbo == null)
			{
				Tracer.WriteTrace(20, "Warning: tid<" + threadid
						+ "> konnte nicht in thread.db gefunden werden");
				continue;
			}
			// zwei getrennte durchläufe
			// a) Baue den Header auf
			htmlpage = BauePostinguebersichtauf1(startzeit, threadid,
					postcounter, htmlpage, tdbo, udb,pref);

			// b) Baue die Postingsauf
			if (htmlpage != null)
				BauePostingsauf2(startzeit, threadid, postcounter, htmlpage,
						tdbo, udb);
		}

		return true;
	}

	// /main*********************************************************************
	public void BaueTagesinfo(int alluserflag, UserStatistik ustat, ProgressBar pb,
			Text text, Display dis,String pref,HashSet tidset,String rangfile)
	{
		Inf inf = new Inf();
		AktDB aktdb = new AktDB();
		UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
		String inffile = GC.rootpath + "\\info\\"+pref+"\\userpostings\\observeinfo.txt";
		ControllObj co = new ControllObj();
		String lastcreatetime = co.getLastCreationDirTime();
		String jetztzeit1 = BerechneAktTimeMinus1(co);
		int postcounter = co.getPostcount();

		inf.appendzeile(inffile, Tools.get_aktdatetime_str()
				+ ":***Untersuche postings von <" + lastcreatetime + "> bis <"
				+ jetztzeit1 + "> schreibe in <userinfo\\"
				+ lastcreatetime.substring(0, lastcreatetime.indexOf(" "))
				+ "> ", true);
		// hier werden für die user die übersichtsseiten aufgebaut
		SammleUserpostings(udb, aktdb, alluserflag, "observeuser.txt", lastcreatetime,
				jetztzeit1, postcounter, ustat, pb, text, dis,pref,tidset,rangfile);

		// hier werden die Threadübersichtsseiten aufgebaut
		//!!! wird erst mal abgeeschaltet da bisher noch nicht benötigt
		//SammleThreadinfos(udb, alluserflag, "observethreads.txt", lastcreatetime,
		//		jetztzeit1, postcounter,pref);

		// bestimme den Verzeichnissnamen
		String postverzeichniss = CalcPostverzeichniss(lastcreatetime,pref);
		GenKursiconverzeichniss(postverzeichniss);

		// Kopiere Sliderübersicht
		String fnam = GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht.html";
		String fnam2 = postverzeichniss + "\\slideruebersicht.html";
		FileAccess.copyFile2_dep(fnam, fnam2);

		// generiert das frame info.html
		baueInfoseite(udb, postverzeichniss);
		// generiere die linkseite userlinks.html
		baueUserlinks(udb, postverzeichniss);

		lastcreatetime = CalcSetNewTimes(co, jetztzeit1);
		inf.appendzeile(inffile, Tools.get_aktdatetime_str() + ":*fertig.....",
				true);
	}

	public boolean BaueTagesinfo_allezeit(String userobserveconfig,
			UserStatistik ustatx,String pref,HashSet tidset)
	{
		UserDB udb = new UserDB(userobserveconfig, "boostraenge.txt");
		UserDbObj userobj = new UserDbObj();
		SammleUserpostings samuser = new SammleUserpostings();

		int maxuser = udb.SetDurchlaufmode(GC.MODE_OBSERVE);
		int i = 1;

		String inffile = GC.rootpath + "\\info\\+pref+\\userpostings\\"
				+ userobserveconfig;
		String akttime1 = Tools.get_aktdatetime_str();
		udb.ResetDB(-1);

		while ((userobj = (UserDbObj) udb.GetNextObject()) != null)
		{
			String username = userobj.get_username();
			System.out.println("untersuche user<" + i + "|" + maxuser + "><"
					+ username + ">");
			samuser.StartSammleSchreibeUserpostings(udb, userobj,
					"11.11.55 18:08:03", akttime1, 0, ustatx,null,tidset);
			i++;
		}
		return true;
	}
}
