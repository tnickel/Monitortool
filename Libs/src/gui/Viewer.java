package gui;

import hiflsklasse.FileAccess;
import hiflsklasse.GC;
import hiflsklasse.Inf;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class Viewer extends SwtTools
{
	String kopfzeile_glob=null;
	public Viewer()
	{
	}
	public void setKopfzeile(String kopfzeile)
	{
		kopfzeile_glob=kopfzeile;
	}
	
	public void viewTableExtFile(Display dis, String fnam)
	{
		// stellt ein .db-file als tabelle da
		// Baut hierzu ein neues Fenster auf
		// Hierzu wird das Display benötigt

		Shell sh = new Shell(dis);
		sh.setLayout(new FillLayout());
		
		
		 GridLayout gridLayout = new GridLayout();
		 Composite composite = new Composite(sh, SWT.FULL_SELECTION);
		 composite.setLayout(gridLayout);
		 Table table = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		 table.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true,true));
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		String[] titles=null;
		
		if(kopfzeile_glob==null)
		 titles = baueTabellenkopfStringFile(fnam);

		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		baueTabelleFile(table, fnam, titles.length);

		for (int i = 0; i < titles.length; i++)
		{
			table.getColumn(i).pack();
		}
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		//dis.dispose();
	}

	public void viewFile(Table table, String fnam)
	{
		// stellt das file in der tabelle dar

		if (FileAccess.FileAvailable0(fnam) == false)
		{
			Tracer.WriteTrace(10, "File<" + fnam + "> nicht vorhanden");
			return;
		}
		String[] titles=null;
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		

		if(kopfzeile_glob==null)
			 titles = baueTabellenkopfStringFile(fnam);
		else
			titles = this.baueKopfzeileString(kopfzeile_glob);

		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		baueTabelleFile(table, fnam, titles.length);

		for (int i = 0; i < titles.length; i++)
		{
			table.getColumn(i).pack();
		}

	}
	public void viewFileHmap(Table table, String fnam,HashMap<Integer, String> hmap)
	{
		// stellt das file in der tabelle dar

		if (FileAccess.FileAvailable0(fnam) == false)
		{
			Tracer.WriteTrace(10, "File<" + fnam + "> nicht vorhanden");
			return;
		}
		String[] titles=null;
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
//		GridData data = new GridData();
//		data.verticalAlignment = GridData.FILL;
//		data.horizontalAlignment = GridData.FILL;
//		data.grabExcessHorizontalSpace = true;
//		data.grabExcessVerticalSpace = true;
//		table.setLayoutData(data);

		if(kopfzeile_glob==null)
			 titles = baueTabellenkopfStringFile(fnam);
		else
			titles = this.baueKopfzeileString(kopfzeile_glob);

		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		baueTabelleFileHmap(table, fnam, titles.length,hmap);

		for (int i = 0; i < titles.length; i++)
		{
			table.getColumn(i).pack();
		}

	}
	public void viewTableThreadsMid(Display dis, String fnam,
			HashSet<Integer> midmenge)
	{
		// stellt ein .db-file als tabelle dar
		// Baut hierzu ein neues Fenster auf
		// Hierzu wird das Display benötigt
		// Es werden nur die threads mit der passenden mid angezeigt

		Shell sh = new Shell(dis);
		sh.setLayout(new GridLayout());
		Table table = new Table(sh, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);
		String[] titles = baueTabellenkopfStringFile(fnam);

		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		baueTabelleThreadsMid(table, fnam, titles.length, midmenge);

		for (int i = 0; i < titles.length; i++)
		{
			table.getColumn(i).pack();
		}
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		dis.dispose();
	}

	public void viewTableThreadsMid(Table table, String fnam,
			HashSet<Integer> midmenge)
	{
		// stellt ein .db-file als tabelle dar
		// übergeben wurde schon eine passende tabelle

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		/*GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);*/
		String[] titles = baueTabellenkopfStringFile(fnam);

		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		baueTabelleThreadsMid(table, fnam, titles.length, midmenge);

		for (int i = 0; i < titles.length; i++)
		{
			table.getColumn(i).pack();
		}

	}

	public void viewTableFile(Table table, String fnam)
	{
		// stellt ein .db-file als tabelle dar
		// Baut hierzu ein neues Fenster auf
		// Hierzu wird das Display benötigt
		// Table table = new Table(group, SWT.MULTI | SWT.BORDER |
		// SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		/*GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);*/
		String[] titles = baueTabellenkopfStringFile(fnam);

		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		baueTabelleFile(table, fnam, titles.length);
		// table.setBounds(50,50,500,500);
		for (int i = 0; i < titles.length; i++)
		{
			table.getColumn(i).pack();
		}
	}

	public void viewTableKurseFile(Table table, String fnam)
	{
		// füllt eine vorgebene Tabelle mit den
		// Elementen aus dem File
		// Die letzten Elemente binhaltet noch Eintraege
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		/*GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);*/
		String[] titles = baueTabellenkopfStringFile(fnam);

		// Tabellenkopf aufbauen
		for (int i = 0; i < titles.length; i++)
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}

		// Ein weiteres Elemente für den Progressbar
		TableColumn columnVerfuegbarkeit = new TableColumn(table, SWT.NONE);
		columnVerfuegbarkeit.setText("Kursverfügbarkeit");

		// Ein weiters Element für die Kursauswahl
		TableColumn columnKursauswahl = new TableColumn(table, SWT.NONE);
		columnKursauswahl.setText("Kursauswahl");

		// Das pack packt die Info in die Tabelle
		table.getColumn(titles.length).pack();
		table.getColumn(titles.length + 1).pack();

		// Baue die Gesammttabelle aus den Datenfile auf
		baueKursTabelleProgressFile(table, fnam, titles.length);
		for (int i = 0; i < titles.length + 2; i++)
		{
			table.getColumn(i).pack();
		}
	}

	public void viewHtmlExtFile(Display dis, String fnam)
	{
		Browser browser_glob = null;
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String mem = inf.readMemFile();
		Shell sh = new Shell(dis);
		sh.setLayout(new GridLayout());
		try
		{
			browser_glob = new Browser(sh, SWT.H_SCROLL);

		} catch (SWTError e)
		{
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			// display.dispose();
			return;
		}
		browser_glob.setText(mem);
		browser_glob.setBounds(240, 20, 1071, 800);
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		// dis.dispose();
	}

	public String fileRequester(Display dis, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		dialog.setFilterNames(new String[]
		{ "Rang Files" });
		dialog.setFilterExtensions(new String[]
		{ "*.rng" }); // Windows
		// wild
		// cards
		dialog.setFilterPath(dir); // Windows path

		if (mode == SWT.SAVE)
		{
			String fnam = Tools.entferneZeit(Tools.get_aktdatetime_str());
			fnam = fnam.replace(" ", "");
			dialog.setFileName("Rang" + fnam);
			System.out.println("Save to: " + dialog.open());

		} else
			System.out.println("Load from: " + dialog.open());

		
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFileName());
		//dis.dispose();
		System.out.println("fnam=<"+fnamd+">");
		return GC.rootpath+"\\export\\"+fnamd;
	}
	public String fileConfRequester(Display dis, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		dialog.setFilterNames(new String[]
		{ "conf Files" });
		dialog.setFilterExtensions(new String[]
		{ "*.txt" }); // Windows
		// wild
		// cards
		dialog.setFilterPath(dir); // Windows path
		dialog.open();
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFileName());
		//dis.dispose();
		System.out.println("fnam=<"+fnamd+">");
		return dir+"\\"+fnamd;
	}
	public String fileMqldirRequester(Display dis, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		dialog.setFilterNames(new String[]
		{ "mql4" });
		dialog.setFilterExtensions(new String[]
		{ "*.mq4*" }); // Windows
		// wild
		// cards
		dialog.setFilterPath(dir); // Windows path
		dialog.open();
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFilterPath());
		
		return fnamd;
	}
	public String filedirRequester(Display dis, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		
		dialog.setFilterPath(dir); // Windows path
		dialog.open();
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFilterPath());
		
		return fnamd;
	}
	
	public String fileRequester2(Display dis,String message, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		dialog.setText(message);
		dialog.setFilterNames(new String[]{".xml","*.*"});
		dialog.setFilterExtensions(new String[]{"*.*",".xml"}); // Windows
		// wild
		// cards
		dialog.setFilterPath(dir); // Windows path

		

		dialog.open();
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFileName());
		fnamd=dialog.getFilterPath()+"\\"+fnamd;
		return fnamd;
	}
	public String fileRequesterStr(Display dis,String message, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		dialog.setText(message);
		dialog.setFilterNames(new String[]{".str","*.*"});
		dialog.setFilterExtensions(new String[]{"*.*",".str"}); // Windows
		// wild
		// cards
		dialog.setFilterPath(dir); // Windows path

		

		dialog.open();
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFileName());
		fnamd=dialog.getFilterPath()+"\\"+fnamd;
		return fnamd;
	}
	public String fileRequesterPic(Display dis,String message, String dir, int mode)
	{
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, mode);
		dialog.setText(message);
		dialog.setFilterNames(new String[]{".gif",".jpg","*.*"});
		dialog.setFilterExtensions(new String[]{"*.*",".gif",".jpg"}); // Windows
		// wild
		// cards
		dialog.setFilterPath(dir); // Windows path

		

		dialog.open();
		
		// Ergebniss auswerten
		String fnamd = new String(dialog.getFileName());
		fnamd=dialog.getFilterPath()+"\\"+fnamd;
		return fnamd;
	}
}
