package hilfsobjekt;

public class Fensterinfo
{
	 private String suchstartdatum="";
	 private String iststartdatum="";
	 private String istenddatum="";
	 private int postcounter=0;
	 

	public int getPostcounter()
	{
		return postcounter;
	}
	public void setPostcounter(int postcounter)
	{
		this.postcounter = postcounter;
	}
	public String getIstStartdatum()
	{
		return iststartdatum;
	}
	public void setIstStartdatum(String startdatum)
	{
		this.iststartdatum = startdatum;
	}
	public String getIstEnddatum()
	{
		return istenddatum;
	}
	public void setIstEnddatum(String enddatum)
	{
		this.istenddatum = enddatum;
	}
	public String getSuchstartdatum()
	{
		return suchstartdatum;
	}
	public void setSuchstartdatum(String suchstartdatum)
	{
		this.suchstartdatum = suchstartdatum;
	}

	

	
}
