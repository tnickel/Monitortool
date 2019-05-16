package Sync;

import hiflsklasse.Inf;

import java.io.File;

import data.Rootpath;

public class LockTradeliste
{
	//klasse dient zum synchronisieren von monitortool und dem update process
	public void delTradelock()
	{
		//einen alten sync löschen
		File syncfile = new File(Rootpath.getRootpath() + "\\data\\tra.locked");
		if(syncfile.exists()==true)
			syncfile.delete();
	}
	public void lockTradeliste()
	{
		String syncfile= Rootpath.getRootpath()+"\\data\\tra.locked";
		Inf inf= new Inf();
		inf.setFilename(syncfile);
		inf.writezeile("sync");
		inf.close();
	}
	
	
	public void waitTradelisteReady()
	{
		File locktrade = new File(Rootpath.getRootpath() + "\\data\\tra.locked");
		File tradeliste = new File(Rootpath.getRootpath()+"\\data\\tra.txt.zip.cr");
		
		//falls gelocked warte
		while (locktrade.exists()==true)
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//solange gelocked warte
			if(locktrade.exists()==true)
				continue;
		}

		//warte bis die tradliste da ist
		while (tradeliste.exists()==false)
		{
			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//solange gelocked warte
			if(tradeliste.exists()==false)
				continue;
		}
	
	}
}
