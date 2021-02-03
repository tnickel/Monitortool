package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.ToolsException;

import java.io.File;

import mtools.Mlist;
import Metriklibs.FileAccessDyn;
import datefunkt.Mondate;

public class Metaconfig implements Comparable<Metaconfig>
{
	String networkshare;
	String brokername;
	// 0= monitor, 1 = gbautomatic, 2=Real, 3=FST
	int accounttype = 0;
	String mqlquellverz = "empty";
	String connectedBroker;
	String desciption = "descipe trading strategies here";
	String infostring = null;
	String daxname = null;
	String fstdir = null;
	boolean magiclistactive = false;
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
	//some broker use currency.postfix for currency pair
	private String curpostfix="";
	//only handinstalled flag, if 1= than the eas are never modified by the tool
	private int onlyhandinstalled=0;

	static String[] accounttypename =
	{ "Mon", "Aut", "Real", "FST" };

	// datum des ersten trades
	String datumDesErstenTrades = null;

	//hier kommt der process hin
	private Process processkennung=null;
	
	//Frequenteaupdatedir
	//dies ist das rootverzeichniss wo die wiederholenden (wöchtentlich) neuen EA befinden
	private String frequenteaupdatedir="";
	
	//hier wird das frequenteaupdate ein/aus geschaltet
	private int usefrequenteaupdateflag=0;
	
	//falls es geünscht ist das der ea vor der installation renamed wird
	private String earenametext="";
	
	//hiermit wird das earename ein/ausgeschaltet
	private int useearenametextflag=0;
	
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
			
			initmagiclist();
			//processkennung immer löschen
			processkennung=null;
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
		if (magiclistactive == true)
			maglist = new Magiclist(magicliststring);
		processkennung=null;
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
		return magicliststring;
	}

	public void setMagicliststring(String magicliststring)
	{
		this.magicliststring = magicliststring;
		initmagiclist();
	}

	public boolean isInstmyfxbookea()
	{
		return instmyfxbookea;
	}

	public void setInstmyfxbookea(boolean instmyfxbookea)
	{
		this.instmyfxbookea = instmyfxbookea;
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

	public String getInitMetaversion()
	{
		String fnam = networkshare;
		File fna = new File(fnam + "\\MQL4");
		String orpath = null;
		
		//falls portable=1 dann schaue nicht im orpath nach !!!
		if ((GlobalVar.getPortableflag()==0)&&(orpath = holeOrginPath()) != null)
		{
			Appdata = orpath;
			Mqldata = orpath + "\\MQL4";
			Expertdata = orpath + "\\MQL4\\Experts";
			Filedata = orpath + "\\MQL4\\Files";
			metaversion = ">=600";
			return (">=600");
		} else
		if (fna.exists())
		{
			Appdata = fnam;
			Mqldata = fnam + "\\MQL4";
			Expertdata = fnam + "\\MQL4\\Experts";
			Filedata = fnam + "\\MQL4\\Files";
			metaversion = ">=600";
			return (">=600");
		}
		{ // 500er Version
			Appdata = networkshare;
			Mqldata = networkshare + "\\Experts";
			Expertdata = networkshare + "\\Experts";
			Filedata = networkshare + "\\Experts\\Files";

			metaversion = "<=509";
			return ("<509");
		}
	}

	private String holeOrginPath()
	{
		// sucht appdata ab
		String mdir = System.getProperty("user.home")
				+ "\\AppData\\Roaming\\MetaQuotes\\Terminal";
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

	public void pongCheck()
	{
		File pongfile = new File(this.getFiledata() + "\\pong.txt");
		if (pongfile.exists() == false)
			return;

		long pongalter_sec = Mondate.FilealterSec(this.getFiledata()
				+ "\\pong.txt");
		// falls zu alt dann fehler melden
		if (pongalter_sec > GlobalVar.getMaxpongtime())
			this.setPongerrorflag(1);
		else
			this.setPongerrorflag(0);

	}
	
	public boolean isRealbroker()
	{
		String kontofilenam=this.getFiledata() + "\\Kontoinformation.txt";
		File kontofile = new File(this.getFiledata() + "\\Kontoinformation.txt");
		if(kontofile.exists()==false)
			return false;
		else
		{
			if(FileAccess.FileContainString(kontofilenam,"Realaccount")==true)
				return true;
			else
				return false;
		}
	}

	public int compareTo(Metaconfig metatrader)
	{
		return -1;
	}
}
