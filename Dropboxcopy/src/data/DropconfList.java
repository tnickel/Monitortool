package data;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;

public class DropconfList
{
	

	private ArrayList<DropActor> dropconflist = new ArrayList<DropActor>();

	
	
	public void clear()
	{
		dropconflist.clear();
	}
	public DropconfList()
	{
		preprocess();
	}
	public DropActor getElem(int index)
	{
		return(dropconflist.get(index));
		
	}
	
	public boolean checkElem(String nam)
	{
		int anz=dropconflist.size();
		for(int i=0; i<anz; i++)
		{
			DropActor me=dropconflist.get(i);
			if(me.getTraderexe().equals(nam))
				return true;
			
		}
		return false;
	}
	
	public void addElem(String nam,int type)
	{
		DropActor me= new DropActor();
		me.setActiveflag(1);
		me.setTraderexe(nam);
		me.setType(type);
		me.setValidflag(1);
		if(checkElem(nam)==false)
			dropconflist.add(me);
	}
	public void setValid(String traderexe)
	{
		int anz=dropconflist.size();
		for(int i=0; i<anz; i++)
		{
			DropActor da=dropconflist.get(i);
			if(da.getTraderexe().equals(traderexe))
			{
				da.setValidflag(1);
				return;
			}
		}
	}
	
	public void delElem(DropActor dmef)
	{
		int anz=dropconflist.size();
		for(int i=0; i<anz; i++)
		{
			DropActor da=dropconflist.get(i);
			if(da.getTraderexe().equals(dmef.getTraderexe()))
			{
				dropconflist.remove(i);
				i--;
				anz--;
				return;
			}
		}
		Tracer.WriteTrace(10, "Error internal 4545454");
	}
	public int getSize()
	{
		return(dropconflist.size());
	}
	
	@SuppressWarnings("unchecked")
	private void preprocess()
	{
		// die config einlesen
		String fnam = DropRootpath.getRootpath() + "\\conf\\metatraderconf.xml";

		if (FileAccess.FileAvailable(fnam) == false)
			return;

		Inf inf = new Inf();
		inf.setFilename(fnam);

		// keylist einlesen
		dropconflist = (ArrayList<DropActor>) inf.loadXST();
		inf.close();
	}
	
	public void save()
	{
		// die config abspeichern

		File fnam = new File(DropRootpath.getRootpath() + "\\conf\\metatraderconf.xml");
		File fnamold = new File(DropRootpath.getRootpath() + "\\conf\\metatraderconf.old");
		File fnamtmp = new File(DropRootpath.getRootpath()
				+ "\\conf\\brokerconf.tmp");

		

		// speichere als temp
		Inf inf = new Inf();
		inf.setFilename(fnamtmp.getPath());
		inf.saveXST(dropconflist);
		inf.close();

		//prüfe ob tmp-file >= länge von dem alten file
		if(fnam.exists()&&fnamtmp.exists())
		if(fnamtmp.length()+1000<fnam.length())
		{
			Tracer.WriteTrace(10, "Error: internal length protection brokerconf.xml len1<"+fnamtmp.length()+"> len2<"+fnam.length()+">");
			System.exit(0);
		}
		
		//das alte löschen
		if(fnamold.exists())
			if(fnamold.delete()==false)
				Tracer.WriteTrace(10, "12 Can´t delete file <"+fnamold+">");
		
		//*.xml nach *.old umbenennen
		if(fnam.exists()==true)
			if(fnam.renameTo(fnamold)==false)
				Tracer.WriteTrace(10, "13 Cant rename <"+fnam+">");
		
		
		if(fnamtmp.renameTo(fnam)==false)
			Tracer.WriteTrace(10, "14: cant rename <"+fnamtmp+">");
	}
}
