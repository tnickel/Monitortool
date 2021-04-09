package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Sys;


public class GlobalVar
{
	//diese Struktur beinhaltet alle globalen variablen
	static GlobVarDat gc= new GlobVarDat();
	static private String conffnam=Rootpath.getRootpath()+"\\conf\\config.xml";
	static String version=null;
	static String serverip1=null;
	static String serverip2=null;
	static int ipthreadflag=0;
	static String ipmessage="check for updates";
	static int silentmode=0;
	static int askforupdate=1;
	static int metatraderrunning=0;
	
	
	public GlobalVar()
	{
		if(FileAccess.FileAvailable(conffnam)==false)
		{
			gc.setPortableflag(1);
			return;
		}
		Inf inf = new Inf();
		inf.setFilename(conffnam);

		// config einlesen
		gc = (GlobVarDat) inf.loadXST();

		inf.close();
	}

	public static int getMetatraderrunning()
	{
		return metatraderrunning;
	}

	public static void setMetatraderrunning(int metatraderrunning)
	{
		GlobalVar.metatraderrunning = metatraderrunning;
	}

	static public String getMqlsourcedirprefix()
	{
		return gc.getMqlsourcedir();
	}

	static public void setMqlsourcedirprefix(String mqlsourcedirprefix)
	{
		gc.setMqlsourcedir(mqlsourcedirprefix);
	}

	static public String getNetzwerkshareprefix()
	{
		return gc.getNetworkshareprefix();
	}
	static public void setNetworkshareprefix(String prefix)
	{
		gc.setNetworkshareprefix(prefix);
	}
	static public void setDefaultGd(int gd)
	{
		gc.setDefaultGd(gd);
	}
	static public int getDefaultGd()
	{
		return(gc.getDefaultGd());
	}
	static public int getSecondGd()
	{
		return(gc.getSecondGd());
	}
	static public void setSecondGd(int gd)
	{
		gc.setSecondGd(gd);
	}
	static public void setSwitchOnTradeTrigger(int anz)
	{
		gc.setSwitchontradetrigger(anz);
	}

	static public int getShowMaxTradetablesize()
	{
		return(gc.getShowmaxtradetablesize());
	}
	static public void setShowmaxTradetablesize(int x)
	{
		gc.setShowmaxtradetablesize(x);
	}
	
	
	public static void setSortcriteria(int crit)
	{
		gc.setSortcriteria(crit);
	}
	public static int getSortcriteria()
	{
		return(gc.getSortcriteria());
	}
	public static String getMyfxbookemail()
	{
		return(gc.getMyfxbookemail());
	}
	public static void setMyfxbookemail(String email)
	{
		gc.setMyfxbookemail(email);
	}
	public static String getMyfxbookpassword()
	{
		return(gc.getMyfxbookpassword());
	}
	public static void setMyfxbookpassword(String password)
	{
		gc.setMyfxbookpassword(password);
	}
	public static int getMyfxbookintervall()
	{
		return(gc.getMyfxbookeintervall());
	}
	public static void setMyfxbookintervall(int intervall)
	{
		gc.setMyfxbookintervall(intervall);
	}
	public static void setUsername(String uname)
	{
		gc.setUsername(uname);
	}
	public static String getUsername()
	{
		return(gc.getUsername());
	}
	public static void setEmail(String email)
	{
		gc.setEmail(email);
	}
	public static String getEmail()
	{
		return(gc.getEmail());
	}
	
	public static int getForgetdays()
	{
		return(gc.getForgetdays());
	}
	public static void setForgetdays(int days)
	{
		gc.setForgetdays(days);
	}
	public static int getSernumber()
	{
		return(gc.getSernumber());
	}
	public static void setSernumber(int number)
	{
		gc.setSernumber(number);
	}
	static public String getSerialString()
	{
		String hostname = Sys.getHostname();
		String serstring= hostname + " "+gc.getSernumber();
		return serstring;
		
	}
	
	public static String getVersion()
	{
		return version;
	}

	public static void setVersion(String version)
	{
		GlobalVar.version = version;
	}

	public static String getServerip1()
	{
		return serverip1;
	}

	public static void setServerip2(String serverip)
	{
		GlobalVar.serverip2 = serverip;
	}
	public static String getServerip2()
	{
		return serverip2;
	}

	public static void setServerip1(String serverip)
	{
		GlobalVar.serverip1 = serverip;
	}
	
	public static int getIpthreadflag()
	{
		return ipthreadflag;
	}

	public static void setIpthreadflag(int ipthreadflag)
	{
		GlobalVar.ipthreadflag = ipthreadflag;
	}

	public static String getUpdatechannel()
	{
		return gc.getUpdatechannel();
	}

	public static void setUpdatechannel(String updatechannel)
	{
		gc.setUpdatechannel(updatechannel);
	}

	public static String getIpmessage()
	{
		return ipmessage;
	}

	public static void setIpmessage(String ipmessage)
	{
		GlobalVar.ipmessage = ipmessage;
	}
	public static int getShowLicenceflag()
	{
		//holt das flag was angibt ob die lizenz schon gesehen wurde
		return(gc.getShowlicenceflag());
	}
	public static void setShowLicenseflag(int val)
	{
		gc.setShowlicenceflag(val);
	}
	
	public static void setMaxpongtime(int ptime)
	{
		gc.setMaxpongtime(ptime);
	}
	public static int getMaxpongtime()
	{
		return(gc.getMaxpongtime());
	}
	
	public static String calcVersionstring()
	{
		return("Monitortool V0.516");
	}
	
	public static String calcHeadline()
	{
		String headline=calcVersionstring()+" (c) Thomas Nickel         license<"+Lic.getlicstring()+">        updatechannel<"+GlobalVar.getUpdatechannel()+"> <"+GlobalVar.getRootpath()+"> ";
		return headline;
	}
	
	public static int getAutostartfeature()
	{
		return (gc.getAutostartfeature());
	}

	public static void setAutostartfeature(int autostartfeature)
	{
		gc.setAutostartfeature(autostartfeature);
	}

	public static int getSilentmode()
	{
		return silentmode;
	}

	public static void setSilentmode(int silentmode)
	{
		GlobalVar.silentmode = silentmode;
	}
	public static void setAskforUpdateflag(int flag)
	{
		gc.setAskforupdate(flag);
	}
	public static int getAskforUpdateflag()
	{
		return(gc.getAskforupdate());
	}
	public static int getPortableflag()
	{
		int flag=gc.getPortableflag();
		
		return(flag);
	}
	public static void setPortableflag(int val)
	{
		gc.setPortableflag(val);
	}
	public static String getRootpath()
	{
		
		return(Rootpath.getRootpath());
	}
	public static int getLastcopytrademagic()
	{
		return(gc.getLastcopytrademagic());
	}
	public static void setLastcopytrademagic(int val)
	{
		gc.setLastcopytrademagic(val);
	}
	static public void save()
	{
		Inf inf = new Inf();
		inf.setFilename(conffnam);
		inf.saveXST(gc);
		
		inf.close();
	}
}
