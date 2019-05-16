package html;

import hilfsklasse.FileAccess;
import hilfsklasse.IsValid;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.MidDbObj;
import stores.MidDB;
import stores.ThreadsDB;

public class Threads extends HtmlCore
{
	// diese Klasse beinhaltet die info zu einer Threadseite
	// Ein Thread beinhaltet Masterid, Seitenzahl und Symbol etc...
	private String filename_g = null;
	int tid_g = 0;

	// Hier wird die middb als einzigstes verwaltet
	// und zwar nur einmal, da static
	private MidDB middb_glob = null;
	

	private int Threadtype = 0; // Aktienthread, oder sofathread

	public Threads(MidDB middb)
	{
		middb_glob = middb;

	}

	public void entferneEndmuell()
	{
		//Der Endmüll mit der Werbung und den Links wird entfernt.
		super.entferneEndmuell();
		
	}
	
	

	public Threads(MidDB middb, ThreadsDB tdb, String fnam, int tid)
	{
		filename_g = fnam;
		tid_g = tid;
		middb_glob = middb;

		super.ReadHtmlPage(fnam);
		GetSeitentype();

	}

	protected Boolean GetSeitentype()
	{
		if (htmlseite_str == null)
		{
			Tracer.WriteTrace(20, "W:htmlseite fehlerhaft fnam<" + filename_g
					+ ">, nix geladen 01");
			Threadtype = GC.ThreadFehlerhaft;
			return false;
		}

		if ((GetMasterId(GC.NOWARNING) > 0)
				&& (GetSymbol(GC.NOWARNING).length() > 2))
		{
			Threadtype = GC.AktienThread;
		} else
			Threadtype = GC.Sofathread;
		return true;
	}

	public int holeSeitentype()
	{
		// Seite ist a)Aktienthread b)Sofathread oder c)defekt
		if (htmlseite_str == null)
		{
			Tracer.WriteTrace(20, "W:htmlseite fehlerhaft fnam<" + filename_g
					+ ">, nix geladen 01");
			Threadtype = GC.ThreadFehlerhaft;
			return Threadtype;
		}

		if ((GetMasterId(GC.NOWARNING) > 0)
				&& (GetSymbol(GC.NOWARNING).length() > 2))
		{
			Threadtype = GC.AktienThread;
		} else
			Threadtype = GC.Sofathread;

		return Threadtype;
	}

	public int GetMasterId(int flag)
	{
		// holt die masterid aus der htmlseite. Wenn dort keine Masterid drin
		// ist wird die aus der middb
		// geholt.

		int masterid = 0;
		// falls flag != 0 wird eine warnung ausgegeben
		// GetMasterid(NOWARNING) erzeugt keine Warnung falls was nicht stimmt
		int mid = this.GetKwMasterId(htmlseite_str);
		String mstring = GetMasterString(1);

	
			
		
		Tracer.WriteTrace(50, "Webseite<" + filename_g + "> mid<" + mid
				+ "> mstring<" + mstring + ">");

		if(mstring!=null)
			mstring=mstring.toUpperCase();
		
		
		// holt middbobj
		MidDbObj mobj = (MidDbObj) middb_glob.holeTidObj(tid_g);

		if (mobj == null)
		{
			// falls tid nicht in middb dann generiere und speichere
			if ((mstring != null) && (mstring.length() > 2) && (mid != 0))
			{
				mobj = new MidDbObj();
				mobj.setMasterstring(mstring);
				mobj.setMid(mid);
				mobj.setTid(tid_g);
				middb_glob.AddObject(mobj);
				middb_glob.WriteDB();
			}
		}

		// falls die webseite einen Masterstring hat, dann setzte in der middb
		if ((mstring != null) && (mstring.equals("0") == false))
		{
			// Masterstring in Webseite und in middb schon gesetzt
			String masterstring = mobj.getMasterstring().toUpperCase();
			if ((masterstring != null) && (masterstring.equals("0") == false)
					&& (masterstring.equals("null") == false))
			{
				// falls webmasterstring != masterstring in der middb, dann
				// plausierror
				if (mstring.equals(masterstring) == false)
				{
					Tracer.WriteTrace(20,
							"Error: plausi middb inkonsistent tid<" + tid_g
									+ "> mstring<" + mstring
									+ "> masterstring middb<" + masterstring
									+ ">");
					return mid;
				}
				// setze Masterstring
				mobj.setMasterstring(mstring);

			}
			// Masterstring noch nicht gesetzt => setze und speichere
			else
			{
				// setze Masterstring
				mobj.setMasterstring(mstring);
				middb_glob.WriteDB();
			}
		}

		// masterid konnte für die Webseite nicht gefunden werden, seite defekt
		// => lösche die
		// Jede Webseite muss eine Mid haben, ausser Sofathreads

		if ((mid == 0) && (Threadtype == GC.AktienThread))
		{
			Tracer.WriteTrace(10, "Warning: Webseite defekt keine masterid <"
					+ masterid + "> und masterstring<" + mstring
					+ "> aber Aktienthreads => lösche <" + filename_g
					+ "> (bestät)");
			if (FileAccess.FileAvailable0(filename_g))
				FileAccess.FileDelete(filename_g, 1);
		}
		return mid;
	}

