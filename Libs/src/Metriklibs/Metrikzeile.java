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
	private ArrayList<String> attributlistNames = new ArrayList<String>();

	public Metrikzeile()
	{
	}

	public int  getLength()
	{
		return examplezeile.size();
	}
	
	public int getAttributanz()
	{
		return(attributlistNames.size());
		
	}
	public String getAllAttributsNamesAsString()
	{
		//holt alle attriute in einen string aus der metrikzeile
		int n=attributlistNames.size();
		String outzeile="";
		for(int i=0; i<n; i++)
		{
			outzeile=outzeile+attributlistNames.get(i)+";";
		}
		return outzeile;
	}
	public String getAllAttributsValuesAsString()
	{
		//holt alle attriute-values aus der metrikzeile und baue hieraus einen string.
		int n=attributlistNames.size();
		String outzeile="";
		for(int i=0; i<n; i++)
		{
			Metrikentry me=examplezeile.get(i);
			outzeile=outzeile+me.getValue()+";";
		}
		return outzeile;
	}
	public String getSpecificAttributValueAsString(String attribname)
	{
		//holt alle attriute-values aus der metrikzeile und baue hieraus einen string.
		int n=attributlistNames.size();
		String outzeile="";
		for(int i=0; i<n; i++)
		{
			Metrikentry me=examplezeile.get(i);
			if(me.getAttributName().toLowerCase().equals(attribname.toLowerCase()))
			  return me.getValue();
		}
		return outzeile;
	}
	
	
	
	public void setzeAttribute(String zeile)
	{

		// die erste Zeile beinhaltet die attributsnamen
		// in der Arrylist wird jedes Attribut abgelegt
		// als ersten werden schon mal die Namen abgelegt
		
		String splitter=calcSplitter(zeile);
				
		String[] segs = zeile.split(Pattern.quote(splitter));
		int anzattribs = segs.length;
		for (int i = 0; i < anzattribs; i++)
		{
			attributlistNames.add(segs[i]);
		}
	}

	public void  baueMetrikzeile(String zeile)
	{
		if(zeile.endsWith(";"))
			zeile=zeile+"0";
		ArrayList<Metrikentry> metrikzeile = new ArrayList<Metrikentry>();
		String splitter=calcSplitter(zeile);
		String[] segs = zeile.split(Pattern.quote(splitter));
		segs = zeile.split(Pattern.quote(splitter));
		int anzattribs = segs.length;
		//Tracer.WriteTrace(20, "len<"+anzattribs+"> zeile<"+zeile.substring(0, 20)+">");
		
		
		//wir vergleichen hier Die Attributsanzahl, das ist die erste Zeile mit den folgezeilen
		//Alle zeilen sollten die gleiche anzahl einträge haben
		if(segs.length!=attributlistNames.size())
		{
			//Hier stimmt die Attributanzahl nicht mit den spalten der folgezeilen überein.
			Tracer.WriteTrace(20, "segs.length="+segs.length+" atributlist.size="+attributlistNames.size());
			Tracer.WriteTrace(20, "Error: #attribute<"+segs.length+"> != #values in zeile<"+attributlistNames.size()+">");
			Tracer.WriteTrace(20, "Zeile<"+zeile+">");
			
			Tracer.WriteTrace(10, "Error: Corrupt DatabankExport.csv Attribanzahl don´t fit with the entrys");
		}
		for (int i = 0; i < anzattribs; i++)
		{
			Metrikentry metrikentry = new Metrikentry();
			metrikentry.setValue(segs[i]);
			metrikentry.setAttributName(attributlistNames.get(i));
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
	public String holeStratName()
	{
		// Der Strategiename kommt an position 0
		Metrikentry me=examplezeile.get(0);
		me.getValue();
		return(me.getValue());
	}
	private String calcSplitter(String zeile)
	{
		if(zeile.contains(";"))
			return (";");
		else if(zeile.contains(","))
			return (",");
		else
			Tracer.WriteTrace(10, "E: cant find spliter ', or ; in zeile<"+zeile+">'");
		return null;
					
	}
}
