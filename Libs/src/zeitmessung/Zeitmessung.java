package zeitmessung;

public class Zeitmessung
{
	private long startzeit=0;
	private long lasttime=0;
	private long akttime=0;
	private int onflag_glob=0;
	
	public Zeitmessung(int onflag)
	{
		onflag_glob=onflag;
		startzeit=System.currentTimeMillis();
		lasttime=startzeit;
	}
	public void showZeitdiff(String msg)
	{
		if(onflag_glob==0)
			return;
		akttime=System.currentTimeMillis();
		System.out.println("<"+msg+"> Laufzeit <"+(akttime-lasttime)+">ms");
		lasttime=akttime;
	}
}
