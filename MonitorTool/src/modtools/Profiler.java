package modtools;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import Metriklibs.FileAccessDyn;
import data.Ea;
import data.Metaconfig;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import mtools.Mlist;

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
		// holt das
		
		String path = null;
		
		// z.B. path =
		// C:\Users\tnickel\AppData\Roaming\MetaQuotes\Terminal\CC6E1102573BF694B8273CD181A4EFF7\profiles\default
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
		// die profiles werden alle komplett gelöscht
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
		// es wird geprüft ob der EAname im Profile vorkommt, nur wenn er vorkommt
		// wird das profile gezählt
		
		// beispiel: eaname=historyexporter
		// hier wird geschaut ob der historyexporter schon konfiguriert ist.
		String profiledir = getprofiledir();
		int countprofiles = 0;
		
		if (profiledir == null)
			Mbox.Infobox("profiledir ==null");
		
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// holt ein profilenamen
			String filename = profiledir + "\\" + fa.holeFileSystemName();
			
			// schaut für dieses profile ob hier der EA vorkommt
			if (checkKeyword(filename, "name=" + eaname.toLowerCase()) == true)
				countprofiles++;
		}
		return countprofiles;
		
	}
	
	public String getProfileEa(String eaname)
	{
		// sucht das Profile mit dem Eanamen
		String profiledir = getprofiledir();
		
		if (profiledir == null)
			Mbox.Infobox("profiledir ==null");
		
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// holt ein profilenamen
			String filename = profiledir + "\\" + fa.holeFileSystemName();
			if (checkKeyword(filename, "name=" + eaname.toLowerCase()))
				return filename;
		}
		// der Eaname hat kein profile
		return null;
	}
	
	public String getProfileEaChannel(String eaname, String channel)
	{
		// sucht das Profile mit dem Eanamen und den passenden channel
		// diese funktion wird für den tradekopierer benötigt
		
		String profiledir = getprofiledir();
		
		if (profiledir == null)
			Mbox.Infobox("profiledir ==null");
		
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// holt ein profilenamen
			String filename = profiledir + "\\" + fa.holeFileSystemName();
			if ((checkKeyword(filename, "name=" + eaname.toLowerCase()))
					&& (checkKeyword(filename, "channel=" + channel.toLowerCase())))
				return filename;
		}
		// der Eaname hat kein profile
		return null;
	}
	public int countEaChannel( String channel)
	{
		//zählt wieviele profiles für diesen Channel beim Realbroker vorhanden sind
		// die channels werden nur für die Realbroker gezählt
		int channelcounter=0;
		String profiledir = getprofiledir();
		
		if(channel.equals("8")==true)
			System.out.println("found8");
		
		if (profiledir == null)
			Mbox.Infobox("profiledir ==null");
		
		if(meconf_glob.getAccounttype()!=2)
		{
			Tracer.WriteTrace(10, "E:countEaChannel, I can only count channels on realbroker, but <"+meconf_glob.getBrokername()+"> is not a realbroker");
			return -1;
		}
			
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// holt ein profilenamen
			String filename = profiledir + "\\" + fa.holeFileSystemName();
			if  (checkKeyword(filename, "MagicNumber=" + channel.toLowerCase()))
				  channelcounter++;
		}
		
		return channelcounter;
	}
	public Boolean checkKeyword(String filename, String keyword)
	{
		// prüft für ein bestimmtes profile ob der keynamen drin ist
		String profiledir = getprofiledir();
		
		File ffnam = new File(filename);
		if (ffnam.exists() == false)
		{
			Mbox.Infobox("profile not exists <" + ffnam.getAbsoluteFile() + ">");
			return false;
		}
		if (ffnam.length() < 5)
		{
			Tracer.WriteTrace(20, "profile bad <" + ffnam.getAbsoluteFile() + ">");
			return false;
		}
		Inf inf = new Inf();
		inf.setFilename(filename);
		inf.close();
		
		// Tracer.WriteTrace(20, "I: checkprofile index<"+i+">");
		String mem = inf.readMemFile(8000);
		inf.close();
		if (mem.toLowerCase().contains(keyword.toLowerCase()))
		{
			
			return true;
		}
		return false;
	}
	
	public Boolean delAllProfiles(String eaname, String channel)
	{
		// Es werden alle Profiles für den Tradekopierer mit
		// eaname und channel gelöscht
		// bsp:name=FX Blue - TradeCopy Receiver
		// Channel=gbebroker17023
		// eaname: names des eas
		// channel: number of the cannel, or null
		String profiledir = getprofiledir();
		
		if (profiledir == null)
			Mbox.Infobox("profiledir ==null");
		
		FileAccessDyn fa = new FileAccessDyn();
		fa.initFileSystemList(profiledir, 1);
		int anz = fa.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// holt ein profilenamen
			String profilename = profiledir+"\\"+fa.holeFileSystemName();
			
			// dies ist ein Tradecopierer profile
			if (channel != null)
			{
				if ((checkKeyword(profilename, "name=" + eaname)) && (checkKeyword(profilename, "channel=" + channel)))
				{
					// Profile exists dann lösche
					File fna = new File(profilename);
					if (fna.delete() == false)
						Tracer.WriteTrace(10, "E:del all profiles:can´t delete file <" + profilename + ">");
				}
			} else if (checkKeyword(profilename, "name=" + eaname))
			{
				// Profile exists dann lösche
				File fna = new File(profilename);
				if (fna.delete() == false)
					Tracer.WriteTrace(10, "E:del all profiles:can´t delete file <" + profilename + ">");
			}
			
		}
		return true;
		
	}
	
	public int getFreeChartNumber()
	{
		// rückgabe : free number
		// -1: falls keine Nummer mehr frei ist
		// es wird die nächste freie chartNummer ermittelt
		String path = getprofiledir();
		NumberFormat numberFormat = new DecimalFormat("00");
		
		for (int i = 1; i <= 99; i++)
		{
			// Tracer.WriteTrace(20, "I: suchen freie chartnummer index<"+i+">");
			String fnam = path + "\\chart" + numberFormat.format(i) + ".chr";
			File ffile = new File(fnam);
			if (ffile.exists() == true)
				continue;
			else
				// freie nummer gefunden
				return i;
		}
		
		Mbox.Infobox("Error:metatrader is full, max 97 metatrader possible in <" + path + ">");
		return -1;
	}
	
	public String getFreeChartName()
	{
		// ermittelt den nächsten freien chartnamen
		NumberFormat numberFormat = new DecimalFormat("00");
		int freenumber = getFreeChartNumber();
		if (freenumber == -1)
		{
			return null;
		}
		String chartname = "chart" + numberFormat.format(freenumber) + ".chr";
		return chartname;
	}
	
	public void createSpecialProfile(String quelle, String expertname)
	{
		FileAccessDyn fd = new FileAccessDyn();
		// hier wird ein spezielles Profile kopiert
		// historyexporter oder tickdataexporter
		// falls das profile schon da ist
		if (getanzProfiles(expertname) > 0)
		{
			Tracer.WriteTrace(20,
					"W:Profile for expert already exists, I don´t create profile for ea <" + expertname + ">");
			return;
		}
		// den namen des Zielfile ermitteln
		String zielfile = meconf_glob.getAppdata() + "\\profiles\\default\\" + getFreeChartName();
		fd.copyFile2(quelle, zielfile);
		
		// das spezielle profile muss noch modifziert werden bzgl. cur postfix
		
		String histcurrencystring = meconf_glob.getHistexportcurrency();
		FileAccess.FileReplaceString(zielfile, "symbol=EURUSD", "symbol=" + histcurrencystring);
	}
	
	public void checkDoubleEa(String expertname, String path)
	{
		// Es wird überprüft ob dieser Ea mehr als Einmal vorkommt. Ist das der Fall
		// wird das gemeldet
		int anz = 0;
		
		if ((anz = getanzProfiles(expertname)) > 1)
		{
			Mbox.Infobox("W: Ea<" + expertname + "> more than once installation #=<" + anz
					+ ">, please delete some in path<" + path + ">");
		}
	}
	
	public void createProfile(MqlPatch mqlpatch, String quelle, Ea ea, Metaconfig meconf)
	{
		// hier wird das profile.chr in Metaroot/profiles/default geschrieben
		
		String expertname = mqlpatch.getExpertname().replace(".mq4", "");
		
		// falls das profile schon da ist
		if (getanzProfiles(expertname) > 0)
			return;
		
		// zielfile ermitteln
		String zielfile = meconf_glob.getAppdata() + "\\profiles\\default\\" + getFreeChartName();
		
		writeChrFile(mqlpatch, quelle, zielfile, ea, meconf);
	}
	
	private void writeChrFile(MqlPatch mqlpatch, String quelle, String zielfile, Ea ea, Metaconfig meconf)
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
		String curpostfix = meconf.getCurpostfix();
		if (curpostfix != null)
			cfgpatch.setSymbol(symbol + curpostfix);
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
