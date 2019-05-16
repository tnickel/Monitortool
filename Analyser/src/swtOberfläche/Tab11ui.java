package swtOberfläche;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import hilfsrepeattask.SlidWorker;

import java.util.HashSet;

import kurse.KurseDB;
import mainPackage.GC;
import objects.ThreadDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import ranking.SVirtI;
import ranking.SVirtIII;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;

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
public class Tab11ui
{
	static private Display display_glob = null;
	static private List list1;
	static private List list2;
	static private Label label1;
	static private Label label5;
	static private Button button2protoflag;
	static private Label label4;
	static private Button button2forceflag;
	static private Label label3;
	static private Text text1tid;
	static private Label label2;
	static private Text text1attribpfad;
	static private Button button2bewerteTidmenge;
	static private Button button2starteinzel;
	static private Group group3;
	static private Button button2bewerteAlles;
	static private Group group2;
	static private Button button2Standartauswertungsflag;
	static private Button button2Sliderauswertungsflag;
	static private Text text1bundlesize;
	static private Group group1;
	static private Button button2zurueck;
	static private Button button2hin;
	static private Button button1;
	static private ProgressBar progressBar1;
	static private ThreadsDB tdb_glob = null;
	static private KurseDB kdb_glob = null;
	static private UserDB udb_glob=null;

