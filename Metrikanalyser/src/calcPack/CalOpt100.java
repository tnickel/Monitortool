package calcPack;

import gui.DisTool;
import gui.JLibsProgressWin;
import gui.Mbox;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Swttool;
import hilfsklasse.Tracer;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import work.CommentWork;
import Metriklibs.CorrelationResultliste;
import Metriklibs.EndtestFitnessfilter;
import Metriklibs.Metriktabellen;
import data.Best100;
import data.Best100dat;
import data.CorelSetting;
import data.EndtestResult;
import data.MFilter;
import data.Metrikglobalconf;
import data.Stratliste;
import data.meglob;
import fileActorPack.Filemanager;
import filterPack.Filterzeitraume;

public class CalOpt100
{
	// hier werden die besten 100 str ermittelt und gespeichert
	//in den Metriktabellen sind die ganzen Metriken in mehreren Tabellen
	Metriktabellen met_glob = new Metriktabellen();
	//hier sind die Filterzeiträume gespeichert
	Filterzeitraume filtzeitraume_glob = new Filterzeitraume();

	// CommentWork wird für die gr.-Anzeige benötigt
	private CommentWork cw_glob = new CommentWork();
	//Die besten Strategien werden hier gespeichert
	private Best100 best100_glob = null;

	public void readMetriktabellen()
	{
		// tabellen erst mal einlesen
		String rpath = Metrikglobalconf.getFilterpath();
		met_glob.readAllTabellen(rpath);

		// die filterzeiträume aus den Tabellen generieren, bzw
		// die einlesen wenn die auf platte
		// In den Dateien.filter stehen zu den Metriken Filter. Hier kann man wertebereiche vorgeben wenn man möchte
		// auch kann man sagen ob man hier die Metrik für die Analyse betrachten möchte, man setzt dann einfach ein flag.
		filtzeitraume_glob.genAllFilterzeitraume(met_glob);
		if (filtzeitraume_glob.getAnz() < 2)
			Tracer.WriteTrace(10, "E:filterzeitraume init fehler anz="
					+ filtzeitraume_glob.getAnz());

	}

	public void genConfig()
	{
		// schreibt die ganzen filter in mehrere dateien
		filtzeitraume_glob.writeFilterSettings();
	}

	public boolean checkConfigAvailable()
	{
		boolean flag = filtzeitraume_glob.checkConfigAvailable(met_glob);
		return (flag);
	}

	public void readFilterzeitraume()
	{
		filtzeitraume_glob = new Filterzeitraume();
		// liest die ganzen Metrikfilter ein
		filtzeitraume_glob.readFilterSettings(met_glob);
		if (filtzeitraume_glob.getAnz() < 2)
			Tracer.WriteTrace(10, "E:read config error #filtzraume="
					+ filtzeitraume_glob.getAnz());
	}

	public void cleanConfig()
	{
		filtzeitraume_glob.cleanFilterSettings(met_glob);
	}

	public Best100 startCalculation(int infoflag, int ncalc, Text textcount,
			Text text1maxprof, Text text1anzrestmenge, MFilter mfilter,
			int randomflag,CorelSetting corelsetting,EndtestFitnessfilter endfitnessfilter)
	{
		// ncalc: Es wird n mal gerechnet und geschaut ob was besseres gefunden
		// wurde
		// infoflag: 1, dann wird ein result als fenster ausgegeben
		// randomflag: 1, dann werden die metriken randommässig geändert
		// 0, dann wird nach der person-korrelation optimiert
		// calopt100: hier sind die korrelationswerte für die metriken drin

		// hier werden die besten 100 gepeichert
		Best100 best100 = new Best100();

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
			stratliste.buildStratliste(met_glob);

			// filtere mit dem filterfile falls da
			stratliste.filterSelfile(Metrikglobalconf.getFilterpath()
					+ "\\filter.txt");

			// baue filterzeiträume neu auf
			readFilterzeitraume();

			// modifiziere alle filter
			if (randomflag == 1)
				filtzeitraume_glob.modifyRandom();
			else
				filtzeitraume_glob.modifyPersonKorrelation( corelsetting);

			// dann wird alles gefiltert
			stratliste.filterAll(met_glob, filtzeitraume_glob, 1);

			// mit den Reststrategien muss noch ein Endtest gemacht werden
			// der Endresult ist nur ein Floatwert
			EndtestResult endresult = stratliste.Endtest(met_glob, 0,endfitnessfilter);

			// Dieses Endergebniss wird versucht in die liste der 100 besten
			// einzufügen
			best100.checkAdd(stratliste, filtzeitraume_glob, endresult, mfilter);

			// bestvalue ausgeben
			float bestval = best100.holeBestvalue();
			text1maxprof.setText(String.valueOf(bestval));
			// anz strat in Restmenge
			int anzbestval = best100.calcAnzStratBestvalue();
			text1anzrestmenge.setText(String.valueOf(anzbestval));

			if (j % 100 == 0)
			{
				System.out.println("calc best100 n=" + j + " i=" + j
						+ " bestval<" + bestval + "> anz<" + anzbestval + ">");
			}
			DisTool.UpdateDisplay2();
			// bestvalue ausgeben
			text1maxprof.setText(String.valueOf(best100.holeBestvalue()));

			if (Metrikglobalconf.getStopflag_glob() == 1)
				break;
		}
		best100_glob = best100;
		if (meglob.showdisplay == 1)
			jprog.end();
		// die bestenliste wird jetzt zurückgeliefert
		return best100;
	}

	public void kopiereBestStrfiles()
	{
		//ermittelt das str-portfolio was am besten ist
		//kopiert die strfiles um die jahresgraphik anzuzeigen
		
		
		if(best100_glob==null)
			return;
		Best100dat b100d=best100_glob.holeBestElem();
		if(b100d==null)
			return;
		Stratliste stratliste=b100d.getStratliste();
		
		if(stratliste==null)
			return;
		
		//dann wird das beste umkopiert
		Filemanager.kopiereEndfiles(stratliste,met_glob);
		
	}
	
	public void storeResults()
	{
		// die Ergebnisse speichern
		
		best100_glob.speichereDieResultate();
	}

	public void showGraphik()
	{
		//es wird eine Endtestgraphik angezeigt
		String endfile = met_glob.holeEndtestFilnamen();
		String endtestdir_sel = endfile.substring(0,
				endfile.lastIndexOf("\\") + 1) + "str__selected_endtestfiles";

		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(endtestdir_sel, 1);
		if (fdyn.holeFileAnz() == 0)
		{
			Mbox.Infobox("E:no strfiles in <"+endtestdir_sel+"> to view");
			return;
		}
		cw_glob.setWorkdir(endtestdir_sel);
		cw_glob.calcEndtestGraphic();
	}

	public void correlate(int showflag)
	{
		//für jede Metrik wird die person Correlation ermittelt
		// die metriktabellen wurden schon eingelesen
		
		readMetriktabellen();
		// als nächstes wird korreliert
		
		CorrelationResultliste.CalcPersonCorrelation(met_glob);
		if(showflag==1)
			CorrelationResultliste.zeigeTabelle(Metrikglobalconf.getFilterpath()+"\\correlation.txt");
	}
	public void setTable(Table table)
	{
		//Hier wird die tabelle zugewiesen
		best100_glob.setTable(table);
	}
}
