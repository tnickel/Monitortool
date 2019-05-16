package jhilf;

import hiflsklasse.SG;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import work1.JTableDrawdownComperator;
import work1.JTableIndexComperator;
import work1.JTableNameComperator;
import work1.JTableProfitComperator;
import work1.JTableProfitFaktorAnzComperator;
import work1.JTableProfitfaktorComperator;
import work1.JTableTradesComperator;
import data.IndiGrundfunkt;
import data.IndicatordataListe_0B;
import data.Indidat;
import data.OptimizeResult;
import data.OptimizeResultliste;
import data.Trade;
import data.Tradeliste;

public class JTableTool extends IndiGrundfunkt
{
	// Diese Klasse dient zum bearbeiten von Tabellen

	static public JTable createIndikatorJTable(IndicatordataListe_0B indiliste)
	{
		//in  der indieliste ist ein indikator zeilenweise eingelesen
		//dieser wird nun für die Tabelle aufbereitet !!
		
		//ein indikator element holen (besteht aus x booleans)
		Indidat idat=indiliste.getIndi(0);

		//anzahl der booleans holen
		int anzdat=idat.calcAnzDat();
		//die gesammliste hat listengroesse einträge
		int listengroesse=indiliste.getanzelems();
		
		//hier wird der Tabellentitel aufgebaut
		Vector<String> title = new Vector<String>();
		title.add("i");
		title.add("Date");//date,time
		title.add("OpenPrice");
		
		for(int i=1; i<anzdat; i++)
		{   
			//den spaltennamen setzen
			String indinam=indiliste.calcIndexname(i);
			//die beiden führenden nullen entfernen
			indinam=indinam.substring(2,5);
			title.add(indinam);
		}
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		//in der erstenZeile kommen die indikatornamen
	
		
		//Hier wird die Tabelle zeilenweise gefüllt
		for (int i = 0; i < listengroesse; i++)
		{
			idat=indiliste.getIndi(i);
			Vector<String> rowA = new Vector<String>();

			System.out.println("add indicator line="+i);
			String valueRight = format(String.valueOf(i), '0', 6, true); // erzeugt "00000i"
			rowA.add(valueRight);
			//position datum+uhrzeit
			rowA.add(idat.getDt().toString());
			//den price
			rowA.add(String.valueOf(idat.getPrice()));
			
			//dann die booleans 
			for(int j=0; j<anzdat; j++)
			{
				//den wert eines einzelnen indikators holen
				if(idat.getArrayPos(j)==false)
					rowA.add("0");
				else
					rowA.add("1");
			}
			
			//zur gesammttablle die zeile addieren
			data.add(rowA);
		}

		// Das JTable initialisieren
		JTable table = new JTable(data, title);

		return table;
	}

	static public JTable createCompJTable(Tradeliste tl, int gdx,
			OptimizeResult or)
	{
		// die vergleichstabelle aufbauen

		// wenn marker gesetzt ist dann Zeile grün, wenn nicht dann zeile rot
		// man möchte ja wissen welche Zeilen übernommen werden

		Vector<String> title = new Vector<String>();
		title.add("i");
		title.add("dir");
		title.add("Open");
		title.add("OpenPrice");
		title.add("Close");
		title.add("Closeprice");
		title.add("Profit");
		title.add("Sum");
		title.add("Gdx");
		title.add("Duration");
		title.add("markertime");
		title.add("marker");

		Vector<Vector<String>> data = new Vector<Vector<String>>();

		int[] markerarray = null;
		if (or != null)
			markerarray = or.getMarker();

		int anz = tl.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tl.getelem(i);
			Vector<String> rowA = new Vector<String>();

			
			
			String valueRight = format(String.valueOf(i), '0', 6, true); // erzeugt "00000i"
			
			
			
			rowA.add(valueRight);
			rowA.add(tr.calcDirection());
			rowA.add(tr.getOpentime());
			rowA.add(SG.kuerzeFloatstring(String.valueOf(tr.getOpenprice()), 5));
			rowA.add(tr.getClosetime());
			rowA.add(SG.kuerzeFloatstring(String.valueOf(tr.getCloseprice()), 5));
			rowA.add(SG.kuerzeFloatstring(String.valueOf(tr.getProfit()), 2));
			rowA.add(SG.kuerzeFloatstring(String.valueOf(tl.get_tsumx(i)), 2));
			rowA.add(SG.kuerzeFloatstring(String.valueOf(tl.calc_gdx(i, gdx)),
					2));
			rowA.add(tr.calcDuration());
			rowA.add(tr.getIndikatorcheckdate());
			if (markerarray != null)
			{
				if (markerarray[i] == 1)
					rowA.add("1");
				else if (markerarray[i] == 0)
					rowA.add("0");
				else
					rowA.add("-1");
			}
			data.add(rowA);
		}

