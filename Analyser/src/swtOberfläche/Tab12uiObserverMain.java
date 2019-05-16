package swtOberfläche;

import hilfsklasse.Swttool;
import hilfsklasse.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import kurse.KurseDB;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.EventsDB;
import stores.ThreadsDB;
import stores.UeberwachungDB;
import stores.UserDB;

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
public class Tab12uiObserverMain
{
	Display display_glob = null;
	private KurseDB kdb_glob = null;
	private Table table1 = null;
	private Text text1selected = null;

	private Button button1delete = null;
	private Text text1startdate;
	private Table table2;
	private Button button2zeigeKurswerte;
	private Button button1;
	private Button button1new = null;
	private Button button1edit = null;
	private Group group3 = null;
	private Button button1startdate = null;
	private Button button2clear;
	private Button button2setall;
	private Label label1;
	private Group group2 = null;
	private Button button1ueberwachung = null;
	private Button button1ereignisse = null;
	// private UserDB udb_glob = null;
	private AktDB aktdb_glob = null;
	// kursmengeglob sind alle symbole in der liste
	HashMap<String, String> kursmenge_glob = null;
	// selmap glob gibt an ob puscher selektiert oder nicht
	HashMap<String, Boolean> selmap_glob = new HashMap<String, Boolean>();
	// puscherliste ist die liste die unten rechts angezeigt wird
	ArrayList<String> puscherliste_glob = new ArrayList<String>();
	private ThreadsDB tdb_glob = null;
	// hmap: tabellenzeile <-> uebmid
	private HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
	private EventsDB evdb_glob = new EventsDB();
	private UeberwachungDB uebdb_glob = null;
	private Tab12selFilter tab12filter_glob = new Tab12selFilter();
	private Button button2type3;
	private Button button2type2;
	private Button button2type1;
	private Button button2typem;
	private Tab12typefilter tab12typefilter_glob = new Tab12typefilter();

