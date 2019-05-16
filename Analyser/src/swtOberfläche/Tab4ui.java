package swtOberfläche;

import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;

import mainPackage.GC;
import objects.UserDbObj;

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

import stores.ThreadsDB;
import stores.UserDB;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Tab4ui
{
	static UserDB udb_glob = null;
	
	static List ulist_glob = null;
	static Group groupListe_glob = null;
	static private HashMap<Integer, String> map_glob = new HashMap<Integer, String>();
	static private String selUsername_glob = null;
	static private Display display_glob =null;
	static private ThreadsDB tdb_glob=null;
	static private Button button1extuserinfo;
	static private Button button1zeigeuserpostings;
	
	final static private String[] checkinfo_glob =
	{ "Handdata", "OBSERVE", "PROGNO", "B4" };

	static private void BuildUserliste(Group groupListe, int x, int y,
			int breite, int hoehe, Tab4selFilter self, final Text USERANZAHL)
	{
		Handdata hand=new Handdata("userinfo");
		
		
		if (ulist_glob == null)
			ulist_glob = new List(groupListe, SWT.BORDER | SWT.MULTI
					| SWT.V_SCROLL);

		// hole den prefix
		String pref = self.getPrefix().toLowerCase();
		int fanz = 0;

		// userliste aufbauen
		int anz = udb_glob.GetanzObj();
		ulist_glob.setBounds(x, y, breite, hoehe);
		Tracer.WriteTrace(20, "Info: pref<" + pref + "> attrib<"
				+ self.getPrefix() + ">");

		for (int i = 0; i < anz; i++)
	

		{
			UserDbObj udbo = (UserDbObj) udb_glob.GetObjectIDX(i);
			String unam = udbo.get_username().toLowerCase();

			// Den Filter bei der userlistenauswahl beachten
			if (unam.contains(pref))
			{
				String attrib = self.getAttrib();
				// hier wird geprüft ob nur user mit vorhandenen handdata
				// angezeigt werden
				if ((attrib != null) && (attrib.contains("Handdata") == true))
				{
					if(hand.HasHanddata(unam)==false)
						continue;
				}
				if ((attrib != null) && (attrib.contains("PROGNO") == true))
				{
					if(hand.HasPrognose(unam)==false)
					  continue;
				}

				if ((attrib != null) && (attrib.contains("OBSERVE") == true))
				{
					if (udbo.getMode() != 8000)
						continue;
				}
				ulist_glob.add(i + "<" + udbo.get_username() + "> ");
				map_glob.put(fanz, udbo.get_username());
				fanz++;
			}
		}
		USERANZAHL.setText(String.valueOf(fanz));
	}

	static Button SetCheckbottons(TabFolder folder, Listener listener,
			Group groupListe, int x, int y, int breite, int hoehe)
	{
		Button button = null;
		// check button (Button für bestimmte Filter)
		//groupListe.setLayout(new RowLayout(SWT.HORIZONTAL));
		for (int i = 0; i < 4; i++)
		{
			button = new Button(groupListe, SWT.CHECK);
			button.setText(checkinfo_glob[i]);
			button.setBounds(250 + (i * x), y, breite, hoehe);
			button.addListener(SWT.Selection, listener);
		}
		{
			button1zeigeuserpostings = new Button(groupListe_glob, SWT.PUSH | SWT.CENTER);
			button1zeigeuserpostings.setText("Zeige Userpostinguebersicht");
			button1zeigeuserpostings.setBounds(999, 156, 199, 30);
			button1zeigeuserpostings.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) 
				{
					button1zeigeuserpostingsWidgetSelected(evt);
				}
			});
		}
		{
			button1extuserinfo = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
			button1extuserinfo.setText("Extended Userinfo");
			button1extuserinfo.setBounds(592, 580, 134, 20);
		}
		return button;
	}

	static private void ActionRefresh(Group groupListe, int x, int y,
			int breite, int hoehe, Tab4selFilter self, final Text USERANZAHL,
			final Text FILTER1)
	{
		// Die Filtertaste wurde betätigt
		// hole, den prefix und erstelle die Userliste neu

		String selt=FILTER1.getText();
		self.setPrefix(selt);
		System.out.println("text=" + self.getPrefix());
		BuildUserliste(groupListe, x, y, breite, hoehe, self, USERANZAHL);
	}

	static private void ActionUserelementAusgewaehlt(String msgUsername,
			final Text t, final Text M1)
	{
		Handdata hand= new Handdata("userinfo");
		// Baue Userinfo auf
	
		// Ein User wurde ausgewählt
		System.out.println("pos=" + msgUsername);

		// die position holen
		String msgpos = msgUsername.substring(msgUsername.indexOf("{") + 1,
				msgUsername.indexOf("}") - 1);
		int pos = Tools.get_zahl(msgpos);

		String name = map_glob.get(pos);
		M1.setText("username<" + name + ">");

		selUsername_glob=new String(name);
		hand.anzeigeHanddata(t, name);
	}

	static private Listener ActionCheckboxGedrueckt(
			final Tab4selFilter selfilter)
	{
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

	static private void ActionSaveButtonGedrueckt(final Text t)
	{
		Handdata hand=new Handdata("userinfo");
		hand.speichereHanddata(t, selUsername_glob);
	}

	static private void cleanUlist()
	{
		ulist_glob.removeAll();
		// altes map zeug löschen
		map_glob.clear();
	}

	static public void init(TabFolder folder, TabItem tab,Display dis,UserDB udb,ThreadsDB tdb)
	{
		final Tab4selFilter self = new Tab4selFilter();
		display_glob=dis;
		udb_glob=udb;
		// UserListGroup erstellen
		groupListe_glob = new Group(folder, SWT.NONE);
		groupListe_glob.setText("Userinfo");
		groupListe_glob.setSize(1204, 800);
		Display display_glob =null;
		tdb_glob=tdb;
		tab.setControl(groupListe_glob);
		// Inhalt des Group erstellen
		Label group = new Label(groupListe_glob, SWT.NONE);
		group.setText("Liste: ");

		// Listener für die checkbuttons
		Listener listener = ActionCheckboxGedrueckt(self);

		// check button (Button für bestimmte Filter)
		SetCheckbottons(folder, listener, groupListe_glob, 80, 40, 80, 30);

		// username
		final Text M1 = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI);
		M1.setBounds(230, 80, 400, 50);
		M1.setText("Username");

		// Ausgabefenster (Editorfeld etc..)
		final Text Ausgabe = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		Ausgabe.setBounds(236, 156, 744, 400);

		// Protofenster
		Text Proto = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		Proto.setBounds(20, 700, 600, 100);
		Proto.setText("Rootpath=<" + GC.rootpath + ">");

		// useranzahl
		final Text USERANZAHL = new Text(groupListe_glob, SWT.BORDER
				| SWT.MULTI);
		USERANZAHL.setBounds(20, 560, 100, 20);
		USERANZAHL.setText("??");

		// userliste
		BuildUserliste(groupListe_glob, 20, 20, 200, 530, self, USERANZAHL);
		initListenerUserliste(M1, Ausgabe);

		// Selektor
		final Text FILTER1 = new Text(groupListe_glob, SWT.BORDER | SWT.MULTI);
		FILTER1.setBounds(20, 600, 100, 20);
		FILTER1.setText("");
		self.setPrefix("");

		// Refresh Button gedrückt
		Button refreshbutton = new Button(groupListe_glob, SWT.PUSH);
		refreshbutton.setText("Refresh");
		refreshbutton.setBounds(140, 600, 50, 20);
		refreshbutton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				if (ulist_glob != null)
					cleanUlist();

				ActionRefresh(groupListe_glob, 20, 20, 200, 530, self,
						USERANZAHL, FILTER1);
				initListenerUserliste(M1, Ausgabe);
				System.out.println("Refresh Butten gedrückt");
			}
		});

		// ok button
		Button ok = new Button(groupListe_glob, SWT.PUSH);
		ok.setText("Save");
		ok.setBounds(932, 580, 50, 20);
		ok.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				System.out.println("Safe Button gedrückt OK");
				ActionSaveButtonGedrueckt(Ausgabe);
			}
		});
		// Userinfo button
		Button userinfo = new Button(groupListe_glob, SWT.PUSH);
		userinfo.setText("Show Userinfo");
		userinfo.setBounds(732, 580, 120, 20);
		userinfo.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				System.out.println("Userinfo Button gedrückt OK");
				ActionUserinfoGedrueckt();
			}
		});
		// userpostings button
		Button userpostings = new Button(groupListe_glob, SWT.PUSH);
		userpostings.setText("Show Userpostings");
		userpostings.setBounds(732, 600, 120, 20);
		userpostings.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				System.out.println("Userpostings Button gedrückt OK");
				ActionUserPostingsGedrueckt();
			}
		});
		button1extuserinfo.setSelection(true);
	}

	private static void initListenerUserliste(final Text M1, final Text Ausgabe)
	{
		ulist_glob.addListener(SWT.Selection, new Listener()
		{
			// listenelement gewählt
			public void handleEvent(Event e)
			{
				String string = "";
				int[] selection = ulist_glob.getSelectionIndices();

				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				String msg = "Selection={" + string + "}";
				System.out.println(msg);
				M1.setText(msg);
				ActionUserelementAusgewaehlt(msg, Ausgabe, M1);
			}
		});
	}
	static private void ActionUserinfoGedrueckt()
	{
		int extflag=0;
		Tab4uiShowExternalUserinfo se= new Tab4uiShowExternalUserinfo();
		if(button1extuserinfo.getSelection()==true)
			extflag=1;
		se.viewTableExt(display_glob, selUsername_glob,udb_glob,tdb_glob,extflag);
	}
	static private void ActionUserPostingsGedrueckt()
	{
		Tab4uiShowExternalUserpostings se= new Tab4uiShowExternalUserpostings();
		se.viewTableExt(display_glob, selUsername_glob,udb_glob,tdb_glob);
	}
	
	static private void button1zeigeuserpostingsWidgetSelected(SelectionEvent evt) 
	{
		//Button: Zeige userpostingübersicht
		System.out.println("button1zeigeuserpostings.widgetSelected, event="+evt);
		
	}

}
