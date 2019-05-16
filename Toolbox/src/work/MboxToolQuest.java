package work;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
public class MboxToolQuest extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Button ok;
	private Label label1;
	private Button button1no;
	private int exitflag=0;
	private int message_glob=0;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			MboxToolQuest inst = new MboxToolQuest(shell, SWT.NULL);
			inst.open("hallo");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int Questbox(String message)
	{
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			MboxToolQuest inst = new MboxToolQuest(shell, SWT.NULL);
			int ret=inst.open( message);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public MboxToolQuest(Shell parent, int style) {
		super(parent, style);
	}

	public int open(String message) {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.ON_TOP|SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			dialogShell.setLayout(new FormLayout());
			{
				button1no = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData button1noLData = new FormData();
				button1noLData.left =  new FormAttachment(0, 1000, 773);
				button1noLData.top =  new FormAttachment(0, 1000, 130);
				button1noLData.width = 64;
				button1noLData.height = 30;
				button1no.setLayoutData(button1noLData);
				button1no.setText("no");
				button1no.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1noWidgetSelected(evt);
					}
				});
			}
			{
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 17);
				label1LData.top =  new FormAttachment(0, 1000, 29);
				label1LData.width = 1116;
				label1LData.height = 42;
				label1 = new Label(dialogShell, SWT.NONE);
				label1.setLayoutData(label1LData);
				label1.setText(message);
			}
			{
				ok = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData button1LData = new FormData();
				button1LData.left =  new FormAttachment(0, 1000, 697);
				button1LData.top =  new FormAttachment(0, 1000, 130);
				button1LData.width = 64;
				button1LData.height = 30;

			
				ok.setLayoutData(button1LData);
				ok.setText("yes");
				ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						okWidgetSelected(evt);
					}
				});
			}
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(1195, 215);
			dialogShell.setVisible(false);
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
				if(exitflag==1)
				{
					dialogShell.close();
					return message_glob;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message_glob;
	}
	
	private void okWidgetSelected(SelectionEvent evt) {
		System.out.println("ok.widgetSelected, event="+evt);
		//ok-exit
		exitflag=1;
		message_glob=1;
	}
	
	private void button1noWidgetSelected(SelectionEvent evt) {
		System.out.println("button1no.widgetSelected, event="+evt);
		//TODO add your code for button1no.widgetSelected
		//ok-exit
				exitflag=1;
				message_glob=0;
	}

}
