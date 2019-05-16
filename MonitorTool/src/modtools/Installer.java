package modtools;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import mqlLibs.eaclass;
import mtools.DisTool;
import mtools.MboxQuest;
import mtools.Mlist;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;

import FileTools.Filefunkt;
import Metriklibs.FileAccessDyn;
import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.Ea;
import data.Ealiste;
import data.GlobalVar;
import data.Metaconfig;
import data.Profit;
import data.Rootpath;

public class Installer
{
	// die Installerklasse ist für die Installation zuständig
	// hier werden die mql-File, history exporter kopiert und upgedated

	public void UpdateHistoryExporter(Display dis, Brokerview brokerview)
	{
		FileAccessDyn fd = new FileAccessDyn();
		DisTool.waitCursor();
		// in dieser funktion wird nur der neue HistoryExporter kopiert
		// ausserdem die neue monitorlib
		// weiterhin das RestartTerminal.bat
		int anz = brokerview.getAnz();

		if (anz == 0)
		{
			Mbox.Infobox("please Reaload all Data first !!");
			return;
		}

		//gehe durch alle Broker und update die Historyexporter
		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);
			String bnam = meconf.getBrokername();
			Mlist.add("update<" + bnam + ">", 1);

			String histexporter_quelle = Rootpath.getRootpath()
					+ "\\install\\MT4_experts\\historyexporter.mq4";
		
			String monitorlib_quelle = Rootpath.getRootpath()
					+ "\\install\\MT4_Include\\monitorlib.mqh";
			String restart_quelle = Rootpath.getRootpath()
					+ "\\install\\RestartTerminal.bat";

			
			
			// Kopiere den historyexporter
			fd.copyFile3(histexporter_quelle, meconf.getExpertdata()
					+ "//historyexporter.mq4",1);
		

			// kopiere die Monitorlib
			fd.copyFile3(monitorlib_quelle, meconf.getMqldata()
					+ "//include//monitorlib.mqh",1);

			FileAccess.FileDelete(meconf.getMqldata()
					+ "//include//monitorlib.ex4", 0);

