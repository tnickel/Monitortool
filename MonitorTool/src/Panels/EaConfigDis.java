package Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.eclipse.swt.widgets.Display;
import org.jdesktop.application.Application;
import org.jfree.chart.JFreeChart;

import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.Ea;
import data.Ealiste;
import data.GlobalVar;
import data.Metaconfig;
import data.Profitliste;
import data.Tradeliste;
import hiflsklasse.Tracer;
import modtools.AutoCreator;
import modtools.FsbPortfolioEa;
import modtools.MetaStarter;
import modtools.Toogler;
import mqlLibs.Eaclass;
import swtHilfsfenster.SwtShowTradeliste;

public class EaConfigDis extends javax.swing.JPanel
{
	private JLabel jLabel1;
	private JButton jButton1;
	private JTextField jTextField1_lots;
	private JButton jButton2copytoautocreator;
	private JTextField autocreatorname;
	private JLabel jLabel6;
	private JTextField jTextField1;
	private JLabel jLabel6locked;
	private JTextField jTextField1index;
	private JTextField jTextField1prof30;
	private JTextField jTextField1prof7;
	private JLabel jLabel5;
	private JLabel jLabel4;
	private JTextField jTextField1profit;
	private JLabel jLabel4profit;
	private JTextField jTextField1pf;
	private JLabel jLabel4pf;
	private JTextField jTextField1tpsl;
	private JButton jButton2showtradelist;
	private JTextField jTextField2pkz1;
	private JLabel jLabel7pkz1;
	private JButton jButton2;
	private JTabbedPane jTabbedPane1;
	private JTextField jTextField1info;
	private JLabel jLabel3info;
	private JTextField jTextField1connection;
	private JButton jButton2deleteEA;
	private JButton jButton2tradecopyonoff;
	private JLabel jLabel1_2;
	
	private int magic_glob = 0;
	private String curency_glob = "";
	private String filedata_glob = "";
	private EaConfigF eaconf_glob = null;
	private int on_glob = 0;
	private String broker_glob = null;
	private Brokerview brokerview_glob = null;
	private JTextField jTextField1channel;
	private JLabel jLabel3channel;
	private JTextField jTextField1magic;
	private JLabel jLabel3magic;
	private JTextField jTextField1currency;
	private JLabel jLabel3currency;
	private JLabel jLabel3broker;
	private JTextField jTextField1broker;
	private Ealiste eal_glob = null;
	private Tableview tv_glob = null;
	private Tradeliste tl_glob = null;
	private Profitliste pl_glob = null;
	private String comment_glob = null;
	private Ea ea_glob = null;
	private int index_glob = 0;
	private JFreeChart chart_glob = null;

	public static void main(String[] args, int magic, String cur, String metarootpath)
	{
		System.exit(99);
	}
	
	public EaConfigDis(int index, int magic, String cur, String filedata, int on, String broker, Brokerview bv,
			Tableview tv, Ealiste eal, Tradeliste tl, String comment, Ea ea, JFreeChart chart, Profitliste pl)
	{
		super();
		magic_glob = magic;
		curency_glob = cur;
		filedata_glob = filedata;
		broker_glob = broker;
		brokerview_glob = bv;
		on_glob = on;
		eal_glob = eal;
		tv_glob = tv;
		tl_glob = tl;
		comment_glob = comment.replace("[tp]", "");
		comment_glob = comment_glob.replace("[sl]", "");
		ea_glob = ea;
		index_glob = index;
		chart_glob = chart;
		pl_glob = pl;
		EaConfigF eaconf = new EaConfigF(filedata + "\\" + magic_glob + ".lot");
		initGUI(eaconf, filedata + "\\" + magic_glob + ".lot");
	}
	
	private void initGUI(EaConfigF eaconf, String lotpath)
	{
		try
		{
			eaconf_glob = eaconf;
			this.setLayout(new BorderLayout(0, 4));
			this.setBackground(new Color(37, 45, 62));
			this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			
			// === TOP: Connection Status ===
			jTextField1connection = new JTextField();
			jTextField1connection.setFont(new Font("Segoe UI", Font.BOLD, 14));
			jTextField1connection.setBackground(new Color(27, 33, 48));
			jTextField1connection.setForeground(Color.WHITE);
			jTextField1connection.setHorizontalAlignment(JTextField.CENTER);
			jTextField1connection.setEditable(false);
			this.add(jTextField1connection, BorderLayout.NORTH);
			
			// === CENTER: Info Fields ===
			JPanel centerPanel = new JPanel(new GridBagLayout());
			centerPanel.setBackground(new Color(37, 45, 62));
			centerPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(new Color(70, 90, 120)),
					"EA Configuration",
					TitledBorder.LEFT, TitledBorder.TOP,
					new Font("Segoe UI", Font.BOLD, 11),
					new Color(140, 165, 200)));
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(2, 6, 2, 6);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.WEST;
			
