package Metriklibs;

import java.util.List;

import hiflsklasse.Tracer;

import java.util.ArrayList;

public class Attribliste
{
	private ArrayList<String> liste;
	
	public Attribliste()
	{
		this.liste = new ArrayList<>(); // Verwende ArrayList zur Initialisierung
	}
	
	public void add(String attribname)
	{
		// Tracer.WriteTrace(20, "nimm attribut <" + attribname + "> auf"); // Falls
		// Tracer verwendet wird, sicherstellen, dass es importiert ist
		liste.add(attribname);
	}
	
	public ArrayList<String> getAttribliste()
	{
		return new ArrayList<>(liste); // Return a copy to maintain encapsulation
	}
	public int getSize()
	{
		if(liste!=null)
			return(liste.size());
			else
				return 0;
	}
}