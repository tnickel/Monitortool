package html;

import hilfsklasse.FileAccess;
import mainPackage.GC;
import objects.ThreadDbObj;
import slider.SlideruebersichtObj;

public class HtmlSlideruebersicht extends HtmlTabelle
{
	
	public HtmlSlideruebersicht()
	{}
	public HtmlSlideruebersicht(String nam)
	{
		this.setfilename(nam);
	}

	public void addKopf(String ueberschrift, String tabellenkopf)
	{
		Kopf(ueberschrift);

		// Kopfzeile
		addKopfleiste(tabellenkopf);
	}

	public void addSliderZeile(int pos, SlideruebersichtObj slo2, String threadlink,ThreadDbObj tdbo)
	{ // die Einträge sind mit Semikolon getrennt
		
		
		// alter: das letzte posting in diesem slider ist schon anz tage alt

		String farbe = "black";
		String farbek = "black";
		String farbeind = "black";
		int hotpoints = 0;

		// Zeilenanfang
		sz("<tr>");

		// pos
		if (slo2.getHotflag() == 1)
			farbeind = "red";
		buildZeile(farbeind, Integer.toString(pos));
		// tid
		buildZeile("black", Integer.toString(slo2.getThreadid()));
		//prio
		buildZeile("black", Integer.toString(slo2.calPrio(tdbo, 1)));
		
		// aktname,symb,compressed
		// falls kein Kurs verfügbar, dann male in Grau
		String fnam = GC.rootpath + "\\db\\kurse\\" + slo2.getSymb() + ".csv";
		if (FileAccess.FileAvailable(fnam) == false)
			farbek = "grey";
		
		char first=String.valueOf(slo2.getThreadid()).charAt(0);
		buildZeile(farbek, slo2.getAktname()
				+ "<br><a href=\"../..//db//kurse/" + slo2.getSymb()
				+ ".csv\" > " + slo2.getSymb()
				+ " </a> " +
						"<a href=\"../..//db//compressedthreads/@"+first+"//"
				+ slo2.getThreadid() + ".db\" > " + "COMPR" + "   </a>  " +
				"<a href=\"../..//handdata//midinfo/"
				+ tdbo.getMasterid() + ".txt\" > " + "MIDINF" + "   </a>  " +
				"<a href=\"../..//db//prognosenliste/"
				+ tdbo.getMasterid() + ".txt\" > " + "PROGL" + "   </a>  " +
						"<br>");
		// Wochenindex
		buildZeile("black", Integer.toString(slo2.getWochenindex()));

		// Sliderpostanzahl
		buildZeile("black", "<a href=\"../..//db//slider/" + slo2.getThreadid()
				+ ".txt\" > " + slo2.getPostanzahl() + " </a><br>"
				+ "<a href=\"../..//db//slider90/" + slo2.getThreadid()
				+ ".txt\" > SL90 </a>");

		buildZeile("black", Float.toString(slo2.getMitlrank()));
		// gute schlechte Postings (+|-)
		if ((slo2.getGuteP() > slo2.getSchlechteP() * 2)
				&& (slo2.getPostanzahl() > 20))
		{
			hotpoints = hotpoints + 2;
			farbe = "red";
		} else if ((slo2.getGuteP() > slo2.getSchlechteP())
				&& (slo2.getPostanzahl() > 10))
		{
			hotpoints = hotpoints + 1;
			farbe = "orange";
		} else
			farbe = "black";
		buildZeile(farbe, "(" + Integer.toString(slo2.getGuteP()) + "/"
				+ Integer.toString(slo2.getSchlechteP()) + ")");
		// (#User|+/-/bad) User
		if ((slo2.getGuteU() > slo2.getSchlechteU() * 2)
				&& (slo2.getPostanzahl() > 20))
		{
			hotpoints = hotpoints + 2;
			farbe = "rot";
		} else if ((slo2.getGuteU() > slo2.getSchlechteU())
				&& (slo2.getPostanzahl() > 10))
		{
			hotpoints = hotpoints + 1;
			farbe = "orange";
		} else
			farbe = "black";
		buildZeile(farbe, "(" + Integer.toString(slo2.getUseranzahl()) + "|"
				+ slo2.getGuteU() + "/" + slo2.getSchlechteU() + "/"
				+ slo2.getBaduseranzahl() + ")");

		// neue User(+/-/bad) User
		if ((slo2.getNeueguteU() > 0)
				&& (slo2.getNeueguteU() > slo2.getNeueschlechteU()))
		{
			hotpoints = hotpoints + 1;
			farbe = "orange";
		} else if ((slo2.getNeueguteU() > 0)
				&& (slo2.getNeueguteU() > slo2.getNeueschlechteU() * 2))
		{
			hotpoints = hotpoints + 2;
			farbe = "rot";
		} else
			farbe = "black";

		// Neue User im Thread
		String fnam2 = GC.rootpath + "\\db\\UserThreadVirtualKonto\\NeueUserImThread\\"
				+ slo2.getThreadid() + ".txt";
		if (FileAccess.FileAvailable(fnam2))
		{
			buildZeile(farbe, "<a href=\"../..//db//UserThreadVirtualKonto//NeueUserImThread//"
					+ slo2.getThreadid() + ".txt\" > " + "("
					+ slo2.getNeueguteU() + "/" + slo2.getNeueschlechteU()
					+ "/" + slo2.getNeuebadU() + ")" + " </a>");
		} else
			buildZeile(farbe, "(" + slo2.getNeueguteU() + "/"
					+ slo2.getNeueschlechteU() + "/" + slo2.getNeuebadU() + ")");

		// Sliderguete
		if(slo2.getSliderguete()>0)
			farbe="rot";
		else
			farbe="black";
		buildZeile(farbe, Float.toString(slo2.getSliderguete()));
		// lastupdate
		buildZeile("black", slo2.getLastupdate());
		// link
		buildZeile("black", "<a href=" + threadlink + "> link</a>");

		// alter
		buildZeile("black", Integer.toString(slo2.getLastpostdatealter()));

		// handelshinweis
		buildZeile("black",slo2.getHandelshinweis());
		
		// Zeilenende
		sz("</tr>");

		// setze hotpoints
		slo2.setHotpoints(hotpoints);

		// hotflag setzen wenn thread wichtig
		if (hotpoints >= 3)
			slo2.setHotflag(1);
	}

}
