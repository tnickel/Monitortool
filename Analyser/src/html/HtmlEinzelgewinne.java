package html;

import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import objects.UserEinPostingGewinnObj;

public class HtmlEinzelgewinne extends HtmlTabelle
{
	public HtmlEinzelgewinne(String nam)
	{
		this.setfilename(nam);
	}

	public void closeFile()
	{
		this.cFile();
	}

	public void addKopf(String ueberschrift, String tabellenkopf)
	{
		this.Kopf(ueberschrift);

		// Kopfzeile
		addKopfleiste(tabellenkopf);
	}

	private void buildZeile(Inf inf, String farbe, String teilzeile)
	{
		String tmpzeile = "<td><font color=" + farbe + ">";
		tmpzeile = tmpzeile.concat(teilzeile);
		tmpzeile = tmpzeile.concat("</td>");
		inf.writezeile(tmpzeile);
	}

	public void addZeile(UserEinPostingGewinnObj gewinnobj, String gewinnzeile)
	{
		// "Aktname@Symb@Kursinfo@Datum@Tid@k1@k2@EinzelGew@Summe"
		String farbe = null;
		try
		{
			// Zeilenanfang
			sz("<tr>");

			
			String datum = Tools.entferneZeit(gewinnobj.getDatum());
			String tname = gewinnzeile.substring(0,
					gewinnzeile.indexOf(":") - 1);

			String gsumme = gewinnzeile.substring(gewinnzeile.lastIndexOf(":"));

			buildZeile("black", WLink("http://aktien.wallstreet-online.de/"
					+ gewinnobj.getMid() + ".html", tname));
			buildZeile("black", gewinnobj.getSymb());
			buildZeile("black", gewinnobj.getKursinfo());
			buildZeile("black", datum);
			buildZeile("black", Integer.toString(gewinnobj.getTid()));
			buildZeile("black", Float.toString(gewinnobj.getVal1()));
			buildZeile("black", Float.toString(gewinnobj.getVal2()));
			float gew = gewinnobj.getGewinn();
			if (gew > 0)
				farbe = "black";
			else
				farbe = "red";
			buildZeile(farbe, Float.toString(gewinnobj.getGewinn()));
			buildZeile("black", gsumme);
			// buildZeile("black",HLink(GC.rootpath+"\\db\\UserThreadVirtualKonto\\Einzelgewinne\\"+udbobj_s.getName()+".txt","Einz"));

			// Zeilenende
			sz("</tr>");
		}

		catch (StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(20, "zeile<" + gewinnzeile + ">");
		}
	}
}
