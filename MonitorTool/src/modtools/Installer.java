package modtools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;

import FileTools.Filefunkt;
import Metriklibs.FileAccessDyn;
import StartFrame.Brokerview;
import StartFrame.Tableview;
import backtest.Mt4Backtester;
import data.Ea;
import data.Ealiste;
import data.GlobalVar;
import data.Metaconfig;
import data.Profit;
import data.Rootpath;
import datefunkt.Mondate;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;
import mqlLibs.Eaclass;
import mtools.DisTool;
import mtools.MboxQuest;
import mtools.Mlist;

public class Installer
{
	// die Installerklasse ist für die Installation zuständig
	// hier werden die mql-File, history exporter kopiert und upgedated
	
	public void UpdateHistoryExporter(Display dis, Brokerview brokerview)
	{
		FileAccessDyn fd = new FileAccessDyn();
		// in dieser funktion wird nur der neue HistoryExporter kopiert
		// ausserdem die neue monitorlib
		// weiterhin das RestartTerminal.bat
		int anz = brokerview.getAnz();
		String histexporter_quelle = null;
		String monitorlib_quelle = null;
		String monitorlib_quelle2 = null;
		
		if (anz == 0)
		{
			Mbox.Infobox("please Reaload all Data first !!");
			return;
		}
		
		// gehe durch alle Broker und update die Historyexporter
		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);
			String bnam = meconf.getBrokername();
			// Mlist.add("update<" + bnam + ">", 1);
			
			if ((meconf.getMttype().toLowerCase() != "mt4") && (meconf.getMttype().toLowerCase() != "mt5"))
				Tracer.WriteTrace(10, "E:UpdateHistoryExporter mttype=<" + meconf.getMttype() + "> not supported");
			
			if (meconf.getMttype().toLowerCase().equals("mt4"))
				histexporter_quelle = Rootpath.getRootpath() + "\\install\\MT4_experts\\historyexporter.mq4";
			else if (meconf.getMttype().toLowerCase().equals("mt5"))
				histexporter_quelle = Rootpath.getRootpath() + "\\install\\MT5_experts\\SQ\\historyexporter.mq5";
			
			if (meconf.getMttype().toLowerCase().equals("mt4"))
			{
				monitorlib_quelle = Rootpath.getRootpath() + "\\install\\MT4_Include\\monitorlib.mqh";
				// kopiere die Monitorlib
				fd.copyFile3(monitorlib_quelle, meconf.getMqldata() + "//include//monitorlib.mqh", 1);
			}
			else
			{
				monitorlib_quelle = Rootpath.getRootpath() + "\\install\\MT5_Include\\monitorlib.mqh";
				monitorlib_quelle2 = Rootpath.getRootpath() + "\\install\\MT5_Include\\monitorlib_mt5.mqh";
				// kopiere die Monitorlib
				fd.copyFile3(monitorlib_quelle, meconf.getMqldata() + "//include//monitorlib.mqh", 1);
				fd.copyFile3(monitorlib_quelle2, meconf.getMqldata() + "//include//monitorlib_mt5.mqh", 1);
			}
			
			// Kopiere den historyexporter
			if (meconf.getMttype().toLowerCase().equals("mt4"))
				fd.copyFile3(histexporter_quelle, meconf.getExpertdata() + "//historyexporter.mq4", 1);
			else if (meconf.getMttype().toLowerCase().equals("mt5"))
				fd.copyFile3(histexporter_quelle, meconf.getExpertdata() + "//historyexporter.mq5", 1);
			
