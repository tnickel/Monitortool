package analyserSwtInterfaceDEP;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Sys;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;

import mainPackage.GC;
import objects.UserDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
public class CompositeMain extends org.eclipse.swt.widgets.Composite
{
	UserDB udb_glob = new UserDB("observeuser.txt", "boostraenge.txt");
	TabselFilter self_glob = new TabselFilter();
	static private HashMap<Integer, String> map_glob = new HashMap<Integer, String>();
	private Composite groupListe_glob;
	static private String selUsername_glob = null;

	private CTabFolder cTabFolder1;
	private Composite cTabItem1;
	private Text text1;
	private Text text3;
	private Button buttonHanddata;
	private Composite cTabItem3;
	private CTabItem cTabItem6;
	private Composite cTabItem2;
	private CTabItem cTabItem5;
	private Button buttonSave;
	private Text text9;
	private Text text8;
	private Button button4;
	private Button button3;
	private Button button2;
	private Text text7;
	private Button button1;
	private Text text6;
	private Text text5;
	private List list2;
	private Button buttonPrognose;
	private Button buttonObserve;
	private Text text4;
	private Text text2; 
	private Button button1refresh;
	private Text text2filter; //dies ist der filter
	private List list1;
	private CTabItem cTabItem4;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{
		showGUI();
	}

	protected void checkSubclass()
	{
	}

