package Metriklibs;

public class MinMaxFilter
{
	String Attribut=null;
	float minvalue=0;
	float maxvalue=0;
	float minfilevalue=0;
	float maxfilevalue=0;
	int anzSteps=0;
	//die stepsize resultiert von dem minvalue und den anzSteps
	float stepSize=0;
	//dies sind die aktmin/max-values
	float aktMinValue=0;
	float aktMaxValue=0;
	public String getAttribut()
	{
		return Attribut;
	}
	public void setAttribut(String attribut)
	{
		Attribut = attribut;
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
	public float getMaxfilevalue()
	{
		return maxfilevalue;
	}
	public void setMaxfilevalue(float maxfilevalue)
	{
		this.maxfilevalue = maxfilevalue;
	}
	public int getAnzSteps()
	{
		return anzSteps;
	}
	public void setAnzSteps(int anzSteps)
	{
		this.anzSteps = anzSteps;
	}
	public float getStepSize()
	{
		return stepSize;
	}
	public void setStepSize(float stepSize)
	{
		this.stepSize = stepSize;
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

}
