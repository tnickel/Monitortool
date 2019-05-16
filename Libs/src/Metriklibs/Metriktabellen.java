package Metriklibs;

import hiflsklasse.Tracer;

import java.util.ArrayList;

public class Metriktabellen
{
	// hier sind alle Metriktabellen drin
	// Eine Metriktabelle besteht aus Metrikzeilen
	// Metrikzeilen sind die EAs mit den entsprechenden Attributen
	private ArrayList<Metriktabelle> metriktabellen = new ArrayList<Metriktabelle>();

	public int getAnz()
	{
		return metriktabellen.size();
	}
	public int getAnzFilter()
	{
		return metriktabellen.size()-1;
	}

	public void readAllTabellen(String rpath)
	{
		// liest alle Metriktabellen ein
		metriktabellen = new ArrayList<Metriktabelle>();
		/*String rpath = Metrikglobalconf.getFilterpath();*/

		if(rpath==null)
			Tracer.WriteTrace(10, "E: please set configpath in File/config");
		if(rpath.length()<3)
			Tracer.WriteTrace(10, "E: please set configpath [path to short] in File/config");
		
		// holt die liste der Verzeichnisse
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(rpath, 0);

		int anz = fdirs.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String dirnam = rpath + "\\" + fdirs.holeFileSystemName();
			Metriktabelle mettabelle = new Metriktabelle();

			// Die Tabellen einlesen
			mettabelle.readTabelle(dirnam);
			// die eingelesene tabelle in der gesammtliste speichern
			metriktabellen.add(mettabelle);
			System.out.println("Read table i="+i+" <"+dirnam+"> ready");
		}
	}

	
	
	
	public Metriktabelle holeNummerI(int j)
	{
		//holt die Metriktabelle mit der nummer i aus der internen datenstruktur
		int anz = metriktabellen.size();
		for (int i = 0; i < anz; i++)
		{
			Metriktabelle met = metriktabellen.get(i);
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_"))
				return (met);
		}
		//Tracer.WriteTrace(20, "E:internal error metriktrablle mit nummer<"+j+"> nicht gefunden");
		return null;
	}

	public void getallFilterzeitraume(ArrayList<Filterzeitraum> filtzeitraume)
	{
		// fuer alle metriktabellen weden die filterzeitraume generiert und
		// zurückgeliefert

		int anz = getAnz();
		for (int i = 0; i < anz; i++)
		{
			Filterzeitraum filt = metriktabellen.get(i).calcFilterzeitraum();
			filtzeitraume.add(filt);
			if(i%1000==0)
				System.out.println("get Filtzeitraum i="+i);
		}
		return;
	}

	public String getFilename(int j)
	{
		// holt den Filenamen der Metriktabelle j
		return (metriktabellen.get(j).holeFilename());
	}

	public ArrayList<Stratelem> buildStratliste()
	{
		//die Strategielist wird aus den Metriken von verz _0_ erzeugt
		return (metriktabellen.get(0).buildStratliste());
	}

	public int calcanzFilter()
	{
		// Sicherheitsscheck
		// prüft nach ob alle Tabellen in der internen Datenstruktur vorhanen
		// sind
		// -1, da die tabelle für den Endtest nicht zählt
		int anztesttabellen = getAnzFilter();
		// Tracer.WriteTrace(20, "I: anz Filtertabellen=" + anztesttabellen);

		// namenscheck
		for (int i = 0; i < anztesttabellen; i++)
		{
			if (checkTabellennumber(i) == false)
				Tracer.WriteTrace(10, "E:tabellenummer _" + i + "_ fehlt");
		}
		return (anztesttabellen);
	}

	private boolean checkTabellennumber(int j)
	{
		// tabellenname muss _1_xxx enthalten
		// prüft ob tabelle nr j in der Tabellenmenge vorhanden ist
		int anztesttabellen = metriktabellen.size();
		for (int i = 0; i < anztesttabellen; i++)
		{
			Metriktabelle met = metriktabellen.get(i);
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_") == true)
				return true;
		}
		return false;
	}

	public Metriktabelle holeEndtestMetriktable()
	{
		// erst mal die endtabelle suchen und zurückliefern
		int anz = metriktabellen.size();
		for (int i = 0; i < anz; i++)
		{
			String fname = null;
			Metriktabelle mettab = metriktabellen.get(i);
			fname = mettab.holeFilename();
			if (fname.contains("_99_") == true)
				return mettab;
		}
		Tracer.WriteTrace(10, "E: keine Endtabelle gefunden _99_");
		return null;
	}

	public float holeEndtestValue(String stratname,EndtestFitnessfilter endfitfilter)
	{
		// holt für den Stratnamen den endtest aus den tabellen

		// hole die endtabelle
		Metriktabelle endtabelle = holeEndtestMetriktable();
		// hole die passende endzeile
		Metrikzeile mz = endtabelle.holeMetrikzeile(stratname);

		float val=0,val2=0;
		if(endfitfilter.isNettoflag())
		{
			// jetzt den floatwert für oos
			val = endtabelle.holeFloatwert(mz,"Net profit");
		}
		else if(endfitfilter.isNettostabilflag())
		{
			val = endtabelle.holeFloatwert(mz,"Net profit");
			val2= endtabelle.holeFloatwert(mz,"Stability");
			val=val*val2;
		}
		else if(endfitfilter.isNettorobustflag())
		{
			val = endtabelle.holeFloatwert(mz,"Net profit (RT)");
		}
		else if(endfitfilter.isNettorobuststabilflag())
		{
			val = endtabelle.holeFloatwert(mz,"Net profit (RT)");
			val2= endtabelle.holeFloatwert(mz,"Stability");
			val=val*val2;
		}
		else if(endfitfilter.isStabilflag())
		{
			val= endtabelle.holeFloatwert(mz,"Stability");
		}
		else
			Tracer.WriteTrace(10, "E: internal error, endfitfilter not set");
		return val;
	}

	public String holeEndtestFilnamen()
	{
		//holt den Filenamen der Endtesttabelle aus der Datenstruktur
		Metriktabelle endtabelle = holeEndtestMetriktable();
		String fnam = endtabelle.holeFilename().replace(".csv", ".result");
		return fnam;
	}
}
