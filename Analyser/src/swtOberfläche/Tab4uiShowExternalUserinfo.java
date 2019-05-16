package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.ThreadDbObj;
import objects.UserDbObj;
import objects.UserThreadPostingObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import stores.PostDBx;
import stores.ThreadsDB;
import stores.UserDB;
import userHilfObj.UserPostingListe;

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
public class Tab4uiShowExternalUserinfo
{
	private Group group1;
	private Text text1username;
	private Label Benutzername;
	private Table table1;
	private Table table3;
	private Group group3;
	private Group group2;

	public void viewTableExt(Display dis, String unam, UserDB udb,
			ThreadsDB tdb, int extflag)
	{
		// stellt ein .db-file als tabelle dar
		// Baut hierzu ein neues Fenster auf
		// Hierzu wird das Display benötigt

		UserDbObj udbo = udb.getUserobj(unam);
		Shell sh = new Shell(dis);
		sh.setLayout(null);

		// Obere Group für die globalen Userinfo
		group1 = new Group(sh, SWT.NONE);
		group1.setLayout(null);
		group1.setText("User Global Info");
		group1.setBounds(12, 0, 886, 87);

		if (unam == null)
		{
			Tracer.WriteTrace(10, "Error: keinen user ausgewählt");
			return;
		}
		text1username = new Text(group1, SWT.NONE);
		text1username.setBounds(8, 22, 259, 17);
		text1username.setText(unam);

		Benutzername = new Label(group1, SWT.NONE);
		Benutzername.setText("Benutzername");
		Benutzername.setBounds(273, 22, 94, 17);

		// In der mittleren Group wird eine Tabelle mit den
		// speziellen Usereingenschaften angezeigt
		group2 = new Group(sh, SWT.NONE);
		GridLayout group2Layout = new GridLayout();
		group2Layout.makeColumnsEqualWidth = true;
		group2.setLayout(group2Layout);
		group2.setText("group2");
		group2.setBounds(12, 99, 886, 226);

		GridData table1LData = new GridData();
		table1LData.widthHint = 353;
		table1LData.heightHint = 175;
		table1 = new Table(group2, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table1.setLinesVisible(true);
		table1.setHeaderVisible(true);
		table1.setLayoutData(table1LData);
		baueTabelleUserInfo(table1, unam, udbo);

		// Diese Group beinhaltet eine Tabelle mit den userthreads
		group3 = new Group(sh, SWT.NONE);
		GridLayout group3Layout = new GridLayout();
		group3Layout.makeColumnsEqualWidth = true;
		group3.setLayout(group3Layout);
		group3.setText("group3");
		group3.setBounds(12, 342, 886, 800);

		table3 = new Table(group3, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		GridData table3LData = new GridData();
		table3LData.widthHint = 775;
		table3LData.heightHint = 425;
		table3.setLinesVisible(true);
		table3.setHeaderVisible(true);
		table3.setLayoutData(table3LData);

		baueTabelleUserPostings(table3, unam, udbo, tdb, udb, extflag, dis);

		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		// dis.dispose();
	}

	private void baueTabelleUserInfo(Table table, String unam, UserDbObj udbo)
	{
		// Hier wird externes Fenster aufgemacht welches die Userinfo beinhaltet
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Beschreibung");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Value");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Heinweis");

		// Zeile 1
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, "Registriert seit");
		item.setText(1, udbo.getRegistriert());

		// Zeile 2
		item = new TableItem(table, SWT.NONE);
		item.setText(0, "Beiträge pro Tag");
		item.setText(1, String.valueOf(udbo.getBeitraegetag()));

		item = new TableItem(table, SWT.NONE);
		item.setText(0, "Rang");
		item.setText(1, String.valueOf(udbo.getRang()));

		item = new TableItem(table, SWT.NONE);
		item.setText(0, "BoostRang");
		item.setText(1, String.valueOf(udbo.getBoostrang()));

		item = new TableItem(table, SWT.NONE);
		item.setText(0, "Postings");
		item.setText(1, String.valueOf(udbo.getPostings()));

		item = new TableItem(table, SWT.NONE);
		item.setText(0, "Letzte Aktualisierung");
		String last = Tools.entferneZeit(udbo.getLetzteAktualisierung());
		String jetzt = Tools.entferneZeit(Tools.get_aktdatetime_str());
		int difftage = Tools.zeitdifferenz_tage(last, jetzt);
		item.setText(1, String.valueOf(udbo.getLetzteAktualisierung()));
		item.setText(2, "Tage=" + difftage);

		item = new TableItem(table, SWT.NONE);
		item.setText(0, "ManBewertung");
		item.setText(1, String.valueOf(udbo.getManbewertung()));

		// die Tabelle hat drei spalten
		for (int i = 0; i < 3; i++)
		{
			table.getColumn(i).pack();
		}
		table.setBounds(20, 20, 350, 200);
	}

	private void baueTabelleUserPostings(Table table, String unam,
			UserDbObj udbo, ThreadsDB tdb, UserDB udb, int extendedflag,
			Display dis)
	{
		PostDBx pdb = new PostDBx();

		UserPostingListe upl = new UserPostingListe();
		UserThreadPostingObj upobj = null;
		upl.ReadUserInfoListe(udbo);
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Pos");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Threadname");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Tid");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Postanz");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Aufnahmedatum");
		column = new TableColumn(table, SWT.NONE);
		column.setText("LastPosting");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Bild");

		column = new TableColumn(table, SWT.NONE);
		column.setText("ManBewertung");

		upobj = upl.getFirstPostingObj();
		int anz = upl.getSize();
		for (int i = 0; i < anz - 1; i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, String.valueOf(i));
			item.setText(1, upobj.getThreadname());
			int tid = upobj.getThreadid();
			item.setText(2, String.valueOf(tid));

			ThreadDbObj tdbo = tdb.SearchThreadid(tid);
			if (tdbo != null)
			{
				if (extendedflag == 1)
				{
					int useranzp = pdb.calcAnzPostings(tdbo, udb, 0022, unam);
					item.setText(3, String.valueOf(useranzp));
					pdb.CleanPostDB();
				} else
				{
					item.setText(3, "no Info");
				}
			} else
				item.setText(3, "tdbo=0");

			item.setText(4, upobj.getAufnahmeDatum());
			item.setText(5, upobj.getLastposting());
			
			if ((tdbo!=null)&&(extendedflag == 1))
			{
				int mid=tdbo.getMasterid();
				String fnam=GC.rootpath+"\\db\\kursicons\\"+mid+"_1y.png";
				

				if(FileAccess.FileAvailable(fnam)==false)
				{
					item.setText(6, "Kein Bild geladen");
					break;
				}
				
				Image img = new Image(dis,
						fnam);
				int width = img.getBounds().width;
				int height = img.getBounds().height;	
				
				final Image scaled030 = new Image(dis, img.getImageData()
						.scaledTo((int) (width ), (int) (height * 0.35)));

				item.setImage(6, scaled030);
			}
			else
				item.setText(6, "Bild");
			
			item.setText(7, String.valueOf(udbo.getManbewertung()));
			upobj = upl.getNextPostingObj();
		}

		for (int i = 0; i < 8; i++)
		{
			table.getColumn(i).pack();
		}
		table.setBounds(20, 20, 800, 500);
	}
}
