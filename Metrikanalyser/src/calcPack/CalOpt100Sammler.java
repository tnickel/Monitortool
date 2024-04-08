package calcPack;

import java.text.DecimalFormat;
import java.util.Random;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import Metriklibs.Correlator2;
import Metriklibs.EndtestFitnessfilter;
import Metriklibs.StrategienSelector;
import Metriklibs.Metriktabellen;
import data.Best100Portfolios;
import data.CorelSetting;
import data.EndtestResult;
import data.MFilter;
import data.Metrikglobalconf;
import data.Portfolio;
import data.Stratliste;
import data.meglob;
import fileActorPack.Filemanager;
import filterPack.Filterfiles;
import gui.DisTool;
import gui.JLibsProgressWin;
import gui.Mbox;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Swttool;
import hilfsklasse.Tracer;
import work.CommentWork;

public class CalOpt100Sammler
{
	// hier werden die besten 100 str ermittelt und gespeichert
	// in den Metriktabellen sind die ganzen Metriken in mehreren Tabellen
	Metriktabellen met_glob = new Metriktabellen();
	// hier sind die Filterzeiträume gespeichert
	Filterfiles filterfiles_glob = new Filterfiles();
	
	// CommentWork wird für die gr.-Anzeige benötigt
	private CommentWork cw_glob = new CommentWork();
	// Die besten Strategien werden hier gespeichert
	private Best100Portfolios best100portfolios_glob = null;
	
	public void init(StrategienSelector sel)
	{
		
		readExportedDatabank(sel);
		// falls schon filter da sind die einlesen
		if (checkConfigAvailable() == true)
			readFilterFiles();
		else
			// wenn keine Filter da sind dann generiere das Standartfile
			genConfig();
	}
	
	private void readExportedDatabank(StrategienSelector msel)
	{
		// tabellen erst mal einlesen
		String rpath = Metrikglobalconf.getFilterpath();
		if ((rpath == null) || (rpath.length() < 5))
			Tracer.WriteTrace(10, "Error: please set config in File/Config");
		
		met_glob.readAllTabellen(msel,rpath);
		
		// die filterzeiträume aus den Tabellen generieren, bzw
		// die einlesen wenn die auf platte
		// In den Dateien.filter stehen zu den Metriken Filter. Hier kann man
		// wertebereiche vorgeben wenn man möchte
		// auch kann man sagen ob man hier die Metrik für die Analyse betrachten möchte,
		// man setzt dann einfach ein flag.
		filterfiles_glob.genAllFilterzeitraume(met_glob);
		if (filterfiles_glob.getAnz() < 2)
			Tracer.WriteTrace(10,
					"E:filterzeitraume init fehler anz=" + filterfiles_glob.getAnz() + "please Check File/Config");
		
	}
	
	private void genConfig()
	{
		// schreibt die ganzen filter in mehrere dateien
		filterfiles_glob.writeFilterSettings();
	}
	
	public boolean checkConfigAvailable()
	{
		boolean flag = filterfiles_glob.checkConfigAvailable(met_glob);
		return (flag);
	}
	
	public void readFilterFiles()
	{
		filterfiles_glob = new Filterfiles();
		// liest die ganzen Metrikfilter ein
		filterfiles_glob.readFilterSettings(met_glob);
		if (filterfiles_glob.getAnz() < 2)
			Tracer.WriteTrace(10, "E:read config error #filtzraume=" + filterfiles_glob.getAnz());
	}
	
	public void cleanConfig()
	{
		filterfiles_glob.cleanFilterSettings(met_glob);
	}
	
