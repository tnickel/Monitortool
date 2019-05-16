package statistikobjekte;

import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.text.DecimalFormat;

public class Kursbewert
{
	// Zähler für einen Part (Aktienwert)
	private int PartOkCount = 0;
	private int PartBadCount = 0;
	private int PartNullCount = 0;
	private int PartKursanzahl = 0;

	// Gesammtzähler
	private int GesOkCount = 0;
	private int GesBadCount = 0;
	private int GesNullCount = 0;
	private int GesKursanzahl = 0;

	
	private Inf infv = new Inf();

	private int pos_g = 0;

	public Kursbewert(String fnam)
	{
		infv.setFilename(fnam);
		
	}
	public void SetStartindex(int index)
	{
		pos_g=index;
	}
	
	public void SetNeuerThread()
	{
		// Löscht den PartSpeicher(Aktienspeicher für einen Wert)
		PartOkCount = 0;
		PartBadCount = 0;
		PartNullCount = 0;
		PartKursanzahl = 0;
	}

	public void UebernehmeThreadcounter()
	{
		// Übernimmt den Partwert in den Gesammtspeicher
		GesOkCount = GesOkCount + PartOkCount;
		GesBadCount = GesBadCount + PartBadCount;
		GesNullCount = GesNullCount + PartNullCount;
		GesKursanzahl = GesKursanzahl + PartKursanzahl;
		SetNeuerThread();
	}

	public void Bewerte(int ok, int bad, int neutral)
	{
		// Das posting wird hier bewertet
		// wenn der kurs gut ist, => ok
		// wenn fehlerhafter kurs , => bad
		// bei einem Kurs mt value 0 => neutral

		int anz;
		anz = ok + bad + neutral;
		if (anz > 1)
			Tracer.WriteTrace(10, "Error: internal anzahl muss 1 sein anz<"
					+ anz + ">");

		PartOkCount = PartOkCount + ok;
		PartBadCount = PartBadCount + bad;
		PartNullCount = PartNullCount + neutral;
		PartKursanzahl = PartKursanzahl + anz;
	}

	public void GibZwischenstandAus(String lastsymbol, String lastthreadname,
			int lastmasterid)
	{
		String pref = "";
		if (PartBadCount > 0)
		{
			pref = "*****";
		}

		infv.writezeile(pref + "date<" + Tools.get_aktdatetime_str() + "> pos<"
				+ pos_g + "> symb<" + lastsymbol + "> mid<" + lastmasterid
				+ "> tname<" + lastthreadname + "> ok/bad/null<" + PartOkCount
				+ "/" + PartBadCount + "/" + PartNullCount
				+ ">gesammt %ok/%bad/%null<" + CalcGesprozString() + ">");

		pos_g=pos_g+1;
		UebernehmeThreadcounter();
		SetNeuerThread();
	}

	private String CalcGesprozString()
	{
		float prozok=0, prozbad=0,proznull=0;
		DecimalFormat f = new DecimalFormat("0.00");
		
		// Hier wird der Prozentsatz der Kursdaten über alle Kurse
		// ermittelt
		float gok = 	(GesOkCount + PartOkCount);
		float gbad = 	(GesBadCount + PartBadCount);
		float gnull = 	(GesNullCount + PartNullCount);
		float ganz = 	(GesKursanzahl + PartKursanzahl);

		int a=GesOkCount + PartOkCount+ GesBadCount + PartBadCount+GesNullCount + PartNullCount;
		int b=GesKursanzahl + PartKursanzahl;
		
		if(a != b)
			Tracer.WriteTrace(10, "Error: plausi<"+a+">!=<"+b+">");

		if(ganz!=0)
		{
			prozok = (100 / ganz) * gok;
			prozbad = (100 / ganz) * gbad;
			proznull = (100 / ganz) * gnull;
		}
		else
		{
			prozok=0;
			prozbad=0;
			proznull=0;
		}
		String s = new String(f.format(prozok) + "/" + f.format(prozbad) + "/"
				+ f.format(proznull));
		return s;
	}
}
