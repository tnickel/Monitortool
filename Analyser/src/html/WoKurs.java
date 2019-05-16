package html;

import hilfsklasse.SG;
import hilfsklasse.Tracer;

public class WoKurs extends HtmlCore
{
	// die klasse wertet die htmlseite von wo aus
	// hier wird der geladene Kurs gelesen
	private String filename_g = null;
	int tid_g = 0;

	public WoKurs()
	{

	}

	public WoKurs(String fnam)
	{
		super.ReadHtmlPage(fnam);
		filename_g = fnam;
	}

	private float getKursVal(String str)
	{

		String vorkomma = "";
		String nachkomma = "";
		String floatstring = "";
		// extrahiere vor dem Komma
		if (str.contains(","))
		{
			vorkomma = str.substring(0, str.indexOf(","));
			nachkomma = str.substring(str.indexOf(",") + 1);
			// punkte entfernen
			vorkomma = vorkomma.replace(".", "");
			nachkomma = nachkomma.replace(".", "");

			floatstring = vorkomma + "." + nachkomma;
			float val = Float.valueOf(floatstring);
			return val;
		} else if (str.contains("-"))
		{
			return 0;
		} else
		{
			Tracer.WriteTrace(20, "Error: Kursseitenaufbau <" + str + ">");
			return 0;
		}
	}

	private float getVolumenVal(String str)
	{

		// komma abschneiden
		if (str.contains(","))
			str = str.substring(0, str.indexOf(","));
		// punkte entfernen falls da
		str = str.replace(".", "");
		str = str.replaceAll("-", "0");
		float val = Float.valueOf(str);
		return val;
	}

	public float GetAktuellKurs()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				">Aktueller Kurs:                                     </td><td class=\"right\">",
				"</td></tr>", 40);
		float val = getKursVal(str);
		return val;
	}

	public float GetEroeffnung()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				">Er&ouml;ffnungskurs:                                </td><td class=\"right\">",
				"</td></tr>", 40);
		float val = getKursVal(str);
		return val;
	}

	public float GetTief()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				">Tagestief:                                          </td><td class=\"right\">",
				"</td></tr>", 40);
		float val = getKursVal(str);
		return val;
	}

	public float GetHoch()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				">Tageshoch:                                          </td><td class=\"right\">",
				"</td></tr>", 40);
		float val = getKursVal(str);
		return val;
	}

	public float GetSchluss()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				">Schlusskurs:                                        </td><td class=\"right\">",
				"</td></tr>", 40);
		float val = getKursVal(str);
		return val;
	}

	public float GetVolumen()
	{
		String str = GetKeywordPartMaxdist(
				htmlseite_str,
				">Volumen:                                            </td><td class=\"right\">",
				"</td></tr>", 40);
		str = SG.zahlenkorrektur(str);
		float val = getVolumenVal(str);
		return val;
	}
}
