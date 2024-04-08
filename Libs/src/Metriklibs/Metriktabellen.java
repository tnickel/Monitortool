package Metriklibs;

import java.util.ArrayList;
import java.util.Comparator;

import hiflsklasse.Tracer;

public class Metriktabellen implements Comparator<Metrikzeile> 
{
	// hier sind alle Metriktabellen drin
	// Eine Metriktabelle besteht aus Metrikzeilen
	// Metrikzeilen: Dies wird aus den Filterfiles von Platte generiert
	// Filterzeiträume:
	private ArrayList<DatabankExportTable> metriktabellen = new ArrayList<DatabankExportTable>();

	public int getAnzMetriktabellen()
	{
		return metriktabellen.size();
	}
	public int getAnzFilter()
	{
		return metriktabellen.size()-1;
	}

	public void readAllTabellen(StrategienSelector msel,String rpath)
	{
		// liest alle Metriktabellen ein
		metriktabellen = new ArrayList<DatabankExportTable>();
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
			DatabankExportTable mettabelle = new DatabankExportTable();

			// Die Tabellen einlesen
			mettabelle.readExportedTable(msel,dirnam);
			
			// die eingelesene tabelle in der gesammtliste speichern
			metriktabellen.add(mettabelle);
			//System.out.println("Read table i="+i+" <"+dirnam+"> ready");
		}
	}

	
	
	
	public DatabankExportTable holeNummerI(int j)
	{
		//holt die Metriktabelle mit der nummer i aus der internen datenstruktur
		int anz = metriktabellen.size();
		for (int i = 0; i < anz; i++)
		{
			DatabankExportTable met = metriktabellen.get(i);
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_"))
				return (met);
		}
		//Tracer.WriteTrace(20, "E:internal error metriktrablle mit nummer<"+j+"> nicht gefunden");
		return null;
	}

	public void getallFilterfiles(ArrayList<Filterfile> filterfiles)
	{
		// fuer alle metriktabellen werden die filterzeitraume generiert und
		// zurückgeliefert

		int anz = getAnzMetriktabellen();
		for (int i = 0; i < anz; i++)
		{
			Filterfile filt = metriktabellen.get(i).calcFilterzeitraum();
			filterfiles.add(filt);
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

	public ArrayList<Stratelem> buildStratliste(StrategienSelector stratsel)
	{
		//die Strategielist wird aus den Metriken von verz _0_ erzeugt
		return (metriktabellen.get(0).buildStratliste(stratsel));
	}
	public ArrayList<Stratelem> buildAllStratliste()
	{
		//die Strategielist wird aus den Metriken von verz _0_ erzeugt
		return (metriktabellen.get(0).buildAllStratliste());
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
			DatabankExportTable met = metriktabellen.get(i);
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_") == true)
				return true;
		}
		return false;
	}

	public double[][] holeAttributes(int index)
	{
		//die Tabelle wird so aufgebaut das in jeder zeile die werte für ein bestimmtes attribut stehen
		//zeile0=attriutsvalues für das attribut 0
		//zeile1=attributesvalues für das attribut 1
		//die Tabelle muss erst mal sauber sein, sonst können wir nicht weitermahen !!
		
		int anz = metriktabellen.size();
		//wir betrachten hier nur die erste Tabelle
		DatabankExportTable mettab = metriktabellen.get(index);
		int anzzeilen=mettab.getAnz();
		int anzattribs=mettab.getAttribAnz();
		
		double[][] attribsvalue=new double[anzattribs][anzzeilen];
		
		//und jetzt wird die Tablle gefüllt
		for(int zeili=0; zeili<anzzeilen; zeili++)
		{
			//jede zeile i der matrix hat die werte für ein bestimmtes attribut
			//wir holen uns eine metrikzeile, und gehen durch die attribute
			Metrikzeile metrikzeile=mettab.holeMetrikzeilePosI(zeili);
			for(int j=0; j<anzattribs; j++)
			{
				//eine metrikzeile hat n attribute
				Metrikentry me=metrikzeile.holeEntry(j);
				
				//falls da ein floatwert ist
				if(me.getAttributflag()==2)
				{
					attribsvalue[j][zeili]=Float.valueOf(me.getValue());
				}
				else
					attribsvalue[j][zeili]=0;
			}
		}
		return attribsvalue;
	}
	
	public int holeAnzAttributes(int metriktabelleindex)
	{
		//gibt die anzahl der attribute zurück die in der metriktabelle sind
		DatabankExportTable mettab = metriktabellen.get(metriktabelleindex);
		return(mettab.getAttribAnz());
		
	}
	
	
	public DatabankExportTable holeEndtestMetriktable()
	{
		// erst mal die endtabelle suchen und zurückliefern
		int anz = metriktabellen.size();
		for (int i = 0; i < anz; i++)
		{
			String fname = null;
			DatabankExportTable mettab = metriktabellen.get(i);
			fname = mettab.holeFilename();
			if (fname.contains("_99_") == true)
				return mettab;
		}
		Tracer.WriteTrace(10, "E: keine Endtabelle gefunden _99_");
		return null;
	}

	public float holeEndtestValue(String stratname,EndtestFitnessfilter endfitfilter)
	{
		//restanzahlstratgien=anzahl der strategien die nach der filterung noch da sind.
		// holt für den Stratnamen den endtest aus den tabellen

		// hole die endtabelle
		DatabankExportTable endtabelle = holeEndtestMetriktable();
		// hole die passende endzeile
		Metrikzeile mz = endtabelle.holeMetrikzeile(stratname);

		float val=0,val2=0;
		if(endfitfilter.isNettoflag())
		{
			// jetzt den floatwert für oos
			val = endtabelle.holeFloatwert(mz,"Net profit (OOS)");
		}
		else if(endfitfilter.isNettoperstrategyflag())
		{
		
			val = endtabelle.holeFloatwert(mz,"Net profit (OOS)");
			
		}
		else if(endfitfilter.isNettostabilflag())
		{
			val = endtabelle.holeFloatwert(mz,"Net profit (OOS)");
			val2= endtabelle.holeFloatwert(mz,"Stability (OOS)");
			val=val*val2;
		}
		else if(endfitfilter.isNettorobustflag())
		{
			val = endtabelle.holeFloatwert(mz,"Net profit (RT)");
		}
		else if(endfitfilter.isNettorobuststabilflag())
		{
			val = endtabelle.holeFloatwert(mz,"Net profit (RT)");
			val2= endtabelle.holeFloatwert(mz,"Stability (OOS)");
			val=val*val2;
		}
		else if(endfitfilter.isStabilflag())
		{
			val= endtabelle.holeFloatwert(mz,"Stability (OOS)");
		}
		else
			Tracer.WriteTrace(10, "E: internal error, endfitfilter not set");
		return val;
	}

	public String holeEndtestFilnamen()
	{
		//holt den Filenamen der Endtesttabelle aus der Datenstruktur
		DatabankExportTable endtabelle = holeEndtestMetriktable();
		String fnam = endtabelle.holeFilename().replace(".csv", ".result");
		return fnam;
	}
	 @Override
	    public int compare(Metrikzeile p1, Metrikzeile p2) 
	 {
	        return 0;
	    }
}
