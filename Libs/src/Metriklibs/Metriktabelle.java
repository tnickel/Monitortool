package Metriklibs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import compareLibs.ComparatorMetrikzeile;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class Metriktabelle
{
	// dies ist die Hauptklasse die alle Tabellen für einen Testzeitraum
	// eingelesen hat
	// Eine Tabelle besteht aus zeilen
	// aufbau: <filename>.csv
	// hier sind zeilenweise die attribute für jede strategie gespeichert
	private ArrayList<Metrikzeile> metriktab_glob = new ArrayList<Metrikzeile>();
	// die Hashmap speichert für jeden Metriknamen die exakte position des
	// metrikvalues
	// Dies sind die namen der Zeilen vom Filterfile
	private HashMap<String, Integer> hashMapMetrikzeilepos = new HashMap<String, Integer>();
	
	// speichert die namen des values
	// dies sind die namen der Spalten vom Filterfile
	private HashMap<String, Integer> hashMapValuemappos = new HashMap<String, Integer>();
	
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
		return (metriktab_glob.size());
	}
	
	public int getAttribAnz()
	{
		Metrikzeile met = metriktab_glob.get(0);
		int anz = met.getLength();
		return anz;
	}
	
	public boolean readExportedTable(StrategieMengen msel, String dirnam)
	{
		// hier wird die ganze Tabelle zeilenweise eingelesen und aufgebaut
		// die attribute aus der ersten Zeile sind hier ja schon bekannt
		String fnam = holeEinenFilenamen(dirnam);
		if(fnam==null)
		{
			Tracer.WriteTrace(20, "E: no exported table file in <"+dirnam+">");
			return false;
		}
		filename_glob = fnam;
		leseAttributeUndRestSorted(msel, fnam);
		
		//this is only a check
		//this.WriteMetriktabelle(fnam + "sorted");
		refreshHashmap();
		refreshValuemap();
		return true;
	}
	
	public void readExportedTableFile(StrategieMengen msel, String fnam)
	{
		
		leseAttributeUndRestSorted(msel, fnam);
		
	}
	
	public Metrikzeile getMetrikzeile(String stratname)
	{
		// den index holen
		
		if (hashMapMetrikzeilepos == null)
			return null;
		
		// falls hashmap noch nicht initialisiert ist mache das
		if ((hashMapMetrikzeilepos.size() + 1 < metriktab_glob.size()))
			refreshHashmap();
		
		if (hashMapMetrikzeilepos.containsKey(stratname) == false)
		{
			checkHashmap();
			return null;
		}
		int i = hashMapMetrikzeilepos.get(stratname);
		
		Metrikzeile met = metriktab_glob.get(i);
		Metrikentry me = met.holeEntry(0);
		if (me.getValue().equals(stratname) == false)
			Tracer.WriteTrace(10, "E: Internal Error metrikzeile stratname<" + stratname + "> not found");
		else
			return met;
		
		return null;
	}
	
	public Metrikzeile holeMetrikzeilePosI(int i)
	{
		// holt die zeile i aus der Tabelle.
		Metrikzeile met = metriktab_glob.get(i);
		return met;
	}
	
	public String holeAttribname(int i)
	{
		// hole den Attributnamen der zeile i aus der Tabelle
		// holt den namen des iten attributes
		Metrikzeile met = metriktab_glob.get(0);
		Metrikentry me = met.holeEntry(i);
		return me.getAttributName();
	}
	
	private void leseAttributeUndRestSorted(StrategieMengen msel, String filename)
	{
		// hier wird die metriktabelle eingelesen, für jedes verzeichniss gibt
		// es so eine
		// tabelle, auch für den endtest
		// hier wird erst nur der Zeilenkopf gelesen
		// falls msel==null dann wir die strategie auf jeden fall aufgenommen
		
		filename_glob = filename;
		Inf inf = new Inf();
		inf.setFilename(filename);
		
		// hier wird die metrik
		File metfile = new File(filename);
		if (metfile.exists() == false)
			Tracer.WriteTrace(10, "I:Metrikfile.csv <" + filename + "> missing");
		
		// die Attribute für die Klasse setzten
		String firstzeile = inf.readZeile().replace("\"", "").replace("#", "anz");
		
		Metrikzeile metzeile = new Metrikzeile();
		metzeile.setzeAttribute(firstzeile);
		Tracer.WriteTrace(50, "Metrikzeile header hat <" + metzeile.getAttributanz() + "> Elemente");
		
		// dann nach und nach die Metrikzeilen aus den Strings generieren und
		// die Metriktabelle füllen
		while (5 == 5)
		{
			
			String zeile = inf.readZeile();// .replace("#", "anz");
			
			if (zeile == null)
				break;
			
			// filter results will be deleted
			zeile = zeile.replace("\"PASSED\";", "1;");
			zeile = zeile.replace("\"FAILED\";", "0;");
			
			// Anführungzeichen weg
			zeile = zeile.replace("\"", "");
			// route durch anzahl
			zeile = zeile.replace("#", "anz");
			
			// ;; darf nicht, da muss ne 0 drin stehen
			zeile = zeile.replace(";;", ";0;");
			
			metzeile = new Metrikzeile();
			// die Metrikzeile bauen
			
			metzeile.setzeAttribute(firstzeile);
			metzeile.baueMetrikzeile(zeile);
			
			String stratname = metzeile.holeStratName();
			if (stratname == null)
				break;
			
			if ((msel != null) && (msel.containsName(stratname) == false))
				continue;
			
			// die Zeile hinzunehmen
			metriktab_glob.add(metzeile);
		}
		inf.close();
		Collections.sort(metriktab_glob, new ComparatorMetrikzeile()); // wir müssen die tabelle noch sortieren nach
																		// namen sonst klappt das nicht mit dem
																		// endtest.!!!
		
		refreshHashmap();
		refreshValuemap();
	}
	
	private void refreshHashmap()
	{
		hashMapMetrikzeilepos.clear();
		
		// baut die hashmap neu auf
		int anz = metriktab_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mez = metriktab_glob.get(i);
			String attribname = mez.holeEntry(0).getValue();
			
			// wenn schon drin dann mach nix
			if (hashMapMetrikzeilepos.containsKey(attribname) == true)
				continue;
			else
				// wenn noch nicht drin dann nimm auf
				hashMapMetrikzeilepos.put(attribname, i);
		}
		// plausi
		int mettab_size = metriktab_glob.size();
		int metrikmap_size = hashMapMetrikzeilepos.size();
		if (mettab_size != metrikmap_size)
		{
			Tracer.WriteTrace(10,
					"E:internal plausi error mettabsize<" + mettab_size + "> metmapsize<" + metrikmap_size + ">");
		}
		// hier wird nochmal alles geprüft
		// checkHashmap();
	}
	
	private void refreshValuemap()
	{
		
		if (hashMapValuemappos == null)
			Tracer.WriteTrace(10, "E:hashMapValuemappos==null-->stop");
		
		hashMapValuemappos.clear();
		// baut die valuemap
		
		if (metriktab_glob == null)
			Tracer.WriteTrace(10, "E:metriktab<_glob==null-->stop");
		
		Metrikzeile mez = metriktab_glob.get(0);
		
		if (mez == null)
			Tracer.WriteTrace(10, "I:mez=null-> stop");
		
		int anzvalues = mez.getLength();
		if (anzvalues == 0)
			Tracer.WriteTrace(10, "I:anz values==0");
		
		if (hashMapValuemappos.size() != anzvalues)
		{
			Tracer.WriteTrace(50, "I: baue valuemap neu auf");
			// baue die valuemap neu auf
			for (int i = 0; i < anzvalues; i++)
			{
				// holt den metrikentry welcher den floatwert für einen Eintrag
				// hat
				Metrikentry meentry = mez.holeEntry(i);
				String name = meentry.getAttributName();
				
				// speichere name+pos in der map
				hashMapValuemappos.put(name, i);
			}
		}
	}
	
	private void checkHashmap()
	{
		int foundflag = 0;
		// plausi
		int mettab_size = metriktab_glob.size();
		int metrikmap_size = hashMapMetrikzeilepos.size();
		if (mettab_size != metrikmap_size)
		{
			Tracer.WriteTrace(10,
					"E:internal plausi error mettabsize<" + mettab_size + "> metmapsize<" + metrikmap_size + ">");
		}
		
		int anz = metriktab_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mez = metriktab_glob.get(i);
			String attribname = mez.holeEntry(0).getValue();
			
			// wenn schon drin dann mach nix
			if (hashMapMetrikzeilepos.containsKey(attribname) == true)
			{
				int pos = hashMapMetrikzeilepos.get(attribname);
				if (pos != i)
					Tracer.WriteTrace(10,
							"E:internal hashmap pos-check failed attribname<" + attribname + "> pos<" + i + ">");
				
				continue;
			} else
				Tracer.WriteTrace(10, "E:internal hashmap check failed attribname<" + attribname + "> pos<" + i + ">");
		}
		
	}
	
	public double[] calcAttribvektor(String attribname)
	{
		double[] result = null;
		// es wird die spalte gesucht welche den attributnamen beinhaltet
		Metrikzeile mez = metriktab_glob.get(1);
		
		int anzspalten = mez.getLength();
		for (int i = 0; i < anzspalten; i++)
		{
			Metrikentry me = mez.holeEntry(i);
			if (me.getAttributName().toLowerCase().contains(attribname.toLowerCase()))
			{
				// die spalte gefunden gefunden
				result = calcAttribvektor(i);
				return result;
			}
		}
		Tracer.WriteTrace(10, "internal attribname<" + attribname + "> not found in endtable");
		return null;
	}
	
	public double[] calcAttribvektor(int attribindex)
	{
		// für das ite attribut wird der Attributvektor bestimmt
		// der attributvektor besteht aus n=anz zeilen floatwerten
		int tabsize = metriktab_glob.size();
		double[] vektor = new double[metriktab_glob.size()];
		
		// gehe durch die ganze Tabelle und baue den vektor von den einzelnen
		// Zeilen auf
		for (int i = 0; i < tabsize; i++)
		{
			// betrachte eine zeile der tabelle
			Metrikzeile mez = metriktab_glob.get(i);
			Metrikentry me = mez.holeEntry(attribindex);
			
			// wenn dies kein floatwert ist dann liefer auch nix zurück
			if (me.getAttributflag() != 2)
				return null;
			vektor[i] = Double.valueOf(me.getValue());
		}
		return vektor;
	}
	
	public double[][] calcAttribMatrix()
	{
		// gehe durch die Elemente und baue die Matrix auf. Jede zeile ist eine
		// Strategie mit attributen, jede spalte dieser
		// zeile hat attribute.
		
		int tabsize = metriktab_glob.size(); // soviele zeilen sind drin
		int attribanz = this.getAttribAnz();
		// die Matrix hat n-spalten, jede spalte beinhaltet eine metrik
		// davon gibt es n zeilen, jede zeilen steht für ein strategie
		double[][] matrix = new double[metriktab_glob.size()][attribanz];
		double val = 0;
		
		// gehe durch die ganze Tabelle und baue den vektor von den einzelnen
		// Zeilen auf
		// gehe durch die Zeilen=Strategien
		for (int i = 0; i < tabsize; i++)
		{
			
			// gehe durch die attribute, betrachte hier nur eine zeile der Tabelle und
			// gehe durch die attribute
			for (int j = 0; j < attribanz; j++)
			{
				
				Metrikzeile mez = metriktab_glob.get(i);
				Metrikentry me = mez.holeEntry(j);
				
				// wenn dies kein floatwert ist dann liefer auch nix zurück
				if (me.getAttributflag() != 2)
					val = 0;
				else
					val = Double.valueOf(me.getValue());
				
				matrix[i][j] = val;
			}
		}
		return matrix;
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
			if (fnam.endsWith(".csv"))
			{
				found_csv = fnam;
				count_csv++;
			}
		}
		if (count_csv > 1)
			Tracer.WriteTrace(10, "E:nur 1 csv-file in Verz<" + dir + "> erlaubt");
		if (count_csv < 1)
		{
			Tracer.WriteTrace(10, "E: no csv-file in directory<" + dir + ">");
			return null;
		}
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
		int anz = metriktab_glob.size();
		
		// gehe durch die einzelnen Zeilen und ermittele min max für das
		// attribut
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile metrikzeile = metriktab_glob.get(i);
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
	
	public Filterfile calcFilterzeitraum()
	{
		
		// was ist ein Filterzeitraum???
		// Ein Filterzeitraum gilt für eine Metriktabelle, er wird aus der Metriktabelle
		// generiert.
		// Für jeden dieser bereiche dir0.... dirn... dir99 exisitert ein Filterzeitraum
		// aus dieser exitierende Metriktabelle wird der Filterzeitrum generiert
		// der Filterzeitraum beinhaltet die min max werte für ein Attribut
		Filterfile filtr = new Filterfile();
		
		filtr.setFilename(filename_glob);
		Metrikzeile mz = metriktab_glob.get(0);
		
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
	
	public ArrayList<Stratelem> buildStratliste(StrategieMengen stratsel)
	{
		// takeallpoolflag==1, dann wird der ganze pool mi dem index i verwendet.
		ArrayList<Stratelem> stratl = new ArrayList<Stratelem>();
		// Stratliste mit den Attributsnamen aufbauen
		int anz = metriktab_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mz = metriktab_glob.get(i);
			// der strname ist im ersten attribut
			String attrib = mz.holeEntry(0).getValue();
			
			// es werden nur bestimmte strategien in die strategieliste aufgenommen.
			// beispiel IS oder OOS
			if (stratsel.containsName(attrib) == false)
				continue;
			
			Stratelem strate = new Stratelem();
			strate.setStratname(attrib);
			strate.setActiveflag(1);
			stratl.add(strate);
			
			// if (i % 2000 == 0) System.out.println("buildStratlist i=" + i);
			
		}
		return stratl;
	}
	
	public ArrayList<Stratelem> buildAllStratliste()
	{
		ArrayList<Stratelem> stratl = new ArrayList<Stratelem>();
		// Stratliste mit den Attributsnamen aufbauen
		int anz = metriktab_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mz = metriktab_glob.get(i);
			// der strname ist im ersten attribut
			String attrib = mz.holeEntry(0).getValue();
			
			Stratelem strate = new Stratelem();
			strate.setStratname(attrib);
			strate.setActiveflag(1);
			stratl.add(strate);
			
			// if (i % 2000 == 0) System.out.println("buildStratlist i=" + i);
			
		}
		return stratl;
	}
	
	public Metrikentry holeMetrikentry(Metrikzeile examplezeile, String valuename)
	{
		// aus der metrikzeile wird ein bestimmtes attribut ausgewählt und
		// an dieser position steht das attribut
		// mapturbo!
		int pos = hashMapValuemappos.get(valuename);
		
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
		if (hashMapValuemappos == null)
			Tracer.WriteTrace(10, "E: internal this is null holeFloatwert");
		
		if (hashMapValuemappos.containsKey(valuename) == false)
			Tracer.WriteTrace(10, "value<" + valuename + "> not in tableline <"
					+ examplezeile.getAllAttributsNamesAsString(";", 0, true) + ">");
		
		int floatpos = hashMapValuemappos.get(valuename);
		
		Metrikentry me = examplezeile.holeEntry(floatpos);
		
		if (me == null)
			return 0;
		try
		{
			// falls der metrikentry ein float-attribut hat, dann wird der wert
			// ausgelesen
			if (me.getAttributflag() == 2)// 2 heisst floatwert
			{
				fval = Float.valueOf(me.getValue());
				return fval;
			}
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(20, "Zeile<" + examplezeile + "> fehlerhaft valuename<" + valuename
					+ "> kann nicht in Float gewandelt werden");
		}
		// wenn das kein floatwert hat
		return 0;
	}
	
	static public ArrayList<Metrikzeile> leseMetriktabelleSimple(String filename)
	{
		// Wir brauchen hier nur die Strategienamen aus einem datenbankfile
		// databankExport.csv
		
		ArrayList<Metrikzeile> stratliste = new ArrayList<Metrikzeile>();
		
		Inf inf = new Inf();
		inf.setFilename(filename);
		
		// hier wird die metrik
		File metfile = new File(filename);
		if (metfile.exists() == false)
			Tracer.WriteTrace(10, "I:Metrikfile.csv <" + filename + "> missing");
		
		// die Attribute für die Klasse setzten
		String firstzeile = inf.readZeile().replace("\"", "").replace("#", "anz");
		Metrikzeile metzeile = new Metrikzeile();
		metzeile.setzeAttribute(firstzeile);
		
		// dann nach und nach die Metrikzeilen aus den Strings generieren und
		// die Metriktabelle füllen
		while (5 == 5)
		{
			
			String zeile = inf.readZeile();// .replace("#", "anz");
			if (zeile == null)
				break;
			
			zeile = zeile.replace("\"", "");
			zeile = zeile.replace("#", "anz");
			
			metzeile = new Metrikzeile();
			// die Metrikzeile bauen
			metzeile.setzeAttribute(firstzeile);
			metzeile.baueMetrikzeile(zeile);
			
			// die Zeile hinzunehmen
			stratliste.add(metzeile);
		}
		inf.close();
		return stratliste;
	}
	
	public void WriteMetriktabelle(String filenam)
	{
		FileAccess.FileDelete(filenam, 1);
		Inf inf = new Inf();
		inf.setFilename(filenam);
		String zeile = null;
		
		int anzlines = metriktab_glob.size();
		for (int i = 0; i < anzlines; i++)
		{
			Metrikzeile mez = metriktab_glob.get(i);
			int anz2 = mez.getAttributanz();
			if (i == 0)
			{
				zeile = mez.getAllAttributsNamesAsString(";", -99, true);
				inf.writezeile(zeile);
			}
			zeile = mez.getAllAttributsValuesAsString(";", true);
			inf.writezeile(zeile);
		}
		inf.close();
	}
	
}
