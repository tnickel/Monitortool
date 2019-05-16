package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import hilfsobjekt.Fensterinfo;
import hilfsrepeattask.SammleThreadpostings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import kurse.KurseDB;
import mainPackage.GC;
import objects.AktDbObj;
import objects.Reporting;
import objects.ThreadDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.KeyDB;
import stores.PrognosenDB;
import stores.ThreadsDB;
import stores.UeberwachungDB;
import stores.UserDB;

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
public class Tab10uiBrowser
{
	Display display_glob = null;
	static private UserDB udb_glob = null;
	static private KurseDB kdb_glob = null;
	static private UeberwachungDB uebdb_glob = null;
	static private AktDB aktdb_glob = null;
	static private Table table1userliste;
	private Button button1NurUsername;
	private Label label10;
	private Button button1zeigeUser;
	private Label label11;
	private Button button1refresh;
	private Label label12;
	private Text text1maxprio;
	private Text text1minprio;
	private Text text1zeigeUser;
	private Button button1zeigeSlider90;
	private Button button1zeigeSlider;
	private Button button1zeigePrognosenliste;
	private Button button1zeigePuschedkeywoerter;
	private Button button1zeigepostinglisten;
	private Button button1zeigeKurswerte;
	private Button button1ueberwaufnahme;
	private Button button1userantworten;
	private Text text1maxlastpostdate;
	private Button button1gepusched;
	static private Label label1;
	private Button button1showpostings;
	private Label label3;
	private Text text1pos;
	private Text text1selthread;
	private Text text1max;
	private Text text1min;
	private Slider slider1;
	static private Label label2;
	static private Table table2threadliste;

	static private ThreadsDB tdb_glob = null;
	static private int seltid_glob = 0;
	static private int selpage_glob = 1;
	static private String seluser_glob = null;
	static private Label Info;
	static private Button button1keinBalken;
	static private Label label5;
	static private Text text1postanzahl;
	static private Label label4;
	static private Text text1rang;
	private Label label9;
	private Button button1geleseneausblenden;
	private Button button1showvertrauen;
	private Button button1nurthreadsmitpages;
	private Button button1nurprognosen;
	private Label label8;
	private Text text1prognosenmaxalter;
	private Button button1neueThreads;
	private Button button1nurAktienthreads;
	private Group group4;
	private Label label7;
	private Label label6;
	static private Text text1date;
	static private Button button1showdate;
	static private Button button1postings;
	static private int nurmitUsernamen_flag = 0;
	static private int mitAntworten_flag = 0;
	// group3 dates
	static private Group group3;
	// group2 postingnumbers
	static private Group group2;
	static private ProgressBar progressBar1;
	static private Button button1loadAllUserthreads;
	static private Group group1 = null;

	// map <index|tid>
	static private HashMap<Integer, String> usermap = new HashMap<Integer, String>();
	static private List<String> seluserlist_glob = new ArrayList<String>();
	static private HashMap<Integer, Integer> threadmap = new HashMap<Integer, Integer>();

	static private PrognosenDB pdb_glob = null;
	static private List<Integer> prognosenliste_glob = null;

