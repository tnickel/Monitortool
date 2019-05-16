package championAnalyser;

import hilfsklasse.Keywortliste;
import hilfsklasse.SG;
import hilfsklasse.Tracer;

import java.util.ArrayList;

import stores.AktDB;

public class Championaktie
{
	// Dies ist eine Championaktie aus der Championtabelle
	// Also jede Championaktie repräsentiert eine Zeile

	// orinalzeilen
	/*
	 * 36 Vossloh 766710 Elektronik * EUR 15% 0,86 12,59 2,39 0,21 0,50 25 82,89
	 * 92 Abwärtstrend 07.07.11 88 5% keine Käufe 37 Schindler A0JEHV
	 * Maschinenbau ** SFR 14% 0,88 12,36 1,99 0,25 0,51 24 92,9 104
	 * Abwärtstrend 02.08.11 89 5% keine Käufe 38 Banpu 882131 Rohstoffe *** EUR
	 * 34% 0,92 30,96 2,95 0,43 1,28 24 14,98 18 Abwärtstrend 26.04.11 17 12%
	 * keine Käufe
	 */

	private String[] braL =
	{ "Elektronik", "Maschinenbau", "Öl", "Gas", "Energie", "&", "Rohstoffe",
			"Netzbetreiber", "Software", "Papier", "Biotechnologie",
			"Energieversorger", "Medizintechnik", "Verkehr", "Getränke",
			"Tabak", ",", "Diversifizierte", "Holding", "Konsumgüter",
			"Bekleidung", "Mode", "/", "Logistik", "Spezialmaschinen",
			"Rohstoffe", "Ernährung", "Handel", "Chemie", "Computer",
			"Internet", "Pharma", "Gesundheit", "Sonstiges", "Umwelt",
			"Lebensmittel", "Pharmazie", "Konsumwerte", "Elektro", "Konsum",
			"Energieversorgung", "Energieversorgung", "Chemische", "Industrie",
			"Konsum", "Versicherungen", "Geschäftsbanken", "Geschäftsbanken",
			"Rüstungstechnik", "Stahl", "Eisen", "Ernührung", "Bildung",
			"Geschaftsbanken", "Konsumgter", "Ernahrung", "Mischkonzerne","Getrünke" };

	ArrayList<String> branchenliste = new ArrayList<String>();
	private String fullline_glob = null;
	int rangcounter_glob = 0;

	// Werte aus der Tabelle
	int rang_i = 0;
	String aktnam = null;
	String wkn = null;
	String branche = null;
	int sternanz_i = 0;
	String waehrung = null;
	int geopak10_i = 0;
	float gk_f = 0;
	float cgewinn_f = 0;
	float verlustratio_f = 0;
	float athv_f = 0;
	float crisiko_f = 0;
	int crang_i = 0;
	float kurs_f = 0;
	float gd200_f = 0;
	String trend = null;
	String seit = null;
	float kursdamals_f = 0;
	int ergebniss_i = 0;
	String strategie = null;

	// Weitere interne Werte
	int threadid = 0; // tid falls vorhanden
	String symbol = null;
	String boerse = null;

	// part=ist die Tabelle in die kleinsten Einzelpositionenzersplittet
	private ArrayList<String> part = new ArrayList<String>();

	public boolean checkKeywortliste(Keywortliste keyl)
	// prüft ob eines der keywörter mit der Aktie übereinstimmt
	{

		int anz = keyl.getanz();

		// betrachte alle keywörter
		for (int i = 0; i < anz; i++)
		{
			String keyx = keyl.getObjIdx(i);
			String mstring = this.getAktnam() + this.getStrategie()
					+ this.getWkn() + this.getTrend();
			if (mstring.contains(keyx) == true)
				return true;
		}

		return false;
	}

