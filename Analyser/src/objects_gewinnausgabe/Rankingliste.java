package objects_gewinnausgabe;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import html.HtmlGewinnuebersicht;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import mainPackage.GC;
import objects.UserDbObjSort;

public class Rankingliste
{
	private String filename_g;
	private int nulllistenflag_g = 0;
	private ArrayList<UserDbObjSort> rankliste_g = new ArrayList<UserDbObjSort>();

	public Rankingliste(String fnam, int nflag)
	{
		// falls das nullflag gesetzt wird, wird die masteruserliste0 ausgegeben
		// dies ist eine Referenzliste für den einfachen Gewinnalgorithmus
		filename_g = fnam;
		nulllistenflag_g = nflag;
	}

	public void sort()
	{
		Collections.sort(rankliste_g);
	}

	public void add(UserDbObjSort obj)
	{
		rankliste_g.add(obj);
	}

	public int getanzObj()
	{
		return rankliste_g.size();
	}

	public UserDbObjSort getObjektIDX(int i)
	{
		return rankliste_g.get(i);
	}

	private void gibGewinnVerlaufAus(String unam, String zeile, int posinfo)
	// hier wird der Verlauf protokolliert
	{
		if (nulllistenflag_g == 0)
		{
			if (unam != null)
			{

					String fnam = GC.rootpath
							+ "\\db\\UserThreadVirtualKonto\\Verlauf\\"
							+ unam + ".txt";
					Inf infmaster = new Inf();
					infmaster.setFilename(fnam);
					infmaster.writezeile(posinfo + ":"
							+ Tools.get_aktdatetime_str() + ":" + zeile);
			}
		}	
	}

	public void gibListeAus(int threadzaehler)
	{
		//threadzaehler ist einfach eine infovariable
		
		String zwischenlistetmp = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Zwischenliste_tmp.txt";

		if (FileAccess.FileAvailable(zwischenlistetmp))
			FileAccess.FileDelete(zwischenlistetmp,0);
		if (FileAccess.FileAvailable(filename_g))
			FileAccess.FileMove(filename_g, zwischenlistetmp);

		BufferedWriter ouf;
		ouf = FileAccess.WriteFileOpen(filename_g, "UTF-8");
		try
		{
			// gibt die sortierte liste der usergewinne aus
			for (int i = 0; i < rankliste_g.size(); i++)
			{
				UserDbObjSort udbobj_s = rankliste_g.get(i);

				String zeile = "username<" + udbobj_s.getName() + "> Points<"+udbobj_s.getPoints()+"> Gewinn<"
						+ udbobj_s.getGewinn() + ">   "
						+ udbobj_s.getGewinninfostring();

				ouf.write(zeile);
				//gibGewinnVerlaufAus(udbobj_s.getName(), zeile, threadzaehler);
				ouf.newLine();
				/*if (i % 100 == 0)
					System.out.println("i=<" + i
							+ "> schreibe sortierte Ergebnisse");*/
			}
			ouf.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void gibHtmlListeAus(int threadzaehler)
	{
		String zwischenlistehtml = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\ZwischenGewinnliste.html";

		if (FileAccess.FileAvailable(zwischenlistehtml))
			FileAccess.FileDelete(zwischenlistehtml,0);

		HtmlGewinnuebersicht hueber = new HtmlGewinnuebersicht(
				zwischenlistehtml);
		hueber.addKopf("GewinnRangUebersicht",
				"username@Points@Gewinn@Handlungen@+Gew@-Gew@0Gew@einzelGew@SummenGew@SumDB");

		// gibt die sortierte liste der usergewinne aus
		for (int i = 0; i < rankliste_g.size(); i++)
		{
			UserDbObjSort udbobj_s = rankliste_g.get(i);

			String zeile = "username<" + udbobj_s.getName() + "> Points<"+udbobj_s.getPoints()+"> Gewinn<"
					+ udbobj_s.getGewinn() + ">   "
					+ udbobj_s.getGewinninfostring();

			//zaehle nur Gewinne/Verluste auf
			if(udbobj_s.getGewinn()!=0)
				hueber.addZeile(udbobj_s);

			/*if (i % 100 == 0)
				System.out.println("i=<" + i
						+ "> schreibe sortierte Ergebnisse");*/
		}

		hueber.addEnde();

	}
}