		// Das JTable initialisieren
		JTable table = new JTable(data, title);
		return table;
	}

	static public JTable createOptiJTable(OptimizeResultliste ol)
	{
		// die optimized tabelle aufbauen welche sich im FrameWorker befindet

		TableModel model = ModelFrameworkertable();
		JTable table = new JTable(model);

		// Der TableRowSorter wird die Daten des Models sortieren
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
		table.setRowSorter(sorter);

		// ... und der Sorter muss wissen, welche Daten er sortieren muss
		sorter.setModel(model);

		// Spalte x hier die comperatoren definieren
		sorter.setComparator(0, new JTableIndexComperator());
		sorter.setComparator(1, new JTableNameComperator());
		sorter.setComparator(2, new JTableProfitComperator());
		sorter.setComparator(3, new JTableProfitfaktorComperator());
		sorter.setComparator(4, new JTableProfitFaktorAnzComperator());
		sorter.setComparator(5, new JTableDrawdownComperator());
		sorter.setComparator(6, new JTableTradesComperator());

		// die Tablle mit den elementen aufbauen
		int anz = ol.getSize();
		for (int i = 0; i < anz; i++)
		{
			OptimizeResult os = ol.getElem(i);
			String row[] =
			{ 
					String.valueOf(i), os.getResultname(),
					String.valueOf(os.getGewinnsumme()),
					String.valueOf(os.getProfitfaktor()),
					String.valueOf(os.getProfitfaktoranzahl()),
					String.valueOf(os.getDrawdownproz()),
					String.valueOf(os.getAnzSelectTrades()),
					os.getOptalgoname() };
			((DefaultTableModel) model).addRow(row);
		}

		// automatisch nach gewinnen sortieren
		ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();

		return (table);
	}

	private static TableModel ModelFrameworkertable()
	{
		String names[] =
		{ "Index", "Resultname", "Profit",  "Pf", "PF*#","drawdown%", "#t", "OptAlgo" };

		DefaultTableModel model = new DefaultTableModel(names, 0)
		{
			@Override
			public Class<?> getColumnClass(int column)
			{
				switch (column)
				{
				case 0:
					return String.class;
				case 1:
					return Integer.class;
				case 2:
					return String.class;
				default:
					return Object.class;
				}
			}

			@Override
			public boolean isCellEditable(int x, int y)
			{
				return false;
			}
		};
		return model;
	}

	
	
	/*
	 * static public JTable createOptiJTable(OptimizeResultliste ol) {
	 * Vector<String> title = new Vector<String>(); title.add("i");
	 * title.add("Bezeichnung"); title.add("Profit");
	 * 
	 * TableModel model = randomModel(); JTable table = new JTable( model );
	 * 
	 * Vector<Vector<String>> data = new Vector<Vector<String>>();
	 * 
	 * int anz = ol.getSize(); for (int i = 0; i < anz; i++) { OptimizeResult os
	 * = ol.getElem(i); Vector<String> rowA = new Vector<String>();
	 * 
	 * rowA.add(String.valueOf(i)); rowA.add(os.getResultname());
	 * rowA.add(String.valueOf(os.getGewinnsumme()));
	 * 
	 * data.add(rowA); }
	 * 
	 * // Die JTable initialisieren //JTable table = new JTable(data, title);
	 * 
	 * // Der TableRowSorter wird die Daten des Models sortieren
	 * TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
	 * 
	 * // Den Comparator für die 2. Spalte (mit den Points) setzen.
	 * sorter.setComparator( 2, new JTableComperator());
	 * 
	 * // Der Sorter muss dem JTable bekannt sein table.setRowSorter( sorter );
	 * 
	 * 
	 * 
	 * return table; }
	 */
	
}
