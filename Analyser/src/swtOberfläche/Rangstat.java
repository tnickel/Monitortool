package swtOberfläche;

public class Rangstat
{
	//rangstatistik
	private int startpos=0;
	private int endpos=0;
	private int stepweite=1;
	
	//gesammtzahl der gesehenen Threads
	private int gesSeenThreads=0;
	//gesammtzahl der gesehenen User
	private int gesSeenUser=0;
	//gesammtzahl der gesehenen links über alle postings
	private int gesSeenLinks=0;
	//gesammtzahl der gesehenen postings
	private int gesSeenPostings=0;
	//gesammtzahl der gesehenen Icons (Jedes icon wird gezählt)
	private int gesSeenIcons=0;
	
	

	public int getStartpos()
	{
		return startpos;
	}
	public void setStartpos(int startpos)
	{
		this.startpos = startpos;
	}
	public int getEndpos()
	{
		return endpos;
	}
	public void setEndpos(int endpos)
	{
		this.endpos = endpos;
	}
	public int getGesSeenThreads()
	{
		return gesSeenThreads;
	}
	public void setGesSeenThreads(int gesSeenThreads)
	{
		this.gesSeenThreads = gesSeenThreads;
	}
	public int getGesSeenUser()
	{
		return gesSeenUser;
	}
	public void setGesSeenUser(int gesSeenUser)
	{
		this.gesSeenUser = gesSeenUser;
	}
	public int getGesSeenLinks()
	{
		return gesSeenLinks;
	}
	public void setGesSeenLinks(int gesSeenLinks)
	{
		this.gesSeenLinks = gesSeenLinks;
	}
	public int getGesSeenPostings()
	{
		return gesSeenPostings;
	}
	public void setGesSeenPostings(int gesSeenPostings)
	{
		this.gesSeenPostings = gesSeenPostings;
	}
	public int getGesSeenIcons()
	{
		return gesSeenIcons;
	}
	public void setGesSeenIcons(int gesSeenIcons)
	{
		this.gesSeenIcons = gesSeenIcons;
	}
	public int getStepweite()
	{
		return stepweite;
	}
	public void setStepweite(int stepweite)
	{
		this.stepweite = stepweite;
	}
	
	
	
	
}
