package filterPack;

import hilfsklasse.Swttool;
import hilfsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import calcPack.CalOpt100Sammler;

import Metriklibs.Filterzeitraum;
import Metriklibs.Metriktabellen;
import data.CorelSetting;
import data.Metrikglobalconf;


public class Filterzeitraume
{
	// die Filterzeiträume beinhalten für jede Metrik einen min/max wert
	// Die filterzeiträume werden für das anschliessende filtern benötigt.
	// Es gibt nur n-filterzeiträume, ein filterzeitraum deckt also viel ab
	// bei n=3 gibt es 2 zeiträume + einen Endtest
	// ein Filterzeitraum besteht aus einer Liste von Metriken mit Werten
	private ArrayList<Filterzeitraum> filterzeitraume_glob = new ArrayList<Filterzeitraum>();

	public Filterzeitraume()
	{
	}

	public int getAnz()
	{
		return (filterzeitraume_glob.size());
	}

	public boolean checkConfigAvailable(Metriktabellen met)
	{
		// es wird die erste filterdatei überprüft
		// wenn die da ist wird ein true zurückgeliefert
		Filterzeitraum filtzeit = holeFilterzeitraumNummerI(0);
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
		int anzmetrikfilterfiles = met.getAnz();
		for (int i = 0; i < anzmetrikfilterfiles; i++)
		{
			
		
			Filterzeitraum filterzeit = new Filterzeitraum();
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
			filterzeitraume_glob.add(filterzeit);
		}
	}

	public void cleanFilterSettings(Metriktabellen met)
	{
		// die filtersettings files auf platte werden gelöscht
		int anzmetrikfilterfiles = met.getAnz();
		for (int i = 0; i < anzmetrikfilterfiles; i++)
		{
			String filtername = met.getFilename(i).replace(".csv", ".filter");
			File filternam = new File(filtername);
			if (filternam.exists())
				filternam.delete();
		}
	}

	public Filterzeitraum holeFilterzeitraumNummerI(int j)
	{
		// hole den filterzeitraum mit er nummer "_j_"
		int anz = filterzeitraume_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Filterzeitraum met = filterzeitraume_glob.get(i);
			if (met == null)
				Tracer.WriteTrace(10, "internal: für <" + j
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
		int anz = filterzeitraume_glob.size();
		for (int i = 0; i < anz; i++)
		{
			filterzeitraume_glob.get(i).writeFilterSettings();
		}
	}

	public void genAllFilterzeitraume(Metriktabellen mettabellen)
	{
		// hier wird aus den Metriktabellen die Filterzeiträume berechnet
		// d.h. ich muss mich durch die Metriktabellen wühlen und hierraus
		// filterlisten/filterzeiträume bauen
		// so ein filterzeitraum beinhaltet im prinzip die min-maxwerte für ein
		// Attribut
		// die Filterzeiträume kann man aus den metriktabellen generieren, da
		// hier sämtliche Informationen drin sind
		mettabellen.getallFilterzeitraume(filterzeitraume_glob);
	}

	public void add(Filterzeitraum filtz)
	{
		filterzeitraume_glob.add(filtz);
	}

	public void modifyRandom()
	{
		// hier werden randommässig die schranken modifiziert
		// gehe durch alle filterzeiträume und modifiziere
		int anz = filterzeitraume_glob.size();
		for (int i = 0; i < anz; i++)
		{
			// filterzeit beinhaltet ein einzelnes verzeichniss
			Filterzeitraum filtzeit = filterzeitraume_glob.get(i);
			// hier werden für jeden Abschnitt die Schranken zufällig geändert
			filtzeit.modifyAllAttribRandom();
		}
	}
	public void modifyPersonKorrelation(CorelSetting corelsetting)
	{
		// hier werden nach der PersonKorrelation die schranken modifiziert
		// gehe durch alle filterzeiträume und modifiziere
		int anz = filterzeitraume_glob.size();
		//anz-1 da letzte tabelle die profittabelle sind
		for (int i = 0; i < anz-1; i++)
		{
			// filterzeit beinhaltet ein einzelnes verzeichniss
			Filterzeitraum filtzeit = filterzeitraume_glob.get(i);
			// hier werden für jeden Abschnitt die Schranken zufällig geändert
			filtzeit.modifySchrankePersonCorel(i, corelsetting);
		}
	}
}
