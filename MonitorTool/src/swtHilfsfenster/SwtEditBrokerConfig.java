package swtHilfsfenster;

import java.io.File;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cloudgarden.resource.SWTResourceManager;

import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.GlobalVar;
import data.Metaconfig;
import data.SymbolReplaceList;
import gui.Dialog;
import gui.Mbox;
import hiflsklasse.Swttool;
import hiflsklasse.Tracer;
import modtools.MetaStarter;

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
public class SwtEditBrokerConfig
{
	// hier wird der jeweilige Metatrader konfiguriert
	// es werden ebenfalls die mq4-files und konfigfiles installiert
	
	private Text MtRoot;
	private Text magiclist;
	
	private Button insthistoryexporter;
	private Text forexstrategytraderdir;
	
	private Button button1realaccountsel;
	
	private Text MqlQuellverzeichniss;
	private Label label10;
	private Label label9;
	private Text auc_prefix;
	private Button button1autocreator;
	private Label label8;
	private Label label7;
	private Text tradesuffixsender;
	private Link link2;
	private Link link1;
	private Button button1symbolreplacement;
	private Button button1lockaccount;
	private Label label7suffix;
	private Text text1suffix;
	private Text text1usemagic;
	private Label label4;
	private Label label3;
	private Button button1showonlyinstalledeas;
	private Text text1currencypair;
	private Button button1tradecopy;
	private Button button1HandInstalled;
	
	private Text infostring;
	private Text lotsize;
	private ProgressBar progressBar1;
	private Button setquellverz;
	private Button installEas;
	private Label label5;
	private Text description;
	private Combo combo1;
	private Button button1tradelotsize;
	
	private Text text1mqldir;
	private Button button1instmyfxbookea;
	private Button button1usemagiclist;
	private Label label1;
	private Button setmetatraderdir;
	private Label label6;
	private Button GbAutomaticaccountflag;
	
	
	private Button SaveExit;
	private Text Brokername;
	private Label label2;
	
	private Brokerview bv_glob = null;
	private Metaconfig me_glob = null;
	private Display dis_glob = null;
	private int exitflag = 0;
	private Tableview tableview_glob = null;
	private int newflag_glob = 0;
	private SwtEditBrokerConfigWork work = new SwtEditBrokerConfigWork();
	
