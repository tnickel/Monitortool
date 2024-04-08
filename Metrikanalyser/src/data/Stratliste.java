package data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import Metriklibs.DatabankExportTable;
import Metriklibs.EndtestFitnessfilter;
import Metriklibs.Filterentry;
import Metriklibs.Filterfile;
import Metriklibs.Metrikentry;
import Metriklibs.Metriktabellen;
import Metriklibs.Metrikzeile;
import Metriklibs.StrategienSelector;
import Metriklibs.Stratelem;
import filterPack.Filterfiles;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;
import zeitmessung.Zeitmessung;

public class Stratliste
{
	//die liste beinhaltet eine Strategiemenge, das ist die Ansammlung der gefundenen
	//Strategien, dies sind also alle Strategien. Im laufe der Zeit wird diese Menge immer geringer da ja ausgefiltert wird.
	private ArrayList<Stratelem> stratliste_glob = new ArrayList<Stratelem>();

	public Stratliste()
	{
	}

	public int anz()
	{
		return stratliste_glob.size();
	}

	public void buildStratliste(StrategienSelector stratsel,Metriktabellen met)
	{

		// Anhand der ersten metiktabelle wird die Stratliste aufgebaut
		// hier wird alles aufgenommen was geht
		stratliste_glob = met.buildStratliste(stratsel);
	}

	public void filterSelfile(String fnam)
	{
		// in diesem Hashset sind die Strategien die genommen werden, alles
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
		//prüft nach ob der strategiename in der bestenliste ist
		
		strname = strname.replace(".str", "");
		strname = strname.replace(".sqx", "");
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

	public void filterAll(Metriktabellen met, Filterfiles filt,
			int best100flag)
	{
		//metriktabellen: 	Das sind die exportierten Tabelle aus dem SQX
		//filterzeiträume: 	Das sind meine definierten Filter
		//best100flag: 		??
		// hier wird alles rausgefiltert was nicht passt.
		int anzfilter = met.calcanzFilter();
		// Tracer.WriteTrace(20,
		// "Filterung Start # Reststrategien<"+stratliste_glob.size()+">");

		Zeitmessung z = new Zeitmessung(0);
		for (int i = 0; i < anzfilter; i++)
		{
			// filtere die Strategieliste mit filter i
			// die Metriktabelle ist das was der SQ abspeichert
			DatabankExportTable metriki = met.holeNummerI(i);
			if (metriki == null)
				continue;
			// der Filterzeitraum ist der Filter für den entsprechenden Zeitraum
			// der Filterzeitraum beinhaltet die Min-maxwerte nach denen
			// gefiltert wird
			Filterfile filteri = filt.holeFilterzeitraumNummerI(i);
			if (filteri == null)
				continue;

			// es wird geschaut ob man die Strategie mit den aktuellen
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

	private void filtereMitFilterI(DatabankExportTable mettab,
			Filterfile filtzeit, int best100flag, int dismissflag)
	{
		// hier wird strategieliste reduziert
		// mettab: das sind die strategien die zu filtern sind, diese Tabelle welche strategien und metriken beinhaltet kommt vom SQX.
		// filterzeit: hier sind die filterwerte drin nach den es zu filter gilt
		// stratliste_glob: aus dieser liste wird rausgeworfen wenn strate nicht
		// den bedingungen genügt
		//dismissflag=

		Zeitmessung z = new Zeitmessung(0);
		
		// betrachte jede verbleibende strate einzeln
		int anzStrategien = stratliste_glob.size();
		for (int i = 0; i < anzStrategien; i++)
		{
			
			/*
			 * if(i%500==0)
			 * System.out.println("filtereMitFilterI stratn=<"+i+">");
			 */
			// das Element und zugehörigen beispiel und filter werden ermittelt
			z.showZeitdiff("M1");
			Stratelem strat = stratliste_glob.get(i);
			String stratname = strat.getStratname();
			//eine metrikzeile ist eine zeile aus der dicken Tabelle die der SQ abgelegt hat
			Metrikzeile met = mettab.holeMetrikzeile(stratname);
			
			//System.out.println("Filtere strategy <"+stratname+">");
			z.showZeitdiff("M2");
			// plausi
			if ((met == null) && (dismissflag == 0))
				Tracer.WriteTrace(10,
						"Strategie <" + stratname
								+ "> hat keine metriken, filterzeitraum<"
								+ filtzeit.holeFilename() + ">");
			z.showZeitdiff("M3");
			
			//es wird geprüft ob die Metrikzeile den filterbedingungen genügt.
			if (pruefeMetrikzeile(stratname,met, filtzeit, best100flag, 1) == false)
			{
				// strategie passt nicht und wird rausgeworfen
				stratliste_glob.remove(i);
				i--;
				anzStrategien--;
			}
			z.showZeitdiff("M4");
		}
	}

	private Boolean pruefeMetrikzeile(String stratname,Metrikzeile met, Filterfile filtzeit,
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
			//jede Metrikzeile besteht aus n values. Die Metrikzeile ist aus der dicken
			//Metriktabelle die der SQx exportiert hat.
			//ein Metrikentry hat nur einen value.
			Metrikentry meentry = met.holeEntry(i);
			String me_attribut = meentry.getAttributName();

			//ein Filterentry hat 6 werte, minvalue, maxvalue,minfilevalue,maxfilevalue..
			Filterentry filterentry = filtzeit.holeFilterEntry(me_attribut);
			if (checkEntry(meentry, filterentry, best100flag) == false)
			{

				/* Tracer.WriteTrace(20, "I:Filter out Strategy<"+stratname+"> because attrib<" + me_attribut
						+ "> val=<" + meentry.getValue()
						+ "> not in Range: Conditions: minval<" + filterentry.getAktMinValue()
						+ "> maxval<" + filterentry.getAktMaxValue() + ">"); */

				return false; //die metrikzeile genügt nicht den bedingungen
			}
		}
		return true;//die metrikzeile ist ok, strategie kann wahrscheinlich bleiben.
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
		if(filterentry==null)
		{
			
			
			Tracer.WriteTrace(10, "E:Something got wrong with the database.filter-file missing metrikattribut<"+meentry.getAttributName()+"> Please Clean Filter");
		}
			
		float min = filterentry.getMinvalue();
		float max = filterentry.getMaxvalue();

		// falls die 100 besten gesucht werden dann ist min und max woanders
		if (best100flag == 1)
		{
			min = filterentry.getAktMinValue();
			max = filterentry.getAktMaxValue();
		}

		//falls die value=0, dann wird nicht rausgefiltert.
		if((mevalue==0)||(filterentry.getMinfilevalue()==0)||(filterentry.getMaxfilealue()==0))
			return true; //wenn mix oder maxfilevalue ==0 ist, bedeutet das das metrik überhaupt nicht verwendet wurde
						 //somit darf man die strategie nicht rausfiltern.
						 //z.B. wurde oos überhaupt nicht berechnet. Der filterentry verlangt aber ein oos, hier darf nicht
						 //weggefiltert werden.
		
		if (mevalue < min)
			return false;//die Strategie ist raus
		if (mevalue > max)
			return false;//die Strategie ist raus
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
		if(endfitnessfilter.isNettoperstrategyflag()==true)
		{
			//falls wir den nettoprofit pro Strategy haben wollen dann, müssen wir die summe noch durch die reststrategien dividieren.
			sumval=sumval/anz;
		}
		
		endresult.setFitnessvalue(sumval);

		
		return (endresult);
	}
}
