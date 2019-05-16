package hilfsrepeattask;

import hilfsklasse.Tracer;
import hilfsobjekt.Fensterinfo;
import html.DB;
import html.Htmlcompress;
import html.ThreadsPostElem;

import java.util.HashSet;

import objects.Htmlpage;
import objects.ThreadDbObj;
import objects.UserDbObj;
import stores.ThreadsDB;
import stores.UserDB;

public class SammleThreadpostings
{
	private void baueKopf(Htmlpage htmlTexte, ThreadDbObj tdbo)
	{
		// Baut den Kopf der Ausgabehtmlseite
		htmlTexte.Append("<a href=\"../../..//db/threaddata/"
				+ tdbo.getThreadname() + "_" + tdbo.getThreadid()
				+ ".db\" target=\"Postingfenster\">Thread '"
				+ tdbo.getThreadname() + "' Postingübersicht  </a>");
		htmlTexte.Append("<a href=\"../../..//db/seitenzaehler/"
				+ tdbo.getThreadname()
				+ ".db\" target=\"Postingfenster\"> Seitenzaehler </a><br>");
	}

	private DB<ThreadsPostElem> holeWebseite(ThreadDbObj tdbo, int page)
	{
		// Holt die Postings für eine ganze Webseite(10 Postings)
		String fnam = tdbo.GetSavePageName_genDir(page);
		DB<ThreadsPostElem> posting = new DB<ThreadsPostElem>(fnam + ".gzip");
		return posting;
	}

	private int werteWebseiteAus(ThreadsDB tdb,Htmlpage htmlTexte,
			DB<ThreadsPostElem> posting, int postanzahl, ThreadDbObj tdbo,
			ThreadsPostElem obj, UserDB udb, int minbewert,
			boolean keinebalken, HashSet<String> usermenge,
			Fensterinfo finfo_page, int nurusernameflag, String uamesuch,int unamAntwflag)
	{
		// rückmeldung: int anz der neuen Postings in dieser Webseite
		// hier werden die postings für eine webseite aufgebaut
		int tmpanz = 0;
		Htmlcompress hw = new Htmlcompress(tdb);
		int postcount = 1;

		// Hier werden die einzelnen Postings behandelt
		while ((obj = posting.GetNextPosting()) != null)
		{
			if (postcount == 1)
				finfo_page.setIstStartdatum(obj.get_datetime());
			postcount++;

			if (postcount > postanzahl)
				break;

			finfo_page.setIstEnddatum(obj.get_datetime());

			String zeile = obj.get_postingzeile();
			// hier noch userinfo einfügen
			String unamepost = obj.get_username();
			UserDbObj udbo = udb.getUserobj(unamepost);

			int rang = udbo.getRang();
			
			if(obj.get_postid()==20806)
				System.out.println("found 20806");
			
			if((nurusernameflag==1) &&(unamepost.equals(uamesuch)))
			{
				// User passt
				htmlTexte.AppendBalken("green", 3);

				tmpanz++;
				htmlTexte.Append("<i>" + hw.cleanHtmlPostingLine(zeile));

				// der link zu dem Thread wird hinter jedem posting angefügt
				htmlTexte.AppendThreadLink(tdbo, obj.get_postid());
			}
			else if((unamAntwflag==1) && 
					( (unamepost.equals(uamesuch)) ||(zeile.contains(uamesuch))))
			{
				//falls eine Antwort auf das Posting des suchusers da ist dann nimm auf
				htmlTexte.AppendBalken("blue", 3);

				tmpanz++;
				htmlTexte.Append("<i>" + hw.cleanHtmlPostingLine(zeile));

				// der link zu dem Thread wird hinter jedem posting angefügt
				htmlTexte.AppendThreadLink(tdbo, obj.get_postid());
			}
			else if((nurusernameflag==1))
			{
				//user passt nicht, dann nix
				continue;
				
			}
			else if ((nurusernameflag==0)&&(unamAntwflag==0)&&(rang > 0) && (rang < minbewert))
			{
				// nimm die user in die usermenge(rechte seite) wenn die
				// postings links auch angezeigt werden
				if (usermenge.contains(unamepost) == false)
					usermenge.add(unamepost);

				// posting wird angzeigt
				htmlTexte.AppendBalken("green", 3);

				tmpanz++;
				// zeige nur gute user an
				htmlTexte.Append("<p>Username[" + unamepost + "] points["
						+ udbo.getRang() + "] </p><br>");
				htmlTexte.Append("<i>" + hw.cleanHtmlPostingLine(zeile));

				// der link zu dem Thread wird hinter jedem posting angefügt
				htmlTexte.AppendThreadLink(tdbo, obj.get_postid());
			} else if (keinebalken == false)
			{
				// posting wurde nicht angezeigt
				String info = "[" + udbo.getUsername() + "|" + udbo.getRang()
						+ "]";
				htmlTexte.AppendBalkenInfo(info, "green", 3);
			}

		}
		return tmpanz;
	}

