package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.Collections;
import java.util.HashSet;

import mainPackage.GC;
import objects.Datumsintervall;
import objects.UserDbGewinnZeitraumObjI;
import objects.UserEinPostingGewinnObj;
import objects.UserGewStrategieObjII;
import ranking.GewinnStatObj;
import userHilfObj.UserTmpGewinnlistenObj;

import comperatoren.UserGewinnZeitraumComparator;

import db.DB;

public class UserSummenGewinneDBI extends DB
{
	// Hier werden die gewinnliste (Summengewinn sowie Einzelgewinne) für einen
	// EINZELNEN User in einem
	// Zeitraumspeicher verwaltet
	//
	// Die Summengewinne und Einzelgewinne sind temporäre listen
	// Die intervallgewinne werden im summengewinnspeicher gehalten
	// (in db/userThreadvirutalKonto/Summengewinnedb)
	// this.writedb speichert diesen Summenspeicher

	private int dbopenflag = 0;
	private String username_glob = null;
	private int intervalltage_glob = 0;
	private int seqnummer_glob = 0;

	// einige Hilfsvariablen
	private int lastzeitraumindex_g = 0;

	// einige weitere Hilfsvarialben
	private float gesammtgewinn_g = 0;

	// dies ist der temporäre Speicher in dem alle Gewinne für einen user
	// gespeichert werden
	private UserTmpGewinnlistenObj tmpgewinn = new UserTmpGewinnlistenObj();

	public UserSummenGewinneDBI(String rootpath, String usernam,
			int intervalltage, int seqnummer)
	{
		
		
		username_glob = usernam;
		intervalltage_glob = intervalltage;
		seqnummer_glob = seqnummer;
		this.LoadDB(rootpath, username_glob, 1);
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public UserDbGewinnZeitraumObjI getEinzelGewinnliste(String usernam)
	{
		// gibt die Gewinnliste für einen User zurück
		if (usernam.equals(usernam) == false)
			Tracer.WriteTrace(10, "Error internal usernam<" + usernam
					+ "> ungleich username<" + username_glob + ">");

		int i;
		int anz = this.GetanzObj();
		for (i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI udbgewI = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if ((udbgewI.getUsername().equalsIgnoreCase(username_glob) == true))
			{
				return udbgewI;
			}
		}
		return null;
	}

	public boolean addEinzelgewinn(String username,
			UserEinPostingGewinnObj ugewobj)
	{
		if (username.equalsIgnoreCase(username_glob) == false)
			Tracer.WriteTrace(10, "Error internal usernam<" + username
					+ "> ungleich username<" + username_glob + ">");

		// Der Gewinn wird im usergewinnspeicher abgelegt, dies ist ja ein
		// temporärer speicher

		tmpgewinn.addObj(username, ugewobj);

		// Ausserdem wird der Gewinn im Zeitraumspeicher abgelegt, das ist der
		// *.db speicher
		this.storeUsergewinnobjZeitraumspeicher(ugewobj);

		// hier wird der Zeitraumspeicher zu Kontrollzwecken sofort geschrieben
		// this.WriteDB();
		return true;
	}

	private float calcGewinnsumme(int seqnr)
	{
		// hier wird die Gewinnsumme für eine seqnr für einen index berechnet
		// ..userthreadvirtualkonto\\summengewinnedb\\<username>.db
		// seqnr: ist die erste position in username.db, anhand der seq-Zahlen
		// ist man
		// in der lage die einzelnen Analysephasen einfach zu differenzieren
		// index: dies ist der Algorithmus auf dem sich der Gewinn bezieht

		float sum = 0;
		// Für einen index wird eine Gewinnsumme für einen User ermittelt
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if (gewobj.getSeqnr() == seqnr)
				sum = sum + gewobj.getGewinn();
		}
		return sum;
	}

