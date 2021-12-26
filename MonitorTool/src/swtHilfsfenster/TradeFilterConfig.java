package swtHilfsfenster;

import gui.Mbox;
import hiflsklasse.SG;
import mtools.DisTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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

import data.Lic;
import filter.Tradefilter;

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
public class TradeFilterConfig extends org.eclipse.swt.widgets.Composite {
	private Group group1;
	private Button okbutton;

	private Button minproftsel_button;
	private Button button1minprofitfaktor;
	private Text text1maxdrawdown;
	private Text text1minProfitfaktor;
	private Button button1maxdrawdown;
	private Text text1lastdays;
	private Text text1pz1;
	private Button button1pz1;
	private Button button1showConRealaccount;
	private Button button1eaTradedLastDays;
	private Label label8;

	private Label label7;
	private Button mintradesel_button;
	private Button commentsel_button;
	private Button onlyautomatic_button;;
	private Text minprofit10;
	private Text text1anzmintradesall;
	private Text commenttext;
	private Text anzmintrades;

	private Label label4;
	private Label label3;
	private Label label6;
	private Label label5;
	private Text minprofitall;
	private Text minprofit30;

	private Text info2text;
	private Text info1text;
	private Button info2button;
	private Button info1button;

	static private Tradefilter tf_glob=null;
	static private int exitflag=0;
	

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	/*public static void main(String[] args) {
		showGUI( tradefilter);
	}*/
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	static public  void showGUI(Tradefilter tf) {
		Display display = Display.getDefault();
		tf_glob=tf;
		exitflag=0;
		Shell shell = new Shell(display);
		TradeFilterConfig inst = new TradeFilterConfig(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while ((!shell.isDisposed())&& (exitflag == 0) )
		{
			if (!display.readAndDispatch())
				
				display.sleep();
		}

		shell.dispose();
	
	
	}

	public TradeFilterConfig(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(656, 553);
			{
				group1 = new Group(this, SWT.NONE);
				group1.setLayout(null);
				FormData group1LData = new FormData();
				group1LData.left =  new FormAttachment(0, 1000, 12);
				group1LData.top =  new FormAttachment(0, 1000, 12);
				group1LData.width = 618;
				group1LData.height = 455;
				group1.setLayoutData(group1LData);
				group1.setText("Tradefilter");
				{
					mintradesel_button = new Button(group1, SWT.CHECK | SWT.LEFT);
					mintradesel_button.setText("MinTrades");
					mintradesel_button.setBounds(8, 51, 127, 30);
					mintradesel_button.setSelection(tf_glob.isMintradesselection());
				}
				{
					anzmintrades = new Text(group1, SWT.NONE);
					anzmintrades.setBounds(170, 54, 33, 19);

				if(tf_glob.getMintrades()!=0)
					anzmintrades.setText(String.valueOf(tf_glob.getMintrades()));
				
				anzmintrades.addTraverseListener(new TraverseListener() 
				{
					public void keyTraversed(TraverseEvent evt) {
						anzmintradesKeyTraversed(evt);
					}
				});
				}
				{
					text1anzmintradesall = new Text(group1, SWT.NONE);
					text1anzmintradesall.setBounds(338, 53, 39, 22);
					if(tf_glob.getMintradesAll()!=0)
						text1anzmintradesall.setText(String.valueOf(tf_glob.getMintradesAll()));
					
					text1anzmintradesall.addTraverseListener(new TraverseListener() 
					{
						public void keyTraversed(TraverseEvent evt) 
						{
							anzmintradesAllKeyTraversed(evt);
					    }
					});
					
				
				}
				
				{
					label3 = new Label(group1, SWT.NONE);
					label3.setText("#");
					label3.setBounds(209, 54, 18, 20);
				}
				{
					commentsel_button = new Button(group1, SWT.CHECK | SWT.LEFT);
					commentsel_button.setText("Comment");
					commentsel_button.setBounds(8, 81, 142, 30);
					
					commentsel_button.setSelection(tf_glob.isCommentselection());
				}
				{
					commenttext = new Text(group1, SWT.NONE);
					commenttext.setBounds(170, 88, 332, 23);
					commenttext.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							commenttextKeyTraversed(evt);
						}
				
					
					});

					
					
					if(tf_glob.getComment()!=null)
						commenttext.setText(tf_glob.getComment());
				}
				{
					label4 = new Label(group1, SWT.NONE);
					label4.setText("in 30 days");
					label4.setBounds(227, 53, 81, 23);
				}
				{
					minprofit10 = new Text(group1, SWT.NONE);
					minprofit10.setBounds(170, 119, 53, 25);
					minprofit10.setText("0");
					minprofit10.setText(String.valueOf(tf_glob.getMinprofit10()));
					minprofit10.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							minprofit10KeyTraversed(evt);
						}
					});
				}
				{
					minprofit30 = new Text(group1, SWT.NONE);
					minprofit30.setBounds(263, 119, 50, 25);
					minprofit30.setText("0");
					minprofit30.setText(String.valueOf(tf_glob.getMinprofit30()));
					minprofit30.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							minprofit30KeyTraversed(evt);
						}
					});
				}
				{
					minprofitall = new Text(group1, SWT.NONE);
					minprofitall.setText("0");
					minprofitall.setText(String.valueOf(tf_glob.getMinprofitall()));
					minprofitall.setBounds(358, 119, 51, 25);
					minprofit10.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							minprofitallKeyTraversed(evt);
						}
					});
				}
				{
					minproftsel_button = new Button(group1, SWT.CHECK | SWT.LEFT);
					minproftsel_button.setText("Min Profit");
					minproftsel_button.setBounds(8, 119, 142, 25);
					minproftsel_button.setSelection(tf_glob.isMinprofitselection());
					
					minproftsel_button.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							minprofWidgetSelected(evt);
						}
					});
				}
				{
					info1button = new Button(group1, SWT.CHECK | SWT.LEFT);
					info1button.setText("Info1");
					info1button.setSelection(tf_glob.isInfo1selection());
					info1button.setBounds(8, 150, 96, 30);
				}
				{
					info2button = new Button(group1, SWT.CHECK | SWT.LEFT);
					info2button.setText("Info2");
					info2button.setSelection(tf_glob.isInfo2selection());
					info2button.setBounds(8, 186, 83, 30);
				}
				{
					info1text = new Text(group1, SWT.NONE);
					info1text.setBounds(170, 156, 268, 24);
					if(tf_glob.getInfo1()!=null)
						info1text.setText(tf_glob.getInfo1());
					info1text.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							info1textKeyTraversed(evt);
						}
					});
				}
				{
					info2text = new Text(group1, SWT.NONE);
					info2text.setBounds(170, 192, 268, 24);
					if(tf_glob.getInfo2()!=null)
						info2text.setText(tf_glob.getInfo2());
					info2text.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							info2textKeyTraversed(evt);
						}
					});
				}
				{
					onlyautomatic_button = new Button(group1, SWT.CHECK | SWT.LEFT);
					onlyautomatic_button.setText("Only Autoamtic EAs");
					onlyautomatic_button.setBounds(8, 216, 162, 26);
					onlyautomatic_button.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							onlyautomatic_buttonWidgetSelected(evt);
						}
					});
					onlyautomatic_button.setSelection(tf_glob.isAutomaticselection());
				
				}
				
				{
					label5 = new Label(group1, SWT.NONE);
					label5.setText("10");
					label5.setBounds(229, 119, 23, 30);
				}
				{
					label6 = new Label(group1, SWT.NONE);
					label6.setText("30");
					label6.setBounds(319, 119, 27, 25);
				}
				{
					label7 = new Label(group1, SWT.NONE);
					label7.setText("all");
					label7.setBounds(415, 119, 27, 30);
				}
				
				{
					label8 = new Label(group1, SWT.NONE);
					label8.setText("# mintrades all");
					label8.setBounds(392, 53, 110, 30);
				}
				{
					button1eaTradedLastDays = new Button(group1, SWT.CHECK | SWT.LEFT);
					button1eaTradedLastDays.setText("Only EAs traded last days ");
					button1eaTradedLastDays.setBounds(8, 248, 235, 30);
					button1eaTradedLastDays.setSelection(tf_glob.isLasttradedayselection());
					button1eaTradedLastDays.setToolTipText("Show only ea´s who traded in the last 3 days");
				}
				{
					text1lastdays = new Text(group1, SWT.NONE);
					text1lastdays.setText(String.valueOf(tf_glob.getLasttradesdays()));
					text1lastdays.setBounds(249, 254, 37, 24);
					text1lastdays.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							text1lastdaysKeyTraversed(evt);
						}
					});
				}
				{
					button1minprofitfaktor = new Button(group1, SWT.CHECK | SWT.LEFT);
					button1minprofitfaktor.setText("Min Profitfaktor");
					button1minprofitfaktor.setSelection(tf_glob.isProfitfaktorselection());
					button1minprofitfaktor.setBounds(8, 284, 162, 30);
				}
				{
					button1maxdrawdown = new Button(group1, SWT.CHECK | SWT.LEFT);
					button1maxdrawdown.setText("MaxDrawdown");
					button1maxdrawdown.setSelection(tf_glob.isDrawdownselection());
					button1maxdrawdown.setBounds(8, 320, 162, 30);
				}
				{
					text1minProfitfaktor = new Text(group1, SWT.NONE);
					
					text1minProfitfaktor.setText(String.valueOf(tf_glob.getProfitfaktor()));
					text1minProfitfaktor.setBounds(249, 290, 37, 24);
					text1minProfitfaktor.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							text1minProfitfaktorKeyTraversed(evt);
						}
					});
				}
				{
					text1maxdrawdown = new Text(group1, SWT.NONE);
					text1maxdrawdown.setText(String.valueOf(tf_glob.getDrawdown()));
					text1maxdrawdown.setBounds(249, 326, 37, 24);
					text1maxdrawdown.addTraverseListener(new TraverseListener() {
						public void keyTraversed(TraverseEvent evt) {
							text1maxdrawdownKeyTraversed(evt);
						}
					});
				}
				{
					button1showConRealaccount = new Button(group1, SWT.CHECK | SWT.LEFT);
					button1showConRealaccount.setText("show only EAs connected to realaccount");
					button1showConRealaccount.setBounds(8, 356, 430, 30);
					button1showConRealaccount.setSelection(tf_glob.isOnlyRealaccountConnected());
				}
				{
					button1pz1 = new Button(group1, SWT.CHECK | SWT.LEFT);
					button1pz1.setText("pz1");
					button1pz1.setSize(60, 30);
					button1pz1.setBounds(8, 384, 60, 30);
					button1pz1.setSelection(tf_glob.isPz1selection());
				}
				{
					text1pz1 = new Text(group1, SWT.NONE);
					text1pz1.setText(String.valueOf(tf_glob.getPz1()));
					text1pz1.setBounds(249, 383, 37, 25);
				}
			}
			{
				okbutton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData okbuttonLData = new FormData();
				okbuttonLData.left =  new FormAttachment(0, 1000, 563);
				okbuttonLData.top =  new FormAttachment(0, 1000, 511);
				okbuttonLData.width = 73;
				okbuttonLData.height = 30;
				okbutton.setLayoutData(okbuttonLData);
				okbutton.setText("ok");
				okbutton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						okbuttonWidgetSelected(evt);
					}
				});
			}
			init();
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void init()
	{
		if(5==5)
			if(Lic.getlic() <5)
			{
				onlyautomatic_button.setVisible(false);
			}
	}
	private void okbuttonWidgetSelected(SelectionEvent evt) {
		System.out.println("okbutton.widgetSelected, event="+evt);
		//der OK-Button wurde gedrückt
		tf_glob.setTradefiltermainselection(true);
		tf_glob.setCommentselection(commentsel_button.getSelection());
		tf_glob.setComment(commenttext.getText());
		tf_glob.setMintradesselection(mintradesel_button.getSelection());
		tf_glob.setMintrades(SG.get_zahl(anzmintrades.getText()));
		tf_glob.setMintradesAll(SG.get_zahl(text1anzmintradesall.getText()));
		tf_glob.setMinprofitselection(minproftsel_button.getSelection());
		tf_glob.setMinprofit10(SG.get_zahl(minprofit10.getText()));
		tf_glob.setMinprofit30(SG.get_zahl(minprofit30.getText()));
		tf_glob.setMinprofitall(SG.get_zahl(minprofitall.getText()));
		tf_glob.setInfo1selection(info1button.getSelection());
		tf_glob.setInfo1(info1text.getText());
		tf_glob.setInfo2selection(info2button.getSelection());
		tf_glob.setInfo2(info2text.getText());
		tf_glob.setAutomaticselection(onlyautomatic_button.getSelection());
		tf_glob.setProfitfaktorselection(button1minprofitfaktor.getSelection());
		tf_glob.setLasttradedayselection(button1eaTradedLastDays.getSelection());
		tf_glob.setDrawdownselection(button1maxdrawdown.getSelection());
		tf_glob.setProfitfaktor(Float.valueOf(text1minProfitfaktor.getText()));
		tf_glob.setOnlyRealaccountConnected(button1showConRealaccount.getSelection());
		tf_glob.setPz1selection(button1pz1.getSelection());
		tf_glob.setPz1(Float.valueOf(text1pz1.getText()));
	
	
		//überprüfung drawdown 1-100%
		float drd=Float.valueOf(text1maxdrawdown.getText());
		
		tf_glob.setDrawdown((float)((float)drd));

		tf_glob.setLasttradedayselection(button1eaTradedLastDays.getSelection());
		String lastdaystext=text1lastdays.getText();
		tf_glob.setLasttradesdays(SG.get_zahl(lastdaystext));
		//tf_glob.store();
		exitflag=1;
	}
	

	
	private void commenttextKeyTraversed(TraverseEvent evt) {
		System.out.println("commenttext.keyTraversed, event="+evt);
		//Ein Comment wird eingegeben
		commentsel_button.setSelection(true);
		tf_glob.setCommentselection(true);
		DisTool.UpdateDisplay();
	}
	
	private void anzmintradesKeyTraversed(TraverseEvent evt) {
		System.out.println("anzmintrades.keyTraversed, event="+evt);
		//Eine Mintradeanzahl wird eingegeben
				mintradesel_button.setSelection(true);
				
				DisTool.UpdateDisplay();
	}
	
	private void anzmintradesAllKeyTraversed(TraverseEvent evt) {
		System.out.println("anzmintrades.keyTraversed, event="+evt);
		//Eine Mintradeanzahl wird eingegeben
				mintradesel_button.setSelection(true);
				
				DisTool.UpdateDisplay();
	}
	
	private void minprofWidgetSelected(SelectionEvent evt) {
		System.out.println("minprofit.widgetSelected, event="+evt);
		//TODO add your code for minprofit.widgetSelected
		//der hacken wurde gemacht
		tf_glob.setMinprofitselection(true);
		DisTool.UpdateDisplay();
	}
	
	private void minprofit10KeyTraversed(TraverseEvent evt) {
		System.out.println("minprofit.keyTraversed, event="+evt);
		//TODO add your code for minprofit.keyTraversed
		//hier wurde ne Zahl eingegeben
		minproftsel_button.setSelection(true);
		
		//zahl übernehmen
		int gewinn=SG.get_zahl(minprofit10.getText());
		tf_glob.setMinprofit10(gewinn);
	}
	
	private void minprofit30KeyTraversed(TraverseEvent evt) {
		System.out.println("minprofit.keyTraversed, event="+evt);
		//TODO add your code for minprofit.keyTraversed
		//hier wurde ne Zahl eingegeben
		minproftsel_button.setSelection(true);
		
		//zahl übernehmen
		int gewinn=SG.get_zahl(minprofit30.getText());
		tf_glob.setMinprofit30(gewinn);
	}
	private void minprofitallKeyTraversed(TraverseEvent evt) {
		System.out.println("minprofit.keyTraversed, event="+evt);
		//TODO add your code for minprofit.keyTraversed
		//hier wurde ne Zahl eingegeben
		minproftsel_button.setSelection(true);
		
		//zahl übernehmen
		int gewinn=SG.get_zahl(minprofitall.getText());
		tf_glob.setMinprofitall(gewinn);
	}
	private void info1textKeyTraversed(TraverseEvent evt) {
		System.out.println("info1text.keyTraversed, event="+evt);
		//TODO add your code for info1text.keyTraversed
		info1button.setSelection(true);
		tf_glob.setInfo1(info1text.getText());
		
	}
	
	private void info2textKeyTraversed(TraverseEvent evt) {
		System.out.println("info2text.keyTraversed, event="+evt);
		//TODO add your code for info2text.keyTraversed
		info2button.setSelection(true);
		tf_glob.setInfo1(info2text.getText());
	}
	
	private void onlyautomatic_buttonWidgetSelected(SelectionEvent evt) {
		System.out.println("onlyautomatic_button.widgetSelected, event="+evt);
		//TODO add your code for onlyautomatic_button.widgetSelected
		DisTool.UpdateDisplay();
	}
	
	private void text1lastdaysKeyTraversed(TraverseEvent evt) {
		System.out.println("text1lastdays.keyTraversed, event="+evt);
		//TODO add your code for text1lastdays.keyTraversed
		button1eaTradedLastDays.setSelection(true);
	}
	
	private void text1minProfitfaktorKeyTraversed(TraverseEvent evt) {
		System.out.println("text1minProfitfaktor.keyTraversed, event="+evt);
		//TODO add your code for text1minProfitfaktor.keyTraversed
		button1minprofitfaktor.setSelection(true);
		
	}
	
	private void text1maxdrawdownKeyTraversed(TraverseEvent evt) {
		System.out.println("text1maxdrawdown.keyTraversed, event="+evt);
		//TODO add your code for text1maxdrawdown.keyTraversed
		button1maxdrawdown.setSelection(true);
	}

}
