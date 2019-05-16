package hilfsklasse;

import ComData.GC;
import ComData.Tools;
import ComData.ToolsException;

public class StringGrundfunktionenDyn
{
	protected boolean CheckKeyword(String zeile, String keyword)
	{
		if (zeile.contains(keyword) == true)
			return true;
		else
			return false;
	}

	public boolean is_zahl(String keyword)
	{
		// 45.880=>45880
		// Punkt entfernen
		String kw = keyword;

		if (keyword.contains(".") == true)
			kw = removePoint(keyword);

		try
		{
			Integer.valueOf(kw);
			return true;
		} catch (NumberFormatException nfe)
		{// keine zahl
			return false;
		}
	}

	public int get_zahl(String keyword)
	{
		if (is_zahl(keyword) == true)
		{
			return (Integer.valueOf(keyword));
		} else
			return -1;
	}

	protected String removePoint(String keyword)
	{
		// Wandelt String:45.800 nach int:45800 um
		// Punkt entfernen
		String kw = null;
		kw = keyword;

		while (keyword.contains(".") == true)
		{
			String left = null, right = null;
			left = keyword.substring(0, keyword.indexOf("."));
			right = keyword.substring(keyword.indexOf(".") + 1, keyword
					.length());
			kw = left + right;
			keyword = kw;
		}
		return (kw);
	}

	protected String nteElement(String mem, String such, int count)
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

	
	
	public String nteilstring(String zeile, String trenner, int position)
			throws ToolsException
	{ /*
	 * splitet eine Zeile bzgl. eines Trenners, zurückgeliefert wird der nte
	 * String
	 */

		if (zeile.contains("vor April") == true)
			zeile = new String(GC.startdatum);

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
			System.out.println("Index kann nicth gefunden werden");
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

	public int countZeichen(String zeile, String trenner)
	{// zaehlt das counter
		// vorkommen von
		// trenner in der
		// zeile
		int counter = 0;

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

	public String convFilename(String quellname)
	{
		// wandelt den String in einen für das Dateisystem verwendbaren String
		// um
		while (quellname.contains("/") == true)
		{
			quellname = quellname.substring(0, quellname.indexOf("/") - 1);
		}

		while (quellname.endsWith(".") == true)
		{
			// short at the end
			quellname = quellname.substring(0, quellname.length() - 1);
		}
		return new String(quellname);
	}
}
