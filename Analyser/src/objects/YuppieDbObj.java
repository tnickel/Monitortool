package objects;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import db.DbException;

public class YuppieDbObj extends Obj implements DBObject
{
	private String wkn = null;
	private String isin = null;
	private int threadid = 0;
	private String aktname = null;
	private int state = 0;
	private String branchesektor = null;

	private String b�rse = null;
	private String benchmark = null;
	private String sigdatmed = null;
	private String sigdatlong = null;
	private String land = null;
	private String betrachtungszeitraum = null;
	private String gewinnmittel = null;// Gewinn bei befolgung der progn.
	private String �nderungmittel = null;// �nderung der Aktie
	private String gewinnlang = null;// Gewinn bei befolgung der progn.
	private String �nderunglang = null;// �nderung der Aktie

	public YuppieDbObj()
	{
	}
	public int compareTo(Obj argument)
	{
		return 0;
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

	public int getThreadid()
	{
		return threadid;
	}

	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}

	public String getAktname()
	{
		return aktname;
	}

	public void setAktname(String aktname)
	{
		this.aktname = aktname;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public String getBranchesektor()
	{
		return branchesektor;
	}

	public void setBranchesektor(String branchesektor)
	{
		this.branchesektor = branchesektor;
	}

	public String getB�rse()
	{
		return b�rse;
	}

	public void setB�rse(String b�rse)
	{
		this.b�rse = b�rse;
	}

	public String getBenchmark()
	{
		return benchmark;
	}

	public void setBenchmark(String benchmark)
	{
		this.benchmark = benchmark;
	}

	public String getSigdatmed()
	{
		return sigdatmed;
	}

	public void setSigdatmed(String sigdatmed)
	{
		this.sigdatmed = sigdatmed;
	}

	public String getSigdatlong()
	{
		return sigdatlong;
	}

	public void setSigdatlong(String sigdatlong)
	{
		this.sigdatlong = sigdatlong;
	}

	public String getLand()
	{
		return land;
	}

	public void setLand(String land)
	{
		this.land = land;
	}

	public String getBetrachtungszeitraum()
	{
		return betrachtungszeitraum;
	}

	public void setBetrachtungszeitraum(String betrachtungszeitraum)
	{
		this.betrachtungszeitraum = betrachtungszeitraum;
	}

	public String getGewinnmittel()
	{
		return gewinnmittel;
	}

	public void setGewinnMittel(String gewinnmittel)
	{
		this.gewinnmittel = gewinnmittel;
	}

	public String get�nderungMittel()
	{
		return �nderungmittel;
	}

	public void set�nderungMittel(String �nderungmittel)
	{
		this.�nderungmittel = �nderungmittel;
	}

	public String getGewinnLang()
	{
		return gewinnlang;
	}

	public void setGewinnLang(String gewinnlang)
	{
		this.gewinnlang = gewinnlang;
	}

	public String get�nderungLang()
	{
		return �nderunglang;
	}

	public void set�nderungLang(String �nderunglang)
	{
		this.�nderunglang = �nderunglang;
	}

	@Override
	public String toString()
	{
		return (aktname + "#" + wkn + "#" + isin + "#" + threadid + "#" + state
				+ "#" + branchesektor + "#" + land + "#" + b�rse + "#"
				+ benchmark + "#" + sigdatmed + "#" + sigdatlong + "#"
				+ betrachtungszeitraum + "#" + gewinnmittel + "#"
				+ �nderungmittel + "#" + gewinnlang + "#" + �nderunglang);
	}

	@Override
	public String GetSaveInfostring()
	{
		return "aktname#wkn#isin#threadid#state#branchesektor#land#b�rse#benchmark#sigdatmed#sigdatlong#betrachtzeitr#gewinnmittel#�nderungmittel#gewinnlang#�nderunglang";
	}

	public YuppieDbObj(String zeile) throws DbException
	{

		if ((Tools.countZeichen(zeile, "#")) != 15)
		{
			System.out.println(YuppieDbObj.class.getName()
					+ ":ERROR:username fehlerhaft in yuppie.db zeile=<" + zeile
					+ "> ");
			Tracer.WriteTrace(10, UserDbObj.class.getName()
					+ ":ERROR:username fehlerhaft in userstore.db zeile=<"
					+ zeile + ">");
			throw new DbException("Fehler im Userdbobj" + zeile);
		}
		try
		{
			// aktname#wkn#isin#threadid#state#branche#sektor#land#b�rse#benchmark#sigdatmed#sigdatlong#betrachtungszeitrum#gewinnmittel#�nderungmittel#gewinnlang#�nderunglang
			aktname = new String(SG.nteilstring(zeile, "#", 1));
			wkn = new String(SG.nteilstring(zeile, "#", 2));
			isin = new String(SG.nteilstring(zeile, "#", 3));
			threadid = Integer.valueOf(SG.nteilstring(zeile, "#", 4));
			state = Integer.valueOf(SG.nteilstring(zeile, "#", 5));
			branchesektor = new String(SG.nteilstring(zeile, "#", 6));
			land = new String(SG.nteilstring(zeile, "#", 7));
			b�rse = new String(SG.nteilstring(zeile, "#", 8));
			benchmark = new String(SG.nteilstring(zeile, "#", 9));
			sigdatmed = new String(SG.nteilstring(zeile, "#", 10));
			sigdatlong = new String(SG.nteilstring(zeile, "#", 11));
			betrachtungszeitraum = new String(SG.nteilstring(zeile, "#", 12));
			gewinnmittel = new String(SG.nteilstring(zeile, "#", 13));
			�nderungmittel = new String(SG.nteilstring(zeile, "#", 14));
			gewinnlang = new String(SG.nteilstring(zeile, "#", 15));
			�nderunglang = new String(SG.nteilstring(zeile, "#", 16));
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void postprocess()
	{}
}
