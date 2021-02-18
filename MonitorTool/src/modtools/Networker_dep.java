package modtools;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;

import java.io.File;

import mtools.Mlist;
import StartFrame.Brokerview;
import data.EaStatus;
import data.Metaconfig;

public class Networker_dep
{
	//wird nicht mehr benötigt da das ein ausschalten jetzt anderes gelöst wird
	//das einausschalten wird jetzt mit Tradecopy gemacht
	
	//der Networker wird mit einer Semaphore geschützt
	public Networker_dep()
	{
	}

	public EaStatus getEaStatus(Brokerview brokerview, int magic, String broker)
	{
		//ermittelt den ea status für den sync
		//man möchte wissen ob der ea auf dem realaccount installiert ist und 
		//ob dieser auch eingeschaltet ist

		Metaconfig mc = brokerview.getMetaconfigByBrokername(broker);

		//den broker gibt es nicht mehr
		if(mc==null)
			return null;
		
		String realbroker = mc.getconnectedBroker();
		
		//falls dies ein Realbroker ist dann ist ja nix connected
		//also haben wir schon den connected broker
		if(mc.getAccounttype()==2)
			realbroker=broker;
	
		//falls der Monitor/demobroker hat keinen verknüpften realbroker also ist auch nix installiert
		if((realbroker==null)||(realbroker.length()<2))
		{
			//der Monitor/demobroker hat keinen verknüpften realbroker also ist auch nix installiert
			EaStatus eastat= new EaStatus();
			eastat.setInstalliert(0);
			eastat.setOn(0);
			return eastat;
		}
			
		// holt sich das share des realbrokers
		String mqldata = brokerview.getMqlData(realbroker);

	
		
		boolean on=false, off=false, onok=false, offok=false;
		
		//stati
		String fnam_on = mqldata + "\\files\\" + magic + ".on";
		String fnam_onok = mqldata + "\\files\\" + magic + ".on.ok";
		String fnam_off = mqldata + "\\files\\" + magic + ".off";
		String fnam_offok = mqldata + "\\files\\" + magic + ".off.ok";
		
		if((new File(fnam_on).exists())==true)
			on=true;
		if((new File(fnam_onok).exists())==true)
			onok=true;
		if((new File(fnam_off).exists())==true)
			off=true;
		if((new File(fnam_offok).exists())==true)
			offok=true;
		
		EaStatus eastat= new EaStatus();
		
		if((on==true)&& (onok==true))
			eastat.setOn(1);
		if((off==true)&& (offok==true))
			eastat.setOn(0);
		
		//fehlzustand Ea läuft nicht
		if(((on==true)&& (onok==false))||((off==true)&& (offok==false)))
		{
			Mlist.add("problem with EA magic<"+magic+"> broker<"+broker+">");
			eastat.setOn(-1);
		}
		

		if((on==true)||(onok==true)||(off==true)||(offok==true))
			eastat.setInstalliert(1);
		else
			eastat.setInstalliert(0);
		
		return eastat;
	}
	
	public int RealBroSwitchOnEa(Brokerview brokerview, int magic, String broker)
	{
		
		
		//Semaphore on
		int ret=schalter(brokerview,magic,broker,".on",".off");
		//Semaphore off
		
		return ret;
	}

	public int RealBroSwitchOffEa(Brokerview brokerview, int magic, String broker)
	{
		//0: ok
		//1: fehler
		
		//man kann nur auf dem realbroker schalten
		
		//Semaphore on
		int ret=schalter(brokerview,magic,broker,".off",".on");
		//Semaphore off
		return ret;
	}

	private int schalter(Brokerview brokerview, int magic, String realbroker,
			String newaktion,String delaktion)
	{
		//ret0: ok
		//ret1: fehler
		
		
		
		// holt sich das share des realbrokers
		
		String mqldata = brokerview.getMqlData(realbroker);

	
		
		if(mqldata==null)
		{
			Tracer.WriteTrace(20, "realshare=null");
			Mlist.add("no connected demobroker  realbroker<"+realbroker+"> magic<"+magic+">  set instfrom !!");
			return 55;
		}
		//erst das alte löschen
		String fnamdel = mqldata + "\\files\\" + magic + delaktion;
		if(FileAccess.FileAvailable0(fnamdel))
			FileAccess.FileDelete(fnamdel, 1);
		
		//den neuen schalter einbringen
		String fnamnew = mqldata + "\\files\\" + magic + newaktion;
		Inf inf = new Inf();
		inf.setFilename(fnamnew);
		inf.writezeile("Generiert am"+Tools.get_aktdatetime_str());
		inf.close();
		
		//überprüfung
		if(FileAccess.FileAvailable(fnamnew)==false)
		{
			Mlist.add("ERROR SwitchOff Automatic not Working !!");
			return 10;
		}
		//alles ok
		return 0;
	}
	public void genNewMetatraderaccount(String mtroot)
	{
		System.out.println("mtroot="+mtroot);
		String fnam=mtroot+"\\experts\\files\\closeAccount.start";
		
		Inf inf=new Inf();
		inf.setFilename(fnam);
		inf.writezeile(Tools.get_aktdatetime_str());
		inf.close();
		
		int count=0;
		while(count<5)
		{
			
			File fn=new File(mtroot+"\\experts\\files\\closeAccount.ok");
			if(fn.exists())
				break;
			
			count++;
			if(count >5)
			{
				Mbox.Infobox("Close Account takes to long time");
				break;
			}
		}
		
	}
}
