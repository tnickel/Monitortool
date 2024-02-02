package sq4xWorkflow;

import java.io.File;
import java.util.ArrayList;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.TextProzessor;
import hiflsklasse.Tracer;

public class SqSumWorkflow
{
	//was ist in der Arrayliste gespeichert ?
	//in al ist die Gesammtliste für die Tabelle gespeichert
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
	public int calcAnzTradesAusLogfile(String cleanname,String portfolioname)
	{
		String workflowdir=sqrootdir_g+"\\user\\projects\\"+cleanname.replace("\"", "");
		int anz=collectAllTradesFromLogfiles(workflowdir,portfolioname);
		return anz;
	}
	private int collectAllTradesFromLogfiles(String workflowdir,String portfolioname)
	{
		//zählt wieviel strategien generiert wurden
		//diese informationen werden aus den logfiles bsp:
		//C:\\forex\\toolbox\\SQ\\1 Master\\user\\projects\\Q67 GBPUSD H1_all_blocks\\log
		//geholt
		//es werden alle trades für einen bestimmten workflow aufsummiert
		//ein workflow kann ja mehrmals durchlaufen werden
		int stratcount_newest=0;
		long lastmodified_newest=0;
		
		int scount=0;
		FileAccess.initFileSystemList(workflowdir+"\\log", 1);
		int anz = FileAccess.holeFileAnz();
		if (anz > 0)
		{	
			for (int i = 0; i < anz; i++)
			{
				String fnam=workflowdir+"\\log\\"+FileAccess.holeFileSystemName();
				File fnam_f=new File(fnam);
				if(fnam.contains(".log"))
				{
					Inf inf=new Inf();
					inf.setFilename(fnam);
					
					scount=extractAnzStrategien( fnam,portfolioname);
					long lmodified=fnam_f.lastModified();
					if(scount>0)
					if(lmodified>lastmodified_newest)
					{
						//found newer file, than take the dater
						lastmodified_newest=lmodified;
						stratcount_newest=scount;
					}
					
					inf.close();
				}
			}
			return stratcount_newest;
		}
		Tracer.WriteTrace(20, "I: no logfiles in <" + workflowdir+"\\log" + ">");
		return 0;
	}
	private int extractAnzStrategien(String fnam,String portfolioname)
	{
		// Wir wollen wissen wieviele Strategien sich denn im Portfolio befinden
		// Hierzu müssen wir im Logfile die Stelle suchen wo das Portfolio generiert wird.
	
		//this text will be searched
		//'Portfolio created from 47 strategies'
		//Portfolio created from 2 strategies from source databank 'Endtest4M' saved to target databank 'Portfolio4M'.
	
		
		//check 3 keywords we need
		//if we don´t have it all we set #anz strategies to 0

		TextProzessor tp=new TextProzessor(fnam);
		//es wird die Zeile gesucht die die 3 keywörter enthält
		String kw1="Portfolio created from ";
		String kw2=" strategies";
		String kw3="saved to target databank '"+portfolioname.toLowerCase()+"'";
		
		String line=tp.getLineWithWords(kw1,kw2,kw3);
		
		
		if(line==null)
		{
			Tracer.WriteTrace(20,"I:cant find string ' strategies' after string 'Portfolio created from' in <"+fnam+"> line<"+line+"> kw1<"+kw1+"> kw2<"+kw2+"> kw3<"+kw3+">");
			return 0;//no strategies in portfolio
		}
		
		String memsub=line.substring(line.indexOf("Portfolio created from ")+23);
		
		
		if((memsub==null)||(memsub.contains(" strategies")==false))
		{
			Tracer.WriteTrace(20, "I:cant find string ' strategies' after string 'Portfolio created from' in <"+fnam+">" );
			return 0;
		}
		memsub=memsub.substring(0,memsub.indexOf(" strategies"));
		int val=Integer.valueOf(memsub);
		return val;
	}
}
