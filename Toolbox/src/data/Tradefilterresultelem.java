package data;

public class Tradefilterresultelem  implements Comparable<Tradefilterresultelem>
{
	String stratnam = null;
	int tradeanz = 0;
	int maxhits = 0;
	int hitpoints = 0;
	String hitstring = null;
	String proz = null;

	public String getStratnam()
	{
		return stratnam;
	}

	public void setStratnam(String stratnam)
	{
		this.stratnam = stratnam;
	}

	public int getTradeanz()
	{
		return tradeanz;
	}

	public void setTradeanz(int tradeanz)
	{
		this.tradeanz = tradeanz;
	}

	public int getMaxhits()
	{
		return maxhits;
	}

	public void setMaxhits(int maxhits)
	{
		this.maxhits = maxhits;
	}

	public int getHitpoints()
	{
		return hitpoints;
	}

	public void setHitpoints(int hitpoints)
	{
		this.hitpoints = hitpoints;
	}

	public String getHitstring()
	{
		return hitstring;
	}

	public void setHitstring(String hitstring)
	{
		this.hitstring = hitstring;
	}

	public String getProz()
	{
		return proz;
	}

	public void setProz(String proz)
	{
		this.proz = proz;
	}

	public String getOutstring()
	{
		String outstring = stratnam + "#" + tradeanz + "#" + maxhits + "#"
				+ hitpoints + "#" + proz + "#" + hitstring;
		return outstring;
	}

	public int compareTo(Tradefilterresultelem te)
	{
		if (te.getHitpoints() > this.getHitpoints())

			return 1;
		else
			return -1;
	}
}
