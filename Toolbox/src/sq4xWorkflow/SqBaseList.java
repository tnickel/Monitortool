package sq4xWorkflow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import comperatoren.SqBaseElemComperator;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqBaseList
{
	// baut die ganze Liste für DatabankExport.csv auf
	ArrayList<SqBaseElem> baselist = new ArrayList<SqBaseElem>();
	private String sqbasefile="c:/tmp/SqBaselist.csv";
	int indexNetprofit = 0;
	int indexPf = 0;
	int indexStabil = 0;
	int indexRetDD = 0;
	
	
	SqBaseList()
	{}
	
	public void SqReadBaseList(String fnam)
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
	
		SqBaseElemComperator c = new SqBaseElemComperator();
		Collections.sort(baselist,c);
		
		
		
	}
	
	public void writeResultlist(String sqbasefile)
	{
		String cleanname=null, lastcleanname=null;
		String zeile=null;
		
		File sqbf=new File(sqbasefile);
		if (sqbf.exists())
			if(sqbf.delete()==false)
				Tracer.WriteTrace(10, "E: write Resultlist,can´t delete file <"+sqbf.getAbsolutePath()+">");
				
		
		Inf inf = new Inf();
		inf.setFilename(sqbasefile);
		//gen resultlist in file in tmp
		int anz=baselist.size();
		inf.writezeile("***Name#NetProfit.#Pf#Stability#RetDD");
		for(int i=0; i<anz; i++)
		{
			SqBaseElem sbe=baselist.get(i);
			cleanname=sbe.getCleanName();
			if(cleanname.equals(lastcleanname)==false)
			{
				//falls sich der cleanname geändert hat füge Leerzeile ein und gehe weiter
				zeile="\"===================================================== \"#0.0#0.0#0.0#0.0";
				inf.writezeile(zeile);
			}
			lastcleanname=cleanname;
			zeile=sbe.getStrategyname()+"#"+sbe.getNetprofit()+"#"+sbe.getProfitfaktor()+"#"+sbe.getStability()+"#"+sbe.getRetdd();
			inf.writezeile(zeile);
		}
		inf.close();
		
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
