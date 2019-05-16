package swtOberfläche;

import hilfsklasse.Keywortliste;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import kurse.KurseDB;
import mainPackage.GC;
import objects.ChampionDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.ChampionDB;
import stores.PrognosenDB;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import stores.UeberwachungDB;
import stores.UserDB;
import swtViewer.ViewExternTable;
import championAnalyser.BoerseDe;
import championAnalyser.BoerseMasterliste;

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
public class Tab15uiChampion
{
	static Display display_glob = null;
	static private UserDB udb_glob = null;
	static private KurseDB kdb_glob = null;
	static private Text wknhistorie;
	private Button KurseLaden;
	private Button showpdf_button;
	private Button start;
	static private Group simulgroup = null;
	static private Button extendedbutton = null;
	static private UeberwachungDB uebdb_glob = null;
	static private AktDB aktdb_glob = null;
	static private Group filtergroup = null;
	static private Text selboerblatt_glob = null;
	static private Button ShowHistorie;
	static private ThreadsDB tdb_glob = null;
	static private PrognosenDB pdb_glob = null;
	static private Table blaetterliste_glob = null;
	static private BoerseMasterliste boerl = null;

	static private Table auswerttableLinks_glob;
	static private Group mastergroup = null;
	static private HashMap<Integer, String> selboermapRechts_glob = new HashMap<Integer, String>();
	static private HashMap<Integer, String> selwknmapLinks_glob = new HashMap<Integer, String>();
	static ChampionDB champdb = new ChampionDB();
	static String wkn_glob = null;
	private Button Suchwortfilter;
	private Button suchbutton;
	private Text suchtext;
	private Button ZeigeKurse;
	private Button MarkerButton;
	private Text marker;
	private Button EditSuchwortfilter;
	private Button EditAutogenfilter;

