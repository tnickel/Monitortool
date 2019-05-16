package bank;

import java.util.ArrayList;

import mainPackage.GC;

public class Bank
{
	// die klasse Bank besitzt viele Depots, jedes Depot ist stellvertretend für
	// einen Handelsalgorithmus
	public ArrayList<Depot> kundenliste = new ArrayList<Depot>();

	public void addKaufx(String symbol, int tid, int postid, String wkn,
			float kaufpreis, float k38kurs, float anzahl, String kaufdatum,
			String uname, String aktname, String grund)
	{
		// suche das depot
		int anz = kundenliste.size();
		for (int i = 0; i < anz; i++)
		{
			// sucht das Depot bei der Bank und addiere den Kauf
			Depot dep = kundenliste.get(i);
			if (dep.getUname().equals(uname))
			{
				dep.kauf(kaufdatum, aktname, symbol, tid, postid, uname,
						kaufpreis, k38kurs, anzahl, grund);
				return;
			}
		}
		// falls kein Depot da ist lege eins an und addiere den Kauf
		Depot dep = new Depot(GC.STARTSUMME, uname);
		kundenliste.add(dep);
		dep.kauf(kaufdatum, aktname, symbol, tid, postid, uname, kaufpreis,
				k38kurs, anzahl, grund);
	}

	public float getGesammtGewinn(String unam)
	{
		// für den user/algo wird der gesammtgewinn ermittelt
		int anz = kundenliste.size();
		for (int i = 0; i < anz; i++)
		{
			// sucht das Depot bei der Bank und addiere den Kauf
			Depot dep = kundenliste.get(i);
			if (dep.getUname().equals(unam))
			{
				return dep.getAktuellerGesGewinn();
			}
		}
		return 0;
	}

	public Depot getDepot(String unam)
	{
		// sucht das depot, falsl keins da ist wird eins angelegt
		int anz = kundenliste.size();
		for (int i = 0; i < anz; i++)
		{
			// sucht das Depot bei der Bank
			Depot dep = kundenliste.get(i);
			if (dep.getUname().equals(unam))
			{
				return dep;
			}
		}

		// kein depot da dann lege eins an
		Depot dep = new Depot(GC.STARTSUMME, unam);
		kundenliste.add(dep);
		return dep;
	}

	public void ueberpruefeAlleDepots(String testdatum, int leerverkaufsflag)
	{
		// Prüft bei allen depots ob da nicht was verkauft werden kann
		// gibt gewinn oder verlust zurück

		int anz = kundenliste.size();
		for (int i = 0; i < anz; i++)
		{
			// sucht das Depot bei der Bank und addiere den Kauf
			Depot dep = kundenliste.get(i);

			// falls der Kunde/Algo was im Depot hat überprüfe ob man da was
			// verkaufen kann
			if (dep.symbolmenge.size() > 0)
				dep.ueberpruefeVerkauefe(testdatum, leerverkaufsflag);
		}

		return;
	}
	public void speichere()
	{
		//speichere alle deposts
		int anz = kundenliste.size();
		for (int i = 0; i < anz; i++)
		{
			Depot dep = kundenliste.get(i);

			// falls der Kunde/Algo was im Depot hat überprüfe ob man da was
			// verkaufen kann
			dep.speichereGewinne();
		}

		return;
	}

	/*
	 * public void allesVerkaufen(String verkaufdatum) { // Alle Aktien
	 * verkaufen und das Cash einsammeln
	 * 
	 * int anz = kundenliste.size(); for (int i = 0; i < anz; i++) { // sucht
	 * das Depot bei der Bank und addiere den Kauf Depot dep =
	 * kundenliste.get(i); dep.verkaufeAlleSymbole(verkaufdatum); } }
	 */
}
