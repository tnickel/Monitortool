package data;

import filterPack.Filterzeitraume;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import zeitmessung.Zeitmessung;
import Metriklibs.EndtestFitnessfilter;
import Metriklibs.Filterentry;
import Metriklibs.Filterzeitraum;
import Metriklibs.Metrikentry;
import Metriklibs.Metriktabelle;
import Metriklibs.Metriktabellen;
import Metriklibs.Metrikzeile;
import Metriklibs.Stratelem;

public class Stratliste
{
	//die liste beinhaltet eine Strategiemenge, das ist die Ansammlung der gefundenen
	//Strategien
	
	private ArrayList<Stratelem> stratliste_glob = new ArrayList<Stratelem>();

	public Stratliste()
	{
	}

	public int anz()
	{
		return stratliste_glob.size();
	}

	public void buildStratliste(Metriktabellen met)
	{

		// Anhand der ersten metiktabelle wird die Stratliste aufgebaut
		// hier wird alles aufgenommen was geht
		stratliste_glob = met.buildStratliste();
	}

	public void filterSelfile(String fnam)
	{
		// in diesem Hashset sind die Strategien die genommen weren, alles
		// andere wird rausgefiltert
		HashSet<String> stratnamenmenge = new HashSet<String>();
		// es werden nur die Strategien genommen die im Selfile spezifiziert
		// sind

		File fnamf = new File(fnam);
		if (fnamf.exists() == false)
		{
			return;
		}
		Inf inf = new Inf();
		inf.setFilename(fnam);

		// die hashmap aufbauen
		String zeile = null;
		while ((zeile = inf.readZeile()) != null)
		{
			stratnamenmenge.add(zeile);
		}

		// aussortieren
		int anz = stratliste_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Stratelem strelem = stratliste_glob.get(i);
			String stratname = strelem.getStratname();
			// falls strat nicht in der configlsite dann entferne
			if (stratnamenmenge.contains(stratname) == false)
			{
				Tracer.WriteTrace(20, "Strat<" + stratname
						+ "> wird wieder entfernt");
				stratliste_glob.remove(i);
				i--;
				anz--;
			}
		}
	}

	public boolean checkStrAvailable(String strname)
	{
		//prüft nach ob der strategiename in der strategieliste ist
		
		strname = strname.replace(".str", "");
		int anz = stratliste_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Stratelem strelem = stratliste_glob.get(i);
			String stratname = strelem.getStratname();
			if (stratname.equalsIgnoreCase(strname) == true)
				return true;
		}
		return false;
	}

	public void filterAll(Metriktabellen met, Filterzeitraume filt,
			int best100flag)
	{
		// hier wird alles rausgefiltert was nicht passt.
		int anzfilter = met.calcanzFilter();
		// Tracer.WriteTrace(20,
		// "Filterung Start # Reststrategien<"+stratliste_glob.size()+">");

		Zeitmessung z = new Zeitmessung(0);
		for (int i = 0; i < anzfilter; i++)
		{
			// filtere die Strategieliste mit filter i
			// die Metriktabelle ist das was der SQ abspeichert
			Metriktabelle metriki = met.holeNummerI(i);
			if (metriki == null)
				continue;
			// der Filterzeitraum ist der Filter für den entsprechenden Zeitraum
			// der Filterzeitraum beinhaltet die Min-maxwerte nach denen
			// gefiltert wird
			Filterzeitraum filteri = filt.holeFilterzeitraumNummerI(i);
			if (filteri == null)
				continue;

			// wird wird geschaut ob man die Strategie mit den aktuellen
			// Metriken rausfiltern kann
			// falls dismissflag==1 dann dürfen die metriktabellen unvollständig
			// sein
			z.showZeitdiff("vorher");
			filtereMitFilterI(metriki, filteri, best100flag, 0);
			z.showZeitdiff("nach filterI");
/*
			Tracer.WriteTrace(20,
					"Filterung # Reststrategien<" + stratliste_glob.size()
							+ "> filtername<" + filteri.holeFilename() + ">");*/
		}
	}

	private void filtereMitFilterI(Metriktabelle mettab,
			Filterzeitraum filtzeit, int best100flag, int dismissflag)
	{
		// hier wird strategieliste reduziert
		// mettab: das sind die strategien die zu filtern sind
		// filterzeit: hier sind die filterwerte drin nach den es zu filter gilt
		// stratliste_glob: aus dieser liste wird rausgeworfen wenn strate nicht
		// den bedingungen genügt

		Zeitmessung z = new Zeitmessung(0);
		
		// betrachte jede verbleibende strate einzeln
		int anz = stratliste_glob.size();
		for (int i = 0; i < anz; i++)
		{
			/*
			 * if(i%500==0)
			 * System.out.println("filtereMitFilterI stratn=<"+i+">");
			 */
			// das Element und zugehörigen beispiel und filter werden ermittelt
			z.showZeitdiff("M1");
			Stratelem strat = stratliste_glob.get(i);
			String stratname = strat.getStratname();
			Metrikzeile met = mettab.holeMetrikzeile(stratname);
			z.showZeitdiff("M2");
			// plausi
			if ((met == null) && (dismissflag == 0))
				Tracer.WriteTrace(10,
						"Strategie <" + stratname
								+ "> hat keine metriken, filterzeitraum<"
								+ filtzeit.holeFilename() + ">");
			z.showZeitdiff("M3");
			
			if (pruefeMetrikzeile(met, filtzeit, best100flag, 1) == false)
			{
				// strategie passt nicht und wird rausgeworfen
				stratliste_glob.remove(i);
				i--;
				anz--;
			}
			z.showZeitdiff("M4");
		}
	}

	private Boolean pruefeMetrikzeile(Metrikzeile met, Filterzeitraum filtzeit,
			int best100flag, int dismissflag)
	{
		// prüft ob die metrikzeile den filterbedingungen genügt
		// sämtliche bedingungen der Metrikzeile werden geprüft
		// return: true oder false
		// falls dismissflag==1, dann wird bei fehlender metrik ein false
		// übergeben
		// wenn keine metrik in der Tabelle ist der defaultwert dismiss//rauswurf !!!
		if ((met == null) && (dismissflag == 1))
			return false;
		else if (met == null)
			Tracer.WriteTrace(10, "E: empty metric");

		// gehe durch alle Metriken/Attribute
		int anzentrys = met.getLength();
		for (int i = 0; i < anzentrys; i++)
		{
			Metrikentry meentry = met.holeEntry(i);
			String me_attribut = meentry.getAttributName();

			Filterentry filterentry = filtzeit.holeFilterEntry(me_attribut);
			if (checkEntry(meentry, filterentry, best100flag) == false)
			{

				/*Tracer.WriteTrace(20, "I:attrib<" + me_attribut
						+ "> strat lost val<" + meentry.getValue()
						+ "> minval<" + filterentry.getAktMinValue()
						+ "> maxval<" + filterentry.getAktMaxValue() + ">");*/

				return false;
			}
		}
		return true;
	}

	private Boolean checkEntry(Metrikentry meentry, Filterentry filterentry,
			int best100flag)
	{
		// best100flag: 1 es werden die besten 100 str gesucht

		// hier wird überprüft ob der entry den filterbedingungen genügt
		if (meentry.getAttributflag() != 2)
			return true; //ist erfüllt falls kein floatwert

		// hole den Wert der metrik
		float mevalue = Float.valueOf(meentry.getValue());
		// und das ist der filter nach dem gefiltert wird
		float min = filterentry.getMinvalue();
		float max = filterentry.getMaxvalue();

		// falls die 100 besten gesucht werden dann ist min und max woanders
		if (best100flag == 1)
		{
			min = filterentry.getAktMinValue();
			max = filterentry.getAktMaxValue();
		}

		if (mevalue < min)
			return false;
		if (mevalue > max)
			return false;
		return true;
	}

	public EndtestResult Endtest(Metriktabellen mettab, int showresultliste,EndtestFitnessfilter endfitnessfilter)
	{
		//wenn infoflag==1, dann wird am ende eine box aufgeblendet
		//wenn showresultliste==1 dann wird "EndtestPartA.result" geschrieben
		//das ist nur interessant wenn ein einzelnes setting ausprobiert wird nicht beim
		//automatischen suchen
		
		// für die restlichen str wird jetzt ein Endtest gemacht
		// das Resultat wird im File protokolliert
		float sumval = 0;
		Inf inf=null;
		EndtestResult endresult = new EndtestResult();

		String fnam = mettab.holeEndtestFilnamen();
		File ffnam = new File(fnam);
		if (ffnam.exists())
			ffnam.delete();

		if(showresultliste==1)
		{
			 inf = new Inf();
			inf.setFilename(fnam);
		}
		
		// gehe durch die Reststartegien und sammle die float-werte
		int anz = stratliste_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Stratelem stratelem = stratliste_glob.get(i);
			String stratname = stratelem.getStratname();
			float val = mettab.holeEndtestValue(stratname,endfitnessfilter);
			sumval = sumval + val;
			if(showresultliste==1)
				inf.writezeile("stratnam<" + stratname + "> gew<" + val + ">");
		}
		if(showresultliste==1)
		{
			inf.writezeile("sumval<" + sumval + ">");
			inf.close();
		}
		endresult.setFitnessvalue(sumval);

		
		return (endresult);
	}
}
