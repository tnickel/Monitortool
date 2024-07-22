package filterPack;

import java.io.File;
import java.util.ArrayList;

import Metriklibs.Filterfile;
import Metriklibs.Metriktabellen;
import data.CorelSetting;
import data.Metrikglobalconf;
import hiflsklasse.Tracer;


public class Filterfiles
{
	// die Filterzeitr�ume beinhalten f�r jede Metrik einen min/max wert
	// Die filterzeitr�ume werden f�r das anschliessende filtern ben�tigt.
	// Es gibt nur n-filterzeitr�ume, ein filterzeitraum deckt also viel ab
	// bei n=3 gibt es 2 zeitr�ume + einen Endtest
	// ein Filterzeitraum besteht aus einer Liste von Metriken mit Werten
	private ArrayList<Filterfile> filterfiles_glob = new ArrayList<Filterfile>();

	public Filterfiles()
	{
	}

	public int getAnz()
	{
		return (filterfiles_glob.size());
	}

	public boolean checkConfigAvailable(Metriktabellen met)
	{
		// es wird die erste filterdatei �berpr�ft
		// wenn die da ist wird ein true zur�ckgeliefert
		Filterfile filtzeit = holeFilterzeitraumNummerI(0);
		String fnam = filtzeit.holeFilename().replace(".csv", ".filter");
		File fnamf = new File(fnam);
		if (fnamf.exists() == true)
			return true;
		else
			return false;
	}

	public void readFilterSettings(Metriktabellen met)
	{
		// hier werden alle metrikfilter wenn sie schon da sind eingelesen
		int anzmetrikfilterfiles = met.getAnzMetriktabellen();
		for (int i = 0; i < anzmetrikfilterfiles; i++)
		{
			
		
			Filterfile filterzeit = new Filterfile();
			String filtername = met.getFilename(i).replace(".csv", ".filter");
			filterzeit.setFilename(filtername);

			// falls endtest dann setzte globalen pfad
			if (filtername.contains("_99_"))
			{
				// den globalen endtestpath setzen
				String mepath = filtername.substring(0,
						filtername.lastIndexOf("\\"));
				Metrikglobalconf.setEndtestpath(mepath);
			}
			filterzeit.readFilter();
			filterfiles_glob.add(filterzeit);
		}
	}

	public void cleanFilterSettings(Metriktabellen met)
	{
		// die filtersettings files auf platte werden gel�scht
		int anzmetrikfilterfiles = met.getAnzMetriktabellen();
		for (int i = 0; i < anzmetrikfilterfiles; i++)
		{
			String filtername = met.getFilename(i).replace(".csv", ".filter");
			File filternam = new File(filtername);
			if (filternam.exists())
				filternam.delete();
		}
	}

	public Filterfile holeFilterzeitraumNummerI(int j)
	{
		// hole den filterzeitraum mit er nummer "_j_"
		int anz = filterfiles_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Filterfile met = filterfiles_glob.get(i);
			if (met == null)
				Tracer.WriteTrace(10, "internal: f�r <" + j
						+ "> kein file mit _" + j + "_ gefunden");
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_"))
				return (met);
		}
		return null;
	}

	public void writeAll()
	{
		// hier werden alle metrikfilter wenn sie da sind geschrieben
	}

	public void writeFilterSettings()
	{
		int anz = filterfiles_glob.size();
		for (int i = 0; i < anz; i++)
		{
			filterfiles_glob.get(i).writeFilterSettings();
		}
	}

	public void genAllFilterzeitraume(Metriktabellen mettabellen)
	{
		// hier wird aus den Metriktabellen die Filterzeitr�ume berechnet
		// d.h. ich muss mich durch die Metriktabellen w�hlen und hierraus
		// filterlisten/filterzeitr�ume bauen
		// so ein filterzeitraum beinhaltet im prinzip die min-maxwerte f�r ein
		// Attribut
		// die Filterzeitr�ume kann man aus den metriktabellen generieren, da
		// hier s�mtliche Informationen drin sind
		mettabellen.getallFilterfiles(filterfiles_glob);
	}

	public void add(Filterfile filtz)
	{
		filterfiles_glob.add(filtz);
	}

	public void modifyLimitsRandom(boolean useonlyselectedmetrics)
	{
		// hier werden randomm�ssig die schranken modifiziert
		// gehe durch alle filterzeitr�ume und modifiziere
		int anz = filterfiles_glob.size();
		for (int i = 0; i < anz; i++)
		{
			// filterzeit beinhaltet ein einzelnes verzeichniss
			Filterfile filtzeit = filterfiles_glob.get(i);
			// hier werden f�r jeden Abschnitt die Schranken zuf�llig ge�ndert
			filtzeit.modifyAllAttribRandom(useonlyselectedmetrics);
		}
	}
	
	public void modifyLimitsRandom2(boolean useonlyselectedmetrics)
	{
		// hier werden randomm�ssig die schranken modifiziert
		// gehe durch alle filterzeitr�ume und modifiziere
		int anz = filterfiles_glob.size();
		for (int i = 0; i < anz; i++)
		{
			// filterzeit beinhaltet ein einzelnes verzeichniss
			Filterfile filtzeit = filterfiles_glob.get(i);
			// hier werden f�r jeden Abschnitt die Schranken zuf�llig ge�ndert
			filtzeit.modifyAllAttribRandom2(useonlyselectedmetrics);
		}
	}
	
	public void modifyLimitsRandomPlus(boolean useonlyselectedmetrics)
	{
		//Beim RandomPlus wird nicht komplett alles neu gew�rfelt, sondern es wird der Filterzeitraum
		//genommen und an den schranken nur etwas ger�ttelt. Wir haben ja jetzt einen Filterzeitraum aus der bestliste geholt
		// gehe durch alle filterzeitr�ume und modifiziere nur ein bissel
		int anz = filterfiles_glob.size();
		for (int i = 0; i < anz; i++)
		{
			// filterzeit beinhaltet ein einzelnes verzeichniss
			Filterfile filtzeit = filterfiles_glob.get(i);
			// hier werden f�r jeden Abschnitt die Schranken zuf�llig ge�ndert
			filtzeit.modifyAllAttribRandomPlus(useonlyselectedmetrics);
		}
	}
	public void modifyLimitsPersonKorrelation(CorelSetting corelsetting,boolean useonlyselectedmetrics)
	{
		// hier werden nach der PersonKorrelation die schranken modifiziert
		// gehe durch alle filterzeitr�ume und modifiziere
		int anz = filterfiles_glob.size();
		//anz-1 da letzte tabelle die profittabelle sind
		for (int i = 0; i < anz-1; i++)
		{
			// filterzeit beinhaltet ein einzelnes verzeichniss
			Filterfile filtzeit = filterfiles_glob.get(i);
			// hier werden f�r jeden Abschnitt die Schranken zuf�llig ge�ndert
			filtzeit.modifySchrankePersonCorel(i, corelsetting,useonlyselectedmetrics);
		}
	}
}
