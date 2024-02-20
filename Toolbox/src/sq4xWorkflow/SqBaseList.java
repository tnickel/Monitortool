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
	// baut die ganze Liste für DatabankExport.csv auf
	// es werden aber nur die relevanten daten in die liste mit aufgenommen, wie
	// netprofit, profitfaktor etc.
	private ArrayList<SqBaseElem> baselist = new ArrayList<SqBaseElem>();
	
	// die sumsumliste beinhaltet die ergebnisse für einen bestimmten workflow
	private SqSumWorkflow sumWorkflow = new SqSumWorkflow();
	
	private String sqbasefile = "c:/tmp/SqBaselist.csv";
	int indexNetprofit = 0;
	int indexPf = 0;
	int indexStabil = 0;
	int indexRetDD = 0;
	private String databankname_glob = null;
	private int normf_glob=0;
	private int maxbacksteps_glob=5000;
	private String sqrootdir_glob=null;
	
	SqBaseList()
	{
	}
	
	public void SqReadBaseList(String fnam, String sqrootdir, String cpart, String databankname,int normf)
	{
		// cpart can be IS or OOS, default ist OOS
		databankname_glob = databankname;
		normf_glob=normf;
		sqrootdir_glob=sqrootdir;
		sumWorkflow.setSqRootdir(sqrootdir);
		// liste mit den Resultaten wird eingelesen und aufgebaut
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String zeile = inf.readZeile();
		// Diese Attribute werden aus der SQX-exportierten liste ausgewertet.
		String kw1 = "Net profit (Portfolio, " + cpart + ")";
		String kw2 = "Profit factor (Portfolio, " + cpart + ")";
		String kw3 = "Stability (Portfolio, " + cpart + ")";
		String kw4 = "Ret/DD Ratio (Portfolio, " + cpart + ")";
		
		if (zeile == null)
			Tracer.WriteTrace(10, "E: SQbaselist defect file<" + fnam + "> -->stop");
		
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
		
		// die SumWorkflowlist aufbauen, diese Liste beinhaltet die Informationen für
		// einen bestimmten workflow
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			// die Zeilensummen für die Statistische Auswertung addieren.
			SqBaseElem be = baselist.get(i);
			sumWorkflow.add(be);
		}
		// show list on screen
		sumWorkflow.showList();
	}
	
	public int getMaxbacksteps()
	{
		return maxbacksteps_glob;
	}

	public void setMaxbacksteps(int maxbacksteps)
	{
		this.maxbacksteps_glob = maxbacksteps;
	}

	public void writeResultlist(String sqbasefile, String portfolioname, int normationtrades,boolean fullreportflag,String outputname,String extension,int maxstepsback)
	{
		// die Resultate der Baselist müssen auf platte und müssen auch als HTML
		// angezeigt werden
		// if normation>0 One problem is that some workflows sometimes produce a lot of
		// strategies. These many strategies can distort the result. For example, a
		// workflow produces 1000 strategies. If they are positive overall, the results
		// of the other workflows are ignored. If this flag is activated, we limit the
		// maximum number of strategies. N= generated strategies. X=Max allowed
		// strategies, SUM = total winnings. If N>X than Profit=(SUM/N)*X. So
		// profit/loss is limited by this number.
		// outputname: for example portfolio
		// extension: for example IS or OOS
		//normationtrades=int wert für den normierungsfaktor
		// maxstepsback: maximal n tage wird in die vergangenheit geschaut. Ist der wert=0, dann keine begrenzung
		// limitations: pf is limited to 5 and RetDD is limited to 15
		
		String cleanname = null, lastcleanname = null;
		String zeile = null;
		int gesTradesInPortfolios = 0;
		double profperTradeSum = 0;
		
		
		File sqbf = new File(sqbasefile);
		if (sqbf.exists())
			if (sqbf.delete() == false)
				Tracer.WriteTrace(10, "E: write Resultlist,can´t delete file <" + sqbf.getAbsolutePath() + ">");
			
		Inf inf = new Inf();
		inf.setFilename(sqbasefile);
		// gen resultlist in file in tmp
		int anz = baselist.size();
		maxbacksteps_glob=maxstepsback;
		
		if (anz == 0)
		{
			Tracer.WriteTrace(10, "I: no data in SQ3, list is empty");
			return;
		}
		inf.writezeile("***Name#Norm NetProfit.#SumNetProf#Pf#Stability#RetDD#Strategies#Endtest");
		
	
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem sbe = baselist.get(i);
			sbe.setNormfaktor(normationtrades);
			cleanname = sbe.getCleanName();
			if (cleanname.equals(lastcleanname) == false)
			{
				// falls sich der cleanname geändert hat füge Leerzeile ein und gehe weiter
				zeile = "\"===================== \"#0.0#0.0#0.0#0.0#0.0#0#0";
				if(fullreportflag==true)
				inf.writezeile(zeile);
			}
			lastcleanname = cleanname;
			
			zeile = sbe.getStrategyname() + "#" + "0" + "#"+String.format(Locale.US, "%.2f", sbe.getSumNetprofit())+"#"
					+ String.format(Locale.US, "%.2f", sbe.getProfitfaktor()) + "#"
					+ String.format(Locale.US, "%.2f", sbe.getStability()) + "#"
					+ String.format(Locale.US, "%.2f", sbe.getRetdd()) + "#0#0";
			
			if(fullreportflag==true)
			inf.writezeile(zeile);
			
			//weiter wollen wir nicht zurückgehen
			if(i>maxstepsback-1)
				break;
		}
		lastcleanname = "";
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "Workflow="+outputname+"#0.0#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = portfolioname+":"+extension+"#0.0#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "Norm n="+normationtrades+"#0.0#0.0#0.0#0.0#0.0#0#0#0";
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
			int trades = sumWorkflow.calcAnzTradesAusLogfile(cleanname, portfolioname);
			sbe.setAnzTrades(trades); //setzt die Trades für eine profitzeile
			gesTradesInPortfolios = gesTradesInPortfolios + trades;
			
			double avrnetprof = calcAvrNettoprofit(cleanname,normationtrades); // Das ist der nettoprofit der links in der Zeile steht
			double avrnetprofohneNormierung=calcAvrNettoprofitOhneNormierung(cleanname);
			
			zeile = "average results <" + cleanname + ">=" + "#" + String.format(Locale.US, "%.2f", avrnetprof) + "#"
					+ String.format(Locale.US, "%.2f", avrnetprofohneNormierung) + "#"
					+ String.format(Locale.US, "%.2f", calcAvrProfitfaktor(cleanname)) + "#"
					+ String.format(Locale.US, "%.2f", calcAvrStability(cleanname)) + "#"
					+ String.format(Locale.US, "%.2f", calcAvrRetDD(cleanname)) + "#" + trades + "#"+getEndtestperiod(sqrootdir_glob,cleanname);
			inf.writezeile(zeile);
			//weiter wollen wir nicht zurückgehen
			if(i>maxstepsback-1)
				break;
		}
		zeile = "#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		
		zeile = "\"@@@@@@@@@@@@@@@@@@@@@@@@@ \"#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		zeile = "#0.0#0.0#0.0#0.0#0#0#0";
		inf.writezeile(zeile);
		
		
		zeile = "overall average results=" + "#" + String.format(Locale.US, "%.2f", calcAvrNettoprofit(null,normationtrades)) + "#"
				+ "0" + "#"
				+ String.format(Locale.US, "%.2f", calcAvrProfitfaktor(null)) + "#"
				+ String.format(Locale.US, "%.2f", calcAvrStability(null)) + "#"
				+ String.format(Locale.US, "%.2f", calcAvrRetDD(null)) + "#"+gesTradesInPortfolios+"#0#0";
		inf.writezeile(zeile);
		
		inf.close();
		
	}
	
	private String getEndtestperiod(String sqrootdir,String cleanname)
	{
		String file=sqrootdir+"\\user\\projects\\"+cleanname.replace("\"", "")+"\\enddate.txt";
		Inf inf=new Inf();
		inf.setFilename(file);
		String zeile=inf.readZeile();
		zeile=zeile.substring(zeile.indexOf("From=")+5);
		zeile=zeile.replace("\"", "");
		zeile=zeile.replace( " dateTo=", "-");
		inf.close();
		
		return zeile;
	}
	
	public double calcAvrNettoprofit(String cleanname,int normf)
	{
		//normf=normfaktor
		int anz = baselist.size();
		// count the amount of cleannames
		int anzc = 0;
		double sum = 0;
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				//Hier werden die normierten Nettoprofits aufsummiert
				double netprof=be.getNetprofit(normf);
				
								
				sum = sum + netprof;
				anzc++;
			}
			if(i>maxbacksteps_glob-1)
				break;
		}
		return (sum / anzc);
	}
	public double calcAvrNettoprofitOhneNormierung(String cleanname)
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
				//hier werdne die summen-Nettoprofits aufssumiert
				double netprof=be.getSumNetprofit();
				
								
				sum = sum + netprof;
				anzc++;
			}
			if(i>maxbacksteps_glob-1)
				break;
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
			if(i>maxbacksteps_glob-1)
				break;
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
			if(i>maxbacksteps_glob-1)
				break;
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
			if(i>maxbacksteps_glob-1)
				break;
		}
		return (sum / anzc);
	}
	
	public double calcStddevNettoprofit(String cleanname,int normf)
	{
		
		double[] dl = new double[10000];
		int dlcount = 0;
		// Tracer.WriteTrace(20, "I:calc StddevNettoprofit <"+cleanname+">");
		int anz = baselist.size();
		for (int i = 0; i < anz; i++)
		{
			SqBaseElem be = baselist.get(i);
			if ((cleanname == null) || (be.getCleanName().contains(cleanname)))
			{
				dl[dlcount] = be.getNetprofit( normf);
				// Tracer.WriteTrace(20, "I:calc StddevNettoprofit <"+cleanname+">
				// c:<"+dlcount+"> NetProfit<"+be.getNetprofit()+">");
				dlcount++;
			}
			if(i>maxbacksteps_glob-1)
				break;
		}
		
		// Statistics.printArray(dl,dlcount);
		double stdnetprofit = Statistics.stddv(dl, dlcount);
		// Tracer.WriteTrace(10, "stdnetprof="+stdnetprofit);
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
			if(i>maxbacksteps_glob-1)
				break;
		}
		return (Statistics.stddv(dl, dlcount));
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
			if(i>maxbacksteps_glob-1)
				break;
		}
		return (Statistics.stddv(dl, dlcount));
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
			if(i>maxbacksteps_glob-1)
				break;
		}
		return (Statistics.stddv(dl, dlcount));
	}
	
	public void ShowChart()
	{
		// hier wir die graphik vom freechart angezeigt.
		SumChart.ShowChart(baselist, databankname_glob,normf_glob,maxbacksteps_glob);
	}
}
