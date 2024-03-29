package modtools;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.io.IOException;

import mtools.DisTool;
import mtools.Mlist;
import StartFrame.Brokerview;
import data.GlobalVar;
import data.Metaconfig;
import data.Rootpath;

public class MetaStarter
{
	private static int startedbymonitortool = 0;
	
	public static void main(String[] args)
	{
		try
		{
			Process p = Runtime.getRuntime().exec("\"C:\\Programme\\Internet Explorer\\IEXPLORE.EXE\"");
			Process p2 = Runtime.getRuntime().exec("notepad");
			Thread.sleep(5000);
			p.destroy();
			p2.destroy();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	static public void StartAllMetatrader(Brokerview brokerview, int forceflag)
	{
		
		if ((GlobalVar.getMetatraderautostartstop() == 0) && (forceflag == 0))
		{
			Tracer.WriteTrace(20, "I:I dont start all Metatrader because startstop=0 && forceflag=0");
			return;
			
		}
		
		String rootpathbinstarter = Rootpath.getRootpath() + "\\bin\\startmetatrader.bat";
		startedbymonitortool = 1;
		String portextension = "";
		int anz = brokerview.getAnz();
		
		File rpstart = new File(rootpathbinstarter);
		if (rpstart.exists())
			rpstart.delete();
		
		Inf infall = new Inf();
		infall.setFilename(rootpathbinstarter);
		infall.writezeile("@echo OFF");
		
		if (GlobalVar.getPortableflag() == 1)
			portextension = " /portable";
		
		String metatraderexepath = null;
		
		for (int i = 0; i < anz; i++)
		{
			Metaconfig meconf = brokerview.getElem(i);
			if (meconf.getOn() == 0)
				continue;
			
			
			if(meconf.getMttype()==null)
				metatraderexepath = meconf.getNetworkshare_INSTALLDIR() + "\\terminal.exe";
			else if (meconf.getMttype().toLowerCase().equals("mt4"))
				metatraderexepath = meconf.getNetworkshare_INSTALLDIR() + "\\terminal.exe";
			else if (meconf.getMttype().toLowerCase().equals("mt5"))
				metatraderexepath = meconf.getNetworkshare_INSTALLDIR() + "\\terminal64.exe";
			else
				Tracer.WriteTrace(10, "E: metatype not supportet 111122");
			
			meconf.genPortableBatch();
			
			Inf inflocal = new Inf();
			String metatraderstartbatfile = "";
			metatraderstartbatfile = meconf.getNetworkshare_INSTALLDIR() + "\\startmetatrader_portable.bat";
			
			inflocal.setFilename(metatraderstartbatfile);
			if (FileAccess.FileAvailable(metatraderstartbatfile))
				FileAccess.FileDelete(metatraderstartbatfile, 0);
			
			infall.writezeile("start /MIN \"\" \"" + metatraderexepath + "\"" + portextension);
			infall.writezeile("timeout 5");
			
			// schreibt die lokale startdatei f�r den metatrader
			inflocal.writezeile("start /MIN \"\" \"" + metatraderexepath + "\"" + portextension);
			inflocal.writezeile("exit");
			inflocal.close();
			
			boolean result = StartMetatrader(meconf, metatraderstartbatfile);
			try
			{
				if (result == true)
					Thread.sleep(3000);
				else
					continue;
				Tracer.WriteTrace(20, "I:Metatrader<" + meconf.getBrokername() + "> started successfully");
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20,
						"Error:Metatrader<" + meconf.getBrokername() + "> start exception<" + e.getMessage() + ">");
				e.printStackTrace();
			}
		}
		infall.close();
		
	}
	
	static public void StopAllMetatrader(Brokerview brokerview, int forceflag)
	{
		
		if ((GlobalVar.getMetatraderautostartstop() == 0) && (forceflag == 0))
		{
			Tracer.WriteTrace(20, "I:I dont stop all Metatrader because startstop=0 && forceflag=0");
			return;
			
		}
		
		startedbymonitortool = 0;
		int anz = brokerview.getAnz();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig meconf = brokerview.getElem(i);
			StopMetatrader(meconf);
		}
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tracer.WriteTrace(20, "I try to kill all Metatrader");
		KillAllMetatrader(forceflag);
		Tracer.WriteTrace(20, "I killed all Metatrader");
	}
	
	static public boolean CheckMetatraderRunning()
	{
		if (startedbymonitortool == 1)
			return true;
		else
			return false;
	}
	
