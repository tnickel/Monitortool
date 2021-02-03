package work;

import graphic.GesammtSumChart2;
import gui.Dialog;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.eclipse.swt.widgets.Display;

import xml.TnParser;
import data.Commentflags;
import data.Endtestdata;
import data.GewinnjahrStategienSum;
import data.GewinnjahrStrategie;
import data.GlobToolbox;
import data.Profitelement;
import data.Tradeliste;

public class CommentWork
{
	private String workdir_glob = null;
	private String monthyear_glob=null;
	private String fileprefix_glob = null;
	private static String lastdir = null;

	public CommentWork()
	{
	}

	public String selectDirectory(String vpref)
	{
		String verzpref = null;

		if(vpref!=null)
		{
			verzpref=vpref;
		}
		
		else if (lastdir == null)
		{
			verzpref = "c:\\GeneticBuilder\\calculatedStrategies\\01_savedStrategies";
		} else
			verzpref = lastdir;

		String dirnam = Dialog.DirDialog(Display.getDefault(), verzpref);
		lastdir = dirnam;
		workdir_glob = dirnam;
		return dirnam;
	}
	public void setWorkdir(String dir)
	{
		workdir_glob=dir;
	}

	public void setFileprefix(String text)
	{
		fileprefix_glob = text;
		Tracer.WriteTrace(20,"fileprefix=" + fileprefix_glob);
	}

	public void setMonthYear(String text)
	{
		monthyear_glob=text;
		Tracer.WriteTrace(20, "I:monthyear="+monthyear_glob);
		
	}
	
