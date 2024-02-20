package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.ToolsException;
import hiflsklasse.Tracer;

import java.io.File;
import java.io.IOException;

import mtools.Mlist;
import Metriklibs.FileAccessDyn;
import datefunkt.Mondate;

public class Metaconfig implements Comparable<Metaconfig>
{
	String networkshare;
	String brokername;
	// 0= monitor, 1 = gbautomatic, 2=Real, 3=FST, 4=autocreator
	int accounttype = 0;
	String mqlquellverz = "empty";
	String connectedBroker;
	String desciption = "descipe trading strategies here";
	String infostring = null;
	String daxname = null;
	String fstdir = null;
	boolean magiclistactive = false;
	// dies ist der string der die eingeschalteten magics für den tradecopierer
	// beinhaltet
	String magicliststring = "1234=12345,123456";
	boolean storetrades = false;
	int validitydays = 99999999;
	// warningflag wird gesetzt wenn die brokertime abläuft
	// bei begrenzten demoaccount
	int warningflag = 0;
	// falls pongflag ==1 dann hat der metatrader in den letzten 5 minuten nicht
	// geantwortet
	int pongerrorflag = 0;
	// status=1 installiert,status=0 initial
	int installationstatus = 0;
	int on = 1;
	double lotsize = 0.01;
	boolean insttickdataexporter = false;
	boolean insthistoryexporter = true;
	boolean instmyfxbookea = false;
	boolean insttradecopy = true;
	int brokerid = 909;
	private Magiclist maglist = null;
	private String metaversion = "";
	
	// Appdata ist das rootverzeichniss auf dem Roaming
	private String Appdata = "";
	// mqldata ist das mqldata auf dem roaming
	private String Mqldata = "";
	// expertdata verzeichniss wo sich die experts befinden
	private String Expertdata = "";
	// filedata, wo sich die files befinden
	private String Filedata = "";
	// some broker use currency.postfix for currency pair
	private String curpostfix = "";
	// only handinstalled flag, if 1= than the eas are never modified by the tool
	private int onlyhandinstalled = 0;
	
	static String[] accounttypename =
	{ "Mon", "Aut", "Real", "FST", "AC" };
	
	// datum des ersten trades
	String datumDesErstenTrades = null;
	
	// hier kommt der process hin
	private Process processkennung = null;
	
	// Frequenteaupdatedir
	// dies ist das rootverzeichniss wo die wiederholenden (wöchtentlich) neuen EA
	// befinden
	private String frequenteaupdatedir = "";
	
	// hier wird das frequenteaupdate ein/aus geschaltet
	private int usefrequenteaupdateflag = 0;
	
	// falls es geünscht ist das der ea vor der installation renamed wird
	private String earenametext = "";
	
	// hiermit wird das earename ein/ausgeschaltet
	private int useearenametextflag = 0;
	
	private int usemyfxbookflag = 0;
	private int usetradecopyflag = 0;
	private int usefxblueflag = 0;
	private String histexportcurrency = "EURUSD";
	private int closefridayflag = 0;
	private int showonlyinstalledeas = 0;
	private int tradecopymagic = 0;
	private String suffix = "";
	private int accountlocked = 0;
	// time of the last installation of eas
	private String lastinstallation = "";
	// automatic symbolreplacement
	private int automaticsymbolreplacement = 0;
	private String tradesuffixsender = "";
	// mttype=mt4 or mt5
	private String mttype = "";
	// AucPrefix is the prefix for autcreator copy ea
	private String aucPrefix = "";
	// Autocreatorpathmode: 1=default, 2= newestdir, 3= use this path
	private int autocreatorpathmode = 1;
	// dies ist der path wo das monitortool die generierten eas sucht
	private String autocreatorpath = "";
	private String autocreatorbackuppath = "";
	private int useautocreatorbackup = 1;
	private String autocreatortestdir = "";
	private int Mt5bugfixing=0;
	private int EodModification=0;
	private int CustomCommentFlag=0;
	private String CustomCommentText="";
	
