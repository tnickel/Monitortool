package data;

import gui.Mbox;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;



public class DropGlobalVar
{
	//diese Struktur beinhaltet alle globalen variablen
	static DropGlobalVarData dropgc= new DropGlobalVarData();
	static private String conffnam=DropRootpath.getRootpath()+"\\conf\\config.xml";

	//falls dropcontrol==1 ist, werden über die Dropbox auch die EA´s ein und ausgeschaltet
	static private Boolean dropcontrol=false;
	
	 public DropGlobalVar()
	{
		if(FileAccess.FileAvailable(conffnam)==false)
		{
			Mbox.Infobox("no config under <"+conffnam+"> please set config first");
			return;
		}
		
		Inf inf = new Inf();
		inf.setFilename(conffnam);

		// config einlesen
		dropgc = (DropGlobalVarData)inf.loadXST();

		inf.close();
	}
	
	static public String getMetarootpath()
	{
		return(dropgc.getMetarootpath());
		
	}
	
	static public void setmetarootpath(String path)
	{
		dropgc.setMetarootpath(path);
	}
	static public String getFstrootpath()
	{
		return(dropgc.getFstrootpath());
	}
	static public void setFstrootpath (String path)
	{
		dropgc.setFstrootpath(path);
	}
	static public String getDropboxdir()
	{
		return(dropgc.getDropboxdir());
	}
	static public void setDropboxdir(String dropboxdir)
	{
		dropgc.setDropboxdir(dropboxdir);
	}
	static public String getUpdatedir()
	{
		return(dropgc.getUpdatepath());
	}
	static public void setUpdatedir (String path)
	{
		dropgc.setUpdatepath(path);
	}
	static public void setDropComm(String path)
	{
		dropgc.setDropcomm(path);
	}
	static public String getDropComm()
	{
		return(dropgc.getDropcomm());
	}
	static public void setDropcontrol(boolean dropc)
	{
		dropgc.setDropcontrol(dropc);
	}
	static Boolean  getDropcontrol()
	{
		return(dropgc.getDropcontrol());
	}
	static public void setCheckintervall(int sec)
	{
		dropgc.setCheckintervall(sec);
	}
	static public int getCheckintervall()
	{
		return(dropgc.getCheckintervall());
	}
	
	static public void save()
	{
		System.out.println("speichere <"+conffnam+">");
		Inf inf = new Inf();
		inf.setFilename(conffnam);
		inf.saveXST(dropgc);
		
		inf.close();
	}
}