	public void init(TabFolder folder, TabItem tab, Display dis, UserDB udb,
			ThreadsDB tdb, AktDB aktdb, UeberwachungDB uebdb, KurseDB kdb,SlideruebersichtDB sldb)
	{
		udb_glob = udb;
		tdb_glob = tdb;
		aktdb_glob = aktdb;
		uebdb_glob = uebdb;
		kdb_glob = kdb;
		display_glob = dis;
		boerl = new BoerseMasterliste(aktdb);

		folder.setLayout(new GridLayout());
		// AllgemeinGroup erstellen

		mastergroup = new Group(folder, SWT.NONE);
		mastergroup.setText("BörseDe Auswertung");
		mastergroup.setLayout(null);
		{
			blaetterliste_glob = new Table(mastergroup, SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER);
			blaetterliste_glob.setBounds(974, 25, 507, 346);
			// blaetterliste_glob.addSelectionListener(new SelectionAdapter() {
			// });
		}

		blaetterliste_glob.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				ActionTableRechts(evt, mastergroup, selboermapRechts_glob,
						display_glob);
			}
		});

		{
			auswerttableLinks_glob = new Table(mastergroup, SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
			auswerttableLinks_glob.setBounds(12, 25, 943, 551);

			auswerttableLinks_glob.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					ActionTableLinks(evt);
				}
			});
		}
		{
			selboerblatt_glob = new Text(mastergroup, SWT.NONE);
			selboerblatt_glob.setText("boerblatt");
			selboerblatt_glob.setBounds(12, 588, 615, 30);
		}
		{
			filtergroup = new Group(mastergroup, SWT.NONE);
			GridLayout FilterLayout = new GridLayout();
			FilterLayout.makeColumnsEqualWidth = true;
			filtergroup.setLayout(FilterLayout);
			filtergroup.setText("Filter");
			filtergroup.setBounds(12, 637, 344, 63);
			{
				extendedbutton = new Button(filtergroup, SWT.CHECK | SWT.LEFT);
				GridData extendedLData = new GridData();
				extendedbutton.setLayoutData(extendedLData);
				extendedbutton.setText("erw. Info anzeigen");
				extendedbutton
						.setToolTipText("weitere Infos in der Aktientabelle anzeigen");
				extendedbutton.setSelection(true);
				extendedbutton.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1extendedinfo(evt);
					}
				});
			}
		}
		{
			simulgroup = new Group(mastergroup, SWT.NONE);
			GridLayout simulLayout = new GridLayout();
			simulLayout.makeColumnsEqualWidth = true;
			simulgroup.setLayout(simulLayout);
			simulgroup.setText("Simulation");
			simulgroup.setBounds(368, 637, 260, 114);
			{
				start = new Button(simulgroup, SWT.PUSH | SWT.CENTER);
				GridData startLData = new GridData();
				start.setLayoutData(startLData);
				start.setText("start");
			}
		}
		{
			showpdf_button = new Button(mastergroup, SWT.PUSH | SWT.CENTER);
			showpdf_button.setText("Pdf Anzeigen");
			showpdf_button.setBounds(749, 588, 108, 30);
			showpdf_button
					.setToolTipText("Das rechts Selektierte Boerse.de Blatt wird angezeigt");
			showpdf_button.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1showpdf(evt);
				}
			});
		}
		{
			KurseLaden = new Button(mastergroup, SWT.PUSH | SWT.CENTER);
			KurseLaden.setText("KurseLaden");
			KurseLaden.setBounds(863, 588, 92, 30);
			KurseLaden
					.setToolTipText("Die Kurse von Boerse.de werden auf den neusten Stand gebracht");
			KurseLaden.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					buttonKurseLaden(evt);
				}
			});
		}
		{
			ShowHistorie = new Button(mastergroup, SWT.PUSH | SWT.CENTER);
			ShowHistorie.setText("ShowHistorie");
			ShowHistorie.setBounds(12, 757, 107, 30);
			ShowHistorie
					.setToolTipText("Hier wird die Historie für einen einzelnen Aktienwert angezeigt");
			ShowHistorie.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					buttonShowHistorie(evt);
				}
			});
		}
		{
			wknhistorie = new Text(mastergroup, SWT.NONE);
			wknhistorie.setText("historie");
			wknhistorie.setBounds(12, 721, 344, 30);
		}
		{
			EditAutogenfilter = new Button(mastergroup, SWT.PUSH | SWT.LEFT);
			EditAutogenfilter.setText("2) ErstelleSuchwortliste (Autogen....)");
			EditAutogenfilter.setBounds(634, 763, 261, 30);
			EditAutogenfilter.setToolTipText("Für die Boersenblätter Suchfuktion Tab13 wird hier eine Suchwortliste erstellt\r\n\r\nErstellt wird:\r\nAUTOGEN_BoerseDE_Suchliste.txt\r\n\r\nHierbei wird folgendermassen vorgegangen:\r\n\r\n* Packe nur die passende Aktien aus Champion.de in die Suchwortliste wenn eines der Keywörter aus [ 1) Suchwortfilter ] in dem \"letzten\" Champion Börsenblatt bei dieser Aktie vorkommt.\r\n\r\n\r\n\r\n");
			EditAutogenfilter.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					ActionErstelleSuchliste(evt);
				}
			});
		}
		{
			EditSuchwortfilter = new Button(mastergroup, SWT.PUSH | SWT.LEFT);
			EditSuchwortfilter.setText("1) EditSuchwortfilter");
			EditSuchwortfilter.setBounds(414, 763, 214, 30);
			EditSuchwortfilter.setToolTipText("Der Suchwortfilter wird benutzt um die Tabelle für die Suchwortliste zu exportieren. Man möchte ja in  Boersenblätter eine Suchliste verwenden.\r\nStichwort \"Boersenblätter_autogen....\"");
			EditSuchwortfilter.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					ActionEditSuchwortfilter(evt);
				}
			});
		}
		{
			Suchwortfilter = new Button(mastergroup, SWT.CHECK | SWT.LEFT);
			Suchwortfilter.setText("akt");
			Suchwortfilter.setBounds(368, 763, 60, 30);
			Suchwortfilter.setSelection(true);
			Suchwortfilter.setToolTipText("Durch diesen Haken kann man den Suchwortfilter Ein/Aus-Schalten");
		}
		{
			marker = new Text(mastergroup, SWT.BORDER);
			marker.setText(champdb.getInfostring());
			marker.setBounds(646, 653, 669, 23);
			marker.addListener(SWT.DefaultSelection, new Listener() 
			{
			      public void handleEvent(Event evt) 
			      {
			    	  //die Returntaste beim Marker wurde gesetzt
			          System.out.println(evt.widget + " - Default Selection");
			          ActionButtonReturn();
			       }
			});
		}
		{
			MarkerButton = new Button(mastergroup, SWT.TOGGLE | SWT.CENTER | SWT.BORDER);
			MarkerButton.setText("MarkerSetzen");
			MarkerButton.setBounds(1195, 688, 120, 30);
			MarkerButton.setToolTipText("Der Suchwortfilter wird benutzt um diese Tabelle farbig zu markieren. Wenn ein Wort des Suchwortfilters in einer Tabellenzeile vorkommt wird die Zeile markiert");
			MarkerButton
			.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(
						SelectionEvent evt)
				{
					ActionMarkerSetzenSelected(evt);
				}
			});
		}
		{
			ZeigeKurse = new Button(mastergroup, SWT.PUSH | SWT.CENTER);
			ZeigeKurse.setText("ZeigeKurs");
			ZeigeKurse.setBounds(125, 757, 83, 30);
			ZeigeKurse.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					ActionZeigeKurs(evt);
				}
			});
		}
		{
			suchtext = new Text(mastergroup, SWT.NONE);
			suchtext.setText("suchtext");
			suchtext.setBounds(967, 508, 198, 30);
		}
		{
			suchbutton = new Button(mastergroup, SWT.PUSH | SWT.CENTER);
			suchbutton.setText("Suchen");
			suchbutton.setBounds(967, 544, 198, 30);
			suchbutton.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					ActionButtonSuchen(evt);
				}
			});
		}

		genChampionBlaetterListe(blaetterliste_glob);

		tab.setControl(mastergroup);
		// mastergroup.setSize(1534, 465);
		mastergroup.pack();
		simulgroup.pack();
		filtergroup.pack();

	}
	private void ActionButtonSuchen(SelectionEvent evt)
	{
		System.out.println("Action Button Suchen event=" + evt);

		String suchzeile=suchtext.getText();
		SwtTools st = new SwtTools();
		int index=st.sucheZeile(auswerttableLinks_glob,suchzeile);
		auswerttableLinks_glob.setSelection(index);
	}
	private void ActionZeigeKurs(SelectionEvent evt)
	{
		System.out.println("Action Zeige Kurs, event=" + evt);
		String symb=null;
		
		ChampionDbObj campObj = (ChampionDbObj) champdb.getChampion(wkn_glob);
		symb = campObj.getSymbol();
		kdb_glob.zeigeSwtTabelle(display_glob, symb, GC.rootpath+"\\db\\Kurse\\"+symb+".csv");
	}
	private void ActionButtonReturn()
	{
		
		String markertext=marker.getText();
		champdb.setInfostring(markertext);
		champdb.WriteDB();
	}
	
	public void genChampionBlaetterListe(Table table)
	{

		String textroot = GC.textzielbase + "\\BoerseDe";
		SwtTabelle.baueTabelleChampionBlaetterListe(table, display_glob,
				textroot, selboermapRechts_glob);
	}

	public void genAuswertTable(Table table, BoerseMasterliste bl,
			String filename, ChampionDB champdb,int markerflag,Keywortliste keyl)
	{
		int extbutton = 0;
		if (extendedbutton.getSelection() == true)
			extbutton = 1;

		// Blatt aus dem Speicher holen
		BoerseDe blatt = bl.holeBoersenblatt(filename);

		// 3 Table aufbauen
		SwtTabelle.baueTabelle100Championliste(display_glob, table, blatt,
				extbutton, bl, aktdb_glob, champdb, selwknmapLinks_glob,markerflag,keyl);
	}
	private void ActionMarkerSetzenSelected(SelectionEvent evt)
	{
		System.out.println("markerbutton gedrückt");

		// 3)Hier wird das rechts selektierte Börsenblatt auf der linken
		// Seite dargestellt
		// Falls der Marker aktiviert ist werden die Zeilen mit den Schlüsselwörtern rot angezeigt
		
		int markerflag=0;

		Keywortliste keyl= new Keywortliste(",");
		keyl.addElem(champdb.getInfostring());
		
		if(MarkerButton.getSelection()==true)
			markerflag=1;
		
		int extbutton = 0;
		if (extendedbutton.getSelection() == true)
			extbutton = 1;
		
		
		
		String fnam=GC.textzielbase+"\\boersede\\"+selboerblatt_glob.getText();
		genAuswertTable(auswerttableLinks_glob, boerl, fnam, champdb,markerflag,keyl);
	}
	
	private void ActionTableRechts(SelectionEvent evt, Group group,
			HashMap<Integer, String> boermap, Display dis)
	{
		String sel = null, name = null;
		// Hier wurde ein Boerblatt angewählt und die Auswertung erscheint dann
		// links
		// boer:ist ist Liste Pos:Boerblattname
		// Ausserdem wird die Tabble mit dem blattinfo aufgebaut

		System.out.println("table1.widgetSelected, event=" + evt);
		String seltext = evt.item.toString();

		if (seltext.contains("{"))
		{
			sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");
		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");

		selboerblatt_glob.setText(name);

		// Linke tabelle aufbauen
		String fnam = getFilename("txt");

		// 1)Hier werden alle Boersenblätter eingelesen
		boerl.readAll(selboermapRechts_glob);

		// 2)Hier wird die champdb erweitert
		champdb.expand(boerl, aktdb_glob, tdb_glob);

		// 3)Hier wird das rechts selektierte Börsenblatt auf der linken
		// Seite dargestellt
		genAuswertTable(auswerttableLinks_glob, boerl, fnam, champdb,0, null);
		
		champdb.pressSymboleKdb(kdb_glob);

	}

	static private String getFilename(String option)
	{
		String filename = null;
		String bbname = selboerblatt_glob.getText();
		// ersetze .txt durch .pdf

		if (option.contains("pdf"))
		{
			bbname = bbname.replace(".txt", ".pdf");
			filename = GC.pdfzielbase + "\\boersede\\" + bbname;
		} else
			filename = GC.textzielbase + "\\boersede\\" + bbname;

		return filename;
	}

	static private void button1extendedinfo(SelectionEvent evt)
	{
		System.out.println("button1extendedinfo.widgetSelected, event=" + evt);
		boolean nuraktien = extendedbutton.getSelection();
		System.out.println("button=" + nuraktien);
	}

	static private void button1showpdf(SelectionEvent evt)
	{
		System.out.println("button1showpdf.widgetSelected, event=" + evt);

		String filename = getFilename("pdf");
		try
		{
			String cmd = "\"" + GC.acroreaderbase + "\" \"" + filename + "\"";
			String line = null;

			System.out.println("zeile<" + cmd + ">");
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader lsOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = lsOut.readLine()) != null)
			{
				System.out.println(line);
			}
		} catch (Exception e)
		{
			System.err.println("ls error " + e);
		}
	}

	static private void buttonKurseLaden(SelectionEvent evt)
	{
		System.out.println("buttonKurseLaden.widgetSelected, event=" + evt);
		
		// Erstelle die Menge der Symbole
		HashSet<String> sm = new HashSet<String>();

		int anz = champdb.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ChampionDbObj campObj = (ChampionDbObj) champdb.GetObjectIDX(i);
			String symb = campObj.getSymbol();
			if (symb.contains("?"))
				continue;
			sm.add(symb);
		}
		kdb_glob.ladeAlleKurseParallel(aktdb_glob, 0, null, display_glob, sm);
	}

	static private void buttonShowHistorie(SelectionEvent evt)
	{
		Table zeitverlauftable = null;
		System.out.println("buttonKurseLaden.widgetSelected, event=" + evt);
		// tabelle aufbauen

		// tabelle extern anzeigen
		ViewExternTable vt = new ViewExternTable();
		vt.ShowTable(display_glob, "titel", wkn_glob, boerl);
	}

	private void ActionTableLinks(SelectionEvent evt)
	{
		String sel = null, name = null;
		// Hier wurde auf eine Zeile in der linken tabelle geklickt

		System.out.println("table1.widgetSelected, event=" + evt);
		String seltext = evt.item.toString();

		if (seltext.contains("{"))
		{
			sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");
		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");

		int pos = Integer.valueOf(name);
		wkn_glob = selwknmapLinks_glob.get(pos);
		wknhistorie.setText(wkn_glob);

	}

	private void ActionEditSuchwortfilter(SelectionEvent evt)
	{
		ExtFileEditor config = new ExtFileEditor();
		config.init(display_glob, GC.rootpath
				+ "\\conf\\boersenblaetter\\Suchwortfilter.txt");
	}

	private void ActionErstelleSuchliste(SelectionEvent evt)
	{
		
		//holt das aktuelle Börsenblatt
		BoerseDe bde=boerl.getBoerblattIdx(0);

		champdb.Init(bde,GC.rootpath
				+ "\\conf\\boersenblaetter\\Suchwortfilter.txt", GC.rootpath
				+ "\\conf\\boersenblaetter\\AUTOGEN_BoerseDE_Suchliste.txt");
	}
}
