package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import hiflsklasse.Tracer;

public class Mbox
{
	static public void Infobox(String infostring)
	{
		if(infostring ==null)
			infostring="empty";
		
		Display display=Display.getDefault();
		Shell shell=display.getActiveShell();
		
		if(shell!=null)
		{
		MessageBox infoBox = new MessageBox(shell, SWT.ICON_INFORMATION);
        infoBox.setText("About Launcher");
        infoBox.setMessage(infostring);
        infoBox.open();
		}
		else
			Tracer.WriteTrace(10, "Info: <"+infostring+">");
	}
}
	/*public class Mbox extends org.eclipse.swt.widgets.Dialog {

		private Shell dialogShell;
		private Button ok;
		private Label label1;
		private int exitflag=0;

		*//**
		* Auto-generated main method to display this 
		* org.eclipse.swt.widgets.Dialog inside a new Shell.
		*//*
		public static void main(String[] args) {
			try {
				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				Mbox inst = new Mbox(shell, SWT.NULL);
				inst.open("hallo");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void Infobox(String message)
		{
			try {
				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				Mbox inst = new Mbox(shell, SWT.NULL);
				inst.open( message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public Mbox(Shell parent, int style) {
			super(parent, style);
		}

		public void open(String message) {
			try {
				Shell parent = getParent();
				dialogShell = new Shell(parent, SWT.ON_TOP|SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

				{
					//Register as a resource user - SWTResourceManager will
					//handle the obtaining and disposing of resources
					SWTResourceManager.registerResourceUser(dialogShell);
				}
				
				
				dialogShell.setLayout(new FormLayout());
				{
					FormData label1LData = new FormData();
					label1LData.left =  new FormAttachment(0, 1000, 17);
					label1LData.top =  new FormAttachment(0, 1000, 29);
					label1LData.width = 1204;
					label1LData.height = 42;
					label1 = new Label(dialogShell, SWT.NONE);
					label1.setLayoutData(label1LData);
					label1.setText(message);
					label1.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
				}
				{
					ok = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
					FormData button1LData = new FormData();
					button1LData.left =  new FormAttachment(0, 1000, 1157);
					button1LData.top =  new FormAttachment(0, 1000, 130);
					button1LData.width = 64;
					button1LData.height = 30;

				
					ok.setLayoutData(button1LData);
					ok.setText("ok");
					ok.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							okWidgetSelected(evt);
						}
					});
				}
				dialogShell.layout();
				dialogShell.pack();			
				dialogShell.setSize(1265, 215);
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
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void okWidgetSelected(SelectionEvent evt) {
			System.out.println("ok.widgetSelected, event="+evt);
			//ok-exit
			exitflag=1;
		}

	}*/

