package bank;

public class Kauf
{
	private int id = 0;
	private int validflag = 0;
	private String symbol = null;
	private int tid = 0;
	private int postid = 0;
	private float kaufpreis = 0;
	private float anzahl = 0;
	private String kaufdatum = null;
	private String uname = null;
	private String aktname = null;

	Kauf(int idx, String symb, int threadid, int pid, float kp, float anz,
			String kaufdat, String algon, String aktn, int validf)
	{
		id = idx;
		symbol = new String(symb);
		tid = threadid;
		postid = pid;
		kaufpreis = kp;
		anzahl = anz;
		kaufdatum = new String(kaufdat);
		uname = new String(algon);
		aktname = new String(aktn);
		validflag = validf;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public int getTid()
	{
		return tid;
	}

	public void setTid(int tid)
	{
		this.tid = tid;
	}

	public int getPostid()
	{
		return postid;
	}

	public void setPostid(int postid)
	{
		this.postid = postid;
	}

	public float getKaufpreis()
	{
		return kaufpreis;
	}

	public void setKaufpreis(float kaufpreis)
	{
		this.kaufpreis = kaufpreis;
	}

	public float getAnzahl()
	{
		return anzahl;
	}

	public void setAnzahl(float anzahl)
	{
		this.anzahl = anzahl;
	}

	public String getKaufdatum()
	{
		return kaufdatum;
	}

	public void setKaufdatum(String kaufdatum)
	{
		this.kaufdatum = kaufdatum;
	}

	public String getUname()
	{
		return uname;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}

	public String getAktname()
	{
		return aktname;
	}

	public void setAktname(String aktname)
	{
		this.aktname = aktname;
	}

	public int getValidflag()
	{
		return validflag;
	}

	public void setValidflag(int validflag)
	{
		this.validflag = validflag;
	}

}
