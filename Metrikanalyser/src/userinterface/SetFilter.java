package userinterface;

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

import data.MFilter;
import hilfsklasse.SG;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SetFilter extends org.eclipse.swt.widgets.Composite
{
	private Text text1minstabil;
	private Button button1minstabil;
	private Text text1minanzstrat;
	private Label label1;
	private Label label4;
	private Text text2stability;
	private Label label3;
	private Text text1;
	private Label label2;
	private Button button1ok;
	private Button button1minanzstrat;
	static int endflag = 0;
	private static MFilter mfilter_glob=null;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args)
	{
		showGUI(null);
	}

	/**
	 * Overriding checkSubclass allows this class to extend
	 * org.eclipse.swt.widgets.Composite
	 */
	protected void checkSubclass()
	{
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void showGUI(MFilter mfilter)
	{
		mfilter_glob=mfilter;
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SetFilter inst = new SetFilter(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		endflag = 0;
		if (size.x == 0 && size.y == 0)
		{
			inst.pack();
			shell.pack();
		} else
		{
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				if (endflag == 1)
					shell.dispose();
			display.sleep();
		}
	}

	public SetFilter(org.eclipse.swt.widgets.Composite parent, int style)
	{
		super(parent, style);
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(492, 119);
			{
				label4 = new Label(this, SWT.NONE);
				FormData label4LData = new FormData();
				label4LData.left = new FormAttachment(0, 1000, 439);
				label4LData.top = new FormAttachment(0, 1000, 23);
				label4LData.width = 41;
				label4LData.height = 15;
				label4.setLayoutData(label4LData);
				label4.setText("stability");
				label4.setEnabled(false);
			}
			{
				text2stability = new Text(this, SWT.NONE);
				FormData text2stabilityLData = new FormData();
				text2stabilityLData.left = new FormAttachment(0, 1000, 393);
				text2stabilityLData.top = new FormAttachment(0, 1000, 23);
				text2stabilityLData.width = 28;
				text2stabilityLData.height = 15;
				text2stability.setLayoutData(text2stabilityLData);
				text2stability.setText("1");
				text2stability.setEnabled(false);
			}
			{
				label3 = new Label(this, SWT.NONE);
				FormData label3LData = new FormData();
				label3LData.left = new FormAttachment(0, 1000, 331);
				label3LData.top = new FormAttachment(0, 1000, 23);
				label3LData.width = 29;
				label3LData.height = 15;
				label3.setLayoutData(label3LData);
				label3.setText("profit");
				label3.setEnabled(false);
			}
			{
				text1 = new Text(this, SWT.NONE);
				FormData text1LData = new FormData();
				text1LData.left = new FormAttachment(0, 1000, 292);
				text1LData.top = new FormAttachment(0, 1000, 23);
				text1LData.width = 27;
				text1LData.height = 15;
				text1.setLayoutData(text1LData);
				text1.setText("1");
				text1.setEnabled(false);
			}
			{
				label2 = new Label(this, SWT.NONE);
				FormData label2LData = new FormData();
				label2LData.left = new FormAttachment(0, 1000, 292);
				label2LData.top = new FormAttachment(0, 1000, 3);
				label2LData.width = 79;
				label2LData.height = 15;
				label2.setLayoutData(label2LData);
				label2.setText("fitnessfunction");
				label2.setEnabled(false);
			}
			{
				label1 = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left = new FormAttachment(0, 1000, 0);
				label1LData.top = new FormAttachment(0, 1000, 3);
				label1LData.width = 142;
				label1LData.height = 15;
				label1.setLayoutData(label1LData);
				label1.setText("filtervalues");
			}
			{
				button1ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1okLData = new FormData();
				button1okLData.left = new FormAttachment(0, 1000, 413);
				button1okLData.top = new FormAttachment(0, 1000, 82);
				button1okLData.width = 67;
				button1okLData.height = 25;
				button1ok.setLayoutData(button1okLData);
				button1ok.setText("ok");
				button1ok.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1okWidgetSelected(evt);
					}
				});
			}
			{
				button1minanzstrat = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button1minanzstratLData = new FormData();
				button1minanzstratLData.left = new FormAttachment(0, 1000, 5);
				button1minanzstratLData.top = new FormAttachment(0, 1000, 45);
				button1minanzstratLData.width = 83;
				button1minanzstratLData.height = 16;
				button1minanzstrat.setLayoutData(button1minanzstratLData);
				button1minanzstrat.setText("minanzstrat");
				button1minanzstrat.setSelection(true);
				button1minanzstrat.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1minanzstratWidgetSelected(evt);
					}
				});
			}
			{
				text1minanzstrat = new Text(this, SWT.NONE);
				FormData text1minanzstratLData = new FormData();
				text1minanzstratLData.left = new FormAttachment(0, 1000, 106);
				text1minanzstratLData.top = new FormAttachment(0, 1000, 50);
				text1minanzstratLData.width = 30;
				text1minanzstratLData.height = 15;
				text1minanzstrat.setLayoutData(text1minanzstratLData);
				text1minanzstrat.setText("5");
				text1minanzstrat.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						text1minanzstratWidgetSelected(evt);
					}
				});
			}
			{
				button1minstabil = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button1minstabilLData = new FormData();
				button1minstabilLData.left = new FormAttachment(0, 1000, 5);
				button1minstabilLData.top = new FormAttachment(0, 1000, 23);
				button1minstabilLData.width = 70;
				button1minstabilLData.height = 16;
				button1minstabil.setLayoutData(button1minstabilLData);
				button1minstabil.setText("minstabil");
				button1minstabil.setSelection(true);
				button1minstabil.setEnabled(false);
			}
			{
				text1minstabil = new Text(this, SWT.NONE);
				FormData text1minstabilLData = new FormData();
				text1minstabilLData.left = new FormAttachment(0, 1000, 106);
				text1minstabilLData.top = new FormAttachment(0, 1000, 23);
				text1minstabilLData.width = 30;
				text1minstabilLData.height = 15;
				text1minstabil.setLayoutData(text1minstabilLData);
				text1minstabil.setText("0.2");
				text1minstabil.setEnabled(false);
			}
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void button1okWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1ok.widgetSelected, event=" + evt);
		endflag = 1;
	}

	private void text1minanzstratWidgetSelected(SelectionEvent evt)
	{
		System.out.println("text1minanzstrat.widgetSelected, event=" + evt);
		
		String minanz_str=text1minanzstrat.getText();
		mfilter_glob.setMinanzahlstrat(SG.get_zahl(minanz_str));

	}
	
	private void button1minanzstratWidgetSelected(SelectionEvent evt) {
		System.out.println("button1minanzstrat.widgetSelected, event="+evt);
		Boolean boolsel=button1minanzstrat.getSelection();
		mfilter_glob.setAnzstratflag(boolsel);
	}

}
