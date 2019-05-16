package db;

import hilfsklasse.IsValid;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import objects.BadObjectException;
import objects.Obj;
import objects.ThreadDbObj;

public class CompressedPostingObj extends Obj
{
	private String username = null;
	private int threadid = 0;
	private String datetime = null;
	private int postid = 0;
	private float kursvalue = 0;
	private String kennung_threadid_g=null;

	public CompressedPostingObj()
	{
	}

	public CompressedPostingObj(String zeile, String kennung_threadid)
			throws BadObjectException
	{

		if(zeile==null)
			Tracer.WriteTrace(20, "Error: compressed threads- DB Error Nullzeile<"+zeile+"> kennung<"+kennung_threadid+">");
		
		kennung_threadid_g=new String(kennung_threadid);
		
		if (SG.countZeichen(zeile, "#") != 4)
		{
			String emsg = ":ERROR:Zeilen für Objekt fehlerhaft zeile a=<" + zeile
					+ "> kennung<" + kennung_threadid + ">";
			System.out.println(emsg);
			Tracer.WriteTrace(20, emsg);
			throw new BadObjectException(emsg);
		}
		int pos = 1;
		try
		{
			datetime = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			username = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			threadid = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			postid = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			kursvalue = Float.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;

		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, ThreadDbObj.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}
	@Override
	public String toString()
	{
		return (datetime + "#" + username + "#" + threadid + "#" + postid + "#" + kursvalue);
	}

	@Override
	public String GetSaveInfostring()
	{
		return "username#threadid#datetime#postid#kursvalue";
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		if(IsValid.isValidUsername(username)==true)
		this.username = username;
		else
			this.username="dummy";
	}

	public int getThreadid()
	{
		return threadid;
	}

	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}

	public String getDatetime()
	{
		return datetime;
	}

	public void setDatetime(String datetime)
	{
		this.datetime = datetime;
	}

	public int getPostid()
	{
		return postid;
	}

	public void setPostid(int postid)
	{
		this.postid = postid;
	}

	public float getKursvalue()
	{
		return kursvalue;
	}

	public void setKursvalue(float kursvalue)
	{
		this.kursvalue = kursvalue;
	}

	public String getKennung_threadid_g()
	{
		return kennung_threadid_g;
	}

	public void setKennung_threadid_g(String kennung_threadid_g)
	{
		this.kennung_threadid_g = kennung_threadid_g;
	}
	

}
