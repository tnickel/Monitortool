package data;


import gui.Mbox;

import java.util.ArrayList;

import javax.swing.JPanel;

import charttool.StaticMultiProfitPanel;

public class OptimizeResultliste
{
	//in diesem Megaarray werden alle optimierten resultate gespeichert
	public ArrayList<OptimizeResult> resultliste = new ArrayList<OptimizeResult>();

	//hier ist die orginale Tradeliste drin
	private Tradeliste orginalTradeliste_glob=null;
	
	public OptimizeResultliste(Tradeliste tr)
	{
		orginalTradeliste_glob=tr;
	}
	
	public int getSize()
	{
		return(resultliste.size());
	}
	
	public OptimizeResult getElem(int i)
	{
		int anz=resultliste.size();
		if(i>anz)
			Mbox.Infobox("out of bounds i<"+i+"> bigger than size<"+anz+">");
		return(resultliste.get(i));
	}
	
	public void add(OptimizeResult os)
	{
		resultliste.add(os);
	}
	
	public PanelResult holeBestPanel(Timefilter timefilter,int panelindex)
	{
		Tradeliste tradeliste_best=holeBestTradelist(timefilter);

		if(tradeliste_best==null)
			return null;
		
		//das jpanel generieren
		StaticMultiProfitPanel profpan= new StaticMultiProfitPanel();
		PanelResult pr = new PanelResult();
		JPanel jp=profpan.createDemoPanel(tradeliste_best, "optimizedxxx",pr.getGdx(),panelindex);
		pr.setPanel(jp);
		
		return (pr);
	}

	public PanelResult holePanelResult(String resname,String optoalgoname,Timefilter timefilter,int panelindex)
	{
		//panelindex 1...4
		StaticMultiProfitPanel profpan= new StaticMultiProfitPanel();

		//die tradeliste holen wo der filter angewendet wurde
		Tradeliste tr=holeTradelisteName(resname,optoalgoname,timefilter);
		
		PanelResult pr= new PanelResult();
		
		
		JPanel pan=profpan.createDemoPanel(tr, resname,pr.getGdx(),panelindex);
		pr.setPanel(pan);
		pr.setGdx(tr.getgdx());
		return pr;
	}
	
	/*public Tradeliste holeTradeliste_dep(int i,Timefilter timefilter)
	{
		//holt die tradeliste mit dem index i
		OptimizeResult or=resultliste.get(i);
		
		//die tradeliste aus dem optimize result berechnen
		Tradeliste tradeliste=calcOptimizedTradelist(orginalTradeliste_glob,or,timefilter);
		tradeliste.setgdx(or.getgdx());
		return tradeliste;
	}*/
	public Tradeliste holeTradelisteName(String name,String optoalgoname,Timefilter timefilter)
	{
		int foundflag=0;
		OptimizeResult or=null;
		//holt die tradeliste mit dem namen
		int anz=resultliste.size();
		for(int i=0; i<anz; i++)
		{
			 or=resultliste.get(i);
			if((or.getResultname().equals(name))&&(or.getOptalgoname().equals(optoalgoname)))
			{
				foundflag=1;
				break;
			}
		}
		if(foundflag==0)
			Mbox.Infobox("internal error resultlist corrupt");
		
		//die tradeliste aus dem optimize result berechnen
		Tradeliste tradeliste=calcOptimizedTradelist(orginalTradeliste_glob,or,timefilter);
		tradeliste.setgdx(or.getgdx());
		return tradeliste;
	}
	
