package objects;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

public class KeyDbObj extends Obj implements  DBObject
{
	private String keyword=null;
	private String aname=null;
	//Aufnahmedatum: gibt an wann das keywort aufgenommen wurde
	private int mid=0;
	private String adat=null;
	//type0=Das Schlüsselwort darf auch teilwort sein
	//type1=Das Schlüsselwort muss am Wortanfang auftauchen
	//type2=Es wird exakt dies Wort gesucht
	private int type=0;
	//jedesmal wenn ein Schlüsselwort in einem Datenfile gefunden wurde, wird
	//der hitcounter erhöht
	//Der Hitcounter wird beim Anlegen der Keyliste auf 0 gesetzt
	private int hitcounter=0;
	
	
	
	public String getKeyword()
	{
		return keyword;
	}
	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}
	
	public String getAname()
	{
		return aname;
	}
	public void setAname(String aname)
	{
		this.aname = aname;
	}
	public int getMid()
	{
		return mid;
	}
	public void setMid(int mid)
	{
		this.mid = mid;
	}
	public String getAdat()
	{
		return adat;
	}
	public void setAdat(String adat)
	{
		this.adat = adat;
	}
	
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getHitcounter()
	{
		return hitcounter;
	}
	public void setHitcounter(int hitcounter)
	{
		this.hitcounter = hitcounter;
	}
	public int getThreadid()
	{
		return 0;
	}
	public KeyDbObj(String zeile)
	{
		int anz = Tools.countZeichen(zeile, "#");
		if ((anz < 5) || (anz > 5))
		{
			System.out.println(BoersenblattDbObj.class.getName()
					+ ":ERROR:threadname fehlerhaft in keydb.db zeile=<"
					+ zeile + "> anz<" + anz + ">");
			Tracer.WriteTrace(10, BoersenblattDbObj.class.getName()
					+ ":ERROR:threadname fehlerhaft in keydb.db zeile=<"
					+ zeile + ">");
			return;
		}
		int pos = 1;
		try
		{
			keyword= new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			aname =new String(Tools.nteilstring(zeile, "#", pos));
			pos=pos+1;
			mid = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			adat = new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			type = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			hitcounter = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, BoersenblattDbObj.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String toString()
	{
		return (keyword+"#"+aname+"#"+mid + "#" + adat +"#"+type+"#"+hitcounter );
	}

	public String GetSaveInfostring()
	{
		return "keyword#aname#mid#adat#type#hitcounter";
	}
	public void postprocess()
	{}
}
