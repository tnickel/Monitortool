package hiflsklasse;

public class Tradestatistik
{
	int anzlong=0;
	int anzshort=0;
	public int getAnzlong()
	{
		return anzlong;
	}
	public void setAnzlong(int anzlong)
	{
		this.anzlong = anzlong;
	}
	public int getAnzshort()
	{
		return anzshort;
	}
	public void setAnzshort(int anzshort)
	{
		this.anzshort = anzshort;
	}
	public void inclong()
	{
		anzlong++;
	}
	public void incshort()
	{
		anzshort++;
	}
}
