package swtOberfläche;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;
import java.util.HashSet;

import kurse.KurseDB;
import mainPackage.GC;
import objects.AktDbObj;
import objects.ThreadDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.ThreadsDB;
import stores.UeberwachungDB;
import stores.UserDB;
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
public class Tab5ui
{
	//Hier ist ein Musterbeispiel für die Textabfrage drin
	//Hinweis hier wurde Static entfernt !!!!
	static ThreadsDB tdb_glob = null;
	static List threadlist_glob = null;
	static Group groupListe_glob = null;
	static private UeberwachungDB uebdb_glob = null;
	static KurseDB kdb_glob = null;
	static private AktDB aktdb_glob = null;
	static private HashMap<Integer, String> map_glob = new HashMap<Integer, String>();
	static private Label label1;
	static private Button button1zeigepostinglisten;
	static private Label label4;
	
	static private Text text1FILTER1_glob;
	static private Button button1zeigepostinguser;
	static private Button button1zeigekurs;
	static private Button button1rebound;
	static private Label label3;
	static private Label label2;
	static private Button button1setpuscherflag;
	
	static private int seltid_glob = 0;
	static private String selThreadname_glob = null;
	static private int selmid_glob = 0;
	static private UserDB udb_glob=null;

	static private Button button1showtabelle;
	static private Display display_glob;
	static private HashSet<Integer> midmenge_glob = new HashSet<Integer>();

	final static private String[] checkinfo_glob =
	{ "Handdata", "Nur1MID", "B3", "B4" };

	static public void init(TabFolder folder, TabItem tab, Display dis,
			KurseDB kdb, AktDB aktdb, UeberwachungDB uebdb,UserDB udb,ThreadsDB tdb)
	{
		final Tab5selFilter self = new Tab5selFilter();
		display_glob = dis;
		aktdb_glob = aktdb;
		uebdb_glob = uebdb;
		udb_glob=udb;
		kdb_glob = kdb;
		tdb_glob = tdb;

		// UserListGroup erstellen
		groupListe_glob = new Group(folder, SWT.NONE);
		groupListe_glob.setText("Threadinfo");
		groupListe_glob.setLayout(null);
		groupListe_glob.setSize(1310, 800);

		tab.setControl(groupListe_glob);
		// Inhalt des Group erstellen
		Label group = new Label(groupListe_glob, SWT.NONE);
		group.setText("Liste: ");

		// Listener für die checkbuttons
		Listener listener = ActionCheckboxGedrueckt(self);
		Listener listenerPr = ActionCheckboxGedruecktPr(self);

		// check button (Button für bestimmte Filter)
		SetCheckbottons(folder, listener, groupListe_glob, 80, 40, 80, 30,
				text1FILTER1_glob);

		// check button (Button für vertrauensanzeige)
		SetCheckbottonsPr(folder, listenerPr, groupListe_glob, 680, 620, 170,
				30);

		// Threadname
		final Text M1 = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI);
		M1.setBounds(230, 80, 400, 50);
		M1.setText("Threadname");

		// Ausgabefenster (Editorfeld etc..)
		final Text Ausgabe = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		Ausgabe.setBounds(230, 150, 600, 400);

		// Protofenster
		Text Proto = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		Proto.setBounds(20, 700, 600, 100);
		Proto.setText("Rootpath=<" + GC.rootpath + ">");

		
		final Text THREADANZAHL = new Text(groupListe_glob, SWT.BORDER
				| SWT.MULTI);
		THREADANZAHL.setBounds(22, 560, 100, 20);
		THREADANZAHL.setText("??");

		// userliste
		BuildThreadliste(groupListe_glob, 20, 20, 200, 530, self, THREADANZAHL);
		initListenerThreadliste(M1, Ausgabe);

		// Selektor
		self.setPrefix("");

		// Refresh Button gedrückt
		Button refreshbutton = new Button(groupListe_glob, SWT.PUSH);
		refreshbutton.setText("Refresh");
		refreshbutton.setBounds(234, 662, 50, 20);
		refreshbutton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				if (threadlist_glob != null)
					cleanThreadliste();

