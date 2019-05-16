package Metriklibs;

public class Metrikentry
{
	// 0= nicht aktiv, 1=string, 2=float
	int attributflag = 0;
	String attributname = null;
	String value = null;

	public String getAttributName()
	{
		return attributname;
	}

	public void setAttributName(String attributname)
	{
		this.attributname = attributname;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String strvalue)
	{
		this.value = new String(strvalue);
		try
		{
			float a = Float.valueOf(value);
			attributflag=2; //zahl ist float
		} catch (NumberFormatException e)
		{
			if(value.length()>0)
				attributflag=1; //zahl ist string
		}

	}

	public int getAttributflag()
	{
		return attributflag;
	}

	public void setAttributflag(int attributflag)
	{
		this.attributflag = attributflag;
	}

}
