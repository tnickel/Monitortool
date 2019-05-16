package kurse;

import objects.Obj;
import db.DB;

public class KursiconDB extends DB
{

	private int dbopenflag = 0;

	public KursiconDB()
	{
		LoadDB("kursicon", null, 0);
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public boolean IstMasteridVorhanden(int masterid)
	{
		int i = 0;
		int anz = GetanzObj();
		for (i = 0; i < anz; i++)
		{
			KursIconObj ki = (KursIconObj) GetObjectIDX(i);
			int mi = ki.getMasterid();
			if (mi == masterid)
				return true;

		}
		return false;
	}

	public KursIconObj GetKursIconObj(int masterid)
	{
		// holt das objekt mit der Masterid aus der db falles es da ist
		// ansonsten null
		int i = 0;
		int anz = GetanzObj();
		for (i = 0; i < anz; i++)
		{
			KursIconObj ki = (KursIconObj) GetObjectIDX(i);
			int mi = ki.getMasterid();
			if (mi == masterid)
				return ki;

		}
		return null;
	}
	public void postprocess()
	{}
}
