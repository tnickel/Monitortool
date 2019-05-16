package Main;

import gui.DisTool;
import gui.Mbox;
import hilfsklasse.SG;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import userinterface.SetConfig;
import userinterface.SetCorrelationConfig;
import userinterface.SetFilter;
import Metriklibs.EndtestFitnessfilter;
import calcPack.CalOpt100;
import calcPack.CalculationOneSetting;

import com.cloudgarden.resource.SWTResourceManager;

import data.CorelSetting;
import data.EndtestResult;
import data.MFilter;
import data.Metrikglobalconf;

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
public class NewSWTApp extends org.eclipse.swt.widgets.Composite
{

	private Menu menu1;
	private Text text1steps;
	private Label label1;
	private Button button1setfilter;
	private Text text1anzrestmenge;
	private Label label2;
	private Text text1maxprof;
	private Text text1count;
	private Table table1;
	private Button button1stabil;
	private Button button1nettorobuststabil;
	private CTabItem cTabItem3;
	private Group group2;
	private Button button1nettorobust;
	private Button button1nettostabilitaet;
	private Button button1nettoprofit;
	private Group group1;
	private Composite composite2;
	private Composite composite1;
	private CTabItem cTabItem2;
	private CTabItem cTabItem1;
	private CTabFolder cTabFolder1;
	private Button button1setCorrelSettings;
	private Button button2showbestresult;
	private Button button2usecorrelation;
	private Button button2userandom;
	private Group group3;
	private Button button2calcCorrelation;
	private Button button1stop;
	private Label label1anzsteps;
	private Label label3;
	private Button button1calcoptimize;
	private Button button1calcgraphic;
	private Button button1cleanConfig;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private Button button1calc;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private MFilter mfilter_glob = new MFilter();
	private CorelSetting corelsetting_glob=new CorelSetting();
	private Button button1scanallmetrics;
	// calone speichert nur eine filterung
	CalculationOneSetting calone = new CalculationOneSetting();
	// in calopt100 werden die besten 100 filterungen ermittelt und gespeichert
	CalOpt100 calopt100 = new CalOpt100();

