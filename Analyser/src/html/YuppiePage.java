package html;

import hilfsklasse.Tracer;

public class YuppiePage extends HtmlCore
{
	YuppiePage()
	{
	}

	public String GetIsin()
	{
		String str = GetKeywordPartMaxdist(htmlseite_str, "search.pl?isin=",
				"&typnr=", 20);

		if ((str == null) || (str.equals("0") == true))
		{
			Tracer.WriteTrace(20,
					"Warning= Yuppie WKN fehlerhaft aufgebaut, Schlüsselwort Isin nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");
			return null;
		}
		return str;
	}

	public String GetBranchesektor()
	{
		String str = GetKeywordPartMaxdist(htmlseite_str,
				"Aktien: Branche, Sektor'; return true;\" >", "</a><", 100);

		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Branchesektor nicht gefunden <"
							+ this.getClass().getName() + ">");

		return str;
	}

	public String GetLand()
	{
		String str = GetKeywordPartMaxdist(htmlseite_str,
				"window.status='Aktien: Land'; return true;\" >", "</a>", 80);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10, "Error= Schlüsselwort Land nicht gefunden <"
					+ Thread.currentThread().getStackTrace() + "> ");

		return str;
	}

	public String GetAktKurs()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str, "class=\"head\">Kurs-Info</TD>",
				200);
		str = GetKeywordPartMaxdist(str, "<FONT COLOR=\"#", "</font>", 30);
		str = str.substring(str.indexOf(">") + 1);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10, "Error= Schlüsselwort Kurs nicht gefunden <"
					+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetKursDatum()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				"Prognose (EUR)</TD><TD>&Auml;nderung (%)</TD></TR><TR class=\"data\"><TD ALIGN=\"right\" nowrap>",
				"</TD>", 80);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(20,
					"Warning= Schlüsselwort KursDatum nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetHandelssignal()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str,
				"<TD>Handelssignal:</TD><TD ALIGN=\"right\"><IMG SRC=\"/pics",
				400);
		str = GetKeywordPartMaxdist(str, "<FONT COLOR=\"", "</FONT>", 60);
		str = str.substring(str.indexOf(">") + 1);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Handelssignalnicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetHandelsvalue()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str,
				"<TD>Handelssignal:</TD><TD ALIGN=\"right\"><IMG SRC=\"/pics",
				200);
		str = GetKeywordPartMaxdist(str, "</FONT>", "</TD></TR>", 20);
		str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Handelsvalue nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetHandelssignalSeit()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str,
				"<TD>Handelssignal:</TD><TD ALIGN=\"right\"><IMG SRC=\"/pics",
				600);
		str = GetKeywordPartMaxdist(str, "class=\"data\"><TD colspan=\"2\">",
				"</TD>", 160);

		return str;

	}

	public String GetRisikoklasse()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str, "Risikoklasse:", 100);
		str = GetKeywordPartMaxdist(str, "\">", "</FONT>", 20);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Risikoklasse nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetVolatilität()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str, "Volatilit&auml;t:", 100);
		str = GetKeywordPartMaxdist(str, "\">", "</FONT>", 20);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Volatilität nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetKursNachCrash()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str, "Kurs nach Crash:", 100);
		str = GetKeywordPartMaxdist(str, "<TD> ", " EUR", 20);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort KursCrash nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetVolumenHeute()
	{
		String str = null;
		str = GetKeywordPartMaxdist(htmlseite_str,
				"<TD>Volumen heute</TD><TD align=\"right\">", "</TD>", 120);

		return str;
	}

	public String GetStärke()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str, "Relative St&auml;rke (9 Tage):",
				200);
		str = GetKeywordPartMaxdist(str, "\">", "</FONT>", 40);

		return str;
	}

	public String GetBenchmark()
	{
		String str = null;
		// erster versuch
		str = GetKeywordPartMaxdist(
				htmlseite_str,
				"onMouseOver=\"window.status='Indexprognose'; return true;\" >",
				"</a>", 600);

		// zweiter versuch
		if ((str == null) || (str.equals("0") == true))
			str = GetKeywordPartMaxdist(htmlseite_str, "Benchmark: </TD><TD>",
					"</a>", 100);

		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Benchmark nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetBörse()
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str,
				"onMouseOver=\"window.status='Aktien: Land", 200);
		str = GetKeywordPartMaxdist(str, "</TD><TD>", "</TD>", 60);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10, "Error= Schlüsselwort Börse nicht gefunden <"
					+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetPrognoseN(int n)
	{
		String str = null;
		str = GetKeywordBytes(htmlseite_str,
				"Prognosezeit</TD></TR><TR class=\"data\"><TD ALIGN=\"right\"",
				1200);
		str = nteElement(str, "ALIGN=\"right\"><FONT COLOR", 1 + ((n - 1) * 2));
		str = GetKeywordPartMaxdist(str, "=\"#", "</font>", 20, 8);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10, "Error= Schlüsselwort Prognose <" + n
					+ "> nicht gefunden <"
					+ Thread.currentThread().getStackTrace() + ">");

		return str;
	}

	public String GetBetrachtungszeitraum()
	{
		String str = null;
		str = GetKeywordPartMaxdist(htmlseite_str,
				"Betrachtungszeitraum:&nbsp;&nbsp;", "</TD></TR>", 60);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Betrachtungszeitraum nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");
		return str;
	}

	public String GetGewinn()
	{
		String str = null;
		str = GetKeywordPartMaxdist(
				htmlseite_str,
				"Gewinn bei Befolgung der Prognosen: </TD><TD ALIGN=\"right\"><FONT COLOR=\"#",
				"</font>", 120, 8);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Gewinn nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");
		return str;
	}

	public String GetÄnderung()
	{
		String str = null;
		str = GetKeywordPartMaxdist(
				htmlseite_str,
				"&Auml;nderung der Aktie:&nbsp;</TD><TD ALIGN=\"right\"><FONT COLOR=\"#",
				"</font>", 120, 8);
		if ((str == null) || (str.equals("0") == true))
			Tracer.WriteTrace(10,
					"Error= Schlüsselwort Änderung nicht gefunden <"
							+ Thread.currentThread().getStackTrace() + ">");
		return str;
	}
}
