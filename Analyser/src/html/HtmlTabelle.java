package html;

import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.io.BufferedWriter;
import java.io.IOException;

public class HtmlTabelle extends Html
{
	private BufferedWriter ouf_g=null;
	
	// Generiert die HtmlSlideruebersicht in den threadvirtualKonto
	public HtmlTabelle()
	{}
	
	protected void setfilename(String fnam)
	{
		if(FileAccess.FileAvailable0(fnam))
			FileAccess.FileDelete(fnam,1);
		
		ouf_g = FileAccess.WriteFileOpenAppend(fnam);
		
	}
	protected void Kopf(String ueberschrift)
	{
		writezeile("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"");
		writezeile("\"http://www.w3.org/TR/html4/strict.dtd\">");
		writezeile("<html>");
		writezeile("<head>");
		writezeile("<title>Zellenabstand und Zelleninnenabstand in Tabellen</title>");
		writezeile("</head>");
		writezeile("<body>");
		writezeile("<h3>"+ueberschrift+"</h3>");
		writezeile("<p> Sliderpostanz= gibt an wieviele Postings im Slider sind");
		writezeile("<p> mitlRank= ");
		writezeile("<p> +Post= anzahl Postings von usern die gut sind");
		writezeile("<p> -Post= anzahl Postings von usern die schlecht sind");
		writezeile("<p> +Useranz= anzahl guter User (User ist gut wenn Rank <1000)");
		writezeile("<p> -Useranz= anzahl schlechter User (User ist schlecht wenn Rank>25000) ");
		writezeile("<p> baduser= anzahl der User im Slider die nicht mehr bei WO tätig sind");
		writezeile("<p> neueguteUser= anzahl der neuen guten User im Slider (User haben mind. 3 Monate nicht mehr hier im Thread gepostet)");
		writezeile("<p> neueschlechteUser=");
		writezeile("<p> neuebadUser");
		writezeile("<p> SlVal=Sortierkriterium fuer den Slider");
		writezeile("<p> SliderGuete=Wenn >0 dann wird gekauft");
		writezeile("<p> Einige Erklaerungen:____________________________________________________");
		writezeile("<p> Aktname eingegraut(bzw. gruen) = nicht alle Kurse verfuegbar");
		writezeile("<p> Nr rot dargestellt = Hotflag fuer diesen Wert gesetzt");
		writezeile("<p> Sortierkriterium:");
		writezeile("<p> Sliderguete,Hotflag, Postanzahl");
		writezeile("<p> Alle Threads wo 180 Tage nicht mehr gepostet wurde haben prio 8");
		writezeile("<table border=\"8\" cellspacing=\"1\" cellpadding=\"0\">");
		writezeile("<p> Einige Erklaerungen zur prognoseübersicht________________________________");
		
		writezeile("<p> rot: 		prognosealter =0 und NEU");
		writezeile("<p> schwarz: 	prognosealter =0 und BEKANNT");
		writezeile("<p> blau: 		prognosealter >0 also ALT");
	}
	protected void addKopfleiste(String zeile)
	{
		int anz=SG.countZeichen(zeile, "@");
		writezeile("<tr>");
		for(int i=1; i<=anz+1; i++)
		{
			try
			{
				String teil=new String(SG.nteilstring(zeile, "@", i));
				writezeile("<th>"+teil+"</th>");
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Tracer.WriteTrace(10, "error: zeile<"+zeile+"> pos<"+i+">");
			}
		}
		writezeile("/<tr>");
	}
	protected void buildZeile( String farbe, String teilzeile)
	{
		
		if(teilzeile==null)
			teilzeile=new String("?");
		String tmpzeile = "<td><font color=" + farbe + ">";
		tmpzeile = tmpzeile.concat(teilzeile);
		tmpzeile = tmpzeile.concat("</td>");
		writezeile(tmpzeile);
		
	}
	public void addEnde()
	{
		writezeile("</table>");
		writezeile("</body>");
		writezeile("</html>");
		this.cFile();
	}
	protected void sz(String zeile)
	{
		//schreibe zeile (sz)
		writezeile(zeile);
	}
	private void writezeile(String zeile) 
	{
		try
		{
			ouf_g.write(zeile);
			ouf_g.newLine();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void cFile()
	{
		try
		{
			ouf_g.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
