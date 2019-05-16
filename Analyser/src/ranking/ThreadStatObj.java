package ranking;

import objects.ThreadDbObj;

public class ThreadStatObj implements Comparable<ThreadStatObj>
{
	// das zugehörige tdbo wird hier verlinkt
	private ThreadDbObj tdbo = null;
	// mittlere erfahrung in Tagen
	private float erf = 0;
	// anzahl user in dem Thread
	private int anzuser = 0;
	// die user haben insgesammt #postings abgegeben
	private int anzpostings = 0;
	// von diesen abgegeben Postings sind nur ein Teil valid. Die User die
	// gespeert sind werden nämlich nicht gezählt
	private int anzvalidpostings = 0;
	// anzahl der Tage wie lange der Thread schon besteht
	private int ttage = 0;

	public int getTtage()
	{
		return ttage;
	}

	public void setTtage(int ttage)
	{
		this.ttage = ttage;
	}

	public ThreadDbObj getTdbo()
	{
		return tdbo;
	}

	public void setTdbo(ThreadDbObj tdbo)
	{
		this.tdbo = tdbo;
	}

	public float getErf()
	{
		return erf;
	}

	public void setErf(float erf)
	{
		this.erf = erf;
	}

	public int getAnzuser()
	{
		return anzuser;
	}

	public void setAnzuser(int anzuser)
	{
		this.anzuser = anzuser;
	}

	public int getAnzpostings()
	{
		return anzpostings;
	}

	public void setAnzpostings(int anzpostings)
	{
		this.anzpostings = anzpostings;
	}

	public int getAnzvalidpostings()
	{
		return anzvalidpostings;
	}

	public void setAnzvalidpostings(int anzvalidpostings)
	{
		this.anzvalidpostings = anzvalidpostings;
	}

	public String getInfoString()
	{
		String outstr = tdbo.getThreadname() + ":ttage<" + ttage
				+ "> postings<" + anzpostings + "> erf/Jahre<" + erf / 365
				+ ">";
		return outstr;
	}

	public String getSaveString()
	{
		String outstr = tdbo.getThreadname() + ":ttage<" + ttage
				+ "> postings<" + anzpostings + "> erf/Jahre<" + erf / 365
				+ ">";
		return outstr;
	}

	public int compareTo(ThreadStatObj tstat)
	{

		if (erf < tstat.getErf())
			return 1;
		else
			return 0;
	}
}
