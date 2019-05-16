package swtOberfläche;

import kurse.KursValueDB;
import mailer.MailsucherListe;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.KeyDB;
import stores.ThreadsDB;
import stores.UserDB;
import wartung.Scheduler;

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
public class Tab3ui
{
	Display dis_g = null;
	private Button button2showresults;
	private static ThreadsDB tdb_glob = null;
	private static AktDB aktdb_glob = null;
	private static UserDB udb_glob = null;
	private static KeyDB keydb_glob = null;
	private Label label2;
	private Text text34threshold2;
	private Label label1;
	private Text text34enddatum;
	private String enddatum = null;
	private Text letzterStart;
	private Button loopflag;
	private ProgressBar progressBar25;
	private ProgressBar progressBar24;
	private ProgressBar progressBar23;
	private Text text33;
	private Text text32;
	private Text text31;
	private Text text30;
	private Text text29;
	private Text text28;
	private Text text27;
	private Text text26;
	private Text text25;
	private Text text24;
	private Button button14;
	private ProgressBar progressBar22;
	private ProgressBar progressBar21;
	private ProgressBar progressBar20;
	private Text text23;
	private Text text22;
	private Text text19;
	private ProgressBar progressBar27;
	private ProgressBar progressBar26;
	private Text text34;
	private Button button16boerflag;
	private Button button16observeUser2;
	private Label label3;
	private Button button19alluser;
	private Text text21;
	private Text text20;
	private Text text18;
	private Text text17;
	private Text text16;
	private Text text15;
	private Text text14;
	private Text text13;
	private Text text12;
	private ProgressBar progressBar19;
	private ProgressBar progressBar18;
	private ProgressBar progressBar17;
	private ProgressBar progressBar16;
	private ProgressBar progressBar15;
	private ProgressBar progressBar14;
	private ProgressBar progressBar13;
	private ProgressBar progressBar12;
	private ProgressBar progressBar11;
	private Button button12;
	private Button button11;
	private Button button10;
	private Text textinfo;
	private Button button1;
	Text text1;
	Text text3;

	private Button check1;
	private Button check2;
	private Button check3;
	private Button check4;
	private Button check5;
	private Button check6;
	private Button check7;
	private Button check8;
	private Button check9;
	private Button check10;
	private Button check11;

	private Text text11;
	private Button startbutton;
	private Composite composite1;
	ProgressBar progressBar10;
	Text text10;
	ProgressBar progressBar9;
	Text text9;
	ProgressBar progressBar8;
	Text text8;
	ProgressBar progressBar3;
	Text text7;
	ProgressBar progressBar7;
	Text text6;
	ProgressBar progressBar6;
	Text text5;
	ProgressBar progressBar5;
	Text text4;
	ProgressBar progressBar4;
	ProgressBar progressBar2;
	ProgressBar progressBar1;
	Text text2;

	public Tab3ui()
	{
	}

