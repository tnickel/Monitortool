package mqlLibs;

import gui.Mbox;
import hiflsklasse.SG;
import hiflsklasse.Tracer;
public class Eaclass
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
	static public int  calcMagicSimple(String quellnam)
	{
		// B23 EURUSD M5_Strategy 454.6035.mq4 oder B23 EURUSD M5_Strategy
		// 454.6035_2.mq4
		// die Magic muss am ende stehen
		
		//new format
		//Q05 EURUSD H1Strategy 0.2.26010.mq4
		if (quellnam.contains(".mq4") == false)
			return 0;
		
		if(quellnam.contains(" ")==false)
			return 0;
			
		
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
	static public String calcMagicFsbPortBaseMagic(int magic)
	{
		//diese ist der prefix für die FSB portfolio eas, die ersten 6 stellen
		//z.b. 210401 sind die base magic number fuer den portfolio ea
		//z.B. P01 EURUSD M1 210401.mq4
		String magicpref = String.valueOf(magic).substring(0, 6);
		return magicpref;
	}
	
	static public String calcMagicFsbPortPostMagic(int magic)
	{
		//z.B. file=P01 EURUSD M1 210401.mq4
		//bsp1:
		//magic 210401011
		//postmagic=011
		//bsp2:
		//magic 210401001
		//postmagic=01

		
		//cutte erst mal die ersten 6 stellen
		String postmagicstr=String.valueOf(magic);
		if(postmagicstr.length()>6)		
				postmagicstr=postmagicstr.substring(6);
				
		//Dann wandle diese zahl in eine integerzahl um führende 0len zu entfernen
		int pmag=Integer.valueOf(postmagicstr);
		//packe davor wieder eine 0
		String pmagstr="0"+pmag;
		return pmagstr;
	}
}
