package Metriklibs;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import FileTools.Counter;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class Metriktabellen implements Comparator<Metrikzeile>
{
	// hier sind alle Metriktabellen drin
	// Eine Metriktabelle besteht aus Metrikzeilen
	// Metrikzeilen: Dies wird aus den Filterfiles von Platte generiert
	// Filterzeiträume:
	private ArrayList<Metriktabelle> metriktabellen_glob = new ArrayList<Metriktabelle>();
	
	public int getAnzMetriktabellen()
	{
		return metriktabellen_glob.size();
	}
	
	public int getAnzFilter()
	{
		return metriktabellen_glob.size() - 1;
	}
	
	public void readAllTabellen(StrategieMengen msel, String rpath)
	{
		// liest alle Metriktabellen ein
		metriktabellen_glob = new ArrayList<Metriktabelle>();
		
		if (rpath == null)
			Tracer.WriteTrace(10, "E: please set configpath in File/config");
		if (rpath.length() < 3)
			Tracer.WriteTrace(10, "E: please set configpath [path to short] in File/config");
		
		// holt die liste der Verzeichnisse
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(rpath, 0);
		
		int zeilcount=0;
		//alle Tabellen einlesen
		int anz = fdirs.holeFileAnz();
		if(anz==0)
			Tracer.WriteTrace(10, "No directorys in <"+rpath+"> --> stop");
		for (int i = 0; i < anz; i++)
		{
			String dirnam = rpath + "\\" + fdirs.holeFileSystemName();
			Metriktabelle mettabelle = new Metriktabelle();
			
			// Die Tabellen einlesen
			if(mettabelle.readExportedTable(msel, dirnam)==false)
				continue;
			
			//alle tabellen müssen die gleiche zeilenanzahl haben, wir haben ja die gleichen Strategien
			if(i==0)
				zeilcount=mettabelle.getAnz();
			else
				if (mettabelle.getAnz()!=zeilcount)
				Tracer.WriteTrace(10, "E:Databank inconsistent linenumbers: table name<"+dirnam+"> #lines <"+mettabelle.getAnz()+"> != first table lines<"+zeilcount+">");
			
			if(zeilcount==0)
				Tracer.WriteTrace(10, "E: Metriktable has 0 entrys path<"+dirnam+">");
			
			// die eingelesene tabelle in der gesammtliste speichern
			metriktabellen_glob.add(mettabelle);
			Tracer.WriteTrace(50,"Read table i="+i+" <"+dirnam+"> ready");
		}
	}
	
	public void exportAllAttributesForWeka(String exportfile, int maxstrategies,  String workdir,boolean usebadendtest,String endtestattibname)
	{
		// wir brauchen hier tabelle 0 und tabelle 99. Wir brauchen hier alle werte aus
		// Tabelle 0 und den NetProfit OOS aus der Tabelle 99
		// wir schreiben die Ergebnisse in ein File, dieses File legen wir im
		// Verzeichniss 99 ab.
		// maxstrategies= anz maximaler strategien in dem file
		// if makebinary==1 dann converiere weka_endtest to 1 falls wert >0, ansonsten
		// =0
		// rpath=
		//wir dürfen max 100 databankExport.csv haben
		//goodattribs: Wenn die attribliste !=null dann dürfen nur attribute exportiert werden die in der Attribliste sind.
		//usebadentest: falls das flag gesetzt wird, wird der endtest kaputt gemacht
		//Die attribliste beinhaltet die teilmenge der Attribute die mit höchster korrelation und vorkommen aus der Megaliste über alle workflows
		//subset=es wird nur ein subset benötigt. Es werden also nicht alle _0_dir.... _n_dir ausgewertet sondern nur ein teil davon
		Metriktabelle[] databankExportTable = new Metriktabelle[100];
		
		String ostring = "";
		Inf inf = new Inf();
		String outfile = workdir + "\\exported_for_weka.csv";
		inf.setFilename(outfile);
		File fnamf = new File(outfile);
		if (fnamf.exists())
			if (fnamf.delete() == false)
				Tracer.WriteTrace(10, "E:can´t delete file <" + outfile + ">--> stop3");
			
		// aus diesen Tabellen holen wir die atrribute und values.
		int anzTabellen = metriktabellen_glob.size();
		
		// Wir bauen die tabelle auf und nehmen die letzte noch hinzu
		for(int i=1; i<=anzTabellen-1; i++)
		{
			databankExportTable[i]=metriktabellen_glob.get(i-1);
		}
		databankExportTable[99]=metriktabellen_glob.get(anzTabellen - 1);
		
		
		// in der ersten und letzten Tabelle müssen gleich viele Strategien drin sein.
		int anz1 = databankExportTable[1].getAnz();
		int anz2 = databankExportTable[99].getAnz();
		if (anz1 != anz2)
			Tracer.WriteTrace(20, "E: error plausicheck table1 #Strategies<" + anz1
					+ "> should have same number as table99<" + anz2 + "> workdir<" + workdir + ">-->STOP");
		
		
		
		int anzZeilen = databankExportTable[1].getAnz();
		for (int i = 0; i < anzZeilen; i++)
		{
			
			// hole alle zeilen aus tabelle1
			Metrikzeile mez1 = databankExportTable[1].holeMetrikzeilePosI(i);
			String stratname = mez1.holeStratName();
		
			
			// aus dieser metrikzeile brauchen wir nur NetProfit OOS,dazu holen wir uns erst
			// mal die ganze Zeile.
			Metrikzeile mez99 = databankExportTable[99].getMetrikzeile(stratname);
			
			// jetzt werden die daten für das schreiben aufbereitet, die kopfzeile muss nur
			// einmal geschrieben werden.
			if (i == 0)
			{// die kopfzeile wird nur einmal geschrieben
				ostring="Strategy_Name,";
				for(int j=1; j<anzTabellen; j++)
				{
					
					Metrikzeile mezi = databankExportTable[j].holeMetrikzeilePosI(i);
										
					//hier wird die j-te tabelle ausgelesen, am anfang ist der name string. für den header brauchen wir den namestring nicht.
					ostring=ostring+ mezi.getAllAttributsNamesAsString(",",j,false);
				}	
				ostring=ostring+"Weka Endtest";
				inf.writezeile(ostring);
			}
			ostring="";
			String firstname="";
			for(int j=1; j<anzTabellen; j++)
			{
				if(j==1)
					firstname=databankExportTable[j].holeMetrikzeilePosI(i).getStrategiename();
				
				
				//wir müssen hier noch prüfen ob der Namestring gleich ist, wir wollen ja nicht alles vermischen.
				//in den internen datenstrukturen sollten die tabellen gleich sortiert sein
				
				String nextname=databankExportTable[j].holeMetrikzeilePosI(i).getStrategiename();
				
				//Tracer.WriteTrace(20, "Bearbeite zeile<"+nextname+">");
				
				if(firstname.equals(nextname)==false)
					Tracer.WriteTrace(10, "E: datatables are not sorted names not equal <"+firstname+"> != <"+nextname+">");
				
				ostring = ostring+databankExportTable[j].holeMetrikzeilePosI(i).getAllAttributsValuesAsString(",",false);
						
			}
		
			
			
			if(usebadendtest==false)
			  ostring=ostring+mez99.getSpecificAttributValueAsString(endtestattibname);
			else
				ostring=ostring+"0.5";
			
			inf.writezeile(firstname+","+ostring);
			
			if (maxstrategies != 0)
				if (i >= maxstrategies - 1)
					break;
		}
		inf.close();
	}
	
	public Metriktabelle holeNummerI(int j)
	{
		// holt die Metriktabelle mit der nummer i aus der internen datenstruktur
		int anz = metriktabellen_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Metriktabelle met = metriktabellen_glob.get(i);
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_"))
				return (met);
		}
		// Tracer.WriteTrace(20, "E:internal error metriktrablle mit nummer<"+j+"> nicht
		// gefunden");
		return null;
	}
	
	public void getallFilterfiles(ArrayList<Filterfile> filterfiles)
	{
		// fuer alle metriktabellen werden die filterzeitraume generiert und
		// zurückgeliefert
		
		int anz = getAnzMetriktabellen();
		for (int i = 0; i < anz; i++)
		{
			Filterfile filt = metriktabellen_glob.get(i).calcFilterzeitraum();
			filterfiles.add(filt);
			if (i % 1000 == 0)
				Tracer.WriteTrace(50, "get Filtzeitraum i=" + i);
		}
		return;
	}
	
	public String getFilename(int j)
	{
		// holt den Filenamen der Metriktabelle j
		return (metriktabellen_glob.get(j).holeFilename());
	}
	
	public ArrayList<Stratelem> buildStratliste(StrategieMengen stratsel)
	{
		// die Strategielist wird aus den Metriken von verz _0_ erzeugt
		return (metriktabellen_glob.get(0).buildStratliste(stratsel));
	}
	
	public ArrayList<Stratelem> buildAllStratliste()
	{
		// die Strategielist wird aus den Metriken von verz _0_ erzeugt
		return (metriktabellen_glob.get(0).buildAllStratliste());
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
		int anztesttabellen = metriktabellen_glob.size();
		for (int i = 0; i < anztesttabellen; i++)
		{
			Metriktabelle met = metriktabellen_glob.get(i);
			String fname = met.holeFilename();
			if (fname.contains("_" + j + "_") == true)
				return true;
		}
		return false;
	}
	
	public double[][] holeAttributes(int index)
	{
		// die Tabelle wird so aufgebaut das in jeder zeile die werte für ein bestimmtes
		// attribut stehen
		// zeile0=attriutsvalues für das attribut 0
		// zeile1=attributesvalues für das attribut 1
		// die Tabelle muss erst mal sauber sein, sonst können wir nicht weitermahen !!
		
		int anz = metriktabellen_glob.size();
		// wir betrachten hier nur die erste Tabelle
		Metriktabelle mettab = metriktabellen_glob.get(index);
		int anzzeilen = mettab.getAnz();
		int anzattribs = mettab.getAttribAnz();
		
		double[][] attribsvalue = new double[anzattribs][anzzeilen];
		
		// und jetzt wird die Tablle gefüllt
		for (int zeili = 0; zeili < anzzeilen; zeili++)
		{
			// jede zeile i der matrix hat die werte für ein bestimmtes attribut
			// wir holen uns eine metrikzeile, und gehen durch die attribute
			Metrikzeile metrikzeile = mettab.holeMetrikzeilePosI(zeili);
			for (int j = 0; j < anzattribs; j++)
			{
				// eine metrikzeile hat n attribute
				Metrikentry me = metrikzeile.holeEntry(j);
				
				// falls da ein floatwert ist
				if (me.getAttributflag() == 2)
				{
					attribsvalue[j][zeili] = Float.valueOf(me.getValue());
				} else
					attribsvalue[j][zeili] = 0;
			}
		}
		return attribsvalue;
	}
	
	public int holeAnzAttributes(int metriktabelleindex)
	{
		// gibt die anzahl der attribute zurück die in der metriktabelle sind
		Metriktabelle mettab = metriktabellen_glob.get(metriktabelleindex);
		return (mettab.getAttribAnz());
		
	}
	
	public Metriktabelle holeEndtestMetriktable()
	{
		// erst mal die endtabelle suchen und zurückliefern
		int anz = metriktabellen_glob.size();
		for (int i = 0; i < anz; i++)
		{
			String fname = null;
			Metriktabelle mettab = metriktabellen_glob.get(i);
			fname = mettab.holeFilename();
			if (fname.contains("_99_") == true)
				return mettab;
		}
		Tracer.WriteTrace(10, "E: keine Endtabelle gefunden _99_");
		return null;
	}
	
	public float holeEndtestValue(String stratname, EndtestFitnessfilter endfitfilter)
	{
		// restanzahlstratgien=anzahl der strategien die nach der filterung noch da
		// sind.
		// holt für den Stratnamen den endtest aus den tabellen
		
		// hole die endtabelle
		Metriktabelle endtabelle = holeEndtestMetriktable();
		// hole die passende endzeile
		Metrikzeile mz = endtabelle.getMetrikzeile(stratname);
		
		float val = 0, val2 = 0;
		if (endfitfilter.isNettoflag())
		{
			// jetzt den floatwert für oos
			val = endtabelle.holeFloatwert(mz, "Net profit (OOS)");
		} else if (endfitfilter.isNettoperstrategyflag())
		{
			
			val = endtabelle.holeFloatwert(mz, "Net profit (OOS)");
			
		} else if (endfitfilter.isNettostabilflag())
		{
			val = endtabelle.holeFloatwert(mz, "Net profit (OOS)");
			val2 = endtabelle.holeFloatwert(mz, "Stability (OOS)");
			val = val * val2;
		} else if (endfitfilter.isNettorobustflag())
		{
			val = endtabelle.holeFloatwert(mz, "Net profit (RT)");
		} else if (endfitfilter.isNettorobuststabilflag())
		{
			val = endtabelle.holeFloatwert(mz, "Net profit (RT)");
			val2 = endtabelle.holeFloatwert(mz, "Stability (OOS)");
			val = val * val2;
		} else if (endfitfilter.isStabilflag())
		{
			val = endtabelle.holeFloatwert(mz, "Stability (OOS)");
		} else
			Tracer.WriteTrace(10, "E: internal error, endfitfilter not set");
		return val;
	}
	
	public String holeEndtestFilnamen()
	{
		// holt den Filenamen der Endtesttabelle aus der Datenstruktur
		Metriktabelle endtabelle = holeEndtestMetriktable();
		String fnam = endtabelle.holeFilename().replace(".csv", ".result");
		return fnam;
	}
	
	@Override
	public int compare(Metrikzeile p1, Metrikzeile p2)
	{
		
		Tracer.WriteTrace(10, "I: not implemented");
		return 0;
	}
}
