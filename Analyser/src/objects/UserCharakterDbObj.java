package objects;

import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import db.DbException;

public class UserCharakterDbObj extends Obj implements DBObject
{
	// Dieses Objekt enthält die Charktereigenschaften des User
	// Wie hat er seine Gewinne gemacht?, wie oft ist er online ?
	// Wie gut ist seine Postingsqualität etc..
	// 
	private String username=null;
	private int userid=0;
	//# verschiedene Aktien
	//gibt an aus wievielen Aktien (mids) der User seine Gewinne gemacht hat
	private int anzPostAktien=0;
	//# trefferquote
	//gibt an wieviele seiner Postings gewinne gemacht haben (Angabe in %)
	private float trefferquoute=0;
	//gibt an wieviel im Monat im Mittel gewonnen wurde
	private float mtlGewinnMonat=0;
	//gibt die Gewinnabweichung an, gewinnabweichung vom mittleren Monat
	private float gewinnabweichung=0;

	//Für 5 verschiedenen Algorithmen 
	//rangpos
	private int[] rangpos = new int[5];
	//rangpoints
	private int[] rangpoints = new int[5];
	
	public UserCharakterDbObj(String zeile) throws DbException
	{

		int n = Tools.countZeichen(zeile, "#");
		if ((n < 15) || (n > 15))
		{
			System.out.println(UserDbObj.class.getName()
					+ ":ERROR:username fehlerhaft in usercharakter.db zeile=<"
					+ zeile + "> ");
			Tracer.WriteTrace(10, UserDbObj.class.getName()
					+ ":ERROR:username fehlerhaft in usercharakter.db zeile=<"
					+ zeile + ">");
			throw new DbException("Fehler im usercharakter" + zeile);
		}
		try
		{
			int pos=1;
			username = new String(Tools.nteilstring(zeile, "#", pos));
			pos++;
			userid= Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos++;
			anzPostAktien=Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos++;
			trefferquoute=Float.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos++;
			mtlGewinnMonat=Float.valueOf(Tools.nteilstring(zeile, "#", pos));
			pos++;
			gewinnabweichung=Float.valueOf(Tools.nteilstring(zeile, "#", pos));
		
			for(int i=0; i<5; i++)
			{   pos++;
				rangpos[i]=Integer.valueOf(Tools.nteilstring(zeile, "#", pos));
				pos++;
				rangpoints[i]=Integer.valueOf(Tools.nteilstring(zeile, "#", pos));        
			}
				
			
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: Number formatfehler usercharakter.db <"+zeile+">");
		} catch (ToolsException e)
		{
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: exception usercharakter.db <"+zeile+">");
		} 
	}
	public int getThreadid()
	{
		return 0;
	}
	public int compareTo(Obj argument)
	{
		
		return 0;
	}
	public String toString()
	{
		String str=new String(username+"#"+userid+"#"+anzPostAktien+"#"+trefferquoute+"#"+mtlGewinnMonat+"#"+gewinnabweichung);
			
		for(int i=0; i<5; i++)
			str=str.concat("#"+rangpos[i]+"#"+rangpoints[i]);
		return ( str );
	}

	public String GetSaveInfostring()
	{
		return "username#userid#anzPostAktien#Trefferquote#mntlGewinn#Gewinnabweichung#rangpos1#rangpoints1#rangpos2#rangpoints2#rangpos3#rangpoints3#rangpos4#rangpoints4#rangpos5#rangpoints5";
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public int getUserid()
	{
		return userid;
	}
	public void setUserid(int userid)
	{
		this.userid = userid;
	}
	public int getAnzPostAktien()
	{
		return anzPostAktien;
	}
	public void setAnzPostAktien(int anzPostAktien)
	{
		this.anzPostAktien = anzPostAktien;
	}
	public float getTrefferquoute()
	{
		return trefferquoute;
	}
	public void setTrefferquoute(float trefferquoute)
	{
		this.trefferquoute = trefferquoute;
	}
	public float getMtlGewinnMonat()
	{
		return mtlGewinnMonat;
	}
	public void setMtlGewinnMonat(float mtlGewinnMonat)
	{
		this.mtlGewinnMonat = mtlGewinnMonat;
	}
	public float getGewinnabweichung()
	{
		return gewinnabweichung;
	}
	public void setGewinnabweichung(float gewinnabweichung)
	{
		this.gewinnabweichung = gewinnabweichung;
	}
	public int getRangpos(int index)
	{
		return rangpos[index];
	}
	public void setRangpos(int index,int val)
	{
		rangpos[index]=val;
	}
	public int getRangpoints(int index)
	{
		return rangpoints[index];
	}
	public void setRangpoints(int index, int val)
	{
		rangpoints[index] = val;
	}

	public void postprocess()
	{}
}
