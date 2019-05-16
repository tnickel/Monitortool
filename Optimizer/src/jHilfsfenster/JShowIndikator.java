package jHilfsfenster;

import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import jhilf.ColoredTableCellRendererIndi;
import jhilf.JTableTool;

import org.jdesktop.application.Application;

import data.Config;
import data.IndicatordataListe_0B;

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
public class JShowIndikator extends javax.swing.JInternalFrame
{
	private JScrollPane jScrollPane1;
	private IndicatordataListe_0B indikatordataliste_glob=null;
	/**
	 * Auto-generated main method to display this JInternalFrame inside a new
	 * JFrame.
	 */
	static public void main( String indikatorfilelist)
	{
		//gesammtliste mit allen indikatoren, es sollte nur einer drinstehen sonst kann es die nachstehnde funktion
		//nicht auswerten
	

		JFrame frame = new JFrame();
		JShowIndikator inst = new JShowIndikator(indikatorfilelist);
		JDesktopPane jdp = new JDesktopPane( );
		jdp.setLayout(new BorderLayout());
		jdp.add(inst);
		jdp.setPreferredSize(inst.getPreferredSize());
		frame.setContentPane(jdp);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void loadIndidata(String indikatorfilelist)
	{
		
		String fnam=indikatorfilelist.replace("[", "");
		fnam=fnam.replace("]", "");
		fnam=Config.getRootdir()+"\\data\\"+fnam;
		
		indikatordataliste_glob = new IndicatordataListe_0B(fnam);
	}
	
	public JShowIndikator( String indikatorfilelist)
	{
		super();
		initGUI(  indikatorfilelist);
		
	
	}

	private void initGUI(String indikatorfilelist)
	{
		try
		{
			//die indikatordaten laden
			loadIndidata( indikatorfilelist);
			
			this.setPreferredSize(new java.awt.Dimension(1024, 842));
			this.setBounds(0, 0, 1042, 842);
		
			setVisible(true);
			this.setName("this");
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1, BorderLayout.CENTER);
				jScrollPane1
						.setPreferredSize(new java.awt.Dimension(1024, 800));
				jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				
				//hier wird die tabelle eingelesen und aufgebaut
				JTable jTable1 = JTableTool.createIndikatorJTable(indikatordataliste_glob
						);
				ColoredTableCellRendererIndi ren = new ColoredTableCellRendererIndi();
				jTable1.setDefaultRenderer(Object.class, ren);
				//Dies ist die Absolute Tabellegrösse
				int hoehe=jTable1.getRowCount()*jTable1.getRowHeight();
				jTable1.setPreferredSize(new java.awt.Dimension(4000,hoehe));

				//autoresize=off weil wir einen grösseren scrollview haben und die tabelle sich sonst
				//dem aktuellen sichfeld automatisch zu klein anpassen würde
				jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

				//bei der Tabelle die ersten spalten die breite setzen
				//100=i, 600=datum, 200=openprice
				resizeTable(jTable1, 100, 600, 200);
				jTable1.updateUI();
				jScrollPane1.setViewportView(jTable1);
				jTable1.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
				jTable1.setName("jTable1");
			}
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(this);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	 public static void resizeTable(final JTable aTable, final int... columnWidth) 
	 {
	        //die JTable muss in einer JScrollPane sein!
	        if(columnWidth.length > aTable.getColumnCount()) throw new IllegalArgumentException();
	        aTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        TableColumnModel tableColumnModel = aTable.getColumnModel();
	        TableColumn tableColumn;
	        for ( int i=0; i<columnWidth.length; i++ ) {
	            tableColumn = tableColumnModel.getColumn( i );
	            tableColumn.setPreferredWidth( columnWidth[i] );
	        }
	 }
}
