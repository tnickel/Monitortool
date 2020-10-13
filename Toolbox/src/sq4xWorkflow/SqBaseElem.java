package sq4xWorkflow;

import java.util.Comparator;

public class SqBaseElem
{
	// dieses ist ein listenelement der Datenbasis
	// dies beinhaltet
	// "Net profit (OOS)"
	// "Profit factor (OOS)"
	// "Stability (OOS)"
	// "Ret/DD Ratio (OOS)"
	private String strategyname = null;
	private float netprofit = 0;
	private float profitfaktor = 0;
	private float stability = 0;
	private float retdd = 0;
	
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
	
	public String getCleanName()
	{
		//realname="Q63 GBPUSD L3_+00000Merged portfolio(10)"
		//cleanname="Q63 GBPUSD L3_+00000";
		String subnam=null;
		if(strategyname.contains("Merged portfolio"))
			subnam=strategyname.substring(0,strategyname.indexOf("Merged portfolio"));
		else
			subnam=strategyname;
		
		return subnam;
		
	}
	public int compare(Object e1, Object e2)
	{
		
		return -1;
	}
}
