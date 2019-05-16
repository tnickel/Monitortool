package html;

import hilfsklasse.FileAccess;
import mainPackage.GC;
import objects.PrognoseDbObj;

public class HtmlPrognosenuebersicht extends HtmlSlideruebersicht
{
	public HtmlPrognosenuebersicht(String fnam)
	{
		super.setfilename(fnam);
	}
	public void addPrognosenZeile(int pos, PrognoseDbObj pobj,String farbe,String threadlink,int mid,String seitdatum)
	{ // die Einträge sind mit Semikolon getrennt

		// alter: das letzte posting in diesem slider ist schon anz tage alt
	
		//Zeilenanfang
		sz("<tr>");
		
		//datum
		buildZeile(farbe, pobj.getAufnahmedatum());
		
		//seit
		//Diese Prognose gibt es seit dem datum x
		buildZeile(farbe, seitdatum);
		
		//info
		buildZeile(farbe, pobj.getInfo());

		//tid
		buildZeile(farbe, Integer.toString(pobj.getThreadid()));
		
		//symb
		buildZeile(farbe,pobj.getSymb());
				
		//aktname
		buildZeile(farbe,  pobj.getAktname());
			
		//midinfo
		buildZeile(farbe, "<a href=\"../..//handdata//midinfo//"
				+ mid + ".txt\" > " + "(Midinfo) </a>");
		
		// postanzahl
		buildZeile(farbe, Integer.toString(pobj.getSliderpostanz()));

		// mitlRang
		buildZeile(farbe, Float.toString(pobj.getMittlerRang()));

		// Neue User im Thread
		String fnam2 = GC.rootpath + "\\db\\UserThreadVirtualKonto\\NeueUserImThread\\"
				+ pobj.getThreadid() + ".txt";
		if (FileAccess.FileAvailable(fnam2))
		{
			buildZeile(farbe, "<a href=\"../..//db//UserThreadVirtualKonto//NeueUserImThread//"
					+ pobj.getThreadid() + ".txt\" > " + "("
					+ pobj.getNeueguteU() + "/" + pobj.getNeueschlechteU()
					+ "/" + pobj.getNeuebadU() + ")" + " </a>");
		} else
			buildZeile(farbe, "(" + pobj.getNeueguteU() + "/"
					+ pobj.getNeueschlechteU() + "/" + pobj.getNeuebadU() + ")");

		//alter letztes posting
		buildZeile(farbe,Integer.toString(pobj.getAlterletztePosting()));
		
		// handelshinweis
		buildZeile(farbe,pobj.getHandelshinweis());
		
	
		
		// link
		buildZeile(farbe, "<a href=" + threadlink + "> link</a>");
		
		//Prognosenalter
		buildZeile(farbe,Integer.toString(pobj.getPrognosenalter()));
		
		// Kennung
		buildZeile(farbe,pobj.getKennung());
		
		// Zeilenende
		sz("</tr>");
	}
}