	public void init(TabFolder folder, TabItem tab, Display dis, UserDB udb,
			ThreadsDB tdb, AktDB aktdb, UeberwachungDB uebdb, KurseDB kdb)
	{
		udb_glob = udb;
		tdb_glob = tdb;
		aktdb_glob = aktdb;
		uebdb_glob = uebdb;
		kdb_glob = kdb;
		display_glob = dis;

		folder.setLayout(new GridLayout());
		// AllgemeinGroup erstellen

		// ProxyGroup erstellen
		group1 = new Group(folder, SWT.NONE);
		group1.setText("Thread Auswertung");
		group1.setLayout(null);
		{
			table1userliste = new Table(group1, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER);
			table1userliste.setBounds(30, 36, 250, 239);
		}
		{
			table2threadliste = new Table(group1, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER | SWT.FULL_SELECTION);
			table2threadliste.setBounds(303, 31, 832, 441);
		}
		{
			label1 = new Label(group1, SWT.NONE);
			label1.setText("Observe Userliste");
			label1.setBounds(172, 12, 108, 18);
		}
		{
			label2 = new Label(group1, SWT.NONE);
			label2.setText("Threadliste");
			label2.setBounds(292, 478, 81, 20);
		}
		{
			text1selthread = new Text(group1, SWT.NONE);
			text1selthread.setBounds(292, 510, 843, 23);
		}
		{
			text1pos = new Text(group1, SWT.NONE);
			text1pos.setBounds(292, 539, 60, 21);
		}
		{
			label3 = new Label(group1, SWT.NONE);
			label3.setText("pos");
			label3.setSize(60, 30);
			label3.setBounds(358, 540, 60, 30);
		}
		{
			button1showpostings = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1showpostings.setText("ShowSpezPostings");
			button1showpostings.setBounds(147, 675, 139, 30);
			button1showpostings.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1showpostingsWidgetSelected(evt);
				}
			});
		}
		{
			text1rang = new Text(group1, SWT.NONE);
			text1rang.setBounds(21, 577, 60, 22);
		}
		{
			label4 = new Label(group1, SWT.NONE);
			label4.setText("Rang");
			label4.setBounds(81, 577, 60, 20);
		}
		{
			text1postanzahl = new Text(group1, SWT.NONE);
			text1postanzahl.setBounds(21, 548, 60, 23);
		}
		{
			label5 = new Label(group1, SWT.NONE);
			label5.setText("Postanzahl");
			label5.setBounds(81, 548, 84, 23);
		}
		{
			button1keinBalken = new Button(group1, SWT.CHECK | SWT.LEFT);
			button1keinBalken.setText("keine Balken");
			button1keinBalken.setBounds(21, 518, 135, 30);
		}
		{
			Info = new Label(group1, SWT.NONE);
			Info.setText("Info");
			Info.setBounds(1095, 478, 28, 20);
			Info.setToolTipText("gruen = letztes Posting ist höchstens 3 Monate alt\r\n rot= Thread in der aktuellen Prognose\r\n '*' = Thread vom user neu aufgespürt(<30T)\r\n ?? postanz, dann existiert die tid nicht in der threadsdb\r\n'P'= Aktie ist gepusched\r\nINFO/PROG=Prognosenliste vorhanden\r\nUpostanz=userpostanzahl=gibt an wieviele postings der user in diesem Thread insgesammt abgelegt hat");
		}
		{
			button1loadAllUserthreads = new Button(group1, SWT.PUSH
					| SWT.CENTER);
			button1loadAllUserthreads.setText("Load All Userthreads");
			button1loadAllUserthreads.setBounds(21, 605, 135, 30);
			button1loadAllUserthreads
					.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent evt)
						{
							button1loadAllUserthreadsSelected(evt, group1,
									progressBar1);
						}
					});
		}
		{
			progressBar1 = new ProgressBar(group1, SWT.NONE);
			progressBar1.setBounds(21, 747, 1108, 23);
		}
		{
			group2 = new Group(group1, SWT.NONE);
			group2.setLayout(null);
			group2.setText("Datumsauswahl");
			group2.setBounds(292, 664, 663, 77);
			{
				text1date = new Text(group2, SWT.NONE);
				text1date.setText("blup1");
			}
		}
		{
			group3 = new Group(group1, SWT.NONE);
			group3.setLayout(null);
			group3.setText("Postingnummer-Auswahl");
			group3.setBounds(292, 564, 663, 83);
			{
				slider1 = new Slider(group3, SWT.NONE);
				slider1.setBounds(12, 59, 639, 18);

				slider1.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						slider1WidgetSelected(evt);
					}
				});
			}
			{
				text1max = new Text(group3, SWT.NONE);
				text1max.setBounds(582, 27, 69, 26);
			}
			{
				text1min = new Text(group3, SWT.NONE);
				text1min.setBounds(12, 21, 22, 26);
			}
		}
		{
			button1postings = new Button(group1, SWT.CHECK | SWT.LEFT);
			button1postings.setText("ShowPostingnumber");
			button1postings.setBounds(961, 617, 168, 30);
			button1postings.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1postingsWidgetSelected(evt);
				}
			});
		}
		{
			button1showdate = new Button(group1, SWT.CHECK | SWT.LEFT);
			button1showdate.setText("Show Date");
			button1showdate.setBounds(961, 685, 168, 30);
			button1showdate.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1showdateWidgetSelected(evt);
				}
			});
		}
		table1userliste.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				ActionTable1(evt, group1, usermap, threadmap, display_glob,
						seluserlist_glob);
			}
		});
		table2threadliste.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				ActionTable2(evt, threadmap);
			}
		});

		text1rang.setText("30000");
		text1postanzahl.setText("100");
		button1showdate.setSelection(true);
		{
			group4 = new Group(group1, SWT.NONE);
			group4.setLayout(null);
			group4.setText("Filter");
			group4.setBounds(30, 335, 250, 183);
			{
				button1nurAktienthreads = new Button(group4, SWT.CHECK
						| SWT.LEFT);
				button1nurAktienthreads.setText("NurAktienthreads");
				button1nurAktienthreads.setBounds(8, 15, 129, 19);
			}
			{
				button1neueThreads = new Button(group4, SWT.CHECK | SWT.LEFT);
				button1neueThreads.setText("Lastpostdate ");
				button1neueThreads.setBounds(8, 34, 105, 19);
			}
			{
				text1prognosenmaxalter = new Text(group4, SWT.NONE);
				text1prognosenmaxalter.setBounds(124, 57, 30, 17);
			}
			{
				label8 = new Label(group4, SWT.NONE);
				label8.setText("Tage max alt");
				label8.setBounds(160, 57, 86, 17);
			}
			{
				button1nurprognosen = new Button(group4, SWT.CHECK | SWT.LEFT);
				button1nurprognosen.setText("NurPrognosen");
				button1nurprognosen.setBounds(8, 57, 110, 20);
			}

			{
				button1nurthreadsmitpages = new Button(group4, SWT.CHECK
						| SWT.LEFT);
				button1nurthreadsmitpages.setText("NurThreadsMitPages");
				button1nurthreadsmitpages.setBounds(8, 77, 219, 22);
			}
			button1nurthreadsmitpages.setSelection(true);
			{
				button1geleseneausblenden = new Button(group4, SWT.CHECK
						| SWT.LEFT);
				button1geleseneausblenden.setText("Gelesene Ausblenden");
				button1geleseneausblenden.setBounds(8, 105, 153, 17);
				button1geleseneausblenden
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1geleseneausblendenWidgetSelected(evt);
							}
						});
			}
			{
				label9 = new Label(group4, SWT.NONE);
				label9.setText("Info");
				label9.setBounds(205, 105, 29, 17);
				label9
						.setToolTipText("Wird dieses Filterelement ausgewählt dann wird als Startpunkt das letzte Lesedatum gewählt.");
			}
			{
				button1gepusched = new Button(group4, SWT.CHECK | SWT.LEFT);
				button1gepusched.setText("Gepusched");
				button1gepusched.setBounds(8, 128, 153, 18);
			}
			{
				text1maxlastpostdate = new Text(group4, SWT.NONE);
				text1maxlastpostdate.setBounds(125, 34, 31, 17);
				text1maxlastpostdate.setText("10");
			}
			{
				label10 = new Label(group4, SWT.NONE);
				label10.setText("Tage max alt");
				label10.setBounds(162, 34, 84, 17);
			}
		}
		{
			button1showvertrauen = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1showvertrauen.setText("ShowSpezVertrauen");
			button1showvertrauen.setBounds(147, 711, 139, 30);
			button1showvertrauen.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1showvertrauenWidgetSelected(evt);
				}
			});
		}
		{
			button1ueberwaufnahme = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1ueberwaufnahme.setText("In Ueberwachung Aufnehmen");
			button1ueberwaufnahme.setBounds(1147, 36, 217, 30);
			button1ueberwaufnahme.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1ueberwaufnahmeWidgetSelected(evt);
				}
			});
		}
		{
			button1zeigeKurswerte = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1zeigeKurswerte.setText("Zeige Kurswerte");
			button1zeigeKurswerte.setBounds(1147, 93, 217, 30);
			button1zeigeKurswerte.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1zeigeKurswerteWidgetSelected(evt);
				}
			});
		}
		{
			button1zeigepostinglisten = new Button(group1, SWT.PUSH
					| SWT.CENTER);
			button1zeigepostinglisten.setText("ZeigePostinglisten");
			button1zeigepostinglisten.setBounds(1147, 146, 217, 30);
			button1zeigepostinglisten
					.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent evt)
						{
							button1zeigepostinglistenWidgetSelected(evt);
						}
					});
		}
		{
			button1zeigePuschedkeywoerter = new Button(group1, SWT.PUSH
					| SWT.CENTER);
			button1zeigePuschedkeywoerter.setText("ZeigePuschedKeywoerter");
			button1zeigePuschedkeywoerter.setBounds(1147, 199, 217, 30);
			button1zeigePuschedkeywoerter
					.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent evt)
						{
							button1zeigePuschedkeywoerterWidgetSelected(evt);
						}
					});
		}
		{
			button1zeigePrognosenliste = new Button(group1, SWT.PUSH
					| SWT.CENTER);
			button1zeigePrognosenliste.setText("Zeige Prognosenliste");
			button1zeigePrognosenliste.setBounds(1147, 258, 217, 30);
			button1zeigePrognosenliste
					.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent evt)
						{
							button1zeigePrognosenlisteWidgetSelected(evt);
						}
					});
		}
		{
			button1zeigeSlider = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1zeigeSlider.setText("ZeigeSlider");
			button1zeigeSlider.setBounds(1147, 314, 217, 30);
			button1zeigeSlider.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1zeigeSliderWidgetSelected(evt);
				}
			});
		}
		{
			button1zeigeSlider90 = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1zeigeSlider90.setText("ZeigeSlider90");
			button1zeigeSlider90.setBounds(1147, 368, 217, 30);
			button1zeigeSlider90.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1zeigeSlider90WidgetSelected(evt);
				}
			});
		}
		{
			text1zeigeUser = new Text(group1, SWT.NONE);
			text1zeigeUser.setBounds(30, 277, 156, 20);
		}
		{
			button1zeigeUser = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1zeigeUser.setText("ZeigeUser");
			button1zeigeUser.setBounds(192, 277, 88, 20);
			button1zeigeUser.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1zeigeUserWidgetSelected(evt,group1);
				}
			});
		}
		{
			text1minprio = new Text(group1, SWT.NONE);
			text1minprio.setText("1");
			text1minprio.setBounds(30, 303, 27, 20);
		}
		{
			label11 = new Label(group1, SWT.NONE);
			label11.setText("Minprio");
			label11.setBounds(63, 303, 45, 20);
		}
		{
			text1maxprio = new Text(group1, SWT.NONE);
			text1maxprio.setText("2");
			text1maxprio.setBounds(114, 303, 24, 20);
		}
		{
			label12 = new Label(group1, SWT.NONE);
			label12.setText("Maxprio");
			label12.setBounds(144, 303, 60, 20);
		}
		{
			button1refresh = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1refresh.setText("Refresh");
			button1refresh.setBounds(210, 303, 70, 20);
			button1refresh.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1refreshWidgetSelected(evt);
				}
			});
		}
		String date = Tools.entferneZeit(Tools.get_aktdatetime_str());
		date = Tools.modifziereDatum(date, 0, 0, -14, 0);
		text1date.setText(date);
		button1neueThreads.setSelection(true);
		text1prognosenmaxalter.setText("30");

		text1date.setBounds(128, 25, 137, 20);
		{
			label6 = new Label(group2, SWT.NONE);
			label6.setText("Info");
			label6.setBounds(631, 25, 26, 20);
			label6
					.setToolTipText("Der Startpunkt gilt nicht wenn \"gelesene ausblenden\" angewählt ist. Dann wird als Startpunkt das letzte Lesedatum genommen");
		}
		{
			label7 = new Label(group2, SWT.NONE);
			label7.setText("Startpunkt");
			label7.setBounds(12, 25, 85, 20);
		}
		{
			button1NurUsername = new Button(group2, SWT.CHECK | SWT.LEFT);
			button1NurUsername.setText("Postings nur mit Usernamen");
			button1NurUsername.setBounds(298, 19, 216, 26);
			button1NurUsername.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1NurUsernameWidgetSelected(evt);
				}
			});
		}
		{
			button1userantworten = new Button(group2, SWT.CHECK | SWT.LEFT);
			button1userantworten.setText("Postings mit Usernamen + Antworten");
			button1userantworten.setBounds(298, 51, 281, 20);
			button1userantworten.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1userantwortenWidgetSelected(evt);
				}
			});
		}
		// baut die liste aller user (liste der linken seite)
		int minprio=SG.get_zahl(text1minprio.getText());
		int maxprio=SG.get_zahl(text1maxprio.getText());
		genObserveUserliste(table1userliste, usermap,minprio,maxprio);
		tab.setControl(group1);
		group1.pack();
		group1.setSize(1384, 770);
		group2.pack();
		group3.pack();
	}

	public void genObserveUserliste(Table table,
			HashMap<Integer, String> usermap,int minprio,int maxprio)
	{
		// hier wird die liste der user aufgebaut die beobachtet werden
		// Ausserdem einige spezialuser
		SwtTabelle.baueTabelleObserveuser(table, udb_glob, usermap,
				display_glob,minprio,maxprio);
	}

	public void genUserThreadliste(Group group, Table table,
			HashMap<Integer, Integer> threadmap, List<String> seluserlist,
			int maxprognosealter, boolean nuraktien, boolean nurneuethreads,
			boolean nurprognosen, boolean nurthreadsmitpages,
			boolean gelesAusblenden, boolean pusched, int maxpostalter)
	{
		pdb_glob = new PrognosenDB();
		prognosenliste_glob = pdb_glob.calcPrognosenliste(maxprognosealter);

		// Hier wird für einen User die Threadliste erstellt und aufgebaut
		SwtTabelle.baueTabelleObservethreads(group, table, udb_glob, threadmap,
				seluserlist, tdb_glob, display_glob, prognosenliste_glob,
				nuraktien, nurneuethreads, nurprognosen, nurthreadsmitpages,
				gelesAusblenden, pusched, maxpostalter);
	}

	private void baueSelektionsliste(String name, List<String> seluserlist)
	{
		// name: dies ist der String der selektiert worden ist
		// Die selektionsliste erstellen,
		// dies ist die liste der User die ausgewählt worden ist
		seluserlist.clear();

		// Falls das spezielle Keyword angewählt worden ist
		// dann nimm alle namen auf
		if (name.contains(GC.AlleObserveuser))
		{
			int anz = usermap.size();
			for (int i = 0; i < anz; i++)
			{
				String nam = usermap.get(i);
				if (nam != GC.AlleObserveuser)
					seluserlist.add(nam);
			}
		} else
			// es ist nur ein Name
			seluserlist.add(name);
	}

	private void ActionTable1(SelectionEvent evt, Group group,
			HashMap<Integer, String> usermap,
			HashMap<Integer, Integer> threadmap, Display dis,
			List<String> seluserlist)
	{
		String sel = null, name = null;
		// Hier wurde ein User ausgewählt und dessen Postinglisten wird
		// angezeigt
		// hier wird die rechte threadliste aufgebaut
		// usermap:ist für die linke liste
		// threadmap:ist für die rechte liste
		// seluserlist:

		System.out.println("table1.widgetSelected, event=" + evt);
		String seltext = evt.item.toString();
		if (seltext.contains("{"))
		{
			sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");
		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");
		
		baueUserThreadliste(dis,seluserlist,group,name);
	
	}
	private void baueUserThreadliste(Display dis,List<String> seluserlist,Group group,String name)
	{
		seluser_glob = name;
		// Baut die Liste der ausgewählten user auf (Es können auch mehrere User
		// in dieser Liste sein
		baueSelektionsliste(name, seluserlist);

		int maxprognosealter = Integer
				.valueOf(text1prognosenmaxalter.getText());
		int maxpostalter = Integer.valueOf(text1maxlastpostdate.getText());
		boolean nuraktien = button1nurAktienthreads.getSelection();
		boolean nurneuethreads = button1neueThreads.getSelection();
		boolean nurprognosen = button1nurprognosen.getSelection();
		boolean nurthreadsmitpages = button1nurthreadsmitpages.getSelection();
		boolean geleseneAusblenden = button1geleseneausblenden.getSelection();
		boolean pusched = button1gepusched.getSelection();
		// baue für diesen user die Threadliste auf
		genUserThreadliste(group, table2threadliste, threadmap, seluserlist,
				maxprognosealter, nuraktien, nurneuethreads, nurprognosen,
				nurthreadsmitpages, geleseneAusblenden, pusched, maxpostalter);
		Swttool.wupdate(dis);
	}
	private void button1zeigeUserWidgetSelected(SelectionEvent evt,Group group) 
	{
		//Button Zeige einen bestimmten User
		System.out.println("button1zeigeUser.widgetSelected, event="+evt);
		
		seluserlist_glob = new ArrayList<String>();
		String unam=text1zeigeUser.getText();
		
		seluserlist_glob.add(unam);
		baueUserThreadliste(display_glob,seluserlist_glob,group,unam);
		
	}
	private void ActionTable2(SelectionEvent evt, HashMap<Integer, Integer> map)
	{
		String msgstr = null;
		// Hier wurde ein Thread ausgewählt und jetzt werden die postings
		// angezeigt
		System.out.println("table2.widgetSelected, event=" + evt);
		String seltext = evt.item.toString();
		String sel = seltext.substring(seltext.indexOf("{") + 1);
		sel = sel.replace("}", "");
		sel = sel.replace("*", "");
		sel = sel.replace("P", "");

		// ermitteln der tid
		int tid = map.get(SG.get_zahl(sel));
		seltid_glob = tid;

		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.SearchThreadid(tid);
		if (tdbo == null)
		{
			String link = Tools.calcTargetnameSimple(tid);
			msgstr = "nicht in threads.db " + link;
			text1min.setText("?");
			text1max.setText("?");
		} else
		{
			msgstr = "pos<" + sel + "> tid<" + tid + "> tnam<"
					+ tdbo.getThreadname() + ">";
			// Den Threadnamen darstellen
			text1selthread.setText(msgstr);

			int panz = tdbo.getPageanz();
			text1min.setText("1");
			text1max.setText(String.valueOf(panz));
			slider1.setMinimum(1);
			slider1.setMaximum(panz + 10);
		}
		text1selthread.setText(msgstr);
		button1keinBalken.setSelection(true);
		Swttool.wupdate(display_glob);
	}

	private void slider1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("slider1.widgetSelected, event=" + evt);
		// TODO add your code for slider1.widgetSelected
		int pos = slider1.getSelection();

		if (pos == 0)
			selpage_glob = 1;
		else
			selpage_glob = pos;

		text1pos.setText(String.valueOf(pos));
	}

	private void button1loadAllUserthreadsSelected(SelectionEvent evt,
			Group group, ProgressBar pb)
	{
		// Hier wurde der Button gedrückt um die Userposting zu aktualisieren
		System.out
				.println("button1loadAllUserthreadsSelected.widgetSelected, event="
						+ evt);

		Reporting rep = new Reporting();

		if ((seluser_glob == null))
		{
			Tracer.WriteTrace(10, "Error: wähle erst einen user aus");
			return;
		}

		String fnam = GC.rootpath + "\\db\\observe_tmp.txt";
		if (FileAccess.FileAvailable0(fnam))
			FileAccess.FileDelete(fnam, 1);

		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.writezeile(seluser_glob + "#0");
		inf.close();

		if (GC.ONLINEMODE == 1)
			rep.StartAktualisiereObserveUser(0, "observe_tmp.txt", 2,
					GC.KEIN_USERGEWINNRANG, pb, null, display_glob, null);

		// baut die selektionsliste auf
		seluserlist_glob.clear();
		seluserlist_glob.add(seluser_glob);

		int maxprognosealter = Integer
				.valueOf(text1prognosenmaxalter.getText());
		int maxpostalter = Integer.valueOf(text1maxlastpostdate.getText());
		boolean nuraktien = button1nurAktienthreads.getSelection();
		boolean nurneuethreads = button1neueThreads.getSelection();
		boolean nurprognosen = button1nurprognosen.getSelection();
		boolean nurthreadsmitpages = button1nurthreadsmitpages.getSelection();
		boolean geleseneAusblenden = button1geleseneausblenden.getSelection();
		boolean pusched = button1gepusched.getSelection();
		genUserThreadliste(group, table2threadliste, threadmap,
				seluserlist_glob, maxprognosealter, nuraktien, nurneuethreads,
				nurprognosen, nurthreadsmitpages, geleseneAusblenden, pusched,
				maxpostalter);
	}
	
	
	private void button1showpostingsWidgetSelected(SelectionEvent evt)
	{
		// Hier wurde der Button gedrückt um die Postings anzuzeigen
		System.out.println("button1showpostings.widgetSelected, event=" + evt);
		HashSet<String> usermenge = new HashSet<String>();

		int rang = 50000;
		String rangstrg = text1rang.getText();
		if (rangstrg.length() > 1)
		{
			rang = Integer.valueOf(text1rang.getText());
		}
		int postanzahl = SG.get_zahl(text1postanzahl.getText());
		String fnam = GC.rootpath + "\\tmp\\showthread.html";
		SammleThreadpostings samt = new SammleThreadpostings();
		boolean keinebalkenflag = button1keinBalken.getSelection();
		Fensterinfo finfo = new Fensterinfo();
		ThreadDbObj tdbo = tdb_glob.SearchThreadid(seltid_glob);

		// nach dem Auswahlkriterium wird entsprechend die html-postings
		// gesammelt
		if (button1showpostings.getSelection() == true)
		{
			// Hier werden bestimmte Pages einer selektierten ID angezeigt
			if (samt.StartSammlePostingsPageid(tdb_glob, seltid_glob,
					selpage_glob, postanzahl, fnam, udb_glob, rang,
					keinebalkenflag, usermenge, finfo, nurmitUsernamen_flag,
					seluser_glob, mitAntworten_flag) == false)
				return;
		} else if (button1showdate.getSelection() == true)
		{
			String startdatum = text1date.getText();
			if (button1geleseneausblenden.getSelection() == true)
				startdatum = tdbo.getLastreadtime();

			finfo.setSuchstartdatum(startdatum);

			// Hier werden die letzten Pages(selektion anhand datum) angezeigt
			if (samt.StartSammlePostingsStartdatum(tdb_glob, seltid_glob,
					startdatum, postanzahl, fnam, udb_glob, rang,
					keinebalkenflag, usermenge, finfo, nurmitUsernamen_flag,
					seluser_glob, mitAntworten_flag) == false)
				return;
		} else
			Tracer.WriteTrace(10, "Error: internal einer muss gewählt sein");

		Tab10uiShowBrowser sw = new Tab10uiShowBrowser();
		sw.init(display_glob, udb_glob, tdb_glob, fnam, usermenge, finfo,
				seltid_glob, tdbo);
		// Viewer view = new Viewer();
		// view.viewHtmlExtFile(display_glob, fnam);
	}

	static private void button1postingsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1postings.widgetSelected, event=" + evt);
		button1showdate.setSelection(false);
	}

	static private void button1showdateWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1showdate.widgetSelected, event=" + evt);
		button1postings.setSelection(false);
	}

	static private void button1showvertrauenWidgetSelected(SelectionEvent evt)
	{
		// Zeigt Vertrauenswerte an
		System.out.println("button1showvertrauen.widgetSelected, event=" + evt);
		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.SearchThreadid(seltid_glob);
		if (tdbo == null)
		{
			Tracer.WriteTrace(10, "Info: tid<" + seltid_glob
					+ "> nicht in threads.db");
			return;
		}
		// baue die graphik auf (mit vertrauenswerten)
		SwingVertrau1 svert = new SwingVertrau1("titel", tdb_glob, tdbo, 1, 0);
	}

	private void button1geleseneausblendenWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1geleseneausblenden.widgetSelected, event="
				+ evt);
		button1NurUsername.setSelection(false);
		nurmitUsernamen_flag = 0;
	}

	private void button1NurUsernameWidgetSelected(SelectionEvent evt)
	{
		// Es werden Postings nur mit usernamen gesucht
		System.out.println("button1NurUsername.widgetSelected, event=" + evt);

		if (button1NurUsername.getSelection() == true)
			nurmitUsernamen_flag = 1;
		else
			nurmitUsernamen_flag = 0;

		// wenn nur die usernamen angezeigt werden sollen, dann werden auch die
		// schon gelesenen
		// informationen angezeigt
		button1geleseneausblenden.setSelection(false);
		button1userantworten.setSelection(false);
	}

	private void button1userantwortenWidgetSelected(SelectionEvent evt)
	{
		// Es werden Postings nur mit usernamen + antworten gesucht
		System.out.println("button1userantworten.widgetSelected, event=" + evt);

		if (button1userantworten.getSelection() == true)
			mitAntworten_flag = 1;
		else
			mitAntworten_flag = 0;
		button1NurUsername.setSelection(false);
		button1geleseneausblenden.setSelection(false);
	}

	private void button1ueberwaufnahmeWidgetSelected(SelectionEvent evt)
	{
		// thread und somit mid in die Überwachung mit aufnehmen
		System.out
				.println("button1ueberwaufnahme.widgetSelected, event=" + evt);

		int tid = seltid_glob;

		AktDbObj aobj = (AktDbObj) aktdb_glob.GetObject(tid);
		int mid = aobj.getMasterid();

		Tab12ExtEdit t12 = new Tab12ExtEdit(uebdb_glob, display_glob, "1",
				String.valueOf(mid), aobj.getAktname(), Tools
						.entferneZeit(Tools.get_aktdatetime_str()), "lastload",
				"filepath", "minval", "maxval", aobj.getSymbol(), "wkn",
				"isin", "Puschertext", "new", 0);
	}

	private void button1zeigeKurswerteWidgetSelected(SelectionEvent evt)
	{
		// zeigt die kurswerte für ein Symbol an
		System.out
				.println("button1zeigeKurswerte.widgetSelected, event=" + evt);
		ThreadDbObj tdbo = tdb_glob.SearchThreadid(seltid_glob);
		String symbol = tdbo.getSymbol();

		kdb_glob.zeigeSwtTabelle(display_glob, symbol, "");
	}

	private void button1zeigepostinglistenWidgetSelected(SelectionEvent evt)
	{
		// Zeige Postinglisten
		System.out.println("button1zeigepostinglisten.widgetSelected, event="
				+ evt);

		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObject(seltid_glob);
		int mid = tdbo.getMasterid();

		tdb_glob.ZeigePostinglisten(mid);

	}

	private void button1zeigePuschedkeywoerterWidgetSelected(SelectionEvent evt)
	{
		// Zeige Keywortlisten
		System.out
				.println("button1zeigePuschedkeywoerter.widgetSelected, event="
						+ evt);

		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObject(seltid_glob);
		int mid = tdbo.getMasterid();

		String fna = GC.rootpath + "\\tmp\\keyliste.txt";

		KeyDB kdb = new KeyDB();
		kdb.ErstellePuschedkeywortliste(display_glob, mid, fna);
	}

	private void button1zeigePrognosenlisteWidgetSelected(SelectionEvent evt)
	{
		// Button zeige prognosenliste
		System.out.println("button1zeigePrognosenliste.widgetSelected, event="
				+ evt);

		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObject(seltid_glob);
		int mid = tdbo.getMasterid();

		String fna = GC.rootpath + "\\db\\prognosenliste\\"+mid+".txt";

		if (FileAccess.FileAvailable(fna) == false)
		{
			Tracer.WriteTrace(10, "Info: Keine Prognosen fnam<" + fna
					+ "> vorhanden");
			return;
		}
		Tab13ShowTexte st = new Tab13ShowTexte();
		st.init(display_glob, fna, "boerblatt");
	}
	
	private void button1zeigeSliderWidgetSelected(SelectionEvent evt) 
	{
		//Button zeige Slider
		System.out.println("button1zeigeSlider.widgetSelected, event="+evt);
		
		String fna = GC.rootpath + "\\db\\slider\\"+seltid_glob+".txt";
		if (FileAccess.FileAvailable(fna) == false)
		{
			Tracer.WriteTrace(10, "Info: Kein 20Tage Slider für fname<" + fna
					+ "> ");
			return;
		}
		Tab13ShowTexte st = new Tab13ShowTexte();
		st.init(display_glob, fna, "boerblatt");
	}
	
	private void button1zeigeSlider90WidgetSelected(SelectionEvent evt) 
	{
		//Button zeige Slider 90
		System.out.println("button1zeigeSlider90.widgetSelected, event="+evt);
		
		String fna = GC.rootpath + "\\db\\slider90\\"+seltid_glob+".txt";
		if (FileAccess.FileAvailable(fna) == false)
		{
			Tracer.WriteTrace(10, "Info: Kein 90Tage Slider für fname<" + fna
					+ ">");
			return;
		}
		Tab13ShowTexte st = new Tab13ShowTexte();
		st.init(display_glob, fna, "boerblatt");
	}
	
	private void button1refreshWidgetSelected(SelectionEvent evt) 
	{
		//Button:Refresh
		System.out.println("button1refresh.widgetSelected, event="+evt);
		
		int minprio=SG.get_zahl(text1minprio.getText());
		int maxprio=SG.get_zahl(text1maxprio.getText());
		genObserveUserliste(table1userliste, usermap,minprio,maxprio);
	}

}
