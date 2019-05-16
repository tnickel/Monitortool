package data;

import hiflsklasse.SG;

public class IndiGrundfunkt
{
	//dies sind einfache grundfuktionen für die indikatoren
	
	public String calcIndexname(int index)
	{
		//berechnet aus dem index 1...100 den indikatornamen
		//0001a, 0001b,0002a,0002b,.....,0049a,0049b,

		//ungerade==>a
		//gerade==>b
		//zahl=(input div 2)+ (input mod 2)
		//wenn(zahl mod 2==0) ==>b
		//wenn(zahl mod 2==1) ==>a
		//1=1a
		//2=1b
		//3=2a
		//4=2b
		//5=3a.....index
		//6=3b
		//7=4a
		//8=4b    8/2=4+0
		//9=5a    9/2=4+1
		//10=5b   10/2=5+0
		
		//die umrechnung bestimmen
		int outzahl=(index / 2)+(index%2);
		
		//das characterzeichen bestimmen
		String outchar="";
		if((index %2)==0)
			outchar="b";
		else if((index%2)==1)
			outchar="a";
		
		//von vorne mit Nullen füllen
		String outstring=String.valueOf(outzahl)+outchar;
		String valueRight = format(outstring, '0', 5, true); // erzeugt "0002a"
		return valueRight;	
	}
	
	public int calcIndex(String indexname)
	{	
		//berechnet aus dem indikatornamen  den index
		//zb. 0005b ==> 10
		
		//das zeichen holen
		String lchar=indexname.substring(4, 5);
		
		//den String mit der zahl holen
		String zstring=indexname.substring(0,4);
		
		//verdoppeln
		int z1=SG.get_zahl(zstring)*2;

		//bei A muss noch einer abgezogen werden
		if(lchar.equals("a"))
			z1=z1-1;

		return z1;
		
		
	}
	public String getPicturePath( int index)
	{
		//berechnet aus dem index den Bildnamen
		//rechnet z.b. aus 9 ==> 0005a.gif
		
		String fna= calcIndexname(index)+".gif";
		String filepath=Config.getRootdir()+"\\picture\\"+fna;
		return filepath;
		
	}
	//quelle:  http://java.soeinding.de/content.php/utilString
	public static final int NO_CUT = 0;
	public static final int CUT_LEFT = 1;
	public static final int CUT_RIGHT = 2;
	public static String format(final String value, final char c, final int count, final boolean fillLeft, final int tooBigStringsCutStrategy) {
	    if (value == null || value.length() == 0) {
	        return createChain(c, count);
	    }
	    if (value.length() >= count) {
	        if (tooBigStringsCutStrategy == CUT_LEFT) {
	            return value.substring(value.length() - count);
	        }
	        return (tooBigStringsCutStrategy == CUT_RIGHT) ? value.substring(0, count) : value;
	    }
	    final StringBuffer result = new StringBuffer(count);
	    if (!fillLeft) {
	        result.append(value);
	    }
	    for (int i = value.length(); i < count; i++) {
	        result.append(c);
	    }
	    if (fillLeft) {
	        result.append(value);
	    }
	    return result.toString();
	}

	public static String format(final String value, final char c, final int count, final boolean fillLeft) {
	    return format(value, c, count, fillLeft, CUT_RIGHT);
	}
	public static String format(final String value, final char c, final int count) {
	    return format(value, c, count, true, CUT_RIGHT);
	}
	public static String createChain(final char c, final int count) {
	    final StringBuffer result = new StringBuffer(count);
	    for (int i = count; i > 0; i--) {
	        result.append(c);
	    }
	    return result.toString();
	}
	
}