	public Metaconfig(String mname)
	{
		String zeile = new String(mname);
		int trennanz = SG.countZeichen(zeile, "#");
		try
		{
			networkshare = new String(SG.nteilstring(zeile, "#", 1));
			brokername = new String(SG.nteilstring(zeile, "#", 2));
			accounttype = SG.get_zahl(SG.nteilstring(zeile, "#", 3));
			mqlquellverz = new String(SG.nteilstring(zeile, "#", 4));
			connectedBroker = new String(SG.nteilstring(zeile, "#", 5));
			desciption = new String(SG.nteilstring(zeile, "#", 6));
			infostring = new String(SG.nteilstring(zeile, "#", 7));
			daxname = new String(SG.nteilstring(zeile, "#", 8));
			fstdir = new String(SG.nteilstring(zeile, "#", 9));
			int val = SG.get_zahl(SG.nteilstring(zeile, "#", 10));
			if (val == 0)
				magiclistactive = true;
			else
				magiclistactive = false;
			magicliststring = new String(SG.nteilstring(zeile, "#", 11));
			
			if (trennanz > 11)
				metaversion = new String(SG.nteilstring(zeile, "#", 12));
			if (trennanz > 12)
				Appdata = new String(SG.nteilstring(zeile, "#", 13));
			if (trennanz > 13)
				Mqldata = new String(SG.nteilstring(zeile, "#", 14));
			if (trennanz > 14)
				Expertdata = new String(SG.nteilstring(zeile, "#", 14));
			if (trennanz > 15)
				Filedata = new String(SG.nteilstring(zeile, "#", 15));
			if (trennanz > 16)
				curpostfix = new String(SG.nteilstring(zeile, "#", 16));
			if (trennanz > 17)
				onlyhandinstalled = SG.get_zahl(SG.nteilstring(zeile, "#", 17));
			if (trennanz > 18)
				frequenteaupdatedir = SG.nteilstring(zeile, "#", 18);
			if (trennanz > 19)
				usefrequenteaupdateflag = SG.get_zahl(SG.nteilstring(zeile, "#", 19));
			if (trennanz > 20)
				earenametext = SG.nteilstring(zeile, "#", 20);
			if (trennanz > 21)
				useearenametextflag = SG.get_zahl(SG.nteilstring(zeile, "#", 21));
			if (trennanz > 22)
				usemyfxbookflag = SG.get_zahl(SG.nteilstring(zeile, "#", 22));
			if (trennanz > 23)
				usetradecopyflag = SG.get_zahl(SG.nteilstring(zeile, "#", 23));
			if (trennanz > 24)
				usefxblueflag = SG.get_zahl(SG.nteilstring(zeile, "#", 24));
			if (trennanz > 25)
				histexportcurrency = SG.nteilstring(zeile, "#", 25);
			if (trennanz > 26)
				closefridayflag = SG.get_zahl(SG.nteilstring(zeile, "#", 26));
			if (trennanz > 27)
				showonlyinstalledeas = SG.get_zahl(SG.nteilstring(zeile, "#", 27));
			if (trennanz > 28)
				tradecopymagic = SG.get_zahl(SG.nteilstring(zeile, "#", 28));
			if (trennanz > 29)
				suffix = SG.nteilstring(zeile, "#", 29);
			if (trennanz > 30)
				accountlocked = SG.get_zahl(SG.nteilstring(zeile, "#", 30));
			if (trennanz > 31)
				lastinstallation = (SG.nteilstring(zeile, "#", 31));
			if (trennanz > 32)
				automaticsymbolreplacement = SG.get_zahl(SG.nteilstring(zeile, "#", 32));
			if (trennanz > 33)
				tradesuffixsender = SG.nteilstring(zeile, "#", 33);
			if (trennanz > 34)
				mttype = SG.nteilstring(zeile, "#", 34);
			if (trennanz > 35)
				aucPrefix = SG.nteilstring(zeile, "#", 35);
			if (trennanz > 36)
				autocreatorpathmode = SG.get_zahl(SG.nteilstring(zeile, "#", 36));
			if (trennanz > 37)
				autocreatorpath = SG.nteilstring(zeile, "#", 37);
			if (trennanz > 38)
				useautocreatorbackup = SG.get_zahl(SG.nteilstring(zeile, "#", 38));
			if (trennanz > 39)
				autocreatorbackuppath = SG.nteilstring(zeile, "#", 39);
			if (trennanz > 40)
				autocreatortestdir = SG.nteilstring(zeile, "#", 40);
			if (trennanz > 41)
				Mt5bugfixing = SG.get_zahl(SG.nteilstring(zeile, "#", 41));
			if (trennanz > 42)
				EodModification = SG.get_zahl(SG.nteilstring(zeile, "#", 42));
			if (trennanz > 43)
				CustomCommentFlag = SG.get_zahl(SG.nteilstring(zeile, "#", 43));
			if (trennanz > 44)
				CustomCommentText = SG.nteilstring(zeile, "#", 44);
			initmagiclist();
			// processkennung immer löschen
			processkennung = null;
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Restart()
	{
		String fnam = networkshare + "\\experts\\files\\restart.start";
		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.writezeile("restart");
		Mlist.add("restart MT4 <" + fnam + ">");
		inf.close();
	}
	
	public void initmagiclist()
	{
		// magicliststring=dies ist der string der die eingeschalteten magics für den
		// tradecopierer beinhaltet
		
		if (magiclistactive == true)
			maglist = new Magiclist(magicliststring);
		processkennung = null;
	}
	
	public int holeErsetzungsmagic(int magic)
	{
		return (maglist.getObermagic(magic));
	}
	
	public String getAccounttypename()
	{
		return accounttypename[accounttype];
	}
	
	public String getInfostring()
	{
		return infostring;
	}
	
	public void setInfostring(String infostring)
	{
		this.infostring = infostring;
	}
	
	public String getNetworkshare_INSTALLDIR()
	{
		// die neue Variable ist mqldata
		return networkshare;
	}
	
	public String getBrokername()
	{
		return brokername;
	}
	
	public void setBrokername(String brokername)
	{
		this.brokername = brokername;
	}
	
	public int getAccounttype()
	{
		return accounttype;
	}
	
	public void setAccounttype(int realconfigflag)
	{
		this.accounttype = realconfigflag;
	}
	
	public String getMqlquellverz()
	{
		return mqlquellverz;
	}
	
	public void setMqlquellverz(String mqlquellverz)
	{
		this.mqlquellverz = mqlquellverz;
	}
	
	public String getconnectedBroker()
	{
		return connectedBroker;
	}
	
	public void setconnectedBroker(String conbroker)
	{
		this.connectedBroker = conbroker;
	}
	
	public void setNetworkshare(String netzwerkshare)
	{
		this.networkshare = netzwerkshare;
	}
	
	public String getDesciption()
	{
		return desciption;
	}
	
	public void setDesciption(String desciption)
	{
		this.desciption = desciption;
	}
	
	public String getDaxname()
	{
		return daxname;
	}
	
	public void setDaxname(String daxname)
	{
		this.daxname = daxname;
	}
	
	public boolean getStoretrades()
	{
		return storetrades;
	}
	
	public void setStoretrades(boolean storetrades)
	{
		this.storetrades = storetrades;
	}
	
	public int getValiditydays()
	{
		return validitydays;
	}
	
	public void setValiditydays(int validitydays)
	{
		this.validitydays = validitydays;
	}
	
	public int getWarningflag()
	{
		return warningflag;
	}
	
	public int getPongerrorflag()
	{
		return pongerrorflag;
	}
	
	public void setPongerrorflag(int pongerrorflag)
	{
		this.pongerrorflag = pongerrorflag;
	}
	
	public void setWarningflag(int warningflag)
	{
		this.warningflag = warningflag;
	}
	
	public int getInstallationstatus()
	{
		return installationstatus;
	}
	
	public void setInstallationstatus(int installationstatus)
	{
		this.installationstatus = installationstatus;
	}
	
	public double getLotsize()
	{
		return lotsize;
	}
	
	public void setLotsize(double lotsize)
	{
		this.lotsize = lotsize;
	}
	
	public String getFstdir()
	{
		return fstdir;
	}
	
	public void setFstdir(String fstdir)
	{
		this.fstdir = fstdir;
	}
	
	public int getOn()
	{
		return on;
	}
	
	public void setOn(int on)
	{
		this.on = on;
	}
	
	public String getDatumDesErstenTrades()
	{
		return datumDesErstenTrades;
	}
	
	public void setDatumDesErstenTrades(String datumDesErstenTrades)
	{
		this.datumDesErstenTrades = datumDesErstenTrades;
	}
	
	public boolean isInsttickdataexporter()
	{
		return insttickdataexporter;
	}
	
	public void setInsttickdataexporter(boolean insttickdataexporter)
	{
		this.insttickdataexporter = insttickdataexporter;
	}
	
	public boolean isInsthistoryexporter()
	{
		return insthistoryexporter;
	}
	
	public void setInsthistoryexporter(boolean insthistoryexporter)
	{
		this.insthistoryexporter = insthistoryexporter;
	}
	
	public boolean isInsttradecopy()
	{
		return insttradecopy;
	}
	
	public void setInsttradecopy(boolean insttradecopy)
	{
		this.insttradecopy = insttradecopy;
	}
	
	public int getBrokerid()
	{
		return brokerid;
	}
	
	public void setBrokerid(int brokerid)
	{
		this.brokerid = brokerid;
	}
	
	public boolean isMagiclistactive()
	{
		return magiclistactive;
	}
	
	public void setMagiclistactive(boolean magiclistactive)
	{
		this.magiclistactive = magiclistactive;
	}
	
	public String getMagicliststring()
	{
		// dies ist der string der die eingeschalteten magics für den tradecopierer
		// beinhaltet
		return magicliststring;
	}
	
	public void setMagicliststring(String magicliststring)
	{
		// dies ist der string der die eingeschalteten magics für den tradecopierer
		// beinhaltet
		this.magicliststring = magicliststring;
		initmagiclist();
	}
	
	public String getAppdata()
	{
		return Appdata;
	}
	
	public void setAppdata(String appdata)
	{
		Appdata = appdata;
	}
	
	public String getMqldata()
	{
		return Mqldata;
	}
	
	public void setMqldata(String mqldata)
	{
		Mqldata = mqldata;
	}
	
	public String getExpertdata()
	{
		return Expertdata;
	}
	
	public void setExpertdata(String expertdata)
	{
		Expertdata = expertdata;
	}
	
	public void setMetaversion(String metaversion)
	{
		this.metaversion = metaversion;
	}
	
	public String getMetaversion()
	{
		return metaversion;
	}
	
	public String getFiledata()
	{
		return Filedata;
	}
	
	public void setFiledata(String filedata)
	{
		Filedata = filedata;
	}
	
	public String getCurpostfix()
	{
		return curpostfix;
	}
	
	public void setCurpostfix(String curpostfix)
	{
		this.curpostfix = curpostfix;
	}
	
	public int getOnlyhandinstalled()
	{
		return onlyhandinstalled;
	}
	
	public void setOnlyhandinstalled(int onlyhandinstalled)
	{
		this.onlyhandinstalled = onlyhandinstalled;
	}
	
	public String getFrequenteaupdatedir()
	{
		return frequenteaupdatedir;
	}
	
	public void setFrequenteaupdatedir(String frequenteaupdatedir)
	{
		this.frequenteaupdatedir = frequenteaupdatedir;
	}
	
	public int getUsefrequenteaupdateflag()
	{
		return usefrequenteaupdateflag;
	}
	
	public void setUsefrequenteaupdateflag(int usefrequenteaupdateflag)
	{
		this.usefrequenteaupdateflag = usefrequenteaupdateflag;
	}
	
	protected String getEarenametext()
	{
		return earenametext;
	}
	
	protected void setEarenametext(String earenametext)
	{
		this.earenametext = earenametext;
	}
	
	protected int getUseearenametextflag()
	{
		return useearenametextflag;
	}
	
	protected void setUseearenametextflag(int useearenametextflag)
	{
		this.useearenametextflag = useearenametextflag;
	}
	
	public int getUsemyfxbookflag()
	{
		return usemyfxbookflag;
	}
	
	public void setUsemyfxbookflag(int usemyfxbookflag)
	{
		this.usemyfxbookflag = usemyfxbookflag;
	}
	
	public int getUsetradecopyflag()
	{
		return usetradecopyflag;
	}
	
	public void setUsetradecopyflag(int usetradecopyflag)
	{
		this.usetradecopyflag = usetradecopyflag;
	}
	
	public int getUsefxblueflag()
	{
		return usefxblueflag;
	}
	
	public void setUsefxblueflag(int usefxblueflag)
	{
		this.usefxblueflag = usefxblueflag;
	}
	
	public String getHistexportcurrency()
	{
		return histexportcurrency;
	}
	
	public void setHistexportcurrency(String histexportcurrency)
	{
		this.histexportcurrency = histexportcurrency;
	}
	
	public int getClosefridayflag()
	{
		return closefridayflag;
	}
	
	public void setClosefridayflag(int closefridayflag)
	{
		this.closefridayflag = closefridayflag;
	}
	
	public int getShowOnlyInstalledEas()
	{
		return showonlyinstalledeas;
	}
	
	public void setShowOnlyInstalledEas(int showonlyinstalledeas)
	{
		this.showonlyinstalledeas = showonlyinstalledeas;
	}
	
	public int getTradecopymagic()
	{
		return tradecopymagic;
	}
	
	public void setTradecopymagic(int tradecopymagic)
	{
		this.tradecopymagic = tradecopymagic;
	}
	
	public String getSuffix()
	{
		return suffix;
	}
	
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}
	
	public int getAccountlocked()
	{
		return accountlocked;
	}
	
	public void setAccountlocked(int accountlocked)
	{
		this.accountlocked = accountlocked;
	}
	
	public String getLastinstallation()
	{
		return lastinstallation;
	}
	
	public void setLastinstallation(String lastinstallation)
	{
		this.lastinstallation = lastinstallation;
	}
	
	public int getAutomaticsymbolreplacement()
	{
		return automaticsymbolreplacement;
	}
	
	public void setAutomaticsymbolreplacement(int automaticsymbolreplacement)
	{
		this.automaticsymbolreplacement = automaticsymbolreplacement;
	}
	
	public String getTradesuffixsender()
	{
		return tradesuffixsender;
	}
	
	public void setTradesuffixsender(String tradesuffixsender)
	{
		this.tradesuffixsender = tradesuffixsender;
	}
	
	public int getAutocreatorpathmode()
	{
		return autocreatorpathmode;
	}
	
	public void setAutocreatorpathmode(int autocreatorpathmode)
	{
		this.autocreatorpathmode = autocreatorpathmode;
	}
	
	public String getAutocreatorpath()
	{
		return autocreatorpath;
	}
	
	public void setAutocreatorpath(String autocreatorpath)
	{
		this.autocreatorpath = autocreatorpath;
	}
	
	public String getAutocreatortestdir()
	{
		return autocreatortestdir;
	}
	
	public void setAutocreatortestdir(String autocreatortestdir)
	{
		this.autocreatortestdir = autocreatortestdir;
	}
	
	public String getInitMetaversion()
	{
		String fnam = networkshare;
		File fna4 = new File(fnam + "\\MQL4");
		File fna5 = new File(fnam + "\\MQL5");
		String orpath = null;
		String mtversnumber = "";
		
		if (new File(fnam + "\\terminal.exe").exists())
			mttype = "mt4";
		else if (new File(fnam + "\\terminal64.exe").exists())
			mttype = "mt5";
		else
			Tracer.WriteTrace(10, "Error: unknown mtx Version. I need terminal.exe or terminal64.exe -> STOP");
		
		// falls portable=1 dann schaue nicht im orpath nach !!!
		if ((GlobalVar.getPortableflag() == 0) && (orpath = holeOrginPath()) != null)
		{
			Appdata = orpath;
			Mqldata = orpath + "\\MQL4";
			Expertdata = orpath + "\\MQL4\\Experts";
			Filedata = orpath + "\\MQL4\\Files";
			metaversion = ">=600";
			mtversnumber = ">=600";
		} else if (fna5.exists())
		{
			Appdata = fnam;
			Mqldata = fnam + "\\MQL5";
			Expertdata = fnam + "\\MQL5\\Experts\\SQ";
			Filedata = fnam + "\\MQL5\\Files";
			metaversion = ">=600";
			mtversnumber = ">=600";
		} 
		 else if (mttype=="mt5")
			{
				Appdata = fnam;
				Mqldata = fnam + "\\MQL5";
				Expertdata = fnam + "\\MQL5\\Experts\\SQ";
				Filedata = fnam + "\\MQL5\\Files";
				metaversion = ">=600";
				mtversnumber = ">=600";
				
				File mql5 = new File(Mqldata);
				if (mql5.exists() == false)
					mql5.mkdir();
				
			} 
	else
		{
			Appdata = fnam;
			Mqldata = fnam + "\\MQL4";
			
			File mql4 = new File(Mqldata);
			if (mql4.exists() == false)
				mql4.mkdir();
			
			Expertdata = fnam + "\\MQL4\\Experts";
			Filedata = fnam + "\\MQL4\\Files";
			metaversion = ">=600";
			mtversnumber = ">=600";
		}
		
		return (mtversnumber);
	}
	
	private String holeOrginPath()
	{
		// sucht appdata ab
		String mdir = System.getProperty("user.home") + "\\AppData\\Roaming\\MetaQuotes\\Terminal";
		// Tracer.WriteTrace(20, "I:holeOrginPath suche Appdata in <"+mdir+">");
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(mdir, 0);
		int anz = fdyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String dirname = fdyn.holeFileSystemName();
			File orginfile = new File(mdir + "\\" + dirname + "\\origin.txt");
			// Tracer.WriteTrace(20,
			// "I:suche Orginfile in <"+orginfile.getAbsolutePath()+">");
			if (orginfile.exists())
			{
				Inf inf = new Inf();
				inf.setFilename(orginfile.getAbsolutePath());
				// Tracer.WriteTrace(20,
				// "lese file<"+orginfile.getAbsolutePath()+">");
				String z = inf.readZeile("UTF-16");
				inf.close();
				// Tracer.WriteTrace(20,
				// "vergleiche <"+z+"> mit <"+networkshare+">");
				if (z.equalsIgnoreCase(networkshare) == true)
					return (mdir + "\\" + dirname);
			}
		}
		return null;
	}
	
