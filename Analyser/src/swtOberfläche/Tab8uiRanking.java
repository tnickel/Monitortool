package swtOberfläche;

import hilfsklasse.Prop2;
import hilfsklasse.Swttool;
import mainPackage.GC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ranking.Rang;
import ranking.Rangparameter;
import stores.AktDB;
import stores.MidDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserPostingverhaltenDB;
import swtViewer.ViewExternFileTable;
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
public class Tab8uiRanking
{
	static private Display display_glob = null;
	static private Group groupKurse_g;
	static private UserDB udb_glob = null;
	static private AktDB aktdb_glob = null;
	static private ThreadsDB tdb_glob = null;
	static private MidDB middb_glob=null;
	static private UserPostingverhaltenDB userpostverhdb_glob = new UserPostingverhaltenDB();
	static private TabFolder folder_glob = null;
	static private Button button1calc_g;
	static private int usergewinnflag_g = 0;
	static private Button button2usergewinne_g;
	static private Label label1;
	static private Label label2;
	static private ProgressBar progressBarA4;
	static private Text text1;
	static private Group group5;
	static private Button button1import;
	static private Button button1Export;
	static private Group group1;
	static private Group group2;
	static private Group group3;
	static private Group group4;
	static private ProgressBar progressBarA2;
	static private Text aktion;
	static private Text bundlesize_g;
	static private Button button1show;
	static private Text text2rangfile;
	static private Text startindex_g;
	static private ProgressBar progressBarA3;
	static private ProgressBar progressBar2;
	static private ProgressBar progressBarA1;
	static Browser browser_glob = null;
	static private Button button4st2;
	static private Button button4st1;
	static private Label label6;
	static private Text text2charprozent;
	static private Label label5;
	static private Button button1;
	static private Table table1config;
	static private TableCursor cursor = null;
	// create an editor to edit the cell when the user hits "ENTER"
	// while over a cell in the table
	static private ControlEditor editor = null;

	static private ProgressBar progressBar1_g;
	static private Rangparameter rp = null;
	static private Label label4;
	static private Button button4useusercharakter;
	static private Button button4zeigetabelle;
	static private Button button3;
	static private Group group6;
	static private Label label3;
	static private Button button3baueRangliste;
	static private Button button2;
	static private String formel_glob = null;
	static private String usercfile_glob = GC.rootpath
			+ "\\db\\stores\\ranking\\userpostingverhalten.txt";
	static private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");
	// rangstatistik
	static Rangstat rs_glob = new Rangstat();

