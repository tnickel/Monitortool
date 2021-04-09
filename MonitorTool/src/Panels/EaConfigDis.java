package Panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jdesktop.application.Application;

import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.Ea;
import data.Ealiste;
import data.Tradeliste;
import hiflsklasse.Tracer;
import modtools.Toogler;

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
public class EaConfigDis extends javax.swing.JPanel {
	//Diese Klasse konfiguriert die lotsize

	
	private JTextField jTextField1_lots;
	private JButton jButton1;
	private JLabel jLabel2;
	private JTextField jTextField1connection;
	private JButton jButton2deleteEA;
	private JButton jButton2tradecopyonoff;
	private JLabel jLabel1;
	
	private int magic_glob=0;
	private String curency_glob="";
	private String filedata_glob="";
	private EaConfigF eaconf_glob=null;
	private int on_glob=0;
	private String broker_glob=null;
	private Brokerview brokerview_glob=null;
	private Ealiste eal_glob=null;
	private Tableview tv_glob=null;
	private Tradeliste tl_glob=null;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args,int magic, String cur,String metarootpath) {
		//main funktion wird nicht benötigt da die nur zum Testen verwendet werden kann
		System.exit(99);
		JFrame frame = new JFrame();
		//frame.getContentPane().add(new EaConfigParamsDisplayX(magic,cur,metarootpath));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public EaConfigDis(int magic,String cur,String filedata,int on,String broker,Brokerview bv,Tableview tv,Ealiste eal,Tradeliste tl) {
		super();
		magic_glob=magic;
		curency_glob=cur;
		filedata_glob=filedata;
		broker_glob=broker;
		brokerview_glob=bv;
		on_glob=on;
		eal_glob=eal;
		tv_glob=tv;
		tl_glob=tl;
		
		
		
		EaConfigF eaconf= new EaConfigF(filedata+"\\"+magic_glob+".lot");
		initGUI(eaconf,filedata+"\\"+magic_glob+".lot");
	}
	
	private void initGUI(EaConfigF eaconf,String lotpath) {
		
		try {
			eaconf_glob=eaconf;
			this.setPreferredSize(new java.awt.Dimension(638, 374));
			this.setLayout(null);
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("lots");
				jLabel1.setBounds(113, 265, 50, 21);
			}
			{
				jButton1 = new JButton();
				this.add(jButton1);
				;
				jButton1.setBounds(20, 296, 143, 28);
				jButton1.setName("jButton1");
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jTextField1_lots = new JTextField();
				this.add(jTextField1_lots);
				jTextField1_lots.setText(eaconf.getLots());
				jTextField1_lots.setBounds(20, 262, 87, 28);
				jTextField1_lots.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jTextField1_lotsActionPerformed(evt);
					}
				});
			}
			{
				jButton2tradecopyonoff = new JButton();
				this.add(jButton2tradecopyonoff);
				jButton2tradecopyonoff.setBounds(181, 294, 442, 32);
				jButton2tradecopyonoff.setName("jButton2tradecopyonoff");
				jButton2tradecopyonoff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2tradecopyonoffActionPerformed(evt);
					}
				});
			}
			{
				jButton2deleteEA = new JButton();
				this.add(jButton2deleteEA);
				jButton2deleteEA.setBounds(181, 257, 91, 32);
				jButton2deleteEA.setName("jButton2deleteEA");
				jButton2deleteEA.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2deleteEAActionPerformed(evt);
					}
				});
			}
			{
				jTextField1connection = new JTextField();
				this.add(jTextField1connection);
				jTextField1connection.setBounds(23, 20, 599, 32);
				jTextField1connection.setName("jTextField1connection");
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setBounds(-3, 326, 626, 16);
				jLabel2.setName("jLabel2");
			}

			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
			init();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void init()
	{
		setEaMessage(on_glob);
		
	}
	private void jTextField1_lotsActionPerformed(ActionEvent evt) {
		System.out.println("jTextField1_lots.actionPerformed, event="+evt);
		String str=jTextField1_lots.getText();
		System.out.println("lots wurden gesetzt <"+str+">");
		eaconf_glob.setLots(str);
	}
	private void setEaMessage(int status)
	{
		if(status==1)
		{
			jTextField1connection.setText("***** connected to realaccount *****");
			jTextField1connection.setEditable(false);
			jTextField1connection.setVisible(true);
			
		}
		else if(status==0)
		{
		
			jTextField1connection.setVisible(false);
		}
		else
		{ //status deleted
			jTextField1connection.setText("----deleted----");
			jTextField1connection.setEditable(false);
			jTextField1connection.setVisible(true);
		}
		
	}

	
	private void jButton1ActionPerformed(ActionEvent evt) {
		System.out.println("jButton1.actionPerformed, event="+evt);
		
		String str=jTextField1_lots.getText();
		if(str.contains("default"))
			return;
		System.out.println("lots wurden gespeichert <"+str+">");
		str=str.replace(" ","");
		eaconf_glob.setLots(str);
	}
	
	private void jButton2tradecopyonoffActionPerformed(ActionEvent evt) {
		System.out.println("jButton2tradecopyonoff.actionPerformed, event="+evt);
		Toogler tog=new Toogler();
		tog.ToggleOnOffEa(brokerview_glob, eal_glob, magic_glob, broker_glob);
		Ea ea=eal_glob.getEa(magic_glob, broker_glob);
		Tracer.WriteTrace(10,"EA statuson="+ea.getOn());
		int status=ea.getOn();
		setEaMessage(status);
	}
	
	private void jButton2deleteEAActionPerformed(ActionEvent evt) {
		System.out.println("jButton2deleteEA.actionPerformed, event="+evt);
		tv_glob.deleteSingleEa(brokerview_glob, tl_glob, broker_glob, magic_glob);
		setEaMessage(2);
	}

}
