package sq4xWorkflow;

import java.util.ArrayList;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqSumWorkflow
{
	//was ist in der Arrayliste gespeichert ?
	//in al ist die Gesammtliste für die Tabelle gepseichert
	//Ein SqSumElem ist ein Summenelement für einen einzelen workflow
	//die al-liste beinhaltet alle workflows
	private ArrayList<SqSumElem> al = new  ArrayList<SqSumElem>();
	private String sqrootdir_g;
	  
	public SqSumWorkflow()
	{
	}
	
	public void setSqRootdir(String sqrootdir)
	{
		sqrootdir_g=sqrootdir;
	}
	
	public int getSize()
	{
		//liefert die anzahl der verschiedenen workflows zurück
		return al.size();
	}
	private int getIndex(String nam)
	{
		//der index für einen bestimmten workflow wird ermittelt
		if(al==null)
			return -1;
		int anz=al.size();
		for(int i=0; i<anz; i++)
		{
			SqSumElem se=al.get(i);
			//cleannam is in list
			if(se.getBaseelem().getCleanName().equals(nam))
				return i;
		}
		return -1;
	}
	
	
	public void add(SqBaseElem se)
	{
		//ein neuer Workflow wird aufgenommen falls noch nicht vorhanden
		//wenn vorhanden dann werden die statistikwerte addiert
		SqSumElem sume=null;
		
		// add a new SqBaseElem to sumlist
		String nam=se.getCleanName();

		//falls der sumlist der cleanname schon bekannt ist
		int index=getIndex(nam);
		if(index>-1)
		{
			//falls für den Workflow schon das element drin ist
			//dann hole das objekt aus der list
			sume=al.get(index);
			//und addiere das neue element zur hashtable
			sume.addBaseelem(se);
		}
		else
		{
			//es ist noch kein element für den workflow erzeugt worden, dann mache das
			//bestimme aber auch gleichzeitig die anz der gesammttrades für den workflow
			//diese gesammtanzahl der trades kann man aus den logfiles extrahieren.
			//hier wird für das erste element einfach mal aus allen logfiles die tradeanzahl ermittelt
			//new first element
			//dann erzeuge einen neuen sqSum-Eintrag
			sume=new SqSumElem();
			//packe das neue base elem hinzu
			sume.addBaseelem(se);
			
			al.add(sume);
			
		}
	}

	public SqSumElem getElem(int index)
	{
		//holt einen bestimmten workflow aus der liste
		return al.get(index);
	}
	
	
	
	public void showList()
	//gibt die liste aller workflows aus
	{
		int anz=al.size();
		if(anz==0)
			return;
		
		for(int i=0; i<anz; i++)
		{
        	
			System.out.println("Name:  " + al.get(i).getBaseelem().getCleanName() );
		}
		
	}
	public int calcAnzTradesAusLogfile(String cleanname)
	{
		String workflowdir=sqrootdir_g+"\\user\\projects\\"+cleanname.replace("\"", "");
		int anz=collectAllTradesFromLogfiles(workflowdir);
		return anz;
	}
	private int collectAllTradesFromLogfiles(String workflowdir)
	{
		//zählt wieviel strategien generiert wurden
		//diese informationen werden aus den logfiles bsp:
		//C:\\forex\\toolbox\\SQ\\1 Master\\user\\projects\\Q67 GBPUSD H1_all_blocks\\log
		//geholt
		//es werden alle trades für einen bestimmten workflow aufsummiert
		//ein workflow kann ja mehrmals durchlaufen werden
		int stratcount=0;
		FileAccess.initFileSystemList(workflowdir+"\\log", 1);
		int anz = FileAccess.holeFileAnz();
		if (anz > 0)
		{	
			for (int i = 0; i < anz; i++)
			{
				String fnam=workflowdir+"\\log\\"+FileAccess.holeFileSystemName();
				if(fnam.contains(".log"))
				{
					Inf inf=new Inf();
					inf.setFilename(fnam);
					String mem=inf.readMemFile(50000);
					stratcount=stratcount+extractAnzStrategien( mem, fnam);
					inf.close();
				}
			}
			return stratcount;
		}
		Tracer.WriteTrace(20, "I: no logfiles in <" + workflowdir+"\\log" + ">");
		return 0;
	}
	private int extractAnzStrategien(String mem,String fnam)
	{
		// Wir wollen wissen wieviele Strategien sich denn im Portfolio befinden
		// Hierzu müssen wir im Logfile die Stelle suchen wo das Portfolio generiert wird.
		
		
		
		//this text will be searched
		//'Portfolio created from 47 strategies'
	
		
		//Portfolio created from 9 strategies from source databank 'Fertig' saved to target databank 'Portfolio'.
		
		if((mem==null)||(mem.contains("Portfolio created from ")==false))
		{
			Tracer.WriteTrace(20, "W:cant find string 'Portfolio created from' in <"+fnam+">" );
			return 0;
		}
		String memsub=mem.substring(mem.indexOf("Portfolio created from ")+23);
		
		
		if((memsub==null)||(memsub.contains(" strategies")==false))
		{
			Tracer.WriteTrace(20, "W:cant find string ' strategies' after string 'Portfolio created from' in <"+fnam+">" );
			return 0;
		}
		memsub=memsub.substring(0,memsub.indexOf(" strategies"));
		int val=Integer.valueOf(memsub);
		return val;
	}
}
