package mathematics;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import Metriklibs.Corelresultelem;
import Metriklibs.Corelresultliste;
import Metriklibs.DatabankExportTable;
import Metriklibs.Metriktabellen;
import Metriklibs.Metrikzeile;
import hiflsklasse.Tracer;

public class Math3
{

		/*
		if (corealalgotype.equals("PearsonsCorrelation"))
		{
			PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
			double correlationCoefficient = pearsonsCorrelation.correlation(attributes, profits);
			return new double[]
			{ correlationCoefficient };
		} else if (corealalgotype.equals("KendallsCorrelation"))
		{
			KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();
			double correlationCoefficient = kendallsCorrelation.correlation(valueMatrix, profits);
			return new double[]
			{ correlationCoefficient };
		} else if (corealalgotype.equals("SpearmansCorrelation"))
		{
			SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
			double correlationCoefficient = spearmansCorrelation.correlation(valueMatrix, profits);
			return new double[]
			{ correlationCoefficient };
		} 	else
			Tracer.WriteTrace(10, "E: unknown Correltype <"+corealalgotype+">");
		return null;
		
	}
	*/
	public static Corelresultliste CalcCorelOnlyOneTable(Metriktabellen met, int index, String endtestattribname,
			String corelalgotype)
	{
		// endtestatrribname="Net profit (OOS)"
		DatabankExportTable endtable = met.holeEndtestMetriktable();
		// die profits holen
		double[] profits = endtable.calcAttribvektor(endtestattribname);
		double[][] attributes = met.holeAttributes(index);
		int anzatribs=met.holeAnzAttributes(index);
		
		DatabankExportTable aktuelleMetriktabelle=met.holeNummerI(index);
		
		//holt sich eine metrikzeile weil wir die attributsnamen brauchen
		//holt sich die erste Metrikzeile weil wir ja die attributnamen brauchen.
		Metrikzeile mezeil=aktuelleMetriktabelle.holeMetrikzeilePosI(1);
		
		//hier werden die ergebnisse gespeichert
		Corelresultliste ci = new Corelresultliste();
		
		for(int a=0; a<anzatribs; a++)
		{
			String attribname = mezeil.holeEntry(a).getAttributName();
			//PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
			//double correlationCoefficient = pearsonsCorrelation.correlation(attributes[a], profits);
			
			double correlationCoefficient=calcCoefficient(attributes[a],profits,corelalgotype);
			
			   if (Double.isNaN(correlationCoefficient))
				   correlationCoefficient=0;
			
			//das result speichern
			Corelresultelem celem = new Corelresultelem();
			celem.setAttribname(attribname);
			celem.setVal(correlationCoefficient);
			ci.addElem(celem);
		}
		return ci;
		
	}
	 private static double calcCoefficient(double[] atribvalues, double[] profitvalues ,String algotype)
	{
		 
		 if(atribvalues.length!=profitvalues.length)
		 {
			 
			 Tracer.WriteTrace(20, "E:atribdatabase anz elements="+atribvalues.length+" != profitvalues anz elements="+profitvalues.length);
			 Tracer.WriteTrace(10, "E:The Endtest database length != the atriblength, possible you don´t selected all elements if you exported Databank contents in 'DatabankExport.csv'");
		 }
			if (algotype.equals("PearsonsCorrelation"))
			{
				PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
				double correlationCoefficient = pearsonsCorrelation.correlation(atribvalues, profitvalues);
				return correlationCoefficient;
				
			} else if (algotype.equals("KendallsCorrelation"))
			{
				KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();
				double correlationCoefficient = kendallsCorrelation.correlation(atribvalues, profitvalues);
				return correlationCoefficient;
				
			} else if (algotype.equals("SpearmansCorrelation"))
			{
				SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
				double correlationCoefficient = spearmansCorrelation.correlation(atribvalues, profitvalues);
				return correlationCoefficient;
				
			} 	else
				Tracer.WriteTrace(10, "E: unknown Correltype <"+algotype+">");
			
		return 0;
	}
}
