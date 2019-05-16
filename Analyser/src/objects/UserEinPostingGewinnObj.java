package objects;

import java.util.HashMap;

public class UserEinPostingGewinnObj
{
	// dieses Objekt speichert einen Gewinn/Verlust für ein Posting

	private int tid = 0;
	private int postid = 0;
	// val 1,2 sind die kurswerte zum Zeitpunkt 1 und 2
	private float val1 = 0, val2 = 0;
	private float gewinn = 0;
	private String datum = null;
	private int mid=0;
	private String symb=null;
	private String kursinfo=null;
	static int objektzaehler = 0;
	// die hashmap dient dazu einer tid einen Namen "tname" zuzuordnen
	// (Speicherplatz spaaren)

	static HashMap<Integer, String> hmap = new HashMap<Integer, String>();

	static void storeThreadname(String tname, int tid)
	{
		hmap.put(tid, new String(tname));
	}

	static public String getThreadname(int tid)
	{
		return (hmap.get(tid));
	}

	static public void cleanObjektzaehler()
	{
		objektzaehler = 0;
	}

	public UserEinPostingGewinnObj(String dat, String tname, int t, int pid,
			float gew, float v1, float v2,int m,String symbol,String kinfo)
	{
		storeThreadname(tname, t);
		datum = new String(dat);
		tid = t;
		postid = pid;
		gewinn = gew;
		val1 = v1;
		val2 = v2;
		mid=m;
		symb= new String(symbol);
		kursinfo=new String(kinfo);
		objektzaehler++;
		if (objektzaehler % 1000 == 0)
			System.out.println("usergewobjanz=<" + objektzaehler + ">");
	}

	public int getTid()
	{
		return tid;
	}

	public void setTid(int tid)
	{
		this.tid = tid;
	}

	public int getPostid()
	{
		return postid;
	}

	public void setPostid(int postid)
	{
		this.postid = postid;
	}

	public float getGewinn()
	{
		return gewinn;
	}

	public void setGewinn(float gewinn)
	{
		this.gewinn = gewinn;
	}

	public String getInfostring()
	{
		String infostring = getThreadname(tid) + ":" + tid + ":" + postid + ":"
				+ gewinn;
		return infostring;
	}

	public String getDatum()
	{
		return datum;
	}

	public void setDatum(String datum)
	{
		this.datum = datum;
	}

	public float getVal1()
	{
		return val1;
	}

	public void setVal1(float val1)
	{
		this.val1 = val1;
	}

	public float getVal2()
	{
		return val2;
	}

	public void setVal2(float val2)
	{
		this.val2 = val2;
	}

	public int getMid()
	{
		return mid;
	}

	public void setMid(int mid)
	{
		this.mid = mid;
	}

	public String getSymb()
	{
		return symb;
	}

	public void setSymb(String symb)
	{
		this.symb = symb;
	}

	public String getKursinfo()
	{
		if(kursinfo==null)
			return new String("?");
		else
			return kursinfo;
	}

	public void setKursinfo(String kursinfo)
	{
		this.kursinfo = kursinfo;
	}

}
