package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Ma;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.UserDbObj;
import objects.UserGewStrategieObjII;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import stores.AktDB;
import stores.UserGewStrategieDB2;

public class Rangparameter
{
	private float anmeldetagefaktor = 0;
	private float themenanzahlfaktor = 0;
	private float postinglistenanzahlfaktor = 0;
	private float antwortanzfaktor = 0;
	private float neueaktthreadsfaktor = 0;
	private float neuesofafaktor = 0;
	private float durchschnittfaktor = 0;

	// dies sind die defaultwerte
	private String[] attribut =
	{ "Anmeldetagefaktor", "Themenanzahlfaktor", "Antwortanzahlfaktor",
			"Postinglistenanzahlfaktor", "neue Aktienthreadsfaktor",
			"NeueSofaThreadsFaktor", "Durchschnittbeiträgefaktor",
			"Gewinnrangkpunktefaktor" };
	private String[] value =
	{ "3", "100", "100", "1000", "100", "2000", "1" };

	private void setDefault()
	{
		Tracer.WriteTrace(50, "Info: Rangparameter =null, nimm defaultsatz");
		// hier werden die defaultwerte gesetzt
		anmeldetagefaktor = Float.valueOf(value[0]);
		themenanzahlfaktor = Float.valueOf(value[1]);
		postinglistenanzahlfaktor = Float.valueOf(value[2]);
		antwortanzfaktor = Float.valueOf(value[3]);
		neueaktthreadsfaktor = Float.valueOf(value[4]);
		neuesofafaktor = Float.valueOf(value[5]);
		durchschnittfaktor = Float.valueOf(value[6]);
	}

