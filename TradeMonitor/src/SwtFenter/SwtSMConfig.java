package SwtFenter;

import gui.Dialog;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import data.SMglobalConfig;


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
public class SwtSMConfig extends org.eclipse.swt.widgets.Composite {
	private Text text1tradelistendir;
	private Button button1settradelistendir;
	private Button button1ok;
	private Button button1setcmdir;
	private Text text1cmddir;
	private static int exitflag=0;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SwtSMConfig inst = new SwtSMConfig(shell, SWT.NULL);
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
		while ((!shell.isDisposed()) &&(exitflag==0))
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		exitflag=0;
		shell.dispose();
	}

	public SwtSMConfig(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(856, 435);
			{
				button1setcmdir = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1setcmdirLData = new FormData();
				button1setcmdirLData.left =  new FormAttachment(0, 1000, 435);
				button1setcmdirLData.top =  new FormAttachment(0, 1000, 76);
				button1setcmdirLData.width = 157;
				button1setcmdirLData.height = 27;
				button1setcmdir.setLayoutData(button1setcmdirLData);
				button1setcmdir.setText("set cmddir");
				button1setcmdir.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1setcmdirWidgetSelected(evt);
					}
				});
			}
			{
				FormData text1cmddirLData = new FormData();
				text1cmddirLData.left =  new FormAttachment(0, 1000, 23);
				text1cmddirLData.top =  new FormAttachment(0, 1000, 76);
				text1cmddirLData.width = 382;
				text1cmddirLData.height = 27;
				text1cmddir = new Text(this, SWT.NONE);
				if(SMglobalConfig.getCmddir()!= null)
					text1cmddir.setText(SMglobalConfig.getCmddir());
				text1cmddir.setLayoutData(text1cmddirLData);
			}
			{
				button1ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1okLData = new FormData();
				button1okLData.left =  new FormAttachment(0, 1000, 761);
				button1okLData.top =  new FormAttachment(0, 1000, 390);
				button1okLData.width = 80;
				button1okLData.height = 30;
				button1ok.setLayoutData(button1okLData);
				button1ok.setText("save");
				button1ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1okWidgetSelected(evt);
					}
				});
			}
			{
				button1settradelistendir = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1settradelistendirLData = new FormData();
				button1settradelistendirLData.left =  new FormAttachment(0, 1000, 435);
				button1settradelistendirLData.top =  new FormAttachment(0, 1000, 29);
				button1settradelistendirLData.width = 157;
				button1settradelistendirLData.height = 30;
				button1settradelistendir.setLayoutData(button1settradelistendirLData);
				button1settradelistendir.setText("set tradelistendir");
				button1settradelistendir.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1settradelistendirWidgetSelected(evt);
					}
				});
			}
			{
				FormData text1tradelistendirLData = new FormData();
				text1tradelistendirLData.left =  new FormAttachment(0, 1000, 23);
				text1tradelistendirLData.top =  new FormAttachment(0, 1000, 29);
				text1tradelistendirLData.width = 382;
				text1tradelistendirLData.height = 30;
				text1tradelistendir = new Text(this, SWT.NONE);
				if(SMglobalConfig.getTradelistendir()!= null)
					text1tradelistendir.setText(SMglobalConfig.getTradelistendir());
				text1tradelistendir.setLayoutData(text1tradelistendirLData);
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void button1settradelistendirWidgetSelected(SelectionEvent evt) {
		System.out.println("button1settradelistendir.widgetSelected, event="+evt);
		String tradelistendir=Dialog.DirDialog(getDisplay(), "\\");
		SMglobalConfig.setTradelistendir(tradelistendir);
		text1tradelistendir.setText(tradelistendir);
	}
	
	private void button1okWidgetSelected(SelectionEvent evt) {
		System.out.println("button1ok.widgetSelected, event="+evt);
		//TODO add your code for button1ok.widgetSelected
		exitflag=1;
	}
	
	private void button1setcmdirWidgetSelected(SelectionEvent evt) {
		System.out.println("button1setcmdir.widgetSelected, event="+evt);
		//TODO add your code for button1setcmdir.widgetSelected
		String cmddir=Dialog.DirDialog(getDisplay(), "\\");
		SMglobalConfig.setCmddir(cmddir);
		text1cmddir.setText(cmddir);
	}

}
