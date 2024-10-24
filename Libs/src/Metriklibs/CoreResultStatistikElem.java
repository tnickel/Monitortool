package Metriklibs;

import Klassifikator.StabilityFromStringCalculator;

public class CoreResultStatistikElem extends Corelresultelem
{
	//Dieses Statistikelement speichert das folgende
	
	// attribname: Dies ist dar attributname
	// value: dieser wert wird gespeichert
	
	//anzvorkommen: Soviele Werte sind in der corsumme drin
	 int anzvorkommen=0;
	//anzgut: soviele werte (gute Werte mit cor>0.2) sind in der corsumme drin.
	 int anzgut=0;
	 int anzbad=0;
	//corsumme: ist die korrelationsumem, hier werden alle korrelationswerte aufsummiert
	 double corsumme=0;
	 String explainstring="";
	 double stabil=0;
	
	public void ExtendExplainString(double newval)
	{
		//wir brauchen später noch eine erkärung wie die corsumme zustande gekommen ist.
		 String formattedString = String.format("%.2f", newval).replace(",", ".");
	
		 explainstring=explainstring+formattedString+" ";
		 
		
	}
	
	public void calcStabil()
	{
		stabil=StabilityFromStringCalculator.calculateStabilityFromString(explainstring);
	}
	
	public String getExplainString()
	{
		return explainstring;
	}
	
	public double getCorsumme()
	{
		 String formattedString = String.format("%.2f", corsumme).replace(",", ".");
		return Double.valueOf(formattedString);
	}
	public void setCorsumme(double corsumme)
	{
		this.corsumme = corsumme;
	}
	public int getAnzvorkommen()
	{
		return anzvorkommen;
	}
	public void setAnzvorkommen(int anzvorkommen)
	{
		this.anzvorkommen = anzvorkommen;
	}
	public int getAnzgut()
	{
		return anzgut;
	}
	public void setAnzgut(int anzgut)
	{
		this.anzgut = anzgut;
	}

	public int getAnzbad()
	{
		return anzbad;
	}

	public void setAnzbad(int anzbad)
	{
		this.anzbad = anzbad;
	}

	public double getStdDev()
	{
		return stabil;
	}

	public void setStabil(double stabil)
	{
		this.stabil = stabil;
	}
	
}
