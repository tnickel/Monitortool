package comperatoren;

import hilfsklasse.Tools;

import java.util.Comparator;

import slider.SlideruebersichtObj;

public class SliderComparator implements Comparator
{
	// sortiere die sliderliste so, das die am wenigsten aktuellen Sliderobjekte
	// vorne stehen

	public int compare(Object o1, Object o2)
	{
		// betrachte beim sortieren das lastupdate

		String lastUpdate1 = ((SlideruebersichtObj) o1).getLastupdate();
		String lastUpdate2 = ((SlideruebersichtObj) o2).getLastupdate();

		if ((lastUpdate2 == null)||(lastUpdate2.contains("null")))
		{
			/*Tracer.WriteTrace(20, "Threadnam <"
					+ ((ThreadDbObj) o1).getThreadname() + "> o1<" + lastUpdate1
					+ "> " + "Threadnam <" + ((ThreadDbObj) o2).getThreadname()
					+ "> o2<" + lastUpdate2 + "> NNN ret=1 ");*/
			return 1;
		}
		if ((lastUpdate1 == null)||(lastUpdate1.contains("null")))
		{
		/*	Tracer.WriteTrace(20, "Threadnam <"
					+ ((ThreadDbObj) o1).getThreadname() + "> o1<" + lastUpdate1
					+ "> " + "Threadnam <" + ((ThreadDbObj) o2).getThreadname()
					+ "> o2<" + lastUpdate2 + "> NNN ret=0 ");*/
			return 0;
		}
		
		
		if (Tools.datum_ist_aelter(lastUpdate1, lastUpdate2)==true)
		{
			/*Tracer.WriteTrace(20, "Threadnam <"
					+ ((ThreadDbObj) o1).getThreadname() + "> o1<" + lastUpdate1
					+ "> " + "Threadnam <" + ((ThreadDbObj) o2).getThreadname()
					+ "> o2<" + lastUpdate2 + "> ret=1 ");*/
			return 1;
		} else
		{
			/*Tracer.WriteTrace(20, "Threadnam <"
					+ ((ThreadDbObj) o1).getThreadname() + "> o1<" + lastUpdate1
					+ "> " + "Threadnam <" + ((ThreadDbObj) o2).getThreadname()
					+ "> o2<" + lastUpdate2 + "> ret=0 ");*/
			return 0;
		}
	}

}
