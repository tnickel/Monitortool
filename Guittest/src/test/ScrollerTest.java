package test;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

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
public class ScrollerTest extends org.eclipse.swt.widgets.Composite {
	private ScrolledComposite scrolledComposite1;
	private Button button2;
	private Button button1;
	private Composite composite1;
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
		ScrollerTest inst = new ScrollerTest(shell, SWT.NULL);
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

	public ScrollerTest(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			{
				//dies ist die gesammtfläche die auf dem bildschrim erscheint
				this.setSize(1000, 1000);
				{
					scrolledComposite1 = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
					//dies ist das Fenster des Scrollt composite, diesen Ausschnitt sieht der user
					scrolledComposite1.setBounds(23, 32, 800, 800);
					scrolledComposite1.setLayout(null);
					{
						composite1 = new Composite(scrolledComposite1, SWT.NONE);
						composite1.setLayout(null);
						scrolledComposite1.setContent(composite1);
						//1600x1600 ist die gesammte virtuelle fläche (composite1), hierdrauf können objekte plaziert werden
						//diese gesammtfläche wird in dem Scrolled Composite eingebaut
						composite1.setBounds(2, 44, 1600, 1600);
						{
							button1 = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button1.setText("button1");
							button1.setBounds(5, 5, 64, 30);
						}
						{
							button2 = new Button(composite1, SWT.PUSH | SWT.CENTER);
							button2.setText("button2");
							button2.setBounds(1275, 1258, 64, 30);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
