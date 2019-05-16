package kurse;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import objects.BadObjectException;
import objects.Obj;
import objects.ThreadDbObj;

public class KursvalueNativeDbObj extends Obj implements DBObject
{
	// dieses Objekt hält einen Kurs aus den von yahoo geladenen Kurslisten

	private String date = null;
	private float open = 0;
	private float high = 0;
	private float low = 0;
	private float close = 0;
	private float volume = 0;
	private float adjclose = 0;
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public float getOpen()
	{
		return open;
	}

	public void setOpen(float open)
	{
		this.open = open;
	}

	public float getHigh()
	{
		return high;
	}

	public void setHigh(float high)
	{
		this.high = high;
	}

	public float getLow()
	{
		return low;
	}

	public void setLow(float low)
	{
		this.low = low;
	}

	public float getClose()
	{
		return close;
	}

	public void setClose(float close)
	{
		this.close = close;
	}

	public float getVolume()
	{
		return volume;
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
	}

	public float getAdjclose()
	{
		return adjclose;
	}

	public void setAdjclose(float adjclose)
	{
		this.adjclose = adjclose;
	}

	public KursvalueNativeDbObj(String zeile) throws BadObjectException
	{

		if (SG.countZeichen(zeile, ",") != 6)
		{
			String emsg = ":ERROR:Zeilen für Objekt fehlerhaft zeile b=<" + zeile
					+ ">";
			System.out.println(emsg);
			Tracer.WriteTrace(20, emsg);
			throw new BadObjectException(emsg);

		}
		int pos = 1;
		try
		{
			// Date,Open,High,Low,Close,Volume,Adj Close,
			date = new String(SG.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			open = Float.valueOf(SG.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			high = Float.valueOf(SG.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			low = Float.valueOf(SG.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			close = Float.valueOf(SG.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			volume = Float.valueOf(SG.nteilstring(zeile, ",", pos));
			pos = pos + 1;
			adjclose = Float.valueOf(SG.nteilstring(zeile, ",", pos));

		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, ThreadDbObj.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		return (date + "," + open + "," + high + "," + low + "," + close + ","
				+ volume + "," + adjclose);
	}

	@Override
	public String GetSaveInfostring()
	{
		return "date#open#high#low#close#volume#adjclose";
	}

	public int getThreadid()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	public void postprocess()
	{}
}
