package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Sys;
import hilfsklasse.Tools;

import java.util.HashMap;

import mainPackage.GC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import swtViewer.ViewExternFileTable;

public class Tab2ui
{
	static private List Configliste;
	static private String selConfig_glob = null;
	static private HashMap<Integer, String> map_glob = new HashMap<Integer, String>();
	static private Display display = null;

	static public void init(TabFolder folder, TabItem tab, Display dis)
	{
		display = dis;
		// ProxyGroup erstellen
		Group groupProxy = new Group(folder, SWT.NONE);
		groupProxy.setText("config");

		GridLayout layoutProxy = new GridLayout();
		layoutProxy.numColumns = 2;
		groupProxy.setLayout(layoutProxy);

		tab.setControl(groupProxy);
		// Inhalt des Group erstellen

		groupProxy.pack();
		groupProxy.setSize(640, 468);
		{
			GridData ConfiglisteLData = new GridData();
			ConfiglisteLData.widthHint = 196;
			ConfiglisteLData.heightHint = 228;
			Configliste = new List(groupProxy, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER);
			Configliste.setLayoutData(ConfiglisteLData);
			initListenerConfigliste();
		}
		BuildConfigliste(Configliste);
		groupProxy.pack();
		folder.pack();
	}

	private static void BuildConfigliste(List cl)
	{
		FileAccess.initFileSystemList(GC.rootpath + "\\db", 1);
		int anz = FileAccess.holeFileAnz();
		int fanz = 0;

		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.endsWith(".db") == true)
			{
				cl.add(fnam);
				map_glob.put(fanz, fnam);
				fanz++;
			}
		}
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto", 1);
		anz = FileAccess.holeFileAnz();

		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if ((fnam.endsWith(".txt") == true)
					&& (fnam.contains("Wochengewinne") == true))
			{
				cl.add(fnam);
				map_glob.put(fanz, fnam);
				fanz++;
			}
		}
	}

	private static void initListenerConfigliste()
	{
		//Doppelklick auf listenelement
		Configliste.addListener(SWT.Selection, new Listener()
		{
			
			public void handleEvent(Event e)
			{
				String string = "";
				int[] selection = Configliste.getSelectionIndices();

				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				String msg = "Selection={" + string + "}";
				System.out.println(msg);

				ActionConfigelementAusgewaehlt(msg);
			}
		});
	}

	static private void ActionConfigelementAusgewaehlt(String msgUsername)
	{
		// Baue Userinfo auf
		String zeile = null;
		// Ein Eintrag wurde ausgewählt
		System.out.println("pos=" + msgUsername);
		String hostname = Sys.getHostname();

		// die position holen
		String msgpos = msgUsername.substring(msgUsername.indexOf("{") + 1,
				msgUsername.indexOf("}") - 1);
		int pos = Tools.get_zahl(msgpos);

		String fname = map_glob.get(pos);
		String name = null;

		if (fname.contains("Wochengewinne") == false)
			name = GC.rootpath + "\\db\\" + fname;
		else
			name = GC.rootpath + "\\db\\UserThreadVirtualKonto\\" + fname;

		selConfig_glob = new String(name);
		System.out.println("fnam=<" + name + ">");

		ViewExternFileTable viewer= new ViewExternFileTable();
		viewer.ShowTable(display, name,"",null);
	}

}
