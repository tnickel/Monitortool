package objects;

public class Obj38200 implements Comparable<Obj38200>
{
	private String symb = null;
	private String name = null;
	private float k = 0;
	private float k38 = 0;
	private float k200 = 0;
	private String boerse = null;
	private String wkn = null;
	private float volumen=0;
	private float volumen38=0;
	private float volumen200=0;
	private int masterid=0;
	//steigung der letzten 10 Tage
	private float s10=0;
	private float s38=0;
	private float s=0;
	private int threadid=0;
	private int postslideranz=0;

	public String getSymb()
	{
		return symb;
	}

	public void setSymb(String symb)
	{
		this.symb = symb;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public float getK()
	{
		return k;
	}

	public void setK(float k)
	{
		this.k = k;
	}

	public float getK38()
	{
		return k38;
	}

	public void setK38(float k38)
	{
		this.k38 = k38;
	}

	public float getK200()
	{
		return k200;
	}

	public void setK200(float k200)
	{
		this.k200 = k200;
	}

	public String getBoerse()
	{
		return boerse;
	}

	public void setBoerse(String boerse)
	{
		this.boerse = new String(boerse);
	}

	public String getWkn()
	{
		return wkn;
	}

	public void setWkn(String wkn)
	{
		this.wkn = new String(wkn);
	}

	public float getVolumen()
	{
		return volumen;
	}

	public void setVolumen(float volumen)
	{
		this.volumen = volumen;
	}

	public float getVolumen38()
	{
		return volumen38;
	}

	public void setVolumen38(float volumen38)
	{
		this.volumen38 = volumen38;
	}

	public float getVolumen200()
	{
		return volumen200;
	}

	public void setVolumen200(float volumen200)
	{
		this.volumen200 = volumen200;
	}

	public int getMasterid()
	{
		return masterid;
	}

	public void setMasterid(int masterid)
	{
		this.masterid = masterid;
	}

	public float getS10()
	{
		return s10;
	}

	public void setS10(float s10)
	{
		this.s10 = s10;
	}

	public float getS38()
	{
		return s38;
	}

	public void setS38(float s38)
	{
		this.s38 = s38;
	}
	
	public float getS()
	{
		return s;
	}

	public void setS(float s)
	{
		this.s = s;
	}

	public int getThreadid()
	{
		return threadid;
	}

	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}

	public int getPostSlideranz()
	{
		return postslideranz;
	}

	public void setPostSlideranz(int postslideranz)
	{
		this.postslideranz = postslideranz;
	}

	public int calcpoints(Obj38200 obj)
	{
		int points = 0;
		if (obj.k > obj.k38)
			points = 50;

		if ((obj.k > obj.k38)&& (obj.k38 > obj.k200))
			points = 100;

		
			if (obj.getK() < 1)
				points = points - 10;
		return points;
	}

		
	public int compareTo(Obj38200 argument)
	{
		int thispoints = calcpoints(this);
		int points = calcpoints(argument);

		if(thispoints==points)
		{
			//falls die punkte gleich sind wird der Steigungsfaktor verglichen
			if(this.getS()>argument.getS())
				return 0;
			else
				return 1;
		}
		
		if (thispoints > points)
			return 0;
		else
			return 1;

	}
}
