package work1;


import gui.Mbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import jhilf.ColoredTableCellRenderer;
import charttool.HourChart;
import charttool.WeekdayChart;
import data.Config;
import data.EaMasterPatcher;
import data.IndiWorker;
import data.OGC;
import data.OptimizeResult;
import data.OptimizeResultliste;
import data.PanelResult;
import data.Timefilter;
import data.Tradeliste;

public class FrameWorker
{
	// Diese Klasse ist eine Sammlung von Fuktionen die für ein bestimmtes frame
	// Zuständig ist
	// Der FrameWorker verwendet den GuiWorker

	// hier ist das weitere connectete slave frameworke
	private FrameWorker slaveFrameworker_glob = null;

	// hier ist das frame wo die orginal graphik drin ist
	private JInternalFrame frameorginal_glob = null;
	// dies frame hier ist das resultframe, hier ist die ergebnissliste drin
	private JInternalFrame ergebnisslistenframe_glob = null;

	private JInternalFrame multioutputframe_glob = null;
	// dies ist das weekdayframe mit den wochentagen
	private JInternalFrame weekdayframe_glob = null;
	// zugehörige ScrollPane
	private JScrollPane jscrollpane_glob = null;
	// dies ist die zugehörige Arbeitsklasse
	private GuiWorker worker_glob = null;

	private Timefilter timefilter_glob = null;

	private JTable jTable_glob;

	private JCheckBox checkboxweekday_glob = null;
	private JCheckBox checkboxtime_glob = null;
	private JTextField indiminmaxdate_glob = null;

	// dies ist die selektierte quellstrategie auf die ein filter angewendet
	// wird
	private JLabel jlabelquelle_glob = null;
	// dies ist der name des selektierten Filters der angewählt worden ist
	private JLabel jlabelfilter_glob = null;

	// die ausgewählten Filter zwischenspeichern
	private String choosenFilter = null;

	private int panelindex_glob = 0;
	private JProgressBar jProgressBar_glob = null;

	private JInternalFrame indikatorframe_glob = null;
	private JLabel indikatorlabel1_glob = null;
	private JTextArea jTextArea1_glob = null;
	private JLabel indikatorlabel2_glob = null;
	private JTextArea jTextArea2_glob = null;
	private IndiWorker inwork_glob= new IndiWorker(Config.getRootdir()+"\\data\\"+OGC.filterqmlfilename);

	//dies sind die verwendeten opti-Filter
	private String resname1_glob=null;
	private String resname2_glob=null;
	
	//dies ist der selektierte Resultname und optoalgoname
	private String resultname_glob=null;
	private String optoalgoname_glob=null;
	
	public FrameWorker(JInternalFrame listenframe, JScrollPane jscrollpane1,
			JInternalFrame frameorginal, JInternalFrame frameoutput,
			JInternalFrame weekdayframe, GuiWorker worker,
			Timefilter timefilter, JCheckBox jCheckBox1weekday,
			JCheckBox jCheckBox1time, JTextField jTextField2indiMinMaxDate,
			JLabel jlabelquelle, JLabel jlabelfilter, int panelindex,
			JInternalFrame indikatorframe, JLabel indikatorlabel1,
			JTextArea jTextArea1, JLabel indikatorlabel2,
			JTextArea jTextArea2)
	{
		// ist die zugehörige scrollpane zum frame
		jscrollpane_glob = jscrollpane1;
		// dies ist das frame mit der liste der Ergebnisse
		ergebnisslistenframe_glob = listenframe;
		// dies ist das multi outputframe wo die ergebniss equitykurven
		// eingezeichnet werden
		multioutputframe_glob = frameoutput;
		// dies ist das frame mit der weekday/time-graphik
		weekdayframe_glob = weekdayframe;
		// dies ist das Frame mit der orginal graphic
		frameorginal_glob = frameorginal;
		// hier wird auf die workerklasse verwiesen
		worker_glob = worker;
		timefilter_glob = timefilter;
		checkboxweekday_glob = jCheckBox1weekday;
		checkboxtime_glob = jCheckBox1time;
		indiminmaxdate_glob = jTextField2indiMinMaxDate;
		jlabelquelle_glob = jlabelquelle;
		jlabelfilter_glob = jlabelfilter;
		panelindex_glob = panelindex;
		indikatorframe_glob = indikatorframe;
		indikatorlabel1_glob = indikatorlabel1;
		jTextArea1_glob = jTextArea1;
		indikatorlabel2_glob = indikatorlabel2;
		jTextArea2_glob = jTextArea2;
		
	
	}

	public void setSlaveFramework(FrameWorker sl)
	{
		slaveFrameworker_glob = sl;
	}

	public void setChoosenFilter(String filternames)
	{
		// die gewählten filter speichern
		choosenFilter = filternames;

	}

	public String getChoosenFilter()
	{
		return choosenFilter;

	}

	public void jButtonClearActionPerformed()
	{
		// der Clear Button wurde betätigt

		// clear button gedrückt
		if (worker_glob != null)
			worker_glob.clear();

		weekdayframe_glob.getContentPane().removeAll();
		weekdayframe_glob.updateUI();

		OptimizeRefreshListe();
	}

