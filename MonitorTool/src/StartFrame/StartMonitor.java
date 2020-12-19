package StartFrame;


import java.awt.Panel;
import java.io.File;
import java.util.Date;
import java.util.Random;

import modtools.Installer;
import modtools.MetaStarter;
import mtools.DisTool;
import mtools.Mlist;
import network.MonitorClient;
import network.Updater;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import swtHilfsfenster.SwtEditGlobalConfig;
import swtHilfsfenster.SwtShowLicense;
import Sync.LockTradeliste;

import com.cloudgarden.resource.SWTResourceManager;

import data.GlobalVar;
import data.Lic;
import data.Rootpath;
import data.Tradeanzahl;
import datefunkt.Mondate;
import filter.Tradefilter;
import gui.Mbox;
import hiflsklasse.GC;
import hiflsklasse.SG;
import hiflsklasse.Sys;
import hiflsklasse.Tracer;

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
public class StartMonitor extends org.eclipse.swt.widgets.Composite
{
	private Menu menu1;
	private Button profitnormalisierung;
	private Label label2;
	private Button stop;
	private Button checkGd20;
	private Button lifecheck;
	private Button button2;
	private Button button1x;
	private Text text1from;
	private Text text1fromx;
	private Button button2showtradelistportfolio;
	private Button button2stopmt;
	private Button button2startmetatrader;
	private Button button2importall;
	private Button button2exportall;
	private Button button2autoconfig;
	private Text statustext;
	private Button button2showallprotfolio;
	private Button button2best100;
	private Label label3;
	private Text text1days;
	private Button button2delete;
	private Button button2newcal;
	private Button buttonExportTradelist;
	private Button buttondown;
	private Button buttonup;
	private Button sortcriteria;
	private Button showbacktest;
	private Button removeimported;
	private MenuItem menuItem1;
	private Button importtradelisten;
	private Button exportEa;
	private Button showselprofits;
	private Button selonlyone;
	private Button fontminus;
	private Button fontplus;
	private Button settradefilter;
	private Button tradefilter;
	private Button loadexpiredhistory;
	private Button comparetradelist;
	private Button setinstfrom;
	private Button searchbutton;
	private Text search;
	private Button showonofflog;
	private Button checkallbroker;
	private Button toggleallprofits;
	private Button clr;
	private Button copyea;
	private Combo combo_targetbroker;
	private Button configea;
	private Button showbroker;
	private Button sync;
	private Button installautoea;
	private Button automaticOnOff;
	private Button manualonoff;
	private Button loop;
	private Text intervall;
	private List messagelist;
	private Label label5;
	private Button button1;
	private Button saveandexit;
	private Button showallprofit;
	private Button forgetoldeas;
	private Label label4;
	private Text anzeas;
	private Text anzincommingtrades;
	private ProgressBar progressBar1side;
	private ProgressBar progressBar2;
	private Label label1;
	private Panel panel1;
	static private Display dis_glob = null;
	private Button showprofit;
	private Button deletebroker;
	private Button addbroker;
	private Button showopenorders;
	private MenuItem updateHistoryExporter;
	private MenuItem backup, transfer, config, info, transferuserdata;
	private Button showTradeliste;
	private Button configbroker;
	private Table table3;
	private Label broker;
	private Button nocanceled;
	private Table table2;
	private Table table1;
	private Button getAllData;
	private MenuItem exitMenuItem;
	private MenuItem updateFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private StartMonitorWork smw = null;
	private Tradefilter tf = new Tradefilter();
	private int loopflag = 0;
	private int progresscount = 0;
	private int table2fontsize = 9;
	private int loadallbrokerflag = 0;
	

