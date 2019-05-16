package Metriklibs;

import java.util.ArrayList;

public class Corelresultliste
{
	//diese liste beinhaltet für einen filter die korrelationswerte
	ArrayList<Corelresultelem> clist = new ArrayList<Corelresultelem>();
	
	public void addElem(Corelresultelem ce)
	{
		clist.add(ce);
	}
	public int getSize()
	{
		return(clist.size());
	}
	public Corelresultelem getElem(int index)
	{
		return(clist.get(index));
	}
	public Corelresultelem getElem(String name)
	{
		int anz=clist.size();
		for(int i=0; i<anz; i++)
		{
			Corelresultelem ce=clist.get(i);
			if(ce.getAttribname().equals(name))
				return ce;
		}
		//element nicht gefunden
		return null;
		
	}
}
