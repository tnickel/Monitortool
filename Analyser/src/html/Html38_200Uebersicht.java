package html;

import hilfsklasse.Tools;
import objects.Obj38200;
import stores.SlideruebersichtDB;

public class Html38_200Uebersicht extends HtmlTabelle
{
	
	public Html38_200Uebersicht(String fnam)
	{
		super.setfilename(fnam);
	}

	public void addKopf(String ueberschrift, String tabellenkopf)
	{
		this.Kopf(ueberschrift);

		// Kopfzeile
		addKopfleiste(tabellenkopf);
	}

	
	public void add38_200Zeile(int pos, Obj38200 pobj, String infstr,SlideruebersichtDB sldb)
	{ // die Einträge sind mit Semikolon getrennt

		// "infstr@symb@wkn@name@boerse@volumen200@volumen38@volumen@kurs200@kurs38@kurs@s38@s10@s@vertrau@link");
		// alter: das letzte posting in diesem slider ist schon anz tage alt
		String farbe = "black";

		// Zeilenanfang
		sz("<tr>");

		buildZeile("black", infstr);
		buildZeile("black", pobj.getSymb());
		buildZeile("black", pobj.getWkn());
		buildZeile("black", Tools.holeSubstring(pobj.getName(), 25));
		buildZeile("black", pobj.getBoerse());
		buildZeile("black", Float.toString(pobj.getVolumen200()));
		buildZeile("black", Float.toString(pobj.getVolumen38()));
		buildZeile("black", Float.toString(pobj.getVolumen()));
		buildZeile("black", Float.toString(pobj.getK200()));
		buildZeile("black", Float.toString(pobj.getK38()));
		buildZeile("black", Float.toString(pobj.getK()));
		float s38 = pobj.getS38();
		buildZeile("black", Float.toString(s38));
		float s10 = pobj.getS10();
		buildZeile("black", Float.toString(s10));
		float s = pobj.getS();
		if ((s > s10 + (s10 * 0.35f)) && (s10 > s38 ))
		{
			buildZeile("blue", Float.toString(s));
		}else
		if ((s > s10 + (s10 * 0.2f)) && (s10 > s38 ))
		{
			buildZeile("green", Float.toString(s));
		} else

		if ((s < s10 - (s10 * 0.3)))
		{
			buildZeile("red", Float.toString(s));
		} else
			buildZeile("black", Float.toString(s));

		buildZeile("black",(Float.toString(sldb.holeVertrauensfaktor(0, pobj.getThreadid()))));
		buildZeile("black",(Float.toString(sldb.holePostSlideranz(0, pobj.getPostSlideranz()))));
		buildZeile("black",(Float.toString(sldb.holeMitlRang(0, pobj.getThreadid()))));
		
		buildZeile("black", "<a href=http://aktien.wallstreet-online.de/" + pobj.getMasterid() + "/chart.html> link</a>");
	
		
		// Zeilenende
		sz("</tr>");
	}
}
