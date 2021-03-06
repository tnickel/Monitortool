package modtools;

import StartFrame.Brokerview;
import data.Ealiste;
import data.Metaconfig;

public class TradeCopy
{
	// diese Klasse �bernimmt alle belange mit dem Tradecopierer
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
		// 0)l�sche die alten configs Sender und empf�ngerseitig
		selbroker_glob = selbroker;
		realbroker_glob = realbroker;
		bv_glob = bv;
		
		Metaconfig meconf_sel = bv.getMetaconfigByBrokername(selbroker);
		Metaconfig meconf_real = bv.getMetaconfigByBrokername(realbroker);
		meconf_sel_glob = meconf_sel;
		meconf_real_glob = meconf_real;
		
		// erst mal die alten configs f�r den tradechannel l�schen
		Installer inst = new Installer();
		inst.cleanTradecopyConfigs(bv, meconf_sel);
		
	}
	
	public void configProfiles(Brokerview bv, Ealiste eal)
	{
		// hier werden die profiles kopiert und anschliessend gepatched
		Installer inst = new Installer();
		
		// 1)erst werden die *.chr r�berkopiert in die metatrader auf sender und empf�ngerseite
		inst.copyConfigTradecopyConfs(meconf_sel_glob, meconf_real_glob);
		
		// 2)hier wird am demobroker gepatched
		// 2.1 suche erst mal das profile
		Profiler profDemo = new Profiler(meconf_sel_glob);
		String profilenameDemo = profDemo.getProfileEaChannel("FX Blue - TradeCopy Sender", meconf_sel_glob.getBrokername());
		
		// 2.2 dann patche das sender profile beim demobroker
		ChrFile chrdemo = new ChrFile();
		chrdemo.setFilename(profilenameDemo);
		chrdemo.patchTradecopyMagics(selbroker_glob, eal);
		chrdemo.patchTradecopyChannel(selbroker_glob);
		chrdemo.patchCurPair(meconf_sel_glob.getHistexportcurrency());
		chrdemo.writeMemFile(null);
		
		// 3)patchen am realborker config
		// 3.1 suche erst mal das empfanger profile des realaccount
		Profiler profReal = new Profiler(meconf_real_glob);
		String profilenameReal = profReal.getProfileEaChannel("FX Blue - TradeCopy Receiver",
				meconf_sel_glob.getBrokername());
		
		// 3.2 dann patche das profile
		ChrFile chrreal = new ChrFile();
		chrreal.setFilename(profilenameReal);
		chrreal.patchTradecopyChannel(selbroker_glob);
		chrreal.patchReceiverMagic(meconf_sel_glob.getTradecopymagic());
		chrreal.patchCurPair(meconf_real_glob.getHistexportcurrency());
		chrreal.patchSuffix(meconf_real_glob.getSuffix());
		chrreal.writeMemFile(null);
		
	}
}
