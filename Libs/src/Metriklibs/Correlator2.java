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
	private static ArrayList<Corelresultliste> reslist_glob = new ArrayList<Corelresultliste>();
	
	public static void CalcCorrelation(Metriktabellen met, String endtestattribname, String correlalgotype)
	{
		Corelresultliste ci = null;
		
		// hier werden die correlatinswerte festgehalten
		reslist_glob.clear();
		
		// anzahl der tabellen bestimmen
		int anztab = met.getAnzMetriktabellen();
		
		// es werden die i-filter-tabellen betrachtet, jede tabelle hat eigene
		// attribute für
		// die die korrelation berechnet wird
		
		if (correlalgotype.equals("PearsonChatGpt"))
		{
			Tracer.WriteTrace(10, "Not implementet --> stop");
			for (int indexFiltertabelle = 0; indexFiltertabelle < anztab - 1; indexFiltertabelle++)
			{
				ci = CorrelationPearsonChatGpt.CalcPersonCorelOnlyOneTable(met, indexFiltertabelle, endtestattribname);
				reslist_glob.add(ci);
			}
		} else 
		{
			for (int indexFiltertabelle = 0; indexFiltertabelle < anztab - 1; indexFiltertabelle++)
			{
				ci = Math3.CalcCorelOnlyOneTable(met, indexFiltertabelle, endtestattribname, correlalgotype);
				
				
				reslist_glob.add(ci);
			}
		}
		
	}
	
	static public void zeigeTabelle(String fnam,String corname)
	{
		
		// hier werden die Ergebnisse ausgegeben
		// bzw. gespeichert
		// String fnam=Metrikglobalconf.getFilterpath()+"\\correlation.txt";
		
		DecimalFormat df = new DecimalFormat("####.####");
		File fnamf = new File(fnam);
		if (fnamf.exists())
			fnamf.delete();
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		
		int anzl = reslist_glob.size();
		MetStat mstat=new MetStat();
		
		inf.writezeile("attribname#correlation*******");
		inf.writezeile("Corelator=#"+corname);
		for (int i = 0; i < anzl; i++)
		{
			Corelresultliste cr = reslist_glob.get(i);
			cr.SortInternal();
			
			int anz = cr.getSize();
			for (int j = 0; j < anz; j++)
			{
				Corelresultelem ce = cr.getElem(j);
				String formatedzahl = df.format(ce.getVal()).replace(",", ".");
				mstat.addValue(ce.getVal());
				inf.writezeile(ce.getAttribname() + "#" + formatedzahl);
				
			}
			inf.writezeile("..............#................");
		}
		inf.writezeile(mstat.getResultstring1());
		inf.writezeile(mstat.getResultstring2());
		Viewer v = new Viewer();
		v.viewTableExtFile(Display.getDefault(), fnam);
		
	}
	
	public static float holeAttribCorel(int filterindex, String attrib)
	{
		// holt für ein Attribut den Korrelationswert
		Corelresultliste corl = reslist_glob.get(filterindex);
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
