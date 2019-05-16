package data;

public class Algodat
{
	//## Algodat ist ein Element von Algoliste
	
	
	//der dateiname
	String path=null;
	//der angezeigte name
	String name=null;
	//sagt ob die liste aktiviert ist
	boolean activated= false;
	
	private static Indidat indidat= null;
	
	public String getPath()
	{
		return path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isActivated()
	{
		return activated;
	}
	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}
	public static Indidat getIndidat()
	{
		return indidat;
	}
	public static void setIndidat(Indidat indidat)
	{
		Algodat.indidat = indidat;
	}
	
}
