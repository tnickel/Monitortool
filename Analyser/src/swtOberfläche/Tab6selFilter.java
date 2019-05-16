package swtOberfläche;

public class Tab6selFilter
{
	// dieser Selektionsfilter nimmt die einzelne Werte auf
	// die dann im Selektionprozess bei der Userliste angezeigt werden

	private String prefix_glob;
	private String attrib_glob;
	private int alleThreadsFlag_glob;
	
	//der prefix ist eine anfangszeichenkette für das selektieren
	public String getPrefix()
	{
		if(prefix_glob==null)
			return new String("");
		else
			return prefix_glob;
	}

	public void setPrefix(String prefix)
	{
		prefix_glob = new String(prefix);
	}

	//attribute könnten z.b sein (handdata vorhanden, observe user)
	//Die attribute werden für die Ratio-Button auswahl benötigt.
	public String getAttrib()
	{
			if(attrib_glob !=null)
				return new String(attrib_glob);
			else
				return new String("");
	}

	public void setAttrib(String attrib)
	{
		attrib_glob = new String(attrib);
	}
	public void appendAttrib(String attrib)
	{
		if(attrib_glob!=null)
		{
			if(attrib_glob.contains(attrib)==false)
			attrib_glob=attrib_glob.concat("#"+attrib);
		}
		else
		{
			attrib_glob=new String("#"+attrib);
		}
	}
	public void removeAttrib(String attrib)
	{
		if(attrib_glob!=null)
		{
			if(attrib_glob.contains(attrib)==false)
				return;
			if(attrib_glob.contains(attrib)==true)
			{
				attrib_glob=attrib_glob.replace(attrib, "");
				return;
			}
		}
		else
    		return;
	}

	public int getAlleThreadsFlag_glob()
	{
		return alleThreadsFlag_glob;
	}

	public void setAlleThreadsFlag_glob(int flag)
	{
		this.alleThreadsFlag_glob = flag;
	}
	
}
