package jHilfsfenster;
import java.awt.FlowLayout;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import jhilf.ColoredTableCellRendererCompare;
import jhilf.JTableTool;

import org.jdesktop.application.Application;

import data.Tradeliste;


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
public class JShowTradelist extends javax.swing.JInternalFrame {
	private JScrollPane jScrollPane1;
	private static Tradeliste trliste_glob=null;
	/**
	* Auto-generated main method to display this 
	* JInternalFrame inside a new JFrame.
	*/
	public static void main(String[] args,Tradeliste tr1) {
		trliste_glob=tr1;
		JFrame frame = new JFrame();
		JShowTradelist inst = new JShowTradelist();
		JDesktopPane jdp = new JDesktopPane();
		jdp.add(inst);
		jdp.setPreferredSize(inst.getPreferredSize());
		frame.setContentPane(jdp);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public JShowTradelist() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(1042, 442));
			this.setBounds(0, 0, 1042, 442);
			FlowLayout thisLayout = new FlowLayout();
			getContentPane().setLayout(thisLayout);
			setVisible(true);
			this.setName("this");
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1);
				jScrollPane1.setPreferredSize(new java.awt.Dimension(1042, 400));

				JTable jTable1=JTableTool.createCompJTable(trliste_glob, 0,null);
    			ColoredTableCellRendererCompare ren = new
        				ColoredTableCellRendererCompare();
        		jTable1.setDefaultRenderer( Object.class, ren );
    			jScrollPane1.setViewportView(jTable1);
			}
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
