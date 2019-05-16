package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import html.HtmlMeistDiskutiert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;

import kurse.KurseDB;
import mailer.BStat;
import mailer.Found;
import mailer.Mailcoder;
import mailer.MailsucherListe;
import mailer.MailsucherSchluesselwortFile_dep;
import mailer.SucheNeuempfehlung;
import mailer.Suchliste;
import mainPackage.GC;
import objects.AktDbObj;
import objects.BoersenblattDbObj;
import objects.ChampionDbObj;
import objects.KeyDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import stores.BoersenBlaetterDB;
import stores.ChampionDB;
import stores.KeyDB;
import stores.MidDB;
import stores.ThreadsDB;
import stores.UeberwachungDB;
import stores.UserDB;
import swtViewer.ViewBoersenblaetter;
import swtViewer.ViewExternFileTable;
import swtViewer.Viewer;

import comperatoren.KeyComperator;

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
public class Tab13uiBB
{
	private Display display_glob = null;
	private AktDB aktdb_glob = null;
	private KeyDB kdb_glob = new KeyDB();
	private ThreadsDB tdb_glob = null;
	private MidDB middb_glob = null;
	private BoersenBlaetterDB boerdb_glob = null;
	private int keineTeilwoerterflag_glob = 1;
	private int auslisteVerwenden_glob = 1;
	private Table table1;
	private Text text1suchtext;
	private Label label1letzteDatenbasis;
	private Group group5;
	private Label label3;
	private Label label2;
	private Label label1;
	private Text text1neuetexte;
	private Text text1neuepdfs;
	private Group group7;
	private Button button1SucheAbgleich;
	private Group group4;
	private Button button1configsuchliste;
	private Button button1zeigesuchliste;
	private Button button1suchliste;
	private Group group1, group2, group3;
	private Button button1showpdf;
	
	private Button AutomaticTask;
	private Button Auto3;
	private Button Auto2;
	private Button Auto1;
	private Button GenWallstreetMeistDiskutierte;
	private Button ZeigeNeuempf;
	private Button EditSuchfile;
	private Label i;
	private Button EditAusliste;
	private Button AListeVerwenden;
	private Button buttonKeineTeilwoerter;
	private Button Zeige;
	private Button Suche;
	private Button SelectFile;
	private Text suchfile;
	private Group group6;
	private Label label10;
	private Button button1zeigealles;
	private Button button1zeigekeyliste;
	private Label label9;
	private Text text4neukw;
	private Label label8;
	private Text text4anz;
	private Label label7;
	private Button button1erweitereKeywortliste;
	private Label label6;
	private Text text3;
	private Label label5;
	private Text text2;
	private Label label4;
	private Text text1;
	private Button button1datenholen;
	private ProgressBar progressBar1_glob;
	private Button button1suche;
	private Button button1startdatum;
	private Text text1Mindat;
	private String suchfile_glob = null;

	// hmap: tabellenzeile <-> filename
	private HashMap<Integer, String> hmap = new HashMap<Integer, String>();

