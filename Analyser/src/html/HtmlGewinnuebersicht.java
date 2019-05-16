package html;

import hilfsklasse.Inf;
import objects.UserDbObjSort;

public class HtmlGewinnuebersicht extends HtmlTabelle
{
	public HtmlGewinnuebersicht(String nam)
	{
		this.setfilename(nam);
	}
	
	public void addKopf(String ueberschrift,String tabellenkopf)
	{
		this.Kopf(ueberschrift);
	
		// Kopfzeile
		addKopfleiste(tabellenkopf);
	}

	private void buildZeile(Inf inf, String farbe, String teilzeile)
	{
		String tmpzeile = "<td><font color=" + farbe + ">";
		tmpzeile = tmpzeile.concat(teilzeile);
		tmpzeile = tmpzeile.concat("</td>");
		inf.writezeile(tmpzeile);
	}

	public void addZeile(UserDbObjSort udbobj_s)
	{ 
		//"username@Gewinn@Handlungen@einzelGew@SummenGew@SumDB"
		
		// die Einträge sind mit Semikolon getrennt
		String farbe = "black";
		int hotpoints = 0;
		
		// Zeilenanfang
		sz("<tr>");

		String infostring=udbobj_s.getGewinninfostring();
		String handl=infostring.substring(infostring.indexOf(":"));
		
		// username
		buildZeile( "black", udbobj_s.getName());
		// points
		buildZeile( "black", Float.toString(udbobj_s.getPoints()));
		// gewinn
		buildZeile( "black", Float.toString(udbobj_s.getGewinn()));
		// Handlungen
		buildZeile( "black", handl);
		buildZeile( "black", Integer.toString(udbobj_s.getAnzGewinne()));
		buildZeile( "black", Integer.toString(udbobj_s.getAnzVerluste()));
		buildZeile( "black", Integer.toString(udbobj_s.getAnzNeutral()));
		//eingzelgew
		//bsp:<td><font color=black><a href="../..//db//UserThreadVirtualKonto//Einzelgewinne/Masteruser.html" >Einz</a></td>
		buildZeile("black",HLink("..//UserThreadVirtualKonto//Einzelgewinne/"+udbobj_s.getName()+".html","Einz"));
		//summengew
		if(udbobj_s.getName().contains("Masteruser"))
		  buildZeile("black",HLink("..//UserThreadVirtualKonto//Summengewinne/"+udbobj_s.getName()+"3.txt","Summe"));
		else
			buildZeile("black",HLink("..//UserThreadVirtualKonto//Summengewinne/"+udbobj_s.getName()+".txt","Summe"));
		//sumdb
		buildZeile("black",HLink("..//UserThreadVirtualKonto//Summengewinnedb/"+udbobj_s.getName()+".db","SummeDB"));

		// Zeilenende
		sz("</tr>");

	
	}
}
