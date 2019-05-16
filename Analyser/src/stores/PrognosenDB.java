package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.HtmlPrognosenuebersicht;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import mainPackage.GC;
import objects.BadObjectException;
import objects.Obj;
import objects.PrognoseDbObj;
import objects.ThreadDbObj;
import slider.SlideruebersichtObj;

import comperatoren.PrognosenComperator;

import db.DB;

public class PrognosenDB extends DB
{
	private HashSet<Integer> tidmenge = new HashSet<Integer>();
	private String neustesDatum_g = null;

	public PrognosenDB()
	{
		// System.out.println("ThreadsDB construktor");
		LoadDB("prognosen", null, 0);

	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public void updatePrognose(SlideruebersichtObj slo2, String aktdate,
			String info, String type, int mid)
	{
		// Neue Prognose wird aufgenommen
		// x)Alle Prognosen werden aufgenommen
		// info: der infostring hat weitere zusatzinfomationen, kann z.B. der
		// aktuelle Kurs sein
		// type: 38200, oder user

		// holt die Prognose mit der tid
		PrognoseDbObj pobjvorh = this.holePrognose(slo2.getThreadid());
		String kennung = null;

		if (type.equals("user"))
			kennung = "U:" + slo2.getThreadid();
		else
			kennung = slo2.getSymb() + ":" + slo2.getThreadid();

		try
		{
			PrognoseDbObj pobj = new PrognoseDbObj(aktdate + "#" + kennung
					+ "#" + info + "#" + +slo2.getThreadid() + "#"
					+ slo2.getAktname() + "#" + slo2.getPostanzahl() + "#"
					+ slo2.getMitlrank() + "#" + slo2.getGuteU() + "#"
					+ slo2.getSchlechteU() + "#" + slo2.getGuteP() + "#"
					+ slo2.getSchlechteP() + "#" + slo2.getSliderguete() + "#"
					+ slo2.getUseranzahl() + "#" + slo2.getBaduseranzahl()
					+ "#" + slo2.getNeueguteU() + "#"
					+ slo2.getNeueschlechteU() + "#" + slo2.getNeuebadU() + "#"
					+ slo2.getSymb() + "#" + slo2.getHandelshinweis() + "#"
					+ "0" + "#" + slo2.getLastpostdatealter() + "#" + aktdate);

			/*
			 * if (pobjvorh != null) { // prognose ist schon vorhanden, dann
			 * ersetzte und behalte // aufdatum String aufdatum =
			 * pobjvorh.getAufnahmedatum(); pobj.setAufnahmedatum(aufdatum);
			 * this.deletePrognose(pobjvorh); }
			 */
			this.AddObject(pobj);

			// Speichere die Info in der Prognosenübersicht
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath + "\\db\\prognosenliste\\" + mid
					+ ".txt");
			inf.writezeile("Datum<" + aktdate + "> tid<" + slo2.getThreadid()
					+ "> symb<" + slo2.getSymb() + "> Handelshinweis<"
					+ slo2.getHandelshinweis() + ">");

		} catch (BadObjectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean checkAvailable(String aktdate, int tid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(i);
			if (pdbo.getAufnahmedatum().equals(aktdate))
				if (pdbo.getThreadid() == tid)
					return true;
		}
		return false;
	}

	private void deletePrognose(PrognoseDbObj pobj)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(i);
			if (pdbo.getThreadid() == pobj.getThreadid())
				return;
		}
		Tracer.WriteTrace(10, "Error: internal, hab tid <" + pobj.getThreadid()
				+ "> nicht in prognosedb gefunden");
	}

	public PrognoseDbObj holePrognose(int tid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(i);
			if (pdbo.getThreadid() == tid)
				return pdbo;
		}
		return null;
	}

	public String ermittleFarbe(PrognoseDbObj pdbo)
	{
		int prognosealter = Tools.zeitdifferenz_tage(pdbo.getAufnahmedatum(),
				this.neustesDatum_g);
		Boolean prognoseneu_flag = this.checkPrognoseIstNeu(pdbo, 10);

		// rot prognose ist bestätigt und ganz neu
		if ((prognosealter <= 0) && (prognoseneu_flag == true))
			return ("red");

		// schwarz, prognose ist bestätigt aber nicht ganz neu
		if ((prognosealter <= 0) && (prognoseneu_flag == false))
			return ("black");

		if (prognosealter >= 0)
			return ("blue");

		return ("green");
	}

	private boolean checkRun(int pos, PrognoseDbObj pdbo_vgl)
	{
		// prüft ob die Prognose in diesem Run ist
		// Bemerkung: Ein Run wird durch ein Datum charakterisiert
		// pos: startposition für diesen check.
		// rundat: datum dieses run´s
		// pdbo: prognose

		int anz = this.GetanzObj();

		if ((pos >= anz) || (pos == -1))
			Tracer.WriteTrace(10, "Error: internal pos<" + pos + "> >= anz<"
					+ anz + ">");
		Tracer.WriteTrace(50, "hole pos<" + pos + "> maxpos<" + anz + ">");
		PrognoseDbObj robj = (PrognoseDbObj) this.GetObjectIDX(pos);
		String rundat = robj.getAufnahmedatum();

		for (int i = pos; i < anz; i++)
		{
			PrognoseDbObj pobj = (PrognoseDbObj) this.GetObjectIDX(i);
			if (pobj.getAufnahmedatum().equals(rundat) == false)
			{
				Tracer.WriteTrace(50, "Info:Prognose tid<"
						+ pdbo_vgl.getThreadid()
						+ "> NICHT MEHR in diesem Run<" + rundat + ">");
				return false; // prognose ist nicht im run
			} else
			{
				int tid = pobj.getThreadid();
				int tidv = pdbo_vgl.getThreadid();
				if ((tid == tidv)
						&& (pobj.getHandelshinweis().contains("Kaufe fuer")))
				{
					// prognose im run
					Tracer.WriteTrace(50, "Info:Prognose tid<"
							+ pdbo_vgl.getThreadid() + "> auch in diesem Run<"
							+ rundat + ">");
					return true;
				}
			}
		}
		return false;
	}

	private int calcNextRunPos(int startpos)
	{
		// sucht den nächsten Run
		// startpos: startpos ab welcher position gesucht werden soll
		// dat: datum des aktuellen runs
		// return: pos falls noch ein weiterer run gefunden wurde
		// -1 fall nix mehr gefunden wurde

		int anz = this.GetanzObj();
		PrognoseDbObj robj = (PrognoseDbObj) this.GetObjectIDX(startpos);
		String rundat = robj.getAufnahmedatum();

		for (int i = startpos; i < anz; i++)
		{
			PrognoseDbObj pobj = (PrognoseDbObj) this.GetObjectIDX(i);
			String laufdat = pobj.getAufnahmedatum();
			if (laufdat.equals(rundat) == false)
			{
				// nächsten Run gefunden
				Tracer.WriteTrace(50, "Info:datum des nächstens runs dat<"
						+ laufdat + ">");
				return i;
			}
		}
		// ende erreicht
		return -1;

	}

	private String calcSeitDatum(PrognoseDbObj pdbo_prognose, int foundpos)
	{
		// Stelle fest seit wann die Prognose das erste Mal aufgetreten ist
		// Seit wann ist der Run der Prognose am laufen

		if (foundpos < 0)
			Tracer.WriteTrace(10, "Error: internal foundpos<" + foundpos + ">");

		// a) Schaue nach wie lange die Prognose schon läuft
		int posNextRun = 0;

		PrognoseDbObj pobj = (PrognoseDbObj) this.GetObjectIDX(foundpos);

		// schaut nach ob die Prognose im nächsten Run ist
		// holt erst mal den startpunkt des nächsten Run, ab foundpos wird
		// gesucht
		posNextRun = calcNextRunPos(foundpos);

		if (posNextRun > 0)
			while (checkRun(posNextRun, pdbo_prognose) == true)
			{
				foundpos = posNextRun;
				posNextRun = calcNextRunPos(foundpos);
				if (posNextRun == -1)
					break;
			}

		if ((foundpos > 0) && (foundpos < this.GetanzObj()))
		{

			pobj = (PrognoseDbObj) this.GetObjectIDX(foundpos);
			String datum = new String(pobj.getAufnahmedatum());
			Tracer.WriteTrace(50, "Info:Seitdatum tid<" + pobj.getThreadid()
					+ "> Seitdat<" + datum + ">");
			return datum;
		}
		return null;
	}

	public void gibPrognosenuebersichtHtmlAus(String fnam, int threadflag)
	{
		// generiert aus der prognosen.db eine html-Übersicht über die
		// Prognosen
		// threadflag=1, dann wende das Verfahren der userbewertung aus
		// threadflag=0, dann 38200 Verfahren
		// threads mit Symbol DAX werden nicht berücksichtigt
		ThreadsDB tdb = new ThreadsDB();
		int lastindex = 0;
		String threadlink = "xxx";
		String farbe = "red";
		String seitdatum = "00.00.0000";
		int mid=0;
		
		this.sortWriteDB();

		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam, 0);
		// xxxxxxxxxxxxxxxxxxxxxxxxx
		tidmenge.clear();

		HtmlPrognosenuebersicht hueber = new HtmlPrognosenuebersicht(fnam);
		hueber
				.addKopf(
						"Prognosenuebersicht",
						"ErstDatum@Seit@Info@tid@symb@aktname@Midinfo@Postanzahl@mitRang@NeueUser@alter letz Posting@Handelshinweis@link@Prognosenalter@Kennung");

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(i);

			// falls userbewertung, dann hat 38200 hier nix zu suchen
			if (threadflag == 1)
			{
				if (pdbo.getInfo().contains("38200"))
					continue;
			} else // bei 38200 hat die userbewertung hier nix zu suchen
			if (pdbo.getInfo().contains("thread"))
				continue;

			if (pdbo.getSymb().equalsIgnoreCase("DAX"))
				continue;

			int tid = pdbo.getThreadid();
			// behandle jede tid nur einmal, d.h. jede Prognose soll nur einmal
			// angezeigt werden
			// hier ist es aber ein problem, wir wollen aber die neuste prognose
			// anzeigen und nicht irgendeine
			// die liste ist sortiert, eigentlich müsste die neuste prognose
			// zuerst kommen

			if (tidmenge.contains(tid))
				continue;
			else
				tidmenge.add(tid);

			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObject(tid);
			if (tdbo == null)
			{
				Tracer.WriteTrace(20, "tid<" + tid
						+ "> unbekannt in threads.db");
				lastindex = 0;
				threadlink = "xxx";
				farbe = "red";
				seitdatum = "00.00.0000";
				mid=0;
			} else
			{
				lastindex = tdbo.calcLetzteVorhThreadseite(1);
				threadlink = tdbo.GetUrlName(lastindex);
				farbe = ermittleFarbe(pdbo);
				seitdatum = calcSeitDatum(pdbo, i);
				mid=tdbo.getMasterid();
			}
			hueber.addPrognosenZeile(i, pdbo, farbe, threadlink, mid, seitdatum);
		}
		hueber.addEnde();
	}

	private boolean checkPrognoseIstNeu(PrognoseDbObj psuch, int maxtage)
	// Eine Prognose ist neu wenn die gleiche Pognose nochmal vorhanden ist, die
	// andere aber schon maxtage
	// alt ist
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(i);
			String kenn1 = psuch.getKennung();
			String kenn2 = pdbo.getKennung();
			String aufnahme = psuch.getAufnahmedatum();

			// kennung gleich, also gleich prognose schon da
			if (kenn1.equals(kenn2) == true)
			{
				// objekt selbst-> dann gehe weiter
				if (aufnahme.equals(pdbo.getAufnahmedatum()))
					continue;

				// ist ein anderes objekt, dann schaue nach wie alt
				int tdiff = Tools.zeitdifferenz_tage(aufnahme, pdbo
						.getAufnahmedatum());

				// falls prognose schon da und die ist nicht so alt
				if (tdiff < maxtage)
					return false;
			}
		}
		return true;
	}

	public void sortWriteDB()
	{
		PrognosenComperator c = new PrognosenComperator();
		Collections.sort(this.dbliste, c);

		PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(0);

		if (pdbo == null)
		{
			Tracer.WriteTrace(20, "Warning: prognosen.db ist leer");
			return;
		}
		neustesDatum_g = new String(pdbo.getAufnahmedatum());
		this.WriteDB();
	}

	public List<Integer> calcPrognosenliste(int maxtage)
	{
		// Ermittelt die Tidmenge der aktuellen Prognosen d.h. eine Prognose ist
		// aktuell wenn
		// sie nicht älter als maxtage ist
		List<Integer> prognosenliste = new ArrayList<Integer>();

		String aktdatum = Tools.get_aktdatetime_str();

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			PrognoseDbObj pdbo = (PrognoseDbObj) this.GetObjectIDX(i);

			int tdiff = Tools.zeitdifferenz_tage(aktdatum, pdbo
					.getAufnahmedatum());

			// falls die prognose zu alt
			if (tdiff > maxtage)
				continue;
			else if (pdbo.getHandelshinweis().contains("Kaufe fuer") == false)
			{
				continue;
			} else
			{
				int tid = pdbo.getThreadid();
				if (prognosenliste.contains(tid) == false)
					prognosenliste.add(tid);
			}
		}
		return prognosenliste;
	}

	public void postprocess()
	{
	}
}
