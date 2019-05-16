package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;
import mainPackage.GC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import swtViewer.Viewer;

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
public class Tab9uiProgmodul
{
	static private Display display_glob = null;
	static private List list1;
	static private Label label1;
	static private Label x;
	static private Text end;
	static private Text von;
	static private Label label7;
	static private Text maxKaufsumme;
	static private Text text2;
	static private Label label6;
	static private Text MaxKaufanzahl;
	static private Label label5;
	static private Label label4;
	static private Label label3;
	static private Slider slider1;
	static private Text halttage;
	static private ProgressBar progressBar1;
	static private Button button1show;
	static private Label label2;
	static private Text text1;
	static private Text text1start;
	static private Group group1;
	static private Group groupProg_g;
	static private TabFolder folder_glob = null;

	
	static public void init(TabFolder folder, TabItem tab, Display dis)
	{
		//das Prognosemodul stellt die Wochengewinne anhand der eingestellten 
		//Parameter dar
		
		display_glob = dis;
		folder_glob = folder;
		folder.setLayout(new GridLayout());
		// AllgemeinGroup erstellen

		// ProxyGroup erstellen
		groupProg_g = new Group(folder, SWT.NONE);
		groupProg_g.setText("Work");
		groupProg_g.setLayout(null);


		{
			String fnam=GC.rootpath+"\\db\\UserThreadVirtualKonto\\Wochengewinne100.txt";
			
			if(FileAccess.FileAvailable(fnam)==false)
			{
				Tracer.WriteTrace(10, "fnam<"+fnam+"> nicht vorhanden ->zeige keine prognosen");
				return;
			}
			
			Viewer view = new Viewer();
			Table table = new Table(groupProg_g, SWT.MULTI | SWT.BORDER
					| SWT.FULL_SELECTION);
			view
					.viewTableFile(table,
							fnam);
			table.setBounds(32, 28, 1007, 512);
		}
		{
			group1 = new Group(groupProg_g, SWT.NONE);
			group1.setLayout(null);
			group1.setText("group1");
			group1.setBounds(32, 554, 1007, 188);
			{
				text1start = new Text(group1, SWT.NONE);
				text1start.setBounds(8, 22, 72, 17);
			}
			{
				label1 = new Label(group1, SWT.NONE);
				label1.setText("erste Datum");
				label1.setBounds(8, 44, 81, 17);
			}
			{
				text1 = new Text(group1, SWT.NONE);
				text1.setBounds(135, 22, 193, 17);
			}
			{
				label2 = new Label(group1, SWT.NONE);
				label2.setText("letzte Datum");
				label2.setBounds(135, 44, 97, 17);
			}
			{
				button1show = new Button(group1, SWT.PUSH | SWT.CENTER);
				button1show.setText("Show");
				button1show.setBounds(836, 22, 159, 30);
			}
			{
				progressBar1 = new ProgressBar(group1, SWT.NONE);
				progressBar1.setBounds(8, 166, 992, 21);
			}
			{
				halttage = new Text(group1, SWT.NONE);
				halttage.setBounds(8, 67, 70, 20);
			}
			{
				slider1 = new Slider(group1, SWT.NONE);
				slider1.setBounds(135, 67, 198, 20);
			}
			{
				label3 = new Label(group1, SWT.NONE);
				label3.setText("Startdatum");
				label3.setBounds(8, 44, 70, 17);
			}
			{
				label4 = new Label(group1, SWT.NONE);
				label4.setText("Startdatum");
				label4.setBounds(8, 44, 70, 17);
			}
			{
				label5 = new Label(group1, SWT.NONE);
				label5.setText("Halttage");
				label5.setBounds(8, 93, 60, 18);
			}
			{
				MaxKaufanzahl = new Text(group1, SWT.NONE);
				MaxKaufanzahl.setBounds(433, 22, 60, 17);
			}
			{
				label6 = new Label(group1, SWT.NONE);
				label6.setText("MaxKaufanzahl");
				label6.setBounds(505, 22, 101, 17);
			}
			{
				text2 = new Text(group1, SWT.NONE);
				text2.setBounds(433, 22, 60, 17);
			}
			{
				maxKaufsumme = new Text(group1, SWT.NONE);
				maxKaufsumme.setBounds(433, 51, 60, 17);
			}
			{
				label7 = new Label(group1, SWT.NONE);
				label7.setText("MaximaleKaufsumme");
				label7.setBounds(505, 51, 134, 17);
			}
			{
				von = new Text(group1, SWT.NONE);
				von.setBounds(8, 117, 60, 19);
			}
			{
				end = new Text(group1, SWT.NONE);
				end.setBounds(135, 117, 60, 19);
			}
			{
				x = new Label(group1, SWT.NONE);
				x.setText("von        (Zeitraum)         bis");
				x.setBounds(8, 136, 325, 30);
			}
		}

		tab.setControl(groupProg_g);
		groupProg_g.pack();
	}
}
