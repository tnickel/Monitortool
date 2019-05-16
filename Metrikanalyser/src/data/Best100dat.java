package data;

import hilfsklasse.Tracer;
import filterPack.Filterzeitraume;

public class Best100dat implements Comparable<Best100dat>
{
	//Ein best100dat value ist eine filterprozessmenge
	//in dieser Menge befinden strategien, ein endresult und filterzeiträume(metriken und schranken)
	private Stratliste stratliste;
	private Filterzeitraume filt;
	private EndtestResult endresult;

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

	public Filterzeitraume getFilt()
	{
		return filt;
	}

	public void setFilt(Filterzeitraume filt)
	{
		if(filt==null)
			Tracer.WriteTrace(10, "internal: not possible filt==null");
		this.filt = filt;
	}
	public Filterzeitraume holeFilterzeitraume()
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

	public int compareTo(Best100dat b100dat)
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
