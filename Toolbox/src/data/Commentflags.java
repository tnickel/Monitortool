package data;

public class Commentflags
{
	private String prefix=null;
	private int buttonstabilitaet=0;
	private int buttonprofit=0;
	
	private boolean stabilitaet=false;
	private boolean profit=false;
	private float netprofitminval=0f;
	private float stabilminval=0f;
	
	
	public String getPrefix()
	{
		return prefix;
	}
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	public boolean isStabilitaet()
	{
		return stabilitaet;
	}
	public void setStabilitaet(boolean stabilitaet)
	{
		this.stabilitaet = stabilitaet;
	}
	public boolean isProfit()
	{
		return profit;
	}
	public void setProfit(boolean profit)
	{
		this.profit = profit;
	}
	public int getButtonstabilitaet()
	{
		return buttonstabilitaet;
	}
	public void setButtonstabilitaet(int buttonstabilitaet)
	{
		this.buttonstabilitaet = buttonstabilitaet;
	}
	public int getButtonprofit()
	{
		return buttonprofit;
	}
	public void setButtonprofit(int buttonprofit)
	{
		this.buttonprofit = buttonprofit;
	}
	public float getNetprofitminval()
	{
		return netprofitminval;
	}
	public void setNetprofitminval(float netprofitminval)
	{
		this.netprofitminval = netprofitminval;
	}
	public float getStabilminval()
	{
		return stabilminval;
	}
	public void setStabilminval(float stabilminval)
	{
		this.stabilminval = stabilminval;
	}
	
}
