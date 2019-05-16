package swtHilfsfenster;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import StartFrame.StartMonitorWork;
import StartFrame.Tableview;
import data.Ea;
import data.Ealiste;
import data.Profit;

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
public class SwtConfigEa extends org.eclipse.swt.widgets.Composite
{
	private Text eainfotext;
	private Label label4;
	private Text eafavorite;
	private Text eainfotext2;
	private Button ShowBacktest;
	private Button ShowProfit;
	private Button showtradelist;
	private Label EaDescription;
	private Text description;

	private Label label2;
	private Text timeafteroff;
	private Button waittimeafteroff;
	private Button onlyoffflag;
	private Button buttonLateSwitchOn;
	private Label label1;
	private Label label3;

	private Button usedefault;
	private Button gdx;
	private Button line;
	private Button fullautomatic;
	private Button ok;
	private Text switchon;
	private Text switchoff;
	private Label switchonlabel;

	private Text gdxvalue;

	private Group group1;
	private Label EaInfo;
	static private Display display_glob = null;
	static private Profit profitelem_glob = null;
	static private Tableview tableview_glob = null;
	static private Ea ea_glob = null;
	static private StartMonitorWork smw_glob = null;
	static int brokertype_glob=0;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void startmain(Display display, Profit selp, Tableview tv,
			StartMonitorWork smw,int brokertype)
	{
		display_glob = display;
		profitelem_glob = selp;
		tableview_glob = tv;
		smw_glob = smw;
		brokertype_glob=brokertype;
		Ealiste eal = tv.getEaliste();
		ea_glob = eal.getEa(selp.getMagic(), selp.getBroker());
		showGUI();
	}

