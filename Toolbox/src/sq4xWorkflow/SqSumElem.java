package sq4xWorkflow;

import java.io.File;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqSumElem
{
	// dies element addiert die statistikwerte der gleichen Zeilen(also hier ist
	// immer ein bestimmter workflow drin)
	// eine Zeile ist gleich wenn der "cleanName" gleich ist. Das ist der Name für
	// den jeweiligen workflow
	private SqBaseElem baseelem;
	private int anzvalues;
	// hier wird die gesammtanzahl der trades für einen workflow aufsummiert
	// diese wird aus dem logfiles des jeweiligen workflows ermittelt
	// bsp:user\projects\Q66 GBPUSD H1 imp 1.0_+00000\log
	// einfach hier alle trades aller logfiles aufsummieren und in
	// gesAnzahlTradesPerWorkflow abspeichern
	// das darf aber nur einmal für einen bestimmten clearnamen gemacht werden,
	// sonst sind die trades doppelt drin
	
	
	public SqBaseElem getBaseelem()
	{
		return baseelem;
	}
	
	public void setBaseelem(SqBaseElem baseelem)
	{
		this.baseelem = baseelem;
	}
	
	public int getAnzvalues()
	{
		return anzvalues;
	}
	
	public void setAnzvalues(int anzvalues)
	{
		this.anzvalues = anzvalues;
	}
	
	public void addBaseelem(SqBaseElem be)
	{
		// nimmt einen neuen workflow auf
		// prüfe nach ob überhaupt ein baeelem da ist
		if (baseelem == null)
			baseelem = new SqBaseElem();
		
		baseelem.setNetprofit(baseelem.getNetprofit() + be.getNetprofit());
		baseelem.setProfitfaktor(baseelem.getProfitfaktor() + be.getProfitfaktor());
		baseelem.setRetdd(baseelem.getRetdd() + be.getRetdd());
		baseelem.setStability(baseelem.getStability() + be.getStability());
		baseelem.setStrategyname(be.getCleanName());
		
		// erhöht die anzahl der gespeicherten durchläufe im workflow
		
		anzvalues++;
	}
	
	
	
	public double getNetprofit()
	{
		return ((baseelem.getNetprofit()));
	}
	
	public double getAvrProfitfaktor()
	{
		return ((baseelem.getProfitfaktor()) / anzvalues);
	}
	
	public double getAvrRetdd()
	{
		return ((baseelem.getRetdd()) / anzvalues);
	}
	
	public double getAvrStability()
	{
		return ((baseelem.getStability()) / anzvalues);
	}
	
	
	
}
