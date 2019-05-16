package jHilfsfenster;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jhilf.ColoredTableCellRendererCompare;
import jhilf.JTableTool;

import org.jdesktop.application.SingleFrameApplication;

import data.OptimizeResult;
import data.OptimizeResultliste;
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
/**
 * 
 */
public class jCompareTradelist extends SingleFrameApplication
{
	private JPanel topPanel;
	private JButton jButton1;
	private JScrollPane jScrollPane2;
	private JTable jTable1;
	private JScrollPane jScrollPane1;
	private JInternalFrame jInternalFrame2;
	private JInternalFrame jInternalFrame1;
	private JTable jTable2;
	private static Tradeliste tr1_glob = null;
	private static OptimizeResult ores_glob = null;
	private static int gdx_glob = 0;
	private static String title_glob=null;

	@Override
	protected void startup()
	{
		{
			getMainFrame().setSize(1430, 568);
			
			getMainFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			getMainFrame().addWindowListener(new FensterWegAdapter(getMainFrame()));
			getMainFrame().setTitle("compare Tradelist "+title_glob);

			
		}
		topPanel = new JPanel();
		topPanel.setPreferredSize(new java.awt.Dimension(1457, 580));

		topPanel.setLayout(null);
		topPanel.setName("topPanel");
		{
			jInternalFrame1 = new JInternalFrame();
			topPanel.add(jInternalFrame1);
			jInternalFrame1.setBounds(1, 14, 612, 476);
			jInternalFrame1.setName("jInternalFrame1");
			jInternalFrame1.setPreferredSize(new java.awt.Dimension(612, 476));
			jInternalFrame1.setTitle(title_glob);
			{
				// dies ist die linke tabelle
				jScrollPane1 = new JScrollPane();
				jInternalFrame1.getContentPane().add(jScrollPane1,
						BorderLayout.CENTER);
				{
					JTable jTable1 = JTableTool.createCompJTable(tr1_glob,
							gdx_glob, ores_glob);
					ColoredTableCellRendererCompare ren = new ColoredTableCellRendererCompare();
					jTable1.setDefaultRenderer(Object.class, ren);
					jScrollPane1.setViewportView(jTable1);
					jTable1.setName("jTable1");

				}
			}
			jInternalFrame1.updateUI();
		}
		{
			jInternalFrame2 = new JInternalFrame();
			topPanel.add(jInternalFrame2);
			jInternalFrame2.setBounds(667, 27, 643, 480);
			jInternalFrame2.setName("jInternalFrame2");
			{
				jScrollPane2 = new JScrollPane();
				jInternalFrame2.getContentPane().add(jScrollPane2,
						BorderLayout.CENTER);
				jScrollPane2.setPreferredSize(new java.awt.Dimension(551, 448));

				Tradeliste tr2_opti = OptimizeResultliste
						.calcOptimizedTradelist(tr1_glob, ores_glob, null);

				JTable jTable2 = JTableTool.createCompJTable(tr2_opti,
						gdx_glob, null);
				ColoredTableCellRendererCompare ren = new ColoredTableCellRendererCompare();
				jTable2.setDefaultRenderer(Object.class, ren);
				jScrollPane2.setViewportView(jTable2);
			}
			jInternalFrame2.updateUI();
		}
		{
			jButton1 = new JButton();
			topPanel.add(jButton1);
			FlowLayout jButton1Layout = new FlowLayout();
			jButton1.setLayout(jButton1Layout);
			jButton1.setBounds(1290, 393, 110, 96);
			jButton1.setName("jButton1");
			jButton1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButton1ActionPerformed(evt);
				}
			});
		}
		show(topPanel);
	}

	@Override
	protected void shutdown()
	{
		try
		{
			// save to the file so we can open it later
			System.out.println("shutdown");
			System.out.println("nixtun2");
			
		} catch (Exception e)
		{

		}
	}

	public static void main(String[] args, Tradeliste tr1, OptimizeResult ores,
			int gdx,String title)
	{
		tr1_glob = tr1;
		ores_glob = ores;
		gdx_glob = gdx;
		title_glob=title;
		launch(jCompareTradelist.class, args);
	}

	private void jButton1ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton1.actionPerformed, event=" + evt);

		// ok == close button gedrückt
		getMainFrame().dispose();

	}

}
