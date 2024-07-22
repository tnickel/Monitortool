package Main;

import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;

import Batch.AutoWorkflow;
import Metriklibs.EndtestFitnessfilter;
import Metriklibs.Metriktabellen;
import Metriklibs.StrategieMengen;
import WekaTools.WekaResultCollector;
import calcPack.CalOpt100Sammler;
import calcPack.CalculationOneSetting;
import data.Best100Portfolios;
import data.CorelSetting;
import data.EndtestResult;
import data.MFilter;
import data.Metrikglobalconf;
import gui.DisTool;
import gui.Mbox;
import hiflsklasse.SWTwindow;
import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import java.awt.event.ActionEvent;
import userinterface.SetConfig;
import userinterface.SetCorrelationConfig;
import userinterface.SetFilter;

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
	private CLabel cLabel1;
	private Text text1trees;
	private Label label13;
	private Text text1crossvalidateanz;
	private Label label12;
	private Label label11;
	private Button button4batchexportlearn;
	private Button button4setnewdata2;
	private Label label10;
	private Text text1newDatafile2;
	private Label label8;
	private Button button4setNewfile;
	private Button button4setLearnerfile;
	private Text newDataFile;
	private Button button4WekaPredict;
	private Text wekaLearnDatafile;
	private Button WekaLearn;
	private Label label6;
	private Button button3;
	private Label label9;
	private Text text5;
	private Text text2;
	private Button button2;
	private Button button1;
	private Composite componenteWeka;
	private Button button1random2;
	private Label label7;
	private Label label5;
	private Text anzinstore;
	private Combo combo1CorelationType;
	private Button buttonUseOnlySelectedMetrics;
	private Button buttonUseAllMetrics;
	private Composite composite3;
	private Button buttonAllMetrics;
	private Button nettoprofitperstrategy;
	private Label label4;
	private Text attributnameendtest;
	private Button userandomplusopti;
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
	private CorelSetting corelsetting_glob = new CorelSetting();
	private Button button1scanallmetrics;
	// calone speichert nur eine filterung
	CalculationOneSetting calone = new CalculationOneSetting();
	// in calopt100 werden die besten 100 filterungen ermittelt und gespeichert
	CalOpt100Sammler calopt100 = new CalOpt100Sammler();
	StrategieMengen strategienselector_glob = null;
	
	// hier werden die fitnessfunktionen für den endtest gespeichert
	EndtestFitnessfilter endtestfitnessfilter_glob = new EndtestFitnessfilter();
	
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
			this.setSize(1279, 623);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				label6 = new Label(this, SWT.NONE);
				FormData label6LData = new FormData();
				label6LData.left = new FormAttachment(0, 1000, 189);
				label6LData.top = new FormAttachment(0, 1000, 346);
				label6LData.width = 207;
				label6LData.height = 16;
				label6.setLayoutData(label6LData);
				label6.setText("(convert endtest_weka attribute to 0/1)");
			}
			
			{
				cTabFolder1 = new CTabFolder(this, SWT.NONE);
				{
					cTabItem1 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem1.setText("Filter");
					{
						button4setnewdata2 = new Button(cTabFolder1, SWT.PUSH | SWT.CENTER);
						cTabItem1.setControl(button4setnewdata2);
						button4setnewdata2.setText("Set");
						button4setnewdata2.setBounds(566, 237, 25, 22);
						button4setnewdata2.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt)
							{
								button4setnewdata2WidgetSelected(evt);
							}
						});
					}
					{
						label10 = new Label(cTabFolder1, SWT.NONE);
						cTabItem1.setControl(label10);
						label10.setText("Load Attributefile: this file will be clasified by the Predicter");
						label10.setBounds(32, 222, 365, 18);
					}
					{
						text1newDatafile2 = new Text(cTabFolder1, SWT.NONE);
						cTabItem1.setControl(text1newDatafile2);
						text1newDatafile2.setText("set new Data");
						text1newDatafile2.setBounds(31, 245, 528, 17);
					}
					{
						button4WekaPredict = new Button(cTabFolder1, SWT.PUSH | SWT.CENTER);
						cTabItem1.setControl(button4WekaPredict);
						button4WekaPredict.setText("WekaPredict");
						button4WekaPredict.setBounds(616, 181, 138, 75);
						button4WekaPredict.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt)
							{
								button4WekaPredictWidgetSelected(evt);
							}
						});
					}
					{
						composite1 = new Composite(cTabFolder1, SWT.NONE);
						cTabItem1.setControl(composite1);
						composite1.setLayout(null);
						{
							button2calcCorrelation = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button2calcCorrelation.setText("CalcCorrelation");
							button2calcCorrelation.setBounds(116, 102, 107, 24);
							button2calcCorrelation.addSelectionListener(new SelectionAdapter() {
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
							button1scanallmetrics.addSelectionListener(new SelectionAdapter() {
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
							button1calcgraphic.setEnabled(false);
							button1calcgraphic.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button1calcgraphicWidgetSelected(evt);
								}
							});
						}
						{
							button1calc = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button1calc.setText("Use Filtering");
							button1calc.setBounds(116, 28, 106, 25);
							button1calc.addSelectionListener(new SelectionAdapter() {
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
							button1cleanConfig.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button1cleanConfigWidgetSelected(evt);
								}
							});
						}
						{
							attributnameendtest = new Text(composite1, SWT.BORDER);
							attributnameendtest.setText("Net profit (OOS)");
							attributnameendtest.setBounds(425, 102, 125, 23);
							attributnameendtest.setEnabled(false);
						}
						{
							label4 = new Label(composite1, SWT.NONE);
							label4.setText("Attributname for Endtest");
									label4.setBounds(556, 102, 143, 20);
						}
						{
							combo1CorelationType = new Combo(composite1, SWT.NONE);
							combo1CorelationType.setText("CorrelationType");
							combo1CorelationType.setBounds(228, 102, 138, 20);
						}
						{
							componenteWeka = new Composite(composite1, SWT.BORDER);
							componenteWeka.setLayout(null);
							componenteWeka.setBounds(45, 218, 879, 328);
							{
								button1 = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button1.setText("ExportDataForWeka");
								button1.setBounds(609, 19, 140, 48);
								button1.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1WidgetSelected(evt);
									}
								});
							}
							{
								button2 = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button2.setText("Set");
								button2.setBounds(579, 20, 25, 21);
								button2.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button2WidgetSelected(evt);
									}
								});
							}
							{
								text2 = new Text(componenteWeka, SWT.NONE);
								String fnam = Metrikglobalconf.getWekafile_glob();
								if (fnam != null)
									text2.setText(fnam);
								else
									text2.setText("Set Weka importfile");
								text2.setBounds(29, 19, 545, 21);
							}
							{
								text5 = new Text(componenteWeka, SWT.NONE);
								text5.setText("5000");
								text5.setBounds(36, 46, 43, 21);
							}
							{
								label9 = new Label(componenteWeka, SWT.NONE);
								label9.setText("MaxStrategiesInExportfile");
								label9.setBounds(100, 46, 142, 21);
							}
							{
								button3 = new Button(componenteWeka, SWT.CHECK | SWT.LEFT);
								button3.setText("make binary");
								button3.setBounds(22, 74, 86, 30);
							}
							{
								WekaLearn = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								WekaLearn.setText("WekaLearn");
								WekaLearn.setBounds(613, 117, 139, 22);
								WekaLearn.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										WekaLearnWidgetSelected(evt);
									}
								});
							}
							{
								wekaLearnDatafile = new Text(componenteWeka, SWT.NONE);
								wekaLearnDatafile.setText("Learner file");
								wekaLearnDatafile.setBounds(28, 128, 551, 21);
							}
							{
								newDataFile = new Text(componenteWeka, SWT.NONE);
								newDataFile.setText("newDataFile");
								newDataFile.setBounds(30, 182, 528, 23);
							}
							{
								button4setLearnerfile = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button4setLearnerfile.setText("Set");
								button4setLearnerfile.setBounds(584, 126, 22, 21);
								button4setLearnerfile.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4setLearnerfileWidgetSelected(evt);
									}
								});
							}
							{
								button4setNewfile = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button4setNewfile.setText("Set");
								button4setNewfile.setBounds(563, 179, 21, 21);
								button4setNewfile.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4setNewfileWidgetSelected(evt);
									}
								});
							}
							{
								label8 = new Label(componenteWeka, SWT.NONE);
								label8.setText("Load learner: The predicter needs a learned model, I load this model");
								label8.setBounds(29, 161, 410, 18);
							}
							{
								label11 = new Label(componenteWeka, SWT.NONE);
								label11.setText("Load Attributefile: This is the attribute File which we will learned. The Model will be stored in the directory");
								label11.setBounds(30, 103, 579, 18);
							}
							{
								label12 = new Label(componenteWeka, SWT.NONE);
								label12.setText("Store Attributes: We geneate a Attributefile for Weka, store this under this location");
										label12.setBounds(31, 2, 492, 21);
							}
							{
								text1crossvalidateanz = new Text(componenteWeka, SWT.NONE);
								text1crossvalidateanz.setText("3");
								text1crossvalidateanz.setBounds(34, 271, 28, 23);
							}
							{
								label13 = new Label(componenteWeka, SWT.NONE);
								label13.setText("Instanz anzahl Crossvalidation");
								label13.setBounds(76, 273, 167, 30);
							}
						}
						{
							button4batchexportlearn = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button4batchexportlearn.setText("Autobatch ExportAndLearn");
							button4batchexportlearn.setBounds(959, 218, 176, 65);
							button4batchexportlearn.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									button4batchexportlearnWidgetSelected(evt);
								}
							});
						}
						{
							text1trees = new Text(composite1, SWT.NONE);
							text1trees.setText("100");
							text1trees.setBounds(965, 338, 43, 21);
						}
						{
							cLabel1 = new CLabel(composite1, SWT.NONE);
							cLabel1.setText("Number of Trees");
							cLabel1.setBounds(1014, 329, 129, 30);
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
							group3.setBounds(6, 12, 244, 248);
							{
								button1calcoptimize = new Button(group3, SWT.PUSH | SWT.CENTER);
								button1calcoptimize.setText("Calc and Optimize");
								button1calcoptimize.setBounds(105, 211, 127, 25);
								button1calcoptimize.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1calcoptimizeWidgetSelected(evt);
									}
								});
							}
							{
								text1steps = new Text(group3, SWT.NONE);
								text1steps.setText("5000");
								text1steps.setBounds(12, 20, 69, 21);
								text1steps.addSelectionListener(new SelectionAdapter() {
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
								button2userandom = new Button(group3, SWT.RADIO | SWT.LEFT);
								button2userandom.setText("use random optimization");
								button2userandom.setBounds(12, 68, 163, 30);
								button2userandom.setSelection(true);
							}
							{
								button2usecorrelation = new Button(group3, SWT.RADIO | SWT.LEFT);
								button2usecorrelation.setText("use correl. optimaziation");
								button2usecorrelation.setBounds(12, 96, 150, 30);
							}
							{
								button2showbestresult = new Button(group3, SWT.CHECK | SWT.LEFT);
								button2showbestresult.setText("show best result");
								button2showbestresult.setBounds(12, 185, 115, 20);
								button2showbestresult.setEnabled(false);
							}
							{
								button1setCorrelSettings = new Button(group3, SWT.PUSH | SWT.CENTER);
								button1setCorrelSettings.setText("setCor");
								button1setCorrelSettings.setBounds(174, 104, 58, 18);
								button1setCorrelSettings.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1setCorrelSettingsWidgetSelected(evt);
									}
								});
							}
							{
								button1setfilter = new Button(group3, SWT.PUSH | SWT.CENTER);
								button1setfilter.setText("setFilter");
								button1setfilter.setBounds(163, 20, 69, 21);
								button1setfilter.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button1setfilterWidgetSelected(evt);
									}
								});
							}
							{
								userandomplusopti = new Button(group3, SWT.RADIO | SWT.LEFT);
								userandomplusopti.setText("use random++ optimization");
								userandomplusopti.setBounds(12, 120, 182, 30);
							}
							{
								button1random2 = new Button(group3, SWT.RADIO | SWT.LEFT);
								button1random2.setText("use random2");
								button1random2.setBounds(12, 143, 134, 30);
							}
						}
						{
							button1stop = new Button(composite2, SWT.PUSH | SWT.CENTER);
							button1stop.setText("stop and save");
							button1stop.setBounds(478, 176, 199, 25);
							button1stop.addSelectionListener(new SelectionAdapter() {
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
							group1.setBounds(256, 12, 199, 210);
							{
								button1nettoprofit = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettoprofit.setText("nettoprofit (OOS)");
								button1nettoprofit.setBounds(12, 27, 145, 25);
							}
							{
								button1nettostabilitaet = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettostabilitaet.setText("nettoprofit * stabilitaet (OOS)");
								button1nettostabilitaet.setBounds(12, 85, 180, 30);
								button1nettostabilitaet.setSelection(false);
								button1nettostabilitaet.setEnabled(false);
							}
							{
								button1nettorobust = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettorobust.setText("netto robust (OOS)");
								button1nettorobust.setBounds(12, 121, 145, 20);
								button1nettorobust.setEnabled(false);
							}
							{
								button1nettorobuststabil = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1nettorobuststabil.setText("netto robust * stabilitaet (OOS)");
								button1nettorobuststabil.setBounds(12, 150, 180, 20);
								button1nettorobuststabil.setEnabled(false);
							}
							{
								button1stabil = new Button(group1, SWT.RADIO | SWT.LEFT);
								button1stabil.setText("stabilitaet (OOS)");
								button1stabil.setBounds(12, 175, 124, 23);
								button1stabil.setEnabled(false);
							}
							{
								nettoprofitperstrategy = new Button(group1, SWT.RADIO | SWT.LEFT);
								nettoprofitperstrategy.setText("nettoprofit per strategy (OOS)");
								nettoprofitperstrategy.setBounds(12, 55, 175, 30);
								nettoprofitperstrategy.setSelection(true);
							}
						}
						{
							group2 = new Group(composite2, SWT.NONE);
							group2.setLayout(null);
							group2.setText("result");
							group2.setBounds(461, 12, 147, 97);
							{
								text1maxprof = new Text(group2, SWT.NONE);
								text1maxprof.setText("0");
								text1maxprof.setEditable(false);
								text1maxprof.setBounds(6, 18, 75, 16);
							}
							{
								label2 = new Label(group2, SWT.NONE);
								label2.setText("bestStrat");
								label2.setBounds(87, 18, 75, 16);
							}
							{
								text1anzrestmenge = new Text(group2, SWT.NONE);
								text1anzrestmenge.setText("0");
								text1anzrestmenge.setEditable(false);
								text1anzrestmenge.setBounds(6, 39, 75, 16);
							}
							{
								label3 = new Label(group2, SWT.NONE);
								label3.setText("#strat");
								label3.setBounds(87, 39, 75, 16);
							}
							{
								anzinstore = new Text(group2, SWT.NONE);
								anzinstore.setText("0");
								anzinstore.setBounds(6, 61, 37, 15);
								anzinstore.setEditable(false);
							}
							{
								label5 = new Label(group2, SWT.NONE);
								label5.setText("bestlist storragesize");
								label5.setBounds(43, 61, 105, 19);
							}
						}
						{
							composite3 = new Composite(composite2, SWT.BORDER);
							composite3.setLayout(null);
							composite3.setBounds(12, 272, 238, 65);
							{
								buttonUseAllMetrics = new Button(composite3, SWT.RADIO | SWT.LEFT);
								buttonUseAllMetrics.setText("UseAllMetrics");
								buttonUseAllMetrics.setSelection(true);
								buttonUseAllMetrics.setBounds(5, 5, 91, 16);
							}
							{
								buttonUseOnlySelectedMetrics = new Button(composite3, SWT.RADIO | SWT.LEFT);
								buttonUseOnlySelectedMetrics.setText("UseOnlySelectedMetrics");
								buttonUseOnlySelectedMetrics.setBounds(5, 26, 148, 16);
							}
						}
						{
							label7 = new Label(composite2, SWT.NONE);
							label7.setText("(i)");
							label7.setBounds(614, 72, 60, 30);
							label7.setToolTipText(
									"This is the number of found portfolios. This are all portfolios, the the good and the bad portfolios");
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
							public void widgetSelected(SelectionEvent evt)
							{
								table1WidgetSelected(evt);
							}
						});
					}
				}
				FormData cTabFolder1LData = new FormData();
				cTabFolder1LData.left =  new FormAttachment(0, 1000, 21);
				cTabFolder1LData.top =  new FormAttachment(0, 1000, 22);
				cTabFolder1LData.width = 1202;
				cTabFolder1LData.height = 558;
				cTabFolder1.setLayoutData(cTabFolder1LData);
				cTabFolder1.setSelection(0);
			}
			this.addPaintListener(new PaintListener() {
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
							openFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							openFileMenuItem.setText("Open");
						}
						{
							newFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							newFileMenuItem.setText("Config");
							newFileMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									newFileMenuItemWidgetSelected(evt);
								}
							});
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
			readparam_userinterface();
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void readparam_userinterface()
	{
		String[] ITEMS =
		{ "PearsonsCorrelation", "KendallsCorrelation", "SpearmansCorrelation" };
		
		endtestfitnessfilter_glob.setNettoflag(button1nettoprofit.getSelection());
		endtestfitnessfilter_glob.setNettorobustflag(button1nettorobust.getSelection());
		endtestfitnessfilter_glob.setNettorobuststabilflag(button1nettorobuststabil.getSelection());
		endtestfitnessfilter_glob.setNettostabilflag(button1nettostabilitaet.getSelection());
		endtestfitnessfilter_glob.setNettorobustflag(button1nettorobust.getSelection());
		endtestfitnessfilter_glob.setNettorobuststabilflag(button1nettorobuststabil.getSelection());
		endtestfitnessfilter_glob.setStabilflag(button1stabil.getSelection());
		endtestfitnessfilter_glob.setNettoperstrategyflag(nettoprofitperstrategy.getSelection());
		combo1CorelationType.setItems(ITEMS);
		combo1CorelationType.select(0);
	}
	
	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void main(String[] args)
	{
		// 1) get data from userinterface
		String userdir = System.getProperty("user.dir");
		if (args.length > 0)
			if (args[0] != null)
				userdir = args[0];
		/*
		Tracer.WriteTrace(10, "userdir=<"+userdir+">");
		 for (int i = 0; i < args.length; i++) 
		 {
	            System.out.println("Parameter " + (i + 1) + ": " + args[i]);
	            Tracer.WriteTrace(20, "Aufrufparameter anz<"+args.length+">");
		 }
		 if(args.length>1)
		 {
			 if(args[1].equals("ExportDataForWeka"))
			 Tracer.WriteTrace(10, "param1<"+args[1]+">");
			 System.exit(55);
		 }
		*/
		// 2)gobal config with userdir
		Metrikglobalconf meconf = new Metrikglobalconf(userdir);
		
		// 3) display etc
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		NewSWTApp inst = new NewSWTApp(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		
		DisTool dt = new DisTool(display, shell);
		
		// 4) Metrikglobalconf setzen
		Metrikglobalconf.setShell(shell);
		Metrikglobalconf.setVersion("Version V0.1.9.0");
		Metrikglobalconf.refreshHeader(userdir);
		
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
		
		calone.readMetriktabellen(strategienselector_glob);
		
		// Falls keine Config, dann wird die erzeugt
		if (calone.checkConfigAvailable() == true)
			calone.readConfig();
		else
			calone.genConfig();
		EndtestResult endresult = calone.startCalculation(strategienselector_glob, 0, endtestfitnessfilter_glob);
		// Mbox.Infobox("ready");
	}
	
	private void button1cleanConfigWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1genConfig.widgetSelected, event=" + evt);
		
		String rpath = Metrikglobalconf.getFilterpath();
		
		File fna = new File(rpath + "\\_0_dir");
		if (fna.exists() == false)
		{
			
			FileAccess.checkgenDirectory(rpath + "\\_0_dir");
			FileAccess.checkgenDirectory(rpath + "\\_99_dir");
			FileAccess.checkgenDirectory(rpath + "\\_99_dir\\best100dir");
			FileAccess.checkgenDirectory(rpath + "\\_99_dir\\str__all_sq3_endtestfiles");
			FileAccess.checkgenDirectory(rpath + "\\_99_dir\\str__all_sq4_endtestfiles");
			FileAccess.checkgenDirectory(rpath + "\\_99_dir\\str__selected_sq3_endtestfiles");
			FileAccess.checkgenDirectory(rpath + "\\_99_dir\\str__selected_sq4_endtestfiles");
			return;
		}
		// strategienselector_glob.setMenge(0);
		StrategieMengen strategienselector_glob = new StrategieMengen(Metrikglobalconf.getFilterpath(),
				Metrikglobalconf.getPercent(), Metrikglobalconf.getFixedSeedflag());
		calone.readMetriktabellen(strategienselector_glob);
		calone.cleanConfig();
		
		// die filter anlegen
		CalOpt100Sammler calopt100 = new CalOpt100Sammler();
		calopt100.initReadMetrikTables(strategienselector_glob);
		
		Mbox.Infobox("Filter cleaned");
	}
	
	private void button1calcgraphicWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1calcgraphic.widgetSelected, event=" + evt);
		// TODO add your code for button1calcgraphic.widgetSelected
		calone.readMetriktabellen(strategienselector_glob);
		
		if (calone.checkConfigAvailable() == true)
			calone.readConfig();
		else
		{
			calone.genConfig();
			calone.genStrDirs();
		}
		calone.startCalculation(strategienselector_glob, 1, endtestfitnessfilter_glob);
		calone.showGraphik();
	}
	
	private void button1calcoptimizeWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1calcoptimize.widgetSelected, event=" + evt);
		
		// Hier wrid die optimale Metrikkombination gesucht
		// algotype=1 nutze korrelationswerte
		// altotype=2 nutze nur random
		int algotype = 2;
		
		// Das ist nur eine Datenstruktur die alle strategien beinhaltet. Jede Strategie
		// wird hier in einem pool gepackt, Pool0 bis Pool5
		StrategieMengen strategiemengen_glob = new StrategieMengen(Metrikglobalconf.getFilterpath(),
				Metrikglobalconf.getPercent(), Metrikglobalconf.getFixedSeedflag());
		
		// Erst mal alle Metriktabellen einlesen
		Metrikglobalconf.setStopflag_glob(0);
		readparam_userinterface();
		calopt100.initReadMetrikTables(strategiemengen_glob);
		
		int stepanzahl = SG.get_zahl(text1steps.getText());
		
		// correlation selection
		if (button2usecorrelation.getSelection() == true)
		{
			// use corelation optimaziation, also muss ich erst die Korrelationsmatrik
			// berechnen.
			algotype = 1; // type=1 dann nutze korrelationswerte für die optimierung
			// korreliere und fülle die globale Klasse
			calopt100.correlate(strategiemengen_glob, 0, attributnameendtest.getText(), combo1CorelationType.getText());
		}
		// random optimaziation
		else if (button2userandom.getSelection() == true)
			algotype = 0;
		else if (button1random2.getSelection() == true)
			algotype = 3; // random2 methode
		// use random+ optimaziation=use top 100 list
		else
			algotype = 2;
		
		Best100Portfolios best100portfolios = new Best100Portfolios();
		// hier wird mn mal nach dem besten gesucht
		calopt100.startOptimizing(best100portfolios, strategiemengen_glob, 1, Integer.valueOf(stepanzahl), text1count,
				text1maxprof, text1anzrestmenge, anzinstore, mfilter_glob, algotype, corelsetting_glob,
				endtestfitnessfilter_glob, buttonUseOnlySelectedMetrics.getSelection());
		
		// berechne die besten 100 Strategiekonfigurationen auf unbekannte daten
		
		for (int index = 1; index < 6; index++)
		{
			// set to unknown data.
			strategiemengen_glob.setPool(index);
			calopt100.initReadMetrikTables(strategiemengen_glob);
			calopt100.calcUnknownData(strategiemengen_glob, endtestfitnessfilter_glob, index);
		}
		
		// alle portfolios wo ein test auf unbekannte daten <0 liefert wird gelöscht
		if (Metrikglobalconf.getCollectOnlyRobustStrategies() == 1)
			calopt100.removeBadResults(1);
		else
			calopt100.removeBadResults(0);
		// die n Ergebnisse werden gespeichert in best100 dir gespeichert
		calopt100.storeResults();
		
		// das beste umkopieren
		calopt100.kopiereBestStrfiles();
		if (button2showbestresult.getSelection() == true)
		{
			// das beste Ergebniss anzeigen
			calopt100.showGraphik();
		}
		// hier wird die resulttable gesetzt,
		// die tabelle ist im resulttab zu finden
		calopt100.setTable(table1);
		// Mbox.Infobox("ready");
		
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
		System.out.println("button1scanallmetrics.widgetSelected, event=" + evt);
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
		System.out.println("button2calcCorrelation.widgetSelected, event=" + evt);
		// hier wird die Korrelation für jede einzelne Metrik berechnet und in
		// einer Tabelle dargestellt.
		
		Metrikglobalconf.setStopflag_glob(0);
		
		StrategieMengen strategienselector_glob = new StrategieMengen(Metrikglobalconf.getFilterpath(), 100,
				Metrikglobalconf.getFixedSeedflag());
		
		// Dann korrelieren
		calopt100.correlate(strategienselector_glob, 1, attributnameendtest.getText(), combo1CorelationType.getText());
		// Mbox.Infobox("ready");
	}
	
	private void thisPaintControl(PaintEvent evt)
	{
		System.out.println("this.paintControl, event=" + evt);
		// TODO add your code for this.paintControl
		
		DisTool.UpdateDisplay2();
	}
	
	private void button1setCorrelSettingsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setCorrelSettings.widgetSelected, event=" + evt);
		SetCorrelationConfig.showGUI(corelsetting_glob);
		System.out.println("done");
	}
	
	private void table1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("table1.widgetSelected, event=" + evt);
		// TODO add your code for table1.widgetSelected
	}
	
	private void button1WidgetSelected(SelectionEvent evt)
	{
		//text2=weka attribut exportfile
		//text5=5000 maxn
		String workdir=text2.getText().substring(0, text2.getText().lastIndexOf("\\"));
		
		System.out.println("button1.widgetSelected, event=" + evt);
		Metriktabellen met = new Metriktabellen();
		met.readAllTabellen(strategienselector_glob, Metrikglobalconf.getFilterpath());
		met.exportAllAttributesForWeka(text2.getText(), Integer.valueOf(text5.getText()), button3.getSelection(),workdir);
	}
	
	private void button3WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button3.widgetSelected, event=" + evt);
		// TODO add your code for button3.widgetSelected
	}
	
	private void button2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2.widgetSelected, event=" + evt);
		String dd = "C:\\forex";
		// ask user for destfile
		String wekaexportfile = SWTwindow.FileDialog(getDisplay(), dd);
		if (wekaexportfile == null)
			wekaexportfile = dd;
		
		if(wekaexportfile.endsWith(".csv")==false)
			wekaexportfile=wekaexportfile+".csv";
		
		text2.setText(wekaexportfile);
		wekaLearnDatafile.setText(wekaexportfile);
		Metrikglobalconf.setWekafile_glob(wekaexportfile);
		
		// TODO add your code for button2.widgetSelected
	}
	
	private void button4setLearnerfileWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setLearnerfile.widgetSelected, event=" + evt);
		
		// der button wurde gedrückt, jetzt wird der pfad gewählt.
		String dd = "C:\\forex";
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		wekaLearnDatafile.setText(filenam);
	}
	
	private void button4setNewfileWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setNewfile.widgetSelected, event=" + evt);
		// der set button für das new file was wir predicten wollen wurde gesetzt
		// dies file hat unbekannte daten, hierdrauf wollen wir den learner lassen.
		// der button wurde gedrückt, jetzt wird der pfad gewählt.
		String dd = "C:\\forex";
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		
		if(filenam.endsWith(".model")==false)
			filenam=filenam+".model";
		
		
		newDataFile.setText(filenam);
	}
	
	private void WekaLearnWidgetSelected(SelectionEvent evt)
	{
		System.out.println("WekaLearn.widgetSelected, event=" + evt);
		int anztrees=Integer.valueOf(text1trees.getText());
		try
		{
			WekaResultCollector wres=new WekaResultCollector("c:\\tmp\\wekaresult.txt");
			WekaTools.WekaLearn.loadAndTrainModel(wekaLearnDatafile.getText().toString(),Integer.valueOf(text1crossvalidateanz.getText()),wres,0,anztrees);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void button4WekaPredictWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4WekaPredict.widgetSelected, event=" + evt);
		try
		{
			WekaTools.WekaLearn.classifyNewData(newDataFile.getText(), text1newDatafile2.getText(),Integer.valueOf(text1crossvalidateanz.getText()));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void button4setnewdata2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setnewdata2.widgetSelected, event=" + evt);
		// TODO add your code for button4setnewdata2.widgetSelected
		String dd = "C:\\forex";
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		text1newDatafile2.setText(filenam);
	}
	
	private void button4batchexportlearnWidgetSelected(SelectionEvent evt) {
		System.out.println("button4batchexportlearn.widgetSelected, event="+evt);
		//AutoBatch
		//text5==anz Strategien for export
		int anztrees=Integer.valueOf(text1trees.getText());
		AutoWorkflow aw=new AutoWorkflow(strategienselector_glob,text5.getText());
		//aw.ExportAllAttributesWeka();
		aw.LearnAllWekaExported(Integer.valueOf(text1crossvalidateanz.getText()),anztrees);
		//aw.PredictAllWeka(Integer.valueOf(text1crossvalidateanz.getText()));
		//aw.PredictAllMatrixWeka(Integer.valueOf(text1crossvalidateanz.getText()));
		aw.PredictLastPeriod(Integer.valueOf(text1crossvalidateanz.getText()));
	}

}