	public boolean checkKeywortlisteFull(Keywortliste keyl)
	// prüft ob eines der keywörter mit der Aktie übereinstimmt
	{

		int anz = keyl.getanz();

		// betrachte alle keywörter
		for (int i = 0; i < anz; i++)
		{
			String keyx = keyl.getObjIdx(i);

			if (fullline_glob.contains(keyx) == true)
				return true;
		}

		return false;
	}

	private int calcSternpos()
	{
		// ermittelt die position wo sich die Sterne befinden
		int anz = part.size();
		for (int i = 0; i < anz; i++)
		{
			if (part.get(i).contains("*"))
				return i;
		}

		Tracer.WriteTrace(10, "Error: internal, kann * nicht finden");
		return -99;
	}

	private int calcProzpos(int nte)
	{
		// Ermittelt die Position wo sich das %zeichen befindet
		int nzaehl = 0;
		int anz = part.size();

		if (nte > 2)
			Tracer.WriteTrace(10, "Error: geht nur bis 2");

		for (int i = 0; i < anz; i++)
		{
			if (part.get(i).contains("%"))
			{
				nzaehl++;
				if (nzaehl == nte)
					return i;
			}
		}

		Tracer.WriteTrace(10, "Error: internal, kann % nicht finden");
		return -99;
	}

	public Championaktie(String line, AktDB aktdb)
	{
		fullline_glob = line;
		Tracer.WriteTrace(20, "Info: zeile<" + line + ">");

		if(line.contains("epsi"))
			System.out.println("epsi");
		
		// erweitere die Branchenliste mit "<Schlüsselwort>,"
		int anz = braL.length;
		for (int i = 0; i < anz; i++)
		{
			branchenliste.add(braL[i].toString());
			branchenliste.add(braL[i].toString() + ",");
		}
		// zerlege die linie in einzelne Silben
		zerlege(line);

		if (line.contains("30 Schindler A0JEHV Maschinenbau") == true)
			System.out.println("found");

		belegeTabellenWerte(aktdb);
	}

	private void zerlege(String line)
	{
		// zerlege die zeile in einzelstrings und speichere die
		// Bsp: line=1 Eldorado Gold 892560 Energie & Rohstoffe **** CAD 40%
		// 0,93 36,70 3,56 0,02 0,07 491 20,53 17 Aufw rtstrend 01.08.11 16 25%
		// halten
		// part[0]=1
		// part[1]=Eldorado
		// part[2]=Gold

		line=line.replaceAll("Aufwärtstrend", " Aufwärtstrend");
		line=line.replaceAll("Abwärtstrend", " Abwärtstrend");
		line=line.replaceAll("Kaufsignal", " Kaufsignal");
		line=line.replaceAll("  ", " ");
		
		int pos = 0;
		while ((line.length() > 0) && (line.contains(" ")))
		{

			pos = line.indexOf(" ");

			String l1 = line.substring(0, pos);

			part.add(l1);
			line = line.substring(l1.length() + 1);
		}
		// das letzte noch hinzunehmen
		part.add(line);
	}

	private void showParts()
	{
		int anz = part.size();
		for (int i = 0; i < anz; i++)
		{
			System.out.print(part.get(i) + ":");
		}
		System.out.println("  ");
	}

	private boolean isBranche(int pos)
	{
		// Prüft nach ob das Schlüsselwort an position part(i) ein
		// branchenschlüsselwort ist
		int anz = branchenliste.size();
		String suchbra = part.get(pos);

		for (int i = 0; i < anz; i++)
		{

			if (suchbra.equals(branchenliste.get(i)) == true)
				return true;
		}
		return false;
	}

	private int calcBrancheStartpos(int sternpos)
	{
		// prüft nach ab welcher position von 0 in dem partarry die Branche
		// anfängt
		int i = 0;
		boolean foundflag = false;
		for (i = 3; i < sternpos; i++)
		{
			if (isBranche(i) == true)
			{
				foundflag = true;
				break;
			}
		}

		if (foundflag == false)
		{
			showParts();
			Tracer.WriteTrace(10, "E: keine Branche gefunden 1!!");
		}
		// i ist die startposition der kategorisierung
		return i;
	}