			jTextField1index = new JTextField(3);
			gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0;
			centerPanel.add(jTextField1index, gbc);
			
			jLabel6locked = new JLabel("Ea is locked");
			jLabel6locked.setForeground(Color.RED);
			jLabel6locked.setFont(new Font("Segoe UI", Font.BOLD, 11));
			gbc.gridx = 1; gbc.gridy = 0;
			centerPanel.add(jLabel6locked, gbc);
			
			jLabel6 = new JLabel("EaType");
			gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
			centerPanel.add(jLabel6, gbc);
			jTextField1 = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.3;
			centerPanel.add(jTextField1, gbc);
			
			jLabel3broker = new JLabel("Broker");
			gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
			centerPanel.add(jLabel3broker, gbc);
			jTextField1broker = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.3;
			centerPanel.add(jTextField1broker, gbc);
			
			jLabel3currency = new JLabel("Currency");
			gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
			centerPanel.add(jLabel3currency, gbc);
			jTextField1currency = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.3;
			centerPanel.add(jTextField1currency, gbc);
			
			jLabel3magic = new JLabel("Magic");
			gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
			centerPanel.add(jLabel3magic, gbc);
			jTextField1magic = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.3;
			centerPanel.add(jTextField1magic, gbc);
			
			jLabel3channel = new JLabel("Channel");
			gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
			centerPanel.add(jLabel3channel, gbc);
			jTextField1channel = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.3;
			centerPanel.add(jTextField1channel, gbc);
			
			jLabel3info = new JLabel("Info");
			gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
			centerPanel.add(jLabel3info, gbc);
			jTextField1info = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = 0.3;
			centerPanel.add(jTextField1info, gbc);
			
			JLabel jLabel3 = new JLabel("TP/SL");
			gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
			centerPanel.add(jLabel3, gbc);
			jTextField1tpsl = new JTextField(12);
			gbc.gridx = 1; gbc.gridy = 7; gbc.weightx = 0.3;
			centerPanel.add(jTextField1tpsl, gbc);
			
			// RIGHT COLUMN: Profit values
			jLabel4 = new JLabel("Prof7");
			gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
			centerPanel.add(jLabel4, gbc);
			jTextField1prof7 = new JTextField(10);
			gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.3;
			centerPanel.add(jTextField1prof7, gbc);
			
			jLabel5 = new JLabel("Prof30");
			gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
			centerPanel.add(jLabel5, gbc);
			jTextField1prof30 = new JTextField(10);
			gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.3;
			centerPanel.add(jTextField1prof30, gbc);
			
			jLabel4profit = new JLabel("Profit All");
			gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
			centerPanel.add(jLabel4profit, gbc);
			jTextField1profit = new JTextField(10);
			gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.3;
			centerPanel.add(jTextField1profit, gbc);
			
			jLabel4pf = new JLabel("Profitfaktor");
			gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
			centerPanel.add(jLabel4pf, gbc);
			jTextField1pf = new JTextField(10);
			gbc.gridx = 3; gbc.gridy = 4; gbc.weightx = 0.3;
			centerPanel.add(jTextField1pf, gbc);
			
			jLabel7pkz1 = new JLabel("pkz1");
			gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0;
			centerPanel.add(jLabel7pkz1, gbc);
			jTextField2pkz1 = new JTextField(10);
			gbc.gridx = 3; gbc.gridy = 5; gbc.weightx = 0.3;
			centerPanel.add(jTextField2pkz1, gbc);
			
