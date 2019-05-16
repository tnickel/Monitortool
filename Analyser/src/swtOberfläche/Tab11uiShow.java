package swtOberfläche;

import objects.ThreadDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;


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
public class Tab11uiShow
{
	
	private Group group1;
	private Display display_glob = null;
	private ThreadDbObj tdbo_glob=null;
	private Table table1;

	public void init(Display dis, int seltid)
	{
		//tdbo_glob=tdbo;
		Shell sh = new Shell(dis);
		sh.setLayout(null);
		display_glob = dis;
	

		// Obere Group für die globalen Userinfo
		{
			group1 = new Group(sh, SWT.NONE);
			group1.setLayout(null);
			
			group1.setText(String.valueOf(seltid));
			//group1.setText(tdbo.getThreadname()+ ": tid<"+tdbo.getThreadid()+">");
			group1.setBounds(0, 5, 1036, 465);
			{
				table1 = new Table(group1, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				table1.setBounds(25, 42, 984, 305);
			}
		}
		sh.pack();
		sh.open();
	}
	public void ShowTid(Display dis,int tid)
	{
		display_glob=dis;
		init(dis,tid);
		SwtTabelle.baueTabelleTidBewertung(group1,  table1,
				display_glob, tid);
		//Zeigt für eine Tid die Attribute an
	}
}
