package swtHilfsfenster;

import hiflsklasse.Inf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
public class SwtShowFile
{
		private Text text1_glob;
		private Group group2;
		private String fnam_glob=null;

	public void init(Display dis, String fnam)
		{
			
				fnam_glob=fnam;
				Shell sh = new Shell(dis);
				sh.setLayout(null);
				
				group2 = new Group(sh, SWT.V_SCROLL);
				group2.setLayout(null);
				group2.setText("Info");
				group2.setBounds(12, 12, 1204, 485);
				{
					text1_glob = new Text(group2, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
					text1_glob.setBounds(14, 28, 1168, 437);
					text1_glob.setEditable(false);
				}
				text1_glob.selectAll();
				text1_glob.cut();
				text1_glob.clearSelection();
				Ladefile(text1_glob);
				
			sh.pack();
			sh.setSize(1269, 540);
			sh.open();
			
			while (!sh.isDisposed())
			{
				if (!dis.readAndDispatch())
					dis.sleep();
			}
		}
		private void Ladefile(final Text t)
		{
			String zeile = null;
			Inf inf = new Inf();
			inf.setFilename(fnam_glob);
			int zeilcount=0;
			
			//while ((zeile = inf.readZeile("ISO-8859-1")) != null)
			while ((zeile = inf.readZeile("UTF-8")) != null)
			{
				//am Ende wird kein carrige return angehangen
				if(zeilcount>0)
					t.append("\n");
				
				//System.out.println("zeile=<" + zeile + ">");
				t.append(zeile);
				zeilcount++;
			}
			
			inf.close();
		}
	}

