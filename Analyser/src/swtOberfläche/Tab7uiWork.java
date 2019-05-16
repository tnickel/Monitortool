package swtOberfläche;

import hilfsklasse.Tracer;
import hilfsrepeattask.SammleHappyYuppie;
import mainPackage.GC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import stores.SlideruebersichtDB;
import wartung.Pinksheets;
import wartung.SammleUserLangeOnline;
import wartung.SplittVerzeichniss;
import wartung.TransferWebseiten;


public class Tab7uiWork
{
	static private Display display_glob = null;
	static private Button button2;
	static private ProgressBar pb;
	static private Button button4;
	static private Button button6;
	static private Button button5;
	static private Button button3;
	static private Button button1;
	static private TabFolder folder_glob = null;
	static private Group groupKurse_g = null;

	static public void init(TabFolder folder, TabItem tab, Display dis)
	{
		display_glob = dis;
		folder_glob = folder;

		folder.setLayout(new GridLayout());
		// AllgemeinGroup erstellen

		// ProxyGroup erstellen
		groupKurse_g = new Group(folder, SWT.NONE);
		groupKurse_g.setText("Work");
		groupKurse_g.setLayout(null);
		{
			button1 = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button1.setText("SplitVerzeichnisse");
			button1.setBounds(12, 30, 229, 31);
			button1.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1WidgetSelected(evt);
				}
			});
		}
		{
			button2 = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button2.setText("ExportNeueWebseiten");
			button2.setBounds(12, 73, 229, 31);
			button2.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button2WidgetSelected(evt);
				}
			});
		}
		{
			button3 = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button3.setText("GibSliderPrioklassenAus");
			button3.setBounds(12, 117, 229, 31);
			button3.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button3WidgetSelected(evt);
				}
			});
		}
		{
			pb = new ProgressBar(groupKurse_g, SWT.BORDER);
			pb.setBounds(12, 335, 664, 30);
		}
		{
			button4 = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button4.setText("CalcHappyYupie");
			button4.setBounds(12, 160, 229, 31);
			button4.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) 
				{
					button4WidgetSelected(evt);
				}
			});
		}
		{
			button5 = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button5.setText("SammleUserLangeOnline");
			button5.setBounds(12, 205, 229, 31);
			button5.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button5WidgetSelected(evt);
				}
			});
		}
		{
			button6 = new Button(groupKurse_g, SWT.PUSH | SWT.CENTER);
			button6.setText("SammleInsiderTrades");
			button6.setBounds(12, 248, 229, 31);
			button6.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button6WidgetSelected(evt);
				}
			});
		}

		tab.setControl(groupKurse_g);
		// Inhalt des Group erstellen

		groupKurse_g.pack();
		{
			GridData ConfiglisteLData = new GridData();
			ConfiglisteLData.widthHint = 196;
			ConfiglisteLData.heightHint = 228;
		}
	}

	static private void button1WidgetSelected(SelectionEvent evt)
	{
		// Split Verzeichnisse
		Tracer.WriteTrace(10,
				"Info: Möchten sie die Verzeichnisse wirklich splitten ??");
		SplittVerzeichniss sv = new SplittVerzeichniss();
		sv
				.start_verschiebe_verzeichnisse(GC.rootpath
						+ "\\downloaded\\threads");
		sv.start_verschiebe_namen(GC.rootpath + "\\downloaded\\userhtmlpages");
		sv.start_verschiebe_namen(GC.rootpath + "\\db\\compressedthreads");
		sv.loescheVerzeichnissinhalt(GC.rootpath + "\\db\\Attribute");
	}

	static private void button2WidgetSelected(SelectionEvent evt)
	{
		// Export neue Webseiten
		Tracer.WriteTrace(10,
				"Info: Möchten sie die Webseiten wirklich exportieren ??");
		TransferWebseiten tw = new TransferWebseiten();
		tw.export();
	}

	static private void button3WidgetSelected(SelectionEvent evt)
	{
		SlideruebersichtDB sldb = new SlideruebersichtDB();
		sldb.GibSliderPrioKlassenAus(pb,display_glob);
	}
	
	static private void button4WidgetSelected(SelectionEvent evt) 
	{
		SammleHappyYuppie sh = new SammleHappyYuppie();
		sh.start();
	}
	
	static private void button5WidgetSelected(SelectionEvent evt) 
	{
		//Sammle User lange Online
		SammleUserLangeOnline samonline = new SammleUserLangeOnline();
	}
	
	static private void button6WidgetSelected(SelectionEvent evt) 
	{
		//Sammle Insider Trades
		Pinksheets pi = new Pinksheets();
		pi.DownloadInsiderTrades();
	}
}
