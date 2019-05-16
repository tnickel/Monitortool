package slider;

public class Sliderbewert
{
	//dies ist nur ein hilfsobjekt zur Sliderbewertung
	private int useranz=0;
	private int postanz=0;
	private int guteU=0;
	private int anzbaduser=0;
	private int schlechteU=0;
	private int anzgutePostings=0;
	private int anzschlechtePostings=0;
	private float mitlrank=0;
	private String handelshinweis=null;

	//-hier werden die anz der neuen User im Slider gezaehlt
	//-Ein User wird als neu angesehen wenn er die letzten 90Tage nicht im Thread gepostet hat
	private int neueGuteUserImSlider=0;
	private int neueSchlechteUserImSlider=0;
	private int neueBaduserImSlider=0;
	
	public Sliderbewert()
	{}
	public int getUseranz()
	{
		return useranz;
	}
	public void setUseranz(int useranz)
	{
		this.useranz = useranz;
	}
	public int getPostanz()
	{
		return postanz;
	}
	public void setPostanz(int postanz)
	{
		this.postanz = postanz;
	}
	public int getGuteU()
	{
		return guteU;
	}
	public void setGuteU(int guteU)
	{
		this.guteU = guteU;
	}
	public int getAnzbaduser()
	{
		return anzbaduser;
	}
	public void setAnzbaduser(int anzbaduser)
	{
		this.anzbaduser = anzbaduser;
	}
	public int getSchlechteU()
	{
		return schlechteU;
	}
	public void setSchlechteU(int schlechteU)
	{
		this.schlechteU = schlechteU;
	}
	public int getAnzgutePostings()
	{
		return anzgutePostings;
	}
	public void setAnzgutePostings(int anzgutePostings)
	{
		this.anzgutePostings = anzgutePostings;
	}
	public int getAnzschlechtePostings()
	{
		return anzschlechtePostings;
	}
	public void setAnzschlechtePostings(int anzschlechtePostings)
	{
		this.anzschlechtePostings = anzschlechtePostings;
	}
	public float getMitlrank()
	{
		return mitlrank;
	}
	public void setMitlrank(float mitlrank)
	{
		this.mitlrank = mitlrank;
	}
	public int getNeueGuteUserImSlider()
	{
		return neueGuteUserImSlider;
	}
	public void setNeueGuteUserImSlider(int neueGuteUserImSlider)
	{
		this.neueGuteUserImSlider = neueGuteUserImSlider;
	}
	public int getNeueSchlechteUserImSlider()
	{
		return neueSchlechteUserImSlider;
	}
	public void setNeueSchlechteUserImSlider(int neueSchlechteUserImSlider)
	{
		this.neueSchlechteUserImSlider = neueSchlechteUserImSlider;
	}
	public int getNeueBaduserImSlider()
	{
		return neueBaduserImSlider;
	}
	public void setNeueBaduserImSlider(int neueBaduserImSlider)
	{
		this.neueBaduserImSlider = neueBaduserImSlider;
	}
	public String getHandelshinweis()
	{
		return handelshinweis;
	}
	public void setHandelshinweis(String handelshinweis)
	{
		this.handelshinweis = handelshinweis;
	}
	public String getSliderInfostring()
	{
	  //"Useranz#mittlRang#gUser#sUser#badUser#-Postings#+Postings#neuGutUser#neuSchlUser#neuBadUser#Bemerkung");
		return("useranz<"+useranz+"> mitlRang<"+mitlrank+"> gUser<"+guteU+"> sUser<"+schlechteU+"> badUser<"+anzbaduser+"> -post<"+anzgutePostings+"> +post<"+anzschlechtePostings+"> ngU<"+neueGuteUserImSlider+"> nsU<"+neueSchlechteUserImSlider+"> nbU<"+neueBaduserImSlider+"> Bem<"+handelshinweis+">");
	}
}
