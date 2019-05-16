package attribute;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;

import mainPackage.GC;
import objects.Obj;
import db.DB;

public class ThreadAttribStoreI extends DB
{
	private int tid_glob = 0;
	// objektcache
	private HashMap<String, Integer> map = new HashMap<String, Integer>();

	private void genMap()
	{
		//Dies ist eine turbomap
		int anz = this.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI attribobj = (ThreadAttribObjI) this
					.GetObjectIDX(i);
			String dat = attribobj.getDatum();

			if (map.containsKey(dat) == false)
				map.put(dat, i);
			else
				Tracer.WriteTrace(10, "Error:internal attrib tid<" + tid_glob
						+ "> datum<" + dat + "> pos<" + i + "> doppelt");
		}
	}

	// dieses Attribut-Objekt verwaltet für eine Threadid die einzelnen
	// Attribute
	// für jeden Tag wird für dieses Objekt eine Attributsliste verwaltet
	public ThreadAttribStoreI(int threadid,String attribpfad)
	{
		String apath=null;
		if(attribpfad==null)
			apath="\\db\\Attribute";
		else
			apath=attribpfad;
		
		this.LoadDB(apath, String.valueOf(threadid), 1);
		tid_glob = threadid;
		genMap();
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	private ThreadAttribObjI sucheObj(String datum)
	{
		int pos = 0;

		if (map.containsKey(datum) == false)
			return null;
		else
			pos = map.get(datum);

		ThreadAttribObjI attribobj = (ThreadAttribObjI) this.GetObjectIDX(pos);

		// plausi
		if (Tools.zeitdifferenz_tage(attribobj.getDatum(), datum) == 0)
			return attribobj;
		else
			return null;
	}

	public String calcMindatum()
	{
		int anz = this.GetanzObj();
		String mindate = GC.enddatum;
		// ermittelt das kleinste datum
		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI attribobj = (ThreadAttribObjI) this
					.GetObjectIDX(i);

			String dat = attribobj.getDatum();
			if (Tools.datum_ist_aelter_gleich(mindate, dat) == false)
				mindate = dat;
		}
		return mindate;
	}
	public String calcMaxdatum()
	{
		int anz = this.GetanzObj();
		String maxdate = GC.startdatum;
		// ermittelt das kleinste datum
		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI attribobj = (ThreadAttribObjI) this
					.GetObjectIDX(i);

			String dat = attribobj.getDatum();
			if (Tools.datum_ist_aelter_gleich(maxdate, dat) == true)
				maxdate = dat;
		}
		return maxdate;
	}
	public float getAttrib(String datum, int index)
	{
		ThreadAttribObjI attribobj = sucheObj(Tools.entferneZeit(datum));
		if (attribobj != null)
		{
			// datum gefunden
			return (attribobj.getAttribvalue(index));
		} else
			return 0;
	}

	public void setAttrib(String datum, int index, float value)
	{
		datum = Tools.entferneZeit(datum);

		ThreadAttribObjI attri = sucheObj(datum);
		if (attri != null)
		{
			// objekt vorhanden
			attri.setAttribvalue(index, value);

		} else
		{
			// objekt nicht vorhanden, dann erzeuge
			attri = new ThreadAttribObjI();
			attri.setDatum(datum);
			attri.setAttribvalue(index, value);

			int pos = this.AddObject(attri);
			map.put(datum, pos);
		}
	}

	public void setHandelshinweis(String datum, String hinweis)
	{
		datum = Tools.entferneZeit(datum);

		ThreadAttribObjI attri = sucheObj(datum);
		if (attri != null)
		{
			// objekt vorhanden
			attri.setHandelshinweis(hinweis);
		} else
		{
			// lege objekt an
			attri = new ThreadAttribObjI();
			attri.setDatum(datum);
			attri.setHandelshinweis(hinweis);
			int pos=this.AddObject(attri);
			map.put(datum,pos);
		}
	}

	public String getHandelshinweis(String datum)
	{
		datum = Tools.entferneZeit(datum);

		ThreadAttribObjI attri = sucheObj(datum);
		if (attri != null)
		{
			// objekt vorhanden
			String hinweis=new String(attri.getHandelshinweis());
			return hinweis;
		} else
		{
			return null;
		}
	}
	public int getMaxattribGroesse(int index)
	{
		// index 0 ist das Attribut slidergroesse
		float maxgroesse = 0;
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI attribobj = (ThreadAttribObjI) this
					.GetObjectIDX(i);

			if (attribobj.getAttribvalue(index) > maxgroesse)
				maxgroesse = attribobj.getAttribvalue(index);
		}
		return ((int) maxgroesse);
	}

	public int calcPrognoseanzahl()
	{
		int anz = this.GetanzObj();
		int proganzahl=0;
		//zaehlt wieviele Prognosen es für dieses Objekt gibt
		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI attribobj = (ThreadAttribObjI) this
					.GetObjectIDX(i);

			if (attribobj.getHandelshinweis().contains("Kaufe fuer"))
					proganzahl++;
		}
		return proganzahl;
	}
	public void postprocess()
	{}
	public void calcAttrib()
	{
		//hier wird das attribut neu berechnet
		
	}
}
