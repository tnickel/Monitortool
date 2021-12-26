package swtHilfsfenster;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import data.GlobalVar;

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
public class SwtSetSortCriteria extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Group group1;
	private Button magic;
	private Button sortprofit30;
	private Button buttonDrawdown;
	private Button buttonProfitfaktor;
	private Button button1info2;
	private Button button1info1;
	private Button buttonpz1;
	private Button okbutton;
	private Button sortanztrades;
	private Button sortprofitall;
	private Button sortprofit10;
	private Button sortcomment;
	private int endflag=0;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void mainx() {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			SwtSetSortCriteria inst = new SwtSetSortCriteria(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SwtSetSortCriteria(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			
		
			

			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			GridLayout dialogShellLayout = new GridLayout();
			dialogShell.setLayout(null);
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(122, 334);
			{
				group1 = new Group(dialogShell, SWT.NONE);
				GridLayout group1Layout = new GridLayout();
				group1.setLayout(null);
				GridData group1LData = new GridData();
				group1.setText("set sort criteria");
				group1.setBounds(5, 5, 82, 264);
				{
					magic = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData magicLData = new GridData();
					magic.setText("magic");
					magic.setBounds(7, 20, 45, 15);
					if(GlobalVar.getSortcriteria()==1)
						magic.setSelection(true);
					magic.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							magicWidgetSelected(evt);
						}
					});
				}
				{
					sortcomment = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData sortcommentLData = new GridData();
					sortcomment.setText("comment");
					if(GlobalVar.getSortcriteria()==2)
						sortcomment.setSelection(true);
						sortcomment.setBounds(7, 40, 61, 15);
					sortcomment.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							sortcommentWidgetSelected(evt);
						}
					});
				}
				{
					sortprofit10 = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData sortprofit10LData = new GridData();
					sortprofit10.setText("profit10");
					sortprofit10.setBounds(7, 60, 53, 15);
					if(GlobalVar.getSortcriteria()==3)
						sortprofit10.setSelection(true);
					sortprofit10.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							sortprofit10WidgetSelected(evt);
						}
					});
				}
				{
					sortprofit30 = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData sortprofit30LData = new GridData();
					sortprofit30.setText("profit30");
					sortprofit30.setBounds(7, 80, 53, 15);
					if(GlobalVar.getSortcriteria()==4)
						sortprofit30.setSelection(true);
					sortprofit30.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							sortprofit30WidgetSelected(evt);
						}
					});
				}
				{
					sortprofitall = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData sortprofitallLData = new GridData();
					sortprofitall.setText("profit all");
					sortprofitall.setBounds(7, 100, 55, 15);
					if(GlobalVar.getSortcriteria()==5)
						sortprofitall.setSelection(true);
					sortprofitall.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							sortprofitallWidgetSelected(evt);
						}
					});
				}
				{
					sortanztrades = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData sortanztradesLData = new GridData();
					sortanztrades.setText("# trades");
					sortanztrades.setBounds(7, 120, 54, 15);
					if(GlobalVar.getSortcriteria()==6)
						sortanztrades.setSelection(true);
					sortanztrades.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							sortanztradesWidgetSelected(evt);
						}
					});
				}
				{
					button1info1 = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData button1info1LData = new GridData();
					button1info1.setText("info1");
					button1info1.setBounds(7, 140, 40, 15);
					if(GlobalVar.getSortcriteria()==7)
						button1info1.setSelection(true);
					button1info1.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							button1info1WidgetSelected(evt);
						}
					});
				}
				{
					button1info2 = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData button1info2LData = new GridData();
					button1info2.setText("info2");
					button1info2.setBounds(7, 160, 40, 15);
					if(GlobalVar.getSortcriteria()==8)
						button1info2.setSelection(true);
					button1info2.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							button1info2WidgetSelected(evt);
						}
					});
				}
				{
					buttonProfitfaktor = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData buttonProfitfaktorLData = new GridData();
					buttonProfitfaktor.setText("profitfaktor");
					buttonProfitfaktor.setBounds(7, 180, 69, 15);
					if(GlobalVar.getSortcriteria()==9)
						buttonProfitfaktor.setSelection(true);
					buttonProfitfaktor.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							buttonProfitfaktorWidgetSelected(evt);
						}
					});
				}
				{
					buttonDrawdown = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData buttonDrawdownLData = new GridData();
					buttonDrawdown.setText("drawdown");
					buttonDrawdown.setBounds(7, 200, 65, 15);
					if(GlobalVar.getSortcriteria()==10)
						buttonDrawdown.setSelection(true);
					buttonDrawdown.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							buttonDrawdownWidgetSelected(evt);
						}
					});
				}
				{
					buttonpz1 = new Button(group1, SWT.RADIO | SWT.LEFT);
					GridData buttonDrawdownLData = new GridData();
					buttonpz1.setText("pz1");
					buttonpz1.setBounds(7, 220, 33, 15);
					if(GlobalVar.getSortcriteria()==11)
						buttonpz1.setSelection(true);
					buttonpz1.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							buttonPz1WidgetSelected(evt);
						}
					});
				}
				
				
			}
			{
				okbutton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				GridData okbuttonLData = new GridData();
				okbutton.setText("ok");
				okbutton.setBounds(70, 275, 17, 20);
				okbutton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						okbuttonWidgetSelected(evt);
					}
				});
			}
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
			dialogShell.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void magicWidgetSelected(SelectionEvent evt) {
		System.out.println("magic.widgetSelected, event="+evt);
		//TODO add your cod.
		GlobalVar.setSortcriteria(1);
	}
	
	private void sortcommentWidgetSelected(SelectionEvent evt) {
		System.out.println("sortcomment.widgetSelected, event="+evt);
		//TODO add your code for sortcomment.widgetSelected
		GlobalVar.setSortcriteria(2);
	}
	
	private void sortprofit10WidgetSelected(SelectionEvent evt) {
		System.out.println("sortprofit10.widgetSelected, event="+evt);
		//TODO add your code for sortprofit10.widgetSelected
		GlobalVar.setSortcriteria(3);
	}
	
	private void sortprofit30WidgetSelected(SelectionEvent evt) {
		System.out.println("sortprofit30.widgetSelected, event="+evt);
		//TODO add your code for sortprofit30.widgetSelected
		GlobalVar.setSortcriteria(4);
	}
	
	private void sortprofitallWidgetSelected(SelectionEvent evt) {
		System.out.println("sortprofitall.widgetSelected, event="+evt);
		//TODO add your code for sortprofitall.widgetSelected
		GlobalVar.setSortcriteria(5);
	}
	
	private void sortanztradesWidgetSelected(SelectionEvent evt) {
		System.out.println("sortanztrades.widgetSelected, event="+evt);
		//TODO add your code for sortanztrades.widgetSelected
		GlobalVar.setSortcriteria(6);
	}
	
	private void okbuttonWidgetSelected(SelectionEvent evt) {
		System.out.println("okbutton.widgetSelected, event="+evt);
		//TODO add your code for okbutton.widgetSelected
		GlobalVar.save();
		endflag=1;
	}
	
	private void button1info1WidgetSelected(SelectionEvent evt) {
		System.out.println("button1info1.widgetSelected, event="+evt);
		//TODO add your code for button1info1.widgetSelected
		GlobalVar.setSortcriteria(7);
	}
	
	private void button1info2WidgetSelected(SelectionEvent evt) {
		System.out.println("button1info2.widgetSelected, event="+evt);
		//TODO add your code for button1info2.widgetSelected
		GlobalVar.setSortcriteria(8);
	}
	
	private void buttonProfitfaktorWidgetSelected(SelectionEvent evt) {
		System.out.println("buttonProfitfaktor.widgetSelected, event="+evt);
		//TODO add your code for buttonProfitfaktor.widgetSelected
		GlobalVar.setSortcriteria(9);
	}
	
	private void buttonDrawdownWidgetSelected(SelectionEvent evt) {
		System.out.println("buttonDrawdown.widgetSelected, event="+evt);
		//TODO add your code for buttonDrawdown.widgetSelected
		GlobalVar.setSortcriteria(10);
	}
	private void buttonPz1WidgetSelected(SelectionEvent evt) {
		System.out.println("buttonPz1.widgetSelected, event="+evt);
		//TODO add your code for buttonDrawdown.widgetSelected
		GlobalVar.setSortcriteria(11);
	}
}
