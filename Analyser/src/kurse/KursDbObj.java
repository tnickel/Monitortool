package kurse;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import objects.Obj;

public class KursDbObj extends Obj
{
	private String symbol = null;
	private String startdatum = null;
	private String enddatum = null;
	private String breakdate = null;
	private String dummy = null;
	private int anzwerte = 0;
	private int gesvolumen = 0;
	private String lastload = null;
	private String boerse = null;
	// der Steigungsfaktor gibt an bei wieviele der Stützwerte in aufsteigender
	// folge kommen. z.B. faktor = 10 => die Kurve ist nur Steigend
	private int steigfaktor = 0;
	//dies ist der mittlere Wert über alle werte des kurses
	private float mittelwert=0;
	//dies ist die standartabweichung vom mittelwert, dieser Wert ist im prinzip
	//so eine art risikofaktor
	private float standartabweichung=0;
	//private float reboundmin=0;
	//private float reboundmax=0;
	
	public KursDbObj(String zeile)
	{
		int anz = SG.countZeichen(zeile, "#");
		
		try
		{
			symbol = new String(SG.nteilstring(zeile, "#", 1));
			startdatum = new String(SG.nteilstring(zeile, "#", 2));
			enddatum = new String(SG.nteilstring(zeile, "#", 3));
			breakdate = new String(SG.nteilstring(zeile, "#", 4));
			dummy = new String(SG.nteilstring(zeile, "#", 5));
			if (dummy.equals("dummy") == false)
				Tracer.WriteTrace(10, "Error: dummy error <" + zeile + ">");
			anzwerte = Integer.valueOf(SG.nteilstring(zeile, "#", 6));
			gesvolumen = Integer.valueOf(SG.nteilstring(zeile, "#", 7));
			steigfaktor = Integer.valueOf(SG.nteilstring(zeile, "#", 8));
			if (anz == 9)
			{
				boerse = new String(SG.nteilstring(zeile, "#", 9));
				lastload = new String(SG.nteilstring(zeile, "#", 10));
			}
			if(anz==11)
			{
				String dummy=null;
				dummy= new String(SG.nteilstring(zeile, "#", 11));
				dummy = new String(SG.nteilstring(zeile, "#", 12));
			}
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
	@Override
	public String GetSaveInfostring()
	{
		return "symbol#startdatum#enddatum#breakdate#dummy#anzwerte#gesvolumen#steigfaktor#boerse#lastload";
	}

	@Override
	public String toString()
	{
		return (symbol + "#" + startdatum + "#" + enddatum + "#" + breakdate
				+ "#" + dummy + "#" + anzwerte + "#" + gesvolumen + "#" + steigfaktor+"#"+boerse+"#"+lastload);
	}

	public int getThreadid()
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

	public String getStartdatum()
	{
		return startdatum;
	}

	public void setStartdatum(String startdatum)
	{
		this.startdatum = startdatum;
	}

	public String getEnddatum()
	{
		return enddatum;
	}

	public void setEnddatum(String enddatum)
	{
		this.enddatum = enddatum;
	}

	public String getBreakdate()
	{
		return breakdate;
	}

	public void setBreakdate(String breakdate)
	{
		this.breakdate = breakdate;
	}

	public String getDummy()
	{
		return dummy;
	}

	public void setDummy(String dummy)
	{
		this.dummy = dummy;
	}

	public int getAnzwerte()
	{
		return anzwerte;
	}

	public void setAnzwerte(int anzwerte)
	{
		this.anzwerte = anzwerte;
	}

	public int getGesvolumen()
	{
		return gesvolumen;
	}

	public void setGesvolumen(int gesvolumen)
	{
		this.gesvolumen = gesvolumen;
	}

	public int getSteigfaktor()
	{
		return steigfaktor;
	}

	public void setSteigfaktor(int steigfaktor)
	{
		this.steigfaktor = steigfaktor;
	}

	public String getLastload()
	{
		return lastload;
	}

	public void setLastload(String lastload)
	{
		this.lastload = lastload;
	}

	public String getBoerse()
	{
		return boerse;
	}

	public void setBoerse(String boerse)
	{
		this.boerse = boerse;
	}

	public String getKurs(String date)
	{
		return null;
	}
	
	
}