	// brokermode=0 alle broker angewählt
	// brokermode=1 nur einen broker angewählt
	private int brokermode = 0;
	// realbrokerflag==1 falls ein realbroker ausgewählt wurde
	private int realbrokerflag = 0;
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public StartMonitor(Composite parent, int style)
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
			this.setSize(1922, 984);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
			{
				button2 = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button2LData = new FormData();
				button2LData.left =  new FormAttachment(0, 1000, 10);
				button2LData.top =  new FormAttachment(0, 1000, 747);
				button2LData.width = 224;
				button2LData.height = 20;
				button2.setLayoutData(button2LData);
				button2.setText("cut trades before");
				button2.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2WidgetSelected(evt);
					}
				});
			}
			{
				text1from = new Text(this, SWT.NONE);
				FormData text1fromLData = new FormData();
				text1fromLData.left =  new FormAttachment(0, 1000, 32);
				text1fromLData.top =  new FormAttachment(0, 1000, 775);
				text1fromLData.width = 93;
				text1fromLData.height = 22;
				text1from.setLayoutData(text1fromLData);
				text1from.setText("01.01.2013");
			}
			{
				button2showtradelistportfolio = new Button(this, SWT.PUSH
						| SWT.CENTER);
				FormData button2showtradelistportfolioLData = new FormData();
				button2showtradelistportfolioLData.left =  new FormAttachment(0, 1000, 463);
				button2showtradelistportfolioLData.top =  new FormAttachment(0, 1000, 792);
				button2showtradelistportfolioLData.width = 198;
				button2showtradelistportfolioLData.height = 27;
				button2showtradelistportfolio.setLayoutData(button2showtradelistportfolioLData);
				button2showtradelistportfolio
						.setText("show tradelist (portfolio)");
				button2showtradelistportfolio
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button2showtradelistportfolioWidgetSelected(evt);
							}
						});
			}
			this.addDisposeListener(new DisposeListener()
			{
				public void widgetDisposed(DisposeEvent evt)
				{
					thisWidgetDisposed(evt);
				}
			});
			{
				button2stopmt = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2stopmtLData = new FormData();
				button2stopmtLData.left =  new FormAttachment(0, 1000, 1701);
				button2stopmtLData.top =  new FormAttachment(0, 1000, 590);
				button2stopmtLData.width = 84;
				button2stopmtLData.height = 18;
				button2stopmt.setLayoutData(button2stopmtLData);
				button2stopmt.setText("stop all MT");
				button2stopmt.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				button2stopmt.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2stopmtWidgetSelected(evt);
					}
				});
			}
			{
				button2startmetatrader = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2startmetatraderLData = new FormData();
				button2startmetatraderLData.left =  new FormAttachment(0, 1000, 1632);
				button2startmetatraderLData.top =  new FormAttachment(0, 1000, 590);
				button2startmetatraderLData.width = 69;
				button2startmetatraderLData.height = 18;
				button2startmetatrader.setLayoutData(button2startmetatraderLData);
				button2startmetatrader.setText("start MT");
				button2startmetatrader.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				button2startmetatrader
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button2startmetatraderWidgetSelected(evt);
							}
						});
			}
			{
				button2importall = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2importallLData = new FormData();
				button2importallLData.left =  new FormAttachment(0, 1000, 463);
				button2importallLData.top =  new FormAttachment(0, 1000, 902);
				button2importallLData.width = 417;
				button2importallLData.height = 27;
				button2importall.setLayoutData(button2importallLData);
				button2importall
						.setText("import all trades (only for viewing)");
				button2importall.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2importallWidgetSelected(evt);
					}
				});
			}
			{
				button2exportall = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2exportallLData = new FormData();
				button2exportallLData.left =  new FormAttachment(0, 1000, 673);
				button2exportallLData.top =  new FormAttachment(0, 1000, 860);
				button2exportallLData.width = 207;
				button2exportallLData.height = 30;
				button2exportall.setLayoutData(button2exportallLData);
				button2exportall.setText("export all trades");
				button2exportall.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2exportallWidgetSelected(evt);
					}
				});
			}
			{
				button2autoconfig = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2autoconfigLData = new FormData();
				button2autoconfigLData.left =  new FormAttachment(0, 1000, 1802);
				button2autoconfigLData.top =  new FormAttachment(0, 1000, 590);
				button2autoconfigLData.width = 69;
				button2autoconfigLData.height = 18;
				button2autoconfig.setLayoutData(button2autoconfigLData);
				button2autoconfig.setText("autoconf");
				button2autoconfig.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				button2autoconfig.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2autoconfigWidgetSelected(evt);
					}
				});
			}
			{
				statustext = new Text(this, SWT.NONE);
				FormData statustextLData = new FormData();
				statustextLData.left =  new FormAttachment(0, 1000, 749);
				statustextLData.top =  new FormAttachment(0, 1000, 18);
				statustextLData.width = 152;
				statustextLData.height = 26;
				statustext.setLayoutData(statustextLData);
				statustext.setText(GlobalVar.getIpmessage());
				statustext.setEditable(false);
			}
			{
				button2showallprotfolio = new Button(this, SWT.PUSH
						| SWT.CENTER);
				FormData button2showallprotfolioLData = new FormData();
				button2showallprotfolioLData.left =  new FormAttachment(0, 1000, 246);
				button2showallprotfolioLData.top =  new FormAttachment(0, 1000, 747);
				button2showallprotfolioLData.width = 117;
				button2showallprotfolioLData.height = 30;
				button2showallprotfolio.setLayoutData(button2showallprotfolioLData);
				button2showallprotfolio.setText("show all portfolio");
				button2showallprotfolio
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								button2showallprotfolioWidgetSelected(evt);
							}
						});
			}
			{
				button2best100 = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button2best100LData = new FormData();
				button2best100LData.left =  new FormAttachment(0, 1000, 369);
				button2best100LData.top =  new FormAttachment(0, 1000, 755);
				button2best100LData.width = 88;
				button2best100LData.height = 20;
				button2best100.setLayoutData(button2best100LData);
				button2best100.setText("best100");
				button2best100.setSelection(true);
			}
			{
				label3 = new Label(this, SWT.NONE);
				FormData label3LData = new FormData();
				label3LData.left =  new FormAttachment(0, 1000, 1091);
				label3LData.top =  new FormAttachment(0, 1000, 642);
				label3LData.width = 30;
				label3LData.height = 20;
				label3.setLayoutData(label3LData);
				label3.setText("days");
			}
			{
				text1days = new Text(this, SWT.MULTI | SWT.WRAP);
				FormData text1daysLData = new FormData();
				text1daysLData.left =  new FormAttachment(0, 1000, 1015);
				text1daysLData.top =  new FormAttachment(0, 1000, 642);
				text1daysLData.width = 62;
				text1daysLData.height = 20;
				text1days.setLayoutData(text1daysLData);
				if (GlobalVar.getForgetdays() > 0)
					text1days
							.setText(String.valueOf(GlobalVar.getForgetdays()));

				else
					text1days.setText("30");
				text1days.addTraverseListener(new TraverseListener()
				{
					public void keyTraversed(TraverseEvent evt)
					{
						text1daysKeyTraversed(evt);
					}
				});
			}
			{
				button2delete = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2deleteLData = new FormData();
				button2deleteLData.left =  new FormAttachment(0, 1000, 892);
				button2deleteLData.top =  new FormAttachment(0, 1000, 860);
				button2deleteLData.width = 295;
				button2deleteLData.height = 30;
				button2delete.setLayoutData(button2deleteLData);
				button2delete.setText("delete selected EAs");
				button2delete.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button2deleteWidgetSelected(evt);
					}

				});
			}
			{
				button2newcal = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button2newcalLData = new FormData();
				button2newcalLData.left =  new FormAttachment(0, 1000, 579);
				button2newcalLData.top =  new FormAttachment(0, 1000, 766);
				button2newcalLData.width = 88;
				button2newcalLData.height = 20;
				button2newcal.setLayoutData(button2newcalLData);
				button2newcal.setText("new cal");
			}
			{
				buttonExportTradelist = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData buttonExportTradelistLData = new FormData();
				buttonExportTradelistLData.left =  new FormAttachment(0, 1000, 1025);
				buttonExportTradelistLData.top =  new FormAttachment(0, 1000, 784);
				buttonExportTradelistLData.width = 109;
				buttonExportTradelistLData.height = 30;
				buttonExportTradelist.setLayoutData(buttonExportTradelistLData);
				buttonExportTradelist.setText("Export Tradelist");
				buttonExportTradelist.setEnabled(false);
				buttonExportTradelist
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								buttonExportTradelistWidgetSelected(evt);
							}
						});
			}
			{
				buttondown = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData buttondownLData = new FormData();
				buttondownLData.left =  new FormAttachment(0, 1000, 1823);
				buttondownLData.top =  new FormAttachment(0, 1000, 534);
				buttondownLData.width = 48;
				buttondownLData.height = 19;
				buttondown.setLayoutData(buttondownLData);
				buttondown.setText("down");
				buttondown.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				buttondown.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						buttondownWidgetSelected(evt);
					}
				});
			}
			{
				buttonup = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData buttonupLData = new FormData();
				buttonupLData.left =  new FormAttachment(0, 1000, 1823);
				buttonupLData.top =  new FormAttachment(0, 1000, 510);
				buttonupLData.width = 48;
				buttonupLData.height = 18;
				buttonup.setLayoutData(buttonupLData);
				buttonup.setText("up");
				buttonup.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				buttonup.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						buttonupWidgetSelected(evt);
					}
				});
			}
			{
				sortcriteria = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData sortcriteriaLData = new FormData();
				sortcriteriaLData.left =  new FormAttachment(0, 1000, 917);
				sortcriteriaLData.top =  new FormAttachment(0, 1000, 18);
				sortcriteriaLData.width = 124;
				sortcriteriaLData.height = 28;
				sortcriteria.setLayoutData(sortcriteriaLData);
				sortcriteria.setText("set sort criteria");
				sortcriteria.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						sortcriteriaWidgetSelected(evt);
					}
				});
			}
			{
				showbacktest = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showbacktestLData = new FormData();
				showbacktestLData.left =  new FormAttachment(0, 1000, 463);
				showbacktestLData.top =  new FormAttachment(0, 1000, 755);
				showbacktestLData.width = 110;
				showbacktestLData.height = 30;
				showbacktest.setLayoutData(showbacktestLData);
				showbacktest.setText("showbacktest");
				showbacktest.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showbacktestWidgetSelected(evt);
					}
				});
			}
			{
				removeimported = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData removeimportedLData = new FormData();
				removeimportedLData.left =  new FormAttachment(0, 1000, 892);
				removeimportedLData.top =  new FormAttachment(0, 1000, 820);
				removeimportedLData.width = 307;
				removeimportedLData.height = 28;
				removeimported.setLayoutData(removeimportedLData);
				removeimported.setText("Remove Imported Out Of Database");
				removeimported.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						removeimportedWidgetSelected(evt);
					}
				});
			}
			{
				importtradelisten = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData importeasLData = new FormData();
				importeasLData.left =  new FormAttachment(0, 1000, 892);
				importeasLData.top =  new FormAttachment(0, 1000, 784);
				importeasLData.width = 127;
				importeasLData.height = 30;
				importtradelisten.setLayoutData(importeasLData);
				importtradelisten.setText("Import Tradelist");
				importtradelisten.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						importeasWidgetSelected(evt);
					}
				});
			}
			{
				exportEa = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData exporteaLData = new FormData();
				exporteaLData.left =  new FormAttachment(0, 1000, 673);
				exporteaLData.top =  new FormAttachment(0, 1000, 820);
				exporteaLData.width = 207;
				exporteaLData.height = 30;
				exportEa.setLayoutData(exporteaLData);
				exportEa.setText("Export 1EA+Tradelist");
				exportEa.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						exporteaWidgetSelected(evt);
					}
				});
			}
			{
				showselprofits = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showselprofitsLData = new FormData();
				showselprofitsLData.left =  new FormAttachment(0, 1000, 246);
				showselprofitsLData.top =  new FormAttachment(0, 1000, 792);
				showselprofitsLData.width = 211;
				showselprofitsLData.height = 30;
				showselprofits.setLayoutData(showselprofitsLData);
				showselprofits.setText("show sel profits (portfolio)");
				showselprofits.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showselprofitsWidgetSelected(evt);
					}
				});
			}
			{
				selonlyone = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData selonlyoneLData = new FormData();
				selonlyoneLData.left =  new FormAttachment(0, 1000, 463);
				selonlyoneLData.top =  new FormAttachment(0, 1000, 642);
				selonlyoneLData.width = 186;
				selonlyoneLData.height = 20;
				selonlyone.setLayoutData(selonlyoneLData);
				selonlyone.setText("SelOneMode");
				selonlyone.setSelection(true);
			}
			{
				fontminus = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData fontminusLData = new FormData();
				fontminusLData.left =  new FormAttachment(0, 1000, 1100);
				fontminusLData.top =  new FormAttachment(0, 1000, 668);
				fontminusLData.width = 20;
				fontminusLData.height = 20;
				fontminus.setLayoutData(fontminusLData);
				fontminus.setText("-");
				fontminus.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						fontminusWidgetSelected(evt);
					}
				});
			}
			{
				fontplus = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData fontplusLData = new FormData();
				fontplusLData.left =  new FormAttachment(0, 1000, 1082);
				fontplusLData.top =  new FormAttachment(0, 1000, 668);
				fontplusLData.width = 19;
				fontplusLData.height = 20;
				fontplus.setLayoutData(fontplusLData);
				fontplus.setText("+");
				fontplus.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						fontplusWidgetSelected(evt);
					}
				});
			}
			{
				settradefilter = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData settradefilterLData = new FormData();
				settradefilterLData.left =  new FormAttachment(0, 1000, 1082);
				settradefilterLData.top =  new FormAttachment(0, 1000, 708);
				settradefilterLData.width = 31;
				settradefilterLData.height = 20;
				settradefilter.setLayoutData(settradefilterLData);
				settradefilter.setText("set");
				settradefilter.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						settradefilterWidgetSelected(evt);
					}
				});
			}
			{
				tradefilter = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData tradefilterLData = new FormData();
				tradefilterLData.left =  new FormAttachment(0, 1000, 892);
				tradefilterLData.top =  new FormAttachment(0, 1000, 708);
				tradefilterLData.width = 178;
				tradefilterLData.height = 20;
				tradefilter.setLayoutData(tradefilterLData);
				tradefilter.setText("tradefilter");
				tradefilter.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						tradefilterWidgetSelected(evt);
					}
				});
			}
			{
				loadexpiredhistory = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData loadexpiredhistoryLData = new FormData();
				loadexpiredhistoryLData.left =  new FormAttachment(0, 1000, 892);
				loadexpiredhistoryLData.top =  new FormAttachment(0, 1000, 676);
				loadexpiredhistoryLData.width = 156;
				loadexpiredhistoryLData.height = 20;
				loadexpiredhistory.setLayoutData(loadexpiredhistoryLData);
				loadexpiredhistory.setText("load expired history");
				loadexpiredhistory.setSelection(true);
				loadexpiredhistory.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						loadexpiredhistoryWidgetSelected(evt);
					}
				});
			}
			{
				comparetradelist = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData comparetradelistLData = new FormData();
				comparetradelistLData.left =  new FormAttachment(0, 1000, 463);
				comparetradelistLData.top =  new FormAttachment(0, 1000, 683);
				comparetradelistLData.width = 149;
				comparetradelistLData.height = 30;
				comparetradelist.setLayoutData(comparetradelistLData);
				comparetradelist.setText("compare tradelist");
				comparetradelist.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						comparetradelistWidgetSelected(evt);
					}
				});
			}
			{
				setinstfrom = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData setinstfromLData = new FormData();
				setinstfromLData.left =  new FormAttachment(0, 1000, 673);
				setinstfromLData.top =  new FormAttachment(0, 1000, 747);
				setinstfromLData.width = 207;
				setinstfromLData.height = 30;
				setinstfrom.setLayoutData(setinstfromLData);
				setinstfrom.setText("set instfrom");
				setinstfrom.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						setinstfromWidgetSelected(evt);
					}
				});
			}
			{
				searchbutton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button2LData = new FormData();
				button2LData.left =  new FormAttachment(0, 1000, 1230);
				button2LData.top =  new FormAttachment(0, 1000, 775);
				button2LData.width = 92;
				button2LData.height = 31;
				searchbutton.setLayoutData(button2LData);
				searchbutton.setText("search");
				searchbutton.setSelection(true);
				searchbutton.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						searchbuttonWidgetSelected(evt);
					}
				});
			}
			{
				FormData searchLData = new FormData();
				searchLData.left =  new FormAttachment(0, 1000, 1179);
				searchLData.top =  new FormAttachment(0, 1000, 749);
				searchLData.width = 95;
				searchLData.height = 20;
				search = new Text(this, SWT.NONE);
				search.setLayoutData(searchLData);
			}
			{
				showonofflog = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showonofflogLData = new FormData();
				showonofflogLData.left =  new FormAttachment(0, 1000, 1282);
				showonofflogLData.top =  new FormAttachment(0, 1000, 668);
				showonofflogLData.width = 49;
				showonofflogLData.height = 20;
				showonofflog.setLayoutData(showonofflogLData);
				showonofflog.setText("on/off log");
				showonofflog.setFont(SWTResourceManager.getFont("Segoe UI", 6,
						0, false, false));
				showonofflog.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showonofflogWidgetSelected(evt);
					}
				});
			}
			{
				checkallbroker = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData checkallbrokerLData = new FormData();
				checkallbrokerLData.left =  new FormAttachment(0, 1000, 1357);
				checkallbrokerLData.top =  new FormAttachment(0, 1000, 590);
				checkallbrokerLData.width = 18;
				checkallbrokerLData.height = 17;
				checkallbroker.setLayoutData(checkallbrokerLData);
			}
			{
				toggleallprofits = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData checkallLData = new FormData();
				checkallLData.width = 19;
				checkallLData.height = 21;
				checkallLData.left =  new FormAttachment(0, 1000, 391);
				checkallLData.top =  new FormAttachment(0, 1000, 642);
				toggleallprofits.setLayoutData(checkallLData);
				toggleallprofits.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						checkallWidgetSelected(evt);
					}
				});
			}
			{
				clr = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData clrLData = new FormData();
				clrLData.top =  new FormAttachment(0, 1000, 696);
				clrLData.width = 18;
				clrLData.height = 20;
				clrLData.left =  new FormAttachment(0, 1000, 1313);
				clr.setLayoutData(clrLData);
				clr.setText("clr");
				clr.setFont(SWTResourceManager.getFont("Segoe UI", 6, 0, false,
						false));
				clr.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						clrWidgetSelected(evt);
					}
				});
			}
			{
				checkGd20 = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData checkGd20LData = new FormData();
				checkGd20LData.left =  new FormAttachment(0, 1000, 1127);
				checkGd20LData.top =  new FormAttachment(0, 1000, 642);
				checkGd20LData.width = 81;
				checkGd20LData.height = 20;
				checkGd20.setLayoutData(checkGd20LData);
				checkGd20.setText("CheckAllGd20");
				checkGd20.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				checkGd20.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						checkGd20WidgetSelected(evt);
					}
				});
			}
			{
				lifecheck = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData lifecheckLData = new FormData();
				lifecheckLData.left =  new FormAttachment(0, 1000, 1230);
				lifecheckLData.top =  new FormAttachment(0, 1000, 812);
				lifecheckLData.width = 92;
				lifecheckLData.height = 36;
				lifecheck.setLayoutData(lifecheckLData);
				lifecheck.setText("Lifecheck");
				lifecheck
						.setToolTipText("Check if all broker available. If where was the last response 15 minutes ago, the broker is not available");
				lifecheck.setEnabled(false);
				lifecheck.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						lifecheckWidgetSelected(evt);
					}
				});
			}
			{
				copyea = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData copyeaLData = new FormData();
				copyeaLData.left =  new FormAttachment(0, 1000, 1077);
				copyeaLData.top =  new FormAttachment(0, 1000, 747);
				copyeaLData.width = 90;
				copyeaLData.height = 30;
				copyea.setLayoutData(copyeaLData);
				copyea.setText("copy EA");
				copyea.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						copyeaWidgetSelected(evt);
					}
				});
			}
			{
				combo_targetbroker = new Combo(this, SWT.NONE);
				FormData targetbrokerLData = new FormData();
				targetbrokerLData.left =  new FormAttachment(0, 1000, 892);
				targetbrokerLData.top =  new FormAttachment(0, 1000, 747);
				targetbrokerLData.width = 140;
				targetbrokerLData.height = 33;
				combo_targetbroker.setLayoutData(targetbrokerLData);
				combo_targetbroker.setText("select target broker");
			}
			{
				configea = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData configeaLData = new FormData();
				configeaLData.left =  new FormAttachment(0, 1000, 673);
				configeaLData.top =  new FormAttachment(0, 1000, 784);
				configeaLData.width = 207;
				configeaLData.height = 30;
				configea.setLayoutData(configeaLData);
				configea.setText("config ea");
				configea.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						configeaWidgetSelected(evt);
					}
				});
			}
			{
				showbroker = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showbrokerLData = new FormData();
				showbrokerLData.left =  new FormAttachment(0, 1000, 1170);
				showbrokerLData.top =  new FormAttachment(0, 1000, 18);
				showbrokerLData.width = 176;
				showbrokerLData.height = 30;
				showbroker.setLayoutData(showbrokerLData);
				showbroker.setText("show files on broker");
				showbroker.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showbrokerWidgetSelected(evt);
					}
				});
			}
			{
				sync = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData syncLData = new FormData();
				syncLData.left =  new FormAttachment(0, 1000, 1047);
				syncLData.top =  new FormAttachment(0, 1000, 18);
				syncLData.width = 111;
				syncLData.height = 30;
				sync.setLayoutData(syncLData);
				sync.setText("sync broker");
				sync.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						syncWidgetSelected(evt);
					}
				});
			}
			{
				installautoea = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData installautoeaLData = new FormData();
				installautoeaLData.left =  new FormAttachment(0, 1000, 673);
				installautoeaLData.top =  new FormAttachment(0, 1000, 642);
				installautoeaLData.width = 207;
				installautoeaLData.height = 30;
				installautoea.setLayoutData(installautoeaLData);
				installautoea.setText("install auto ea on realaccount");
				installautoea.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						installautoeaWidgetSelected(evt);
					}
				});
			}
			{
				automaticOnOff = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData automaticOnOffLData = new FormData();
				automaticOnOffLData.left =  new FormAttachment(0, 1000, 673);
				automaticOnOffLData.top =  new FormAttachment(0, 1000, 676);
				automaticOnOffLData.width = 207;
				automaticOnOffLData.height = 30;
				automaticOnOff.setLayoutData(automaticOnOffLData);
				automaticOnOff.setText("toggle automatic On/Off");
				automaticOnOff.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						automaticOnOffWidgetSelected(evt);
					}
				});
			}
			{
				FormData progressBar2LData = new FormData();
				progressBar2LData.left =  new FormAttachment(0, 1000, 1500);
				progressBar2LData.top =  new FormAttachment(0, 1000, 28);
				progressBar2LData.width = 169;
				progressBar2LData.height = 18;
				progressBar2 = new ProgressBar(this, SWT.NONE);
				progressBar2.setLayoutData(progressBar2LData);
			}
			{
				stop = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData stopLData = new FormData();
				stopLData.left =  new FormAttachment(0, 1000, 2097);
				stopLData.top =  new FormAttachment(0, 1000, 7);
				stopLData.width = 36;
				stopLData.height = 23;
				stop.setLayoutData(stopLData);
				stop.setText("STOP");
				stop.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				stop.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						stopWidgetSelected(evt);
					}
				});
			}
			{
				loop = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData loopLData = new FormData();
				loopLData.left =  new FormAttachment(0, 1000, 1579);
				loopLData.top =  new FormAttachment(0, 1000, 7);
				loopLData.width = 49;
				loopLData.height = 21;
				loop.setLayoutData(loopLData);
				loop.setText("START");
				loop.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				loop.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						loopWidgetSelected(evt);
					}
				});
			}
			{
				FormData intervallLData = new FormData();
				intervallLData.left =  new FormAttachment(0, 1000, 1491);
				intervallLData.top =  new FormAttachment(0, 1000, 7);
				intervallLData.width = 19;
				intervallLData.height = 20;
				intervall = new Text(this, SWT.NONE);
				intervall.setLayoutData(intervallLData);
				intervall.setText("50");
			}
			{
				FormData messagelistLData = new FormData();
				messagelistLData.top =  new FormAttachment(0, 1000, 620);
				messagelistLData.width = 427;
				messagelistLData.height = 304;
				messagelistLData.left =  new FormAttachment(0, 1000, 1357);
				messagelist = new List(this, SWT.H_SCROLL | SWT.V_SCROLL
						| SWT.BORDER);
				messagelist.setLayoutData(messagelistLData);
				messagelist.setFont(SWTResourceManager.getFont("Segoe UI", 7,
						0, false, false));
			}
			{
				manualonoff = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData manualonoffLData = new FormData();
				manualonoffLData.left =  new FormAttachment(0, 1000, 673);
				manualonoffLData.top =  new FormAttachment(0, 1000, 712);
				manualonoffLData.width = 207;
				manualonoffLData.height = 30;
				manualonoff.setLayoutData(manualonoffLData);
				manualonoff.setText("toggle EA On/Off");
				manualonoff.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						manualonoffWidgetSelected(evt);
					}
				});
			}
			{
				button1 = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1LData = new FormData();
				button1LData.left =  new FormAttachment(0, 1000, 1824);
				button1LData.top =  new FormAttachment(0, 1000, 559);
				button1LData.width = 47;
				button1LData.height = 19;
				button1.setLayoutData(button1LData);
				button1.setText("on/off");
				button1.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				button1.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1WidgetSelected(evt);
					}
				});

			}
			{
				saveandexit = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData saveandexitLData = new FormData();
				saveandexitLData.left =  new FormAttachment(0, 1000, 892);
				saveandexitLData.top =  new FormAttachment(0, 1000, 902);
				saveandexitLData.width = 430;
				saveandexitLData.height = 52;
				saveandexit.setLayoutData(saveandexitLData);
				saveandexit.setText("save and exit");
				saveandexit.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						saveandexitWidgetSelected(evt);
					}
				});
			}
			{
				FormData showallprofitLData = new FormData();
				showallprofitLData.left =  new FormAttachment(0, 1000, 246);
				showallprofitLData.top =  new FormAttachment(0, 1000, 712);
				showallprofitLData.width = 117;
				showallprofitLData.height = 30;
				showallprofit = new Button(this, SWT.PUSH | SWT.CENTER);
				showallprofit.setLayoutData(showallprofitLData);
				showallprofit.setText("show all profit");
				showallprofit.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showallprofitWidgetSelected(evt);
					}
				});
			}
			{
				forgetoldeas = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData forgetoldeasLData = new FormData();
				forgetoldeasLData.left =  new FormAttachment(0, 1000, 892);
				forgetoldeasLData.top =  new FormAttachment(0, 1000, 642);
				forgetoldeasLData.width = 117;
				forgetoldeasLData.height = 24;
				forgetoldeas.setLayoutData(forgetoldeasLData);
				forgetoldeas.setText("forget old ea");
				forgetoldeas.setSelection(true);
			}
			{
				label4 = new Label(this, SWT.NONE);
				FormData label4LData = new FormData();
				label4LData.left =  new FormAttachment(0, 1000, 375);
				label4LData.top =  new FormAttachment(0, 1000, 18);
				label4LData.width = 54;
				label4LData.height = 26;
				label4.setLayoutData(label4LData);
				label4.setText("# eas");
			}
			{
				FormData anzeasLData = new FormData();
				anzeasLData.left =  new FormAttachment(0, 1000, 320);
				anzeasLData.top =  new FormAttachment(0, 1000, 18);
				anzeasLData.width = 48;
				anzeasLData.height = 26;
				anzeas = new Text(this, SWT.NONE);
				anzeas.setLayoutData(anzeasLData);
			}
			{
				FormData anzincommingtradesLData = new FormData();
				anzincommingtradesLData.left =  new FormAttachment(0, 1000, 126);
				anzincommingtradesLData.top =  new FormAttachment(0, 1000, 18);
				anzincommingtradesLData.width = 102;
				anzincommingtradesLData.height = 26;
				anzincommingtrades = new Text(this, SWT.NONE);
				anzincommingtrades.setLayoutData(anzincommingtradesLData);
			}
			{
				FormData progressBar1sideLData = new FormData();
				progressBar1sideLData.left =  new FormAttachment(0, 1000, 12);
				progressBar1sideLData.top =  new FormAttachment(0, 1000, 51);
				progressBar1sideLData.width = 18;
				progressBar1sideLData.height = 579;
				progressBar1side = new ProgressBar(this, SWT.VERTICAL);
				progressBar1side.setLayoutData(progressBar1sideLData);
			}
			{
				label1 = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 12);
				label1LData.top =  new FormAttachment(0, 1000, 18);
				label1LData.width = 108;
				label1LData.height = 26;
				label1.setLayoutData(label1LData);
				label1.setText("All Trades");
			}
			{
				showprofit = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showprofitLData = new FormData();
				showprofitLData.left =  new FormAttachment(0, 1000, 463);
				showprofitLData.top =  new FormAttachment(0, 1000, 719);
				showprofitLData.width = 110;
				showprofitLData.height = 30;
				showprofit.setLayoutData(showprofitLData);
				showprofit.setText("show profit");
				showprofit.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showprofitWidgetSelected(evt);
					}
				});
			}
			{
				deletebroker = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData deletebrokerLData = new FormData();
				deletebrokerLData.left =  new FormAttachment(0, 1000, 1531);
				deletebrokerLData.top =  new FormAttachment(0, 1000, 590);
				deletebrokerLData.width = 95;
				deletebrokerLData.height = 18;
				deletebroker.setLayoutData(deletebrokerLData);
				deletebroker.setText("delete broker");
				deletebroker.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				deletebroker.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						deletebrokerWidgetSelected(evt);
					}
				});
			}
			{
				addbroker = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData addbrokerLData = new FormData();
				addbrokerLData.left =  new FormAttachment(0, 1000, 1453);
				addbrokerLData.top =  new FormAttachment(0, 1000, 590);
				addbrokerLData.width = 78;
				addbrokerLData.height = 18;
				addbroker.setLayoutData(addbrokerLData);
				addbroker.setText("add broker");
				addbroker.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				addbroker.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						addbrokerWidgetSelected(evt);
					}
				});
			}

			{
				showopenorders = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData showopenordersLData = new FormData();
				showopenordersLData.left =  new FormAttachment(0, 1000, 10);
				showopenordersLData.top =  new FormAttachment(0, 1000, 722);
				showopenordersLData.width = 224;
				showopenordersLData.height = 20;
				showopenorders.setLayoutData(showopenordersLData);
				showopenorders.setText("show open orders");
				showopenorders.setSelection(false);
			}
			{
				showTradeliste = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showTradelisteLData = new FormData();
				showTradelisteLData.left =  new FormAttachment(0, 1000, 246);
				showTradelisteLData.top =  new FormAttachment(0, 1000, 676);
				showTradelisteLData.width = 117;
				showTradelisteLData.height = 30;
				showTradeliste.setLayoutData(showTradelisteLData);
				showTradeliste.setText("show tradelist");
				showTradeliste.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showTradelisteWidgetSelected(evt);
					}
				});
			}

			{
				configbroker = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData confLData = new FormData();
				confLData.left =  new FormAttachment(0, 1000, 1380);
				confLData.top =  new FormAttachment(0, 1000, 590);
				confLData.width = 73;
				confLData.height = 18;
				configbroker.setLayoutData(confLData);
				configbroker.setText("edit broker");
				configbroker.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				configbroker.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						configbrokerWidgetSelected(evt);
					}
				});

			}
			{
				FormData table3LData = new FormData();
				table3LData.left =  new FormAttachment(0, 1000, 1357);
				table3LData.top =  new FormAttachment(0, 1000, 49);
				table3LData.width = 430;
				table3LData.height = 505;
				table3 = new Table(this, SWT.FULL_SELECTION | SWT.H_SCROLL
						| SWT.V_SCROLL | SWT.BORDER);
				table3.setLayoutData(table3LData);
				table3.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
				table3.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						table3WidgetSelected(evt);
					}
				});

				table3.addTraverseListener(new TraverseListener()
				{
					public void keyTraversed(TraverseEvent evt)
					{
						table3KeyTraversed(evt);
					}
				});
				table3.addTouchListener(new TouchListener()
				{
					public void touch(TouchEvent evt)
					{
						table3Touch(evt);
					}
				});

			}
			{
				broker = new Label(this, SWT.NONE);
				FormData brokerLData = new FormData();
				brokerLData.left =  new FormAttachment(0, 1000, 10);
				brokerLData.top =  new FormAttachment(0, 1000, 642);
				brokerLData.width = 224;
				brokerLData.height = 20;
				broker.setLayoutData(brokerLData);
				broker.setText("broker");
			}
			{
				nocanceled = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData nocanceledLData = new FormData();
				nocanceledLData.left =  new FormAttachment(0, 1000, 10);
				nocanceledLData.top =  new FormAttachment(0, 1000, 696);
				nocanceledLData.width = 224;
				nocanceledLData.height = 20;
				nocanceled.setLayoutData(nocanceledLData);
				nocanceled.setText("no canceled orders");
				nocanceled.setSelection(true);
			}
			{
				profitnormalisierung = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData profitnormalisierungLData = new FormData();
				profitnormalisierungLData.left =  new FormAttachment(0, 1000, 10);
				profitnormalisierungLData.top =  new FormAttachment(0, 1000, 668);
				profitnormalisierungLData.width = 224;
				profitnormalisierungLData.height = 20;
				profitnormalisierung.setLayoutData(profitnormalisierungLData);
				profitnormalisierung.setText("0.1 Lot Profitnorm");
				profitnormalisierung.setEnabled(true);
				profitnormalisierung.setSelection(true);
				profitnormalisierung
						.setToolTipText("Ist die Normalisierung aktiv, wurden sämtliche Trades in 0.1 Lotsize berechnet. Die ursprüngliche Lotsize ist nich mehr im System.");
				profitnormalisierung
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								profitnormalisierungWidgetSelected(evt);
							}
						});
			}
			{
				FormData table2LData = new FormData();
				table2LData.width = 934;
				table2LData.height = 544;
				table2LData.top =  new FormAttachment(0, 1000, 54);
				table2LData.left =  new FormAttachment(0, 1000, 387);
				table2 = new Table(this, SWT.CHECK | SWT.FULL_SELECTION
						| SWT.BORDER);
				table2.setLayoutData(table2LData);
				table2.setFont(SWTResourceManager.getFont("Segoe UI",
						table2fontsize, 0, false, false));
				table2.addFocusListener(new FocusAdapter()
				{
					public void focusGained(FocusEvent evt)
					{
						table2FocusGained(evt);
					}
				});

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
				table1LData.width = 313;
				table1LData.height = 549;
				table1LData.top =  new FormAttachment(0, 1000, 51);
				table1LData.left =  new FormAttachment(0, 1000, 32);
				table1 = new Table(this, SWT.FULL_SELECTION | SWT.H_SCROLL
						| SWT.V_SCROLL | SWT.BORDER);

				table1.setLayoutData(table1LData);
				table1.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0,
						false, false));
			}
			{
				getAllData = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData getDataLData = new FormData();
				getDataLData.left =  new FormAttachment(0, 1000, 1358);
				getDataLData.top =  new FormAttachment(0, 1000, 7);
				getDataLData.width = 127;
				getDataLData.height = 38;
				getAllData.setLayoutData(getDataLData);
				getAllData.setText("reloadAllData");
				getAllData.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						getAllDataWidgetSelected(evt);
					}
				});
			}
			{
				label5 = new Label(this, SWT.NONE);
				FormData label5LData = new FormData();
				label5LData.left =  new FormAttachment(0, 1000, 242);
				label5LData.top =  new FormAttachment(0, 1000, 18);
				label5LData.width = 72;
				label5LData.height = 26;
				label5.setLayoutData(label5LData);
				label5.setText("Overview");
			}
			{
				label2 = new Label(this, SWT.NONE);
				FormData label2LData = new FormData();
				label2LData.left =  new FormAttachment(0, 1000, 1520);
				label2LData.top =  new FormAttachment(0, 1000, 7);
				label2LData.width = 53;
				label2LData.height = 20;
				label2.setLayoutData(label2LData);
				label2.setText("intervall");
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
							updateHistoryExporter = new MenuItem(fileMenu,
									SWT.CASCADE);
							updateHistoryExporter
									.setText("Update All HistoryExporter");
							updateHistoryExporter
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											updateHistoryexporterMenuItemWidgetSelected(evt);
										}
									});
						}
						{
							backup = new MenuItem(fileMenu, SWT.CASCADE);
							backup.setText("Backup");
							backup.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									BackupMenuItemWidgetSelected(evt);
								}
							});
						}

						{
							transfer = new MenuItem(fileMenu, SWT.CASCADE);
							transfer.setText("Transfer historyexporter.txt");
							transfer.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									TransferMenuItemWidgetSelected(evt);
								}
							});
						}
						{
							transferuserdata = new MenuItem(fileMenu,
									SWT.CASCADE);
							transferuserdata
									.setText("transfer to new computersystem");
							transferuserdata
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											TransferuserdataMenuItemWidgetSelected(evt);
										}
									});
						}
						{
							config = new MenuItem(fileMenu, SWT.CASCADE);
							config.setText("Config");
							config.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									ConfigMenuItemWidgetSelected(evt);
								}
							});
						}
						{
							info = new MenuItem(fileMenu, SWT.CASCADE);
							info.setText("license agreenment");

							info.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									InfoMenuItemWidgetSelected(evt);
								}
							});
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
							exitMenuItem
									.addSelectionListener(new SelectionAdapter()
									{
										public void widgetSelected(
												SelectionEvent evt)
										{
											ExitMenuItemWidgetSelected(evt);
										}
									});
						}
						{
							menuItem1 = new MenuItem(fileMenu, SWT.PUSH);
							menuItem1.setText("transfer to portable");
							menuItem1.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									menuItem1WidgetSelected(evt);
								}
							});
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
			}
			initlic();
			checkconfig();
			init();

			tradefilterrefresh();
			Mlist ml = new Mlist(messagelist, dis_glob);
			smw = new StartMonitorWork(dis_glob, progressBar1side, progressBar2);
			
			doAutostart();
			
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void doAutostart()
	{
		if(GlobalVar.getAutostartfeature()==1)
		{
			getAllData();
			//einen timer so ein richten das er nach 5 sekunden ausgelöst wird und dann mit dem timer-event
			//dieses Kommando "	smw.startAllMt(); " ausgeführt wird
			smw.startAllMt(0);
			//timer setzen der in 5 sekunden auslöst
		}
	}
	
	
	private void checkconfig()
	{
		// das konfigmenue aufrufen falls noch keine config gesetzt ist
		if ((GlobalVar.getNetzwerkshareprefix() == null)
				|| (GlobalVar.getNetzwerkshareprefix().length() < 2))
		{
			SwtEditGlobalConfig sc = new SwtEditGlobalConfig();
			Mbox.Infobox("Please set Config");
			sc.init(dis_glob);
			
			
			
			String headline = GlobalVar.calcHeadline();

			if (dis_glob.getActiveShell() != null)
				dis_glob.getActiveShell().setText(headline);
		}
	}

	private void initlic()
	{
		if (GlobalVar.getForgetdays() == 0)
		{
			GlobalVar.setForgetdays(30);
			GlobalVar.save();
		}
		if (5 == 4)
		{
			// freeware version
			if (Lic.getlic() == 0)
			{
				// noch mehr löschen
				showbacktest.setVisible(false);
				button2newcal.setVisible(false);
				button2delete.setVisible(false);
			}

			// monitorversion
			if ((Lic.getlic() == 0) || (Lic.getlic() == 1))
			{
				loop.setVisible(false);
				stop.setVisible(false);
				installautoea.setVisible(false);
				automaticOnOff.setVisible(false);
				manualonoff.setVisible(false);
				setinstfrom.setVisible(false);
				configea.setVisible(false);
				importtradelisten.setVisible(false);
				buttonExportTradelist.setVisible(false);
				exportEa.setVisible(false);
				removeimported.setVisible(false);

				lifecheck.setVisible(false);
				checkGd20.setVisible(false);
				intervall.setVisible(false);
				label2.setVisible(false);
				progressBar2.setVisible(false);
				sync.setVisible(false);
				combo_targetbroker.setVisible(false);
				copyea.setVisible(false);

				button2showallprotfolio.setVisible(false);
				checkallbroker.setVisible(false);
				profitnormalisierung.setVisible(false);
				button1.setVisible(false);
				loadexpiredhistory.setVisible(false);
				button1.setVisible(false);
			}
		}
	}

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{
		LockTradeliste us = new LockTradeliste();
		String userdir = System.getProperty("user.dir");
		String version = "Version V0.44xx";
		
		 String serverip2= "127.0.0.1"; 
		String serverip1 = "127.0.0.1";
		 
		String updatemessage = "";
		
		dis_glob = new Display();// Display.getDefault();

		if (args.length > 0)
			if (args[0] != null)
				userdir = args[0];
		if (userdir.contains("bin") == false)
		{
			Tracer.WriteTrace(10, "Falscher Verzeichnissaufbau userdir<"
					+ userdir + ">");
			System.exit(0);
		}

		// den Rootpath setzen
		userdir = userdir.substring(0, userdir.lastIndexOf("\\"));
		Rootpath glob = new Rootpath(userdir, userdir + "\\conf");
		// global Var einmal einlesen, ganz wichtig !!
		GlobalVar gv = new GlobalVar();
		GlobalVar.setSilentmode(1);

		Tracer.WriteTrace(20, "I: rootpath="+userdir);
		
		// falls auf dem lokalen pc (tnickel-pc) ein testserver läuft
		
		if (Sys.getHostname().equalsIgnoreCase("tnickel-pc") == true)
			GlobalVar.setServerip1(serverip1);
		else
			GlobalVar.setServerip1(serverip2);
		GlobalVar.setServerip2(serverip2);
		
		Shell shell = new Shell(dis_glob);
		Updater.checkNewUpdate();
		Lic lic = new Lic();
		shell.setText(GlobalVar.calcHeadline());

		// falls keine Seriennummer da ist dann setzen
		if (GlobalVar.getSernumber() == 0)
		{
			Random rn = new Random();
			int sernum = Math.abs((rn.nextInt() % 10000)) + 1;
			GlobalVar.setSernumber(sernum);
			GlobalVar.save();
		}

		// den alten update sync freigeben
		 //us.delSync();

		MonitorClient.main(new String[]
		{ "start client" });

		GlobalVar.setVersion(version);
		DisTool dis = new DisTool(dis_glob, shell);

		StartMonitor inst = new StartMonitor(shell, SWT.NULL);
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

			if (!dis_glob.readAndDispatch())
			{
				// System.out.println("sleep");
				dis_glob.sleep();
			}
		}
		// den ip-thread stoppen
		GlobalVar.setIpthreadflag(0);
	}

	private void init()
	{
		MetaStarter.KillAllMetatrader();

		if ((Lic.getlic() == 0))
			GlobalVar.setUpdatechannel("freeware");

		progressBar2.setMinimum(0);
		progressBar2.setMaximum(5);
		statustext.setText(GlobalVar.getIpmessage());

	}

	final Runnable timer = new Runnable()
	{
		public void run()
		{
			if (loopflag == 0)
				return;
			smw.checkGd20(table1, table2, dis_glob, anzincommingtrades, anzeas,
					broker, 1);
			// System.out.println("timer");
			if (loopflag == 1)
			{
				// nach 300sekunden wiederholen
				int waitseconds = SG.get_zahl(intervall.getText()) * 1000 * 60;
				Display.getDefault().timerExec(waitseconds, this);
			}
		}
	};

	final Runnable balkenlauf = new Runnable()
	{
		public void run()
		{
			laufBalken();
			// System.out.println("balkenlauf");
			if (loopflag == 1)
				Display.getDefault().timerExec(1000, this);
		}
	};

	private void laufBalken()
	{
		progresscount++;
		if (progresscount == 6)
			progresscount = 0;
		progressBar2.setSelection(progresscount);
	}

	private void setBrokermode(int mode)
	{
		// brokermode=0 alle broker
		if (mode == 0)
		{
			// einige buttons ausschalten
			installautoea.setEnabled(false);
			automaticOnOff.setEnabled(false);
			manualonoff.setEnabled(false);
			sync.setEnabled(true);
			setinstfrom.setEnabled(false);
			configea.setEnabled(false);
			exportEa.setEnabled(false);
			importtradelisten.setEnabled(false);
			removeimported.setEnabled(false);
			buttonExportTradelist.setEnabled(false);
		}
		if (mode == 1)
		{
			// brokermode=1 nur einen broker angeklickt
			// buttons wieder einschalten

			toggleallprofits.setEnabled(true);
			installautoea.setEnabled(true);
			automaticOnOff.setEnabled(true);
			manualonoff.setEnabled(true);
			sync.setEnabled(false);

			configea.setEnabled(true);

			// falls realbroker
			if (smw.isSelectedBroker(2) == true)
			{
				exportEa.setEnabled(false);
				setinstfrom.setEnabled(true);
			} else
			{
				exportEa.setEnabled(true);
				setinstfrom.setEnabled(false);
			}

			buttonExportTradelist.setEnabled(true);
			importtradelisten.setEnabled(true);
			removeimported.setEnabled(true);
		}
		Tradeanzahl ta = smw.calcTradeanzahl();
		anzincommingtrades.setText(String.valueOf(ta.getAnztrades()));
		anzeas.setText(String.valueOf(ta.getAnzprofits()));
	}

	private void tradefilterrefresh()
	{
		// den tradefilter setzen
		tf.setNocancel(nocanceled.getSelection());
		tf.setProfitnormalisierung(profitnormalisierung.getSelection());
		tf.setShowopenorders(showopenorders.getSelection());
		tf.setForgetoldeas(forgetoldeas.getSelection());
		tf.setLoadexpired(loadexpiredhistory.getSelection());
		tf.setTradefilterbutton(button2.getSelection());
		tf.setTradestartdate(text1from.getText());
	}

	private void getAllDataWidgetSelected(SelectionEvent evt)
	{
		getAllDataWidget();
	}

	private void getAllDataWidget()
	{
		// Nur jede 15 minuten beim Server checken
		Date aktdate = Mondate.getAktDate();

		
		MonitorClient.main(new String[]
		{ "start client" });

		// alle aktivierten Broker laden
		getAllData();
		statustext.setText(GlobalVar.getIpmessage());
	}

	private void getAllData()
	{
		// fourceflag= wenn forceflag =1 wird die anzeige aktuallisiert
		
		

		

		// beim ersten mal
		if (loadallbrokerflag == 0)
		{
			loadallbrokerflag = 1;
			
			getAllData.setText("showAllData");
		}
		getAllData.setEnabled(false);
		setBrokermode(0);
		broker.setText("all brokers");
		tradefilterrefresh();
		smw.loadallbroker(dis_glob, table1, table2, table3, tf,
				anzincommingtrades, anzeas, broker, 1, 1);
		smw.initTargetBrokerCombo(combo_targetbroker);

	
		smw.exportAllTradelistEncryped();

		getAllData.setEnabled(true);
	}

	private void profitnormalisierungWidgetSelected(SelectionEvent evt)
	{
		System.out.println("profitnormalisierung.widgetSelected, event=" + evt);
		// TODO add your code for profitnormalisierung.widgetSelected
		tradefilterrefresh();
	}

	private void table3WidgetSelected(SelectionEvent evt)
	{
		// Button:Hier wurde ein Broker in der Brokerliste selektiert
		// d.h. für diesen Broker werden die Trades geladen und angezeigt
		System.out.println("table3.widgetSelected, event=" + evt);

		DisTool.waitCursor();
		tradefilterrefresh();
		String seltext = evt.item.toString();

		String name = "";

		if (seltext.contains("{"))
		{
			String sel = seltext.substring(seltext.indexOf("{") + 1);
			name = sel.replace("}", "");

		} else
			Tracer.WriteTrace(10, "error internal jdfjfj");

		int forceflag = 0;
		if (loadallbrokerflag == 0)
			forceflag = 1;

		smw.brokerselected(name, tf, table1, table2, table3, broker, dis_glob,
				1, forceflag);

		DisTool.arrowCursor();
		setBrokermode(1);
	}

	private void configbrokerWidgetSelected(SelectionEvent evt)
	{
		// Button: Edit config
		System.out.println("configbroker.widgetSelected, event=" + evt);
		smw.editconfig(table3);
	}

	private void showTradelisteWidgetSelected(SelectionEvent evt)
	{
		// Button: hier wird die Tradeliste dargestellt
		System.out.println("showTradeliste.widgetSelected, event=" + evt);
		smw.showtradelist1();
	}

	private void updateHistoryexporterMenuItemWidgetSelected(SelectionEvent evt)
	{
		// File-Requester: der Historyexporter wird upgedated
		System.out
				.println("updateHistoryexporterMenuItem.widgetSelected, event="
						+ evt);
		smw.updatehistoryexporter(table3);
	}

	private void BackupMenuItemWidgetSelected(SelectionEvent evt)
	{
		smw.backup();
	}

	private void TransferMenuItemWidgetSelected(SelectionEvent evt)
	{
		if (Lic.getlic() != 5)
		{
			Mbox.Infobox("Not allowed in demo/freewareversion");
			return;
		}
		Mbox.Infobox("Warning: Funktion is very dangerous, call this only if you reinstall all metatrader !!!");

		smw.transfer();
	}

	private void TransferuserdataMenuItemWidgetSelected(SelectionEvent evt)
	{
		if (Lic.getlic() != 5)
		{
			Mbox.Infobox("Not allowed in demo/freewareversion");
			return;
		}
		Mbox.Infobox("Warning: Funktion is very dangerous, call this only if you transfer all metatrader !!!");

		smw.transferuserdata();
	}

	private void ConfigMenuItemWidgetSelected(SelectionEvent evt)
	{
		SwtEditGlobalConfig sc = new SwtEditGlobalConfig();
		sc.init(dis_glob);
		dis_glob.getActiveShell().setText(GlobalVar.calcHeadline());
	}

	private void InfoMenuItemWidgetSelected(SelectionEvent evt)
	{

		GlobalVar.setShowLicenseflag(0);
		showLicense();
	}

	private void showLicense()
	{

		SwtShowLicense sf = new SwtShowLicense();
		String licensefile = Rootpath.getRootpath() + "\\info\\license.txt";
		File lfile = new File(licensefile);
		long len = lfile.length();
		if (len != 3543)
		{
			Mbox.Infobox("File <" + licensefile + "> is corrupt");
			System.exit(99);
		}

		sf.init(dis_glob, licensefile);

	}

	private void ExitMenuItemWidgetSelected(SelectionEvent evt)
	{
		smw.saveAndExit();
	}

	private void table2WidgetSelected(SelectionEvent evt)
	{
		// Hier wurde auf ein Element der Tabelle 2 geklickt
		// Hier wurde also auf einen EA-geklickt
		System.out.println("table2.widgetSelected, event=" + evt);

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

		int index = SG.get_zahl(name);
		smw.table2clicked(index, name, selonlyone, table2);
	}

	private void addbrokerWidgetSelected(SelectionEvent evt)
	{
		// add new broker
		System.out.println("addbroker.widgetSelected, event=" + evt);
		smw.addnewbroker(table3);
	}

	private void deletebrokerWidgetSelected(SelectionEvent evt)
	{
		// delete broker button
		System.out.println("deletebroker.widgetSelected, event=" + evt);
		smw.deletebroker(table3);
	}

	private void showprofitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("showprofit.widgetSelected, event=" + evt);
		smw.showProfitGraphik();
	}

	private void showallprofitWidgetSelected(SelectionEvent evt)
	{
		// Alle profite der selektierten EA anzeigen
		System.out.println("showallprofit.widgetSelected, event=" + evt);

		if (button2best100.getSelection() == true)
			smw.showallprofit(tf, 0, 100);
		else
			smw.showallprofit(tf, 0, 99999999);
	}

	private void saveandexitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("saveandexit.widgetSelected, event=" + evt);
		// TODO add your code for saveandexit.widgetSelected
		smw.saveAndExit();
	}

	private void button1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1.widgetSelected, event=" + evt);
		// on/off button
		// die selektierte config on/off schalten
		smw.toggleOnOffBroker(table3);

	}

	private void loopWidgetSelected(SelectionEvent evt)
	{
		System.out.println("loop.widgetSelected, event=" + evt);
		// loop start
		loopflag = 1;

		getAllData.setEnabled(false);
		sync.setEnabled(false);
		loop.setEnabled(false);

		System.out.println("getData.widgetSelected, event=" + evt);
		Display.getDefault().timerExec(1000, timer);
		broker.setText("run automatic");
		tradefilterrefresh();

		// den laufbalken starten
		Display.getDefault().timerExec(1000, balkenlauf);
		checkGd20.setEnabled(false);

	}

	private void stopWidgetSelected(SelectionEvent evt)
	{
		System.out.println("stop.widgetSelected, event=" + evt);
		// TODO add your code for stop.widgetSelected
		loopflag = 0;

		getAllData.setEnabled(true);
		sync.setEnabled(true);
		loop.setEnabled(true);

		broker.setText("Automatic Stopped");
		checkGd20.setEnabled(true);
		progressBar2.setSelection(5);

	}

	private void installautoeaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("installautoea.widgetSelected, event=" + evt);
		// Installiert einen AutoEa
		smw.installAutoEa();
	}

	private void automaticOnOffWidgetSelected(SelectionEvent evt)
	{
		System.out.println("automaticOnOff.widgetSelected, event=" + evt);
		// TODO add your code for automaticOnOff.widgetSelected
		smw.toogleOnOffAutomatic();
	}

	private void manualonoffWidgetSelected(SelectionEvent evt)
	{
		System.out.println("manualonoff.widgetSelected, event=" + evt);
		// Schaltet den EA ein oder aus
		// geht aber nur wenn keine automatic eingeschaltet ist
		smw.toggleOnOffEa();
	}

	private void syncWidgetSelected(SelectionEvent evt)
	{
		System.out.println("sync.widgetSelected, event=" + evt);
		// Sync broker
		// hier wird nachgeschaut welche EA denn auch wirklich installiert ist
		// und ob er ein oder ausgeschaltet ist
		smw.syncBroker(table2);
	}

	private void showbrokerWidgetSelected(SelectionEvent evt)
	{
		System.out.println("showbroker.widgetSelected, event=" + evt);
		// Show broker
		smw.showBroker(dis_glob);
	}

	private void configeaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("logic.widgetSelected, event=" + evt);
		// TODO add your code for logic.widgetSelected
		smw.ConfigEa();
	}

	private void copyeaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("copyea.widgetSelected, event=" + evt);
		// copy ea
		String zielbrokername = combo_targetbroker.getText();
		smw.copyEA(zielbrokername);
	}

	private void lifecheckWidgetSelected(SelectionEvent evt)
	{
		System.out.println("lifecheck.widgetSelected, event=" + evt);
		// hier werden alle Metatrader überprüft ob sie noch laufen
		// Es wird das datum von history.txt ausgewertet.
		// ist es älter als 15 Minuten läuft der Broker nicht
		smw.lifecheck(table3);
	}

	private void checkGd20WidgetSelected(SelectionEvent evt)
	{
		System.out.println("checkGd20.widgetSelected, event=" + evt);
		// TODO add your code for checkGd20.widgetSelected
		// läd alle trades und überprüft ob der GD20 überschritten worden ist
		checkGd20.setEnabled(false);
		smw.checkGd20(table1, table2, dis_glob, anzincommingtrades, anzeas,
				broker, 1);
		checkGd20.setEnabled(true);
	}

	private void clrWidgetSelected(SelectionEvent evt)
	{
		System.out.println("clr.widgetSelected, event=" + evt);
		Mlist.clear();
	}

	private void checkallWidgetSelected(SelectionEvent evt)
	{
		System.out.println("checkall.widgetSelected, event=" + evt);
		// check all in table2
		smw.tooggleallprofits();
	}

	private void showonofflogWidgetSelected(SelectionEvent evt)
	{
		System.out.println("showonofflog.widgetSelected, event=" + evt);
		smw.showOnOffLog();
	}

	private void searchbuttonWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2.widgetSelected, event=" + evt);
		// button2 searchbutton gedrückt
		String searchid = search.getText();
		smw.searchSelId(searchid);
	}

	private void table2FocusGained(FocusEvent evt)
	{
		System.out.println("table2.focusGained, event=" + evt);
		// TODO add your code for table2.focusGained
	}

	private void setinstfromWidgetSelected(SelectionEvent evt)
	{
		System.out.println("setinstfrom.widgetSelected, event=" + evt);
		// TODO add your code for setinstfrom.widgetSelected
		smw.setInstfromDemobroker();
	}

	private void settradefilterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("settradefilter.widgetSelected, event=" + evt);
		// set Tradfilter button gedrückt
		smw.TradefilterConfig(tf);
		tradefilter.setSelection(true);

	}

	private void fontplusWidgetSelected(SelectionEvent evt)
	{
		System.out.println("fontplus.widgetSelected, event=" + evt);
		// TODO add your code for fontplus.widgetSelected
		table2fontsize++;
		table2.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
	}

	private void fontminusWidgetSelected(SelectionEvent evt)
	{
		System.out.println("fontminus.widgetSelected, event=" + evt);
		// TODO add your code for fontminus.widgetSelected
		table2fontsize--;
		table2.setFont(SWTResourceManager.getFont("Segoe UI", table2fontsize,
				0, false, false));
	}

	private void showselprofitsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("showselprofits.widgetSelected, event=" + evt);
		// TODO add your code for showselprofits.widgetSelected
		// hier werden die selektierten EA in einer gemeinssamen graphik
		// angezeigt

		smw.showselprofits(table2);
	}

	private void importeasWidgetSelected(SelectionEvent evt)
	{
		System.out.println("importeas.widgetSelected, event=" + evt);
		// TODO add your code for importeas.widgetSelected
		smw.importTradelist();
	}

	private void exporteaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("exportea.widgetSelected, event=" + evt);
		// TODO add your code for exportea.widgetSelected
		smw.exportOneEaAndTradelist();
	}

	private void removeimportedWidgetSelected(SelectionEvent evt)
	{
		System.out.println("removeimported.widgetSelected, event=" + evt);
		// die importierten Trades werden für einen bestimmten Broker wieder aus
		// der
		// datenbasis geworfen
		smw.removeImported();
	}

	private void showbacktestWidgetSelected(SelectionEvent evt)
	{
		System.out.println("showbacktest.widgetSelected, event=" + evt);
		// show backtest button
		smw.showBacktest(button2newcal.getSelection());
	}

	private void comparetradelistWidgetSelected(SelectionEvent evt)
	{
		System.out.println("comparetradelist.widgetSelected, event=" + evt);
		// compare Tradelist
		smw.comparetradelist();
	}

	private void tradefilterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("tradefilter.widgetSelected, event=" + evt);
		// TODO add your code for tradefilter.widgetSelected
		// der tradefilter wurde selektiert
		tf.setTradefiltermainselection(tradefilter.getSelection());
	}

	private void sortcriteriaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("sortcriteria.widgetSelected, event=" + evt);
		smw.setSortkriteria();
	}

	private void buttonupWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttonup.widgetSelected, event=" + evt);
		// up-button für den broker
		smw.brokerchangeposition(0, table3);
	}

	private void buttondownWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttondown.widgetSelected, event=" + evt);
		// down-button für den broker
		smw.brokerchangeposition(1, table3);
	}

	private void restartWidgetSelected(SelectionEvent evt)
	{
		System.out.println("restart.widgetSelected, event=" + evt);
		// restart all metatrader
		DisTool.waitCursor();
		smw.restartAllMetatrader();
		DisTool.arrowCursor();
	}

	private void loadexpiredhistoryWidgetSelected(SelectionEvent evt)
	{
		System.out.println("loadexpiredhistory.widgetSelected, event=" + evt);
		// TODO add your code for loadexpiredhistory.widgetSelected
		tradefilterrefresh();
	}

	private void table3Touch(TouchEvent evt)
	{
		System.out.println("table3.touch, event=" + evt);
		// TODO add your code for table3.touch
	}

	private void table3KeyTraversed(TraverseEvent evt)
	{
		System.out.println("table3.keyTraversed, event=" + evt);
		// TODO add your code for table3.keyTraversed
	}

	private void buttonExportTradelistWidgetSelected(SelectionEvent evt)
	{
		System.out
				.println("buttonExportTradelist.widgetSelected, event=" + evt);
		// TODO add your code for buttonExportTradelist.widgetSelected
		smw.exportTradelist();
	}

	private void button2reloadWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2reload.widgetSelected, event=" + evt);
		// TODO add your code for button2reload.widgetSelected
		loadallbrokerflag = 0;
		getAllData();
	}

	private void button2deleteWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2delete.widgetSelected, event=" + evt);
		// TODO add your code for button2delete.widgetSelected
		smw.deleteEas();
	}

	private void text1daysKeyTraversed(TraverseEvent evt)
	{
		System.out.println("text1days.keyTraversed, event=" + evt);
		// TODO add your code for text1days.keyTraversed

		saveForgetdays();
	}

	private void saveForgetdays()
	{

		String inptext = text1days.getText();
		String stext = inptext;

		// das return rausfiltern
		stext = inptext.replace("\r", "");
		stext = stext.replace("\n", "");

		// die globale variable setzen
		GlobalVar.setForgetdays(SG.get_zahl(stext));

		// falls logischer wert dann speichern
		if (GlobalVar.getForgetdays() > 0)
		{
			text1days.clearSelection();
			text1days.setText(String.valueOf(GlobalVar.getForgetdays()));

			GlobalVar.save();
		}
	}

	private void text1daysKeyPressed(KeyEvent evt)
	{
		System.out.println("text1days.keyPressed, event=" + evt);
		// TODO add your code for text1days.keyPressed

		saveForgetdays();

	}

	private void text1daysModifyText(ModifyEvent evt)
	{
		System.out.println("text1days.modifyText, event=" + evt);
		// TODO add your code for text1days.modifyText
		saveForgetdays();
	}

	private void exportAlleTrades()
	{

		String fnam = GC.rootpath + "\\data\\alltradeliste.xml.gzip";
		File alltradeliste = new File(fnam);
		if (alltradeliste.exists())
			alltradeliste.delete();
	}

	private void button2showallprotfolioWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2showallprotfolio.widgetSelected, event="
				+ evt);
		// TODO add your code for button2showallprotfolio.widgetSelected
		smw.showallprofit(tf, 1, 99999999);
	}

	private void button2autoconfigWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2autoconfig.widgetSelected, event=" + evt);
		// TODO add your code for button2autoconfig.widgetSelected
		smw.autoconfig();
	}

	private void button2exportallWidgetDefaultSelected(SelectionEvent evt)
	{
		System.out.println("button2exportall.widgetDefaultSelected, event="
				+ evt);
		// TODO add your code for button2exportall.widgetDefaultSelected
	}

	private void button2importallWidgetDefaultSelected(SelectionEvent evt)
	{
		System.out.println("button2importall.widgetDefaultSelected, event="
				+ evt);
		// TODO add your code for button2importall.widgetDefaultSelected
	}

	private void button2importallWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2importall.widgetSelected, event=" + evt);
		// TODO add your code for button2importall.widgetSelected
		smw.importAllTradelist();
		forgetoldeas.setSelection(false);
	}

	private void button2exportallWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2exportall.widgetSelected, event=" + evt);
		// TODO add your code for button2exportall.widgetSelected
		// export all trades
		// alle Tradelisten werden in ein File exportiert
		smw.exportAllTradelist();
	}

	private void button2startmetatraderWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2startmetatrader.widgetSelected, event="
				+ evt);
		// TODO add your code for button2startmetatrader.widgetSelected

		smw.startAllMt(1);
	}

	private void button2stopmtWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2stopmt.widgetSelected, event=" + evt);
		// TODO add your code for button2stopmt.widgetSelected
		smw.stopAllMt();
	}

	private void thisWidgetDisposed(DisposeEvent evt)
	{
		System.out.println("this.widgetDisposed, event=" + evt);
		// TODO add your code for this.widgetDisposed
		// Es wurde auf das X-geklickt (dispose)
		MetaStarter.KillAllMetatrader();
	}

	private void button2showtradelistportfolioWidgetSelected(SelectionEvent evt)
	{
		System.out
				.println("button2showtradelistportfolio.widgetSelected, event="
						+ evt);
		smw.showselprofitstradelistportfolio(table2);
	}

	// start trades filter button
	private void button2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2.widgetSelected, event=" + evt);
		// wenn die selection box aktiviert ist werden alle trades die früheres
		// datum haben gelöscht
		tf.setTradefilterbutton(button2.getSelection());
		tf.setTradestartdate(text1from.getText());
	}
	
	private void menuItem1WidgetSelected(SelectionEvent evt) {
		System.out.println("menuItem1.widgetSelected, event="+evt);
		//transfer to portable
		//copy all ....appdadata/4545454.4545454/ to the $Metatraderroot
		//so all metatrader can be converted to portable version
		
		smw.convertToPortable();
	}

}
