package data;

import hiflsklasse.Tracer;


public class ServerTradeliste
{
	String Filename=null;
	private Tradeliste tradeliste_glob = null;
	
	
	
	public ServerTradeliste(String fnam)
	{
		//die Tradeliste einlesen
		Tradeliste trl= new Tradeliste(null);
		Tracer.WriteTrace(20, "I: try to read tradeliste<"+fnam+">");
		trl.importTextTl(fnam);
		Filename=fnam;
		tradeliste_glob=trl;
	}

	public String getFilename()
	{
		return Filename;
	}

	public void setFilename(String filename)
	{
		Filename = filename;
	}
	
	public Tradeliste getTradeliste()
	{
		return tradeliste_glob;
	}
}
