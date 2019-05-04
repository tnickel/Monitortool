package Panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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

	private JLabel jLabel2;
	private JLabel jLabel3;
	private JComboBox jComboBox1usemm;
	private JComboBox jComboBox1usedfixedmoney;
	private JTextField jTextField1_riskmoney;
	private JTextField jTextField1_maxlots;
	private JLabel jLabel6;
	private JTextField jTextField1_risk;
	private JTextField jTextField1_lots;
	private JLabel jLabel5;
	private JButton jButton1;
	private JLabel jLabel4;
	private JLabel jLabel1;
	
	private int magic_glob=0;
	private String cur_glob="";
	private String metarootpath_glob="";
	private EaConfigF eaconf_glob=null;

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
	
	public EaConfigDis(int magic,String cur,String metarootpath) {
		super();
		magic_glob=magic;
		cur_glob=cur;
		metarootpath_glob=metarootpath;
		
		EaConfigF eaconf= new EaConfigF("D:\\tmp\\p\\"+magic_glob+".lot");
		initGUI(eaconf);
	}
	
	private void initGUI(EaConfigF eaconf) {
		try {
			eaconf_glob=eaconf;
			this.setPreferredSize(new java.awt.Dimension(486, 224));
			this.setLayout(null);
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("lots");
				jLabel1.setBounds(203, 41, 24, 21);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setText("max lots");
				jLabel2.setBounds(203, 90, 56, 21);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("use fixed money");
				jLabel3.setBounds(203, 117, 109, 21);
			}
			{
				jLabel4 = new JLabel();
				this.add(jLabel4);
				jLabel4.setText("risk money");
				jLabel4.setBounds(203, 146, 71, 21);
			}
			{
				jButton1 = new JButton();
				this.add(jButton1);
				jButton1.setText("set values");
				jButton1.setBounds(19, 185, 288, 28);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jLabel5 = new JLabel();
				this.add(jLabel5);
				jLabel5.setText("use MM");
				jLabel5.setBounds(203, 15, 52, 21);
			}
			{
				jTextField1_lots = new JTextField();
				this.add(jTextField1_lots);
				jTextField1_lots.setText(eaconf.getLots());
				jTextField1_lots.setBounds(19, 38, 178, 28);
				jTextField1_lots.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jTextField1_lotsActionPerformed(evt);
					}
				});
			}
			{
				jTextField1_risk = new JTextField();
				this.add(jTextField1_risk);
				jTextField1_risk.setText(eaconf.getRiskinpercent());
				jTextField1_risk.setBounds(19, 64, 178, 28);
				jTextField1_risk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jTextField1_riskActionPerformed(evt);
					}
				});
			}
			{
				jLabel6 = new JLabel();
				this.add(jLabel6);
				jLabel6.setText("risk");
				jLabel6.setBounds(203, 67, 22, 21);
			}
			{
				jTextField1_maxlots = new JTextField();
				this.add(jTextField1_maxlots);
				jTextField1_maxlots.setText(eaconf.getMaximumlots());
				jTextField1_maxlots.setBounds(19, 91, 178, 28);
				jTextField1_maxlots.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jTextField1_maxlotsActionPerformed(evt);
					}
				});
			}
			{
				jTextField1_riskmoney = new JTextField();
				this.add(jTextField1_riskmoney);
				jTextField1_riskmoney.setText(eaconf.getRiskinmoney());
				jTextField1_riskmoney.setBounds(19, 143, 178, 28);
				jTextField1_riskmoney.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jTextField1_riskmoneyActionPerformed(evt);
					}
				});
			}
			{
				ComboBoxModel jComboBox1usedfixedmoneyModel = 
						new DefaultComboBoxModel(
								new String[] { "false", "true" });
				jComboBox1usedfixedmoney = new JComboBox();
				this.add(jComboBox1usedfixedmoney);
				jComboBox1usedfixedmoney.setModel(jComboBox1usedfixedmoneyModel);
				jComboBox1usedfixedmoney.setBounds(19, 118, 178, 28);
				jComboBox1usedfixedmoney.setSelectedItem((String)eaconf.getUsefixedmoney());
				jComboBox1usedfixedmoney.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jComboBox1usedfixedmoneyActionPerformed(evt);
					}
				});
			}
			{
				ComboBoxModel jComboBox1usemmModel = 
						new DefaultComboBoxModel(
								new String[] { "false", "true" });
				jComboBox1usemm = new JComboBox();
				this.add(jComboBox1usemm);
				jComboBox1usemm.setModel(jComboBox1usemmModel);
				jComboBox1usemm.setSelectedItem((String)eaconf.getUsemm());
				jComboBox1usemm.setBounds(19, 11, 178, 28);
				jComboBox1usemm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jComboBox1usemmActionPerformed(evt);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jTextField1_lotsActionPerformed(ActionEvent evt) {
		System.out.println("jTextField1_lots.actionPerformed, event="+evt);
		String str=jTextField1_lots.getText();
		System.out.println("lots wurden gesetzt <"+str+">");
		eaconf_glob.setLots(str);
	}
	
	private void jTextField1_riskActionPerformed(ActionEvent evt) {
		System.out.println("jTextField1_risk.actionPerformed, event="+evt);
		String str=jTextField1_risk.getText();
		System.out.println("risk wurden gesetzt <"+str+">");
		eaconf_glob.setRiskinmoney(str);
	}
	
	private void jTextField1_maxlotsActionPerformed(ActionEvent evt) {
		System.out.println("jTextField1_maxlots.actionPerformed, event="+evt);
		String str=jTextField1_maxlots.getText();
		System.out.println("maxlots wurden gesetzt <"+str+">");
		eaconf_glob.setMaximumlots(str);
	}
	
	private void jTextField1_riskmoneyActionPerformed(ActionEvent evt) {
		System.out.println("jTextField1_riskmoney.actionPerformed, event="+evt);
		String str=jTextField1_riskmoney.getText();
		System.out.println("riskmoney wurden gesetzt <"+str+">");
		eaconf_glob.setRiskinmoney(str);
	}

	

	
	private void jButton1ActionPerformed(ActionEvent evt) {
		System.out.println("jButton1.actionPerformed, event="+evt);
		System.out.println("Der Action Button wurde gedrückt");
		//wir aber hier nicht verwendet da die ereignisse sofort ausgewertet werden
	}
	
	private void jComboBox1usedfixedmoneyActionPerformed(ActionEvent evt) {
		System.out.println("jComboBox1usedfixedmoney.actionPerformed, event="+evt);
		//TODO add your code for jComboBox1usedfixedmoney.actionPerformed
		String str=(String)jComboBox1usedfixedmoney.getSelectedItem();
		eaconf_glob.setUsefixedmoney(str);
	}
	
	private void jComboBox1usemmActionPerformed(ActionEvent evt) {
		System.out.println("jComboBox1usemm.actionPerformed, event="+evt);
		String str=(String)jComboBox1usemm.getSelectedItem();
		eaconf_glob.setUsemm(str);
		
	}
}
