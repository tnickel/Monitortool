package swtOberfläche;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import swtViewer.Viewer;


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
public class Tab5showTidtable
{
	private Group group1;
	private Group group2;
	private Group group3;
	private Table table1;
	private Button button1suche;
	private Text text1such;

	public void ShowTidTable(Display dis,String fnam,HashSet<Integer> midmenge)
	{
		Shell sh = new Shell(dis);
		sh.setLayout(null);
	

		// Obere Group für die globalen Userinfo
		{
			group1 = new Group(sh, SWT.NONE);
			group1.setLayout(null);
			
		
			group1.setBounds(-1, 6, 865, 611);
			{
				table1 = new Table(group1, SWT.NONE);
				table1.setBounds(0, -5, 865, 611);
			}
		}
		{
			group2 = new Group(sh, SWT.NONE);
			group2.setLayout(null);
			group2.setText("group2");
			group2.setBounds(864, 6, 227, 611);
			{
				text1such = new Text(group2, SWT.NONE);
				text1such.setBounds(12, 35, 170, 20);
			}
			{
				button1suche = new Button(group2, SWT.PUSH | SWT.CENTER);
				button1suche.setBounds(12, 61, 170, 23);
				button1suche.setText("Suche");
				button1suche.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1sucheWidgetSelected(evt);
					}
				});
			}
		}
		{
			group3 = new Group(sh, SWT.NONE);
			GridLayout group3Layout = new GridLayout();
			group3Layout.makeColumnsEqualWidth = true;
			group3.setLayout(group3Layout);
			group3.setText("group3");
			group3.setBounds(6, 629, 337, 80);
		}
		group2.pack();
		group3.pack();
		Viewer v = new Viewer();
		v.viewTableThreadsMid(table1, fnam, midmenge);
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		
	}
	
	private void button1sucheWidgetSelected(SelectionEvent evt) 
	{
		System.out.println("button1suche.widgetSelected, event="+evt);
		//TODO add your code for button1suche.widgetSelected
		String suchwort=text1such.getText();
		
		SwtTools st = new SwtTools();
		int zeile = st.sucheZeile(table1, suchwort);
		table1.setSelection(zeile);
	}

}
