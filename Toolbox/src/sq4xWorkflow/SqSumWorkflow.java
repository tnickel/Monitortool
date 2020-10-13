package sq4xWorkflow;

import java.util.ArrayList;

public class SqSumWorkflow
{
	
	private ArrayList<SqSumElem> al = new  ArrayList<SqSumElem>();
	  
	public SqSumWorkflow()
	{
	}
	
	public int getSize()
	{
		return al.size();
	}
	private int getIndex(String nam)
	{
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
			//und packe das ganze in die hashtable
			al.add(sume);
		}
	}

	public SqSumElem getElem(int index)
	{
		return al.get(index);
	}
	
	
	
	public void showList()
	//liste ausgeben
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
