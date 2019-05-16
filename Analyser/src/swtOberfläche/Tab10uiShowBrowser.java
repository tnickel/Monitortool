package swtOberfläche;

import hilfsklasse.SG;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsobjekt.Fensterinfo;

import java.util.HashMap;
import java.util.HashSet;

import objects.ThreadDbObj;
import objects.UserDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import stores.ThreadsDB;
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
public class Tab10uiShowBrowser
{
	private Group group1;
	private List list1;
	private Button button1showUserinfo;
	private Browser browser1;
	private Group group2;
	private Display display_glob = null;
	private UserDB udb_glob = null;
	private ThreadsDB tdb_glob = null;
	private int seltid_glob = 0;
	private String username_glob = "";
	private Button button1setzeGelesen;
	private Group group3;
	private Button button1setuserbewertung;
	private Text text1userbewertung;
	private Text textlesedatum;
	private Button button1extuserinfo;
	private HashMap<Integer, String> usermap = new HashMap<Integer, String>();
	private HashSet<String> usermenge_glob = null;
	private Fensterinfo finfo_glob = null;

	public void init(Display dis, UserDB udb, ThreadsDB tdb, String file,
			HashSet<String> userm, Fensterinfo finfo, int seltid,
			ThreadDbObj tdbo)
	{
		usermenge_glob = userm;
		Shell sh = new Shell(dis);
		sh.setLayout(null);
		display_glob = dis;
		udb_glob = udb;
		tdb_glob = tdb;
		seltid_glob = seltid;
		finfo_glob = finfo;

		// Obere Group für die globalen Userinfo
		{
			group1 = new Group(sh, SWT.NONE);
			group1.setLayout(null);
			String groupinfo = "["
					+ tdbo.getThreadname()
					+ "]"
					+ Tools.entferneZeit(" SSuchdat="
							+ finfo.getSuchstartdatum()) + " Postzeit:"
					+ finfo.getIstStartdatum() + "-"
					+ Tools.entferneZeit(finfo.getIstEnddatum()) + " Postanz="
					+ finfo.getPostcounter() + "  In DB letztes posting="
					+ tdbo.getLastreadtime();
			group1.setText(groupinfo);
			group1.setBounds(-1, 6, 865, 611);
			{
				browser1 = new Browser(group1, SWT.NONE);
				browser1.setText("browser1");
				browser1.setBounds(6, 20, 847, 579);
				SwtTools.fuelleBrowser(browser1, file);
			}
		}
		{
			group2 = new Group(sh, SWT.NONE);
			group2.setLayout(null);
			group2.setText("group2");
			group2.setBounds(864, 6, 227, 611);
			{
				list1 = new List(group2, SWT.H_SCROLL | SWT.V_SCROLL);
				list1.setBounds(8, 22, 207, 462);
				list1.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						list1WidgetSelected(evt);
					}
				});
				SwtTools
						.baueUserListe(list1, usermenge_glob, usermap, udb_glob);
			}
			{
				button1showUserinfo = new Button(group2, SWT.PUSH | SWT.CENTER);
				button1showUserinfo.setText("ShowUserinfo");
				button1showUserinfo.setBounds(81, 487, 134, 30);
				button1showUserinfo.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1showUserinfoWidgetSelected(evt);
					}
				});
			}
			{
				button1setuserbewertung = new Button(group2, SWT.PUSH
						| SWT.CENTER);
				button1setuserbewertung.setText("Set Userbewertung");
				button1setuserbewertung.setBounds(81, 552, 134, 29);
				button1setuserbewertung
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1setuserbewertungWidgetSelected(evt);
							}
						});
			}
			{
				text1userbewertung = new Text(group2, SWT.NONE);
				text1userbewertung.setBounds(8, 552, 67, 29);
			}
			{
				button1extuserinfo = new Button(group2, SWT.CHECK | SWT.LEFT);
				button1extuserinfo.setText("Ext. Userinfo");
				button1extuserinfo.setBounds(81, 520, 134, 20);
			}
		}
		{
			group3 = new Group(sh, SWT.NONE);
			GridLayout group3Layout = new GridLayout();
			group3Layout.makeColumnsEqualWidth = true;
			group3.setLayout(group3Layout);
			group3.setText("group3");
			group3.setBounds(6, 629, 337, 80);
			{
				button1setzeGelesen = new Button(group3, SWT.PUSH | SWT.CENTER);
				GridData button1setzeGelesenLData = new GridData();
				button1setzeGelesen.setLayoutData(button1setzeGelesenLData);
				button1setzeGelesen.setText("Markiere Gelesen");
				button1setzeGelesen.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1setzeGelesenWidgetSelected(evt);
					}
				});
			}
			{
				textlesedatum = new Text(group3, SWT.NONE);
				textlesedatum.setBounds(300, 629, 150, 29);
				textlesedatum.setText(finfo.getIstEnddatum());
			}
		}
		button1extuserinfo.setSelection(false);
		group2.pack();
		group3.pack();
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
	}

	private void button1showUserinfoWidgetSelected(SelectionEvent evt)
	{
		int extinfoflag = 0;
		// Userinfo wurde geklickt
		System.out.println("button1showUserinfo.widgetSelected, event=" + evt);

		if (button1extuserinfo.getSelection() == true)
			extinfoflag = 1;

		Tab4uiShowExternalUserinfo t = new Tab4uiShowExternalUserinfo();
		t.viewTableExt(display_glob, username_glob, udb_glob, tdb_glob,
				extinfoflag);
	}

	private void list1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("list1.widgetSelected, event=" + evt);
		String string = "";
		int[] selection = list1.getSelectionIndices();

		for (int i = 0; i < selection.length; i++)
			string += selection[i] + " ";

		System.out.println(string);
		int pos = SG.get_zahl(string);
		String username = usermap.get(pos);
		username_glob = username;
		UserDbObj udbo = udb_glob.getUserobj(username);
		text1userbewertung.setText(String.valueOf(udbo.getManbewertung()));
		Swttool.wupdate(display_glob);
	}

	private void button1setuserbewertungWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setuserbewertung.widgetSelected, event="
				+ evt);

		// hier die Userbewertung gesetzt (gib den User 0 bis 100 Punkte)
		String userbewert = text1userbewertung.getText();
		Float fl = Float.valueOf(userbewert);
		UserDbObj udbo = udb_glob.getUserobj(username_glob);
		udbo.setManbewertung(fl);
		udb_glob.WriteDB();

		System.out.println("val=" + fl);
	}

	private void button1setzeGelesenWidgetSelected(SelectionEvent evt)
	{
		// setzt den Thread als gelesen und schreibt auf platte
		System.out.println("button1setzeGelesen.widgetSelected, event=" + evt);

		ThreadDbObj tdbo = tdb_glob.SearchThreadid(seltid_glob);
		String newtime = finfo_glob.getIstEnddatum();

		tdbo.setLastreadtime(newtime);
		tdb_glob.WriteDB();
	}
}
