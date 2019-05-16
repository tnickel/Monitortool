package mainPackage;

import hilfsklasse.Sysaktion;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import swtOberfläche.SwtGui;

public class MainClass
{
	// version 19.01.10
	/**
	 * 
	 * @param args
	 * @throws InterruptedException
	 *             Java7:-XX:MaxHeapSize=256m -Xms512m -Xmx512m -XX:MaxPermSize=256m
	 *             -Xms120m -Xmx1536m -Dcom.sun.management.jmxremote
	 *  			-Xms160m -Xmx1800m -Dcom.sun.management.jmxremote
	 *  			-Xmx928m
	 */
	public static void main(String[] args)
	{
		// v002
		int aktion = 1;
		Sysaktion sys = new Sysaktion();
		Tracer.SetTraceFilePrefix(GC.rootpath + "\\db\\trace.txt");
		Tracer.SetTraceLevel(30);
		Tracer.WriteTrace(30, "Aktion=" + aktion);
		/** **** KURSE ***** */
		
		if (aktion == 1)
		{
			Tools.checkFilesystem();
			SwtGui gui = new SwtGui();
			if(GC.TESTMODE==1)
				Tracer.WriteTrace(10, "Info: Testmode is on");
			gui.initialize();
		}
		
	
		
		
		// sys.GoSleep();
		System.out.println("Alles Fertig !!");

	}
}
