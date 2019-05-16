package swtOberfläche;

import hilfsklasse.Inf;
import hilfsklasse.Swttool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
public class Tab13ShowTexte
{
	private Group group1;
	private Browser browser1;
	private String keyword_glob=null;
	private Button button1down;
	private Button button1up;
	private Text suchtext;

	public void init(Display dis, String fnam,String keyword)
	{
		keyword_glob=keyword;
		Shell sh = new Shell(dis);
		sh.setLayout(null);

		// Obere Group für die globalen Userinfo
		{
			group1 = new Group(sh, SWT.NONE);
			group1.setLayout(null);

			group1.setText(keyword);
			group1.setBounds(-1, 6, 930, 739);
			{
				browser1 = new Browser(group1, SWT.NONE);
				browser1.setText("browser1");
				browser1.setBounds(21, 22, 717, 693);
			}
			{
				button1down = new Button(group1, SWT.PUSH | SWT.CENTER);
				button1down.setText("down");
				button1down.setBounds(756, 103, 162, 30);
			}
			{
				button1up = new Button(group1, SWT.PUSH | SWT.CENTER);
				button1up.setText("up");
				button1up.setBounds(756, 67, 162, 30);
			}
			{
				suchtext = new Text(group1, SWT.NONE);
				suchtext.setText(keyword);
				suchtext.setBounds(756, 35, 162, 30);
			}

			ladeBrowserText(dis, fnam,keyword);
		}
		sh.pack();
		sh.setSize(959, 745);
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
	}

	private void ladeBrowserText(Display dis, String fnam,String keyword)
	{
		int convhtmlflag=0;
		Inf inf = new Inf();
		inf.setFilename(fnam);

		if(fnam.contains(".txt"))
			convhtmlflag=1;
			
		
		String mem = inf.readMemFile(keyword_glob,convhtmlflag);
		browser1.setText(mem);
		
		
		Swttool.wupdate(dis);
	}
}
