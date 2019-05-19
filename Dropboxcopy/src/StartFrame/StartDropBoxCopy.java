package StartFrame;






import com.cloudgarden.resource.SWTResourceManager;

import data.DropGlobalVar;
import data.DropRootpath;
import ftool.DropUpdater;
import gui.Mbox;
import hilfsklasse.Tracer;
import mtools.Mlist;
import swtDropwindow.EditConfig;

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
public class StartDropBoxCopy extends org.eclipse.swt.widgets.Composite
{

	private Menu menu1;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private ProgressBar progressBar1;
	private int progresscount = 0;
	private Button scan;
	private Table table1;
	private Button stop;
	private MenuItem update;
	private Button buttonCleanMt;
	private Button restartmetatrader;
	private Button button1exporteas;
	private Button installeas;
	private List list1;
	private Button refresh;
	private Button save;
	private Button start;
	private MenuItem saveFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private int loopflag = 0;
	private StartDropBoxCopyWork swork = new StartDropBoxCopyWork();

	Mlist mlist = null;
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public StartDropBoxCopy(Composite parent, int style)
	{
		super(parent, style);
		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI()
	{
		try
		{

			this.setSize(1256, 473);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				buttonCleanMt = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData buttonCleanMtLData = new FormData();
				buttonCleanMtLData.left =  new FormAttachment(0, 1000, 283);
				buttonCleanMtLData.top =  new FormAttachment(0, 1000, 433);
				buttonCleanMtLData.width = 137;
				buttonCleanMtLData.height = 20;
				buttonCleanMt.setLayoutData(buttonCleanMtLData);
				buttonCleanMt.setText("clean Metatrader");
				buttonCleanMt.setToolTipText("Flag ist gesetzt:\r\nFalls dieses flag gesetzt ist werden alle *.mql *.chr *.ex4 vor der installation im MT gelöscht.\r\n\r\nFlag ist nicht gesetzt:\r\nIst dieses Flag nicht gesetzt wird nur überinstalliert. D.h. Es kommen nur neue EA´s hinzu . Die alten EA´s bleiben bestehen.\r\nMqlcache.dat und die *.ex4 der neuen EAs werden aber gelöscht.");
			}
			{
				restartmetatrader = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData restartmetatraderLData = new FormData();
				restartmetatraderLData.left =  new FormAttachment(0, 1000, 590);
				restartmetatraderLData.top =  new FormAttachment(0, 1000, 431);
				restartmetatraderLData.width = 150;
				restartmetatraderLData.height = 30;
				restartmetatrader.setLayoutData(restartmetatraderLData);
				restartmetatrader.setText("RestartAllMetatrader");
				restartmetatrader.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						restartmetatraderWidgetSelected(evt);
					}
				});
			}
			{
				button1exporteas = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1exporteasLData = new FormData();
				button1exporteasLData.left =  new FormAttachment(0, 1000, 450);
				button1exporteasLData.top =  new FormAttachment(0, 1000, 433);
				button1exporteasLData.width = 79;
				button1exporteasLData.height = 30;
				button1exporteas.setLayoutData(button1exporteasLData);
				button1exporteas.setText("ExportEAs");
				button1exporteas.setToolTipText("die EAs werden aus dem Metatraderverzeichniss in die Dropbox exportiert");
				button1exporteas.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1exporteasWidgetSelected(evt);
					}
				});
			}
			{
				installeas = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData installeasLData = new FormData();
				installeasLData.left =  new FormAttachment(0, 1000, 157);
				installeasLData.top =  new FormAttachment(0, 1000, 431);
				installeasLData.width = 120;
				installeasLData.height = 30;
				installeas.setLayoutData(installeasLData);
				installeas.setText("Install/DeleteEas");
				installeas.setToolTipText("Die EA´s werden aus der Dropbox in das Metatraderverzeichniss installiert. Falls ein EA installiert wird wird das alte *.ex4 vorher gelöscht");
				installeas.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						installeasWidgetSelected(evt);
					}
				});
			}
			{
				FormData list1LData = new FormData();
				list1LData.left = new FormAttachment(0, 1000, 583);
				list1LData.top = new FormAttachment(0, 1000, 12);
				list1LData.width = 619;
				list1LData.height = 339;
				list1 = new List(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				list1.setLayoutData(list1LData);
			}
			{
				refresh = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showLData = new FormData();
				showLData.left = new FormAttachment(0, 1000, 90);
				showLData.top = new FormAttachment(0, 1000, 431);
				showLData.width = 61;
				showLData.height = 30;
				refresh.setLayoutData(showLData);
				refresh.setText("refresh");
				refresh.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showWidgetSelected(evt);
					}
				});
			}
			{
				save = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData saveLData = new FormData();
				saveLData.left = new FormAttachment(0, 1000, 1189);
				saveLData.top = new FormAttachment(0, 1000, 431);
				saveLData.width = 41;
				saveLData.height = 30;
				save.setLayoutData(saveLData);
				save.setText("save");
				save.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						saveWidgetSelected(evt);
					}
				});
			}
			{
				FormData progressBar1LData = new FormData();
				progressBar1LData.left = new FormAttachment(0, 1000, 24);
				progressBar1LData.width = 1206;
				progressBar1LData.height = 21;
				progressBar1LData.top = new FormAttachment(0, 1000, 376);
				progressBar1 = new ProgressBar(this, SWT.NONE);
				progressBar1.setLayoutData(progressBar1LData);
			}
			{
				scan = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData scanLData = new FormData();
				scanLData.left = new FormAttachment(0, 1000, 24);
				scanLData.top = new FormAttachment(0, 1000, 431);
				scanLData.width = 60;
				scanLData.height = 30;
				scan.setLayoutData(scanLData);
				scan.setText("scan");
				scan.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						scanWidgetSelected(evt);
					}
				});
			}
			{
				FormData table1LData = new FormData();
				table1LData.left = new FormAttachment(0, 1000, 24);
				table1LData.top = new FormAttachment(0, 1000, 12);
				table1LData.width = 528;
				table1LData.height = 339;
				table1 = new Table(this, SWT.CHECK | SWT.FULL_SELECTION
						| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				table1.setLayoutData(table1LData);
				table1.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						table1WidgetSelected(evt);
					}
				});
			}
			{
				stop = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData stopLData = new FormData();
				stopLData.left = new FormAttachment(0, 1000, 1018);
				stopLData.top = new FormAttachment(0, 1000, 403);
				stopLData.width = 93;
				stopLData.height = 30;
				stop.setLayoutData(stopLData);
				stop.setText("stop");
				stop.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						stopWidgetSelected(evt);
					}
				});
			}
			{
				start = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData startLData = new FormData();
				startLData.left = new FormAttachment(0, 1000, 921);
				startLData.top = new FormAttachment(0, 1000, 403);
				startLData.width = 91;
				startLData.height = 30;
				start.setLayoutData(startLData);
				start.setText("start");
				start.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						startWidgetSelected(evt);
					}
				});
			}
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("Conf");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							saveFileMenuItem = new MenuItem(fileMenu,
									SWT.CASCADE);
							saveFileMenuItem.setText("set dropbox config");
							saveFileMenuItem
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											setDropboxconfig(evt);
										}
									});
						}
						{
							update = new MenuItem(fileMenu, SWT.PUSH);
							update.setText("update");
							update.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									updateWidgetSelected(evt);
								}
							});
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					helpMenuItem = new MenuItem(menu1, SWT.CASCADE);
					helpMenuItem.setText("Help");
					{
						helpMenu = new Menu(helpMenuItem);
						{
							contentsMenuItem = new MenuItem(helpMenu,
									SWT.CASCADE);
							contentsMenuItem.setText("Contents");
						}
						{
							aboutMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							aboutMenuItem.setText("About");
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
			init();
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	final Runnable timer = new Runnable()
	{
		public void run()
		{
			//loop-schleife
			swork.loop();
			// System.out.println("timer");
			if (loopflag == 1)
				Display.getDefault().timerExec(5000, this);
		}
	};

	final Runnable balkenlauf = new Runnable()
	{
		public void run()
		{

			laufBalken();

			if (loopflag == 1)
				Display.getDefault().timerExec(1000, this);
		}
	};

	private void init()
	{
		mlist = new Mlist(list1, Display.getDefault());
		progressBar1.setMinimum(0);
		progressBar1.setMaximum(DropGlobalVar.getCheckintervall());
	}

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{

		String userdir = System.getProperty("user.dir");
		
		if (args.length > 0)
			if (args[0] != null)
				userdir = args[0];
		if (userdir.contains("bin") == false)
		{
			Tracer.WriteTrace(10, "Falscher Verzeichnissaufbau userdir<"
					+ userdir + ">");
			System.exit(0);
		}

		userdir = userdir.substring(0, userdir.lastIndexOf("\\"));
		System.out.println("akueller pfad=" + userdir);
		DropRootpath glob = new DropRootpath(userdir);
		DropGlobalVar gc = new DropGlobalVar();

		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		if(DropUpdater.checkNewUpdate()==true)
				shell.setText("DropBoxCopyTool V0.161 <"+userdir+">" + "***** new update available ****");
		else
			shell.setText("DropBoxCopyTool V0.161");
		
		StartDropBoxCopy inst = new StartDropBoxCopy(shell, SWT.NULL);
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
			{
				// System.out.println("sleep mainloop");
				display.sleep();
			}
		}
	}

	private boolean checkconfig()
	{
		// prüft ob config gesetzt ist
		if ((DropGlobalVar.getDropboxdir() == null)
				|| (DropGlobalVar.getDropboxdir().length() < 2)
				|| (DropGlobalVar.getMetarootpath() == null)
				|| (DropGlobalVar.getMetarootpath().length() < 2))
		{
			Mbox.Infobox("no config, please set config first");
			return false;
		}
		return true;
	}

	private void setDropboxconfig(SelectionEvent evt)
	{
		System.out.println("saveFileMenuItem.widgetSelected, event=" + evt);
		// edit config
		EditConfig.showGUI();
	}

	private void scanWidgetSelected(SelectionEvent evt)
	{
		//prüft ob konfig gesetzt ist
		if(checkconfig()==false)
			return;
		System.out.println("scan.widgetSelected, event=" + evt);
		// scan
		Mlist.add("scan start", 1);
		swork.scaninit(table1);
		swork.viewTable(table1);
		Mlist.add("scan ready", 1);
	}

	private void table1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table1.widgetSelected, event=" + evt);

		// Hier wurde auf ein Element der Tabelle 1 geklickt
		// Hier wurde also auf einen Metatrader-geklickt
		System.out.println("table1.widgetSelected, event=" + evt);

		// hier wurde eine bestimmte Magic und Broker ausgewählt
		String seltext = evt.item.toString();

		String name = "";
		if (seltext.contains("{"))
		{
			String sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");
			// den globalen selektierten Broker setzen

		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");

		swork.table1clicked(name, table1);
	}

	private void saveWidgetSelected(SelectionEvent evt)
	{
		if(checkconfig()==false)
			return;
		System.out.println("save.widgetSelected, event=" + evt);
		swork.save();
	}

	private void showWidgetSelected(SelectionEvent evt)
	{
		if(checkconfig()==false)
			return;
		System.out.println("show.widgetSelected, event=" + evt);
		swork.refresh(table1);
	}

	private void startWidgetSelected(SelectionEvent evt)
	{
		if(checkconfig()==false)
			return;
		System.out.println("start.widgetSelected, event=" + evt);
		// start loop
		start.setEnabled(false);
		Mlist.add("Loop started", 1);
		loopflag = 1;
		// jede x sekunden werden die EA´s ein/aus geschaltet und
		// historyexporter exportiert
		Display.getDefault().timerExec(DropGlobalVar.getCheckintervall()*1000, timer);
		// balkenlauf jede sekunde
		Display.getDefault().timerExec(1000, balkenlauf);
	}

	private void stopWidgetSelected(SelectionEvent evt)
	{
		if(checkconfig()==false)
			return;
		System.out.println("stop.widgetSelected, event=" + evt);
		// stop loop
		loopflag = 0;
		start.setEnabled(true);
		Mlist.add("Loop stopped", 1);

	}

	private void laufBalken()
	{
		progresscount++;
		if (progresscount == DropGlobalVar.getCheckintervall())
			progresscount = 0;
		progressBar1.setSelection(progresscount);
	}

	private void installeasWidgetSelected(SelectionEvent evt)
	{
		if(checkconfig()==false)
			return;
		System.out.println("installeas.widgetSelected, event=" + evt);
		// button install eas
		swork.installDeleteEas(buttonCleanMt.getSelection());
	}
	
	private void updateWidgetSelected(SelectionEvent evt) {
		System.out.println("update.widgetSelected, event="+evt);
		//TODO add your code for update.widgetSelected
		DropUpdater.updateDropboxtool();
	}
	
	private void button1exporteasWidgetSelected(SelectionEvent evt) {
		System.out.println("button1exporteas.widgetSelected, event="+evt);
		//TODO exportflag button
		swork.exportEas();
	}
	
	private void restartmetatraderWidgetSelected(SelectionEvent evt) {
		System.out.println("restartmetatrader.widgetSelected, event="+evt);
		swork.restartAllMetatrader();
	}
}
