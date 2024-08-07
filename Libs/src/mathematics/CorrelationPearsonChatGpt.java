package mathematics;

import java.util.ArrayList;
import java.util.List;

import Metriklibs.Corelresultelem;
import Metriklibs.Corresultliste;
import Metriklibs.Metriktabelle;
import Metriklibs.Metriktabellen;

public class CorrelationPearsonChatGpt
{
	/* _________________________________________ pearson correlation from chatgpt___________________________*/
	private static double[] pearsonCorrelation(double[] x, double[] y)
	{
		if (x.length != y.length || x.length == 0)
		{
			throw new IllegalArgumentException("Input arrays must be of equal length and non-empty.");
		}
		
		double correlation = pearsonCorrelationCoefficient(x, y);
		return new double[]
		{ correlation };
	}
	
	private static double pearsonCorrelationCoefficient(double[] x, double[] y)
	{
		int n = x.length;
		double sumX = 0.0, sumY = 0.0, sumXY = 0.0, sumXSquare = 0.0, sumYSquare = 0.0;
		
		for (int i = 0; i < n; i++)
		{
			sumX += x[i];
			sumY += y[i];
			sumXY += x[i] * y[i];
			sumXSquare += x[i] * x[i];
			sumYSquare += y[i] * y[i];
		}
		
		double numerator = (n * sumXY) - (sumX * sumY);
		double denominator = Math.sqrt((n * sumXSquare - sumX * sumX) * (n * sumYSquare - sumY * sumY));
		
		if (denominator == 0)
		{
			return 0; // Avoid division by zero
		}
		
		return numerator / denominator;
	}
	
	
	
	public static Corresultliste CalcPersonCorelOnlyOneTable(Metriktabellen met, int index,String endtestattribname)
	{
		//endtestatrribname="Net profit (OOS)"
		Metriktabelle endtable = met.holeEndtestMetriktable();
		// die profits holen
		double[] profits = endtable.calcAttribvektor(endtestattribname);
		double[][] attributes = met.holeAttributes(index);
		
		
		Metriktabelle met1 = met.holeNummerI(index);
		Corresultliste ci = new Corresultliste();
		
		// Calculate Pearson correlation coefficients, in der correlationslist werde die 
		//Ergebnisse gespeichert, die liste enthält attribute und und den
		// berechneten correlationswert.
		List<Double> correlationsList = new ArrayList<>();
		for (int i = 0; i < attributes[0].length; i++)
		{
			double[] attributeValues = new double[attributes.length];
			for (int j = 0; j < attributes.length; j++)
			{
				attributeValues[j] = attributes[j][i];
			}
			double[] correlations = pearsonCorrelation(attributeValues, profits);
			correlationsList.add(correlations[0]);
		}
		
		// Output correlations
		for (int i = 0; i < correlationsList.size(); i++)
		{
			System.out.println("Attribute " + (i + 1) + " correlation with profit: " + correlationsList.get(i));
			String attribname = met1.holeAttribname(i);
			Corelresultelem celem = new Corelresultelem();
			celem.setAttribname(attribname);
			celem.setVal(correlationsList.get(i));
			ci.addElem(celem);
		}
		return ci;
		
		
	}
}
