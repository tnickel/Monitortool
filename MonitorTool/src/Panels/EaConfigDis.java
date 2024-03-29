package Panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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
public class EaConfigDis extends javax.swing.JPanel
{
	
	private static final long serialVersionUID = 1L;
	private JTextField jTextField1_lots;
	private JButton jButton1;
	private JLabel jLabel2;
	private JLabel jLabel3;
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
	private JLabel jLabel1;
	
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
	private Profitliste pl_glob=null;
	private String comment_glob = null;
	private Ea ea_glob = null;
	private int index_glob = 0;
	private JFreeChart chart_glob=null;
	
	
	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public static void main(String[] args, int magic, String cur, String metarootpath)
	{
		// main funktion wird nicht ben�tigt da die nur zum Testen verwendet werden kann
		System.exit(99);
		JFrame frame = new JFrame();
		// frame.getContentPane().add(new
		// EaConfigParamsDisplayX(magic,cur,metarootpath));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public EaConfigDis(int index, int magic, String cur, String filedata, int on, String broker, Brokerview bv,
			Tableview tv, Ealiste eal, Tradeliste tl, String comment, Ea ea,JFreeChart chart,Profitliste pl)
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
		comment_glob= comment_glob.replace("[sl]", "");
		ea_glob = ea;
		index_glob = index;
		chart_glob=chart;
		pl_glob=pl;
	
		
		EaConfigF eaconf = new EaConfigF(filedata + "\\" + magic_glob + ".lot");
		initGUI(eaconf, filedata + "\\" + magic_glob + ".lot");
	}
	
	private void initGUI(EaConfigF eaconf, String lotpath)
	{
		
		try
		{
			eaconf_glob = eaconf;
			this.setPreferredSize(new java.awt.Dimension(899, 354));
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
					public void actionPerformed(ActionEvent evt)
					{
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
					public void actionPerformed(ActionEvent evt)
					{
						jTextField1_lotsActionPerformed(evt);
					}
				});
			}
			{
				jButton2tradecopyonoff = new JButton();
				this.add(jButton2tradecopyonoff);
				jButton2tradecopyonoff.setBounds(181, 294, 652, 32);
				jButton2tradecopyonoff.setName("jButton2tradecopyonoff");
				jButton2tradecopyonoff.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt)
					{
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
					public void actionPerformed(ActionEvent evt)
					{
						jButton2deleteEAActionPerformed(evt);
					}
				});
			}
			{
				jTextField1connection = new JTextField();
				this.add(jTextField1connection);
				jTextField1connection.setBounds(23, 16, 810, 32);
				jTextField1connection.setName("jTextField1connection");
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				jLabel2.setBounds(-3, 326, 860, 16);
				jLabel2.setName("jLabel2");
			}
			{
				jTextField1broker = new JTextField();
				this.add(jTextField1broker);
				jTextField1broker.setBounds(86, 100, 220, 21);
				jTextField1broker.setName("jTextField1broker");
				
			}
			{
				jLabel3broker = new JLabel();
				this.add(jLabel3broker);
				jLabel3broker.setBounds(23, 100, 101, 21);
				jLabel3broker.setName("jLabel3broker");
			}
			{
				jLabel3currency = new JLabel();
				this.add(jLabel3currency);
				jLabel3currency.setBounds(23, 127, 52, 16);
				jLabel3currency.setName("jLabel3currency");
			}
			{
				jTextField1currency = new JTextField();
				this.add(jTextField1currency);
				jTextField1currency.setBounds(87, 125, 219, 20);
				jTextField1currency.setName("jTextField1currency");
			}
			{
				jLabel3magic = new JLabel();
				this.add(jLabel3magic);
				jLabel3magic.setBounds(23, 149, 35, 16);
				jLabel3magic.setName("jLabel3magic");
			}
			{
				jTextField1magic = new JTextField();
				this.add(jTextField1magic);
				jTextField1magic.setBounds(87, 148, 219, 18);
				jTextField1magic.setName("jTextField1magic");
			}
			{
				jLabel3channel = new JLabel();
				this.add(jLabel3channel);
				jLabel3channel.setBounds(23, 171, 46, 16);
				jLabel3channel.setName("jLabel3channel");
			}
			{
				jTextField1channel = new JTextField();
				this.add(jTextField1channel);
				jTextField1channel.setBounds(87, 170, 219, 18);
				jTextField1channel.setName("jTextField1channel");
			}
			{
				jLabel3info = new JLabel();
				this.add(jLabel3info);
				jLabel3info.setBounds(23, 193, 21, 16);
				jLabel3info.setName("jLabel3info");
			}
			{
				jTextField1info = new JTextField();
				this.add(jTextField1info);
				jTextField1info.setBounds(87, 191, 219, 20);
				jTextField1info.setName("jTextField1info");
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setBounds(23, 215, 33, 16);
				jLabel3.setName("jLabel3");
			}
			{
				jTextField1tpsl = new JTextField();
				this.add(jTextField1tpsl);
				jTextField1tpsl.setBounds(87, 213, 219, 20);
				jTextField1tpsl.setName("jTextField1tpsl");
			}
			{
				jLabel4pf = new JLabel();
				this.add(jLabel4pf);
				jLabel4pf.setBounds(318, 152, 65, 16);
				jLabel4pf.setName("jLabel4pf");
			}
			{
				jTextField1pf = new JTextField();
				this.add(jTextField1pf);
				jTextField1pf.setBounds(401, 152, 77, 20);
				jTextField1pf.setName("jTextField1pf");
			}
			{
				jLabel4profit = new JLabel();
				this.add(jLabel4profit);
				jLabel4profit.setBounds(319, 127, 64, 16);
				jLabel4profit.setName("jLabel4profit");
			}
			{
				jTextField1profit = new JTextField();
				this.add(jTextField1profit);
				jTextField1profit.setBounds(401, 125, 77, 20);
				jTextField1profit.setName("jTextField1profit");
			}
			{
				jLabel4 = new JLabel();
				this.add(jLabel4);
				jLabel4.setBounds(319, 79, 31, 16);
				jLabel4.setName("jLabel4");
			}
			{
				jLabel5 = new JLabel();
				this.add(jLabel5);
				jLabel5.setBounds(318, 101, 38, 16);
				jLabel5.setName("jLabel5");
			}
			{
				jTextField1prof7 = new JTextField();
				this.add(jTextField1prof7);
				jTextField1prof7.setBounds(401, 77, 77, 20);
				jTextField1prof7.setName("jTextField1prof7");
			}
			{
				jTextField1prof30 = new JTextField();
				this.add(jTextField1prof30);
				jTextField1prof30.setBounds(401, 99, 77, 20);
				jTextField1prof30.setName("jTextField1prof30");
			}
			{
				jTextField1index = new JTextField();
				this.add(jTextField1index);
				jTextField1index.setBounds(23, 54, 35, 24);
				jTextField1index.setName("jTextField1index");
			}
			{
				jLabel6locked = new JLabel();
				this.add(jLabel6locked);
				jLabel6locked.setBounds(184, 235, 68, 16);
				jLabel6locked.setName("jLabel6locked");
			}
			{
				jTextField1 = new JTextField();
				this.add(jTextField1);
				jTextField1.setBounds(86, 77, 221, 23);
			}
			{
				jLabel6 = new JLabel();
				this.add(jLabel6);
				jLabel6.setBounds(23, 80, 41, 16);
				jLabel6.setName("jLabel6");
			}
			{
				autocreatorname = new JTextField();
				this.add(autocreatorname);
				autocreatorname.setBounds(283, 257, 206, 31);
				autocreatorname.setName("SetStrategyPrefix");
			}
			{
				jButton2copytoautocreator = new JButton();
				this.add(jButton2copytoautocreator);
				jButton2copytoautocreator.setBounds(495, 259, 147, 26);
				jButton2copytoautocreator.setName("jButton2copytoautocreator");
				jButton2copytoautocreator.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2copytoautocreatorActionPerformed(evt);
					}
				});
			}
			{
				jLabel7pkz1 = new JLabel();
				this.add(jLabel7pkz1);
				jLabel7pkz1.setBounds(318, 180, 27, 16);
				jLabel7pkz1.setName("jLabel7pkz1");
			}
			{
				jTextField2pkz1 = new JTextField();
				this.add(jTextField2pkz1);
				jTextField2pkz1.setBounds(401, 178, 77, 20);
			}
			{
				jButton2showtradelist = new JButton();
				this.add(jButton2showtradelist);
				jButton2showtradelist.setBounds(714, 74, 119, 26);
				jButton2showtradelist.setName("jButton2showtradelist");
				jButton2showtradelist.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2showtradelistActionPerformed(evt);
					}
				});
			}
			{

			}
		
			
			
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
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
			Tracer.WriteTrace(10, "Error: can�t delete EA because it is connected to realaccount");
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
