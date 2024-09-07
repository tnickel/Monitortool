package start;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;

import cluster.Cluster;
import data.Commentflags;
import data.Conf100Tools;
import data.Endtestdata;
import data.GlobToolbox;
import data.Metriktools;
import data.RtTools;
import data.Toolboxconf;
import data.TradefilterVerarbeitung;
import gui.CfxDateViewer;
import gui.Guitools;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.SG;
import hiflsklasse.SWTwindow;
import hiflsklasse.Tracer;
import montool.MonDia;
import pricedataseries.PriceDataSeries;
import sq4xWorkflow.SqGoogle;
import sq4xWorkflow.SqWorkflowMaster;
import work.CommentWork;

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
public class StartToolbox extends org.eclipse.swt.widgets.Composite
{
	
	private Menu menu1;
	
	private Label CTlabel2stabilminval;
	private Text CTtext1stabilminval;
	private Button CTbutton2stabilitaet;
	private Button CTbutton1netprofit;
	private Button button1werteeinzelaus300;
	private CTabItem cTabItem3;
	private Label label11;
	private CLabel cLabel2;
	private CLabel cLabel1;
	private Label label11maxddrobustdiff;
	private Text text2maxddrobustdiff;
	private Button button6calc100;
	private Button button6conf100_50destin;
	private Text conf100_50destinationdir;
	private Button button6set100sourcedir;
	private Text text1_confsource;
	private Group group1conf100_50;
	private Composite composite9;
	private CTabItem cTabItem2;
	private Text text1attributname;
	private Button button9calculate;
	private Button button8zielmetrik;
	private Text text3zielmetrik;
	private Button button7conf50;
	private Text text2conf50;
	private Button button6conf100;
	private Text text1conf100;
	private Group group11;
	private Composite composite8;
	private Composite composite7;
	private Button button5;
	private Label label10;
	private Text text1maxdd;
	private Label label9;
	private Text text1budget;
	private Button button5calcAutomaticNettoprofit;
	private Label label8;
	private Text text1confidenz;
	private Label label7;
	private Button button5NetProfitRobust;
	private Button button5OssNettoprofit;
	private Button button5IsNettoprofit;
	private Label label6;
	private Text text1ndays;
	private Text text1maxddtrail;
	private Button button4;
	private Button button3;
	private Text text1maxddfromstart;
	private Label label5;
	private Composite composite6;
	private Label label4;
	private Composite composite5;
	private Label label3;
	private Label label2;
	private Label label1;
	private Composite composite4;
	private Composite composite3;
	private Composite composite2;
	private Button button2;
	private Button button1;
	private Text text1maxddproz;
	private Text maxdd;
	private Button button1calcEndtestgraphic;
	private Button button1deleteEndtest;
	private Button button1generateEndtest;
	private Button button1calcNettoprofits;
	private CTabItem cTabItem1;
	private Text CTtext1netprofitminval;
	private Label CTlabel1pref;
	private Button button6Lastyearsflag;
	private Button button7pdc;
	private Label label22pdc;
	private Text text3;
	private Button button6;
	private Text text3pdc;
	private Button button6pdc;
	private Text text2;
	private Group priceDataSeries;
	private Composite composite12;
	private CTabItem cTabItem6;
	private Button button6cleartradefilter;
	private Button button6filterstrategies;
	private Button button6calcmatchingtable;
	private Button button6tradefilterrootdir;
	private Text text1;
	private Label label21;
	private Text text1proz;
	private Composite composite11;
	private CTabItem cTabItem4;
	private Button button6deleteifequal;
	private Button button6showtable;
	private Label label20;
	private Text text1faktor;
	private Button button6longshort;
	private Label label19;
	private Text text1equityfaktor;
	private Button button6cut2years;
	private Label label18;
	private Label label17;
	private Text text1monthsPerSegment;
	private Label label17rtdestdir;
	private Label label17rtsourcedir;
	private Button button6rtCheckequity;
	private Button button6rt_checkrt;
	private Label label14;
	private Label label16;
	private Label label12;
	private Label label15;
	private Label label13;
	private Text text1maxlosses;
	private Button button6calcRtFilter;
	private Text faktorMaxDrawdownRobust;
	private Button button6setRT_destdir;
	private Button button6set_rt_sourcedir;
	private Text rt_destdir;
	private Text rt_sourcedir;
	private Group group1;
	private Button Cluster_StartGeneration;
	private Button ClusterSourceDir;
	private Button clusterStrDestdir;
	private Text ClusterDestDir;
	private Text cluster_strsourcedir;
	private Composite composite10;
	private Button CTbutton4addComment;
	private Text CTtext3prefix;
	private Button CTbutton3clean;
	private Button CTbutton2;
	private Label CTlabel1;
	private Button CTbutton1setdir;
	private Text CTtext2verzeichniss;
	private Text CTtext1prefix;
	
	private Composite composite1;
	private Metriktools metriktools = new Metriktools();
	private Conf100Tools conf100tools = new Conf100Tools();
	private RtTools rttools = new RtTools();
	private CTabItem cTabItem5;
	private CTabFolder cTabFolder1;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private Button button9showfile;
	private Button button9copyinshared;
	private Label label34;
	private Text GetEnddateFromDatabase;
	private Label label33;
	private Button MaxStepsBackFlag;
	private Text maxstepsback;
	private Button fullreportselection;
	private Button nomalizeselection;
	private Text normn;
	private Label label32;
	private Button normnflag;
	private Text nomationn;
	private Button SelektorUseDeltaDaysConfigfile;
	private Button selektorShiftDays;
	private Button setdeltadays;
	private Text deltadaysconfigfile;
	private Label label31;
	private Combo combo1cpart;
	private Text text4databankname;
	private Label label30;
	private Label label29;
	private Text text4magicprefix;
	private Button button9showresults;
	private Button button9backupdrive;
	private Button button9shareddrive;
	private Button button8;
	private Text text4workflowstore;
	private Label label29anzfiles;
	private Button button8clearprojectdir;
	private Button button8showprojectdir;
	private Label label28;
	private Text text4resultdir;
	private Text text4shareddrive;
	private Text text4backupdrive;
	private Button button7;
	private Button button8shareddirectory;
	private Button button8setbackupdrive;
	private Button button8showResultdir;
	private Button button8showshareddir;
	private Button button8showbackupdir;
	private Label label27;
	private Button button8saveinfomassage;
	private Text text4infotext;
	private Label label26;
	private Label label25;
	private Text text4stepsfuture;
	private Button collectresultsbutton;
	private Label label24;
	private Text outputname;
	private Label label23;
	private Text steps;
	private Label label22;
	private Button button7calc;
	private Text text4filterkeyword;
	private Button button7setdestdir;
	private Button button7set;
	private Text text4filterdestdir;
	private Text FilterSourceDir;
	private Group group2filter;
	private Composite composite13;
	private CTabItem cTabItem7;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	
	private CommentWork cw_glob = null;
	private Commentflags cf_glob = null;
	private Endtestdata enddata_glob = null;
	private String clustersourcedir_glob = null;
	private String clusterdestdir_glob = null;
	private String pdc_inputfile_glob = null;
	private String pdc_outputfile_glob = null;
	private String filter_indir_glob = null;
	private String filter_outdir_glob = null;
	private String filter_keyword_glob = null;
	private SqWorkflowMaster sqprojects = new SqWorkflowMaster();
	