	//hier werden die fitnessfunktionen für den endtest gespeichert
	EndtestFitnessfilter endtestfitnessfilter_glob= new EndtestFitnessfilter();
	
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public NewSWTApp(Composite parent, int style)
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
			this.setSize(899, 560);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				cTabFolder1 = new CTabFolder(this, SWT.NONE);
				{
					cTabItem1 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem1.setText("Filter");
					{
						composite1 = new Composite(cTabFolder1, SWT.NONE);
						composite1.setLayout(null);
						cTabItem1.setControl(composite1);
						{
							button2calcCorrelation = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button2calcCorrelation.setText("CalcCorrelation");
							button2calcCorrelation.setBounds(116, 102, 106, 23);
							button2calcCorrelation
							.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									button2calcCorrelationWidgetSelected(evt);
								}
							});
						}
						{
							button1scanallmetrics = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button1scanallmetrics.setText("ScanAllMetrics");
							button1scanallmetrics.setEnabled(false);
							button1scanallmetrics.setBounds(116, 137, 106, 25);
							button1scanallmetrics
							.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									button1scanallmetricsWidgetSelected(evt);
								}
							});
						}
						{
							button1calcgraphic = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button1calcgraphic.setText("Calc & Graphik");
							button1calcgraphic.setBounds(116, 65, 106, 25);
							button1calcgraphic
							.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									button1calcgraphicWidgetSelected(evt);
								}
							});
						}
						{
							button1calc = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button1calc.setText("Calc");
							button1calc.setBounds(116, 28, 106, 25);
							button1calc.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									button1calcWidgetSelected(evt);
								}
							});
						}
						{
							button1cleanConfig = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button1cleanConfig.setText("CleanFilter");
							button1cleanConfig.setBounds(45, 28, 65, 25);
							button1cleanConfig
							.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									button1cleanConfigWidgetSelected(evt);
								}
							});
						}
					}
				}
				{
					cTabItem2 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem2.setText("Calc and Optimize");
					{
						composite2 = new Composite(cTabFolder1, SWT.NONE);
						composite2.setLayout(null);
						cTabItem2.setControl(composite2);
						{
							group3 = new Group(composite2, SWT.NONE);
							group3.setLayout(null);
							group3.setText("Calc and Optimize");
							group3.setBounds(6, 7, 244, 207);
							{
								button1calcoptimize = new Button(group3, SWT.PUSH
										| SWT.CENTER);
								button1calcoptimize.setText("Calc and Optimize");
								button1calcoptimize.setBounds(93, 169, 127, 25);
								button1calcoptimize
								.addSelectionListener(new SelectionAdapter()
								{
									public void widgetSelected(
											SelectionEvent evt)
									{
										button1calcoptimizeWidgetSelected(evt);
									}
								});
							}
							{
								text1steps = new Text(group3, SWT.NONE);
								text1steps.setText("5");
								text1steps.setBounds(12, 20, 69, 21);
								text1steps.addSelectionListener(new SelectionAdapter()
								{
									public void widgetSelected(SelectionEvent evt)
									{
										text1stepsWidgetSelected(evt);
									}
								});
							}
							{
								label1anzsteps = new Label(group3, SWT.NONE);
								label1anzsteps.setText("#steps");
								label1anzsteps.setBounds(93, 20, 58, 21);
							}
							{
								text1count = new Text(group3, SWT.NONE);
								text1count.setText("0");
								text1count.setEditable(false);
								text1count.setBounds(12, 41, 69, 21);
							}
							{
								label1 = new Label(group3, SWT.NONE);
								label1.setText("# akt. Step");
								label1.setBounds(93, 41, 70, 21);
							}
							{
								button2userandom = new Button(group3, SWT.RADIO
										| SWT.LEFT);
								button2userandom.setText("use random optimization");
								button2userandom.setBounds(12, 68, 163, 30);
							}
							{
								button2usecorrelation = new Button(group3, SWT.RADIO
										| SWT.LEFT);
								button2usecorrelation.setText("use correl. optimaziation");
								button2usecorrelation.setBounds(12, 96, 150, 30);
								button2usecorrelation.setSelection(true);
							}
							{
								button2showbestresult = new Button(group3, SWT.CHECK
										| SWT.LEFT);
								button2showbestresult.setText("show best result");
								button2showbestresult.setBounds(12, 132, 115, 20);
							}
							{
								button1setCorrelSettings = new Button(group3, SWT.PUSH | SWT.CENTER);
								button1setCorrelSettings.setText("setCor");
								button1setCorrelSettings.setBounds(174, 104, 58, 18);
								button1setCorrelSettings.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt) {
										button1setCorrelSettingsWidgetSelected(evt);
									}
								});
							}
							{
								button1setfilter = new Button(group3, SWT.PUSH | SWT.CENTER);
								button1setfilter.setText("setFilter");
								button1setfilter.setBounds(163, 20, 69, 21);
								button1setfilter
								.addSelectionListener(new SelectionAdapter()
								{
									public void widgetSelected(SelectionEvent evt)
									{
										button1setfilterWidgetSelected(evt);
									}
								});
							}
						}
						{
							button1stop = new Button(composite2, SWT.PUSH | SWT.CENTER);
							button1stop.setText("stop and save");
							button1stop.setBounds(478, 176, 199, 25);
							button1stop.addSelectionListener(new SelectionAdapter()
							{
								public void widgetSelected(SelectionEvent evt)
								{
									button1stopWidgetSelected(evt);
								}
							});
						}
						{
							group1 = new Group(composite2, SWT.NONE);
							group1.setLayout(null);
							group1.setText("fitness");
							group1.setBounds(256, 12, 169, 164);
							{
								button1nettoprofit = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettoprofit.setText("nettoprofit");
								button1nettoprofit.setBounds(12, 27, 145, 25);
							}
							{
								button1nettostabilitaet = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettostabilitaet.setText("nettoprofit * stabilitaet");
								button1nettostabilitaet.setBounds(12, 51, 145, 30);
								button1nettostabilitaet.setSelection(true);
							}
							{
								button1nettorobust = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettorobust.setText("netto robust");
								button1nettorobust.setBounds(12, 84, 145, 20);
							}
							{
								button1nettorobuststabil = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettorobuststabil.setText("netto robust * stabilitaet");
								button1nettorobuststabil.setBounds(12, 110, 145, 20);
							}
							{
								button1stabil = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1stabil.setText("stabilitaet");
								button1stabil.setBounds(12, 136, 87, 23);
							}
						}
						{
							group2 = new Group(composite2, SWT.NONE);
							group2.setLayout(null);
							group2.setText("result");
							group2.setBounds(431, 12, 174, 75);
							{
								text1maxprof = new Text(group2, SWT.NONE);
								text1maxprof.setText("0");
								text1maxprof.setEditable(false);
								text1maxprof.setBounds(6, 21, 75, 16);
							}
							{
								label2 = new Label(group2, SWT.NONE);
								label2.setText("bestStrat");
								label2.setBounds(87, 21, 75, 16);
							}
							{
								text1anzrestmenge = new Text(group2, SWT.NONE);
								text1anzrestmenge.setText("0");
								text1anzrestmenge.setEditable(false);
								text1anzrestmenge.setBounds(6, 42, 75, 16);
							}
							{
								label3 = new Label(group2, SWT.NONE);
								label3.setText("#strat");
								label3.setBounds(87, 42, 75, 16);
							}
						}
					}
				}
				{
					cTabItem3 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem3.setText("Results");
					{
						table1 = new Table(cTabFolder1, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
						cTabItem3.setControl(table1);
						table1.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt) {
								table1WidgetSelected(evt);
							}
						});
					}
				}
				FormData cTabFolder1LData = new FormData();
				cTabFolder1LData.left =  new FormAttachment(0, 1000, 21);
				cTabFolder1LData.top =  new FormAttachment(0, 1000, 22);
				cTabFolder1LData.width = 689;
				cTabFolder1LData.height = 489;
				cTabFolder1.setLayoutData(cTabFolder1LData);
				cTabFolder1.setSelection(0);
			}
			this.addPaintListener(new PaintListener()
			{
				public void paintControl(PaintEvent evt)
				{
					thisPaintControl(evt);
				}
			});
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
			}
			readparam();
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void readparam()
	{
		endtestfitnessfilter_glob.setNettoflag(button1nettoprofit.getSelection());
		endtestfitnessfilter_glob.setNettorobustflag(button1nettorobust.getSelection());
		endtestfitnessfilter_glob.setNettorobuststabilflag(button1nettorobuststabil.getSelection());
		endtestfitnessfilter_glob.setNettostabilflag(button1nettostabilitaet.getSelection());
		endtestfitnessfilter_glob.setNettorobustflag(button1nettorobust.getSelection());
		endtestfitnessfilter_glob.setNettorobuststabilflag(button1nettorobuststabil.getSelection());
		endtestfitnessfilter_glob.setStabilflag(button1stabil.getSelection());
	}
	
	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		NewSWTApp inst = new NewSWTApp(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();

		DisTool dt = new DisTool(display, shell);

		String userdir = System.getProperty("user.dir");
		String version = "Version V0.013";
	

		if (args.length > 0)
			if (args[0] != null)
				userdir = args[0];
		
		Metrikglobalconf meconf = new Metrikglobalconf(userdir);
		shell.setText("Metrikanalyser " + version+ "          Store Strategies in directory="+userdir);
		System.out.println("akueller pfad=" + userdir);
		
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
		// TODO add your code for newFileMenuItem.widgetSelected
		SetConfig.main(null);
	}

	private void button1calcWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1scanDir.widgetSelected, event=" + evt);
		// hier werden die Datenstrukturen eingelesen und aufgebaut
		calone.readMetriktabellen();

		if (calone.checkConfigAvailable() == true)
			calone.readConfig();
		else
			calone.genConfig();
		EndtestResult endresult = calone.startCalculation(0,endtestfitnessfilter_glob);
		Mbox.Infobox("ready");
	}

	private void button1cleanConfigWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1genConfig.widgetSelected, event=" + evt);
		// TODO add your code for button1genConfig.widgetSelected
		calone.readMetriktabellen();
		calone.cleanConfig();
		Mbox.Infobox("Filter cleaned");
	}

	private void button1calcgraphicWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1calcgraphic.widgetSelected, event=" + evt);
		// TODO add your code for button1calcgraphic.widgetSelected
		calone.readMetriktabellen();

		if (calone.checkConfigAvailable() == true)
			calone.readConfig();
		else
		{
			calone.genConfig();
			calone.genStrDirs();
		}
		calone.startCalculation(1,endtestfitnessfilter_glob);
		calone.showGraphik();
	}

	private void button1calcoptimizeWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1calcoptimize.widgetSelected, event=" + evt);

		// Hier wrid die optimale Metrikkombination gesucht
		int randomflag = 1;

		// Erst mal alle Metriktabellen einlesen
		Metrikglobalconf.setStopflag_glob(0);
		calopt100.readMetriktabellen();

		// falls schon filter da sind die einlesen
		if (calopt100.checkConfigAvailable() == true)
			calopt100.readFilterzeitraume();
		else
			// wenn keine Filter da sind dann generiere das Standartfile
			calopt100.genConfig();

		int stepanzahl = SG.get_zahl(text1steps.getText());

		// correlation selection
		if (button2usecorrelation.getSelection() == true)
		{
			randomflag = 0;
			// korreliere und fülle die globale Klasse
			calopt100.correlate(0);
		}
		// hier wird mn mal nach dem besten gesucht
		calopt100.startCalculation(1, Integer.valueOf(stepanzahl), text1count,
				text1maxprof, text1anzrestmenge, mfilter_glob, randomflag,corelsetting_glob,endtestfitnessfilter_glob);
		// die n Ergebnisse werden gespeichert
		calopt100.storeResults();

		//
		if (button2showbestresult.getSelection() == true)
		{

			// das beste umkopieren
			calopt100.kopiereBestStrfiles();

			// das beste Ergebniss anzeigen
			calopt100.showGraphik();
		}
		//hier wird die resulttable gesetzt, 
		//die tabelle ist im resulttab zu finden
		calopt100.setTable(table1);
		Mbox.Infobox("ready");

	}

	private void text1stepsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("text1steps.widgetSelected, event=" + evt);
		// TODO add your code for text1steps.widgetSelected
		// hier wurde die stepanzahl geändert
	}

	private void button1setfilterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setfilter.widgetSelected, event=" + evt);
		// set Filtersettings
		SetFilter.showGUI(mfilter_glob);
	}

	private void button1scanallmetricsWidgetSelected(SelectionEvent evt)
	{
		System.out
				.println("button1scanallmetrics.widgetSelected, event=" + evt);
		// Hier werden alle Metriken gescannt
		// d.h. die Metriken laufen von 0.....n und hierzu wird die entsprechende
		// equitykurve gezeichnet.
		
	}

	private void button1stopWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1stop.widgetSelected, event=" + evt);
		Metrikglobalconf.setStopflag_glob(1);
	}

	private void button2calcCorrelationWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2calcCorrelation.widgetSelected, event="
				+ evt);
		// hier wird die Korrelation für jede einzelne Metrik berechnet und in
		// einer Tabelle dargestellt.

		Metrikglobalconf.setStopflag_glob(0);

		// Dann korrelieren
		calopt100.correlate(1);
		Mbox.Infobox("ready");
	}

	private void thisPaintControl(PaintEvent evt)
	{
		System.out.println("this.paintControl, event=" + evt);
		// TODO add your code for this.paintControl

		DisTool.UpdateDisplay2();
	}
	
	private void button1setCorrelSettingsWidgetSelected(SelectionEvent evt) {
		System.out.println("button1setCorrelSettings.widgetSelected, event="+evt);
		SetCorrelationConfig.showGUI(corelsetting_glob);
		System.out.println("done");
	}
	
	private void table1WidgetSelected(SelectionEvent evt) {
		System.out.println("table1.widgetSelected, event="+evt);
		//TODO add your code for table1.widgetSelected
	}

}