	static public void init(TabFolder folder, TabItem tab, Display dis,
			ThreadsDB tdb, KurseDB kdb,UserDB udb)
	{
		display_glob = dis;
		tdb_glob = tdb;
		kdb_glob = kdb;
		udb_glob = udb;

		Group group_glob = new Group(folder, SWT.NONE);
		group_glob.setText("Bewerte Threads bzgl. Masteruser");
		tab.setControl(group_glob);

		group_glob.setLayout(null);
		{
			progressBar1 = new ProgressBar(group_glob, SWT.NONE);
			progressBar1.setBounds(12, 671, 889, 21);
		}
		{
			list1 = new List(group_glob, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER);
			list1.setBounds(12, 22, 161, 444);
		}
		{
			button2hin = new Button(group_glob, SWT.PUSH | SWT.CENTER);
			button2hin.setText("<--");
			button2hin.setSize(60, 30);
			button2hin.setBounds(187, 233, 60, 30);
		}
		{
			button2zurueck = new Button(group_glob, SWT.PUSH | SWT.CENTER);
			button2zurueck.setText("--->");
			button2zurueck.setSize(60, 30);
			button2zurueck.setBounds(187, 275, 60, 30);
		}
		{
			list2 = new List(group_glob, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER);
			list2.setBounds(265, 24, 160, 444);
		}
		{
			group1 = new Group(group_glob, SWT.NONE);
			group1.setLayout(null);
			group1.setText("Bewertungsaktionen");
			group1.setBounds(12, 480, 889, 185);
			{
				button1 = new Button(group1, SWT.PUSH | SWT.CENTER);
				button1.setText("Start ");
				button1.setBounds(484, 148, 393, 25);
				button1.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1WidgetSelected(evt);
					}
				});
			}
			{
				text1bundlesize = new Text(group1, SWT.NONE);
				text1bundlesize.setBounds(6, 23, 97, 20);
				text1bundlesize.setText("100");
			}
			{
				label1 = new Label(group1, SWT.NONE);
				label1.setText("Bundlesize (100)");
				label1.setBounds(115, 23, 115, 20);
			}
			{
				button2Sliderauswertungsflag = new Button(group1, SWT.CHECK
						| SWT.LEFT);
				button2Sliderauswertungsflag.setText("SliderAuswertungsflag");
				button2Sliderauswertungsflag.setBounds(6, 55, 211, 18);
			}
			{
				button2Standartauswertungsflag = new Button(group1, SWT.CHECK
						| SWT.LEFT);
				button2Standartauswertungsflag
						.setText("StandartAuswertungsflag");
				button2Standartauswertungsflag.setBounds(6, 79, 184, 20);
			}
		}
		button2Sliderauswertungsflag.setSelection(true);
		button2Standartauswertungsflag.setSelection(true);

		{
			group2 = new Group(group1, SWT.NONE);
			group2.setLayout(null);
			group2.setText("Aktion");
			group2.setBounds(271, 23, 207, 150);
			{
				button2bewerteAlles = new Button(group2, SWT.CHECK | SWT.LEFT);
				button2bewerteAlles.setText("BewerteAlles");
				button2bewerteAlles.setBounds(8, 22, 98, 19);
				button2bewerteAlles.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2bewerteAllesWidgetSelected(evt);
					}
				});
			}
			{
				button2bewerteTidmenge = new Button(group2, SWT.CHECK
						| SWT.LEFT);
				button2bewerteTidmenge.setText("BewerteTidMenge");
				button2bewerteTidmenge.setBounds(8, 46, 131, 19);
				button2bewerteTidmenge
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button2bewerteTidmengeWidgetSelected(evt);
							}
						});
			}
			{
				button2forceflag = new Button(group2, SWT.CHECK | SWT.LEFT);
				button2forceflag.setText("Forceflag");
				button2forceflag.setBounds(8, 70, 130, 19);
			}
			{
				label4 = new Label(group2, SWT.NONE);
				label4.setText("(inf)");
				label4.setBounds(171, 70, 24, 17);
				label4
						.setToolTipText("Falls das forceflag gesetzt ist wird das Attribut auf jeden Fall neu berechnet. Ist es nicht gesetzt wird geschaut ob schon was berechnetes vorliegt.");
			}
			{
				button2protoflag = new Button(group2, SWT.CHECK | SWT.LEFT);
				button2protoflag.setText("Protokollflag");
				button2protoflag.setBounds(8, 95, 130, 18);
			}
		}
		{
			text1attribpfad = new Text(group1, SWT.NONE);
			text1attribpfad.setBounds(484, 55, 393, 20);
		}
		{
			label2 = new Label(group1, SWT.NONE);
			label2.setText("Attributpfad");
			label2.setBounds(484, 36, 268, 19);
		}
		text1bundlesize.setText("100");
		button2bewerteTidmenge.setSelection(false);
		text1attribpfad
				.setText("\\db\\userthreadvirtualkonto\\Einzelbewertung\\Attribute");

		button2forceflag.setSelection(true);
		button2protoflag.setSelection(true);
		{
			label5 = new Label(group2, SWT.NONE);
			label5.setText("(inf)");
			label5.setBounds(171, 95, 29, 30);
			label5
					.setToolTipText("Falls das Protokollflag gesetzt ist werden in dem Verzeichniss Attribute/Einzelbewertung ausführliche Protokollinformationen geschrieben.");
		}
		
		{
			group3 = new Group(group_glob, SWT.NONE);
			group3.setLayout(null);
			group3.setText("Einzelbewertung");
			group3.setBounds(543, 285, 214, 171);
			{
				text1tid = new Text(group3, SWT.NONE);
				text1tid.setBounds(8, 22, 82, 20);
				text1tid.setText("1153986");
			}
			{
				label3 = new Label(group3, SWT.NONE);
				label3.setText("tid");
				label3.setBounds(125, 22, 60, 30);
			}
			{
				button2starteinzel = new Button(group3, SWT.PUSH | SWT.CENTER);
				button2starteinzel.setText("Start Einzel");
				button2starteinzel.setBounds(8, 141, 138, 23);
				button2starteinzel.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2starteinzelWidgetSelected(evt);
					}
				});
			}
		}
		// Inhalt des Group erstellen

		group_glob.pack();
		group2.pack();
		group1.pack();
		folder.pack();
	}

	static private void button1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1.widgetSelected, event=" + evt);
		// 05.01.10
		// a)Hier wird geschaut welches die beste Handelstrategie ist
		// Sämmtliche Handelsstrategien werden bewertet und gschaut wieviel
		// Gewinn rauskommt
		// Die Gewinne kann man in den virtuellen Konten sehen.
		// b)Ausserdem wird der Attributstore wieder neu aufgebaut
		// c)Ausserdem werden die Gewinnverläufe abgespeichert

		// kdb_glob.ladeAlleKurseUpdateKurseDB(tdb,30, 0,1);

		/*
		 * UeberpruefeAlleKursePlausi uku = new UeberpruefeAlleKursePlausi();
		 * uku.Start();
		 */
		Tracer.WriteTrace(20, "Start time:" + Tools.get_aktdatetime_str());
		// hier wird gesammelt wieviel gewinn/verlust die einzelnen User
		// machen

		if (tdb_glob == null)
			tdb_glob = new ThreadsDB();
		UserGewStrategieDB2 ugewinnstrategie2 = new UserGewStrategieDB2(
				"UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb");
		// wenn das sliderauswertungsflag=1 ist werden auch die masteruser
		// ausgewertet
		String bundlesize = text1bundlesize.getText();
		int slflag = 0, stflag = 0, protoflag = 0;

		String attribpfad = text1attribpfad.getText();

		if (button2Sliderauswertungsflag.getSelection() == true)
			slflag = 1;
		if (button2Standartauswertungsflag.getSelection() == true)
			stflag = 1;
		if (button2protoflag.getSelection() == true)
			protoflag = 1;

		if (button2bewerteAlles.getSelection() == true)
		{
			SVirtI s = new SVirtI(tdb_glob);
			s.SucheBestenMasteruser(GC.SUCHMODE_UEBERSPRINGE_FEHLER, Tools
					.get_zahl(bundlesize), GC.MODE_ALL, slflag, stflag,
					tdb_glob);
		}
		Tab11uiShow tab11show = new Tab11uiShow();
		int tid = Integer.valueOf(text1tid.getText());
		int forceflag = 0;
		if (button2forceflag.getSelection() == true)
			forceflag = 1;

		if (button2bewerteTidmenge.getSelection() == true)
		{
			SVirtIII s3 = new SVirtIII(tdb_glob);
			s3.BewerteTidmenge(GC.SUCHMODE_UEBERSPRINGE_FEHLER, Tools
					.get_zahl(bundlesize), GC.MODE_ALL, slflag, stflag,
					attribpfad, tid, forceflag, protoflag, tdb_glob);
		}
		tab11show.ShowTid(display_glob, tid);
		ThreadDbObj tdbo = tdb_glob.SearchThreadid(tid);

		Tab11uiShowSliderSwing ts1 = new Tab11uiShowSliderSwing("xx", tdb_glob,
				tdbo);

		/*
		 * s.SucheBestenMasteruser(GC.SUCHMODE_UEBERSPRINGE_FEHLER, 100,
		 * GC.MODE_ALL, 1, 0);
		 */
	}

	static private void button2bewerteAllesWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2bewerteAlles.widgetSelected, event=" + evt);
		button2bewerteTidmenge.setSelection(false);
		button2bewerteAlles.setSelection(true);
	}

	static private void button2bewerteTidmengeWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2bewerteTidmenge.widgetSelected, event="
				+ evt);
		button2bewerteTidmenge.setSelection(true);
		button2bewerteAlles.setSelection(false);
	}

	static private void button2starteinzelWidgetSelected(SelectionEvent evt)
	{
		// Button Start Einzel
		System.out.println("button2starteinzel.widgetSelected, event=" + evt);

		SlidWorker slider = new SlidWorker(tdb_glob,udb_glob);
		SlideruebersichtDB sldb = new SlideruebersichtDB();
		HashSet<Integer> tidmenge = new HashSet<Integer>();
		
		int tid=Tools.get_zahl(text1tid.getText());
		tidmenge.add(tid);
		
		slider.GenPrognosen(sldb,0,GC.SUCHMODE_UEBERSPRINGE_FEHLER, 20,
				GC.KEINE_EINZELGEWINNE, 1, 10,200,1,200,progressBar1,tidmenge);
	}
}
