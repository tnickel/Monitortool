package stores;

import hilfsklasse.Tracer;
import objects.Obj;
import db.DB;

public class BBeventDB extends DB
{
	public BBeventDB()
	{

		Tracer.WriteTrace(20, "Info:konstruktor von UeberwachungDB");
		LoadDB("bbevent", null, 0);

	}
	
	public boolean plausiVorNeuaufnahme(Object obj)
	{
	return true;
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public void postprocess()
	{}
}
