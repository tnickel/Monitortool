package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;

import java.util.HashMap;

import mainPackage.GC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

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
public class Tab6ui
{
	static List fileliste_glob = null;
	static Group groupListe_glob = null;
	static TabFolder folder_glob = null;
	static Browser browser_glob = null;
	final static private String[] checkinfo_glob =
	{ "Handdata", "Nur1MID", "B3", "B4" };
	static private HashMap<Integer, String> filemap_glob = new HashMap<Integer, String>();

	static public void init(TabFolder folder, TabItem tab)
	{
		// folder:Hauptfenster
		// tab:Unterpunkt

		// folder.setLayout(new GridLayout());
		Group groupListe_glob = new Group(folder, SWT.NONE);
		groupListe_glob.setText("Allgemein");
		tab.setControl(groupListe_glob);

		String html = "<HTML><HEAD><TITLE>HTML Test</TITLE></HEAD><BODY>";
		for (int i = 0; i < 100; i++)
			html += "<P>This is line " + i + "</P>";
		html += "</BODY></HTML>";

		Button refreshbutton = new Button(groupListe_glob, SWT.PUSH);
		refreshbutton.setText("Refresh");
		refreshbutton.setBounds(20, 550, 80, 30);
		try
		{
			browser_glob = new Browser(groupListe_glob, SWT.H_SCROLL);

		} catch (SWTError e)
		{
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			// display.dispose();
			return;
		}
		browser_glob.setText(html);
		browser_glob.setBounds(240, 20, 1071, 800);

		// Fileliste
		BuildFileliste(groupListe_glob, 20, 20, 200, 530);
		initListenerFileliste();
	}

	static private void BuildFileliste(Group groupListe, int x, int y,
			int breite, int hoehe)
	{
		if (fileliste_glob == null)
			fileliste_glob = new List(groupListe, SWT.BORDER | SWT.MULTI
					| SWT.V_SCROLL | SWT.H_SCROLL);

		fileliste_glob.setBounds(x, y, breite, hoehe);

		// Baue Prognosen UserThread
		String dirpath = GC.rootpath + "\\db\\UserThreadVirtualKonto";
		FileAccess.initFileSystemList(dirpath, 1);
		int anz = FileAccess.holeFileAnz();
		int laufz = 0;
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();

			if (fnam.endsWith(".html") == true)
			{
				fileliste_glob.add("<" + fnam + "> ");
				filemap_glob.put(laufz, fnam);
				laufz++;
			}
		}

		// add Prognosen aus Prognosen
		dirpath = GC.rootpath + "\\db\\UserThreadVirtualKonto\\prognosen";
		FileAccess.initFileSystemList(dirpath, 1);
		anz = FileAccess.holeFileAnz();
		int endwert = laufz + anz;

		for (int i = laufz; i < endwert; i++)
		{
			String fnam = FileAccess.holeFileSystemName();

			if (fnam.endsWith(".html") == true)
			{
				fileliste_glob.add("<" + fnam + "> ");
				filemap_glob.put(laufz, fnam);
				laufz++;
			}
		}
	}

	private static void initListenerFileliste()
	{
		fileliste_glob.addListener(SWT.Selection, new Listener()
		{
			// listenelement gewählt
			public void handleEvent(Event e)
			{
				String string = "";
				int[] selection = fileliste_glob.getSelectionIndices();

				for (int i = 0; i < selection.length; i++)
					string += selection[i];

				System.out.println(string);
				ActionFileelementAusgewaehlt(string);
			}
		});
	}

	static private void ActionFileelementAusgewaehlt(String msgUsername)
	{
		GridData data = new GridData();
		data.horizontalSpan = 3;

		// Ein User wurde ausgewählt
		System.out.println("pos=" + msgUsername);

		// die position holen
		int pos = Tools.get_zahl(msgUsername);

		String filenam = filemap_glob.get(pos);

		// ermittle die mid zu den threadnamen
		// Baut mit dieser Seite ne Htmlseite auf

		Inf inf = new Inf();
		if ((filenam.contains("p_38200")) || (filenam.contains("p_threads")))
			inf.setFilename(GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\prognosen\\" + filenam);
		else
			inf.setFilename(GC.rootpath + "\\db\\UserThreadVirtualKonto\\"
					+ filenam);
		String mem = inf.readMemFile();
		browser_glob.setText(mem);
	}
}