	public Best100Portfolios startOptimizing(Best100Portfolios best100portfolios,StrategienSelector stratsel,int infoflag, int ncalc, Text textcount, Text text1maxprof,
			Text text1anzrestmenge, MFilter mfilter, int algotype, CorelSetting corelsetting,
			EndtestFitnessfilter endfitnessfilter, boolean useonlyselectedmetrics)
	{
		// ncalc: Es wird n mal gerechnet und geschaut ob was besseres gefunden
		// wurde
		// infoflag: 1, dann wird ein result als fenster ausgegeben
		// algotype: 1, dann werden die metriken randommässig geändert
		// 2, person correlation
		// 3, random with 50% take of the best
		// 0, dann wird nach der person-korrelation optimiert
		// calopt100: hier sind die korrelationswerte für die metriken drin
		
		Random rand = new Random();
		// in dieser Datenstruktur werden die 100 Besten portfolios gespeichert
	
		DecimalFormat df = new DecimalFormat("#.0");
		JLibsProgressWin jprog = null;
		if (meglob.showdisplay == 1)
			jprog = new JLibsProgressWin("calc", 0, ncalc);
		
		// suche n mal das beste
		for (int j = 0; j < ncalc; j++)
		{
			textcount.setText(String.valueOf(j + 1));
			Swttool.wupdate(Display.getDefault());
			
			if (meglob.showdisplay == 1)
				jprog.update(j);
			
			Stratliste stratliste = new Stratliste();
			// erst mal die strliste mit allen strat aufbauen
			// die stratliste wird aus den metriken und den filtern erstellt
			stratliste.buildStratliste(stratsel,met_glob);
			
			// filtere mit dem filterfile falls da
			stratliste.filterSelfile(Metrikglobalconf.getFilterpath() + "\\filter.txt");
			
			// baue filterzeiträume neu auf
			// Es gibt 2 Möglichkeiten die Filterzeiträume aufzubauen.
			// 1) Wir lesen das von Platte ein
			// 2) Wir nehmen den Filterzeitraum aus der Bestenliste
			
			// 1: Step1: modifiziere die Filter (random oder anders)
			// modifiziere alle filter
			if (algotype == 0)
			{
				readFilterFiles();
				filterfiles_glob.modifyRandom(useonlyselectedmetrics);
			} else if (algotype == 1)
			{
				readFilterFiles();
				filterfiles_glob.modifyPersonKorrelation(corelsetting, useonlyselectedmetrics);
			} else
			{
				int randomNumber = rand.nextInt(100);
				if (randomNumber > 50)
				{
					readFilterFiles();
					filterfiles_glob.modifyRandom(useonlyselectedmetrics);
				} else
				{
					
					filterfiles_glob = best100portfolios.holeFilterFiles(0);
					// falls noch nix in der bestliste
					if (filterfiles_glob == null)
						continue;
					filterfiles_glob.modifyRandomPlus(useonlyselectedmetrics);
				}
				
			}
			// 2:Step2: Rausfiltern mit den Filtern
			// Aus der Gesammtmenge von Stratgien werden Strategien rausgefiltert.
			// Die filterfiles_glob besitzen die min-max schranken für die einzelnen
			// metriken
			// Anhand dieser Filterschranken werden die Strategien aus der gesammtmenge
			// rausgefiltert
			stratliste.filterAll(met_glob, filterfiles_glob, 1);
			
			// 3:Step3: Mache einen Endtest mit den übrig gebliebenen Strategien.
			// mit den Reststrategien muss noch ein Endtest gemacht werden
			// der Endresult ist nur ein Floatwert
			EndtestResult endresult = stratliste.Endtest(met_glob, 0, endfitnessfilter);
			
			// 4:Step4: Merke das Ergebniss in der Best100-Liste
			// Dieses Endergebniss wird versucht in die liste der 100 besten
			// einzufügen
			best100portfolios.checkAdd(stratliste, filterfiles_glob, endresult, mfilter);
			
			// 5:Step5: Zeige Daten im Display an.
			// bestvalue ausgeben
			float bestval = best100portfolios.holeBestvalue();
			text1maxprof.setText(String.valueOf(df.format(bestval)));
			// anz strat in Restmenge
			int anzbestval = best100portfolios.calcAnzStratBestvalue();
			text1anzrestmenge.setText(String.valueOf(anzbestval));
			
			if (j % 100 == 0)
			{
				System.out.println(
						"calc best100 n=" + j + " i=" + j + " bestval<" + bestval + "> anz<" + anzbestval + ">");
			}
			DisTool.UpdateDisplay2();
			// bestvalue ausgeben
			text1maxprof.setText(String.valueOf(best100portfolios.holeBestvalue()));
			
			if (Metrikglobalconf.getStopflag_glob() == 1)
				break;
			// 6:STep6: mache mit dem nächsten Versuch weiter.
		}
		best100portfolios_glob = best100portfolios;
		if (meglob.showdisplay == 1)
			jprog.end();
		// die bestenliste wird jetzt zurückgeliefert
		// 7:Step7: wir liefern die Bestenliste zurück.
		return best100portfolios;
	}
	
