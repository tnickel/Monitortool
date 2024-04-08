package data;

import filterPack.Filterfiles;
import hilfsklasse.Tracer;

public class Portfolio implements Comparable<Portfolio>
{
	//Ein best100dat value ist eine filterprozessmenge
	//in dieser Menge befinden strategien, ein endresult und filterzeiträume(metriken und schranken)
	//Was genau ist ein Portfolio
	//Ein Portfolio: besteht aus einer Strategielist, dies ist eine Anzahl an Strategien die in diesem Portfolio ist.
	//Filterzeiträume:hier sind die Filtersettings drin, diese Daten brauchen wir wenn wir filern wollen
	//EndtestResult:Hier ist nur der value, also der gesammtprofit für das Portfolio drin.
	private Stratliste stratliste;
	private Filterfiles filt;
	private EndtestResult endresult;
	private float ResultUnknown[]=new float[6];

	public Stratliste getStratliste()
	{
		return stratliste;
	}

	public void setStratliste(Stratliste stratliste)
	{
		if(stratliste==null)
			Tracer.WriteTrace(10, "internal: not possible stratliste==null");
		this.stratliste = stratliste;
	}

	public Filterfiles getFilt()
	{
		return filt;
	}

	public void setFilt(Filterfiles filt)
	{
		if(filt==null)
			Tracer.WriteTrace(10, "internal: not possible filt==null");
		this.filt = filt;
	}
	public Filterfiles holeFilterzeitraume()
	{
		return filt;
	}
	
	public EndtestResult getEndresult()
	{
		return endresult;
	}

	public void setEndresult(EndtestResult endresult)
	{
		if(endresult==null)
			Tracer.WriteTrace(10, "internal: not possible endresult==null");
		this.endresult = endresult;
	}
	public void setEndresultUnknownData(EndtestResult endr,int index)
	{
		//Endtestwert auf unbekannte daten
		ResultUnknown[index]=endr.getFitnessvalue();
		
	}

	public float getEndresultUnknownData(int index)
	{
		//Endtestwert auf unbekannte daten
		return(ResultUnknown[index]);
	}
	
	public int compareTo(Portfolio b100dat)
	{
		EndtestResult eres = b100dat.getEndresult();
		if (eres.getFitnessvalue() < endresult.getFitnessvalue())
			return -1;
		else
		if (eres.getFitnessvalue() > endresult.getFitnessvalue())
		{
			return 1;
		} else
			return 0;// gleich

	}

}
