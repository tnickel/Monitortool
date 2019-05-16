package work1;

import gui.Mbox;
import jHilfsfenster.JShowTradelist;
import jHilfsfenster.jCompareTradelist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import jhilf.JTableTool;
import charttool.ProfitPanel;
import data.OptimizeAlgo;
import data.OptimizeResult;
import data.OptimizeResultliste;
import data.PanelResult;
import data.Timefilter;
import data.Tradeliste;

public class GuiWorker
{
	// dies ist die Workerklasse die die alles beinhaltet, hier wird keine GUI
	// benötigt
	// diese Klasse dient zum Trennen von der GUI

	// Beschreibung was die Klase Worker braucht um arbeiten zu können
	// 1) LoadQuellStrategie als File oder Tradeliste
	// 2)

	// dies ist die globale sourcetradeliste auf der die Berechnungen
	// durchgeführt werden
	Tradeliste tradeliste_orginal_glob = null;
	// wenn man im Gui auf eine Tradeliste geklickt hat wird diese hier
	// gespeichert
	Tradeliste tradeliste_selected_glob = null;

	//selektierte Strategiename und selektierter optoalgoname
	private String selStratName_glob = null;
	private String selOptoalgoName_glob=null;
	private OptimizeAlgo optAlgo_glob = null;

	public OptimizeResultliste HoleOptimizeResultliste()
	{
		return(optAlgo_glob.HoleOptimizeResultliste());
	}
	public Tradeliste HoleTradelisteOrginal()
	{
		return tradeliste_orginal_glob;
	}
	// hier wird die Strategie von einem File geladen
	public void LoadQuellStrategy(String file)
	{
		File filex = new File(file);

		if (filex.exists() == false)
			Mbox.Infobox("file <" + file + "> don´t exists");

		tradeliste_orginal_glob = new Tradeliste(null);
		tradeliste_orginal_glob.initBacktest(file);

		//tradeliste nach den Closezeiten sortieren
		tradeliste_orginal_glob.sortliste();
		
		tradeliste_orginal_glob.calcSummengewinne();
		optAlgo_glob = new OptimizeAlgo(tradeliste_orginal_glob);

		if (tradeliste_orginal_glob == null)
			Mbox.Infobox("orginal tradeliste is null1");
	}

