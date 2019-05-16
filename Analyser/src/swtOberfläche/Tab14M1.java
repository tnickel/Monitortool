package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.Collections;

import mainPackage.GC;
import objects.ThreadDbObj;
import objects.ThreadTable;

import org.eclipse.swt.SWT;
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

import slider.SlideruebersichtObj;
import stores.AktDB;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import swtViewer.ViewExternFileTable;
import attribute.ThreadAttribStoreI;

import comperatoren.TdboSortComp;

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
public class Tab14M1
{
	private Display display_glob = null;
	private AktDB aktdb_glob = null;
	
	private Table table1;
	private Label label1;
	private Button button3;
	private Button button2;
	private Button button1;
	private Button button6suche;
	private Text text1suche;

	private Group group1;
	private Button button1zeigeKurseSL20;
	private Group group1tdbfilter;
	private Label label4;
	private Text text1lastthreadposttime;
	private Label label2;
	private Button button1zeigeAttribute;
	private Label label5;
	private Text text1minslider;
	private Button button6kleineSliderausblenden;
	private Label label3;
	private Text text1lastposting_glob;
	private Button button6keinDAX;
	private Button button6nurAktWerte;
	private Button button6altesAusblenden;
	private Button button1refresh;

	private ThreadsDB tdb_glob = null;
	private Group groupM1;
	private ProgressBar progressBar1_glob;
	private ArrayList<ThreadTable> sortlist = new ArrayList<ThreadTable>();
	private SlideruebersichtDB sldb_glob = null; //new SlideruebersichtDB();

	// sortsel=namen des Buttons
	private ArrayList<String> sortsel_glob = new ArrayList<String>();

	private ArrayList<Button> sortbutton_glob = new ArrayList<Button>();

	// gibt an nach welcher Spalte die Tabelle sortiert wird

	private Group group2;
	private Button button5;
	private Button button4;
	private int selpos_glob = 0;
	private String tabellenkopf_str = "Pos#Threadname#tid#lastAttr.Date#nUser#+User#-User#Sl20_3T#Sl20akt#Sl20steig#Threadfullname#Prio";

