package mqlLibs;

import gui.Mbox;
import hiflsklasse.SG;
import hiflsklasse.Tracer;
public class eaclass
{
	//class to handle something with the mql
	
	
	static public int  calcMagic(String quellnam)
	{
		// B23 EURUSD M5_Strategy 454.6035.mq4 oder B23 EURUSD M5_Strategy
		// 454.6035_2.mq4
		// die Magic muss am ende stehen
		
		//new format
		//Q05 EURUSD H1Strategy 0.2.26010.mq4
		if (quellnam.contains(".mq4") == false)
			Tracer.WriteTrace(10, "Error: '.mq4'-kennung fehlt im file <"
					+ quellnam + ">");
		
		if(quellnam.contains(" ")==false)
			Tracer.WriteTrace(10, "E:wrong EA name <"+quellnam+"> name should contains [currency] [timeframe] [magic].mql with spaces");
		
		String keyword = quellnam.substring(quellnam.lastIndexOf(" "),
				quellnam.indexOf(".mq4"));

		keyword = keyword.replace(".", "");
		keyword = keyword.replace("_", "");
		keyword = keyword.replace("(", "");
		keyword = keyword.replace(")", "");

		// die führenden nullen entfernen
		while ((keyword.startsWith("0")) || (keyword.startsWith(" ")))
			keyword = keyword.substring(1, keyword.length());

		if (keyword.length() > 11)
			Mbox.Infobox("Magic<" + keyword + "> too long max 9 digits");
	
		return SG.get_zahl(keyword);
	}
	
	
}