				ActionRefresh(groupListe_glob, 20, 20, 200, 530, self,
						THREADANZAHL, text1FILTER1_glob);
				initListenerThreadliste(M1, Ausgabe);
				System.out.println("Refresh Butten gedrückt");
			}
		});

		// ok button
		Button ok = new Button(groupListe_glob, SWT.PUSH);
		ok.setText("Save");
		ok.setBounds(580, 560, 50, 20);
		ok.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				System.out.println("Safe Button gedrückt OK");
				ActionSaveButtonGedrueckt(Ausgabe);
			}
		});

		// show vertrauen-Button
		Button vertraubutton = new Button(groupListe_glob, SWT.PUSH);
		vertraubutton.setText("ShowVertrauen");
		vertraubutton.setBounds(630, 654, 100, 20);

		// texteingabe für namen
		{
			text1FILTER1_glob = new Text(groupListe_glob, SWT.BORDER);
			text1FILTER1_glob.setBounds(26, 660, 194, 20);
		}
		// label
		{
			label3 = new Label(groupListe_glob, SWT.NONE);
			label3.setText("Threadname oder WKN(AKTDB) suche");
			label3.setBounds(24, 628, 260, 30);
		}
		vertraubutton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				System.out.println("Vertrauen Button gedrückt OK");
				ActionVertrauenButtonGedrueckt(Ausgabe, self);
			}
		});

	}

	static private void cleanThreadliste()
	{
		threadlist_glob.removeAll();
		// altes map zeug löschen
		map_glob.clear();
	}

	static private Boolean CheckAttribute(Tab5selFilter self,Handdata hand,String tnam,int mid)
	{
		//true: falls die attribute dieses objekt zulassen
		//false: falls durch die attribute dies objekt nicht erlaubt ist

		String attrib = self.getAttrib();
		// hier wird geprüft ob nur user mit vorhandenen handdata
		// angezeigt werden
		if ((attrib != null) && (attrib.contains("Handdata") == true))
		{
			String midstr = String.valueOf(mid);
			if (hand.HasHanddata(midstr) == false)
				return false;
		}
		if ((attrib != null) && (attrib.contains("Nur1MID")))
		{
		
			if (midmenge_glob.contains(mid))
				return false;
			else
				midmenge_glob.add(mid);
		}
		if ((attrib != null) && (attrib.contains("OBSERVE") == true))
		{
			if (hand.HasHanddata(tnam) == false)
				return false;
		}
		return true;
	}
	
	static  private void BuildThreadliste(Group groupListe, int x, int y,
			int breite, int hoehe, Tab5selFilter self, final Text THREADANZAHL)
	{
		Handdata hand = new Handdata("midinfo");
		midmenge_glob = new HashSet<Integer>();

		if (threadlist_glob == null)
			threadlist_glob = new List(groupListe, SWT.BORDER | SWT.MULTI
					| SWT.V_SCROLL);

		// hole den prefix
		String prefix = self.getPrefix().toLowerCase();
		int fanz = 0;

		// tidliste
		int anz = tdb_glob.GetanzObj();
		threadlist_glob.setBounds(20, 20, 200, 530);
		Tracer.WriteTrace(20, "Info: pref<" + prefix + "> attrib<"
				+ self.getPrefix() + ">");

		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObjectIDX(i);
			String tnam = tdbo.getThreadname().toLowerCase();
			if (tnam.contains(prefix))
			{
				int mid = tdbo.getMasterid();
				
				// hier wird geprüft ob nur user mit vorhandenen handdata
				// angezeigt werden
				if( CheckAttribute(self,hand,tnam, mid)==false)
					continue;
			
				String tname = null;
				if (tdbo.getPuscherflag() == 1)
					tname = "PUSH<" + tdbo.getThreadname() + "> ";
				else
					tname = i + "<" + tdbo.getThreadname() + "> ";
				threadlist_glob.add(tname);
				map_glob.put(fanz, tdbo.getThreadname());
				fanz++;
				
				if (midmenge_glob.contains(mid) == false)
					midmenge_glob.add(mid);
			}
		}
		 anz = aktdb_glob.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			AktDbObj aktobj = (AktDbObj)  aktdb_glob.GetObjectIDX(i);
			String tnam = aktobj.getWkn().toLowerCase();
			if (tnam.contains(prefix))
			{
				int mid = aktobj.getMasterid();
				
				// hier wird geprüft ob nur user mit vorhandenen handdata
				// angezeigt werden
				if( CheckAttribute(self,hand,tnam, mid)==false)
					continue;
			
				String tname = null;
				tname = "AKTDB<" + aktobj.getAktname() + "> ";
				
				threadlist_glob.add(tname);
				map_glob.put(fanz, aktobj.getAktname());
				fanz++;
				
				if (midmenge_glob.contains(mid) == false)
					midmenge_glob.add(mid);
			}
		}
		
		
		
		
		THREADANZAHL.setText(String.valueOf(fanz));
	}

	static  Button SetCheckbottons(TabFolder folder, Listener listener,
			Group groupListe, int x, int y, int breite, int hoehe,
			final Text FILTER1)
	{
		Button button = null;
		// check button (Button für bestimmte Filter)
		// groupListe.setLayout(new RowLayout(SWT.HORIZONTAL));
		for (int i = 0; i < 4; i++)
		{
			button = new Button(groupListe, SWT.CHECK);
			button.setText(checkinfo_glob[i]);
			button.setBounds(250 + (i * x), y, breite, hoehe);
			{
				button1showtabelle = new Button(groupListe_glob, SWT.PUSH
						| SWT.CENTER);
				button1showtabelle.setText("Tabelle");
				button1showtabelle.setBounds(290, 654, 143, 26);
				button1showtabelle.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1showtabelleWidgetSelected(evt);
					}
				});
			}
			{
				button1setpuscherflag = new Button(groupListe_glob, SWT.PUSH
						| SWT.CENTER);
				button1setpuscherflag.setText("SetPuscherflag");
				button1setpuscherflag.setBounds(844, 152, 116, 30);
				button1setpuscherflag
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1setpuscherflagWidgetSelected(evt);
							}
						});
			}
			{
				label1 = new Label(groupListe_glob, SWT.NONE);
				label1.setText("(i)");
				label1.setBounds(232, 22, 18, 23);
				label1
						.setToolTipText("Aktien die gepusched werden werden in rot dargestellt");
			}
			{
				label2 = new Label(groupListe_glob, SWT.NONE);
				label2.setText("(i)");
				label2.setBounds(439, 662, 20, 18);
				label2
						.setToolTipText("Hier wird in der ThreadsDB nach dem Namen gesucht und in der Tabelle die Namensübereinstimmungen aufgenommen");
			}
			{
				button1rebound = new Button(groupListe_glob, SWT.PUSH
						| SWT.CENTER);
				button1rebound.setText("In Ueberwachung Aufnehmen");
				button1rebound.setBounds(844, 188, 257, 30);
				button1rebound.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1reboundWidgetSelected(evt);
					}
				});
			}
			{
				button1zeigekurs = new Button(groupListe_glob, SWT.PUSH
						| SWT.CENTER);
				button1zeigekurs.setText("ZeigeKurswerte");
				button1zeigekurs.setBounds(842, 292, 118, 30);
				button1zeigekurs.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1zeigekursWidgetSelected(evt);
					}
				});
			}
			{
				button1zeigepostinglisten = new Button(groupListe_glob,
						SWT.PUSH | SWT.CENTER);
				button1zeigepostinglisten.setText("ZeigePostinglisten");
				button1zeigepostinglisten.setBounds(844, 418, 116, 30);
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
				final Text text1maxdays;
				text1maxdays = new Text(groupListe_glob,  SWT.BORDER | SWT.MULTI);
				text1maxdays.setBounds(978, 471, 50, 20);
				text1maxdays.setText("50");
				
			
				button1zeigepostinguser = new Button(groupListe_glob, SWT.PUSH | SWT.CENTER);
				button1zeigepostinguser.setText("ZeigePostinguser");
				button1zeigepostinguser.setBounds(844, 460, 116, 30);
				button1zeigepostinguser.addSelectionListener(new SelectionAdapter() 
				{
					public void widgetSelected(SelectionEvent evt) 
					{
						button1zeigepostinguserWidgetSelected(evt,text1maxdays);
					}
				});
			}
			
			{
				label4 = new Label(groupListe_glob, SWT.NONE);
				label4.setText("MaxTage");
				label4.setBounds(1036, 473, 60, 34);
			}
			button.addListener(SWT.Selection, listener);
		}
		return button;
	}

	static Button SetCheckbottonsPr(TabFolder folder, Listener listener,
			Group groupListe, int x, int y, int breite, int hoehe)
	{
		Button button = null;
		// check button (Button für die Übergabeparameter von Vertrauen)

		for (int i = 0; i < 1; i++)
		{
			button = new Button(groupListe, SWT.CHECK);
			button.setText("Alle Threads Sammeln");
			button.setBounds(630, 628, 178, 20);
			button.addListener(SWT.Selection, listener);
		}
		return button;
	}

	static  private void ActionRefresh(Group groupListe, int x, int y,
			int breite, int hoehe, Tab5selFilter self, final Text THREADANZAHL,
			Text FILTER1)
	{
		// Die Filtertaste wurde betätigt
		// hole, den prefix und erstelle die Threadliste neu

		String selt = FILTER1.getText();
		self.setPrefix(selt);

		System.out.println("text=" + self.getPrefix());
		Handdata hand = new Handdata("midinfo");
		BuildThreadliste(groupListe, x, y, breite, hoehe, self, THREADANZAHL);
	}

	static  private void ActionThreadelementAusgewaehlt(String msgThreadname,
			final Text t, final Text M1)
	{
		// Baue Userinfo auf
		// Ein thread wurde ausgewählt
		Handdata hand = new Handdata("midinfo");
		System.out.println("pos=" + msgThreadname);

		// die position holen
		String msgpos = msgThreadname.substring(msgThreadname.indexOf("{") + 1,
				msgThreadname.indexOf("}") - 1);
		int pos = Tools.get_zahl(msgpos);

		String name = map_glob.get(pos);

		// ermittle die mid zu den threadnamen
		ThreadDbObj tdbo = tdb_glob.sucheThread(name);
		selmid_glob = tdbo.getMasterid();
		seltid_glob = tdbo.getThreadid();
		// Im Infofenster weitere Infos zur Auswahl geben
		if(selmid_glob==0)
		{
			M1.setText("Thread mit name<"+name+"> nicht in ThreadsDB");
			return;
		}
		M1.setText("name<" + tdbo.getThreadname() + "> tid<"
				+ tdbo.getThreadid() + "> mid<" + tdbo.getMasterid()
				+ ">\n eröff<" + tdbo.getEroeffnet() + "> + pageanz<"
				+ tdbo.getPageanz() + "> + prognanz<"
				+ tdbo.getPrognoseanzahl() + ">");
		String midstr = String.valueOf(tdbo.getMasterid());
		/*
		 * String fnam = GC.rootpath + "\\handdata\\Rechnername " + hostname +
		 * "\\midinfo\\" + selmid_glob + ".txt";
		 */

		selThreadname_glob = tdbo.getThreadname();
		hand.anzeigeHanddata(t, midstr);
	}

	static  private Listener ActionCheckboxGedrueckt(
			final Tab5selFilter selfilter)
	{
		// hier wird die obere Checkbox ausgewertet
		Listener listener = new Listener()
		{
			public void handleEvent(Event e)
			{
				System.out.println("event");
				Control[] children = groupListe_glob.getChildren();
				for (int i = 0; i < children.length; i++)
				{
					Control child = children[i];
					if ((e.widget == child) && (child instanceof Button))
					{
						Boolean sel = ((Button) child).getSelection();
						System.out.println("i=" + i + " selektion=" + sel);
						if (sel == true)
							selfilter.appendAttrib(checkinfo_glob[i - 1]);
						if (sel == false)
							selfilter.removeAttrib(checkinfo_glob[i - 1]);
					}
				}
			}
		};
		return listener;
	}

	static  private Listener ActionCheckboxGedruecktPr(
			final Tab5selFilter selfilter)
	{
		// hier wird die obere Checkbox ausgewertet
		Listener listener = new Listener()
		{
			public void handleEvent(Event e)
			{
				System.out.println("event");
				Control[] children = groupListe_glob.getChildren();
				for (int i = 0; i < children.length; i++)
				{
					Control child = children[i];
					if ((e.widget == child) && (child instanceof Button))
					{
						Boolean sel = ((Button) child).getSelection();
						System.out.println("i=" + i + " selektion=" + sel);
						if (sel == true)
							selfilter.setAlleThreadsFlag_glob(1);
						if (sel == false)
							selfilter.setAlleThreadsFlag_glob(0);
					}
				}
			}
		};
		return listener;
	}

	static  private void ActionSaveButtonGedrueckt(final Text t)
	{
		// Save Button gedrückt
		Handdata hand = new Handdata("midinfo");
		hand.speichereHanddata(t, String.valueOf(selmid_glob));
	}

	static  private void ActionVertrauenButtonGedrueckt(final Text t,
			final Tab5selFilter self)
	{
		// suche den thread
		ThreadDbObj tdbo = tdb_glob.sucheThread(selThreadname_glob);

		// baue die graphik auf (mit vertrauenswerten)
		SwingVertrau1 svert = new SwingVertrau1("titel", tdb_glob, tdbo, 1,
				self.getAlleThreadsFlag_glob());
	}

	static   void initListenerThreadliste(final Text M1,
			final Text Ausgabe)
	{
		threadlist_glob.addListener(SWT.Selection, new Listener()
		{
			// listenelement gewählt
			public void handleEvent(Event e)
			{
				String string = "";
				int[] selection = threadlist_glob.getSelectionIndices();

				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";

				String msg = "Selection={" + string + "}";
				System.out.println(msg);
				M1.setText(msg);
				ActionThreadelementAusgewaehlt(msg, Ausgabe, M1);
			}
		});
	}

	static  private void button1showtabelleWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1showtabelle.widgetSelected, event=" + evt);
		Viewer tabv = new Viewer();
		// tabv.viewTableThreadsMid(display_glob,
		// GC.rootpath+"\\db\\threads.db",midmenge_glob);
		Tab5showTidtable tt = new Tab5showTidtable();
		tt.ShowTidTable(display_glob, GC.rootpath + "\\db\\threads.db",
				midmenge_glob);
	}

	 static  private void button1setpuscherflagWidgetSelected(SelectionEvent evt)
	{
		System.out
				.println("button1setpuscherflag.widgetSelected, event=" + evt);
		// hier wird das Puscherflag für eine MID gesetzt

		tdb_glob.setPusherflag(selmid_glob);
		tdb_glob.WriteDB();
	}

	 static  private void button1reboundWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1rebound.widgetSelected, event=" + evt);
		// In Überwachung aufnehmen

		int tid = seltid_glob;

		AktDbObj aobj = (AktDbObj) aktdb_glob.GetObject(tid);
		int mid = aobj.getMasterid();

		Tab12ExtEdit t12 = new Tab12ExtEdit(uebdb_glob, display_glob, "1",
				String.valueOf(mid), aobj.getAktname(), Tools
						.entferneZeit(Tools.get_aktdatetime_str()), "lastload",
				"filepath", "minval", "maxval", aobj.getSymbol(), "wkn",
				"isin", "Puschertext", "new", 0);
	}

	 static  private void button1zeigekursWidgetSelected(SelectionEvent evt)
	{
		// Zeige Kurse
		System.out.println("button1zeigekurs.widgetSelected, event=" + evt);

		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObject(seltid_glob);
		String symb = tdbo.getSymbol();

		kdb_glob.zeigeSwtTabelle(display_glob, symb, "");
	}
	
	 static  private void button1zeigepostinglistenWidgetSelected(SelectionEvent evt) 
	{
		//zeige Postinglisten
		//Eine postingliste gibt exakt an welche user mit welchen Postingnummern gepostet haben
		System.out.println("button1zeigepostinglisten.widgetSelected, event="+evt);
		tdb_glob.ZeigePostinglisten(selmid_glob);
	}
	
	 static  void button1zeigepostinguserWidgetSelected(SelectionEvent evt,Text text1maxdays) 
	{
		
		String maxd=text1maxdays.getText();
		int max=Integer.valueOf(maxd);
		//Zeige Postinguser
		//Hier werden exakte Informationen über die postenden User gegeben.
		//Welcher User hat wieviel gepostet und welchen Rang hat der User
		System.out.println("button1zeigepostinguser.widgetSelected, event="+evt);
		tdb_glob.zeigePostinguser(selmid_glob,udb_glob,max);
	}
	
	
}
