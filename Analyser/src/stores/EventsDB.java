package stores;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.EventDbObj;
import db.DB;

public class EventsDB extends DB
{
	public EventsDB()
	{

		Tracer.WriteTrace(20, "Info:konstruktor von UeberwachungDB");
		LoadDB("events", null, 0);

	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public String getNeustesDatum()
	{
		String maxdate = GC.startdatum;
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			EventDbObj evobj = (EventDbObj) this.GetObjectIDX(i);
			String dat = evobj.getAusloesedate();
			// suche neues mindate
			if ((maxdate != null) && (dat != null))
				if (Tools.datum_ist_aelter_gleich(dat, maxdate) == false)
					maxdate = dat;
		}
		return maxdate;
	}
	public void postprocess()
	{}
}
