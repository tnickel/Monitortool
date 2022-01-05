package data;

import java.io.File;

import filter.Tradefilter;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;
import swingHilfsfenster.ProgressBarDisplay;

public class TableViewBasic
{
	
	public TableViewBasic()
	{
	}
	
	public String readTradesAutoCreator(Tradeliste tl, String dirnam, boolean nocanceledflag, boolean showopenorders,
			boolean normflag, Metaconfig mc, Tradefilter tf)
	{
		
		if (mc.getAutocreatorpathmode() == 1)
			readAllFilesAC(tl, dirnam, nocanceledflag, showopenorders, normflag, mc, tf);
		else if (mc.getAutocreatorpathmode() == 3)
			readOneAcDir(new File(mc.getAutocreatorpath()),tl, nocanceledflag, showopenorders, normflag, mc, tf);
		else if (mc.getAutocreatorpathmode() == 2)
			readFilesACNewest(tl, dirnam, nocanceledflag, showopenorders, normflag, mc, tf);
		
		return null;
	}
	
	private void readAllFilesAC(Tradeliste tl, String dirnam, boolean nocanceledflag, boolean showopenorders,
			boolean normflag, Metaconfig mc, Tradefilter tf)
	{
		// liest alle file in allen directorys, fall 1 (default) und fall2 (bestimmten
		// pfad)
		File[] directorys = new File(dirnam).listFiles();
		
		// go through the directorys
		if (directorys == null)
			return;
		int anzdir = directorys.length;
		
		// read all directorys
		for (int i = 0; i < anzdir; i++)
		{
			File subdirnam = directorys[i];
			if (subdirnam.isDirectory() == false)
				continue;
			if (subdirnam.getAbsolutePath().endsWith("_") == true)
				continue;
			
			// read all Files in dir i
			File[] files = subdirnam.listFiles();
			int anzf = files.length;
			ProgressBarDisplay pdisp = new ProgressBarDisplay(
					"Load Autocreator Files <" + subdirnam.getAbsolutePath() + ">", 0, anzf);
			for (int j = 0; j < anzf; j++)
			{
				pdisp.update(j);
				File fnamf = files[j];
				readOneAcFile(fnamf, tl, nocanceledflag, showopenorders, normflag, mc, tf);
			}
			pdisp.end();
			
		}
	}
	
	private void readFilesACNewest(Tradeliste tl, String dirnam, boolean nocanceledflag, boolean showopenorders,
			boolean normflag, Metaconfig mc, Tradefilter tf)
	{
		// 1)erst das neuste directory ermitteln un dann alle files dort ermitteln
		long aktdate = 0;
		long newestdate = 0;
		File newestdir = new File("");
		
		// gehe durch dalle directorys und schaue was am neusten ist
		File[] directorys = new File(dirnam).listFiles();
		
		// go through the directorys
		if (directorys == null)
			return;
		
		int anzdir = directorys.length;
		// gehe durch dalle directorys und ermittle das neuste
		for (int i = 0; i < anzdir; i++)
		{
			File subdirnam = directorys[i];
			if (subdirnam.isDirectory() == false)
				continue;
			if (subdirnam.getAbsolutePath().endsWith("_") == true)
				continue;
			// falls ein neueres verzeichniss gefunden wurde
			if (subdirnam.lastModified() > newestdate)
			{
				// ein neueres Verzeichniss gefunden
				newestdir = subdirnam;
				newestdate = subdirnam.lastModified();
			}
		}
		
		//2) das neuste directory wurde gefunden und jetzt lese alle files
		// read all Files in dir neustedir
		readOneAcDir(newestdir,  tl,  nocanceledflag,  showopenorders, normflag,  mc,  tf);
		
	}
	
	private void readOneAcDir(File dir, Tradeliste tl, boolean nocanceledflag, boolean showopenorders,
	boolean normflag, Metaconfig mc, Tradefilter tf)
	{
		//hier werden alle files für ein bestimmtes directory gelesen
		
		File[] files = dir.listFiles();
		int anzf = files.length;

		if(anzf<3)
		{
			Tracer.WriteTrace(10, "Error: to less files in <"+dir.getAbsolutePath()+">");
			return;
		}
		ProgressBarDisplay pdisp = new ProgressBarDisplay(
				"Load Autocreator Files <" + dir.getAbsolutePath() + ">", 0, anzf);
		for (int j = 0; j < anzf; j++)
		{
			pdisp.update(j);
			File fnamf = files[j];
			if(fnamf.getAbsolutePath().contains(".csv"))
				readOneAcFile(fnamf, tl, nocanceledflag, showopenorders, normflag, mc, tf);
			
		}
		pdisp.end();
	}
	
	
	private boolean readOneAcFile(File fnamf, Tradeliste tl, boolean nocanceledflag, boolean showopenorders,
			boolean normflag, Metaconfig mc, Tradefilter tf)
	{
		//hier wird nur ein bestimmtes file gelesen
		if (fnamf.isDirectory() == true)
			return false;
		if ((fnamf.getAbsolutePath().contains("_stats") == true) || (fnamf.getAbsolutePath().contains("_set") == true))
			return false;
		
		if (fnamf.length() < 2)
			return false;
		
		String fnamcsv = fnamf.getAbsolutePath();
		readTradesFile(tl, fnamcsv, nocanceledflag, showopenorders, normflag, mc, tf);
		return true;
	}
	