	public boolean StartSammlePostingsPageid(ThreadsDB tdb, int tid,
			int startpage, int postanzahl, String filename, UserDB udb,
			int minbewert, boolean keinebalkenflag, HashSet<String> usermenge,
			Fensterinfo finfo_all, int nurusernameflag, String username, int unamAntwflag)
	{
		// Stellt die letzten Threadspostings für eine tid übersichtlich in
		// einem htmlfile dar
		// startpage: ab dieser page wird angefangen auszuwerten
		// postanzahl: maximale anzahl der postings die angezeigt wird
		// filename: resultatfile
		// minbewert: die user müssen diese minimale bewertung haben sonst wird
		// nix angezeigt
		// usermenge: dies ist die menge der user die dem kriterium entsprechen
		// und die in dem htmlfile angezeigt werden

		int postcounter = 0;
		Htmlpage htmlTexte = null;
		htmlTexte = new Htmlpage(filename);
		ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObject(tid);
		Fensterinfo finfo_page = new Fensterinfo();

		if (tdbo == null)
		{
			Tracer.WriteTrace(10, "tid<" + tid + "> ist nicht in threads.db");
			return false;
		}
		ThreadsPostElem obj = new ThreadsPostElem();
		baueKopf(htmlTexte, tdbo);
		int maxpage = tdbo.calcLetzteVorhThreadseite(1);

		for (int i = startpage; i <= maxpage; i++)
		{

			DB<ThreadsPostElem> posting = holeWebseite(tdbo, i);
			int anzneuerpostings = werteWebseiteAus(tdb,htmlTexte, posting,
					postanzahl, tdbo, obj, udb, minbewert, keinebalkenflag,
					usermenge, finfo_page,nurusernameflag,  username,unamAntwflag);
			postcounter = postcounter + anzneuerpostings;

			if (i == startpage)
				finfo_all.setIstStartdatum(finfo_page.getIstStartdatum());
			else if (i == maxpage)
				finfo_all.setIstEnddatum(finfo_page.getIstEnddatum());

			// Das Ende ist erreicht
			if (postcounter > postanzahl)
				break;
		}
		return true;
	}

	public boolean StartSammlePostingsStartdatum(ThreadsDB tdb, int tid,
			String startdatum, int postanzahl, String filename, UserDB udb,
			int minbewert, boolean keinebalkenflag, HashSet<String> usermenge,
			Fensterinfo finfo, int nuruserflag, String username,int unamantwflag)
	{
		// Stellt die letzten Threadspostings für eine tid übersichtlich in
		// einem htmlfile dar
		// startdatum: bsp. es werden die letzten 2 wochen ausgewertet
		// postanzahl: maximale anzahl der postings die angezeigt wird
		// filename: resultatfile
		// minbewert: die user müssen diese minimale bewertung haben sonst wird
		// nix angezeigt
		// usermenge: dies ist die menge der user die dem kriterium entsprechen
		// und die in dem htmlfile angezeigt werden
		// nuruserflag: falls dies flag = 1 ist werden nur die postings dieses
		// users angezeigt
		// username: username der selektiert wurde

		int postcounter = 0;
		Htmlpage htmlTexte = null;
		htmlTexte = new Htmlpage(filename);
		ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObject(tid);
		Fensterinfo finfo_tmp = new Fensterinfo();
		finfo_tmp.setSuchstartdatum(finfo.getSuchstartdatum());
		
		if (tdbo == null)
		{
			Tracer.WriteTrace(20, "tid<" + tid + "> ist nicht in threads.db");
			return false;
		}
		ThreadsPostElem obj = new ThreadsPostElem();
		baueKopf(htmlTexte, tdbo);
		int maxpage = tdbo.calcLetzteVorhThreadseite(1);
		
		//sucht die erste seite
		int startindex = tdbo.sucheSeiteStartdatum(tdb,startdatum,maxpage);

		int firstflag=0;
		for (int i = startindex; i <= maxpage; i++)
		{
			DB<ThreadsPostElem> posting = holeWebseite(tdbo, i);
			int anzneuerpostings = werteWebseiteAus(tdb,htmlTexte, posting,
					postanzahl, tdbo, obj, udb, minbewert, keinebalkenflag,
					usermenge, finfo_tmp, nuruserflag, username,unamantwflag);
			// startdatum setzten
			if ((anzneuerpostings>0)&&(firstflag==0))
			{
				firstflag=1;
				finfo.setIstStartdatum(finfo_tmp.getIstStartdatum());
			}
			
			postcounter = postcounter + anzneuerpostings;
			if(anzneuerpostings>0)
				finfo.setIstEnddatum(finfo_tmp.getIstEnddatum());
			finfo.setPostcounter(postcounter);

			// Das Ende ist erreicht
			if (postcounter > postanzahl)
				break;

		}
		return true;
	}
}
