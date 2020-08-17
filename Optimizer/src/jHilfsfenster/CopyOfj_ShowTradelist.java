package jHilfsfenster;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import data.Tradeliste;
import jhilf.ColoredTableCellRendererCompare;
import jhilf.JTableTool;


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
public class CopyOfj_ShowTradelist extends SingleFrameApplication {

	    private JPanel topPanel;
	    private JButton jButton1;
	    private JScrollPane jScrollPane1;
	    private JInternalFrame jInternalFrame1;
	    static private Tradeliste tr1_glob=null;
 

	    @Override
	    protected void startup() {
	    	{
		    	getMainFrame().setSize(1430, 568);
	    	}
	        topPanel = new JPanel();
	        topPanel.setPreferredSize(new java.awt.Dimension(500, 300));
	        topPanel.setLayout(null);
	        {
	        	jButton1 = new JButton();
	        	topPanel.add(jButton1);
	        	jButton1.setBounds(1348, 482, 45, 25);
	        	jButton1.setName("jButton1");
	        	jButton1.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent evt) {
	        			jButton1ActionPerformed(evt);
	        		}
	        	});
	        }
	        {
	        	jInternalFrame1 = new JInternalFrame();
	        	topPanel.add(jInternalFrame1);
	        	jInternalFrame1.setBounds(39, 27, 1271, 480);
	        	jInternalFrame1.setName("jInternalFrame1");
	        	{
	        		jScrollPane1 = new JScrollPane();
	        		jInternalFrame1.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
	        		jScrollPane1.setPreferredSize(new java.awt.Dimension(848, 450));
	        		{
	        			JTable jTable1=JTableTool.createCompJTable(tr1_glob,0,null);
	        			ColoredTableCellRendererCompare ren = new
	            				ColoredTableCellRendererCompare();
	            		jTable1.setDefaultRenderer( Object.class, ren );
	        			jScrollPane1.setViewportView(jTable1);
	        			
	        		}
	        	}
	        	jInternalFrame1.updateUI();
	        }
	      
	        show(topPanel);
	    }

	    
	    @Override
	    protected void shutdown() {
	        try {
	            //save to the file so we can open it later
	            System.out.println("shutdown");
	        }
	        catch (Exception e) {
	 
	        }
	    }
	    public static void main(String[] args,Tradeliste tr1) 
	    {
	    	tr1_glob=tr1;
		
	    	//launch(jShowTradelist.class,args);
	    }
	    
	    private void jButton1ActionPerformed(ActionEvent evt) {
	    	System.out.println("jButton1.actionPerformed, event="+evt);
	    	
	    	//ok == close button gedrückt
	    	exit();
	    	
	    }

	}