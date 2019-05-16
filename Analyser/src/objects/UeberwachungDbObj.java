package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

public class UeberwachungDbObj extends Obj implements DBObject
{
	// dieses Objekt beinhaltet überwachungsfunktionen. Es existiert eine Liste
	// mit zu
	// Überwachenden Ereignissen.

	// type: Kurs, Optionsschein, Future
	// id: Eine eindeutige ID für dies Ereigniss
	// anzeigename:
	// erstelldat: Wann dieser Eintrag erstellt/geändert wurde
	// lastload: Wenn zum letzten male was runtergeladen wurde
	// filepath: Pfad zu einem Textfile welches weitere Infos enthält
	// minval: minvalue
	// maxvalue: maxvalue
	// symbol:
	// wkn:
	// isin:

	private int type = 0;
	private int uebmid = 0;
	private int tid = 0;
	private String anzeigename = null;
	private String erstelldat = null;
	private String lastload = null;
	private String filepath = null;
	private float minval = 0;
	private float maxval = 0;
	private String symbol = null;
	private String wkn = null;
	private String isin = null;
	private String puschertext=null;
	private int marker=0;

	public UeberwachungDbObj()
	{
	}

	public UeberwachungDbObj(String zeile) throws BadObjectException
	{
		int anz = SG.countZeichen(zeile, "#");
		if ((anz < 12) || (anz > 13))
		{
			Tracer.WriteTrace(10, UeberwachungDbObj.class.getName()
					+ ":ERROR:zeile fehlerhaft in UeberwachungDbObj zeile=<"
					+ zeile + "> anzahl # muss =<12-13> sein");
			return;
		}

		try
		{
			type = Integer.valueOf(SG.nteilstring(zeile, "#", 1));
			uebmid = Integer.valueOf(SG.nteilstring(zeile, "#", 2));
			tid = Integer.valueOf(SG.nteilstring(zeile, "#", 3));

			anzeigename = new String(SG.nteilstring(zeile, "#", 4));
			erstelldat = new String(SG.nteilstring(zeile, "#", 5));
			lastload = new String(SG.nteilstring(zeile, "#", 6));
			filepath = new String(SG.nteilstring(zeile, "#", 7));

			minval = Float.valueOf(SG.nteilstring(zeile, "#", 8));
			maxval = Float.valueOf(SG.nteilstring(zeile, "#", 9));

			symbol = new String(SG.nteilstring(zeile, "#", 10));
			wkn = new String(SG.nteilstring(zeile, "#", 11));
			isin = new String(SG.nteilstring(zeile, "#", 12));
			puschertext=new String(SG.nteilstring(zeile, "#", 13));
			if(anz==13)
				marker=Integer.valueOf(SG.nteilstring(zeile, "#", 14));

		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String GetSaveInfostring()
	{
		return "type#uebmid#tid#anzeigename#erstelldat#lastload#filepath#minval#maxval#symbol#wkn#isin#puschertext#marker";
	}

	@Override
	public String toString()
	{
		return (type + "#" + uebmid + "#" + tid + "#" + anzeigename + "#"
				+ erstelldat + "#" + lastload + "#" + filepath + "#" + minval
				+ "#" + maxval + "#" + symbol + "#" + wkn + "#" + isin+"#"+puschertext+"#"+marker);
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
		UeberwachungDbObj other = (UeberwachungDbObj) obj;
		if (uebmid != other.uebmid)
			return false;
		return true;
	}
	
	public int getThreadid()
	{
		return tid;
	}
	//*****************************************************************************************************
	//getter und setter
	//*****************************************************************************************************
	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getUebmid()
	{
		return uebmid;
	}

	public void setUebmid(int id)
	{
		this.uebmid = id;
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

	public String getErstelldat()
	{
		return erstelldat;
	}

	public void setErstelldat(String erstelldat)
	{
		this.erstelldat = erstelldat;
	}

	public String getLastload()
	{
		return lastload;
	}

	public void setLastload(String lastload)
	{
		this.lastload = lastload;
	}

	public String getFilepath()
	{
		return filepath;
	}

	public void setFilepath(String filepath)
	{
		this.filepath = filepath;
	}

	public float getMinval()
	{
		return minval;
	}

	public void setMinval(float minval)
	{
		this.minval = minval;
	}

	public float getMaxval()
	{
		return maxval;
	}

	public void setMaxval(float maxval)
	{
		this.maxval = maxval;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getWkn()
	{
		return wkn;
	}

	public void setWkn(String wkn)
	{
		this.wkn = wkn;
	}

	public String getIsin()
	{
		return isin;
	}

	public void setIsin(String isin)
	{
		this.isin = isin;
	}

	public String getPuschertext()
	{
		return puschertext;
	}

	public void setPuschertext(String puschertext)
	{
		this.puschertext = puschertext;
	}

	public int getMarker()
	{
		return marker;
	}

	public void setMarker(int marker)
	{
		this.marker = marker;
	}
	
	//*****************************************************************************************************
	//Funktionen
	//*****************************************************************************************************
	
	public void postprocess()
	{}
	
}
