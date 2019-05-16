package objects;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.AttribFileObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class UserThreadPostingObj implements AttribFileObject
{
	// dieses UserInfPostingObj beinhaltet eine Zeile aus postingliste\<name>.db
	// dieses objekt beinhaltet einen Thread wo der user gepostet hat

	private int threadid = 0;
	private String threadname = null;
	private String aufnahmedatum = null;
	private String lastposting = "0"; // 01.01.2008 17:30
	private static String trenner = null;
	private String username_glob=null;
	
	public UserThreadPostingObj(String unam)
	{
		username_glob =unam;
		// System.out.println("constructor UserPostingObj");
	}

		
	
	public String getUsername_glob()
	{
		return username_glob;
	}

	public String GetSaveInfostring()
	{
		return "noinfo";
	}

	public void setUserInfPostingObj(int tid, String tname, String da,
			String lastp)
	{
		threadid = tid;
		threadname = new String(tname);
		aufnahmedatum = new String(da);
		lastposting = new String(lastp);
	}

	public int getThreadid()
	{
		return threadid;
	}

	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}

	public String getThreadname()
	{
		return threadname;
	}

	public void setThreadname(String threadname)
	{
		this.threadname = new String(threadname);
	}

	public String getAufnahmeDatum()
	{
		return aufnahmedatum;
	}

	public void setAufnahmeDatum(String date)
	{
		this.aufnahmedatum = new String(date);
	}

	public String getLastposting()
	{
		return lastposting;
	}

	public void setLastposting(String lastposting)
	{
		this.lastposting = new String(lastposting);
	}

	public String Kennung()
	{
		return ("*****userdata");
	}

	public boolean Read(BufferedReader inf, String zeile)
	{
		int anz = 0;
		
		
		//Tracer.WriteTrace(20, "Zeile05=<"+zeile+">");
		
		if(zeile==null)
			Tracer.WriteTrace(10, "Error: internal zeile ==null unam<"+username_glob+">");
		
		if (zeile.length() == 0)
		{
			Tracer.WriteTrace(20, "Warning: zeilenlänge = 0 unam<"+username_glob+">");
			return false;
		}
		
		anz = Tools.countZeichen(zeile, "#");
		
		if (trenner == null)
		{
			if (anz >= 2)
			{
				trenner = "#";
			} else
			{
				Tracer.WriteTrace(10, "Trenner : nicht erlaubt ->stop unam<"+username_glob+">");
				trenner = ":";
			}
		}
		try
		{
			if(anz!=3)
			{
				Tracer.WriteTrace(20, "Error: internal anz muss 4 sein !!! unam<"+username_glob+"> zeile<"+zeile+">");
				return false;
			}
			threadid = Integer.valueOf(new String(SG.nteilstring(zeile,
					trenner, 1)));
			threadname = new String(SG.nteilstring(zeile, trenner, 2));
			aufnahmedatum = new String(SG.nteilstring(zeile, trenner, 3));
			lastposting = new String(SG.nteilstring(zeile, trenner, 4));
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean Write(BufferedWriter ouf)
	{
		try
		{
			ouf.write(threadid + "#" + threadname + "#" + aufnahmedatum + "#"
					+ lastposting);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
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
		final UserThreadPostingObj other = (UserThreadPostingObj) obj;
		if (threadid != other.threadid)
			return false;
		return true;
	}
}
