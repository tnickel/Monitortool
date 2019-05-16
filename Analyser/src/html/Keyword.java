package html;

import hilfsklasse.IsValid;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

public class Keyword extends SG
{
	// Grundklasse von Keywörtern
	// Diese Klasse extrahiert aus Html-Texten Schlüsselwörter

	static public String GetBetragsnummer(String part1)
	{
		String str = null;

		// Versuch 1, neue version
		int pos1 = part1.indexOf("Beitrag&nbsp;Nr.");
		int idx2 = pos1 + 16;
		int pos2 = (part1).indexOf("&nbsp;", idx2);

		if ((pos1 > 0) && (pos2 > 0))
		{
			str = part1.substring(pos1 + 16, pos2);
			if (str.contains(":") == true)
			{

				str = "0";
			}
		}
		// Versuch 2, alte version
		if ((str == null) || (str.equals("0") == true))
		{
			str = part1.substring(0, part1.indexOf("<"));
		}

		if ((str == null) || (str.equals("0") == true))
		{
			Tracer.WriteTrace(20, "W:no Betragsnummer in htmlfile <");
			return ("0".toString());
		}
		return str;

	}

	static public String GetDateTime(String part1)
	// 04.08.11 21:46:34
	{
		int min, max;
		String suchstring = "Benutzers ausblenden";
		String suchstring2 = "schrieb am ";

		if (part1 == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		// Versuch1: neues format
		String tmp = part1.substring(0, 200);
		if (tmp.contains(suchstring2))
		{
			tmp = tmp.substring(tmp.indexOf("schrieb am ") + 11);
			tmp = tmp.substring(0, 17);
			if (tmp.length() != 17)
				Tracer.WriteTrace(10,
						"Error: webseitenaufbau Zeitformat string<" + part1
								+ ">");

			return (tmp);
		}

		// Versuch2: altes format
		if (part1.contains(suchstring))
		{
			min = part1.indexOf(suchstring) + suchstring.length() + 17;

			if (min < 0)
			{
				Tracer.WriteTrace(10, "Error:GetDateTime min=<" + min + "> z=<"
						+ part1 + ">");
				return ("0".toString());
			}
			if (min > part1.length())
			{
				Tracer.WriteTrace(10, "Error:GetDateTime min=<" + min + "> z=<"
						+ part1 + ">");
				return ("0".toString());
			}

			part1 = part1.substring(min, part1.length());// cutte links ab

			max = part1.indexOf("</b>");

			part1 = part1.substring(0, max);

			if (part1.contains("b>"))
				part1 = part1.substring(2);

			// System.out.println("part1=" + part1);
			if (part1.length() != 17)
			{
				System.out.println("Datum error laenge sollte 17 sein<" + part1
						+ ">");
				try
				{
					Thread.sleep(5000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return (part1);
		}

		System.out.println("Keyword error6");
		return ("0".toString());
	}

	static public String GetBenutzername(String part1)
	// holt den benutzernamen aus dem Posting
	// "Threads des nten- Benutzers">MrRipley</a>
	{

		String suchstring1 = "Threads des Benutzers";
		String suchstring2 = "title=\"Benutzerprofil";
		String suchstring3 = ", '";
		String suchstring = null;

		if (part1 == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		if (part1.contains("1594</i></font>  "))
			Tracer.WriteTrace(20, "1594</i></font>  ");

		suchstring = suchstring3;
		// a) neue version, versuch 1
		String tmp = part1.substring(0, 100);
		if ((tmp.contains(suchstring)) && (tmp.contains("');")))
		{
			tmp = tmp.replaceAll("<b>", "");
			tmp = tmp.replace("[mod]", "");
			tmp = tmp.substring(tmp.indexOf(", '") + 3);
			tmp = tmp.substring(0, tmp.indexOf("');"));
			return (IsValid.BenutzernameNachverarbeitung(tmp));
		}

		// b) alte version, versuch 2
		
		
		if ((part1.substring(0, 300).contains(suchstring)) && (part1.contains("</a>")))
		{
			part1 = part1.substring(
					part1.indexOf(suchstring) + suchstring.length() + 2,
					part1.indexOf("</a>"));
			// System.out.println("part1=" + part1);

			if (part1.contains("<b>"))// entferne Bold
			{
				if (part1.contains("[mod]"))
				{
					part1 = part1.substring(part1.indexOf("<b>") + 3,
							part1.indexOf("<font"));
				} else if ((part1.contains("[Mod]")))
				{
					int start = 0, end = 0;
					start = part1.indexOf("<b>") + 3;
					end = part1.indexOf("<font");
					part1 = part1.substring(part1.indexOf("<b>") + 3,
							part1.indexOf("<font"));
				} else
				{
					part1 = part1.substring(part1.indexOf("<b>") + 3,
							part1.indexOf("</b>"));
				}
			}
			// System.out.println("Benutzername=<"+part1+">");
			return (IsValid.BenutzernameNachverarbeitung(part1));
		}

		// c) alte version versuch 3
		suchstring = suchstring2;
		if ((part1.contains(suchstring)) && (part1.contains("</a>")))
		{
			part1 = part1.substring(
					part1.indexOf(suchstring) + suchstring.length() + 2,
					part1.indexOf("</a>"));
			// System.out.println("part1=" + part1);

			part1 = part1.replaceAll("<b>", "");
			part1 = part1.replace("</b>", "");
			part1 = part1.replaceAll("[mod]", "");

			// System.out.println("Benutzername=<"+part1+">");
			return (IsValid.BenutzernameNachverarbeitung(part1));
		}

		// Wenn er hier hinkommt wurde kein Benutzername gefunden
		Tracer.WriteTrace(20, "W: Keyword Benutzername nicht gefunden");

		return ("0".toString());

	}

	protected String GetKeyword(String zeile, String Keyword, int min, int max)
	{
		// Extrahiert
		// einen String,
		// Das Keyword
		// gibt die
		// Zeile an,
		// zwischen min
		// und max wird
		// extrahiert
		if (zeile == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		if (zeile.contains(Keyword))
		{
			String retstring = zeile.substring(zeile.indexOf(Keyword) + min,
					zeile.indexOf(Keyword) + max);
			return (retstring);
		}
		return ("error".toString());
	}

	static public String GetKeywordMem(String mem, String Keyword1,
			String Keyword2)
	{
		// Holt einen String der zwischen 2 Schlüsselwörtern liegt.
		// Es wird hierbei die ganze Html-Seite ausgewertet
		int links = 0, rechts = 0;
		String retstring = null;

		if (mem == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		if ((mem.contains(Keyword1)) && (mem.contains(Keyword2)))
		{
			links = mem.indexOf(Keyword1) + Keyword1.length();
			rechts = mem.indexOf(Keyword2, links);// suche mit offset links

			if ((links > 0) && (rechts > 0))
			{
				retstring = mem.substring(links, rechts);
				return (retstring);
			}
		}
		return ("0".toString());
	}

	protected String GetKeywordZahlnachLeerfeld(String mem, String keyword)
	{
		// sucht eine zahl (maximale länge = 10)im Feld
		// das maximale leerfeld darf 30 zeichen betragen
		// bsp. 'Seite 1 von 552</a>'
		// keyword ="Seite 1 von"
		// gesucht wird die 552
		int i = 0;
		String zahl = null;
		char t = 0;
		if (mem == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		if (mem.contains(keyword) == true)
		{
			String listring = mem.substring(mem.indexOf(keyword)
					+ keyword.length());

			// sucht den Anfang der zahl
			for (i = 0; i < 30; i++)
			{
				t = listring.charAt(i);
				// falls der Anfang der Zahl gefunden wurde

				if (t != ' ')
					break;
			}
			// bei 30 wurde keine Zahl gefunden, also fehler

			if (i == 30)
				return ("0".toString());
			// schneide die leerzeichen am anfangab
			listring = listring.substring(i);

			// nimmt 10 zeichen und testet ob zahl, verrringert
			// und stellt fest wann die zahl geortet
			for (i = 10; i > 0; i--)
			{
				zahl = listring.substring(0, i);
				if (is_zahl(zahl) == true)
					break;
			}
			// bei 10 wurde keine Zahl gefunden
			if (i == 10)
				return ("0".toString());

			// schneide die zahl aus
			listring = listring.substring(0, i);
			return listring;
		}

		return ("0".toString());
	}

	protected String GetKeywordDatumImLeerfeld(String mem, String keyword)
	{
		// sucht das Datum nach einem Leerfeld (maximale länge = 10)im Feld
		// das maximale leerfeld darf 30 zeichen betragen
		// 16.11.06 14:49:07
		int i = 0;
		String zahl = null;
		char t = 0;
		if (mem == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		if (mem.contains(keyword) == true)
		{
			String listring = mem.substring(mem.indexOf(keyword)
					+ keyword.length());

			// sucht den Anfang der zahl
			for (i = 0; i < 30; i++)
			{
				t = listring.charAt(i);
				// falls der Anfang der Zahl gefunden wurde

				if (t != ' ')
					break;
			}
			// bei 30 wurde keine Zahl gefunden, also fehler

			if (i == 30)
				return ("0".toString());
			// schneide die leerzeichen am anfangab
			listring = listring.substring(i);

			for (i = 0; i < 10; i++)
			{
				t = listring.charAt(i);
				if (t == ' ')
					break;
			}
			// bei 10 wurde keine Zahl gefunden
			if (i == 10)
				return ("0".toString());

			// schneide die zahl aus
			listring = listring.substring(0, i);
			return listring;
		}

		return ("0".toString());
	}

	protected String GetKeywordPart(String zeile, String Keyword1,
			String Keyword2)
	{
		// Holt einen String der zwischen 2 Schlüsselwörtern liegt.

		if (zeile == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		if (zeile.contains(Keyword1))
		{
			String part1 = zeile.substring(
					zeile.indexOf(Keyword1) + Keyword1.length(),
					zeile.indexOf(Keyword1) + 80);
			// System.out.println("part1="+part1);
			String retstring = part1.substring(0, part1.indexOf(Keyword2));
			return (retstring);
		}
		System.out.println("Keyword error1");
		return ("0".toString());
	}

	protected String GetKeywordPart2(String alles, String part1,
			String Keyword1, String Keyword2)
	{
		// Holt einen String der zwischen 2 Schlüsselwörtern liegt.
		// es wird nur der Part1 ausgewertet, in Part1 steckt nur ein
		// userposting
		int links = 0, rechts = 0;
		String reststring = null;

		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		if ((alles.contains(Keyword1)) && (alles.contains(Keyword2)))
		{
			links = part1.indexOf(Keyword1) + Keyword1.length();
			rechts = part1.indexOf(Keyword2, links);// suche mit offset links

			if ((links > 0) && (rechts > 0))
			{
				String retstring = part1.substring(links, rechts);
				return (retstring);
			}
		}
		return ("0".toString());
	}

	protected String GetKeywordBytes(String mem, String Keyword, int bytes)
	{
		// holt aus dem Gesammtspeicher einen Teilstring der laenge
		// Es wird die position des keywortes gesucht und das keywort incl.
		// der folgenden anz bytes geholt
		if (mem == null)
		{
			Tracer.WriteTrace(20, "Error:mem=null");
			return "0".toString();
		}
		if (mem.contains(Keyword))
		{
			String part1 = mem.substring(mem.indexOf(Keyword));
			if (part1.length() < bytes)
			{
				Tracer.WriteTrace(10, "Error:mem not enough bytes");
				return ("0".toString());
			}
			return part1.substring(0, bytes);
		}
		return ("0".toString());

	}

	protected String GetKeywordPartMaxdist(String zeile, String Keyword1,
			String Keyword2, int maxdist, int offset)
	{
		// hier wird vom Ergebnis von vorne noch ein Offet abgeschnitten
		// Bsp:
		// String=<FONT COLOR="#00a000">1.18</font>
		// Keyword1=FONT COLOR="#
		// Keyword2=</font>
		// maxdist=20
		// offset=8
		// Hier wird '1.18' extrahiert
		String ret = null;
		ret = GetKeywordPartMaxdist(zeile, Keyword1, Keyword2, maxdist);
		ret = ret.substring(offset);
		return ret;
	}

	protected String GetKeywordPartMaxdist(String mem, String Keyword1,
			String Keyword2, int maxdist)
	{
		// Holt einen String der zwischen 2 Schlüsselwörtern liegt.
		// Der Zwischenraum zwischen den beiden Keywörtern ist 'maxdist' gross
		int links = 0, rechts = 0, suchoffset = 0;

		boolean b1 = false, b2 = false;

		if (mem == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		// vorüberprüfung:prüfe keywort 1 und 2

		b1 = mem.contains(Keyword1);
		b2 = mem.contains(Keyword2);

		while ((b1 == true) && (b2 == true))
		{// gesuchte wörter sind vorhanden
			links = ((mem).indexOf(Keyword1, suchoffset)) + Keyword1.length();

			// falls einmal durchgelaufen dann nicht gefunden
			if (links < suchoffset)
				return ("0".toString());

			rechts = ((mem).indexOf(Keyword2, links));
			// suche mit offset links

			if ((links < 0) || (rechts < 0))// nicht gefunden
				return ("0".toString());

			if (links > rechts)
				Tracer.WriteTrace(10,
						"Error: internal suchfunktion fehlerhaft links<"
								+ links + "> rechts<" + rechts + ">");

			if ((rechts - links) > maxdist)
			{ // distanz zu gross
				if (links + 1 <= suchoffset)
					Tracer.WriteTrace(10,
							"Error: internal suchfunktion fehlerhaft suchoffset<"
									+ suchoffset + "> links<" + links + ">");
				suchoffset = links + 1;
				continue;
			}

			String retstring = mem.substring(links, rechts);

			return (retstring);
		}

		return ("0".toString());
	}

	protected String GetKeywordTd(String zeile, String part1, String Keyword)
	{// Sucht einen String der hinter
		// einem Keywort liegt, der
		// String wird durch
		// <td>...</td>
		// eingegrenzt <td>31.03.08 13:30</td>

		if (zeile == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		if (zeile.contains(Keyword))
		{
			part1 = zeile.substring(zeile.indexOf(Keyword) + Keyword.length()
					+ 7, zeile.indexOf(Keyword) + 80);
			// System.out.println("part1=" + part1);
			String retstring = part1.substring(part1.indexOf("<td>") + 4,
					part1.indexOf("</td>"));
			if (retstring.equals("-"))
				retstring = retstring.replace('-', '0');
			return (retstring);
		}

		return ("error".toString());
	}

	protected int GetSeitenzahl(String alles)
	{
		String seitenanz_str = null;
		int zahl = 0;
		int faktor = 1;

		if (alles == null)
		{
			Tracer.WriteTrace(10, "E:htmlpagesmem=null");
			return 0;
		}

		while (5 == 5)
		{
			// versuch 0
			seitenanz_str = GetKeywordPartMaxdist(alles, "Seitenzahl===",
					"###", 40);
			if (seitenanz_str.equals("0") == false)
			{
				faktor = 1;
				break;
			}

			/*
			 * // Versuch -1 seitenanz_str = GetKeywordPartMaxdist(alles,
			 * "content=\"Seite "," der Diskussion", 40); if
			 * (seitenanz_str.equals("0") == false) { faktor=1; break;
			 * 
			 * }
			 */

			// erster Versuch
			seitenanz_str = GetKeywordPartMaxdist(alles,
					"Anzahl Beiträge:</p> <p>", "</p>", 40);
			if (seitenanz_str.equals("0") == false)// falls was gefunden
			{
				// durch 10 teilen da postings und nicht pages
				faktor = 10;
				break;
			}

			// zweiter Versuch
			seitenanz_str = GetKeywordPartMaxdist(alles, "Seite 1 von ",
					" (1-10 von", 40);
			if (seitenanz_str.equals("0") == false)
			{
				faktor = 1;
				break;
			}

			// 2.5 Versuch
			seitenanz_str = GetKeywordPartMaxdist(alles, "Seite 1 von ",
					" </div> ", 40);
			if (seitenanz_str.equals("0") == false)
			{
				faktor = 1;
				break;
			}

			// dritter Versuch
			seitenanz_str = GetKeywordPartMaxdist(alles, "ge:</p> <p>", "</p>",
					40);
			if (seitenanz_str.equals("0") == false)
			{
				faktor = 10;
				break;
			}

			// vierter Versuch
			seitenanz_str = GetKeywordZahlnachLeerfeld(alles,
					"Seite 1               von");
			if (seitenanz_str.equals("0") == false)
			{
				// was gefunden
				faktor = 1;
				break;
			}
			// fünfter Versuch
			seitenanz_str = GetKeywordPartMaxdist(alles,
					"Postings 1 bis 10 von ", "\">", 30);
			if (seitenanz_str.equals("0") == false)
			{
				// was gefunden
				faktor = 1;
				break;
			}

			//
			// sechster Versuch
			seitenanz_str = GetKeywordPartMaxdist(alles,
					"<div class=\"activePage\">        Seite ", " von", 70);
			if (seitenanz_str.equals("0") == false)
			{
				// was gefunden
				faktor = 1;
				break;
			}

			Tracer.WriteTrace(20, "W:Keine Seitenzahl im Html-file gefunden");
			return 0;
		}

		// falls was gefunden wurde
		if (seitenanz_str.equals("0") == false)
		{
			// überprüfe ob zahl
			if (is_zahl(seitenanz_str) == true)
			{
				int postzahl = Integer.valueOf(zahlenkorrektur(seitenanz_str));
				int szahl = postzahl / faktor;
				if (faktor == 10)
				{
					// addiere aber nur 1 dazu wenn die postzahl nicht durch 10
					// teilbar
					// bsp: 9230 Postings -> 923 Seiten
					// 9231 Postings -> 924 Seiten

					if (postzahl % 10 != 0)
						szahl = szahl + 1;
				}
				return (szahl);
			} else
				return 0;
		}
		Tracer.WriteTrace(10, "Error: keine Seitenanzahl gefunden in ?");
		return 0; // nix gefunden
	}

	protected String GetSymbol(String alles)
	{
		String symbol = null;
		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		// Versuch 0
		symbol = GetKeywordPartMaxdist(alles, "Symbol===", "###", 20);

		// erster Versuch
		if (symbol.equals("0") == true)
			symbol = GetKeywordPartMaxdist(alles, "Symbol: ", "    </div>", 20);

		// leerzeichen entfernen
		return (symbol.trim());
	}

	protected String GetUrlAktName(String alles)
	// holt den akt-threadnamen aus der ersten webpage
	// dieser wird für den webdownload für den pda verwendet
	// auf dem pda funktioniert das url-forwarding nicht
	{
		String urlteilname = null;

		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		// versuch 0
		urlteilname = GetKeywordPartMaxdist(alles, "UrlAktName===", "###", 300);
		if (urlteilname.equals("0") == false)
			return (urlteilname);

		// versuch 1
		urlteilname = GetKeywordPartMaxdist(alles, "/",
				"')\\\">als Startseite<", 140);
		if (urlteilname.contains("/") == true)
			urlteilname = urlteilname.substring(
					urlteilname.lastIndexOf("/") + 1, urlteilname.length());

		if (urlteilname.equals("0") == true)
		{

			urlteilname = GetKeywordPartMaxdist(alles,
					"www.wallstreet-online.de/diskussion/", "#neuster_beitrag",
					140);
			if (urlteilname.contains("/") == true)
				urlteilname = urlteilname
						.substring(urlteilname.indexOf("/") + 1);
		}

		return (urlteilname);
	}

	protected String GetWkn(String alles)
	{// prüft auf zahl
		String wkn = null;
		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		// Versuch 0
		wkn = GetKeywordPartMaxdist(alles, "Wkn===", "###", 15);
		if (wkn.equals("0") == false)
			return (wkn);

		// Versuch 1
		wkn = GetKeywordPartMaxdist(alles, "WKN: ", "<br>", 15);
		return (wkn);
	}

	protected int GetKwMasterId(String alles)
	{
		String masterid = null;
		int zahl = 0;
		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return 0;
		}
		// versuch 0
		masterid = GetKeywordPartMaxdist(alles, "Masterid===", "###", 30);

		// erster Versuch
		if (masterid.equals("0"))
			masterid = GetKeywordPartMaxdist(alles, "inst_id=",
					"')\" type=\"button", 40);

		// zweiter Versuch
		if (masterid.equals("0"))
			masterid = GetKeywordPartMaxdist(alles, "inst_id=",
					"&amp;market_id", 30);

		// dritter Versuch
		if (masterid.equals("0"))
			masterid = GetKeywordPartMaxdist(alles,
					"aktien.wallstreet-online.de/", ".html", 20);

		if (masterid.equals("0") == true)
			return 0;

		if (is_zahl(masterid) == true)
			return (Integer.valueOf(masterid));
		else
			return 0;
	}

	protected String GetMasterString(String alles)
	{
		String ms = null;
		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return null;
		}
		// versuch 0
		ms = GetKeywordPartMaxdist(alles, "MasterString===", "###", 30);

		// erster Versuch
		if ((ms == null) || (ms.equals("0") == true))
		{
			// Bsp: <p class="stockname"><a
			// href="http://www.wallstreet-online.de/aktien/indo-mines-aktie">Indo
			// Mines</a></p>
			// <p class="stockname"><a
			// href="http://www.wallstreet-online.de/aktien/11533109">Curcas
			// Oil</a></p>

			ms = GetKeywordPartMaxdist(
					alles,
					"class=\"stockname\"><a href=\"http://aktien.wallstreet-online.de/",
					"\">", 150);

			if ((ms == null) || (ms.equals("0")))
				ms = GetKeywordPartMaxdist(
						alles,
						"<p class=\"stockname\"><a href=\"http://www.wallstreet-online.de/aktien/",
						"\">", 150);

			// sonderfall im masterstring ist html, dann ist das nix
			if ((ms.contains(".html") == true) || (Tools.is_zahl(ms) == true))
			{
				Tracer.WriteTrace(20,
						"Warning: defekter Masterstring in Webseite (rohstoffe?)  mstring<"
								+ ms + ">=> filtere raus (ok)");
				return null;
			}

		}
		// zweiter Versuch
		if ((ms == null) || (ms.equals("0") == true))
		{
			// Bsp: <p class="stockname"><a
			// href="http://www.wallstreet-online.de/aktien/indo-mines-aktie">Indo
			// Mines</a></p>
			// <p class="stockname"><a
			// href="http://www.wallstreet-online.de/aktien/11533109">Curcas
			// Oil</a></p>

			if ((ms == null) || (ms.equals("0")))
				ms = GetKeywordPartMaxdist(
						alles,
						"<p class=\"stockname\"><a href=\"http://www.wallstreet-online.de/aktien/",
						"\">", 150);

			// sonderfall im masterstring ist html, dann ist das nix
			if ((ms.contains(".html") == true) || (Tools.is_zahl(ms) == true))
			{
				Tracer.WriteTrace(20,
						"Warning: defekter Masterstring in Webseite  mstring<"
								+ ms + ">=> filtere raus (ok)");
				return null;
			}

		}

		return new String(ms);

	}

	protected String GetAktName(String alles)
	{
		String aktname = null;

		if (alles.contains("Gold PM MiniFuture"))
			System.out.println("found Gold PM MiniFuture");

		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		// versuch 0
		aktname = GetKeywordPartMaxdist(alles, "AktName===", "###", 20);

		// erster Versuch
		if (aktname.equals("0"))
			aktname = GetKeywordPartMaxdist(alles,
					"title=\"Zum Wertpapier\"><b>", "</b>", 10);
		// zweiter Versuch
		if (aktname.equals("0"))
			aktname = GetKeywordPartMaxdist(alles, ".html\">", "</a></p>", 60);

		// dritter Versuch
		if (aktname.equals("0"))
			aktname = GetKeywordPartMaxdist(alles,
					"Thread-Kopf //-->  <div class=\"inst-snapshot\">  <h1>",
					"</h1>", 70);

		// vierter Versuch
		if (aktname.equals("0"))
			aktname = GetKeywordPartMaxdist(
					alles,
					"class=\"stockname\"><a href=\"http://aktien.wallstreet-online.de/",
					"\">", 60);
		// fünfter Versuch
		if (aktname.equals("0"))
			aktname = GetKeywordPartMaxdist(alles, ">", "</a></p>", 30);

		// sechster Versuch
		if (aktname.equals("0"))
			aktname = GetKeywordPartMaxdist(alles, "Weitere Diskussionen zu ",
					"<", 60);

		// filtere sonderzeichen raus
		aktname = this.convFilename(aktname);

		if (aktname.contains("/") == true)
			Tracer.WriteTrace(10, "ERROR: internal error aktname=<" + aktname
					+ ">");

		return (aktname);
	}

	static public String GetThreadIdFromMem(String mem)
	{
		// holt sich aus der datei offline/userhtmlpages/*.html
		// die threadid
		String threadid_str = null;
		int zahl = 0;

		if (mem == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}
		// versuch 0
		threadid_str = GetKeywordMem(mem, "Threadid===", "###");
		if ((threadid_str.length() > 1) && (threadid_str.length() < 12)
				&& (is_zahl(threadid_str) == true))
			return (threadid_str);

		// old pageformat erster versuch
		threadid_str = GetKeywordMem(mem, "thread_id=", "&amp;");
		if ((threadid_str.length() > 1) && (threadid_str.length() < 12)
				&& (is_zahl(threadid_str) == true))
			return (threadid_str);

		// zweiter versuch
		threadid_str = GetKeywordMem(mem,
				"www.wallstreet-online.de/diskussion/", "-");
		if ((threadid_str.length() > 2) && (is_zahl(threadid_str) == true))
			return (threadid_str);

		// dritter versuch
		threadid_str = GetKeywordMem(mem, "<a name=\"", "_");
		if ((threadid_str.length() > 1) && (threadid_str.length() < 12)
				&& (is_zahl(threadid_str) == true))
			return (threadid_str);

		System.out.println("ThreadId not found in Mem !!!");
		return ("0".toString());
	}

	protected String GetPostDateTimeFromUserHtmlPosting(String alles,
			String part)
	{
		// holt sich das aktuelle postdatum+zeit aus der datei
		// offline/userhtmlpages/*.html
		String postdatum_str = null;

		if (alles == null)
		{
			Tracer.WriteTrace(20, "W:htmlpagesmem=null");
			return "0".toString();
		}

		postdatum_str = GetKeywordPart2(alles, part, "<td class=\"right\">",
				"<br />");
		postdatum_str = postdatum_str.substring(postdatum_str
				.indexOf("<td class=\"right\">") + 28);
		if (postdatum_str.length() == 17)
			return (postdatum_str);
		System.out.println("Datumsfehler String<" + postdatum_str
				+ "> ist kein Datum");
		return ("0".toString());
	}

}