	public void jButton1optimizeActionPerformed(ActionEvent evt)
	{
		// OPTIMIZE1 Der optimize Button1 wurde gedrückt
		System.out.println("jButton1optimize.actionPerformed, event=" + evt);

		//Hier werden alle otimierungen gestartet
		worker_glob.optimizeAll(timefilter_glob);

		// die Tabelle mit den Ergebnissen aufbauen und Anzeigen
		OptimizeRefreshListe();
	}

	public OptimizeResultliste HoleOptimizeResultliste()
	{
		return(worker_glob.HoleOptimizeResultliste());
	}
	public Tradeliste HoleTradelisteOrginal()
	{
		return(worker_glob.HoleTradelisteOrginal());
	}
	
	public void jButton1optimizeAND_ActionPerformed(ActionEvent evt,Tradeliste tradeliste,OptimizeResultliste optresultliste)
	{
		//hier werden auf allen ergebnissen von F1 nochmal alle indikatoren angewendet (Optimize AND)
		//hole eine tradeliste aus der tradelistenliste
		
		int anz=optresultliste.getSize();
		
		for (int i=1; i<6; i++)
		{
			//lader die masterstrategie die optimiert werden soll
			OptimizeResult or= optresultliste.getElem(i);

			//baue aus der Tradeliste und dem optimize Result eine neue Tradeliste und lade die
			Tradeliste opttr=tradeliste.getOptimizedTradelist(or);
			worker_glob.LoadQuellStrategy(opttr,0);
		
			worker_glob.optimizeAll(timefilter_glob);

			// die Tabelle mit den Ergebnissen aufbauen und Anzeigen
			OptimizeRefreshListe();
		}
	}
	
	public void MausWurdegeklickt(MouseEvent evt)
	{
		// es wurde eine optimierte Strategie aus der Ergebnissliste angeklickt
		System.out.println("jTable3.mouseClicked, event=" + evt);
		// TODO add your code for jTable5.mouseClicked
		int row = jTable_glob.getSelectedRow();
		String resultname = (String) jTable_glob.getValueAt(row, 1);
		String optoalgoname=(String) jTable_glob.getValueAt(row, 7);
		resultname_glob=resultname;
		optoalgoname_glob=optoalgoname;

		System.out.println("row=" + row + " value=" + resultname);
		int sortedrow = worker_glob.calcSortedRow(resultname,optoalgoname);

		String clickedstratname = worker_glob.table3Clicked(resultname,optoalgoname);

		// das Frame2 löschen
		multioutputframe_glob.getContentPane().removeAll();

		// das Result holen
		PanelResult pr = worker_glob.holePanelResult(resultname,optoalgoname,				timefilter_glob, panelindex_glob);
		multioutputframe_glob.getContentPane().add(pr.getPanel(),
				BorderLayout.CENTER);

		int gdx = pr.getGdx();

		// die Tradeliste im worker als aktuelle selektieren
		worker_glob.setSelTradeliste(resultname, optoalgoname,timefilter_glob);

		// an den slaveFrameworker die Daten weiterreichen
		slaveFrameworker_glob.worker_glob.LoadQuellStrategy(this
				.getSelTradeliste(),0);
		slaveFrameworker_glob.jlabelquelle_glob.setText(clickedstratname);

		// die orginalstrategie mit dem optimierten gdx einzeichnen
		frameorginal_glob.getContentPane().removeAll();
		JPanel jp = worker_glob.GenQuellStratJPanel(gdx);
		frameorginal_glob.getContentPane().add(jp, BorderLayout.CENTER);
		jp.setPreferredSize(new java.awt.Dimension(372, 283));

		// den wochentag im jpanel2 anzeigen
		Tradeliste tl = worker_glob.holeResultTradeliste(resultname,optoalgoname,
				timefilter_glob);
		// das panel generieren
		JPanel tradespanel = null;
		if (checkboxweekday_glob.isSelected() == true)
		{
			WeekdayChart wc = new WeekdayChart();
			tradespanel = wc.genPanel("weekday (trades)", tl, "weekday");

		} else if (checkboxtime_glob.isSelected() == true)
		{
			HourChart wc = new HourChart();
			tradespanel = wc.genPanel("starthour (trades)", tl, "starthour");
		}
		// min-maxdate angeben
		worker_glob.calcMinMaxDateOptStrat(indiminmaxdate_glob,clickedstratname,optoalgoname,
				timefilter_glob);

		// das wochenpanel im frame anzeigen
		weekdayframe_glob.getContentPane().removeAll();
		if (tradespanel != null)
			weekdayframe_glob.getContentPane().add(tradespanel,
					BorderLayout.CENTER);

		// den angeklickten indikator 'resultname'im indikatorframe anzeigen
		showResultgraphikIndikator( resultname);
		this.updateGuis();
		slaveFrameworker_glob.updateGuis();
	}