	public void init(TabFolder folder, TabItem tab, Display dis, ThreadsDB tdb,
			AktDB aktdb,UserDB udb,KeyDB kdb)
	{
		aktdb_glob = aktdb;
		tdb_glob = tdb;
		udb_glob=udb;
		dis_g = dis;
		keydb_glob=kdb;
		Group groupListe_glob = new Group(folder, SWT.NONE);
		groupListe_glob.setText("Maintask overview");
		tab.setControl(groupListe_glob);

		{
			text1 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text1.setText("Kurse Laden");
			text1.setBounds(19, 27, 186, 21);
		}
		{
			text2 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text2.setText("LadeUserdaten");
			text2.setBounds(18, 63, 187, 21);
		}
		{
			text3 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text3.setText("calcRanking");
			text3.setBounds(18, 142, 187, 22);
		}
		{
			text4 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text4.setText("LadeSlider gen Progn.");
			text4.setBounds(18, 182, 187, 22);
		}
		{
			text5 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text5.setText("Liste Wochengewinne");
			text5.setBounds(17, 224, 187, 22);
		}
		{
			text6 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text6.setText("Zeige Slider Prios");
			text6.setBounds(17, 268, 187, 22);
		}
		{
			text7 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text7.setText("Schaue Neue Threads");
			text7.setBounds(18, 101, 187, 22);
		}
		{
			text8 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text8.setText("Lade 38200");
			text8.setBounds(17, 309, 187, 22);
		}
		{
			text9 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text9.setText("Lade Icons");
			text9.setBounds(17, 352, 187, 22);
		}
		{
			text10 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text10.setText("Baue Tagesinfo");
			text10.setBounds(17, 394, 187, 22);
		}
		{
			text11 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP
					| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			text11.setText("Protokoll");
			text11.setBounds(19, 572, 851, 114);
		}
		{
			progressBar1 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar1.setBounds(205, 27, 259, 22);
		}
		{
			progressBar2 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar2.setBounds(205, 63, 259, 22);
		}
		{
			progressBar3 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar3.setBounds(205, 101, 259, 22);
		}
		{
			progressBar4 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar4.setBounds(205, 142, 259, 22);
		}
		{
			progressBar5 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar5.setBounds(205, 182, 259, 22);
		}
		{
			progressBar6 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar6.setBounds(205, 224, 259, 22);
		}
		{
			progressBar7 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar7.setBounds(205, 268, 259, 22);
		}
		{
			progressBar8 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar8.setBounds(205, 309, 259, 22);
		}
		{
			progressBar9 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar9.setBounds(205, 352, 259, 22);
		}
		{
			progressBar10 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar10.setBounds(205, 394, 259, 22);
		}
		{
			composite1 = new Composite(groupListe_glob, SWT.BORDER);
			composite1.setLayout(null);
			composite1.setBounds(742, 29, 309, 373);
			{
				startbutton = new Button(composite1, SWT.PUSH | SWT.CENTER);
				startbutton.setText("Start");
				startbutton.setBounds(25, 19, 60, 30);
				startbutton.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						startbuttonWidgetSelected(evt);
					}
				});
			}
			{
				loopflag = new Button(composite1, SWT.CHECK | SWT.LEFT);
				loopflag.setText("Loop");
				loopflag.setBounds(105, 19, 60, 30);
				loopflag.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						loopflagWidgetSelected(evt);
					}
				});
			}
			{
				letzterStart = new Text(composite1, SWT.BORDER);
				letzterStart.setBounds(165, 22, 138, 27);
			}
			{
				text34enddatum = new Text(composite1, SWT.NONE);
				text34enddatum.setBounds(165, 68, 136, 22);
				KursValueDB kvdb = new KursValueDB("DTE", 1, tdb);
				String maxdate = kvdb.calcMaxdate();
				text34enddatum.setText(maxdate);
				enddatum = new String(maxdate);
			}
			{
				label1 = new Label(composite1, SWT.NONE);
				label1.setText("NeusteKurse vom");
				label1.setBounds(25, 68, 112, 30);
			}
		}

		{
			check1 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check1.setBounds(470, 18, 60, 30);
		}
		{
			check2 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check2.setBounds(470, 54, 60, 30);
		}
		{
			check3 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check3.setBounds(470, 90, 60, 30);
		}
		{
			check4 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check4.setBounds(470, 132, 60, 30);
		}
		{
			check5 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check5.setBounds(470, 174, 60, 30);
		}
		{
			check6 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check6.setBounds(470, 218, 19, 30);
		}
		{
			check7 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check7.setBounds(470, 260, 60, 30);
		}
		{
			check8 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check8.setBounds(470, 302, 60, 30);
		}
		{
			check9 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check9.setBounds(470, 344, 60, 30);
		}
		{
			check10 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check10.setBounds(470, 386, 60, 30);
		}
		{
			check11 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			check11.setBounds(470, 430, 24, 30);
		}
		
		check1.setSelection(true);
		check2.setSelection(true);
		check3.setSelection(true);
		check4.setSelection(true);
		check5.setSelection(true);
		check6.setSelection(true);
		check7.setSelection(true);
		check8.setSelection(true);
		check9.setSelection(true);
		check10.setSelection(true);
		check11.setSelection(true);
		{
			button1 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button1.setSelection(true);
			button1.setBounds(469, 464, 60, 30);
			button1.setText("all");
			button1.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1WidgetSelected(evt);
				}
			});
		}
		{
			textinfo = new Text(groupListe_glob, SWT.BORDER);
			textinfo.setBounds(21, 527, 482, 30);
		}
		{
			button10 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button10.setSelection(true);
			button10.setBounds(470, 90, 60, 30);
		}
		{
			button11 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button11.setSelection(true);
			button11.setBounds(470, 54, 60, 30);
		}
		{
			button12 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button12.setSelection(true);
			button12.setBounds(470, 18, 60, 30);
		}
		{
			progressBar11 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar11.setBounds(205, 352, 259, 22);
		}
		{
			progressBar12 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar12.setBounds(205, 309, 259, 22);
		}
		{
			progressBar13 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar13.setBounds(205, 268, 259, 22);
		}
		{
			progressBar14 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar14.setBounds(205, 224, 259, 22);
		}
		{
			progressBar15 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar15.setBounds(205, 182, 259, 22);
		}
		{
			progressBar16 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar16.setBounds(205, 142, 259, 22);
		}
		{
			progressBar17 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar17.setBounds(205, 101, 259, 22);
		}
		{
			progressBar18 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar18.setBounds(205, 63, 259, 22);
		}
		{
			progressBar19 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar19.setBounds(205, 27, 259, 22);
		}
		{
			text12 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text12.setText("Baue Tagesinfo");
			text12.setBounds(17, 394, 187, 22);
		}
		{
			text13 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text13.setText("Lade Icons");
			text13.setBounds(17, 352, 187, 22);
		}
		{
			text14 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text14.setText("Lade 38200");
			text14.setBounds(17, 309, 187, 22);
		}
		{
			text15 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text15.setText("Schaue Neue Threads");
			text15.setBounds(18, 101, 187, 22);
		}
		{
			text16 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text16.setText("Zeige Slider Prios");
			text16.setBounds(17, 268, 187, 22);
		}
		{
			text17 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text17.setText("Liste Wochengewinne");
			text17.setBounds(17, 224, 187, 22);
		}
		{
			text18 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text18.setText("LadeSlider gen Progn.");
			text18.setBounds(18, 182, 187, 22);
		}
		{
			text19 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text19.setText("calcRanking");
			text19.setBounds(18, 142, 187, 22);
		}
		{
			text20 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text20.setText("LadeUserdaten");
			text20.setBounds(18, 63, 187, 21);

		}
		{
			text21 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text21.setText("Kurse Laden");
			text21.setBounds(19, 27, 186, 21);
		}
		{
			text22 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text22.setText("Kurse Laden");
			text22.setBounds(19, 27, 186, 21);
		}
		{
			text23 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text23.setText("LadeUserdaten");
			text23.setBounds(18, 63, 187, 21);
		}
		{
			progressBar20 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar20.setBounds(205, 27, 259, 22);
		}
		{
			progressBar21 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar21.setBounds(205, 63, 259, 22);
		}
		{
			progressBar22 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar22.setBounds(205, 101, 259, 22);
		}
		{
			button14 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button14.setSelection(true);
			button14.setBounds(470, 54, 16, 30);
		}
		{
			text24 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text24.setText("Kurse Laden");
			text24.setBounds(19, 27, 186, 21);
		}
		{
			text25 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text25.setText("LadeUserdaten");
			text25.setBounds(18, 63, 187, 21);
		}
		{
			text26 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text26.setText("calcRanking");
			text26.setBounds(18, 142, 187, 22);
		}
		{
			text27 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text27.setText("LadeSlider gen Progn.");
			text27.setBounds(18, 182, 187, 22);
		}
		{
			text28 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text28.setText("Liste Wochengewinne");
			text28.setBounds(17, 224, 187, 22);
		}
		{
			text29 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text29.setText("Zeige Slider Prios");
			text29.setBounds(17, 268, 187, 22);
		}
		{
			text30 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text30.setText("Schaue Neue Threads");
			text30.setBounds(18, 101, 187, 22);
		}
		{
			text31 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text31.setText("Lade 38200");
			text31.setBounds(17, 309, 187, 22);
		}
		{
			text32 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text32.setText("Lade Icons");
			text32.setBounds(17, 352, 187, 22);
		}
		{
			text33 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text33.setText("Baue Tagesinfo");
			text33.setBounds(17, 394, 187, 22);
		}
		{
			text34 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
			text34.setText("Durchsuche Börsenblätter");
			text34.setBounds(18, 430, 187, 22);
		}
		{
			progressBar23 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar23.setBounds(205, 27, 259, 22);
		}
		{
			progressBar24 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar24.setBounds(205, 63, 259, 22);
		}
		{
			progressBar25 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar25.setBounds(205, 101, 259, 22);
		}
		{
			progressBar26 = new ProgressBar(groupListe_glob, SWT.SMOOTH);
			progressBar26.setBounds(205, 430, 259, 22);
		}

		{
			// Threshold für das laden der neuen Userdaten
			text34threshold2 = new Text(groupListe_glob, SWT.BORDER);
			text34threshold2.setBounds(533, 54, 32, 21);
			text34threshold2.setText("96");
		}
		{
			label2 = new Label(groupListe_glob, SWT.NONE);
			label2.setText("Threshold/h");
			label2.setBounds(566, 56, 80, 19);
		}
		{
			// wenn dieser selektor gesetzt ist werden für alle user die
			// userdaten geladen/und ausgewertet
			button19alluser = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button19alluser.setText("AllUser");
			button19alluser.setBounds(536, 75, 119, 18);
			button19alluser.setSelection(true);
		}
		{
			label3 = new Label(groupListe_glob, SWT.NONE);
			label3.setText("(inf)");
			label3.setBounds(693, 101, 27, 21);
			label3
					.setToolTipText("Hier wird geschaut ob die User neue Threads in den besuchten Threads haben und diese ggf. in aktdb.db und threads.db aufgenommen.\nIst AllUser aktiviert, werden die Threaddaten von allen Threads aktualisiert und nicht nur nur von den observeruser");
		}
		{
			button16observeUser2 = new Button(groupListe_glob, SWT.CHECK
					| SWT.LEFT);
			button16observeUser2.setText("Nur Observeuser");
			button16observeUser2.setBounds(536, 93, 135, 30);
		}
		button16observeUser2.setSelection(true);
		{
			button16boerflag = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button16boerflag.setText("BoerNeubestimmung");
			button16boerflag.setBounds(536, 27, 170, 20);
		}
		{
			button2showresults = new Button(groupListe_glob, SWT.PUSH | SWT.CENTER);
			button2showresults.setText("ShowResults");
			button2showresults.setBounds(494, 430, 98, 22);
			button2showresults.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button2showresultsWidgetSelected(evt);
				}
			});
		}

	};

	// der startbutton startet den ganzen Analysevorgang
	private void startbuttonWidgetSelected(SelectionEvent evt)
	{
		boolean alluser = button19alluser.getSelection();
		boolean observeuser2 = button16observeUser2.getSelection();
		boolean boerbstimmflag = button16boerflag.getSelection();

		System.out.println("startbutton.widgetSelected, event=" + evt);
		Scheduler sched = new Scheduler();
		sched.start(check1, check2, check3, check4, check5, check6, check7,
				check8, check9, check10, check11, progressBar1, progressBar2,
				progressBar3, progressBar4, progressBar5, progressBar6,
				progressBar7, progressBar8, progressBar9, progressBar10,
				progressBar11, dis_g, textinfo, loopflag, letzterStart,
				enddatum, text34threshold2, alluser, observeuser2,
				boerbstimmflag, tdb_glob, aktdb_glob, udb_glob,keydb_glob);
	}

	private void button1WidgetSelected(SelectionEvent evt)
	{
		// select all, deselect all
		System.out.println("button1.widgetSelected, event=" + evt);

		if (button1.getSelection() == true)
		{
			check1.setSelection(true);
			check2.setSelection(true);
			check3.setSelection(true);
			check4.setSelection(true);
			check5.setSelection(true);
			check6.setSelection(true);
			check7.setSelection(true);
			check8.setSelection(true);
			check9.setSelection(true);
			check10.setSelection(true);
			check11.setSelection(true);

		} else
		{
			check1.setSelection(false);
			check2.setSelection(false);
			check3.setSelection(false);
			check4.setSelection(false);
			check5.setSelection(false);
			check6.setSelection(false);
			check7.setSelection(false);
			check8.setSelection(false);
			check9.setSelection(false);
			check10.setSelection(false);
			check11.setSelection(false);
		}
	}

	private void button2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2.widgetSelected, event=" + evt);
		// TODO add your code for button2.widgetSelected
	}

	private void loopflagWidgetSelected(SelectionEvent evt)
	{
		System.out.println("loopflag.widgetSelected, event=" + evt);
		// TODO add your code for loopflag.widgetSelected
	}

	private void button2showresultsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2showresults.widgetSelected, event=" + evt);
		MailsucherListe ms = new MailsucherListe();
		ms.zeigeErgebnisse(dis_g);
	}
}
