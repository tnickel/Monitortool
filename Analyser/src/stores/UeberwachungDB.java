package stores;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashSet;

import kurse.KursValueDB;
import kurse.Kursvalue;
import objects.EventDbObj;
import objects.UeberwachungDbObj;
import db.DB;

public class UeberwachungDB extends DB
{
	static private int minFreeID = 80000000;

	public UeberwachungDB()
	{

		Tracer.WriteTrace(20, "Info:konstruktor von UeberwachungDB");
		LoadDB("ueberwachung", null, 0);

	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public int getFreeID()
	{
		int anz = this.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj uebobj = (UeberwachungDbObj) this.GetObjectIDX(i);
			if ((uebobj.getUebmid()) >= minFreeID)
				minFreeID++;
		}
		return minFreeID;
	}

	public void DeleteId(int id)
	{
		int anz = this.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj uebobj = (UeberwachungDbObj) this.GetObjectIDX(i);
			if ((uebobj.getUebmid()) == id)
			{
				this.DeleteObjectIDX(i);
				return;
			}
		}
	}
	public UeberwachungDbObj getObjektIDXid(int id)
	{
		int anz = this.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj uebobj = (UeberwachungDbObj) this.GetObjectIDX(i);
			if ((uebobj.getUebmid()) == id)
			{
				return uebobj;
				
			}
		}
		return null;
	}
	public HashSet<String> getSymbmenge(int type)
	{
		HashSet<String> symbolmenge = new HashSet<String>();

		int anz = this.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj uebobj = (UeberwachungDbObj) this.GetObjectIDX(i);
			String symb = uebobj.getSymbol();

			// Aktienkurs und Symbol
			if (uebobj.getType() != type)
				continue;

			if (symb.length() > 2)
				symbolmenge.add(symb);
			else
				Tracer.WriteTrace(10,
						"Error: ueberwachungsobjekt type=1 muss symbol haben");
		}
		return symbolmenge;
	}

	public void checkEvents(EventsDB evdb,int type,HashSet<String> symbolmenge,ThreadsDB tdb)
	{
		// überprüft ob sich die kurse in den Grenzen befinden
		// Wenn nein werden events ausgelöst und gespeichert
		
		int anz = this.GetanzObj();

		// gehe durch die ganze überwachung
		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj uebobj = (UeberwachungDbObj) this.GetObjectIDX(i);
			// der type muss stimmen
			if (uebobj.getType() != type)
				continue;
			
			// das Symbol muss stimmen
			String symb = uebobj.getSymbol();
			if(symbolmenge.contains(symb)==false)
				continue;
			
			//Bedingungen erfüllt dann überprüfe den Kurs
			KursValueDB kvdb = new KursValueDB(symb, 1,tdb);

			if(kvdb.calcAnzKurswerte()==0)
				continue;
			
			//hole den neusten Kurswert
			String maxdate = kvdb.calcMaxdate();
			Kursvalue k1obj = kvdb.holeKurswert(maxdate, 0);

			// schaue nach ob der Kurs in den grenzen ist
			Float val = k1obj.getKv();
			if ((val < uebobj.getMinval()) || (val > uebobj.getMaxval()))
			{
				//Überwachung hat ausgelöst und ein event wird erzeugt
				EventDbObj evdbo = new EventDbObj();
				evdbo.setAusloesedate(Tools.entferneZeit(Tools
						.get_aktdatetime_str()));
				evdbo.setId(uebobj.getUebmid());
				evdbo.setType(uebobj.getType());
				evdbo.setTid(uebobj.getThreadid());
				evdbo.setAnzeigename(uebobj.getAnzeigename());
				evdbo.setVal(val);
				//objekt updaten
				evdb.GenUpdateObject(evdbo);
			}
		}
		evdb.WriteDB();
		this.WriteDB();
	}
	public void postprocess()
	{}
}