	public static void showGUI()
	{
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		CompositeMain inst = new CompositeMain(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if (size.x == 0 && size.y == 0)
		{
			inst.pack();
			shell.pack();
		} else
		{
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	public CompositeMain(org.eclipse.swt.widgets.Composite parent, int style)
	{
		super(parent, style);
		initGUI();
	}
	private void initGUI()
	{
		try
		{
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{  
				//erster Folder *********
				cTabFolder1 = new CTabFolder(this, SWT.NONE);
				FormData cTabFolder1LData = new FormData();
				cTabFolder1LData.width = 892;
				cTabFolder1LData.height = 800;
				cTabFolder1LData.left = new FormAttachment(0, 1000, 0);
				cTabFolder1LData.top = new FormAttachment(0, 1000, 21);
				cTabFolder1.setLayoutData(cTabFolder1LData);
				{
					cTabItem4 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem4.setText("Userinfo");
					{
						cTabItem1 = new Composite(cTabFolder1, SWT.NONE);
						cTabItem4.setControl(cTabItem1);
						cTabItem1.setLayout(null);
						{
							list1 = new List(cTabItem1, SWT.H_SCROLL
									| SWT.V_SCROLL);
							list1.setBounds(0, 14, 273, 588);
							list1.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									Tab1list1WidgetSelected(evt);
								}
							});
						}
						{
							text1 = new Text(cTabItem1, SWT.NONE);
							text1.setText("text1");
							text1.setBounds(7, 616, 67, 21);
						}
						{
							text2filter = new Text(cTabItem1, SWT.NONE);
							text2filter.setText("");
							text2filter.setBounds(7, 644, 154, 28);
						}
						{
							button1refresh = new Button(cTabItem1, SWT.PUSH
									| SWT.CENTER);
							button1refresh.setText("Refresh");
							button1refresh.setSize(60, 30);
							button1refresh.setBounds(210, 644, 63, 28);
							button1refresh
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											Tab1button1refreshWidgetSelected(evt);
										}
									});
						}
						{
							text2 = new Text(cTabItem1, SWT.MULTI | SWT.WRAP);
							text2.setText("");
							text2.setBounds(287, 63, 504, 63);
						}
						{
							text3 = new Text(cTabItem1, SWT.MULTI | SWT.WRAP);
							text3.setText("text3");
							text3.setBounds(287, 140, 504, 462);
						}
						{
							text4 = new Text(cTabItem1, SWT.MULTI | SWT.WRAP);
							text4.setText("");
							text4.setBounds(7, 693, 392, 119);
						}
						{
							buttonHanddata = new Button(cTabItem1, SWT.CHECK
									| SWT.LEFT);
							buttonHanddata.setText("Handdata");
							buttonHanddata.setBounds(287, 14, 98, 28);
							buttonHanddata
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											Tab1buttonHanddataWidgetSelected(evt);
										}
									});
						}
						{
							buttonObserve = new Button(cTabItem1, SWT.CHECK
									| SWT.LEFT);
							buttonObserve.setText("Observe");
							buttonObserve.setBounds(392, 14, 98, 28);
							buttonObserve
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											Tab1buttonObserveWidgetSelected(evt);
										}
									});
						}
						{
							buttonPrognose = new Button(cTabItem1, SWT.CHECK
									| SWT.LEFT);
							buttonPrognose.setText("Prognose");
							buttonPrognose.setBounds(490, 14, 98, 28);
							buttonPrognose
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											Tab1buttonPrognoseWidgetSelected(evt);
										}
									});
						}
						{
							buttonSave = new Button(cTabItem1, SWT.PUSH
									| SWT.CENTER);
							buttonSave.setText("Save");
							buttonSave.setBounds(728, 616, 63, 28);
							buttonSave.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									Tab1buttonSaveWidgetSelected(evt);
								}
							});
						}
					}
				}
				{  
					//zweiter folder *************
					cTabItem5 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem5.setText("Midinfo");
					{
						cTabItem2 = new Composite(cTabFolder1, SWT.NONE);
						cTabItem5.setControl(cTabItem2);
						GridLayout cTabItem2Layout = new GridLayout();
						cTabItem2Layout.makeColumnsEqualWidth = true;
						cTabItem2.setLayout(cTabItem2Layout);
						{
							groupListe_glob = new Composite(cTabItem2, SWT.NONE);
							GridData groupListe_globLData = new GridData();
							groupListe_globLData.widthHint = 881;
							groupListe_globLData.heightHint = 792;
							groupListe_glob.setLayoutData(groupListe_globLData);
							groupListe_glob.setLayout(null);
							{
								list2 = new List(groupListe_glob, SWT.H_SCROLL | SWT.V_SCROLL);
								list2.setBounds(0, 0, 314, 483);
							}
							{
								text5 = new Text(groupListe_glob, SWT.NONE);
								text5.setText("text5");
								text5.setBounds(12, 504, 103, 30);
							}
							{
								text6 = new Text(groupListe_glob, SWT.NONE);
								text6.setText("text5");
								text6.setBounds(12, 556, 103, 30);
							}
							{
								button1 = new Button(groupListe_glob, SWT.PUSH | SWT.CENTER);
								button1.setText("Refresh");
								button1.setBounds(134, 556, 60, 30);
							}
							{
								text7 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
								text7.setText("hallo\n");
								text7.setBounds(7, 693, 392, 119);
							}
							{
								button2 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
								button2.setText("Prognose");
								button2.setBounds(506, 14, 98, 28);
							
								
							}
							{
								button3 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
								button3.setText("Observe");
								button3.setBounds(425, 14, 98, 28);
								
								
							}
							{
								button4 = new Button(groupListe_glob, SWT.CHECK | SWT.LEFT);
								button4.setText("Handdata");
								button4.setBounds(337, 14, 98, 28);
								
									
								
							}
							{
								text8 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
								text8.setText("...........");
								text8.setBounds(336, 110, 504, 373);
							}
							{
								text9 = new Text(groupListe_glob, SWT.MULTI | SWT.WRAP);
								text9.setText("username ");
								text9.setBounds(336, 41, 504, 63);
							}
						}
					}
				}
				{
					cTabItem6 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem6.setText("Prognosenbrowser");
					{
						cTabItem3 = new Composite(cTabFolder1, SWT.NONE);
						cTabItem6.setControl(cTabItem3);
						GridLayout cTabItem3Layout = new GridLayout();
						cTabItem3Layout.makeColumnsEqualWidth = true;
						cTabItem3.setLayout(cTabItem3Layout);
					}
				}
				cTabFolder1.setSelection(0);
			}
			pack();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void Tab1button1refreshWidgetSelected(SelectionEvent evt)
	{
		//Refresh-Button gesetzt
		System.out.println("button1refresh.widgetSelected, event=" + evt);
		// TODO add your code for button1refresh.widgetSelected
		Tab1BuildUserliste(self_glob);
		text4.setText("hallo\n");
	}

	private void Tab1BuildUserliste(TabselFilter self)
	{
		String hostname = Sys.getHostname();
		Tab1cleanUlist();
		// hole den prefix
		String ftext=text2filter.getText();
		self.setPrefix(ftext);
		String pref = self.getPrefix().toLowerCase();
		int fanz = 0;

		// userliste aufbauen
		int anz = udb_glob.GetanzObj();

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
					String fnam = GC.rootpath + "\\handdata\\Rechnername "
							+ hostname + "\\userinfo\\" + unam + ".txt";
					if (FileAccess.FileAvailable(fnam) == false)
						continue;
				}
				if ((attrib != null) && (attrib.contains("Prognose") == true))
				{
					String fnam = GC.rootpath + "\\handdata\\Rechnername "
							+ hostname + "\\userinfo\\" + unam + ".txt";
					if (FileAccess.FileAvailable(fnam) == false)
						continue;
					else
					{
						// prüft nach ob das Schlüsselwort prognose im text
						// vorkommt
						if (FileAccess.CheckFileKeyword(fnam, "Prognose",0) == false)
							continue;
					}
				}
				if ((attrib != null) && (attrib.contains("Observe") == true))
				{
					if (udbo.getMode() != 8000)
						continue;
				}
				list1.add(i + "<" + udbo.get_username() + "> ");
				map_glob.put(fanz, udbo.get_username());
				fanz++;
			}
		}
		text1.setText(String.valueOf(fanz));
	}

	private void Tab1list1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("list1.widgetSelected, event=" + evt);

		String string = "";
		int[] selection = list1.getSelectionIndices();

		for (int i = 0; i < selection.length; i++)
			string += selection[i] + " ";
		String msg = "Selection={" + string + "}";
		System.out.println(msg);
		text2.setText(msg);
		Tab1ActionUserelementAusgewaehlt(msg, text3, text2);
	}

	private void Tab1ActionUserelementAusgewaehlt(String msgUsername, final Text t,
			final Text M1)
	{
		// Baue Userinfo auf
		// t: das ist das dicke Feld wo die Infomationen eingegeben werden
		//M1: ist das Infofeld wo zum User weitere Infos angegeben werden
		
		String zeile = null;
		// Ein User wurde ausgewählt
		System.out.println("pos=" + msgUsername);
		String hostname = Sys.getHostname();

		t.selectAll();
		t.cut();
		t.clearSelection();

		// die position holen
		String msgpos = msgUsername.substring(msgUsername.indexOf("{") + 1,
				msgUsername.indexOf("}") - 1);
		int pos = Tools.get_zahl(msgpos);

		String name = map_glob.get(pos);
		M1.setText("username<" + name + ">");

		String fnam = GC.rootpath + "\\handdata\\Rechnername " + hostname
				+ "\\userinfo\\" + name + ".txt";
		selUsername_glob = new String(name);

		if (FileAccess.FileAvailable0(fnam) == true)
		{
			Inf inf = new Inf();
			
			while ((zeile = inf.readZeile(fnam)) != null)
			{
				//System.out.println("zeile=<" + zeile + ">");
				t.append(zeile);
				t.append("\n");
			}
			inf.close();
		} else
			t.setText("...........");
	}

	static private void Tab1ActionSaveButtonGedrueckt(final Text t)
	{
		String hostname = Sys.getHostname();
		// Save Button gedrückt
		String fnam = GC.rootpath + "\\handdata\\Rechnername " + hostname
				+ "\\userinfo\\" + selUsername_glob + ".txt";
		String fnams = GC.rootpath + "\\handdata\\Rechnername " + hostname
				+ "\\userinfo\\" + selUsername_glob + ".txt.sav";

		// Den save löschen
		if (FileAccess.FileAvailable(fnams))
			FileAccess.FileDelete(fnams,0);

		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileMove(fnam, fnams);

		Inf inf = new Inf();
		inf.setFilename(fnam);
		String zeile = t.getText();
		inf.writezeile(zeile);
	}

	private void Tab1cleanUlist()
	{
		list1.removeAll();
		// altes map zeug löschen
		map_glob.clear();
	}

	private void Tab1buttonHanddataWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttonHanddata.widgetSelected, event=" + evt);
		String st = Swttool.holeButtonEventText(evt);
		self_glob.toggleAttrib(st);
	}

	private void Tab1buttonObserveWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttonObserve.widgetSelected, event=" + evt);
		String st = Swttool.holeButtonEventText(evt);
		self_glob.toggleAttrib(st);
	}

	private void Tab1buttonPrognoseWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttonPrognose.widgetSelected, event=" + evt);
		String st = Swttool.holeButtonEventText(evt);
		self_glob.toggleAttrib(st);
	}
	
	private void Tab1buttonSaveWidgetSelected(SelectionEvent evt) 
	{
		System.out.println("buttonSave.widgetSelected, event="+evt);
		//Speichert ds dicke Textfeld t
		Tab1ActionSaveButtonGedrueckt(text3);
	}
	
	
	
	
}
