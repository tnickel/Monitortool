package sq4xWorkflow;

public class SqBaseElem
{
	//dieses ist ein listenelement
	//dies beinhaltet
	//"Net profit (OOS)"
	//"Profit factor (OOS)"
	//"Stability (OOS)"
	//"Ret/DD Ratio (OOS)"
	private String strategyname=null;
	private float netprofit =0;
	private float profitfaktor =0;
	private float stability  =0;
	private float retdd =0;
	
	public String getStrategyname()
	{
		return strategyname;
	}
	public void setStrategyname(String strategyname)
	{
		this.strategyname = strategyname;
	}
	public float getNetprofit()
	{
		return netprofit;
	}
	public void setNetprofit(float netprofit)
	{
		this.netprofit = netprofit;
	}
	public float getProfitfaktor()
	{
		return profitfaktor;
	}
	public void setProfitfaktor(float profitfaktor)
	{
		this.profitfaktor = profitfaktor;
	}
	public float getStability()
	{
		return stability;
	}
	public void setStability(float stability)
	{
		this.stability = stability;
	}
	public float getRetdd()
	{
		return retdd;
	}
	public void setRetdd(float retdd)
	{
		this.retdd = retdd;
	}
	
}