	public void renameAllFiles()
	{
		
		
		if ((workdir_glob==null)||(workdir_glob.length() < 3))
		{
			Mbox.Infobox("please set workdir");
			return;
		}
		if (fileprefix_glob.length() < 1)
		{
			Mbox.Infobox("please select a prefix");
			return;
		}
		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if ((fnam0.contains(".str") == false) && (fnam0.contains(".sq4")==false)&&(fnam0.contains(".mq4")==false))
				continue;

			
			
			// Das Ende des Strings sichern 
			//bsp version 3.8.2:"xxxxxx 0.4454545.str"
			//version 4.X :"Q05 EURUSD H1Strategy 0.1.38(1).sq4"
			//EA Studio EURUSD H1 21026117.mq4
			//laststring = H1 21026117.mq4
			
			//den string splitten
			String[] parts = fnam0.split(" ");
			int posanz=parts.length;
			
			//die endung holen
			String endstring=fnam0.substring(fnam0.lastIndexOf("."));
			//die magic holen
			String magic = parts[posanz-1];
			//den timeframe holen
			String timeframe= parts[posanz-2];
			
			//die Dateiendung enfernen
			magic=magic.substring(0,magic.indexOf("."));
			
			// neuen Filenamen berechnen

			//version 4.x replace (-->0  and )-->0
			magic=magic.replace("(", "0");
			magic=magic.replace(")","0");
			int slen=magic.length();
			int cutlen=5;
			if(slen>cutlen)
			{	//die magic auf 5 stellen von rechts abschneiden
				magic=magic.substring(slen-cutlen);
			}
			
			String fnamNeu = workdir_glob + "\\" + fileprefix_glob + " " + timeframe +" "
					+ monthyear_glob+magic+endstring;
			File falt = new File(workdir_glob + "\\" + fnam0);
			File fneu = new File(fnamNeu);

			
			
			if (falt.renameTo(fneu) == false)
			{
				Mbox.Infobox("can´t rename <" + falt.getName() + "> to <"
						+ fneu.getName() + ">");
			}
		}
		Mbox.Infobox("all ready");
	}

	public void addComment(Commentflags cf)
	{
		if ((workdir_glob == null) || (workdir_glob.length() < 5))
		{
			Mbox.Infobox("Please select directory");
			return;
		}

		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);

		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if (fnam0.contains(".str") == false)
				continue;

			String fnam = workdir_glob + "\\" + fnam0;

			// der AddComment Butten wurde gedrückt
			TnParser parser = new TnParser();
			parser.loadFile(fnam, 0);
			parser.addComments(cf);
			parser.saveFile(fnam);
		}
		Mbox.Infobox("ready, please reload strategies in GB");
	}

	public void cleanComments()
	{
		if ((workdir_glob == null) || (workdir_glob.length() < 5))
		{
			Mbox.Infobox("Please select directory");
			return;
		}

		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);
		Mbox.Infobox("Information: I delete all notes in directory<"
				+ workdir_glob + ">");
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if (fnam0.contains(".str") == false)
				continue;

			String fnam = workdir_glob + "\\" + fnam0;

			// der Kommentar in Note wird gelöscht
			TnParser parser = new TnParser();
			parser.loadFile(fnam, 0);
			parser.cleanComment();
			parser.saveFile(fnam);
		}
		Mbox.Infobox("all cleaned");
	}

	public void calcNettoprofits()
	{
		int poszaehler = 0, negzaehler = 0, geszaehler = 0;
		float sumnettoprofit = 0, loss = 0, prof = 0;
		int notfoundzaehler=0;
		Endtestdata enddata = new Endtestdata();
		enddata.loadEndtestdata();

		// läd alle strfiles und berechnet den Nettorprofit über alles
		if ((workdir_glob == null) || (workdir_glob.length() < 5))
		{
			Mbox.Infobox("Please select directory");
			return;
		}

		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);

		int anz = FileAccess.holeFileAnz();
		JToolboxProgressWin jp = new JToolboxProgressWin("load data <calc>", 0,
				(int) anz);
		jp.update(0);
		for (int i = 0; i < anz; i++)
		{
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if ((fnam0.contains(".str") == false)&&(fnam0.contains(".txt")==false))
				continue;
			String fnam = workdir_glob + "\\" + fnam0;

			//holt den profit aus der vergleichsstruktur
			float netprof = enddata.getProfit(fnam);
			if (netprof == -99999999)
			{
				notfoundzaehler++;
				continue;
			}
			

			if (netprof > 0)
			{
				poszaehler++;
				prof = prof + netprof;
				// Tracer.WriteTrace(20,
				// "file<"+fnam+"> gpsum<"+prof+"> netprof<"+netprof+">");
			}
			if (netprof <= 0)
			{
				negzaehler++;
				loss = loss + netprof;
				// Tracer.WriteTrace(20,
				// "file<"+fnam+"> glsum<"+loss+"> netprof<"+netprof+">");
			}
			geszaehler++;
			sumnettoprofit = sumnettoprofit + netprof;

			if (i % 10 == 0)
			{
				jp.update(i);
				System.out.println(geszaehler + "|" + anz);
			}
		}
		jp.update(anz);
		jp.end();
		// profitfaktorberechnung
		float pf = (Math.abs(prof) / Math.abs(loss));
		NumberFormat numberFormat = new DecimalFormat("0.00");
		if(notfoundzaehler>0)
			Mbox.Infobox("Warning: #<"+notfoundzaehler+"> strategies not found in the enddata-database");
		Mbox.Infobox("result: #Strat+<" + poszaehler + "> #Strat-<"
				+ negzaehler + "> ges<" + geszaehler + "> Sumprof<" + prof
				+ "> Sumloss<" + loss + "> sum<" + sumnettoprofit + "> pf<"
				+ numberFormat.format(pf) + ">");

	}

	public void calcEndtestGraphic()
	{
		GewinnjahrStategienSum gewinnjahrsumme = new GewinnjahrStategienSum();

		// läd alle strfiles und berechnet den Nettorprofit über die einzelnen
		// monate
		if ((workdir_glob == null) || (workdir_glob.length() < 5))
		{
			Mbox.Infobox("Please select directory");
			return;
		}

		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);

		int anz = FileAccess.holeFileAnz();
		JToolboxProgressWin jp = new JToolboxProgressWin("load data <calc>", 0,
				(int) anz);
		jp.update(0);
		//lade jede Strategie
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if (fnam0.contains(".str") == false)
				continue;
			String fnam = workdir_glob + "\\" + fnam0;

			Tradeliste eatl = new Tradeliste(null);
			int anztrades=eatl.initBacktest(fnam);
			if(anztrades==0)
			{
				Tracer.WriteTrace(20, "Warning: anz trades =0 strategie<"+fnam+">");
				continue;
			}
			GewinnjahrStrategie gewjahr = eatl.calcGewinnjahrType1(0, 0, 0, 0, 0, 0);

			// die Gewinne der einzelnen Strategie zur gesammtsumme addieren
			gewinnjahrsumme.addStrategie(gewjahr);
		}
		jp.update(anz);
		jp.end();

		GesammtSumChart2 pd= new GesammtSumChart2(gewinnjahrsumme.getZeitraumname(),gewinnjahrsumme);
	
	}

	public void calcEndtestGraphic300(int maxdd, int maxddproz,
			int maxddfromstart, int trailflag, int maxddtrail, int ntraildays,int budget,int maxddbudget)
	{
		GewinnjahrStategienSum gewinnjahrsumme = new GewinnjahrStategienSum();

		// läd alle strfiles und berechnet den Nettorprofit über die einzelnen
		// monate
		if ((workdir_glob == null) || (workdir_glob.length() < 5))
		{
			Mbox.Infobox("Please select directory");
			return;
		}

		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);

		int anz = FileAccess.holeFileAnz();
		JToolboxProgressWin jp = new JToolboxProgressWin("load data <calc>", 0,
				(int) anz);
		jp.update(0);
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if (fnam0.contains(".str") == false)
				continue;
			String fnam = workdir_glob + "\\" + fnam0;

			Tradeliste eatl = new Tradeliste(null);
			eatl.initBacktest(fnam);
			
			GewinnjahrStrategie gewjahr=null;
			if(budget==0)
			{
				 gewjahr = eatl.calcGewinnjahrType1(maxdd, maxddproz,
					maxddfromstart, trailflag, maxddtrail, ntraildays);
			}
			else
				 gewjahr = eatl.calcGewinnjahrType2(budget,maxddbudget,anz);

			// die Gewinne der einzelnen Strategie zur gesammtsumme addieren
			gewinnjahrsumme.addStrategie(gewjahr);
		}
		jp.update(anz);
		jp.end();

		
		GesammtSumChart2 pd= new GesammtSumChart2(gewinnjahrsumme.getZeitraumname(),gewinnjahrsumme);
	
	}

	public void generateEndtest(int mode, int confidenz)
	{
		// mode=1, calc from is-nettoprofit
		// mode=2, calc from oos-Nettorprofit
		// mode=3, calc from NetProfitRobust

		// den initialen Endtest generieren
		int poszaehler = 0, negzaehler = 0, geszaehler = 0;
		float sumgrossprofit = 0,sumgrossloss=0, loss = 0, prof = 0;

		// läd alle strfiles und berechnet den Nettorprofit über alles
		if ((workdir_glob == null) || (workdir_glob.length() < 5))
		{
			Mbox.Infobox("Please select directory");
			return;
		}

		// check if old database available
		File endfnam = new File(GlobToolbox.getRootpath()
				+ "\\data\\endtest.xml");
		if (endfnam.exists())
		{
			MboxToolQuest.Questbox("datafile already available in <" + endfnam
					+ "> please delete this first");
			return;
		}
		// die Fileliste des Directorys lesen
		FileAccess.initFileSystemList(workdir_glob, 1);

		int anz = FileAccess.holeFileAnz();
		JToolboxProgressWin jp = new JToolboxProgressWin("calc Endtest für n<"
				+ anz + "> data", 0, (int) anz);
		jp.update(0);

		Endtestdata enddata = new Endtestdata();
		enddata.setMode(mode);
		for (int i = 0; i < anz; i++)
		{
			// der alte name
			String fnam0 = FileAccess.holeFileSystemName();
			if (fnam0.contains(".str") == false)
				continue;

			String fnam = workdir_glob + "\\" + fnam0;

			TnParser parser = new TnParser();

			//optimierung da bei mode1 und mode2 die werte vorne stehen.
			if (mode < 3)
				parser.loadFile(fnam, 200);
			else
				// werden werte des robustnesstest gelesen dann muss alles
				// eingelesen werden da die info
				// am ende des xml steht
				parser.loadFile(fnam, 0);

			// profit auslesen
			float grossprof = 0,grossloss=0;
			if (mode == 1)
			{
				grossprof = parser.getGrossProfit_IS();
				grossloss= parser.getGrossLoss_IS();
			}
			else if (mode == 2)
			{
				grossprof = parser.getGrossProfit_OSS();
				grossloss= parser.getGrossLoss_OSS();
			}
			else if (mode == 3)
				grossprof = parser.getNettoprofit_Robust(confidenz);
			else
				Tracer.WriteTrace(10, "E:unknown mode<" + mode + ">");

			// profit speichern

			Profitelement profdata = new Profitelement();

			profdata.setFilename(fnam.substring(fnam.lastIndexOf("\\") + 1));
			profdata.setGrossprofit(grossprof);
			profdata.setGrossloss(grossloss);
			enddata.addProfit(profdata);

			if (Math.abs(grossprof) > Math.abs(grossloss))
			{
				poszaehler++;

			}
			if (Math.abs(grossprof) <= Math.abs(grossloss))
			{
				negzaehler++;
				
			}

			sumgrossprofit = sumgrossprofit + grossprof;
			sumgrossloss=sumgrossloss+grossloss;
			geszaehler++;
			
			if (i % 10 == 0)
			{
				jp.update(i);
				System.out.println(i + "|" + anz);
			}

		}
		jp.update(anz);
		jp.end();
		enddata.saveEndtestdata();
		// profitfaktorberechnung
		float pf = Math.abs(sumgrossprofit) / Math.abs(sumgrossloss);
		NumberFormat numberFormat = new DecimalFormat("0.00");
		Mbox.Infobox("result: #Strat+<" + poszaehler + "> #Strat-<"
				+ negzaehler + "> ges<" + geszaehler + "> prof<" + sumgrossprofit
				+ ">loss<" + sumgrossloss + "> sum<" + (sumgrossprofit+sumgrossloss)
				+ "> pf(Strategien)<" + numberFormat.format(pf) + ">");

		
		
	}

	public void deleteEndtest()
	{
		Endtestdata enddata = new Endtestdata();
		enddata.deleteEnddatafile();
	}
}
