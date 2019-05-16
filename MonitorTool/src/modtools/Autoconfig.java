package modtools;

import gui.Mbox;
import hiflsklasse.Tracer;

import java.io.File;

import swtHilfsfenster.SwtEditBrokerConfigWork;
import Metriklibs.FileAccessDyn;
import StartFrame.Brokerview;
import data.GlobalVar;
import data.Metaconfig;

public class Autoconfig
{
	public Autoconfig()
	{}
	public void checkDoppelteEas(Brokerview brokerview_glob)
	{
		//hier muss noch überprüft werden ob einige EAs doppelt auf dem Metatrader installiert sind
		
		
	}
	public void configAllMetatrader(Brokerview brokerview)
	{
		//geht durch alle Metatrader und ergänzt die Konfiguration
		String metatraderroot = GlobalVar.getNetzwerkshareprefix();

		//alle Broker einladen
		brokerview.LoadBrokerTable();
		
		if((metatraderroot==null)||(metatraderroot.length()<2))
		{
			Mbox.Infobox("please set Metatrader root in config");
			return;
		}
		
		Mbox.Infobox("please stop all metatrader I will do automatic configuration");
		
		FileAccessDyn fadyn = new FileAccessDyn();
		fadyn.initFileSystemList(metatraderroot, 0);
		int anz=fadyn.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String metapath=metatraderroot+"\\"+fadyn.holeFileSystemName();
			File terminalexe=new File(metapath+"\\terminal.exe");
		   if(terminalexe.exists())
			{
				//prüft nach ob das terminal.exe schon konfiguriert ist
				if(checkBrokerIsConfigured(brokerview,metapath)==true)
					autoupdateBrokerconfig(brokerview,metapath);
				else
					//fuegt einen neuen broker ein
					addNewBrokerconfig(brokerview,metapath);
			}
		}
		Mbox.Infobox("automatic configuration ready, please klick (reload/show-AllData)-button and restart metatrader");
	}
	private Metaconfig holeMetaconfig(Brokerview brokerview,String brokerpath)
	{
		int anz=brokerview.getAnz();
		for(int i=0; i<anz; i++)
		{
			Metaconfig meconf= brokerview.getElem(i);
			if((meconf.getNetworkshare_INSTALLDIR().equalsIgnoreCase(brokerpath))==true)
				return meconf;
		}
		return null;
	}
	private boolean checkBrokerIsConfigured(Brokerview brokerview,String brokerpath)
	{
		//prüft nach ob der Broker schon konfiguiert ist
		int anz=brokerview.getAnz();
		for(int i=0; i<anz; i++)
		{
			Metaconfig meconf= brokerview.getElem(i);
			if((meconf.getNetworkshare_INSTALLDIR().equalsIgnoreCase(brokerpath))==true)
				return true;
		}
		return false;
	}
	private void addNewBrokerconfig(Brokerview brokerview,String brokerpath)
	{
		Tracer.WriteTrace(20, "I: add new broker <"+brokerpath+"> by by autoconfig");
		
		//den Broker aufnehmen
		Metaconfig me = new Metaconfig("##0######0##");
	
		//das installationsverzeichniss setzen
		me.setNetworkshare(brokerpath);
	
		//den brokernamen setzen
		me.setBrokername(brokerpath.substring(brokerpath.lastIndexOf("\\")+1));
		
		//lotsize setzen
		me.setLotsize(0.01);
	
		//autoconf info setzen
		me.setInfostring("gen by autoconf");
	
		//den type setzen (Monitor)
		me.setAccounttype(0);
	
		//das mql-verzeichniss setzen
		me.getInitMetaversion();
	
		//den metatrader initialisieren
		SwtEditBrokerConfigWork swork= new SwtEditBrokerConfigWork();
		
		swork.initMetatrader(me);
		
		//die Config aufnehmen
		brokerview.addElem(me);
		
		//alles speichern
		brokerview.SaveBrokerTable();
	}
	private void autoupdateBrokerconfig(Brokerview brokerview,String brokerpath)
	{
		Tracer.WriteTrace(20, "I: autoupdate broker <"+brokerpath+"> by by autoconfig");
		
		//den Broker holen
		Metaconfig me=holeMetaconfig( brokerview,brokerpath);
	
		if(me==null)
		{
			Tracer.WriteTrace(10, "I:Broker<"+brokerpath+"> not found, can´t do autoconfigst");
			return;
		}
		
		//das installationsverzeichniss setzen
		me.setNetworkshare(brokerpath);
	
		//das mql-verzeichniss setzen
		me.getInitMetaversion();
	
		//alles speichern
		brokerview.SaveBrokerTable();
	}
}
