package data;

import java.io.File;
import java.util.ArrayList;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class Historyexporter
{
	String zeile=null;
	
	String historyexportername_glob=null;
	//hlist ist die history vom historyexporter
	ArrayList<String> hlist=new ArrayList<String>();
	public Historyexporter(String fnam)
	{
		
		historyexportername_glob=fnam;
		//list die history vom historyexporter ein
		Inf inf=new Inf();
		inf.setFilename(fnam);

		while( (zeile=inf.readZeile())!=null)
			hlist.add(zeile);
		
		if(hlist!=null)
		Tracer.WriteTrace(20, "I:<"+historyexportername_glob+"> with n=<"+hlist.size()+"> rows read");
	}
	public void storeHistoryTxt()
	{
		//historyexporter.txt wird erst mal als *.tmp gespeichert 
		String histexporter_tmp=historyexportername_glob+".tmp";
		Inf inf=new Inf();
		inf.setFilename(histexporter_tmp);

		//schreibe die einzelnen Zeilen des historyexporter.txt.tmp
		int anz=hlist.size();
		for(int i=0; i<anz; i++)
			inf.writezeile(hlist.get(i));
		inf.close();
		
		//dann den alten historyexporter.txt löschen
		File fnamf=new File(historyexportername_glob);
		if(fnamf.exists())
			if(fnamf.delete()==false)
				Tracer.WriteTrace(10, "I:storeHistoryTxt can´t delete file <"+fnamf.getAbsolutePath()+">");

		//*.txt.tmp nach *.txt umkopieren
		if(FileAccess.copyFile(histexporter_tmp, histexporter_tmp.replace(".tmp", ""))==false)
			Tracer.WriteTrace(10, "E:storeHistoryTxt cant copy file from<"+histexporter_tmp+"> to<"+histexporter_tmp.replace(".tmp", "")+"> ");

		//*.txt.tmp löschen
		if((new File(histexporter_tmp).delete())==false)
			Tracer.WriteTrace(10, "E:storeHistoryTxt cant del file <"+histexporter_tmp+"> -> stop");
	}
	public void deleteEa(String magic)
	{
		//den ea aus history.txt löschen
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
	public void deleteEaMagicComment(String magic,String comment)
	{
		//den ea aus history.txt löschen
		int anz=hlist.size();
		for(int i=0; i<anz; i++)
		{
			String zeile=hlist.get(i);
			if((zeile.startsWith(magic)==true)&&(zeile.contains(comment)))
			{	
				hlist.remove(i);
				anz--;
				i--;
			}
		}
		return;
	}
}