	public float calcGewinnsumme(int seqnr, String datum)
	{
		// ermittelt die gewinnsumme für ein bestimmtes datum

		float sum = 0;
		// Für einen index wird eine Gewinnsumme für einen User ermittelt
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if ((gewobj.getSeqnr() == seqnr)
					&& (gewobj.getStartdatum().equals(datum)))

				sum = sum + gewobj.getGewinn();
		}
		return sum;
	}

	private float calcGewinnsumme(int seqnr, String startdatum, String enddatum)
	{
		// durchsucht die ganze DB nach passenden einträgen und ermittelt die
		// summe

		float sum = 0;
		// Für einen index wird eine Gewinnsumme für einen User ermittelt
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if ((gewobj.getSeqnr() == seqnr)
					&& (gewobj.getStartdatum().equals(startdatum))
					&& (gewobj.getEnddatum().equals(enddatum)))
				sum = sum + gewobj.getGewinn();
		}
		return sum;
	}

	private int calcAktienanzahl(int seqnr)
	{
		// zaehlt mit wie vielen verschiedenen Aktien der user die Gewinne
		// gemacht hat
		HashSet<Integer> tidmenge = new HashSet<Integer>();
		tidmenge.clear();

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if (gewobj.getSeqnr() == seqnr)
			{
				// falls die sequenzennummer stimmt dann fülle eine menge mit
				// der aktienid
				tidmenge.add(gewobj.getThreadid());
			}
		}
		return tidmenge.size();
	}

	private int calcStuetzstellenanz(int seqnr)
	{
		// zaehlt wieviele Einträge Summengewinnedb/<username.db> für einen
		// seqnr beinhaltet
		int stuetzanz = 0;
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if (gewobj.getSeqnr() == seqnr)
			{
				stuetzanz++;
			}
		}
		return stuetzanz;
	}

	private int calcIntervall(int seqnr, Datumsintervall interv)
	{
		// berechnet das Intervall (min und max) für eine seqnr
		// return: anzahl der Bewertungstage in diesem Intervall
		String mindat = "30.12.40";
		String maxdat = "01.01.99";

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if (gewobj.getSeqnr() == seqnr)
			{
				// neues Maxdatum gefunden
				if (Tools.datum_ist_aelter(maxdat, gewobj.getEnddatum()) == true)
					maxdat = new String(gewobj.getEnddatum());

				// neues Mindatum gefunden
				if (Tools.datum_ist_aelter(mindat, gewobj.getStartdatum()) == false)
					mindat = new String(gewobj.getStartdatum());
			}
		}
		interv.setStartdatum(mindat);
		interv.setEnddatum(maxdat);
		int tagediff = Tools.zeitdifferenz_tage(mindat, maxdat);

		return tagediff;
	}

	private float calcGewinnabweichung(int seqnr, float mittelgewinn_interv,
			int pos)
	{
		// hier wird die 'Abweichung' gegenüber den Mittelgewinn ermittelt
		// Man möchte mit dieser Funktion ermitteln wie stabil der Gewinn ist
		float abweichung = 0;

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if (gewobj.getSeqnr() == seqnr)
			{
				float diff = Math.abs(gewobj.getGewinn() - mittelgewinn_interv);
				abweichung = abweichung + diff;
			}
		}

		return abweichung;
	}

	public GewinnStatObj calcStabilitätsfaktor(int seqnr, int index)
	{
		GewinnStatObj gstat = new GewinnStatObj();

		// Frage: Was wollen wir wissen. Wer macht über die Intervalle die
		// konstantesten Gewinne
		// Frage: Wer macht in den Intervallen die höchsten Gewinn
		// Frage: Gesucht ist ein User der Konstant gute Gewinne erwirtschaftet
		// Zur Übersicht
		// hier wird ein Faktor ermittelt der angibt wie stabil die Gewinne für
		// einen index
		// sind
		// a) Betrachtungszeitraum start,end
		// b) Summengewinn fuer den Betrachtungszeitaum
		// c) Aktienanzahl
		// d) Gesammtstützstellen
		// e) Wertestützstellen (Also die Stützstellen die auch mit
		// gewinn-Verlust behaftet sind)
		// f) Alg1 abweichungsfaktor
		// g) Alg2 abweichungsfaktor
		// .) ......................
		// ...Algn

		float sum = calcGewinnsumme(seqnr);
		gstat.setGewinnsumme(sum);
		gstat.setStuetzstellenanz(calcStuetzstellenanz(seqnr));

		Datumsintervall datinterv = new Datumsintervall();
		gstat.setDatinterv(datinterv);

		// Es wird ein bestimmtes Intervall betrachtet
		int bewertungstage = calcIntervall(seqnr, datinterv);
		gstat.setBewertungstage(bewertungstage);
		gstat.setMittelgewinn(sum / bewertungstage);

		// dieser Gewinn wird im Mittel in jeden Intervall erzielt
		float mittelgewinn_interv = gstat.getMittelgewinn()
				* intervalltage_glob;

		// hier wird die Gewinnabweichung berechnet
		gstat.setGewabweichungsSumme(calcGewinnabweichung(seqnr,
				mittelgewinn_interv, index));

		// wir müssen das Gewinnobjekt jetzt in einer Liste speichern
		// .....

		// dann kann diese liste sortiert werden und die Ergebnisse können
		// übersichtlich dargestellt werden

		return gstat;
	}

	public void protoGewinnintervalle(int seqnr)
	{
		// passe auf, das jeder Zeitraum nur einmal betrachtet wird
		HashSet<String> zeitmenge = new HashSet<String>();

		String fnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\Proto\\"
				+ username_glob + ".txt";
		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam,0);
		Inf inf = new Inf();
		inf.setFilename(fnam);

		// gesammtsumme ermitteln und speichern
		float sum = calcGewinnsumme(seqnr);
		gesammtgewinn_g = sum;
		String msg = "Gesammtgewinnsumme=" + sum;
		inf.writezeile(msg);

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			String zeitraum = gewobj.getStartdatum() + "-"
					+ gewobj.getEnddatum();

			// falls die zeit schon ausgewertet wurde
			if (zeitmenge.contains(zeitraum) == true)
				continue;

			// werte die Zeit aus
			zeitmenge.add(zeitraum);
			float gew = this.calcGewinnsumme(seqnr, gewobj.getStartdatum(),
					gewobj.getEnddatum());

			msg = "Zeitraum<" + zeitraum + "> Gewinn<" + gew + ">";
			inf.writezeile(msg);
		}
	}

	public float calcStabilitaet()
	{
		// ein Algorithmus wird als gut angesehen wenn er immer positive Gewinne
		// in den Intervallen generiert
		// Es wird hier eine Algrithmenbewertung vorgenommen und eine Liste
		// der besten Algorithmen ausgegeben.

		if (username_glob.contains("Masteruser38x"))
			System.out.println("foundMasteruser");

		int anz = this.GetanzObj();
		// zaehler die zaehlen ob der teilgewinn positiv oder negativ sind
		int poszaehler = 0, negzaehler = 0;

		// zaehler der zaehlt wie oft der mittelgewinn überschritten wurde
		int mittelgewinnzaehler = 0;

		float mittelgewinn = gesammtgewinn_g / anz;

		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) this
					.GetObjectIDX(i);
			if (gewobj.getGewinn() > 0)
			{
				poszaehler++;
				if (gewobj.getGewinn() > mittelgewinn)
					mittelgewinnzaehler++;
			} else
			{
				negzaehler++;
			}
		}
		return mittelgewinnzaehler * 1000 + (poszaehler - negzaehler) * 50;
	}

	public void DeleteUserGewinnListe()
	{
		tmpgewinn.cleanGewinnSpeicher();
	}

	private boolean checkIntervalldaten(Datumsintervall dinter, int tid,
			UserDbGewinnZeitraumObjI gewobj)
	{
		if (gewobj == null)
			return false;
		if ((tid == gewobj.getThreadid())
				&& (gewobj.getStartdatum().equalsIgnoreCase(dinter
						.getStartdatum()))
				&& (gewobj.getEnddatum().equalsIgnoreCase(dinter.getEnddatum())))
		{
			return true;
		} else
			return false;
	}

	private UserDbGewinnZeitraumObjI sucheGenZeitraumspeicherelement(
			Datumsintervall dinter, int tid)
	{
		int anz = this.GetanzObj();
		UserDbGewinnZeitraumObjI gewobj = null;

		// erst wird geschaut ob der letzte index passt
		int indx = lastzeitraumindex_g;
		gewobj = (UserDbGewinnZeitraumObjI) this.GetObjectIDX(indx);
		if (checkIntervalldaten(dinter, tid, gewobj) == true)
			return gewobj;

		for (indx = 0; indx < anz; indx++)
		{
			gewobj = (UserDbGewinnZeitraumObjI) this.GetObjectIDX(indx);
			if (checkIntervalldaten(dinter, tid, gewobj) == true)
			{
				lastzeitraumindex_g = indx;
				return gewobj;
			}
		}
		// objekt ist nicht vorhanden, dann lege es an
		// Tracer.WriteTrace(20,
		// "I:neuer user<"+this.username_glob+"> userdaten im Zeitraumspeicher angelegt");

		gewobj = new UserDbGewinnZeitraumObjI(dinter, tid);
		this.AddObject(gewobj);
		return gewobj;
	}

	private Datumsintervall calcDatumsintervall(UserEinPostingGewinnObj ugewobj)
	{
		// berechnet für das Gewinnobjekt ein Datumsintervall
		Datumsintervall dinter = new Datumsintervall();
		int tage_begin_ival = 0;
		int tage_postingtime = Tools.getDateInt(GC.startdatum, ugewobj
				.getDatum());

		while (5 == 5)
		{
			if ((tage_postingtime > tage_begin_ival)
					&& (tage_postingtime < tage_begin_ival + intervalltage_glob))
			{
				// intervall gefunden
				String startd = Tools.getDateString(GC.startdatum,
						tage_begin_ival);
				String endd = Tools.getDateString(GC.startdatum,
						tage_begin_ival + intervalltage_glob);

				// schneide die uhrzeiten ab
				dinter.setStartdatum(startd.substring(0, startd.indexOf(" ")));
				dinter.setEnddatum(endd.substring(0, endd.indexOf(" ")));
				break;
			} else
				tage_begin_ival = tage_begin_ival + intervalltage_glob;
		}
		return dinter;
	}

	private void storeUsergewinnobjZeitraumspeicher(
			UserEinPostingGewinnObj ugewobj)
	{
		// hier wird das Gewinnobjekt im Zeitspeicher abgelegt
		int anzvorher = this.GetanzObj();
		Datumsintervall dinter = calcDatumsintervall(ugewobj);

		// prüft nach ob dies datumsintervall im speicher schon ist, wenn nicht
		// wird es schon mal
		// angelegt.
		int tid = ugewobj.getTid();
		UserDbGewinnZeitraumObjI obj1 = sucheGenZeitraumspeicherelement(dinter,
				tid);
		float gew = obj1.getGewinn();
		// erhöhe den Gewinn für den Thread/Intervall
		gew = gew + ugewobj.getGewinn();
		obj1.setGewinn(gew);
		obj1.setThreadid(tid);
		int anznahher = this.GetanzObj();

		if (anzvorher > anznahher)
		{
			// der Zeitraumspeicher kann immer nur grösser werden, d.h. die Zahl
			// der zeitintervalle kann nur
			// erhöht werden
			Tracer.WriteTrace(10,
					"Error: Plausicheck UserSummengewinneDBI user<"
							+ this.username_glob + "> anzvorher<" + anzvorher
							+ "> anznahher<" + anznahher + ">");
		}
	}

	public void reportUsergewinneDelMem(int erstflag, int anztageintervall,
			int auswertungsflag, UserGewStrategieObjII gewinnStratObj)
	{
		// Hier wird nur ein user berücksichtigt !!
		// mache einen Plausicheck für die Userposings (die Postid wird hier
		// überprüft)
		// die postingid´s von offline/db/threaddata muss die gleichen
		// postingid´s in der tmpgewinnliste haben
		// Bsp: <threadname>.db #"Procera" 97 [4, 6, 9, 15, 18, 23, 25, 28, 30,
		// 32, 36, 39, 40, 50, 53, 55, 59, 66, 68, 10896, 18470, 18474, 18483,
		// 18583, 18625, 18628, 18632, 18661, 18663, 18665, 18681, 18684, 18688,
		// 18710, 18713, 18719, 18744, 18746, 18748, 18750, 18752, 18755, 18757,
		// 18759, 18761, 18765, 18766, 18783, 18790, 18798, 18826, 18833, 18845,
		// 18847, 18849, 18852, 18858, 18865, 18867, 18892, 18895, 18901, 18906,
		// 18909, 18913, 18915, 18917, 18949, 18952, 18954, 18956, 18969, 18980,
		// 18982, 18994, 18996, 19000, 19003, 19011, 19021, 19028, 19030, 19048,
		// 19050, 19092, 19099, 19107, 19111, 19116, 19168, 19174, 19180, 19267,
		// 19268, 19272, 19290, 19373]
		// => in der tmpliste sollten für den User Procera auch sämtliche diese
		// Nummern wiederzufinden sein. Ist dies nicht der Fall ist die Tmpliste
		// kaputt oder nicht vollständig

		// Monitor mon=MonitorFactory.start("DelMem 1User");

		tmpgewinn.reportSpeichereUsergewinne(erstflag, auswertungsflag,
				gewinnStratObj);

		// Erhöhe die Gesammtgewinnsumme
		// Hierbei wird der grösste Indexgewinn benutzt

		// Nur für Alorithmus 0 wird der Plausicheck für einen user durchgeführt
		tmpgewinn.plausicheck(0);

		// lösche die temporären Gewinne

		tmpgewinn.delMem();

		// speichere die datenbank für den user
		this.WriteDB();

		// mon.stop();
		// Tracer.WriteTrace(20, "Monitor:"+mon);
		return;
	}

	public void sortiereDatum()
	{
		// sortiere nach datum
		UserGewinnZeitraumComparator c = new UserGewinnZeitraumComparator();
		Collections.sort(dbliste, c);

		// speichere
		this.WriteDB();

	}
	public void postprocess()
	{}
}
