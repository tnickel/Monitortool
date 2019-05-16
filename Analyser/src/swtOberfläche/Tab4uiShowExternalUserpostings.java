package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;

import java.util.HashSet;

import mainPackage.GC;
import objects.InfoUserpost;
import objects.UserDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import ranking.UserStatistik;
import stores.AktDB;
import stores.ThreadsDB;
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
public class Tab4uiShowExternalUserpostings
{
	private Group group1;
	private Text text1username;
	private Label Benutzername;
	private Button button1;
	private Text text1startdatum;
	private Button button2startdatum;
	private Button button2selectall;
	private Button button2reset;
	private Text text1info;
	private Button button2ganzedb;
	private ProgressBar progressBar1;
	private Button button2suche;
	private Text text1such;
	private Button button2selthreads;
	private Button button2enddatum;
	private Text text1enddatum;
	private Table table3;
	private Group group3, group2;
	private Button button1show;
	private Display dis_glob = null;
	private String unam_glob = null;
	private SwtTools st = new SwtTools();
	// diese liste verwaltet welches häcken angeklickt wurde
	private boolean[] uplliste_g = new boolean[1000];
	private ThreadsDB tdb_glob=null;
	private int sellallstate_g=0;

	public void viewTableExt(Display dis, String unam, UserDB udb,ThreadsDB tdb)
	{
		// stellt ein .db-file als tabelle dar
		// Baut hierzu ein neues Fenster auf
		// Hierzu wird das Display benötigt
		UserDbObj udbo = udb.getUserobj(unam);
		unam_glob = unam;
		tdb_glob=tdb;
		Shell sh = new Shell(dis);
		sh.setLayout(null);
		dis_glob = dis;

		// Obere Group für die globalen Userinfo
		group1 = new Group(sh, SWT.NONE);
		group1.setLayout(null);
		group1.setText("User Global Info");
		group1.setBounds(12, 0, 566, 47);

		text1username = new Text(group1, SWT.NONE);
		text1username.setBounds(8, 22, 259, 17);
		text1username.setText(unam);
		Benutzername = new Label(group1, SWT.NONE);
		Benutzername.setText("Benutzername");
		Benutzername.setBounds(273, 22, 94, 17);

		// Diese Group beinhaltet eine Tabelle mit den userthreads
		group3 = new Group(sh, SWT.NONE);
		group3.setLayout(null);
		group3.setText("group3");
		group3.setBounds(12, 53, 1005, 419);
		{
			group2 = new Group(sh, SWT.NONE);
			FormLayout group2Layout = new FormLayout();
			group2.setLayout(group2Layout);
			group2.setText("group2");
			{
				text1info = new Text(group2, SWT.NONE);
				FormData text1infoLData = new FormData();
				text1infoLData.left =  new FormAttachment(0, 1000, 5);
				text1infoLData.top =  new FormAttachment(0, 1000, 106);
				text1infoLData.width = 962;
				text1infoLData.height = 17;
				text1info.setLayoutData(text1infoLData);
			}
			{
				button2ganzedb = new Button(group2, SWT.CHECK | SWT.LEFT);
				FormData button2ganzedbLData = new FormData();
				button2ganzedbLData.left =  new FormAttachment(0, 1000, 361);
				button2ganzedbLData.top =  new FormAttachment(0, 1000, 75);
				button2ganzedbLData.width = 167;
				button2ganzedbLData.height = 19;
				button2ganzedb.setLayoutData(button2ganzedbLData);
				button2ganzedb.setText("Ganze DB durchsuchen");
				button2ganzedb.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button2ganzedbWidgetSelected(evt);
					}
				});
			}
			{
				FormData progressBar1LData = new FormData();
				progressBar1LData.left =  new FormAttachment(0, 1000, 5);
				progressBar1LData.top =  new FormAttachment(0, 1000, 129);
				progressBar1LData.width = 970;
				progressBar1LData.height = 21;
				progressBar1 = new ProgressBar(group2, SWT.NONE);
				progressBar1.setLayoutData(progressBar1LData);
			}
			{
				button2reset = new Button(group2, SWT.PUSH | SWT.CENTER);
				FormData button2resetLData = new FormData();
				button2resetLData.left = new FormAttachment(0, 1000, 856);
				button2resetLData.top = new FormAttachment(0, 1000, -5);
				button2resetLData.width = 119;
				button2resetLData.height = 27;
				button2reset.setLayoutData(button2resetLData);
				button2reset.setText("Reset");
			}
			{
				button2selthreads = new Button(group2, SWT.CHECK | SWT.LEFT);
				FormData button2selthreadsLData = new FormData();
				button2selthreadsLData.left = new FormAttachment(0, 1000, 164);
				button2selthreadsLData.top = new FormAttachment(0, 1000, 75);
				button2selthreadsLData.width = 163;
				button2selthreadsLData.height = 19;
				button2selthreads.setLayoutData(button2selthreadsLData);
				button2selthreads.setText("Nur selektierte Threads");
				button2selthreads
						.setToolTipText("Die selektierten Threads werden für einen User betrachtet. Hierbei werden für die TID´s die Mid´s ermittelt und sämmtliche Threads mit gleicher Mid ebenfalls ausgewählt");
			}
			{
				button2enddatum = new Button(group2, SWT.CHECK | SWT.LEFT);
				FormData button2enddatumLData = new FormData();
				button2enddatumLData.left = new FormAttachment(0, 1000, 164);
				button2enddatumLData.top = new FormAttachment(0, 1000, 44);
				button2enddatumLData.width = 88;
				button2enddatumLData.height = 19;
				button2enddatum.setLayoutData(button2enddatumLData);
				button2enddatum.setText("Enddatum");
			}
			{
				button2startdatum = new Button(group2, SWT.CHECK | SWT.LEFT);
				FormData button2startdatumLData = new FormData();
				button2startdatumLData.left = new FormAttachment(0, 1000, 164);
				button2startdatumLData.top = new FormAttachment(0, 1000, 13);
				button2startdatumLData.width = 94;
				button2startdatumLData.height = 19;
				button2startdatum.setLayoutData(button2startdatumLData);
				button2startdatum.setText("Startdatum");
			}
			{
				text1enddatum = new Text(group2, SWT.NONE);
				FormData text1enddatumLData = new FormData();
				text1enddatumLData.left = new FormAttachment(0, 1000, 264);
				text1enddatumLData.top = new FormAttachment(0, 1000, 44);
				text1enddatumLData.width = 103;
				text1enddatumLData.height = 17;
				text1enddatum.setLayoutData(text1enddatumLData);
			}
			{
				FormData text1startdatumLData = new FormData();
				text1startdatumLData.left = new FormAttachment(0, 1000, 264);
				text1startdatumLData.top = new FormAttachment(0, 1000, 13);
				text1startdatumLData.width = 103;
				text1startdatumLData.height = 17;
				text1startdatum = new Text(group2, SWT.NONE);
				text1startdatum.setLayoutData(text1startdatumLData);
			}

			{
				button1show = new Button(group2, SWT.PUSH | SWT.CENTER);
				FormData button1showLData = new FormData();
				button1showLData.width = 89;
				button1showLData.height = 20;
				button1showLData.left =  new FormAttachment(5, 1000, 0);
				button1showLData.right =  new FormAttachment(94, 1000, 0);
				button1showLData.top =  new FormAttachment(22, 1000, 0);
				button1showLData.bottom =  new FormAttachment(200, 1000, 0);
				button1show.setLayoutData(button1showLData);
				button1show.setText("ShowHtmlfile");
				button1show.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1showWidgetSelected(evt);
					}
				});
			}
			{
				button1 = new Button(group2, SWT.RADIO | SWT.LEFT);
				FormData button1LData = new FormData();
				button1LData.left = new FormAttachment(0, 1000, 149);
				button1LData.top = new FormAttachment(0, 1000, -229);
				button1LData.width = 91;
				button1LData.height = 19;
				button1.setLayoutData(button1LData);
				button1.setText("Startdatum");
			}
			group2.setBounds(4, 478, 1011, 178);

			String aktdate = Tools.get_aktdatetime_str();
			String lastcreatetime = Tools.modifziereDatum(aktdate, 0, -1, 0, 0);
			text1startdatum.setText(lastcreatetime);
			text1enddatum.setText(aktdate);

			button2startdatum.setSelection(true);
			button2enddatum.setSelection(true);
			button2selthreads.setSelection(true);
			button2selthreads.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button2selthreadsWidgetSelected(evt);
				}
			});

		}
		table3 = new Table(group3, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION
				| SWT.BORDER);
		table3.setLinesVisible(true);
		table3.setHeaderVisible(true);
		table3.setBounds(25, 14, 822, 393);
		table3.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				table3WidgetSelected(evt);
			}
		});
		{
			text1such = new Text(group3, SWT.BORDER);
			text1such.setBounds(859, 24, 121, 30);
		}
		{
			button2suche = new Button(group3, SWT.PUSH | SWT.CENTER);
			button2suche.setText("Suche");
			button2suche.setBounds(859, 60, 121, 30);
			button2suche.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button2sucheWidgetSelected(evt);
				}
			});
		}
		{
			button2selectall = new Button(group3, SWT.PUSH | SWT.CENTER);
			button2selectall.setText("AlleSelektieren");
			button2selectall.setBounds(859, 96, 121, 30);
			button2selectall.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button2selectallWidgetSelected(evt);
				}
			});
		}
		
		SwtTabelle.baueTabelleUserPostings(table3, unam,udbo,  uplliste_g);

		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		// dis.dispose();
	}

	private void button1showWidgetSelected(SelectionEvent evt)
	{
		HashSet<Integer> tidset=null;
		
		System.out.println("button1show.widgetSelected, event=" + evt);
		String inffile = GC.rootpath + "\\db\\observeuser_tmp.txt";
		if (FileAccess.FileAvailable0(inffile))
			FileAccess.FileDelete(inffile,1);

		UserStatistik ustat = new UserStatistik();

		Inf inf = new Inf();
		inf.setFilename(inffile);
		inf.writezeile(unam_glob + "#0");
		inf.close();

		UserDB udb = new UserDB("observeuser_tmp.txt", "boostraenge.txt");
		AktDB aktdb = new AktDB();

		button2selthreads.getSelection();

		// Das Enddatum bestimmen
		String aktdate = null;
		if (button2enddatum.getSelection() == true)
			aktdate = text1enddatum.getText();
		else
			aktdate = Tools.get_aktdatetime_str();

		// Das Startdatum bestimmen
		String lastcreatetime = null;
		if (button2startdatum.getSelection() == true)
			lastcreatetime = text1startdatum.getText();
		else
			lastcreatetime = "1.1.1999 20:15";

		
		//baut eine tid-Menge der selektierten Threads auf
		//mid´s werden zusammengefasst
		if(button2selthreads.getSelection()==true)
		{
			SwtTools stool = new SwtTools();
			tidset=stool.genSelektTidMenge(uplliste_g,table3,2);
		}
		
		InfoUserpost infu = new InfoUserpost();
		infu.SammleUserpostings(udb, aktdb, 0, "observuser_tmp.txt",
				lastcreatetime, aktdate, 1, ustat, progressBar1, text1info, dis_glob,
				"EinzelneUser",tidset,"rangfile");
		
		Viewer view = new Viewer();
		view.viewHtmlExtFile(dis_glob, GC.rootpath
				+ "\\info\\EinzelneUser\\userpostings\\"
				+ Tools.entferneZeit(lastcreatetime) + "\\" + unam_glob
				+ "_post1.html");
	}

	private void button2sucheWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2suche.widgetSelected, event=" + evt);
		String suchtext = text1such.getText();
		System.out.println("der text<" + suchtext + "> wird gesucht");

		int zeile = st.sucheZeile(table3, suchtext);
		table3.setSelection(zeile);
	}

	private void table3WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table3.widgetSelected, event=" + evt);

		if (evt.detail == SWT.CHECK)
		{
			String seltext = evt.item.toString();
			String selzahl = seltext.substring(seltext.indexOf("{") + 1,
					seltext.indexOf("}"));
			int sel = Integer.valueOf(selzahl);
			System.out.println("You checked " + evt.item);
			if (uplliste_g[sel] == true)
				uplliste_g[sel] = false;
			else if (uplliste_g[sel] == false)
				uplliste_g[sel] = true;
			System.out.println("index=<" + sel + "> aktivierung=<"
					+ uplliste_g[sel] + ">");

		} else
		{
			System.out.println("You selected " + evt.item);
		}

		String string = evt.detail == SWT.CHECK ? "Checked" : "Selected";
		System.out.println(evt.item + " " + string);

	}
	
	private void button2selectallWidgetSelected(SelectionEvent evt) 
	{
		System.out.println("button2selectall.widgetSelected, event="+evt);
		//TODO add your code for button2selectall.widgetSelected

		int anz=table3.getItemCount();
		
		if(sellallstate_g==0)
		{
			sellallstate_g=1;
			for(int i=0; i<anz; i++)
			{
				table3.getItem(i).setChecked(true);
				uplliste_g[i]=true;
			}
		}
		else
		{  //sellallstate=1
			sellallstate_g=0;
			for(int i=0; i<anz; i++)
			{
				table3.getItem(i).setChecked(false);
				uplliste_g[i]=false;
			}
		}
		Swttool.wupdate(dis_glob);
	}
	
	private void button2selthreadsWidgetSelected(SelectionEvent evt) 
	{
		System.out.println("button2selthreads.widgetSelected, event="+evt);
		//TODO add your code for button2selthreads.widgetSelected
		button2ganzedb.setSelection(false);
	}
	
	private void button2ganzedbWidgetSelected(SelectionEvent evt) 
	{
		System.out.println("button2ganzedb.widgetSelected, event="+evt);
		//TODO add your code for button2ganzedb.widgetSelected
		button2selthreads.setSelection(false);
	}

}
