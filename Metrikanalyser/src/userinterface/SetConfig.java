package userinterface;


import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
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

import data.Metrikglobalconf;
import gui.Dialog;
import hiflsklasse.FileAccess;


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
public class SetConfig extends org.eclipse.swt.widgets.Composite {
	private Text tabellenrootverzeichnis;
	private Label label3;
	private Text minstratportfolio;
	private Button CollectOnlyRobustStrategies;
	private Label label2;
	private Text anzbestlist;
	private Button UseFixedSeed;
	private Label label1;
	private Text PercentInSample;
	private Button button1settabellenrootverz;
	private StyledText styledText1;
	private Button button1;
	private static int exitflag_glob=0;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SetConfig inst = new SetConfig(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		exitflag_glob=0;
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
			if(exitflag_glob==1)
				break;
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();
	}

	public SetConfig(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(708, 419);
			{
				label3 = new Label(this, SWT.NONE);
				FormData label3LData = new FormData();
				label3LData.left =  new FormAttachment(0, 1000, 84);
				label3LData.top =  new FormAttachment(0, 1000, 114);
				label3LData.width = 170;
				label3LData.height = 16;
				label3.setLayoutData(label3LData);
				label3.setText("Minimum Strategies in Portfolio");
			}
			{
				minstratportfolio = new Text(this, SWT.NONE);
				FormData minstratportfolioLData = new FormData();
				minstratportfolioLData.left =  new FormAttachment(0, 1000, 20);
				minstratportfolioLData.top =  new FormAttachment(0, 1000, 114);
				minstratportfolioLData.width = 52;
				minstratportfolioLData.height = 16;
				minstratportfolio.setLayoutData(minstratportfolioLData);
				
				minstratportfolio.setText(String.valueOf(Metrikglobalconf.getMinStratPortfolio()));
			}
			{
				CollectOnlyRobustStrategies = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData CollectOnlyRobustStrategiesLData = new FormData();
				CollectOnlyRobustStrategiesLData.left =  new FormAttachment(0, 1000, 212);
				CollectOnlyRobustStrategiesLData.top =  new FormAttachment(0, 1000, 92);
				CollectOnlyRobustStrategiesLData.width = 180;
				CollectOnlyRobustStrategiesLData.height = 16;
				CollectOnlyRobustStrategies.setLayoutData(CollectOnlyRobustStrategiesLData);
				CollectOnlyRobustStrategies.setText("Collect Only Robust Strategies");

				if(Metrikglobalconf.getCollectOnlyRobustStrategies()==1)
					CollectOnlyRobustStrategies.setSelection(true);
				else
					CollectOnlyRobustStrategies.setSelection(false);
			}
			{
				label2 = new Label(this, SWT.NONE);
				FormData label2LData = new FormData();
				label2LData.left =  new FormAttachment(0, 1000, 84);
				label2LData.top =  new FormAttachment(0, 1000, 92);
				label2LData.width = 79;
				label2LData.height = 17;
				label2.setLayoutData(label2LData);
				label2.setText("max Bestlist");
			}
			{
				anzbestlist = new Text(this, SWT.NONE);
				FormData anzbestlistLData = new FormData();
				anzbestlistLData.left =  new FormAttachment(0, 1000, 20);
				anzbestlistLData.top =  new FormAttachment(0, 1000, 92);
				anzbestlistLData.width = 52;
				anzbestlistLData.height = 16;
				anzbestlist.setLayoutData(anzbestlistLData);
				anzbestlist.setText(String.valueOf(Metrikglobalconf.getmaxBestlist()));
			}
			{
				UseFixedSeed = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData UseFixedSeedLData = new FormData();
				UseFixedSeedLData.left =  new FormAttachment(0, 1000, 212);
				UseFixedSeedLData.top =  new FormAttachment(0, 1000, 70);
				UseFixedSeedLData.width = 212;
				UseFixedSeedLData.height = 16;
				UseFixedSeed.setLayoutData(UseFixedSeedLData);
				UseFixedSeed.setText("UseFixedSeed for Randomgenerator");
				
				if(Metrikglobalconf.getFixedSeedflag()==1)
					UseFixedSeed.setSelection(true);
				else 
					UseFixedSeed.setSelection(false);
			}
			{
				label1 = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 84);
				label1LData.top =  new FormAttachment(0, 1000, 70);
				label1LData.width = 96;
				label1LData.height = 16;
				label1.setLayoutData(label1LData);
				label1.setText("Percent In Sample");
			}
			{
				PercentInSample = new Text(this, SWT.NONE);
				FormData PercentInSampleLData = new FormData();
				PercentInSampleLData.left =  new FormAttachment(0, 1000, 20);
				PercentInSampleLData.top =  new FormAttachment(0, 1000, 70);
				PercentInSampleLData.width = 52;
				PercentInSampleLData.height = 16;
				PercentInSample.setLayoutData(PercentInSampleLData);
				
				int percent=Metrikglobalconf.getPercent();
				if(percent!=0)
				   PercentInSample.setText(String.valueOf(percent));
				else
					 PercentInSample.setText("50");
			}
			{
				styledText1 = new StyledText(this, SWT.H_SCROLL | SWT.V_SCROLL);
				FormData styledText1LData = new FormData();
				styledText1LData.left =  new FormAttachment(0, 1000, 20);
				styledText1LData.top =  new FormAttachment(0, 1000, 164);
				styledText1LData.width = 414;
				styledText1LData.height = 188;
				styledText1.setLayoutData(styledText1LData);
				styledText1.setText("Info:\r\nthe rootdirectory is the directory with the strategies I want to analyse.\r\nFor example:\r\n\r\nset \r\nc:\\Forex\\Metrikanalyser\\Analyse1\r\nas rootdirectory\r\n\r\nThe Analyse1-directory contains\r\n\r\n[_1_directory] \r\ndatabank.csv\r\n\r\n[_2_directory]\r\ndatabank.csv\r\n\r\n[_99_directory]\r\ndatabank.csv (this datafile contains the endtest)\r\n[str__all_endtestfiles] (here are the *.strfiles)\r\n[str__selected_endtestfiles]");
				styledText1.setWordWrap(true);
				styledText1.setDoubleClickEnabled(false);
				styledText1.setDragDetect(false);
				styledText1.setEditable(false);
			}
			{
				button1 = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1LData = new FormData();
				button1LData.left =  new FormAttachment(0, 1000, 549);
				button1LData.top =  new FormAttachment(0, 1000, 361);
				button1LData.width = 147;
				button1LData.height = 27;
				button1.setLayoutData(button1LData);
				button1.setText("ok");
				button1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1WidgetSelected(evt);
					}
				});
			}
			{
				button1settabellenrootverz = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData button1settabellenrootverzLData = new FormData();
				button1settabellenrootverzLData.left =  new FormAttachment(0, 1000, 549);
				button1settabellenrootverzLData.top =  new FormAttachment(0, 1000, 38);
				button1settabellenrootverzLData.width = 147;
				button1settabellenrootverzLData.height = 27;
				button1settabellenrootverz.setLayoutData(button1settabellenrootverzLData);
				button1settabellenrootverz.setText("set table root directory");
				button1settabellenrootverz.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						button1settabellenrootverzWidgetSelected(evt);
					}
				});
			}
			{
				tabellenrootverzeichnis = new Text(this, SWT.NONE);
				FormData tabellenrootverzeichnisLData = new FormData();
				tabellenrootverzeichnisLData.left =  new FormAttachment(0, 1000, 20);
				tabellenrootverzeichnisLData.top =  new FormAttachment(0, 1000, 38);
				tabellenrootverzeichnisLData.width = 517;
				tabellenrootverzeichnisLData.height = 20;
				tabellenrootverzeichnis.setLayoutData(tabellenrootverzeichnisLData);

				
				String rverz=Metrikglobalconf.getFilterpath();
				if(rverz!=null)
					tabellenrootverzeichnis.setText(rverz);
				else
					tabellenrootverzeichnis.setText("please set rootverz");
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void button1WidgetSelected(SelectionEvent evt) {
		System.out.println("button1.widgetSelected, event="+evt);
		//ok und save
		
		String rootverz=tabellenrootverzeichnis.getText();
		
		//check gen resultdirs
		FileAccess.checkDirectory(rootverz+"\\_99_dir\\best100dir");
		FileAccess.checkDirectory(rootverz+"\\_99_dir\\str__all_sq3_endtestfiles");
		FileAccess.checkDirectory(rootverz+"\\_99_dir\\str__all_sq4_endtestfiles");
		FileAccess.checkDirectory(rootverz+"\\_99_dir\\str__selected_sq3_endtestfiles");
		FileAccess.checkDirectory(rootverz+"\\_99_dir\\str__selected_sq4_endtestfiles");
		
		Metrikglobalconf.setFilterpath(rootverz);
		Metrikglobalconf.setPercent(Integer.valueOf(PercentInSample.getText()));
		Metrikglobalconf.setMaxbestlist(Integer.valueOf(anzbestlist.getText()));
		Metrikglobalconf.setMinStratPortfolio(Integer.valueOf(minstratportfolio.getText()));

		if(CollectOnlyRobustStrategies.getSelection()==true)
			Metrikglobalconf.setCollectOnlyRobustStrategies(1);
		else
			Metrikglobalconf.setCollectOnlyRobustStrategies(0);
		
		
		
		if(UseFixedSeed.getSelection()==true)
			Metrikglobalconf.setFixedSeedflag(1);
		else
		 Metrikglobalconf.setFixedSeedflag(0);
		
		Metrikglobalconf.save();
		exitflag_glob=1;
	}
	
	private void button1settabellenrootverzWidgetSelected(SelectionEvent evt) {
		System.out.println("button1settabellenrootverz.widgetSelected, event="+evt);
		//TODO add your code for button1settabellenrootverz.widgetSelected
		
		String dirnam = Dialog
				.DirDialog(Display.getDefault(), Metrikglobalconf.getFilterpath());

		
		if (dirnam != null)
		{
			Metrikglobalconf.setFilterpath(dirnam);
			tabellenrootverzeichnis.setText(dirnam);
		}
	}

}
