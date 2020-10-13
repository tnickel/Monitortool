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
	// es werden aber nur die relevanten daten in die liste mit aufgenommen, wie netprofit, profitfaktor etc.
	private ArrayList<SqBaseElem> baselist = new ArrayList<SqBaseElem>();

	//die sumsumliste beinhaltet die ergebnisse für einen bestimmten workflow
	private SqSumWorkflow sumsum = new SqSumWorkflow();
	
	private String sqbasefile = "c:/tmp/SqBaselist.csv";
	int indexNetprofit = 0;
	int indexPf = 0;
	int indexStabil = 0;
	int indexRetDD = 0;
	
	SqBaseList()
	{
	}
	
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
			if (elemstr.contains("Net profit (Portfolio, OOS)"))
				indexNetprofit = i;
			else if (elemstr.contains("Profit factor (Portfolio, OOS)"))
				indexPf = i;
			else if (elemstr.contains("Stability (Portfolio, OOS)"))
				indexStabil = i;
			else if (elemstr.contains("Ret/DD Ratio (Portfolio, OOS)"))
				indexRetDD = i;
		}
		if ((indexNetprofit == 0) || (indexPf == 0) || (indexStabil == 0) || (indexRetDD == 0))
			Tracer.WriteTrace(10, "keywort not found-> show in header");
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
		Collections.sort(baselist, c);
		
		// die summenliste aufbauen
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			// die Zeilensummen für die Statistische Auswertung addieren.
			SqBaseElem be = baselist.get(i);
			sumsum.add(be);
		}
		sumsum.showList();
	}
	
	public void writeResultlist(String sqbasefile)
	{
		String cleanname = null, lastcleanname = null;
		String zeile = null;
		
		File sqbf = new File(sqbasefile);
		if (sqbf.exists())
			if (sqbf.delete() == false)
				Tracer.WriteTrace(10, "E: write Resultlist,can´t delete file <" + sqbf.getAbsolutePath() + ">");
			
		Inf inf = new Inf();
		inf.setFilename(sqbasefile);
		// gen resultlist in file in tmp
		int anz = baselist.size();
		inf.writezeile("***Name#NetProfit.#Pf#Stability#RetDD");
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem sbe = baselist.get(i);
			cleanname = sbe.getCleanName();
			if (cleanname.equals(lastcleanname) == false)
			{
				// falls sich der cleanname geändert hat füge Leerzeile ein und gehe weiter
				zeile = "\"===================================================================================== \"#0.0#0.0#0.0#0.0";
				inf.writezeile(zeile);
			}
			lastcleanname = cleanname;
			zeile = sbe.getStrategyname() + "#" + sbe.getNetprofit() + "#" + sbe.getProfitfaktor() + "#"
					+ sbe.getStability() + "#" + sbe.getRetdd();
			inf.writezeile(zeile);
		}
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0";
		inf.writezeile(zeile);
		zeile = "\"average results per workflow\"#0.0#0.0#0.0#0.0";
		inf.writezeile(zeile);
		anz = sumsum.getSize();
		for (int i = 0; i < anz; i++)
		{
			SqSumElem su = sumsum.getElem(i);
			zeile = su.getBaseelem().getStrategyname() + "#" + su.getNetprofit() + "#"
					+ su.getAvrProfitfaktor() + "#" + su.getAvrStability() + "#" + su.getAvrRetdd();
			inf.writezeile(zeile);
		}
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0";
		inf.writezeile(zeile);
		zeile = "\"\"#0.0#0.0#0.0#0.0";
		inf.writezeile(zeile);
		zeile="overall average results="+"#"+calcAvrNettoprofit()+"#"+calcAvrProfitfaktor()+"#"+calcAvrStability()+"#"+calcAvrRetDD();
		inf.writezeile(zeile);
		inf.close();
		
	}
	
	public float calcAvrNettoprofit()
	{
		int anz = baselist.size();
		float sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			sum = sum + be.getNetprofit();
		}
		return (sum / anz);
	}
	
	public float calcAvrRetDD()
	{
		int anz = baselist.size();
		float sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			sum = sum + be.getRetdd();
		}
		return (sum / anz);
	}
	
	public float calcAvrProfitfaktor()
	{
		int anz = baselist.size();
		float sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			sum = sum + be.getProfitfaktor();
		}
		return (sum / anz);
	}
	
	public float calcAvrStability()
	{
		int anz = baselist.size();
		float sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			sum = sum + be.getStability();
		}
		return (sum / anz);
	}
	
}
