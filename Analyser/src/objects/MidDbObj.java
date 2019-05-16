package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

public class MidDbObj extends Obj implements DBObject
{
	private int mid=0;
	private String masterstring=null;
	private int tid=0;
	private String adat=null;

	public MidDbObj()
	{}
	
	public MidDbObj(String zeile) throws BadObjectException
	{
		int anz = SG.countZeichen(zeile, "#");
		if ((anz < 3) || (anz > 3))
		{
			Tracer.WriteTrace(10, MidDbObj.class.getName()
					+ ":ERROR:zeile fehlerhaft in MidDbObj zeile=<"
					+ zeile + "> anzahl muss =<4> sein");
			return;
		}
		
		try
		{
			mid = Integer.valueOf(SG.nteilstring(zeile, "#", 1));
			masterstring = new String(SG.nteilstring(zeile, "#", 2));
			tid = Integer.valueOf(SG.nteilstring(zeile, "#", 3));
			adat = new String(SG.nteilstring(zeile, "#", 4));
			
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public String GetSaveInfostring()
	{
		return "mid#masterstring#tid#adat";
	}

	@Override
	public String toString()
	{
		return (mid + "#" + masterstring +  "#"+tid+"#" + adat );
	}


	public int getMid()
	{
		return mid;
	}

	public void setMid(int mid)
	{
		this.mid = mid;
	}

	public String getMasterstring()
	{
		return masterstring;
	}

	public void setMasterstring(String masterstring)
	{
		this.masterstring = masterstring;
	}

	public String getAdat()
	{
		return adat;
	}

	public void setAdat(String adat)
	{
		this.adat = adat;
	}
	public int getThreadid()
	{
		return tid;
	}

	public int getTid()
	{
		return tid;
	}

	public void setTid(int tid)
	{
		this.tid = tid;
	}
	public void postprocess()
	{}
}
