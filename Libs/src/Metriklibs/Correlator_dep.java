package Metriklibs;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import gui.Viewer;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;


public class Correlator_dep
{
	// hier werden die Ergebnisse festgehalten
	static ArrayList<Corelresultliste> reslist_glob = new ArrayList<Corelresultliste>();
	
	public static double getPearsonCorrelation(double[] scores1,
			double[] scores2)
	{
		double result = 0;
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = scores1[0];
		double mean_y = scores2[0];
		for (int i = 2; i < scores1.length + 1; i += 1)
		{
			double sweep = Double.valueOf(i - 1) / i;
			double delta_x = scores1[i - 1] - mean_x;
			double delta_y = scores2[i - 1] - mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x / scores1.length);
		double pop_sd_y = (double) Math.sqrt(sum_sq_y / scores1.length);
		double cov_x_y = sum_coproduct / scores1.length;
		result = cov_x_y / (pop_sd_x * pop_sd_y);
		return result;
	}

	public static double getPearsonCorrelation2(double[] array1,double[] array2)
	{
		//person Correlation nur mit absolutwerten
		double result = 0;
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = array1[0];
		double mean_y = array2[0];
		for(int i=2;i<array1.length+1;i+=1){
			double sweep =Double.valueOf(i-1)/i;
			double delta_x = array1[i-1]-mean_x;
			double delta_y = array2[i-1]-mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x/array1.length);
		double pop_sd_y = (double) Math.sqrt(sum_sq_y/array1.length);
		double cov_x_y = sum_coproduct / array1.length;
		result = cov_x_y / (pop_sd_x*pop_sd_y);
		return Math.abs(result);
	}

	
	
	public static float holeAttribCorel(int filterindex,String attrib)
	{
		//holt für ein Attribut den Korrelationswert
		Corelresultliste corl=reslist_glob.get(filterindex);																	
		Corelresultelem coreelem=corl.getElem(attrib);

		if(coreelem==null)
		{
			//Tracer.WriteTrace(20, "I:attrib <"+attrib+"> nicht bekannt");
			return 0;
		}
		String attribname=coreelem.getAttribname();
		
		if(attribname.equals(attrib)==false)
			Tracer.WriteTrace(10, "internal error, müssen gleich sein");
		return((float)coreelem.getVal());
	}
	public static void CalcPersonCorrelation(Metriktabellen met)
	{

		// für die Metriktabellen wird die korrelation berechnet
		// für jedes Attribut wird gemessen wie stark es zum Nettoprofit
		// korreliert
		// und das Ergebniss wird in einer Arrayliste festgehalten

		// hier werden die Ergebnisse festgehalten
		
	
		reslist_glob.clear();
		DatabankExportTable endtable = met.holeEndtestMetriktable();
		//genau hier ist das problem, der Endvektor muss so sortiert sein das an gleicher position die gleichen Attribute stehen
		//wenn der endvektor anders sortiert ist dann geht es nicht.
		//hier wird zwar Netprofit(oos) aus der endtable gehole. Es wird aber davon ausgegangen, das die metriknamen in den datenbank zu der der DAtenbank
		//des endvektors gleich sortiert sind. Dies ist aber nicht so.
		double[] endvektor = endtable.calcAttribvektor("Net profit (OOS)");

		// anzahl der tabellen bestimmen
		int anztab = met.getAnzMetriktabellen();

		// es werden die i-filter-tabellen betrachtet, jede tabelle hat eigene
		// attribute für
		// die die korrelation berechnet wird
		for (int i = 0; i < anztab - 1; i++)
		{
			Corelresultliste ci = new Corelresultliste();

			// hole tabelle i
			DatabankExportTable met1 = met.holeNummerI(i);

			int attribanz = met1.getAttribAnz();
			for (int j = 0; j < attribanz; j++)
			{
				//holt den Attributvektor für eine Metrik
				double[] vektor1 = met1.calcAttribvektor(j);

				if (vektor1 == null)
					continue;

				String attribname = met1.holeAttribname(j);
				// korreliere und speichere das Ergebniss in der resultliste
				double perval = getPearsonCorrelation(vektor1, endvektor);
				Corelresultelem celem = new Corelresultelem();
				celem.setAttribname(attribname);
				celem.setVal(perval);
				ci.addElem(celem);
			}
			// addiere die corelliste für den iten-filter
			reslist_glob.add(ci);
		}
	
	}

	static public void zeigeTabelle(String fnam)
	{
		
		//hier werden die Ergebnisse ausgegeben
		//bzw. gespeichert
		//String fnam=Metrikglobalconf.getFilterpath()+"\\correlation.txt";
		DecimalFormat df = new DecimalFormat("####.####");
		File fnamf= new File(fnam);
		if(fnamf.exists())
			fnamf.delete();
		
		Inf inf= new Inf();
		inf.setFilename(fnam);
		
		int anzl=reslist_glob.size();
		
		inf.writezeile("attribname#correlation*******");
		for(int i=0; i<anzl; i++)
		{
			Corelresultliste cr=reslist_glob.get(i);
			cr.SortInternal();
			
			int anz=cr.getSize();
			for(int j=0; j<anz; j++)
			{
				Corelresultelem ce=cr.getElem(j);
				String formatedzahl=df.format(ce.getVal()).replace(",", ".");
				inf.writezeile(ce.getAttribname()+"#"+formatedzahl);
				
			}
			inf.writezeile("..............#................");
		}
		Viewer v= new Viewer();
		v.viewTableExtFile(Display.getDefault(), fnam);
	
	}
}
