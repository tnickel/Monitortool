package data;

public class CorelSetting
{
	 int anzSteps=100;
	 float minCorelLevel=0.1f;
	 float correlFaktor=1.5f;
	
	public int getAnzSteps()
	{
		return anzSteps;
	}
	public void setAnzSteps(int anzStepsx)
	{
		anzSteps = anzStepsx;
	}
	public float getMinCorelLevel()
	{
		return minCorelLevel;
	}
	public void setMinCorelLevel(float minCorelLevel)
	{
		this.minCorelLevel = minCorelLevel;
	}
	public float getCorrelFaktor()
	{
		return correlFaktor;
	}
	public void setCorrelFaktor(float correlFaktor)
	{
		this.correlFaktor = correlFaktor;
	}
	
	
}
