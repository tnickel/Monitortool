package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

public class UsernameErsetzungsObj extends Obj implements DBObject
{
	private String dat_g = null, uname_g = null, ename_g = null,
			dummy_g = null;

	public UsernameErsetzungsObj(String zeile)
	{
		if (SG.countZeichen(zeile, "#") != 3)
		{
			Tracer.WriteTrace(10, ":ERROR:symbolliste fehlerhaft zeile=<"
					+ zeile + ">");
			return;
		}
		int pos = 1;
		try
		{
			dat_g = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			uname_g = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			ename_g = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			dummy_g = new String(SG.nteilstring(zeile, "#", pos));

		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, ":ERROR:nteilstring exception<" + zeile
					+ "> pos<" + pos + " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String retstr = new String("");
		retstr = retstr.concat(dat_g + "#" + uname_g + "#" + ename_g + "#"
				+ dummy_g);
		return new String(retstr);
	}

	@Override
	public String GetSaveInfostring()
	{
		return "dat#uname#ersetzungsname#dummy";
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public int getThreadid()
	{
		return 0;
	}

	public String getDat_g()
	{
		return dat_g;
	}

	public void setDat_g(String dat_g)
	{
		this.dat_g = dat_g;
	}

	public String getUname_g()
	{
		return uname_g;
	}

	public void setUname_g(String uname_g)
	{
		this.uname_g = uname_g;
	}

	public String getEname_g()
	{
		return ename_g;
	}

	public void setEname_g(String ename_g)
	{
		this.ename_g = ename_g;
	}

	public String getDummy_g()
	{
		return dummy_g;
	}

	public void setDummy_g(String dummy_g)
	{
		this.dummy_g = dummy_g;
	}
	public void postprocess()
	{}
}
