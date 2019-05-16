package objects;

import hilfsklasse.Tools;

public class Datumsintervall
{
	String startdatum = null;
	String enddatum = null;

	public Datumsintervall()
	{
	}

	public Datumsintervall(String start, String end)
	{
		startdatum = start;
		enddatum = end;
	}

	public String getStartdatum()
	{
		return startdatum;
	}

	public void setStartdatum(String startdatum)
	{
		this.startdatum = startdatum;
	}

	public String getEnddatum()
	{
		return enddatum;
	}

	public void setEnddatum(String enddatum)
	{
		this.enddatum = enddatum;
	}

	public int calcBewertungstage()
	{
		// Es wird die Zeitdifferenz in tagen zwischen start und enddatum
		// berechnet
		int tageanz = Tools.getDateInt(startdatum, enddatum);
		return tageanz;
	}
}
