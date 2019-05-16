package data;

import gui.Mbox;
import hiflsklasse.Inf;
import modtools.Patcher;

public class MqlIndikatorEa extends Patcher
{
	//diese Funktion holt die Filterfunktion aus dem File Filteranalysev.03.mq4
	//zurückgeliefert wird ein Stringarray mit den entsprechenden Zeilen
	
	Inf inf_glob=null;
	
	public String[] getPatchArray(String string1)
	{
		//bool Filter_0026b (){// (foundpos1)
		//	 int periode = 4;
		//		double breakline = 55.0;
		//		double RSI_Line_Bar_1 = iRSI(NULL,0,periode,PRICE_CLOSE,1);
		//		double RSI_Line_Bar_2 = iRSI(NULL,0,periode,PRICE_CLOSE,2);
		//		double RSI_Line_Bar_3 = iRSI(NULL,0,periode,PRICE_CLOSE,3);
		//			
		//		if ( 	RSI_Line_Bar_1 < breakline)return (true);
		//	    return (false);
		//	} (foundpos2)
		
		
		String suchstring="bool Filter_"+string1;
		int i=0,j=0,foundpos1=0, foundpos2=0;
		//der Anfang der funktion suchstring wird gesucht
		int anz = zeilenspeicher.length;
		for (i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile.contains(suchstring) == true)
			{
				foundpos1=i;
				break;
			}
		}
		if(foundpos1==0)
			Mbox.Infobox("filter beginn<"+suchstring+"> not found");
		
		//das Ende der funktion suchstring wird gesucht
		for(j=foundpos1; j<anz; j++)
		{
			String zeile = zeilenspeicher[j];
			//a) return (false) suchen
			if ((zeile.contains("return") == true)&&(zeile.contains("(false);")==true))
			{
				//und nächste Zeile muss } beinhalten
				String zeile2 = zeilenspeicher[j+1];
				if(zeile2.contains("}")==true)
				{
					foundpos2=j+1;
					break;
				}
			}
		}
		if(foundpos2==0)
			Mbox.Infobox("filter end<return (false)\n }> not found");
		
		//das Ergebniss als array zurückliefern
		int anzpos=foundpos2-foundpos1+1;
		 String zsp[] = new String[anzpos];
		 for(i=0; i<anzpos; i++)
			zsp[i]=new String(zeilenspeicher[foundpos1+i]);
		 return zsp;
		 
		
	}
}