	// alternativ kann auch eine Tradeliste eingeladen werden
	public void LoadQuellStrategy(Tradeliste tradeliste,int newflag)
	{
		//newflag==0, dann wird einfach geadded

		if (tradeliste == null)
			Mbox.Infobox("loadquellStrategy error tradeliste==null");

		tradeliste_orginal_glob = tradeliste;
		if(newflag==1)
			optAlgo_glob = new OptimizeAlgo(tradeliste);

		if (tradeliste_orginal_glob == null)
			Mbox.Infobox("orginal tradeliste is null2");
	}
	// Für die Quellstrategie wird ein JPanel gezeichnet
	public JPanel GenQuellStratJPanel(int gdx)
	{

		if (tradeliste_orginal_glob == null)
			Mbox.Infobox("orginal tradeliste is null");
		if (tradeliste_orginal_glob.getsize() <= 0)
			Mbox.Infobox("orginal tradeliste is empty");

		// Profitanzeige profanz= new
		// Profitanzeige("hallo",tradeliste_glob,null,null);
		ProfitPanel profpan = new ProfitPanel();
		JPanel pan = profpan.createDemoPanel(tradeliste_orginal_glob,
				"orginal strategy", gdx);
		return pan;
	}
	// Die suchergebnisse werden gelöscht
	public void clear()
	{
		if (optAlgo_glob != null)
			optAlgo_glob.cleanOptimizeResult();
	}
	// es wurde eine optimierte Strategie angeklickt
	public String table3Clicked(String resultname,String optoalgoname)
	{
		selStratName_glob = resultname;
		selOptoalgoName_glob=optoalgoname;

		return selStratName_glob;
	}
	public String getSelStratName()
	{
		return (selStratName_glob);
	}
	// es wird die Quelltradeliste mit der optimierten Tradeliste verglichen
	public void CompareTradelist()
	{
		OptimizeResult os_comp = null;

		if (selStratName_glob == null)
			os_comp = optAlgo_glob.holeBestOptimizeResult();
		else
			os_comp = optAlgo_glob.holeOptimizeResult(selStratName_glob,selOptoalgoName_glob);

		if (os_comp != null)
		{
			
			jCompareTradelist.main(null, tradeliste_orginal_glob, os_comp,
					os_comp.getgdx(),selStratName_glob);
		}
	}
	// die quelltradeliste wird angezeigt
	public void ShowTradelist()
	{
		JShowTradelist.main(null, tradeliste_orginal_glob);
	}
	// die Strategien die optimierte Ergebnisstabelle wird berechnet und als
	// JTable übereicht
	public JTable GenOptimizeTable()
	{
		// die optimierte tabelle generieren. Dies ist die Tabelle in der mitte
		// also die Ergebnisstabelle

		if (optAlgo_glob != null)
		{
			JTable jta = JTableTool.createOptiJTable(optAlgo_glob.oges_glob);
			return (jta);
		}
		return null;
	}
	// die beste optimierte Strategie wird als Panel berechnet
	public PanelResult holeBestPanel(Timefilter timefilter, int panelindex)
	{
		return (optAlgo_glob.oges_glob.holeBestPanel(timefilter, panelindex));
	}
	// Es wird eine bestimmte optimierte Strategie als Panel übermittelt
	public PanelResult holePanelResult(String resultname, String optoalgoname,Timefilter timefilter,
			int panelindex)
	{
		PanelResult pr = optAlgo_glob.oges_glob.holePanelResult(resultname,optoalgoname,
				timefilter, panelindex);
		return (pr);
	}
	// Es wird die optimierte Tradeliste mit dem index ermittelt
	public Tradeliste holeResultTradeliste(String resultname,  String optoalgoname,Timefilter timefilter)
	{
		Tradeliste tr = optAlgo_glob.oges_glob
				.holeTradelisteName(resultname, optoalgoname,timefilter);
		return (tr);
	}
	public void setSelTradeliste(String resultname,  String optoalgoname,Timefilter timefilter)
	{
		// hier wird eine Tradeliste als selected Tradelist gespeichert
		// die selected Tradelist ist die tradeliste auf der geklickt wurden ist
		tradeliste_selected_glob = optAlgo_glob.oges_glob.holeTradelisteName(resultname,optoalgoname,
				timefilter);

	}
	public Tradeliste getSelTradeliste()
	{
		// die gespeicherte Tradeliste wird ausgegeben
		// diese Tradeliste kann die neue Mastertradeliste für den neuen
		// Bearbeitungsvorgang sein
		return (tradeliste_selected_glob);
	}
	// das min-maxdatum der quellstrategie wird ermittelt
	public void setMinMaxDateQuellStrat(JTextField jtextfield)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		tradeliste_orginal_glob.calcMinMaxdate();
		Date mindate = tradeliste_orginal_glob.getMindate();
		Date maxdate = tradeliste_orginal_glob.getMaxdate();
		String mindatestring = df.format(mindate);
		String maxdatestring = df.format(maxdate);
		jtextfield.setText("von " + mindatestring + " bis " + maxdatestring);
	}
	// Das min-maxdatum der optimierten Strategie wird ermittelt
	public void calcMinMaxDateOptStrat(JTextField jtextfield,String resultname,String optoalgoname,
			Timefilter timefilter)
	{
		// auf der Strategie mit dem index i wurde geklickt

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		
		Tradeliste tradeliste_optimized = optAlgo_glob.oges_glob
				.holeTradelisteName(resultname,optoalgoname, timefilter);

		tradeliste_optimized.calcMinMaxdate();
		Date mindate = tradeliste_optimized.getMindate();
		Date maxdate = tradeliste_optimized.getMaxdate();

		if(mindate==null)
			Mbox.Infobox("Error: mindate ==null");
		if(maxdate==null)
			Mbox.Infobox("Error: maxdate ==null");
		
		String mindatestring = df.format(mindate);
		String maxdatestring = df.format(maxdate);
		jtextfield.setText("von " + mindatestring + " bis " + maxdatestring);
	}
	// die Algoliste , das ist die Liste mit den Optimierungsalgorithmen wird
	// aufgebaut
	// den optimierungsvorgang starten
	public void optimizeAll(Timefilter timefilter)
	{
		if (optAlgo_glob == null)
		{
			Mbox.Infobox("please load/select source strategy to optimize");
			return;
		}
		optAlgo_glob.optimizeAll(timefilter);
	}
	// ein optierungsalgo wurde selektiert, dies muss in der internen Struktur
	// auch gemacht werden
	public void setSelectedAlgoStrategies(String algostring)
	{
		if (optAlgo_glob == null)
			return;
		optAlgo_glob.setSelectedAlgoStrategies(algostring);
	}
	public int calcSortedRow(String algostring,String optoalgoname)
	{
		// res wird die rownummer ermittelt auf die geklickt worden ist
		// wir haben ja nur den Algostring

		return (optAlgo_glob.holeOptimizeResultindex(algostring,optoalgoname));
	}
}