	static public void init(TabFolder folder, TabItem tab, Display dis,
			UserDB udb, ThreadsDB tdb, AktDB aktdb)
	{
		aktdb_glob = aktdb;
		display_glob = dis;
		tdb_glob = tdb;
		folder_glob = folder;
		udb_glob = udb;
		

		folder.setLayout(new GridLayout());
		// AllgemeinGroup erstellen

		// ProxyGroup erstellen
		groupKurse_g = new Group(folder, SWT.NONE);
		groupKurse_g.setText("Work");
		groupKurse_g.setLayout(null);
		{
			button1calc_g = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button1calc_g.setText("calcTunedRang (St0...3)");
			button1calc_g.setBounds(445, 422, 266, 31);
			button1calc_g.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					actionCalc(evt);
				}
			});
		}
		{
			progressBar1_g = new ProgressBar(groupKurse_g, SWT.NONE);
			progressBar1_g.setBounds(37, 689, 831, 20);
		}
		{
			group1 = new Group(groupKurse_g, SWT.NONE);
			group1.setLayout(null);
			group1.setText("Step 0 (Sammle Grundgewinne)");
			group1.setBounds(61, 38, 248, 92);
			{
				startindex_g = new Text(group1, SWT.NONE);
				startindex_g.setBounds(7, 19, 60, 19);
				startindex_g.setText("....");
				startindex_g
						.setToolTipText("Bei startindex 0 werden die Usergewinne gelöscht");
				startindex_g.setOrientation(SWT.HORIZONTAL);
			}
			{
				label1 = new Label(group1, SWT.NONE);
				label1.setText("Startindex");
				label1.setBounds(73, 19, 72, 30);
			}
			{
				bundlesize_g = new Text(group1, SWT.NONE);
				bundlesize_g.setBounds(7, 44, 60, 19);
				bundlesize_g.setText("200");
				bundlesize_g
						.setToolTipText("In diesen Intervallen werden die Ergebnisse auf Platte gespeichert");
			}
			{
				label2 = new Label(group1, SWT.NONE);
				label2.setText("Bundlesize");
				label2.setBounds(73, 46, 79, 43);
			}
		}
		{
			group2 = new Group(groupKurse_g, SWT.NONE);
			group2.setLayout(null);
			group2.setText("Step 2 (Userdaten Laden/Bewerten)");
			group2.setBounds(61, 224, 372, 45);
			{
				progressBar2 = new ProgressBar(group2, SWT.NONE);
				progressBar2.setBounds(-42, 30, 17, 104);
			}
		}
		{
			progressBarA1 = new ProgressBar(groupKurse_g, SWT.NONE);
			progressBarA1.setBounds(38, 52, 17, 69);
		}
		{
			progressBarA3 = new ProgressBar(groupKurse_g, SWT.NONE);
			progressBarA3.setBounds(38, 236, 17, 33);
		}
		{
			text2rangfile = new Text(groupKurse_g, SWT.NONE);
			text2rangfile.setBounds(734, 60, 227, 30);
			text2rangfile.setText("rangfile");
		}
		{
			button1show = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button1show.setText("ZeigeZwischengewinnliste.html");
			button1show.setBounds(734, 96, 227, 30);
			button1show.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1showWidgetSelected(evt);
				}
			});
		}
		{
			aktion = new Text(groupKurse_g, SWT.NONE);
			aktion.setText("aktion......");
			aktion.setBounds(37, 664, 831, 19);
		}
		{
			button2usergewinne_g = new Button(groupKurse_g, SWT.CHECK
					| SWT.LEFT);
			button2usergewinne_g.setText("Activate 0/1");
			button2usergewinne_g.setBounds(315, 182, 118, 30);
			button2usergewinne_g.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button2WidgetUsergewinneSelected(evt);
				}
			});
		}
		{
			group3 = new Group(groupKurse_g, SWT.NONE);
			GridLayout group3Layout = new GridLayout();
			group3Layout.makeColumnsEqualWidth = true;
			group3.setLayout(group3Layout);
			group3.setText("Step 1 (Erstelle Usergewinnliste)");
			group3.setBounds(61, 136, 248, 76);
		}
		{
			progressBarA2 = new ProgressBar(groupKurse_g, SWT.NONE);
			progressBarA2.setBounds(38, 142, 17, 70);
		}
		{
			group4 = new Group(groupKurse_g, SWT.NONE);
			group4.setLayout(null);
			group4.setText("Step 3 (Erstelle Rangliste)");
			group4.setBounds(61, 281, 372, 183);
			{
				table1config = new Table(group4, SWT.FULL_SELECTION
						| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				table1config.setBounds(8, 31, 285, 118);
				table1config.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						table1configWidgetSelected(evt);
					}
				});
			}
			{
				button1 = new Button(group4, SWT.PUSH | SWT.CENTER);
				button1.setText("Reset");
				button1.setBounds(8, 155, 60, 20);
				button1.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1WidgetSelected(evt);
					}
				});
			}
			{
				button2 = new Button(group4, SWT.PUSH | SWT.CENTER);
				button2.setText("ShowFormel");
				button2.setBounds(80, 155, 94, 20);
				button2.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2WidgetSelected(evt);
					}
				});
			}
		}
		{
			progressBarA4 = new ProgressBar(groupKurse_g, SWT.NONE);
			progressBarA4.setBounds(38, 281, 17, 183);
		}
		{
			button1Export = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button1Export.setText("ExportRanking");
			button1Export.setBounds(734, 132, 227, 30);
			button1Export.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1ExportWidgetSelected(evt);
				}
			});
		}
		{
			button1import = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button1import.setText("ImportRanking");
			button1import.setBounds(734, 168, 227, 30);
			button1import.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1importWidgetSelected(evt);
				}
			});
		}
		{
			group5 = new Group(groupKurse_g, SWT.NONE);
			group5.setLayout(null);
			group5.setText("Step 4: Info");
			group5.setBounds(38, 472, 672, 180);
			group5
					.setToolTipText("BaueRangliste Step3 \r\n[Läd nix aus dem Internet]\r\n\r\nKalkulliert die Rankingliste\r\nund schreibt sie in\r\nGC.rootpath + \"\\db\rankingliste.txt\"\r\n");
			{
				button3baueRangliste = new Button(group5, SWT.PUSH | SWT.CENTER);
				button3baueRangliste.setText("BaueRangliste (St 4)");
				button3baueRangliste.setBounds(8, 25, 143, 25);
				button3baueRangliste
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button3showranglisteWidgetSelected(evt);
							}
						});
			}
			{
				button4useusercharakter = new Button(group5, SWT.CHECK
						| SWT.LEFT);
				button4useusercharakter
						.setText("verwendeUsercharakter (STEP A)");
				button4useusercharakter.setBounds(192, 25, 245, 30);
			}
			{
				text1 = new Text(group5, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);

			}
		}
		{
			label3 = new Label(groupKurse_g, SWT.NONE);
			label3.setText("Rangfile");
			label3.setBounds(967, 60, 58, 20);
		}
		{
			group6 = new Group(groupKurse_g, SWT.NONE);
			group6.setLayout(null);
			group6.setText("Usercharakterisierung");
			group6.setBounds(445, 38, 283, 319);
			{
				button3 = new Button(group6, SWT.PUSH | SWT.CENTER);
				button3.setText("CalcUserCharRang");
				button3.setBounds(12, 236, 142, 27);
				button3.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button3WidgetSelected(evt);
					}
				});
			}
			{
				button4zeigetabelle = new Button(group6, SWT.PUSH | SWT.CENTER);
				button4zeigetabelle.setText("ZeigeUserCharRangliste");
				button4zeigetabelle.setBounds(12, 289, 209, 24);
				button4zeigetabelle.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button4zeigetabelleWidgetSelected(evt);
					}
				});
			}
			{
				label5 = new Label(group6, SWT.NONE);
				label5.setText("Step A:");
				label5.setSize(60, 30);
				label5.setBounds(12, 22, 60, 30);
			}
			{
				button4st1 = new Button(group6, SWT.CHECK | SWT.LEFT);
				button4st1.setText("Step1");
				button4st1.setBounds(166, 221, 60, 30);
			}
			{
				button4st2 = new Button(group6, SWT.CHECK | SWT.LEFT);
				button4st2.setText("Step2");
				button4st2.setBounds(166, 246, 60, 30);
			}
			button4st1.setSelection(false);
			button4st1.setToolTipText("Hier werden die 20Mio Postings untersucht und ein Userprofil erstellt (z.B. wie Informationsfreudig ist der User etc...)");
			button4st2.setSelection(true);
			button4st2.setToolTipText("Ordne die Ergebnisse von Step 1 (0.... 100000) und erstelle eine Rangliste");
		}
		{
			label4 = new Label(groupKurse_g, SWT.NONE);
			label4.setText("(i)");
			label4.setBounds(710, 422, 60, 30);
			label4
					.setToolTipText("Führe Step 0...3 aus. \r\n[Läd Daten aus dem Internet]\r\nWenn das verwendeusercharakter Flag gesetzt ist.\r\nWird die Usercharakterisierung für den Rang mit berücksichtigt\r\nStep3: schreibt die Rankingliste in\r\nGC.rootpath + \"\\db\rankingliste.txt\r\n\r\nHier kann einiges getuned werden:\r\nrangmode=0 => ohne sergewinne\r\nrangmode=1 => mit usergewinne\r\n");
		}
		tab.setControl(groupKurse_g);
		// Inhalt des Group erstellen

		group5.pack();
		group6.pack();
		groupKurse_g.pack();
		groupKurse_g.setSize(1266, 698);
		{
			GridData ConfiglisteLData = new GridData();
			ConfiglisteLData.widthHint = 196;
			ConfiglisteLData.heightHint = 228;
		}
		rp = new Rangparameter(text2rangfile.getText());
		specialInit();

		cursor = new TableCursor(table1config, SWT.NONE);
		editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		zeigeInitFormel(rp, text1);
		text1.setText(formel_glob);
		text1.setBounds(11, 55, 634, 78);
		{
			text2charprozent = new Text(group5, SWT.NONE);
			text2charprozent.setText("30");
			text2charprozent.setBounds(443, 31, 33, 24);
		}
		{
			label6 = new Label(group5, SWT.NONE);
			label6.setText("Gewichtung %");
			label6.setBounds(488, 31, 103, 24);
		}

	}

	static private void specialInit()
	{
		// setzt Startindex
		String laststring = prop2.getprop("lastvirtKontozaehler");
		int lastkontozaehler = Integer.parseInt(laststring);
		startindex_g.setText(String.valueOf(lastkontozaehler));
		SwtTabelle.baueTabelleRankingconfig(table1config, rp);
	}

	static private void actionCalc(SelectionEvent evt)
	{
		System.out.println("button1_g.widgetSelected, event=" + evt);
		int startindex = Integer.valueOf(startindex_g.getText());
		int bundlesize = Integer.valueOf(bundlesize_g.getText());
		Rang.calcTunedRang(usergewinnflag_g, progressBar1_g, display_glob,
				startindex, bundlesize, aktion, progressBarA1, progressBarA2,
				progressBarA3, progressBarA4, null, text2rangfile.getText(),
				aktdb_glob, tdb_glob,udb_glob);
	}

	static private void button2WidgetUsergewinneSelected(SelectionEvent evt)
	{
		System.out.println("button1.widgetSelected, event=" + evt);
		if (button2usergewinne_g.getSelection() == true)
			usergewinnflag_g = 1;
		else
			usergewinnflag_g = 0;
	}

	public static Text getText1()
	{
		return startindex_g;
	}

	static private void button1showWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1show.widgetSelected, event=" + evt);
		// TODO add your code for button1show.widgetSelected

		Viewer viewer = new Viewer();
		viewer.viewHtmlExtFile(display_glob, GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\ZwischenGewinnliste.html");
	}

	static private void button1ExportWidgetSelected(SelectionEvent evt)
	{
		//Export Ranking
		System.out.println("button1Export.widgetSelected, event=" + evt);
		
		Viewer viewer = new Viewer();
		String fnam = viewer.fileRequester(display_glob, GC.rootpath
				+ "\\export", SWT.SAVE);
		udb_glob.exportRanking(fnam);
	}

	static private void button1importWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1import.widgetSelected, event=" + evt);
		// TODO add your code for button1import.widgetSelected
		Viewer viewer = new Viewer();
		String fnam = viewer.fileRequester(display_glob, GC.rootpath
				+ "\\export", SWT.SEARCH);
		udb_glob.importRanking(fnam);
	}

	static private void table1configWidgetSelected(SelectionEvent e)
	{
		System.out.println("table1config.widgetSelected, event=" + e);

		final Text text = new Text(cursor, SWT.NONE);
		// holt sich eine tabellenzeile
		TableItem row = cursor.getRow();

		// hier wird das selektierte Zeichen geholt
		int column = cursor.getColumn();

		// Der text wird extrahiert
		String sel = row.getText(column);

		// im speziellen textfeld gesetzt
		text.setText(sel);

		text.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				// close the text editor and copy the data over
				// when the user hits "ENTER"
				if (e.character == SWT.CR)
				{
					TableItem row = cursor.getRow();
					int column = cursor.getColumn();
					row.setText(column, text.getText());
					text.dispose();
				}
				// close the text editor when the user hits "ESC"
				if (e.character == SWT.ESC)
				{
					text.dispose();
				}
			}
		});
		editor.setEditor(text);
		text.setFocus();
	}

	static private void zeigeInitFormel(Rangparameter rp, Text ausgabe)
	{
		// zeigt die Bewertungsformel in der ausgabe an
		rp.setRangparameter(table1config);
		String formel = rp.getBewertungsformel();
		ausgabe.setText(formel);
		formel_glob = formel;
		Swttool.wupdate(display_glob);

	}

	static private void button1WidgetSelected(SelectionEvent evt)
	{ // Reset configtable
		System.out.println("button1.widgetSelected, event=" + evt);
		// TODO add your code for button1.widgetSelected

		SwtTabelle.baueTabelleRankingconfig(table1config, rp);

		// setzt die rangparameter aus der tabelle
		rp.setRangparameter(table1config);
		zeigeInitFormel(rp, text1);
	}

	static private void button2WidgetSelected(SelectionEvent evt)
	{
		// Show formel
		System.out.println("button2.widgetSelected, event=" + evt);
		rp.setRangparameter(table1config);
		zeigeInitFormel(rp, text1);
	}

	static private void button3showranglisteWidgetSelected(SelectionEvent evt)
	{
		// Baue Rangliste
		// Hier werden alle user anhand des Profilverhaltens charaktierisiert
		System.out.println("button3showrangliste.widgetSelected, event=" + evt);

		// rangliste: ist die liste der sortierten user
		//xx ArrayList<UserDbObj> rangliste = null;
		// rp:rangparameter (parameter für die Rangberechnung)
		// GC.Usergewinnrang (Hier werden auch die usergewinne berücksichtigt)
		udb_glob.BaueRankingListe(GC.USERGEWINNRANG, 1, rp,
				aktdb_glob, button4useusercharakter.getSelection(),
				userpostverhdb_glob);
		
		SwtTabelle.baueTabelleRangliste_ext(udb_glob, display_glob);
	}

	static private void button3WidgetSelected(SelectionEvent evt)
	{
		// Baue Usercharakterisierung
		// Hier werden alle User anhand des userpostingverhaltens
		// charakterisiert

		System.out.println("button3.widgetSelected, event=" + evt);
		String startstr = Prop2.getprop("Verhaltensrang_startpos");

		userpostverhdb_glob.CalcRangingProto(middb_glob,rs_glob, display_glob, udb_glob,
				tdb_glob, usercfile_glob, button4st1.getSelection(), button4st2
						.getSelection());
	}

	static private void button4zeigetabelleWidgetSelected(SelectionEvent evt)
	{
		// zeige Tabelle der usercharakterisierung
		System.out.println("button4zeigetabelle.widgetSelected, event=" + evt);
		ViewExternFileTable ve = new ViewExternFileTable();

		String rsmessage = "#user=" + rs_glob.getGesSeenUser() + " #threads="
				+ rs_glob.getGesSeenThreads() + " #geslinks="
				+ rs_glob.getGesSeenLinks();
		ve.ShowTable(display_glob, usercfile_glob, rsmessage,null);
	}

}
