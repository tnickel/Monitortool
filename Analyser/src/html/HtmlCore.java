package html;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import stores.ThreadsDB;

public class HtmlCore extends Keyword
{
	// beinhaltet eine beliebige Html-Seite
	// Es wird hier nur der Datenstrom gespeichert
	// Dies ist die Grundklasse für eine HtmlPage
	// möchte man spezielle HTML-Klassen auswerten so ist
	// dise HTMLPage zu erweitern

	// Dieser String beinhaltet die gesammte HTML-Seite
	protected String htmlseite_str = null;
	private String fnam_for_logging = null;

	static int ladefehlerzaehler = 0;
	static private BufferedReader inf_p = null;
	private ThreadsDB tdb_glob = null;

	public HtmlCore()
	{
		// System.out.println("Constructor von HtmlCore");
	}

	public HtmlCore(ThreadsDB tdb, String filename)
	{
		fnam_for_logging = filename;
		tdb_glob = tdb;
		// System.out.println("Constructor von HtmlCore read
		// file<"+filename+">");
		ReadHtmlPage(filename);
	}

	public boolean HtmlReadPage(String filename)
	{
		fnam_for_logging = filename;
		return (ReadHtmlPage(filename));
	}

	private Boolean CheckHtmlpage(String htmlseite_str, String filename)
	{
		if (htmlseite_str == null)
		{

			htmlseite_str = "Dies ist ein dummy htmlseite";
			// Bei 3 aufeinander fehlerhaften Seiten stoppe

			Tracer.WriteTrace(20, "Warning:broken html-page<" + filename + ">");
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (filename != null)
			{
				Tracer.WriteTrace(20, "I:delete file<" + filename + ">");
				FileAccess.FileDelete(filename, 0);
			}
			return false;
		} else
			ladefehlerzaehler = 0;
		return true;
	}

