package kurse;

import hilfsklasse.DateExcecption;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.io.File;
import java.util.HashMap;

import mainPackage.GC;
import objects.AktDbObj;
import objects.ThreadDbObj;

public class LadeKurs
{ // Diese Klasse ist für das Laden von Kursen zuständig

	static private String filename = null;
	static private String urlstr = null;
	static private String ladestatus = null;
	static private HashMap<String, String> hmap_g = new HashMap<String, String>();
	private static int tdblimit = GC.TDBLIMIT;

	public String GetLadestatus()
	{
		// hier wird der börsenplatz als ladestatus zurückgemeldet
		// N = Nasdaq, F= Frankfurt
		// anderes Symbol
		// 1 = bereits vorhanden
		// 99=error

		return ladestatus;
	}

	public LadeKurs()
	{
		hmap_g.put("F",".F");
		hmap_g.put("NAS", "");
		hmap_g.put("ST", ".ST");
		hmap_g.put("OB",".OB");
		hmap_g.put("DE",".DE");
	}

	public boolean LoadYahooKurs(String filekuerzel, String symb, String börse,
			DownloadManager dm)
	{
		int year = 0, month = 0, day = 0;
		String aktdate = Tools.get_aktdatetime_str();
		try
		{
			year = Tools.get_year_int(aktdate);
			month = Tools.get_month_int(aktdate);
			day = Tools.get_day_int(aktdate);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String file = GC.rootpath + "\\db\\kurse\\" + filekuerzel + ".csv";
		/*
		 * http://ichart.yahoo.com/table.csv?s=hua.F&d=3&e=22&f=2010&g=d&a=1&b=1&c=2000&ignore=.csv
		 * 
		 * urlstr = "http://ichart.yahoo.com/table.csv?s=" + symb + börse +
		 * "&d=3&e=22&f=2009&g=d&a=1&b=1&c=2000&ignore=.csv";
		 * 
		 * ^GDAXI => läd dxhttp://ichart.yahoo.com/table.csv?s=%
		 * 5EGDAXI&d=3&e=22&f=2009&g=d&a=10&b=26&c=1990&ignore=.csv
		 * 
		 * der dax wird so geladen, falls das symbol dax auftaucht wird nicht
		 * s=DAX.F sondern s=%5EGDAXI gesetzt undgeladen !!!!
		 * 
		 * ***http://ichart.yahoo.com/table.csv?s=%
		 * 5EGDAXI&d=3&e=22&f=2009&g=d&a=10&b=26&c=1990&ignore=.csv
		 */

		urlstr = "http://ichart.yahoo.com/table.csv?s=" + symb + börse + "&d="
				+ month + "&e=" + day + "&f=" + year
				+ "&g=d&a=1&b=1&c=2000&ignore=.csv";

		System.out.println("Lade Kurse für Symbol <" + symb + "> <" + börse
				+ ">");
		dm.DownloadHtmlPage(urlstr, file, 1, 50000, 1, 1, 0);

		if (FileAccess.FileLength(file) > 0) // file wurde geladen
			return true; // geladen
		else
			return false; // ladefehler
	}
		
	public boolean LoadYahooKursSchnell(String filekuerzel, String symb, String börse,
			DownloadManager dm)
	{
		
		//gibt eine Böse vor, dies Kuerzel bestimmt die börse
		String kuerzel=hmap_g.get(börse);
			
		if((börse==null)||(kuerzel==null))
		{
			Tracer.WriteTrace(20, "Error: Kursladefehler symb<"+symb+"> börse<"+börse+"> kürzel<"+kuerzel+"> es wird kein Kurs geladen !!");
			return false;
		}
		
		int year = 0, month = 0, day = 0;
		String aktdate = Tools.get_aktdatetime_str();
		try
		{
			year = Tools.get_year_int(aktdate);
			month = Tools.get_month_int(aktdate);
			day = Tools.get_day_int(aktdate);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		String file = GC.rootpath + "\\db\\kurse\\" + filekuerzel + ".csv";
		/*
		 * urlstr = "http://ichart.yahoo.com/table.csv?s=" + symb + börse +
		 * "&d=3&e=22&f=2009&g=d&a=1&b=1&c=2000&ignore=.csv";
		 * 
		 * ^GDAXI => läd dxhttp://ichart.yahoo.com/table.csv?s=%
		 * 5EGDAXI&d=3&e=22&f=2009&g=d&a=10&b=26&c=1990&ignore=.csv
		 * 
		 * der dax wird so geladen, falls das symbol dax auftaucht wird nicht
		 * s=DAX.F sondern s=%5EGDAXI gesetzt undgeladen !!!!
		 * 
		 * ***http://ichart.yahoo.com/table.csv?s=%
		 * 5EGDAXI&d=3&e=22&f=2009&g=d&a=10&b=26&c=1990&ignore=.csv
		 */
		
			
		
		urlstr = "http://ichart.yahoo.com/table.csv?s=" + symb + kuerzel + "&d="
				+ month + "&e=" + day + "&f=" + year
				+ "&g=d&a=1&b=1&c=2000&ignore=.csv";

		dm.DownloadHtmlPage(urlstr, file, 1, 50000, 0, 1, 0);
		return true;
	}

	public void LoadYahooKursFile(String symb, String börse,
			DownloadManager dm,String file)
	{
		//gibt eine Böse vor, dies Kuerzel bestimmt die börse
		String kuerzel=hmap_g.get(börse);
		//läd einen Yahoo-Kurs in ein bestimmtes file 
		//läd asynchron
		
		int year = 0, month = 0, day = 0;
		String aktdate = Tools.get_aktdatetime_str();
		try
		{
			year = Tools.get_year_int(aktdate);
			month = Tools.get_month_int(aktdate);
			day = Tools.get_day_int(aktdate);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: internal date <"+aktdate+">");
		}
		
		urlstr = "http://ichart.yahoo.com/table.csv?s=" + symb + kuerzel + "&d="
				+ month + "&e=" + day + "&f=" + year
				+ "&g=d&a=1&b=1&c=2000&ignore=.csv";

		dm.DownloadHtmlPage(urlstr, file, 1, 50000, 0, 1, 0);
		
	}

	
	
	public boolean SucheBoerseLadeKurs(AktDbObj aktobj, DownloadManager dm)
	{
		// bei diesem Kurs ist noch keine Boerse bekannt, es wird also alles
		// durchgegangen
		// falls boerse bekannt
		Inf inf = new Inf();

		if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), ".F", dm) == true)
		{
			// frankfurt
			aktobj.setStatus(1);
			aktobj.setBoerse("F");
			return true;
		}