	public String getEaDir()
	{
		return null;
	}
	
	public String getFileDir()
	{
		return null;
	}
	
	public String getIndicatorDir()
	{
		return null;
	}
	
	public Process getProcessKennung()
	{
		return processkennung;
	}
	
	public void setProcessKennung(Process process)
	{
		this.processkennung = process;
	}
	
	public String getAucPrefix()
	{
		return aucPrefix;
	}
	
	public void setAucPrefix(String aucPrefix)
	{
		this.aucPrefix = aucPrefix;
	}
	
	public String getAutocreatorbackuppath()
	{
		return autocreatorbackuppath;
	}
	
	public void setAutocreatorbackuppath(String autocreatorbackuppath)
	{
		this.autocreatorbackuppath = autocreatorbackuppath;
	}
	
	public int getUseautocreatorbackup()
	{
		return useautocreatorbackup;
	}
	
	public void setUseautocreatorbackup(int useautocreatorbackup)
	{
		this.useautocreatorbackup = useautocreatorbackup;
	}
	
	public boolean isMt5bugfixing()
	{
		if(Mt5bugfixing==1)
			return true;
		else
			return false;
		
	}

	public void setMt5bugfixing(boolean flag)
	{
		if(flag==true)
			Mt5bugfixing=1;
		else
			Mt5bugfixing=0;
		
	}

