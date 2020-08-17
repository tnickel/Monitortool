package data;

import java.util.HashMap;

import hiflsklasse.Tracer;

public class Hashmapper
{
	/*
	Wir brauchen eine Datenstruktur wie z.B.
	init(Ticket,Open Date,Close Date,Symbol,Action,Lots,SL,TP,Open Price,Close Price,Commission,Swap,Pips,Profit,Gain,Comment,Magic Number,Duration )
	
	und dann mit einer Funktion kann man die Attribute aus der Struktur holen wie z.B.
	get("Ticket")
	get("Open Date")
	get("Symbol")
	=> Dies macht der Hashmapper
	*/
	private String[] headliste_g =null;
	private HashMap<String, String> attribmap_g = null;
	
	public Hashmapper(String initstring)
	//erster init, hier werden die attribute in einer permanenten liste gespeichert
	{
		headliste_g = initstring.split(",");
	}
	public void addLine(String line)
	{
		//hier wird nur eine einzige Linie in der Menge aufgenommen
		//man nimmt immer die lininenattribute zusammen mit den values aus der linie in eine
		//map auf, so kann man die attribute schnell finden und extrahieren
		
		attribmap_g = new HashMap<String, String>();
		String[] lineparts=line.split(",");
		
		int anz=lineparts.length;
		for(int i=0; i<anz; i++)
			attribmap_g.put(headliste_g[i], lineparts[i]);
		
		
	}
	public String getAttribute(String line,String key)
	{
		//hier holt man sich ein bestimmtes attribut
		
		
		if(key.equals("Magic Number"))
			System.out.println("magic number");
		
		if(attribmap_g.containsKey(key)==true)
		{
			String value=attribmap_g.get(key);
			return value;
		}
		else
		{
			Tracer.WriteTrace(10, "E:Internal key<"+key+"> is not in map line<"+line+">");
		}
		return null;
	}
	
}
