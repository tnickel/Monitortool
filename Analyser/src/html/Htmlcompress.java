package html;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import mainPackage.GC;
import stores.MidDB;
import stores.ThreadsDB;

public class Htmlcompress
{
	private ThreadsDB tdb_glob = null;

	public Htmlcompress(ThreadsDB tdb)
	{
		tdb_glob = tdb;
		/* System.out.println("constructor von Htmlwork"); */

	}

	private String delSubstring(String instr, String startkey, String endkey)
	{
		// löscht aus dem instr einen Teil
		// startkey= anfangsmarke die geloscht werden soll
		// endkey= endmarke die gelöscht werden soll
		// falls startkey nicht gefunden wurde dann wird der orginalstring
		// zurückgegeben
		// Beispiel:
		// instr="blabla href=\"http://www.wallstreet-online.de" blup blup";
		// startkey="href=\";
		// endkey=""";
		// strneu="blabla blup blup"

		int pos1 = 0, pos2 = 0, pos = 0;
		String p1 = null, p2 = null;
		String strneu = null;
		// entfernt hreff aus dem html-string

		// falls keyword nicht vorhanden
		if (instr.contains(startkey) == false)
			return instr;

		// suche den Anfang des keywords
		pos1 = instr.indexOf(startkey);
		// wandere bis Ende des Anführungszeichens
		p1 = instr.substring(pos1);

		p2 = p1.substring(startkey.length() + 1, p1.length());
		// suche das letzte Anführungszeichen
		pos2 = p2.indexOf(endkey);
		p2 = p2.substring(pos2 + endkey.length());
		// baue neuen String zusammen
		strneu = new String(instr.substring(0, pos1) + p2);
		return strneu;
	}

	private String delHtml(String Startword, String Endword, String instr)
	{

		while (instr.contains(Startword) == true)
		{
			instr = delSubstring(instr, Startword, Endword);
		}

		return instr;
	}

	public String cleanHtmlPostingLine(String instr)
	{
		// säubert eine htmlzeile indem unnötige infos weggeworfen werden
		// zurückgegeben wird die gesäuberte zeile
		instr = delHtml("onclick=\"", "\"", instr);
		instr = delHtml("onclick='", "'", instr);
		// instr = delHtml("href=\"", "\"", instr);
		instr = delHtml("target=\"", "\"", instr);
		// instr = delHtml("<img src=\"http://", ">", instr);
		instr = delHtml("<td style=\"background: url(http:", ">", instr);
		instr = delHtml("td style=\"width", ">", instr);
		instr = delHtml(
				"td class=\"right\" style=\"width:130px;background: url(http://img.wallstreet",
				">", instr);
		instr = delHtml("<script type=\"text/javascript", "</script>", instr);
		// instr = delHtml("<img src=\"http", "/>", instr);// entfernt masterid
		instr = delHtml("class=\"postforumnavigation", "</body></html>", instr);
		instr = delHtml("<font class=\"s8\">      &nbsp;Dieses&nbsp;Posting:",
				"value=\"Antwort schreiben\" />", instr);
		instr = delHtml(
				"<font class=\"s8\">    &nbsp;Dieses&nbsp;Posting:&nbsp;&nbsp;",
				"</font>", instr);

		return instr;
	}

