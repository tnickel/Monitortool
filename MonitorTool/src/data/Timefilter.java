package data;

public class Timefilter
{
	//diese Klasse beinhaltet die Timefilterfunktionen
	//Man möchte hier z.B. die Trades eines bestimmten Wochentages rausfiltern, bzw
	//die Trade einer bestimmten Uhrzeit
	
	//Wochentagsliste
	private boolean dayarray[] = new boolean[7];  
	
	//Stundenliste
	private boolean timearray[] = new boolean[24];

	public Timefilter()
	{}
	
	public void setday(int day,boolean value)
	{
		//den wochentag setzen, true oder false
		dayarray[day]=value;
	}
	public boolean getday(int day)
	{
		return (dayarray[day]);
		
	}
	public void settime(int timeindex,boolean value)
	{
		//den wochentag setzen, true oder false
		timearray[timeindex]=value;
	}
	public boolean gettime(int timeindex)
	{
		return (timearray[timeindex]);
		
	}
}
