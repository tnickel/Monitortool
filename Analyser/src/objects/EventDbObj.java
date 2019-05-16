package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

public class EventDbObj extends Obj implements DBObject
{
	//Wird ein Überwachungsobjekt ausgelöst wird ein Ereigniss registriert
	//Bsp: überwachungsobjekt kurs Symbol: abc, min=0.5 max=0.7
	//Ereigniss Symbol=abc, Kurs = 0.8, d.h. es wird ein Ereigniss generiert und in der
	//Ereignissdb abgelegt
	
	//type: Kurs, Optionsschein, Future
	//id: Eindeutige ID-für dies Ereigniss, die id ist im überwachungsobjekt zu finden
	//anzeigename:
	//ausloesedate:Datum des auftretens
	//Info: Gibt info was genau passiert ist
	//Bezeichner:(symbol,wkn,isin)
	
	private int type=0;
	private int id=0;
	private int tid=0;
	private String anzeigename=null;
	private String ausloesedate=null;
	private String info=null;
	private String bezeichner=null;
	private float val=0;
	
	public EventDbObj()
	{}
	
	public EventDbObj(String zeile) throws BadObjectException
	{
		int anz = SG.countZeichen(zeile, "#");
		if ((anz < 7) || (anz > 7))
		{
			Tracer.WriteTrace(10, EventDbObj.class.getName()
					+ ":ERROR:zeile fehlerhaft in EreignissDbObj zeile=<"
					+ zeile + "> anzahl # muss =<7> sein");
			return;
		}
		
		try
		{
			type = Integer.valueOf(SG.nteilstring(zeile, "#", 1));
			id = Integer.valueOf(SG.nteilstring(zeile, "#", 2));
			tid=Integer.valueOf(SG.nteilstring(zeile, "#", 3));
			anzeigename = new String(SG.nteilstring(zeile, "#", 4));
			ausloesedate = new String(SG.nteilstring(zeile, "#", 5));
			info = new String(SG.nteilstring(zeile, "#", 6));
			bezeichner = new String(SG.nteilstring(zeile, "#", 7));
			val=Float.valueOf(SG.nteilstring(zeile, "#", 8));
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//getter und setter
	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getTid()
	{
		return tid;
	}

	public void setTid(int tid)
	{
		this.tid = tid;
	}

	public String getAnzeigename()
	{
		return anzeigename;
	}

	public void setAnzeigename(String anzeigename)
	{
		this.anzeigename = anzeigename;
	}

	public String getAusloesedate()
	{
		return ausloesedate;
	}

	public void setAusloesedate(String ausloesedate)
	{
		this.ausloesedate = ausloesedate;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public String getBezeichner()
	{
		return bezeichner;
	}

	public void setBezeichner(String bezeichner)
	{
		this.bezeichner = bezeichner;
	}
	
	
	//allemeine Funktionen
	
	@Override
	public String GetSaveInfostring()
	{
		return "type#id#tid#anzeigename#ausloesedate#info#bezeichner#val";
	}

	public float getVal()
	{
		return val;
	}

	public void setVal(float val)
	{
		this.val = val;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventDbObj other = (EventDbObj) obj;
		if (id != other.id)
			return false;
		if (ausloesedate.equals(other.ausloesedate)==false)
				return false;
			
		return true;
	}

	@Override
	public String toString()
	{
		return (type + "#" + id + "#"+tid+ "#"+anzeigename+"#" + ausloesedate+"#"+info+"#"+bezeichner+"#"+val );
	}
	public int getThreadid()
	{
		return tid;
	}
	//spezielle Funktionen
	public void postprocess()
	{}
}
