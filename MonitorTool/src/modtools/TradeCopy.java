package modtools;

import StartFrame.Brokerview;
import data.Ealiste;
import data.Metaconfig;
import data.Tradeliste;
import hiflsklasse.Tracer;

public class TradeCopy
{
	// diese Klasse übernimmt alle belange mit dem Tradecopierer
	// hier wird der tradecopierer konfiguriert
	String selbroker_glob = null;
	String realbroker_glob = null;
	Metaconfig meconf_sel_glob = null;
	Metaconfig meconf_real_glob = null;
	Brokerview bv_glob = null;
	
	public TradeCopy()
	{
	}
	
	public void init(String selbroker, String realbroker, Brokerview bv)
	{
		if (realbroker == null)
		{
			Tracer.WriteTrace(10, "E:Error this broker<" + selbroker
					+ "> dont have a connected realbroker, set realbroker in Edit Broker first");
			return;
		}
		
		// 0)lösche die alten configs Sender und empfängerseitig
		selbroker_glob = selbroker;
		realbroker_glob = realbroker;
		bv_glob = bv;
		
		Metaconfig meconf_sel = bv.getMetaconfigByBrokername(selbroker);
		Metaconfig meconf_real = bv.getMetaconfigByBrokername(realbroker);
		meconf_sel_glob = meconf_sel;
		meconf_real_glob = meconf_real;
		
		// erst mal die alten configs für den tradechannel löschen
		Installer inst = new Installer();
		inst.cleanTradecopyConfigs(bv, meconf_sel);
		
	}
	
	public void configProfiles(Brokerview bv, Ealiste eal,Tradeliste tl)
	{
		// hier werden die profiles kopiert und anschliessend gepatched
		Installer inst = new Installer();
		
		// 1)erst werden die *.chr rüberkopiert in die metatrader auf sender und
		// empfängerseite
		inst.copyConfigTradecopyConfs(meconf_sel_glob, meconf_real_glob);
		
		// 2)hier wird am demobroker gepatched
		// 2.1 suche erst mal das profile
		Profiler profDemo = new Profiler(meconf_sel_glob);
		String profilenameDemo = profDemo.getProfileEaChannel("FX Blue - TradeCopy Sender",
				meconf_sel_glob.getBrokername());
		
		// 2.2 dann patche das sender profile beim demobroker
		ChrFile chrdemo = new ChrFile();
		chrdemo.setFilename(profilenameDemo);
		
		int accounttype=bv.getAccounttype(meconf_sel_glob.getBrokername());
		if(accounttype==1)
    		chrdemo.patchTradecopyMagics(selbroker_glob,eal);
		else if(accounttype==4)
			chrdemo.patchTradecopyMagicsAC(selbroker_glob, eal);
		else
			Tracer.WriteTrace(10, "Error config profiles: can´t patch tradechannel for accounttype<"+accounttype+">");
		
		
		chrdemo.patchCurPair(meconf_sel_glob.getHistexportcurrency());
		chrdemo.writeMemFile(null);
		
		// 3)patchen am realborker config
		// 3.1 suche erst mal das empfanger profile des realaccount
		Profiler profReal = new Profiler(meconf_real_glob);
		String profilenameReal = profReal.getProfileEaChannel("FX Blue - TradeCopy Receiver",
				meconf_sel_glob.getBrokername());
		
		// 3.2 dann patche das profile des realchannel
		ChrFile chrreal = new ChrFile();
		chrreal.setFilename(profilenameReal);
		chrreal.patchTradecopyChannel(selbroker_glob);
		chrreal.patchReceiverMagic(meconf_sel_glob.getTradecopymagic());
		chrreal.patchCurPair(meconf_real_glob.getHistexportcurrency());
		chrreal.patchSuffix(meconf_real_glob.getSuffix());
		chrreal.writeMemFile(null);
	}
	
	
	
	
	public boolean checkdoubleChannel(String realbroker, Brokerview brokerview, int maxchannel)
	{
		Metaconfig meconfreal = brokerview.getMetaconfigByBrokername(realbroker);
		Profiler profreal = new Profiler(meconfreal);
		
		for (int i = 1; i < maxchannel; i++)
		{
			int anzchannel = profreal.countEaChannel(String.valueOf(i));
			if(anzchannel==1)
				Tracer.WriteTrace(20, "Realbroker<"+realbroker+"> have channel<"+i+">");
			if (anzchannel > 1)
			{
				Tracer.WriteTrace(10, "E:consistency error realbroker<" + realbroker + "> have #<" + anzchannel
						+ "> channel for id<" + i + "> I will delete the double channel <"+i+">");
				profreal.delDoubleChannel(String.valueOf(i));
			}
		}
		return true;
		
	}
	
}
