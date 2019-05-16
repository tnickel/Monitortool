package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import interfaces.DBObject;

public class ChampionDbObj extends Obj implements DBObject
{
	private String name=null;
	private String wkn=null;
	private String symbol=null;
	private String pressboerse=null;
	private int masterid=0;
	private int status=0;
	private String adatum=null;
	private String suchwoerter=null;
	
	public ChampionDbObj()
	{}
	
	public ChampionDbObj(String zeile) throws BadObjectException
	{
		try
		{
			name = new String(SG.nteilstring(zeile, "#", 1));
			wkn = new String(SG.nteilstring(zeile, "#", 2));
			symbol = new String(SG.nteilstring(zeile, "#", 3));
			//diese boerse winrd in kursedb durchgepresst, hat also eine höhere prio
			pressboerse = new String(SG.nteilstring(zeile, "#", 4));
			masterid = Integer.valueOf(SG.nteilstring(zeile, "#", 5));
			status = Integer.valueOf(SG.nteilstring(zeile, "#", 6));
			adatum = new String(SG.nteilstring(zeile, "#", 7));
			suchwoerter = new String(SG.nteilstring(zeile, "#", 8));
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getWkn()
	{
		return wkn;
	}

	public void setWkn(String wkn)
	{
		this.wkn = wkn;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	
	public String getPressBoerse()
	{
		return pressboerse;
	}

	public void setPressBoerse(String boer)
	{
		this.pressboerse = boer;
	}

	public int getMasterid()
	{
		return masterid;
	}

	public void setMasterid(int masterid)
	{
		this.masterid = masterid;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getAdatum()
	{
		return adatum;
	}

	public void setAdatum(String adatum)
	{
		this.adatum = adatum;
	}

	public String getSuchwoerter()
	{
		return suchwoerter;
	}

	public void setSuchwoerter(String suchwoerter)
	{
		this.suchwoerter = suchwoerter;
	}

	@Override
	public String GetSaveInfostring()
	{
		return "mame#wkn#symbol#SetBoerse#masterid#status#adatum#suchwoerter";
	}

	@Override
	public String toString()
	{
		return (name + "#" + wkn+"#"+symbol +"#"+pressboerse+"#" + masterid + "#"
				+ status+"#"+adatum+"#"+suchwoerter );
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
		ChampionDbObj other = (ChampionDbObj) obj;
		if (wkn.equalsIgnoreCase(other.wkn)==false)
			return false;
		return true;
	}

	public int getThreadid()
	{
		return 0;
	}
	public void postprocess()
	{}
}
