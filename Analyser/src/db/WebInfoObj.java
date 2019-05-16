package db;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import objects.Obj;

//Diese Klasse speichert Datenbank objekte
//Diese Objekte haben die Form
//Symbol#status#Ladedatum1#Webstring1#Ladedatum2#Webstring2
//Dieses obersverobjekt stellt eine Webseite da, man möchte diese 
//Webseite auf Veränderungen überwachen. Z.B. insidertrades werden hier 
//auf Veränderungen überprüft.
//Dieses objekt beinhaltet alles was für die Überwachung der Webseite 
//Notwendig ist.
//Das Symbol ist die pivotkennung, diese Kennung muss Eindeutig sein
//Die Pivotkennung fliesst in den zu speichernden Dateinamen mit ein

public class WebInfoObj extends Obj
{

	public String symbol = null;
	public int status = 0;
	public String Ladedatum1 = null;
	public String Webstring1 = null;
	public String Changedatum2 = null;
	public String Webstring2 = null;

	public WebInfoObj(String zeile)
	{
		try
		{
			symbol = new String(SG.nteilstring(zeile, "#", 1));
			status = Integer.valueOf(SG.nteilstring(zeile, "#", 2));
			Ladedatum1 = new String(SG.nteilstring(zeile, "#", 3));
			Webstring1 = new String(SG.nteilstring(zeile, "#", 4));
			Changedatum2 = new String(SG.nteilstring(zeile, "#", 5));

			Webstring2 = new String(SG.nteilstring(zeile, "#", 6));
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getLadedatum1()
	{
		return Ladedatum1;
	}

	public void setLadedatum1(String ladedatum1)
	{
		Ladedatum1 = ladedatum1;
	}

	public String getWebstring1()
	{
		return Webstring1;
	}

	public void setWebstring1(String webstring1)
	{
		Webstring1 = webstring1;
	}

	public String getChangedatum2()
	{
		return Changedatum2;
	}

	public void setLadedatum2(String changedatum2)
	{
		Changedatum2 = changedatum2;
	}

	public String getWebstring2()
	{
		return Webstring2;
	}

	public void setWebstring2(String webstring2)
	{
		Webstring2 = webstring2;
	}

	@Override
	public String GetSaveInfostring()
	{
		return "symbol#status#Ladedatum1#Webstring#Changedatum2#Webstring2";
	}

	@Override
	public String toString()
	{
		return (symbol + "#" + status + "#" + Ladedatum1 + "#" + Webstring1
				+ "#" + Changedatum2 + "#" + Webstring2);
	}

	public String getMasterkennungx()
	{
		return ("***AKT-DB***");
	}

	@Override
	public boolean equals(Object obj)
	{
		// anhand der Pivotkennung wird entschieden ob es sich hier
		// um die gleichen webseitenobjekte handelt
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WebInfoObj other = (WebInfoObj) obj;
		if (symbol == null)
		{
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

}