	public void init(TabFolder folder, TabItem tab, Display dis, UserDB udb,
			ThreadsDB tdb, AktDB aktdb, UeberwachungDB ueb, KurseDB kdb)
	{
		// udb_glob = udb;
		tdb_glob = tdb;
		aktdb_glob = aktdb;
		uebdb_glob = ueb;
		kdb_glob = kdb;
		display_glob = dis;

		final Group group1 = new Group(folder, SWT.NONE);
		group1.setText("Thread Auswertung");
		group1.setLayout(null);
		{
			button1ereignisse = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1ereignisse.setText("Show Ereignisse");
			button1ereignisse.setBounds(29, 620, 118, 30);
			button1ereignisse.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1ereignisseWidgetSelected(evt);
				}
			});
		}
		{
			table1 = new Table(group1, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
					| SWT.FULL_SELECTION);
			table1.setBounds(27, 50, 969, 558);
		}
		table1.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				ActionTable1(evt);
			}
		});
		{
			group2 = new Group(group1, SWT.NONE);
			group2.setText("ShowEreignisse");
			group2.setBounds(1019, 43, 232, 155);
			{
				button1startdate = new Button(group2, SWT.CHECK | SWT.LEFT);
				button1startdate.setText("StartDate");
				button1startdate.setBounds(12, 22, 105, 30);
				button1startdate.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1startdateWidgetSelected(evt);
					}
				});
			}
			{
				text1startdate = new Text(group2, SWT.NONE);
				text1startdate.setBounds(117, 22, 97, 22);
				text1startdate.setText(evdb_glob.getNeustesDatum());
			}
		}
		{
			group3 = new Group(group1, SWT.NONE);
			group3.setLayout(null);
			group3.setText("Ueberwachung");
			group3.setBounds(1019, 245, 134, 144);
			{
				button1edit = new Button(group3, SWT.PUSH | SWT.CENTER);
				button1edit.setText("Edit");
				button1edit.setBounds(12, 24, 60, 30);
				button1edit.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1editWidgetSelected(evt);
					}
				});
			}
			{
				button1new = new Button(group3, SWT.PUSH | SWT.CENTER);
				button1new.setText("New");
				button1new.setSize(60, 30);
				button1new.setBounds(12, 66, 60, 30);
				button1new.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1newWidgetSelected(evt);
					}
				});
			}
			{
				button1delete = new Button(group3, SWT.PUSH | SWT.CENTER);
				button1delete.setText("Delete");
				button1delete.setBounds(12, 108, 60, 30);
				button1delete.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						button1deleteWidgetSelected(evt);
					}
				});
			}
		}
		{

			button1ueberwachung = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1ueberwachung.setText("Ueberwachung");
			button1ueberwachung.setBounds(153, 620, 149, 30);
			button1ueberwachung.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1ueberwachungWidgetSelected(evt);
				}
			});
		}
		{
			text1selected = new Text(group1, SWT.NONE);
			text1selected.setBounds(314, 620, 671, 25);
		}
		{
			button1 = new Button(group1, SWT.PUSH | SWT.CENTER);
			button1.setText("CheckEreignisse");
			button1.setBounds(29, 681, 118, 30);
			button1.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1WidgetSelected(evt);
				}
			});
		}
		{
			button2zeigeKurswerte = new Button(group1, SWT.PUSH | SWT.CENTER);
			button2zeigeKurswerte.setText("ZeigeKurswerte");
			button2zeigeKurswerte.setBounds(314, 681, 192, 30);
			button2zeigeKurswerte.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button2zeigeKurswerteWidgetSelected(evt);
				}
			});
		}
		{
			table2 = new Table(group1, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
					| SWT.CHECK);
			table2.setBounds(1012, 444, 235, 269);
			table2.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					table2WidgetSelected(evt);
				}
			});
		}
		{
			label1 = new Label(group1, SWT.NONE);
			label1.setText("(i)");
			label1.setBounds(159, 681, 21, 30);
			label1.setToolTipText("id= eindeutige Id welche in der ueberwachungsid gespeichert wird (hier mid oder >800000)\ntid=tid\nrot=Falls Kurs über der Grenze\tist\nrosa=Kurs ist über der Grenze und Marker gesetzt\nM hinter der id= der Marker ist gesetzt (Was besonderes z.B. investiert)");
		}
		{
			button2setall = new Button(group1, SWT.PUSH | SWT.CENTER);
			button2setall.setText("Set");
			button2setall.setBounds(1014, 721, 31, 18);
			button2setall.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button2setallWidgetSelected(evt);
				}
			});
		}
		{
			button2clear = new Button(group1, SWT.PUSH | SWT.CENTER);
			button2clear.setText("Clear");
			button2clear.setBounds(1051, 721, 36, 18);
			button2clear.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button2clearWidgetSelected(evt);
				}
			});
		}
		{
			button2type1 = new Button(group1, SWT.CHECK | SWT.LEFT);
			button2type1.setText("Type1");
			button2type1.setBounds(789, 662, 84, 19);
		}
		{
			button2type2 = new Button(group1, SWT.CHECK | SWT.LEFT);
			button2type2.setText("Type2");
			button2type2.setBounds(789, 687, 60, 19);
		}
		{
			button2type3 = new Button(group1, SWT.CHECK | SWT.LEFT);
			button2type3.setText("Type3");
			button2type3.setBounds(789, 712, 60, 19);
		}
		button2type1.setSelection(true);
		button2type2.setSelection(true);
		button2type3.setSelection(true);
		{
			button2typem = new Button(group1, SWT.CHECK | SWT.LEFT);
			button2typem.setText("TypeM");
			button2typem.setBounds(789, 737, 72, 21);
		}
		tab12typefilter_glob.setTypes(button2type1.getSelection(), button2type2
				.getSelection(), button2type3.getSelection(),button2typem.getSelection());

		tab.setControl(group1);
		group1.pack();
		group2.pack();
		group3.pack();
	}

	// ################################################################################/
	private void button1ereignisseWidgetSelected(SelectionEvent evt)
	{
		// Show Ereignisse
		System.out.println("button1ereignisse.widgetSelected, event=" + evt);

		group3.setVisible(false);
		group2.setVisible(true);
		button2zeigeKurswerte.setVisible(false);

		// Baut EventTabelle auf und zeigt an
		SwtTabelle.baueTabelleEvents(table1, evdb_glob, uebdb_glob, hmap,
				tab12filter_glob);
		Swttool.wupdate(display_glob);
	}

	private void button1ueberwachungWidgetSelected(SelectionEvent evt)
	{
		// show ueberwachung
		System.out.println("button1config.widgetSelected, event=" + evt);
		group3.setVisible(true);
		group2.setVisible(false);
		button2zeigeKurswerte.setVisible(true);

		ProgressBar pb = null;
		// HashSet<String> symbolmenge=LadeUeberwachungsKurse(kdb_glob,pb);

		// ermittle welche Symbole in der tablle anzegt werden
		HashSet<String> symbolmenge = uebdb_glob.getSymbmenge(1);

		// Erstellt die aktuelle kursmenge (symbol,Datum#kurs)
		// man möchte ja für jedes Symbol den aktuellen kurs wissen
		HashMap<String, String> kursmenge_glob = kdb_glob
				.erstelleKursmenge(symbolmenge);

		//typen setzen
		tab12typefilter_glob.setTypes(button2type1.getSelection(), button2type2
				.getSelection(), button2type3.getSelection(),button2typem.getSelection());
		
		// Ueberwachungstablle darstellen
		SwtTabelle.baueTabelleUeberwachung(display_glob, table1, uebdb_glob,
				hmap, kursmenge_glob, puscherliste_glob, selmap_glob,
				tab12typefilter_glob);

		
		
		// Puschertabelle aufbauen
		SwtTabelle.baueTabellePuschertexte(display_glob, table2,
				puscherliste_glob, selmap_glob);

		Swttool.wupdate(display_glob);
	}

	// **************************************************************************************/
	private void ActionTable1(SelectionEvent evt)
	{
		// Hier wurde eine msg ausgewählt
		System.out.println("table1.widgetSelected, event=" + evt);

		int itemindex = Swttool.holeButtonEventZahl(evt, "TableItem");
		text1selected.setText(String.valueOf(itemindex));
		Swttool.wupdate(display_glob);
	}

	private void button1editWidgetSelected(SelectionEvent evt)
	{
		// Edit
		System.out.println("button1edit.widgetSelected, event=" + evt);

		if (text1selected == null)
			return;

		// holt den index der Zeile
		int index = Integer.valueOf(text1selected.getText());

		// holt eine Zeile aus der Tabelle
		TableItem item = table1.getItem(index);

		// Holt die werte aus der zeile
		String type = item.getText(4);
		String id = item.getText(1);
		String name = item.getText(3);
		String date = item.getText(6);
		String lastload = item.getText(7);
		String filepath = item.getText(13);
		String minval = item.getText(8);
		String maxval = item.getText(9);
		String symbol = item.getText(5);
		String wkn = item.getText(11);
		String isin = item.getText(12);
		String puschertext = item.getText(14);
		String pos = item.getText(0);
		int marker = 0;
		if (pos.contains("M") == true)
			marker = 1;

		Tab12ExtEdit t12 = new Tab12ExtEdit(uebdb_glob, display_glob, type, id,
				name, date, lastload, filepath, minval, maxval, symbol, wkn,
				isin, puschertext, "update", marker);

		//typen setzen
		tab12typefilter_glob.setTypes(button2type1.getSelection(), button2type2
				.getSelection(), button2type3.getSelection(),button2typem.getSelection());
		
		// Tabelle refreshen
		SwtTabelle.baueTabelleUeberwachung(display_glob, table1, uebdb_glob,
				hmap, kursmenge_glob, puscherliste_glob, selmap_glob,
				tab12typefilter_glob);

		Swttool.wupdate(display_glob);
	}

	private void button1newWidgetSelected(SelectionEvent evt)
	{
		// New
		System.out.println("button1new.widgetSelected, event=" + evt);

		Tab12ExtEdit t12 = new Tab12ExtEdit(uebdb_glob, display_glob, "1",
				String.valueOf(uebdb_glob.getFreeID()), "Edit Name", Tools
						.entferneZeit(Tools.get_aktdatetime_str()), "lastload",
				"filepath", "minval", "maxval", "Symbol", "wkn", "isin",
				"Puschertext", "new", 0);

		//typen setzen
		tab12typefilter_glob.setTypes(button2type1.getSelection(), button2type2
				.getSelection(), button2type3.getSelection(),button2typem.getSelection());
		
		// Tabelle refreshen
		SwtTabelle.baueTabelleUeberwachung(display_glob, table1, uebdb_glob,
				hmap, kursmenge_glob, puscherliste_glob, selmap_glob,
				tab12typefilter_glob);
		Swttool.wupdate(display_glob);

	}

	private void button1deleteWidgetSelected(SelectionEvent evt)
	{
		// Delete
		System.out.println("button1delete.widgetSelected, event=" + evt);

		if (text1selected == null)
			return;

		// holt den index der Zeile
		int index = Integer.valueOf(text1selected.getText());

		// holt eine Zeile aus der Tabelle
		TableItem item = table1.getItem(index);

		// Holt die werte aus der zeile
		String id = item.getText(1);
		uebdb_glob.DeleteId(Integer.valueOf(id));

		//typen setzen
		tab12typefilter_glob.setTypes(button2type1.getSelection(), button2type2
				.getSelection(), button2type3.getSelection(),button2typem.getSelection());
		
		// Tabelle refreshen
		SwtTabelle.baueTabelleUeberwachung(display_glob, table1, uebdb_glob,
				hmap, kursmenge_glob, puscherliste_glob, selmap_glob,
				tab12typefilter_glob);
		Swttool.wupdate(display_glob);
	}

	private HashSet<String> LadeUeberwachungsKurse(KurseDB kdb, ProgressBar pb)
	{
		// Hier werden alle Kurse geladen die in der Ueberwachung sind
		// Return: Die Symbolmenge die in der Überwachung ist

		// Type 1 (Kurse + Symbol)
		// symbolmenge aus Überwachung definieren
		HashSet<String> symbolmenge = uebdb_glob.getSymbmenge(1);
		// symbolmenge der kurse laden
		kdb.ladeAlleKurseParallel(aktdb_glob, 0, pb, display_glob, symbolmenge);

		return symbolmenge;
	}

	/**************************************************************/
	private void button1WidgetSelected(SelectionEvent evt)
	{
		// Alle kurse prüfen und ggf. Events auslösen
		System.out.println("button1.widgetSelected, event=" + evt);

		group3.setVisible(false);
		group2.setVisible(false);

		// lade alle Kurse
		ProgressBar pb = null;

		HashSet<String> symbolmenge = LadeUeberwachungsKurse(kdb_glob, pb);

		// check events
		uebdb_glob.checkEvents(evdb_glob, 1, symbolmenge,tdb_glob);
	}

	private void button2zeigeKurswerteWidgetSelected(SelectionEvent evt)
	{
		// Zeige Kurswerte
		System.out
				.println("button2zeigeKurswerte.widgetSelected, event=" + evt);
		if (text1selected == null)
			return;

		// holt den index der Zeile
		String text=text1selected.getText();
		int index = Integer.valueOf(text);

		// holt eine Zeile aus der Tabelle
		TableItem item = table1.getItem(index);
		String symbol = item.getText(5);

		String titel=item.getText(3)+" Symbol="+symbol;
		kdb_glob.zeigeSwtTabelle(display_glob, symbol,titel);
	}

	private void button1startdateWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1startdate.widgetSelected, event=" + evt);
		// TODO add your code for button1startdate.widgetSelected

		if (button1startdate.getSelection() == true)
		{
			tab12filter_glob.setStartdate(text1startdate.getText());
		} else
			tab12filter_glob.setStartdate("");

	}

	private void table2WidgetSelected(SelectionEvent evt)
	{
		// die Checkbox bei der Tabelle wurde gedrückt
		// Also in der Puschertabelle wurde eine checkbox gedrückt
		System.out.println("table2.widgetSelected, event=" + evt);
		// TODO add your code for table2.widgetSelected

		String string = evt.detail == SWT.CHECK ? "Checked" : "Selected";

		String selname = evt.item.toString();
		selname = selname.substring(selname.indexOf("{") + 1);
		selname = selname.replace("}", "");

		Boolean val = false;
		if (selmap_glob.containsKey(selname))
		{
			val = selmap_glob.get(selname);
			selmap_glob.remove(selname);
		}

		// toggle Selektor
		if (val == true)
			selmap_glob.put(selname, false);
		else
			selmap_glob.put(selname, true);
		System.out.println("selname<" + selname + "> selektor<"
				+ selmap_glob.get(selname) + ">");
	}

	private void button2setallWidgetSelected(SelectionEvent evt)
	{
		// button SET
		System.out.println("button2setall.widgetSelected, event=" + evt);

		int anz = puscherliste_glob.size();
		for (int i = 0; i < anz; i++)
		{
			String name = puscherliste_glob.get(i);
			selmap_glob.put(name, true);
		}

		SwtTabelle.baueTabellePuschertexte(display_glob, table2,
				puscherliste_glob, selmap_glob);
	}

	private void button2clearWidgetSelected(SelectionEvent evt)
	{
		// button CLEAR
		System.out.println("button2clear.widgetSelected, event=" + evt); // TODO
		// add
		// your
		// code
		// for
		// button2clear.widgetSelected

		int anz = puscherliste_glob.size();
		for (int i = 0; i < anz; i++)
		{
			String name = puscherliste_glob.get(i);
			selmap_glob.put(name, false);
		}

		SwtTabelle.baueTabellePuschertexte(display_glob, table2,
				puscherliste_glob, selmap_glob);
	}
}
