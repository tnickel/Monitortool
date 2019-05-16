package objects;

public class Gewinn
{
	// gesammtgewinn
	String datum1 = null;
	String datum2 = null;
	float Gewinn;
	// erstkurs
	float kurs1;
	// kurs nach zeitraum x
	float kurs2;
	//kursinfo beinhaltet weitere infos woher der kurs kommt
	String kursinfo=null;

	public Gewinn(float gew, float k1, float k2, String dat1, String dat2,String kursinf)
	{
		Gewinn = gew;
		kurs1 = k1;
		kurs2 = k2;
		datum1 = dat1;
		datum2 = dat2;
		//kursinfo beinhaltet weitere Infos woher der kurs stammt
		if(kursinf!=null)
			kursinfo = new String(kursinf);
		else
			kursinfo = new String("");
	}

	public float getGewinn()
	{
		return Gewinn;
	}

	public void setGewinn(float gewinn)
	{
		Gewinn = gewinn;
	}

	public float getKurs1()
	{
		return kurs1;
	}

	public void setKurs1(float kurs1)
	{
		this.kurs1 = kurs1;
	}

	public float getKurs2()
	{
		return kurs2;
	}

	public void setKurs2(float kurs2)
	{
		this.kurs2 = kurs2;
	}

	public String getDatum1()
	{
		return datum1;
	}

	public void setDatum1(String datum1)
	{
		this.datum1 = datum1;
	}

	public String getDatum2()
	{
		return datum2;
	}

	public void setDatum2(String datum2)
	{
		this.datum2 = datum2;
	}

	public String getKursinfo()
	{
		return kursinfo;
	}

	public void setKursinfo(String kursinfo)
	{
		this.kursinfo = kursinfo;
	}

}
