package Metriklibs;

public class Filterentry
{
	//so heisst das filterkriterium
	String attribut =null;
	//nach diesen kriterien wird gefiltert
	float minvalue=0;
	float maxvalue=0;
	//minfile... ist der min max wert in dem file
	float minfilevalue=0;
	float maxfilealue=0;
	//fuer das automatische Suchen werden #steps ausprobiert
	int anzSteps=10;
	//dies sind die aktmin/max-values
	float aktMinValue=0;
	float aktMaxValue=0;
	//der entry wird nur ausgewertet wenn das aktivflag==1 ist
	int optflag=0;
	
	public String getAttribut()
	{
		return attribut;
	}
	public void setAttribut(String attribut)
	{
		this.attribut = attribut;
	}
	public float getMinvalue()
	{
		return minvalue;
	}
	public void setMinvalue(float minvalue)
	{
		this.minvalue = minvalue;
	}
	public float getMaxvalue()
	{
		return maxvalue;
	}
	public void setMaxvalue(float maxvalue)
	{
		this.maxvalue = maxvalue;
	}
	public float getMinfilevalue()
	{
		return minfilevalue;
	}
	public void setMinfilevalue(float minfilevalue)
	{
		this.minfilevalue = minfilevalue;
	}
	public float getMaxfilealue()
	{
		return maxfilealue;
	}
	public void setMaxfilealue(float maxfilealue)
	{
		this.maxfilealue = maxfilealue;
	}
	
	
	public float getAktMinValue()
	{
		return aktMinValue;
	}
	public void setAktMinValue(float aktMinValue)
	{
		this.aktMinValue = aktMinValue;
	}
	public float getAktMaxValue()
	{
		return aktMaxValue;
	}
	public void setAktMaxValue(float aktMaxValue)
	{
		this.aktMaxValue = aktMaxValue;
	}
	public int getAnzSteps()
	{
		return anzSteps;
	}
	public void setAnzSteps(int anzsteps)
	{
		this.anzSteps = anzsteps;
	}
	public int getOptflag()
	{
		return optflag;
	}
	public void setOptflag(int optflag)
	{
		this.optflag = optflag;
	}
	
}
