package aut;

import java.util.Calendar;

import org.eclipse.swt.widgets.Display;

import StartFrame.StartMonitorWork;
import hiflsklasse.Tracer;
import mtools.Mlist;

public class FridayClose
{
		static private StartMonitorWork smw_glob=null;
		static public void startTimer(StartMonitorWork smw)
		{
			//Start Friday Timer after 5 secs
			smw_glob=smw;
	
			
			Tracer.WriteTrace(20, "Friday Timer started");
			
			Calendar aktCal = Calendar.getInstance(); 
			long timeakt = aktCal.getTime().getTime();
			int val=aktCal.get(Calendar.DAY_OF_WEEK);
			aktCal.set(Calendar.DAY_OF_WEEK,6);
			aktCal.set(Calendar.HOUR_OF_DAY, 20);
			aktCal.set(Calendar.MINUTE, 58);
			long timefriday=aktCal.getTime().getTime();
			
			long diffsec_ms=(timefriday-timeakt);
			if (diffsec_ms<0)
				diffsec_ms=diffsec_ms+(60*60*24*7*1000);
			
			Display.getDefault().timerExec((int)diffsec_ms, fridaytimer);
			int shours=(int)diffsec_ms/1000/3600;
			int sdays=shours/24;
			String message="Set Friday ShutdownTimer in hours=<"+shours+"> days= <"+sdays+"> ";
			Mlist.add(message,1);
			Tracer.WriteTrace(20, message);
		}
		
		static final Runnable fridaytimer = new Runnable()
		{
			public void run()
			{
				
				Tracer.WriteTrace(20, "Hier wird alles gestoppt");
				Mlist.add("Friday Shutdown", 1);
				smw_glob.stopAllMt(1);
				smw_glob.saveAndExit();
				
			}
		};
		
	
	
}
