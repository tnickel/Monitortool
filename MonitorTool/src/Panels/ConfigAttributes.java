package Panels;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


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
public class ConfigAttributes extends org.eclipse.swt.widgets.Composite {
	private Button button1ok;
	private Button attribute2;
	private Button attribute1;

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
		ConfigAttributes inst = new ConfigAttributes(shell, SWT.NULL);
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

	public ConfigAttributes(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(405, 348);
			{
				attribute2 = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData attribute2LData = new FormData();
				attribute2LData.left =  new FormAttachment(0, 1000, 23);
				attribute2LData.top =  new FormAttachment(0, 1000, 52);
				attribute2LData.width = 71;
				attribute2LData.height = 16;
				attribute2.setLayoutData(attribute2LData);
				attribute2.setText("attribute2");
			}
			{
				attribute1 = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData attribute1LData = new FormData();
				attribute1LData.left =  new FormAttachment(0, 1000, 23);
				attribute1LData.top =  new FormAttachment(0, 1000, 24);
				attribute1LData.width = 71;
				attribute1LData.height = 16;
				attribute1.setLayoutData(attribute1LData);
				attribute1.setText("attribute1");
			}
			{
				button1ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1okLData = new FormData();
				button1okLData.left =  new FormAttachment(0, 1000, 316);
				button1okLData.top =  new FormAttachment(0, 1000, 299);
				button1okLData.width = 64;
				button1okLData.height = 21;
				button1ok.setLayoutData(button1okLData);
				button1ok.setText("Ok");
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
