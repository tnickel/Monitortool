package filter;

import hilfsklasse.Tools;

public class Filter
{
	protected Boolean on=false;
	protected String adate=Tools.entferneZeit(Tools.get_aktdatetime_str());
	
	
	public Boolean getOn()
	{
		return on;
	}

	public void setOn(Boolean on)
	{
		this.on = on;
	}
	
}
