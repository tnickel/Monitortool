package Metriklibs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import hiflsklasse.Tracer;

public class StrategieMengen
{
	// diese klasse liest alle strategienamen ein und kann daraus Strategiemengen
	// bauen
	// diese Klasse wird verwendet um nur ein bestimmten Teil der Strategien zu
	// selektieren.
	private ArrayList<Metrikzeile> metriktab_glob = null;
	
	private HashSet<String> crowd0 = new HashSet<String>();
	private HashSet<String> crowd1 = new HashSet<String>();
	private HashSet<String> crowd2 = new HashSet<String>();
	private HashSet<String> crowd3 = new HashSet<String>();
	private HashSet<String> crowd4 = new HashSet<String>();
	private HashSet<String> crowd5 = new HashSet<String>();
	int mode = 0; // 0=is, 1=oss
	
	public String getInfo()
	{
		String inf="c0<"+crowd0.size()+"> c1<"+crowd1.size()+"> c2<"+crowd2.size()+"> c3<"+crowd3.size()+"> c4<"+crowd4.size()+"> c5<"+crowd5.size()+">";
		return inf;
	}
	
	public StrategieMengen(String rpath,int percent,int fixedseedflag)
	{
		crowd0.clear();
		crowd1.clear();
		crowd2.clear();
		crowd3.clear();
		crowd4.clear();
		crowd5.clear();
		
		// wir brauchen hier im StrategienSelector die Stratgienamen, die holen wir aus
		// der letzten datenbank raus
		// Dann bauen wir mehrere Mengen auf
		
		// wir lesen die letzte Tabelle ein, wir brauchen ja nur die namen der
		// strategien
		metriktab_glob = DatabankExportTable.leseMetriktabelleSimple(rpath + "\\_99_dir\\databankExport.csv");
		Random rand=null;
		
		if(fixedseedflag==1)
			rand = new Random(123456);
		else
			rand=new Random();
		
		int restpercent=100-percent;
		int deltapercent=restpercent/5;
		int schranke5=100-(deltapercent*1);
		int schranke4=100-(deltapercent*2);
		int schranke3=100-(deltapercent*3);
		int schranke2=100-(deltapercent*4);
		
		
		int anz = metriktab_glob.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikzeile mez = metriktab_glob.get(i);
			String nam = mez.holeStratName();
			int ranval = rand.nextInt(100) + 1;
			
			if (ranval <= percent)
			{
				// speichere in IS
				crowd0.add(nam);
			} else if (ranval > schranke5)
			{
				// speicher OOS
				crowd5.add(nam);
			} else if (ranval > schranke4)
			{
				// speicher OOS
				crowd4.add(nam);
			} else if (ranval > schranke3)
			{
				// speicher OOS
				crowd3.add(nam);
			} else if (ranval > schranke2)
			{
				// speicher OOS
				crowd2.add(nam);
			} else // 50<x<60
			{
				// speicher OOS
				crowd1.add(nam);
			}
		}
	}
	
	public int getMode_dep()
	{
		return mode;
	}
	
	public void setPool(int mode)
	{
		this.mode = mode;
	}
	
	public int getPool()
	{
		return mode;
	}
	
	public int getAktPoolAnzStrategies()
	{
		if(mode==0)
			return(crowd0.size());
		else if(mode==1)
			return(crowd1.size());
		else if(mode==2)
			return(crowd2.size());
		else if(mode==3)
			return(crowd3.size());
		else if(mode==4)
			return(crowd4.size());
		else if(mode==5)
			return(crowd5.size());
		else
			Tracer.WriteTrace(10, "E:unknown mode<"+mode+">");
		return -1;
		
	}
	
	public boolean containsName(String name)
	{
		if (mode == 0)
			return (crowd0.contains(name));
		else if (mode == 1)
			return (crowd1.contains(name));
		else if (mode == 2)
			return (crowd2.contains(name));
		else if (mode == 3)
			return (crowd3.contains(name));
		else if (mode == 4)
			return (crowd4.contains(name));
		else if (mode == 5)
			return (crowd5.contains(name));
		else
			Tracer.WriteTrace(10, "E:unknown mode");
		return false;
	}
	
}