	public boolean isEodModification()
	{
		if(EodModification==1)
			return true;
		else
			return false;
	
	}

	public void setEodModification(boolean flag)
	{
		if(flag==true)
			EodModification = 1;
		else
			EodModification=0;
	}
	public void setCustomCommentText(String commenttext)
	{
		CustomCommentText=commenttext;
	}
	public String getCustomCommentText()
	{
		if(CustomCommentText==null)
			return "";
		else
		return CustomCommentText;
	}
	public boolean isCustomCommentFlag()
	{
		if(CustomCommentFlag==1)
			return true;
		else
			return false;
	}
	public void setCustomCommentFlag(boolean flag)
	{
		if(flag==true)
			CustomCommentFlag=1;
		else
			CustomCommentFlag=0;
		
	}
	
	
	public void pongCheck()
	{
		// prüft ob es einen zu alten pong gibt.
		
		// directory von .../files holen
		File dir = new File(this.getFiledata());
		
		File[] files = dir.listFiles();
		if (files != null)
		{ // Erforderliche Berechtigungen etc. sind vorhanden
			for (int i = 0; i < files.length; i++)
			{
				// System.out.print(files[i].getAbsolutePath());
				if (files[i].isFile())
				{
					
					String fpath = files[i].getAbsolutePath();
					if (fpath.toLowerCase().contains("pong"))
					{
						if (checkOldPong(fpath) == true)
						{
							this.setPongerrorflag(1);
							Tracer.WriteTrace(20, "I:found old pong <" + fpath + ">");
							return;
						}
						
					}
				}
			}
		}
		
		this.setPongerrorflag(0);
		
	}
	