	public void init(TabFolder folder, TabItem tab, Display dis, ThreadsDB tdb,SlideruebersichtDB sldb)
	{
		tdb_glob = tdb;
		display_glob = dis;
		sldb_glob=sldb;
		
		groupM1 = new Group(folder, SWT.NONE);
		groupM1.setText("S1");
		groupM1.setLayout(null);
		{
			button1refresh = new Button(groupM1, SWT.PUSH | SWT.CENTER);
			button1refresh.setText("Refresh");
			button1refresh.setBounds(783, 707, 60, 30);
			button1refresh.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1refreshWidgetSelected(evt);
				}
			});
		}
		{
			table1 = new Table(groupM1, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER | SWT.FULL_SELECTION);
			table1.setBounds(37, 26, 687, 382);
			table1.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					table1WidgetSelected(evt);
				}
			});

		}
		{
			progressBar1_glob = new ProgressBar(groupM1, SWT.NONE);
			progressBar1_glob.setBounds(39, 461, 687, 20);
		}
		{
			label1 = new Label(groupM1, SWT.NONE);
			label1.setText("(i)");
			label1.setBounds(732, 28, 21, 19);
			label1
					.setToolTipText("lastdate=Dies ist das letzte Datum aus dem Attributstore.");
		}
		{
			button1zeigeAttribute = new Button(groupM1, SWT.PUSH | SWT.CENTER);
			button1zeigeAttribute.setText("ZeigeAttribute");
			button1zeigeAttribute.setBounds(771, 28, 141, 24);
			button1zeigeAttribute.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1zeigeAttributeWidgetSelected(evt);
				}
			});
		}
		{
			label2 = new Label(groupM1, SWT.NONE);
			label2.setText("x");
			label2.setBounds(1028, 719, 60, 30);
		}
		{
			group1tdbfilter = new Group(groupM1, SWT.NONE);
			group1tdbfilter.setLayout(null);
			group1tdbfilter.setText("Filter");
			group1tdbfilter.setBounds(47, 493, 402, 256);
			{
				text1lastthreadposttime = new Text(group1tdbfilter, SWT.NONE);
				text1lastthreadposttime.setBounds(149, 49, 38, 17);
				text1lastthreadposttime.setText("30");
				text1lastthreadposttime
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								text1lastthreadposttimeWidgetSelected(evt);
							}
						});
			}
			{
				label4 = new Label(group1tdbfilter, SWT.NONE);
				label4.setText(" älter Tage");
				label4.setBounds(194, 49, 91, 25);
			}
			{
				button6altesAusblenden = new Button(group1tdbfilter, SWT.CHECK
						| SWT.LEFT);
				button6altesAusblenden.setText("altes Ausblenden");
				button6altesAusblenden.setBounds(8, 44, 144, 30);
				button6altesAusblenden.setSelection(true);
			}
			{
				button6nurAktWerte = new Button(group1tdbfilter, SWT.CHECK
						| SWT.LEFT);
				button6nurAktWerte.setText("nur AktDiskussionen");
				button6nurAktWerte.setBounds(8, 74, 163, 24);
			}
			{
				button6keinDAX = new Button(group1tdbfilter, SWT.CHECK
						| SWT.LEFT);
				button6keinDAX.setText("kein DAX");
				button6keinDAX.setBounds(8, 98, 110, 30);
			}
			{
				button6kleineSliderausblenden = new Button(group1tdbfilter,
						SWT.CHECK | SWT.LEFT);
				button6kleineSliderausblenden
						.setText("kleine Slider ausblenden");
				button6kleineSliderausblenden.setBounds(8, 127, 164, 27);
				button6kleineSliderausblenden.setSelection(true);
			}
			{
				text1minslider = new Text(group1tdbfilter, SWT.NONE);
				text1minslider.setBounds(178, 133, 60, 18);
				text1minslider.setText("20");
			}
			{
				label5 = new Label(group1tdbfilter, SWT.NONE);
				label5.setText("Minslider");
				label5.setBounds(256, 133, 60, 18);
			}
		}
		{
			group1 = new Group(groupM1, SWT.NONE);
			group1.setLayout(null);
			group1.setText("Parameter");
			group1.setBounds(771, 83, 172, 214);
			{
				button1zeigeKurseSL20 = new Button(group1, SWT.PUSH
						| SWT.CENTER);
				button1zeigeKurseSL20.setText("ZeigeKurse&Parameter");
				button1zeigeKurseSL20.setBounds(12, 179, 148, 23);
				button1zeigeKurseSL20
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1zeigeKurseSL20WidgetSelected(evt);
							}
						});
			}
			{
				button1 = new Button(group1, SWT.CHECK | SWT.LEFT);
				button1.setText("Sl20");
				button1.setBounds(12, 24, 141, 30);
			}
			{
				button2 = new Button(group1, SWT.CHECK | SWT.LEFT);
				button2.setText("neueUser");
				button2.setBounds(12, 50, 112, 30);
			}
			{
				button3 = new Button(group1, SWT.CHECK | SWT.LEFT);
				button3.setText("neue+User");
				button3.setBounds(12, 80, 112, 22);
			}
			{
				button4 = new Button(group1, SWT.CHECK | SWT.LEFT);
				button4.setText("neue - User");
				button4.setBounds(12, 108, 94, 22);
			}
			{
				button5 = new Button(group1, SWT.CHECK | SWT.LEFT);
				button5.setText("Vertrau");
				button5.setBounds(12, 136, 112, 22);
			}
		}
		{
			group2 = new Group(groupM1, SWT.NONE);
			GridLayout group2Layout = new GridLayout(2, false);
			group2.setLayout(group2Layout);
			group2.setText("Sortiereung");
			group2.setBounds(455, 493, 277, 256);
		}
		{
			text1lastposting_glob = new Text(groupM1, SWT.NONE);
			text1lastposting_glob.setBounds(39, 416, 114, 21);
			text1lastposting_glob.setText(tdb.calcLastPosting());
		}
		{
			label3 = new Label(groupM1, SWT.NONE);
			label3.setText("Lastposting");
			label3.setBounds(159, 416, 108, 30);
		}
		{
			text1suche = new Text(groupM1, SWT.NONE);
			text1suche.setBounds(783, 316, 149, 21);
		}
		{
			button6suche = new Button(groupM1, SWT.PUSH | SWT.CENTER);
			button6suche.setText("Suche");
			button6suche.setBounds(872, 343, 60, 30);
			button6suche.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button6sucheWidgetSelected(evt);
				}
			});
		}
		{
			int anz = SG.countZeichen(tabellenkopf_str, "#");
			for (int i = 0; i < anz + 1; i++)
			{
				sortbutton_glob.add(new Button(group2, SWT.RADIO));
				try
				{
					String keyword = SG.nteilstring(tabellenkopf_str, "#",
							i + 1);
					sortbutton_glob.get(i).setText(keyword);
					sortsel_glob.add(keyword);

				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (i == 0)
					sortbutton_glob.get(i).setSelection(true);

				sortbutton_glob.get(i).addSelectionListener(
						new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								sortbuttonWidgetSelected(evt);
							}
						});
			}
		}
		tab.setControl(groupM1);
		groupM1.pack();
		group2.pack();
	}

	private void treeConfig()
	{
	}

	private void button1refreshWidgetSelected(SelectionEvent evt)
	{
		/*
		 * Hier ist die baustelle. Was muss getan werden.
		 * 
		 * a) initSortliste(baut liste gnadenlos auf) b) checkFilter hier wird
		 * aus der sortliste rausgefiltert was irrelevant ist c) sortTable d)
		 * baue Tabelle
		 */

		int spaltenpos = 0;
		// Refresh button gedrückt
		System.out.println("refresh");
		Boolean altAusblenden = button6altesAusblenden.getSelection();
		Boolean keinDax = button6keinDAX.getSelection();
		Boolean nurAkt = button6nurAktWerte.getSelection();
		initSortliste(sortlist);

		// Werte das Kriterium aus nachdem sortiert werden soll
		int anz = sortbutton_glob.size();
		for (int i = 1; i < anz; i++)
	
		{
			if (sortbutton_glob.get(i).getSelection() == true)
			{
				spaltenpos = i;
			}
		}

		sortTable(sortlist, spaltenpos);
		baueTabelleThreadsAttribute(groupM1, table1, sortlist, altAusblenden,
				keinDax, nurAkt);
	}

	public void initSortliste(ArrayList<ThreadTable> sortlist)
	{
		// In die Sortliste wird erst mal alles aufgenommen
		String proto = GC.rootpath + "\\tmp\\user20tage.txt";

		// die sortliste am einfang einmal aufbauen
		if (sortlist.size() > 0)
			return;

		Inf inf = new Inf();
		inf.setFilename(proto);
		FileAccess.FileDelete(proto, 0);

		String lastattribdate = null;
		int anz = sortlist.size();

		anz = tdb_glob.GetanzObj();
		progressBar1_glob.setMinimum(0);
		progressBar1_glob.setMaximum(anz);
		for (int i = 0; i < anz; i++)
		{
			progressBar1_glob.setSelection(i);
			Swttool.wupdate(display_glob);
			ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObjectIDX(i);
			ThreadAttribStoreI attribstore = new ThreadAttribStoreI(tdbo
					.getThreadid(), "\\db\\Attribute\\Threads");
			if (attribstore.GetanzObj() == 0)
			{
				Tracer.WriteTrace(20, "Info:Keine Attribute für tid<"
						+ tdbo.getThreadid() + "> tnam<" + tdbo.getThreadname()
						+ "> im Attribstore");
			} else
			{
				lastattribdate = attribstore.calcMaxdatum();
				float uneu_bad = attribstore.getAttrib(lastattribdate,
						GC.ATTRIB_anzNeueBadUser);
				float uneu_plus = attribstore.getAttrib(lastattribdate,
						GC.ATTRIB_anzNeueGuteUser);
				float uneu_minus = attribstore.getAttrib(lastattribdate,
						GC.ATTRIB_anzNeueSchlechteUser);

				float uneu = uneu_bad + uneu_plus + uneu_minus;
				float slgr = attribstore.getAttrib(lastattribdate,
						GC.ATTRIB_USERIN20TAGEN);

				// Rechne den durchschnittlichen Slider20 der letzten 3 Tage
				String adate = lastattribdate;
				float valsum = 0, valcounter = 0, trycounter = 0;
				inf.writezeile("....neue tid<" + tdbo.getThreadid() + ">");

				// Die 3 tage Slidersumme berechnen
				valsum = calcSlider3Tage(inf, tdbo, attribstore, adate, valsum,
						valcounter, trycounter);

				float slider20t3groesse = valsum / 3;
				inf.writezeile("valsum<" + valsum + "> sl20t3<"
						+ slider20t3groesse + ">");
				float slider20Steigung = (100 / slider20t3groesse) * slgr;

				ThreadTable tt = new ThreadTable();
				tt.setTdbo(tdbo);
				tt.setAttLastdate(lastattribdate);
				tt.setAnzguteuser(uneu_plus);
				tt.setAnzneuUser(uneu);
				tt.setAnzschluser(uneu_minus);
				tt.setSlider20groesse(slgr);
				tt.setSlider20t3groesse(slider20t3groesse);
				tt.setSlider20Steigung(slider20Steigung);

				SlideruebersichtObj slobj = (SlideruebersichtObj) sldb_glob
						.GetObject(tdbo.getThreadid());
				int prio = sldb_glob.calcPrio(tdbo, 1);
				tt.setPrio(prio);
				String lastslupdate = slobj.getLastupdate();
				tt.setLastSliderUpdate(lastslupdate);

				// Alles aufnehmen was Attribute hat
				sortlist.add(tt);
			}
		}
	}

	private float calcSlider3Tage(Inf inf, ThreadDbObj tdbo,
			ThreadAttribStoreI attribstore, String adate, float valsum,
			float valcounter, float trycounter)
	{
		if (attribstore.GetanzObj() < 10)
		{
			inf.writezeile("Info: keine Attribauswertung da anz<10 attribanz<"
					+ attribstore.GetanzObj() + ">");
			Tracer.WriteTrace(20, "Info:keine Attributsauswertung da val<"
					+ attribstore.GetanzObj() + "><10 Werte tid<"
					+ tdbo.getThreadid() + ">");
		} else
		{
			for (;;)
			{
				if (valcounter >= 3)
					break;

				adate = Tools.modifziereDatum(adate, 0, 0, -1, 0);
				float val = attribstore.getAttrib(adate,
						GC.ATTRIB_USERIN20TAGEN);

				if (val == 0)
				{
					trycounter++;
					if (trycounter >= 8)
					{
						Tracer.WriteTrace(20,
								"Info: Info: fehlerhafte Attributsberechnung 8 Fehlversuche tid<"
										+ tdbo.getThreadid() + ">");
						inf
								.writezeile("Info: fehlerhafte Attributsberechnung 8 Fehlversuche");
						break;
					}
					continue;
				}
				valcounter++;
				inf.writezeile("dat<" + adate + "> val<" + val + ">");
				valsum = valsum + val;
			}
		}
		return valsum;
	}

	public void baueTabelleThreadsAttribute(Group group, final Table table,
			ArrayList<ThreadTable> sortlist, Boolean altausblenden,
			Boolean keinDax, Boolean nurAkt)
	{
		// Zeige Tabelle Attribute
		int anz = sortlist.size();
		progressBar1_glob.setMinimum(0);
		progressBar1_glob.setMaximum(anz);
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);
		Swttool.baueTabellenkopf(table, tabellenkopf_str);

		// filter setzen Maxalter des Threads
		if (button6altesAusblenden.getSelection() == true)
			tdb_glob.filter.setMaxthreadalter(Integer
					.valueOf(text1lastthreadposttime.getText()));

		// 0|Pos#1|Threadname#2|tid#3|lastAttr.Date#4|nUser#5|+User#
		// 6|-User#7|Sl20_3T#8|Sl20akt#9|Sl20steig#10|Threadfullname#
		// 11|prio#12|slastupdate";
		for (int i = 0; i < anz; i++)
		{

			ThreadTable tt = (ThreadTable) sortlist.get(i);

			if (checkFilter(tt, altausblenden, keinDax, nurAkt) == false)
				continue;

			progressBar1_glob.setSelection(i);
			if (i % 100 == 0)
				Swttool.wupdate(display_glob);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, String.valueOf(i));
			item.setText(1, tt.getTdbo().getThreadname());
			item.setText(2, String.valueOf(tt.getTdbo().getThreadid()));
			item.setText(3, tt.getAttLastdate());
			item.setText(4, String.valueOf(tt.getAnzneuUser()));
			item.setText(5, String.valueOf(tt.getAnzguteuser()));
			item.setText(6, String.valueOf(tt.getAnzschluser()));
			item.setText(7, String.valueOf(tt.getSlider20t3groesse()));
			item.setText(8, String.valueOf(tt.getSlider20groesse()));
			item.setText(9, String.valueOf(tt.getSlider20Steigung()));
			item.setText(10, Tools.cutStringlaenge(
					tt.getTdbo().getUrlaktname(), 20));
			item.setText(11, String.valueOf(tt.getPrio()));
			item.setText(12, tt.getLastSliderUpdate());

			if (i % 100 == 0)
				System.out.println("baue tabelle i=<" + i + ">");
		}
		int ccount = table.getColumnCount();
		for (int i = 0; i < ccount; i++)
		{
			table.getColumn(i).pack();
		}

		System.out.println("zeige Tabelle an");
		Swttool.wupdate(display_glob);
	}

	private Boolean checkFilter(ThreadTable tt, Boolean altesAusblenden,Boolean keinDax, Boolean nurAkt)
	{
		// return: true falls Objekt die Filtereigenschaften besitzt
		// false, falls Objekt aus dem Filter fällt

		// falls im slider 20t3 fast nix drin ist dann false
		if (button6kleineSliderausblenden.getSelection() == true)
			if (tt.getSlider20groesse() <= Tools.get_zahl(text1minslider
					.getText()))
			{
				Tracer
						.WriteTrace(20, "Info: slider3Tage valsum<"
								+ tt.getSlider20groesse() + "> tid<"
								+ tt.getTdbo().getThreadid()
								+ "> zu schlecht-> weiter");
				return false;
			}

		if (altesAusblenden == true)
			if (tdb_glob.filter.checkFilter(tt.getTdbo(), altesAusblenden) == false)
				return false;

		if(keinDax==true)
			if(tt.getTdbo().getSymbol().equalsIgnoreCase("dax"))
				return false;
		
		if(nurAkt==true)
			if(tt.getTdbo().isValidAktienthread(0)==false)
				return false;
		
		return true;
	}

	private void sortTable(ArrayList<ThreadTable> sortlist, int colpos)
	{
		TdboSortComp c1 = new TdboSortComp();

		c1.setColpos(colpos);
		Collections.sort(sortlist, c1);
	}

	private void button1zeigeAttributeWidgetSelected(SelectionEvent evt)
	{
		// Button: Zeige Attribute
		System.out
				.println("button1zeigeAttribute.widgetSelected, event=" + evt);
		// hole die tid
		int tid = sortlist.get(selpos_glob).getTdbo().getThreadid();
		String fnam = GC.rootpath + "\\db\\attribute\\threads\\" + tid + ".db";

		ViewExternFileTable viewer = new ViewExternFileTable();
		viewer
				.ShowTable(
						display_glob,
						fnam,
						"",
						"date#LastpostId#Slider20#mitlRang#+U#-U#badU#-Postings#+Postings#n+U#n-U#nbadU#a12#a13#a14#a15#Handelshinweis");

	}

	private void table1WidgetSelected(SelectionEvent evt)
	{
		String sel = "", name = "";
		// Zeile ausgewählt
		System.out.println("Zeile ausgewählt");
		System.out.println("table1.widgetSelected, event=" + evt);
		String seltext = evt.item.toString();
		System.out.println("seltext<" + seltext + ">");
		if (seltext.contains("{"))
		{
			sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");
		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");
		selpos_glob = Tools.get_zahl(name);
	}

	private void text1lastthreadposttimeWidgetSelected(SelectionEvent evt)
	{
		// Set Maximum Days
		System.out.println("text1lastthreadposttime.widgetSelected, event="
				+ evt);
		tdb_glob.filter.setMaxthreadalter(Integer
				.valueOf(text1lastthreadposttime.getText()));
	}

	private void button1zeigeKurseSL20WidgetSelected(SelectionEvent evt)
	{
		// Button: zeige Kurse und Parameter
		System.out
				.println("button1zeigeKurseSL20.widgetSelected, event=" + evt);
		ThreadTable tt = sortlist.get(selpos_glob);

		// baue die graphik auf (mit vertrauenswerten)
		Tab14M1SwingKurs svert = new Tab14M1SwingKurs("titel", tdb_glob, tt
				.getTdbo());
	}

	private void sortbuttonWidgetSelected(SelectionEvent evt)
	{
		System.out.println("sortbutton.widgetSelected, event=" + evt);
		String msg = evt.toString();

		int anz = sortsel_glob.size();
		for (int i = 0; i < anz; i++)
		{
			sortbutton_glob.get(i).setSelection(false);
			String nam = sortsel_glob.get(i);
			if (msg.contains(nam))
			{
				sortbutton_glob.get(i).setSelection(true);
			}
		}
		Swttool.wupdate(display_glob);
	}
	
	private void button6sucheWidgetSelected(SelectionEvent evt) 
	{
		//Button Suche
		System.out.println("button6suche.widgetSelected, event="+evt);
	
		String suchwort=text1suche.getText();
		
		SwtTools st = new SwtTools();
		int zeile = st.sucheZeile(table1, suchwort);
		table1.setSelection(zeile);
		
	}

}
