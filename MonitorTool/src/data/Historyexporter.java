package data;

import java.io.File;
import java.util.ArrayList;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class Historyexporter
{
	String zeile=null;
	
	String fnam_glob=null;
	ArrayList<String> hlist=new ArrayList<String>();
	public Historyexporter(String fnam)
	{
		fnam_glob=fnam;
		//list die history vom historyexporter ein
		Inf inf=new Inf();
		inf.setFilename(fnam);

		while( (zeile=inf.readZeile())!=null)
		{
			hlist.add(zeile);
		}
		if(hlist!=null)
		Tracer.WriteTrace(20, "I:<"+fnam_glob+"> with n=<"+hlist.size()+"> rows read");
	}
	public void storeHistoryTxt()
	{
		String fnam=fnam_glob+".tmp";
		Inf inf=new Inf();
		inf.setFilename(fnam);
		int anz=hlist.size();
		for(int i=0; i<anz; i++)
		{
			inf.writezeile(hlist.get(i));
		}
		inf.close();
		
		File fnamf=new File(fnam_glob);
		if(fnamf.exists())
			if(fnamf.delete()==false)
				Tracer.WriteTrace(10, "I:can´t delete file <"+fnamf.getAbsolutePath()+">");
		if(FileAccess.copyFile(fnam, fnam.replace(".tmp", ""))==false)
			Tracer.WriteTrace(10, "E:cant copy file from<"+fnam+"> to<"+fnam.replace(".tmp", "")+"> ");
		if((new File(fnam).delete())==false)
			Tracer.WriteTrace(10, "E:cant del file <"+fnam+"> -> stop");
	}
	public void deleteEa(String magic)
	{
		int anz=hlist.size();
		for(int i=0; i<anz; i++)
		{
			String zeile=hlist.get(i);
			if(zeile.startsWith(magic)==true)
			{	
				hlist.remove(i);
				anz--;
				i--;
			}
		}
		return;
	}
	
}