	static public void KillAllMetatrader(int forceflag)
	{
		// kill all metatrader wird nur ausgef�hrt wenn die metatrader auch
		// durch diese
		// klasse gestartet wurden
		
		// Falls noautostartstop ==1 wird nicht gekiliit
		
		if ((GlobalVar.getMetatraderautostartstop() == 0) && (forceflag == 0))
		{
			Tracer.WriteTrace(20, "I:I dont kill all Metatrader because startstop=0 && forceflag=0");
			return;
			
		}
		
		// taskkill /IM terminal.exe /F
		try
		{
			Process p = Runtime.getRuntime().exec("taskkill /IM terminal.exe");
			Thread.sleep(5000);
			p.destroy();
			
			
			 p = Runtime.getRuntime().exec("taskkill /IM terminal64.exe");
					Thread.sleep(1000);
					p.destroy();
				
			
			 p = Runtime.getRuntime().exec("taskkill /F  /IM terminal.exe  ");
			Thread.sleep(1000);
			p.destroy();
			
			
			 p = Runtime.getRuntime().exec("taskkill /F  /IM terminal64.exe  ");
			Thread.sleep(1000);
			p.destroy();
			
			
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
	
	static public void StartStopMetatrader(Metaconfig meconf)
	//start and stop metatrader after 5 seconds in portablemode
	{
		StartMetatrader(meconf, meconf.getAppdata() + "\\startmetatrader_portable.bat");
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KillAllMetatrader(1);
	}
	
	
	static public boolean StartMetatrader(Metaconfig meconf, String batchfile)
	{
		String portextension = "";
		String metatraderexepath = meconf.getNetworkshare_INSTALLDIR() + "\\terminal.exe";
		String metatraderexepath32 = meconf.getNetworkshare_INSTALLDIR() + "\\terminal64.exe";
		
		// falls der Metatrader noch l�uft
		Process p1 = meconf.getProcessKennung();
		
		if (checkIsRunning(p1, meconf.getBrokername()) == true)
			return false;
		
		// starte den Metatrader
		try
		{
			// Process p = Runtime.getRuntime().exec("" + metatraderexepath +
			// ""+portextension );
			// String exstr="cmd /k start /MIN "+metatraderexepath +" /portable";
			
			String exstr = null;
			if (meconf.getMttype().toLowerCase().equals("mt4"))
			{
				exstr = "cmd /k start /MIN " + metatraderexepath + " /portable";
				;
			} else if (meconf.getMttype().toLowerCase().equals("mt5"))
			{
				exstr = "cmd /k start /MIN " + metatraderexepath32 + " /portable";
				;
			}
			else Tracer.WriteTrace(10, "E:StartMetatrader unkown mttype");
			
			// String[] cmdarray= {"cmd","/k","start","/min",metatraderexepath,"/portable"};
			String[] cmdarray =
			{ "cmd", "/c", "start", "\"\"", "\"" + batchfile + "\"" };
			
			Process p = Runtime.getRuntime().exec(cmdarray);
			meconf.setProcessKennung(p);
			
			// Mlist.add("I:MT<" + meconf.getBrokername() + "> started on
			// <"+metatraderexepath+">", 1);
		} catch (IOException e)
		{
			Tracer.WriteTrace(20, "E: metatrader start exception<" + e.getMessage() + ">");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	static public void StopMetatrader(Metaconfig meconf)
	{
		Process p = meconf.getProcessKennung();
		if (p == null)
		{
			Tracer.WriteTrace(20, "W:process id<" + meconf.getBrokername() + "> could not be stopped");
			return;
		}
		p.destroy();
		Tracer.WriteTrace(20, "I:process id<" + meconf.getBrokername() + ">  stopped");
		
	}
	
	static public void RestartMetatrader_dep(Metaconfig meconf)
	{
		if (meconf.getProcessKennung() == null)
			return;
		try
		{
			StopMetatrader(meconf);
			Thread.sleep(2000);
			// StartMetatrader(meconf);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static private boolean checkIsRunning(Process p, String pname)
	{
		// pr�ft ob precess noch l�uft
		// false falls process nicht l�uft
		// true falls der process noch l�uft
		int val = 0;
		
		try
		{
			// falls �berhaupt onch nicht gestartet, dann l�uft er nicht
			if (p == null)
				return false;
			
			// falls es einen exitwert gibt dann l�uft er nicht mehr
			val = p.exitValue();
			return false;
		} catch (IllegalThreadStateException e)
		{
			// die exception wird ausgel�st falls kein exit-value ausgelesen
			// werden kann
			// d.h. der process l�uft dann noch
			Tracer.WriteTrace(20, "20: I can�t start process <" + pname + "> again, it is already running");
			return true;
		}
	}
	
}
