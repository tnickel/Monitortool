package sq4xWorkflow;

import java.util.ArrayList;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqBaseList
{
	// baut die ganze Liste für DatabankExport.csv auf
	ArrayList<SqBaseElem> baselist = new ArrayList<SqBaseElem>();
	
	int indexNetprofit = 0;
	int indexPf = 0;
	int indexStabil = 0;
	int indexRetDD = 0;
	
	SqBaseList(String fnam)
	{
		// liste wird eingelesen und aufgebaut
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String zeile = inf.readZeile();
		String[] headerparts = zeile.split(";");
		int anzheaderparts = headerparts.length;
		for (int i = 0; i < anzheaderparts; i++)
		{
			String elemstr = headerparts[i];
			if (elemstr.contains("Net profit (OOS)"))
				indexNetprofit = i;
			else if (elemstr.contains("Profit factor (OOS)"))
				indexPf = i;
			else if (elemstr.contains("Stability (OOS)"))
				indexStabil = i;
			else if (elemstr.contains("Ret/DD Ratio (OOS)"))
				indexRetDD = i;
		}
		
		// lese alle weiteren Zeilen und baue die baselist auf
		
		zeile = inf.readZeile();
		while (zeile != null)
		{
			SqBaseElem be = new SqBaseElem();
			String[] parts = zeile.split(";");
			be.setNetprofit(Float.valueOf(parts[indexNetprofit].replace("\"", "")));
			be.setProfitfaktor(Float.valueOf(parts[indexPf].replace("\"", "")));
			be.setStability(Float.valueOf(parts[indexStabil].replace("\"", "")));
			be.setRetdd(Float.valueOf(parts[indexRetDD].replace("\"", "")));
			be.setStrategyname(parts[0]);
			baselist.add(be);
			zeile = inf.readZeile();
		}
	
	}
	
	public float calcNettoprofit(String workflowname)
	{
		int anz=baselist.size();
		float sum =0;
		for(int i=0; i<anz; i++)
		{
			SqBaseElem be=baselist.get(i);
			sum=sum+be.getNetprofit();
		}
		return (sum/anz);
	}
	
	public float calcRetDD(String workflowname)
	{
		int anz=baselist.size();
		float sum =0;
		for(int i=0; i<anz; i++)
		{
			SqBaseElem be=baselist.get(i);
			sum=sum+be.getRetdd();
		}
		return (sum/anz);
	}
	
	public float calcProfitfaktor(String workflowname)
	{
		int anz=baselist.size();
		float sum =0;
		for(int i=0; i<anz; i++)
		{
			SqBaseElem be=baselist.get(i);
			sum=sum+be.getProfitfaktor();
		}
		return (sum/anz);
	}
	
	public float calcStability(String workflowname)
	{
		int anz=baselist.size();
		float sum =0;
		for(int i=0; i<anz; i++)
		{
			SqBaseElem be=baselist.get(i);
			sum=sum+be.getStability();
		}
		return (sum/anz);
	}
	
}
