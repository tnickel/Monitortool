package Metriklibs;

import java.util.ArrayList;
import java.util.regex.Pattern;

import hiflsklasse.Tracer;


public class Metrikzeile
{
	// die Filterklasse behinhaltet die Attribute
	// hier wird nur eine Zeile der Metriktabelle zurückgeliefert
	private String filename_glob = null;
	// filtertype =0 => filter, filtertype=1 dann endtest
	int filtertype = 0;
	//hier sind die zeilen der matrix abgebildet
	private ArrayList<Metrikentry> examplezeile = new ArrayList<Metrikentry>();
	//hier sind die Namen der Attribute drin
	private ArrayList<String> attributlist = new ArrayList<String>();

	public Metrikzeile()
	{
	}

	public int  getLength()
	{
		return examplezeile.size();
	}
	
	public void setzeAttribute(String zeile)
	{

		// die erste Zeile beinhaltet die attributsnamen
		// in der Arrylist wird jedes Attribut abgelegt
		// als ersten werden schon mal die Namen abgelegt
		String[] segs = zeile.split(Pattern.quote(","));
		int anzattribs = segs.length;
		for (int i = 0; i < anzattribs; i++)
		{
			attributlist.add(segs[i]);
		}
	}

	public void  baueMetrikzeile(String zeile)
	{
		ArrayList<Metrikentry> metrikzeile = new ArrayList<Metrikentry>();
		String[] segs = zeile.split(Pattern.quote(","));
		segs = zeile.split(Pattern.quote(","));
		int anzattribs = segs.length;
		
		if(segs.length!=attributlist.size())
			Tracer.WriteTrace(10, "Error: #attribute<"+segs.length+"> != #values in zeile<"+attributlist.size()+">");
		
		for (int i = 0; i < anzattribs; i++)
		{
			Metrikentry metrikentry = new Metrikentry();
			metrikentry.setValue(segs[i]);
			metrikentry.setAttributName(attributlist.get(i));
			metrikzeile.add(metrikentry);
		}
		examplezeile=metrikzeile;
	}

	public Metrikentry holeMetrikentry_dep(String attribut)
	{
		int anz = examplezeile.size();
		for (int i = 0; i < anz; i++)
		{
			Metrikentry me = examplezeile.get(i);
			if (me.getAttributName().equals(attribut))
				return me;
		}
		return null;
	}

	public float holeFloatwert_dep(String attribut)
	{
		float fval = 0;
		Metrikentry me = (holeMetrikentry_dep(attribut));

		if(me==null)
			return 0;
		
		// falls der metrikentry ein float-attribut hat, dann wird der wert
		// ausgelesen
		if (me.getAttributflag() == 2)
			fval = Float.valueOf(me.getValue());

		return fval;
	}

	public Metrikentry holeEntry(int pos)
	{
		return (examplezeile.get(pos));
	}
}
