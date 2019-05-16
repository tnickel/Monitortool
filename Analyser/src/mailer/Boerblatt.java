package mailer;


public class Boerblatt
{
	private String fnam;
	private String foundpatterns = "";	
	public String getFnam()
	{
		return fnam;
	}

	public void setFnam(String fnam)
	{
		this.fnam = fnam;
	}

	public void addPattern(String pattern)
	{
		foundpatterns=foundpatterns.concat("@"+pattern);
	}
	public void setPattern(String pattern)
	{
		foundpatterns=new String(pattern);
	}
	
	public String getPatternstring()
	{
		
		return foundpatterns;
	}
}