	public String readTradesFile(Tradeliste tl, String fnam, boolean nocanceledflag, boolean showopenorders,
			boolean normflag, Metaconfig mc, Tradefilter tf)
	{
		// tl= die tradeliste wird aufgebaut
		// fnam= das file wird eingelesen
		// nocanceledflag= canceled trades werden nicth gelesen
		// shoopenorders= falls true dann werden die open orders gelesen
		// normflag= falls true dann wird auf 0.1 lotsize genormt
		// mc=metaconfig die verwendet wird
		// tf=tradefilter wird verwendet falls auch alte trades angezeigt werden
		
		String maxdate = "";
		int mindist = 50000;
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String zeil = null;
		int zeilcount = -1;
		int routencount = 0;
		int lastroutencount = 0;
		
		Tracer.WriteTrace(20, "I:read brokerfile<" + fnam + ">");
		
		while ((zeil = inf.readZeile()) != null)
		{
			zeilcount++;
			// System.out.println(zeil);
			
			if (zeil.length() > 200)
			{
				Tracer.WriteTrace(20, "E:zeile<" + zeilcount + "> in File<" + fnam + "> zu lang, bitte löschen");
				continue;
			}
			if (zeil.contains("@") == true)
				continue;
			
			if (zeil.contains("Deposit") == true)
				continue;
			if (zeil.contains("balance") == true)
				continue;
			if (zeil.contains("Transfer") == true)
				continue;
			if (zeil.contains("close hedge by #"))
				zeil = zeil.replace("close hedge by #", "close hedge by *");
			
			// open orders überlesen
			if ((showopenorders == false) && (zeil.contains("2050.01.01 00:00:00")))
				continue;
			
			if (zeil.contains("cancelled"))
				continue;
			
			// schmutzzeile rausfiltern
			if (zeil.contains("#to#") == true)
				zeil = zeil.replace("#to#", "#");
			if (zeil.contains("#to #") == true)
				zeil = zeil.replace("#to #", "#");
			if (zeil.contains("#from #") == true)
				zeil = zeil.replace("#from #", "#");
			if (zeil.contains("#from#") == true)
				zeil = zeil.replace("#from#", "#");
			
			{// plausicheck
				routencount = SG.countZeichen(zeil, "#");
				
				// init routencount
				if (lastroutencount == 0)
					lastroutencount = routencount;
					
				// plausi:falls sich der routenzaehler unterscheidet dann stimmt
				// was nicht
				if (routencount != lastroutencount)
				{
					
					lastroutencount = routencount;
					Tracer.WriteTrace(20, "W:history.txt <" + fnam + "> defect, I delete this file file<"
							+ (zeilcount + 1) + "> <" + zeil + ">");
					File fnamf = new File(fnam);
					fnamf.delete();
					
					continue;
				}
			}
			Trade tr = new Trade(zeil, mc.getBrokername(), normflag, 0);
			
			// falls das kein sauberer trade ist
			if (tr.getMagic() < 0)
				continue;
			
			// hier wird geschaut ob das anfangsdatum gefiltert wird
			if (tf.checkTradeIsToOld(tr) == true)
				continue;
				
			// es untersucht seit wann der Account aktiv ist
			// hierzu wird das älteste Datum der Datei gesucht
			
			// nur die neuen aufnehmen
			if (zeil.contains(("Deposit")) == false)
			{
				
				tl.addTradeElem(tr);
				
				// Magiclistenbearbeitung, es wird überprüft ob ersatzmagics
				// vorhanden sind
				if (mc.isMagiclistactive())
					workErsetzungliste(tl, mc, tr);
				
			}
			// suche die kleinste distanz da dies das älteste datum ist
			int dist = Tools.getDateInt("2000.01.01 00:00:00", tr.getOpentime());
			if (dist < mindist)
			{
				// neues älteres datum gefunden, da die dist zum Nullpunkt
				// kleiner ist
				maxdate = tr.getOpentime();
				mindist = dist;
			}
		}
		inf.close();
		return maxdate;
	}
	
	public void workErsetzungliste(Tradeliste tl, Metaconfig mc, Trade tr)
	{
		{
			int ersatzmagic = mc.holeErsetzungsmagic(tr.getMagic());
			if (ersatzmagic >= 0)
			{
				tr.setMagic(ersatzmagic);
				String transnumber = String.valueOf(tr.getTransactionnumber());
				String transsubnumber = transnumber.substring(4);
				tr.setTransactionnumber(SG.get_zahl("99" + transsubnumber));
				tl.addTradeElem(tr);
			}
		}
		
	}
}