	private void showResultgraphikIndikator(String resultname)
	{
		//indikatorgraphik ins jpanel laden
		String resname=resultname.substring(resultname.indexOf("<")+1,resultname.indexOf(">"));
		//resname=0020a, 0020b
		String resname1=resname.substring(0,resname.indexOf(","));
		String resname2=resname.substring(resname.indexOf(" ")+1);
		
		String filename1 = Config.getRootdir()+"\\data\\picture\\"+resname1+".gif";
		ImageIcon icon = new ImageIcon(filename1,"blup");
		indikatorlabel1_glob.setIcon(icon);
		
		String filename2 = Config.getRootdir()+"\\data\\picture\\"+resname2+".gif";
		ImageIcon icon2 = new ImageIcon(filename2,"blup");
		indikatorlabel2_glob.setIcon(icon2);
		
		IndiWorker inwork_glob= new IndiWorker(Config.getRootdir()+"\\data\\"+OGC.filterqmlfilename);
		
		//text aus file extrahieren
		jTextArea1_glob.setText(inwork_glob.getTextSection(resname1));
		jTextArea2_glob.setText(inwork_glob.getTextSection(resname2));
		resname1_glob=resname1;
		resname2_glob=resname2;
	}
	
	public void modifySaveEa(String quellname)
	{
		System.out.println("test");
		int timeframe=0;
		
		//den timeframe ermitteln
		if(optoalgoname_glob.contains("-M1."))
			timeframe=1;
		else if(optoalgoname_glob.contains("-M5."))
			timeframe=5;
		else if(optoalgoname_glob.contains("-M15."))
			timeframe=15;
		else if(optoalgoname_glob.contains("-M30."))
			timeframe=30;	
		else if(optoalgoname_glob.contains("-H1."))
			timeframe=60;
		else if(optoalgoname_glob.contains("-H4."))
			timeframe=240;
		else if(optoalgoname_glob.contains("-D1."))
			timeframe=1440;
		else
			Mbox.Infobox("unknown timeframe<"+optoalgoname_glob+">");
		
		//den quellnamen laden
		String qname=quellname.substring(0,quellname.lastIndexOf("."));
		EaMasterPatcher em= new EaMasterPatcher(Config.getRootdir()+"\\data\\"+OGC.filterqmlfilename,qname+".mq4");
		em.patchEa(worker_glob.getSelStratName(),resname1_glob, resname2_glob,timeframe);
	}
	
	public void OptimizeRefreshListe()
	{
		// Ergebnissliste1 wird refreshed
		// In der Ergebnissliste stehen die gefundenen Ergebnisse der
		// Optimierung

		// falls kein worker, dann ist eh alles leer
		if (worker_glob == null)
			return;

		// Tabelle aufbauen
		jTable_glob = worker_glob.GenOptimizeTable();

		if (jTable_glob == null)
			return;

		// nur eine Zeile selektierbar
		jTable_glob.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// die Zeilen und Spalten sind nicht editierbar
		jTable_glob.setEditingColumn(0);
		jTable_glob.setEditingRow(0);

		// Die tabellen einfärben
		DefaultTableCellRenderer ren = new ColoredTableCellRenderer();
		if (panelindex_glob == 0)
			jTable_glob.setGridColor(Color.blue);
		if (panelindex_glob == 1)
			jTable_glob.setGridColor(Color.green);
		if (panelindex_glob == 2)
			jTable_glob.setGridColor(Color.red);
		if (panelindex_glob == 3)
			jTable_glob.setGridColor(Color.black);

		// das Ergebniss im scroller aufnehmen und anzeigen anzeigen
		jscrollpane_glob.setViewportView(jTable_glob);

		// den Mauslistener setzen
		jTable_glob.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				jTable1MouseClicked(evt);
			}
		});

		ergebnisslistenframe_glob.updateUI();
	}

	private void jTable1MouseClicked(MouseEvent evt)
	{
		// es wurde eine optimierte Strategie aus der Ergebnissliste angeklickt
		System.out.println("jTable3.mouseClicked, event=" + evt);
		MausWurdegeklickt(evt);
	}

	public String getSelStratName()
	{
		// der name der selektierten Strategie wird zurückgegeben
		return (worker_glob.getSelStratName());
	}

	public Tradeliste getSelTradeliste()
	{
		return (worker_glob.getSelTradeliste());
	}

	public void selektiereStrategie(FrameWorker frameworkerquelle)
	{
		jlabelquelle_glob.setText("Strat="
				+ frameworkerquelle.getSelStratName());
	}

	public void showFiltername(String name)
	{
		jlabelfilter_glob.setText("Filter=" + name);
	}

	public void loadQuellStrategie(FrameWorker frameworkerquelle)
	{
		worker_glob.LoadQuellStrategy(frameworkerquelle.getSelTradeliste(),1);
		updateGuis();
		frameworkerquelle.updateGuis();
	}

	public void updateGuis()
	{
		frameorginal_glob.updateUI();
		ergebnisslistenframe_glob.updateUI();
		multioutputframe_glob.updateUI();
		weekdayframe_glob.updateUI();
	}

}
