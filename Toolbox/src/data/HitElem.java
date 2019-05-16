package data;

public class HitElem
{
	//Ein Hit Element speichert für einen bestimmten Index die Treffer
	private int[] hitcount = null;

	public HitElem()
	{
		hitcount=new int[20];
	}
	
	public void inc(int index,int anzhits)
	{
		int count=hitcount[index];
		count=count+anzhits;
		hitcount[index]=count;
	}
	public void setcounter(int index, int anzhits)
	{
		hitcount[index]=anzhits;
	}
	public int gethitsum()
	{
		int hisum=0;
		for (int i=0; i<20; i++)
			hisum=hisum+hitcount[i];
		return hisum;
	}
	public String gethistring()
	{
		//Aus einem hitstring geht hervor wieviele Hits eine Strategie auf einer
		//datenreihe beinhaltet, es wird ausserdem die Hitsumme angegeben
		int hisum=0;
		String hitstr="";
		for (int i=0; i<20; i++)
		{
			hisum=hisum+hitcount[i];
			hitstr=hitstr+" "+hitcount[i];
		}
		hitstr=hitstr+" Sum="+hisum;
		return hitstr;
	}
}