	/**
	 * Overriding checkSubclass allows this class to extend
	 * org.eclipse.swt.widgets.Composite
	 */
	protected void checkSubclass()
	{
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	private static void showGUI()
	{
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SwtConfigEa inst = new SwtConfigEa(shell, SWT.NULL);
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

	private SwtConfigEa(org.eclipse.swt.widgets.Composite parent, int style)
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
			this.setSize(717, 467);
			{
				label4 = new Label(this, SWT.NONE);
				FormData label4LData = new FormData();
				label4LData.left = new FormAttachment(0, 1000, 527);
				label4LData.top = new FormAttachment(0, 1000, 340);
				label4LData.width = 158;
				label4LData.height = 20;
				label4.setLayoutData(label4LData);
				label4.setText("favorite number (1-10)");
			}
			{
				FormData eafavoriteLData = new FormData();
				eafavoriteLData.left = new FormAttachment(0, 1000, 449);
				eafavoriteLData.top = new FormAttachment(0, 1000, 340);
				eafavoriteLData.width = 64;
				eafavoriteLData.height = 20;
				eafavorite = new Text(this, SWT.NONE);
				eafavorite.setLayoutData(eafavoriteLData);
				eafavorite.setText("5");
			}
			{
				ShowBacktest = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData ShowBacktestLData = new FormData();
				ShowBacktestLData.left = new FormAttachment(0, 1000, 218);
				ShowBacktestLData.top = new FormAttachment(0, 1000, 420);
				ShowBacktestLData.width = 103;
				ShowBacktestLData.height = 30;
				ShowBacktest.setLayoutData(ShowBacktestLData);
				ShowBacktest.setText("ShowBacktest");
				ShowBacktest.setEnabled(false);
			}
			{
				ShowProfit = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData ShowProfitLData = new FormData();
				ShowProfitLData.left = new FormAttachment(0, 1000, 128);
				ShowProfitLData.top = new FormAttachment(0, 1000, 420);
				ShowProfitLData.width = 84;
				ShowProfitLData.height = 30;
				ShowProfit.setLayoutData(ShowProfitLData);
				ShowProfit.setText("ShowProfit");
				ShowProfit.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						ShowProfitWidgetSelected(evt);
					}
				});
			}
			{
				showtradelist = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData showtradelistLData = new FormData();
				showtradelistLData.left = new FormAttachment(0, 1000, 17);
				showtradelistLData.top = new FormAttachment(0, 1000, 420);
				showtradelistLData.width = 105;
				showtradelistLData.height = 30;
				showtradelist.setLayoutData(showtradelistLData);
				showtradelist.setText("ShowTradelist");
				showtradelist.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						showtradelistWidgetSelected(evt);
					}
				});
			}
			{
				EaDescription = new Label(this, SWT.NONE);
				FormData EaDescriptionLData = new FormData();
				EaDescriptionLData.left = new FormAttachment(0, 1000, 449);
				EaDescriptionLData.top = new FormAttachment(0, 1000, 69);
				EaDescriptionLData.width = 92;
				EaDescriptionLData.height = 20;
				EaDescription.setLayoutData(EaDescriptionLData);
				EaDescription.setText("EaDescription");
			}
			{
				FormData text2LData = new FormData();
				text2LData.left = new FormAttachment(0, 1000, 449);
				text2LData.top = new FormAttachment(0, 1000, 95);
				text2LData.width = 221;
				text2LData.height = 233;
				description = new Text(this, SWT.MULTI | SWT.WRAP
						| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				description.setLayoutData(text2LData);
				if (ea_glob.getDescription() != null)
					description.setText(ea_glob.getDescription());
			}

			{
				ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData okLData = new FormData();
				okLData.left = new FormAttachment(0, 1000, 613);
				okLData.top = new FormAttachment(0, 1000, 420);
				okLData.width = 92;
				okLData.height = 29;
				ok.setLayoutData(okLData);
				ok.setText("ok");
				ok.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						okWidgetSelected(evt);
					}
				});
			}
			{
				EaInfo = new Label(this, SWT.NONE);
				FormData EaInfoLData = new FormData();
				EaInfoLData.left = new FormAttachment(0, 1000, 379);
				EaInfoLData.top = new FormAttachment(0, 1000, 12);
				EaInfoLData.width = 58;
				EaInfoLData.height = 20;
				EaInfo.setLayoutData(EaInfoLData);
				EaInfo.setText("EaInfo1");
			}
			{
				FormData eainfotextLData = new FormData();
				eainfotextLData.left = new FormAttachment(0, 1000, 12);
				eainfotextLData.top = new FormAttachment(0, 1000, 12);
				eainfotextLData.width = 347;
				eainfotextLData.height = 20;
				eainfotext = new Text(this, SWT.NONE);
				eainfotext.setLayoutData(eainfotextLData);
				if (ea_glob.getInfo() != null)
					eainfotext.setText(ea_glob.getInfo());
			}
			{
				label3 = new Label(this, SWT.NONE);
				FormData label3LData = new FormData();
				label3LData.left = new FormAttachment(0, 1000, 379);
				label3LData.top = new FormAttachment(0, 1000, 38);
				label3LData.width = 58;
				label3LData.height = 20;
				label3.setLayoutData(label3LData);
				label3.setText("EaInfo2");
			}
			{
				FormData eainfo2LData = new FormData();
				eainfo2LData.left = new FormAttachment(0, 1000, 12);
				eainfo2LData.top = new FormAttachment(0, 1000, 38);
				eainfo2LData.width = 347;
				eainfo2LData.height = 20;
				eainfotext2 = new Text(this, SWT.NONE);
				eainfotext2.setLayoutData(eainfo2LData);
				if (ea_glob.getInfo2() != null)
					eainfotext2.setText(ea_glob.getInfo2());
			}
			{
				group1 = new Group(this, SWT.NONE);
				group1.setLayout(null);
				FormData group1LData = new FormData();
				group1LData.left = new FormAttachment(0, 1000, 12);
				group1LData.top = new FormAttachment(0, 1000, 66);
				group1LData.width = 419;
				group1LData.height = 214;
				group1.setLayoutData(group1LData);
				group1.setText("EaControllLogic");
				{
					gdx = new Button(group1, SWT.RADIO | SWT.LEFT);
					gdx.setText("gdx");
					gdx.setBounds(24, 58, 53, 20);
				}
				{
					gdxvalue = new Text(group1, SWT.NONE);
					gdxvalue.setBounds(83, 59, 37, 20);
					gdxvalue.setText("20");
				}
				{
					line = new Button(group1, SWT.RADIO | SWT.LEFT);
					line.setText("line");
					line.setBounds(23, 85, 54, 25);
				}
				{
					switchon = new Text(group1, SWT.NONE);
					switchon.setText("0");
					switchon.setBounds(83, 85, 37, 19);
				}
				{
					switchonlabel = new Label(group1, SWT.NONE);
					switchonlabel.setText("SwitchOn ");
					switchonlabel.setBounds(126, 85, 79, 19);
				}
				{
					switchoff = new Text(group1, SWT.NONE);
					switchoff.setBounds(223, 85, 49, 19);
					switchoff.setText("0");
				}
				{
					label1 = new Label(group1, SWT.NONE);
					label1.setText("SwitchOff");
					label1.setBounds(278, 85, 90, 19);
				}
				{
					onlyoffflag = new Button(group1, SWT.CHECK | SWT.LEFT);
					onlyoffflag.setText("SwitchOnlyOff");
					onlyoffflag.setBounds(23, 148, 138, 30);
				}
				{
					fullautomatic = new Button(group1, SWT.RADIO | SWT.LEFT);
					fullautomatic.setText("fullautomatic (search best gdx)");
					fullautomatic.setBounds(23, 112, 255, 24);
					fullautomatic.setGrayed(true);
					fullautomatic.setEnabled(false);
				}
				{
					waittimeafteroff = new Button(group1, SWT.CHECK | SWT.LEFT);
					waittimeafteroff.setText("WaitTimeAfterOff");
					waittimeafteroff.setBounds(23, 175, 169, 30);
					waittimeafteroff.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							waittimeafteroffWidgetSelected(evt);
						}
					});
				}
				{
					timeafteroff = new Text(group1, SWT.NONE);
					timeafteroff.setText("720");
					timeafteroff.setBounds(223, 175, 49, 20);
				}
				{
					label2 = new Label(group1, SWT.NONE);
					label2.setText("minutes");
					label2.setBounds(278, 175, 60, 20);
				}
				{
					usedefault = new Button(group1, SWT.RADIO | SWT.LEFT);
					usedefault.setText("default");
					usedefault.setBounds(24, 29, 103, 30);
				}
				{
					buttonLateSwitchOn = new Button(group1, SWT.CHECK | SWT.LEFT);
					buttonLateSwitchOn.setText("LateSwitchOn gdx");
					buttonLateSwitchOn.setBounds(23, 203, 138, 30);
					buttonLateSwitchOn.setToolTipText("This ea-automatic will be switched on if the acumulated winn  crossed below the gdx");
				}
				initbuttons();
			}

			this.layout();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void initbuttons()
	{
		if(brokertype_glob==2)
		{
			group1.setEnabled(false);
			group1.setVisible(false);
		}
		// hier werden daten aus der ea-konfig ausgelesen und in dem Fenster
		// angezeigt
		switch (ea_glob.getTradelogiktype())
		{
		case 0:
			usedefault.setSelection(true);
			break;
		case 1:
			gdx.setSelection(true);
			break;
		case 2:
			line.setSelection(true);
			break;
		case 3:
			fullautomatic.setSelection(true);
			break;
		default:
			Tracer.WriteTrace(10, "internal error 55555444");
			break;
		}
		if (ea_glob.getSwitchOnlyOffFlag() == 1)
			onlyoffflag.setSelection(true);
		else
			onlyoffflag.setSelection(false);

		if (ea_glob.getWaitAfterOffTime() != 0)
		{
			waittimeafteroff.setSelection(true);
			timeafteroff.setText(String.valueOf(ea_glob.getWaitAfterOffTime()));
		} else
			waittimeafteroff.setSelection(false);

		if(ea_glob.getLateswitchonflag()==1)
			buttonLateSwitchOn.setSelection(true);
		else
			buttonLateSwitchOn.setSelection(false);
		
		gdxvalue.setText(String.valueOf(ea_glob.getGdx()));
		switchon.setText(String.valueOf(ea_glob.getSwitchonval()));
		switchoff.setText(String.valueOf(ea_glob.getSwitchoffval()));

		
		
		
	}

	private void okWidgetSelected(SelectionEvent evt)
	{
		System.out.println("ok.widgetSelected, event=" + evt);
		// save all
		if (eainfotext.getText() != null)
			ea_glob.setInfo(eainfotext.getText());
		if (eainfotext2.getText() != null)
			ea_glob.setInfo2(eainfotext2.getText());
		if (description.getText() != null)
			ea_glob.setDescription(description.getText());

		// den Tradetype bestimmen
		if (usedefault.getSelection())
			ea_glob.setTradelogiktype(0);
		else if (gdx.getSelection())
			ea_glob.setTradelogiktype(1);
		else if (line.getSelection())
			ea_glob.setTradelogiktype(2);
		else if (fullautomatic.getSelection())
			ea_glob.setTradelogiktype(3);

		// die anderen werte auslesen
		if (timeafteroff.getText() != null)
		{
			int minuten = SG.get_zahl(timeafteroff.getText());
			ea_glob.setWaitAfterOffTime(minuten);
		}
		if (waittimeafteroff.getSelection() == false)
			ea_glob.setWaitAfterOffTime(0);

		if(buttonLateSwitchOn.getSelection()==true)
			ea_glob.setLateswitchonflag(1);
		else
			ea_glob.setLateswitchonflag(0);
		
			
		
		if (switchon.getText() != null)
		{
			int val = SG.get_zahl(switchon.getText());
			ea_glob.setSwitchonval(val);
		}
		if (onlyoffflag.getSelection() == true)
			ea_glob.setSwitchOnlyOffFlag(1);
		else
			ea_glob.setSwitchOnlyOffFlag(0);

		if (switchoff.getText() != null)
		{
			int val = SG.get_zahl(switchoff.getText());
			ea_glob.setSwitchoffval(val);
		}
		if (gdxvalue.getText() != null)
		{
			int val = SG.get_zahl(gdxvalue.getText());
			ea_glob.setGdx(val);
		}
		

		
	

		tableview_glob.getEaliste().store(0);
	}

	private void showtradelistWidgetSelected(SelectionEvent evt)
	{
		System.out.println("showtradelist.widgetSelected, event=" + evt);
		smw_glob.showtradelist1();
	}

	private void ShowProfitWidgetSelected(SelectionEvent evt)
	{
		System.out.println("ShowProfit.widgetSelected, event=" + evt);
		// TODO add your code for ShowProfit.widgetSelected
		smw_glob.showProfitGraphik();
	}

	

	
	
	private void waittimeafteroffWidgetSelected(SelectionEvent evt) {
		System.out.println("waittimeafteroff.widgetSelected, event="+evt);
		//time after off auf null setzten falls hacken geklickt
		timeafteroff.setText("0");
	}

}
