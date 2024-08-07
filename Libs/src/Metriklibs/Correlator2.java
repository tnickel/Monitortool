package Metriklibs;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import data.MetStat;
import gui.Viewer;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import mathematics.CorrelationPearsonChatGpt;
import mathematics.Math3;

public class Correlator2
{
	// hier werden die Ergebnisse festgehalten, für jede Filtertabelle gibt es eine
	// correlationsliste
	// Z.B. Bei _1_dir.... _5_dir gibt es 5 listen
	// für jedes _n_dir wird eine eigene Liste angefertigt.
	private static ArrayList<Corresultliste> reslist_glob = new ArrayList<Corresultliste>();
	
	public static CoreWorkflowResult CalcCorrelation(Metriktabellen met, String endtestattribname,
			String correlalgotype)
	{
		// met: beinhalten die ganzen attribute
		// endtestattribname: dieses attribut soll gelernt werden.
		// corerelagotype: es gibt 3 verschiedene korrelationsareten.
		
		Corresultliste corListe = null;
		CoreWorkflowResult corWorkflow = new CoreWorkflowResult();
		
		// hier werden die correlatinswerte festgehalten
		reslist_glob.clear();
		
		// anzahl der tabellen bestimmen
		int anzMetriktabellen = met.getAnzMetriktabellen();
		
		// es werden die i-filter-tabellen betrachtet, jede tabelle hat eigene
		// attribute für
		// die die korrelation berechnet wird
		
		if (correlalgotype.equals("PearsonChatGpt"))
		{
			Tracer.WriteTrace(10, "Not implementet --> stop");
			for (int indexFiltertabelle = 0; indexFiltertabelle < anzMetriktabellen - 1; indexFiltertabelle++)
			{
				corListe = CorrelationPearsonChatGpt.CalcPersonCorelOnlyOneTable(met, indexFiltertabelle,
						endtestattribname);
				reslist_glob.add(corListe);
				corWorkflow.addListe(corListe, indexFiltertabelle);
			}
		} else
		{
			for (int indexFiltertabelle = 0; indexFiltertabelle < anzMetriktabellen - 1; indexFiltertabelle++)
			{
				// hier geht er durch die metriktabelle und berechnet für _n_dir tabelle die
				// korrelationen=corliste und speichert diese im corworkflow
				corListe = Math3.CalcCorelOnlyOneTable(met, indexFiltertabelle, endtestattribname, correlalgotype);
				reslist_glob.add(corListe);
				// die korrelationsliste für den index (dieser steht für _n_dir) wird hier
				// festgehalten.
				// corWorkflow beinhaltet alle korrelationswerte für einen workflow.
				corWorkflow.addListe(corListe, indexFiltertabelle);
				
			}
		}
		return (corWorkflow);
		
	}
	
	static public void schreibeTabelle(String fnam, String corname)
	{
		
		// hier werden die Ergebnisse ausgegeben
		// bzw. gespeichert
		// String fnam=Metrikglobalconf.getFilterpath()+"\\correlation.txt";
		// corname= correlatorname
		
		DecimalFormat df = new DecimalFormat("####.####");
		File fnamf = new File(fnam);
		if (fnamf.exists())
			fnamf.delete();
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		
		int anzl = reslist_glob.size();
		MetStat mstat = new MetStat();
		
		inf.writezeile("attribname#correlation*******");
		inf.writezeile("Corelator=#" + corname);
		inf.writezeile("file=#" + fnam);
		
		for (int i = 0; i < anzl; i++)
		{
			Corresultliste cr = reslist_glob.get(i);
			cr.SortInternal();
			
			int anz = cr.getSize();
			for (int j = 0; j < anz; j++)
			{
				Corelresultelem ce = cr.getElem(j);
				String formatedzahl = df.format(ce.getVal()).replace(",", ".");
				mstat.addValue(ce.getVal());
				inf.writezeile(ce.getAttribname() + "#" + formatedzahl);
				
				
			}
			inf.writezeile("^^^^"+i+"-dir ^^^^..............#................");
		}
		inf.writezeile(mstat.getResultstring1());
		inf.writezeile(mstat.getResultstring2());
		// Viewer v = new Viewer();
		// v.viewTableExtFile(Display.getDefault(), fnam);
		
	}
	
	static public void zeigeTabelle(String fnam, String corname)
	{
		Viewer v = new Viewer();
		v.viewTableExtFile(Display.getDefault(), fnam);
	}
	
	public static float holeAttribCorel(int filterindex, String attrib)
	{
		if (filterindex - 1 > reslist_glob.size())
			Tracer.WriteTrace(10,
					"E: filterindex<" + filterindex + "> bigger as  anz elems <" + reslist_glob.size() + ">");
		
		// holt für ein Attribut den Korrelationswert
		
		if (reslist_glob == null)
			Tracer.WriteTrace(10, "E:reslist_glob not initialisiert");
		
		if (filterindex < 0 || filterindex >= reslist_glob.size())
		{
			Tracer.WriteTrace(10,
					"E: filterindex 1712<" + filterindex + "> out of bounds, size <" + reslist_glob.size() + ">");
			return 0; // oder einen anderen Standardwert, der passend ist
		}
		Corresultliste corl = reslist_glob.get(filterindex);
		Corelresultelem coreelem = corl.getElem(attrib);
		
		if (coreelem == null)
		{
			// Tracer.WriteTrace(20, "I:attrib <"+attrib+"> nicht bekannt");
			return 0;
		}
		String attribname = coreelem.getAttribname();
		
		if (attribname.equals(attrib) == false)
			Tracer.WriteTrace(10, "internal error, müssen gleich sein");
		return ((float) coreelem.getVal());
	}
	
}
