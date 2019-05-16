package swtDropwindow;

import hilfsklasse.SG;

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

import data.DropGlobalVar;

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
public class EditConfig extends org.eclipse.swt.widgets.Composite
{
	private Text path;
	private Button setmetatraderrootpath;
	private Button okbutton;
	private Button buttonSetdropboxdir;
	private Label label1;
	private Text text1intervall;
	private Button button1dropcontrol;
	private Button setupdatepath;
	private Text updatepath;
	private Button SetFstDir;
	private Text fstdir;
	public Text dropboxdir;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */

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
	public static void showGUI()
	{
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		EditConfig inst = new EditConfig(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
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
				display.sleep();
		}
	}

	public EditConfig(org.eclipse.swt.widgets.Composite parent, int style)
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
			this.setSize(510, 282);
			{
				label1 = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 50);
				label1LData.top =  new FormAttachment(0, 1000, 245);
				label1LData.width = 115;
				label1LData.height = 20;
				label1.setLayoutData(label1LData);
				label1.setText("checkintervall sec");
			}
			{
				text1intervall = new Text(this, SWT.MULTI | SWT.WRAP);
				FormData text1intervallLData = new FormData();
				text1intervallLData.left =  new FormAttachment(0, 1000, 12);
				text1intervallLData.top =  new FormAttachment(0, 1000, 245);
				text1intervallLData.width = 24;
				text1intervallLData.height = 20;
				text1intervall.setLayoutData(text1intervallLData);
				if((DropGlobalVar.getCheckintervall()!=0))
						text1intervall.setText(String.valueOf(DropGlobalVar.getCheckintervall()));
				else
					text1intervall.setText("300");
			}
			{
				button1dropcontrol = new Button(this, SWT.CHECK | SWT.LEFT);
				FormData button1dropcontrolLData = new FormData();
				button1dropcontrolLData.left =  new FormAttachment(0, 1000, 12);
				button1dropcontrolLData.top =  new FormAttachment(0, 1000, 213);
				button1dropcontrolLData.width = 252;
				button1dropcontrolLData.height = 20;
				button1dropcontrol.setLayoutData(button1dropcontrolLData);
				button1dropcontrol.setText("ControlRealAccountsOverDropbox");
				button1dropcontrol.setToolTipText("Warning: For monitoring you don´t need this feature, please leave this off");
			}
			{
				setupdatepath = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData setupdatepathLData = new FormData();
				setupdatepathLData.left = new FormAttachment(0, 1000, 305);
				setupdatepathLData.top = new FormAttachment(0, 1000, 166);
				setupdatepathLData.width = 165;
				setupdatepathLData.height = 20;
				setupdatepath.setLayoutData(setupdatepathLData);
				setupdatepath.setText("SetUpdatepath");
				setupdatepath.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						setupdatepathWidgetSelected(evt);
					}
				});
			}
			{
				FormData updatepathLData = new FormData();
				updatepathLData.left = new FormAttachment(0, 1000, 7);
				updatepathLData.top = new FormAttachment(0, 1000, 166);
				updatepathLData.width = 284;
				updatepathLData.height = 20;
				updatepath = new Text(this, SWT.NONE);
				updatepath.setLayoutData(updatepathLData);
					if (DropGlobalVar.getUpdatedir() != null)
						updatepath.setText(DropGlobalVar.getUpdatedir());
			}
			{
				FormData SetFstDirLData = new FormData();
				SetFstDirLData.left = new FormAttachment(0, 1000, 305);
				SetFstDirLData.top = new FormAttachment(0, 1000, 81);
				SetFstDirLData.width = 165;
				SetFstDirLData.height = 20;
				SetFstDir = new Button(this, SWT.PUSH | SWT.CENTER);
				SetFstDir.setLayoutData(SetFstDirLData);
				SetFstDir.setText("SetFstDir");
				SetFstDir.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						SetFstDirWidgetSelected(evt);
					}
				});
			}
			{
				FormData fstdirLData = new FormData();
				fstdirLData.left = new FormAttachment(0, 1000, 7);
				fstdirLData.top = new FormAttachment(0, 1000, 81);
				fstdirLData.width = 284;
				fstdirLData.height = 20;
				fstdir = new Text(this, SWT.NONE);
				fstdir.setLayoutData(fstdirLData);
				if (DropGlobalVar.getFstrootpath() != null)
					fstdir.setText(DropGlobalVar.getFstrootpath());
			}
			{
				buttonSetdropboxdir = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData buttonSetdropboxdirLData = new FormData();
				buttonSetdropboxdirLData.left = new FormAttachment(0, 1000, 305);
				buttonSetdropboxdirLData.top = new FormAttachment(0, 1000, 55);
				buttonSetdropboxdirLData.width = 165;
				buttonSetdropboxdirLData.height = 20;
				buttonSetdropboxdir.setLayoutData(buttonSetdropboxdirLData);
				buttonSetdropboxdir.setText("SetDropboxdir");
				buttonSetdropboxdir.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						buttonSetdropboxdirWidgetSelected(evt);
					}
				});
			}
			{
				dropboxdir = new Text(this, SWT.NONE);
				FormData dropboxdirLData = new FormData();
				dropboxdirLData.left = new FormAttachment(0, 1000, 7);
				dropboxdirLData.top = new FormAttachment(0, 1000, 55);
				dropboxdirLData.width = 284;
				dropboxdirLData.height = 20;
				dropboxdir.setLayoutData(dropboxdirLData);
				if (DropGlobalVar.getMetarootpath() != null)
					dropboxdir.setText(DropGlobalVar.getDropboxdir());
			}
			{
				okbutton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData okbuttonLData = new FormData();
				okbuttonLData.left = new FormAttachment(0, 1000, 458);
				okbuttonLData.top = new FormAttachment(0, 1000, 240);
				okbuttonLData.width = 28;
				okbuttonLData.height = 30;
				okbutton.setLayoutData(okbuttonLData);
				okbutton.setText("ok");
				okbutton.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						okbuttonWidgetSelected(evt);
					}
				});
			}
			{
				setmetatraderrootpath = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData setmetatraderrootpathLData = new FormData();
				setmetatraderrootpathLData.left = new FormAttachment(0, 1000,
						305);
				setmetatraderrootpathLData.top = new FormAttachment(0, 1000, 29);
				setmetatraderrootpathLData.width = 165;
				setmetatraderrootpathLData.height = 20;
				setmetatraderrootpath.setLayoutData(setmetatraderrootpathLData);
				setmetatraderrootpath.setText("SetMetatraderrootpath");

				setmetatraderrootpath
						.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								setmetatraderrootpathWidgetSelected(evt);
							}
						});
			}
			{
				path = new Text(this, SWT.NONE);
				FormData pathLData = new FormData();
				pathLData.left = new FormAttachment(0, 1000, 7);
				pathLData.top = new FormAttachment(0, 1000, 29);
				pathLData.width = 284;
				pathLData.height = 20;
				path.setLayoutData(pathLData);
				if (DropGlobalVar.getMetarootpath() != null)
					path.setText(DropGlobalVar.getMetarootpath());
			}
			this.layout();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void okbuttonWidgetDefaultSelected(SelectionEvent evt)
	{
		System.out.println("okbutton.widgetDefaultSelected, event=" + evt);
		// TODO add your code for okbutton.widgetDefaultSelected
		System.out.println("hallo");
		Display.getDefault().getActiveShell().dispose();
	}

	private void setmetatraderrootpathWidgetSelected(SelectionEvent evt)
	{

		String fnam = EditconfigWork.setMetatraderpath(path);
		if (fnam != null)
			DropGlobalVar.setmetarootpath(fnam);

		DropGlobalVar.save();

	}

	private void buttonSetdropboxdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("buttonSetdropboxdir.widgetSelected, event=" + evt);
		// set dropboxdir
		String fnam = EditconfigWork.setDropboxdir(dropboxdir);
		if (fnam != null)
			DropGlobalVar.setDropboxdir(fnam);
		DropGlobalVar.save();
	}

	private void setupdatepathWidgetSelected(SelectionEvent evt)
	{
		System.out.println("setupdatepath.widgetSelected, event=" + evt);
		// setze updatepath
		String fnam = EditconfigWork.setUpdatedir(updatepath);
		if (fnam != null)
			DropGlobalVar.setUpdatedir(fnam);
		DropGlobalVar.save();
	}

	private void okbuttonWidgetSelected(SelectionEvent evt)
	{
		System.out.println("okbutton.widgetSelected, event=" + evt);
		// TODO add your code for okbutton.widgetSelected
		DropGlobalVar.setDropcontrol(button1dropcontrol.getSelection());
		DropGlobalVar.setCheckintervall(SG.get_zahl(text1intervall.getText()));
		DropGlobalVar.save();
		Display.getDefault().getActiveShell().dispose();
	}

	private void SetFstDirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("SetFstDir.widgetSelected, event=" + evt);

		String fnam = EditconfigWork.setFstdir(fstdir);
		DropGlobalVar.setFstrootpath(fnam);
		DropGlobalVar.save();
	}

}
