package swtOberfläche;

import kurse.KurseDB;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import stores.AktDB;
import stores.BoersenBlaetterDB;
import stores.KeyDB;
import stores.SlideruebersichtDB;
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
public class SwtGui
{
	private Display display;
	private Shell shell;
	private Composite composite1;
	private Group group1;

	public void initialize()
	{
		this.display = new Display();
		this.shell = new Shell(this.display);
		this.shell.setText("Hauptfenster");
		this.createGUI(this.shell);
		this.shell.pack();
		this.shell.open();
		while (!this.shell.isDisposed())
		{
			if (!this.display.readAndDispatch())
				this.display.sleep();
		}
		this.display.dispose();
	}

	private void createGUI(Composite parent)
	{
		/*Hcheck h= new Hcheck();
		h.check();*/
		
		UserDB udb_glob = new UserDB("observeuser.txt", "boostraenge.txt");
		ThreadsDB tdb_glob = new ThreadsDB();
		AktDB akt_glob = new AktDB();
		KurseDB kurse_glob = new KurseDB(tdb_glob);
		UeberwachungDB ueb_glob = new UeberwachungDB();
		KeyDB kdb_glob = new KeyDB();
		SlideruebersichtDB sldb_glob = new SlideruebersichtDB();
		BoersenBlaetterDB boerdb_glob= new BoersenBlaetterDB();
		
		
		// Layout festlegen
		parent.setLayout(new FillLayout());
		ScrolledComposite sc = new ScrolledComposite(parent,SWT.H_SCROLL|SWT.V_SCROLL);
		sc.setMinSize(1600,1600);
		sc.setExpandHorizontal(true);	
    	sc.setExpandVertical(true);

    	group1 = new Group(sc,SWT.NONE);
    	group1.setBounds(0,0,600,400);
    	
    	GridLayout group1Layout = new GridLayout();
    	group1Layout.makeColumnsEqualWidth = true;
    	group1.setLayout(group1Layout);
		sc.setContent(group1);

		GridData tabFolderLData = new GridData();
		tabFolderLData.widthHint = 1600;
		tabFolderLData.heightHint = 800;
		TabFolder tabFolder = new TabFolder(group1, SWT.H_SCROLL|SWT.V_SCROLL);
		tabFolder.setLayoutData(tabFolderLData);

		// EinstellungenTab erstellen
		TabItem tab1 = new TabItem(tabFolder, SWT.NONE);
		tab1.setText("Kursmodul");
		tab1
				.setToolTipText("Das Kursmodul dient zur Anzeige der gespeicherten Kurse");
		Tab1ui.init(tabFolder, tab1, display,tdb_glob,kurse_glob,akt_glob);

		TabItem tab2 = new TabItem(tabFolder, SWT.NONE);
		tab2.setText("Tabellen-Uebersicht");
		tab2
				.setToolTipText("Dies Modul stellt die *.db-Files als externe Tabelle\nübersichtlich da\n");
		Tab2ui.init(tabFolder, tab2, display);

		TabItem tab3 = new TabItem(tabFolder, SWT.NONE);
		tab3.setText("Maintask");
		Tab3ui tab3ui = new Tab3ui();
		tab3
				.setToolTipText("Dies ist der Haupttask der\na)Daten laden\nb)Prognosen erstellen\nc)user beobachten\n");
		tab3ui.init(tabFolder, tab3, display,tdb_glob,akt_glob,udb_glob,kdb_glob);

		TabItem tab4 = new TabItem(tabFolder, SWT.NONE);
		tab4.setText("Userinfo");
		tab4
				.setToolTipText("Hier werden für einen User spezifische Infos ausgegeben");
		Tab4ui.init(tabFolder, tab4,display,udb_glob,tdb_glob);

		TabItem tab5 = new TabItem(tabFolder, SWT.NONE);
		tab5.setText("MidInfo");
		tab5
				.setToolTipText("Hier werden sämtliche Infos zu einer MID ausgegeben\nAuch Infos zur tid werden gesammelt dargestellt\n");
		Tab5ui.init(tabFolder, tab5,display,kurse_glob,akt_glob,ueb_glob,udb_glob,tdb_glob);

		TabItem tab6Prognosen = new TabItem(tabFolder, SWT.NONE);
		tab6Prognosen.setText("Prognosenbrowser");
		tab6Prognosen
				.setToolTipText("Hier werden Prognosen/Wochegewinne etc. in einem HTML-Browser dargestellt");
		Tab6ui.init(tabFolder, tab6Prognosen);

		TabItem tab7uiWork = new TabItem(tabFolder, SWT.NONE);
		tab7uiWork.setText("Work");
		tab7uiWork
				.setToolTipText("Arbeitsfenster:\nhier werden bestimmte immer wieder kehrende Aufgaben zur verfügung gestellt");
		Tab7uiWork.init(tabFolder, tab7uiWork, display);

		TabItem tab8uiRanking = new TabItem(tabFolder, SWT.NONE);
		tab8uiRanking.setText("calcRanking");
		tab8uiRanking
				.setToolTipText("Dies ist das Ranking modul. Mit diesem Modul kann man ein Ranking erstellen und exportieren\nZwar wird diese Rankinginfo auch im Maintask erstellt\ndies Modul erlaubt aber erweiterte Einstellungen und export/import\n");
		Tab8uiRanking.init(tabFolder, tab8uiRanking, display,udb_glob,tdb_glob,akt_glob);

		TabItem tab9uiProgmodul = new TabItem(tabFolder, SWT.NONE);
		tab9uiProgmodul.setText("Progmodul");
		tab9uiProgmodul.setToolTipText("Das Prognosemodul dient zur detalierten Auswertung von 'prognosen.db'" );
		Tab9uiProgmodul.init(tabFolder, tab9uiProgmodul, display);

		TabItem tab10uiBrowser = new TabItem(tabFolder, SWT.NONE);
		Tab10uiBrowser t10 = new Tab10uiBrowser();
		tab10uiBrowser.setText("Browser");
		tab10uiBrowser.setToolTipText("Das Browsermodul dient zum Lesen der Threads" );
		t10.init(tabFolder, tab10uiBrowser, display,udb_glob,tdb_glob,akt_glob,ueb_glob,kurse_glob);

		TabItem tab11 = new TabItem(tabFolder, SWT.NONE);
		tab11.setText("Baue Attribute");
		tab11
				.setToolTipText("Dies Modul sammelt die Gewinne der Masteruser\n");
		Tab11ui.init(tabFolder, tab11, display,tdb_glob,kurse_glob,udb_glob);
		
		TabItem tab12 = new TabItem(tabFolder, SWT.NONE);
		tab12.setText("Observer");
		tab12.setToolTipText("Dies Modul dient zum Beobachten und Auswerten von Triggern");
		Tab12uiObserverMain t12 = new Tab12uiObserverMain();
		t12.init(tabFolder, tab12, display,udb_glob,tdb_glob,akt_glob,ueb_glob,kurse_glob);

		TabItem tab13 = new TabItem(tabFolder, SWT.NONE);
		tab13.setText("Börsenblätter");
		tab13.setToolTipText("Dies Modul dient zum Auswerten von BB");
		Tab13uiBB t13 = new Tab13uiBB();
		t13.init(tabFolder, tab13, display,udb_glob,tdb_glob,akt_glob,ueb_glob,kurse_glob,boerdb_glob);
		
		TabItem tab14 = new TabItem(tabFolder, SWT.NONE);
		tab14.setText("Scanner");
		tab14.setToolTipText("Dies Modul dient zum Beobachten und Auswerten von Triggern");

		Tab14ScannerM t14 = new Tab14ScannerM();
		t14.init(tabFolder, tab14, display,udb_glob,tdb_glob,akt_glob,ueb_glob,kurse_glob,sldb_glob);

		TabItem tab15 = new TabItem(tabFolder, SWT.NONE);
		tab15.setText("Champion Analyser");
		tab15.setToolTipText("Dies Modul dient zum Auswerten von Börse.de");

		Tab15uiChampion t15 = new Tab15uiChampion();
		t15.init(tabFolder, tab15, display,udb_glob,tdb_glob,akt_glob,ueb_glob,kurse_glob,sldb_glob);
		
		tabFolder.pack();
		{
			composite1 = new Composite(tabFolder, SWT.H_SCROLL|SWT.V_SCROLL);
			GridLayout composite1Layout = new GridLayout();
			composite1Layout.makeColumnsEqualWidth = true;
			composite1.setLayout(composite1Layout);
			composite1.setBounds(5, 5, 1024, 768);
		}
	}

	public static void main(String args[])
	{
		SwtGui gui = new SwtGui();
		gui.initialize();
	}
}
