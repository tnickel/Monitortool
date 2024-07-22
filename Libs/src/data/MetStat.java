package data;

import java.text.DecimalFormat;

public class MetStat
{
	private double possum=0;
	private double negsum=0;
	private int anzpos=0;
	private int anzneg=0;
	
	public MetStat()
	{}
	public void addValue(double val)
	{
		if(val>0)
		{
			possum=possum+val;
			anzpos++;
		}
	
		if(val<0)
		{
			negsum=negsum+val;
			anzneg++;
		}
		
		
	}
	public String getResultstring1()
	{
		DecimalFormat df = new DecimalFormat("####.####");
		double avg_pos=(possum/anzpos);
		double avg_neg=(negsum/anzneg);
		
		String output="avg_cor+="+df.format(avg_pos)+"# avg_cor-="+df.format(avg_neg);
		return output;
	}
	public String getResultstring2()
	{
		String output="anz="+anzpos+"# anz="+anzneg;
		return output;
		
	}
}