	public Rangparameter(String parameterfile)
	{
		String fnam = GC.rootpath + "\\db\\parameter\\" + parameterfile
				+ ".txt";
		if ((parameterfile == null)
				|| (FileAccess.FileAvailable(fnam) == false))
		{
			Tracer.WriteTrace(50, "Error: kein datenfile <" + parameterfile
					+ "> set defaultwerte");
			setDefault();
		} else
		{
			// lade den parametersatz
			Inf inf = new Inf();
			inf.setFilename(fnam);
			String zeile = inf.readZeile();
			inf.close();

			int n = Tools.countZeichen(zeile, "#");
			if (n != 6)
				Tracer.WriteTrace(10, "Error: datenfile <" + parameterfile
						+ "> fehlerhaft");

			try
			{
				float val = Float.valueOf(Tools.nteilstring(zeile, "#", 1));
				anmeldetagefaktor = val;
				value[0] = String.valueOf(val);

				val = Float.valueOf(Tools.nteilstring(zeile, "#", 2));
				themenanzahlfaktor = val;
				value[1] = String.valueOf(val);

				val = Float.valueOf(Tools.nteilstring(zeile, "#", 3));
				postinglistenanzahlfaktor = val;
				value[2] = String.valueOf(val);

				val = Float.valueOf(Tools.nteilstring(zeile, "#", 4));
				antwortanzfaktor = val;
				value[3] = String.valueOf(val);

				val = Float.valueOf(Tools.nteilstring(zeile, "#", 5));
				neueaktthreadsfaktor = val;
				value[4] = String.valueOf(val);

				val = Float.valueOf(Tools.nteilstring(zeile, "#", 6));
				neuesofafaktor = val;
				value[5] = String.valueOf(val);

				val = Float.valueOf(Tools.nteilstring(zeile, "#", 7));
				durchschnittfaktor = val;
				value[6] = String.valueOf(val);

			} catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Tracer.WriteTrace(10, "Error: internal filename<"
						+ parameterfile + ">");
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public String getAttribut(int index)
	{
		if ((index < 0) || (index > 7))
			Tracer.WriteTrace(10, "Error internal: indexfehler index<" + index
					+ ">");
		return attribut[index];
	}

	public float getVal(int index)
	{
		if ((index < 0) || (index > 7))
			Tracer.WriteTrace(10, "Error internal: indexfehler index<" + index
					+ ">");
		return (Float.valueOf(value[index]));
	}

	public int getAnz()
	{
		return value.length;
	}

	public String getBewertungsformel()
	{
		String formel = "rankingpionts =(anmeldetage *" + anmeldetagefaktor
				+ ")+(themenanz * " + themenanzahlfaktor + ")+\n"
				+ "     (antwortanz *" + antwortanzfaktor
				+ ")+(postinglisteanz * " + postinglistenanzahlfaktor + ")\n"
				+ "     (neueAktThreads * " + neueaktthreadsfaktor
				+ ")+(neueSofaThreads *" + neuesofafaktor + ")\n"
				+ "     (durchschnbeitraege *" + durchschnittfaktor
				+ ")+gewinnrangpunkte";
		return formel;
	}

	public Rangparameter(float anmeld, float themen, float posting,
			float antwortanz, float neuethreads, float neuesofa,
			float durchschnitt)
	{
		/*
		 * double rankingpoints = ((float) anmeldetage * 3) + ((float) themenanz
		 * * 100) + ((float)antwortanz * 1) + ((float) postinglisteanz * 100) +
		 * ((float) neueAktThreads * 1000) + ((float) neueSofaThreads * 100) +
		 * ((float)durchschnbeitraege * 2000) + gewinnrangpunkte;
		 */

		anmeldetagefaktor = anmeld;
		themenanzahlfaktor = themen;
		postinglistenanzahlfaktor = posting;
		antwortanzfaktor = antwortanz;
		neueaktthreadsfaktor = neuethreads;
		neuesofafaktor = neuesofa;
		durchschnittfaktor = durchschnitt;
	}

	private float gettabval(int zeilenindex, Table table)
	{
		// holt einen eintrag aus der tabelle
		TableItem item = null;

		// holt die werte aus der zeile mit dem index
		item = table.getItem(zeilenindex);

		// holt den floatwert aus der spalte 1
		String val = item.getText(1);
		Float f = Float.valueOf(val);
		return f;
	}

	public boolean setRangparameter(Table table)
	{
		// holt die rangparameter aus der tabelle und setzt die hier
		anmeldetagefaktor = gettabval(0, table);
		themenanzahlfaktor = gettabval(1, table);
		postinglistenanzahlfaktor = gettabval(2, table);
		antwortanzfaktor = gettabval(3, table);
		neueaktthreadsfaktor = gettabval(4, table);
		neuesofafaktor = gettabval(5, table);
		durchschnittfaktor = gettabval(6, table);
		return true;
	}

	public float getAnmeldetagefaktor()
	{
		return anmeldetagefaktor;
	}

	public void setAnmeldetagefaktor(float anmeldetagefaktor)
	{
		this.anmeldetagefaktor = anmeldetagefaktor;
	}

	public float getThemenanzahlfaktor()
	{
		return themenanzahlfaktor;
	}

	public void setThemenanzahlfaktor(float themenanzahlfaktor)
	{
		this.themenanzahlfaktor = themenanzahlfaktor;
	}

	public float getPostinglistenanzahlfaktor()
	{
		return postinglistenanzahlfaktor;
	}

	public void setPostinglistenanzahlfaktor(float postinglistenanzahlfaktor)
	{
		this.postinglistenanzahlfaktor = postinglistenanzahlfaktor;
	}

	public float getAntwortanzfaktor()
	{
		return antwortanzfaktor;
	}

	public void setAntwortanzfaktor(float antwortanzfaktor)
	{
		this.antwortanzfaktor = antwortanzfaktor;
	}

	public float getNeueaktthreadsfaktor()
	{
		return neueaktthreadsfaktor;
	}

	public void setNeueaktthreadsfaktor(float neueaktthreadsfaktor)
	{
		this.neueaktthreadsfaktor = neueaktthreadsfaktor;
	}

	public float getNeuesofafaktor()
	{
		return neuesofafaktor;
	}

	public void setNeuesofafaktor(float neuesofafaktor)
	{
		this.neuesofafaktor = neuesofafaktor;
	}

	public float getDurchschnittfaktor()
	{
		return durchschnittfaktor;
	}

	public void setDurchschnittfaktor(float durchschnittfaktor)
	{
		this.durchschnittfaktor = durchschnittfaktor;
	}

	public int CalcRanking(UserGewStrategieDB2 ugewinnedb, AktDB aktdb,
			int usergewinnflag, UserDbObj udbo, Rangparameter rp)
	{
		int gewinnrangpunkte = 0;
		// usergewinnflag==1, dann werden die einzelnen usergewinne mit in das
		// Ranking
		// gezogen
		// hier findet die Rankingberechnung statt
		// Für das Ranking werden die Folgenden Fakten aufgenommen
		// a) anz registrierter Tage
		// b) Erstellte Themen
		// c) Erstellte Antworten
		// d) grösse der Threadübersicht (Wieviele verschiedene Threads hat er)
		// e) durchschnitt Beiträge/pro Tag
		// f) Neue Akt-Threads im letzten halben Jahr (Schaut er auch nach was
		// neuen um ?)
		// g) Neue Diskussions-Threads im letzten halben Jahr
		// h) Handdata (Hier wird eine manuelle Wertung vorgenommen, 0-100)
		// i) Pemiumuser bekommen hier einen Bonus
		// user ein optimales Trading machen würde
		// j) Value der Aktiengewinne-Verluste (Hier werden die
		// Aktien-Gewinne/Verluste betrachtet) [Gewinnrankpunkte]

		int gewinnrang = 0;

		// a)Anmeldetage
		int anmeldetage = Tools.get_date_days(udbo.getRegistriert());
		// b)Themenanzahl
		int themenanz = udbo.getThemen();
		// c)Antwortanzahl
		int antwortanz = udbo.getAntworten();
		// d)Grösse der Threadübersicht
		int postinglisteanz = udbo.getUserPostingListe().getSize();
		// e)durchschnitt Beiträge pro Tag
		float durchschnbeitraege = udbo.getBeitraegetag();
		// f)neue aktien-Threads im letzten halben Jahr
		String time6M = Tools.modifziereDatum(Tools.get_aktdatetime_str(), 0,
				-2, 0, 1);
		int neueAktThreads = udbo.getUserPostingListe().getAnzneuerThreadsSeit(
				time6M, 1, udbo, aktdb);
		// g)neue Diskuss im letzten halben Jahr
		int neueSofaThreads = udbo.getUserPostingListe()
				.getAnzneuerThreadsSeit(time6M, 0, udbo, aktdb);

		// h)Handata(0-100)

		// i) Expertenuser bekommen 300% auf alles
		int expertenuserflag = udbo.getWoexpertenflag();

		// j) Bewerte den Gewinnrank mit 50 %
		// Hole den Gewinnrank aus usergewinne2
		// Der Gewinnrank gibt an wie gut ein User mit "naiven"
		// Handelsverhalten ist. Es kommt noch ein Bonus hinzu wenn der
		// user mehr gewinne wie verluste hat.
		// Der Gewinnrank ist auf 100000 Skaliert d.h. für alle User werden
		// bewertet und
		// bekommen Punkte zwischen 0 und 100000. Der jenige mit 0 Punkten ist
		// der beste.

		// zusätzliche Bemerkung
		String addmessage = "";

		String unam = udbo.get_username();
		UserGewStrategieObjII ugew2 = ugewinnedb.sucheUser(unam);
		if (ugew2 == null)
		{
			addmessage = addmessage + "[kGew---]";
			// falls es für den User noch keine Bewertung gibt
			Tracer.WriteTrace(20, "Warning: für user <" + unam
					+ "> ist kein Gewinnrank in usergewinne.db definiert");
			gewinnrang = 50000;
		} else
			gewinnrang = ugew2.getGewinnrank();

		// bei gewinnrank 0...10000 werde noch dick Punkte vergeben
		// Punkte von 10000 bis 1Mio
		if (usergewinnflag == 1)
			gewinnrangpunkte = Ma.interpol(gewinnrang);
		else
			gewinnrangpunkte = 0;

		
		double rankingpoints = ((float) anmeldetage * rp.anmeldetagefaktor)
				+ ((float) themenanz * rp.themenanzahlfaktor)
				+ ((float) antwortanz * rp.antwortanzfaktor)
				+ ((float) postinglisteanz * rp.postinglistenanzahlfaktor)
				+ ((float) neueAktThreads * rp.neueaktthreadsfaktor)
				+ ((float) neueSofaThreads * rp.neuesofafaktor)
				+ (durchschnbeitraege * rp.durchschnittfaktor)
				+ gewinnrangpunkte;

		if (anmeldetage < 300)
		{
			//Abwertung da <300 Tage
			addmessage = addmessage + "[k300---]";
			Tracer.WriteTrace(20, "Info: Anmeldetage<" + anmeldetage
					+ "> user<" + udbo.get_username()
					+ "> <<<< 300 führt zur Abwertung");
			rankingpoints = rankingpoints / 8;
		}

		if ((anmeldetage > 300) && (anmeldetage < 700))
		{ 
			//schwächere Abwertung da <700 Tage
			addmessage = addmessage + "[k300700---]";
			Tracer.WriteTrace(20, "Info: Anmeldetage<" + anmeldetage
					+ "> user<" + udbo.get_username()
					+ "> <<<< 700 führt zur Abwertung");
			rankingpoints = rankingpoints / 4;
		}

		// Bonuszahlung auf die Gewinnrangpunkte (User muss aber mindestens 100
		// Postings haben
		// Bonus falls t-wkeit >70% (Bonus von 30%)
		// Der Bonus wird in abhängigkeit von der Wkeit und der Postanzahl
		// gezahlt
		if (ugew2 != null)
		{

			float gewinnanz = ugew2.getGewinnpostings_G();
			float verlustanz = ugew2.getVerlustpostings_G();
			float proz = gewinnanz / (gewinnanz + verlustanz);
			udbo.setTrefferwkeit(proz);
		}

		// Bei Wo experte einen Bonus von 200%+2MIO Punkte
		if (expertenuserflag == 1)
		{
			addmessage = addmessage + "[WOexpert+++]";
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\BonusPunkteWoExperte.txt");
			float add = (float) (rankingpoints / 100f) * 200f;
			rankingpoints = rankingpoints + add+2000000f;
			String msg = Tools.entferneZeit(Tools.get_aktdatetime_str())+": Info: Expertenuser <" + unam
					+ "> erhält Bonus von 30% Rankingpoints<" + add
					+ "> => gesammtpunkte<" + rankingpoints + ">";

			Tracer.WriteTrace(20, msg);
			inf.writezeile(msg);
		}

		udbo.setRankinginfostring("t<" + anmeldetage + "> themen#<" + themenanz
				+ "> antw<" + antwortanz + "> post#<" + postinglisteanz
				+ "> nThr<" + neueAktThreads + "> nSof<" + neueSofaThreads
				+ "> durch.Beitr<" + durchschnbeitraege + "> GEWR <"
				+ gewinnrang + "> GewRpunkte<" + gewinnrangpunkte + "> RP <"
				+ rankingpoints + ">" + addmessage);

		udbo.setRankinginfostring2(rankingpoints + "#" + anmeldetage + "#"
				+ themenanz + "#" + antwortanz + "#" + postinglisteanz + "#"
				+ neueAktThreads + "#" + neueSofaThreads + "#"
				+ durchschnbeitraege + "#" + gewinnrang + "#"
				+ gewinnrangpunkte);

		return (int) rankingpoints;
	}
}