			jButton2showtradelist = new JButton("Show Tradelist");
			jButton2showtradelist.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { jButton2showtradelistActionPerformed(evt); }
			});
			gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 0;
			centerPanel.add(jButton2showtradelist, gbc);
			gbc.gridwidth = 1;
			
			this.add(centerPanel, BorderLayout.CENTER);
			
			// === BOTTOM: Action Buttons ===
			JPanel bottomPanel = new JPanel(new GridBagLayout());
			bottomPanel.setBackground(new Color(37, 45, 62));
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
			GridBagConstraints bgbc = new GridBagConstraints();
			bgbc.insets = new Insets(2, 4, 2, 4);
			bgbc.fill = GridBagConstraints.HORIZONTAL;
			
			jTextField1_lots = new JTextField(6);
			jTextField1_lots.setText(eaconf.getLots());
			jTextField1_lots.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { jTextField1_lotsActionPerformed(evt); }
			});
			bgbc.gridx = 0; bgbc.gridy = 0; bgbc.weightx = 0;
			bottomPanel.add(jTextField1_lots, bgbc);
			
			jLabel1 = new JLabel("lots");
			bgbc.gridx = 1; bgbc.gridy = 0;
			bottomPanel.add(jLabel1, bgbc);
			
			jButton2deleteEA = new JButton("Delete EA");
			jButton2deleteEA.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { jButton2deleteEAActionPerformed(evt); }
			});
			bgbc.gridx = 2; bgbc.gridy = 0;
			bottomPanel.add(jButton2deleteEA, bgbc);
			
			autocreatorname = new JTextField(12);
			bgbc.gridx = 3; bgbc.gridy = 0; bgbc.weightx = 0.5;
			bottomPanel.add(autocreatorname, bgbc);
			
			jButton2copytoautocreator = new JButton("Copy to Autocreator");
			jButton2copytoautocreator.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { jButton2copytoautocreatorActionPerformed(evt); }
			});
			bgbc.gridx = 4; bgbc.gridy = 0; bgbc.weightx = 0;
			bottomPanel.add(jButton2copytoautocreator, bgbc);
			
			jButton1 = new JButton("Set Lotsize");
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { jButton1ActionPerformed(evt); }
			});
			bgbc.gridx = 0; bgbc.gridy = 1; bgbc.gridwidth = 2; bgbc.weightx = 0;
			bottomPanel.add(jButton1, bgbc);
			bgbc.gridwidth = 1;
			
			jButton2tradecopyonoff = new JButton("Tradecopy on/off");
			jButton2tradecopyonoff.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { jButton2tradecopyonoffActionPerformed(evt); }
			});
			bgbc.gridx = 2; bgbc.gridy = 1; bgbc.gridwidth = 3; bgbc.weightx = 1.0;
			bottomPanel.add(jButton2tradecopyonoff, bgbc);
			bgbc.gridwidth = 1;
			
			this.add(bottomPanel, BorderLayout.SOUTH);
			
			// Apply Financial Blue theme to all labels
			java.awt.Color labelFg = new Color(200, 210, 225);
			java.awt.Color fieldBg = new Color(232, 236, 240);
			java.awt.Color fieldFg = new Color(20, 25, 35);
			java.awt.Color btnBg = new Color(184, 204, 228);
			java.awt.Color btnFg = new Color(20, 25, 35);
			
			for (java.awt.Component comp : centerPanel.getComponents()) {
				if (comp instanceof JLabel) { comp.setForeground(labelFg); }
				if (comp instanceof JTextField) { comp.setBackground(fieldBg); comp.setForeground(fieldFg); }
				if (comp instanceof JButton) { comp.setBackground(btnBg); comp.setForeground(btnFg); }
			}
			for (java.awt.Component comp : bottomPanel.getComponents()) {
				if (comp instanceof JLabel) { comp.setForeground(labelFg); }
				if (comp instanceof JTextField) { comp.setBackground(fieldBg); comp.setForeground(fieldFg); }
				if (comp instanceof JButton) { comp.setBackground(btnBg); comp.setForeground(btnFg); }
			}
			jLabel6locked.setForeground(new Color(255, 80, 80));
			
			init();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

		private void init()
	{
		Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(broker_glob);
		
		setEaMessage(on_glob);
		jTextField1broker.setText(broker_glob);
		//jTextField1broker.setText("dies ist ein broker");
		jTextField1channel.setText(String.valueOf(meconf.getTradecopymagic()));
		jTextField1magic.setText(String.valueOf(magic_glob));
		jTextField1currency.setText(curency_glob);
		jTextField1info.setText(comment_glob);
		
		jTextField1pf.setText(String.valueOf(tl_glob.calcProfitfactor()));
		int anz = tl_glob.getsize();
		jTextField1profit.setText(String.valueOf(tl_glob.get_tsumx(anz - 1)));
		jTextField1prof7.setText(String.valueOf(tl_glob.get_tsumx(7)));
		jTextField1prof30.setText(String.valueOf(tl_glob.get_tsumx(30)));
		jTextField1index.setText(String.valueOf(index_glob));
		
		if ((magic_glob < 100)&& (brokerview_glob.getAccounttype(broker_glob) !=4))
		{
			jTextField1.setText("RealAccountChannel");
			jTextField1_lots.setEnabled(false);
			jButton1.setEnabled(false);
			jTextField1tpsl.setText("RealAccount");
		} else if ((brokerview_glob.getAccounttype(broker_glob) !=4)&&(FsbPortfolioEa.checkIsPortfolioEa(magic_glob, meconf) == true))
		{
			jTextField1.setText("FSB Portfolio EA");
			jTextField1_lots.setEnabled(false);
			jButton1.setEnabled(false);
			
			// sl-tp setzen
			
			String sltp = FsbPortfolioEa.PortfolioEaGetTpSl(magic_glob, meconf);
			if (sltp != null)
				jTextField1tpsl.setText(sltp);
			else
				jTextField1tpsl.setText("no Ea");
			
			String filename = FsbPortfolioEa.searchEaFilename(Integer.valueOf(Eaclass.calcMagicFsbPortBaseMagic(magic_glob)), meconf);
			if (filename != null)
				jTextField1info.setText(filename);
		} else if((brokerview_glob.getAccounttype(broker_glob) !=4))
		{
			jTextField1.setText("SingleEa");
			jTextField1_lots.setEnabled(true);
			jButton1.setEnabled(true);
			
			// sl-tp beim normalen ea setzen
			jTextField1tpsl.setText("TP=" + ea_glob.getTp() + " SL=" + ea_glob.getSl());
		}
		else if((brokerview_glob.getAccounttype(broker_glob) ==4))
		{
			jTextField1.setText("AutoCreator");
			jTextField1_lots.setEnabled(false);
			
			
			if(GlobalVar.getAutocreatormode()==1)
				jButton2deleteEA.setEnabled(false);
			
			String apath=meconf.getAppdata();
			String bpath=meconf.getAutocreatortestdir();
			if(bpath.contains(apath)==false)
			{
				//falls der Metatrader so konfiguriert ist das er nur Eas anzeigen soll dann sind diese optionen zu disablen
				jButton2deleteEA.setEnabled(false);
				jButton2tradecopyonoff.setEnabled(false);
				
			}
		}
		
		
		
		jTextField1broker.setEditable(false);
		jTextField1channel.setEditable(false);
		jTextField1magic.setEditable(false);
		jTextField1currency.setEditable(false);
		jTextField1info.setEditable(false);
		jTextField1tpsl.setEditable(false);
		jTextField1pf.setEditable(false);
		jTextField1profit.setEditable(false);
		jTextField1prof7.setEditable(false);
		jTextField1prof30.setEditable(false);
		jTextField1index.setEditable(false);
		jTextField1connection.setEditable(false);
		jLabel6locked.setVisible(false);
		jTextField1.setEditable(false);
		
		//hier wird der autocreatorprefix noch reingepackt
		String aucpref=meconf.getAucPrefix();
		if((aucpref!=null)&&(aucpref.length()>0))
			autocreatorname.setText(meconf.getAucPrefix()+"_"+comment_glob);
		else
			autocreatorname.setText(comment_glob);
		
		// if realbroker
		if (brokerview_glob.getAccounttype(broker_glob) == 2)
			setDisabledButtons("is Realbroker");
		
		// if locked ea
		if (brokerview_glob.getMetaconfigByBrokername(broker_glob).getAccountlocked() == 1)
		{
			jButton2deleteEA.setEnabled(false);
			jLabel6locked.setVisible(true);
		}
		
		//if autocreator metatrader
		if (brokerview_glob.getAccounttype(broker_glob) == 4)
		{
			
			autocreatorname.setEnabled(true);
			jButton2copytoautocreator.setEnabled(true);
			jTextField2pkz1.setEnabled(true);
			jLabel7pkz1.setEnabled(true);
			
			String pkz1=AutoCreator.getPkz1(meconf, comment_glob);
			jTextField2pkz1.setText(pkz1);
		}
		else
		{
			
			autocreatorname.setEnabled(false);
			jButton2copytoautocreator.setEnabled(false);
			jTextField2pkz1.setEnabled(false);
			jLabel7pkz1.setEnabled(false);
			
		}
	}
	
	private void setDisabledButtons(String message)
	{
		jTextField1connection.setText(message);
		jButton2deleteEA.setEnabled(false);
		jButton2tradecopyonoff.setEnabled(false);
		jButton1.setEnabled(false);
		
	}
	
	private void jTextField1_lotsActionPerformed(ActionEvent evt)
	{
		System.out.println("jTextField1_lots.actionPerformed, event=" + evt);
		String str = jTextField1_lots.getText();
		System.out.println("lots wurden gesetzt <" + str + ">");
		eaconf_glob.setLots(str);
	}
	
	private void setEaMessage(int status)
	{
		if (status == 1)
		{
			String realacc = brokerview_glob.getConBroker(broker_glob);
			jTextField1connection.setText("*** connected to realaccount <" + realacc + "> ***");
			jTextField1connection.setEditable(false);
			jTextField1connection.setVisible(true);
			
		} else if (status == 0)
		{
			jTextField1connection.setText("...");
			jTextField1connection.setEditable(false);
			jTextField1connection.setVisible(true);
			
		} else
		{ // status deleted
			jTextField1connection.setText("----deleted----");
			jTextField1connection.setEditable(false);
			jTextField1connection.setVisible(true);
		}
		
	}
	
	private void jButton1ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton1.actionPerformed, event=" + evt);
		
		String str = jTextField1_lots.getText();
		if (str.contains("default"))
			return;
		System.out.println("lots wurden gespeichert <" + str + ">");
		str = str.replace(" ", "");
		eaconf_glob.setLots(str);
	}
	
	private void jButton2tradecopyonoffActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton2tradecopyonoff.actionPerformed, event=" + evt);
		
		if (GlobalVar.getMetatraderrunning() == 1)
			MetaStarter.KillAllMetatrader(0);
		
		Toogler tog = new Toogler();
		
		//int brokertype=brokerview_glob.getAccounttype(broker_glob);
		
		tog.ToggleOnOffEa(tl_glob,brokerview_glob, eal_glob, magic_glob, comment_glob, broker_glob,pl_glob);
		
		Ea ea = eal_glob.getEa(magic_glob, broker_glob);
		
		
		Tracer.WriteTrace(20, "EA statuson=" + ea.getOn());
		int status = ea.getOn();
		setEaMessage(status);
	}
	
	private void jButton2deleteEAActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton2deleteEA.actionPerformed, event=" + evt);
		
		if (GlobalVar.getMetatraderrunning() == 1)
			MetaStarter.KillAllMetatrader(0);
			
		//check ea type, autocreator needs a special handling
		Ea ea = eal_glob.getEa(magic_glob, broker_glob);
		//comment=10003_01
				
		Metaconfig meconf=brokerview_glob.getMetaconfigByBrokername(broker_glob);
		
		
		if (ea_glob.getOn() == 1)
		{
			Tracer.WriteTrace(10, "Error: canÂ´t delete EA because it is connected to realaccount");
		}else  
		if(meconf.getAccounttype()==4)
		{
			String new_name=autocreatorname.getText();
			setDisabledButtons("----deleted----");
			if(tv_glob.deleteSingleEaAC(brokerview_glob, tl_glob, broker_glob,magic_glob,comment_glob)==true)
				AutoCreator.delteEaAutoCreatorFiles(meconf,comment_glob);
			setEaMessage(2);
			return;
		}
		else
		{ // delete ea
			setDisabledButtons("----deleted----");
			tv_glob.deleteSingleEa(brokerview_glob, tl_glob, broker_glob, magic_glob);
			setEaMessage(2);
			
		}
		
		
		
	}
	
	private void jButton2copytoautocreatorActionPerformed(ActionEvent evt) {
		System.out.println("jButton2copytoautocreator.actionPerformed, event="+evt);
		//copy to autocreator button klicked
		//TODO add your code for jButton2copytoautocreator.actionPerformed
		Ea ea = eal_glob.getEa(magic_glob, broker_glob);
		System.out.println("found ea");
		//comment=10003_01
				
		Metaconfig meconf=brokerview_glob.getMetaconfigByBrokername(broker_glob);
		String new_name=autocreatorname.getText();
		int anz=(new_name.length())-(new_name.replace("_", "").length());
		
		if(anz>2)
			Tracer.WriteTrace(10, "E:In String <"+new_name+"> maxanz of '_'=2 but I got<"+anz+">");
		else
			AutoCreator.copyToAutoCreator(meconf,comment_glob,new_name,chart_glob,eal_glob);
		
	}
	
	private void jButton2showtradelistActionPerformed(ActionEvent evt) {
		System.out.println("jButton2showtradelist.actionPerformed, event="+evt);
		//TODO add your code for jButton2showtradelist.actionPerformed
		Display dis=new Display();
		
		SwtShowTradeliste st = new SwtShowTradeliste();
		Tradeliste etl = tv_glob.buildTradeliste(String.valueOf(magic_glob), broker_glob,-1,null);
		
		st.init(dis,  etl,tv_glob,broker_glob,String.valueOf(magic_glob));
		dis.close();
	}

}