			FileAccess.FileDelete(meconf.getMqldata() + "//Experts//mqlcache.dat", 1);
			
		}
		DisTool.arrowCursor();
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("Info:The Historyexporter was updated, you can start the metatrader");
		dialog.open();
	}
	
	private void copyInstHistoryExporter(Metaconfig metaconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		String histexporter_quelle = null;
		String histexporterchr_quelle = null;
		
		// Kopiere den historyexporter
		if (metaconf.getMttype().toLowerCase().equals("mt4"))
		{
			histexporter_quelle = Rootpath.getRootpath() + "\\install\\MT4_experts\\historyexporter.mq4";
			histexporterchr_quelle = Rootpath.getRootpath() + "\\install\\MT4_profiles\\historyexporter.chr";
			fd.copyFile2(histexporter_quelle, metaconf.getExpertdata() + "//historyexporter.mq4");
		} else if (metaconf.getMttype().toLowerCase().equals("mt5"))
		{
			histexporter_quelle = Rootpath.getRootpath() + "\\install\\MT5_experts\\historyexporter.mq5";
			histexporterchr_quelle = Rootpath.getRootpath() + "\\install\\MT5_profiles\\historyexporter.chr";
			fd.copyFile2(histexporter_quelle, metaconf.getExpertdata() + "//historyexporter.mq5");
		}
		Tracer.WriteTrace(20,
				"I: copy histroyexporter from<" + histexporter_quelle + "> to<" + metaconf.getExpertdata() + ">");
		
		
		
		FileAccess.FileDelete(metaconf.getMqldata() + "//Experts//mqlcache.dat", 1);
		
		Tracer.WriteTrace(20, "I: special profile1");
		Profiler profiler = new Profiler(metaconf);
		profiler.delAllProfiles("historyexporter", null);
		profiler.createSpecialProfile(histexporterchr_quelle, "historyexporter");
		profiler.checkDoubleEa("historyexporter", metaconf.getExpertdata());
		Tracer.WriteTrace(20, "I: special profile2");
	}
	
	private void copyTickdataExporter(Metaconfig metaconf)
	{
		if (metaconf.getMttype().toLowerCase().equals("mt4"))
		{
			FileAccessDyn fd = new FileAccessDyn();
			String histexporter_quelle = Rootpath.getRootpath()
					+ "\\install\\MT4_experts\\GenBuilder_TickDataExportEA.mq4";
			String histexporterchr_quelle = Rootpath.getRootpath()
					+ "\\install\\MT4_profiles\\GenBuilder_TickDataExportEA.chr";
			
			Tracer.WriteTrace(20,
					"I:copy tickdataexporter from<" + histexporter_quelle + "> to<" + metaconf.getExpertdata() + ">");
			
			// Kopiere den Tickdataexporter
			fd.copyFile2(histexporter_quelle, metaconf.getExpertdata() + "//GenBuilder_TickDataExportEA.mq4");
			
			Profiler profiler = new Profiler(metaconf);
			profiler.createSpecialProfile(histexporterchr_quelle, "GenBuilder_TickDataExportEA");
		} else if (metaconf.getMttype().toLowerCase().equals("mt5"))
		{
			Tracer.WriteTrace(10, "Error: TickdataExporter for MT5 not supported at the moment");
		}
		
	}
	
	private void copyBacktestEnvironment(Metaconfig metaconf)
	{
		Mt4Backtester mt4b = new Mt4Backtester();
		mt4b.Install(Rootpath.getRootpath(), metaconf.getAppdata());
	}
	
	private void copyMyFxbookEa(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		String myfxbookea = null;
		String expertdll_quelle = null;
		
		// Mt4
		if (meconf.getMttype().toLowerCase().equals("mt4"))
		{
			myfxbookea = Rootpath.getRootpath() + "\\install\\MT4_experts\\Myfxbook.ex4";
			if (fd.copyFile2(myfxbookea, meconf.getExpertdata() + "\\Myfxbook.ex4") == false)
				Tracer.WriteTrace(10, "E:cant copy myfxbook ea");
			
			expertdll_quelle = Rootpath.getRootpath() + "\\install\\MT4_libraries\\Myfxbook.dll";
		}
		// mt5
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
		{
			myfxbookea = Rootpath.getRootpath() + "\\install\\MT5_experts\\Myfxbook.ex5";
			if (fd.copyFile2(myfxbookea, meconf.getExpertdata() + "\\Myfxbook.ex5") == false)
				Tracer.WriteTrace(10, "E:cant copy myfxbook ea");
			
			expertdll_quelle = Rootpath.getRootpath() + "\\install\\MT5_libraries\\Myfxbook.dll";
		}
		
		Tracer.WriteTrace(20, "I: copy myfxbookea from<" + myfxbookea + "> to<" + meconf.getAppdata() + ">");
		
		// funktioniert nur noch mit mt600+
		if (fd.copyFile2(expertdll_quelle, meconf.getMqldata() + "\\libraries\\Myfxbook.dll") == false)
			Tracer.WriteTrace(10, "E:cant copy myfxbook.dll ");
		
		FileAccess.FileDelete(meconf.getMqldata() + "\\libraries\\mqlcache.dat", 1);
		
		// diese config muss mit password gepatched werden
		InstalliereMyFxbookConfig(meconf);
		
	}
	
	private void copyIncludes(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		if (meconf.getMttype().toLowerCase().equals("mt4"))
			fd.copyFile2(Rootpath.getRootpath() + "\\install\\MT4_Include\\monitorlib.mqh", meconf.getMqldata() + "//include//monitorlib.mqh");
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
		{
			fd.copyFile2(Rootpath.getRootpath() + "\\install\\MT5_Include\\monitorlib.mqh", meconf.getMqldata() + "//include//monitorlib.mqh");
			fd.copyFile2(Rootpath.getRootpath() + "\\install\\MT5_Include\\monitorlib_mt5.mqh", meconf.getMqldata() + "//include//monitorlib_mt5.mqh");
			fd.copyFile2(Rootpath.getRootpath() + "\\install\\MT5_Include\\MT4Orders.mqh", meconf.getMqldata() + "//include//MT4Orders.mqh");
			fd.copyFile2(Rootpath.getRootpath() + "\\install\\MT5_Include\\mql4_to_mql5.mqh", meconf.getMqldata() + "//include//mql4_to_mql5.mqh");
		}
		
		
	}
	
	private void loescheAltes(String quellverz, Metaconfig meconf)
	{
		if (meconf.getMttype().toLowerCase().equals("mt4"))
		{
			
			FileAccessDyn fd = new FileAccessDyn();
			// Das alte löschen
			Mlist.add("I:delete all mql on destbroker <" + meconf.getMqldata() + ">", 1);
			FileAccess.FilesDelete(quellverz + "\\realinstall", ".mq4");
			FileAccess.FilesDelete(quellverz + "\\install", ".mq4");
			FileAccess.FilesDelete(meconf.getExpertdata(), ".mq4");
			FileAccess.FilesDelete(meconf.getExpertdata(), ".ex4");
			FileAccess.FilesDelete(meconf.getExpertdata(), ".log");
			
			// hier muss abfrage rein das dies kein tradechannel ist, die tradechannel *.chr
			// files nicht löschen sonst sind die verbindungen
			// bei der überinstallation weg!!
			FileAccess.FilesDelete(meconf.getAppdata() + "\\profiles\\default", ".chr");
			FileAccess.FilesDelete(meconf.getExpertdata(), ".dat");
		} else if (meconf.getMttype().toLowerCase().equals("mt5"))
		{
			
			FileAccessDyn fd = new FileAccessDyn();
			// Das alte löschen
			Mlist.add("I:delete all mql on destbroker <" + meconf.getMqldata() + ">", 1);
			
			FileAccess.FilesDelete(quellverz + "\\realinstall", ".mq5");
			FileAccess.FilesDelete(quellverz + "\\install", ".mq5");
			FileAccess.FilesDelete(meconf.getExpertdata() , ".mq5");
			FileAccess.FilesDelete(meconf.getExpertdata() , ".ex5");
			FileAccess.FilesDelete(meconf.getExpertdata() , ".log");
			File sqdir = new File(meconf.getExpertdata() );
			if (sqdir.exists() == true)
				if (sqdir.delete() == false)
					Tracer.WriteTrace(10, "E: can´t delete directory <" + sqdir + ">");
				else if (sqdir.mkdir() == false)
					Tracer.WriteTrace(10, "E:can´t create dir <" + sqdir + ">");
					
			// hier muss abfrage rein das dies kein tradechannel ist, die tradechannel *.chr
			// files nicht löschen sonst sind die verbindungen
			// bei der überinstallation weg!!
			FileAccess.FilesDelete(meconf.getAppdata() + "\\MQL5\\profiles\\charts\\default", ".chr");
			FileAccess.FilesDelete(meconf.getAppdata(), "\\MQL5\\experts.dat");
			
		} else
			Tracer.WriteTrace(10, "E: not supported 134545454543");
		
	}
	
	private void kopiereIndikatoren(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		Mlist.add("I:install indicators", 1);
		String indiverz_quelle = null;
		
		if (meconf.getMttype().toLowerCase().equals("mt4"))
			indiverz_quelle = Rootpath.getRootpath() + "\\install\\MT4_indikatoren";
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
			indiverz_quelle = Rootpath.getRootpath() + "\\install\\MT5_indikatoren";
		else
			Tracer.WriteTrace(10, "E:KopiereIndikatoren meconf-mql4type not supported");
		
		String inidverz_ziel = meconf.getMqldata() + "\\indicators";
		Tracer.WriteTrace(20, "I:copy indicators from <" + indiverz_quelle + "> to <" + inidverz_ziel + ">");
		
		if (meconf.getMttype().toLowerCase().equals("mt4"))
			FileAccess.CopyDirectory(indiverz_quelle, inidverz_ziel, ".mq4");
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
			FileAccess.CopyDirectory(indiverz_quelle, inidverz_ziel, ".mq5");
		
	}
	
	private void InstallSernumber(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		Inf inf = new Inf();
		Tracer.WriteTrace(20, "I: inst sernumber in<" + meconf.getAppdata() + "//monitor.info" + ">");
		inf.setFilename(meconf.getAppdata() + "//monitor.info");
		inf.writezeile(GlobalVar.getSerialString());
		inf.close();
	}
	
	private void ready(Display dis)
	{
		if (dis == null)
			Tracer.WriteTrace(10, "internal 4444jj dis=null");
		if (dis.getActiveShell() == null)
		{
			Tracer.WriteTrace(20, "internal 5544jj shell=null");
			return;
		}
		Mbox.Infobox("installation ready");
	}
	
	private void kopiereEaSysteme(String mqlquellverz, ProgressBar progressBar1, Metaconfig metaconfig,
			Metaconfig metarealconfig, String zwischenspeichernam, int realpatchflag, Ealiste eal, Tableview tv)
	{
		// Dies ist die Funktion die die EAs installiert
		
		FileAccessDyn fd = new FileAccessDyn();
		// realpatchflag=1 realsysteme werden noch besonders gepatched
		// und die *.ex4 werden auch gelöscht
		String mqlnam = null;
		Profiler profiler = null;
		FileAccessDyn fadyn = new FileAccessDyn();
		
		if (realpatchflag == 0)
			profiler = new Profiler(metaconfig);
		else
			profiler = new Profiler(metarealconfig);
		
		String cfg_quelle = null;
		String mqlpostfix = null;
		
		if (metaconfig.getMttype().toLowerCase().equals("mt4"))
		{
			cfg_quelle = Rootpath.getRootpath() + "\\install\\MT4_profiles\\chrmaster.chr";
			mqlpostfix = ".mq4";
		} else if (metaconfig.getMttype().toLowerCase().equals("mt5"))
		{
			cfg_quelle = Rootpath.getRootpath() + "\\install\\MT5_profiles\\chrmaster.chr";
			mqlpostfix = ".mq5";
		} else
			Tracer.WriteTrace(10, "E:kopiere EaSysteme mtversion <" + metaconfig.getMetaversion() + "> not supported");
		
		Tracer.WriteTrace(20, "Rootpath<" + Rootpath.getRootpath() + "> mqlquellverz<" + mqlquellverz + ">");
		
		// Falls Tradesuffix gesetzt dann wird z.B.
		// Q80 EURUSD M15 134.mq4 nach Q80 EURUSD.r M15 134.mq4 renamed
		// 1)Namensanpassung
		fadyn.initFileSystemList(mqlquellverz, 1);
		int anz = fadyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String mqlquellnam = fadyn.holeFileSystemName();
			if (mqlquellnam.endsWith(mqlpostfix))
			{
				Tracer.WriteTrace(20, "rename quellnam<" + mqlquellnam + ">");
				// den mqlnamen bestimmen
				if (mqlquellnam.endsWith(mqlpostfix))
					mqlnam = mqlquellnam.substring(0, mqlquellnam.indexOf(mqlpostfix));
				
				// Den quellnamen renamen das Keyword Strategy muss raus
				String tradesuffix = metaconfig.getTradesuffixsender();
				if ((tradesuffix != null) && (tradesuffix.length() > 1))
					renameQuellnamTradeSuffixFile(metaconfig.getTradesuffixsender(),
							metaconfig.getMqlquellverz() + "\\" + mqlnam);
			}
		}
		
		// Remove substring "Strategy" out of quellname
		fadyn.initFileSystemList(mqlquellverz, 1);
		anz = fadyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String mqlquellnam = fadyn.holeFileSystemName();
			if ((mqlquellnam.endsWith(mqlpostfix)) || (mqlquellnam.endsWith(".sqx")))
			{
				Tracer.WriteTrace(20, "rename quellnam<" + mqlquellnam + ">");
				// den mqlnamen bestimmen
				if (mqlquellnam.endsWith(mqlpostfix))
					mqlnam = mqlquellnam.substring(0, mqlquellnam.indexOf(mqlpostfix));
				if (mqlquellnam.endsWith(".sqx"))
					mqlnam = mqlquellnam.substring(0, mqlquellnam.indexOf(".sqx"));
				
				// Den quellnamen renamen das Keyword Strategy muss raus
				renameQuellnamFiles(metaconfig.getMqlquellverz() + "\\" + mqlnam,metaconfig.getMttype());
			}
		}
		
		// 2) Kopiere ins tmp-install-verzeichniss
		// kopiere die Mq4-files ins temporäre install verzeichniss
		fadyn.initFileSystemList(mqlquellverz, 1);
		anz = fadyn.holeFileAnz();
		progressBar1.setMinimum(0);
		progressBar1.setMaximum(anz - 1);
		
		for (int i = 0; i < anz; i++)
		{
			String mqlquellnam = fadyn.holeFileSystemName();
			Tracer.WriteTrace(20, "quellnam<" + mqlquellnam + ">");
			progressBar1.setSelection(i);
			
			if (mqlquellnam.contains(".bak") == true)
				continue;
			
			if (mqlquellnam.contains(".exe") == true)
				continue;
			
			// 2a)import tradeliste falls eine da ist
			CheckTradeListenImport(tv, metaconfig.getMqlquellverz() + "\\" + mqlquellnam, metaconfig.getBrokername());
			
			// hier werden nur mq4/5-files verarbeitet
			if (mqlquellnam.contains(mqlpostfix) == false)
				continue;
			
			Tracer.WriteTrace(20, "mqlquellnam<" + mqlquellnam + "> mqlquellverz<" + mqlquellverz + "> zielshare<"
					+ metaconfig.getMqldata() + "> zwischenspeichernam<" + zwischenspeichernam + ">");
			
			int magic = Eaclass.calcMagic(mqlquellnam);
			String brokername = metaconfig.getBrokername();
			
			// hole Daten aus dem Mql-File
			Patcher pat = new Patcher();
			pat.setFilename(mqlquellverz + "\\" + mqlquellnam);
			String sl = pat.getSl();
			String tp = pat.getTp();
			int eatype = pat.getEaType();
			
			// Die Ea-Daten werden in die Datenstruktur aufgenommen
			Ea ea = eaAufnahme(eal, mqlquellnam, magic, brokername, sl, tp, eatype);
			// remove old *.del files out of ../files
			removeEaDels(magic, metaconfig);
			
			// hier wird das Mql-File geschrieben, aber wohin?
			
			MqlPatch mqlp = writeMqlFileZwischenspeicher(mqlquellnam, mqlquellverz, zwischenspeichernam, realpatchflag,
					metaconfig, metarealconfig, ea);
			
			if (mqlp == null)
				continue;
			
			String conbrokername = metaconfig.getconnectedBroker();
			
			// falls auf einen echtbroker was installiert wird
			// den brokernamen und die installflags setzen
			if (realpatchflag == 1)
			{
				eal.setInstFrom(magic, conbrokername, brokername);
			}
			
			// Schreibt das Metatrader generierte chr.file
			// Schreibt das Profile
			if (realpatchflag == 1)
				profiler.createProfile(mqlp, cfg_quelle, ea, metarealconfig);
			else
			{
				profiler.createProfile(mqlp, cfg_quelle, ea, metaconfig);
				
			}
			
		}
		eal.store(0);
		
		String datetime = Tools.get_aktdatetime_str();
		metaconfig.setLastinstallation(datetime);
		
		if (realpatchflag == 0)
			loescheMqlCache(metaconfig.getMqldata());
		else
			loescheMqlCache(metarealconfig.getMqldata());
		
	}
	
	private void renameQuellnamFiles(String fnamsource,String mtversion)
	{
		String endung=".mq4";
		
		if (fnamsource.contains("Strategy"))
		{
			//endung ist .mq4 oder .mq5
			if(mtversion.toLowerCase().equals("mt5"))
				endung=".mq5";
			
			
			File fnamsource_f = new File(fnamsource + endung);
			File fnamdest_f = new File(fnamsource.replace("Strategy", "") + endung);
			if (fnamsource_f.renameTo(fnamdest_f) == false)
				Tracer.WriteTrace(10, "E: can´t rename file <" + fnamsource_f.getAbsolutePath() + "> to <"
						+ fnamdest_f.getAbsolutePath() + ">");
			
			Tracer.WriteTrace(20, "I:renamed Strategyname from<" + fnamsource_f.getAbsolutePath() + "> to<"
					+ fnamdest_f.getAbsolutePath() + ">");
		}
	}
	
	private void renameQuellnamTradeSuffixFile(String tradesuffix, String fnamsource)
	{
		// falls tradesuffix gesetzt ist dann wird
		// Q80 EURUSD M15 1234.mq4 nach Q80 EURUSD.r M15 1234.mq4 renamed
		
		File fnamsource_f = new File(fnamsource + ".mq4");
		// falls der tradesuffix noch nicht drin ist
		if (fnamsource.contains(tradesuffix) == false)
		{
			// splite auf
			String[] parts = fnamsource_f.getAbsolutePath().split(" ");
			// hole die position der currency
			int curindex = parts.length - 3;
			String currency = parts[curindex];
			currency = currency + tradesuffix;
			parts[curindex] = currency;
			
			// setzte den String wieder zusammen
			String neuname = "";
			int n = parts.length;
			for (int i = 0; i < n; i++)
				neuname = neuname + parts[i] + " ";
			File neuname_f = new File(neuname);
			
			if (fnamsource_f.renameTo(neuname_f) == false)
				Tracer.WriteTrace(10, "E: can´t rename file <" + fnamsource_f.getAbsolutePath() + "> to <"
						+ neuname_f.getAbsolutePath() + ">");
			
		}
		
	}
	
	private void loescheMqlCache(String verz)
	{
		String cachenam = verz + "\\experts\\mqlcache.dat";
		File f = new File(cachenam);
		if (f.exists())
			f.delete();
	}
	
	private void CheckTradeListenImport(Tableview tv, String mqlquellnam, String brokername)
	{
		String fnam = mqlquellnam.replace(".mq4", ".tradeliste");
		
		if ((fnam.contains(".tradeliste")) && (FileAccess.FileAvailable(fnam)))
			tv.importTradeliste(fnam, brokername);
	}
	
	private Ea eaAufnahme(Ealiste eal, String mqlquellnam, int magic, String brokername, String sl, String tp,
			int eatype)
	{
		
		mqlquellnam = mqlquellnam.replace("_Strategy", "");
		
		// der ea wird in der ealiste aufgenommen wenn er noch nicht drin ist
		Ea ea = eal.getEa(magic, brokername);
		if (ea != null)
		{
			ea.setEafilename(mqlquellnam);
			ea.setSl(sl);
			ea.setTp(tp);
			ea.setType(eatype);
		} else
		{
			// in die ealiste aufnehmen
			ea = new Ea();
			ea.setMagic(magic);
			ea.setBroker(brokername);
			ea.setEafilename(mqlquellnam);
			ea.setSl(sl);
			ea.setTp(tp);
			ea.setType(eatype);
			eal.add(ea);
		}
		return ea;
	}
	
	public void removeEaDels(int magic, Metaconfig meconf)
	{
		String mqldir = meconf.getMqldata();
		File fnam_f = new File(mqldir + "\\files\\" + magic + ".del");
		if (fnam_f.exists())
			if (fnam_f.delete() == false)
				Tracer.WriteTrace(10, "removeEaDels:cant remove file <" + fnam_f.getAbsolutePath() + ">");
			
	}
	
	public boolean InstalliereEinRealEaSystemFromDemobroker_dep(Tableview tableview, Profit prof, Metaconfig meconf,
			Metaconfig meRealconf, Ealiste eal)
	{
		
		// quellnam: B29 EURUSD M1 0.19036.mq4
		// quellverz:
		// C:\Forex\Strategien_ImEinsatz\GeneticBuilder\03_ImEinsatz\SYSTEM_AT_DEMOS_CORE8\FinFx
		// zielverz: z.b. //Core8/MT4_finfx oder
		// c:\forex\mt4\metatrader1\mql4\experts
		
		// mache ein Init des Realaccounts, wenn schon historyexporter.exe da
		// ist dann mache nix
		InitMetatrader(meRealconf, 0);
		
		// hier wird nur ein einziges EA-System ins Realsystem kopiert
		int magic = prof.getMagic();
		Profiler profiler = new Profiler(meRealconf);
		
		String cfg_quelle = Rootpath.getRootpath() + "\\install\\MT4_profiles\\chrmaster.chr";
		
		Ea ea = eal.getEa(magic, meconf.getBrokername());
		
		String mqlquellnam = ea.getEafilename();
		if (mqlquellnam == null)
		{
			Mbox.Infobox("Error: ea installation not correct magic<" + magic + "> broker<" + meconf.getBrokername()
					+ ">, no ea Filename in ealiste.xml");
			return false;
		}
		// hier ist die quelle
		String quellverz = meconf.getMqlquellverz();
		// hier kommt die modifizierte quelle hin
		String quellinstallverz = quellverz + "\\realinstall";
		// hier kommt der installierte ea hin
		String zielverz = meRealconf.getExpertdata();
		
		// install-verzeichniss erzeugen falls keins da ist
		FileAccess.checkgenDirectory(quellinstallverz);
		
		// flags setzen, fuer magic, broker, instfrombroker
		eal.setInstFrom(magic, meconf.getconnectedBroker(), meconf.getBrokername());
		
		// Schreibt das mq4/5-File
		MqlPatch mqlp = writeMqlInstOneRealsystem_dep(mqlquellnam, quellverz, quellinstallverz, zielverz, meconf,
				meRealconf, ea);
		
		// lösche den mqlcache.dat
		String zielfile = zielverz + "//mqlcache.dat";
		File fmql = new File(zielfile);
		if (fmql.exists())
			fmql.delete();
		
		// Schreibt das Metatrader generierte cfg.file
		if (mqlp != null)
		{
			// schreibt das cfg.file
			
			profiler.createProfile(mqlp, cfg_quelle, ea, meRealconf);
			
			return true;
		} else
			return false;
		
	}
	
	public void InstalliereMyFxbookConfig(Metaconfig meconf)
	{
		String cfg_quelle = null;
		
		if (meconf.getMttype().toLowerCase().equals("mt4"))
			cfg_quelle = Rootpath.getRootpath() + "\\install\\MT4_profiles\\chrmaster.chr";
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
			cfg_quelle = Rootpath.getRootpath() + "\\install\\MT5_profiles\\chrmaster.chr";
		else
			Tracer.WriteTrace(10, "E: internal mt4/mt5 mttype not set");
		
		String zielsharename = meconf.getAppdata() + "\\profiles\\default\\myfxbook.chr";
		
		// prüft nach ob myfxbook schon da ist
		Profiler profiler = new Profiler(meconf);
		profiler.delAllProfiles("myfxbook", null);
		if (profiler.getanzProfiles("myfxbook") > 0)
		{
			Tracer.WriteTrace(20, "I:myfxbook ea already configured for broker<" + meconf.getBrokername() + ">");
			return;
		}
		// dann schreibe myfxbook.chr
		MqlPatch mfx = new MqlPatch();
		mfx.setFilename(cfg_quelle);
		mfx.setExpertname("myfxbook");
		mfx.patchMyFxbookEa();
		mfx.writeMemFile(zielsharename);
		
		// dann ein rename von myfxbook.chr nach chartXX.chr
		File myfxbookname = new File(zielsharename);
		File newname = new File(meconf.getAppdata() + "\\profiles\\default\\" + profiler.getFreeChartName());
		if (myfxbookname.renameTo(newname) == false)
			Tracer.WriteTrace(10, "E:installieremyfxbook:cant rename file <" + myfxbookname + ">");
		
		// dann patche noch das currencypair
		String histcurrencystring = meconf.getHistexportcurrency();
		FileAccess.FileReplaceString(newname.getAbsolutePath(), "symbol=EURUSD", "symbol=" + histcurrencystring);
		
	}
	
	public void InstallMetatraderDemoEaFiles(Display dis, ProgressBar progressBar1, String mqlquellverz,
			Metaconfig metaconfig, Metaconfig metarealconfig, Ealiste eal, Tableview tv)
	{
		// quellverz: ist das mql-Quellverzeichniss wo sich die ea´s befinden
		// zielshare: ist der zielshare
		// metaconfig: ist das metatrader konfigfile aus dem benutzermenue
		
		// install-verzeichniss erzeugen
		FileAccess.checkgenDirectory(mqlquellverz + "\\install");
		
		loescheAltes(mqlquellverz, metaconfig);
		// InitMetatrader(metaconfig, 1); das wird beim save gemacht
		kopiereEaSysteme(mqlquellverz, progressBar1, metaconfig, metarealconfig, "install", 0, eal, tv);
		
		metaconfig.setInstallationstatus(1);
		
		if (metaconfig.getMttype().toLowerCase().equals("mt5"))
		{
			metaconfig.backupProfiles();
			
			// 1 Start & Stop MT5
			MetaStarter.StartStopMetatrader(metaconfig);
			
			metaconfig.restoreProfiles();
		}
		
		ready(dis);
		return;
	}
	
	public void InitMetatrader(Metaconfig metaconfig, int forceflag)
	{
		// falls forceflag==1 dann wird auf jeden fall initialisiert, also auch
		// überinitialisiert
		
		// bei keinem zwang, schaue nach ob schon der historyexporter drüber
		// ist,
		// wenn ja dann mache keine initialisierung
		
		if (metaconfig == null)
			Tracer.WriteTrace(10, "E: metaconfig=null");
		
		if (metaconfig.isInsthistoryexporter())
			copyInstHistoryExporter(metaconfig);
		if (metaconfig.getUsemyfxbookflag() == 1)
			copyMyFxbookEa(metaconfig);
		if (metaconfig.isInsttradecopy())
			copyTradecopy(metaconfig);
		if (metaconfig.isInsttickdataexporter())
			copyTickdataExporter(metaconfig);
		
		copyBacktestEnvironment(metaconfig);
		copyIncludes(metaconfig);
		kopiereIndikatoren(metaconfig);
		setFridayend(metaconfig, metaconfig.getClosefridayflag());
		
		InstallSernumber(metaconfig);
		metaconfig.setInstallationstatus(1);
		setFridayend(metaconfig, metaconfig.getClosefridayflag());
		
	}
	
	public void InstallMetatraderRealEaFiles(Display dis, ProgressBar progressBar1, String mqlquellverz,
			String zielshare, Metaconfig metaconfig, Metaconfig metarealconfig, Ealiste eal, Tableview tv)
	{
		// quellverz: ist das mql-Quellverzeichniss wo sich die ea´s befinden
		// zielshare: ist der zielshare
		// metaconfig: ist das metatrader konfigfile aus dem benutzermenue
		
		// install-verzeichniss erzeugen
		FileAccess.checkgenDirectory(mqlquellverz + "\\realinstall");
		
		InitMetatrader(metarealconfig, 1);
		kopiereEaSysteme(mqlquellverz, progressBar1, metaconfig, metarealconfig, "realinstall", 1, eal, tv);
		
		ready(dis);
		return;
	}
	
	public void setFridayend(Metaconfig meconf, int val)
	{
		String path = meconf.getMqldata();
		File fend = new File(path + "\\files\\fridayend.txt");
		
		// falls abschaltund an und es wird aber aus gefordert, dann lösche
		if ((fend.exists() == true) && (val == 0))
		{
			if (fend.delete() == false)
				Tracer.WriteTrace(10, "E:cant delete file <" + fend.getPath() + ">");
			return;
		} // falls abschaltung aus, aber an ist gefordert
		else if ((fend.exists() == false) && (val == 1))
		{
			Inf inf = new Inf();
			inf.setFilename(fend.getAbsolutePath());
			inf.writezeile("swith on " + Mondate.getAktDate());
			inf.close();
			return;
		}
		return;
		
	}
	
	public void cleanRealAccount(Display dis, ProgressBar progressBar1, String mqlquellverz, Metaconfig metaconfig_real,
			Metaconfig metaconfig)
	{
		// alle installierten EA´s werden auf dem Realaccount gelöscht
		// ebenfalls die konfigurationen
		
		int lotsizedelflag = 0;
		lotsizedelflag = MboxQuest.Questbox("Do you want to delete the lostsize ?");
		
		// alle experts löschen
		FileAccess.initFileSystemList(metaconfig_real.getMqldata() + "\\experts", 1);
		int anz = FileAccess.holeFileAnz();
		progressBar1.setMinimum(0);
		progressBar1.setMaximum(anz - 1);
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.contains("GenBuilder_"))
				continue;
			if (fnam.contains("historyexporter"))
				continue;
			if (FileAccess.FileAvailable0(metaconfig_real.getMqldata() + "\\experts\\" + fnam))
				FileAccess.FileDelete(metaconfig_real.getMqldata() + "\\experts\\" + fnam, 1);
		}
		
		// alle on-off löschen
		FileAccess.initFileSystemList(metaconfig_real.getMqldata() + "\\files", 1);
		anz = FileAccess.holeFileAnz();
		progressBar1.setMinimum(0);
		progressBar1.setMaximum(anz - 1);
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.contains(".csv"))
				continue;
			if (fnam.contains(".txt"))
				continue;
			
			// falls lotsize nicht gelöscht werden soll, dann überspringe .lot
			if (lotsizedelflag == 0)
				if (fnam.contains(".lot"))
					continue;
			if (new File(metaconfig_real.getMqldata() + "\\files\\" + fnam).exists() == true)
				FileAccess.FileDelete(metaconfig_real.getMqldata() + "\\files\\" + fnam, 1);
		}
		
		// alle configs löschen
		FileAccess.initFileSystemList(metaconfig_real.getAppdata() + "\\profiles\\default", 1);
		anz = FileAccess.holeFileAnz();
		progressBar1.setMinimum(0);
		progressBar1.setMaximum(anz - 1);
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.contains("GenBuilder_"))
				continue;
			if (fnam.contains("historyexporter"))
				continue;
			if (fnam.contains("order"))
				continue;
			if (new File(metaconfig_real.getAppdata() + "\\profiles\\default\\" + fnam).exists() == true)
				FileAccess.FileDelete(metaconfig_real.getAppdata() + "\\profiles\\default\\" + fnam, 1);
		}
		
	}
	
	private MqlPatch writeMqlInstOneRealsystem_dep(String eanam, String quellverz, String quellinstallverz,
			String zielverz, Metaconfig meconf, Metaconfig meRealconf, Ea ea)
	{
		
		Tracer.WriteTrace(10, "E: writeMqlInstOneRealsystem_dep!!");
		FileAccessDyn fd = new FileAccessDyn();
		// Schribt das jeweilige mql-File
		MqlPatch mqlpatch = new MqlPatch();
		// entferne schmutz im namen
		
		// mq4 einlesen, modifizieren und im install ablegen und auf das Share
		// kopieren
		if (FileAccess.FileAvailable(quellverz + "\\" + eanam) == false)
		{
			Mbox.Infobox("file <" + quellverz + "\\" + eanam + "> not available");
			return null;
		}
		
		mqlpatch.setFilename(quellverz + "\\" + eanam);
		mqlpatch.setExpertname(eanam.replace("_Strategy", ""));
		
		String mqlzielnam = quellinstallverz + "\\" + eanam.replace("_Strategy", "");
		
		// Die magic berechnen
		int magic = Eaclass.calcMagic(eanam);
		mqlpatch.setMagic(magic);
		
		mqlpatch.patchMqlProtection();
		mqlpatch.patchInit(meconf.getMttype());
		mqlpatch.patchVariables();
		mqlpatch.patchGBMeta600();
		
		// abschaltautomatik hinzufügen
		mqlpatch.addAbschaltAutomatic("", meconf.getMttype());
		mqlpatch.patchLotsize(ea, meRealconf);
		mqlpatch.patchComment(ea);
		
		mqlpatch.patchSleeptimemod();
		// add the postcode from mt4_additional code to every ea
		// sq3.8.2=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq3
		// sq4=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq4
		String postfilename = Rootpath.getRootpath() + "\\install\\Mt4_additionalcode\\ea_postcode";
		mqlpatch.addPostcode(postfilename);
		mqlpatch.writeMemFile(mqlzielnam);
		
		// Hier wird auf dem share kopiert
		// mql installieren
		fd.copyFile2(quellinstallverz + "\\" + eanam, zielverz + "\\" + eanam);
		
		// und das ex4 löschen
		String ex4filename = zielverz// "
				+ mqlpatch.getExpertname();
		File ex4file = new File(ex4filename.replace(".mq4", ".ex4"));
		if (ex4file.exists())
			ex4file.delete();
		
		Mlist.add("I: write mql-file <" + eanam + ">", 1);
		
		return mqlpatch;
	}
	
	private MqlPatch writeMqlFileZwischenspeicher(String quellnam, String quellverz, String zwischenspeichernam,
			int realpatchflag, Metaconfig meconf, Metaconfig meRealconf, Ea ea)
	{
		// Hier wird nur in dem Zwischenspeicher gespeichert
		// quellnam: B29 EURUSD M1 0.19036.mq4
		// quellverz:
		// C:\Forex\Strategien_ImEinsatz\GeneticBuilder\03_ImEinsatz\SYSTEM_AT_DEMOS_CORE8\FinFx
		// zielverz: //Core8/MT4_finfx
		// zwischenspeichernam: install
		// falls realpatchflag == 1 wird das ex4 gelöscht
		
		// zielverzeichniss wo das mql hin installiert wird
		String zielverzexpert = null;
		FileAccessDyn fd = new FileAccessDyn();
		// Schribt das jeweilige mql-File
		MqlPatch mqlpatch = new MqlPatch();
		// entferne schmutz im namen
		
		// mq4 einlesen, modifizieren und im install ablegen und auf das Share
		// kopieren
		if (FileAccess.FileAvailable(quellverz + "\\" + quellnam) == false)
		{
			Mbox.Infobox("file <" + quellverz + "\\" + quellnam + "> not available");
			return null;
		}
		
		mqlpatch.setFilename(quellverz + "\\" + quellnam);
		// mqlpatch.setLotsize_depricated(String.valueOf(lotsize));
		
		mqlpatch.setExpertname(quellnam.replace("_Strategy", ""));
		
		String mqlziel = quellverz + "\\" + zwischenspeichernam + "\\" + quellnam.replace("_Strategy", "");
		
		// Die magic berechnen
		int magic = Eaclass.calcMagic(quellnam);
		mqlpatch.setMagic(magic);
		mqlpatch.patchMqlProtection();
		mqlpatch.patchInit(meconf.getMttype());
		mqlpatch.patchVariables();
		mqlpatch.patchGBMeta600();
		mqlpatch.patchSleeptimemod();
		mqlpatch.patchComment(ea);
		
		if (realpatchflag == 1)
		{
			mqlpatch.addAbschaltAutomatic("", meconf.getMttype());
			mqlpatch.patchLotsize(ea, meRealconf);
		} else
			mqlpatch.patchLotsize(ea, meconf);
		
		// write the file with the lotsize
		genLotfile(magic, ea, meconf);
		
		// add the postcode from mt4_additional code to every ea
		// sq3.8.2=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq3
		// sq4=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq4
		// sea=Studio Expert Advisor
		String postfilename = null;
		if (meconf.getMttype().toLowerCase().equals("mt4"))
			postfilename = Rootpath.getRootpath() + "\\install\\Mt4_additionalcode\\ea_postcode";
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
			postfilename = Rootpath.getRootpath() + "\\install\\Mt5_additionalcode\\ea_postcode";
    	else
		  Tracer.WriteTrace(10, "E:write mqlspeicher 123");
		
		mqlpatch.addPostcode(postfilename);
		mqlpatch.writeMemFile(mqlziel);
		
		if (realpatchflag == 1)
			zielverzexpert = meRealconf.getExpertdata();
		else
			zielverzexpert = meconf.getExpertdata();
			
		// Hier wird auf dem share kopiert, also ins Metatrader 4/5 -Verzeichnis
		// mql installieren
		if (meconf.getMttype().toLowerCase().equals("mt4"))
			fd.copyFile2(mqlziel, zielverzexpert + "//" + mqlpatch.getExpertname());
		else if (meconf.getMttype().toLowerCase().equals("mt5"))
		{
			File fna=new File(zielverzexpert );
			if(fna.exists()==false)
				if(fna.mkdir()==false)
					Tracer.WriteTrace(10, "E:Mqlpatch can´t create directory <"+fna.getAbsolutePath()+">");
			fd.copyFile2(mqlziel, zielverzexpert +"\\" + mqlpatch.getExpertname());
		}
		else
			Tracer.WriteTrace(10, "E:not supported jjfdjfjdj");
		
		// und das ex4 löschen
		String ex4filename = zielverzexpert + "//" + mqlpatch.getExpertname();
		if (meconf.getMttype().toLowerCase().equals("mt4"))
		{
			File ex4file = new File(ex4filename.replace(".mq4", ".ex4"));
			if (ex4file.exists())
				ex4file.delete();
		} else if (meconf.getMttype().toLowerCase().equals("mt5"))
		{
			// und das ex5 löschen
			File ex5file = new File(ex4filename.replace(".mq5", ".ex5"));
			if (ex5file.exists())
				ex5file.delete();
		}
		
		Mlist.add("I: write mql-file <" + quellnam + ">", 1);
		
		return mqlpatch;
	}
	
	// wirte the lotfile in ...\mt4\<Brokername>\\MQL4\\Files\\<magic>.lot
	private void genLotfile(int magic, Ea ea, Metaconfig meconf)
	{
		double lotsize = meconf.getLotsize();
		String zielverzexpert = meconf.getExpertdata();
		String zielFiles = meconf.getMqldata()+"\\files";
		
		String zf = zielFiles + "\\" + magic + ".lot";
		File zff = new File(zf);
		if (zff.exists() == true)
			if (zff.delete() == false)
				Tracer.WriteTrace(10, "E: can´t delete file <" + zf + "> -->STOP");
		Inf inf = new Inf();
		inf.setFilename(zf);
		inf.writezeile("\\Install\\Mt4_additionalcode SQ 4.X");
		inf.writezeile("extern double mmLotsIfNoMM = " + lotsize + ";");
		inf.close();
		return;
		
	}
	
	public void copyEA(int magic, String quellverz, String zielverz)
	{
		FileAccessDyn fd = new FileAccessDyn();
		int copyflag = 0;
		FileAccess.initFileSystemList(quellverz, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam_org = FileAccess.holeFileSystemName();
			String fnam = fnam_org;
			fnam = fnam.substring(fnam.lastIndexOf(" "), fnam.lastIndexOf("."));
			fnam = fnam.replace(".", "");
			if (magic == SG.get_zahl(fnam))
			{
				fd.copyFile2(quellverz + "\\" + fnam_org, zielverz + "\\" + fnam_org);
				copyflag = 1;
			}
			
		}
		
		if (copyflag == 0)
			Mbox.Infobox("EA magic<" + magic + "> not available in <" + quellverz + ">");
	}
	
	public void backup(Display dis, Brokerview brokerview)
	{
		copy(new File(Rootpath.getRootpath() + "\\conf\\brokerconf.xml"),
				new File(Rootpath.getRootpath() + "\\conf\\brokerconf.xml.backup"), 0);
		copy(new File(Rootpath.getRootpath() + "\\data\\tradeliste.xml"),
				new File(Rootpath.getRootpath() + "\\data\\tradeliste.xml.backup"), 0);
		copy(new File(Rootpath.getRootpath() + "\\data\\ealiste.xml"),
				new File(Rootpath.getRootpath() + "\\data\\ealiste.xml.backup"), 0);
		
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("Backup");
		dialog.setMessage("backup  ready");
		dialog.open();
	}
	
	public void restore(Display dis, Brokerview brokerview)
	{
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("******** RESTORE *********");
		dialog.setMessage("I will make an RESTORE of the datafiles");
		dialog.open();
		
		copy(new File(Rootpath.getRootpath() + "\\conf\\brokerconf.xml.backup"),
				new File(Rootpath.getRootpath() + "\\conf\\brokerconf.xml"), 1);
		copy(new File(Rootpath.getRootpath() + "\\data\\tradeliste.xml.backup"),
				new File(Rootpath.getRootpath() + "\\data\\tradeliste.xml"), 1);
		copy(new File(Rootpath.getRootpath() + "\\data\\ealiste.xml.backup"),
				new File(Rootpath.getRootpath() + "\\data\\ealiste.xml"), 1);
		
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("Restore");
		dialog.setMessage("Restore ready I will Shutdown Monitortool, please restart it");
		dialog.open();
		System.exit(99);
	}
	
	private void copy(File fsrc, File fdst, int lencheck)
	{
		// if lencheck==1
		// die quelllänge sollte wesentlich länger sein als die Ziellänge
		// es wird ja ein backup zurückgespielt
		// quellänge sollte > ziellänge/2 sein
		
		if (lencheck == 1)
		{
			if (fsrc.length() < fdst.length() / 2)
				Tracer.WriteTrace(10,
						"E:restore:backtup src<" + fsrc.getAbsolutePath() + "> dst<" + fdst.getAbsolutePath() + ">");
			
		}
		
		if (fdst.exists())
			if (fdst.delete() == false)
				Tracer.WriteTrace(10, "E:backup:cant delete file <" + fdst.getAbsolutePath() + ">");
		if (FileAccess.copyFile(fsrc.getAbsolutePath(), fdst.getAbsolutePath()) == false)
			Tracer.WriteTrace(10, "E:backup: cant copy file from <" + fsrc.getAbsolutePath() + "> to <"
					+ fdst.getAbsolutePath() + ">");
	}
	
	public void transfer(Display dis, Brokerview brokerview)
	{
		FileAccessDyn fd = new FileAccessDyn();
		// von den history.txt wird nach history_transfer kopiert
		// das wird dann benötigt wenn man einen neuen Metatrader installiert
		// aber die alte
		// historie noch behalten möchte
		
		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to transfer historyexporter.txt to historyexporter_transfer.txt ");
		if (dialog.open() != 32)
			return;
		
		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("Backup error");
			dialog.setMessage("no data for transfer, please reloadAllData");
			dialog.open();
			return;
		}
		
		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);
			
			String zielshare = meconf.getFiledata();
			String quellfile = zielshare + "//history.txt";
			String backupfile = zielshare + "//history_transfer.txt";
			
			// Kopiere den historyexporter
			File qfile = new File(quellfile);
			if (qfile.exists())
				fd.copyFile2(quellfile, backupfile);
			
		}
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("transfer of history.txt ready");
		dialog.open();
	}
	
	public void transferuserdata(Display dis, Brokerview brokerview)
	{
		
		// der userdatabereich aus dem userdata/454545454545/.... wird ins
		// metatradertrootverzeichniss kopiert
		// dies wird benötigt wenn man die metatrader transportieren/umziehen
		// möchte
		
		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to transfer all userdata ");
		if (dialog.open() != 32)
			return;
		
		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("Backup error");
			dialog.setMessage("no data for transfer, please reloadAllData");
			dialog.open();
			return;
		}
		
		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);
			String source_mqlroot = meconf.getMqldata();
			String source_profiles = meconf.getAppdata() + "\\profiles";
			String dest = meconf.getNetworkshare_INSTALLDIR() + "\\copyFromAppdata";
			String infofile = meconf.getNetworkshare_INSTALLDIR() + "\\copyFromAppdata\\info.txt";
			
			// prüft nach ob das quellverzeichniss
			// ....\AppData\Roaming\MetaQuotes\Terminal\880772D848F4FEDB1FFFC14F4DD5B143
			if (source_mqlroot.toLowerCase().contains("appdata") == false)
				continue;
			
			Inf inf = new Inf();
			inf.setFilename(infofile);
			
			File destinfile = new File(dest);
			File destmql4 = new File(dest + "\\MQL4");
			File destprofiles = new File(dest + "\\profiles");
			if (destinfile.isDirectory() == false)
				destinfile.mkdir();
			if (destmql4.isDirectory() == false)
				destmql4.mkdir();
			if (destprofiles.isDirectory() == false)
				destprofiles.mkdir();
			
			// Kopiere die Verzeichnisse von mql4
			Filefunkt ff = new Filefunkt();
			try
			{
				Mlist.add("backup from<" + source_mqlroot + "> to<" + dest + ">", 1);
				ff.copyDir(new File(source_mqlroot), new File(dest + "\\MQL4"));
				inf.writezeile("copied from <" + source_mqlroot + "> successfull");
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Kopiere die Verzeichnisse von profiles
			try
			{
				ff.copyDir(new File(source_profiles), new File(dest + "\\profiles"));
				inf.writezeile("copied from <" + source_profiles + "> successfull");
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("transfer of userdata ready");
		dialog.open();
	}
	
	public void restoreuserdata(Display dis, Brokerview brokerview)
	{
		// der bereich \\copyFromAppdata.... wird in userdata
		// userdata/454545454545/.... zurückkopiert
		// Diesen Vorgang macht man wenn man die Metatraderverzeichnisse auf einem
		// neuen Rechner von Hand kopiert hat und den Userdatabereich noch restoren
		// möchte
		
		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to restore all userdata from ..\\copyFromAppdata to ../userdata ");
		if (dialog.open() != 32)
			return;
		
		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("Restore error");
			dialog.setMessage("no data for transfer, please reloadAllData");
			dialog.open();
			return;
		}
		
		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);
			String dest_mqlroot = meconf.getMqldata();
			String dest_profiles = meconf.getAppdata() + "\\profiles";
			String sourcedir_mql4 = meconf.getNetworkshare_INSTALLDIR() + "\\copyFromAppdata\\mql4";
			String sourcedir_profiles = meconf.getNetworkshare_INSTALLDIR() + "\\copyFromAppdata\\profiles";
			// in dem infofile steht von wo nach wo kopiert wurde
			String infofile = meconf.getNetworkshare_INSTALLDIR() + "\\copyFromAppdata\\info.txt";
			
			// prüft nach ob das zielverzeichniss den richtigen namen beinhaltet
			// ....\AppData\Roaming\MetaQuotes\Terminal\880772D848F4FEDB1FFFC14F4DD5B143
			if (dest_mqlroot.toLowerCase().contains("appdata") == false)
				continue;
			
			// prüft ob überhaupt was zum restoren da ist
			File appdataquelle = new File(meconf.getNetworkshare_INSTALLDIR() + "\\copyFromAppdata");
			if (appdataquelle.isDirectory() == false)
				continue;
			
			Inf inf = new Inf();
			inf.setFilename(infofile);
			
			// Kopiere die Verzeichnisse von mql4
			Filefunkt ff = new Filefunkt();
			try
			{
				ff.copyDir(new File(sourcedir_mql4), new File(dest_mqlroot));
				inf.writezeile("restored from <" + dest_mqlroot + "> successfull");
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Kopiere die Verzeichnisse von profiles
			try
			{
				ff.copyDir(new File(sourcedir_profiles), new File(dest_profiles));
				inf.writezeile("restored from <" + dest_profiles + "> successfull");
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("restored of userdata ready");
		dialog.open();
	}
	
	public void convertToPortable(Display dis, Brokerview brokerview)
	{
		
		// der userdatabereich aus dem userdata/454545454545/.... wird ins
		// metatradertrootverzeichniss kopiert
		// dies wird benötigt wenn man die metatrader von der Standartinstallation to
		// portable umwandeln möchte
		
		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to convert to portable");
		if (dialog.open() != 32)
			return;
		
		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("Backup error");
			dialog.setMessage("no data for transfer, please reloadAllData");
			dialog.open();
			return;
		}
		
		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);
			String source_mqlroot = meconf.getMqldata();
			String source_profiles = meconf.getAppdata() + "\\profiles";
			String destinstdir = meconf.getNetworkshare_INSTALLDIR();
			String infofile = meconf.getNetworkshare_INSTALLDIR() + "\\info.txt";
			
			// prüft nach ob das quellverzeichniss
			// ....\AppData\Roaming\MetaQuotes\Terminal\880772D848F4FEDB1FFFC14F4DD5B143
			if (source_mqlroot.toLowerCase().contains("appdata") == false)
				continue;
			
			Inf inf = new Inf();
			inf.setFilename(infofile);
			
			File destinfile = new File(destinstdir);
			File destmql4 = new File(destinstdir + "\\MQL4");
			File destprofiles = new File(destinstdir + "\\profiles");
			if (destinfile.isDirectory() == false)
				destinfile.mkdir();
			if (destmql4.isDirectory() == false)
				destmql4.mkdir();
			if (destprofiles.isDirectory() == false)
				destprofiles.mkdir();
			
			// Kopiere die Verzeichnisse von mql4
			Filefunkt ff = new Filefunkt();
			try
			{
				Mlist.add("backup from<" + source_mqlroot + "> to<" + destinstdir + ">", 1);
				ff.copyDir(new File(source_mqlroot), new File(destinstdir + "\\MQL4"));
				inf.writezeile("copied from <" + source_mqlroot + "> successfull");
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Kopiere die Verzeichnisse von profiles
			try
			{
				ff.copyDir(new File(source_profiles), new File(destinstdir + "\\profiles"));
				inf.writezeile("copied from <" + source_profiles + "> successfull");
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// renamed the sourcedir
			FileAccess.Rename(meconf.getAppdata() + "\\origin.txt",
					meconf.getAppdata() + "\\origin_converted_to_portable.txt");
			FileAccess.Rename(meconf.getAppdata(), meconf.getAppdata() + "_converted_to_portable");
			
		}
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage(
				"transfer of userdata ready \\n you must reconfig 'set metatraderdir' in the brokerconfig \\n");
		dialog.open();
	}
	
	public void genNewMetatraderaccount(String mtroot)
	{
		Networker_dep net = new Networker_dep();
		net.genNewMetatraderaccount(mtroot);
	}
	
	public String checkMetatraderrootverz(String verz)
	{
		// prüft ob dies ein metatraderroot verzeichniss ist
		// wenn das verzeichniss ok ist ist wird das verzeichniss zurückgemeldet
		// wenn es nicht ok ist wird es versucht zu korregieren und dann zurückgemeldet
		// null: im fehlerfall
		
		String fnam = null;
		File fnamf = null;
		
		// check1
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(verz, 0);
		int anz = fdyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			fnam = verz + "\\" + fdyn.holeFileSystemName() + "\\terminal.exe";
			fnamf = new File(fnam);
			
			// Metatrader gefunden
			if (fnamf.exists() == true)
				return verz;
		}
		
		// check2
		fnam = verz + "\\terminal.exe";
		fnamf = new File(fnam);
		// we choose wrong directory, I will correct this
		
		if (fnamf.exists())
		{
			// hier wird \\terminal.exe und das verzeichnis davor weggecutted umd auf das
			// rootverzeichnis zu kommen
			int index1 = 0;
			int index2 = fnam.lastIndexOf("\\");
			fnam = fnam.substring(index1, index2);
			index2 = fnam.lastIndexOf("\\");
			fnam = fnam.substring(index1, index2);
			return fnam;
		}
		
		return null;
		
	}
	
	public Boolean cleanTradecopyConfigs(Brokerview brokerview, Metaconfig meconfdemo)
	{
		// delete profiles for demobroker
		Profiler prof = new Profiler(meconfdemo);
		prof.delAllProfiles("FX Blue - TradeCopy Sender", null);
		
		// get the connected broker and delete profiles on realbroker
		String conrealbroker = meconfdemo.getconnectedBroker();
		
		Metaconfig meconfreal = brokerview.getMetaconfigByBrokername(conrealbroker);
		Profiler profreal = new Profiler(meconfreal);
		profreal.delAllProfiles("FX Blue - TradeCopy Receiver", meconfdemo.getBrokername());
		return true;
	}
	
	private void copyTradecopy(Metaconfig metaconf)
	{
		if (metaconf.getMttype().toLowerCase().equals("mt4"))
		{
			FileAccessDyn fd = new FileAccessDyn();
			String _quelleRec = Rootpath.getRootpath() + "\\install\\MT4_experts\\FX Blue - TradeCopy Receiver.ex4";
			String _quelleSend = Rootpath.getRootpath() + "\\install\\MT4_experts\\FX Blue - TradeCopy Sender.ex4";
			String _quellelib = Rootpath.getRootpath() + "\\install\\MT4_libraries\\FXBlueQuickChannel.dll";
			String _recfilter = Rootpath.getRootpath() + "\\install\\MT4_libraries\\CopierReceiverFilter.ex4";
			String _sendfilter = Rootpath.getRootpath() + "\\install\\MT4_libraries\\CopierSenderFilter.ex4";
			// String chr_quelle = Rootpath.getRootpath() +
			// "\\install\\MT4_profiles\\tradecopysender.chr";
			
			Tracer.WriteTrace(20, "I: copy tradcopier from<" + _quelleRec + "> and <" + _quelleSend + ">  to<"
					+ metaconf.getExpertdata() + ">");
			
			// Kopiere den Tradecopierer und Tradereceiver
			fd.copyFile2(_quelleRec, metaconf.getExpertdata() + "\\FX Blue - TradeCopy Receiver.ex4");
			fd.copyFile2(_quelleSend, metaconf.getExpertdata() + "\\FX Blue - TradeCopy Sender.ex4");
			fd.copyFile2(_quellelib, metaconf.getMqldata() + "\\libraries\\FXBlueQuickChannel.dll");
			
			// kopiere die filterfiles
			fd.copyFile2(_recfilter, metaconf.getMqldata() + "\\libraries\\CopierReceiverFilter.ex4");
			fd.copyFile2(_sendfilter, metaconf.getMqldata() + "\\libraries\\CopierSenderFilter.ex4");
			FileAccess.FileDelete(metaconf.getMqldata() + "\\Experts\\mqlcache.dat", 1);
		} else if (metaconf.getMttype().toLowerCase().equals("mt5"))
		{
			FileAccessDyn fd = new FileAccessDyn();
			String _quelleRec = Rootpath.getRootpath() + "\\install\\MT5_experts\\FX Blue - TradeCopy Receiver.ex5";
			String _quelleSend = Rootpath.getRootpath() + "\\install\\MT5_experts\\FX Blue - TradeCopy Sender.ex5";
			String _quellelib = Rootpath.getRootpath() + "\\install\\MT5_libraries\\FXBlueQuickChannel64.dll";
			String _recfilter = Rootpath.getRootpath() + "\\install\\MT5_libraries\\CopierReceiverFilter.ex5";
			String _sendfilter = Rootpath.getRootpath() + "\\install\\MT5_libraries\\CopierSenderFilter.ex5";
			// String chr_quelle = Rootpath.getRootpath() +
			// "\\install\\MT4_profiles\\tradecopysender.chr";
			
			Tracer.WriteTrace(20, "I: copy tradcopier from<" + _quelleRec + "> and <" + _quelleSend + ">  to<"
					+ metaconf.getExpertdata() + ">");
			
			// Kopiere den Tradecopierer und Tradereceiver
			fd.copyFile2(_quelleRec, metaconf.getExpertdata() + "\\FX Blue - TradeCopy Receiver.ex5");
			fd.copyFile2(_quelleSend, metaconf.getExpertdata() + "\\FX Blue - TradeCopy Sender.ex5");
			fd.copyFile2(_quellelib, metaconf.getMqldata() + "\\libraries\\FXBlueQuickChannel64.dll");
			
			// kopiere die filterfiles
			fd.copyFile2(_recfilter, metaconf.getMqldata() + "\\libraries\\CopierReceiverFilter.ex5");
			fd.copyFile2(_sendfilter, metaconf.getMqldata() + "\\libraries\\CopierSenderFilter.ex5");
			FileAccess.FileDelete(metaconf.getMqldata() + "\\Experts\\mqlcache.dat", 1);
			
		}
		
	}
	
	public void copyConfigTradecopyConfs(Metaconfig metaconfdemo, Metaconfig metaconfreal)
	{
		// hier wird nur rüberkopiert, also die *.chr files auf sender und
		// empfängerseite
		
		String _quelleSender = null;
		String _quelleReceiver=null;
		
		String brokername = metaconfdemo.getBrokername();
		FileAccessDyn fd = new FileAccessDyn();
	
		// 1) TradecopySender.chr wird rüberkopiert
		Profiler prof1 = new Profiler(metaconfdemo);
		String fnam1 = prof1.getFreeChartName();
		String senderconfigfile=null;
		if(metaconfdemo.getMttype().toLowerCase().equals("mt4"))
		{
			senderconfigfile = metaconfdemo.getAppdata() + "\\profiles\\default\\" + fnam1;
			_quelleSender = Rootpath.getRootpath() + "\\install\\MT4_profiles\\TradeCopySender.chr";
			_quelleReceiver = Rootpath.getRootpath() + "\\install\\MT4_profiles\\TradeCopyReceiver.chr";
		}
		else if(metaconfdemo.getMttype().toLowerCase().equals("mt5"))
		{
			senderconfigfile = metaconfdemo.getAppdata() + "\\mql5\\profiles\\charts\\default\\" + fnam1;
			_quelleSender = Rootpath.getRootpath() + "\\install\\MT5_profiles\\TradeCopySender.chr";
			_quelleReceiver = Rootpath.getRootpath() + "\\install\\MT5_profiles\\TradeCopyReceiver.chr";
		}
		else
			Tracer.WriteTrace(10, "E: only mt4 or mt5 allowed -> stop");
		
		if (fd.copyFile2(_quelleSender, senderconfigfile) == false)
			Tracer.WriteTrace(10,
					"E:copyconfig:cant copy file from<" + _quelleSender + "> to<" + senderconfigfile + "> ");
		// 1.1)patch the channel
		ChrFile chrdemo = new ChrFile();
		chrdemo.setFilename(senderconfigfile);
		chrdemo.patchTradecopyChannel(brokername);
		chrdemo.writeMemFile(null);
		
		// 2) TradecopyReceiver.chr wird rüberkopiert
		Profiler prof2 = new Profiler(metaconfreal);
		String fnam2 = prof2.getFreeChartName();
		
		String empfaengerconfigfile=null;

		if(metaconfdemo.getMttype().toLowerCase().equals("mt4"))
			empfaengerconfigfile = metaconfreal.getAppdata() + "\\profiles\\default\\" + fnam2;
		else if(metaconfdemo.getMttype().toLowerCase().equals("mt5"))
			empfaengerconfigfile = metaconfreal.getAppdata() + "\\mql5\\profiles\\charts\\default\\" + fnam2;
		
		if (fd.copyFile2(_quelleReceiver, empfaengerconfigfile) == false)
			Tracer.WriteTrace(10,
					"E:copyconfig:cant copy file from<" + _quelleReceiver + "> to<" + empfaengerconfigfile + "> ");
		// 2.1)patch the channel
		ChrFile chrreal = new ChrFile();
		chrreal.setFilename(empfaengerconfigfile);
		chrreal.patchTradecopyChannel(brokername);
		chrreal.writeMemFile(null);
		
		// clean the cache
		FileAccess.FileDelete(metaconfdemo.getMqldata() + "//Experts//mqlcache.dat", 1);
		FileAccess.FileDelete(metaconfreal.getMqldata() + "//Experts//mqlcache.dat", 1);
		FileAccess.FileDelete(metaconfdemo.getMqldata() + "//experts.dat", 1);
		FileAccess.FileDelete(metaconfreal.getMqldata() + "//experts.dat", 1);
		
		
	}
}