	private float holeFloatwert(int pos)
	{
		// GK, 0,93 -> float
		String fls = part.get(pos);
		fls = fls.replace(",", ".");
		float fl = Float.valueOf(fls);
		return fl;

	}

	private int holeIntegerwert(int pos)
	{
		String ers = part.get(pos);
		ers = ers.replace("%", "");
		int er = Integer.valueOf(ers);
		return er;
	}

	private void belegeTabellenWerte(AktDB aktdb)
	{
		System.out.println("starte");
		int sternpos = calcSternpos();
		int pcount = sternpos;

		int p1 = calcProzpos(1);
		int p2 = calcProzpos(2);

		// Rang: 1 ->integer
		int rangpos = 0;
		setRang_i(Integer.valueOf(part.get(rangpos)));

		// Branche: Energie & Rohstoffe -> String
		int brancheStartpos = calcBrancheStartpos(sternpos);
		String brastring = "";
		// Baue den Branchenstring zusammen
		for (int i = brancheStartpos; i < pcount; i++)
			brastring = brastring.concat(part.get(i));
		setBranche(brastring);

		// Sterne zählen und setzen
		// Bsp: ***->integer
		int anzsterne = SG.countZeichen(part.get(sternpos), "*");
		setSternanz_i(anzsterne);

		// wkn: 454545->String
		int wknpos = brancheStartpos - 1;
		setWkn(part.get(wknpos));

		// Aktie: colgate palmolive -> String
		// namen aufbauen
		String namstring = "";
		for (int i = 1; i < wknpos; i++)
		{
			if (i == 1)
				namstring = namstring.concat(part.get(i));
			else
				namstring = namstring.concat(" " + part.get(i));
		}
		setAktnam(namstring);

		// Die daten nach dem * aufbauen
		pcount++;
		setWaehrung(part.get(pcount));

		// geopak10, umwandeln von 40% -> int
		pcount++;
		setGeopak10_i(holeIntegerwert(pcount));

		// GK, 0,93 -> float
		pcount++;
		setGk_f(holeFloatwert(pcount));

		// cgewinn 36,70 -> float
		pcount++;
		setCgewinn_f(holeFloatwert(pcount));

		// verlustratio 3,56 ->float
		pcount++;
		setVerlustratio_f(holeFloatwert(pcount));

		// athv 0,02 -> float
		pcount++;
		setAthv_f(holeFloatwert(pcount));

		// crisiko 0,07 -> float
		pcount++;
		setCrisiko_f(holeFloatwert(pcount));

		// Crang 497 -> int
		pcount++;
		setCrang_i(holeIntegerwert(pcount));

		// Kurs 4,979 ->float
		pcount++;
		setKurs_f(holeFloatwert(pcount));

		// GD200 454,0 -> float
		pcount++;
		setGd200_f(holeFloatwert(pcount));

		// Trend:Kaufsignal ->String
		pcount++;
		setTrend(part.get(pcount));

		// Sonderbehandlung Datumsstring ist manchmal fehlerhaft
		// seit: String -> String, das seit kann fehlerbehaftet sein, d.h. seit
		// und kursDamals müssen
		// erst getrennt werden Bsp:02.12.201066 -> 02.12.2010 66
		pcount++;

		String seitstr = part.get(pcount);

		if (seitstr.length() == 8)
		{// alles ok datum ist 02.12.10

			setSeit(seitstr);
			// kursDamals: 0.545 -> float
			pcount++;
			setKursdamals_f(holeFloatwert(pcount));
		} else if (seitstr.length() == 10)
		{
			// mache aus 02.12.2010 -> 02.12.10
			String px1 = seitstr.substring(0, 6);
			String px2 = seitstr.substring(8, 10);
			setSeit(px1.concat(px2));
			pcount++;
			setKursdamals_f(holeFloatwert(pcount));
		} else
		{// Datum fehlerhaft z.B. 02.12.201066

			setSeit(seitstr);
			// String dat=seitstr.substring(0,10);
			// setSeit(dat);
			// kursDamals: 0.545 -> float
			// String rest=seitstr.substring(10,seitstr.length());
			setKursdamals_f(0);
		}

		// Ergebnis in %: 15% -> integer
		pcount++;
		setErgebniss_i(holeIntegerwert(pcount));

		// Strategie: Halten, keine Käufe, Stop-Buy Limit etc..
		int anz = part.size();
		pcount++;
		if ((pcount + 1) == anz)
		{
			// ein String bei der Strategie
			// z.b. Halten
			setStrategie(part.get(pcount));
		} else
		{
			// zweispaltig
			String strat = part.get(pcount) + part.get(pcount + 1);
			setStrategie(strat);
		}

	}