	public String GetBoerplatz(int flag)
	{
		String str = null;

		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, " <div class=\"market\">",
				",", 30);

		if ((str.equals("0") || (str == null)) && (flag == 0))
			Tracer.WriteTrace(20, "W:no Boerplatz in htmlfile <" + filename_g
					+ ">");
		return str;
	}

	public String GetMasterString(int flag)
	{
		// Es gibt nur Masterid´s als integer !!!!!
		// falls flag != 0 wird eine warnung ausgegeben
		// GetMasterstring(NOWARNING) erzeugt keine Warnung falls was nicht
		// stimmt
		String mstr = this.GetMasterString(htmlseite_str);
		if ((flag == 0) && ((mstr == null) || (mstr.equals("0"))))
			Tracer.WriteTrace(20, "W:no masterstring in htmlfile <"
					+ filename_g + ">");
		return mstr;
	}

	public int GetSeitenzahl()
	{
		int sz = this.GetSeitenzahl(htmlseite_str);
		if (sz < 0)
		{
			Tracer.WriteTrace(10, "W:no pageanz in htmlfile <" + filename_g
					+ ">");

		}
		return (sz);
	}

	public String GetSymbol(int flag)
	{
		// falls flag == 0 wird eine warnung ausgegeben
		// GetSymbol(NOWARNING) erzeugt keine Warnung falls was nicht stimmt
		String str = new String(this.GetSymbol(htmlseite_str));
		if ((str.length() < 2) && (flag == 0))
			Tracer.WriteTrace(20, "W:no symbol <" + str + "> in htmlfile <"
					+ filename_g + ">");
		return str;
	}

	public String GetUrlAktName()
	{
		String str = new String(this.GetUrlAktName(htmlseite_str));
		if (str.length() < 2)
		{
			Tracer.WriteTrace(20, "Error:no UrlAktName <" + str
					+ "> in htmlfile I delete it <" + filename_g + ">");
			if (FileAccess.FileAvailable(filename_g) == true)
				FileAccess.FileDelete(filename_g, 0);
		}
		return str;
	}

	public String GetAktName(int flag)
	{
		// falls flag != 0 wird keine warnung ausgegeben
		String str = new String(this.GetAktName(htmlseite_str));
		if ((str.length() < 2) && (flag == 0))
			Tracer.WriteTrace(20, "W:no AktName <" + str + "> in htmlfile <"
					+ filename_g + ">");

		str=IsValid.ThreadnameNachverarbeitung(str);
		return str;
	}

	public String GetWkn()
	{

		String str = new String(this.GetWkn(htmlseite_str));
		return str;
	}

	public int GetThreadId()
	{
		String str = new String(this.GetThreadIdFromMem(htmlseite_str));

		if (str.length() < 2)
		{
			Tracer.WriteTrace(20, "W:no Threadid <" + str + "> in htmlfile <"
					+ filename_g + "> I delete it");
			if (FileAccess.FileAvailable(filename_g) == true)
				FileAccess.FileDelete(filename_g, 0);
		} else
		// plausi
		if ((tid_g != 0) && (String.valueOf(tid_g).equals(str) == false))
		{
			Tracer.WriteTrace(20, "Error: internal tid<" + str
					+ "> erwartete tid<" + tid_g + "> ungleich !!! => Lösche");
			Tracer.WriteTrace(20, "Lösche Webseite <" + filename_g + ">");
			if (FileAccess.FileAvailable(filename_g) == true)
				FileAccess.FileDelete(filename_g, 0);
			return 0;
		}
		return Integer.valueOf(str);
	}

	public int GetBeitragsanzahl()
	{
		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "Betragsanzahl===", "###",
				20);

		// Versuch 2
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Anzahl Beiträge:</span> <span>", "</span><br>", 60);

		// Versuch 2.1 (Webseite fehlerhaft)
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Anzahl Beiträge:</span> <span>", "</span><br/>", 60);

		// Versuch 3
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Anzahl Beiträge:</p> <p>", "</p>", 20);

		// Versuch 4
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Erste Seite - Postings 1 bis 10 von ", "\">", 40);

		// Versuch 5

		if (str.equals("0") == true)
		{
			str = GetKeywordBytes(htmlseite_str,
					"Gelesen&nbsp;heute:<br />        Forum:<br />", 200);
			str = nteElement(str, "<br />", 2);
			str = GetKeywordZahlnachLeerfeld(str,
					"</td>      <td class=\"left\">");
		}

		if(str.equals("-")==true)
		{
			Tracer.WriteTrace(20, "I: Keine Beitragsanzahl in fnam<"+filename_g+">");
			return 0;
		}
		
		if((str==null)||(str.equals("")))
				Tracer.WriteTrace(10, "Error: Keine Beitragsanzahl in fnam<"+filename_g+">");
		
		int val = Integer.valueOf(zahlenkorrektur(str));
		if (val == 0)
		{
			Tracer.WriteTrace(20, "W:no Anzahl Beiträge <" + val
					+ "> in htmlfile <" + filename_g + "> ");

		}
		return (val);
	}

	public String GetEroeffnetAm()
	{
		if (htmlseite_str==null)
			return ("0");
		
		if (htmlseite_str.length() == 0)
			Tracer.WriteTrace(10, "Error: Seitenlänge0 file<" + filename_g
					+ ">");

		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "EroeffnetAm===", "###", 20);
		// Versuch 2
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(
					htmlseite_str,
					"eröffnet am:        </p>        <p class=\"timeuser\">           ",
					" von:", 60);

		// Versuch 3
		if (str.equals("0") == true)
		{
			str = GetKeywordBytes(htmlseite_str,
					"Thread&nbsp;er&ouml;ffnet&nbsp;von:<br />        am:", 500);
			str = GetKeywordBytes(str, "title=\"Threads des Users", 90);
			str = GetKeywordDatumImLeerfeld(str, "/>");
		}
		// Versuch 4
		if (str.equals("0") == true)
		{
			if(htmlseite_str.contains("eröffnet am	")==true)
			{
			str = htmlseite_str
					.substring(htmlseite_str.indexOf("eröffnet am	"));
			if (str.length() > 8)
				str = str.substring(12, 12 + 8);
			}
		}

		if (str.length() < 2)
		{
			Tracer.WriteTrace(20, "E:no Eroeffnet am <" + str
					+ "> in htmlfile <" + filename_g + ">");

		}

		return (new String(str));
	}

	public String GetEroeffnetVon()
	{
		String str = null, substr = null;

		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "EroeffnetVon===", "###", 20);

		// Versuch2
		if (str.equals("0") == true)
		{
			str = GetKeywordPartMaxdist(htmlseite_str,
					"<div style=\"float: left;\"><b>", "</b></div>", 50);
		}

		// Versuch 3
		if (str.equals("0") == true)
		{
			substr = GetKeywordBytes(htmlseite_str, "eröffnet am", 300);
			str = GetKeywordPartMaxdist(substr, "Threads des Users\">", "</a>",
					30);
		}
		// versuch 4
		if (str.equals("0") == true)
		{
			str = GetKeywordBytes(htmlseite_str,
					"Thread&nbsp;er&ouml;ffnet&nbsp;von:<br />        am:", 500);
			str = GetKeywordPartMaxdist(str, "Threads des Users\">", "</a>", 90);
		}

		// versuch 5
		if (str.equals("0") == true)
		{
			substr = GetKeywordBytes(htmlseite_str, "eröffnet am", 300);
			str = GetKeywordPartMaxdist(substr,
					"<div style=\"float:left;\"><b>", "</b>", 90);
		}
		// versuch 5
		if (str.equals("0") == true)
		{
			substr = GetKeywordBytes(htmlseite_str, "eröffnet am", 300);
			str = GetKeywordPartMaxdist(substr,
					"<div style=\"float:left;\">", "</div>", 90);
		}
		// versuch 6
		if (str.equals("0")== true)
		{
			
			str = GetKeywordPartMaxdist(htmlseite_str,
					"<div class=\"threadUser\">                        <div style=\"float:left;\"><b>", "<font color=\"green\">", 180);
			
		}
		
		if (str.length() < 2)
		{
			Tracer.WriteTrace(20, "W:no Eroeffnet von <" + str
					+ "> in htmlfile <" + filename_g + "> set emty !!");
		}
		str=new String("**NOT**FOUND**");
		return (new String(str));
	}

	public int GetLeserGesammt()
	{
		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "LeserGesammt===", "###", 20);
		// Versuch 2
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str, "Leser gesamt:</p> <p>",
					"</p>", 20);
		// versuch 3
		if (str.equals("0") == true)
		{
			str = GetKeywordBytes(htmlseite_str,
					"Gelesen&nbsp;heute:<br />        Forum:<br />", 120);

			if (str.length() > 50)
			{
				str = str.substring(50);
				str = GetKeywordZahlnachLeerfeld(str, "<br />");
			} else
				str = "0";
		}

		// versuch 4
		if (str.equals("0") == true)
		{
			String substr = GetKeywordBytes(htmlseite_str,
					"Leser gesamt:</span>", 300);
			if (substr.length() > 50)
			{
				str = GetKeywordPartMaxdist(substr, "<span>", "</span>", 50);
			} else
				str = "0";
		}

		// versuch 5
		if (str.equals("0") == true)
		{
			String substr = GetKeywordBytes(htmlseite_str,
					"Aufrufe gesamt:</span>", 300);
			if (substr.length() > 50)
			{
				str = GetKeywordPartMaxdist(substr, "<span>", "</span>", 50);
			} else
				str = "0";
		}
		
		int val = Integer.valueOf(zahlenkorrektur(str));
		if (val < 2)
		{
			Tracer.WriteTrace(20, "E:no LeserGesammt <" + val
					+ "> in htmlfile <" + filename_g + "> ");
			return 0;
		}
		return (val);
	}

	public int GetLeserHeute()
	{
		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "LeserHeute===", "###", 20);
		// Versuch 2
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str, "davon heute:</p> <p>",
					"</p>", 20);
		
		// versuch 3
		if (str.equals("0") == true)
		{
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Leser heute:</span> <span>", "</span><br/>", 60);

		}
		// versuch 4
		if (str.equals("0") == true)
		{
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Aufrufe heute:</span> <span>", "</span><br/>", 60);

		}
		int val = Integer.valueOf(zahlenkorrektur(str));

		return (val);
	}

	public String GetDisknr()
	{
		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "Disknr===", "###", 20);
		// Versuch 2
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Diskussionsnr.:</p> <p>", "</p>", 20);

		// Versuch 2.5
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str,
					"Diskussionsnr.:</span> <span>", "</span>", 20);
		
		
		// versuch 3
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(
					htmlseite_str,
					"setHomePage('http://www.wallstreet-online.de/community/thread/",
					"-", 60);
		
		// versuch 4
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(
					htmlseite_str,
					"href=\"/diskussion/",
					"-", 60);
		
		if (str.length() < 2)
		{
			Tracer.WriteTrace(20, "Error:no Diskussionsnr <" + str
					+ "> in htmlfile <" + filename_g + ">");
		}
		return (new String("0"));
	}

	public String GetNeusterBeitrag()
	{
		// dieser neuster Beitrag steht in den unkompresseten Htmlseiten
		// diese info wird nur einmal ausgewertet

		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "NeusterBeitrag===", "###",
				20);
		// Versuch 2
		if (str.equals("0") == true)
		{
			str = GetKeywordPartMaxdist(htmlseite_str,
					"neuster_beitrag\">neuster Beitrag</a>: ", "</p>", 150);
			if (str.length() > 2)
			{
				str = str.replace(" |", "");
				str = str.substring(0, 19);
				return (new String(str));
			}
		}
		//Versuch 3
		//neuster_beitrag">neuster Beitrag</a> 01.08.11 17:18:19<br/>
		if (str.equals("0") == true)
		{
			str = GetKeywordPartMaxdist(htmlseite_str,
					"neuster_beitrag\">neuster Beitrag</a>", "<br/>", 150);
			
			
			
			if (str.length() > 2)
			{
				str = str.replace(" |", "");
				str = str.substring(1, 18);
			}
			return (new String(str));
		}
		
		
		if (str.length() < 1)
		{
			Tracer.WriteTrace(10, "Error:no neuster Beitrag<" + str
					+ "> in htmlfile <" + filename_g + ">");
		}
		return (new String(str));
	}

	public String GetIsin()
	{
		// firstcheckflag==1=> bleib stehn bei fehler
		// firstcheckflag=0, dann mache weiter
		String str = null;
		// Versuch 1
		str = GetKeywordPartMaxdist(htmlseite_str, "Isin===", "###", 20);
		// Versuch 2
		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str, "ISIN: ", "<br>", 20);
		if (str.length() < 5)
			Tracer.WriteTrace(20, "W:no Isin <" + str + "> in htmlfile <"
					+ filename_g + ">");
		return (new String(str));
	}

	public String toString()
	{
		if (Threadtype == GC.AktienThread)
			return (GetMasterId(GC.DEFAULT) + "#" + GetSeitenzahl() + "#"
					+ GetSymbol(GC.DEFAULT) + "#" + GetUrlAktName() + "#"
					+ GetAktName(GC.DEFAULT) + "#" + GetWkn() + "#" + GetThreadId());
		else if (Threadtype == GC.Sofathread)
			return ("0#" + GetSeitenzahl() + "#" + "0#" + GetUrlAktName() + "#"
					+ "0#" + GetWkn() + "#" + GetThreadId());
		else
			Tracer.WriteTrace(10, "Error: unknown threadtype <" + Threadtype
					+ ">");
		return (null);
	}

	public String GetNextPostingString()
	{

		String postline = GetFullHtmlPosting();

		return postline;

	}

	public int GetPostinganzahl()
	{
		String postline = null;
		int postanz = 0;
		while (((postline = GetFullHtmlPosting()) != null)
				&& (postline.length() > 5))
			postanz++;
		return postanz;
	}

	public String GetHtmlElements(int erwtid)
	// holt die relevanten Infos aus der Threadpage und stellt diese als
	// HTML-Info
	// zur Verfügung
	// Wird für die Html-Seitekompression verwendet
	// erwtid: erwartete tid
	// fehlerpruefflag=1, threadseite wird auf fehler überprüft und im
	// fehlerfall wird gestoppt.
	{

		if (Threadtype == GC.AktienThread)
		{
			String clstring = "Masterid===" + GetMasterId(GC.DEFAULT) + "###"
					+ "Seitenzahl===" + GetSeitenzahl() + "###" + "Symbol==="
					+ GetSymbol(GC.DEFAULT) + "###" + "UrlAktName==="
					+ GetUrlAktName() + "###" + "AktName==="
					+ GetAktName(GC.DEFAULT) + "###" + "Wkn===" + GetWkn()
					+ "###" + "Threadid===" + GetThreadId() + "###"
					+ "Betragsanzahl===" + GetBeitragsanzahl() + "###"
					+ "EroeffnetAm===" + GetEroeffnetAm() + "###"
					+ "EroeffnetVon===" + GetEroeffnetVon() + "###"
					+ "LeserGesammt===" + GetLeserGesammt() + "###"
					+ "LeserHeute===" + GetLeserHeute() + "###" + "Disknr==="
					+ GetDisknr() + "###" + "Isin===" + GetIsin() + "###"
					+ "Masterstring===" + GetMasterString(0) + "###" + GetNeusterBeitrag()+"###";
			return clstring;
		} else if (Threadtype == GC.Sofathread)
		{
			String clstring = "Masterid===" + "0###" + "Seitenzahl==="
					+ GetSeitenzahl() + "###" + "Symbol===" + "0###"
					+ "UrlAktName===" + GetUrlAktName() + "###" + "AktName==="
					+ "0###" + "Wkn===" + GetWkn() + "###" + "Threadid==="
					+ GetThreadId() + "###" + "Betragsanzahl==="
					+ GetBeitragsanzahl() + "###" + "EroeffnetAm==="
					+ GetEroeffnetAm() + "###" + "EroeffnetVon==="
					+ GetEroeffnetVon() + "###" + "LeserGesammt==="
					+ GetLeserGesammt() + "###" + "LeserHeute==="
					+ GetLeserHeute() + "###" + "Disknr===" + GetDisknr()
					+ "###" + "Isin===" + "0###" + "Masterstring===" + "0"+"###"+GetNeusterBeitrag()+"###";
			return clstring;
		}
		Tracer
				.WriteTrace(10, "Error: unknown threadtype x<" + Threadtype
						+ ">");
		return null;
	}

}
