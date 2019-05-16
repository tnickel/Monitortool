package attribute;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import objects.Obj;
import objects.UserDbGewinnZeitraumObjI;

public class ThreadAttribObjI extends Obj implements DBObject
{
	// dies objekt speichert für einen tag die Attribute
	private String datum = null;
	private float[] attribvalue = new float[15];
	private String handelshinweis = null;
	public ThreadAttribObjI()
	{
	}
	public String getDatum()
	{
		return datum;
	}
	public void setDatum(String datum)
	{
		this.datum = datum;
	}
	public float getAttribvalue(int index)
	{
		return attribvalue[index];
	}
	public void setAttribvalue(int index, float attribvalue)
	{
		this.attribvalue[index] = attribvalue;
	}
	public String getHandelshinweis()
	{
		return handelshinweis;
	}
	public void setHandelshinweis(String handelshinweis)
	{
		this.handelshinweis = handelshinweis;
	}
	public ThreadAttribObjI(String zeile)
	{
		
		
		
		
		//Tracer.WriteTrace(20, "xay:zeile<"+zeile+">");
		int i = 0;
		int anz = SG.countZeichen(zeile, "#");
		if (anz != 16)
		{
			System.out.println(ThreadAttribObjI.class.getName()
					+ ":ERROR:zeile fehlerhaft zeile=<" + zeile + "> anz<"
					+ anz + ">");
			Tracer.WriteTrace(10, ThreadAttribObjI.class.getName()
					+ ":ERROR:zeile fehlerhaft  zeile=<" + zeile + ">anz<"
					+ anz + ">");
			return;
		}
		int pos = 1;
		try
		{
			datum = new String(SG.nteilstring(zeile, "#", pos));
			pos++;

			for (int j = 0; j < 15; j++)
			{
				attribvalue[j] = Float.valueOf(SG.nteilstring(zeile, "#", pos));
				pos = pos + 1;
			}
			handelshinweis = new String(String.valueOf(SG.nteilstring(zeile, "#", pos)));

		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, UserDbGewinnZeitraumObjI.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String toString()
	{
		String infostring = null;
		infostring = datum;

		for (int i = 0; i < 15; i++)
			infostring = infostring.concat("#" + attribvalue[i]);
		infostring=infostring.concat("#"+handelshinweis);

		return (infostring);
	}
	@Override
	public String GetSaveInfostring()
	{
		return "datum#attribvalue1......#attribvalue15#handelshinweis";
	}
	public int getThreadid()
	{
		return 0;
	}
	public void postprocess()
	{}
}
