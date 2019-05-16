package swtHilfsfenster;
import modtools.Installer;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import StartFrame.Brokerview;

import com.cloudgarden.resource.SWTResourceManager;

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
public class SwtTransferUserdata extends org.eclipse.swt.widgets.Composite {

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	private Button button1copytobackup;
	private Button button1restore;
	private Text text2;
	private Text text1;
	
	private static Brokerview brokerview_glob=null;
	private Label label1;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI(Brokerview brokerview) {
		brokerview_glob =brokerview;
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SwtTransferUserdata inst = new SwtTransferUserdata(shell, SWT.NULL);
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
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	
	
	public SwtTransferUserdata(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(967, 394);
			{
				label1 = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 421);
				label1LData.top =  new FormAttachment(0, 1000, 17);
				label1LData.width = 195;
				label1LData.height = 17;
				label1.setLayoutData(label1LData);
				label1.setText("please read introduction carefully");
			}
			{
				text2 = new Text(this, SWT.MULTI | SWT.BORDER);
				FormData text2LData = new FormData();
				text2LData.left =  new FormAttachment(0, 1000, 12);
				text2LData.top =  new FormAttachment(0, 1000, 131);
				text2LData.width = 349;
				text2LData.height = 123;
				text2.setLayoutData(text2LData);
				text2.setText("Step2:\r\n- copy all Metatrader to the new Computer \r\n- Make update of all metatrader to the newest version\r\n- config Monitortool (autoconfig)\r\n- start all Metatrader\r\n- stop all Metatrader\r\n=> goto Step3");
				text2.setEditable(false);
			}
			{
				text1 = new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
				FormData text1LData = new FormData();
				text1LData.left =  new FormAttachment(0, 1000, 421);
				text1LData.top =  new FormAttachment(0, 1000, 40);
				text1LData.width = 448;
				text1LData.height = 336;
				text1.setLayoutData(text1LData);
				text1.setText("Introduction:\r\nWe need this function if we want to transfer all Metatrader from one computer to the next computer \r\n\r\nStep1:\r\nIf you click this button the following steps are done automaticaliy\r\n\r\nAll userdata in C:\\Users\\<username>\\AppData\\Roaming\\MetaQuotes\\Terminal\\xxDEF3369B8B652012F1DF12EC6D57066Cxx\r\n\r\nwill be copied in \r\n\r\n$METATRADERROOT/copyFromAppdata\r\n\r\n------------------------------------------------------------------\r\nStep2:\r\n\r\nThe following steps have to be done manualy\r\n- copy all Metatrader to the new Computer \r\n- Make update of all metatrader to the newest version\r\n- config Monitortool (autoconfig)\r\n- start all Metatrader\r\n- stop all Metatrader\r\n\r\n------------------------------------------------------------------\r\nStep3:\r\nYou do this step on the new system.\r\n\r\nIf you click this button all data from \r\n$METATRADERROOT/copyFromAppdata\r\n\r\nare restored into\r\n\r\nC:\\Users\\<username>\\AppData\\Roaming\\MetaQuotes\\Terminal\\xxDEF3369B8B652012F1DF12EC6D57066Cxx");
				text1.setEditable(false);
				text1.setFont(SWTResourceManager.getFont("Segoe UI", 10, 0, false, false));
			}
			{
				button1restore = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1restoreLData = new FormData();
				button1restoreLData.left =  new FormAttachment(0, 1000, 12);
				button1restoreLData.top =  new FormAttachment(0, 1000, 266);
				button1restoreLData.width = 361;
				button1restoreLData.height = 116;
				button1restore.setLayoutData(button1restoreLData);
				button1restore.setText("Step3: transfer to new system");
				button1restore.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1restoreWidgetSelected(evt);
					}
				});
			}
			{
				button1copytobackup = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1copytobackupLData = new FormData();
				button1copytobackupLData.left =  new FormAttachment(0, 1000, 12);
				button1copytobackupLData.top =  new FormAttachment(0, 1000, 17);
				button1copytobackupLData.width = 361;
				button1copytobackupLData.height = 108;
				button1copytobackup.setLayoutData(button1copytobackupLData);
				button1copytobackup.setText("Step1: copy to backup");
				button1copytobackup.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1copytobackupWidgetSelected(evt);
					}
				});
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void button1copytobackupWidgetSelected(SelectionEvent evt) {
		System.out.println("button1copytobackup.widgetSelected, event="+evt);
		//TODO add your code for button1copytobackup.widgetSelected
		Installer inst = new Installer();
		inst.transferuserdata(Display.getDefault(), brokerview_glob);
	}
	
	private void button1restoreWidgetSelected(SelectionEvent evt) {
		System.out.println("button1restore.widgetSelected, event="+evt);
		//TODO add your code for button1restore.widgetSelected
		Installer inst = new Installer();
		inst.restoreuserdata(Display.getDefault(), brokerview_glob);
	}

}