	public void init(TabFolder folder, TabItem tab, Display dis, UserDB udb,
			ThreadsDB tdb, AktDB aktdb, UeberwachungDB ueb, KurseDB kdb,
			BoersenBlaetterDB boerdb)
	{
		// udb_glob = udb;

		display_glob = dis;
		aktdb_glob = aktdb;
		tdb_glob = tdb;
		boerdb_glob = boerdb;

		group1 = new Group(folder, SWT.NONE);
		group1.setText("Thread Auswertung");
		group1.setLayout(null);
		{
			table1 = new Table(group1, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
					| SWT.FULL_SELECTION);
			table1.setBounds(42, 48, 697, 515);
		}
		table1.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				ActionTable1(evt);
			}
		});
		{
			group2 = new Group(group1, SWT.NONE);
			group2.setLayout(null);
			group2.setText("Einzelsuche");
			group2.setBounds(771, 96, 371, 97);
			{
				button1suche = new Button(group2, SWT.PUSH | SWT.CENTER);
				button1suche.setText("Einzelsuche");
				button1suche.setBounds(7, 58, 86, 30);
				button1suche.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1sucheWidgetSelected(evt);
					}
				});
			}
			{
				text1suchtext = new Text(group2, SWT.NONE);
				text1suchtext.setBounds(7, 28, 219, 24);

			}
		}
		{
			progressBar1_glob = new ProgressBar(group1, SWT.NONE);
			progressBar1_glob.setBounds(44, 571, 697, 19);
		}
		{
			button1showpdf = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1showpdf.setText("Bitte Auswählen");
			button1showpdf.setBounds(771, 676, 350, 30);
			button1showpdf.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1showpdfWidgetSelected(evt);
				}
			});
		}
		{
			text1Mindat = new Text(group1, SWT.BORDER);
			text1Mindat.setBounds(771, 50, 95, 23);
		}

		String dat = Tools.entferneZeit(Tools.get_aktdatetime_str());
		String dat2 = Tools.entferneZeit(Tools.modifziereDatum(dat, 0, 0, -10,
				0));
		text1Mindat.setText(dat2);
		{
			button1startdatum = new Button(group1, SWT.CHECK | SWT.LEFT);
			button1startdatum.setText("Startdatum");
			button1startdatum.setBounds(880, 52, 102, 23);
			button1startdatum.setSelection(true);
		}
		{
			group3 = new Group(group1, SWT.SHADOW_ETCHED_IN);
			group3.setLayout(null);
			group3.setText("Suchliste");
			group3.setBounds(771, 210, 543, 130);
			{
				button1suchliste = new Button(group3, SWT.PUSH | SWT.CENTER);
				button1suchliste.setText("Erstelle Suchliste");
				button1suchliste.setBounds(12, 91, 111, 27);
				button1suchliste.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1suchlisteWidgetSelected(evt);
					}
				});
			}
			{
				button1zeigesuchliste = new Button(group3, SWT.PUSH
						| SWT.CENTER);
				button1zeigesuchliste.setText("Zeige Ergebnisse");
				button1zeigesuchliste.setBounds(144, 91, 119, 27);
				button1zeigesuchliste
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1zeigesuchlisteWidgetSelected(evt);
							}
						});
			}
			{
				button1configsuchliste = new Button(group3, SWT.PUSH
						| SWT.CENTER);
				button1configsuchliste.setText("Edit Config");
				button1configsuchliste.setBounds(12, 28, 111, 27);
				button1configsuchliste
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1configsuchlisteWidgetSelected(evt);
							}
						});
			}
			{
				Auto2 = new Button(group3, SWT.CHECK | SWT.LEFT);
				Auto2.setText("SlAuto2");
				Auto2.setBounds(444, 91, 77, 30);
				
			}
		}
		{
			group4 = new Group(group1, SWT.NONE);
			group4.setLayout(null);
			group4.setText("Suche Neuempfehlungen");
			group4.setBounds(771, 352, 544, 134);
			{
				button1SucheAbgleich = new Button(group4, SWT.PUSH | SWT.CENTER);
				button1SucheAbgleich.setText("Suche Neuempfehlung");
				button1SucheAbgleich.setBounds(8, 22, 161, 27);
				button1SucheAbgleich
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1SucheNeuempfehlung(evt);
							}
						});
			}
			{
				button1erweitereKeywortliste = new Button(group4, SWT.PUSH
						| SWT.CENTER);
				button1erweitereKeywortliste.setText("Erweitere Keywortliste");
				button1erweitereKeywortliste.setBounds(187, 22, 145, 27);
				button1erweitereKeywortliste
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1erweitereKeywortlisteWidgetSelected(evt);
							}
						});
			}
			{
				label7 = new Label(group4, SWT.NONE);
				label7.setText("(i)");
				label7.setBounds(338, 22, 20, 31);
				label7.setToolTipText("Erweitere die Keywortliste aus der Aktdb.\r\nDie Schlüsselwörter der Aktdb werden im Mailtask erweitert.\r\n");
			}
			{
				text4anz = new Text(group4, SWT.NONE);
				text4anz.setBounds(187, 55, 67, 19);
			}
			{
				label8 = new Label(group4, SWT.NONE);
				label8.setText("#Keywörter");
				label8.setBounds(260, 55, 80, 30);
			}
			{
				text4neukw = new Text(group4, SWT.NONE);
				text4neukw.setBounds(188, 80, 66, 18);
			}
			{
				label9 = new Label(group4, SWT.NONE);
				label9.setText("#neu");
				label9.setBounds(260, 80, 60, 20);
			}
			{
				button1zeigekeyliste = new Button(group4, SWT.PUSH | SWT.CENTER);
				button1zeigekeyliste.setText("Zeige KeyDB");
				button1zeigekeyliste.setBounds(188, 104, 144, 23);
				button1zeigekeyliste
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1zeigekeylisteWidgetSelected(evt);
							}
						});
			}
			{
				button1zeigealles = new Button(group4, SWT.PUSH | SWT.CENTER);
				button1zeigealles.setText("Zeige #Keys/BB");
				button1zeigealles.setBounds(8, 104, 161, 24);
				button1zeigealles.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1zeigeallesWidgetSelected(evt);
					}
				});
			}
			{
				label10 = new Label(group4, SWT.NONE);
				label10.setText("(i)");
				label10.setBounds(169, 26, 12, 17);
				label10.setToolTipText("\r\na)Durchsuche alle BB-Blätter nach den Keywörtern und baue die Boersenblatt.db weiter auf.\r\nFalls das Keyword von einem Börsenblatt gepusched wurde wird dies auch in tdb vermerkt. Es wird gespeichert wann der letzte Pusch stattgefunden hat.\r\nb)Es wird auch nach Neuempfehlungen gesucht\r\nb1) Neuempfehlung über alles \r\nfalls diese Empfehlung das erste mal über alle Börsenblätter gefunden wurde (20 Tage Zeitraum)\r\nb2)Neuempfehlung für ein BB\r\nfalls diese Empfehlung das erste mal beim Börsenblatt vorkommt (20 Tage Zeitraum) ");
			}
			{
				ZeigeNeuempf = new Button(group4, SWT.PUSH | SWT.CENTER);
				ZeigeNeuempf.setText("ZeigeNeuempfehlungen");
				ZeigeNeuempf.setBounds(338, 104, 172, 23);
				ZeigeNeuempf.setToolTipText("Werden neue WKNs in den Börsenblättern gefunden spricht man hier von einer Neuempfehlung.\r\nEine WKN wird als neu angesehen wenn diese in dem entsprechenden Börsenblatt in den letzten 3 Monaten nicht aufgetaucht ist.");
				ZeigeNeuempf
				.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1zeigeNeuempfehlung(evt);
					}
				});
			}
			{
				Auto3 = new Button(group4, SWT.CHECK | SWT.LEFT);
				Auto3.setText("NeuAuto3");
				Auto3.setBounds(427, 12, 93, 30);
				
			}
			
		}
		{
			group7 = new Group(group1, SWT.NONE);
			group7.setLayout(null);
			group7.setText("Puscherblattinfo Datenaquise");
			group7.setBounds(44, 602, 369, 147);
			{
				text1neuepdfs = new Text(group7, SWT.NONE);
				text1neuepdfs.setText("0");
				text1neuepdfs.setBounds(10, 19, 45, 17);
			}
			{
				text1neuetexte = new Text(group7, SWT.NONE);
				text1neuetexte.setBounds(10, 42, 45, 17);
				text1neuetexte.setText("0");
			}
			{
				label1 = new Label(group7, SWT.NONE);
				label1.setText("anz neue PDFs");
				label1.setBounds(61, 19, 162, 18);
			}
			{
				label2 = new Label(group7, SWT.NONE);
				label2.setText("anz neue Texte");
				label2.setBounds(61, 42, 98, 19);
			}
			{
				label1letzteDatenbasis = new Label(group7, SWT.BORDER);
				String lastm = FileAccess
						.getVerzAttribLastModified(GC.textzielbase);

				label1letzteDatenbasis.setText(lastm);
				label1letzteDatenbasis.setBounds(12, 67, 211, 22);
			}
			{
				button1datenholen = new Button(group7, SWT.PUSH
						| SWT.CENTER);
				button1datenholen.setText("NeueDatenHolen");
				button1datenholen.setBounds(229, 105, 128, 30);
				button1datenholen.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1datenholenWidgetSelected(evt);
					}
				});
			}
			{
				label3 = new Label(group7, SWT.NONE);
				label3.setText("letztes Holdatum");
				label3.setBounds(243, 67, 114, 30);
			}
			{
				Auto1 = new Button(group7, SWT.CHECK | SWT.LEFT);
				Auto1.setText("AqAuto1");
				Auto1.setBounds(5, 117, 78, 30);
				
			}
		}
		{
			group5 = new Group(group1, SWT.NONE);
			group5.setLayout(null);
			group5.setText("Datenbasis Info");
			group5.setBounds(419, 596, 322, 147);
			{
				text1 = new Text(group5, SWT.NONE);
				text1.setText("0");
				text1.setBounds(8, 25, 33, 20);
			}
			{
				label4 = new Label(group5, SWT.NONE);
				label4.setText("Anz Pdfs");
				label4.setBounds(53, 25, 57, 20);
			}
			{
				text2 = new Text(group5, SWT.NONE);
				text2.setText("0");
				text2.setBounds(8, 57, 33, 21);
			}
			{
				label5 = new Label(group5, SWT.NONE);
				label5.setText("Anz Texte");
				label5.setBounds(53, 57, 79, 21);
			}
			{
				text3 = new Text(group5, SWT.NONE);
				text3.setText("0");
				text3.setBounds(8, 90, 33, 20);
			}
			{
				label6 = new Label(group5, SWT.NONE);
				label6.setText("Anz versch Börblätter");
				label6.setBounds(53, 90, 171, 20);
			}
		}
		{
			group6 = new Group(group1, SWT.NONE);
			group6.setLayout(null);
			group6.setText("Suche File");
			group6.setBounds(785, 498, 680, 150);
			{
				suchfile = new Text(group6, SWT.NONE);
				suchfile.setText("name");
				suchfile.setBounds(8, 25, 660, 20);
			}
			{
				SelectFile = new Button(group6, SWT.PUSH | SWT.CENTER);
				SelectFile.setText("SelectFile");
				SelectFile.setBounds(8, 51, 158, 26);
				SelectFile.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1SelectFile(evt);
					}
				});
			}
			{
				Suche = new Button(group6, SWT.PUSH | SWT.CENTER);
				Suche.setText("ErstelleSuchliste2");
				Suche.setBounds(8, 83, 158, 24);
				Suche.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1ErstelleSuchliste2(evt);
					}
				});
			}
			{
				Zeige = new Button(group6, SWT.PUSH | SWT.CENTER);
				Zeige.setText("Zeige");
				Zeige.setBounds(172, 83, 172, 24);
				Zeige.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1zeige2(evt);
					}
				});
			}
			{
				EditSuchfile = new Button(group6, SWT.PUSH | SWT.CENTER);
				EditSuchfile.setText("Edit");
				EditSuchfile.setBounds(172, 51, 172, 26);
				EditSuchfile.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1EditSuchfile(evt);
					}
				});
			}
			{
				GenWallstreetMeistDiskutierte = new Button(group6, SWT.PUSH | SWT.CENTER);
				GenWallstreetMeistDiskutierte.setText("GenWallstrMeistDiskutierte");
				GenWallstreetMeistDiskutierte.setBounds(426, 51, 152, 30);
				GenWallstreetMeistDiskutierte.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1Wallstreet(evt);
					}
				});
			}
		}
		{
			buttonKeineTeilwoerter = new Button(group1, SWT.CHECK | SWT.LEFT);
			buttonKeineTeilwoerter.setText("KeineTeilwoerter");
			buttonKeineTeilwoerter.setBounds(982, 48, 161, 30);
			buttonKeineTeilwoerter.setSelection(true);
			buttonKeineTeilwoerter
					.setToolTipText("Wenn das angeklickt ist werden Wörter die in andere eingebettet sind ausgeschlossen.\r\nEs muss sich vor oder hinter dem Wort ein LEERZEICHEN befinden\r\nBsp: nike\r\n\"kanikel\" genügt nicht den Bedingungen\r\n\r\nnike1 oder 1nike schon \r\n");
			buttonKeineTeilwoerter.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1keineTeilwoerter(evt);
				}
			});
		}
		{
			AListeVerwenden = new Button(group1, SWT.CHECK | SWT.LEFT);
			AListeVerwenden.setText("Auss.ListeVerwenden");
			AListeVerwenden.setBounds(1155, 48, 169, 30);
			AListeVerwenden.setSelection(true);
			AListeVerwenden.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					buttonAlisteVerwenden(evt);
				}
			});
		}
		{
			EditAusliste = new Button(group1, SWT.PUSH | SWT.CENTER);
			EditAusliste.setText("EditAusliste");
			EditAusliste.setBounds(1330, 48, 101, 30);
			EditAusliste.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1EditAliste(evt);
				}
			});
		}
		{
			i = new Label(group1, SWT.NONE);
			i.setText("(i)");
			i.setBounds(711, 24, 60, 30);
			i.setToolTipText("In der letzten Spalte Patterns können maximal 250 Zeichen dargestellt werden. D.h. es können möglicherweise nicht alle Patterns angezeigt werden. ");
		}
		{
			AutomaticTask = new Button(group1, SWT.PUSH | SWT.CENTER);
			AutomaticTask.setText("AutomaticTask");
			AutomaticTask.setBounds(1311, 719, 154, 30);
			AutomaticTask.setToolTipText("Wenn Automatik-Task angewählt wird werden die folgenden Aktionen in Reihe ausgeführt.\r\n\r\n1) AqAuto1 (Textextration der Börsenblätter)\r\n2) SlAuto2 (Durchsuchen nach Keywörtern)\r\n3) NeuAuto3 (Suchen der Neuempfehlungen)");
			AutomaticTask.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					buttonAutomaticTask(evt);
				}
			});
		}

		tab.setControl(group1);
		Auto1.setSelection(true);
		Auto2.setSelection(true);
		Auto3.setSelection(true);
		group1.pack();
		group1.setSize(1496, 753);
		group2.pack();
		group3.pack();
	}

	
	
	private void buttonAutomaticTask(SelectionEvent evt)
	{
		System.out.println("AutomaticTask, event=" + evt);
		//neue daten holen
		if(Auto1.getSelection()==true)
			button1datenholenWidgetSelected(evt);
		//suchliste erstellen
		if(Auto2.getSelection()==true)
			button1suchlisteWidgetSelected(evt);
		//neue Empfehlungen suchen
		if(Auto3.getSelection()==true)
		{
			button1SucheNeuempfehlung( evt);
			//zeige neuempfehlungen auch an
			button1zeigeNeuempfehlung(evt);
		}
	}
	private void button1EditAliste(SelectionEvent evt)
	{
		System.out.println("button1EditAliste, event=" + evt);
		ExtFileEditor config = new ExtFileEditor();
		config.init(display_glob, GC.rootpath
				+ "\\conf\\boersenblaetter\\configAusListe.txt");

	}

	private void button1keineTeilwoerter(SelectionEvent evt)
	{
		if (buttonKeineTeilwoerter.getSelection() == true)
			keineTeilwoerterflag_glob = 1;
		else
			keineTeilwoerterflag_glob = 0;

	}

	private void buttonAlisteVerwenden(SelectionEvent evt)
	{
		if (AListeVerwenden.getSelection() == true)
			auslisteVerwenden_glob = 1;
		else
			auslisteVerwenden_glob = 0;
	}

	private void button1zeige2(SelectionEvent evt)
	{
		MailsucherListe ms = new MailsucherListe();
		ms.zeigeErgebnisse(display_glob);
	}

	private void button1ErstelleSuchliste2(SelectionEvent evt)
	{
		// BUTTON Erstelle Suchliste LISTE2 (z.B. suchliste von Boerse.de)
		System.out.println("button1suchliste.widgetSelected, event=" + evt);
		MailsucherListe ms = new MailsucherListe();
		String config = suchfile_glob;

		String ausliste = GC.rootpath
				+ "\\conf\\boersenblaetter\\configAusliste.txt";

		String dat = null;
		if (button1startdatum.getSelection() == true)
			dat = text1Mindat.getText();

		// a) Hier wird Liste aufgebaut was wir denn suchen
		Suchliste such13 = ms.erstelleSuchliste(display_glob,
				progressBar1_glob, config, dat, keineTeilwoerterflag_glob,
				auslisteVerwenden_glob, ausliste);

		// e) Stelle die Ergebnisse in eine Tabelle dar
		SwtTabelle.baueTabellePuscherFilesMulti(display_glob, such13, table1,
				hmap, boerdb_glob);
	}

	private void button1EditSuchfile(SelectionEvent evt)
	{

		// Edit Suchfile
		System.out.println("button1configsuchliste.widgetSelected, event="
				+ evt);
		ExtFileEditor config = new ExtFileEditor();
		config.init(display_glob, suchfile_glob);
	}

	private void button1SelectFile(SelectionEvent evt)
	{
		// Filerequester um ein configfile auszuwählen
		Viewer viewer = new Viewer();
		String fnam = viewer.fileConfRequester(display_glob, GC.rootpath
				+ "\\conf\\boersenblaetter", SWT.SEARCH);
		suchfile.setText(fnam);
		suchfile_glob = fnam;
	}

	private void button1sucheWidgetSelected(SelectionEvent evt)
	{
		// Button Einzelsuche
		String dat = null;

		System.out.println("button1suche.widgetSelected, event=" + evt);
		String Suchtext = text1suchtext.getText();
		Found found = new Found("Pos#Boerblatt#Datum#Filename");

		if (button1startdatum.getSelection() == true)
			dat = text1Mindat.getText();

		MailsucherSchluesselwortFile_dep ms = new MailsucherSchluesselwortFile_dep();
		ms.SucheSchluesselwort(display_glob, progressBar1_glob, dat, Suchtext,
				found, 1000, keineTeilwoerterflag_glob);

		SwtTabelle.baueTabellePuscherFiles(display_glob, table1, found, hmap,
				boerdb_glob);
	}

	private void button1datenholenWidgetSelected(SelectionEvent evt)
	{
		// Button "Neue Daten holen"
		System.out.println("button1datenholen.widgetSelected, event=" + evt);
		BStat stat = Mailcoder.convertiereAlleMails(display_glob,
				progressBar1_glob);

		text1neuepdfs.setText(String.valueOf(stat.getNeuePdfs()));
		text1neuetexte.setText(String.valueOf(stat.getNeueTexte()));

		String lastm = FileAccess.getVerzAttribLastModified(GC.textzielbase);
		label1letzteDatenbasis.setText(lastm);
		Swttool.wupdate(display_glob);
	}

	private void ActionTable1(SelectionEvent evt)
	{
		// Doppelklick auf Tabelle !!
		// Hier wurde aus der Liste ein File Ausgewählt
		// Per Doppelklick
		String seltext = evt.item.toString();
		String sel = seltext.substring(seltext.indexOf("{") + 1);
		sel = sel.replace("}", "");

		int pos = (SG.get_zahl(sel));
		String filenam = hmap.get(pos);
		String filepath = GC.textzielbase + filenam;


		//Anzeige mit Notepad
		button1showpdf.setText(filepath);
		Tools.showText(filepath);
		
		
		
		//Anzeige unter SWT
		/* Tab13ShowTexte st = new Tab13ShowTexte();
		// Hier wird der Text angezeigt
		st.init(display_glob, filepath, text1suchtext.getText());
		button1showpdf.setText(filepath);
		Swttool.wupdate(display_glob);

		*/

		System.out.println("hallo");
	}

	private void button1zeigeNeuempfehlung(SelectionEvent evt)
	{
		System.out.println("buttonZeigeNeuempfehlung.widgetSelected, event=" + evt);
		ViewExternFileTable viewer= new ViewExternFileTable();
		viewer.ShowTable(display_glob, GC.rootpath+"\\db\\bbevent.db","",null);
	}
	
	private void button1showpdfWidgetSelected(SelectionEvent evt)
	{
		// Button Pdf anzeigen
		System.out.println("button1showpdf.widgetSelected, event=" + evt);
		String filepath = button1showpdf.getText();
		String filepathtext = filepath;
		filepath = filepath.replace("text", "pdf");
		filepath = filepath.replace(".txt", ".pdf");
		// filepath="G:\\Mail\\Pdf\\HEBEL\\2010-1-8_KW_10_01_DERHEBEL.pdf";

		// holt das boersenblatt
		BoersenblattDbObj boerbl = null;
		boerbl = boerdb_glob.getBoerblatt(filepath);
		// noch nicht drin
		if (boerbl == null)
		{
			// erzeuge es und speichere
			String blattname = BoersenBlaetterDB.calcBoername(filepath);
			boerbl = new BoersenblattDbObj(blattname + "#" + filepath + "#"
					+ Tools.get_aktdatetime_str());
			boerdb_glob.AddObject(boerbl);
		}
		boerbl.setLastread(Tools.get_aktdatetime_str());
		boerdb_glob.WriteDB();

		try
		{
			String cmd = "\"" + GC.acroreaderbase + "\" \"" + filepath + "\"";
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

	private void button1configsuchlisteWidgetSelected(SelectionEvent evt)
	{
		// Edit config
		System.out.println("button1configsuchliste.widgetSelected, event="
				+ evt);
		ExtFileEditor config = new ExtFileEditor();
		config.init(display_glob, GC.rootpath
				+ "\\conf\\boersenblaetter\\ConfigSuchliste.txt");
	}

	private void button1suchlisteWidgetSelected(SelectionEvent evt)
	{
		// BUTTON Erstelle Suchliste LISTE
		System.out.println("button1suchliste.widgetSelected, event=" + evt);
		MailsucherListe ms = new MailsucherListe();
		String config = GC.rootpath
				+ "\\conf\\boersenblaetter\\ConfigSuchliste.txt";

		String ausliste = GC.rootpath
				+ "\\conf\\boersenblaetter\\configAusliste.txt";

		String dat = null;
		if (button1startdatum.getSelection() == true)
			dat = text1Mindat.getText();

		// a) Hier wird Liste aufgebaut was wir denn suchen
		Suchliste such13 = ms.erstelleSuchliste(display_glob,
				progressBar1_glob, config, dat, keineTeilwoerterflag_glob,
				auslisteVerwenden_glob, ausliste);

		// b) Hier wird eine Liste aufgebaut was wir denn gefunden haben
		// ms.erstelleFoundliste

		// c) Sortiere die Foundliste und Färbe ein

		// d) Speichere die Ergebnisse in ein File

		// e) Stelle die Ergebnisse in eine Tabelle dar
		SwtTabelle.baueTabellePuscherFilesMulti(display_glob, such13, table1,
				hmap, boerdb_glob);

	}

	private void button1zeigesuchlisteWidgetSelected(SelectionEvent evt)
	{
		System.out
				.println("button1zeigesuchliste.widgetSelected, event=" + evt);

		MailsucherListe ms = new MailsucherListe();
		ms.zeigeErgebnisse(display_glob);
	}

	private void button1erweitereKeywortlisteWidgetSelected(SelectionEvent evt)
	{
		// Erweitere die Keywortliste
		System.out
				.println("button1erweitereKeywortliste.widgetSelected, event="
						+ evt);
		int neukwzaehler = 0;

		int kdbanz = kdb_glob.GetanzObj();

		int anz = aktdb_glob.GetanzObj();
		text4anz.setText(String.valueOf(kdbanz));

		String aufdat = Tools.entferneZeit(Tools.get_aktdatetime_str());
		progressBar1_glob.setMaximum(0);
		progressBar1_glob.setMaximum(anz);

		// gehe durch die aktdb
		for (int i = 0; i < anz; i++)
		{
			// holt die nächste wkn
			AktDbObj aobj = (AktDbObj) aktdb_glob.GetObjectIDX(i);
			String wkn = aobj.getWkn();
			String aname = aobj.getAktname();
			progressBar1_glob.setSelection(i);
			Swttool.wupdate(display_glob);

			// Wkn ist ein Suchschlüssel
			if ((wkn.length() > 2) && (kdb_glob.checkKey(wkn) == false))
			{
				KeyDbObj ko = new KeyDbObj(wkn + "#" + aname + "#"
						+ aobj.getMasterid() + "#" + aufdat + "#0#0");
				kdb_glob.AddObject(ko);
				neukwzaehler++;
			}
		}
		ChampionDB champdb = new ChampionDB();
		anz = champdb.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			ChampionDbObj champobj = (ChampionDbObj) champdb.GetObjectIDX(i);
			String wkn = champobj.getWkn();
			if (kdb_glob.checkKey(wkn) == false)
			{
				KeyDbObj ko = new KeyDbObj(wkn + "#" + champobj.getName()
						+ "#0#" + aufdat + "#0#0");
				kdb_glob.AddObject(ko);
				neukwzaehler++;
			}
		}

		text4neukw.setText(String.valueOf(neukwzaehler));
		Swttool.wupdate(display_glob);
		kdb_glob.clearHitcounter();
		kdb_glob.WriteDB();
	}

	private void button1SucheNeuempfehlung(SelectionEvent evt)
	{
		// BUTTON: Suche Neuempfehlung
		String dat = null;
		if (button1startdatum.getSelection() == true)
			dat = text1Mindat.getText();
		else
			dat = "30.08.00";

		System.out.println("button1SucheAbgleich.widgetSelected, event=" + evt);

		// Durchsuche alle BB-Blätter nach den Keywörtern und baue die
		// Boersenblatt.db weiter auf, falls das Keyword von einem Börsenblatt
		// gepusched
		// wurde wird dies auch in tdb vermerkt. Es wird gespeichert wann der
		// letzte Pusch
		// stattgefunden hat.

		// Wie werden die Neuempfehlungen gefunden
		// Verwendete Files:
		// 1:keydb.db: liste aller zu suchenenden Wörter (z.B. wkn´s)
		// 2:boersenblaetter.db: lister aller boersenblatter mit files
		// 3:..sublisten/boersenblatt/... befinden sich die suchergebnisse zu
		// den boersenblaettern
		// 4:bbevent.db: beinhaltet die NEUEMPFEHLUNGEN (Also Ergebniss)

		// Suchvorgang zum Finden der Neuempfehlungen:
		// Die börsenblätter in 2) mit einem maximalen alter von 20 Tagen werden
		// nach allen Schlüsselwörtern in 1) durchsucht
		// Die gefundenen Schlüsselwörter werden in passender zuordnung zum BB
		// in 3) abgespeichert
		// Wurde ein Schlüsselwort aus 1) das erste Mal in 3) gefunden ist dies eine Neuempfehlung
		// Wurde ein Schlüsselwort aus 1) das erste mal in einem Boersenblatt von 3) gefunden ist 
		// dies eine Neuempfehlung über alles

		SucheNeuempfehlung sa = new SucheNeuempfehlung();
		sa.sucheAlles(tdb_glob, dat, boerdb_glob);

	}

	private void button1zeigekeylisteWidgetSelected(SelectionEvent evt)
	{
		// Zeige Keydb
		System.out.println("button1zeigekeyliste.widgetSelected, event=" + evt);
		KeyComperator c1 = new KeyComperator();

		Collections.sort(kdb_glob.dbliste, c1);
		kdb_glob.WriteDB();
		ViewExternFileTable viewer = new ViewExternFileTable();
		viewer.ShowTable(display_glob, GC.rootpath + "\\db\\keydb.db", "KeyDB",
				null);
	}

	private void button1zeigeallesWidgetSelected(SelectionEvent evt)
	{ // Zeige Suchergebnisse
		System.out.println("button1zeigealles.widgetSelected, event=" + evt);

		// Hier wird eine Tabelle der Schlüsselwörter aufgebaut und Angezeigt
		ViewBoersenblaetter viewer = new ViewBoersenblaetter();
		viewer.ShowTable(display_glob, "BoersenblaetterDB");

	}
	
	private void button1Wallstreet(SelectionEvent evt)
	{
		HtmlMeistDiskutiert hm= new HtmlMeistDiskutiert();
		hm.DownloadPage();
		hm.ErstelleConfigMeistDiskutiert();
	}
	
}
