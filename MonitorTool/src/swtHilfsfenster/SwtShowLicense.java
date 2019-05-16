package swtHilfsfenster;
import gui.Mbox;
import hiflsklasse.Inf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;


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
public class SwtShowLicense
{
	
		private Text text1_glob;
		private Group group2;
		private String fnam_glob=null;
		private Button button1;
		private Button button1agree;

	public void init(Display dis, String fnam)
		{
			
				fnam_glob=fnam;
				Shell sh = new Shell(dis,SWT.APPLICATION_MODAL);

				{
					//Register as a resource user - SWTResourceManager will
					//handle the obtaining and disposing of resources
					SWTResourceManager.registerResourceUser(sh);
				}
				
				sh.setLayout(null);
				
				group2 = new Group(sh, SWT.V_SCROLL);
				group2.setLayout(null);
				group2.setText("Info");
				group2.setBounds(12, 12, 1204, 485);
				{
					button1agree = new Button(sh, SWT.PUSH | SWT.CENTER | SWT.BORDER);
					button1agree.setText("I agree the license");
					button1agree.setBounds(596, 515, 620, 30);
					button1agree.setFont(SWTResourceManager.getFont("Segoe UI", 9, 1, false, false));
					button1agree.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							button1agreeWidgetSelected(evt);
						}
					});
				}
				{
					button1 = new Button(sh, SWT.PUSH | SWT.CENTER);
					button1.setText("I don´t agree the license");
					button1.setBounds(19, 515, 565, 30);
					button1.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							button1WidgetSelected(evt);
						}
					});
				}
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
			sh.setSize(1269, 600);
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
		
		private void button1agreeWidgetSelected(SelectionEvent evt) {
			System.out.println("button1agree.widgetSelected, event="+evt);
			//agree license
			Display.getDefault().getActiveShell().dispose();
		}
		
		private void button1WidgetSelected(SelectionEvent evt) {
			System.out.println("button1.widgetSelected, event="+evt);
			//TODO add your code for button1.widgetSelected
			Mbox.Infobox("You don´t agree the License, so you have to deinstall this software !!!");
			System.exit(99);
		}

	}

