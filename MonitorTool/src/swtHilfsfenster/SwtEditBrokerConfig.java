package swtHilfsfenster;

import gui.Dialog;
import gui.Mbox;
import hiflsklasse.SG;
import hiflsklasse.Swttool;
import hiflsklasse.Tracer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import StartFrame.Brokerview;
import StartFrame.Tableview;

import com.cloudgarden.resource.SWTResourceManager;

import data.GlobalVar;
import data.Lic;
import data.Metaconfig;

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
	private Label labelbrokerid;
	
	private Label label1postfix;
	private Text text1postfix;
	private Button insthistoryexporter;
	private Button SetFstDir;
	private Text forexstrategytraderdir;

	private Button insttickdataexporter;
	private Button init;
	private Button GenNewMetatraderaccount;
	private Label label8;
	private Text validitydays;
	private Button storable;
	private Button removefromreal;
	private Button installonrealaccount;
	private Label label7;
	private Text daxname;
	private Label label4;
	private Label label3;
	private Text MqlQuellverzeichniss;
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
	private Text text1money;
	private Button button1startstopautomatic;
	private Label label10;
	private Text text1mqlversion;
	private Label label9;
	private Text text1mqldir;
	private Button button1instmyfxbookea;
	private Button button1usemagiclist;
	private Label label1;
	private Button setmetatraderdir;
	private Label label6;
	private Button GbAutomaticaccountflag;
	private Button Monitoraccountflag;

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

	public void init(Display dis, Metaconfig me, Brokerview bv, int newflag,
			Tableview tv)
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
		sh.setSize(1033, 615);
		{
			label2 = new Label(sh, SWT.NONE);
			label2.setText("Brokername e.g. (Alpari1)");
			label2.setBounds(350, 39, 175, 23);
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
			SaveExit.setBounds(821, 486, 182, 30);
			SaveExit.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					SaveExitWidgetSelected(evt);
				}
			});

		}
		{
			Monitoraccountflag = new Button(sh, SWT.CHECK | SWT.LEFT);
			Monitoraccountflag.setText("MonitorAccount");
			Monitoraccountflag.setBounds(7, 3, 143, 30);
			Monitoraccountflag.setSelection(true);
			Monitoraccountflag.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					DemoaccountflagWidgetSelected(evt);
				}
			});
		}
		{
			GbAutomaticaccountflag = new Button(sh, SWT.CHECK | SWT.LEFT);
			GbAutomaticaccountflag.setText("GB_AutomaticAccount");
			GbAutomaticaccountflag.setBounds(150, 3, 185, 30);
			GbAutomaticaccountflag.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					AutoDemoaccountWidgetSelected(evt);
				}
			});
		}
		{
			description = new Text(sh, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL
					| SWT.V_SCROLL);
			description.setBounds(7, 113, 337, 95);
			description.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0,
					false, false));
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
			installEas.setBounds(7, 437, 337, 48);
			installEas.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					installEasWidgetSelected(evt);

				}
			});
		}
		{
			setquellverz = new Button(sh, SWT.PUSH | SWT.LEFT);
			setquellverz.setText("set EA sourcedir");
			setquellverz.setBounds(537, 300, 163, 24);
			setquellverz.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					setquellverzWidgetSelected(evt);
				}
			});
		}
		{
			progressBar1 = new ProgressBar(sh, SWT.NONE);
			progressBar1.setBounds(7, 537, 996, 23);
		}
		{
			lotsize = new Text(sh, SWT.NONE);
			lotsize.setBounds(937, 12, 60, 23);
			lotsize.setEditable(false);

			if (me_glob.getLotsize() != 0)
				lotsize.setText(String.valueOf(me_glob.getLotsize()));
			else
				lotsize.setText("0.1");
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
			label6.setBounds(350, 77, 73, 30);
		}
		{
			setmetatraderdir = new Button(sh, SWT.PUSH | SWT.LEFT);
			setmetatraderdir.setText("set metatrader dir");
			setmetatraderdir.setBounds(537, 220, 162, 25);
			setmetatraderdir
					.setToolTipText("The monitor copy alle *.mq4 to this location");
			setmetatraderdir.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					setmetatraderdirWidgetSelected(evt);
				}
			});
		}

		
		
		
		{
			daxname = new Text(sh, SWT.NONE);
			daxname.setBounds(7, 340, 243, 30);
			if (me_glob.getDaxname() != null)
				daxname.setText(me_glob.getDaxname());
		}
		{
			label7 = new Label(sh, SWT.NONE);
			label7.setText("Dax Name");
			label7.setBounds(256, 340, 82, 30);
		}
		
		
		{
			storable = new Button(sh, SWT.CHECK | SWT.LEFT);
			storable.setText("store trades in database");
			storable.setBounds(537, 90, 221, 30);
			storable.setSelection(me_glob.getStoretrades());
		}
		{
			validitydays = new Text(sh, SWT.NONE);
			validitydays.setBounds(537, 126, 76, 22);
			if (me_glob.getValiditydays() > 0)
				validitydays.setText(String.valueOf(me_glob.getValiditydays()));

		}
		{
			label8 = new Label(sh, SWT.NONE);
			label8.setText("period of validity days");
			label8.setBounds(619, 126, 160, 30);
		}
		{
			GenNewMetatraderaccount = new Button(sh, SWT.PUSH | SWT.CENTER);
			GenNewMetatraderaccount.setText("GenNewMetatraderaccount");
			GenNewMetatraderaccount.setBounds(806, 90, 197, 30);
			GenNewMetatraderaccount.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					GenNewMetatraderaccountWidgetSelected(evt);
				}
			});
		}
		{
			init = new Button(sh, SWT.PUSH | SWT.CENTER);
			init.setText("Init Metatrader");
			init.setBounds(821, 437, 182, 33);
			init.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					initWidgetSelected(evt);
				}
			});
		}
		{
			insttickdataexporter = new Button(sh, SWT.CHECK | SWT.LEFT);
			insttickdataexporter.setText("InstTrickdataexporter");
			insttickdataexporter.setBounds(821, 397, 182, 30);
			insttickdataexporter.setSelection(me.isInsttickdataexporter());
		}

		
		{
			SetFstDir = new Button(sh, SWT.PUSH | SWT.CENTER);
			SetFstDir.setText("set Fst Dir");
			SetFstDir.setBounds(537, 304, 106, 23);
			SetFstDir.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					SetFstDirWidgetSelected(evt);
				}
			});
		}
		{
			insthistoryexporter = new Button(sh, SWT.CHECK | SWT.LEFT);
			insthistoryexporter.setText("InstHistoryexporter");
			insthistoryexporter.setBounds(821, 369, 182, 30);
			insthistoryexporter.setSelection(true);

			insthistoryexporter.setSelection(me.isInsthistoryexporter());
		}
		
		
		{
			magiclist = new Text(sh, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER);
			magiclist.setBounds(806, 201, 197, 133);
			magiclist
					.setToolTipText("Example: the magic numbers 172 contains the 501.... 504 magic numbers.\r\n172=501,502,503,504");
			if (me.getMagicliststring() != null)
				magiclist.setText(me.getMagicliststring());
			magiclist.setEnabled(me.isMagiclistactive());
		}
		{
			button1usemagiclist = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1usemagiclist.setText("use magiclist");
			button1usemagiclist.setBounds(806, 175, 197, 30);
			button1usemagiclist.setSelection(me.isMagiclistactive());

			button1usemagiclist.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1usemagiclistWidgetSelected(evt);
				}
			});
		}
		{
			button1instmyfxbookea = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1instmyfxbookea.setText("Inst MyFxbookEa");
			button1instmyfxbookea.setBounds(821, 342, 168, 30);
			button1instmyfxbookea.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1instmyfxbookeaWidgetSelected(evt);
				}
			});
		}
		{
			text1mqldir = new Text(sh, SWT.BORDER);
			text1mqldir.setBounds(7, 258, 692, 30);
			text1mqldir.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0,
					false, false));
			text1mqldir
					.setToolTipText("This is the path were I am looking for historyxeporter.txt");
			text1mqldir.setEditable(false);
			if (me_glob.getMqldata() != null)
				text1mqldir.setText(me_glob.getMqldata());
		}
		{
			label9 = new Label(sh, SWT.NONE);
			label9.setText("You can only install GeneticBuilderEAs (www.strategyquant.com)");
			label9.setBounds(6, 491, 347, 20);
			label9.setFont(SWTResourceManager.getFont("Segoe UI", 7, 0, false,
					false));
		}
	
		
		{
			button1startstopautomatic = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1startstopautomatic.setText("start/stop-automatic");
			button1startstopautomatic.setBounds(537, 333, 162, 30);
			button1startstopautomatic.setSelection(true);
		}

		{
			button1tradelotsize = new Button(sh, SWT.RADIO | SWT.LEFT);
			button1tradelotsize.setText("trade lotsize");
			button1tradelotsize.setBounds(819, 12, 112, 30);
			button1tradelotsize.setSelection(true);
		}
		{
			text1postfix = new Text(sh, SWT.NONE);
			text1postfix.setBounds(937, 39, 60, 20);
			String pf = "";
			if ((pf = me_glob.getCurpostfix()) != null)
				text1postfix.setText(me_glob.getCurpostfix());
		}
		{
			label1postfix = new Label(sh, SWT.NONE);
			label1postfix.setText("postfix");
			label1postfix.setBounds(870, 39, 55, 22);
			label1postfix
					.setToolTipText("If a currency postfix is set, this postfix is added in the installation process at the currency pairs.\r\nFor example.\r\n\r\nIf you are a private customer at pepperstone the currency pairs you trade have the postfix \".r\". You can only trade with EURUSD.r , GBPUSD.r    ......\r\n\r\nThe Currency paris will be internal handled as the orginal currency pair without the postfix. The postfix will only used in the installation process for the configuration the metatrader.");
		}
		{
			button1HandInstalled = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1HandInstalled.setText("only HandInstalled");
			button1HandInstalled.setBounds(452, 4, 150, 30);
			button1HandInstalled.setToolTipText("!! if this flag is set, this installation never changed by the monitortool. All changes have to by handisch !!!!");
			button1HandInstalled.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1HandInstalledWidgetSelected(evt);
				}
			});
		}

		sh.open();
		refreshbuttons();

		
		initLic();

		while ((!sh.isDisposed()) && (exitflag == 0))
		{
			if (!dis.readAndDispatch())
			{
				dis.sleep();

			}
		}
		// sh.close();
	}

	private void initLic()
	{
		if (5 == 5)
		{
			// freeware version, am meisten löschen
			if (Lic.getlic() == 0)
			{
				GbAutomaticaccountflag.setVisible(false);
				lotsize.setVisible(false);

				MqlQuellverzeichniss.setVisible(false);
				setquellverz.setVisible(false);
				installEas.setVisible(false);
				label9.setVisible(false);
			}

			if ((Lic.getlic() == 0) || (Lic.getlic() == 1))
			{
				forexstrategytraderdir.setVisible(false);
				SetFstDir.setVisible(false);
				daxname.setVisible(false);
				label7.setVisible(false);
				label3.setVisible(false);
				combo1.setVisible(false);
				installonrealaccount.setVisible(false);
				removefromreal.setVisible(false);

				
				storable.setVisible(false);

				label1.setVisible(false);

				button1usemagiclist.setVisible(false);
				magiclist.setVisible(false);
				button1instmyfxbookea.setVisible(false);
				insttickdataexporter.setVisible(false);
			

				GenNewMetatraderaccount.setVisible(false);
				button1usemagiclist.setVisible(false);
				magiclist.setVisible(false);
				button1instmyfxbookea.setVisible(false);
				insttickdataexporter.setVisible(false);
			

				
				validitydays.setVisible(false);
				label8.setVisible(false);
				insthistoryexporter.setVisible(false);
			}
		}
	}

	private void refreshbuttons()
	{
		int type = me_glob.getAccounttype();

		Monitoraccountflag.setSelection(false);
		GbAutomaticaccountflag.setSelection(false);
		
		
		if(type==0)
			Monitoraccountflag.setSelection(true);
		else if(type==1)
			GbAutomaticaccountflag.setSelection(true);
	
			
		if(me_glob.getOnlyhandinstalled()==1)
			button1HandInstalled.setSelection(true);
		else
			button1HandInstalled.setSelection(false);
		
		
		
		if (me_glob.getLotsize() != 0)
			lotsize.setText(String.valueOf(me_glob.getLotsize()));
		else
			lotsize.setText("0.01");
		lotsize.setVisible(true);

		Swttool.wupdate(dis_glob);
		daxname.setVisible(true);

		// brokerid

		label9.setVisible(true);
		// qellverztextfeld

		Swttool.wupdate(dis_glob);

		initLic();
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
				Mbox.Infobox("error: the directory <"
						+ merootverz
						+ ">ist not a Metatraderinstallation teminal.exe is missing");
				return false;
			}
		}

		if (infostring.getText() != null)
			me_glob.setInfostring(infostring.getText());

		// Accounttype 0,2 ist monitoraccount oder realaccount, hier kan der
		// check entfallen
		if ((MqlQuellverzeichniss.getText() != null)
				&& (me_glob.getAccounttype() != 0)
				&& (me_glob.getAccounttype() != 2))
		{
			// prüft nach ob sich mql-quellen im Quellverzeichniss befinden
			if (work.checkMqlQuellverzeichniss(MqlQuellverzeichniss.getText()) == true)
				me_glob.setMqlquellverz(MqlQuellverzeichniss.getText());
			else
			{
				Mbox.Infobox("error: the directory <"
						+ MqlQuellverzeichniss.getText()
						+ "> dont´t have mql-files please set correct EA sourcedir");
				return false;
			}

		}
	

		me_glob.setValiditydays(SG.get_zahl(validitydays.getText()));

		
		me_glob.setStoretrades(storable.getSelection());

		if (me_glob.getAccounttype() != 2)
		{
			if (Monitoraccountflag.getSelection() == true)
				me_glob.setAccounttype(0);
			else if (GbAutomaticaccountflag.getSelection() == true)
				me_glob.setAccounttype(1);
			

		}
		if (lotsize.getText() == null)
			Tracer.WriteTrace(10, "Keine losize");
		me_glob.setLotsize(Double.valueOf(lotsize.getText()));

		me_glob.setDaxname(daxname.getText());
		me_glob.setInsttickdataexporter(insttickdataexporter.getSelection());
		me_glob.setInsthistoryexporter(insthistoryexporter.getSelection());
	
		me_glob.setMagicliststring(magiclist.getText());
		String pf = text1postfix.getText();
		if (pf != null)
			me_glob.setCurpostfix(pf);
		return true;
	}

	private void SaveExitWidgetSelected(SelectionEvent evt)
	{
		boolean ausgang = false;
		System.out.println("SaveExit.widgetSelected, event=" + evt);
		// save-button gedrückt
		ausgang = meRefreshConfig();
		if (ausgang == false)
			return;

		// hier wird die ganze Brokerconfig abgespeichert

		if (newflag_glob == 1)
			bv_glob.addElem(me_glob);

		bv_glob.SaveBrokerTable();

		exitflag = 1;
		dis_glob.getActiveShell().dispose();
		return;
	}

	private void DemoaccountflagWidgetSelected(SelectionEvent evt)
	{
		System.out.println("Demoaccountflag.widgetSelected, event=" + evt);
		me_glob.setAccounttype(0);
		Monitoraccountflag.setSelection(true);
		GbAutomaticaccountflag.setSelection(false);
		
		refreshbuttons();
	}

	private void RealaccontflagWidgetSelected(SelectionEvent evt)
	{
		System.out.println("Realaccontflag.widgetSelected, event=" + evt);
		// TODO add your code for Realaccontflag.widgetSelected
		// Realaccount gibt es kein mqlQuellverzeichniss da die quellen immer
		// aus einen verknüften demokonto kommen
		me_glob.setAccounttype(2);
		Monitoraccountflag.setSelection(false);
		GbAutomaticaccountflag.setSelection(false);
		
		refreshbuttons();
	}

	private void AutoDemoaccountWidgetSelected(SelectionEvent evt)
	{
		System.out.println("AutoDemoaccount.widgetSelected, event=" + evt);
		me_glob.setAccounttype(1);
		Monitoraccountflag.setSelection(false);
		GbAutomaticaccountflag.setSelection(true);
	
		refreshbuttons();
	}

	private boolean installEasWidgetSelected(SelectionEvent evt)
	{
		System.out.println("installEas.widgetSelected, event=" + evt);
		// Install demo

		meRefreshConfig();

		String selbroker = me_glob.getconnectedBroker();
		Metaconfig metatraderrealconfig = bv_glob
				.getMetaconfigByBrokername(selbroker);

		work.installDemoEas(dis_glob, progressBar1, me_glob,
				metatraderrealconfig, MqlQuellverzeichniss.getText(),
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
			Tracer.WriteTrace(10, "Kein metatraderroot für <" + realsharename
					+ ">");

		String selbroker = me_glob.getconnectedBroker();
		Metaconfig metatraderrealconfig = bv_glob
				.getMetaconfigByBrokername(selbroker);

		work.installRealEas(dis_glob, progressBar1, me_glob,
				metatraderrealconfig, MqlQuellverzeichniss.getText(), mqldata,
				tableview_glob);

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
		Metaconfig meconf_real = bv_glob
				.getMetaconfigByBrokername(realsharename);

		if (meconf_real == null)
			Tracer.WriteTrace(10, "Kein metatraderroot für <" + realsharename
					+ ">");

		work.cleanRealAccount(dis_glob, progressBar1, me_glob,
				MqlQuellverzeichniss.getText(), meconf_real);
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

		System.out.println("fnam=" + fnam);

		MqlQuellverzeichniss.setText(fnam);
		return;
	}

	private void setmetatraderdirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("setmetatraderdir.widgetSelected, event=" + evt);
		// Set Metatraderdir Networkshare

		String netshare = me_glob.getMqldata();

		// falls keine mqldata definiert ist
		if ((netshare != null) && (netshare.length() < 2))
			netshare = GlobalVar.getNetzwerkshareprefix();

		String fnam = Dialog.DirDialog(dis_glob,
				GlobalVar.getNetzwerkshareprefix());

		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = me_glob.getMqldata();
		else
			me_glob.setNetworkshare(fnam);

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

	private void GenNewMetatraderaccountWidgetSelected(SelectionEvent evt)
	{
		System.out.println("GenNewMetatraderaccount.widgetSelected, event="
				+ evt);
		// gen new metatraderaccount after expiration of 30 days

		Mbox.Infobox("Warning you delete the old metatraderaccount after expiration ");
		work.genNewMetatraderaccount(MtRoot.getText());
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

		work.initMetatrader(me_glob);
		Mbox.Infobox("Initialisation of metatrader <" + me_glob.getBrokername()
				+ "> ready, please start metatrader");
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
		System.out
				.println("button1instmyfxbookea.widgetSelected, event=" + evt);
		// TODO add your code for button1instmyfxbookea.widgetSelected
		me_glob.setInstmyfxbookea(button1instmyfxbookea.getSelection());
	}

	private void button1setMqlDirWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1setMqlDir.widgetSelected, event=" + evt);
		// TODO add your code for button1setMqlDir.widgetSelected
	}
	
	private void button1HandInstalledWidgetSelected(SelectionEvent evt) {
		System.out.println("button1HandInstalled.widgetSelected, event="+evt);
		
		if(button1HandInstalled.getSelection()==true)
			me_glob.setOnlyhandinstalled(1);
		else
			me_glob.setOnlyhandinstalled(0);
	}

}