			FileAccess.FileDelete(meconf.getMqldata()
					+ "//Experts//mqlcache.dat", 1);
			
		}
		DisTool.arrowCursor();
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("Der Historyexporter wurde upgedated bitte den Metatrader");
		dialog.open();
	}

	private void copyInstHistoryExporter(Metaconfig metaconf)
	{
		FileAccessDyn fd = new FileAccessDyn();

		String histexporter_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_experts\\historyexporter.mq4";
		String histexporterchr_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_profiles\\historyexporter.chr";
		

		Tracer.WriteTrace(20, "I: copy histroyexporter from<"
				+ histexporter_quelle + "> to<" + metaconf.getExpertdata()
				+ ">");

		// Kopiere den historyexporter
		fd.copyFile2(histexporter_quelle, metaconf.getExpertdata()
				+ "//historyexporter.mq4");
	

		FileAccess.FileDelete(metaconf.getMqldata()
				+ "//Experts//mqlcache.dat", 1);
		
		Tracer.WriteTrace(20, "I: special profile1");
		Profiler profiler = new Profiler(metaconf);
		profiler.createSpecialProfile(histexporterchr_quelle, "historyexporter");
		profiler.checkDoubleEa("historyexporter", metaconf.getExpertdata());
		Tracer.WriteTrace(20, "I: special profile2");
	}

	private void copyTickdataExporter(Metaconfig metaconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		String histexporter_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_experts\\GenBuilder_TickDataExportEA.mq4";
		String histexporterchr_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_profiles\\GenBuilder_TickDataExportEA.chr";

		Tracer.WriteTrace(20, "I:copy tickdataexporter from<"
				+ histexporter_quelle + "> to<" + metaconf.getExpertdata()
				+ ">");

		// Kopiere den Tickdataexporter
		fd.copyFile2(histexporter_quelle, metaconf.getExpertdata()
				+ "//GenBuilder_TickDataExportEA.mq4");

		Profiler profiler = new Profiler(metaconf);
		profiler.createSpecialProfile(histexporterchr_quelle,
				"GenBuilder_TickDataExportEA");

	}

	

	private void copyMyFxbookEa(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		String myfxbookea = Rootpath.getRootpath()
				+ "\\install\\MT4_experts\\Myfxbook.ex4";
		fd.copyFile2(myfxbookea, meconf.getExpertdata() + "//Myfxbook.ex4");

		String expertdll_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_experts\\libraries\\Myfxbook.dll";

		Tracer.WriteTrace(20, "I: copy myfxbookea from<" + myfxbookea + "> to<"
				+ meconf.getAppdata() + ">");

		// funktioniert nur noch mit mt600+
		fd.copyFile2(expertdll_quelle, meconf.getMqldata()
				+ "//libraries//Myfxbook.dll");
		
		FileAccess.FileDelete(meconf.getMqldata()
				+ "//libraries//mqlcache.dat", 1);

		// diese config muss mit password gepatched werden
		InstalliereMyFxbookConfig(meconf);
	}

	private void copyMonitorlib(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		String monitorlib_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_Include\\monitorlib.mqh";

		Tracer.WriteTrace(20, "I: copy monitorlib from<" + monitorlib_quelle
				+ "> to<" + meconf.getMqldata() + "//include//" + ">");

		// Kopiere den historyexporter
		fd.copyFile2(monitorlib_quelle, meconf.getMqldata()
				+ "//include//monitorlib.mqh");

	}

	private void loescheAltes(String quellverz, Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		// Das alte löschen
		Mlist.add("I:delete all mql on destbroker <" + meconf.getMqldata()
				+ ">", 1);
		FileAccess.FilesDelete(quellverz + "\\realinstall", ".mq4");
		FileAccess.FilesDelete(quellverz + "\\install", ".mq4");
		FileAccess.FilesDelete(meconf.getExpertdata(), ".mq4");
		FileAccess.FilesDelete(meconf.getExpertdata(), ".ex4");
		FileAccess.FilesDelete(meconf.getExpertdata(), ".log");
		FileAccess.FilesDelete(meconf.getAppdata() + "//profiles\\default",
				".chr");
		FileAccess.FilesDelete(meconf.getExpertdata(), ".dat");
	}

	private void kopiereIndikatoren(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		Mlist.add("I:install indicators", 1);
		String indiverz_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_indikatoren";
		String inidverz_ziel = meconf.getMqldata() + "//indicators";
		Tracer.WriteTrace(20, "I:copy indicators from <" + indiverz_quelle
				+ "> to <" + inidverz_ziel + ">");
		FileAccess.CopyDirectory(indiverz_quelle, inidverz_ziel, ".mq4");
	}

	private void InstallSernumber(Metaconfig meconf)
	{
		FileAccessDyn fd = new FileAccessDyn();
		Inf inf = new Inf();
		Tracer.WriteTrace(20, "I: inst sernumber in<" + meconf.getAppdata()
				+ "//monitor.info" + ">");
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

		}
		Mbox.Infobox("installation ready");
	}

	private void kopiereEaSysteme(String mqlquellverz,
			ProgressBar progressBar1, Metaconfig metaconfig,
			Metaconfig metarealconfig, String zwischenspeichernam,
			int realpatchflag, Ealiste eal, Tableview tv)
	{
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

		String cfg_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_profiles\\chrmaster.chr";

		Tracer.WriteTrace(20, "Rootpath<" + Rootpath.getRootpath()
				+ "> mqlquellverz<" + mqlquellverz + ">");
		// hole die Mq4-files ins temporäre install verzeichniss
		fadyn.initFileSystemList(mqlquellverz, 1);
		int anz = fadyn.holeFileAnz();
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

			// import tradeliste falls eine da ist
			CheckTradeListenImport(tv, metaconfig.getMqlquellverz() + "\\"
					+ mqlquellnam, metaconfig.getBrokername());

			// hier werden nur mq4-files verarbeitet
			if (mqlquellnam.contains(".mq4") == false)
				continue;

			// den mqlnamen bestimmen
			mqlnam = mqlquellnam.substring(0, mqlquellnam.indexOf(".mq4"));

			Tracer.WriteTrace(20, "mqlquellnam<" + mqlquellnam
					+ "> mqlquellverz<" + mqlquellverz + "> zielshare<"
					+ metaconfig.getMqldata() + "> zwischenspeichernam<"
					+ zwischenspeichernam + ">");

			int magic = eaclass.calcMagic(mqlquellnam);
			String brokername = metaconfig.getBrokername();

			Ea ea = eaAufnahme(eal, mqlquellnam, magic, brokername);

			// hier wird das Mql-File geschrieben

			MqlPatch mqlp = writeMqlFileZwischenspeicher(mqlquellnam,
					mqlquellverz, zwischenspeichernam, realpatchflag,
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
			
			
			if(realpatchflag==1)
			  profiler.createProfile(mqlp, cfg_quelle, ea,metarealconfig);
			else
				 profiler.createProfile(mqlp, cfg_quelle, ea,metaconfig);

		}
		eal.store(0);
		if (realpatchflag == 0)
			loescheMqlCache(metaconfig.getMqldata());
		else
			loescheMqlCache(metarealconfig.getMqldata());
	}

	private void loescheMqlCache(String verz)
	{
		String cachenam = verz + "\\experts\\mqlcache.dat";
		File f = new File(cachenam);
		if (f.exists())
			f.delete();
	}

	private void CheckTradeListenImport(Tableview tv, String mqlquellnam,
			String brokername)
	{
		String fnam = mqlquellnam.replace(".mq4", ".tradeliste");

		if ((fnam.contains(".tradeliste")) && (FileAccess.FileAvailable(fnam)))
			tv.importTradeliste(fnam, brokername);
	}

	private Ea eaAufnahme(Ealiste eal, String mqlquellnam, int magic,
			String brokername)
	{

		mqlquellnam = mqlquellnam.replace("_Strategy", "");

		// der ea wird in der ealiste aufgenommen wenn er noch nicht drin ist
		Ea ea = eal.getEa(magic, brokername);
		if (ea != null)
			ea.setEafilename(mqlquellnam);
		else
		{
			// in die ealiste aufnehmen
			ea = new Ea();
			ea.setMagic(magic);
			ea.setBroker(brokername);
			ea.setEafilename(mqlquellnam);
			eal.add(ea);
		}
		return ea;
	}



	public boolean InstalliereEinRealEaSystemFromDemobroker(
			Tableview tableview, Profit prof, Metaconfig meconf,
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

		String cfg_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_profiles\\chrmaster.chr";

		Ea ea = eal.getEa(magic, meconf.getBrokername());

		String mqlquellnam = ea.getEafilename();
		if (mqlquellnam == null)
		{
			Mbox.Infobox("Error: ea installation not correct magic<" + magic
					+ "> broker<" + meconf.getBrokername()
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
		eal.setInstFrom(magic, meconf.getconnectedBroker(),
				meconf.getBrokername());

		// Schreibt das mq4-File
		MqlPatch mqlp = writeMqlInstOneRealsystem(mqlquellnam, quellverz,
				quellinstallverz, zielverz, meconf, meRealconf, ea);

		// lösche den mqlcache.dat
		String zielfile = zielverz + "//mqlcache.dat";
		File fmql = new File(zielfile);
		if (fmql.exists())
			fmql.delete();

		// Schreibt das Metatrader generierte cfg.file
		if (mqlp != null)
		{
			// schreibt das cfg.file
			
			profiler.createProfile(mqlp, cfg_quelle, ea,meRealconf);

			return true;
		} else
			return false;

	}

	public void InstalliereMyFxbookConfig(Metaconfig meconf)
	{
		String cfg_quelle = Rootpath.getRootpath()
				+ "\\install\\MT4_profiles\\chrmaster.chr";

		String zielsharename = meconf.getAppdata()
				+ "\\profiles\\default\\myfxbook.chr";

		// prüft nach ob myfxbook schon da ist
		Profiler profiler = new Profiler(meconf);
		if (profiler.getanzProfiles("myfxbook") > 0)
			return;

		// dann schreibe myfxbook.chr
		MqlPatch mfx = new MqlPatch();
		mfx.setFilename(cfg_quelle);
		mfx.setExpertname("myfxbook");
		mfx.patchMyFxbookEa();
		mfx.writeMemFile(zielsharename);

		// dann ein rename von myfxbook.chr nach chartXX.chr
		File myfxbookname = new File(zielsharename);
		File newname = new File(meconf.getAppdata() + "\\profiles\\default"
				+ profiler.getFreeChartName());
		myfxbookname.renameTo(newname);
	}

	public void InstallMetatraderDemoEaFiles(Display dis,
			ProgressBar progressBar1, String mqlquellverz,
			Metaconfig metaconfig, Metaconfig metarealconfig, Ealiste eal,
			Tableview tv)
	{
		// quellverz: ist das mql-Quellverzeichniss wo sich die ea´s befinden
		// zielshare: ist der zielshare
		// metaconfig: ist das metatrader konfigfile aus dem benutzermenue

		// install-verzeichniss erzeugen
		FileAccess.checkgenDirectory(mqlquellverz + "\\install");

		loescheAltes(mqlquellverz, metaconfig);
		InitMetatrader(metaconfig, 1);
		kopiereEaSysteme(mqlquellverz, progressBar1, metaconfig,
				metarealconfig, "install", 0, eal, tv);
		metaconfig.setInstallationstatus(1);
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

		if (metaconfig.isInstmyfxbookea())
			copyMyFxbookEa(metaconfig);

		if (metaconfig.isInsttickdataexporter())
			copyTickdataExporter(metaconfig);

		

		InstallSernumber(metaconfig);

		copyMonitorlib(metaconfig);
		kopiereIndikatoren(metaconfig);
		metaconfig.setInstallationstatus(1);
	}

	public void InstallMetatraderRealEaFiles(Display dis,
			ProgressBar progressBar1, String mqlquellverz, String zielshare,
			Metaconfig metaconfig, Metaconfig metarealconfig, Ealiste eal,
			Tableview tv)
	{
		// quellverz: ist das mql-Quellverzeichniss wo sich die ea´s befinden
		// zielshare: ist der zielshare
		// metaconfig: ist das metatrader konfigfile aus dem benutzermenue

		// install-verzeichniss erzeugen
		FileAccess.checkgenDirectory(mqlquellverz + "\\realinstall");

		InitMetatrader(metarealconfig, 1);
		kopiereEaSysteme(mqlquellverz, progressBar1, metaconfig,
				metarealconfig, "realinstall", 1, eal, tv);

		ready(dis);
		return;
	}

	public void cleanRealAccount(Display dis, ProgressBar progressBar1,
			String mqlquellverz, Metaconfig metaconfig_real,
			Metaconfig metaconfig)
	{
		// alle installierten EA´s werden auf dem Realaccount gelöscht
		// ebenfalls die konfigurationen

		int lotsizedelflag = 0;
		lotsizedelflag = MboxQuest
				.Questbox("Do you want to delete the lostsize ?");

		// alle experts löschen
		FileAccess.initFileSystemList(metaconfig_real.getMqldata()
				+ "\\experts", 1);
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
			if (FileAccess.FileAvailable0(metaconfig_real.getMqldata()
					+ "\\experts\\" + fnam))
				FileAccess.FileDelete(metaconfig_real.getMqldata()
						+ "\\experts\\" + fnam, 1);
		}

		// alle on-off löschen
		FileAccess.initFileSystemList(metaconfig_real.getMqldata() + "\\files",
				1);
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
			if (new File(metaconfig_real.getMqldata() + "\\files\\" + fnam)
					.exists() == true)
				FileAccess.FileDelete(metaconfig_real.getMqldata()
						+ "\\files\\" + fnam, 1);
		}

		// alle configs löschen
		FileAccess.initFileSystemList(metaconfig_real.getAppdata()
				+ "\\profiles\\default", 1);
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
			if (new File(metaconfig_real.getAppdata() + "\\profiles\\default\\"
					+ fnam).exists() == true)
				FileAccess.FileDelete(metaconfig_real.getAppdata()
						+ "\\profiles\\default\\" + fnam, 1);
		}

	}

	private MqlPatch writeMqlInstOneRealsystem(String eanam, String quellverz,
			String quellinstallverz, String zielverz, Metaconfig meconf,
			Metaconfig meRealconf, Ea ea)
	{

		FileAccessDyn fd = new FileAccessDyn();
		// Schribt das jeweilige mql-File
		MqlPatch mqlpatch = new MqlPatch();
		// entferne schmutz im namen

		// mq4 einlesen, modifizieren und im install ablegen und auf das Share
		// kopieren
		if (FileAccess.FileAvailable(quellverz + "\\" + eanam) == false)
		{
			Mbox.Infobox("file <" + quellverz + "\\" + eanam
					+ "> not available");
			return null;
		}

		mqlpatch.setFilename(quellverz + "\\" + eanam);
		mqlpatch.setExpertname(eanam.replace("_Strategy", ""));

		String mqlzielnam = quellinstallverz + "\\"
				+ eanam.replace("_Strategy", "");

		// Die magic berechnen
		int magic = eaclass.calcMagic(eanam);
		mqlpatch.setMagic(magic);

		mqlpatch.patchMqlProtection();
		mqlpatch.patchInit();
		mqlpatch.patchVariables();
		mqlpatch.patchGBMeta600();

		// abschaltautomatik hinzufügen
		mqlpatch.addAbschaltAutomatic("");
		mqlpatch.patchLotsize(ea, meRealconf);

		if (mqlpatch.isDaxEA() == true)
			mqlpatch.patchDaxEA();

		mqlpatch.patchSleeptimemod();
		//add the postcode from mt4_additional code to every ea
		//sq3.8.2=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq3
		//sq4=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq4
		String postfilename = Rootpath.getRootpath()
					+ "\\install\\Mt4_additionalcode\\ea_postcode";
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

	private MqlPatch writeMqlFileZwischenspeicher(String quellnam,
			String quellverz, String zwischenspeichernam, int realpatchflag,
			Metaconfig meconf, Metaconfig meRealconf, Ea ea)
	{
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
			Mbox.Infobox("file <" + quellverz + "\\" + quellnam
					+ "> not available");
			return null;
		}

		mqlpatch.setFilename(quellverz + "\\" + quellnam);
		// mqlpatch.setLotsize_depricated(String.valueOf(lotsize));

		mqlpatch.setExpertname(quellnam.replace("_Strategy", ""));

		String mqlziel = quellverz + "\\" + zwischenspeichernam + "\\"
				+ quellnam.replace("_Strategy", "");

		// Die magic berechnen
		int magic = eaclass.calcMagic(quellnam);
		mqlpatch.setMagic(magic);
		mqlpatch.patchMqlProtection();
		mqlpatch.patchInit();
		mqlpatch.patchVariables();
		mqlpatch.patchGBMeta600();
		mqlpatch.patchSleeptimemod();
	
		
		if (realpatchflag == 1)
		{
			mqlpatch.addAbschaltAutomatic("");
			mqlpatch.patchLotsize(ea, meRealconf);
		} else
			mqlpatch.patchLotsize(ea, meconf);

		if (mqlpatch.isDaxEA() == true)
			mqlpatch.patchDaxEA();
		
		//add the postcode from mt4_additional code to every ea
		//sq3.8.2=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq3
		//sq4=C:\Forex\monitorDevelop2\Install\Mt4_additionalcode\ea_postcode.sq4
		String postfilename = Rootpath.getRootpath()
				+ "\\install\\Mt4_additionalcode\\ea_postcode";
		mqlpatch.addPostcode(postfilename);
		
		mqlpatch.writeMemFile(mqlziel);

		if (realpatchflag == 1)
			zielverzexpert = meRealconf.getExpertdata();
		else
			zielverzexpert = meconf.getExpertdata();

		// Hier wird auf dem share kopiert
		// mql installieren
		fd.copyFile2(mqlziel, zielverzexpert + "//" + mqlpatch.getExpertname());

		// und das ex4 löschen
		String ex4filename = zielverzexpert + "//" + mqlpatch.getExpertname();
		File ex4file = new File(ex4filename.replace(".mq4", ".ex4"));
		if (ex4file.exists())
			ex4file.delete();

		Mlist.add("I: write mql-file <" + quellnam + ">", 1);

		return mqlpatch;
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
				fd.copyFile2(quellverz + "\\" + fnam_org, zielverz + "\\"
						+ fnam_org);
				copyflag = 1;
			}

		}

		if (copyflag == 0)
			Mbox.Infobox("EA magic<" + magic + "> not available in <"
					+ quellverz + ">");
	}

	public void backup(Display dis, Brokerview brokerview)
	{
		FileAccessDyn fd = new FileAccessDyn();
		// von den history.txt files wird ein backup angelegt
		int anz = brokerview.getAnz();

		if (anz == 0)
		{
			MessageBox dialog = new MessageBox(dis.getActiveShell(),
					SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("Backup error");
			dialog.setMessage("no data for backup, please reloadAllData");
			dialog.open();
			return;
		}

		for (int i = 0; i < anz; i++)
		{
			// holt sich eine config für den metatrader
			Metaconfig meconf = brokerview.getElem(i);

			String zielshare = meconf.getFiledata();
			String quellfile = zielshare + "//history.txt";
			String backupfile = zielshare + "//history_"
					+ Tools.entferneZeit(Tools.get_aktdatetime_str()) + ".txt";

			// Kopiere den historyexporter
			File qfile = new File(quellfile);
			if (qfile.exists())
				fd.copyFile2(quellfile, backupfile);

		}
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("backup of history.txt ready");
		dialog.open();
	}

	public void transfer(Display dis, Brokerview brokerview)
	{
		FileAccessDyn fd = new FileAccessDyn();
		// von den history.txt wird nach history_transfer kopiert
		// das wird dann benötigt wenn man einen neuen Metatrader installiert
		// aber die alte
		// historie noch behalten möchte

		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to transfer historyexporter.txt to historyexporter_transfer.txt ");
		if (dialog.open() != 32)
			return;

		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
					| SWT.OK);
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
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
				| SWT.OK);
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
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to transfer all userdata ");
		if (dialog.open() != 32)
			return;

		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
					| SWT.OK);
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
			String dest = meconf.getNetworkshare_INSTALLDIR()
					+ "\\copyFromAppdata";
			String infofile=meconf.getNetworkshare_INSTALLDIR()
					+ "\\copyFromAppdata\\info.txt";

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
			if (destprofiles.isDirectory()==false)
				destprofiles.mkdir();

			// Kopiere die Verzeichnisse von mql4
			Filefunkt ff = new Filefunkt();
			try
			{
				Mlist.add("backup from<"+source_mqlroot+"> to<"+dest+">", 1);
				ff.copyDir(new File(source_mqlroot), new File(dest+"\\MQL4" ));
				inf.writezeile("copied from <"+source_mqlroot+"> successfull" );
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
				inf.writezeile("copied from <"+source_profiles+"> successfull" );
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
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
				| SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("transfer of userdata ready");
		dialog.open();
	}

	public void restoreuserdata(Display dis, Brokerview brokerview)
	{
		// der bereich \\copyFromAppdata.... wird in userdata
		// userdata/454545454545/.... zurückkopiert
		// Diesen Vorgang macht man wenn man die Metatraderverzeichnisse auf einem
		// neuen Rechner von Hand kopiert hat und den Userdatabereich noch restoren möchte

		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to restore all userdata from ..\\copyFromAppdata to ../userdata ");
		if (dialog.open() != 32)
			return;

		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
					| SWT.OK);
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
			String sourcedir_mql4 = meconf.getNetworkshare_INSTALLDIR()
					+ "\\copyFromAppdata\\mql4";
			String sourcedir_profiles = meconf.getNetworkshare_INSTALLDIR()
					+ "\\copyFromAppdata\\profiles";
			//in dem infofile steht von wo nach wo kopiert wurde
			String infofile=meconf.getNetworkshare_INSTALLDIR()
					+ "\\copyFromAppdata\\info.txt";

			// prüft nach ob das zielverzeichniss den richtigen namen beinhaltet
			// ....\AppData\Roaming\MetaQuotes\Terminal\880772D848F4FEDB1FFFC14F4DD5B143
			if (dest_mqlroot.toLowerCase().contains("appdata") == false)
				continue;
			
			//prüft ob überhaupt was zum restoren da ist
			File appdataquelle=new File(meconf.getNetworkshare_INSTALLDIR()+ "\\copyFromAppdata");
			if(appdataquelle.isDirectory()==false)
				continue;
			
			Inf inf = new Inf();
			inf.setFilename(infofile);

			// Kopiere die Verzeichnisse von mql4
			Filefunkt ff = new Filefunkt();
			try
			{
				ff.copyDir( new File(sourcedir_mql4),new File(dest_mqlroot));
				inf.writezeile("restored from <"+dest_mqlroot+"> successfull" );
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
				ff.copyDir( new File(sourcedir_profiles ),new File(dest_profiles));
				inf.writezeile("restored from <"+dest_profiles+"> successfull" );
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
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
				| SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("restored of userdata ready");
		dialog.open();
	}
	
	public void convertToPortable(Display dis, Brokerview brokerview)
	{
		
		// der userdatabereich aus dem userdata/454545454545/.... wird ins
		// metatradertrootverzeichniss kopiert
		// dies wird benötigt wenn man die metatrader von der Standartinstallation to portable umwandeln möchte
		

		// Sicherheitsabfrage
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to convert to portable");
		if (dialog.open() != 32)
			return;

		int anz = brokerview.getAnz();
		if (anz == 0)
		{
			dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
					| SWT.OK);
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
			String infofile=meconf.getNetworkshare_INSTALLDIR()
					+ "\\info.txt";

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
			if (destprofiles.isDirectory()==false)
				destprofiles.mkdir();

			// Kopiere die Verzeichnisse von mql4
			Filefunkt ff = new Filefunkt();
			try
			{
				Mlist.add("backup from<"+source_mqlroot+"> to<"+destinstdir+">", 1);
				ff.copyDir(new File(source_mqlroot), new File(destinstdir+"\\MQL4" ));
				inf.writezeile("copied from <"+source_mqlroot+"> successfull" );
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
				inf.writezeile("copied from <"+source_profiles+"> successfull" );
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//renamed the sourcedir
			FileAccess.Rename(meconf.getAppdata()+"\\origin.txt", meconf.getAppdata()+"\\origin_converted_to_portable.txt");
			FileAccess.Rename(meconf.getAppdata(), meconf.getAppdata()+"_converted_to_portable");
			
		}
		dialog = new MessageBox(dis.getActiveShell(), SWT.ICON_QUESTION
				| SWT.OK);
		dialog.setText("My info");
		dialog.setMessage("transfer of userdata ready \\n you must reconfig 'set metatraderdir' in the brokerconfig \\n");
		dialog.open();
	}

	
	
	public void genNewMetatraderaccount(String mtroot)
	{
		Networker net = new Networker();
		net.genNewMetatraderaccount(mtroot);
	}

	public boolean checkMetatraderrootverz(String verz)
	{
		// prüft ob dies ein metatraderroot verzeichniss ist
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(verz, 0);
		int anz = fdyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = verz + "\\" + fdyn.holeFileSystemName()
					+ "\\terminal.exe";
			File fnamf = new File(fnam);

			// Metatrader gefunden
			if (fnamf.exists() == true)
				return true;
		}
		return false;

	}
}