		if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), "", dm) == true)
		{
			// Nasdaq
			aktobj.setStatus(1);
			aktobj.setBoerse("NAS");
			return true;
		}

		if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), ".ST", dm) == true)
		{
			// Stuttgart
			aktobj.setStatus(1);
			aktobj.setBoerse("ST");
			return true;
		}
		if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), ".OB", dm) == true)
		{
			// Stuttgart
			aktobj.setStatus(1);
			aktobj.setBoerse("OB");
			return true;
		}
		if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), ".DE", dm) == true)
		{
			// deutschland
			aktobj.setStatus(1);
			aktobj.setBoerse("DE");
			return true;
		}
		// kein Kurs gefunden
		aktobj.setStatus(55);
		aktobj.setBoerse("NOKURS");
		Tracer.WriteTrace(20, "Warning: für symbol <" + aktobj.getSymbol()
				+ "> konnte keine Börse gefunden werden");
		inf.appendzeile(GC.rootpath + "\\db\\reporting\\symbolfehler.txt",
				"W: für Symbol <" + aktobj.getSymbol()
						+ ">konnte kein Kurs gefunden werden", true);
		inf.appendzeile(GC.rootpath + "\\db\\reporting\\KeinKurs.txt",
				"W: für Symbol <" + aktobj.getSymbol()
						+ ">konnte kein Kurs gefunden werden", true);
		return false; // ladefehler
	}
	
	public boolean SucheBoerseLadeKurs2(AktDbObj aktobj, DownloadManager dm,ThreadDbObj tdbo)
	{
		// bei diesem Kurs ist noch keine Boerse bekannt, es wird also alles
		// durchgegangen
		// falls boerse bekannt
		Inf inf = new Inf();
		Boolean status=false;
		
		
		String boer=null,file=null,zfile=null;
		String[] boerfeld={ "F","","ST","OB","DE" };
	
		String symb=aktobj.getSymbol();
		int n=boerfeld.length;	
		int maxlen=0, maxindex=0;

		
	
		//Lade alle Kurse parallel
		for(int index=0; index <n; index ++)
		{
			file=GC.rootpath+"\\tmp\\Kurse\\"+symb+"_"+boerfeld[index]+".csv";

			File ftmp=new File(file);

			if(ftmp.exists()==false)
				LoadYahooKursFile(symb,boerfeld[index], dm,file);
		}
		
		dm.waitEnd();
		
		//Werte aus welches die richtige Boerse ist
		//ermittele die längen
		for(int index=0; index <n; index ++)
		{
			file=GC.rootpath+"\\tmp\\kurse\\"+symb+"_"+boerfeld[index]+".csv";
			
			int len=FileAccess.FileLength(file);
			if(len>maxlen)
			{
				maxlen=len;
				maxindex=index;
			}
		}
		
		//Auswertung
		if(maxlen>500)
		{
			//Kurs und Boerse gefunden
			
			//Kopiere den maximalen kurs passend
			file=GC.rootpath+"\\tmp\\kurse\\"+symb+"_"+boerfeld[maxindex]+".csv";
			zfile=GC.rootpath+"\\db\\kurse\\"+symb+".csv";
		
			//Neue Kurse kopieren
			FileAccess.copyFile2_dep(file, zfile);
		
			aktobj.setStatus(1);
			aktobj.setBoerse(boerfeld[maxindex]);
			status = true;
		}
		else
		{
			// kein Kurs gefunden
			aktobj.setStatus(55);
			aktobj.setBoerse("NOKURS");
			Tracer.WriteTrace(20, "Warning: für symbol <" + aktobj.getSymbol()
					+ "> konnte keine Börse gefunden werden");
			inf.appendzeile(GC.rootpath + "\\db\\reporting\\symbolfehler.txt",
					"W: für Symbol <" + aktobj.getSymbol()
							+ ">konnte kein Kurs gefunden werden", true);
			inf.appendzeile(GC.rootpath + "\\db\\reporting\\KeinKurs.txt",
					"W: für Symbol <" + aktobj.getSymbol()
							+ ">konnte kein Kurs gefunden werden", true);
			status = false;
		}
		
		//Bereinignen, tmp wieder löschen
		/*for(int index=0; index <n; index ++)
		{
			file=GC.rootpath+"\\tmp\\kurse\\"+symb+"_"+boerfeld[index]+".csv";
			FileAccess.FileDelete(file);
		}*/
		return status;
	}
	
	public boolean LadeKurs(AktDbObj aktobj, ThreadDbObj tdbo,
			DownloadManager dm, int threshold) throws InterruptedException
	{ // hier wird für ein einzelner Kurs für ein Symbol
		// geladen
		// loadmode ==2 => mache update, also lade immer
		String file = GC.rootpath + "\\db\\kurse\\" + aktobj.getSymbol()
				+ ".csv";
		// ist doch vorhanden lade nix

		// Prüfe falls File da und zu neu
		if (FileAccess.FileAvailable(file) == true)
			if (FileAccess.CheckIsFileOlderHours(file, threshold) == false)
			{
				aktobj.setStatus(1);
				return true; // alles ok
			}

		// falls boerse bekannt
		if ((aktobj.getBoerse().contains("?") == false)
				&& (aktobj.getBoerse().contains("NOKURS") == false))
		{

			if (aktobj.getBoerse().equals("F"))
			{
				if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), ".F",
						dm) == true)
				{
					// frankfurt
					aktobj.setStatus(1);
					aktobj.setBoerse("F");
					return true;
				}
			}
			if (aktobj.getBoerse().equals("NAS"))
			{
				if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(), "",
						dm) == true)
				{
					// Nasdaq
					aktobj.setStatus(1);
					aktobj.setBoerse("NAS");
					return true;
				}
			}
			if (aktobj.getBoerse().equals("ST"))
			{
				if (LoadYahooKurs(aktobj.getSymbol(), aktobj.getSymbol(),
						".ST", dm) == true)
				{
					// Stuttgart
					aktobj.setStatus(1);
					aktobj.setBoerse("ST");
					return true;
				}
			}
		} else
		{
			// falls keine börse bekannt dann suche
			if (SucheBoerseLadeKurs2(aktobj, dm,tdbo) == true)
				return true;

		}
		// kein Kurs gefunden
		aktobj.setStatus(55);
		aktobj.setBoerse("NOKURS");
		return false; // ladefehler
	}
public boolean LadeZertifikat(String name)
{
	//http://www.wallstreet-online.de/informer/?informer_searchkey=Suche+nach+k	
	return true;
}
}
