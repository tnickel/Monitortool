package Main;

import java.io.File;
import java.util.List;
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
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
import Metriklibs.GeneratorStatistik;
import Metriklibs.Metriktabellen;
import Metriklibs.StrategieMengen;
import WekaTools.WekaAttributeReduction;
import WekaTools.WekaResultCollector;
import WekaTools.WekaTabTools;
import calcPack.CalOpt100Sammler;
import calcPack.CalcCorrelationAllWorkflows;
import calcPack.CalculationOneSetting;
import data.Best100Portfolios;
import data.CorelSetting;
import data.EndtestResult;
import data.MFilter;
import data.Metrikglobalconf;
import gui.DisTool;
import gui.Mbox;
import gui.ProfitTableViewer;
import hiflsklasse.CheckboxSelector;
import hiflsklasse.SWTwindow;
import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import hilfsklasse.Tracer;
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
	private Button button4predictallwekamatrix;
	private Button button4predictalllastperiod;
	private Button button4predictallweka;
	private Button button4learnallwekaexported;
	private Button button4exportallattributes;
	private CLabel cLabel1;
	private Text text1trees;
	private Label label13;
	private Text text1crossvalidateanz;
	private Label label12;
	private Button button4batchexportlearn;
	private Button button4setnewdata2;
	private Label label10;
	private Text text1newDatafile2;
	private Button button4setNewfile;
	private Button button4setLearnerfile;
	private Text newDataFile;
	private Button button4WekaPredict;
	private Text wekaLearnDatafile;
	private Button WekaLearn;
	private Label label6;
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
	private CTabItem cTabItem4;
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
	private Button button3useminprofittakenbest;
	private Button button3showpredictvalues;
	private Combo combo1endtestattributename;
	private Text text3reducerInputfile;
	private Button button3reduceAttribs;
	private Text text3filenamereducedattribfile;
	private Combo combo1attribreduction;
	private Label label15;
	private Text text3tableroot;
	private Composite composite4;
	private Button button3setwekaattribfilename;
	private Text text3wekaattribfilename;
	private Label label14;
	private Text text3maxattribs;
	private Label label11;
	private Button button3takenbest;
	private Text text1;
	private Button button3showcounter;
	private Button button3usebadendtest;
	private Button button3useNormalisation;
	private Button button3copyStrategies;
	private Label label8;
	private Button button4DeleteAllEndtestfilesInMetrikanalyser;
	private Text text1minprofit;
	private CLabel cLabel2;
	private Button button4setpreditmodelfile;
	private Text text1predictmodelfile;
	private Button button4setmodelfile;
	private Text text1learnmodelfile;
	private Button button4wekapredict2;
	private Button button4autobatchallworkflowscorrelation;
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
			this.setSize(1375, 623);
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
				label6.setText("");
			}
			
			{
				cTabFolder1 = new CTabFolder(this, SWT.NONE);
				cTabFolder1.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						CTabItem selectedItem = cTabFolder1.getSelection();
						if (selectedItem == cTabItem4)
						{ // Überprüfen, ob "Wekatools" ausgewählt wurde
							text3tableroot.setText(Metrikglobalconf.getFilterpath());
							text3tableroot.redraw(); // Optional, um das Neuzeichnen zu erzwingen
						}
					}
				});
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
							button2calcCorrelation.setBounds(115, 102, 110, 25);
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
							button4batchexportlearn = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button4batchexportlearn.setText("Autobatch ExportAndLearn");
							button4batchexportlearn.setBounds(964, 220, 214, 65);
							button4batchexportlearn.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button4batchexportlearnWidgetSelected(evt);
								}
							});
						}
						{
							text1trees = new Text(composite1, SWT.NONE);
							text1trees.setText("100");
							text1trees.setBounds(988, 55, 43, 21);
						}
						{
							cLabel1 = new CLabel(composite1, SWT.NONE);
							cLabel1.setText("Number of Trees");
							cLabel1.setBounds(1031, 55, 102, 14);
						}
						{
							button4exportallattributes = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button4exportallattributes.setText("ExportDataForWeka");
							button4exportallattributes.setBounds(899, 12, 128, 19);
						}
						{
							button4learnallwekaexported = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button4learnallwekaexported.setText("WekaLearn");
							button4learnallwekaexported.setBounds(899, 54, 83, 19);
						}
						{
							button4predictallweka = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button4predictallweka.setText("Weka Predict");
							button4predictallweka.setBounds(899, 71, 154, 20);
						}
						{
							button4predictalllastperiod = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button4predictalllastperiod.setText("Predict all last Period");
							button4predictalllastperiod.setBounds(898, 105, 133, 26);
							button4predictalllastperiod.setSelection(true);
						}
						{
							button4predictallwekamatrix = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button4predictallwekamatrix.setText("predictallwekamatrix");
							button4predictallwekamatrix.setBounds(899, 90, 172, 16);
						}
						{
							button4autobatchallworkflowscorrelation = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button4autobatchallworkflowscorrelation.setText("Autobatch AllWorkflowsCorrelation");
							button4autobatchallworkflowscorrelation.setBounds(962, 327, 200, 37);
							button4autobatchallworkflowscorrelation.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button4autobatchallworkflowscorrelationWidgetSelected(evt);
								}
							});
						}
						{
							componenteWeka = new Composite(composite1, SWT.BORDER);
							componenteWeka.setLayout(null);
							componenteWeka.setBounds(12, 200, 879, 319);
							{
								button1 = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button1.setText("ExportDataForWeka");
								button1.setBounds(741, 21, 137, 48);
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
								button2.setBounds(703, 20, 25, 21);
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
								text2.setBounds(30, 20, 668, 21);
								text2.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
							}
							{
								text5 = new Text(componenteWeka, SWT.NONE);
								text5.setText("5000");
								text5.setBounds(31, 47, 43, 21);
							}
							{
								label9 = new Label(componenteWeka, SWT.NONE);
								label9.setText("MaxStrategiesInExportfile");
								label9.setBounds(100, 46, 142, 21);
							}
							{
								WekaLearn = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								WekaLearn.setText("WekaLearn");
								WekaLearn.setBounds(739, 124, 140, 55);
								WekaLearn.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										WekaLearnWidgetSelected(evt);
									}
								});
							}
							{
								wekaLearnDatafile = new Text(componenteWeka, SWT.NONE);
								wekaLearnDatafile.setText("Set File/Config TablerootDirectory first");
								wekaLearnDatafile.setBounds(32, 125, 668, 21);
								wekaLearnDatafile.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
							}
							{
								newDataFile = new Text(componenteWeka, SWT.NONE);
								newDataFile.setText("newDataFile");
								newDataFile.setBounds(33, 219, 669, 23);
								newDataFile.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
							}
							{
								button4setLearnerfile = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button4setLearnerfile.setText("Set");
								button4setLearnerfile.setBounds(712, 125, 22, 21);
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
								button4setNewfile.setBounds(712, 216, 21, 21);
								button4setNewfile.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4setNewfileWidgetSelected(evt);
									}
								});
							}
							{
								label12 = new Label(componenteWeka, SWT.NONE);
								label12.setText(
										"Store Attributes: We geneate a Attributefile for Weka, store this under this location");
								label12.setBounds(31, 2, 492, 21);
							}
							{
								text1crossvalidateanz = new Text(componenteWeka, SWT.NONE);
								text1crossvalidateanz.setText("3");
								text1crossvalidateanz.setBounds(34, 271, 28, 23);
							}
							{
								label13 = new Label(componenteWeka, SWT.NONE);
								label13.setText("Number of Instances for CrossValidation");
								label13.setBounds(78, 275, 290, 20);
							}
							{
								button4wekapredict2 = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button4wekapredict2.setText("Weka Predict");
								button4wekapredict2.setBounds(741, 193, 139, 47);
								button4wekapredict2.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4wekapredict2WidgetSelected(evt);
									}
								});
							}
							{
								text1learnmodelfile = new Text(componenteWeka, SWT.NONE);
								text1learnmodelfile.setText("set modelfile");
								text1learnmodelfile.setBounds(34, 152, 667, 21);
								text1learnmodelfile.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
							}
							{
								button4setmodelfile = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button4setmodelfile.setText("Set");
								button4setmodelfile.setBounds(708, 153, 25, 21);
								button4setmodelfile.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4setmodelfileWidgetSelected(evt);
									}
								});
							}
							{
								text1predictmodelfile = new Text(componenteWeka, SWT.NONE);
								text1predictmodelfile.setText("set modelfile");
								text1predictmodelfile.setBounds(32, 192, 669, 21);
								text1predictmodelfile
										.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
							}
							{
								button4setpreditmodelfile = new Button(componenteWeka, SWT.PUSH | SWT.CENTER);
								button4setpreditmodelfile.setText("Set");
								button4setpreditmodelfile.setBounds(713, 192, 21, 19);
								button4setpreditmodelfile.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(SelectionEvent evt)
									{
										button4setpreditmodelfileWidgetSelected(evt);
									}
								});
							}
						}
						{
							cLabel2 = new CLabel(composite1, SWT.NONE);
							cLabel2.setText("Work all WEKA Workflows automaticaly");
							cLabel2.setBounds(947, 493, 243, 34);
						}
						{
							text1minprofit = new Text(composite1, SWT.NONE);
							text1minprofit.setText("0");
							text1minprofit.setBounds(914, 130, 44, 19);
						}
						{
							button4DeleteAllEndtestfilesInMetrikanalyser = new Button(composite1,
									SWT.PUSH | SWT.CENTER);
							button4DeleteAllEndtestfilesInMetrikanalyser.setText("DeleteAllEndtestfilesMetrikanalyser");
							button4DeleteAllEndtestfilesInMetrikanalyser.setBounds(450, 28, 206, 19);
							button4DeleteAllEndtestfilesInMetrikanalyser.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button4DeleteAllEndtestfilesInMetrikanalyserWidgetSelected(evt);
								}
							});
						}
						{
							label8 = new Label(composite1, SWT.NONE);
							label8.setText("Min Profit");
							label8.setBounds(964, 130, 60, 17);
						}
						{
							button3copyStrategies = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button3copyStrategies.setText("CopyStratgegies");
							button3copyStrategies.setBounds(1036, 103, 140, 30);
							button3copyStrategies.setSelection(true);
						}
						{
							button3useNormalisation = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button3useNormalisation.setText("Use Normalisation");
							button3useNormalisation.setBounds(969, 430, 207, 30);
							button3useNormalisation.setSelection(true);
						}
						{
							button3usebadendtest = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button3usebadendtest.setText("UseBadEndtest");
							button3usebadendtest.setBounds(1038, 7, 102, 30);
						}
						{
							button3showcounter = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button3showcounter.setText("Show Good/Bad-Counter");
							button3showcounter.setBounds(962, 376, 200, 30);
							button3showcounter.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button3showcounterWidgetSelected(evt);
								}
							});
						}
						{
							text1 = new Text(composite1, SWT.NONE);
							text1.setText("10");
							text1.setBounds(1012, 159, 41, 20);
						}
						{
							button3takenbest = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button3takenbest.setText("Take N Best");
							button3takenbest.setBounds(915, 153, 85, 30);
						}
						{
							label11 = new Label(composite1, SWT.NONE);
							label11.setText("Take the N best Strategies with min Profit");
							label11.setBounds(1070, 159, 256, 17);
						}
						{
							text3maxattribs = new Text(composite1, SWT.NONE);
							text3maxattribs.setText("3000");
							text3maxattribs.setBounds(1146, 12, 44, 18);
						}
						{
							label14 = new Label(composite1, SWT.NONE);
							label14.setText("Max Strategies");
							label14.setBounds(1196, 12, 92, 17);
						}
						{
							text3wekaattribfilename = new Text(composite1, SWT.NONE);
							text3wekaattribfilename.setText("exported_for_weka.csv");
							text3wekaattribfilename.setBounds(899, 36, 291, 17);
						}
						{
							button3setwekaattribfilename = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button3setwekaattribfilename.setText("SetWekaAttribFilename");
							button3setwekaattribfilename.setBounds(1196, 36, 130, 17);
						}
						{
							combo1endtestattributename = new Combo(composite1, SWT.NONE);
							combo1endtestattributename.setText("Set Endtestattributename");
							combo1endtestattributename.setBounds(679, 12, 208, 20);
							combo1endtestattributename.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									combo1endtestattributenameWidgetSelected(evt);
								}
								
								public void widgetDefaultSelected(SelectionEvent evt)
								{
									System.out
											.println("combo1endtestattributename.widgetDefaultSelected, event=" + evt);
									// TODO add your code for combo1endtestattributename.widgetDefaultSelected
								}
							});
						}
						{
							button3showpredictvalues = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button3showpredictvalues.setText("Show Predictvalues");
							button3showpredictvalues.setBounds(1194, 115, 132, 18);
							button3showpredictvalues.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									button3showpredictvaluesWidgetSelected(evt);
								}
							});
						}
						{
							button3useminprofittakenbest = new Button(composite1, SWT.CHECK | SWT.LEFT);
							button3useminprofittakenbest.setText("Use Minprofit + Take N Best");
							button3useminprofittakenbest.setBounds(915, 182, 187, 30);
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
								userandomplusopti.setEnabled(false);
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
				{
					cTabItem4 = new CTabItem(cTabFolder1, SWT.NONE);
					cTabItem4.setText("Wekatools");
					{
						composite4 = new Composite(cTabFolder1, SWT.NONE);
						composite4.setLayout(null);
						cTabItem4.setControl(composite4);
						{
							text3tableroot = new Text(composite4, SWT.BORDER);
							text3tableroot.setText(Metrikglobalconf.getFilterpath());
							text3tableroot.setBounds(12, 34, 791, 18);
							text3tableroot.setEnabled(false);
							text3tableroot.redraw();
						}
						{
							label15 = new Label(composite4, SWT.NONE);
							label15.setText("Tableroot");
							label15.setBounds(12, 17, 60, 17);
						}
						{
							combo1attribreduction = new Combo(composite4, SWT.NONE);
							combo1attribreduction.setText("Attribute Reduction");
							combo1attribreduction.setBounds(12, 71, 210, 20);
						}
						{
							text3filenamereducedattribfile = new Text(composite4, SWT.NONE);
							text3filenamereducedattribfile.setText("exported_for_weka_red.csv");
							text3filenamereducedattribfile.setBounds(12, 135, 210, 19);
						}
						{
							button3reduceAttribs = new Button(composite4, SWT.PUSH | SWT.CENTER);
							button3reduceAttribs.setText("Reduce Attribs in all workflows");
							button3reduceAttribs.setBounds(241, 129, 257, 30);
							button3reduceAttribs.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt)
								{
									button3reduceAttribsWidgetSelected(evt);
								}
							});
						}
						{
							text3reducerInputfile = new Text(composite4, SWT.NONE);
							text3reducerInputfile.setText("exported_for_weka.csv");
							text3reducerInputfile.setBounds(12, 109, 210, 20);
						}
					}
					{
						
					}
				}
				FormData cTabFolder1LData = new FormData();
				cTabFolder1LData.left = new FormAttachment(0, 1000, 21);
				cTabFolder1LData.top = new FormAttachment(0, 1000, 22);
				cTabFolder1LData.width = 1338;
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
			initGui();
			readparam_userinterface();
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void initGui()
	{
		combo1attribreduction.add("cfssubseteval_bestfirst");
		combo1attribreduction.add("cfssubseteval_greedystepwise");
		combo1attribreduction.select(1);
		
		combo1endtestattributename.add("Net Profit (OOS)");
		combo1endtestattributename.add("Fitness (OOS)");
		
		String endtestattrib = Metrikglobalconf.getEndtestattributname();
		if ((endtestattrib != null) && (endtestattrib.equals("Net Profit (OOS)")))
			combo1endtestattributename.select(0);
		else if ((endtestattrib != null) && (endtestattrib.equals("Fitness (OOS)")))
			combo1endtestattributename.select(1);
		else
		{
			Metrikglobalconf.setEndtestattributname("Net Profit (OOS");
			combo1endtestattributename.select(0);
			Tracer.WriteTrace(20, "Unknown endtestattribute<" + endtestattrib + "> I set default");
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
			
		Tracer.SetTraceLevel(60);
		Tracer.WriteTrace(20, "userdir=<" + userdir + ">");
		
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
		Metrikglobalconf.setVersion("Version V0.1.15.1");
		Metrikglobalconf.refreshHeader(userdir);
		
		System.out.println("akueller pfad=" + userdir);
		
		// Hinzufügen eines ShellListeners, um das Schließen zu überwachen
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e)
			{
				// Hier kannst du anpassen, was beim Schließen passiert
				System.out.println("Shell is closing");
				// Standardmäßig macht dies nichts weiter, außer dass es das Schließen erlaubt.
				// Wenn du das Schließen verhindern möchtest, setze e.doit = false;
				// e.doit = false;
			}
		});
		
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
		refreshValues();
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
		if (Metrikglobalconf.isValidSubRootpath() == false)
		{
			Tracer.WriteTrace(10, "E: Wrong Tablerootpath<" + Metrikglobalconf.getFilterpath()
					+ "> please set correct Tablerootpath");
			return;
		}
		StrategieMengen strategienselector_glob = new StrategieMengen(Metrikglobalconf.getFilterpath(), 100,
				Metrikglobalconf.getFixedSeedflag());
		
		calopt100.initReadMetrikTables(strategienselector_glob);
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
		// text2=weka attribut exportfile
		// text5=5000 maxn
		String workdir = text2.getText().substring(0, text2.getText().lastIndexOf("\\"));
		
		System.out.println("button1.widgetSelected, event=" + evt);
		Metriktabellen met = new Metriktabellen();
		met.readAllTabellen(strategienselector_glob, Metrikglobalconf.getFilterpath());
		met.exportAllAttributesForWeka(text2.getText(), Integer.valueOf(text5.getText()), workdir, false,
				Metrikglobalconf.getEndtestattributname());
	}
	
	private void button2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button2.widgetSelected, event=" + evt);
		
		String dd = Metrikglobalconf.getWekalearndatapath();
		// ask user for destfile
		String wekaexportfile = SWTwindow.FileDialog(getDisplay(), dd);
		if (wekaexportfile == null)
			wekaexportfile = dd;
		
		if (wekaexportfile.endsWith(".csv") == false)
			Tracer.WriteTrace(10, "E:file should end with .csv");
		
		text2.setText(wekaexportfile);
		wekaLearnDatafile.setText(wekaexportfile);
		Metrikglobalconf.setWekafile_glob(wekaexportfile);
		
		// TODO add your code for button2.widgetSelected
	}
	
	private void button4setLearnerfileWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setLearnerfile.widgetSelected, event=" + evt);
		
		// der button wurde gedrückt, jetzt wird der pfad gewählt.
		String dd = Metrikglobalconf.getWekalearnmodelpath();
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		if (filenam.endsWith(".model") == false)
			Tracer.WriteTrace(10, "E: filenam should end with .model");
		
		wekaLearnDatafile.setText(filenam);
	}
	
	private void button4setNewfileWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setNewfile.widgetSelected, event=" + evt);
		// der set button für das new file was wir predicten wollen wurde gesetzt
		// dies file hat unbekannte daten, hierdrauf wollen wir den learner lassen.
		// der button wurde gedrückt, jetzt wird der pfad gewählt.
		String dd = Metrikglobalconf.getWekaclassimodelpath();
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		
		if (filenam.endsWith(".csv") == false)
			Tracer.WriteTrace(10, "E: filename should end with .csv");
		
		newDataFile.setText(filenam);
	}
	
	private void WekaLearnWidgetSelected(SelectionEvent evt)
	{
		System.out.println("WekaLearn.widgetSelected, event=" + evt);
		int anztrees = Integer.valueOf(text1trees.getText());
		String csvdatafile = wekaLearnDatafile.getText().toString();
		String learnerfile = text1learnmodelfile.getText().toString();
		
		try
		{
			WekaResultCollector wres = new WekaResultCollector("c:\\tmp\\wekaresult.txt");
			WekaTools.WekaLearn.loadAndTrainModel(csvdatafile, learnerfile,
					Integer.valueOf(text1crossvalidateanz.getText()), wres, 0, anztrees,
					button3useNormalisation.getSelection());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void button4WekaPredictWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4WekaPredict.widgetSelected, event=" + evt);
		
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
	
	private void button4batchexportlearnWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4batchexportlearn.widgetSelected, event=" + evt);
		// AutoBatch
		String globpath = Metrikglobalconf.getFilterpath();
		String last10Chars = globpath.substring(Math.max(0, globpath.length() - 10));
		
		if (last10Chars.contains("0000") || last10Chars.contains("_--0"))
			Tracer.WriteTrace(10, "E: please set Tableroot-Directory in File/Config set TableRoot-Directory");
		// text5==anz Strategien for export
		int anztrees = Integer.valueOf(text1trees.getText());
		AutoWorkflow aw = new AutoWorkflow(strategienselector_glob, text5.getText());
		if (button4exportallattributes.getSelection())
			aw.ExportAllAttributesWeka(button3usebadendtest.getSelection(), Integer.valueOf(text3maxattribs.getText()),
					Metrikglobalconf.getEndtestattributname());
		
		if (button4learnallwekaexported.getSelection())
			aw.LearnAllWekaExported(Integer.valueOf(text1crossvalidateanz.getText()), anztrees,
					button3useNormalisation.getSelection(), text3wekaattribfilename.getText());
		
		if (button4predictallweka.getSelection())
			aw.PredictAllWeka(Integer.valueOf(text1crossvalidateanz.getText()), button3useNormalisation.getSelection(),
					text3wekaattribfilename.getText());
		
		if (button4predictallwekamatrix.getSelection())
			aw.PredictAllMatrixWeka(Integer.valueOf(text1crossvalidateanz.getText()),
					button3useNormalisation.getSelection(), text3wekaattribfilename.getText());
		
		if (button4predictalllastperiod.getSelection())
			// aw.PredictLastPeriod(Integer.valueOf(text1crossvalidateanz.getText()));
			aw.PredictLastPeriodCopyStrategies(Integer.valueOf(text1crossvalidateanz.getText()),
					Double.valueOf(text1minprofit.getText()), button3copyStrategies.getSelection(),
					button3useNormalisation.getSelection(), button3takenbest.getSelection(),
					Integer.valueOf(text1.getText()), text3wekaattribfilename.getText(),button3useminprofittakenbest.getSelection());
		
	}
	
	private void button4autobatchallworkflowscorrelationWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4autobatchallworkflowscorrelation.widgetSelected, event=" + evt);
		CalcCorrelationAllWorkflows cal = new CalcCorrelationAllWorkflows();
		cal.CalcCorrelationAllWorkflows(attributnameendtest.getText(), combo1CorelationType.getText(), true);
		
	}
	
	private void button4wekapredict2WidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4wekapredict2.widgetSelected, event=" + evt);
		try
		{
			String wekamodelfile = text1predictmodelfile.getText();
			String path = text1predictmodelfile.getText();
			String workdir = path.substring(0, path.lastIndexOf("\\"));
			String wekacsvdatafile = newDataFile.getText();
			WekaTools.WekaLearn.classifyNewData(wekamodelfile, wekacsvdatafile,
					Integer.valueOf(text1crossvalidateanz.getText()), button3useNormalisation.getSelection(), workdir);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void button4setmodelfileWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setmodelfile.widgetSelected, event=" + evt);
		// set modelfile for learner
		
		String dd = Metrikglobalconf.getWekalearndatapath();
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		if (filenam.endsWith(".model") == false)
			Tracer.WriteTrace(10, "E:filename should end with .model");
		
		text1learnmodelfile.setText(filenam);
	}
	
	private void button4setpreditmodelfileWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4setpreditmodelfile.widgetSelected, event=" + evt);
		// set modelfile for predicter
		String dd = Metrikglobalconf.getWekaclassidatapath();
		// ask user for destfile
		String filenam = SWTwindow.FileDialog(getDisplay(), dd);
		if (filenam == null)
			filenam = dd;
		if (filenam.endsWith(".model") == false)
			Tracer.WriteTrace(10, "E:Filename should end with .model");
		
		text1predictmodelfile.setText(filenam);
	}
	
	public void refreshValues()
	{
		text2.setText(Metrikglobalconf.getWekabuildattributespath());
		text1learnmodelfile.setText(Metrikglobalconf.getWekalearnmodelpath());
		wekaLearnDatafile.setText(Metrikglobalconf.getWekalearndatapath());
		text1predictmodelfile.setText(Metrikglobalconf.getWekaclassimodelpath());
		newDataFile.setText(Metrikglobalconf.getWekaclassidatapath());
	}
	
	private void button4DeleteAllEndtestfilesInMetrikanalyserWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button4DeleteAllEndtestfilesInMetrikanalyser.widgetSelected, event=" + evt);
		// Delete all Endtestfiles in Metrikanalyser
		AutoWorkflow.DeleteAllEndtestfiles();
	}
	
	private void button3showcounterWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button3showcounter.widgetSelected, event=" + evt);
		// show godd and badcounter
		GeneratorStatistik gstat = new GeneratorStatistik();
		gstat.collectGoodBad(Metrikglobalconf.getFilterpath(), Metrikglobalconf.getFilterpath());
		gstat.showGoodBadcounter();
	}
	
	private void button3reduceAttribsWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button3reduceAttribs.widgetSelected, event=" + evt);
		WekaAttributeReduction reducer = new WekaAttributeReduction();
		
		String methode = combo1attribreduction.getText();
		
		WekaTabTools wt = new WekaTabTools();
		wt.attributeReduction(text3reducerInputfile.getText(), text3filenamereducedattribfile.getText(), methode);
		
	}
	
	private void combo1endtestattributenameWidgetSelected(SelectionEvent evt)
	{
		System.out.println("combo1endtestattributename.widgetSelected, event=" + evt);
		String selection = combo1endtestattributename.getText();
		Metrikglobalconf.setEndtestattributname(selection);
		Metrikglobalconf.save();
	}
	
	private void button3showpredictvaluesWidgetSelected(SelectionEvent evt) {
		System.out.println("button3showpredictvalues.widgetSelected, event="+evt);
		String fnam=Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictPastPeriodProfit.txt";
		ProfitTableViewer pt=new ProfitTableViewer();
		pt.createProfitTable(getShell().getDisplay(), fnam);
	}

}
