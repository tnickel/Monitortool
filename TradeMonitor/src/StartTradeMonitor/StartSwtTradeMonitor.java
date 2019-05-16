package StartTradeMonitor;

import filter.Tradefilter;
import hiflsklasse.SG;
import hiflsklasse.Tracer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import SwtFenter.SwtSMConfig;

import com.cloudgarden.resource.SWTResourceManager;

import data.Rootpath;
import data.SMglobalConfig;

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
public class StartSwtTradeMonitor extends org.eclipse.swt.widgets.Composite
{

	private Menu menu1;
	private Button button1showprofit;
	private Button button1getalleas;
	private Button button1getEAs;
	private Button button1comparetradelist;
	private Button button1setsortcriteria;
	private Button button1tfset;
	private Button tradefilter;
	private Button button1showallprofit;
	private Text text1eaname;
	private Button button1showtradelist;
	private Button button1loadTradelisten;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private Table table2;
	private Table table1;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private Tradefilter tf = new Tradefilter();

	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public StartSwtTradeMonitor(Composite parent, int style)
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
			this.setSize(1709, 796);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				button1getalleas = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1getalleasLData = new FormData();
				button1getalleasLData.left =  new FormAttachment(0, 1000, 1610);
				button1getalleasLData.top =  new FormAttachment(0, 1000, 58);
				button1getalleasLData.width = 81;
				button1getalleasLData.height = 27;
				button1getalleas.setLayoutData(button1getalleasLData);
				button1getalleas.setText("Get ALL EAs");
				button1getalleas.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1getalleasWidgetSelected(evt);
					}
				});
			}
			{
				button1getEAs = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1getEAsLData = new FormData();
				button1getEAsLData.left =  new FormAttachment(0, 1000, 1473);
				button1getEAsLData.top =  new FormAttachment(0, 1000, 649);
				button1getEAsLData.width = 131;
				button1getEAsLData.height = 27;
				button1getEAs.setLayoutData(button1getEAsLData);
				button1getEAs.setText("Get EAs from Broker");
				button1getEAs.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1getEAsWidgetSelected(evt);
					}
				});
			}
			{
				button1comparetradelist = new Button(this, SWT.PUSH
						| SWT.CENTER);
				FormData button1comparetradelistLData = new FormData();
				button1comparetradelistLData.left = new FormAttachment(0, 1000,
						454);
				button1comparetradelistLData.top = new FormAttachment(0, 1000,
						708);
				button1comparetradelistLData.width = 115;
				button1comparetradelistLData.height = 27;
				button1comparetradelist
						.setLayoutData(button1comparetradelistLData);
				button1comparetradelist.setText("compare tradelist");
				button1comparetradelist
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1comparetradelistWidgetSelected(evt);
							}
						});
			}
			{
				button1setsortcriteria = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1setsortcriteriaLData = new FormData();
				button1setsortcriteriaLData.left = new FormAttachment(0, 1000,
						740);
				button1setsortcriteriaLData.top = new FormAttachment(0, 1000,
						27);
				button1setsortcriteriaLData.width = 100;
				button1setsortcriteriaLData.height = 21;
				button1setsortcriteria
						.setLayoutData(button1setsortcriteriaLData);
				button1setsortcriteria.setText("set sort criteria");
				button1setsortcriteria
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1setsortcriteriaWidgetSelected(evt);
							}
						});
			}
			{
				button1tfset = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1tfsetLData = new FormData();
				button1tfsetLData.left = new FormAttachment(0, 1000, 976);
				button1tfsetLData.top = new FormAttachment(0, 1000, 682);
				button1tfsetLData.width = 28;
				button1tfsetLData.height = 20;
				button1tfset.setLayoutData(button1tfsetLData);
				button1tfset.setText("set");
				button1tfset.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1tfsetWidgetSelected(evt);
					}
				});
			}
			{
				tradefilter = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button1tradefilterLData = new FormData();
				button1tradefilterLData.left = new FormAttachment(0, 1000, 857);
				button1tradefilterLData.top = new FormAttachment(0, 1000, 682);
				button1tradefilterLData.width = 113;
				button1tradefilterLData.height = 20;
				tradefilter.setLayoutData(button1tradefilterLData);
				tradefilter.setText("tradefilter");
				tradefilter.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1tradefilterWidgetSelected(evt);
					}
				});
			}
			{
				button1showallprofit = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1showallprofitLData = new FormData();
				button1showallprofitLData.left = new FormAttachment(0, 1000,
						454);
				button1showallprofitLData.top = new FormAttachment(0, 1000, 741);
				button1showallprofitLData.width = 115;
				button1showallprofitLData.height = 27;
				button1showallprofit.setLayoutData(button1showallprofitLData);
				button1showallprofit.setText("showallprofit");
				button1showallprofit
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1showallprofitWidgetSelected(evt);
							}
						});
			}
			{
				FormData text1eanameLData = new FormData();
				text1eanameLData.left = new FormAttachment(0, 1000, 452);
				text1eanameLData.top = new FormAttachment(0, 1000, 649);
				text1eanameLData.width = 181;
				text1eanameLData.height = 17;
				text1eaname = new Text(this, SWT.NONE);
				text1eaname.setLayoutData(text1eanameLData);
			}
			{
				button1showprofit = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1showprofitLData = new FormData();
				button1showprofitLData.left = new FormAttachment(0, 1000, 647);
				button1showprofitLData.top = new FormAttachment(0, 1000, 675);
				button1showprofitLData.width = 89;
				button1showprofitLData.height = 27;
				button1showprofit.setLayoutData(button1showprofitLData);
				button1showprofit.setText("showprofit");
				button1showprofit.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1showprofitWidgetSelected(evt);
					}
				});
			}
			{
				button1showtradelist = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1showtradelistLData = new FormData();
				button1showtradelistLData.left = new FormAttachment(0, 1000,
						452);
				button1showtradelistLData.top = new FormAttachment(0, 1000, 675);
				button1showtradelistLData.width = 117;
				button1showtradelistLData.height = 27;
				button1showtradelist.setLayoutData(button1showtradelistLData);
				button1showtradelist.setText("showtradelist");
				button1showtradelist
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1showtradelistWidgetSelected(evt);
							}
						});
			}
			{
				button1loadTradelisten = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1loadTradelistenLData = new FormData();
				button1loadTradelistenLData.left = new FormAttachment(0, 1000,
						299);
				button1loadTradelistenLData.top = new FormAttachment(0, 1000,
						649);
				button1loadTradelistenLData.width = 141;
				button1loadTradelistenLData.height = 30;
				button1loadTradelisten
						.setLayoutData(button1loadTradelistenLData);
				button1loadTradelisten.setText("reload tradelisten");
				button1loadTradelisten
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button1loadTradelistenWidgetSelected(evt);
							}
						});
			}
			{
				FormData table2LData = new FormData();
				table2LData.left =  new FormAttachment(0, 1000, 450);
				table2LData.top =  new FormAttachment(0, 1000, 58);
				table2LData.width = 1131;
				table2LData.height = 558;
				table2 = new Table(this, SWT.FULL_SELECTION | SWT.BORDER);
				table2.setLayoutData(table2LData);
				table2.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						table2WidgetSelected(evt);
					}
				});
			}
			{
				FormData table1LData = new FormData();
				table1LData.left = new FormAttachment(0, 1000, 22);
				table1LData.top = new FormAttachment(0, 1000, 54);
				table1LData.width = 393;
				table1LData.height = 558;
				table1 = new Table(this, SWT.H_SCROLL | SWT.V_SCROLL
						| SWT.BORDER);
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
							newFileMenuItem.setText("Config");
							newFileMenuItem
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											newFileMenuItemWidgetSelected(evt);
										}
									});
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
				LoadTradelisten(table1);
			}
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void LoadTradelisten(Table table1)
	{
		// baut die Tradelistentable auf
		TradeMonitorWork.LoadTradelisten(table1);

		// ServerMonitorWork.ShowTradelisten();
	}

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		String userdir = System.getProperty("user.dir");

		if (args.length > 0)
		{
			if (args[0] != null)
				userdir = args[0];
		} else
			userdir = System.getProperty("user.dir");

		if(userdir.contains("bin"))
			userdir=userdir.substring(0,userdir.lastIndexOf("\\bin"));
		
		
	
		// rootdir setzen
		SMglobalConfig sconf = new SMglobalConfig(userdir);
		

	
		
		// den rootpath in der zweiten Struktur setzen
		Rootpath glob = new Rootpath(userdir, userdir + "\\conf");

		StartSwtTradeMonitor inst = new StartSwtTradeMonitor(shell, SWT.NULL);
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

	private void newFileMenuItemWidgetSelected(SelectionEvent evt)
	{
		System.out.println("newFileMenuItem.widgetSelected, event=" + evt);
		SwtSMConfig.main(null);
	}

	private void button1loadTradelistenWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1loadTradelisten.widgetSelected, event="
				+ evt);
		// TODO add your code for button1loadTradelisten.widgetSelected
	}

	private void table1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table1.widgetSelected, event=" + evt);
		// hier wurde auf ein element der übersichtsliste geklickt

		String seltext = evt.item.toString();

		String name = "";
		if (seltext.contains("{"))
		{
			String sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");
			// den globalen selektierten Broker setzen

		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");

		TradeMonitorWork.LoadShowProfittable(Display.getDefault(), tf, table2,
				name);
		TradeMonitorWork.setTable1Name(name);
	}

	private void button1showtradelistWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1showtradelist.widgetSelected, event=" + evt);
		// TODO add your code for button1showtradelist.widgetSelected
		TradeMonitorWork.showTradelist();
	}

	private void button1showprofitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1showprofit.widgetSelected, event=" + evt);
		// ServerMonitorWork.showProfit();
		TradeMonitorWork.showProfitGraphik();
	}

	private void table2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table2.widgetSelected, event=" + evt);
		// TODO add your code for table2.widgetSelected
		String seltext = evt.item.toString();

		String name = "";
		if (seltext.contains("{"))
		{
			String sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");

		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");

		int zahl = SG.get_zahl(name);
		String magicstring = TradeMonitorWork.selectEa(zahl);
		text1eaname.setText(magicstring);
		TradeMonitorWork.setTable2Name(name);
	}

	private void button1showallprofitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1showallprofit.widgetSelected, event=" + evt);
		TradeMonitorWork.showallprofit(null, 0, 100);
	}

	private void button1tradefilterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1tradefilter.widgetSelected, event=" + evt);

	}

	private void button1tfsetWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1tfset.widgetSelected, event=" + evt);
		TradeMonitorWork.TradefilterConfig(tf);
		tradefilter.setSelection(true);
	}

	private void button1setsortcriteriaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setsortcriteria.widgetSelected, event="
				+ evt);
		TradeMonitorWork.SetSortCriteria();
	}

	private void button1comparetradelistWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1comparetradelist.widgetSelected, event="
				+ evt);
		TradeMonitorWork.CompareTradelist();
	}
	
	private void button1getEAsWidgetSelected(SelectionEvent evt) {
		System.out.println("button1getEAs.widgetSelected, event="+evt);
		
		TradeMonitorWork.getMetatrader();
	}
	
	private void button1getalleasWidgetSelected(SelectionEvent evt) {
		System.out.println("button1getalleas.widgetSelected, event="+evt);
		//TODO add your code for button1getalleas.widgetSelected
		TradeMonitorWork.getAllMetatrader();
	}

}
