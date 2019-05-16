package data;

public class OptimizeResult
{
	//hier ist eine Optimierung
	
	//gesamtprofit dieser optimierung
	private double gewinnsumme=0;
	
	//profitfaktor
	private float profitfaktor=0;
	//drawdown
	private float drawdownproz=0;
	//anzahl allertrades der ursprünglichen Tradeliste
	private int anzGesTrades=0;
	//anz der ausgewählten trades
	private int anzSelectTrades=0;
	//dieser marker selektiert die ausgewählten positionen
	private int[] marker =null;
	//errechneter optimaler gdx
	private int gdx=0;
	//Tradeliste
	//private Tradeliste tradeliste=null;
	//String name(bezeichner) für diese optimierung
	private String resultname=null;
	
	//der verwendete Optimierungsalgorithmus
	private String optalgoname=null;
	
	//berechnet sich aus profitfaktor * anzahl
	private float profitfaktoranzahl=0;
	
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
	
	
	public float getProfitfaktor()
	{
		return profitfaktor;
	}
	public void setProfitfaktor(float profitfaktor)
	{
		this.profitfaktor = profitfaktor;
	}
	public float getDrawdownproz()
	{
		return drawdownproz;
	}
	public void setDrawdownproz(float drawdownproz)
	{
		this.drawdownproz = drawdownproz;
	}
	public String getOptalgoname()
	{
		return optalgoname;
	}
	public void setOptalgoname(String optalgoname)
	{
		this.optalgoname = optalgoname;
	}
	
	public float getProfitfaktoranzahl()
	{
		return profitfaktoranzahl;
	}
	public void setProfitfaktoranzahl(float profitfaktoranzahl)
	{
		this.profitfaktoranzahl = profitfaktoranzahl;
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
	public int[] getMarker()
	{
		return marker;
	}
	public void setMarker(int[] marker)
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
