package kurse;

public class Kursvalue
{
	float kv = 0; // kursvalue

	String date = null; // der kurs ist nur gültig wenn das Flag true ist
	boolean validflag = false;
	private float volumen=0;
	private String kursinfo=null;
	
	public Kursvalue()
	{
	}

	public boolean isValidflag()
	{
		return validflag;
	}

	public void setValidflag(boolean validflag)
	{
		this.validflag = validflag;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public float getKv()
	{
		return kv;
	}

	public void setKv(float kv)
	{
		this.kv = kv;
	}

	public float getVolumen()
	{
		return volumen;
	}

	public void setVolumen(float volumen)
	{
		this.volumen = volumen;
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
