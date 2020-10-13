package sq4xWorkflow;

public class SqSumElem
{
	//dies element addiert die statistikwerte der gleichen Zeilen
	//eine Zeile ist gleich wenn der "cleanName" gleich ist. Das ist der Name für den jeweiligen workflow
	private SqBaseElem baseelem;
	private int anzvalues;
	
	public SqBaseElem getBaseelem()
	{
		return baseelem;
	}
	public void setBaseelem(SqBaseElem baseelem)
	{
		this.baseelem = baseelem;
	}
	public int getAnzvalues()
	{
		return anzvalues;
	}
	public void setAnzvalues(int anzvalues)
	{
		this.anzvalues = anzvalues;
	}
	public void addBaseelem(SqBaseElem be)
	{
		//prüfe nach ob überhaupt ein baeelem da ist
		if(baseelem==null)
			baseelem=new SqBaseElem();
		
		baseelem.setNetprofit(baseelem.getNetprofit()+be.getNetprofit());
		baseelem.setProfitfaktor(baseelem.getProfitfaktor()+be.getProfitfaktor());
		baseelem.setRetdd(baseelem.getRetdd()+be.getRetdd());
		baseelem.setStability(baseelem.getStability()+be.getStability());
		baseelem.setStrategyname(be.getCleanName());
		anzvalues++;
	}
	public float getNetprofit()
	{
		return((baseelem.getNetprofit()));
	}
	public float getAvrProfitfaktor()
	{
		return((baseelem.getProfitfaktor())/anzvalues);
	}
	public float getAvrRetdd()
	{
	  return((baseelem.getRetdd())/anzvalues);
	}
	public float getAvrStability()
	{
		return ((baseelem.getStability())/anzvalues);
	}
}