	protected boolean ReadHtmlPage(String filename)
	// läd eine ganze HTML-Seite nach htmlseite
	{
		String fnam = null;
		htmlseite_str = null;

		fnam_for_logging = filename;
		int ladefehlerzaehler = 0;

		fnam = filename;
		try
		{

			if (FileAccess.FileAvailable(fnam) == false)
			{
				Tracer.WriteTrace(20, "Error: file<" + fnam
						+ "> not available 06");
				return (false);
			}
			inf_p = FileAccess.ReadFileOpen(fnam, "UTF-8");

			if (inf_p == null)
				return false;

			String postzeil = "";
			while ((postzeil = inf_p.readLine()) != null)
			{
				// System.out.println(zeil);
				if (htmlseite_str == null)
					htmlseite_str = new String(postzeil);
				else
					htmlseite_str = htmlseite_str.concat(postzeil);
			}
			inf_p.close();

			CheckHtmlpage(htmlseite_str, fnam);

			return true;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void CloseHtmlPage()
	{
		try
		{
			if (inf_p != null)
				inf_p.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void entferneEndmuell()
	{
		
		//Schneide den Muell und die Werbung am Ende ab
		if((htmlseite_str.contains("[&nbsp;Seite:&nbsp;<a href=\"/diskussion")==true) && (htmlseite_str.contains("<!-- Google AdSense -->")==true))
		{
			htmlseite_str=htmlseite_str.substring(0,htmlseite_str.indexOf("<!-- Google AdSense -->"));
		}
		else if(htmlseite_str.contains("Beitrag zu dieser Diskussion schreiben")==true)
			htmlseite_str=htmlseite_str.substring(0,htmlseite_str.indexOf("Beitrag zu dieser Diskussion schreiben"));
		
	}
	
	
	
	public String GetKeywordPart(String t1, String t2)
	{
		return (this.GetKeywordPart(htmlseite_str, t1, t2));
	}

	public String GetKeyword(String Keyword, int min, int max)
	{
		return (this.GetKeyword(htmlseite_str, Keyword, min, max));
	}

	public String GetKeywordTd(String suchstring)
	{
		return (GetKeywordTd(htmlseite_str, htmlseite_str, suchstring));
	}

	public Boolean CheckKeyword(String keyword)
	{
		return (CheckKeyword(htmlseite_str, keyword));
	}

	public String GetThreadLineX(String suchstring)
	{
		// holt aus der htmlseite den Suchstring,
		// Diese Funktion wird immer dann angewendet wenn in einer HTML-Seite
		// Listenelemente extrahiert werden müssen. Die Listenelemente werden
		// nur
		// einmal gefunden da sie anschliessend kaputt gemacht werden

		if (CheckHtmlpage(htmlseite_str, null) == false)
			return ("0".toString());

		if (htmlseite_str.contains(suchstring))
		{
			int laenge = 0;
			int start = 0;
			int end = 0;

			start = htmlseite_str.indexOf(suchstring) + suchstring.length();
			end = htmlseite_str.length();
			laenge = htmlseite_str.length();

			if (end > laenge)
				end = laenge;

			String part1 = htmlseite_str.substring(start, end);
			// System.out.println("part1=" + part1);

			// mache String kaputt, damit er beim nächsten Male nicht wieder
			// genommen wird
			htmlseite_str = htmlseite_str.replaceFirst("threadlink",
					"threadlin@");
			return (part1);
		}
		return ("0".toString());
	}

	protected String GetFullHtmlSignal()
	// a)holt einen Signalstring aus einer Htmlseite
	{
		// a)holt einen Signalstring aus einer Htmlseite
		String suchstring = null;

		if (CheckHtmlpage(htmlseite_str, null) == false)
			return (null);

		suchstring = "class=\"morebutton\">Weitere&nbsp;Chartsignal";
		if (htmlseite_str.contains(suchstring))
		{
			int start = 0;
			int end = 0;
			String seite = null;
			String k1 = "<tr class=\"hl\"> <td>";
			String k2 = "border=\"0\" ></a></td></tr><tr > <td>";
			String k1kaputt = "<tr class=\"hx\"> <td>";
			String k2kaputt = "borderxx=\"0\" ></a></td></tr><tr > <td>";
			String retstr = null;

			// sucht den start des signalblockes
			start = htmlseite_str.indexOf(suchstring) + suchstring.length();
			seite = htmlseite_str.substring(start);

			int pos1 = seite.indexOf(k1);
			int pos2 = seite.indexOf(k2);

			if (pos1 < 0)
				pos1 = 100000;
			if (pos2 < 0)
				pos2 = 10000;

			if (pos1 < pos2)
				retstr = holeKennerString(seite, k1, k1kaputt);
			else
				retstr = holeKennerString(seite, k2, k2kaputt);
			return retstr;
		}
		return (null);
	}

	private String holeKennerString(String seite, String kenner,
			String kennerkaputt)
	{
		// holt aus einer htmlseite einen part der durch einen kenner
		// identifiziert wird
		if (seite.contains(kenner))
		{
			// holt ein signal und macht den kenner kaputt
			String part1 = GetKeywordPartMaxdist(seite, kenner, "<a href", 160);
			htmlseite_str = htmlseite_str.replaceFirst(kenner, kennerkaputt);

			return new String(part1);
		} else
			return (null);
	}

	protected String GetFullHtmlPosting()
	// a)holt den nten Teilstring aus zeile, d.h. es wird der zwischenraum von
	// suchstring-suchstring rausgeschnitten
	// z.B. wird ein userposting gesucht.
	// wurde dies gefunden wird das erste suchmuster zerstört, so das beim
	// nächsten Male ein weiteres gesucht wird
	// b)Falls die Webseite schon komprimiert ist wird das Posting durch
	// das Schlüsselwort Postingx=== ### extrahiert
	// Hinweis: Beim ersten download werden die htmlseiten geladen und dann in
	// diesem
	// komprimierten Format mit den Schlüsselwörtern gespeichert !!
	{
		String suchstring = null, replacement = null,suchstringReg=null;

		//xxxxxxxxxxxxxxxxxxxxxxx		
		if (CheckHtmlpage(htmlseite_str, null) == false)
			return ("0".toString());

		// b) prüfe ob komprimierte Webseite
		suchstring = "Posting===";

		//Falls alle postings schon geholt wurden
		if((htmlseite_str.contains("###compressed###")==true)&&
				(htmlseite_str.contains("Posting===")==false))
					return ("0".toString());
			
		
		if (htmlseite_str.contains(suchstring))
		{
			String part = GetKeywordMem(htmlseite_str, "Posting===", "###");
			htmlseite_str = htmlseite_str.replaceFirst(suchstring,
					"blupblup===");
			return new String(part);
		}

		// a)die Webseite ist noch nicht komprimiert, dann wende die
		// konventionelle methode an

		// schaue nach welcher der beiden Suchstring in der Webseite die
		// einzelnen Postings extrahieren kann

		if (htmlseite_str.contains("&nbsp;<i>#"))
		{
			suchstring = "&nbsp;<i>#";
			suchstringReg="&nbsp;<i>#";
			replacement = "&nbsp;<i>@";
		} else if (htmlseite_str
				.contains("writeUserFunctions('#userMenu_"))
		{
			suchstring =  "writeUserFunctions('#userMenu_";
			suchstringReg =  "writeUserFunctions\\('#userMenu_";
			replacement = "writeUserFunctions('#userMe##_";
		}
		// nix mehr gefunden, dann schaue ob denn wenigsstens ein suchclean da
		// ist, also
		// die Seite schon abgearbeitet ist
		else if ((htmlseite_str.contains("&nbsp;<i>@") == false)
				&& (htmlseite_str
						.contains("myCommunity.writeUserFunctions('#userMe##") == false))
		{
			// nix da, weder suchstrings, noch suchclean, dann fataler fehler
			Tracer.WriteTrace(20,
					"Error: Webseitenaufbau hat sich geändert file<"
							+ fnam_for_logging + "> Full Posting Error: =>Anpassen oder Ladefehler");
			return ("0".toString());
		}

		if (htmlseite_str.contains(suchstring))
		{
			int start = 0;
			int end = 0;

			start = htmlseite_str.indexOf(suchstring) + suchstring.length();
			// mache suchstring kaputt !!!!
			htmlseite_str=htmlseite_str.replaceFirst(suchstringReg,
					replacement);
			end = htmlseite_str.indexOf(suchstring);

			if (end == 0)
				end = htmlseite_str.length();
			if (end == -1)// nix mehr gefunden
				end = htmlseite_str.length();
			String part1 = (htmlseite_str.substring(start, end));
			// System.out.println("part1=" + part1);

			Htmlcompress html = new Htmlcompress(tdb_glob);
			//Hier wird nochmal Müll entfernt
			part1 = html.cleanHtmlPostingLine(part1);
		
			
			return new String(part1);
		}
		return ("0".toString());
	}

	protected String GetFullUserhtmlListeneintrag()
	{
		// holt den nächsten Beitrag aus userhtmlpages..postings
		// a) Die Webseite ist noch nicht komprimiert, dann hole nativ
		// b) Die Webseite ist komprimiert
		String suchstring = null;

		if (CheckHtmlpage(htmlseite_str, null) == false)
			return ("0".toString());
		// b) versuche was aus dem komprimierten teil zu holen

		suchstring = "Listeneintrag===";
		if (htmlseite_str.contains(suchstring))
		{
			String part = GetKeywordMem(htmlseite_str, suchstring, "###");
			htmlseite_str = htmlseite_str.replaceFirst(suchstring,
					"blupblup===");
			return new String(part);
		} else
		{
			// a hole nativ da noch nicht komprimiert
			String zeile = new String(GetThreadLineX("class=\"threadlink\""));
			if (zeile.length() > 10)
			{
				if (zeile
						.contains("</table></td></tr></table></div>  <div id=\"sitefoot") == true)
					zeile = zeile
							.substring(
									0,
									zeile
											.indexOf("</table></td></tr></table></div>  <div id=\"sitefoot"));
				else if (zeile.contains("neustebeitraege") == true)
					zeile = zeile
							.substring(0, zeile.indexOf("neustebeitraege"));
			}
			if (zeile.equals("0") == false)
				return zeile;
		}
		return ("0".toString());
	}
	protected String GetFullHtmlMeistDiskutiertPosting()
	
	{
		String suchstring = null, replacement = null;

	

		// a)die Webseite ist noch nicht komprimiert, dann wende die
		// konventionelle methode an

		// schaue nach welcher der beiden Suchstring in der Webseite die
		// einzelnen Postings extrahieren kann

	
			suchstring = "class=\"fadeOutBox\"><div class=\"innerFadeOutBox\">";
			replacement = "class=\"fadeOutBox\"><div class=\"innerFadeOutBox@@@";
		
			
			
		// nix mehr gefunden, dann schaue ob denn wenigsstens ein suchclean da
		// ist, also
		// die Seite schon abgearbeitet ist
		if ((htmlseite_str.contains(suchstring) == false)
				&& (htmlseite_str
						.contains(replacement) == false))
		{
			// nix da, weder suchstrings, noch suchclean, dann fataler fehler
			Tracer.WriteTrace(10,
					"Error: Webseitenaufbau hat sich geändert file<"
							+ fnam_for_logging + "> Full Posting Error: =>Anpassen oder Ladefehler");
			return ("0".toString());
		}

		if (htmlseite_str.contains(suchstring))
		{
			int start = 0;
			int end = 0;

			start = htmlseite_str.indexOf(suchstring) + suchstring.length();
			// mache suchstring kaputt !!!!
			htmlseite_str=htmlseite_str.replaceFirst(suchstring,
					replacement);
			end = htmlseite_str.indexOf(suchstring);

			if (end == 0)
				end = htmlseite_str.length();
			if (end == -1)// nix mehr gefunden
				end = htmlseite_str.length();
			String part1 = (htmlseite_str.substring(start, end));
			// System.out.println("part1=" + part1);
			
			return new String(part1);
		}
		return ("0".toString());
	}
}