	public int getRang_i()
	{
		return rang_i;
	}

	public void setRang_i(int rangI)
	{
		rang_i = rangI;
	}

	public String getAktnam()
	{
		return aktnam;
	}

	public void setAktnam(String aktnam)
	{
		this.aktnam = aktnam;
	}

	public String getBranche()
	{
		return branche;
	}

	public void setBranche(String branche)
	{
		this.branche = branche;
	}

	public int getSternanz_i()
	{
		return sternanz_i;
	}

	public void setSternanz_i(int sternanzI)
	{
		sternanz_i = sternanzI;
	}

	public String getWaehrung()
	{
		return waehrung;
	}

	public void setWaehrung(String waehrung)
	{
		this.waehrung = waehrung;
	}

	public int getGeopak10_i()
	{
		return geopak10_i;
	}

	public void setGeopak10_i(int geopak10I)
	{
		geopak10_i = geopak10I;
	}

	public float getGk_f()
	{
		return gk_f;
	}

	public void setGk_f(float gkF)
	{
		gk_f = gkF;
	}

	public float getCgewinn_f()
	{
		return cgewinn_f;
	}

	public void setCgewinn_f(float cgewinnF)
	{
		cgewinn_f = cgewinnF;
	}

	public float getVerlustratio_f()
	{
		return verlustratio_f;
	}

	public void setVerlustratio_f(float verlustratioF)
	{
		verlustratio_f = verlustratioF;
	}

	public float getAthv_f()
	{
		return athv_f;
	}

	public void setAthv_f(float athvF)
	{
		athv_f = athvF;
	}

	public float getCrisiko_f()
	{
		return crisiko_f;
	}

	public void setCrisiko_f(float crisikoF)
	{
		crisiko_f = crisikoF;
	}

	public int getCrang_i()
	{
		return crang_i;
	}

	public void setCrang_i(int crangI)
	{
		crang_i = crangI;
	}

	public float getKurs_f()
	{
		return kurs_f;
	}

	public void setKurs_f(float kursF)
	{
		kurs_f = kursF;
	}

	public float getGd200_f()
	{
		return gd200_f;
	}

	public void setGd200_f(float gd200F)
	{
		gd200_f = gd200F;
	}

	public String getTrend()
	{
		return trend;
	}

	public void setTrend(String trend)
	{
		this.trend = trend;
	}

	public String getSeit()
	{
		return seit;
	}

	public void setSeit(String seit)
	{
		this.seit = seit;
	}

	public float getKursdamals_f()
	{
		return kursdamals_f;
	}

	public void setKursdamals_f(float kursdamalsF)
	{
		kursdamals_f = kursdamalsF;
	}

	public float getErgebniss_i()
	{
		return ergebniss_i;
	}

	public void setErgebniss_i(int ergebnissI)
	{
		ergebniss_i = ergebnissI;
	}

	public String getStrategie()
	{
		return strategie;
	}

	public void setStrategie(String strategie)
	{
		this.strategie = strategie;
	}

	public String getWkn()
	{
		return wkn;
	}

	public void setWkn(String wkn)
	{
		this.wkn = wkn;
	}

}
