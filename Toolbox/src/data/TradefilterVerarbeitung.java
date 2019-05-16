package data;

import gui.Mbox;
import gui.Viewer;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import work.JToolboxProgressWin;

public class TradefilterVerarbeitung
{
	// Masterklasse
	// hier werden die Resultate des Tradefilters gespeichert
	// wir möchten ja irgendwann eine schöne Tabelle ausgeben
	// dies ist die Masterklasse die den speziellen Tradefilter anwendet

	// hier ist die liste aller filenamen
	private ArrayList<String> stratfileliste = new ArrayList<String>();
	CorelAllResult coreall_glob=new CorelAllResult();
	
	private String firstverzeichniss_g = "";
	private String toolboxrootdir_g=null;

	public TradefilterVerarbeitung()
	{
	}

	void init(String dir_g)
	{
		stratfileliste= new ArrayList<String>();
		coreall_glob=new CorelAllResult();
		
		String hitfile = GlobToolbox.getRootpath() + "\\bin\\hitfile.txt";
		File hitfile_f = new File(hitfile);
		if (hitfile_f.exists())
			hitfile_f.delete();
	}

	private void filterAllStr(String dir_g)
	{
		
		if(stratfileliste.size()>0)
		{
			Tracer.WriteTrace(20, "I: already calculated");
			return;
		}
		int diranz = 0;
		// statistikcounter für die hitsumme
		int hitcountsum = 0;
		toolboxrootdir_g=dir_g;
		
		//lösche das outdir
		delOutdir();
		
		// das erste verzeichniss mit den Stratgien holen
		FileAccess.initFileSystemList(dir_g, 0);
		firstverzeichniss_g = FileAccess.holeFileSystemName();
		diranz = FileAccess.holeFileAnz();

		// dann baue eine Liste mit den Filenamen in dieser ersten liste auf
		FileAccess.initFileSystemList(dir_g + "\\" + firstverzeichniss_g, 1);
		int anzfiles = FileAccess.holeFileAnz();

		//progress slider
		JToolboxProgressWin jp = new JToolboxProgressWin("filter <0/" + anzfiles
								+ ">", 0, (int) anzfiles);
		jp.update(0);
		stratfileliste=new ArrayList<String>();
		//baue die Liste aller filenamen auf
		for (int i = 0; i < anzfiles; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			stratfileliste.add(fnam);
		}

		//gehe durch die stratfileliste und zähle für jede strategie die hitpunkte
		//0 ist die indexmenge die mit allen anderen vergleichen wird
		for (int i = 0; i < anzfiles; i++)
		{
			String fnam = stratfileliste.get(i);
			jp.update(i);
			// 1)Tradespeicher für eine Strategie aufbauen
			// baue den Tradelistenzeitspeicher für einen Strategienamen auf
			// der Tradelistenzeitspeicher beinhaltet alle trades für alle
			// datenserien
			TradelistenZeitSpeicherInit tzeit = new TradelistenZeitSpeicherInit(
					dir_g, fnam);

			// 2)Die Tradeliste für die Strategie aufbauen
			Tradeliste eatl = new Tradeliste(null);
			eatl.initBacktest(dir_g + "\\" + firstverzeichniss_g + "\\" + fnam);

			// anzahl trades für eine Strategie holen
			int tradeanz = eatl.getsize();
			//max hits pro strategie
			coreall_glob.setTradeanz(fnam, tradeanz);
			coreall_glob.setMaxhits(fnam, tradeanz * (2*(diranz-1)));
			for (int t = 0; t < tradeanz; t++)
			{
				// überprüfe jeden Trade der liste 0, ob dieser Trade auch in
				// liste 1 und 2 ist
				Trade trade = eatl.getelem(t);

				for (int hitreihe = 1; hitreihe < diranz; hitreihe++)
				{
					int hitcount = tzeit.getHitcount(hitreihe, trade);
					// speichere das Ergebniss im Coreall-speicher
					Tracer.WriteTrace(20,
							"I:Speichere hits fnam<" + fnam + "> tradenr<" + t
									+ "> opent<" + trade.getOpentime()
									+ "> hitreihe<" + hitreihe + "> hitcount<"
									+ hitcount + ">");
					coreall_glob.addHits(fnam, hitreihe, hitcount);
					hitcountsum = hitcountsum + hitcount;
				}
			}
			// am ende für jedes file die statistik ausgeben
			Tracer.WriteTrace(20, "___I: (" + (i + 1) + "|" + (anzfiles)
					+ ") fnam<" + fnam + "> hitcountsum<" + hitcountsum
					+ ">____");
			hitcountsum = 0;

		}
		System.out.println("fertig");
		jp.end();
		//printResulttable();
		//showResulttable();
		
	}

