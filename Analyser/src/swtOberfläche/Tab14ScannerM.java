package swtOberfläche;

import kurse.KurseDB;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import stores.AktDB;
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
public class Tab14ScannerM
{
	private Display display_glob = null;
	private AktDB aktdb_glob = null;
	private SlideruebersichtDB sldb_glob=null;
	
	private ThreadsDB tdb_glob = null;
	private Group groupMain;
	private TabItem tabItem1;
	private TabItem tabItem2;
	private Label label1;
	private TabItem tabItem3;
	private TabFolder tabFolder1;
	private Tab14M1 t14m1 = null;

	public void init(TabFolder folder, TabItem tab, Display dis, UserDB udb,
			ThreadsDB tdb, AktDB aktdb, UeberwachungDB ueb, KurseDB kdb,SlideruebersichtDB sldb)
	{
		// udb_glob = udb;

		display_glob = dis;
		aktdb_glob = aktdb;
		tdb_glob = tdb;
		sldb_glob=sldb;
		

		groupMain = new Group(folder, SWT.NONE);
		groupMain.setText("Scanner");
		groupMain.setLayout(null);
		{
			tabFolder1 = new TabFolder(groupMain, SWT.NONE);
			{
				tabItem1 = new TabItem(tabFolder1, SWT.NONE);
				tabItem1.setText("ThreadsDB");
			}
			{
				tabItem2 = new TabItem(tabFolder1, SWT.NONE);
				tabItem2.setText("User");
			}
			{
				tabItem3 = new TabItem(tabFolder1, SWT.NONE);
				tabItem3.setText("tabItem3");
			}
			// Tab1 mit dem Tab14M1 verknüpfen
			t14m1 = new Tab14M1();
			t14m1.init(tabFolder1, tabItem1, display_glob, tdb_glob,sldb_glob);

			tabFolder1.setBounds(27, 39, 906, 815);
			tabFolder1.setSelection(0);
			
		}
		{
			label1 = new Label(groupMain, SWT.NONE);
			label1.setText("x");
			label1.setBounds(983, 933, 24, 20);
		}
		tab.setControl(groupMain);
		groupMain.pack();
	}

	

}