	public void calcUnknownData(StrategienSelector stratselector,EndtestFitnessfilter endfitnessfilter,int index)
	{
		//der index läuft von 1-5, wir wollen 5 mal ungesehene daten nutzen
		//hier werden die 100 besten filterrules bewertet
		//D.h. die Filterrules werden mit ungesehenen daten getestet.
		
		//set to unknown data.
		stratselector.setMenge(index);
		
		//0)erst mal eine Stratgieliste mit allen strategien aufbauen
		//aus dieser Gesammtliste wird dann weggefiltert
		Stratliste stratliste = new Stratliste();
		stratliste.buildStratliste(stratselector,met_glob);
		stratliste.filterSelfile(Metrikglobalconf.getFilterpath() + "\\filter.txt");
		
		int anzbest=100;
		
		for(int i=0; i<anzbest; i++)
		{
			//1) die filterfiles holen, und die sind diesmal in den bestportolios 
			//die Filterfiles sind die configs mit den schranken
			
			//hole erst mal ein portfolio raus
			Portfolio port=best100portfolios_glob.holePortfolio(i);
			if(port==null)//ende errreicht
				break;
			//dann den filter, den brauchen wir ja zum neuberechnen
			Filterfiles fi=port.getFilt();
			
			//2) und hier wird weggefiltert.
			stratliste.buildStratliste(stratselector,met_glob);
			stratliste.filterAll(met_glob, fi, 1);
			
			//3) Dann wird mit den reststrategien ein Endtest gemacht
			EndtestResult endresult = stratliste.Endtest(met_glob, 0, endfitnessfilter);
			
			//4) Dieses endresult muss in der bestliste festgehalten werden
			port.setEndresultUnknownData(endresult,index);
		}
	
	}
	
	
	private void initFilterzeiträume()
	{
		// hier werden jedes mal die Filterzeiträume wieder zurückgesetzt.
		// Es gibt 2 möglichkeiten
		// 1) Es wird das genommen was auf Platte ist
		// 2) Es wird aus der best100-liste was genommen
		readFilterFiles();
		
	}
	
	public void kopiereBestStrfiles()
	{
		// ermittelt das str-portfolio was am besten ist
		// kopiert die strfiles um die jahresgraphik anzuzeigen
		
		if (best100portfolios_glob == null)
			return;
		Portfolio bestPortfolio = best100portfolios_glob.holeBestPortfolio();
		if (bestPortfolio == null)
			return;
		Stratliste stratliste = bestPortfolio.getStratliste();
		
		if (stratliste == null)
			return;
		
		// dann wird das beste umkopiert
		Filemanager.kopiereEndfiles(stratliste, met_glob);
		
	}
	
	public void storeResults()
	{
		// die Ergebnisse speichern
		
		best100portfolios_glob.speichereDieResultate();
	}
	
	public void showGraphik()
	{
		// es wird eine Endtestgraphik angezeigt
		String endfile = met_glob.holeEndtestFilnamen();
		String endtestdir_sel = endfile.substring(0, endfile.lastIndexOf("\\") + 1) + "str__selected_endtestfiles";
		
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(endtestdir_sel, 1);
		if (fdyn.holeFileAnz() == 0)
		{
			Mbox.Infobox("E:no strfiles in <" + endtestdir_sel + "> to view");
			return;
		}
		cw_glob.setWorkdir(endtestdir_sel);
		cw_glob.calcEndtestGraphic();
	}
	
	public void correlate(StrategienSelector msel,int showflag, String entestattribname, String corealalgo)
	{
		// für jede Metrik wird die person Correlation ermittelt
		// die metriktabellen wurden schon eingelesen
		
		readExportedDatabank(msel);
		// als nächstes wird korreliert
		
		Correlator2.CalcCorrelation(met_glob, entestattribname, corealalgo);
		
		int anzstrat = met_glob.holeAnzAttributes(0);
		
		if (showflag == 1)
			Correlator2.zeigeTabelle(Metrikglobalconf.getFilterpath() + "\\correlation.txt", corealalgo);
	}
	
	public void setTable(Table table)
	{
		// Hier wird die tabelle zugewiesen
		best100portfolios_glob.setTable(table);
	}
}