	public OptimizeResult holeOptimizeResult(String resultname,String optoalgoname)
	{
		OptimizeResult or=null;
		
		int anz=resultliste.size();
		for(int i=0; i<anz; i++)
		{
			or=resultliste.get(i);
			if((or.getResultname().equals(resultname))&&(or.getOptalgoname().equals(optoalgoname)))
				return or;
		}
		if(or==null)
			Mbox.Infobox("error resultname<"+resultname+"> <"+optoalgoname+"> not found");
		
		return or;
	}
	public int holeOptimizeIndex(String resultname,String optoalgoname)
	{
		OptimizeResult or=null;
		
		int anz=resultliste.size();
		for(int i=0; i<anz; i++)
		{
			or=resultliste.get(i);
			if((or.getResultname().equals(resultname))&&(or.getOptalgoname().equals(optoalgoname)))
				return i;
		}
		if(or==null)
			Mbox.Infobox("error resultname<"+resultname+"> <"+optoalgoname+"> not found");
		
		return -1;
	}
	
	public OptimizeResult holeBestOptimizeResult()
	{
		OptimizeResult bestoptimize= null;
		OptimizeResult os=null;
		double maxgewinn=0;
		
		int anz=resultliste.size();
		
		if(anz==0)
		{
			Mbox.Infobox("No strategie selected, please select it");
			return null;
		}
		for(int i=0; i<anz; i++)
		{
			os= resultliste.get(i);
			
			double gewinn=os.getGewinnsumme();
			int period=os.getgdx();
			System.out.println("gewinn="+gewinn+ "period="+period);
			
			if(gewinn>maxgewinn)
			{
				System.out.println("*** neuer maxgewinn="+gewinn+ "period="+period);
				bestoptimize=os;
				maxgewinn=gewinn;
				
			}
		}

		return bestoptimize;
	}
	
	
	public Tradeliste holeBestTradelist(Timefilter timefilter)
	{
		//holt die tradeliste mit dem maximalen Gewinn
		
		OptimizeResult bestoptimize= null;
		double maxgewinn=0;
		
		int anz=resultliste.size();
		for(int i=0; i<anz; i++)
		{
			OptimizeResult os= resultliste.get(i);
			
			double gewinn=os.getGewinnsumme();
			int period=os.getgdx();
			System.out.println("gewinn="+gewinn+ "period="+period);
			
			if(gewinn>maxgewinn)
			{
				System.out.println("*** neuer maxgewinn="+gewinn+ "period="+period);
				bestoptimize=os;
				maxgewinn=gewinn;
				
			}
		}

		Tradeliste tradeliste_best=calcOptimizedTradelist(orginalTradeliste_glob,bestoptimize,timefilter);

		if(tradeliste_best==null)
		{
			Mbox.Infobox("no results");
			return null;
		}
		System.out.println("maxgewinn="+bestoptimize.getGewinnsumme()+" maxperiod="+bestoptimize.getgdx());

		tradeliste_best.setgdx(bestoptimize.getgdx());
		
		return (tradeliste_best);
	}
	
	
	static public Tradeliste calcOptimizedTradelist(Tradeliste tradeliste,OptimizeResult or,Timefilter timefilter)
	{
		
		//es wird die optimierte Tradesliste errechnet
		//hier werden alle trades gelöscht die nicht im optimalen result vorkommen
		Tradeliste tradelisteopt = new Tradeliste(Rootpath.rootpath+ "\\data\\tradeliste.xml");

		if(or==null)
			return null;
		
		int[] marker =or.getMarker();
		
		if(tradeliste==null)
			return null;
		
		int anz = tradeliste.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade tr1 = tradeliste.getelem(i);

			//prüfe den den Timefilter
			if(timefilter!=null)
			if(tr1.checkTimefilter(timefilter)==false)
				continue;//nicht übernehmen da timefilter nicht passt
			
			//falls das element markiert ist dann übernehme
			if(marker[i]==1)
			{
				tr1.setTransactionnumber(1000+i);
				tradelisteopt.addTradeElem(tr1);
			}
		}

		if(tradelisteopt.getsize()==0)
			Mbox.Infobox("no trades in optimized tradelist after timefiler");
		tradelisteopt.calcSummengewinne();
		
		return tradelisteopt;
	}
}
