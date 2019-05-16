package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import interfaces.DBObject;

public class BBeventDbObj extends Obj implements DBObject
{
	private String date=null;
	private String typename=null;
	private String suchwort=null;
	private String aktienname=null;
	private String boerblatt=null;
	private String verznam=null;
	private int threadid=0;
	
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public BBeventDbObj()
	{}
	public BBeventDbObj(String zeile) 
	{
		try
		{
			date = new String(SG.nteilstring(zeile, "#", 1));
			typename = new String(SG.nteilstring(zeile, "#", 2));
			suchwort = new String(SG.nteilstring(zeile, "#", 3));
			aktienname=new String(SG.nteilstring(zeile, "#", 4));
			boerblatt = new String(SG.nteilstring(zeile, "#", 5));
			verznam = new String(SG.nteilstring(zeile, "#", 6));

			
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getTypename()
	{
		return typename;
	}
	public void setTypename(String typename)
	{
		this.typename = typename;
	}
	public String getSuchwort()
	{
		return suchwort;
	}
	public void setSuchwort(String suchwort)
	{
		this.suchwort = suchwort;
	}
	public String getAktienname()
	{
		return aktienname;
	}
	public void setAktienname(String aktienname)
	{
		this.aktienname = aktienname;
	}
	public String getBoerblatt()
	{
		return boerblatt;
	}
	public void setBoerblatt(String boerblatt)
	{
		this.boerblatt = boerblatt;
	}
	public String getVerznam()
	{
		return verznam;
	}
	public void setVerznam(String verznam)
	{
		this.verznam = verznam;
	}
	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}
	@Override
	public String GetSaveInfostring()
	{
		return "date#typename#suchwort#aktienname#boerblatt#verznam";
	}

	@Override
	public String toString()
	{
		return (date + "#" + typename + "#" + suchwort + "#" + aktienname + "#"
				+ boerblatt + "#" + verznam);
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
		BBeventDbObj other = (BBeventDbObj) obj;
	
		if (typename.equals(other.typename)==false)
			return false;
		if( suchwort.equals(other.suchwort)==false )
			return false;
		
		return true;
	}

	public int getThreadid()
	{
		return threadid;
	}
	public void postprocess()
	{}
}
