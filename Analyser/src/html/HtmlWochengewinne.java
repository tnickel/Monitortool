package html;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

public class HtmlWochengewinne extends HtmlTabelle
{
	public HtmlWochengewinne(String nam)
	{
		this.setfilename(nam);
	}

	public void closeFile()
	{
		this.cFile();
	}

	public void addKopf(String ueberschrift, String tabellenkopf)
	{
		this.Kopf(ueberschrift);

		// Kopfzeile
		addKopfleiste(tabellenkopf);
	}



	public void addZeile(String zeile)
	{
		// "Name@SYMB@Tid@Kaufdat@Kurs@anz@Summe@Verkaufdat@Kurs@anz@Summe@Gewinn@GesGewinn");
		// Name         @SYMB@Tid    @Kaufdat @Kurs@anz      @Summe    @Verkaufdat       @Kurs@anz      @Summe    @Gewinn  @GesGewinn
		// SMT SCHARF AG@S4A @1123805@09.11.09@9.14@187940.58@1717777.0@16.11.09 14:13:42@9.2 @187940.58@1729053.2@11276.25@11276.25

	
		
		int pos=1;
		try
		{
			int anz = SG.countZeichen(zeile, "@");
			if (anz != 12)
				Tracer.WriteTrace(10, "Internal Error: HtmlWochengewinne<"
						+ zeile + "> muss 12 elemente haben anz <" + anz + ">");

			// Zeilenanfang
			sz("<tr>");

			for(pos=1; pos<=anz+1; pos++)
			{
				String str=new String(SG.nteilstring(zeile, "@", pos));
				buildZeile("black",str);
			}

			// Zeilenende
			sz("</tr>");
		}

		catch (StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(20, "zeile<" + zeile + ">");
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