	private boolean checkOldPong(String fname)
	{
		long pongalter_sec = Mondate.FilealterSec(fname);
		// falls zu alt dann fehler melden
		if (pongalter_sec > GlobalVar.getMaxpongtime())
			return true;
		else
			return false;
		
	}
	
	public boolean isRealbroker()
	{
		if (accounttype == 2)
			return true;
		else
			return false;
		
	}
	
	public String getMttype()
	{
		return mttype;
	}
	
	public void setMttype(String mttype)
	{
		this.mttype = mttype;
	}
	
	public void SetCloseAllTradesOnce(int magic)
	{
		// die trades für eine magic werden geschlossen, da wird dies file hier in
		// ../files abgelegt
		// <magic>.clo
		File clofile = new File(this.getFiledata() + "\\" + magic + ".clo");
		if (clofile.exists() == false)
			try
			{
				if (clofile.createNewFile() == false)
					Tracer.WriteTrace(10, "SetCloseAllTrades: cant create file <" + clofile.getAbsolutePath() + ">");
			} catch (IOException e)
			{
				e.printStackTrace();
				Tracer.WriteTrace(10, "SetCloseAllTrades: cant create file <" + clofile.getAbsolutePath() + ">");
			}
	}
	
	public void SetCloseAllTradesOnceAC(String comment)
	{
		// die trades für eine bestimmte comment werden geschlossen
		// das wird für den autocreator benötigt
		// ../files abgelegt
		// <comment>.clocom
		File clofile = new File(this.getFiledata() + "\\" + comment + ".clocom");
		if (clofile.exists() == false)
			try
			{
				if (clofile.createNewFile() == false)
					Tracer.WriteTrace(10, "SetCloseAllTrades: cant create file <" + clofile.getAbsolutePath() + ">");
			} catch (IOException e)
			{
				e.printStackTrace();
				Tracer.WriteTrace(10, "SetCloseAllTrades: cant create file <" + clofile.getAbsolutePath() + ">");
			}
	}
	
