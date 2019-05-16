package hilfsklasse;

import ComData.Tools;
import ComData.ToolsException;

public class SG
{
	/*
	 * tips
	 * 
	 * Auftrennen in Teilstrings: split(String trennzeichen, int anz)
	 * 
	 * • Zerlegt den aufrufenden String in Teilstrings • bei jedem Vorkommen des
	 * Trennzeichens wird ein neuer String begonnen und der alte abgeschlossen •
	 * maximale Anzahl an Teilstrings festlegbar • Die Übergabe der Teilstrings
	 * erfolgt in ein Stringarray
	 */

	protected static boolean CheckKeyword(String zeile, String keyword)
	{
		if (zeile.contains(keyword) == true)
			return true;
		else
			return false;
	}

	public static boolean is_zahl(String keyword)
	{
		// 45.880=>45880
		// Punkt entfernen
		String kw = keyword;

		if (keyword == null)
			return false;

		if (keyword.contains(".") == true)
			kw = zahlenkorrektur(keyword);

		//leerzeichen entfernen
		kw=kw.replaceAll(" ", "");
		
		try
		{
			Integer.valueOf(kw);
			return true;
		} catch (NumberFormatException nfe)
		{// keine zahl
			return false;
		}
	}

	public static int get_zahl(String keyword)
	{
		if (keyword == null)
			return -1;

		if (keyword.contains(" "))
			keyword = keyword.replace(" ", "");

		if (is_zahl(keyword) == true)
		{
			return (Integer.valueOf(keyword));
		} else
			return -1;
	}
	public static float get_float_zahl(String keyword,int anznachkommastellen)
	{
		String fzahl=keyword;
		if (keyword == null)
			return -1;

		if (keyword.contains(" "))
			keyword = keyword.replace(" ", "");
		
		int pointpos=keyword.indexOf(".");
	
		
		//falls noch was zum abschneiden ist
		if(pointpos>0)
		if((pointpos+anznachkommastellen)<keyword.length())
		{
		 fzahl=keyword.substring(0,pointpos+anznachkommastellen+1);
		}
		
		return (Float.valueOf(fzahl));
		
	}

	public static String kuerzeFloatstring(String keyword, int anznachkommastellen)
	{
		int pointpos=keyword.indexOf(".");
		
		//falls noch was zum abschneiden ist
		if(pointpos>0)
		if((pointpos+anznachkommastellen)<keyword.length())
		{
		 keyword=keyword.substring(0,pointpos+anznachkommastellen+1);
		}
		return keyword;
	}
	public static String zahlenkorrektur(String keyword)
	{
		// Wandelt String:45.800 nach int:45800 um
		// Punkt entfernen
		String kw=null;
		
		
		
		if(keyword.contains(" "))
			keyword=keyword.replaceAll(" ", "");
		
		
		kw=keyword;
		while (keyword.contains(".") == true)
		{
			String left = null, right = null;
			left = keyword.substring(0, keyword.indexOf("."));
			right = keyword.substring(keyword.indexOf(".") + 1, keyword
					.length());
			kw = left + right;
			keyword = kw;
		}
		return (keyword);
	}

	public static String nteElement(String mem, String such, int count)
	// mem:gesammtspeicher
	// such:suchelement
	// n:das nte element wird gesucht
	{
		int n = 0, anz = 0;
		if ((n = countZeichen(mem, such)) < count)
		{
			Tracer.WriteTrace(20, "W:string <" + such + "> found only <" + n
					+ "> times in <" + mem + ">=");
			return null;
		}

		while (anz < count)
		{
			mem = mem.substring(mem.indexOf(such) + such.length());
			anz++;
		}

		return mem;
	}

	public static String nteElementHinten(String mem, String trenner, int count)
	{
		// extrahiert das nte Element von hinten
		// trenner: trenner nach dem extrahiert wird
		// count:position (die letzte Position fängt bei 1 an)

		int n = 0, anz = 1;
		if ((n = countZeichen(mem, trenner)) < count)
		{
			Tracer.WriteTrace(20, "W:string <" + trenner + "> found only <" + n
					+ "> times in <" + mem + ">=");
			return null;
		}

		while (anz < count)
		{
			mem = mem.substring(0, mem.lastIndexOf(trenner));
			anz++;
		}

		String erg = mem.substring(mem.lastIndexOf(trenner) + trenner.length());
		return erg;
	}

	public static String nteilstring(String zeile, String trenner, int position)
			throws ToolsException
	{ /*
	 * splitet eine Zeile bzgl. eines Trenners, zurückgeliefert wird der nte
	 * String
	 */
		/* Text1#Text2#Text3#Text4 */
		String anfangspart = null, restpart = null;

		if (trenner.length() != 1)
		{
			Tracer.WriteTrace(10, Tools.class.getName()
					+ ":ERROR:trenner fehler<" + trenner
					+ "> nur länge 1 erlaubt");
		}

		int anz = 0, linkspos = 0, rechtspos = 0;
		// erste position

		anz = countZeichen(zeile, trenner);
		if ((position == 0) || (position > anz + 1))
		{
			// Sicherheitsabfrage:
			System.out.println("Index kann nicht gefunden werden");
			System.out.println("Zeile<" + zeile + "> trenner<" + trenner
					+ "> position<" + position + ">");
			Tracer.WriteTrace(10, Tools.class.getName()
					+ ":ERROR:Indexfehler zeile=<" + zeile + "> trenner<"
					+ trenner + "> position<" + position + ">");
			throw new ToolsException("Fehler im Index aufgetreten...");
		}

		anfangspart = zeile;

		if (position > 1)
			for (int i = 0; i < position - 1; i++)
			{
				anfangspart = anfangspart.substring(anfangspart
						.indexOf(trenner) + 1, anfangspart.length());
			}

		if (position < anz + 1)
			restpart = anfangspart.substring(0, anfangspart.indexOf(trenner));
		else
			// ist letzte position
			restpart = anfangspart.substring(0, anfangspart.length());

		return (new String(restpart));
	}

