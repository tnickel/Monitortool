package mathematics;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import Metriklibs.Corelresultelem;
import Metriklibs.Corresultliste;
import Metriklibs.Metriktabelle;
import Metriklibs.Metriktabellen;
import Metriklibs.Metrikzeile;
import hiflsklasse.Tracer;

public class Math3
{

	
	public static Corresultliste CalcCorelOnlyOneTable(Metriktabellen met, int indexfiltertabelle, String endtestattribname,
			String corelalgotype)
	{
		//met=Hier sind die ganzen Metriktabellen drin.
		//indexfiltertablelle=index der filtertabelle
		//endtestattribname= dies ist der Name des endtestattributes was wir korrelieren wollen z.b. netprofit OOS
		
		
		// endtestatrribname="Net profit (OOS)"
		Metriktabelle endtable = met.holeEndtestMetriktable();
		// die profits holen
		//1) wir holen uns mal den Attributvektor
		double[] profits = endtable.calcAttribvektor(endtestattribname);

		//2) Wir haben hier eine Attributmatrix. Jetzt Zeile stellt eine Strategie mit Attributen da.
		//jede spalte hiervon beinhaltet die attribute dieser Strategie
		double[][] attributes = met.holeAttributes(indexfiltertabelle);
		
		//Die Strategien haben #Attribute
		int anzatribs=met.holeAnzAttributes(indexfiltertabelle);
		
		Metriktabelle aktuelleMetriktabelle=met.holeNummerI(indexfiltertabelle);
		
		//holt sich eine metrikzeile weil wir die attributsnamen brauchen
		//holt sich die erste Metrikzeile weil wir ja die attributnamen brauchen.
		Metrikzeile mezeil=aktuelleMetriktabelle.holeMetrikzeilePosI(1);
		
		//hier werden die ergebnisse gespeichert
		Corresultliste ci = new Corresultliste();
		
		//Hier geht es spaltenweise durch die tabelle, jetzt spalte repärsentiert ein attribut
		for(int a=0; a<anzatribs; a++)
		{
			String attribname = mezeil.holeEntry(a).getAttributName();
			//PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
			//double correlationCoefficient = pearsonsCorrelation.correlation(attributes[a], profits);
			
			//für jedes Attribut wird ein korrelationswert bestimmt
			double correlationCoefficient=calcCoefficient(attributes[a],profits,corelalgotype);
			
			   if (Double.isNaN(correlationCoefficient))
				   correlationCoefficient=0;
			
			//den korrelationswert für ein Attribut speichern
			Corelresultelem celem = new Corelresultelem();
			celem.setAttribname(attribname);
			celem.setVal(correlationCoefficient);
			ci.addElem(celem);
		}
		//wir liefern die liste mit den korrelationswerten für die Attribute zurück.
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
