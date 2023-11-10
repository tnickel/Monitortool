package sq4xWorkflow;

import java.util.Comparator;

import hiflsklasse.Tracer;

public class SqBaseElem
{
	// dieses ist ein listenelement der Datenbasis
	// dies beinhaltet
	// "Net profit (OOS)"
	// "Profit factor (OOS)"
	// "Stability (OOS)"
	// "Ret/DD Ratio (OOS)"
	private String strategyname = null;
	private double netprofit = 0;
	private double profitfaktor = 0;
	private double stability = 0;
	private double retdd = 0;
	
	
	
	public String getStrategyname()
	{
		return strategyname;
	}
	
	public void setStrategyname(String strategyname)
	{
		this.strategyname = strategyname;
	}
	
	public double getNetprofit()
	{
		return netprofit;
	}
	
	public void setNetprofit(double netprofit)
	{
		this.netprofit = netprofit;
	}
	
	public double getProfitfaktor()
	{
		return profitfaktor;
	}
	
	public void setProfitfaktor(double profitfaktor)
	{
		this.profitfaktor = profitfaktor;
	}
	
	public double getStability()
	{
		return stability;
	}
	
	public void setStability(double stability)
	{
		this.stability = stability;
	}
	
	public double getRetdd()
	{
		return retdd;
	}
	
	public void setRetdd(double retdd)
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
	
	public int getIndexVal()
	{
		//realname="Q63 GBPUSD L3_+00000Merged portfolio(10)"
		if(strategyname.contains("Merged portfolio"))
		{
			String subnam=null;
			String firstmarker="";
			subnam=strategyname.substring(0,strategyname.indexOf("Merged portfolio"));

			if(subnam.contains("_+"))
				firstmarker="_+";
			else if(subnam.contains("_--"))
				firstmarker="_--";
			else
				Tracer.WriteTrace(10, "E: cant find pattern '_+' or '_--' in Merged Portfolio Name -> stop 1702");
			
			subnam=subnam.substring(subnam.indexOf(firstmarker)+firstmarker.length());
			int retval=Integer.valueOf(subnam);
			return (retval);
		}
		else
			Tracer.WriteTrace(10, "E: cant find name Merged portfolio -> stop 1701");
		return 0;
	}
	
	public int compare(Object e1, Object e2)
	{
		
		return -1;
	}
}
