package swtGbWindow;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
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
public class EditGbAnalyserConfig extends org.eclipse.swt.widgets.Composite {
	private Text gbtmppath;
	private Button foundstrategiespath;
	private Button ok;
	private Text foundstrategies;
	private Button settmppath;

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
		EditGbAnalyserConfig inst = new EditGbAnalyserConfig(shell, SWT.NULL);
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

	public EditGbAnalyserConfig(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(471, 167);
			{
				ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData okLData = new FormData();
				okLData.left =  new FormAttachment(0, 1000, 431);
				okLData.top =  new FormAttachment(0, 1000, 125);
				okLData.width = 28;
				okLData.height = 30;
				ok.setLayoutData(okLData);
				ok.setText("ok");
			}
			{
				foundstrategiespath = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData foundstrategiespathLData = new FormData();
				foundstrategiespathLData.left =  new FormAttachment(0, 1000, 272);
				foundstrategiespathLData.top =  new FormAttachment(0, 1000, 66);
				foundstrategiespathLData.width = 187;
				foundstrategiespathLData.height = 30;
				foundstrategiespath.setLayoutData(foundstrategiespathLData);
				foundstrategiespath.setText("FoundStrategiesPath");
			}
			{
				FormData foundstrategiesLData = new FormData();
				foundstrategiesLData.left =  new FormAttachment(0, 1000, 12);
				foundstrategiesLData.top =  new FormAttachment(0, 1000, 66);
				foundstrategiesLData.width = 240;
				foundstrategiesLData.height = 26;
				foundstrategies = new Text(this, SWT.NONE);
				foundstrategies.setLayoutData(foundstrategiesLData);
			}
			{
				settmppath = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData settmppathLData = new FormData();
				settmppathLData.left =  new FormAttachment(0, 1000, 272);
				settmppathLData.top =  new FormAttachment(0, 1000, 28);
				settmppathLData.width = 187;
				settmppathLData.height = 26;
				settmppath.setLayoutData(settmppathLData);
				settmppath.setText("SetGeneticTmpPath");
			}
			{
				FormData gbtmppathLData = new FormData();
				gbtmppathLData.left =  new FormAttachment(0, 1000, 12);
				gbtmppathLData.top =  new FormAttachment(0, 1000, 28);
				gbtmppathLData.width = 240;
				gbtmppathLData.height = 26;
				gbtmppath = new Text(this, SWT.NONE);
				gbtmppath.setLayoutData(gbtmppathLData);
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
