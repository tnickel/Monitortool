package basis;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.DB;
import html.Htmlcompress;
import html.Threads;
import html.ThreadsPostElem;
import internetPackage.DownloadManager;
import mainPackage.GC;
import objects.ThreadDbObj;
import stores.MidDB;
import stores.ThreadsDB;

/**
 * @author tnickel test
 */
abstract public class BAWorkHtml
{
	// hier wird eine htmlseite verarbeitet !!
	String fnam = null;

	
	DownloadManager Wpage = new DownloadManager(GC.MAXLOW);
	int downloadtry_g = 0;
	// sagt ob der compressed alg. für den htmldurchlauf erlaubt ist
	int compressedflag_g = 0;
	static private String lastplausiname = "";
	public abstract void BAworkOnePostingAbstract(ThreadsPostElem obj);

	/**
	 * @param obj
	 *            Dieses Objekt wird hier verarbeitet
	 * 
	 * 
	 */
	public void BAsetHtml(String n)
	{
		// compressedflag=0; keine compressed htmlfiles
		// compressedflag=1; compressed htmlfiles sind erlaubt
		fnam = n;
		downloadtry_g = 0;
	}

	private boolean plausicheck(MidDB middb,ThreadsDB tdb,ThreadDbObj tdbo)
	{
		// hier wird geprüft ob die threadseite eine Seite zu dem zugehörigen
		// Thread ist
		// Dies wird anhand des Eröffnet Datums überprüft
		if (FileAccess.FileAvailable(fnam) == false)
			return false;
		String eroeff = tdbo.getEroeffnet();
		if ((eroeff == null) || (eroeff.equalsIgnoreCase("null")))
		{
			if (lastplausiname.equalsIgnoreCase(tdbo.getThreadname()) == false)
				Tracer.WriteTrace(20,
						"Warning: keine EröffnetZeit in threadsdb für <"
								+ tdbo.getThreadname() + "> tid<"
								+ tdbo.getThreadid() + ">-> kein Plausicheck");
			lastplausiname = tdbo.getThreadname();
			return true;
		}

		// läd die Htmlseite zum auswerten
		Threads html = new Threads(middb,tdb,fnam, tdbo.getThreadid());
		String EroeffnetMasterseiteTdb = Tools
				.entferneZeit(tdbo.getEroeffnet());
		String eroeffnethtmlseite = Tools.entferneZeit(html.GetEroeffnetAm());

		// Sonderfall2: Kein Eröffnungsdatum in der threadsdb, aber in der
		// htmlseite
		if ((EroeffnetMasterseiteTdb.length() < 3)
				&& (eroeffnethtmlseite.length() > 3))
		{
			Tracer.WriteTrace(20,
					"I:Setze Eröffnungszeit in threadsdb tdboeroeffnet<"
							+ EroeffnetMasterseiteTdb + "> htmlpage<"
							+ tdbo.GetSaveInfostring() + "> eroeffnet<"
							+ eroeffnethtmlseite + ">");
			tdbo.setEroeffnet(eroeffnethtmlseite);
			
		}
		
		//HtmlSeitencheck, einmal pro Woche
	
		
		
		int tageeroeffTdb = Tools.getDateInt(GC.startdatum,
				EroeffnetMasterseiteTdb);
		int tageeroeffHtml = Tools
				.getDateInt(GC.startdatum, eroeffnethtmlseite);
		// max 2 Tage darf das eröffnungsdatum abweichen
		if (Math.abs(tageeroeffTdb - tageeroeffHtml) > 1)
		{
			// Die Seite passt hier nicht rein
			Tracer.WriteTrace(20,
					"Error: die Seite passt hier nicht rein fnam<" + fnam
							+ "> eröffnetTdb<" + EroeffnetMasterseiteTdb
							+ ">tage<" + tageeroeffTdb + "> eröffnet dies<"
							+ eroeffnethtmlseite + "> tage<" + tageeroeffHtml
							+ "> lösche");
			return false;
		}
		// alles ok
		else
			return true;
	}

	public int BAdurchlaufHtml(MidDB middb,ThreadsDB tdb,ThreadDbObj tdbo, int j)
	{
		//return 0, falls durchlauf erfolgreich
		//return GC.STATUSBAD falls nicht erfolgreich
		
		// Tracer.WriteTrace(20, "I:durchlaufe file<" + fnam + ">");
		DB<ThreadsPostElem> posting = new DB<ThreadsPostElem>(fnam);
		ThreadsPostElem einposting = new ThreadsPostElem();
		Htmlcompress hw = new Htmlcompress(tdb);
		
		
		while ((plausicheck(middb,tdb,tdbo) == false) && (downloadtry_g < 2)
				&& (GC.ONLINEMODE == 1))
		{
			Tracer.WriteTrace(20, "Warning: Seite defekt tdbo<"
					+ tdbo.getThreadname() + "> und offlinemode=>überspringe");

			if (FileAccess.FileAvailable(fnam) == true)
			{
				FileAccess.FileDelete(fnam,0);
			}

			Tracer.WriteTrace(20, "I:Lade Seite wegen plausicheck neu !!!<"
					+ fnam + ">");

			// an dieser Stelle muss die Htmlseite neu vom Web geladen werden
			String urlnam = tdbo.GetUrlName(j);

			Wpage.DownloadHtmlPage(urlnam, fnam, 0, 50000, 1, 1, 0);
			int trys=0;
			while((hw.cleanCompressHtmlFile(middb,fnam, fnam, tdbo.getThreadid(),urlnam)==false)&&(trys<3))
			{
				Tracer.WriteTrace(20, "Warning: corrupt html file <"+fnam+"> lade nocheinmal");
				int dstatus=Wpage.DownloadHtmlPage(urlnam, fnam, 0, 50000, 1, 1, 0);
				
				if(dstatus==88)
				{
					//markiere Thread als bad
					Tracer.WriteTrace(20, "I: Thread wird auf BAD gesetzt, von hand schauen");
					tdbo.setState(GC.STATUSBAD);
					return GC.STATUSBAD;
				}
				trys++;
				if(trys>3)
				{
					Tracer.WriteTrace(20, "Error: Webseite corrupt schaue von hand urlnam <"+urlnam+"> fnam<"+fnam+">");
					tdbo.setState(GC.STATUSBAD);
					return GC.STATUSBAD;
				}
			}
			posting = new DB<ThreadsPostElem>(fnam);
			einposting = new ThreadsPostElem();
			downloadtry_g++;
		}
		while ((einposting = posting.GetNextPosting()) != null)
		{
			BAworkOnePostingAbstract(einposting);
		}
		return 0;
	}
}
