package modtools;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import mtools.Mlist;
import Metriklibs.FileAccessDyn;
import data.Ea;
import data.Metaconfig;

public class Profiler
{
	// dies ist die Klasse die sich mit MTroot/Profiles/default beschäftigt
	Metaconfig meconf_glob = null;

	String fnam = null;

	public Profiler(Metaconfig meconf)
	{
		meconf_glob = meconf;
	}

	private String getprofiledir()
	{
		//holt das 
	
		String path = null;

		//z.B. path = C:\Users\tnickel\AppData\Roaming\MetaQuotes\Terminal\CC6E1102573BF694B8273CD181A4EFF7\profiles\default
		path = meconf_glob.getAppdata() + "\\profiles\\default";
		if (meconf_glob.getAppdata() == null)
		{
			Tracer.WriteTrace(10, "internal error no appdata");
			System.exit(99);
		}
		return path;
	}

	public void cleanAllProfiles()
	{
		// die profiles werden komplett gelöscht
		String path = getprofiledir();
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(path, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			File fchr = new File(path + "\\" + fa.holeFileSystemName());
			if (fchr.getName().endsWith(".chr"))
				fchr.delete();
		}
	}

	public int getanzProfiles(String eaname)
	{
		// hier wird gezählt wie oft das Profile vorhanden ist
		// >0: falls für den ea schon eine config gibt (chr)
		// 0:falls es für den ea keine config gibt (kein passenden char-file
		// vorhanden)
		
		//beispiel: eaname=historyexporter
		//hier wird geschaut ob der historyexporter schon konfiguriert ist.
		String profiledir = getprofiledir();
		int countprofiles=0;
		
		if(profiledir==null)
			Mbox.Infobox("profiledir ==null");
		
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			//holt ein profilenamen
			String profilename = profiledir + "\\" + fa.holeFileSystemName();
			File ffnam= new File(profilename);
			if(ffnam.exists()==false)
			{
				Mbox.Infobox("profile not exists <"+ffnam.getAbsoluteFile()+">");
				continue;
			}
			if(ffnam.length()<5)
			{
				Tracer.WriteTrace(20, "profile bad <"+ffnam.getAbsoluteFile()+">");
				continue;
			}
			
			Inf inf = new Inf();
			inf.setFilename(profilename);
			
			//Tracer.WriteTrace(20, "I: checkprofile index<"+i+">");
			String mem = inf.readMemFile(2500);
			inf.close();
			if (mem.toLowerCase().contains("name=" + eaname.toLowerCase()))
				countprofiles++;
		}
		return countprofiles;

	}
	public int delProfiles(String eaname)
	{
		// falls das profile dienen eanamen beinhaltet wird es gelöscht
		// es wird gezählt wieviel mal das gemacht wurde
		
		//beispiel: eaname=historyexporter
		//hier wird geschaut ob der historyexporter schon konfiguriert ist.
		String profiledir = getprofiledir();
		int countprofiles=0;
		
		if(profiledir==null)
			Mbox.Infobox("profiledir ==null");
		
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			//holt ein profilenamen
			String profilename = profiledir + "\\" + fa.holeFileSystemName();
			File ffnam= new File(profilename);
			if(ffnam.exists()==false)
			{
				Mbox.Infobox("profile not exists <"+ffnam.getAbsoluteFile()+">");
				continue;
			}
			if(ffnam.length()<5)
			{
				Tracer.WriteTrace(20, "profile bad <"+ffnam.getAbsoluteFile()+">");
				continue;
			}
			
			Inf inf = new Inf();
			inf.setFilename(profilename);
			
			//Tracer.WriteTrace(20, "I: checkprofile index<"+i+">");
			String mem = inf.readMemFile(2500);
			inf.close();
			if (mem.toLowerCase().contains("name=" + eaname.toLowerCase()))
			{
				File fnam=new File(profilename);
				if(fnam.delete()==false)
					Tracer.WriteTrace(10, "E:delProfiles:cant del file <"+eaname+">");
				countprofiles++;
			}
		}
		return countprofiles;

	}
	public int getFreeChartNumber()
	{
		// rückgabe : free number
		// -1: falls keine Nummer mehr frei ist
		// es wird die nächste freie chartNummer ermittelt
		String path = getprofiledir();
		NumberFormat numberFormat = new DecimalFormat("00");

		for (int i = 1; i < 100; i++)
		{
			//Tracer.WriteTrace(20, "I: suchen freie chartnummer index<"+i+">");
			String fnam = path + "\\chart" + numberFormat.format(i)+".chr";
			File ffile = new File(fnam);
			if (ffile.exists() == true)
				continue;
			else
				// freie nummer gefunden
				return i;
		}
		
		Mbox.Infobox("Error:metatrader is full, max 99 metatrader possible in <"+path+">");
		return -1;
	}

	public String getFreeChartName()
	{
		// ermittelt den nächsten freien chartnamen
		NumberFormat numberFormat = new DecimalFormat("00");
		int freenumber = getFreeChartNumber();
		if(freenumber==-1)
		{
			return null;
		}
		String chartname = "chart" + numberFormat.format(freenumber) + ".chr";
		return chartname;
	}

	public void createSpecialProfile(String quelle, String expertname)
	{
		FileAccessDyn fd= new FileAccessDyn();
		// hier wird ein spezielles Profile kopiert
		// historyexporter oder tickdataexporter
		// falls das profile schon da ist
		if (getanzProfiles(expertname) >0)
		{
			Tracer.WriteTrace(20, "W:Profile for expert already exists, I don´t create profile for ea <"+expertname+">");
			return;
		}
		// den namen des Zielfile ermitteln
		String zielfile = meconf_glob.getAppdata() + "\\profiles\\default\\"
				+ getFreeChartName();
		fd.copyFile2(quelle, zielfile);
		
		//das spezielle profile muss noch modifziert werden bzgl. cur postfix
		

		String histcurrencystring=meconf_glob.getHistexportcurrency();
		FileAccess.FileReplaceString(zielfile,"symbol=EURUSD", "symbol="+histcurrencystring);
	}
	public void checkDoubleEa( String expertname,String path)
	{
		//Es wird überprüft ob dieser Ea mehr als Einmal vorkommt. Ist das der Fall wird das gemeldet
		int anz=0;
	
		if ((anz=getanzProfiles(expertname))>1)
		{
			Mbox.Infobox("W: Ea<"+expertname+"> more than once installation #=<"+anz+">, please delete some in path<"+path+">");
		}
	}
	public void deleteAllProfiles( String expertname,String path)
	{
		//es werden alle profiles für den expertnamen gelöscht
		
	
		int anz=delProfiles(expertname);
		Tracer.WriteTrace(20, "I:delete #profiles=<"+anz+"> for expertname<"+expertname+">");
	}
	public void createProfile(MqlPatch mqlpatch, String quelle, Ea ea,Metaconfig meconf)
	{
		// hier wird das profile.chr in Metaroot/profiles/default geschrieben

		String expertname = mqlpatch.getExpertname().replace(".mq4", "");

		// falls das profile schon da ist
		if (getanzProfiles(expertname) >0)
			return;

		// zielfile ermitteln
		String zielfile = meconf_glob.getAppdata() + "\\profiles\\default\\"
				+ getFreeChartName();

		writeChrFile(mqlpatch, quelle, zielfile, ea,meconf);
	}

	private void writeChrFile(MqlPatch mqlpatch, String quelle,
			String zielfile, Ea ea,Metaconfig meconf)
	{
		int period = mqlpatch.getPeriod();
		String symbol = "";

		// lösche das chrfile falls schon da

		if (FileAccess.FileAvailable0(zielfile))
			FileAccess.FileDelete(zielfile, 0);

		symbol = mqlpatch.getSymbol(meconf_glob);

		String expertname = mqlpatch.getExpertname().replace(".mq4", "");

		ChrFile cfgpatch = new ChrFile();
		cfgpatch.setFilename(quelle);
		cfgpatch.readMemFile();
		cfgpatch.setPeriod(period);
		String curpostfix=meconf.getCurpostfix();
		if(curpostfix!=null)
		  cfgpatch.setSymbol(symbol+curpostfix);
		cfgpatch.setExpertname(expertname);
		cfgpatch.setYellowmessage(expertname);

		if (ea == null)
			Tracer.WriteTrace(10, "ea <" + quelle + "> nicht bekannt");

		cfgpatch.writeMemFile(zielfile);

		ea.setSymbol(symbol);
		ea.setPeriod(period);
		Mlist.add("I:write cfg-file<" + zielfile + ">", 1);
	}

	public void deleteProfile(String eaname)
	{
		// das Profile für einen bestimmten eanamen wird gelöscht
		String path = getprofiledir();
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(path, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = path + "\\" + fa.holeFileSystemName();
			Inf inf = new Inf();
			inf.setFilename(fnam);
			String mem = inf.readMemFile();
			inf.close();
			if (mem.contains("file=" + eaname))
			{
				File ffile = new File(fnam);
				ffile.delete();
			}
		}
		Mbox.Infobox("can´t find profile for eaname<" + eaname + ">");
		return;
	}
}
