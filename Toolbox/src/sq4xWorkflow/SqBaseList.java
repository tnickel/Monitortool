package sq4xWorkflow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import comperatoren.SqBaseElemComperator;
import graphic.SumChart;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import statistic.Statistics;

public class SqBaseList
{
	// In der Baselist sind alle Baselemems. Ein Baseelem ist eine Zeile aus dem sq
	// Exportierten datenfile
	// baut die ganze Liste f�r DatabankExport.csv auf
	// es werden aber nur die relevanten daten in die liste mit aufgenommen, wie
	// netprofit, profitfaktor etc.
	private ArrayList<SqBaseElem> baselist = new ArrayList<SqBaseElem>();
	
	// die sumsumliste beinhaltet die ergebnisse f�r einen bestimmten workflow
	private SqSumWorkflow sumWorkflow = new SqSumWorkflow();
	
	private String sqbasefile = "c:/tmp/SqBaselist.csv";
	int indexNetprofit = 0;
	int indexPf = 0;
	int indexStabil = 0;
	int indexRetDD = 0;
	private String databankname_glob=null;
	
	SqBaseList()
	{
	}
	
	public void SqReadBaseList(String fnam,String sqrootdir,String cpart,String databankname)
	{
		//cpart can be IS or OOS, default ist OOS
		databankname_glob=databankname;
		sumWorkflow.setSqRootdir(sqrootdir);
		// liste mit den Resultaten wird eingelesen und aufgebaut
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String zeile = inf.readZeile();
		//Diese Attribute werden aus der SQX-exportierten liste ausgewertet.
		String kw1="Net profit (Portfolio, "+cpart+")";
		String kw2="Profit factor (Portfolio, "+cpart+")";
		String kw3="Stability (Portfolio, "+cpart+")";
		String kw4="Ret/DD Ratio (Portfolio, "+cpart+")";
	
		
		if(zeile==null)
			Tracer.WriteTrace(10, "E: SQbaselist defect file<"+fnam+"> -->stop");
		
		String[] headerparts = zeile.split(";");
		int anzheaderparts = headerparts.length;
		for (int i = 0; i < anzheaderparts; i++)
		{
			String elemstr = headerparts[i];
			if (elemstr.contains(kw1))
				indexNetprofit = i;
			else if (elemstr.contains(kw2))
				indexPf = i;
			else if (elemstr.contains(kw3))
				indexStabil = i;
			else if (elemstr.contains(kw4))
				indexRetDD = i;
		}
		if ((indexNetprofit == 0) || (indexPf == 0) || (indexStabil == 0) || (indexRetDD == 0))
			Tracer.WriteTrace(10, "keywort not found-> show in header");
		// lese alle weiteren Zeilen und baue die baselist auf
		// in der Baselist sind alle Zeilen drin
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
		
		// die SumWorkflowlist aufbauen, diese Liste beinhaltet die Informationen f�r
		// einen bestimmten workflow
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			// die Zeilensummen f�r die Statistische Auswertung addieren.
			SqBaseElem be = baselist.get(i);
			sumWorkflow.add(be);
		}
		// show list on screen
		sumWorkflow.showList();
	}
	
	public void writeResultlist(String sqbasefile)
	{
		// die Resultate der Baselist m�ssen auf platte und m�ssen auch als HTML
		// angezeigt werden
		
		String cleanname = null, lastcleanname = null;
		String zeile = null;
		int gesTradesInPortfolios=0;
		double profperTradeSum=0;
		
		File sqbf = new File(sqbasefile);
		if (sqbf.exists())
			if (sqbf.delete() == false)
				Tracer.WriteTrace(10, "E: write Resultlist,can�t delete file <" + sqbf.getAbsolutePath() + ">");
			
		Inf inf = new Inf();
		inf.setFilename(sqbasefile);
		// gen resultlist in file in tmp
		int anz = baselist.size();
		
		if(anz==0)
		{
			Tracer.WriteTrace(10, "I: no data in SQ3, list is empty");
			return;
		}
		inf.writezeile("***Name#NetProfit.#Prof per Trade#Pf#Stability#RetDD#Strategies#0");
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem sbe = baselist.get(i);
			cleanname = sbe.getCleanName();
			if (cleanname.equals(lastcleanname) == false)
			{
				// falls sich der cleanname ge�ndert hat f�ge Leerzeile ein und gehe weiter
				zeile = "\"===================== \"#0.0#0.0#0.0#0.0#0.0#0#0";
				inf.writezeile(zeile);
			}
			lastcleanname = cleanname;
		
			zeile = sbe.getStrategyname() + "#" + String.format(Locale.US,"%.2f",sbe.getNetprofit()) + "#0#" + String.format(Locale.US,"%.2f",sbe.getProfitfaktor()) + "#"
					+ String.format(Locale.US,"%.2f",sbe.getStability()) + "#" + String.format(Locale.US,"%.2f",sbe.getRetdd())+"#0#0";
			inf.writezeile(zeile);
		}
		lastcleanname="";
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "average results  #0.0#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem sbe = baselist.get(i);
			cleanname = sbe.getCleanName();
			
			if (cleanname.equals(lastcleanname) == true)
				continue;
			lastcleanname = cleanname;
			int trades=sumWorkflow.calcAnzTradesAusLogfile(cleanname);
			gesTradesInPortfolios=gesTradesInPortfolios+trades;
			
			double avrnetprof=calcAvrNettoprofit(cleanname); //Das ist der nettoprofit der links in der Zeile steht
			double netprofitPerTrade=(avrnetprof/trades);//Wir wollen den durchschnittlichen profit f�r einen trade ermitteln
			profperTradeSum=profperTradeSum+netprofitPerTrade; //wir summieren das ganze
			zeile = "average results <" + cleanname + ">=" + "#" + String.format(Locale.US,"%.2f",avrnetprof) + "#"+String.format(Locale.US,"%3.1f",(avrnetprof/trades))+"#"
					+ String.format(Locale.US,"%.2f",calcAvrProfitfaktor(cleanname)) + "#" + String.format(Locale.US,"%.2f",calcAvrStability(cleanname)) + "#"
					+ String.format(Locale.US,"%.2f",calcAvrRetDD(cleanname))+"#"+trades+"#0";
			inf.writezeile(zeile);
			
		}
		zeile = "#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);

	
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "overall average results=" + "#" + String.format(Locale.US,"%.2f",calcAvrNettoprofit(null)) + "#"+String.format(Locale.US,"%.2f",(profperTradeSum/anz))+"#" + String.format(Locale.US,"%.2f",calcAvrProfitfaktor(null)) + "#"
				+ String.format(Locale.US,"%.2f",calcAvrStability(null)) + "#" + String.format(Locale.US,"%.2f",calcAvrRetDD(null))+"#0#0#0";
		inf.writezeile(zeile);
		//zeile = "overall standart deviation=" + "#" + String.format(Locale.US,"%.2f",calcStddevNettoprofit(null)) + "#" + String.format(Locale.US,"%.2f",calcStddevProfitfaktor(null))
		//		+ "#" + String.format(Locale.US,"%.2f",calcStddevStability(null)) + "#" + String.format(Locale.US,"%.2f",calcStddevRetDD(null))+"#0";
		//inf.writezeile(zeile);
		inf.close();
		
	}
	
	public double calcAvrNettoprofit(String cleanname)
	{
		int anz = baselist.size();
		// count the amount of cleannames
		int anzc = 0;
		double sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				sum = sum + be.getNetprofit();
				anzc++;
			}
		}
		return (sum / anzc);
	}
	
	public double calcAvrRetDD(String cleanname)
	{
		int anz = baselist.size();
		// count the amount of cleannames
		int anzc = 0;
		double sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				sum = sum + be.getRetdd();
				anzc++;
			}
		}
		return (sum / anzc);
	}
	
	public double calcAvrProfitfaktor(String cleanname)
	{
		int anz = baselist.size();
		// count the amount of cleannames
		int anzc = 0;
		double sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				sum = sum + be.getProfitfaktor();
				anzc++;
			}
		}
		return (sum / anzc);
	}
	
	public double calcAvrStability(String cleanname)
	{
		int anz = baselist.size();
		// count the amount of cleannames
		int anzc = 0;
		double sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				sum = sum + be.getStability();
				anzc++;
			}
		}
		return (sum / anzc);
	}
	
	public double calcStddevNettoprofit(String cleanname)
	{
		
		double[] dl = new double[10000];
		int dlcount=0;
		//Tracer.WriteTrace(20, "I:calc StddevNettoprofit <"+cleanname+">");
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				dl[dlcount] = be.getNetprofit();
				//Tracer.WriteTrace(20, "I:calc StddevNettoprofit <"+cleanname+"> c:<"+dlcount+"> NetProfit<"+be.getNetprofit()+">");
				dlcount++;
			}
		}
		
		//Statistics.printArray(dl,dlcount);
		double stdnetprofit=Statistics.stddv(dl,dlcount);
		//Tracer.WriteTrace(10, "stdnetprof="+stdnetprofit);
		return (stdnetprofit);
	}
	
	public double calcStddevRetDD(String cleanname)
	{
		double[] dl = new double[10000];
		int dlcount = 0;
		int anz = baselist.size();
		
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				dl[dlcount] = be.getRetdd();
				dlcount++;
			}
		}
		return (Statistics.stddv(dl,dlcount));
	}
	
	public double calcStddevProfitfaktor(String cleanname)
	{
		double[] dl = new double[10000];
		int dlcount = 0;
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				dl[dlcount] = be.getProfitfaktor();
				dlcount++;
			}
		}
		return (Statistics.stddv(dl,dlcount));
	}
	
	public double calcStddevStability(String cleanname)
	{
		double[] dl = new double[10000];
		int dlcount = 0;
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				dl[dlcount] = be.getStability();
				dlcount++;
			}
		}
		return (Statistics.stddv(dl,dlcount));
	}
	public void ShowChart()
	{
		//hier wir die graphik vom freechart angezeigt.
		SumChart.ShowChart(baselist,databankname_glob);
	}
}
