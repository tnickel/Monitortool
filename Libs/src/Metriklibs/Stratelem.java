package Metriklibs;

public class Stratelem
{
	//beinhaltet einen strategienamen und sagt einem ob diese strategie aktiv is.
	private String stratname=null;
	private int activeflag=0;

	public String getStratname()
	{
		return stratname;
	}
	public void setStratname(String stratname)
	{
		this.stratname = stratname;
	}
	public int getActiveflag()
	{
		return activeflag;
	}
	public void setActiveflag(int activeflag)
	{
		this.activeflag = activeflag;
	}
	
}
