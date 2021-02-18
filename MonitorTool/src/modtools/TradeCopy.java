package modtools;

import StartFrame.Brokerview;
import data.Ealiste;
import data.Metaconfig;

public class TradeCopy
{
	//diese Klasse übernimmt alle belange mit dem Tradecopierer
	//hier wird der tradecopierer konfiguriert
	String selbroker_glob=null;
	String realbroker_glob=null;
	Metaconfig meconf_sel_glob=null;
	Metaconfig meconf_real_glob=null;
	Brokerview bv_glob=null;
	
	
	
	
	public TradeCopy()
	{}
	public void init(String selbroker,String realbroker,Brokerview bv)
	{
		selbroker_glob=selbroker;
		realbroker_glob=realbroker;
		bv_glob=bv;
		//hier werden die configs erstellt
		Metaconfig meconf_sel=bv.getMetaconfigByBrokername(selbroker);
		Metaconfig meconf_real=bv.getMetaconfigByBrokername(realbroker);
		meconf_sel_glob=meconf_sel;
		meconf_real_glob=meconf_real;
		
		Installer inst=new Installer();
		inst.copyTradecopyConf(selbroker,meconf_sel);
		inst.copyTradecopyConf(realbroker,meconf_real);
	}
	public void configProfiles(Ealiste eal)
	{
		//hier wird am demobroker gepatched
		ChrFile chrdemo=new ChrFile();
		chrdemo.setFilename(meconf_sel_glob.getNetworkshare_INSTALLDIR() + "\\profiles\\default\\TradeCopySender_"+selbroker_glob+".chr");
		chrdemo.patchTradecopyMagics(selbroker_glob,eal);
		chrdemo.patchTradecopyChannel(selbroker_glob);
		chrdemo.writeMemFile(null);
		//patchen am realborker config
		ChrFile chrreal=new ChrFile();
		chrreal.setFilename(meconf_real_glob.getNetworkshare_INSTALLDIR() + "\\profiles\\default\\TradeCopyReceiver_"+realbroker_glob+".chr");
		chrdemo.patchTradecopyChannel(selbroker_glob);
		chrdemo.writeMemFile(null);
		
	}
}