	public void RemoveCloseAllTradesOnce(int magic)
	{
		// es wird geschaut of das File <magic>.clo vorhanden ist, wenn ja wird es
		// gelöscht
		File clofile = new File(this.getFiledata() + "\\" + magic + ".clo");
		if (clofile.exists() == false)
			return;
		else if (clofile.delete() == false)
			Tracer.WriteTrace(10, "E:RemoveCloseAllTrades: cant remove file <" + clofile.getAbsolutePath() + ">");
	}
	
	public void RemoveCloseAllTradesOnceAC(String comment)
	{
		// es wird geschaut of das File <magic>.clocom vorhanden ist, wenn ja wird es
		// gelöscht
		// wird für autocreator benötigt
		File clofile = new File(this.getFiledata() + "\\" + comment + ".delcom");
		if (clofile.exists() == false)
			return;
		else if (clofile.delete() == false)
			Tracer.WriteTrace(10, "E:RemoveCloseAllTrades: cant remove file <" + clofile.getAbsolutePath() + ">");
	}
	
	public void backupProfiles()
	{
		// Diese Funktion wird für Metatrader 5 verwendet,die eas lassen sich hier nicht
		// so einfach installieren
		// 1) Man muss nachdem man die eas installiert hat, den metatrader einmal kurz
		// start
		// 2) Dann nochmal die configfiles im Mt5 schreiben
		// 3) Dies wird durch den backupmechanismus getätigt
		
		String chrdir = this.getAppdata() + "\\MQL5\\profiles\\charts\\default";
		String backupdir = this.getAppdata() + "\\MQL5\\profiles\\charts\\default\\backup";
		File bdir = new File(backupdir);
		
		// backup leeren falls es schon vorhanden
		if (bdir.exists() == true)
			FileAccess.deleteDirectoryContent(bdir);
		
		// wenn nicht vorhanden, dann erzeuge das backupdir
		if (bdir.exists() == false)
			if (bdir.mkdir() == false)
				Tracer.WriteTrace(10, "E:error gendir <" + bdir + ">");
			
		// mache das backup
		FileAccess.CopyDirectory(chrdir, backupdir, null);
		
	}
	
