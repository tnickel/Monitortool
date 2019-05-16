package swtOberfläche;

import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsrepeattask.UeberpruefeAlleKursePlausi;
import kurse.KurseDB;
import mainPackage.GC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.ThreadsDB;
import swtViewer.Viewer;

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
public class Tab1ui extends Swttool
{
	static private Table table1;
	static private Button KursUeberpruefung;
	static private Button show38;
	static private Button button1;
	static private Button button1Checkonly;
	static private Text Monitor;
	static private Label label1;
	static private Text threshold;
	static private Button Schnellflag;
	static private Button Forceflag;
	static private Group group2;
	static private Button button1Lade38200;
	static private Group group1;
	static private Group groupKurse_g = null;
	static private ProgressBar progressBar1;
	static private Display display_glob = null;
	static private TabFolder folder_glob = null;
	static private ThreadsDB tdb_glob=null;
	static private KurseDB kdb_glob=null;
	static private AktDB aktdb_glob=null;
	
	static public void init(TabFolder folder, TabItem tab, Display dis,ThreadsDB tdb,KurseDB kdb,AktDB aktdb)
	{
		display_glob = dis;
		folder_glob = folder;
		tdb_glob=tdb;
		kdb_glob = kdb;
		aktdb_glob=aktdb;

		folder.setLayout(new GridLayout());
		// AllgemeinGroup erstellen

		// ProxyGroup erstellen
		groupKurse_g = new Group(folder, SWT.NONE);
		groupKurse_g.setText("Kurse");
		groupKurse_g.setLayout(null);

		tab.setControl(groupKurse_g);
		// Inhalt des Group erstellen

		groupKurse_g.pack();
		{
			GridData ConfiglisteLData = new GridData();
			ConfiglisteLData.widthHint = 196;
			ConfiglisteLData.heightHint = 228;
		}

		groupKurse_g.pack();
		groupKurse_g.setSize(1273, 709);
		{
			progressBar1 = new ProgressBar(groupKurse_g, SWT.NONE);
			progressBar1.setBounds(12, 537, 1247, 15);
		}
		{
			group1 = new Group(groupKurse_g, SWT.NONE);
			group1.setLayout(null);
			group1.setBounds(12, 564, 270, 165);
			{
				button1Lade38200 = new Button(group1, SWT.PUSH | SWT.CENTER);
				button1Lade38200.setText("LadeAlleKurse Calc 38200");
				button1Lade38200.setBounds(0, 48, 180, 30);
				button1Lade38200.setToolTipText("a) sucht alle 38/200 Kurse und sortiert anschliessend die tdb nach dem Steigungsfaktor\r\nb) danach wird in db/reporting/gregor.csv eine exel-Übersichtsliste über die Steigungen erstellt\r\n\r\nOutput:\r\nExelliste: \\\\db\\\\reporting\\\\gregor.csv\r\nHtmlliste: \\\\db\\\\reporting\\\\38200Kurse");
				button1Lade38200.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						Actionbutton1Lade38200(evt);
					}
				});
			}
			{
				KursUeberpruefung = new Button(group1, SWT.PUSH | SWT.CENTER);
				KursUeberpruefung.setText("KursUeberpruefung");
				KursUeberpruefung.setBounds(0, 84, 140, 30);
				KursUeberpruefung.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						KursUeberpruefungWidgetSelected(evt);
					}
				});
			}
			{
				group2 = new Group(group1, SWT.EMBEDDED);
				StackLayout group2Layout = new StackLayout();
				group2Layout.topControl = null;
				group2.setLayout(group2Layout);
				group2.setBounds(162, 159, 163, 164);
			}
			{
				button1Checkonly = new Button(group1, SWT.CHECK | SWT.LEFT);
				button1Checkonly.setText("Checkonly");
				button1Checkonly.setBounds(146, 84, 106, 30);
				button1Checkonly.setSelection(true);
			}
			{
				button1 = new Button(group1, SWT.PUSH | SWT.CENTER);
				button1.setText("ZeigeAlleKurse");
				button1.setBounds(0, 12, 264, 30);
				button1.addSelectionListener(new SelectionAdapter() 
				{
					public void widgetSelected(SelectionEvent evt) 
					{
						ActionZeigeAlleKurse(evt);
					}
				});
			}
			{
				show38 = new Button(group1, SWT.PUSH | SWT.CENTER);
				show38.setText("Show38200");
				show38.setBounds(180, 48, 84, 30);
				show38.addSelectionListener(new SelectionAdapter() 
				{
					public void widgetSelected(SelectionEvent evt) 
					{
						ActionButton38(evt);
					}
				});
			}
		}
		{
			table1 = new Table(groupKurse_g, SWT.FULL_SELECTION | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER);
			table1.setBounds(14, 27, 1245, 504);
		}
		{
			Forceflag = new Button(groupKurse_g, SWT.CHECK | SWT.LEFT);
			Forceflag
					.setText("Forceflag (Lade auch defekte Symbole und suche Börse Force)");
			Forceflag.setBounds(300, 619, 445, 23);
			Forceflag.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					LadeAllesNeuWidgetSelected(evt);
				}
			});
		}
		{
			Schnellflag = new Button(groupKurse_g, SWT.CHECK | SWT.LEFT);
			Schnellflag
					.setText("Schnellflag (keine neuen Symbole aus Aktdb und Tdb)");
			Schnellflag.setBounds(300, 640, 450, 29);
			Schnellflag.setSelection(true);
			Schnellflag.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					SucheNeueSymbolWidgetSelected(evt);
				}
			});
		}
		{
			threshold = new Text(groupKurse_g, SWT.NONE);
			threshold.setBounds(296, 675, 25, 23);
			threshold.setText("30");
		}
		{
			label1 = new Label(groupKurse_g, SWT.NONE);
			label1.setText("Threshold Stunden");
			label1.setBounds(327, 675, 133, 23);
		}
		{
			Monitor = new Text(groupKurse_g, SWT.NONE);
			Monitor.setBounds(300, 575, 959, 27);

			if (GC.ONLINEMODE == 0)
				Monitor.setText("offlinemode !!!!!!!!");
			else
				Monitor.setText("ONLINEMODE");
		}
		folder.pack();
	}

	private static void Actionbutton1Lade38200(SelectionEvent evt)
	{
		Viewer table = new Viewer();
		table1.removeAll();
		table.viewTableKurseFile(table1, GC.rootpath+"\\db\\kurse.db");
		groupKurse_g.redraw();

		// hier werden die 38200 Kurse berechnet
		
		//KurseDB kdb = new KurseDB();
		String thres = threshold.getText();

		// läd alle kurse und baut kursedb neu auf
		boolean schnellfl = Schnellflag.getSelection();
		boolean forcefl = Forceflag.getSelection();
		int threshold = Tools.get_zahl(thres);
		Monitor.setText("Lade alle Kurse");
		wupdate(display_glob);

		if (GC.ONLINEMODE == 1)
			kdb_glob.ladeAlleKurseUpdateKurseDB(aktdb_glob,tdb_glob, threshold, schnellfl, forcefl,
					progressBar1,display_glob);
		Monitor.setText("Suche 38200");
		wupdate(display_glob);

		kdb_glob.suche38_200LimitsSortSteigung(1, progressBar1,display_glob);

		Monitor.setText("Fertig");
		wupdate(display_glob);
	}

	private static void LadeAllesNeuWidgetSelected(SelectionEvent evt)
	{
		System.out.println("LadeAllesNeu.widgetSelected, event=" + evt);
	}

	private static void SucheNeueSymbolWidgetSelected(SelectionEvent evt)
	{
		System.out.println("SucheNeueSymbol.widgetSelected, event=" + evt);
	}

	private static void KursUeberpruefungWidgetSelected(SelectionEvent evt)
	{
		int flag = 0;

		Viewer table = new Viewer();
		table1.removeAll();
		table.viewTableKurseFile(table1, GC.rootpath+"\\db\\kurse.db");
		groupKurse_g.redraw();
		wupdate(display_glob);
		
		if (button1Checkonly.getSelection() == true)
			flag = 1;

		UeberpruefeAlleKursePlausi uku = new UeberpruefeAlleKursePlausi();
		Monitor.setText("Ergebnisse in reporting\\Kursverfügbarkeit.txt");
		wupdate(display_glob);
		uku.Start(flag,progressBar1,display_glob);
		// uku.Start(0);
	}
	
	private static void ActionZeigeAlleKurse(SelectionEvent evt) 
	{
		System.out.println("button1.widgetSelected, event="+evt);
		Viewer table = new Viewer();
		table1.removeAll();
		table.viewTableKurseFile(table1, GC.rootpath+"\\db\\kurse.db");
		groupKurse_g.redraw();
		wupdate(display_glob);
		Monitor.setText("Refreshed");
	}
	private static void ActionButton38(SelectionEvent evt) 
	{
		//Zeigt die htmlliste an
		//db//reporting/htmlliste
		
		Viewer v = new Viewer();
		v.setKopfzeile("kopfzeile");
		v.viewHtmlExtFile(display_glob,  GC.rootpath+"\\db\\reporting\\38200Kurse.html");
	}
}
