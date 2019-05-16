package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Infbuf;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.HtmlSlideruebersicht;
import html.HtmlWochengewinne;

import java.util.ArrayList;
import java.util.Collections;

import kurse.KursValueDB;
import kurse.Kursvalue;
import mainPackage.GC;
import objects.ThreadDbObj;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import slider.SlideruebersichtObj;

import comperatoren.SliderComparator;

import db.DB;

public class SlideruebersichtDB extends DB
{
	private ArrayList<SlideruebersichtObj> slidersorthtml = new ArrayList<SlideruebersichtObj>();

	public SlideruebersichtDB()
	{
		LoadDB("slideruebersicht", null, 0);
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public int UpdateSliderObject(SlideruebersichtObj slo)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo2 = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo2.getThreadid() == slo.getThreadid())
					&& (slo2.getWochenindex() == slo.getWochenindex()))
			{
				// objekt in der Sliderübersicht vorhanden, dann tausche aus
				this.DeleteObjectIDX(i);
				return (this.AddObject(slo));

			}
		}
		// objekt nicht da dann lege an
		return (this.AddObject(slo));

	}

	@SuppressWarnings("unchecked")
	public boolean workSlideruebersicht(String msg2, ThreadsDB tdb,
			String fnam, int minwoindex, int maxwoindex, int maxausgabeanz,
			int maxslidertage, int allflag)
	{
		String msg = null;
		int ausgabeanz = 0;
		String fnamslider = fnam + ".csv";
		String fnamhtml = fnam + ".html";
		if (FileAccess.FileAvailable(fnamslider))
			FileAccess.FileDelete(fnamslider,0);
		if (FileAccess.FileAvailable(fnamhtml))
			FileAccess.FileDelete(fnamhtml,0);
		// liste aufbauen
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo2 = (SlideruebersichtObj) this
					.GetObjectIDX(i);

			// Beachte den wochenindex
			if ((slo2.getWochenindex() >= minwoindex)
					&& (slo2.getWochenindex() <= maxwoindex))
				slidersorthtml.add(slo2);
		}
		// sortiere die Übersichtsliste
		Collections.sort(slidersorthtml);

		// schreiben
		Infbuf infb = new Infbuf(fnamslider);

		HtmlSlideruebersicht hueber = new HtmlSlideruebersicht(fnamhtml);
		hueber
				.addKopf(
						"Slideruebersicht",
						"NR@Threadid@Prio@Aktname@WoI@Sliderpostanz@mitl.Rang@(+/-)Postings@(#user|+/-/bad)@neueUser(+/-/bad)@Sliderguete@Lastupdate der Sliderdaten@link@alter letz Posting@Handelshinweis");

		anz = slidersorthtml.size();
		// gib in der übersicht nur die 1000 besten aus
		for (int i = 0; i < anz; i++)
		{
			if (i % 100 == 0)
				System.out.println(msg2 + ":schreibe Slidereintrag<" + i + ">");
			SlideruebersichtObj slo2 = slidersorthtml.get(i);

			if (slo2.getSliderguete() > 0)
				msg = "***" + slo2.getExcelString();
			else
				msg = slo2.getExcelString();

			infb.writezeile(msg);

			// schreibt die zwischengewinnliste
			ThreadDbObj tdbo = tdb.SearchThreadid(slo2.getThreadid());

			if (tdbo != null)
			{
				if (allflag == 0)
				{
					// nimm keine daxtitel in die Liste auf
					if ((tdbo.getThreadname().contains("dax") == true)
							|| (tdbo.getThreadname().contains("DAX") == true))
						continue;

					if (tdbo.getSymbol().equalsIgnoreCase("DAX") == true)
						continue;

					// Keine Diskussionsrunden aufnehmen
					String aktname = tdbo.getThreadname();
					if (Tools.is_zahl(aktname) == true)
						continue;

					// mindestens 3 postings müssen im Slider sein
					if (slo2.getPostanzahl() < 3)
						continue;
				}
				String aktdate = Tools
						.entferneZeit(Tools.get_aktdatetime_str());
				String lastpostdate = Tools.entferneZeit(tdbo
						.getLastThreadPosttime());

				// falls lastpostdate noch nicht gesetzt ist, setzte das
				// aktuelle datum
				if (lastpostdate.equals("0"))
					lastpostdate = aktdate;

				// letztes posting ist nicht älter als 90 Tage sonst wird nix
				// ausgegeben
				int alter = Tools.zeitdifferenz_tage(aktdate, lastpostdate);
				slo2.setLastpostdatealter(alter);
				if (alter < maxslidertage)
				{
					int lastindex = tdbo.calcLetzteVorhThreadseite(1);
					String threadlink = tdbo.GetUrlName(lastindex);
					hueber.addSliderZeile(i, slo2, threadlink, tdbo);
					ausgabeanz++;
					// gib max maxausgabeanz elemente aus
					if (ausgabeanz > maxausgabeanz)
						break;
				} else
				{
					// Der Slider ist zu alt, dann entferne die Werte und lösche
					// das Hotflag
					String handelshin = slo2.getHandelshinweis();
					handelshin = new String("zu alt>60T");
					slo2.setHandelshinweis(handelshin);
					slo2.setHotflag(0);
					slo2.setSliderguete(0);
					Tracer.WriteTrace(50, "I:Slider zu alt lastpost<"
							+ lastpostdate + "> Slider<" + tdbo.getThreadname()
							+ "> tid<" + tdbo.getThreadid() + ">");
				}
			} else
			{
				Tracer.WriteTrace(20, "I: tid<" + slo2.getThreadid()
						+ ">  fehlt in tdb.db symb<" + slo2.getSymb()
						+ "> aktname<" + slo2.getAktname() + "> ");
			}
		}
		hueber.addEnde();
		infb.close();
		slidersorthtml.clear();
		return true;
	}

	public boolean erstellePrognosen(String msg2, ThreadsDB tdb, String fnam)
	{
		PrognosenDB pdb = new PrognosenDB();
		int anz = this.GetanzObj();

		// geht durch die Sliderliste und baut hiervon ne Prognosendatenbank
		// Kriterum:"kaufe fuer"
		// Achtung: nimm aber keine Prognosen mit Symbol DAX auf
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo2 = (SlideruebersichtObj) this
					.GetObjectIDX(i);

			// schreibt die zwischengewinnliste
			ThreadDbObj tdbo = tdb.SearchThreadid(slo2.getThreadid());

			if (tdbo != null)
			{
				if (slo2.getSymb().equalsIgnoreCase("DAX") == true)
					continue;

				// Keine Diskussionsrunden aufnehmen
				String aktname = slo2.getAktname();
				if (Tools.is_zahl(aktname) == true)
					continue;

				// Nimm keine Prognosen mit Symbol dax auf
				if (slo2.getHandelshinweis().contains("Kaufe fuer"))
				{
					String aktdate = Tools.entferneZeit(Tools
							.get_aktdatetime_str());
					pdb.updatePrognose(slo2, aktdate, "thread", "user", tdbo
							.getMasterid());
				}
			}
		}

		// die Prognosen werden nach Erstdatum sortiert,
		// das neuste Datum steht am anfang
		pdb.sortWriteDB();
		String dat=Tools.entferneZeit(Tools.get_aktdatetime_str());
		
		String threadsfile = GC.rootpath
		+ "\\db\\UserThreadVirtualKonto\\prognosen\\p_threads_"+dat+".html";
		String file38200 = GC.rootpath
		+ "\\db\\UserThreadVirtualKonto\\prognosen\\p_38200_"+dat+".html";
		
		pdb.gibPrognosenuebersichtHtmlAus(threadsfile + ".html",
				1);
		pdb.gibPrognosenuebersichtHtmlAus(file38200 + ".html", 0);
		return true;
	}

	public int calcPrio(ThreadDbObj tdbo, int alterflag)
	{
		// falls alterflag =1 wird die prio auf 8 gesetzt wenn der thread zu alt
		// ist
		SlideruebersichtObj sldbo = (SlideruebersichtObj) this.GetObject(tdbo
				.getThreadid());

		if (sldbo == null)
			return 10;

		int pri = sldbo.calPrio(tdbo, alterflag);
		return pri;

	}

	public String getLastupdatetime(ThreadDbObj tdbo)
	{
		// die gleiche lastupdatetime steht in threadsdb
		SlideruebersichtObj sldbo = (SlideruebersichtObj) this.GetObject(tdbo
				.getThreadid());
		if (sldbo == null)
			return null;
		String lupdate = sldbo.getLastupdate();
		return lupdate;
	}

	public void GibSliderPrioKlassenAus(ProgressBar pb, Display dis)
	{
		// Array was die 10 Prios zählt
		int prio11n[] = new int[11];
		int prio11a[] = new int[11];
		Infbuf inf = new Infbuf(GC.rootpath
				+ "\\db\\reporting\\sliderprios.txt");
		Infbuf infl = new Infbuf(GC.rootpath
				+ "\\db\\reporting\\sliderprio_liste.txt");

		ThreadsDB tdb = new ThreadsDB();
		int anz = tdb.GetanzObj();

		pb.setMaximum(anz);

		// zaehlt die Prios 1...10
		for (int i = 0; i < anz; i++)
		{
			if (pb != null)
				pb.setSelection(i);
			if (dis != null)
				dis.readAndDispatch();
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			int prio = this.calcPrio(tdbo, 0);
			prio11n[prio]++;
			SlideruebersichtObj sldbo = (SlideruebersichtObj) this
					.GetObject(tdbo.getThreadid());
			if (sldbo != null)
				infl.writezeile("i<" + i + "> tid<" + tdbo.getThreadid()
						+ "> postanz<" + sldbo.getPostanzahl() + ">");
			if (i % 100 == 0)
				System.out.println("i=<" + i + "> ");
		}
		inf.writezeile("Prio ohne alterflag");

		for (int i = 0; i < 11; i++)
		{
			String msg = "Pio:<" + i + "> anz threads<" + prio11n[i] + ">";
			inf.writezeile(msg);
		}

		// zaehlt die Prios 1...10
		for (int i = 0; i < anz; i++)
		{
			pb.setSelection(i);
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			int prio = this.calcPrio(tdbo, 1);
			prio11a[prio]++;
			if (i % 100 == 0)
				System.out.println("i=<" + i + "> ");
		}
		inf.writezeile("Prio mit alterflag");
		for (int i = 0; i < 11; i++)
		{
			String msg = "Pio:<" + i + "> anz threads<" + prio11a[i] + ">";
			inf.writezeile(msg);
		}
		inf.close();
		infl.close();

	}

	@SuppressWarnings("unchecked")
	public void SortWriteDB()
	{
		// erst nach dem letzen Aktualisierungsdatum sortieren
		slidersorthtml.clear();

		// hier wird die ganze dbliste sortiert
		SliderComparator c = new SliderComparator();
		Collections.sort(dbliste, c);

		// dann schreiben
		this.WriteDB();
	}

	public float holeVertrauensfaktor(int woindex, int threadid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == woindex)
					&& (slo.getThreadid() == threadid))
				return (slo.getSliderguete());
		}
		return 0;
	}

	public float holeMitlRang(int woindex, int threadid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == woindex)
					&& (slo.getThreadid() == threadid))
				return (slo.getMitlrank());
		}
		return 0;
	}

	public float holePostSlideranz(int woindex, int threadid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == woindex)
					&& (slo.getThreadid() == threadid))
				return (slo.getPostanzahl());
		}
		return 0;
	}

	public SlideruebersichtObj holeSliderobj(int woindex, int threadid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == woindex)
					&& (slo.getThreadid() == threadid))
				return (slo);
		}
		return null;
	}

	public int calcAnzObj(String symbol)
	{
		// zaehlt wieviele sliderobjekte es für ein symbol gibt
		// Es wird nur der wochenindex 0 betrachtet
		int anz = this.GetanzObj();
		int count = 0;
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == 0) && (slo.getSymb().equals(symbol)))
				count++;
		}
		return count;
	}

	public SlideruebersichtObj getObjMinpos(String symb, int nte)
	{
		// holt ein sliderobjekt mit passenden symbol >= ntes vorkommen
		// Es wird nur der Wochenindex 0 betrachtet
		// Es wird mit Position 1 angefangen also 1...2...3...4...5.

		int pos = 0;
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == 0) && (slo.getSymb().equals(symb)))
				pos++;
			if (pos == nte)
				return slo;
		}
		Tracer.WriteTrace(10, "Error: internal nte<" + nte
				+ "> position vom symbol <" + symb
				+ "> nicht in der sliderdb gefunden");
		return null;
	}

	public SlideruebersichtObj getCalcObjMaxpostings(String symb)
	{
		// sucht das Objekt mit den maximalen Postings für ein Symbol
		// Es wird nur der Wochenindex 0 berücksichtigt
		int anz = this.GetanzObj();
		int maxfound = 0;
		SlideruebersichtObj maxobj = null;

		for (int i = 0; i < anz; i++)
		{
			SlideruebersichtObj slo = (SlideruebersichtObj) this
					.GetObjectIDX(i);
			if ((slo.getWochenindex() == 0) && (slo.getSymb().equals(symb)))
			{
				if (slo.getPostanzahl() >= maxfound)
				{
					// neues maximum gefunden
					maxfound = slo.getPostanzahl();
					maxobj = slo;
				}
			}
		}
		return maxobj;
	}

	public void ListeWochengewinne(String enddatum, int tage, ProgressBar pb,
			Display display)
	{
		float gesgewinn = 0;
		float gewinn = 0;
		Float kaufeuro = 0f;
		Inf inf = new Inf();
		float anzaktien = 0;
		float verkaufseuro = 0;
		ThreadsDB tdb = new ThreadsDB();
		PrognosenDB pdb = new PrognosenDB();

		enddatum=Tools.entferneZeit(enddatum);
		
		int anz = this.GetanzObj();
		String startdate = Tools.entferneZeit(Tools.modifziereDatum(enddatum, 0, 0, -tage, 0));
		String fna = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Wochengewinne" + tage;

		inf.setFilename(fna + ".txt");
		HtmlWochengewinne hwo = new HtmlWochengewinne(fna + ".html");
		if (FileAccess.FileAvailable0(fna + ".html") == false)
		{
			hwo
					.addKopf(
							"Wochengewinne" + tage + " Zeitraum<" + startdate
									+ ">-bis<" + enddatum + ">",
							"Name@SYMB@Tid@Kaufdat@Kurs@anz@Summe@Verkaufdat@Kurs@anz@Summe@Gewinn@GesGewinn");
		}

		inf.writezeile("******....neue Gewinnbetrachtung..... Zeitraum von<"
				+ startdate + ">-bis<" + enddatum + ">");
		inf
				.writezeile("****Aktname#Symbol#Tid#Kaufdatum#Kurs#Anzahl#Kraufpreis#Verkaufdatum#Verkaufkurs#Anzahl#Verkaufspreis#Verkaufsgewinn.#Gesammtgewinnsumme");

		pb.setMinimum(0);
		pb.setMaximum(anz);

		for (int i = 0; i < anz; i++)
		{
			pb.setSelection(i);
			Swttool.wupdate(display);
			//if (i % 100 == 0)
			{
				System.out.println("Wochengewinne <" + i + "|" + anz + "> sum<"
						+ gesgewinn + ">");
			}
			SlideruebersichtObj slo2 = (SlideruebersichtObj) this
					.GetObjectIDX(i);

			// schreibt die zwischengewinnliste
			ThreadDbObj tdbo = tdb.SearchThreadid(slo2.getThreadid());

			if (tdbo != null)
			{
				if (slo2.getSymb().equalsIgnoreCase("DAX") == true)
					continue;
				if (slo2.getAktname().contains("DAX"))
					continue;

				// Betrachte keine Werte mit Symbol DAX
				if (slo2.getHandelshinweis().contains("Kaufe fuer"))
				{
					String kdat = Tools.entferneZeit(slo2.getLastupdate());
					String kdat10=Tools.entferneZeit(Tools.modifziereDatum(kdat, 0, 0, +10, 0));
					
					// schaue ob zeitdiff <= Tage
					if (Tools.datum_im_intervall(kdat, startdate, enddatum) == false)
						continue;
					if (Tools.datum_im_intervall(kdat10, startdate, enddatum) == false)
						continue;
					if (kdat.equals(enddatum))
						continue;

					// ermittle Kaufsumme
					String hw = slo2.getHandelshinweis();

					hw = hw.substring(hw.indexOf("<") + 1, hw.indexOf(">"));
					kaufeuro = Float.valueOf(hw);

					// ermittle Kaufkurs
					KursValueDB kvdb = new KursValueDB(slo2.getSymb(), 0,tdb);
					Kursvalue kvkauf = kvdb.holeKurswert(kdat, 0);

					if (kvkauf.getKv() == 0)
					{
						Tracer.WriteTrace(20,
								"Warning:Wochengewinne für Symbol<"
										+ slo2.getSymb() + "> kein Kurs dat<"
										+ kdat + ">");
						
						inf.writezeile( slo2.getAktname() + "#"
								+ slo2.getSymb() + "#" + slo2.getThreadid()
								+ "#" + kdat + "#x#x#x#x#x#x#x#x#kein Kurs verfügbar!");
						continue;
					}

					// ermittle Anzahl
					if (kvkauf.getKv() > 0)
						anzaktien = (Float) kaufeuro / kvkauf.getKv();
					else
						anzaktien = 0;

					// ermittle Verkaufswert
					Kursvalue kvverkauf = kvdb.holeKurswert(kdat10, 0);

					if (kvverkauf.getKv() >= 0)
						verkaufseuro = anzaktien * kvverkauf.getKv();
					else
						verkaufseuro = 0;

					// ermittle Gewinn-Verlust

					if ((verkaufseuro > 0) && (kaufeuro > 0))
						gewinn = verkaufseuro - kaufeuro;
					else
						gewinn = 0;

					gesgewinn = gesgewinn + gewinn;
					// protokolliere
					inf.writezeile( slo2.getAktname() + "#"
							+ slo2.getSymb() + "#" + slo2.getThreadid()
							+ "#" + kdat + "#" + kvkauf.getKv()
							+ "#" + anzaktien + "#" + kaufeuro
							+ "#" + kdat10 + "#"
							+ kvverkauf.getKv() + "#" + anzaktien
							+ "#" + verkaufseuro + "#" + gewinn
							+ "#" + gesgewinn );

					hwo.addZeile(slo2.getAktname() + "@" + slo2.getSymb() + "@"
							+ slo2.getThreadid() + "@" + kdat + "@"
							+ kvkauf.getKv() + "@" + anzaktien + "@" + kaufeuro
							+ "@" + kdat10 + "@" + kvverkauf.getKv() + "@"
							+ anzaktien + "@" + verkaufseuro + "@" + gewinn
							+ "@" + gesgewinn);
				}
			}
		}
		hwo.addEnde();

	}
	public void postprocess()
	{}
}
