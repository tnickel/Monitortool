package gui;

import hiflsklasse.FileAccess;
import hiflsklasse.GC;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.ToolsException;
import hiflsklasse.Tracer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class SwtTools
{
	private int[] vorzeichenflag = new int[50];

	public SwtTools()
	{
	}

	
	
	protected String[] baueTabellenkopfStringFile(String fnam)
	{
		// Extrahiert aus dem *.db feile das array of strings für den
		// Tabellenkopf
		int lpos = 0;
		// 25 tabellenspalten maximal
		String[] titles = null;

		Inf inf = new Inf();
		inf.setFilename(fnam);

		// suche den beschreibheader in den ersten 5 zeilen
		while (lpos < 5)
		{
			lpos++;
			String line = inf.readZeile();

			// schaue nach ob dies ein beschreibheader ist
			if (line.contains("#") && (line.contains("***")))
			{
				int anz = SG.countZeichen(line, "#");
				titles = new String[anz + 1];
				int pos = 1;

				// extrahiere und bauen den neuen string auf
				while (pos < anz + 2)
				{
					try
					{
						String teilstring = new String(SG.nteilstring(line,
								"#", pos));
						if (teilstring.contains("*"))
							teilstring = teilstring.replace("*", "");

						titles[pos - 1] = new String(teilstring);

						// falls die bescheibung einen Punkt beinhaltet wird
						// eine
						// vorzeichenbetrachtung gemacht
						if (teilstring.contains(".") == true)
							vorzeichenflag[pos - 1] = 1;
						else
							vorzeichenflag[pos - 1] = 0;

					} catch (ToolsException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pos++;
				}
				break;
			}
			
			if (line.contains("Date,Open,") )
			{
				int anz = SG.countZeichen(line, ",");
				titles = new String[anz + 1];
				int pos = 1;

				// extrahiere und bauen den neuen string auf
				while (pos < anz + 2)
				{
					try
					{
						String teilstring = new String(SG.nteilstring(line,
								",", pos));
						
						titles[pos - 1] = new String(teilstring);
					} catch (ToolsException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pos++;
				}
				break;
			}
			
			
		}
		if (lpos == 5)
			Tracer.WriteTrace(10, "Error: kein beschreibheader in file <"
					+ fnam + "> gefunden");

		inf.close();
		return titles;
	}

	protected  String[] baueKopfzeileString(String kopfzeile)
	{
		int lpos = 0;
		// 25 tabellenspalten maximal
		String[] titles = null;
		// schaue nach ob dies ein beschreibheader ist
		if (kopfzeile.contains("#") )
		{
			int anz = SG.countZeichen(kopfzeile, "#");
			titles = new String[anz + 1];
			int pos = 1;

			// extrahiere und bauen den neuen string auf
			while (pos < anz + 2)
			{
				try
				{
					String teilstring = new String(SG.nteilstring(kopfzeile,
							"#", pos));
					if (teilstring.contains("*"))
						teilstring = teilstring.replace("*", "");

					titles[pos - 1] = new String(teilstring);

					// falls die bescheibung einen Punkt beinhaltet wird
					// eine
					// vorzeichenbetrachtung gemacht
					if (teilstring.contains(".") == true)
						vorzeichenflag[pos - 1] = 1;
					else
						vorzeichenflag[pos - 1] = 0;

				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pos++;
			}
		}
		return titles;
	}
	
	protected void baueTabelleFile(Table table, String fnam, int spaltenanzahl)
	{
		Inf inf = new Inf();
		inf.setFilename(fnam);
		int pos = 0, linecounter = 0;
		int trennerflag=0;
		String trenner=null;
		String line = null;
		while ((line = inf.readZeile()) != null)
		{
			// schaue nach ob dies ein beschreibheader ist
			if (line.contains("***"))
				continue;
			if(line.contains("Date,Open,"))
				continue;
			
			if(trennerflag==0)
			{
				int anz1 = SG.countZeichen(line, "#");
				int anz2 = SG.countZeichen(line, ",");

				if((anz1==0) && (anz2==0))
					continue;
				
				trennerflag=1;
				if(anz1>anz2)
					trenner="#";
				else
					trenner=",";
			}
			// Baue eine Tabellenzeile auf
			TableItem item = new TableItem(table, SWT.NONE);
			pos = 0;
			while (pos < spaltenanzahl)
			{
				try
				{
					String teilstring = new String(SG.nteilstring(line, trenner,
							pos + 1));

					item.setText(pos, teilstring);
					if (vorzeichenflag[pos] == 1)
					{
						if (teilstring.contains("x") == false)
						{
							Float val = Float.valueOf(teilstring);
							if (val < 0)
								item.setForeground(Display.getCurrent()
										.getSystemColor(SWT.COLOR_RED));
						}
					}
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pos++;
			}
			linecounter++;
			//System.out.println("linecounter<" + linecounter + ">");
		}
		inf.close();
	}
	
	protected void baueTabelleFileHmap(Table table, String fnam, int spaltenanzahl,HashMap<Integer, String> hmap)
	{
		//hier muss noch die spalte gesucht werden die ein file beinhaltet
		
		int tablelineindex=0;
		Inf inf = new Inf();
		inf.setFilename(fnam);
		int pos = 0, linecounter = 0;
		int trennerflag=0;
		String trenner=null;
		String line = null;
		while ((line = inf.readZeile()) != null)
		{
			// schaue nach ob dies ein beschreibheader ist
			if (line.contains("***"))
				continue;
			if(line.contains("Date,Open,"))
				continue;
			
			if(trennerflag==0)
			{
				int anz1 = SG.countZeichen(line, "#");
				int anz2 = SG.countZeichen(line, ",");

				if((anz1==0) && (anz2==0))
					continue;
				
				trennerflag=1;
				if(anz1>anz2)
					trenner="#";
				else
					trenner=",";
			}
			// Baue eine Tabellenzeile auf
			TableItem item = new TableItem(table, SWT.NONE);
			pos = 0;
			while (pos < spaltenanzahl)
			{
				try
				{
					String teilstring = new String(SG.nteilstring(line, trenner,
							pos + 1));

					item.setText(pos, teilstring);
					if((teilstring.contains(".txt")==true)||(teilstring.contains(".pdf")==true))
					{
						String temp=teilstring.replace(".txt", ".pdf");
						temp=temp.replace("\\text\\", "\\pdf\\");
						hmap.put(tablelineindex, temp);
						tablelineindex++;
					}
						
					if (vorzeichenflag[pos] == 1)
					{
						if (teilstring.contains("x") == false)
						{
							Float val = Float.valueOf(teilstring);
							if (val < 0)
								item.setForeground(Display.getCurrent()
										.getSystemColor(SWT.COLOR_RED));
						}
					}
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pos++;
			}
			linecounter++;
			//System.out.println("linecounter<" + linecounter + ">");
		}
		inf.close();
	}
	
	protected void baueTabelleThreadsMid(Table table, String fnam, int spaltenanzahl,HashSet<Integer> midmenge)
	{
		//Baut die threadsdb tabelle so auf, das nur die threads mit der passenden mid 
		//angezeigt wrden
		Inf inf = new Inf();
		inf.setFilename(fnam);
		int pos = 0, linecounter = 0;
		String line = null;
		while ((line = inf.readZeile()) != null)
		{
			// schaue nach ob dies ein beschreibheader ist
			if (line.contains("***"))
				continue;

			try
			{
				String midstring = new String(SG.nteilstring(line, "#",
						5));
				int mid=Integer.valueOf(midstring);
				if (midmenge.contains(mid)==false)
					continue;
				
			} catch (ToolsException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// Baue eine Tabellenzeile auf
			TableItem item = new TableItem(table, SWT.NONE);
			pos = 0;
			while (pos < spaltenanzahl)
			{
				try
				{
					String teilstring = new String(SG.nteilstring(line, "#",
							pos + 1));

					item.setText(pos, teilstring);
					if (vorzeichenflag[pos] == 1)
					{
						if (teilstring.contains("x") == false)
						{
							Float val = Float.valueOf(teilstring);
							if (val < 0)
								item.setForeground(Display.getCurrent()
										.getSystemColor(SWT.COLOR_RED));
						}
					}
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pos++;
			}
			linecounter++;
			//System.out.println("linecounter<" + linecounter + ">");
		}
		inf.close();
	}
	
	protected void baueTabelleSortFile(Table table, String fnam,
			int spaltenanzahl)
	{
		Inf inf = new Inf();
		inf.setFilename(fnam);
		int pos = 0, linecounter = 0;
		String line = null;

		while ((line = inf.readZeile()) != null)
		{
			// schaue nach ob dies ein beschreibheader ist
			if (line.contains("****"))
				continue;
			// Baue eine Tabellenzeile auf
			TableItem item = new TableItem(table, SWT.NONE);
			pos = 0;
			while (pos < spaltenanzahl)
			{
				try
				{
					String teilstring = new String(SG.nteilstring(line, "#",
							pos + 1));
					item.setText(pos, teilstring);

				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pos++;
			}
			linecounter++;
			//System.out.println("linecounter<" + linecounter + ">");
		}
		inf.close();
	}

	protected void baueKursTabelleProgressFile(Table table, String fnam,
			int spaltenanzahl)
	{
		// Baut die Tabelle mit Progressbar auf
		Inf inf = new Inf();
		inf.setFilename(fnam);
		int pos = 0, linecounter = 0;
		String line = null;
		String symb = null;

		while ((line = inf.readZeile()) != null)
		{
			// schaue nach ob dies ein beschreibheader ist
			if ((line.contains("****"))||(line.contains("*@*INFOSTRING*@*")))
				continue;
			// Baue eine Tabellenzeile auf
			TableItem item = new TableItem(table, SWT.NONE);
			pos = 0;
			while (pos < spaltenanzahl)
			{
				try
				{
					String teilstring = new String(SG.nteilstring(line, "#",
							pos + 1));
					if (pos == 0)
						symb = new String(teilstring);
					item.setText(pos, teilstring);

				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pos++;
			}
			pos++;
			// ProgressBar auf letzter Spalte
			/*
			 * ProgressBar bar = new ProgressBar(table, SWT.NONE);
			 * bar.setMinimum(0); bar.setMaximum(100); bar.setSelection(0);
			 * TableEditor editor = new TableEditor(table);
			 * editor.grabHorizontal = editor.grabVertical = true;
			 * editor.setEditor(bar, item, pos);
			 */

			// baue die Kursverfügbarkeit ein
			item.setText(pos, this.calcKurslaengenString(symb));
			linecounter++;
			//System.out.println("linecounter<" + linecounter + ">");
		}
		inf.close();
	}

	public String calcKurslaengenString(String symb)
	{
		String[] boerfeld =
		{ "F", "", "ST", "OB", "DE" };
		int n = boerfeld.length;
		String file = null;
		String kursinfo = "";

		for (int index = 0; index < n; index++)
		{
			file = GC.rootpath + "\\tmp\\kurse\\" + symb + "_"
					+ boerfeld[index] + ".csv";
			int len = FileAccess.FileLength(file);
			kursinfo = kursinfo
					.concat("<" + boerfeld[index] + ">=" + len + " ");
		}
		return new String(kursinfo);
	}

	public int sucheZeile(Table table, String sw)
	{
		String suchwort = sw.toLowerCase();
		// durchsucht eine Tabelle und gibt die passende Zeile wieder
		int zeilenanzahl = table.getItemCount();
		int spaltenanzahl = table.getColumnCount();

		for (int i = 0; i < zeilenanzahl; i++)
		{
			for (int j = 0; j < spaltenanzahl; j++)
			{
				String text = table.getItem(i).getText(j).toLowerCase();

				System.out.println("überprüfe zeile<" + i + "> spalte<" + j
						+ "> text<" + text + ">");
				if (text.contains((suchwort)))
				{
					System.out.println("gefunden in zeile=" + i + "text<"
							+ text + ">");
					return i;
				}
			}
		}
		return 0;
	}

	// funktion bildet aus einer selektionsliste eine TID-Menge
	public HashSet<Integer> genSelektTidMenge(boolean[] uplliste, Table table,
			int index)
	{
		// uplliste: hier steht drin welche zeile der Tabelle aktiv ist
		// index: aus dieser spalte der tabelle wird die tid geholt
		HashSet<Integer> tidliste = new HashSet<Integer>();

		int zeilenanzahl = table.getItemCount();

		for (int i = 0; i < zeilenanzahl; i++)
		{
			// falls diese Zeile aktiv ist
			if (uplliste[i] == true)
			{
				String text = table.getItem(i).getText(index).toLowerCase();
				// die tid der zeile ermitteln
				int tid = Integer.valueOf(text);
				tidliste.add(tid);
			}
		}

		return tidliste;
	}

	static public void fuelleBrowser(Browser browser, String fnam)
	{
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String mem = inf.readMemFile();
		browser.setText(mem);
	}

	

}
