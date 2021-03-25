package swtHilfsfenster;

import hiflsklasse.FileAccess;
import hiflsklasse.SG;
import hiflsklasse.Tracer;

import java.util.ArrayList;
import java.util.HashSet;

import montool.MonDia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import StartFrame.Tableview;

import com.cloudgarden.resource.SWTResourceManager;

import data.GlobalVar;
import data.Marklines;
import data.Metaconfig;
import data.Profit;
import data.Rootpath;
import data.Tradeliste;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/* allgemeine gui klasse um die Trades zwischen gleichen magics zu vergleichen */

public class SwtCompareTradelist
{
	{
		// Set Look & Feel
		try
		{
			javax.swing.UIManager
					.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	int exitflag = 0;
	private Table table1;
	private Display display_glob = null;
	private Profit profelem_glob = null;
	private Combo combo1magic;
	private Combo combo2broker;
	private Combo combo2magic;
	private Label label1;
	private Button button1glzeitraum;
	private Text text1days;
	private Text text1bis;
	private Text text1von;
	private Label brokerlabel;
	private Table table2;
	private Tableview tv_glob = null;
	// Broker, magic links
	private int magic1_glob = 0;
	private String broker1_glob = null;
	private Button button1setlotsize2;
	private Text lotsize2;
	private Button button1setlotsize1;
	private Text lotsize1;
	// broker und magic rechts
	private int magic2_glob = 0;
	private String broker2_glob = null;
	private Image img_links_glob = null;
	private Image img_rechts_glob = null;
	private Tradeliste etl_links_glob = null;
	private Tradeliste etl_rechts_glob = null;
	private Text text1namerechts;
	private Button button1refresh;
	private Label label4;
	private Text text1prozfit;
	private Label label3;
	private Text text1minutentolleranz_glob=null;;
	private Label label2;
	private Text gmt;

	private ArrayList<Integer> maglist_glob = null;

	public void init(Display dis, Profit profelem, Tableview tv,
			ArrayList<Integer> mlist)
	{
		// hier wird noch ne Magicliste übergeben
		// für diese magicliste wird noch ein comboelement aufgemacht
		// dis: dies ist das allgemeine Display
		// profelem: dies ist das selektierte Profitelement, das braucht man für
		// den Brokernamen
		// tv: tableview, ist eine hilfsklasse fuer den view
		// mlist: ist die gesammtliste der selektierten magics

		if (profelem == null)
		{
			Tracer.WriteTrace(10, "E:No Broker selected");
			return;
		}
		profelem_glob = profelem;
		broker1_glob = profelem.getBroker();
		broker2_glob = broker1_glob;
		magic1_glob = profelem.getMagic();
		magic2_glob = profelem.getMagic();
		tv_glob = tv;
		display_glob = dis;
		maglist_glob = mlist;
		Shell sh = new Shell(dis);

		{
			// Register as a resource user - SWTResourceManager will
			// handle the obtaining and disposing of resources
			SWTResourceManager.registerResourceUser(sh);
		}

		sh.setLayout(null);
		String eainfostring = tv_glob.getComment(profelem.getMagic());

		sh.pack();
		sh.setSize(1833, 938);
		sh.addPaintListener(new PaintListener()
		{
			public void paintControl(PaintEvent evt)
			{
				shPaintControl(evt);
			}
		});
		{
			table1 = new Table(sh, SWT.FULL_SELECTION | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER);
			table1.setBounds(32, 244, 878, 494);
			table1.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
			table1.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					table1WidgetSelected(evt);
				}
			});
		}
		{
			table2 = new Table(sh, SWT.FULL_SELECTION | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER);
			table2.setBounds(930, 244, 868, 494);
			table2.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
			table2.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					table2WidgetSelected(evt);
				}
			});
		}
		{
			combo2broker = new Combo(sh, SWT.NONE);
			combo2broker.setText(broker2_glob);
			combo2broker.setBounds(946, 755, 199, 33);

			combo2broker.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					combo2brokerselected(evt);
				}
			});
		}
		{
			brokerlabel = new Label(sh, SWT.BORDER);
			brokerlabel.setBounds(32, 763, 117, 28);
		}
		{
			text1von = new Text(sh, SWT.NONE);
			text1von.setBounds(32, 803, 67, 22);
		}
		{
			text1bis = new Text(sh, SWT.NONE);
			text1bis.setBounds(104, 803, 57, 22);
		}
		{
			text1days = new Text(sh, SWT.NONE);
			text1days.setBounds(167, 803, 36, 22);
		}
		{
			label1 = new Label(sh, SWT.NONE);
			label1.setText("days");
			label1.setBounds(215, 803, 46, 22);
		}
		{
			button1glzeitraum = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1glzeitraum.setText("set same time");
			button1glzeitraum.setBounds(273, 795, 170, 30);
		}
		{
			combo2magic = new Combo(sh, SWT.NONE);
			combo2magic.setText(String.valueOf(profelem_glob.getMagic()));
			combo2magic.setBounds(1169, 755, 225, 33);
			combo2magic.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					combo2magicSelected(evt);
				}
			});
		}
		{
			combo1magic = new Combo(sh, SWT.NONE);
			combo1magic.setText("magic");
			combo1magic.setBounds(155, 763, 118, 28);
			combo1magic.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					combo1magicSelected(evt);
				}
			});
		}
		{
			lotsize1 = new Text(sh, SWT.NONE);
			lotsize1.setText(tv_glob.getLotsize_str(broker1_glob, magic1_glob));
			lotsize1.setBounds(32, 837, 123, 20);
		}
		{
			button1setlotsize1 = new Button(sh, SWT.PUSH | SWT.CENTER);
			button1setlotsize1.setText("set lotsize");
			button1setlotsize1.setBounds(167, 837, 118, 33);
			button1setlotsize1.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1setlotsizeWidgetSelected(evt);
				}
			});
		}
		{
			lotsize2 = new Text(sh, SWT.NONE);
			lotsize2.setBounds(799, 829, 111, 20);
		}
		{
			button1setlotsize2 = new Button(sh, SWT.PUSH | SWT.CENTER);
			button1setlotsize2.setText("set lotsize");
			button1setlotsize2.setBounds(922, 829, 85, 28);
			button1setlotsize2.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1setlotsize2WidgetSelected(evt);
				}
			});
		}
		{
			gmt = new Text(sh, SWT.NONE);
			gmt.setText("0");
			gmt.setBounds(1412, 763, 36, 22);
		}
		{
			label2 = new Label(sh, SWT.NONE);
			label2.setText("gmtdiff");
			label2.setBounds(1466, 763, 123, 29);
		}
		{
			text1minutentolleranz_glob= new Text(sh, SWT.NONE);
			text1minutentolleranz_glob.setText("5");
			text1minutentolleranz_glob.setBounds(1412, 803, 31, 22);
		}
		{
			label3 = new Label(sh, SWT.NONE);
			label3.setText("minutes toleranz");
			label3.setBounds(1466, 798, 161, 20);
		}
		{
			text1prozfit = new Text(sh, SWT.NONE);
			text1prozfit.setText("0");
			text1prozfit.setBounds(780, 761, 33, 22);
			text1prozfit.setEditable(false);
		}
		{
			label4 = new Label(sh, SWT.NONE);
			label4.setText("% fit");
			label4.setBounds(816, 761, 94, 22);
		}
		{
			button1refresh = new Button(sh, SWT.PUSH | SWT.CENTER);
			button1refresh.setText("refresh");
			button1refresh.setBounds(1636, 818, 152, 30);
			button1refresh.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1refreshWidgetSelected(evt);
				}
			});
		}
		{
			text1namerechts = new Text(sh, SWT.NONE);
			text1namerechts.setText("text1");
			text1namerechts.setBounds(297, 837, 613, 22);
			text1namerechts.setEditable(false);
		}
		sh.open();
		// init die comboliten
		init();
		// magics für die erste comboliste berechnen
		calcCombo1Magiclist(magic1_glob, maglist_glob, combo1magic);
		// magics für die rechte comboliste bestimmen
		calcCombo2Brokerlist(magic1_glob, combo2broker);
		// baut die einzeltradeliste links auf
		showEinzelTradesLinks(text1prozfit);
		showEinzelTradesRechts(text1prozfit);//xxx

		// holt für einen broker+magic die lotsize und zeigt an
		String lotsize_str = tv_glob.getLotsize_str(broker2_glob, magic2_glob);
		lotsize2.setText(lotsize_str);

		// event-schleife
		while ((!sh.isDisposed()) && (exitflag == 0))
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		// sh.close();
	}

	private void init()
	{
		// die rechte combobox aufbauen mit den magics
		int anz = maglist_glob.size();
		for (int i = 0; i < anz; i++)
		{
			combo2magic.add(String.valueOf(maglist_glob.get(i)));
		}
	}

	private void calcCombo2Brokerlist(int magic, Combo combo2broker)
	{
		// die brokerliste für die rechte combobox wird berechnet
		// Es wird gesucht wo überall die magic noch vorkommt
		ArrayList<String> bl = tv_glob.calcBrokerliste(magic);

		int anz = bl.size();

		for (int i = 0; i < anz; i++)
		{
			combo2broker.add(bl.get(i));
		}

		// dann noch das Schlüsselwort Backtest aufnehmen
		combo2broker.add("Backtest");
		combo2broker.select(0);
		broker2_glob = bl.get(0);
	}

	private void calcCombo1Magiclist(int magic, ArrayList<Integer> maglist,
			Combo combo1magic)
	{
		int anz = maglist.size();
		for (int i = 0; i < anz; i++)
		{
			combo1magic.add(String.valueOf(maglist.get(i)));
		}
		// für die Magic wird die Liste der Backtests erstellt
		// die Backtests müssen sich in den Verzeichniss Root/backtests befinden

		ArrayList<String> al = BuildFilelist(Rootpath.getRootpath()
				+ "\\backtests", magic);
		anz = al.size();
		for (int i = 0; i < anz; i++)
		{
			combo1magic.add(al.get(i));
		}
		combo2broker.select(0);
	}

	private ArrayList<String> BuildFilelist(String path, int magic)
	{
		ArrayList<String> fl = new ArrayList<String>();
		FileAccess.initFileSystemList(path, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			fnam = fnam.replaceAll(".", "");

			String magnam = String.valueOf(magic);
			if (fnam.contains(" " + magnam + "str") == true)
				fl.add(path + "\\" + fnam);
		}
		return fl;
	}

	// tradeanzeige
	private void showEinzelTradesLinks(Text text1prozfit)
	{
		Tradeliste etl = tv_glob.buildTradeliste(String.valueOf(magic1_glob), broker1_glob,-1);
		//global zuweisen
		etl_links_glob=etl;
		refresh_links(text1prozfit);
		refresh_rechts(text1prozfit);
	}
	
	private void refresh_links(Text prozgleich)
	{
		if(etl_links_glob==null)
			return;
		
		// berechne die Marklines struktur neu
		Marklines ml = new Marklines();
		ml.initMark(etl_links_glob, etl_rechts_glob, 0,Integer.valueOf(text1minutentolleranz_glob.getText()),prozgleich);
		tv_glob.showEinzelTradeliste(etl_links_glob, display_glob, table1, ml, 0
				, null,
				null);

		brokerlabel.setText(broker1_glob);
		String fnam = tv_glob.calcTradepicture(etl_links_glob.getelem(0).getComment(), 1,
				etl_links_glob);

		if (fnam.contains("???"))
			return;
		GC gc = new GC(display_glob.getActiveShell());
		img_links_glob = new Image(display_glob, fnam);

		if(GlobalVar.getSilentmode()==0)
			gc.drawImage(img_links_glob, 0, 0, 640, 300, 0, 0, 600, 250);
		System.out.println();
	}

	private void showEinzelTradesRechts(Text prozgleich)
	{
		// Erst mal die Tradeliste für Rechts aufbauen
		
		
		int channel=tv_glob.getChannel(broker1_glob);
		Tradeliste etl = tv_glob.buildTradeliste(String.valueOf(magic2_glob), broker2_glob,channel);
		if(etl.getsize()==0)
		{
			Tracer.WriteTrace(10, "Info: no trades on broker <"+broker2_glob+"> for magic <"+magic2_glob+">");
			return;
		}
		//global zuweisen
		etl_rechts_glob=etl;
		text1namerechts.setText(Tradeliste.getTradelistenam());
		refresh_rechts(prozgleich);
		refresh_links(prozgleich);
	}
	private void refresh_rechts(Text prozgleich)
	{
		if(etl_rechts_glob==null)
			return;
		//marklines sind die zeilen die farblich markiert sind
		Marklines ml = new Marklines();
		//im init werden die farben in der internen struktur gesetzt
		ml.initMark(etl_links_glob, etl_rechts_glob,
				Integer.valueOf(gmt.getText().toString()),Integer.valueOf(text1minutentolleranz_glob.getText()),prozgleich);
		tv_glob.showEinzelTradeliste(etl_rechts_glob, display_glob, table2, ml, 1, null,
				null);

		if (etl_rechts_glob.getsize() == 0)
			return;

		// den panel rechts neu berechnen
		String fnam = tv_glob.calcTradepicture(etl_rechts_glob.getelem(0).getComment(), 1,
				etl_rechts_glob);
		GC gc = new GC(display_glob.getActiveShell());
		img_rechts_glob = new Image(display_glob, fnam);
		if(GlobalVar.getSilentmode()==0)
			//gc.drawImage(img_rechts_glob, 0, 0, 640, 300, 700, 0, 640, 240);
			gc.drawImage(img_rechts_glob, 0, 0, 640, 300, 400, 0, 600, 230);
	}

	private void table1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table1.widgetSelected, event=" + evt);
		// TODO add your code for table1.widgetSelected
		// table 1 auf ein Element geklickt
		// !! wird im Augenblick nicht ausgewertet !!
		
		/*
		 * String input = (String) evt.toString(); String index =
		 * input.substring(input.indexOf("TableItem {") + 11); index =
		 * index.substring(0, index.indexOf("}")); int
		 * indexval=SG.get_zahl(index);
		 * 
		 * 
		 * HashSet<Integer> marklines = new HashSet<Integer>();
		 * marklines.add(indexval); Tradeliste etl =
		 * tv_glob.buildTradeliste(magic2_glob, broker2_glob);
		 * tv_glob.showEinzelTradeliste(etl,display_glob, table1, magic1_glob,
		 * broker1_glob,marklines); table1.setSelection(indexval);
		 */
	}

	private void button1refreshWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1refresh.widgetSelected, event=" + evt);
		// TODO add your code for button1refresh.widgetSelected
		showEinzelTradesLinks(text1prozfit);
		showEinzelTradesRechts(text1prozfit);//xxx
	}

	// combo selektion
	private void combo2brokerselected(SelectionEvent evt)
	{
		//combo2 ist die rechte seite
		System.out.println("combo1.widgetSelected, event=" + evt);
		// In der rechten combobox  wurde ein broker angewählt
		// ermittle den broker
		// SelectionEvent{Combo {ActiveTrades4} time=40150585 data=null
		// item=null detail=0 x=0 y=0 width=0 height=0 stateMask=0 text=null
		// doit=true}
		String input = (String) evt.toString();
		String broker = input.substring(input.indexOf("Combo {") + 7);
		broker = broker.substring(0, broker.indexOf("}"));
		broker2_glob = broker;
		showEinzelTradesRechts(text1prozfit);

		//falls der broker backtest heisst, dann git es keine lotsize
		if (broker.toLowerCase().contains("backtest"))
			lotsize2.setText("Backtest");
		else
		{
			// lotsize vom broker holen
			// zeige hierfür auch die lotsize an
			String lotsize_str = tv_glob.getLotsize_str(broker2_glob,
					magic2_glob);
			lotsize2.setText(lotsize_str);
		}
	}

	private void combo2magicSelected(SelectionEvent evt)
	{
		//eine magic wurde in der rechten combobox ausgewählt
		System.out.println("combo2.widgetSelected, event=" + evt);
		// Combo 2 wurde selektiert
		String input = (String) evt.toString();
		String magic = input.substring(input.indexOf("Combo {") + 7);
		magic = magic.substring(0, magic.indexOf("}"));
		magic2_glob = SG.get_zahl(magic);
		showEinzelTradesRechts(text1prozfit);

		
		// zeige hierfür auch die lotsize an
		String lotsize_str = tv_glob.getLotsize_str(broker2_glob, magic2_glob);
		lotsize2.setText(lotsize_str);
	}

	private void combo1magicSelected(SelectionEvent evt)
	{
		//combo1 ist die linke seite
		// eine neue Magic wurde in der combobox links ausgewählt
		System.out.println("combo1magic.widgetSelected, event=" + evt);
		String input = (String) evt.toString();
		String magic = input.substring(input.indexOf("Combo {") + 7);
		magic = magic.substring(0, magic.indexOf("}"));
		magic1_glob = SG.get_zahl(magic);
		showEinzelTradesLinks(text1prozfit);

		// zeige hierfür auch die lotsize an
		String lotsize_str = tv_glob.getLotsize_str(broker2_glob, magic2_glob);
		lotsize2.setText(lotsize_str);
	}

	private void table2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table2.widgetSelected, event=" + evt);
		// TODO add your code for table2.widgetSelected
		// Auf table 2 wurde geklickt
		// !! wird aber nicht ausgewertet im Moment !!
		String input = (String) evt.toString();
		String index = input.substring(input.indexOf("TableItem {") + 11);
		index = index.substring(0, index.indexOf("}"));
		int indexval = SG.get_zahl(index);
		table2.setSelection(indexval);
	}

	private void buttonLoadTradelistWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttonLoadTradelist.widgetSelected, event=" + evt);
		// TODO add your code for buttonLoadTradelist.widgetSelected
		// load tradelist(tradelist vom GeneticBuilder einlesen)
		String filename = MonDia.FileDialog(display_glob, "c:\\");
	

		// jetzt die Tradeliste noch einlesen und anzeigen
		Tradeliste trl = new Tradeliste(null);

		// den Backtest aus dem GB laden
		trl.initGBcsvBacktest(filename);
		trl.calcSummengewinne();

		// den backtest in table 2 anzeigen
		tv_glob.showEinzelTradeliste(trl, display_glob, table2, null, 0, null,
				null);

	}

	
	
	private void button1setlotsizeWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setlotsize.widgetSelected, event=" + evt);

		// button set lotsize1
		String lots = lotsize1.getText();
		tv_glob.setLotsize(broker1_glob, magic1_glob, Float.valueOf(lots));
	}

	private void button1setlotsize2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setlotsize2.widgetSelected, event=" + evt);
		// TODO add your code for button1setlotsize2.widgetSelected
		// set lotsize 2 ausgewählt
		String lots = lotsize2.getText();
		tv_glob.setLotsize(broker2_glob, magic2_glob, Float.valueOf(lots));
	}

	private void shPaintControl(PaintEvent evt)
	{
		System.out.println("sh.paintControl, event=" + evt);
		// repaint
		Shell sh=null;
		
		try{
		sh=display_glob.getActiveShell();
		
		
		GC gc = new GC(sh);
		if (img_links_glob != null)
			//gc.drawImage(img_rechts_glob, 0, 0, 640, 300, 400, 0, 640, 230);
			gc.drawImage(img_links_glob, 0, 0, 640, 300, 0, 0, 900, 230);
		if (img_rechts_glob != null)
			gc.drawImage(img_rechts_glob, 0, 0, 640, 300, 900, 0, 900, 230);
		}catch (Exception e)
		{
			Tracer.WriteTrace(20, "E:exception e=<"+e.getMessage()+">");
			return;
		}
	}

	private void button1markWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1mark.widgetSelected, event=" + evt);
		// wenn der button mark gedrückt wird werden gleiche Trades markiert
		// wenn button gedrückt dann schaue in den beiden globalen Listen (sind
		// noch als global zu definieren)
		// welche trades gleich sind und markiere die positionen in den hashsets
		
	}
}
