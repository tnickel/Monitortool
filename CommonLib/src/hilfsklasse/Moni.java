package hilfsklasse;


import ComData.GC;
import ComData.Inf;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class Moni
{
	Monitor mon_g = null;
	Inf minf = new Inf();

	public Moni()
	{
		minf.setFilename(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\monitor.txt");
	}

	public void begin(String monitorname)
	{
		mon_g = MonitorFactory.start(monitorname);
		minf.writezeile("begin:<" + monitorname + ">");
	}

	public void end(String ausgabestring)
	{
		mon_g.stop();
		minf.writezeile("end:" + mon_g + ":" + ausgabestring);
	}
}
