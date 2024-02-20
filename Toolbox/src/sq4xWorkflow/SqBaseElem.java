package sq4xWorkflow;

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
	private double avrprofitpertrade = 0;
	private int anzTrades_glob = 0;
	private int normfaktor = 0;
	
	public String getStrategyname()
	{
		return strategyname;
	}
	
	public void setStrategyname(String strategyname)
	{
		this.strategyname = strategyname;
	}
	
	public double getNetprofit(int normf)
	{
		double np = 0;
		if (normf > 0)
		{
			// falls normiert werden muss, wir also mehr trades haben als den normfaktor
			if (anzTrades_glob == 0)
				return 0;
			
			np = ((netprofit / anzTrades_glob) * normf);
			return np;
			
		}
		// keine normierung dann gib einfach den netprofit zurück
		return netprofit;
	}
	
	public double getSumNetprofit()
	{
		double np = 0;
		
		// keine normierung dann gib einfach den netprofit zurück
		return netprofit;
	}
	
	public void setNetprofit(double netprofit)
	{
		this.netprofit = netprofit;
	}
	
	public void addNetprofit(double netprofi)
	{
		this.netprofit = this.netprofit + netprofi;
	}
	
	public double getProfitfaktor()
	{
		// profitfkator is limited to 5
		if (profitfaktor > 5)
			return 5;
		else
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
		// retDD is limited to 15
		if (retdd > 15)
			return 15;
		else
			return retdd;
	}
	
	public void setRetdd(double retdd)
	{
		this.retdd = retdd;
	}
	
	public double getAvrprofitpertrade()
	{
		return avrprofitpertrade;
	}
	
	public void setAvrprofitpertrade(double avrprofitpertrade)
	{
		this.avrprofitpertrade = avrprofitpertrade;
	}
	
	public int getAnzTrades()
	{
		return anzTrades_glob;
	}
	
	public void setAnzTrades(int anzTrades)
	{
		this.anzTrades_glob = anzTrades;
	}
	
	public int getNormfaktor()
	{
		return normfaktor;
	}
	
	public void setNormfaktor(int normfaktor)
	{
		this.normfaktor = normfaktor;
	}
	
	public String getCleanName()
	{
		// realname="Q63 GBPUSD L3_+00000Merged portfolio(10)"
		// cleanname="Q63 GBPUSD L3_+00000";
		String subnam = null;
		if (strategyname.contains("Merged portfolio"))
			subnam = strategyname.substring(0, strategyname.indexOf("Merged portfolio"));
		else
			subnam = strategyname;
		
		return subnam;
		
	}
	
	public int getIndexVal()
	{
		boolean negindexflag = false;
		// realname="Q63 GBPUSD L3_+00000Merged portfolio(10)"
		if (strategyname.contains("Merged portfolio"))
		{
			String subnam = null;
			String firstmarker = "";
			subnam = strategyname.substring(0, strategyname.indexOf("Merged portfolio"));
			
			if (subnam.contains("_+"))
			{
				firstmarker = "_+";
				// positiver index gefunden
			} else if (subnam.contains("_--"))
			{
				firstmarker = "_--";
				// negativer index gefunden
				negindexflag = true;
			} else
				Tracer.WriteTrace(10, "E: cant find pattern '_+' or '_--' in Merged Portfolio Name -> stop 1702");
			
			subnam = subnam.substring(subnam.indexOf(firstmarker) + firstmarker.length());
			int retval = Integer.valueOf(subnam);
			
			if (negindexflag == true)
				return (-retval);
			else
				return (retval);
		} else
			Tracer.WriteTrace(10, "E: cant find name Merged portfolio in <" + strategyname + "> -> stop 1701");
		return 0;
	}
	

}