	private void delOutdir()
	{
		String outdir = toolboxrootdir_g + "\\out";
		File outdir_f = new File(outdir);
		
		if (outdir_f.exists() == true)
		{
			FileAccess.deleteDirectoryContent(outdir_f);
			outdir_f.delete();
		}
	}
	
	  public void copyGoodResultfiles(String rdirnam, float proz)
	{
		
		//hier wird die filterung gemacht
		filterAllStr(rdirnam);
		
		// kopiert die guten strategien in das out verzeichniss
		String outdir = rdirnam + "\\out";
		File outdir_f = new File(outdir);

		delOutdir();
		if (outdir_f.exists() == false)
			outdir_f.mkdir();
		
		int copyzaehler=0;
		// copy strategy with least 80% equal trades
		int anzfiles = stratfileliste.size();
		for (int i = 0; i < anzfiles; i++)
		{
			String stratnam = stratfileliste.get(i);
			float hitproz = Float.valueOf(coreall_glob.getHitproz(stratnam));
			if (hitproz > proz)
			{
				copyzaehler++;
				String quelle=rdirnam+"\\"+firstverzeichniss_g+"\\"+stratnam;
				String ziel=rdirnam+"\\out\\"+stratnam;
				// strategy is good than copy file
				FileAccess.copyFile(quelle, ziel);
			}
		}

		Mbox.Infobox("Ready, I copied #<"+copyzaehler+"> files");
	}

	private void printResulttable(CorelAllResult coreall)
	{
		// gib die liste mit den Hitcounts aus
		Tracer.WriteTrace(20, "I:Result Result");
		int anzfiles = stratfileliste.size();
		for (int i = 0; i < anzfiles; i++)
		{
			String stratnam = stratfileliste.get(i);
			Tracer.WriteTrace(20, "i<" + i + "> strname<" + stratnam
					+ "> maxhits <" + coreall.getMaxhits(stratnam)
					+ "> hitpoints<" + coreall.getHitpoints(stratnam) + ">");
		}

	}

	public void calcShowResulttable(String rdirnam)
	{
		//hier wird die filterung gemacht
		filterAllStr(rdirnam);
		printResulttable(coreall_glob);
		
		String hitfile=GlobToolbox.getRootpath() + "\\bin\\hitfile.txt";
		
		TradeResultliste tres= new TradeResultliste();
		int anzfiles = stratfileliste.size();

		
		for (int i = 0; i < anzfiles; i++)
		{
			String stratnam = stratfileliste.get(i);
			int tradeanz=coreall_glob.getTradeanz(stratnam);
			int maxhits = coreall_glob.getMaxhits(stratnam);
			int hitpoints = coreall_glob.getHitpoints(stratnam);
			String hitstring = coreall_glob.getHitstring(stratnam);
			String proz = coreall_glob.getHitproz(stratnam);

			Tradefilterresultelem te = new Tradefilterresultelem();
			te.setStratnam(stratnam);
			te.setTradeanz(tradeanz);
			te.setMaxhits(maxhits);
			te.setHitstring(hitstring);
			te.setHitpoints(hitpoints);
			te.setProz(proz);
			tres.addE(te);
			
		}
		
		tres.sorting();
		tres.writefile(hitfile);

		// Zeige dann alles mal an
		Viewer v = new Viewer();
		v.viewTableExtFile(Display.getDefault(), hitfile);
	}
	public void delall(String dirnam)
	{
		//löscht die internen speicher
		init(dirnam);
	}
	
}
