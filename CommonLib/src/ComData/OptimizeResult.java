package ComData;

public class OptimizeResult
{
	//hier ist eine Optimierung
	
	//gesamtprofit dieser optimierung
	private double gewinnsumme=0;
	//anzahl allertrades der urspr�nglichen Tradeliste
	private int anzGesTrades=0;
	//anz der ausgew�hlten trades
	private int anzSelectTrades=0;
	//dieser marker selektiert die ausgew�hlten positionen
	private boolean[] marker =null;
	//errechneter optimaler gdx
	private int gdx=0;
	//Tradeliste
	//private Tradeliste tradeliste=null;
	//String name(bezeichner) f�r diese optimierung
	private String resultname=null;
	
	
	
	public String getResultname()
	{
		return resultname;
	}
	public void setResultname(String resultname)
	{
		this.resultname = resultname;
	}
	public double getGewinnsumme()
	{
		return gewinnsumme;
	}
	public void setGewinnsumme(double gewinnsumme)
	{
		this.gewinnsumme = gewinnsumme;
	}
	public int getAnzGesTrades()
	{
		return anzGesTrades;
	}
	public void setAnzGesTrades(int anzGesTrades)
	{
		this.anzGesTrades = anzGesTrades;
	}
	public int getAnzSelectTrades()
	{
		return anzSelectTrades;
	}
	public void setAnzSelectTrades(int anzSelectTrades)
	{
		this.anzSelectTrades = anzSelectTrades;
	}
	public boolean[] getMarker()
	{
		return marker;
	}
	public void setMarker(boolean[] marker)
	{
		this.marker = marker;
	}
	public int getgdx()
	{
		return gdx;
	}
	public void setgdx(int optgdx)
	{
		this.gdx = optgdx;
	}
	
	
}
