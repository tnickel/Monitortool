package mailer;

import java.util.ArrayList;

public class Found
{
	private ArrayList<Foundelem> l = new ArrayList<Foundelem>();
	private String aufbau=null;
	private String mindat=null;

	//Dies ist die foundliste zum halten der gefundenen Werte
	public Found(String ab)
	{
		aufbau=new String(ab);
	}


	public String getAufbau()
	{
		return aufbau;
	}


	public void setAufbau(String aufbau)
	{
		this.aufbau = aufbau;
	}

	public int getAnz()
	{
		return l.size();
	}
	
	public void add(String mdat,String schluesselwort,String verz,String pdfnam)
	{
		//ein neues Ereigniss wurde gefunden
		Foundelem fe= new Foundelem();
		mindat=new String(mdat);
		fe.setSuchwoerter(schluesselwort);
		fe.setBoerblattname(verz);
		fe.setFilename(pdfnam);
		String fd=pdfnam.substring(0,pdfnam.indexOf("_"));
		String y=fd.substring(2,4);
		String m=fd.substring(fd.indexOf("-")+1,fd.lastIndexOf("-"));
		String d=fd.substring(fd.lastIndexOf("-")+1);
		String fdat=d+"."+m+"."+y;
		fe.setDatum(fdat);
		l.add(fe);
	}

	public Foundelem getElem(int pos)
	{
		return l.get(pos);
	}
	
}