	// golbal Tradelistenverarbeitung
	TradefilterVerarbeitung trverarbeit = new TradefilterVerarbeitung();
	
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	public StartToolbox(Composite parent, int style)
	{
		super(parent, style);
		Toolboxconf tc = new Toolboxconf(GlobToolbox.getRootpath());
		initGUI();
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void initGUI()
	{
		try
		{
			init();
			this.setSize(1611, 904);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			this.setLayout(null);
			{
				cTabFolder1 = new CTabFolder(this, SWT.NONE);
				
				cTabFolder1.setBounds(12, 19, 1639, 870);
				{
					
					cTabItem5 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem5.setText("commenttool");
					{
						composite1 = new Composite(cTabFolder1, SWT.NONE);
						composite1.setLayout(null);
						cTabItem5.setControl(composite1);
						composite1.setEnabled(true);
						{
							CTtext2verzeichniss = new Text(composite1, SWT.NONE);
							CTtext2verzeichniss.setBounds(36, 25, 1263, 22);
							String rdir = Toolboxconf.getPropAttribute("commenttool_rdir");
							setWorkdir(rdir);
							CTtext2verzeichniss.setText(rdir);
							
						}
						{
							CTbutton1setdir = new Button(composite1, SWT.PUSH | SWT.CENTER);
							CTbutton1setdir.setText("set Workdir");
							CTbutton1setdir.setBounds(1305, 25, 129, 22);
							CTbutton1setdir.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									CTbutton1setdirWidgetSelected(evt);
								}
							});
						}
						{
							composite2 = new Composite(composite1, SWT.BORDER);
							composite2.setLayout(null);
							composite2.setBounds(965, 548, 524, 179);
							{
								maxdd = new Text(composite2, SWT.NONE);
								maxdd.setText("300");
								maxdd.setBounds(14, 14, 32, 20);
							}
							{
								text1maxddproz = new Text(composite2, SWT.NONE);
								text1maxddproz.setText("30");
								text1maxddproz.setBounds(16, 44, 30, 19);
							}
							{
								button1 = new Button(composite2, SWT.CHECK | SWT.LEFT);
								button1.setText("max dd from high");
								button1.setBounds(54, 16, 135, 18);
								button1.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1WidgetSelected(evt);
									}
								});
							}
							{
								button2 = new Button(composite2, SWT.CHECK | SWT.LEFT);
								button2.setText("max dd% from high");
								button2.setBounds(54, 46, 156, 30);
								button2.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button2WidgetSelected(evt);
									}
								});
							}
							{
								button1werteeinzelaus300 = new Button(composite2, SWT.PUSH | SWT.CENTER);
								button1werteeinzelaus300.setText("WerteEinzeltradesAus MaxDD300");
								button1werteeinzelaus300.setBounds(275, 123, 237, 44);
								button1werteeinzelaus300.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1werteeinzelaus300WidgetSelected(evt);
									}
								});
							}
							{
								text1maxddfromstart = new Text(composite2, SWT.NONE);
								text1maxddfromstart.setText("50");
								text1maxddfromstart.setBounds(14, 80, 32, 23);
							}
							{
								button3 = new Button(composite2, SWT.CHECK | SWT.LEFT);
								button3.setText("max dd from start");
								button3.setBounds(54, 82, 131, 19);
								button3.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button3WidgetSelected(evt);
									}
								});
							}
							{
								button4 = new Button(composite2, SWT.CHECK | SWT.LEFT);
								button4.setText("trail min");
								button4.setBounds(58, 111, 70, 30);
								button4.setSelection(true);
								button4.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4WidgetSelected(evt);
									}
								});
							}
							{
								text1maxddtrail = new Text(composite2, SWT.NONE);
								text1maxddtrail.setText("50");
								text1maxddtrail.setBounds(15, 117, 31, 22);
							}
							{
								text1ndays = new Text(composite2, SWT.NONE);
								text1ndays.setText("8");
								text1ndays.setBounds(127, 119, 17, 20);
							}
							{
								label6 = new Label(composite2, SWT.NONE);
								label6.setText("days");
								label6.setSize(60, 30);
								label6.setBounds(152, 121, 60, 30);
							}
							{
								text1budget = new Text(composite2, SWT.NONE);
								text1budget.setText("10000");
								text1budget.setBounds(285, 24, 45, 21);
							}
							{
								label9 = new Label(composite2, SWT.NONE);
								label9.setText("budget");
								label9.setBounds(340, 24, 50, 19);
							}
							{
								text1maxdd = new Text(composite2, SWT.NONE);
								text1maxdd.setText("30");
								text1maxdd.setBounds(391, 22, 31, 22);
							}
							{
								label10 = new Label(composite2, SWT.NONE);
								label10.setText("maxdd");
								label10.setBounds(427, 24, 45, 22);
							}
							{
								button5 = new Button(composite2, SWT.CHECK | SWT.LEFT);
								button5.setText("use maxdd budget with trail");
								button5.setBounds(289, 50, 185, 30);
								button5.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button5WidgetSelected(evt);
									}
								});
							}
						}
						{
							composite3 = new Composite(composite1, SWT.BORDER);
							composite3.setLayout(null);
							composite3.setBounds(965, 428, 469, 72);
							{
								button1calcEndtestgraphic = new Button(composite3, SWT.PUSH | SWT.CENTER);
								button1calcEndtestgraphic.setText("WerteEinzeltradesAus");
								button1calcEndtestgraphic.setBounds(220, 20, 237, 45);
								button1calcEndtestgraphic.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1calcEndtestgraphicWidgetSelected(evt);
									}
								});
							}
						}
						{
							composite4 = new Composite(composite1, SWT.BORDER);
							composite4.setLayout(null);
							composite4.setBounds(965, 241, 469, 163);
							{
								button1calcNettoprofits = new Button(composite4, SWT.PUSH | SWT.CENTER);
								button1calcNettoprofits.setText("CalcNettoprofit");
								button1calcNettoprofits.setBounds(12, 68, 237, 36);
								if (enddata_glob.checkDatafile() == true)
									button1calcNettoprofits.setEnabled(true);
								else
									button1calcNettoprofits.setEnabled(false);
								button1calcNettoprofits.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1calcNettoprofitsWidgetSelected(evt);
									}
								});
							}
							{
								button1deleteEndtest = new Button(composite4, SWT.PUSH | SWT.CENTER);
								button1deleteEndtest.setText("DeleteEndtest");
								button1deleteEndtest.setBounds(368, 31, 95, 30);
								if (enddata_glob.checkDatafile() == true)
									button1deleteEndtest.setEnabled(true);
								else
									button1deleteEndtest.setEnabled(false);
								button1deleteEndtest.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1deleteEndtestWidgetSelected(evt);
									}
								});
							}
							{
								button1generateEndtest = new Button(composite4, SWT.PUSH | SWT.CENTER);
								button1generateEndtest.setText("GenerateEndtest");
								button1generateEndtest.setBounds(313, 92, 152, 54);
								if (enddata_glob.checkDatafile() == true)
									button1generateEndtest.setEnabled(false);
								else
									button1generateEndtest.setEnabled(true);
								button1generateEndtest.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1generateEndtestWidgetSelected(evt);
									}
								});
							}
							{
								button5IsNettoprofit = new Button(composite4, SWT.RADIO | SWT.LEFT);
								button5IsNettoprofit.setText("IS Nettoprofit");
								button5IsNettoprofit.setSelection(true);
								button5IsNettoprofit.setBounds(15, 39, 96, 17);
								button5IsNettoprofit.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button5IsNettoprofitWidgetSelected(evt);
									}
								});
							}
							{
								button5OssNettoprofit = new Button(composite4, SWT.RADIO | SWT.LEFT);
								button5OssNettoprofit.setText("OSS Nettoprofit");
								button5OssNettoprofit.setBounds(120, 41, 109, 17);
								button5OssNettoprofit.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button5OssNettoprofitWidgetSelected(evt);
									}
								});
							}
							{
								button5NetProfitRobust = new Button(composite4, SWT.RADIO | SWT.LEFT);
								button5NetProfitRobust.setText("NettoprofitRobust");
								button5NetProfitRobust.setBounds(237, 43, 128, 17);
								button5NetProfitRobust.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button5NetProfitRobustWidgetSelected(evt);
									}
								});
							}
							{
								text1confidenz = new Text(composite4, SWT.NONE);
								text1confidenz.setText("70");
								text1confidenz.setBounds(269, 22, 25, 17);
							}
							{
								label8 = new Label(composite4, SWT.NONE);
								label8.setText("confidenz");
								label8.setBounds(298, 20, 60, 19);
							}
							{
								button5calcAutomaticNettoprofit = new Button(composite4, SWT.PUSH | SWT.CENTER);
								button5calcAutomaticNettoprofit.setText("CalcAutomaticNettoprofit Table ???");
								button5calcAutomaticNettoprofit.setBounds(14, 118, 235, 30);
							}
						}
						{
							label1 = new Label(composite1, SWT.NONE);
							label1.setText("endtest with database");
							label1.setBounds(965, 225, 136, 30);
						}
						{
							label2 = new Label(composite1, SWT.NONE);
							label2.setText("endtest");
							label2.setBounds(965, 414, 60, 30);
						}
						{
							label3 = new Label(composite1, SWT.NONE);
							label3.setText("endtest with switch off automatic");
							label3.setBounds(965, 527, 214, 30);
						}
						{
							composite5 = new Composite(composite1, SWT.BORDER);
							composite5.setLayout(null);
							composite5.setBounds(36, 223, 516, 236);
							{
								CTbutton3clean = new Button(composite5, SWT.PUSH | SWT.CENTER);
								CTbutton3clean.setText("CleanAllComments");
								CTbutton3clean.setBounds(12, 16, 422, 30);
								CTbutton3clean.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										CTbutton3cleanWidgetSelected(evt);
									}
								});
							}
							{
								CTtext3prefix = new Text(composite5, SWT.NONE);
								CTtext3prefix.setBounds(14, 73, 31, 22);
							}
							{
								CTbutton4addComment = new Button(composite5, SWT.PUSH | SWT.CENTER);
								CTbutton4addComment.setText("AddComment");
								CTbutton4addComment.setBounds(12, 147, 129, 22);
								CTbutton4addComment.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										CTbutton4addCommentWidgetSelected(evt);
									}
								});
							}
							{
								CTlabel1pref = new Label(composite5, SWT.NONE);
								CTlabel1pref.setText("Prexfix");
								CTlabel1pref.setBounds(16, 103, 60, 30);
							}
							{
								CTlabel2stabilminval = new Label(composite5, SWT.NONE);
								CTlabel2stabilminval.setText("minval");
								CTlabel2stabilminval.setBounds(376, 88, 60, 30);
							}
							{
								CTtext1stabilminval = new Text(composite5, SWT.BORDER);
								CTtext1stabilminval.setBounds(316, 88, 54, 24);
								CTtext1stabilminval.setText("0");
							}
							{
								CTbutton2stabilitaet = new Button(composite5, SWT.CHECK | SWT.LEFT);
								CTbutton2stabilitaet.setText("Stabil");
								CTbutton2stabilitaet.setBounds(195, 88, 91, 24);
								
							}
							{
								CTbutton1netprofit = new Button(composite5, SWT.CHECK | SWT.LEFT);
								CTbutton1netprofit.setText("NetProfit");
								CTbutton1netprofit.setBounds(195, 60, 99, 22);
								
							}
							{
								CTlabel1 = new Label(composite5, SWT.NONE);
								CTlabel1.setText("minval");
								CTlabel1.setBounds(376, 60, 60, 30);
							}
							{
								CTtext1netprofitminval = new Text(composite5, SWT.BORDER);
								CTtext1netprofitminval.setBounds(316, 60, 54, 22);
								CTtext1netprofitminval.setText("0");
							}
						}
						{
							label4 = new Label(composite1, SWT.NONE);
							label4.setText("add comments");
							label4.setBounds(36, 208, 212, 30);
						}
						{
							composite6 = new Composite(composite1, SWT.BORDER);
							composite6.setLayout(null);
							composite6.setBounds(965, 89, 469, 119);
							{
								CTtext1prefix = new Text(composite6, SWT.BORDER);
								CTtext1prefix.setBounds(5, 5, 170, 26);
								CTtext1prefix.setText("F1pre");
								CTtext1prefix.addTraverseListener(new TraverseListener() {
									public void keyTraversed(TraverseEvent evt)
									{
										CTtext1prefixKeyTraversed(evt);
									}
								});
								
							}
							{
								CTlabel1 = new Label(composite6, SWT.NONE);
								CTlabel1.setText("fileprefix");
								CTlabel1.setBounds(189, 7, 91, 23);
								CTlabel1.setToolTipText("Timeframe and Magic will set automaticaly");
							}
							{
								CTbutton2 = new Button(composite6, SWT.PUSH | SWT.CENTER);
								CTbutton2.setText("RenameAllFiles");
								CTbutton2.setBounds(328, 9, 129, 26);
								CTbutton2.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										CTRenameFiles2WidgetSelected(evt);
									}
								});
							}
							{
								text4magicprefix = new Text(composite6, SWT.NONE);
								text4magicprefix.setText("2110");
								text4magicprefix.setBounds(7, 45, 60, 23);
							}
							{
								label29 = new Label(composite6, SWT.NONE);
								label29.setText("magic prefix (year/month)");
								label29.setBounds(79, 48, 203, 30);
							}
							{
								label30 = new Label(composite6, SWT.NONE);
								label30.setText("The timeframe will not be replaced !");
								label30.setBounds(14, 84, 443, 30);
							}
						}
						{
							label5 = new Label(composite1, SWT.NONE);
							label5.setText("rename files");
							label5.setBounds(965, 73, 93, 30);
						}
						{
							label7 = new Label(composite1, SWT.NONE);
							label7.setText("calculation mode");
							label7.setBounds(605, 411, 140, 30);
						}
					}
				}
				{
					cTabItem1 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem1.setText("Metriktools");
					{
						composite8 = new Composite(cTabFolder1, SWT.NONE);
						composite8.setLayout(null);
						cTabItem1.setControl(composite8);
						{
							group11 = new Group(composite8, SWT.NONE);
							group11.setLayout(null);
							group11.setText("Build new Metrik");
							group11.setBounds(21, 25, 827, 277);
							{
								text1conf100 = new Text(group11, SWT.NONE);
								text1conf100.setText("Metriktable conf100");
								text1conf100.setBounds(8, 20, 363, 25);
							}
							{
								button6conf100 = new Button(group11, SWT.PUSH | SWT.CENTER);
								button6conf100.setText("*.csv Quelldir1 conf100");
								button6conf100.setBounds(377, 20, 131, 25);
								button6conf100.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6Widgetconf100Selected(evt);
									}
								});
							}
							{
								text2conf50 = new Text(group11, SWT.NONE);
								text2conf50.setText("Metriktable conf50");
								text2conf50.setBounds(8, 57, 363, 24);
							}
							{
								button7conf50 = new Button(group11, SWT.PUSH | SWT.CENTER);
								button7conf50.setText("*.csv Quelldir2 conf50");
								button7conf50.setBounds(377, 57, 131, 24);
								button7conf50.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button7conf50WidgetSelected(evt);
									}
								});
							}
							{
								text3zielmetrik = new Text(group11, SWT.NONE);
								text3zielmetrik.setText("Ziel Metriktable");
								text3zielmetrik.setBounds(8, 93, 363, 24);
							}
							{
								button8zielmetrik = new Button(group11, SWT.PUSH | SWT.CENTER);
								button8zielmetrik.setText("set Zielmetriktable");
								button8zielmetrik.setBounds(377, 93, 131, 24);
								button8zielmetrik.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8ZielmetrikWidgetSelected(evt);
									}
								});
							}
							{
								button9calculate = new Button(group11, SWT.PUSH | SWT.CENTER);
								button9calculate.setText("calculate");
								button9calculate.setBounds(448, 197, 60, 30);
								button9calculate.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button9calculateWidgetSelected(evt);
									}
								});
							}
							{
								text1attributname = new Text(group11, SWT.NONE);
								text1attributname.setText("maxPctDD");
								text1attributname.setBounds(8, 135, 118, 20);
							}
							{
								label11 = new Label(group11, SWT.NONE);
								label11.setText("Metrikattribut");
								label11.setBounds(138, 135, 82, 30);
							}
							{
								cLabel2 = new CLabel(group11, SWT.NONE);
								cLabel2.setText(
										"Info:\r\n\r\nHier wird aus der ersten *.csv Tabelle das \r\nMetrikattribut maxPctDDRobust (maxDD%) \r\ngelesen und hiervon wird das gleiche Attribut \r\naus der zweiten Tabelle subtrahiert. \r\nTabelle1 sollte Confidenzwerte von 100 und \r\nTabelle2 Confidenzwerte von 50 beinhalten.\r\nAnschliessend wird eine neue Zielmetriktabelle \r\nmit dem neuen errechneteten Attribut \r\nConf100-Conf50 geschrieben.\r\nDiese neue Tabelle kann mit \r\ndem Metrikanalyser ausgewertet werden.");
								cLabel2.setBounds(555, 12, 260, 245);
							}
						}
					}
				}
				{
					cTabItem2 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem2.setText("RT Filter");
					{
						composite9 = new Composite(cTabFolder1, SWT.NONE);
						composite9.setLayout(null);
						cTabItem2.setControl(composite9);
						{
							group1conf100_50 = new Group(composite9, SWT.BORDER);
							group1conf100_50.setLayout(null);
							group1conf100_50.setText("Conf100_50Filter");
							group1conf100_50.setBounds(29, 12, 822, 229);
							{
								text1_confsource = new Text(group1conf100_50, SWT.NONE);
								text1_confsource.setText("conf100-50 strategzdirectorz");
								text1_confsource.setBounds(22, 47, 284, 26);
							}
							{
								button6set100sourcedir = new Button(group1conf100_50, SWT.PUSH | SWT.CENTER);
								button6set100sourcedir.setText("set conf100_50 Sourcedir");
								button6set100sourcedir.setBounds(312, 47, 179, 30);
								button6set100sourcedir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6set100sourcedirWidgetSelected(evt);
									}
								});
							}
							{
								conf100_50destinationdir = new Text(group1conf100_50, SWT.NONE);
								conf100_50destinationdir.setText("conf100-50 destinationdir");
								conf100_50destinationdir.setBounds(22, 102, 284, 30);
							}
							{
								button6conf100_50destin = new Button(group1conf100_50, SWT.PUSH | SWT.CENTER);
								button6conf100_50destin.setText("conf100_50 Destinationdir");
								button6conf100_50destin.setBounds(312, 102, 179, 30);
								button6conf100_50destin.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6conf100_50destinWidgetSelected(evt);
									}
								});
							}
							{
								button6calc100 = new Button(group1conf100_50, SWT.PUSH | SWT.CENTER);
								button6calc100.setText("calc");
								button6calc100.setBounds(429, 170, 60, 30);
								button6calc100.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6calc100WidgetSelected(evt);
									}
								});
							}
							{
								text2maxddrobustdiff = new Text(group1conf100_50, SWT.NONE);
								text2maxddrobustdiff.setText("20");
								text2maxddrobustdiff.setBounds(27, 165, 60, 18);
							}
							{
								label11maxddrobustdiff = new Label(group1conf100_50, SWT.NONE);
								label11maxddrobustdiff.setText("maxdd robust diff");
								label11maxddrobustdiff.setBounds(99, 165, 114, 30);
							}
							{
								cLabel1 = new CLabel(group1conf100_50, SWT.NONE);
								cLabel1.setText(
										"Info:\r\n\r\n1) Es werden die *.str Files auf dem \r\nVerzeichniss conf100_50 Sourcedir eingelesen.\r\n2) Anschliessend wird \r\nx=maxddRobust(conf100)-maxddRobust(conf50) \r\ngerechnet\r\n3) Falls x<maxddrobust dann wird die *.str datei \r\nvon sourcedir ins destdir kopiert\r\n");
								cLabel1.setBounds(509, 47, 283, 153);
							}
						}
						{
							group1 = new Group(composite9, SWT.SHADOW_ETCHED_IN | SWT.BORDER);
							group1.setLayout(null);
							group1.setText("RT-Filter");
							group1.setBounds(29, 264, 616, 417);
							{
								rt_sourcedir = new Text(group1, SWT.NONE);
								rt_sourcedir.setBounds(34, 29, 287, 20);
								rt_sourcedir.setMessage(Toolboxconf.getPropAttribute("rt_sourcedir"));
								rt_sourcedir.setDoubleClickEnabled(false);
							}
							{
								rt_destdir = new Text(group1, SWT.NONE);
								rt_destdir.setBounds(34, 55, 287, 20);
								rt_destdir.setMessage(Toolboxconf.getPropAttribute("rt_destdir"));
							}
							{
								button6set_rt_sourcedir = new Button(group1, SWT.PUSH | SWT.CENTER);
								button6set_rt_sourcedir.setText("set RT sourcedir");
								button6set_rt_sourcedir.setBounds(333, 29, 124, 20);
								button6set_rt_sourcedir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6set_rt_sourcedirWidgetSelected(evt);
									}
								});
							}
							{
								button6setRT_destdir = new Button(group1, SWT.PUSH | SWT.CENTER);
								button6setRT_destdir.setText("set RT destdir");
								button6setRT_destdir.setBounds(333, 55, 124, 19);
								button6setRT_destdir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6setRT_destdirWidgetSelected(evt);
									}
								});
							}
							{
								faktorMaxDrawdownRobust = new Text(group1, SWT.NONE);
								faktorMaxDrawdownRobust.setText("0.5");
								faktorMaxDrawdownRobust.setBounds(36, 112, 60, 20);
							}
							{
								button6calcRtFilter = new Button(group1, SWT.PUSH | SWT.CENTER);
								button6calcRtFilter.setText("calcRtFilter");
								button6calcRtFilter.setBounds(491, 375, 113, 30);
								button6calcRtFilter.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6calcRtFilterWidgetSelected(evt);
									}
								});
							}
							{
								text1maxlosses = new Text(group1, SWT.NONE);
								text1maxlosses.setText("1");
								text1maxlosses.setBounds(42, 213, 33, 22);
							}
							{
								label13 = new Label(group1, SWT.NONE);
								label13.setText("MaxRobust faktor ");
								label13.setBounds(110, 114, 160, 23);
								label13.setToolTipText(
										"set MaxDDrobust faktor:\r\n\r\nIt will be calced\r\nok if: RetDDrobust95>RetDD*faktor\r\n\r\nSet 0.5 or 0.33\r\n0.5 is harder criterium\r\n\r\nfor example for 0.5\r\nRetDD95        RetDD     fullfill\r\n10.1\t\t\t     5        yes\r\n8                           5        no ");
							}
							{
								label15 = new Label(group1, SWT.NONE);
								label15.setText("Max Losses");
								label15.setBounds(89, 210, 93, 30);
								label15.setToolTipText(
										"set Max Losses:\r\n\r\nWe allow some years of losses (means profit<faktor * maxDrawdown)\r\nIF n=1 we allow one year of looses\r\nIf n=2 we allow two years of looses\r\n\r\nhint:\r\nThe last 4 years is not allowed that this strategy looses. \r\nThis strategy sould be good in the last 4 years");
							}
							{
								label12 = new Label(group1, SWT.NONE);
								label12.setText("Check1: (robustnesscheck)");
								label12.setBounds(38, 92, 201, 18);
							}
							{
								label16 = new Label(group1, SWT.NONE);
								label16.setText("Check2: (equitycheck)");
								label16.setBounds(44, 178, 151, 30);
							}
							{
								label14 = new Label(group1, SWT.NONE);
								label14.setText("ok if: RetDDrobust95>RetDD*faktor");
								label14.setBounds(272, 94, 241, 27);
								label14.setToolTipText("if: RetDDrobust95<RetDD*faktor => ok");
							}
							{
								button6rt_checkrt = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6rt_checkrt.setText("button6");
								button6rt_checkrt.setBounds(9, 84, 19, 30);
								button6rt_checkrt.setSelection(true);
							}
							{
								button6rtCheckequity = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6rtCheckequity.setBounds(11, 174, 23, 30);
							}
							{
								label17rtsourcedir = new Label(group1, SWT.NONE);
								label17rtsourcedir.setText("?");
								label17rtsourcedir.setBounds(477, 31, 48, 22);
							}
							{
								label17rtdestdir = new Label(group1, SWT.NONE);
								label17rtdestdir.setText("?");
								label17rtdestdir.setBounds(475, 57, 60, 21);
							}
							{
								button6Lastyearsflag = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6Lastyearsflag.setText("last two years should be good");
								button6Lastyearsflag.setBounds(299, 218, 224, 30);
								button6Lastyearsflag.setToolTipText(
										"If this flag is activated the strategy have to win in the last two years. Win means: The yearly win of the last two year have to be bigger than drawdown in each year.");
								button6Lastyearsflag.setSelection(true);
							}
							{
								text1monthsPerSegment = new Text(group1, SWT.NONE);
								text1monthsPerSegment.setText("12");
								text1monthsPerSegment.setBounds(44, 249, 31, 24);
								text1monthsPerSegment.setEditable(false);
							}
							{
								label17 = new Label(group1, SWT.NONE);
								label17.setText("Intervallength in months");
								label17.setBounds(89, 248, 191, 25);
								label17.setToolTipText(
										"the intervalllenght in months. For example. If the intervall has 12 months, the profit will added 12 months.");
								label17.setEnabled(false);
							}
							{
								label18 = new Label(group1, SWT.NONE);
								label18.setText("0.5 or 0.33,  0.5 is a harder condition");
								label18.setBounds(271, 116, 251, 30);
							}
							{
								button6cut2years = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6cut2years.setText("cut first and last year");
								button6cut2years.setBounds(303, 254, 223, 25);
							}
							{
								text1equityfaktor = new Text(group1, SWT.NONE);
								text1equityfaktor.setText("0.9");
								text1equityfaktor.setBounds(203, 180, 38, 22);
								text1equityfaktor.setToolTipText(
										"1=100%=yearly profit > maxDD\r\n0.8=80% = yearly profit >maxDD*0.8\r\n0.5=50% = yearly profit > maxDD *0.5\r\n\r\ne.g.\r\nmaxDD = 10000 Euro\r\nfaktor=0.5\r\n\r\nyearly profit must be > 5000 Euro");
							}
							{
								label19 = new Label(group1, SWT.NONE);
								label19.setText("equityfaktor:  yearly profit > maxDD* equityfaktor");
								label19.setBounds(255, 182, 343, 30);
								label19.setToolTipText(
										"1=100%=yearly profit > maxDD\r\n0.8=80% = yearly profit >maxDD*0.8\r\n0.5=50% = yearly profit > maxDD *0.5\r\n\r\ne.g.\r\nmaxDD = 10000 Euro\r\nfaktor=0.5\r\n\r\nyearly profit must be > 5000 Euro");
							}
							{
								button6longshort = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6longshort.setText("Check3: (long/short check)");
								button6longshort.setBounds(12, 325, 207, 24);
							}
							{
								text1faktor = new Text(group1, SWT.NONE);
								text1faktor.setText("0.3");
								text1faktor.setBounds(217, 327, 29, 22);
							}
							{
								label20 = new Label(group1, SWT.NONE);
								label20.setText("long short faktor (minimum long short equivalence)");
								label20.setBounds(262, 329, 349, 30);
								label20.setToolTipText(
										"0.2 = 20% ever side have to be 20% trades\r\n0.5 = 50% every side have to be 50% trades\r\nMore then 0.5 is not possible");
							}
							{
								button6showtable = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6showtable.setText("show result in table");
								button6showtable.setBounds(323, 373, 166, 30);
							}
							{
								button6deleteifequal = new Button(group1, SWT.CHECK | SWT.LEFT);
								button6deleteifequal.setText("don´t use if Retdd==RetDDrobut");
								button6deleteifequal.setBounds(275, 139, 262, 30);
							}
						}
					}
				}
				{
					cTabItem3 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem3.setText("Clusterfilter");
					{
						composite10 = new Composite(cTabFolder1, SWT.NONE);
						composite10.setLayout(null);
						cTabItem3.setControl(composite10);
						{
							cluster_strsourcedir = new Text(composite10, SWT.NONE);
							cluster_strsourcedir.setBounds(47, 47, 209, 30);
						}
						{
							ClusterDestDir = new Text(composite10, SWT.NONE);
							ClusterDestDir.setBounds(47, 100, 209, 30);
						}
						{
							clusterStrDestdir = new Button(composite10, SWT.PUSH | SWT.CENTER);
							clusterStrDestdir.setText("SetClusterDestdir");
							clusterStrDestdir.setBounds(268, 100, 144, 30);
							clusterStrDestdir.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									clusterStrDestdirWidgetSelected(evt);
								}
							});
						}
						{
							ClusterSourceDir = new Button(composite10, SWT.PUSH | SWT.CENTER);
							ClusterSourceDir.setText("SetClusterSourceDir");
							ClusterSourceDir.setBounds(268, 47, 144, 30);
							ClusterSourceDir.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									ClusterSourceDirWidgetSelected(evt);
								}
							});
						}
						{
							Cluster_StartGeneration = new Button(composite10, SWT.PUSH | SWT.CENTER);
							Cluster_StartGeneration.setText("Start Clustering");
							Cluster_StartGeneration.setBounds(228, 195, 184, 30);
							Cluster_StartGeneration.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									Cluster_StartGenerationWidgetSelected(evt);
								}
							});
						}
					}
				}
				{
					cTabItem4 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem4.setText("Tradefilter");
					{
						composite11 = new Composite(cTabFolder1, SWT.NONE);
						composite11.setLayout(null);
						cTabItem4.setControl(composite11);
						{
							text1proz = new Text(composite11, SWT.NONE);
							text1proz.setText("80");
							text1proz.setBounds(665, 62, 34, 28);
						}
						{
							label21 = new Label(composite11, SWT.NONE);
							label21.setText("at least % matching");
							label21.setBounds(722, 62, 179, 30);
						}
						{
							text1 = new Text(composite11, SWT.NONE);
							text1.setText("E:\\tradefilter");
							text1.setBounds(161, 96, 538, 30);
						}
						{
							button6tradefilterrootdir = new Button(composite11, SWT.PUSH | SWT.CENTER);
							button6tradefilterrootdir.setText("set tradefilterrootdir");
							button6tradefilterrootdir.setBounds(722, 96, 172, 30);
							button6tradefilterrootdir.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button6tradefilterrootdirWidgetSelected(evt);
								}
							});
						}
						{
							button6calcmatchingtable = new Button(composite11, SWT.PUSH | SWT.CENTER);
							button6calcmatchingtable.setText("show matchingtable");
							button6calcmatchingtable.setBounds(722, 143, 172, 30);
							button6calcmatchingtable.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button6calcmatchingtableWidgetSelected(evt);
								}
							});
						}
						{
							button6filterstrategies = new Button(composite11, SWT.PUSH | SWT.CENTER);
							button6filterstrategies.setText("filter strategies");
							button6filterstrategies.setBounds(722, 190, 172, 30);
							button6filterstrategies.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button6filterstrategiesWidgetSelected(evt);
								}
							});
						}
						{
							button6cleartradefilter = new Button(composite11, SWT.PUSH | SWT.CENTER);
							button6cleartradefilter.setText("clear strategies");
							button6cleartradefilter.setBounds(722, 238, 172, 30);
							button6cleartradefilter.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button6cleartradefilterWidgetSelected(evt);
								}
							});
						}
					}
				}
				{
					cTabItem6 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem6.setText("PriceDataSeries");
					{
						composite12 = new Composite(cTabFolder1, SWT.NONE);
						composite12.setLayout(null);
						cTabItem6.setControl(composite12);
						{
							priceDataSeries = new Group(composite12, SWT.NONE);
							priceDataSeries.setLayout(null);
							priceDataSeries.setText("PriceDataSeriesConverter");
							priceDataSeries.setBounds(40, 29, 516, 260);
							{
								text2 = new Text(priceDataSeries, SWT.NONE);
								text2.setText("F:\\Kurse\\Asikury\\EURUSD_Asirikuy_M1_part.csv");
								text2.setBounds(8, 25, 375, 28);
							}
							{
								button6pdc = new Button(priceDataSeries, SWT.PUSH | SWT.CENTER);
								button6pdc.setText("set input File");
								button6pdc.setBounds(395, 25, 109, 30);
								button6pdc.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6pdcWidgetSelected(evt);
									}
								});
							}
							{
								text3pdc = new Text(priceDataSeries, SWT.NONE);
								text3pdc.setText("F:\\Kurse\\Asikury\\EURUSD_Asirikuy_M1_out.csv");
								text3pdc.setBounds(8, 59, 375, 30);
							}
							{
								button6 = new Button(priceDataSeries, SWT.PUSH | SWT.CENTER);
								button6.setText("set output File");
								button6.setBounds(395, 61, 109, 30);
								button6.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button6WidgetSelected(evt);
									}
								});
							}
							{
								text3 = new Text(priceDataSeries, SWT.NONE);
								text3.setText("0");
								text3.setSize(52, 30);
								text3.setBounds(8, 108, 60, 30);
							}
							{
								label22pdc = new Label(priceDataSeries, SWT.NONE);
								label22pdc.setText("new Timediff in hours");
								label22pdc.setBounds(80, 108, 303, 30);
							}
							{
								button7pdc = new Button(priceDataSeries, SWT.PUSH | SWT.CENTER);
								button7pdc.setText("convert");
								button7pdc.setBounds(395, 187, 104, 63);
								button7pdc.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button7pdcWidgetSelected(evt);
									}
								});
							}
						}
					}
				}
				
				{
					
					cTabItem7 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem7.setText("Sq4.X Workflow Generator");
					
					{
						composite13 = new Composite(cTabFolder1, SWT.NONE);
						composite13.setLayout(null);
						cTabItem7.setControl(composite13);
						{
							group2filter = new Group(composite13, SWT.NONE);
							group2filter.setLayout(null);
							group2filter.setBounds(0, -54, 1611, 862);
							{
								FilterSourceDir = new Text(group2filter, SWT.NONE);
								FilterSourceDir.setText(Toolboxconf.getPropAttribute("masterfile"));
								FilterSourceDir.setBounds(12, 82, 1120, 30);
							}
							{
								button7set = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button7set.setText("Step1: set Masterfile");
								button7set.setBounds(1162, 57, 285, 72);
								button7set.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button7setWidgetSelected(evt);
									}
								});
							}
							{
								text4filterdestdir = new Text(group2filter, SWT.NONE);
								text4filterdestdir.setText(Toolboxconf.getPropAttribute("sqrootdir"));
								text4filterdestdir.setBounds(12, 162, 811, 30);
							}
							{
								button7setdestdir = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button7setdestdir.setText("set SQ Rootdir");
								button7setdestdir.setBounds(829, 162, 236, 32);
								button7setdestdir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button7setdestdirWidgetSelected(evt);
									}
								});
							}
							{
								text4filterkeyword = new Text(group2filter, SWT.NONE);
								text4filterkeyword.setText(Toolboxconf.getPropAttribute("anzdays"));
								text4filterkeyword.setBounds(12, 204, 68, 30);
							}
							{
								button7calc = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button7calc.setText("Step2 Generate SQ Workflows");
								button7calc.setBounds(1170, 501, 285, 79);
								button7calc.setToolTipText("SQworkflow modifies SQ 4.X Workflows\r\n");
								button7calc.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button7calcWidgetSelected(evt);
									}
								});
							}
							{
								label22 = new Label(group2filter, SWT.NONE);
								label22.setText("delta days");
								label22.setBounds(92, 204, 153, 30);
							}
							{
								steps = new Text(group2filter, SWT.NONE);
								steps.setText(Toolboxconf.getPropAttribute("backcount"));
								steps.setBounds(257, 204, 43, 30);
							}
							{
								label23 = new Label(group2filter, SWT.NONE);
								label23.setText("steps back");
								label23.setBounds(306, 204, 98, 30);
							}
							{
								outputname = new Text(group2filter, SWT.NONE);
								outputname.setText(Toolboxconf.getPropAttribute("outputname"));
								outputname.setBounds(12, 280, 417, 30);
								outputname.addModifyListener(new ModifyListener() {
									public void modifyText(ModifyEvent evt)
									{
										outputnameModifyText(evt);
									}
								});
							}
							{
								label24 = new Label(group2filter, SWT.NONE);
								label24.setText("generated workflow name");
								label24.setBounds(435, 280, 219, 30);
							}
							{
								collectresultsbutton = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								collectresultsbutton.setText("Step3 collect results");
								collectresultsbutton.setBounds(1170, 731, 285, 88);
								collectresultsbutton.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										collectresultsbuttonWidgetSelected(evt);
									}
									
								});
							}
							{
								text4stepsfuture = new Text(group2filter, SWT.NONE);
								text4stepsfuture.setText(Toolboxconf.getPropAttribute("futurecount"));
								text4stepsfuture.setBounds(428, 204, 41, 30);
							}
							{
								label25 = new Label(group2filter, SWT.NONE);
								label25.setText("steps future");
								label25.setBounds(481, 204, 106, 30);
							}
							{
								label26 = new Label(group2filter, SWT.NONE);
								label26.setText("(should be uniqe !!)");
								label26.setBounds(660, 280, 157, 30);
							}
							{
								text4infotext = new Text(group2filter,
										SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
								// read infomessage and show in multilinebox
								text4infotext.setText(Toolboxconf.getPropAttribute("infotext"));
								
								text4infotext.setBounds(12, 322, 807, 189);
								text4infotext.addFocusListener(new FocusAdapter() {
									public void focusGained(FocusEvent evt)
									{
										text4infotextFocusGained(evt);
									}
								});
								text4infotext.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										text4infotextWidgetSelected(evt);
									}
								});
								
							}
							
							{
								label27 = new Label(group2filter, SWT.NONE);
								label27.setText("please add additional description to this box above");
								label27.setBounds(12, 511, 701, 30);
							}
							{
								button8showbackupdir = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8showbackupdir.setText("show dir");
								button8showbackupdir.setBounds(1030, 747, 114, 30);
								button8showbackupdir
										.setImage(SWTResourceManager.getImage("start/resources/icons/open.png"));
								button8showbackupdir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8showbackupdirWidgetSelected(evt);
									}
								});
							}
							{
								button8showshareddir = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8showshareddir.setText("show dir");
								button8showshareddir.setBounds(1030, 701, 114, 30);
								button8showshareddir
										.setImage(SWTResourceManager.getImage("start/resources/icons/open.png"));
								button8showshareddir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8showshareddirWidgetSelected(evt);
									}
								});
							}
							{
								button8showResultdir = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8showResultdir.setText("show dir");
								button8showResultdir.setBounds(1030, 654, 114, 30);
								button8showResultdir
										.setImage(SWTResourceManager.getImage("start/resources/icons/open.png"));
								button8showResultdir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8showResultdirWidgetSelected(evt);
									}
								});
							}
							{
								button8setbackupdrive = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8setbackupdrive.setText("set backupdrive **");
								button8setbackupdrive.setBounds(821, 747, 168, 30);
								button8setbackupdrive.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8setbackupdriveWidgetSelected(evt);
									}
								});
							}
							{
								button8shareddirectory = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8shareddirectory.setText("set shared drive **");
								button8shareddirectory.setBounds(821, 701, 168, 30);
								button8shareddirectory.setToolTipText(
										"This can be googledrive. In this drive a directorystructure with generated workflowname will be stored. All results will be stored in this drive.");
								button8shareddirectory.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8shareddirectoryWidgetSelected(evt);
									}
								});
							}
							{
								button7 = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button7.setText("set resultdir");
								button7.setBounds(821, 654, 168, 30);
								button7.setToolTipText(
										"The generated portfolios will be collected out of the SQ directoy and will be stored in this directory");
								button7.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button7WidgetSelected(evt);
									}
								});
							}
							{
								text4backupdrive = new Text(group2filter, SWT.NONE);
								text4backupdrive.setText(Toolboxconf.getPropAttribute("backupdrive"));
								text4backupdrive.setBounds(12, 747, 803, 30);
							}
							{
								text4shareddrive = new Text(group2filter, SWT.NONE);
								text4shareddrive.setText(Toolboxconf.getPropAttribute("shareddrive"));
								text4shareddrive.setBounds(11, 701, 804, 30);
							}
							{
								text4resultdir = new Text(group2filter, SWT.NONE);
								text4resultdir.setText(Toolboxconf.getPropAttribute("resultdir"));
								text4resultdir.setBounds(12, 654, 803, 30);
							}
							{
								label28 = new Label(group2filter, SWT.NONE);
								label28.setText(
										"Collect the portfolios from Step2 =\"SQ rootdir/usr/projects/<...>/databanks/portfolio\" and store this to this directorys below");
								label28.setBounds(12, 617, 587, 25);
							}
							{
								button8showprojectdir = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8showprojectdir.setText("show rootdir/user/projects");
								button8showprojectdir.setBounds(829, 227, 236, 30);
								button8showprojectdir.setBackgroundImage(
										SWTResourceManager.getImage("start/resources/icons/open.png"));
								button8showprojectdir
										.setImage(SWTResourceManager.getImage("start/resources/icons/open.png"));
								button8showprojectdir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8showprojectdirWidgetSelected(evt);
									}
								});
							}
							{
								button8clearprojectdir = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8clearprojectdir.setText("clear projectdir");
								button8clearprojectdir.setBounds(829, 269, 236, 30);
								button8clearprojectdir.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8clearprojectdirWidgetSelected(evt);
									}
								});
								
							}
							{
								label29anzfiles = new Label(group2filter, SWT.NONE);
								refreshProjectfilesanzahlMessages();
								label29anzfiles.setBounds(1071, 237, 49, 20);
							}
							{
								text4workflowstore = new Text(group2filter, SWT.NONE);
								text4workflowstore
										.setText("The workflow will be stored under SQ Rootdir/user/projects");
								text4workflowstore.setBounds(12, 547, 809, 30);
								text4workflowstore.setEditable(false);
							}
							{
								button8 = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button8.setText("save");
								button8.setBounds(759, 511, 60, 30);
								button8.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button8WidgetSelected(evt);
									}
								});
							}
							{
								button9shareddrive = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								button9shareddrive.setBounds(992, 706, 26, 25);
								button9shareddrive.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button9shareddriveWidgetSelected(evt);
									}
								});
							}
							{
								button9backupdrive = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								button9backupdrive.setBounds(991, 752, 27, 25);
								button9backupdrive.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button9backupdriveWidgetSelected(evt);
									}
								});
							}
							{
								button9showresults = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								button9showresults.setText("Show Results");
								button9showresults.setBounds(720, 789, 95, 29);
								button9showresults.setSelection(true);
								button9showresults.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button9showresultsWidgetSelected(evt);
									}
								});
							}
							{
								text4databankname = new Text(group2filter, SWT.NONE);
								text4databankname.setText(Toolboxconf.getPropAttribute("portfolioname"));
								text4databankname.setBounds(334, 595, 83, 20);
								text4databankname.setToolTipText(
										"This is the the there the portfolio is stored, the default is the databankname portfolio");
							}
							{
								combo1cpart = new Combo(group2filter, SWT.NONE);
								combo1cpart.setText(Toolboxconf.getPropAttribute("combo1cpart"));
								
								combo1cpart.setBounds(423, 596, 60, 22);
								combo1cpart.addModifyListener(new ModifyListener() {
									public void modifyText(ModifyEvent evt) {
										combo1cpartModifyText(evt);
									}
								});

							}
							{
								label31 = new Label(group2filter, SWT.NONE);
								label31.setText("(i)");
								label31.setBounds(1461, 731, 12, 16);
								label31.setToolTipText("Please activate in SQ configuration/databanks/synchronisize to files automaticaly..");
							}
							{
								deltadaysconfigfile = new Text(group2filter, SWT.NONE);
								deltadaysconfigfile.setText(Toolboxconf.getPropAttribute("deltadaysfile"));
								deltadaysconfigfile.setBounds(12, 244, 457, 30);
							}
							{
								setdeltadays = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								setdeltadays.setText("set file");
								setdeltadays.setSize(60, 30);
								setdeltadays.setBounds(481, 246, 60, 30);
								setdeltadays.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										setdateconfigfile(evt);
									}
								});
							}
							{
								selektorShiftDays = new Button(group2filter, SWT.RADIO | SWT.LEFT);
								selektorShiftDays.setText("Shift Days");
								selektorShiftDays.setBounds(639, 204, 120, 30);
								selektorShiftDays.setSelection(true);
								selektorShiftDays.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										selektorShiftDaysWidgetSelected(evt);
									}
								});
							}
							{
								SelektorUseDeltaDaysConfigfile = new Button(group2filter, SWT.RADIO | SWT.LEFT);
								SelektorUseDeltaDaysConfigfile.setText("Use datefile");
								SelektorUseDeltaDaysConfigfile.setBounds(639, 246, 95, 30);
							}
							{
								normn = new Text(group2filter, SWT.NONE);
								normn.setText("10");
								normn.setText(Toolboxconf.getPropAttribute("normn"));
								normn.setBounds(833, 789, 29, 22);
								normn.addModifyListener(new ModifyListener() {
									public void modifyText(ModifyEvent evt)
									{
										normnModifyText(evt);
									}
								});
								normn.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										normnWidgetSelected(evt);
									}
								});
							}
							{
								nomalizeselection = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								nomalizeselection.setText("Normalize");
								nomalizeselection.setBounds(874, 781, 85, 30);
								if(Toolboxconf.getPropAttribute("normn").equals("0"))
									nomalizeselection.setSelection(false);
								else
									nomalizeselection.setSelection(true);
								nomalizeselection.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										nomalizeselectionWidgetSelected(evt);
									}
								});
							}
							{
								label32 = new Label(group2filter, SWT.NONE);
								label32.setText("(i)");
								label32.setBounds(965, 789, 18, 22);
								label32.setToolTipText("One problem is that some workflows sometimes produce a lot of strategies. \r\nThese many strategies can distort the result. \r\nFor example, a workflow produces 1000 strategies. \r\nIf they are positive overall, the results\r\nof the other workflows are ignored. \r\nIf this flag is activated, we limit the maximum number of strategies. \r\nN= generated strategies. \r\nX=Max allowed strategies, \r\nSUM = total winnings. \r\nIf N>X than Profit=(SUM/N)*X. \r\n\r\n=>so profit/loss is limited by this number.");
							}
							{
								fullreportselection = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								fullreportselection.setText("Show full Report");
								fullreportselection.setBounds(1030, 781, 114, 30);
								if(Toolboxconf.getPropAttribute("fullreportselection").equals("1"))
										fullreportselection.setSelection(true);
								else
									fullreportselection.setSelection(false);
								fullreportselection.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										fullreportselectionWidgetSelected(evt);
									}
								});
							}
							{
								maxstepsback = new Text(group2filter, SWT.NONE);
								maxstepsback.setText("5000");
								maxstepsback.setBounds(565, 797, 39, 22);
								maxstepsback.setEnabled(false);
							}
							{
								MaxStepsBackFlag = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								MaxStepsBackFlag.setText("MaxStepsBack");
								MaxStepsBackFlag.setBounds(610, 789, 98, 30);
								MaxStepsBackFlag.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										MaxStepsBackFlagWidgetSelected(evt);
									}
								});
							}
							{
								label33 = new Label(group2filter, SWT.NONE);
								label33.setText("Limitations: PF is limited to 5 and RetDD to 15.");
								label33.setBounds(12, 789, 258, 30);
							}
							{
								GetEnddateFromDatabase = new Text(group2filter, SWT.NONE);
								GetEnddateFromDatabase.setText(Toolboxconf.getPropAttribute("Endtestname"));
								GetEnddateFromDatabase.setBounds(829, 328, 93, 21);
							}
							{
								label34 = new Label(group2filter, SWT.NONE);
								label34.setText("Get Endtest Dates from Database");
								label34.setBounds(934, 328, 205, 21);
								label34.setToolTipText("If you klick \"Step2 Generate SQ Workflow\" Toolbox read the dates out of the SQX-Database and write this information into the SQX-Workflow directory.\r\nFor example.\r\nC:\\forex\\Toolbox\\SQ\\2 Generator\\user\\projects\\Nick31_35 optimize_+00014\\Enddate.txt\r\n\r\nThis datafile Enddate.txt will be used in the \"3 Collect Result\" Step.");
							}
							{
								button9copyinshared = new Button(group2filter, SWT.CHECK | SWT.LEFT);
								button9copyinshared.setText("CopyMasterworkflow in Shared Drive");
								button9copyinshared.setBounds(1170, 586, 281, 30);
								button9copyinshared.setSelection(true);
							}
							{
								button9showfile = new Button(group2filter, SWT.PUSH | SWT.CENTER);
								button9showfile.setText("ShowFile");
								button9showfile.setBounds(1072, 112, 60, 30);
								button9showfile.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										button9showfileWidgetSelected(evt);
									}
								});
							}
						}
					}
				}
				{
					composite7 = new Composite(cTabFolder1, SWT.NONE);
					GridLayout composite7Layout = new GridLayout();
					composite7Layout.makeColumnsEqualWidth = true;
					composite7.setLayout(composite7Layout);
				}
				cTabFolder1.setSelection(0);
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
							openFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							openFileMenuItem.setText("Open");
						}
						{
							newFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							newFileMenuItem.setText("New");
						}
						{
							saveFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							saveFileMenuItem.setText("Save");
						}
						{
							closeFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
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
							contentsMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
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
			postinit();
			this.layout();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void postinit()
	{
		// den inittimer auf 3 sekunden setzen
		Display.getDefault().timerExec(300, inittimer);
		// text4resultdir.setText(Toolboxconf.getPropAttribute("resultdir"));
		// text4shareddrive.setText(Toolboxconf.getPropAttribute("shareddrive"));
		// text4backupdrive.setText(Toolboxconf.getPropAttribute("backupdrive"));
		
		// set infotext
		String sd = text4shareddrive.getText();
		String outname = outputname.getText();
		sqprojects.setSharedDrive(sd);
		
		text1confidenz.setEnabled(false);
		label8.setEnabled(false);
		cTabFolder1.setSelection(cTabItem7);
		
		if (button1calcNettoprofits.isEnabled() == true)
		{
			button5IsNettoprofit.setEnabled(false);
			button5OssNettoprofit.setEnabled(false);
			button5NetProfitRobust.setEnabled(false);
			button5calcAutomaticNettoprofit.setEnabled(false);
		} else
		{
			button5IsNettoprofit.setEnabled(true);
			button5OssNettoprofit.setEnabled(true);
			button5NetProfitRobust.setEnabled(true);
			button5calcAutomaticNettoprofit.setEnabled(true);
		}
		
		int mode = enddata_glob.getMode();
		setGlobalMode(mode);
		combo1cpart.add("OOS");
		combo1cpart.add("IS");

		if(Toolboxconf.getPropAttribute("shiftdays").equals("false"))
		{
			selektorShiftDays.setSelection(false);
			SelektorUseDeltaDaysConfigfile.setSelection(true);
		}
		else
		{
			selektorShiftDays.setSelection(true);
			SelektorUseDeltaDaysConfigfile.setSelection(false);
		}
		
		if(Toolboxconf.getPropAttribute("maxstepsback").equals("0"))
			MaxStepsBackFlag.setSelection(false);
		else
			MaxStepsBackFlag.setSelection(true);
		
	}
	
	private void setGlobalMode(int mode)
	{
		button5IsNettoprofit.setSelection(false);
		button5OssNettoprofit.setSelection(false);
		button5NetProfitRobust.setSelection(false);
		button5calcAutomaticNettoprofit.setEnabled(false);
		if (mode == 1)
			button5IsNettoprofit.setSelection(true);
		else if (mode == 2)
			button5OssNettoprofit.setSelection(true);
		else if (mode == 3)
			button5NetProfitRobust.setSelection(true);
	}
	
	private void init()
	{
		
		cw_glob = new CommentWork();
		cf_glob = new Commentflags();
		enddata_glob = new Endtestdata();
		
	}
	
	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void main(String[] args)
	{
		Display display = Display.getDefault();
		
		String userdir = System.getProperty("user.dir");
		if (args.length > 0)
			if (args[0] != null)
				userdir = args[0];
		if (userdir.contains("bin") == false)
		{
			Tracer.WriteTrace(10, "Falscher Verzeichnissaufbau userdir<" + userdir + ">");
			System.exit(0);
		}
		
		userdir = userdir.substring(0, userdir.lastIndexOf("\\"));
		GlobToolbox.setRootpath(userdir);
		Tracer.WriteTrace(20, "I:start toolbox userdir=<" + userdir + ">");
		Shell shell = new Shell(display);
		StartToolbox inst = new StartToolbox(shell, SWT.NULL);
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
		shell.setText("Toolbox V1.3.6.0    "+userdir);
		
		while (!shell.isDisposed())
		{
			
			if (!display.readAndDispatch())
				
				display.sleep();
		}
	}
	
	// ***************************************************************************
	// CommentTool CT
	// ***************************************************************************
	private void CTbutton1setdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setdir.widgetSelected, event=" + evt);
		// TODO add your code for button1setdir.widgetSelected
		String verz = CTtext2verzeichniss.getText();
		String dirname = cw_glob.selectDirectory(verz);
		CTtext2verzeichniss.setText(dirname);
	}
	
	private void setWorkdir(String workdir)
	{
		cw_glob.setWorkdir(workdir);
	}
	
	private void CTtext1prefixKeyTraversed(TraverseEvent evt)
	{
		System.out.println("CTtext1prefix.keyTraversed, event=" + evt);
		// TODO add your code for CTtext1prefix.keyTraversed
		cw_glob.setFileprefix(CTtext1prefix.getText());
	}
	
	private void CTRenameFiles2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("CTbutton2.widgetSelected, event=" + evt);
		
		Toolboxconf.setPropAttribute("commenttool_rdir", CTtext2verzeichniss.getText());
		cw_glob.setFileprefix(CTtext1prefix.getText());
		cw_glob.setMonthYear(text4magicprefix.getText());
		cw_glob.renameAllFiles();
		
	}
	
	private void CTbutton4addCommentWidgetSelected(SelectionEvent evt)
	{
		System.out.println("CTbutton4.widgetSelected, event=" + evt);
		// add Comment wurde gedrückt
		
		if (CTbutton1netprofit.getSelection() == true)
			cf_glob.setButtonprofit(1);
		else
			cf_glob.setButtonprofit(0);
		
		if (CTbutton2stabilitaet.getSelection() == true)
			cf_glob.setButtonstabilitaet(1);
		else
			cf_glob.setButtonstabilitaet(0);
		
		cf_glob.setPrefix(CTtext3prefix.getText());
		cf_glob.setNetprofitminval(SG.get_float_zahl(CTtext1netprofitminval.getText(), 5));
		cf_glob.setStabilminval(SG.get_float_zahl(CTtext1stabilminval.getText(), 5));
		cf_glob.setProfit(CTbutton1netprofit.getSelection());
		cf_glob.setStabilitaet(CTbutton2stabilitaet.getSelection());
		cw_glob.addComment(cf_glob);
	}
	
	private void CTbutton3cleanWidgetSelected(SelectionEvent evt)
	{
		System.out.println("CTbutton3clean.widgetSelected, event=" + evt);
		// TODO add your code for CTbutton3clean.widgetSelected
		// clean all comments
		cw_glob.cleanComments();
	}
	
	private void button1calcNettoprofitsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1calcNettoprofits.widgetSelected, event=" + evt);
		// calc Nettoprofitsummen
		
		cw_glob.calcNettoprofits();
	}
	
	private void button1deleteEndtestWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1deleteEndtest.widgetSelected, event=" + evt);
		// TODO add your code for button1deleteEndtest.widgetSelected
		cw_glob.deleteEndtest();
		
		button1deleteEndtest.setEnabled(false);
		button1generateEndtest.setEnabled(true);
		button5calcAutomaticNettoprofit.setEnabled(false);
		button1calcNettoprofits.setEnabled(false);
		
		button5IsNettoprofit.setEnabled(true);
		button5OssNettoprofit.setEnabled(true);
		button5NetProfitRobust.setEnabled(true);
	}
	
	private void button1generateEndtestWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1generateEndtest.widgetSelected, event=" + evt);
		// Hier wird der Endtest generiert
		System.out.println("************************************************** button generate endtest");
		
		int mode = 0;
		if (button5IsNettoprofit.getSelection() == true)
			mode = 1;
		else if (button5OssNettoprofit.getSelection() == true)
			mode = 2;
		else if (button5NetProfitRobust.getSelection() == true)
			mode = 3;
		
		cw_glob.generateEndtest(mode, SG.get_zahl(text1confidenz.getText()));
		button1deleteEndtest.setEnabled(true);
		button1generateEndtest.setEnabled(false);
		button1calcNettoprofits.setEnabled(true);
		button5calcAutomaticNettoprofit.setEnabled(true);
		button5IsNettoprofit.setEnabled(false);
		button5OssNettoprofit.setEnabled(false);
		button5NetProfitRobust.setEnabled(false);
	}
	
	private void button1calcEndtestgraphicWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1calcEndtestgraphic.widgetSelected, event=" + evt);
		// TODO add your code for button1calcEndtestgraphic.widgetSelected
		cw_glob.calcEndtestGraphic();
	}
	
	private void button1werteeinzelaus300WidgetSelected(SelectionEvent evt)
	{
		
		System.out.println("button1werteeinzelaus300.widgetSelected, event=" + evt);
		// TODO add your code for button1werteeinzelaus300.widgetSelected
		
		int maxddval = 0, maxddproz = 0, maxddfromstart = 0;
		int budget = 0, maxddbudget = 0;
		if (button1.getSelection() == true)
		{
			maxddval = SG.get_zahl((maxdd.getText()));
			if (maxddval == 0)
			{
				Mbox.Infobox("maxdd=0 is not allowed");
				return;
			}
		}
		if (button2.getSelection() == true)
		{
			maxddproz = SG.get_zahl(text1maxddproz.getText());
			if (maxddproz == 0)
			{
				Mbox.Infobox("maxdd=0 is not allowed");
				return;
			}
		}
		if (button3.getSelection() == true)
		{
			maxddfromstart = SG.get_zahl(text1maxddfromstart.getText());
			if (maxddfromstart == 0)
			{
				Mbox.Infobox("maxdd=0 is not allowed");
				return;
			}
		}
		
		int trailflag = 0;
		if (button4.getSelection() == true)
			trailflag = 1;
		
		if (button5.getSelection() == true)
		{
			budget = SG.get_zahl(text1budget.getText());
			maxddbudget = SG.get_zahl(text1maxdd.getText());
		}
		
		cw_glob.calcEndtestGraphic300(maxddval, maxddproz, maxddfromstart, trailflag,
				SG.get_zahl(text1maxddtrail.getText()), SG.get_zahl(text1ndays.getText()), budget, maxddbudget);
	}
	
	private void button1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1.widgetSelected, event=" + evt);
		// maxdd
		maxdd.setEnabled(button1.getSelection());
		button5.setSelection(false);
		setButton300();
	}
	
	private void setButton300()
	{
		
		if ((button1.getSelection() == false) && (button2.getSelection() == false) && (button3.getSelection() == false)
				&& (button4.getSelection() == false) && (button5.getSelection() == false))
			button1werteeinzelaus300.setEnabled(false);
		else
			button1werteeinzelaus300.setEnabled(true);
	}
	
	private void button2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2.widgetSelected, event=" + evt);
		// maxddprozent
		
		button5.setSelection(false);
		setButton300();
	}
	
	private void button3WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button3.widgetSelected, event=" + evt);
		text1maxddfromstart.setEnabled(button3.getSelection());
		button5.setSelection(false);
		setButton300();
		// TODO add your code for button3.widgetSelected
	}
	
	private void button4WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4.widgetSelected, event=" + evt);
		// TODO add your code for button4.widgetSelected
		button5.setSelection(false);
		setButton300();
	}
	
	private void button5IsNettoprofitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button5IsNettoprofit.widgetSelected, event=" + evt);
		// TODO add your code for button5IsNettoprofit.widgetSelected
		text1confidenz.setEnabled(false);
		label8.setEnabled(false);
	}
	
	private void button5OssNettoprofitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button5OssNettoprofit.widgetSelected, event=" + evt);
		// TODO add your code for button5OssNettoprofit.widgetSelected
		text1confidenz.setEnabled(false);
		label8.setEnabled(false);
	}
	
	private void button5NetProfitRobustWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button5NetProfitRobust.widgetSelected, event=" + evt);
		// TODO add your code for button5NetProfitRobust.widgetSelected
		text1confidenz.setEnabled(true);
		label8.setEnabled(true);
	}
	
	private void button5WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button5.widgetSelected, event=" + evt);
		// use budget
		button1.setSelection(false);
		button2.setSelection(false);
		button3.setSelection(false);
		button4.setSelection(false);
		setButton300();
	}
	
	private void button6Widgetconf100Selected(SelectionEvent evt)
	{
		System.out.println("button6.widgetSelected, event=" + evt);
		// set conf100tabelle
		String path = metriktools.setConf100quelle();
		text1conf100.setText(path);
	}
	
	private void button7conf50WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button7.widgetSelected, event=" + evt);
		// set conf50tabelle
		String path = metriktools.setConf50quelle();
		text2conf50.setText(path);
	}
	
	private void button8ZielmetrikWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8.widgetSelected, event=" + evt);
		// set Zielmetriktabelle
		String path = metriktools.setZieltable();
		text3zielmetrik.setText(path);
	}
	
	private void button9calculateWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button9calculate.widgetSelected, event=" + evt);
		// button calc
		metriktools.calcSave(text1attributname.getText());
	}
	
	private void button6set100sourcedirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6set100sourcedir.widgetSelected, event=" + evt);
		// TODO add your code for button6set100sourcedir.widgetSelected
		String quelldir = conf100tools.setQuelldir();
		text1_confsource.setText(quelldir);
	}
	
	private void button6conf100_50destinWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6conf100_50destin.widgetSelected, event=" + evt);
		// TODO add your code for button6conf100_50destin.widgetSelected
		String zieldir = conf100tools.setZieldir();
		conf100_50destinationdir.setText(zieldir);
	}
	
	private void button6calc100WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6calc100.widgetSelected, event=" + evt);
		// TODO add your code for button6calc100.widgetSelected
		conf100tools.filter(text2maxddrobustdiff.getText());
	}
	
	private void button6set_rt_sourcedirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6set_rt_sourcedir.widgetSelected, event=" + evt);
		String quelldir_gui = Guitools.Text_GetMessageText(rt_sourcedir);
		String quelldir = rttools.setQuelldirRequester(quelldir_gui);
		
		if (quelldir == null)
			quelldir = quelldir_gui;
		
		int n = FileAccess.countDirectoryContent(new File(quelldir));
		label17rtsourcedir.setText(String.valueOf(n));
		rt_sourcedir.setText(quelldir);
		
	}
	
	private void button6setRT_destdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6setRT_destdir.widgetSelected, event=" + evt);
		String zieldir_gui = Guitools.Text_GetMessageText(rt_destdir);
		String zieldir = rttools.setZieldirRequester(zieldir_gui);
		
		if (zieldir == null)
			zieldir = zieldir_gui;
		
		rt_destdir.setText(zieldir);
	}
	
	private void button6calcRtFilterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6calcRtFilter.widgetSelected, event=" + evt);
		// calculate filter
		// get values
		float maxddrobust_faktor = SG.get_float_zahl(faktorMaxDrawdownRobust.getText(), 5);
		int maxlosses = SG.get_zahl(text1maxlosses.getText());
		float longshortfaktor_faktor = SG.get_float_zahl(text1faktor.getText(), 2);
		
		String sourcedir = Guitools.Text_GetMessageText(rt_sourcedir);
		String destdir = Guitools.Text_GetMessageText(rt_destdir);
		
		File sdir = new File(sourcedir);
		if (sdir.exists() == false)
		{
			Mbox.Infobox("<" + sourcedir + "> not exists");
			return;
		}
		File ddir = new File(destdir);
		if (ddir.exists() == false)
		{
			Mbox.Infobox("<" + destdir + "> not exists");
			return;
		}
		boolean rtcheck = button6rt_checkrt.getSelection();
		boolean eqcheck = button6rtCheckequity.getSelection();
		boolean cut2yearsflag = button6cut2years.getSelection();
		boolean longshortflag = button6longshort.getSelection();
		boolean showtableresultflag = button6showtable.getSelection();
		
		rttools.setMaxlosses_glob(maxlosses);
		rttools.setMaxdrawdown_faktor_glob(maxddrobust_faktor);
		rttools.setLongshort_faktor_glob(longshortfaktor_faktor);
		
		// die config speichern
		
		Toolboxconf.setPropAttribute("maxlosses", String.valueOf(maxlosses));
		Toolboxconf.setPropAttribute("maxddrobust_faktor", String.valueOf(maxddrobust_faktor));
		
		// die attribute in der config speichern
		Toolboxconf.setPropAttribute("rt_sourcedir", sourcedir);
		Toolboxconf.setPropAttribute("rt_destdir", destdir);
		
		// die attribute in der struktur speichern
		rttools.setQuelldir_glob(sourcedir);
		rttools.setZieldir_glob(destdir);
		
		rttools.setEquityfaktor_glob(Float.valueOf(text1equityfaktor.getText()));
		boolean lastyearsflag = button6Lastyearsflag.getSelection();
		
		boolean xauusdflag = button6deleteifequal.getSelection();
		// hier wird gefiltert
		rttools.filter(rtcheck, eqcheck, lastyearsflag, cut2yearsflag, longshortflag, showtableresultflag, xauusdflag);
		
		// die Zahlen aktualisieren
		int n = FileAccess.countDirectoryContent(new File(rttools.getQuelldir_glob()));
		label17rtsourcedir.setText(String.valueOf(n));
		n = FileAccess.countDirectoryContent(new File(rttools.getZieldir_glob()));
		label17rtdestdir.setText(String.valueOf(n));
		
	}
	
	private void Cluster_StartGenerationWidgetSelected(SelectionEvent evt)
	{
		System.out.println("Cluster_StartGeneration.widgetSelected, event=" + evt);
		// TODO add your code for Cluster_StartGeneration.widgetSelected
		// Start clustering
		Cluster.genCluster(clustersourcedir_glob, clusterdestdir_glob);
	}
	
	private void ClusterSourceDirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("ClusterSourceDir.widgetSelected, event=" + evt);
		
		clustersourcedir_glob = rttools.setQuelldirRequester("E:\\Fuer Thomas2");
		cluster_strsourcedir.setText(clustersourcedir_glob);
		
	}
	
	private void clusterStrDestdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("clusterStrDestdir.widgetSelected, event=" + evt);
		clusterdestdir_glob = rttools.setQuelldirRequester("E:\\Fuer Thomas2");
		ClusterDestDir.setText(clusterdestdir_glob);
	}
	
	private void button6filterstrategiesWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6filterstrategies.widgetSelected, event=" + evt);
		trverarbeit.copyGoodResultfiles(text1.getText(), Float.valueOf(text1proz.getText()));
	}
	
	private void button6calcmatchingtableWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6calcmatchingtable.widgetSelected, event=" + evt);
		trverarbeit.calcShowResulttable(text1.getText());
	}
	
	private void button6tradefilterrootdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6tradefilterrootdir.widgetSelected, event=" + evt);
		String quelldir_gui = Guitools.Text_GetMessageText(text1);
		String quelldir = rttools.setQuelldirRequester(quelldir_gui);
		
		if (quelldir == null)
			quelldir = quelldir_gui;
		
		text1.setText(quelldir);
	}
	
	private void button6cleartradefilterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6cleartradefilter.widgetSelected, event=" + evt);
		trverarbeit.delall(text1.getText());
	}
	
	private void button6pdcWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6pdc.widgetSelected, event=" + evt);
		// set inputfile pdc_inputfile_glob;
		String quelldir_gui = Guitools.Text_GetMessageText(text2);
		String quelldir = MonDia.FileDialog(getDisplay(), quelldir_gui);
		
		if (quelldir == null)
			quelldir = quelldir_gui;
		
		text2.setText("F:\\Kurse\\Asikury\\EURUSD_Asirikuy_M1_part.csv");
		pdc_inputfile_glob = quelldir;
	}
	
	private void button6WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button6.widgetSelected, event=" + evt);
		// set pdc_outputfile_glob;
		String quelldir_gui = Guitools.Text_GetMessageText(text3pdc);
		String quelldir = MonDia.FileDialog(getDisplay(), quelldir_gui);
		
		if (quelldir == null)
			quelldir = quelldir_gui;
		
		text3pdc.setText("F:\\Kurse\\Asikury\\EURUSD_Asirikuy_M1_out.csv");
		pdc_outputfile_glob = quelldir;
	}
	
	private void button7pdcWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button7pdc.widgetSelected, event=" + evt);
		
		// pdc convert data
		// PriceDataSeries.PriceGmtConvertion(Integer.valueOf(text3.getText()),
		// pdc_inputfile_glob, pdc_outputfile_glob);
		PriceDataSeries.PriceGmtConvertion(Integer.valueOf(text3.getText()),
				"F:\\Kurse\\Asikury\\EURUSD_Asirikuy_M1_part.csv", "F:\\Kurse\\Asikury\\EURUSD_Asirikuy_M1_out.csv");
	}
	
	private void getInterfaceData()
	{
		// get all the data from interface to sqworkflow
		sqprojects.setMasterfile(FilterSourceDir.getText());
		sqprojects.setSqrootdir(text4filterdestdir.getText());
		sqprojects.setBackcount(steps.getText());
		sqprojects.setFuturecount(text4stepsfuture.getText());
		sqprojects.setOutputname(outputname.getText());
		sqprojects.setStepvalue(text4filterkeyword.getText());
		sqprojects.setResultdir(text4resultdir.getText());
		sqprojects.setSharedDrive(text4shareddrive.getText());
		sqprojects.setBackupDrive(text4backupdrive.getText());
		
		Toolboxconf.setPropAttribute("backcount", steps.getText());
		Toolboxconf.setPropAttribute("futurecount", text4stepsfuture.getText());
		Toolboxconf.setPropAttribute("anzdays", text4filterkeyword.getText());
		Toolboxconf.setPropAttribute("outputname", outputname.getText());
		Toolboxconf.setPropAttribute("INFOTEXT", text4infotext.getText());
		Toolboxconf.setPropAttribute("shareddrive", text4shareddrive.getText());
		Toolboxconf.setPropAttribute("backupdrive", text4backupdrive.getText());
		Toolboxconf.setPropAttribute("resultdir", text4resultdir.getText());
		Toolboxconf.setPropAttribute("masterfile", FilterSourceDir.getText());
		Toolboxconf.setPropAttribute("sqrootdir", text4filterdestdir.getText());
		Toolboxconf.setPropAttribute("anzprojectfiles", label29anzfiles.getText());
		Toolboxconf.setPropAttribute("portfolioname", text4databankname.getText());
		Toolboxconf.setPropAttribute("Endtestname", GetEnddateFromDatabase.getText());
	
		
		
		text4workflowstore.setText("The workflow will be stored in " + sqprojects.getSqRootdir() + "\\user\\projects");
		refreshProjectfilesanzahlMessages();
		
	}
	
	private void button7calcWidgetSelected(SelectionEvent evt)
	{
		// calc button used !! ************************************
		
		System.out.println("button7calc.widgetSelected CalcCfilter, event=" + evt);
		getInterfaceData();
		
		SqGoogle.WriteInfomessage(text4shareddrive.getText(), outputname.getText(), text4infotext.getText());
		
		if (button9shareddrive.getSelection())
		if (sqprojects.checkEmptyShareddrive() == false)
		{
			Mbox.Infobox(
					"E: directory <" + text4shareddrive.getText() + "\\" + outputname.getText() + "> should be empty");
			return;
		}
		int ret=Mbox.QuestBox("Warning","All old Workflows will be deleted !!\n Do you want to do this?");
		Tracer.WriteTrace(20, "Info: ret="+ret);
		if(ret==32)
		{
			sqprojects.genWorkflow(GetEnddateFromDatabase.getText());
			refreshProjectfilesanzahlMessages();
		}
		if (button9copyinshared.getSelection()==true)
			sqprojects.copyMasterfile(FilterSourceDir.getText(),text4shareddrive.getText());
	}
	
	private void button7setWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button7set.widgetSelected, event=" + evt);
		
		// get sourcedir from configfile
		String sd = Toolboxconf.getPropAttribute("masterfile");
		
		// ask user for sourcedir
		String masterfile = SWTwindow.FileDialog(getDisplay(), sd);
		
		if (masterfile == null)
			masterfile = sd;
		sqprojects.setMasterfile(masterfile);
		
		// show new sourcedir
		FilterSourceDir.setText(masterfile);
		// set new sourcedir in configfile
		Toolboxconf.setPropAttribute("masterfile", masterfile);
		
		// extract parendir from masterfile
		File masterfile_f = new File(masterfile);
		String pd = masterfile_f.getParent();
		if ((pd != null) && (pd.contains("\\")))
		{
			String noutdir = pd.substring(pd.lastIndexOf("\\") + 1);
			outputname.setText(noutdir);
			ModifiedOutputDirectory();
		}
	}
	
	private void button7setdestdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button7setdestdir.widgetSelected, event=" + evt);
		// get sqrootdir from configfile
		
		String dd = Toolboxconf.getPropAttribute("sqrootdir");
		
		// ask user for destdir
		String sqrootdir = SWTwindow.DirDialog(getDisplay(), dd);
		if (sqrootdir == null)
			sqrootdir = dd;
		
		// check sqrootdir
		String licfile = sqrootdir + "\\StrategyQuantX.exe";
		File sqr = new File(licfile);
		if (sqr.exists() == false)
		{
			Tracer.WriteTrace(10,
					"This is not an valid SQ installation StrategyQuantX.exe is missing under <" + licfile + ">");
			return;
		}
		sqprojects.setSqrootdir(sqrootdir);
		sqprojects.setSqrootdir(text4filterdestdir.getText());
		// show new destdir
		text4filterdestdir.setText(sqrootdir);
		// set new destdir in configfile
		Toolboxconf.setPropAttribute("sqrootdir", sqrootdir);
		label29anzfiles.setText(String.valueOf(sqprojects.getAnzProjectfiles()));
	}
	private void buttonSetConfigfileSelected(SelectionEvent evt)
	{}
	
	private void collectresultsbuttonWidgetSelected(SelectionEvent evt)
	{
		//Gewinn%, DD% und evtl. Gew./DD%
		
	
		
		//hier werden die Resultate eingesammelt
		//Als erstes wird mit cpart und combo1cpart gesagt was denn gesammelt werden soll
		String	cpart=combo1cpart.getText();
		int maxstepsbackval=Integer.valueOf(maxstepsback.getText());
	
		
		// button collect results !!!!!!!!!!!!
		System.out.println("collectresultsbutton.widgetSelected, event=" + evt);
		// get all information from the visual interface
		getInterfaceData();
		
		if (button9shareddrive.getSelection())
		SqGoogle.WriteInfomessage(text4shareddrive.getText(), outputname.getText(), text4infotext.getText());
		
		// collect all results and write infos in file
		// holt alle resulate aus den workflows
		int normval=Integer.valueOf(normn.getText());
		
		sqprojects.collectResults(button9shareddrive.getSelection(), button9backupdrive.getSelection(),
				button9showresults.getSelection(), text4databankname.getText(), cpart,normval,fullreportselection.getSelection(),outputname.getText(),maxstepsbackval
				);
		refreshProjectfilesanzahlMessages();
		
		
		
		if (button9shareddrive.getSelection())
			Toolboxconf.backupToolboxconf(text4shareddrive.getText() + "\\" + outputname.getText());
		Tracer.WriteTrace(20, "I:all results collected and stored under <" + text4resultdir.getText() + ">");
	}
	
	private void button7WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button7.widgetSelected, event=" + evt);
		// set resultdir
		String dd = Toolboxconf.getPropAttribute("resultdir");
		
		// ask user for destdir
		String resultdir = SWTwindow.DirDialog(getDisplay(), dd);
		if (resultdir == null)
			resultdir = dd;

		//C:\\forex\\toolbox\\SQ\\3 Results\\user\\projects\\Retester\\databanks\\Results
		sqprojects.setResultdir(resultdir);
		// show new resultdir
		text4resultdir.setText(resultdir);
		// set new resultdir in configfile
		Toolboxconf.setPropAttribute("resultdir", resultdir);
	}
	
	private void button8shareddirectoryWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8shareddirectory.widgetSelected, event=" + evt);
		// set resultdir
		String dd = Toolboxconf.getPropAttribute("shareddrive");
		
		// ask user for shareddrive
		String resultdir = SWTwindow.DirDialog(getDisplay(), dd);
		if (resultdir == null)
			resultdir = dd;
		
		sqprojects.setSharedDrive(resultdir);
		// show new resultdir
		text4shareddrive.setText(resultdir);
		// set new resultdir in configfile
		Toolboxconf.setPropAttribute("shareddrive", resultdir);
	}
	
	private void text4infotextWidgetSelected(SelectionEvent evt)
	{
		System.out.println("text4infotext.widgetSelected, event=" + evt);
		Tracer.WriteTrace(10, "widget selected");
		
	}
	
	private void button8loadinfomessageWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8loadinfomessage.widgetSelected, event=" + evt);
		text4infotext.setText("empty");
	}
	
	private void text4infotextFocusGained(FocusEvent evt)
	{
		System.out.println("text4infotext.focusGained, event=" + evt);
		// xxx die folgezeile verursacht ein Problem im jigloo builder, ist diese zeile
		// vorhanden können in der oberfläche nicht mehr
		// alle events konfiguriert werden
		text4infotext.setText(SqGoogle.ReadInfomessage(text4shareddrive.getText(), outputname.getText()));
	}
	
	private void button8setbackupdriveWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8setbackupdrive.widgetSelected, event=" + evt);
		// setbackupdrive
		// set resultdir
		String dd = Toolboxconf.getPropAttribute("backupdrive");
		
		// ask user for backupdrive
		String backupdrive = SWTwindow.DirDialog(getDisplay(), dd);
		if (backupdrive == null)
			backupdrive = dd;
		
		sqprojects.setBackupDrive(backupdrive);
		// show new backupdrive
		text4backupdrive.setText(backupdrive);
		// set new resultdir in configfile
		Toolboxconf.setPropAttribute("backupdrive", backupdrive);
	}
	
	private void outputnameModifyText(ModifyEvent evt)
	{
		System.out.println("outputname.modifyText, event=" + evt);
		// workflowname has changed, than check directory and load description window
		ModifiedOutputDirectory();
	}
	
	private void ModifiedOutputDirectory()
	{
		String genworkflowname = outputname.getText();
		text4infotext.setText(SqGoogle.ReadInfomessage(text4shareddrive.getText(), genworkflowname));
		sqprojects.setOutputname(genworkflowname);
		
		if (sqprojects.checkEmptyShareddrive() == false)
		{
			Mbox.Infobox("E: directory <" + text4shareddrive.getText() + "\\" + genworkflowname + "> should be empty");
			return;
		}
		
	}
	
	private void button8showResultdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8showResultdir.widgetSelected, event=" + evt);
		Shell shell = new Shell(Display.getDefault());
		FileDialog dialog = new FileDialog(shell, SWT.DefaultSelection);
		dialog.setFilterPath(sqprojects.getResultdir());
		dialog.open();
	}
	
	private void button8showshareddirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8showshareddir.widgetSelected, event=" + evt);
		// TODO add your code for button8showshareddir.widgetSelected
		getInterfaceData();
		Shell shell = new Shell(Display.getDefault());
		FileDialog dialog = new FileDialog(shell, SWT.DefaultSelection);
		dialog.setFilterPath(sqprojects.getSharedDrive());
		dialog.open();
	}
	
	private void button8showbackupdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8showbackupdir.widgetSelected, event=" + evt);
		// TODO add your code for button8showbackupdir.widgetSelected
		getInterfaceData();
		Shell shell = new Shell(Display.getDefault());
		FileDialog dialog = new FileDialog(shell, SWT.DefaultSelection);
		dialog.setFilterPath(sqprojects.getBackupDrive());
		dialog.open();
	}
	
	private void button8showprojectdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8showrootdir.widgetSelected, event=" + evt);
		// TODO add your code for button8showprojectdir.widgetSelected
		// get all data from interface into sqworkflow
		getInterfaceData();
		Shell shell = new Shell(Display.getDefault());
		FileDialog dialog = new FileDialog(shell, SWT.DefaultSelection);
		dialog.setFilterPath(sqprojects.getSqRootdir() + "\\user\\projects");
		dialog.open();
	}
	
	private void button8clearprojectdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8clearprojectdir.widgetSelected, event=" + evt);
		// TODO add your code for button8clearprojectdir.widgetSelected
		sqprojects.deleteProjectfiles();
		refreshProjectfilesanzahlMessages();
	}
	
	private void refreshProjectfilesanzahlMessages()
	{
		// falls kein rootdir gesetzt
		if (sqprojects.getSqRootdir() == null)
		{
			String rt = text4filterdestdir.getText();
			if (rt != null)
				sqprojects.setSqrootdir(rt);
			else
			{
				rt = Toolboxconf.getPropAttribute("sqrootdir");
				sqprojects.setSqrootdir(rt);
			}
		}
		label29anzfiles.setText(String.valueOf(sqprojects.getAnzProjectfiles()));
	}
	
	private void button8WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button8.widgetSelected, event=" + evt);
		SqGoogle.WriteInfomessage(text4shareddrive.getText(), outputname.getText(), text4infotext.getText());
	}
	
	private void button9shareddriveWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button9shareddrive.widgetSelected, event=" + evt);
		// TODO add your code for button9shareddrive.widgetSelected
		Boolean sel = button9shareddrive.getSelection();
		text4shareddrive.setEnabled(sel);
		button8shareddirectory.setEnabled(sel);
	}
	
	private void button9backupdriveWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button9backupdrive.widgetSelected, event=" + evt);
		// TODO add your code for button9backupdrive.widgetSelected
		Boolean sel = button9backupdrive.getSelection();
		text4backupdrive.setEnabled(sel);
		button8setbackupdrive.setEnabled(sel);
	}
	
	private void button9showresultsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button9showresults.widgetSelected, event=" + evt);
		// TODO add your code for button9showresults.widgetSelected
	}
	
	private void setdateconfigfile(SelectionEvent evt) {
		System.out.println("setdeltadays.widgetSelected, event="+evt);
		System.out.println("blup");
		
		String dd = Toolboxconf.getPropAttribute("deltadaysfile");
		
		// ask user for destdir
		String deltadaysfile = SWTwindow.FileDialog(getDisplay(), dd);
		if (deltadaysfile == null)
			Tracer.WriteTrace(10, "Error: empty file<"+deltadaysfile+">");

		deltadaysconfigfile.setText(deltadaysfile);
		Toolboxconf.setPropAttribute("deltadaysfile", deltadaysfile);
		
	}
	
	private void selektorShiftDaysWidgetSelected(SelectionEvent evt) {
		System.out.println("selektorShiftDays.widgetSelected, event="+evt);
		if(selektorShiftDays.getSelection()==false)
			Toolboxconf.setPropAttribute("shiftdays", "false");
		else
			Toolboxconf.setPropAttribute("shiftdays", "true");
		
		
			
	}
	
	private void nomalizeselectionWidgetSelected(SelectionEvent evt) {
		System.out.println("nomalizeselection.widgetSelected, event="+evt);
		//TODO add your code for nomalizeselection.widgetSelected
		if(nomalizeselection.getSelection()==true)
			normn.setEnabled(true);
		else
		{
			normn.setEnabled(false);
			normn.setText("0");
		}
	}
	
	private void normnWidgetSelected(SelectionEvent evt) {
		System.out.println("normn.widgetSelected, event="+evt);
		//TODO add your code for normn.widgetSelected
	}
	private void normnModifyText(ModifyEvent evt)
	{
		System.out.println("normn.modifyText, event=" + evt);
		//The normvalue has changed
		String nm=normn.getText();
		Toolboxconf.setPropAttribute("normn", nm);
		
	}
	
	private void combo1cpartModifyText(ModifyEvent evt) {
		System.out.println("combo1cpart.modifyText, event="+evt);
		//TODO add your code for combo1cpart.modifyText
		String sel=combo1cpart.getText();
		Toolboxconf.setPropAttribute("combo1cpart",sel);
	}
	
	private void fullreportselectionWidgetSelected(SelectionEvent evt) {
		System.out.println("fullreportselection.widgetSelected, event="+evt);
		boolean sel=fullreportselection.getSelection();
		if(sel==true)
			Toolboxconf.setPropAttribute("fullreportselection","1");
		else
			Toolboxconf.setPropAttribute("fullreportselection","0");
	}
	
	private void MaxStepsBackFlagWidgetSelected(SelectionEvent evt) {
		System.out.println("MaxDaysBackFlag.widgetSelected, event="+evt);
		//If selected than activate the number
		maxstepsback.setEnabled(MaxStepsBackFlag.getSelection());
		
		if(MaxStepsBackFlag.getSelection()==true)
		{
			Toolboxconf.setPropAttribute("maxstepsback",maxstepsback.getText());
		}
		else
			  Toolboxconf.setPropAttribute("maxstepsback", "0");
	}
	
	private void button9showfileWidgetSelected(SelectionEvent evt) {
		System.out.println("button9showfile.widgetSelected, event="+evt);
		  try {
	            CfxDateViewer.processCfxFile(Display.getDefault(),FilterSourceDir.getText());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	final Runnable inittimer = new Runnable() {
		public void run()
		{
			
			System.out.println("inittimer run once");
			text4infotext.setText(SqGoogle.ReadInfomessage(text4shareddrive.getText(), outputname.getText()));
			
		}
	};
	
}
