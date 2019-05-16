package objects_gewinnausgabe;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.Html38_200Uebersicht;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import mainPackage.GC;
import objects.Obj38200;
import slider.SlideruebersichtObj;
import stores.PrognosenDB;
import stores.SlideruebersichtDB;

import comperatoren.PrognosenComperator;

public class Liste38200
{
	private String filename_g;
	private String kopfzeile = "";

	ArrayList<Obj38200> rankliste_g = new ArrayList<Obj38200>();

	public Liste38200(String fnam)
	{
		filename_g = fnam;
	}

	public String getKopfzeile()
	{
		return kopfzeile;
	}

	public void setKopfzeile(String kopfzeile)
	{
		this.kopfzeile = kopfzeile;
	}

	public void sort()
	{
		Collections.sort(rankliste_g);
	}

	public void addObj(Obj38200 obj)
	{
		rankliste_g.add(obj);
	}

	public void gibListeAus()
	{
		String zwischenlistetmp = GC.rootpath
				+ "\\db\\reporting\\Zwischenliste_tmp.txt";
		String infstr = "";

		if (FileAccess.FileAvailable(zwischenlistetmp))
			FileAccess.FileDelete(zwischenlistetmp,0);

		if (FileAccess.FileAvailable(filename_g))
			FileAccess.FileMove(filename_g, zwischenlistetmp);

		BufferedWriter ouf;
		ouf = FileAccess.WriteFileOpen(filename_g, "UTF-8");
		try
		{
			ouf.write(kopfzeile);
			ouf.newLine();
			ouf
					.write("********************************************************************************************************");
			ouf.newLine();
			// gibt die sortierte liste der usergewinne aus
			for (int i = 0; i < rankliste_g.size(); i++)
			{
				Obj38200 obj_s = rankliste_g.get(i);
				if (obj_s.calcpoints(obj_s) >= 90)
					infstr = "++";
				else if ((obj_s.calcpoints(obj_s) >= 40))
					infstr = "+-";
				else
					infstr = "--";
				if (obj_s.getK38() < 1)
					infstr = infstr.concat("p");

				String zeile = i + " , " + infstr + " , " + obj_s.getSymb()
						+ " , " + obj_s.getWkn() + " , " + obj_s.getName()
						+ " , " + obj_s.getBoerse() + " , " + obj_s.getK()
						+ " , " + obj_s.getVolumen() + " , "
						+ obj_s.getVolumen38() + " , " + obj_s.getVolumen200()
						+ " , " + obj_s.getK38() + " , " + obj_s.getK200()
						+ " , " + obj_s.getS();
				ouf.write(zeile);

				ouf.newLine();
				/*
				 * if (i % 100 == 0) System.out.println("i=<" + i +
				 * "> schreibe sortierte Ergebnisse");
				 */
			}
			ouf.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void erzeugeHtmlliste(String fnam)
	{
		String fnamhtml = fnam + ".html";
		SlideruebersichtDB sldb = new SlideruebersichtDB();

		String infstr = "";

		if (FileAccess.FileAvailable(fnamhtml))
			FileAccess.FileDelete(fnamhtml,0);

		Html38_200Uebersicht h38 = new Html38_200Uebersicht(fnamhtml);
		h38
				.addKopf(
						"Kursuebersicht",
						"infstr@symb@wkn@name@boerse@volumen200@volumen38@volumen@kurs200@kurs38@kurs@s38@s10@s@vertrau@mitlRang@Postanz@web");

		// gibt die sortierte liste der usergewinne aus
		for (int i = 0; i < rankliste_g.size(); i++)
		{
			Obj38200 obj_s = rankliste_g.get(i);
			if (obj_s.calcpoints(obj_s) >= 90)
				infstr = "++";
			else if ((obj_s.calcpoints(obj_s) >= 40))
				infstr = "+-";
			else
				infstr = "--";
			if (obj_s.getK38() < 1)
				infstr = infstr.concat("p");

			h38.add38_200Zeile(i, obj_s, infstr, sldb);

			/*
			 * if (i % 100 == 0) System.out.println("i=<" + i +
			 * "> schreibe sortierte Ergebnisse");
			 */
		}

		h38.addEnde();
		h38.cFile();
	}

	public void updatePrognosenDB()
	{
		// geht durch die rankingliste und fügt falls Steigungsfaktor >2 diesen
		// Wert zu
		// der Prognosendb hinzu
		// für ein Symbol kann es aber mehrere tids geben, also mehrere
		// Prognosen, es wird aber nur der thread rausgesucht der die meisten
		// postings hat
		PrognosenDB pdb = new PrognosenDB();
		SlideruebersichtDB sldb = new SlideruebersichtDB();

		int anz = rankliste_g.size();

		// gehe durch alle symbole mit Steigungsfaktor >2
		for (int i = 0; i < anz; i++)
		{
			Obj38200 obj_s = rankliste_g.get(i);
			if (obj_s.getS() > 2)
			{
				String symb = obj_s.getSymb();

				// sucht den die passenden Slider in der SliderDB für ein symb
				// Ein Symbol kann mehrere Objekte haben !!
				// Es wird aber nur das Objekt/Thread mit den meisten postings
				// zur Prognose hinzugefügt

				// hole das Sliderobjekt mit den maximalen Postings
				SlideruebersichtObj slobj = sldb.getCalcObjMaxpostings(symb);

				if (slobj == null)
				{
					Tracer
							.WriteTrace(
									20,
									"Warning: symb<"
											+ symb
											+ "> wurde nicht in der slideruebersicht.db gefunden !!!");
				} else
				{
					// update der Prognose
					pdb.updatePrognose(slobj, Tools.entferneZeit(Tools
							.get_aktdatetime_str()), "38200:s=" + obj_s.getS(),
							"38200", 0);
				}
			}
		}
		pdb.WriteDB();

		PrognosenComperator c = new PrognosenComperator();
		Collections.sort(pdb.dbliste, c);
		String dat=Tools.entferneZeit(Tools.get_aktdatetime_str());
		
		String threadsfile = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\prognosen\\p_threads_"+dat+".html";
		String file38200 = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\prognosen\\p_38200_"+dat+".html";

		pdb.gibPrognosenuebersichtHtmlAus(threadsfile, 1);
		pdb.gibPrognosenuebersichtHtmlAus(file38200, 0);

	}
}
