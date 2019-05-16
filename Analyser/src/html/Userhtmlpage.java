package html;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;

public class Userhtmlpage extends HtmlCore
{
	// in der Userhtmlpage stehen die Grunddaten eines Benutzers
	// wie anz. Postings, zugehörigkeit etc.
	private String filename = null;

	public Userhtmlpage(String fnam)
	{
		filename = fnam;
		super.ReadHtmlPage(fnam);
	}

	
	
	
	public String getRegistriertSeit()
	{
		// Versuch 1
		String str = GetKeywordPartMaxdist(htmlseite_str, "RegistriertSeit===",
				"###", 20);

		// Versuch 2
		if (str.equals("0") == true)
			str = new String(this.GetKeyword("Registriert seit", 38, 48));

		if ((str.contains("</td>")) || (str.contains("error")))
		{
			str = new String("0");
			return str;
		}
		if (str.length() < 2)
		{
			Tracer.WriteTrace(20, "Error:no Registriert seit <" + str
					+ "> in htmlfile <" + filename + "> I delete it");
			if (FileAccess.FileAvailable(filename) == true)
				FileAccess.FileDelete(filename,0);
		}

		return str;
	}

	public boolean checkUserErrorInformation()
	{
		// prüft ob der benutzername überhaupt gültig ist
		if (htmlseite_str == null)
		{
			Tracer.WriteTrace(20, "I:die htmlseite <" + filename
					+ "> ist leer'");
			return false;
		}

		if (htmlseite_str.contains("(Mitgliedschaft durch User beendet)"))
		{
			Tracer.WriteTrace(20, "I:die htmlseite <" + filename
					+ "> beinhaltet '(Mitgliedschaft durch User beendet)'");
			return true;
		}
		if (htmlseite_str.contains("(Mitgliedschaft durch User beendet)"))
		{
			Tracer.WriteTrace(20, "I:die htmlseite <" + filename
					+ "> beinhaltet '(Mitgliedschaft durch User beendet)'");
			return true;
		}

		return false;
	}

	public int getUserid()
	{
		// Versuch 1
		String str = GetKeywordPartMaxdist(htmlseite_str, "Userid===", "###",
				20);

		// Versuch 2
		if (str.equals("0") == true)
			str = this.GetKeywordPart("/userinfo/", ".html");

		if ((str==null)||(str.length() == 0)||(str.equals("0")))
		{
			Tracer.WriteTrace(20, "Error:no Userid <" + str + "> in htmlfile <"
					+ filename + "> I delete it");
			if (FileAccess.FileAvailable(filename) == true)
				FileAccess.FileDelete(filename,0);
		}
		int val = Integer.valueOf(zahlenkorrektur(str));
		return val;
	}
	public int getWoExpertenflag()
	{
		// Versuch 1
		String str = GetKeywordPartMaxdist(htmlseite_str, "WoExperte===", "###", 25);

		if (str.equals("0") == true)
			if(htmlseite_str.contains("w:o Experte")==true)
				return 1;
			else
				return 0;
		return 0;
	}
	public int getThemen()
	{
		String str = null;

		// Versuch 1
		if (htmlseite_str.contains("Themen==="))
		{
			str = GetKeywordPartMaxdist(htmlseite_str, "Themen===", "###", 20);
			int val = Integer.valueOf(zahlenkorrektur(str));
			return val;
		}
		// Versuch 2

		str = this.GetKeywordTd("Erstellte Themen");

		int val = Integer.valueOf(zahlenkorrektur(str));
		return val;
	}

	public int getAntworten()
	{
		// Versuch 1
		String str = GetKeywordPartMaxdist(htmlseite_str, "Antworten===",
				"###", 20);

		if (str.equals("0") == true)
			str = this.GetKeywordTd("Erstellte Antworten");

		int val = Integer.valueOf(zahlenkorrektur(str));
		return val;
	}

	public float getBeitraegeTag()
	{

		// Versuch 1
		String str = GetKeywordPartMaxdist(htmlseite_str, "BTag===", "###", 25);

		if (str.equals("0") == true)
			str = GetKeywordPartMaxdist(htmlseite_str, "Durchschnittlich <b>",
					"</b>", 25);

		str = str.replaceAll(",", ".");
		float val = Float.valueOf(str);
		return val;
	}


	
	public String GetHtmlElements()
	// holt die relevanten Infos aus der Threadpage und stellt diese als
	// HTML-Info
	// zur Verfügung
	// Wird für die Html-Seitekompression verwendet
	{
		String clstring = "RegistriertSeit===" + getRegistriertSeit() + "###"
				+ "Userid===" + getUserid() + "###" + "Themen===" + getThemen()
				+ "###" + "Antworten===" + getAntworten() + "###" + "BTag==="
				+ getBeitraegeTag() + "###";

		return clstring;
	}
}