	public boolean cleanCompressHtmlFile(MidDB middb, String infilename,
			String outfilename, int erwtid, String urlnam)
	{
		// Liest ein Htmlfile ein und löscht unnötige links, und speichert dann
		// das Htmlfile unter den gleichen Namen outfilename
		// im augenblick werden nur Threadseiten komprimiert !!!
		// ret: n = anz postings auf der seite
		// -1= fehler

		// zeigt an das beide filenamen gleich sind
		// erwtid: ist die erwartete tid

		BufferedReader inf = null;
		BufferedWriter ouf = null;
		// gibt an wieviele postings auf der seite sein sollten

		if (FileAccess.FileAvailable(infilename) == false)
		{
			Tracer.WriteTrace(20, "Warning file <" + infilename
					+ "> zum compressen nicht vorhanden -> mache nix");

			return false;
		}

		inf = FileAccess.ReadFileOpen(infilename);
		if (inf == null)
		{
			Tracer.WriteTrace(20, "Error: file lesefehler");
			return false;
		}
		String outfilenamet = outfilename;
		outfilenamet = outfilename + "t";

		String zeile = "";
		int i = 0;

		try
		{
			while ((zeile = inf.readLine()) != null)
			{
				if ((zeile.contains("Fatal error: Uncaught exception"))
						&& (zeile.length() < GC.HTMLLENGTHMIN))
				{
					Tracer.WriteTrace(20, "Warning: file <" + infilename
							+ "> contains corrupt information I delete it");
					inf.close();
					File f = new File(infilename);
					f.delete();
					return false;
				}
				if ((zeile
						.contains("Es ist ein Fehler aufgetreten. Der Seitenbetreiber wurde informiert. Bitte versuchen Sie es") == true)
						&& (zeile.length() < GC.HTMLLENGTHMIN))
				{
					Tracer.WriteTrace(
							20,
							
							"Warning: file <"
									+ infilename
									+ "> contains corrupt information 'Seitenbetreibererror'I delete it");
					inf.close();
					File f = new File(infilename);
					f.delete();
					return false;

				}
				Tracer.WriteTrace(50, this.getClass().getSimpleName()
						+ "zeile=<" + zeile + ">");

				// falls schon komprimiert dann mache nix
				if (zeile.contains("###compressed###") == true)
				{

					break;
				}
				if (ouf == null)
					ouf = FileAccess.WriteFileOpen(outfilenamet, "UTF-8");
				// die zeile muss gemacht werden damit der browser die umlaute
				// richtig anzeigt
				ouf.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" >");
				ouf.write("###compressed###");
				ouf.newLine();
				// schaue nach ob thread-htmlseite
				if (infilename.contains("\\downloaded\\threads\\") == true)
				{
					
					if(infilename.contains("MORPHOSYS404_884")==true)
						System.out.println("found morphosys 884");
					
					// schreibe die kopfelemente
					Threads html = new Threads(middb, tdb_glob, infilename,
							erwtid);
					// Die Werbung am Ende der Htmlseite wird entfernt
					html.entferneEndmuell();
					zeile = html.GetHtmlElements(erwtid) + "url===" + urlnam
							+ "###";

					ouf.write(zeile);
					ouf.newLine();

					// schreibe die postings
					for (i = 1; i <= 10; i++)
					{
						ouf.newLine();
						String postline = html.GetNextPostingString();
						if (postline.length() <= 2)
						{
							// falls i=1 und keine postings dann stimmt was
							// nicht
							if (i < 2)
							{
								Tracer.WriteTrace(
										20,
										"Warning: Webseite <"
												+ infilename
												+ "> postnr<"
												+ i
												+ "> hat keine postings für die komprimierung -> Webseite bei WO kaputt?-> Seite wird neu geladen");
								ouf.close();
								inf.close();
								return false;
							}
						}
						zeile = "Posting===" + postline + "###";

						if (zeile.length() <= 2)
						{
							Tracer.WriteTrace(
									20,
									"Warning: posting konnte nicht aus postinzeile extrahiert werden file<"
											+ infilename
											+ "> -> Webseite bei WO kaputt?-> Seite wird neu geladen ");
							if (ouf != null)
								ouf.close();
							inf.close();
							return false;
						}

						ouf.write(zeile);
						ouf.newLine();

					}
				}
				// schaue nach ob user-htmlpage
				else if (infilename.contains("\\downloaded\\userhtmlpages\\") == true)
				{
					if (zeile.contains("Keine passenden User gefunden") == true)
					{
						Tracer.WriteTrace(20,
								"Error: Keine passenden User gefunden");
						inf.close();
						if (ouf != null)
							ouf.close();
						return false;
					}

					Userhtmlpage html = new Userhtmlpage(infilename);

					zeile = html.GetHtmlElements();
					ouf.write(zeile);
					ouf.write("<br ><br >");
					ouf.newLine();
				}
			}

			inf.close();
			if (ouf != null)
				ouf.close();

			if (FileAccess.FileAvailable(outfilenamet) == true)
				FileAccess.Rename(outfilenamet, outfilename);

			// alles ok
			return true;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(20, "Error: compression error <" + infilename
					+ "> <" + outfilename + ">=");

		} catch (StringIndexOutOfBoundsException e)
		{
			// falls html-file fehlerhaft ist dann gibt einen Fehlermeldung aus
			// Errorclass 10
			Tracer.WriteTrace(20, "W: htmlfile<" + infilename
					+ "> fehlerhaft (out of BoundException), -> loesche es");

		}

		// Fehlerausgang

		if (inf != null)
			try
			{
				inf.close();

			} catch (IOException e1)
			{
				// TODO Auto-generated catch block

				e1.printStackTrace();

			}

		if (ouf != null)
			try
			{
				ouf.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}
		FileAccess.FileDelete(infilename, 0);
		FileAccess.FileDelete(outfilenamet, 0);
		return false;

	}
}
