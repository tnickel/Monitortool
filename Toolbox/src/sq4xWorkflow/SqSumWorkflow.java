package sq4xWorkflow;

import java.util.ArrayList;

public class SqSumWorkflow
{
	//was ist in der Arrayliste gespeichert ?
	//in al ist die Gesammtliste für die Tabelle gepseichert
	//Ein SqSumElem ist ein Summenelement für einen einzelen workflow
	//die al-liste beinhaltet alle workflows
	private ArrayList<SqSumElem> al = new  ArrayList<SqSumElem>();
	
	  
	public SqSumWorkflow()
	{
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
			//dann hole das objekt aus der list
			sume=al.get(index);
			//und addiere das neue element zur hashtable
			sume.addBaseelem(se);
		}
		else
		{
			//new first element
			//dann erzeuge einen neuen sqSum-Eintrag
			sume=new SqSumElem();
			//packe das neue base elem hinzu
			sume.addBaseelem(se);
			//und packe das ganze in die liste
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

	
}
