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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import data.CorelSetting;
import hilfsklasse.SG;


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
public class SetCorrelationConfig extends org.eclipse.swt.widgets.Composite {
	private Button button1ok;
	private Group group1;
	private Label label3;
	private Text text2corefaktor;
	private Label label2;
	private Text text1;
	private Label label1;
	private Text text1anzsteps;
	private static CorelSetting corelsetting_glob=null;
	static private int endflag = 0;
	
	
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI(CorelSetting corelsetting) {
		corelsetting_glob=corelsetting;
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SetCorrelationConfig inst = new SetCorrelationConfig(shell, SWT.NULL);
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
				if (endflag == 1)
				{
					endflag=0;
					shell.dispose();
				}
			display.sleep();
		}
	}

	public SetCorrelationConfig(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(350, 128);
			{
				group1 = new Group(this, SWT.NONE);
				group1.setLayout(null);
				FormData group1LData = new FormData();
				group1LData.left =  new FormAttachment(0, 1000, 12);
				group1LData.top =  new FormAttachment(0, 1000, 12);
				group1LData.width = 244;
				group1LData.height = 86;
				group1.setLayoutData(group1LData);
				group1.setText("correlation settings");
				{
					text1anzsteps = new Text(group1, SWT.NONE);
					text1anzsteps.setText(String.valueOf(corelsetting_glob.getAnzSteps()));
					text1anzsteps.setBounds(8, 20, 33, 15);
				}
				{
					label1 = new Label(group1, SWT.NONE);
					label1.setText("anz steps (i)");
					label1.setBounds(47, 20, 74, 15);
					label1.setToolTipText("#steps in the random variation");
				}
				{
					text1 = new Text(group1, SWT.NONE);
					text1.setText(String.valueOf(corelsetting_glob.getMinCorelLevel()));
					text1.setBounds(8, 47, 33, 15);
				}
				{
					label2 = new Label(group1, SWT.NONE);
					label2.setText("min correlation level (i)");
					label2.setBounds(47, 47, 127, 15);
					label2.setToolTipText("The metric must have at least this minimum correlation level. If the metric has less correlation, this metric will not be used.");
				}
				{
					text2corefaktor = new Text(group1, SWT.NONE);
					text2corefaktor.setText(String.valueOf(corelsetting_glob.getCorrelFaktor()));
					text2corefaktor.setBounds(8, 74, 33, 15);
				}
				{
					label3 = new Label(group1, SWT.NONE);
					label3.setText("correlation faktor (i)");
					label3.setBounds(47, 74, 112, 15);
					label3.setToolTipText("a)\tI use only the metric if the correlation is >0.1\r\n\r\nb)\tIf the correlation >0.1 I calc double x = Math.pow(corelval, 1.5f);\r\n\r\nc)\tI generate random number ran and I use this attribute if if(ran<x)\r\n\r\n\uf0f0\tMetrics with good correlation will be used more than metrics with bad correlation\r\n\uf0f0\tAll metrics will be used, this will be randomly determined\r\n\uf0f0\tVery bad metrics correlation <0.1 will never be used\r\n\uf0f0\tAll combination of metrics will be generated in this random-process.\r\n");
				}
			}
			{
				button1ok = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1okLData = new FormData();
				button1okLData.left =  new FormAttachment(0, 1000, 268);
				button1okLData.top =  new FormAttachment(0, 1000, 91);
				button1okLData.width = 60;
				button1okLData.height = 25;
				button1ok.setLayoutData(button1okLData);
				button1ok.setText("ok");
				button1ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1okWidgetSelected(evt);
					}
				});
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void button1okWidgetSelected(SelectionEvent evt) {
		System.out.println("button1ok.widgetSelected, event="+evt);
		corelsetting_glob.setAnzSteps(SG.get_zahl(text1anzsteps.getText()));
		corelsetting_glob.setCorrelFaktor(SG.get_float_zahl(text2corefaktor.getText(), 2));
		corelsetting_glob.setMinCorelLevel(SG.get_float_zahl(text1.getText(), 2));
		endflag = 1;
	}

}
