package swtHilfsfenster;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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
public class SwtPicture extends org.eclipse.swt.widgets.Composite {
	private Label label1;
	private Button button1ok;
	static private Shell shell_glob;
	static private String fnam_glob=null;
	static int endflag=0;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void mainx(String fnam)
	{
		endflag=0;
		fnam_glob=fnam;
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
		shell_glob=shell;
		SwtPicture inst = new SwtPicture(shell, SWT.NULL);
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
		while (!shell.isDisposed()) 
		{
			if (!display.readAndDispatch())
				display.sleep();
			if(endflag==1)
			{
				shell.dispose();
			}
		}
	}

	public SwtPicture(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(1140, 476);
			{
				button1ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1LData = new FormData();
				button1LData.left =  new FormAttachment(0, 1000, 1051);
				button1LData.top =  new FormAttachment(0, 1000, 422);
				button1LData.width = 64;
				button1LData.height = 30;
				button1ok.setLayoutData(button1LData);
				button1ok.setText("ok");
				button1ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1WidgetSelected(evt);
					}
				});
			}
			{
				label1 = new Label(this, SWT.CENTER | SWT.BORDER);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 12);
				label1LData.top =  new FormAttachment(0, 1000, 12);
				label1LData.width = 1101;
				label1LData.height = 388;
				label1.setLayoutData(label1LData);
			}
			init();
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void init()
	{
		Image myImage = new Image( Display.getDefault(), fnam_glob );
		Image myImage2=resize(myImage, 1100, 350);
		label1.setImage( myImage2 );
	}
	private Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0,
		image.getBounds().width, image.getBounds().height,
		0, 0, width, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
		}
	
	private void button1WidgetSelected(SelectionEvent evt) {
		System.out.println("button1.widgetSelected, event="+evt);
		//TODO add your code for button1.widgetSelected
		endflag=1;
	}
}
