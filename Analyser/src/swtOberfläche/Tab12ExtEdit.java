package swtOberfläche;

import hilfsklasse.Tracer;
import objects.BadObjectException;
import objects.UeberwachungDbObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import stores.UeberwachungDB;

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
public class Tab12ExtEdit
{
	private Group group1;
	private Text text1type;
	private Text text1uebmid;
	private Text text1name;
	private Text text1erstelldat;
	private Text text1lastload;
	private Text text1filepath;
	private Text text1minval;
	private Text text1maxval;
	private Text text1symbol;
	private Text text1wkn;
	private Text text1isin;
	private Text text1puschertext;
	private Label label1;
	private ScrolledComposite sc1;
	private Text text1;
	private Text text2;
	private Label label2;
	private Button button1marker;
	private Button button1save;

	UeberwachungDB uebdb_glob;
	String atype_glob = null;

	public Tab12ExtEdit(UeberwachungDB uebdb, Display dis, String type,
			String uebmid, String name, String erstelldat, String lastload,
			String filepath, String minval, String maxval, String symbol,
			String wkn, String isin, String puschertext, String atype,
			int marker)
	{
		uebdb_glob = uebdb;
		atype_glob = atype;
		Color red = dis.getSystemColor(SWT.COLOR_RED);
		Color blue = dis.getSystemColor(SWT.COLOR_DARK_BLUE);
		Shell sh = new Shell(dis);
		sh.setLayout(null);

		// Obere Group für die globalen Userinfo
		group1 = new Group(sh, SWT.NONE);
		group1.setLayout(null);
		group1.setText("Edit");
		group1.setBounds(-5, -10, 1397, 722);

		{
			text1type = new Text(group1, SWT.BORDER);
			text1type.setBounds(32, 44, 225, 24);
			text1type.setForeground(red);
			text1type.setText(type);
		}
		{
			text1uebmid = new Text(group1, SWT.BORDER);
			text1uebmid.setBounds(32, 74, 223, 22);
			text1uebmid.setForeground(red);
			text1uebmid.setText(uebmid);

		}
		{
			text1name = new Text(group1, SWT.BORDER);
			text1name.setBounds(30, 102, 223, 24);
			text1name.setForeground(red);
			text1name.setText(name);
		}
		{
			text1erstelldat = new Text(group1, SWT.BORDER);
			text1erstelldat.setBounds(30, 132, 223, 24);
			text1erstelldat.setText(erstelldat);
		}
		{
			text1lastload = new Text(group1, SWT.BORDER);
			text1lastload.setBounds(30, 162, 223, 24);
			text1lastload.setText(lastload);
		}
		{
			text1filepath = new Text(group1, SWT.BORDER);
			text1filepath.setBounds(32, 194, 223, 22);
			text1filepath.setText(filepath);
		}
		{
			text1minval = new Text(group1, SWT.BORDER);
			text1minval.setBounds(30, 222, 223, 24);
			text1minval.setForeground(red);
			text1minval.setText(minval);
		}
		{
			text1maxval = new Text(group1, SWT.BORDER);
			text1maxval.setBounds(30, 252, 223, 24);
			text1maxval.setForeground(red);
			text1maxval.setText(maxval);
		}
		{
			text1symbol = new Text(group1, SWT.BORDER);
			text1symbol.setBounds(30, 282, 223, 24);
			text1symbol.setForeground(red);
			text1symbol.setText(symbol);
		}
		{
			text1wkn = new Text(group1, SWT.BORDER);
			text1wkn.setBounds(30, 312, 223, 24);
			text1wkn.setText(wkn);
		}
		{
			text1isin = new Text(group1, SWT.BORDER);
			text1isin.setBounds(30, 342, 223, 24);
			text1isin.setText(isin);
		}
		{
			text1puschertext = new Text(group1, SWT.BORDER);
			text1puschertext.setBounds(30, 372, 297, 26);
			text1puschertext.setText(puschertext);
		}

		{
			label1 = new Label(group1, SWT.NONE);
			label1.setText("1(Akt mit kor. mid), 2(Akt ohne mid), 3(Zertifikat)");
			label1.setBounds(271, 46, 308, 24);
		}
		{
			button1save = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1save.setText("Save");
			button1save.setSize(60, 30);
			button1save.setBounds(32, 475, 60, 30);
			button1save.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1saveWidgetSelected(evt);
				}
			});
		}
		{
			button1marker = new Button(group1, SWT.CHECK | SWT.LEFT);
			button1marker.setText("Marker");
			button1marker.setBounds(32, 412, 116, 20);
			if (marker == 1)
				button1marker.setSelection(true);
		}
		{
			label2 = new Label(group1, SWT.NONE);
			label2.setText("(i)");
			label2.setBounds(160, 412, 60, 30);
			label2.setToolTipText("Falls dieser Marker gesetzt wird, dann wird diese Aktie als wichtig angesehen und mit einem M in der Übersicht markiert");
		}
		{
			sc1 = new ScrolledComposite(group1, SWT.H_SCROLL | SWT.V_SCROLL
					| SWT.BORDER);
			GridLayout sc1Layout = new GridLayout();
			sc1Layout.numColumns = 2;

			sc1.setLayout(sc1Layout);
			sc1.setBounds(553, 108, 701, 539);

			
			  sc1.setExpandHorizontal(true); 
			  //sc1.setExpandVertical(true);
			 
		}
		
		
		
		Composite entrycomp = new Composite(sc1, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		entrycomp.setLayoutData(gd);
			
		
	    entrycomp.setSize(600, 600);

		
		
		
		

		

		
		sc1.setContent(entrycomp);
		{
			text1 = new Text(entrycomp, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			text1.setText("text1");
			text1.setSize(500,500);
		}
		
		Button b1 = new Button(entrycomp, SWT.PUSH);
		b1.setText("fixed size button");
		b1.setSize(200,200);
		
		// Zeige den midtext
		Handdata hand = new Handdata("midinfo");
		hand.anzeigeHanddata(text1, uebmid);

		group1.pack();
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}

	}

	private void button1saveWidgetSelected(SelectionEvent evt)
	{
		// Save Button
		System.out.println("button1.widgetDefaultSelected, event=" + evt);

		try
		{
			// hole werte aus maske und speichere
			int type = Integer.valueOf(text1type.getText());
			int uebmid = Integer.valueOf(text1uebmid.getText());
			String name = new String(text1name.getText());
			String erstelldat = new String(text1erstelldat.getText());
			String lastload = new String(text1lastload.getText());
			String filepath = new String(text1filepath.getText());
			float minval = Float.valueOf(text1minval.getText());
			float maxval = Float.valueOf(text1maxval.getText());
			String symbol = new String(text1symbol.getText());
			String wkn = new String(text1wkn.getText());
			String isin = new String(text1isin.getText());
			String puschert = new String(text1puschertext.getText());
			int marker = 0;

			if (button1marker.getSelection() == true)
				marker = 1;
			else
				marker = 0;

			UeberwachungDbObj uebobj;
			String objtext = new String(type + "#" + uebmid + "#" + "0" + "#"
					+ name + "#" + erstelldat + "#" + lastload + "#" + filepath
					+ "#" + minval + "#" + maxval + "#" + symbol + "#" + wkn
					+ "#" + isin + "#" + puschert + "#" + marker);

			uebobj = new UeberwachungDbObj(objtext);

			if (atype_glob.contains("new"))
				uebdb_glob.AddObject(uebobj);
			else if (atype_glob.contains("update"))
				uebdb_glob.UpdateObject(uebobj);
			else
				Tracer.WriteTrace(10, "Internal error unbekannter objekttype<"
						+ atype_glob + ">");

			uebdb_glob.WriteDB();

			// den midtext speichern
			Handdata hand = new Handdata("midinfo");
			hand.speichereHanddata(text1, String.valueOf(text1uebmid.getText()));

		} catch (BadObjectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: eingabefehler");
		}

	}

}
