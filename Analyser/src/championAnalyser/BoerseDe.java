package championAnalyser;

import hilfsklasse.Inf;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.util.ArrayList;

import mainPackage.GC;

import org.eclipse.swt.widgets.Table;

import stores.AktDB;

public class BoerseDe
{
	// Hier ist der Speicher der Seite
	// Das Börsenblatt besteht aus einer liste von Championaktien
	private ArrayList<Championaktie> championliste = new ArrayList<Championaktie>();

	static private BufferedReader inf_p = null;
	private String textmem_glob = "";
	public String fnamefull=null;
	
	public String getDatum()
	{
		return fnamefull.substring(fnamefull.lastIndexOf("\\"),fnamefull.indexOf("_"));
	}

	public BoerseDe(String fnam,AktDB aktdb)
	{
		// hier wird das Blatt in den Speicher eingelesen
		readBoersedeBlatt(fnam);
		fnamefull=new String(GC.textzielbase+"\\BoerseDe\\"+fnam);
		
		
		if(textmem_glob.contains("64 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,84 4,79 1,71 0,25 0,42 11 119750 Aufwärtstrend 24.11.10 120500 1% halten")==true)
			textmem_glob=textmem_glob.replace("64 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,84 4,79 1,71 0,25 0,42 11 119750 Aufwärtstrend 24.11.10 120500 1% halten", "64 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,84 4,79 1,71 0,25 0,42 11 119 750 Aufwärtstrend 24.11.10 120500 1% halten");
		
		if(textmem_glob.contains("68 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,83 4,75 1,71 0,24 0,41 12 118890 Aufwärtstrend 24.11.10 120500 0% halten")==true)
			textmem_glob=textmem_glob.replace("68 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,83 4,75 1,71 0,24 0,41 12 118890 Aufwärtstrend 24.11.10 120500 0% halten", "68 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,83 4,75 1,71 0,24 0,41 12 118 890 Aufwärtstrend 24.11.10 120 500 0% halten");
		
		if(textmem_glob.contains("66 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,84 4,97 1,70 0,23 0,39 13  121270 122300 Trend-Bruch 13.04.2011121230 0% keine Käufe")==true)
			textmem_glob=textmem_glob.replace("66 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,84 4,97 1,70 0,23 0,39 13  121270 122300 Trend-Bruch 13.04.2011121230 0% keine Käufe", "66 Berkshire Hathaway 854075 Diversifizierte Holding ** USD 6% 0,84 4,97 1,70 0,23 0,39 13 121270 122300 Trend-Bruch 13.04.2011 121 230 0% keine Käufe");
		
		if(textmem_glob.contains("1 Stericycle 902518 Umwelt ***** USD 21% 0,93 19,67 1,36 0,00 0,00 14051 87,38 71 Aufwärtstrend 16.02.201052 67% halten, ATH")==true)
			textmem_glob=textmem_glob.replace("1 Stericycle 902518 Umwelt ***** USD 21% 0,93 19,67 1,36 0,00 0,00 14051 87,38 71 Aufwärtstrend 16.02.201052 67% halten, ATH", "1 Stericycle 902518 Umwelt ***** USD 21% 0,93 19,67 1,36 0,00 0,00 14051 87,38 71 Aufwärtstrend 16.02.2010 52 67% halten, ATH");

		
	
	
			
		
		//plausi
		//"damals Ergebnis Strategie" muss 3 mal vorkommen
		if(textmem_glob.contains("100 CHAMPIONS-AKTIEN")==false)
		{
			Tracer.WriteTrace(10, "Error: fnam<"+fnam+"> ist kein BoerseDeChampionblatt");
			return;
		}
		//anpassung
		//"damals Ergebnis Strategie " -> "damals Ergebnis Strategie"
		textmem_glob=textmem_glob.replaceAll("damalsErgebnis Strategie", "damals Ergebnis Strategie");
		textmem_glob=textmem_glob.replaceAll("damals Ergebnis Strategie ", "damals Ergebnis Strategie");
		textmem_glob=textmem_glob.replaceAll("Stop-Buy Limit", "Stop-Buy-Limit");
		
		// hier wird die Tabelle aufgebaut
		baueChampionUebersicht(aktdb);

	}