	public void restoreProfiles()
	{
		String chrdir = this.getAppdata() + "\\MQL5\\profiles\\charts\\default";
		String backupdir = this.getAppdata() + "\\MQL5\\profiles\\charts\\default\\backup";
		File bdir = new File(backupdir);
		File cdir = new File(chrdir);
		
		if (bdir.exists() == false)
			Tracer.WriteTrace(10, "E: cant find backupdir <" + bdir.getAbsolutePath() + ">");
		
		FileAccess.deleteDirectoryContentPostfix(cdir, ".chr");
		
		// mache restore
		FileAccess.CopyDirectory(backupdir, chrdir, null);
		
	}
	
	public int compareTo(Metaconfig metatrader)
	{
		return -1;
	}
	public void genPortableBatch()
	{
		//generiert das portable file \\startmetatrader_portable.bat
		
		String portextension="";
		
		if (GlobalVar.getPortableflag() == 1)
			portextension = " /portable";
		
		String metatraderexepath = null;
		if(getMttype()==null)
			metatraderexepath = getNetworkshare_INSTALLDIR() + "\\terminal.exe";
		else if (getMttype().toLowerCase().equals("mt4"))
			metatraderexepath = getNetworkshare_INSTALLDIR() + "\\terminal.exe";
		else if (getMttype().toLowerCase().equals("mt5"))
			metatraderexepath = getNetworkshare_INSTALLDIR() + "\\terminal64.exe";
		else
			Tracer.WriteTrace(10, "E: metatype not supportet 111122");
		
		Inf inflocal = new Inf();
		String metatraderstartbatfile = "";
		metatraderstartbatfile = getNetworkshare_INSTALLDIR() + "\\startmetatrader_portable.bat";
		
		inflocal.setFilename(metatraderstartbatfile);
		if (FileAccess.FileAvailable(metatraderstartbatfile))
			FileAccess.FileDelete(metatraderstartbatfile, 0);
	
		// schreibt die lokale startdatei für den metatrader
		inflocal.writezeile("start /MIN \"\" \"" + metatraderexepath + "\"" + portextension);
		inflocal.writezeile("exit");
		inflocal.close();
		
	}
	public Boolean isAvailablePortablebatch()
	{
		String metatraderstartbatfile = "";
		metatraderstartbatfile = getNetworkshare_INSTALLDIR() + "\\startmetatrader_portable.bat";
		File fnam=new File(metatraderstartbatfile);
		return (fnam.exists());
	}
}
