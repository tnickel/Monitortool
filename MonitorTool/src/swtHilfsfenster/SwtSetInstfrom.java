package swtHilfsfenster;


import gui.Mbox;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import StartFrame.Brokerview;
import data.Ea;
import data.Ealiste;
import data.Metaconfig;
import data.Profit;

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
public class SwtSetInstfrom extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Combo combo1;
	private Button button1ok;
	private Label label1;
	private int endflag=0;
	static private Brokerview brokerview_glob=null;
	static private ArrayList<Profit> profitliste_glob=null;
	static private Ealiste ealiste_glob=null;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void mainx(Brokerview brokerview,ArrayList<Profit> profitliste,Ealiste ealist) 
	{
		try {
			brokerview_glob=brokerview;
			profitliste_glob=profitliste;
			ealiste_glob=ealist;
			
			if(profitliste_glob.size()==0)
			{
				Mbox.Infobox("no ea selected, please select one or more");
				return;
			}
			
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			SwtSetInstfrom inst = new SwtSetInstfrom(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SwtSetInstfrom(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			dialogShell.setLayout(new FormLayout());
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(233, 167);
			{
				label1 = new Label(dialogShell, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 19);
				label1LData.top =  new FormAttachment(0, 1000, 12);
				label1LData.width = 184;
				label1LData.height = 20;
				label1.setLayoutData(label1LData);
				label1.setText("Set Inst From Demobroker");
			}
			{
				button1ok = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData button1okLData = new FormData();
				button1okLData.left =  new FormAttachment(0, 1000, 19);
				button1okLData.top =  new FormAttachment(0, 1000, 87);
				button1okLData.width = 184;
				button1okLData.height = 30;
				button1ok.setLayoutData(button1okLData);
				button1ok.setText("ok");
				button1ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1okWidgetSelected(evt);
					}
				});
			}
			{
				combo1 = new Combo(dialogShell, SWT.NONE);
				FormData combo1LData = new FormData();
				combo1LData.left =  new FormAttachment(0, 1000, 19);
				combo1LData.top =  new FormAttachment(0, 1000, 47);
				combo1LData.width = 151;
				combo1LData.height = 28;
				combo1.setLayoutData(combo1LData);
				combo1.setText("SetInstfrom");
				combo1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						combo1WidgetSelected(evt);
					}
				});
			}
			init();
			
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) 
			{
				if(endflag==1)
					break;
				if (!display.readAndDispatch())
					display.sleep();
				
			}
			display.getActiveShell().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init()
	{
		int anz=brokerview_glob.getAnz();
		for(int i=0; i<anz; i++)
		{
			Metaconfig meconf=brokerview_glob.getElem(i);
			if(meconf.getAccounttype()==1)
				combo1.add(meconf.getBrokername());
		}
	}
	
	private void button1okWidgetSelected(SelectionEvent evt) {
		System.out.println("button1ok.widgetSelected, event="+evt);
		//TODO add your code for button1ok.widgetSelected
		endflag=1;
	}
	
	private void combo1WidgetSelected(SelectionEvent evt) {
		System.out.println("combo1.widgetSelected, event="+evt);
		//TODO Ein demobroker wurde in der combobox selectiert

		int index=combo1.getSelectionIndex();
		String demobrokername=combo1.getItem(index);
		
		//setzte bei allen ea´s das instfrom feld
		int anz=profitliste_glob.size();
		
	
		
		for(int i=0; i<anz; i++)
		{
			//jeder ea im Realbroker bekommt die verkünpfung instfrom gesetzt
			Profit prof=profitliste_glob.get(i);
			int magic=prof.getMagic();
			String broker=prof.getBroker();
			Ea ea= ealiste_glob.getEa(magic, broker);
			ea.setInstFrom(demobrokername);
		}
		//ealiste speichern
		ealiste_glob.store(0);
	}

}