	private int calcFolgezaehlerpos(int endcounter)
	{
		// 1 Eldorado Gold 892560 Energie & Rohstoffe **** CAD 40% 0,93 36,70
		// 3,56 0,02 0,07 491 20,53 17 Aufw rtstrend 01.08.11 16 25% halten "DELIMITER" 2
		// McDonald's 856958 Lebensmittel

		// Hier wird die exakte position der nächsten zeile anhand des
		// delemiters ermittelt
		// delimiter =GC.delimiter
		
		
		String substr = textmem_glob.substring(70,180);
		// position des Schlüsselwortes an dem man das Ende identifizieren kann
		int pos = 0;

		if (substr.contains(GC.delimiter) == true)
		{
			pos = substr.indexOf(GC.delimiter) + GC.delimiter.length();
		}
		else
			Tracer.WriteTrace(10, "Error: kein Enddelimter gefunden in substr<"+substr+">");

		// Überprüfung der positon der folge zahl
		// befindet sich also an dieser Position die folgezahl endcounter
		// Folgezahlstring ermitteln

		// substring ist der string der folgezeile
		substr = substr.substring(pos);

		return pos+70;
	}

	private String takeNextChampLine(int erwcounter)
	{
		// die funktion extrahier genau eine Zeile der Championtabelle
		// damit die gleiche zeile nicht nochmal wiedergefunden wird wird
		// textmem_glob entsprechend
		// angepasst, also der teil wird gelöscht
		
		// suche den aktcounter, der muss am anfang sein
		String z = textmem_glob.substring(0, textmem_glob.indexOf(" "));
		z=z.replace(GC.delimiter, "");
		
		//hole den Zeilencounter
		int istcount = new Integer(z);

		if (erwcounter != istcount)
		{
			//gehe weiter bis zum nächsten block
			gehe_anfang_Tabellennummerierung();
			z = textmem_glob.substring(0, textmem_glob.indexOf(" "));
			z=z.replace(GC.delimiter, "");
			istcount = new Integer(z);
		}
		
		if(erwcounter!=istcount)
			Tracer.WriteTrace(10, "Error: sequenzenfehler konnte aktcounter<"
					+ erwcounter + "> nicht finden in <"
					+ textmem_glob.substring(0, 100) + ">");

		// suche den counter +1, der muss am ende der zeile sein
		istcount++;

		// ermittle die position wo sich der folgezaehler befindet
		int folgepos = calcFolgezaehlerpos(istcount);
		// jetzt die Zeile kopieren
		String line = new String(textmem_glob.substring(0, folgepos));

		// Lösche die Zextrahierte zeile aus dem gesammtspeicher
		textmem_glob = textmem_glob.substring(folgepos);
		return line;
	}

	private void gehe_anfang_Tabellennummerierung()
	{
		//Geht bis zum Anfang der nächsten Tabellennummerrierung
		
		if(textmem_glob.contains("damals Ergebnis Strategie")==true)
		{
			textmem_glob = textmem_glob.substring(textmem_glob
				.indexOf("damals Ergebnis Strategie") + 25);
		}
		else
		if(textmem_glob.contains("Ergeb-@@@@@nis Strategie")==true)
		{
			textmem_glob = textmem_glob.substring(textmem_glob
					.indexOf("Ergeb-@@@@@nis Strategie") + 24);
		}
		return ;
	}
	
	private void baueChampionUebersicht(AktDB aktdb)
	{
		int counter = 1;

		
		gehe_anfang_Tabellennummerierung();
		
		while (counter <= 100)
		{
			String line = takeNextChampLine(counter);
			line=line.replace("@@@@@", "");
			System.out.println("line gefunden<"+line+">");
			counter++;

			Championaktie ca = new Championaktie(line,aktdb);
			championliste.add(ca);
		}
	}

	private void readBoersedeBlatt(String fnam)
	{
		// hier wird das Börsenblatt geladen
		// es gibt ne fertige funktion die das in den speicher liesst
		Inf inf = new Inf();
		inf.setFilename(GC.textzielbase+"\\BoerseDe\\"+fnam);
		textmem_glob = inf.readMemFileDelimter(999999999);
		inf.close();
		
	}

	public void setFilter()
	{

	}

	public Table genDefaultTable()
	{
		// Hier wird die Standarttabelle für eine Übersicht aufgebaut

		return null;
	}
	public int getAnzLines()
	{
		return championliste.size();
	}
	public Championaktie getChampionaktie(int index)
	{
		return championliste.get(index);
	}

	public Championaktie getChampionaktieWkn(String wkn)
	{
		int anz=championliste.size();
		for(int i=0; i<anz; i++)
		{
			Championaktie ca = championliste.get(i);
			String wknx=ca.getWkn();
			if(wknx.equalsIgnoreCase(wkn)==true)
				return ca;
		}
		return null;
		
	}
	
	public String getFnameFull()
	{
		return fnamefull;
	}

	public void setFnameFull(String fname)
	{
		this.fnamefull = fname;
	}
	
}
