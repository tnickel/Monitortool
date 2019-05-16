package data;

import java.util.ArrayList;

import javax.swing.JPanel;

import charttool.ProfitPanel;

public class OptimizeGesammtliste
{
	//in diesem Megaarray werden alle optimierten resultate gespeichert
	public ArrayList<OptimizeResult> resultliste = new ArrayList<OptimizeResult>();

	//hier ist die orginale Tradeliste drin
	private Tradeliste orginalTradeliste_glob=null;
	
	public OptimizeGesammtliste(Tradeliste tr)
	{
		orginalTradeliste_glob=tr;
	}
	
	public int getSize()
	{
		return(resultliste.size());
	}
	
	public OptimizeResult getElem(int i)
	{
		return(resultliste.get(i));
	}
	
	public void add(OptimizeResult os)
	{
		resultliste.add(os);
	}
	
	public PanelResult holeBestPanel()
	{
		Tradeliste tradeliste_best=holeBestTradelist();

		if(tradeliste_best==null)
			return null;
		
		//das jpanel generieren
		ProfitPanel profpan= new ProfitPanel();
		PanelResult pr = new PanelResult();
		JPanel jp=profpan.createDemoPanel(tradeliste_best, "xxxxxxx",pr.getGdx());
		pr.setPanel(jp);
		
		return (pr);
	}

	public PanelResult holePanelResult(int index)
	{
		ProfitPanel profpan= new ProfitPanel();
		Tradeliste tr=holeTradeliste(index);
		
		PanelResult pr= new PanelResult();
		
		
		JPanel pan=profpan.createDemoPanel(tr, String.valueOf(index),pr.getGdx());
		pr.setPanel(pan);
		pr.setGdx(tr.getgdx());
		return pr;
	}
	
	public Tradeliste holeTradeliste(int i)
	{
		//holt die tradeliste mit dem index i
		OptimizeResult or=resultliste.get(i);
		
		//die tradeliste aus dem optimize result berechnen
		Tradeliste tradeliste=orginalTradeliste_glob.getOptimizedTradelist(or);
		tradeliste.setgdx(or.getgdx());
		return tradeliste;
	}
	public OptimizeResult holeOptimizeResult(int i)
	{
		//holt die tradeliste mit dem index i
		OptimizeResult or=resultliste.get(i);
		
		
		return or;
	}
	public OptimizeResult holeBestOptimizeResult()
	{
		OptimizeResult bestoptimize= null;
		OptimizeResult os=null;
		double maxgewinn=0;
		
		int anz=resultliste.size();
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
	
	
	public Tradeliste holeBestTradelist()
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

		Tradeliste tradeliste_best=orginalTradeliste_glob.getOptimizedTradelist(bestoptimize);
		System.out.println("maxgewinn="+bestoptimize.getGewinnsumme()+" maxperiod="+bestoptimize.getgdx());

		tradeliste_best.setgdx(bestoptimize.getgdx());
		
		return (tradeliste_best);
	}
	
	
	
}