	public void init(Display dis, Metaconfig me, Brokerview bv, int newflag, Tableview tv)
	{
		// newflag=1, eine neue config wird hinzugefügt
		bv_glob = bv;
		me_glob = me;
		tableview_glob = tv;
		dis_glob = dis;
		newflag_glob = newflag;
		Shell sh = new Shell(dis);
		
		{
			// Register as a resource user - SWTResourceManager will
			// handle the obtaining and disposing of resources
			SWTResourceManager.registerResourceUser(sh);
		}
		
		sh.setLayout(null);
		
		if (me == null)
		{
			Mbox.Infobox("please enter first a broker");
			return;
		}
		sh.pack();
		sh.setSize(1508, 862);
		{
			label2 = new Label(sh, SWT.NONE);
			label2.setText("Brokername e.g. (Alpari1)");
			label2.setBounds(350, 39, 191, 23);
		}
		{
			Brokername = new Text(sh, SWT.NONE);
			Brokername.setBounds(7, 39, 337, 30);
			if (me.getBrokername() != null)
				Brokername.setText(me.getBrokername());
		}
		{
			MtRoot = new Text(sh, SWT.NONE);
			MtRoot.setBounds(7, 220, 524, 30);
			if (me.getMqldata() != null)
				MtRoot.setText(me.getNetworkshare_INSTALLDIR());
		}
		{
			MqlQuellverzeichniss = new Text(sh, SWT.NONE);
			MqlQuellverzeichniss.setBounds(7, 297, 524, 30);
			if (me.getMqlquellverz() != null)
				MqlQuellverzeichniss.setText(me.getMqlquellverz());
		}
		{
			SaveExit = new Button(sh, SWT.PUSH | SWT.CENTER);
			SaveExit.setText("SaveConfig");
			SaveExit.setBounds(1149, 647, 210, 30);
			SaveExit.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					SaveExitWidgetSelected(evt);
				}
			});
			
		}
		
		{
			GbAutomaticaccountflag = new Button(sh, SWT.CHECK | SWT.LEFT);
			GbAutomaticaccountflag.setText("DemoAccount");
			GbAutomaticaccountflag.setBounds(8, 3, 111, 30);
			
			GbAutomaticaccountflag.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					AutoDemoaccountWidgetSelected(evt);
				}
			});
		}
		{
			description = new Text(sh, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
			description.setBounds(7, 113, 337, 95);
			description.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
			if (me.getDesciption() != null)
				description.setText(me.getDesciption());
		}
		{
			label5 = new Label(sh, SWT.NONE);
			label5.setText("Discription");
			label5.setBounds(350, 113, 117, 30);
		}
		{
			installEas = new Button(sh, SWT.PUSH | SWT.CENTER | SWT.BORDER);
			installEas.setText("INSTALL All EAs on (Demo/Real)Account");
			installEas.setBounds(12, 623, 337, 48);
			installEas.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					installEasWidgetSelected(evt);
					
				}
			});
		}
		{
			setquellverz = new Button(sh, SWT.PUSH | SWT.LEFT);
			setquellverz.setText("set EA sourcedir");
			setquellverz.setBounds(537, 297, 184, 30);
			setquellverz.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					setquellverzWidgetSelected(evt);
				}
			});
		}
		{
			progressBar1 = new ProgressBar(sh, SWT.NONE);
			progressBar1.setBounds(12, 683, 1347, 23);
		}
		{
			lotsize = new Text(sh, SWT.BORDER);
			lotsize.setBounds(1130, 12, 60, 30);
			
			if (me_glob.getLotsize() != 0)
				lotsize.setText(String.valueOf(me_glob.getLotsize()));
			else
				lotsize.setText("0.01");
		}
		{
			infostring = new Text(sh, SWT.NONE);
			infostring.setBounds(7, 77, 337, 30);
			if (me_glob.getInfostring() != null)
				infostring.setText(me_glob.getInfostring());
		}
		{
			label6 = new Label(sh, SWT.NONE);
			label6.setText("Infostring");
			label6.setBounds(350, 77, 124, 30);
		}
		{
			setmetatraderdir = new Button(sh, SWT.PUSH | SWT.LEFT);
			setmetatraderdir.setText("set metatrader dir");
			setmetatraderdir.setBounds(537, 220, 184, 25);
			setmetatraderdir.setToolTipText("The monitor copy alle *.mq4 to this location");
			setmetatraderdir.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					setmetatraderdirWidgetSelected(evt);
				}
			});
		}
		
		{
			insthistoryexporter = new Button(sh, SWT.CHECK | SWT.LEFT);
			insthistoryexporter.setText("InstHistoryexporter");
			insthistoryexporter.setBounds(806, 295, 182, 30);
			insthistoryexporter.setSelection(true);
			insthistoryexporter.setSelection(me.isInsthistoryexporter());
		}
		
		{
			magiclist = new Text(sh, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			magiclist.setBounds(806, 201, 225, 130);
			magiclist.setToolTipText(
					"Example: the magic numbers 172 contains the 501.... 504 magic numbers.\r\n172=501,502,503,504");
			magiclist.setVisible(false);
			if (me.getMagicliststring() != null)
				magiclist.setText(me.getMagicliststring());
			magiclist.setEnabled(me.isMagiclistactive());
		}
		{
			button1usemagiclist = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1usemagiclist.setText("use magiclist");
			button1usemagiclist.setBounds(806, 175, 197, 30);
			button1usemagiclist.setVisible(false);
			button1usemagiclist.setSelection(me.isMagiclistactive());
			
			button1usemagiclist.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					button1usemagiclistWidgetSelected(evt);
				}
			});
		}
		{
			button1instmyfxbookea = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1instmyfxbookea.setText("Inst MyFxbookEa");
			button1instmyfxbookea.setBounds(806, 266, 168, 30);
			if (me_glob.getUsemyfxbookflag() == 1)
				button1instmyfxbookea.setSelection(true);
			else
				button1instmyfxbookea.setSelection(false);
			button1instmyfxbookea.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					button1instmyfxbookeaWidgetSelected(evt);
				}
			});
		}
		{
			text1mqldir = new Text(sh, SWT.BORDER);
			text1mqldir.setBounds(7, 258, 714, 30);
			text1mqldir.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false, false));
			text1mqldir.setToolTipText("This is the path were I am looking for historyxeporter.txt");
			text1mqldir.setEditable(false);
			if (me_glob.getMqldata() != null)
				text1mqldir.setText(me_glob.getMqldata());
		}
		
		{
			button1tradelotsize = new Button(sh, SWT.RADIO | SWT.LEFT);
			button1tradelotsize.setText("trade lotsize");
			button1tradelotsize.setBounds(974, 12, 123, 30);
			button1tradelotsize.setSelection(true);
		}
		{
			button1HandInstalled = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1HandInstalled.setText("only HandInstalled");
			button1HandInstalled.setBounds(452, 4, 186, 30);
			button1HandInstalled.setToolTipText(
					"!! if this flag is set, this installation never changed by the monitortool. All changes have to by handisch !!!!");
			button1HandInstalled.setVisible(false);
			button1HandInstalled.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					button1HandInstalledWidgetSelected(evt);
				}
			});
		}
		{
			combo1 = new Combo(sh, SWT.NONE);
			combo1.setText("select realaccount");
			combo1.setBounds(800, 130, 340, 33);
			combo1.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					combo1WidgetSelected(evt);
				}
			});
		}
		{
			button1realaccountsel = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1realaccountsel.setText("RealAccount");
			button1realaccountsel.setBounds(125, 3, 96, 31);
			button1realaccountsel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt)
				{
					button1realaccountselWidgetSelected(evt);
				}
			});
		}
		{
			button1tradecopy = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1tradecopy.setText("Inst fxblue tradecopy");
			button1tradecopy.setBounds(806, 240, 213, 30);
			button1tradecopy.setSelection(me_glob.isInsttradecopy());
			
		}
		{
			text1currencypair = new Text(sh, SWT.NONE);
			if (me_glob.getHistexportcurrency() != null)
				text1currencypair.setText(me_glob.getHistexportcurrency());
			else
				text1currencypair.setText("EURUSD");
			text1currencypair.setBounds(803, 214, 94, 26);
		}
		{
			label1 = new Label(sh, SWT.NONE);
			label1.setText("use currency pair");
			label1.setBounds(904, 214, 99, 30);
			label1.setToolTipText(
					"The historyexporter need a currencypair, I use EURUSD as standart currencypair. If your broker don´t have EURUSD as currencypair you can choose an other pari");
		}
		{
			button1showonlyinstalledeas = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1showonlyinstalledeas.setText("show only installed EAs");
			button1showonlyinstalledeas.setBounds(806, 352, 225, 30);
			if (me_glob.getShowOnlyInstalledEas() == 1)
				button1showonlyinstalledeas.setSelection(true);
		}
		{
			label3 = new Label(sh, SWT.NONE);
			label3.setText("Connect Demoaccount with Realaccount");
			label3.setBounds(800, 95, 384, 30);
		}
		{
			label4 = new Label(sh, SWT.NONE);
			label4.setText("use magic");
			label4.setBounds(1065, 244, 86, 30);
			label4.setToolTipText(
					"This magic will be automaticaly set. In problem cases you can set this magic by hand");
		}
		{
			text1usemagic = new Text(sh, SWT.BORDER);
			text1usemagic.setText("0");
			text1usemagic.setBounds(1022, 244, 35, 26);
			if (me_glob.getTradecopymagic() > 0)
				text1usemagic.setText(String.valueOf(me_glob.getTradecopymagic()));
		}
		{
			text1suffix = new Text(sh, SWT.BORDER);
			text1suffix.setBounds(1207, 244, 49, 22);
			if (me_glob.getSuffix() != null)
				text1suffix.setText(me_glob.getSuffix());
			
		}
		{
			label7suffix = new Label(sh, SWT.NONE);
			label7suffix.setText("trade suffix receiver");
			label7suffix.setBounds(1262, 244, 132, 30);
			label7suffix.setToolTipText(
					"Use this trade suffix only if your Realbroker trade on different currency pairs. For example. The demobroker have EURUSD and the Realbroker have EURUSD.r so you should choose the suffix \".r\". This Suffix will refresed if you switch on/off the EA !!");
		}
		{
			button1lockaccount = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1lockaccount.setText("lock all EAs on this account");
			button1lockaccount.setBounds(806, 382, 291, 30);
			button1lockaccount.setToolTipText(
					"If a account is locked no one can delete EAs on this account. All EAs are protected against deletion. Unlock this account first if you want to delete EAs");
		}
		{
			button1symbolreplacement = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1symbolreplacement.setText("Automatic Symbol Replacement");
			button1symbolreplacement.setBounds(806, 326, 301, 30);
			button1symbolreplacement.setToolTipText(
					"Make automatic Symbolereplacement after EA installation. This is helpfull if you install bitcoin EAs for example.");
		}
		{
			link1 = new Link(sh, SWT.NONE);
			link1.setText("<a href=\"https://youtu.be/hEsY-6wOrLI\">info</a>");
			link1.setBounds(727, 220, 60, 30);
			link1.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					link1WidgetSelected(evt);
				}
				
			});
		}
		{
			link2 = new Link(sh, SWT.NONE);
			link2.setText("<a href=\"https://youtu.be/hEsY-6wOrLI\">info</a>");
			link2.setBounds(727, 301, 42, 30);
			link2.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					link2WidgetSelected(evt);
				}
				
			});
		}
		{
			tradesuffixsender = new Text(sh, SWT.NONE);
			tradesuffixsender.setBounds(1021, 214, 49, 20);
			if(me_glob.getTradesuffixsender()!=null)
			tradesuffixsender.setText(me_glob.getTradesuffixsender());
		}
		{
			label7 = new Label(sh, SWT.NONE);
			label7.setText("currency add");
			label7.setBounds(1080, 214, 78, 20);
		}
		{
			label8 = new Label(sh, SWT.NONE);
			label8.setText("(info)");
			label8.setBounds(1158, 214, 44, 20);
			label8.setToolTipText("If currency add is set the currency name will be renamed before installation. For example: add=.r than EURUSD will be named to EURUSD.r");
		}
		{
			button1autocreator = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1autocreator.setText("AutoCreator");
			button1autocreator.setBounds(233, 3, 111, 30);
			button1autocreator.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1autocreatorWidgetSelected(evt);
				}
			});
		}
		{
			
			auc_prefix = new Text(sh, SWT.NONE);
			String apref=me_glob.getAucPrefix();
			if(apref!=null)
			auc_prefix.setText(apref);
			auc_prefix.setBounds(806, 485, 184, 19);
			auc_prefix.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent evt) {
					auc_prefixModifyText(evt);
				}
			});
		}
		{
			label9 = new Label(sh, SWT.NONE);
			label9.setText("Settings for Autocreator");
					label9.setBounds(806, 460, 184, 19);
		}
		{
			label10 = new Label(sh, SWT.NONE);
			label10.setText("StrategyPrefix");
			label10.setBounds(1002, 485, 100, 20);
		}

		sh.open();
		initBrokereditM();
		refreshbuttons();
		
		while ((!sh.isDisposed()) && (exitflag == 0))
		{
			if (!dis.readAndDispatch())
			{
				dis.sleep();
				
			}
		}
		// sh.close();
		
	}
	
	private void refreshbuttons()
	{
		build_combobox(combo1, bv_glob);
		
		if (me_glob.getAccounttype() == 1)
		{
			
			setDemoaccount();
		} else if(me_glob.getAccounttype()==2)
		{
			setRealaccount();
		} else if (me_glob.getAccounttype()==4)
			setAutoCreatorAccount();
		else
			Tracer.WriteTrace(10, "refreshbuttons: unknown accounttype = <"+me_glob.getAccounttype()+">");
		
		if (me_glob.getLotsize() != 0)
			lotsize.setText(String.valueOf(me_glob.getLotsize()));
		else
			lotsize.setText("0.01");
		lotsize.setVisible(true);
		if (me_glob.getAccountlocked() == 1)
			button1lockaccount.setSelection(true);
		else
			button1lockaccount.setSelection(false);
		
		Swttool.wupdate(dis_glob);
		
	}
	
	private void build_combobox(Combo combo1, Brokerview bv)
	{
		
		// den configurierten Broker setzen
		String selbroker = me_glob.getconnectedBroker();
		
		int anz = bv.getAnz();
		for (int i = 0, j = 0; i < anz; i++)
		{
			Metaconfig me = bv.getElem(i);
			if (me.getAccounttype() == 2)
			{
				String masteraccountname = me.getBrokername();
				combo1.add(masteraccountname);
				
				// falls der brokername im configfile der selektierte ist dann
				// selektiere auch gleich die combobox
				if (selbroker.equals(masteraccountname) == true)
					combo1.select(j);
				j++;
			}
		}
	}
	
	private boolean meRefreshConfig()
	{
		// true: falls alles ok
		// false: falls fehler
		
		if (Brokername.getText().contains("___") == false)
			me_glob.setBrokername(Brokername.getText());
		else
		{
			Mbox.Infobox("char '___' is not allowed in Brokername");
			return false;
		}
		
		if (description.getText() != null)
			me_glob.setDesciption(description.getText());
		
		if (MtRoot.getText() != null)
		{
			String merootverz = MtRoot.getText();
			
			if (work.checkMetatraderverzeichniss(merootverz) == true)
				me_glob.setNetworkshare(merootverz);
			else
			{
				Mbox.Infobox("error: the directory <" + merootverz
						+ ">ist not a Metatraderinstallation teminal.exe is missing");
				return false;
			}
		}
		
		if (infostring.getText() != null)
			me_glob.setInfostring(infostring.getText());
			
		// Accounttype 0,2,3 ist monitoraccount oder realaccount, hier kann der
		// check entfallen,3=autocreator
		if ((MqlQuellverzeichniss.getText() != null) && (me_glob.getAccounttype() == 1))

		{
			// prüft nach ob sich mql-quellen im Quellverzeichniss befinden
			if (work.checkMqlQuellverzeichniss(MqlQuellverzeichniss.getText()) == true)
				me_glob.setMqlquellverz(MqlQuellverzeichniss.getText());
			else
			{
				Mbox.Infobox("error: the directory <" + MqlQuellverzeichniss.getText()
						+ "> dont´t have mql-files please set correct EA sourcedir");
				return false;
			}
		}
		String conbroker = combo1.getText();
		me_glob.setconnectedBroker(conbroker);
		return true;
	}
	
	void initBrokereditM()
	{
		//bei create account wird erst default mässig auf demoaccount gesetzt
		if(me_glob.getAccounttype() == 0)
			setDemoaccount();
		
		
		me_glob.setValiditydays(999999);
		me_glob.setStoretrades(true);
		
		if (lotsize.getText() == null)
			Tracer.WriteTrace(10, "Keine losize");
		me_glob.setLotsize(Double.valueOf(lotsize.getText()));
		
		me_glob.setInsthistoryexporter(insthistoryexporter.getSelection());
		me_glob.setMagicliststring(magiclist.getText());
		me_glob.setHistexportcurrency(text1currencypair.getText());
		
		if (button1instmyfxbookea.getSelection() == true)
			me_glob.setUsemyfxbookflag(1);
		else
			me_glob.setUsemyfxbookflag(0);
		
		if (button1tradecopy.getSelection() == true)
			me_glob.setUsefxblueflag(1);
		else
			me_glob.setUsefxblueflag(0);
		
		if (button1showonlyinstalledeas.getSelection() == true)
			me_glob.setShowOnlyInstalledEas(1);
		else
			me_glob.setShowOnlyInstalledEas(0);
		
		if (me_glob.getAutomaticsymbolreplacement() == 1)
			button1symbolreplacement.setSelection(true);
		else
			button1symbolreplacement.setSelection(false);
		
		if (Brokername.getText().length() > 0)
			Brokername.setEditable(false);
	}
	
	private void SaveExitWidgetSelected(SelectionEvent evt)
	{
		boolean ausgang = false;
		System.out.println("SaveExit.widgetSelected, event=" + evt);
		// save-button gedrückt
		ausgang = meRefreshConfig();
		if (ausgang == false)
			return;
		
		// init metatrader
		if (MtRoot == null)
		{
			Mbox.Infobox("No Metatraderdir");
			return;
		}
		if(GlobalVar.getMetatraderautostartstop()==0)
		  MetaStarter.KillAllMetatrader(0);
		meRefreshConfig();
		
		me_glob.setLotsize(Double.valueOf(lotsize.getText()));
		me_glob.setHistexportcurrency(text1currencypair.getText());
		me_glob.setInsttradecopy(button1tradecopy.getSelection());
		work.initMetatrader(me_glob);
		
		if (button1showonlyinstalledeas.getSelection() == true)
			me_glob.setShowOnlyInstalledEas(1);
		else
			me_glob.setShowOnlyInstalledEas(0);
		
		if (button1lockaccount.getSelection() == true)
			me_glob.setAccountlocked(1);
		else
			me_glob.setAccountlocked(0);
		
		String suffix = text1suffix.getText();
		if (suffix != null)
			me_glob.setSuffix(suffix);
		
		if (button1symbolreplacement.getSelection() == true)
			me_glob.setAutomaticsymbolreplacement(1);
		else
			me_glob.setAutomaticsymbolreplacement(0);
		
		me_glob.setTradesuffixsender(tradesuffixsender.getText());
		
		// global config speichern
		int magic = Integer.valueOf(text1usemagic.getText());
		me_glob.setTradecopymagic(magic);
		
		
		String aucpref=auc_prefix.getText();
		me_glob.setAucPrefix(aucpref);
		
		// GlobalVar.setLastcopytrademagic(magic);
		GlobalVar.save();
		
		// hier wird die ganze Brokerconfig abgespeichert
		
		if (newflag_glob == 1)
			bv_glob.addElem(me_glob);
		
		bv_glob.SaveBrokerTable();
		
		if (button1symbolreplacement.getSelection() == true)
		{
			SymbolReplaceList s = new SymbolReplaceList(bv_glob);
			s.ReplaceAllSymbols();
			s.ShowReplaceResults();
			
		}
		
		exitflag = 1;
		if (dis_glob.getActiveShell() != null)
			dis_glob.getActiveShell().dispose();
		return;
	}
	

	
	
	
	private boolean installEasWidgetSelected(SelectionEvent evt)
	{
		System.out.println("installEas.widgetSelected, event=" + evt);
		// Install demo
		MetaStarter.KillAllMetatrader(0);
		meRefreshConfig();
		
		String selbroker = me_glob.getconnectedBroker();
		Metaconfig metatraderrealconfig = bv_glob.getMetaconfigByBrokername(selbroker);
		
		me_glob.setLotsize(Double.valueOf(lotsize.getText()));
		work.installDemoEas(dis_glob, progressBar1, me_glob, metatraderrealconfig, MqlQuellverzeichniss.getText(),
				tableview_glob);
		bv_glob.SaveBrokerTable();
		
		return true;
	}
	
	private boolean installonrealaccountWidgetSelected(SelectionEvent evt)
	{
		System.out.println("installonrealaccount.widgetSelected, event=" + evt);
		// install eas on realaccount
		meRefreshConfig();
		
		int index = combo1.getSelectionIndex();
		String realsharename = combo1.getItem(index);
		
		if (realsharename == null)
			Tracer.WriteTrace(10, "Error: kein realshare definiert");
		
		String mqldata = bv_glob.getMqlData(realsharename);
		
		if (mqldata == null)
			Tracer.WriteTrace(10, "Kein metatraderroot für <" + realsharename + ">");
		
		String selbroker = me_glob.getconnectedBroker();
		Metaconfig metatraderrealconfig = bv_glob.getMetaconfigByBrokername(selbroker);
		
		work.installRealEas(dis_glob, progressBar1, me_glob, metatraderrealconfig, MqlQuellverzeichniss.getText(),
				mqldata, tableview_glob);
		
		bv_glob.SaveBrokerTable();
		return true;
	}
	
	private void removefromrealWidgetSelected(SelectionEvent evt)
	{
		System.out.println("removefromreal.widgetSelected, event=" + evt);
		// Alle Ea´s von diesem Realaccount löschen
		meRefreshConfig();
		
		int index = combo1.getSelectionIndex();
		String realsharename = combo1.getItem(index);
		
		if (realsharename == null)
			Tracer.WriteTrace(10, "Error: kein realshare definiert");
		
		// String metatraderroot = bv_glob.getRealshare(realsharename);
		Metaconfig meconf_real = bv_glob.getMetaconfigByBrokername(realsharename);
		
		if (meconf_real == null)
			Tracer.WriteTrace(10, "Kein metatraderroot für <" + realsharename + ">");
		
		work.cleanRealAccount(dis_glob, progressBar1, me_glob, MqlQuellverzeichniss.getText(), meconf_real);
	}
	
	private void setquellverzWidgetSelected(SelectionEvent evt)
	{
		// selektiere das mql-quellverzeichniss mit hilfe eines Filerequesters
		System.out.println("setquellverz.widgetSelected, event=" + evt);
		
		String mqlverz = me_glob.getMqlquellverz();
		if (mqlverz.length() < 5)
			mqlverz = GlobalVar.getMqlsourcedirprefix();
		
		String fnam = Dialog.DirDialog(dis_glob, mqlverz);
	
		
		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = me_glob.getMqlquellverz();
		else if ((fnam.toLowerCase().contains("programme")) || (fnam.toLowerCase().contains("programs"))
				|| (fnam.toLowerCase().contains("program files")))
		{
			Tracer.WriteTrace(10,
					"Error: don´t install EAs in Windows programfile dir\n use instead c:\\forex\\Strategies\\subfolder1..");
			return;
			
		} 
		
		else
		{
			// if global var not set, than set it
			if ((GlobalVar.getMqlsourcedirprefix() == null) || (GlobalVar.getMqlsourcedirprefix().length() < 2))
			{
				// D:\\Forex\\StrategienImEinsatz
				String fpref = fnam.substring(0, fnam.lastIndexOf("\\"));
				GlobalVar.setMqlsourcedirprefix(fpref);
				GlobalVar.save();
				
			}
			
		}
		
		System.out.println("fnam=" + fnam);
		MqlQuellverzeichniss.setText(fnam);
		me_glob.setMqlquellverz(fnam);
		
		return;
	}
	
	private void setmetatraderdirWidgetSelected(SelectionEvent evt)
	{
	
	
		System.out.println("setmetatraderdir.widgetSelected, event=" + evt);
		// Set Metatraderdir Networkshare
		String netshare = me_glob.getMqldata();
		
		// falls keine mqldata definiert ist
		// netzwerkshareprefix == metatraderroot
		if ((netshare != null) && (netshare.length() < 2))
			netshare = GlobalVar.getNetzwerkshareprefix();
		
		String fnam = Dialog.DirDialog(dis_glob, GlobalVar.getNetzwerkshareprefix());
		
		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = me_glob.getMqldata();
		else if ((fnam.toLowerCase().contains("programme")) || (fnam.toLowerCase().contains("programs"))
				|| (fnam.toLowerCase().contains("program files")))
		{
			Tracer.WriteTrace(10,
					"Warning: don´t install Metatrader in Windows programfile dir\n use instead for example c:\\forex\\mt4\\pepperstone1");
			return;
			
		} 
		else if ((new File(fnam+"\\terminal.exe").exists()==false)&&
				(new File(fnam+"\\terminal64.exe").exists()==false))
		{
			Tracer.WriteTrace(10,
					"Error: This directory is not an metatrader rootdir, terminal.exe is missing\n please set correct dir");
			return;
			
		}
		else if(bv_glob.checkMetatraderExists(fnam,1)==true)
		{
			Tracer.WriteTrace(20,"Error: This metatrader is already configured");
			return;
		}
		else
		{ // fnam=D:\\Forex\\mt4\\audrn2
			me_glob.setNetworkshare(fnam);
			if ((GlobalVar.getNetzwerkshareprefix() == null) || ((GlobalVar.getNetzwerkshareprefix().length() < 2)))
			{
				// if networkshareprefix is not set, then set this global variable
				String pref = fnam.substring(0, fnam.lastIndexOf("\\"));
				GlobalVar.setNetworkshareprefix(pref);
				GlobalVar.save();
			}
		}
		me_glob.getInitMetaversion();
		
		
	
		System.out.println("fnam=" + fnam);
		
		MtRoot.setText(fnam);
		
		// die metatrader version ermitteln
		
		if (me_glob.getMqldata() != null)
			text1mqldir.setText(me_glob.getMqldata());
		
		return;
	}
	
	private void SetFstDirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("SetFstDir.widgetSelected, event=" + evt);
		// set FstDir
		String fstdir = me_glob.getFstdir();
		
		String fnam = Dialog.DirDialog(dis_glob, fstdir);
		
		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = me_glob.getFstdir();
		
		System.out.println("fnam=" + fnam);
		
		if (fnam != null)
			forexstrategytraderdir.setText(fnam);
		return;
	}
	
	private void initWidgetSelected(SelectionEvent evt)
	{
		System.out.println("init.widgetSelected, event=" + evt);
		// TODO add your code for init.widgetSelected
		
		if (MtRoot == null)
		{
			Mbox.Infobox("No Metatraderdir");
			return;
		}
		MetaStarter.KillAllMetatrader(0);
		meRefreshConfig();
		me_glob.setHistexportcurrency(text1currencypair.getText());
		me_glob.setInsttradecopy(button1tradecopy.getSelection());
		work.initMetatrader(me_glob);
		Mbox.Infobox("Initialisation of metatrader <" + me_glob.getBrokername() + "> ready, please start metatrader");
	}
	
	private void button1usemagiclistWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1usemagiclist.widgetSelected, event=" + evt);
		// TODO add your code for button1usemagiclist.widgetSelected
		// magiclist ein und auschalten
		
		me_glob.setMagiclistactive(button1usemagiclist.getSelection());
		magiclist.setEnabled(button1usemagiclist.getSelection());
	}
	
	private void button1instmyfxbookeaWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1instmyfxbookea.widgetSelected, event=" + evt);
		// TODO add your code for button1instmyfxbookea.widgetSelected
		if (button1instmyfxbookea.getSelection() == true)
			me_glob.setUsemyfxbookflag(1);
		else
			me_glob.setUsemyfxbookflag(0);
	}
	
	private void button1setMqlDirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setMqlDir.widgetSelected, event=" + evt);
		// TODO add your code for button1setMqlDir.widgetSelected
	}
	
	private void button1HandInstalledWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1HandInstalled.widgetSelected, event=" + evt);
		
		if (button1HandInstalled.getSelection() == true)
		{
			me_glob.setOnlyhandinstalled(1);
			GbAutomaticaccountflag.setSelection(false);
		} else
			me_glob.setOnlyhandinstalled(0);
	}
	
	private void button1freqentlyupdateWidgetDefaultSelected(SelectionEvent evt)
	{
		System.out.println("button1freqentlyupdate.widgetDefaultSelected, event=" + evt);
		// TODO add your code for button1freqentlyupdate.widgetDefaultSelected
	}
	
	private void button1freqentlyupdateWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1freqentlyupdate.widgetSelected, event=" + evt);
		// TODO add your code for button1freqentlyupdate.widgetSelected
	}
	
	private void text1frequentlyupdateWidgetSelected(SelectionEvent evt)
	{
		System.out.println("text1frequentlyupdate.widgetSelected, event=" + evt);
		// TODO add your code for text1frequentlyupdate.widgetSelected
	}
	
	private void text1earenametextWidgetSelected(SelectionEvent evt)
	{
		System.out.println("text1earenametext.widgetSelected, event=" + evt);
		// TODO add your code for text1earenametext.widgetSelected
	}
	
	private void button1earenameWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1earename.widgetSelected, event=" + evt);
		
	}
	
	private void combo1WidgetSelected(SelectionEvent evt)
	{
		System.out.println("combo1.widgetSelected, event=" + evt);
		// build_combo1();
	}
	
	private void button1tradecopyWidgetSelectedAdapter(Consumer evt)
	{
		System.out.println("button1tradecopy.widgetSelectedAdapter, event=" + evt);
		me_glob.setInsttradecopy(button1tradecopy.getSelection());
	}
	
	private void button1setupdatedirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setupdatedir.widgetSelected, event=" + evt);
		// TODO add your code for button1setupdatedir.widgetSelected
	}
	
	private void AutoDemoaccountWidgetSelected(SelectionEvent evt)
	{
		System.out.println("AutoDemoaccount.widgetSelected, event=" + evt);
		setDemoaccount();
	}
	
	private void setDemoaccount()
	{
		//1=demoaccount
		//2=realacccount
		//3=autocreatoraccount
		me_glob.setAccounttype(1);
		text1usemagic.setEnabled(true);
		int magic = me_glob.getTradecopymagic();
		// if magic not set
		if (magic == 0)
		{
			// generiere neue magic
			
			magic = bv_glob.getNewTradecopymagic();
			GlobalVar.setLastcopytrademagic(magic);
		}
		
		text1usemagic.setText(String.valueOf(magic));
		me_glob.setTradecopymagic(magic);
		GbAutomaticaccountflag.setSelection(true);
		button1autocreator.setSelection(false);
		button1showonlyinstalledeas.setEnabled(true);
		if (me_glob.getShowOnlyInstalledEas() == 1)
			button1showonlyinstalledeas.setSelection(true);
		button1realaccountsel.setSelection(false);
		text1suffix.setEnabled(false);
		label7suffix.setEnabled(false);
		combo1.setEnabled(true);
		installEas.setEnabled(true);
		text1usemagic.setEnabled(true);
		label4.setEnabled(true);
		tradesuffixsender.setEnabled(true);
		setquellverz.setEnabled(true);
		MqlQuellverzeichniss.setEnabled(true);
		link2.setEnabled(true);
		
	}
	
	private void button1realaccountselWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1realaccountsel.widgetSelected, event=" + evt);
		setRealaccount();
	}
	
	private void setRealaccount()
	{
		//1=demoaccount
		//2=realacccount
		//3=autocreatoraccount
		me_glob.setAccounttype(2);
		text1usemagic.setText("0");
		
		GbAutomaticaccountflag.setSelection(false);
		button1autocreator.setSelection(false);
		button1showonlyinstalledeas.setEnabled(false);
		button1showonlyinstalledeas.setSelection(false);
		button1realaccountsel.setSelection(true);
		combo1.setEnabled(false);
		text1suffix.setEnabled(true);
		label7suffix.setEnabled(true);
		installEas.setEnabled(false);
		text1usemagic.setEnabled(false);
		label4.setEnabled(false);
		tradesuffixsender.setEnabled(false);
		setquellverz.setEnabled(false);
		MqlQuellverzeichniss.setEnabled(false);
		link2.setEnabled(false);
		
	}
	private void setAutoCreatorAccount()
	{
		//1=demoaccount
		//2=realacccount
		//4=autocreatoraccount
		me_glob.setAccounttype(4);
		text1usemagic.setEnabled(true);
		int magic = me_glob.getTradecopymagic();
		// if magic not set
		if (magic == 0)
		{
			// generiere neue magic
			
			magic = bv_glob.getNewTradecopymagic();
			GlobalVar.setLastcopytrademagic(magic);
		}
		
		text1usemagic.setText(String.valueOf(magic));
		me_glob.setTradecopymagic(magic);
		GbAutomaticaccountflag.setSelection(false);
		button1showonlyinstalledeas.setEnabled(true);
		if (me_glob.getShowOnlyInstalledEas() == 1)
			button1showonlyinstalledeas.setSelection(true);
		button1realaccountsel.setSelection(false);
		button1autocreator.setSelection(true);
		text1suffix.setEnabled(false);
		label7suffix.setEnabled(false);
		combo1.setEnabled(true);
		installEas.setEnabled(false);
		text1usemagic.setEnabled(true);
		label4.setEnabled(true);
		tradesuffixsender.setEnabled(true);
		setquellverz.setEnabled(false);
		MqlQuellverzeichniss.setEnabled(false);
		link2.setEnabled(false);
		
	}
	
	
	private void link1WidgetSelected(SelectionEvent evt) {
		System.out.println("link1.widgetSelected, event="+evt);
		
		Program.launch(evt.text);
	}
	private void link2WidgetSelected(SelectionEvent evt) {
		System.out.println("link1.widgetSelected, event="+evt);
		
		Program.launch(evt.text);
	}
	
	private void button1autocreatorWidgetSelected(SelectionEvent evt) {
		System.out.println("button1autocreator.widgetSelected, event="+evt);
		setAutoCreatorAccount();
	}
	
	private void auc_prefixModifyText(ModifyEvent evt) {
		System.out.println("auc_prefix.modifyText, event="+evt);
		String auctmp=auc_prefix.getText();
		if(auctmp==null)
			return;
		if(auctmp.length()>6)
			Tracer.WriteTrace(10, "Maxlen Strategyprefix<"+auctmp+"= 6");
		if(auctmp.contains("_"))
			Tracer.WriteTrace(10,"character _ is not allowed in Strategyprefix<"+auctmp+">");
					
	}
}
