package swtViewer;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import mainPackage.GC;
import objects.BoersenblattDbObj;

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

import stores.BoersenBlaetterDB;
import swtOberfläche.SwtTools;

public class ViewBoersenblaetter
{
	private Group group1;
	private Group group2;
	private Group group3;
	private Table table1;
	private Button button1suche;
	private Text text1such;

	public void ShowTable(Display dis,String titel)
	{
		
		Shell sh = new Shell(dis);
		sh.setLayout(null);
	

		// Obere Group für die globalen Userinfo
		{
			group1 = new Group(sh, SWT.NONE);
			group1.setLayout(null);
			group1.setText(titel);
			
		
			group1.setBounds(10, 10, 850, 600);
			{
				table1 = new Table(group1, SWT.NONE);
				table1.setBounds(12, 23, 826, 565);
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
		String fnam=GC.rootpath+"\\tmp\\boerblaetter.db";
		genBoersenblaetterfile(fnam);
		Viewer v = new Viewer();
		v.viewFile(table1, fnam);
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		
	}
	private void genBoersenblaetterfile(String fnam)
	{
		Inf inf= new Inf();
		inf.setFilename(fnam);
		FileAccess.FileDelete(fnam, 0);
		inf.writezeile("*****boerblattname#fname#anzKeywoerter#Keywoerter");
		
		BoersenBlaetterDB bbdb= new BoersenBlaetterDB();
		int anz=bbdb.GetanzObj();
		for(int i=0; i<anz; i++)
		{
			BoersenblattDbObj bbobj= (BoersenblattDbObj)bbdb.GetObjectIDX(i);
			bbobj.readExtension();
			String bbname=bbobj.getBoerblattname();
			String fname=bbobj.getFname();
			int anzkeyw=bbobj.calcKeywordanzahl();
			String ks=bbobj.calcAllKeyString();
			
			inf.writezeile(bbname+"#"+fname+"#"+anzkeyw+"#"+ks);
		}
		inf.close();
		
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
