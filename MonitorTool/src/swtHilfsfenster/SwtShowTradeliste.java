package swtHilfsfenster;

import gui.Mbox;
import hiflsklasse.FileAccess;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import StartFrame.Tableview;

import com.cloudgarden.resource.SWTResourceManager;

import data.Profit;
import data.Rootpath;
import data.Tradeliste;


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
public class SwtShowTradeliste
{
	int exitflag=0;
	private Table table1;
	private Button showEA;
	private Display display_glob=null;
	private Profit profelem_glob=null;
	private Label label1;
	private Text text1drawdown;
	private Label label2;
	private Text text1profitfactor;
	private Label eaname;
	private Button showonofflog;
	private Tableview tv_glob=null;
	private Tradeliste etl_glob=null;
	private String broker_glob=null;

	public void init(Display dis,Tradeliste etl, Tableview tv,String broker,String comment)
	{
		broker_glob=broker;
		etl_glob=etl;
		tv_glob=tv;
		display_glob=dis;
		Shell sh = new Shell(dis);

		{
			//Register as a resource user - SWTResourceManager will
			//handle the obtaining and disposing of resources
			SWTResourceManager.registerResourceUser(sh);
		}
		
		sh.setLayout(null);
		
		if(etl_glob==null)
		{
			Mbox.Infobox("no ea selected");
			return;
		}
		String eainfostring=comment;

		sh.pack();
		sh.setSize(1373, 597);
		{
			table1 = new Table(sh, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			table1.setBounds(33, 34, 1306, 446);
			table1.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
		}
		{
			showEA = new Button(sh, SWT.PUSH | SWT.CENTER);
			showEA.setText("Show EA");
			showEA.setBounds(33, 492, 87, 30);
			showEA.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					showEAWidgetSelected(evt);
				}
			});
		}
		{
			showonofflog = new Button(sh, SWT.PUSH | SWT.CENTER);
			showonofflog.setText("Show On/Off Log");
			showonofflog.setBounds(132, 492, 131, 30);
			showonofflog.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					showonofflogWidgetSelected(evt);
				}
			});
		}
		{
			eaname = new Label(sh, SWT.NONE);
			eaname.setText(eainfostring);
			eaname.setBounds(33, 8, 635, 20);
		}
		{
			text1profitfactor = new Text(sh, SWT.NONE);
			text1profitfactor.setText("1");
			text1profitfactor.setBounds(366, 496, 87, 26);
		}
		{
			text1drawdown = new Text(sh, SWT.NONE);
			text1drawdown.setText("2");
			text1drawdown.setBounds(580, 496, 68, 26);
		}
		{
			label2 = new Label(sh, SWT.NONE);
			label2.setText("drawdown%");
			label2.setBounds(654, 496, 116, 26);
		}
		{
			label1 = new Label(sh, SWT.NONE);
			label1.setText("profitfactor");
			label1.setBounds(453, 496, 101, 26);
		}
		
		showEinzelTrades(etl_glob,tv,broker_glob,comment);
		sh.open();

		
		while ((!sh.isDisposed()) && (exitflag == 0))
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		//sh.close();
	}
	private void showEinzelTrades(Tradeliste etl, Tableview tv,String broker,String comment)
	{
	
		//Tradeliste etl = tv_glob.buildTradeliste(magic, broker); dies wurde aus der funktion entfernt
		tv.showEinzelTradeliste(etl,display_glob, table1, null,0,text1profitfactor,text1drawdown);
		
	}
	
	private void showEAWidgetSelected(SelectionEvent evt) {
		//Button Show EA
		System.out.println("showEA.widgetSelected, event="+evt);
		String name=profelem_glob.getBroker();
		int id=profelem_glob.getMagic();
		String comment=tv_glob.getComment(id);
		
		String sourcedir=tv_glob.getSourcedir(name);
		
		//hier den Pfad des infofiles basteln
		String fnam=sourcedir+"\\"+comment+".txt";
		fnam=fnam.replace("[tp]", "");
		fnam=fnam.replace("[sl]", "");
		
		if(FileAccess.FileAvailable(fnam)==false)
			fnam=fnam.replace(".txt", ".mq4");
		
		SwtShowFile sf= new SwtShowFile();
		sf.init(display_glob,fnam);
		
	}
	
	private void showonofflogWidgetSelected(SelectionEvent evt) {
		System.out.println("showonofflog.widgetSelected, event="+evt);
		//show ON/Off -log
		String lognamesuffix=profelem_glob.getBroker()+" "+profelem_glob.getMagic()+".txt";
		String fnam=Rootpath.getRootpath()+"\\log\\"+lognamesuffix;
		SwtShowFile sf= new SwtShowFile();
		sf.init(display_glob,fnam);
	}

}
