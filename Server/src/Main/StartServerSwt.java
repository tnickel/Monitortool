package Main;

import hilfsklasse.Tracer;

import java.io.IOException;

import mtools.Mlist;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import soc.ServerExample;

import com.cloudgarden.resource.SWTResourceManager;

import data.ConfigServer;
import data.GlobServer;
import data.Rootpath;

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
public class StartServerSwt extends org.eclipse.swt.widgets.Composite
{

	private Menu menu1;
	private Table table2;
	private Button button1stop;
	private Button button1start;
	private Composite composite4;
	private Text text1status;
	private Composite composite3;
	private List list1;
	private Composite composite2;
	private Table table1;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private Composite composite1;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;

	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public StartServerSwt(Composite parent, int style)
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
			this.setSize(1434, 679);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				FormData table2LData = new FormData();
				table2LData.left = new FormAttachment(0, 1000, 28);
				table2LData.top = new FormAttachment(0, 1000, 44);
				table2LData.width = 269;
				table2LData.height = 341;
				table2 = new Table(this, SWT.H_SCROLL | SWT.V_SCROLL
						| SWT.BORDER);
				table2.setLayoutData(table2LData);
			}
			{
				composite4 = new Composite(this, SWT.NONE);
				RowLayout composite4Layout = new RowLayout(
						org.eclipse.swt.SWT.HORIZONTAL);
				FormData composite4LData = new FormData();
				composite4LData.left = new FormAttachment(0, 1000, 1048);
				composite4LData.top = new FormAttachment(0, 1000, 623);
				composite4LData.width = 360;
				composite4LData.height = 49;
				composite4.setLayoutData(composite4LData);
				composite4.setLayout(composite4Layout);
				{
					button1start = new Button(composite4, SWT.PUSH | SWT.CENTER);
					RowData button1startLData = new RowData();
					button1start.setLayoutData(button1startLData);
					button1start.setText("start");
					button1start.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent evt)
						{
							button1startWidgetSelected(evt);
						}
					});
				}
				{
					button1stop = new Button(composite4, SWT.PUSH | SWT.CENTER);
					RowData button1stopLData = new RowData();
					button1stop.setLayoutData(button1stopLData);
					button1stop.setText("stop");
					button1stop.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent evt)
						{
							button1stopWidgetSelected(evt);
						}
					});
				}
			}
			{
				composite3 = new Composite(this, SWT.NONE);
				GridLayout composite3Layout = new GridLayout();
				composite3Layout.makeColumnsEqualWidth = true;
				FormData composite3LData = new FormData();
				composite3LData.left = new FormAttachment(0, 1000, 1020);
				composite3LData.top = new FormAttachment(0, 1000, 19);
				composite3LData.width = 379;
				composite3LData.height = 82;
				composite3.setLayoutData(composite3LData);
				composite3.setLayout(composite3Layout);
				{
					GridData text1statusLData = new GridData();
					text1statusLData.widthHint = 144;
					text1statusLData.heightHint = 31;
					text1status = new Text(composite3, SWT.BORDER);
					text1status.setLayoutData(text1statusLData);
				}
			}
			{
				composite2 = new Composite(this, SWT.NONE);
				FillLayout composite2Layout = new FillLayout(
						org.eclipse.swt.SWT.HORIZONTAL);
				FormData composite2LData = new FormData();
				composite2LData.left = new FormAttachment(0, 1000, 1048);
				composite2LData.top = new FormAttachment(0, 1000, 335);
				composite2LData.width = 360;
				composite2LData.height = 269;
				composite2.setLayoutData(composite2LData);
				composite2.setLayout(composite2Layout);
				{
					list1 = new List(composite2, SWT.H_SCROLL | SWT.V_SCROLL
							| SWT.BORDER);
				}
			}
			{
				composite1 = new Composite(this, SWT.NONE);
				FillLayout composite1Layout = new FillLayout(
						org.eclipse.swt.SWT.HORIZONTAL);
				FormData composite1LData = new FormData();
				composite1LData.left = new FormAttachment(0, 1000, 415);
				composite1LData.top = new FormAttachment(0, 1000, 101);
				composite1LData.width = 592;
				composite1LData.height = 503;
				composite1.setLayoutData(composite1LData);
				composite1.setLayout(composite1Layout);
				{
					table1 = new Table(composite1, SWT.H_SCROLL | SWT.V_SCROLL
							| SWT.BORDER);
				}
			}
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							openFileMenuItem = new MenuItem(fileMenu,
									SWT.CASCADE);
							openFileMenuItem.setText("Open");
						}
						{
							newFileMenuItem = new MenuItem(fileMenu,
									SWT.CASCADE);
							newFileMenuItem.setText("New");
						}
						{
							saveFileMenuItem = new MenuItem(fileMenu,
									SWT.CASCADE);
							saveFileMenuItem.setText("Save");
						}
						{
							closeFileMenuItem = new MenuItem(fileMenu,
									SWT.CASCADE);
							closeFileMenuItem.setText("Close");
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
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
			this.layout();
			Mlist ml = new Mlist(list1, Display.getDefault());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{

		String userdir = System.getProperty("user.dir");
		String updatemessage = "";
		System.out.println("akueller pfad=" + userdir);
		GlobServer.setRootdir(userdir);
		ConfigServer conf = new ConfigServer();

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
		Rootpath glob = new Rootpath(userdir, userdir + "\\conf");

		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		StartServerSwt inst = new StartServerSwt(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();

		// den serverprozess starten

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
			// System.out.println("swtloop");
		}
	}

	private void button1startWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1start.widgetSelected, event=" + evt);
		// Start Server
		try
		{
			ServerExample.main(null);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		text1status.setText("server started");
		Mlist.add("server Started");
	}

	private void button1stopWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1stop.widgetSelected, event=" + evt);
		text1status.setText("server stopped");
		Mlist.add("server stopped");
		ServerExample.stopServer();
	}

}