	public static int countZeichen(String zeile, String trenner)
	{// zaehlt das counter
		// vorkommen von
		// trenner in der
		// zeile
		int counter = 0;

		if (zeile == null)
		{
			Tracer.WriteTrace(20, "Warning: countZeichen Nullzeile zeile<"
					+ zeile + "> trenner<" + trenner + ">");
			return 0;

		}
		if (trenner.length() != 1)
		{
			Tracer.WriteTrace(20, Tools.class.getName()
					+ ":W:Trennerfehler, nur länge 1 erlaubt trenner<"
					+ trenner + ">");
			return 0;
		}
		for (int i = 0; i < zeile.length(); i++)
		{
			if (zeile.charAt(i) == trenner.charAt(0))
				counter++;
		}

		return (counter);
	}

	public static int countExternalWeblinks(String zeile)
	{
		//zählt wie oft der Substring in der Zeile vorkommt 
		//www.wallstreet... werden nicht gezählt
		int counter = 0;

		if (zeile == null)
		{
			Tracer.WriteTrace(20, "Warning: countSubstring Nullzeile zeile<"
					+ zeile + "> trenner<" + "nofollow" + ">");
			return 0;
		}

		//zählt nach wie oft der Substring vorkommt
		int xpos=999; int spos=0;
		if (zeile.contains("nofollow") == true)
		{
			while(xpos>0)
			{
				xpos=zeile.substring(spos).indexOf("nofollow");
				
				//falls das ende erreicht ist
				if(xpos+spos+80>=zeile.length())
					return counter;
				
				//schneidet das Teil hinter dem gefundenen ab
				String sublink=zeile.substring(xpos+spos,xpos+spos+80);
				//System.out.println("sublink<"+sublink+">");

				//gehe weiter im Suchstring
				spos=spos+xpos+1;
				
				//Der interne Link wird nicht gezählt
				if(sublink.contains("www.wallstreet-online.de")==true)
					continue;

				//dies ist kein weblink, wird also nicht gezählt
				if(sublink.contains("http://")==false)
					continue;

				if(xpos>0)
					counter++;
				else
				{
					if(spos>zeile.length())
						Tracer.WriteTrace(10, "Error: spos zu gross");
					
					return counter;
					
				}
			}
		} else
			return 0;

		return (counter);
	}

	public static int countIcons(String zeile)
	{
		//sucht nach smily links
		//http://img.wallstreet-online.de/smilies
		
		
		//zählt die anz der Icons im Posting
		int counter = 0;

		if (zeile == null)
		{
			Tracer.WriteTrace(20, "Warning: countSubstring Nullzeile zeile<"
					+ zeile + "> trenner<" + "nofollow" + ">");
			return 0;
		}

		//zählt nach wie oft der Substring vorkommt
		int xpos=999; int spos=0;
		if (zeile.contains("http://img.wallstreet-online.de/smilies") == true)
		{
			while(xpos>0)
			{
				xpos=zeile.substring(spos).indexOf("http://img.wallstreet-online.de/smilies");
				
				//falls das ende erreicht ist
				if(xpos+spos+80>=zeile.length())
					return counter;
				
				//schneidet das Teil hinter dem gefundenen ab
				String sublink=zeile.substring(xpos+spos,xpos+spos+80);
				//System.out.println("sublink<"+sublink+">");

				//gehe weiter im Suchstring
				spos=spos+xpos+1;

				if(xpos>0)
					counter++;
				else
				{
					if(spos>zeile.length())
						Tracer.WriteTrace(10, "Error: spos zu gross");
					
					return counter;
					
				}
			}
		} else
			return 0;

		return (counter);
	}
	public static String convFilename(String quellname)
	{
		if (quellname == null)
			Tracer.WriteTrace(10, "internal quellname==null");

		quellname=quellname.replace("?", "X");
		
		// wandelt den String in einen für das Dateisystem verwendbaren String
		// um
		while (quellname.contains("/") == true)
		{
			Tracer.WriteTrace(20, "Warning: quellname <" + quellname
					+ "> hat / als Zeichen, wird entfernt");
			quellname = quellname.substring(0, quellname.indexOf("/") - 1);
		}

		while (quellname.endsWith(".") == true)
		{
			// short at the end
			Tracer.WriteTrace(20, "Warning: quellname <" + quellname
					+ "> hat . als Zeichen, wird entfernt");
			quellname = quellname.substring(0, quellname.length() - 1);
		}
		if (quellname.contains(":") == true)
		{
			// entferne : aus dem Namen
			Tracer.WriteTrace(20, "Warning: quellname <" + quellname
					+ "> hat : als Zeichen, wird entfernt");
			quellname = quellname.replaceAll(":", "");
		}
		while (quellname.endsWith(" ") == true)
		{
			// short at the end
			Tracer.WriteTrace(20, "Warning: quellname <" + quellname
					+ "> hat . als Zeichen, wird entfernt");
			quellname = quellname.substring(0, quellname.length() - 1);
		}
		
		return new String(quellname);
	}

	public static String calcAnfangsstring(String eingabe, int len)
	{	
		if(eingabe.length()>=len)
			return(eingabe.substring(0,len));
		else
			return(eingabe);
	}
}
