package Metriklibs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class Statliste implements Comparator<CoreResultStatistikElem>
{
	// hier wird die statistik für die korrelationen festgehalten.
	// wir halten die daten ursprünglich in der Map, diese Liste brauchen wir weil
	// wir die daten
	// sortiert anzeigen wollen
	private List<CoreResultStatistikElem> statliste;
	private String sortcriteria_glob = "sumcorrealation";
	
	public Statliste()
	{
		// Initialisieren der statliste
		statliste = new ArrayList<>();
	}
	
	public void setSortCriteria(String sortcriteria)
	{
		sortcriteria_glob = sortcriteria;
		
	}
	
	public void addElem(CoreResultStatistikElem e)
	{
		statliste.add(e);
	}
	
	public void WriteToFile(String file)
	{
		File file_f = new File(file);
		if (file_f.exists())
			if (file_f.delete() == false)
				Tracer.WriteTrace(10, "E:can´t delete File<" + file + "> 127");
			
		int anz = statliste.size();
		Inf inf = new Inf();
		inf.setFilename(file);
		
		// Hier statliste sortieren
		Collections.sort(statliste, this);
		inf.writezeile("attributename#anzgood#anzbad#corsum#Stabil  #Explain***");
		for (CoreResultStatistikElem elem : statliste)
		{
			inf.writezeile(elem.getAttribname() + "#" + elem.getAnzgut() + " of " + elem.getAnzvorkommen() + "#"
					+ elem.getAnzbad() + " of " + elem.getAnzvorkommen() + "#" + elem.getCorsumme() + "#"
					+ String.format("%.3f", elem.getStabil()).replace(",", ".") + "#" + elem.getExplainString());
			
		}
		
		inf.close();
	}
	
	@Override
	public int compare(CoreResultStatistikElem p1, CoreResultStatistikElem p2)
	{
		double pval1, pval2;
		
		if (sortcriteria_glob.equals("stabil"))
		{
			pval1 = p1.getStabil();
			pval2 = p2.getStabil();
			if (pval1 > pval2)
				return 1;
			else if (pval1 < pval2)
				return -1;
			else
				return 0;
		} else
		{
			pval1 = p1.getCorsumme();
			pval2 = p2.getCorsumme();
			if (pval1 > pval2)
				return -1;
			else if (pval1 < pval2)
				return 1;
			else
				return 0;
		}
	}
	
}
