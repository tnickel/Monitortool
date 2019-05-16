package Metriklibs;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Metriktabelle
{
	// dies ist die Hauptklasse die alle Tabellen für einen Testzeitraum
	// eingelesen hat
	// Eine Tabelle besteht aus zeilen
	// aufbau: <filename>.csv
	// hier sind zeilenweise die attribute für jede strategie gespeichert
	private ArrayList<Metrikzeile> metriktab = new ArrayList<Metrikzeile>();
	// die Hashmap speichert für jeden Metriknamen die exakte position des
	// metrikvalues
	// Dies sind die namen der Zeilen vom Filterfile
	private HashMap<String, Integer> metrikzeilepos = new HashMap<String, Integer>();

	// speichert die namen des values
	// dies sind die namen der Spalten vom Filterfile
	private HashMap<String, Integer> valuemappos = new HashMap<String, Integer>();

	// jede Tabelle besitzt einen eindeutigen Filenamen
	private String filename_glob = null;

	public Metriktabelle()
	{

	}

	public String holeFilename()
	{
		return filename_glob;
	}

	public int getAnz()
	{
		return (metriktab.size());
	}

	public int getAttribAnz()
	{
		Metrikzeile met = metriktab.get(0);
		int anz = met.getLength();
		return anz;
	}

	public void readTabelle(String dirnam)
	{
		// hier wird die ganze Tabelle zeilenweise eingelesen und aufgebaut
		// die attribute aus der ersten Zeile sind hier ja schon bekannt
		String fnam = holeEinenFilenamen(dirnam);
		filename_glob = fnam;
		leseAttributeUndRest(fnam);
		refreshHashmap();
		refreshValuemap();
	}

	public void readTabelleFile(String fnam)
	{

		leseAttributeUndRest(fnam);

	}

	public Metrikzeile holeMetrikzeile(String stratname)
	{
		// den index holen

		if (metrikzeilepos == null)
			return null;

		// falls hashmap noch nicht initialisiert ist mache das
		if ((metrikzeilepos.size() + 1 < metriktab.size()))
			refreshHashmap();

		if (metrikzeilepos.containsKey(stratname) == false)
		{
			checkHashmap();
			return null;
		}
		int i = metrikzeilepos.get(stratname);

		Metrikzeile met = metriktab.get(i);
		Metrikentry me = met.holeEntry(0);
		if (me.getValue().equals(stratname) == false)
			Tracer.WriteTrace(10, "E: Internal Error metrikzeile stratname<"
					+ stratname + "> not found");
		else
			return met;

		return null;
	}

	public Metrikzeile holeMetrikzeilePosI(int i)
	{
		Metrikzeile met = metriktab.get(i);
		return met;
	}

	public String holeAttribname(int i)
	{
		// holt den namen des iten attributes
		Metrikzeile met = metriktab.get(0);
		Metrikentry me = met.holeEntry(i);
		return me.getAttributName();
	}

	private void leseAttributeUndRest(String filename)
	{
		// hier wird die metriktabelle eingelesen, für jedes verzeichniss gibt
		// es so eine
		// tabelle, auch für den endtest
		// hier wird erst nur der Zeilenkopf gelesen

		filename_glob = filename;
		Inf inf = new Inf();
		inf.setFilename(filename);

		// hier wird die metrik
		File metfile = new File(filename);
		if (metfile.exists() == false)
			Tracer.WriteTrace(10, "I:Metrikfile.csv <" + filename + "> missing");

		// die Attribute für die Klasse setzten
		String firstzeile = inf.readZeile();
		Metrikzeile metzeile = new Metrikzeile();
		metzeile.setzeAttribute(firstzeile);

		// dann nach und nach die Metrikzeilen aus den Strings generieren und
		// die Metriktabelle füllen
		while (5 == 5)
		{

			String zeile = inf.readZeile();
			if (zeile == null)
				break;

			metzeile = new Metrikzeile();
			// die Metrikzeile bauen
			metzeile.setzeAttribute(firstzeile);
			metzeile.baueMetrikzeile(zeile);

			// die Zeile hinzunehmen
			metriktab.add(metzeile);
		}
		inf.close();
		refreshHashmap();
		refreshValuemap();
	}

	private void refreshHashmap()
	{
		// baut die hashmap neu auf
		int anz = metriktab.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mez = metriktab.get(i);
			String attribname = mez.holeEntry(0).getValue();

			// wenn schon drin dann mach nix
			if (metrikzeilepos.containsKey(attribname) == true)
				continue;
			else
				// wenn noch nicht drin dann nimm auf
				metrikzeilepos.put(attribname, i);
		}
		// plausi
		int mettab_size = metriktab.size();
		int metrikmap_size = metrikzeilepos.size();
		if (mettab_size != metrikmap_size)
		{
			Tracer.WriteTrace(10, "E:internal plausi error mettabsize<"
					+ mettab_size + "> metmapsize<" + metrikmap_size + ">");
		}
		// hier wird nochmal alles geprüft
		// checkHashmap();
	}

	private void refreshValuemap()
	{
		// baut die valuemap
		Metrikzeile mez = metriktab.get(0);
		int anzvalues = mez.getLength();

		if (valuemappos.size() != anzvalues)
		{
			Tracer.WriteTrace(20, "I: baue valuemap neu auf");
			// baue die valuemap neu auf
			for (int i = 0; i < anzvalues; i++)
			{
				// holt den metrikentry welcher den floatwert für einen Eintrag
				// hat
				Metrikentry meentry = mez.holeEntry(i);
				String name = meentry.getAttributName();

				// speichere name+pos in der map
				valuemappos.put(name, i);
			}
		}
	}

	private void checkHashmap()
	{
		int foundflag = 0;
		// plausi
		int mettab_size = metriktab.size();
		int metrikmap_size = metrikzeilepos.size();
		if (mettab_size != metrikmap_size)
		{
			Tracer.WriteTrace(10, "E:internal plausi error mettabsize<"
					+ mettab_size + "> metmapsize<" + metrikmap_size + ">");
		}

		int anz = metriktab.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mez = metriktab.get(i);
			String attribname = mez.holeEntry(0).getValue();

			// wenn schon drin dann mach nix
			if (metrikzeilepos.containsKey(attribname) == true)
			{
				int pos = metrikzeilepos.get(attribname);
				if (pos != i)
					Tracer.WriteTrace(10,
							"E:internal hashmap pos-check failed attribname<"
									+ attribname + "> pos<" + i + ">");

				continue;
			} else
				Tracer.WriteTrace(10,
						"E:internal hashmap check failed attribname<"
								+ attribname + "> pos<" + i + ">");
		}

	}

	public double[] calcAttribvektor(String attribname)
	{
		double[] result = null;
		// es wird die spalte gesucht welche den attributnamen beinhaltet
		Metrikzeile mez = metriktab.get(1);

		int anzspalten = mez.getLength();
		for (int i = 0; i < anzspalten; i++)
		{
			Metrikentry me = mez.holeEntry(i);
			if (me.getAttributName().equals(attribname))
			{
				// die position gefunden
				result = calcAttribvektor(i);
				return result;
			}
		}
		Tracer.WriteTrace(10,
				"internal nettprofit nicht in endtesttabelle gefunden");
		return null;
	}

	public double[] calcAttribvektor(int attribindex)
	{
		// für das ite attribut wird der Attributvektor bestimmt
		// der attributvektor besteht aus n=anz zeilen floatwerten
		int tabsize = metriktab.size();
		double[] vektor = new double[metriktab.size()];

		// gehe durch die ganze Tabelle und baue den vektor von den einzelnen
		// Zeilen auf
		for (int i = 0; i < tabsize; i++)
		{
			// betrachte eine zeile der tabelle
			Metrikzeile mez = metriktab.get(i);
			Metrikentry me = mez.holeEntry(attribindex);

			// wenn dies kein floatwert ist dann liefer auch nix zurück
			if (me.getAttributflag() != 2)
				return null;
			vektor[i] = Double.valueOf(me.getValue());
		}
		return vektor;
	}

	public String holeEinenFilenamen(String dir)
	{
		// holt den einen filenamen aus dem verzeichniss
		// prüfe ab ob wirklich nur ein .csv-File in dem Verzeichnis

		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(dir, 1);
		int anz = fdirs.holeFileAnz();
		int count_csv = 0;
		String found_csv = null;
		for (int i = 0; i < anz; i++)
		{
			String fnam = fdirs.holeFileSystemName();
			if (fnam.contains(".csv"))
			{
				found_csv = fnam;
				count_csv++;
			}
		}
		if (count_csv > 1)
			Tracer.WriteTrace(10, "E:nur 1 csv-file in Verz<" + dir
					+ "> erlaubt");
		if (count_csv < 1)
			Tracer.WriteTrace(10, "E: no csv-file in directory<" + dir + ">");

		return dir + "\\" + found_csv;
	}

	private MinMaxFilter calcMinMaxFilter(String attribut)
	{
		// hier wird der min-maxfilter für eine Tabelle berechnet
		// der min-max wert bezieht sich immer auf ein attribut
		float minval = 999999;
		float maxval = 0;
		int anzsteps = 10;

		MinMaxFilter minmaxf = new MinMaxFilter();
		int anz = metriktab.size();

		// gehe durch die einzelnen Zeilen und ermittele min max für das
		// attribut
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile metrikzeile = metriktab.get(i);
			float val = holeFloatwert(metrikzeile, attribut);

			if (val > maxval)
				maxval = val;
			if (val < minval)
				minval = val;
		}
		// diese werte sind für die einfachberechnung also die berechnung ohne
		// automatische suche
		minmaxf.setAttribut(attribut);
		minmaxf.setMaxfilevalue(maxval);
		minmaxf.setMaxvalue(maxval);
		minmaxf.setMinfilevalue(minval);
		minmaxf.setMinvalue(minval);
		minmaxf.setAnzSteps(anzsteps);
		// hier kommen die parameter für die automatische suche
		minmaxf.setAktMaxValue(maxval);
		minmaxf.setAktMinValue(minval);

		return (minmaxf);
	}

	public Filterzeitraum calcFilterzeitraum()
	{
		// aus dieser exitierende Metriktabelle wird der Filterzeitrum generiert
		// der Filterzeitraum beinhaltet die min max werte für ein Attribut
		Filterzeitraum filtr = new Filterzeitraum();

		filtr.setFilename(filename_glob);

		Metrikzeile mz = metriktab.get(0);

		int anzmetriken = mz.getLength();
		for (int i = 0; i < anzmetriken; i++)
		{
			// sammle für jedes attribut die min/max werte
			Metrikentry met = mz.holeEntry(i);
			String attribut = met.getAttributName();
			MinMaxFilter mimax = calcMinMaxFilter(attribut);

			filtr.addFilterEntry(mimax);
		}
		return filtr;
	}

	public ArrayList<Stratelem> buildStratliste()
	{
		ArrayList<Stratelem> stratl = new ArrayList<Stratelem>();
		// Stratliste mit den Attributsnamen aufbauen
		int anz = metriktab.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mz = metriktab.get(i);
			// der strname ist im ersten attribut
			String attrib = mz.holeEntry(0).getValue();
			Stratelem strate = new Stratelem();
			strate.setStratname(attrib);
			strate.setActiveflag(1);
			stratl.add(strate);

			/*
			 * if (i % 2000 == 0) System.out.println("buildStratlist i=" + i);
			 */
		}
		return stratl;
	}

	public Metrikentry holeMetrikentry(Metrikzeile examplezeile,
			String valuename)
	{
		// aus der metrikzeile wird ein bestimmtes attribut ausgewählt und
		// an dieser position steht das attribut
		// mapturbo!
		int pos = valuemappos.get(valuename);

		Metrikentry me = examplezeile.holeEntry(pos);

		if (me == null)
			return null;

		return me;
	}

	public float holeFloatwert(Metrikzeile examplezeile, String valuename)
	{
		// aus der metrikzeile wird ein bestimmtes attribut ausgewählt und
		// dessen
		// floatwert zurückgeliefert
		float fval = 0;
		// an dieser position steht der floatwert für das attribut
		// mapturbo!
		int floatpos = valuemappos.get(valuename);

		Metrikentry me = examplezeile.holeEntry(floatpos);

		if (me == null)
			return 0;
		try
		{
			// falls der metrikentry ein float-attribut hat, dann wird der wert
			// ausgelesen
			if (me.getAttributflag() == 2)
			{
				fval = Float.valueOf(me.getValue());
				return fval;
			}
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(20, "Zeile<" + examplezeile
					+ "> fehlerhaft valuename<" + valuename
					+ "> kann nicht in Float gewandelt werden");
		}
		// wenn das kein floatwert hat
		return 0;
	}
}
