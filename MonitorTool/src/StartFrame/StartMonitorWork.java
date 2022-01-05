package StartFrame;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import Metriklibs.FileAccessDyn;
import Sync.LockTradeliste;
import backtest.Mt4Backtester;
import charttool.Profitanzeige;
import charttool.ShowAllProfitsConfig;
import charttool.ShowConfigAllP3;
import data.Ea;
import data.GlobalVar;
import data.Metaconfig;
import data.Profit;
import data.Profitliste;
import data.Rootpath;
import data.SymbolReplaceList;
import data.Trade;
import data.Tradeanzahl;
import data.Tradeliste;
import eaAutTools.AutomaticCheck_dep;
import filter.Tradefilter;
import gui.Mbox;
import hiflsklasse.Archive;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;
import hiflsklasse.Viewer;
import modtools.Autoconfig;
import modtools.Installer;
import modtools.MetaStarter;
import montool.MonDia;
import mtools.DisTool;
import mtools.Mlist;
import network.Crypto;
import swingHilfsfenster.ShowUebersicht;
import swtHilfsfenster.SwtCompareTradelist;
import swtHilfsfenster.SwtConfigEa;
import swtHilfsfenster.SwtEditBrokerConfig;
import swtHilfsfenster.SwtPicture;
import swtHilfsfenster.SwtSetInstfrom;
import swtHilfsfenster.SwtSetSortCriteria;
import swtHilfsfenster.SwtShowFile;
import swtHilfsfenster.SwtShowTradeliste;
import swtHilfsfenster.SwtTransferUserdata;
import swtHilfsfenster.TradeFilterConfig;

public class StartMonitorWork
{
	private Tableview tv_glob = new Tableview();
	private Brokerview brokerview_glob = new Brokerview();
	private String glob_selectedBrokerShare = "";
	private String glob_selectedBroker = "";
	private Profit selectedProfitelem_glob = null;
	private int table2clickedindex = 0;
	private Display display_glob = null;
	private ProgressBar pb1_glob = null;
	private ProgressBar pb2_glob = null;
	
	public StartMonitorWork(Display dis, ProgressBar pb1, ProgressBar pb2)
	{
		display_glob = dis;
		pb1_glob = pb1;
		pb2_glob = pb2;
	}
	
	public void clearTradeliste()
	{
		Tradeliste tl = tv_glob.getTradeliste();
		tl.clear();
	}
	
	public Tradeanzahl calcTradeanzahl()
	{
		if (tv_glob == null)
		{
			Tradeanzahl tr = new Tradeanzahl();
			tr.setAnztrades(0);
			tr.setAnzprofits(0);
			return tr;
		}
		return (tv_glob.calcTradeanzahl());
		
	}
	
	public Profitliste getAktProfitliste()
	{
		return (tv_glob.getAktProfitliste());
	}
	
	public void buildBrokerliste(Table table3, int forceflag)
	{
		// forceflag==1, es wird von platte alles geladen
		// forceflag==0 es wird nur angezeigt
		
		// hier wird intern die Brokertabelle aufgebaut
		if (forceflag == 1)
			brokerview_glob.LoadBrokerTable();
		
		// die interne Brokertabelle wird in table3 angezeigt
		brokerview_glob.ShowBrokerTable(display_glob, table3, forceflag);
	}
	
	public boolean isSelectedBroker(int type)
	{
		if (brokerview_glob.getAccounttype(glob_selectedBroker) == type)
			return true;
		else
			return false;
		
	}
	

	
	public void loadallbroker(Display dis, Table table1, Table table2, Table table3, Tradefilter tf,
			Text anzincommingtrades, Text anzeas, org.eclipse.swt.widgets.Label broker, int showflag, int forceloadflag,
			int onlyopenflag)
	{
		// forceloadflag=1, dann werden die trades neu eingeladen
		DisTool.waitCursor();
		// dis.getActiveShell().setCursor(new Cursor(Display.getCurrent(),
		// SWT.CURSOR_WAIT));
		// falls profitnormalisierung==true dann werden die gewinne auf lotsize
		// 0.1 umgerechnet
		
		buildBrokerliste(table3, forceloadflag);
		broker.setText("load all active broker");
		
		// hier wird die Tradeliste gelöscht, also nach dem Init sind 0 trades drin
		tv_glob.init(dis, brokerview_glob, tf, table2, pb1_glob, forceloadflag); // forceflag auf 1 gesetzt
		int anz = brokerview_glob.getAnz();
		
		pb1_glob.setMinimum(0);
		pb1_glob.setMaximum(anz - 1);
		
		pb1_glob.setOrientation(1);
		
		if (forceloadflag == 1)
			for (int i = 0; i < anz; i++)
			{
				Metaconfig mc = brokerview_glob.getElem(i);
				pb1_glob.setSelection(i);
				Mlist.add("load broker <" + mc.getBrokername() + ">");
				
				Tracer.WriteTrace(20,
						"Info: try to read <" + mc.getBrokername() + "> on <" + tv_glob.getFiledata(mc) + ">");
				if (mc.getOn() == 1)
				{
					tv_glob.LoadTradeTable(mc, dis, 0, showflag, onlyopenflag);
					Tracer.WriteTrace(20, "tradeanzahl in globtradeliste=" + tv_glob.calcTradeanzahl().getAnztrades());
					
				}
			}
		
		// pongcheck
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mc = brokerview_glob.getElem(i);
			mc.pongCheck();
		}
		// den brokerview aktualisieren falls der pongcheck noch änderungen gemacht hat
		brokerview_glob.ShowBrokerTable(dis, table3, 0);
		
		Tracer.WriteTrace(20, "Info: TradeTable anzeigen");
		tv_glob.ShowTradeTable(display_glob, table1, null, GlobalVar.getShowMaxTradetablesize(), forceloadflag);
		tv_glob.CalcProfitTable(null, 1);
		
		Tracer.WriteTrace(20, "Info: Profittable anzeigen");
		tv_glob.ShowProfitTable();
		if(onlyopenflag==1)tv_glob.checkProfitliste();
		tv_glob.showCounter(anzincommingtrades, anzeas);
		
		if (forceloadflag == 1)
			brokerview_glob.SaveBrokerTable();
		Tracer.WriteTrace(20, "Info: speichern");
		DisTool.arrowCursor();
	}
	
	public void cleanAllWaste()
	{
		int anz = brokerview_glob.getAnz();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mc = brokerview_glob.getElem(i);
			
			String filedir = mc.getAppdata() + "\\MQL4\\Files";
			FileAccess.initFileSystemList(filedir, 1);
			int anzbmp = FileAccess.holeFileAnz();
			
			// lösche alle bmp files
			for (int j = 0; j < anzbmp; j++)
			{
				// betrachte nur .bmp files
				File fnamf = new File(filedir + "\\" + FileAccess.holeFileSystemName());
				if ((fnamf.getAbsolutePath().endsWith(".bmp")) == false)
					continue;
				if (FileAccess.checkOlderXDays(fnamf, 1) == true)
					if (fnamf.delete() == false)
						Tracer.WriteTrace(10, "E:Clean all waste:can´t delete file <" + fnamf.getAbsolutePath() + ">");
			}
		}
	}
	
	public void workTrades(Metaconfig mc, Tradefilter tf, Table table1, Table table2, Table table3, Display dis,
			int showflag, int forceloadflag, int onlyopenflag)
	{
		
		// dieser broker wurde selektiert
		String brokername = mc.getBrokername();
		
		// die Tradetable für einen bestimmten Broker laden
		tv_glob.LoadTradeTable(mc, dis, 0, showflag, onlyopenflag);
		// die Tradetable für einen bestimmten broker anzeigen
		tv_glob.ShowTradeTable(dis, table1, brokername, GlobalVar.getShowMaxTradetablesize(), forceloadflag);
		// die profittable für einen bestimmten broker berechnen
		tv_glob.CalcProfitTable(brokername, 1);
		// die profittabelle anzeigen
		tv_glob.ShowProfitTable();
		
	}
	
	public void brokerselected(String name, Tradefilter tf, Table table1, Table table2, Table table3,
			org.eclipse.swt.widgets.Label broker, Display dis, int showflag, int forceloadflag, int onlyopenflag)
	{
		glob_selectedBrokerShare = name;
		// holt sich die konfiguration
		Metaconfig me = brokerview_glob.getMetaconfig(name);
		
		if (me == null)
		{
			Tracer.WriteTrace(10, "E:can´t find broker <" + name + "> in brokerconfig");
			return;
		}
		glob_selectedBroker = me.getBrokername();
		
		broker.setText(glob_selectedBroker);
		
		// hier werden die trades geladen
		// if (me.getInstallationstatus() != 0)
		
		// hier wird die Ealiste für das mittlere Fenster aufgebaut
		workTrades(me, tf, table1, table2, table3, dis, showflag, forceloadflag, onlyopenflag);
		
		brokerview_glob.SaveBrokerTable();
		System.out.println(name);
	}
	
	public void editconfig(Table table3)
	{
		
		// metatraderconfig holen
		Metaconfig me = brokerview_glob.getMetaconfig(glob_selectedBrokerShare);
		
		SwtEditBrokerConfig sc = new SwtEditBrokerConfig();
		sc.init(display_glob, me, brokerview_glob, 0, tv_glob);
		buildBrokerliste(table3, 0);
	}
	
	public void showtradelist1()
	{
		
		if (selectedProfitelem_glob == null)
		{
			Mbox.Infobox("please select ea first");
			return;
		}
		
		String magic = String.valueOf(selectedProfitelem_glob.getMagic());
		String broker = selectedProfitelem_glob.getBroker();
		
		SwtShowTradeliste st = new SwtShowTradeliste();
		Tradeliste etl = tv_glob.buildTradeliste(magic, broker, -1,null);
		
		st.init(display_glob, etl, tv_glob, broker, magic);
		
	}
	
	public void comparetradelist()
	{
		// graphische Klasse um Trades zu vergleichen
		SwtCompareTradelist sc = new SwtCompareTradelist();
		// ermittle die Liste der seletierten magics
		ArrayList<Integer> maglist = tv_glob.CalcSelectedMagics();
		// hier werden die Trades verglichen
		sc.init(display_glob, selectedProfitelem_glob, tv_glob, maglist);
	}
	
	public void updatehistoryexporter(Table table3, int forceflag)
	{
		Tracer.WriteTrace(20, "I:stop all Metatrader");
		if (GlobalVar.getMetatraderautostartstop() == 1)
			MetaStarter.StopAllMetatrader(brokerview_glob, forceflag);
		
		buildBrokerliste(table3, 0);
		Installer inst = new Installer();
		inst.UpdateHistoryExporter(display_glob, brokerview_glob);
	}
	
	public void backup()
	{
		Installer inst = new Installer();
		inst.backup(display_glob, brokerview_glob);
	}
	
	public void restore()
	{
		Installer inst = new Installer();
		inst.restore(display_glob, brokerview_glob);
	}
	
	public void transfer()
	{
		Installer inst = new Installer();
		inst.transfer(display_glob, brokerview_glob);
	}
	
	public void transferuserdata()
	{
		SwtTransferUserdata.showGUI(brokerview_glob);
		
	}
	
	public void table2clicked(int index, String name, org.eclipse.swt.widgets.Button selonlyone, Table table)
	{
		
		Profit prof = tv_glob.getprofitelem(index);
		selectedProfitelem_glob = prof;
		glob_selectedBrokerShare = name;
		table2clickedindex = index;
		
		if (selonlyone.getSelection() == true)
		{
			// falls nur einer aktiviert sein darf müssen die andern gelöscht
			// werden
			int anz = table.getItemCount();
			for (int i = 0; i < anz; i++)
			{
				TableItem item = table.getItem(i);
				if (i != index)
					item.setChecked(false);
			}
			DisTool.UpdateDisplay();
		}
		
	}
	
	public void addnewbroker(Table table3)
	{
		// metatraderconfig erzeugen
		Metaconfig me = new Metaconfig("##0######0######");
		
		SwtEditBrokerConfig sc = new SwtEditBrokerConfig();
		sc.init(display_glob, me, brokerview_glob, 1, tv_glob);
		buildBrokerliste(table3, 0);
		
	}
	
	public void deletebroker(Table table3)
	{
		MessageBox dialog = new MessageBox(display_glob.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to do delete the broker <" + glob_selectedBrokerShare + ">");
		if (dialog.open() != 32)
			return;
		
		brokerview_glob.removeBroker(glob_selectedBroker);
		brokerview_glob.SaveBrokerTable();
		buildBrokerliste(table3, 1);
		
	}
	
	public void showProfitGraphik()
	{
		// die tradeliste für einen bestimmten ea generieren und anzeigen
		Profit pro = selectedProfitelem_glob;
		
		if (pro == null)
		{
			Tracer.WriteTrace(10, "E:no element selected");
			return;
		}
		// die aktuelle profitliste laden
		Tradeliste eatradeliste = tv_glob.buildTradeliste(String.valueOf(pro.getMagic()), pro.getBroker(), -1,null);
		
		if (eatradeliste == null)
			return;
		
		Ea ea = tv_glob.getEaliste().getEa(pro.getMagic(), pro.getBroker());
		String headline = pro.getBroker() + ":" + pro.getSymbol() + ":" + pro.getMagic() + ":" + ea.getInfo() + ":"
				+ pro.getComment();
		
		Profitanzeige profanz = new Profitanzeige("Gewinnverlauf", eatradeliste, headline, null);
		
		// das demopanel im hauptscreen unten rechtes anzeigen
		// JPanel jp = Profitanzeige.createDemoPanel(eatradeliste, headline);
		
	}
	
	public void showBacktestGraphik()
	{
		// die tradeliste für einen bestimmten ea generieren und anzeigen
		Profit pro = selectedProfitelem_glob;
		
		if (pro == null)
		{
			Tracer.WriteTrace(10, "E:no element selected");
			return;
		}
		// die aktuelle profitliste laden
		Tradeliste eatradeliste = tv_glob.buildTradeliste(String.valueOf(pro.getMagic()), pro.getBroker(), -1,null);
		
		if (eatradeliste == null)
			return;
		
		Ea ea = tv_glob.getEaliste().getEa(pro.getMagic(), pro.getBroker());
		String eaname = ea.getEafilename();
		int period = ea.getPeriod();
		String symbol = ea.getSymbol();
		Mt4Backtester mtb = new Mt4Backtester();
		
		String brokername = pro.getBroker();
		Metaconfig metaconfig = brokerview_glob.getMetaconfigByBrokername(brokername);
		mtb.Install(Rootpath.getRootpath(), metaconfig.getAppdata());
		
		// hier weitermachen!!!!!!!!!!!!!!xxxxxxxxxxxxxxxxxxxx
		
		mtb.setvalues(eaname, "H1", symbol);
		mtb.makeBacktest();
		mtb.showResults();
		
	}
	
	public void showselprofits(Table table2)
	{
		// hier wird die gewinnkurve mehrerer EA´s in einer graphik angezeigt
		Tradeliste eatradeliste = tv_glob.buildTradelisteAllSel(0);
		if (eatradeliste == null)
			return;
		Profitanzeige profanz = new Profitanzeige("Gewinnverlauf", eatradeliste, "sum profits", null);
	}
	
	public void showselprofitstradelistportfolio(Table table2)
	{
		// hier wird die gewinnkurve mehrerer EA´s in einer liste
		Tradeliste eatradeliste = tv_glob.buildTradelisteAllSel(0);
		if (eatradeliste == null)
			return;
		SwtShowTradeliste st = new SwtShowTradeliste();
		st.init(display_glob, eatradeliste, tv_glob, "", "list");
	}
	
	public void showallprofit(Tradefilter tf, int portfolioflag, int maxprofanz)
	{
		
		// hier wird eine Tradeliste gebildet welche alle ea´s beinhaltet
		// Also eine Liste der Tradelisten
		// portfolioflag=1, dann werden die ganzen portfolios zuzsammengefalls
		// ArrayList<Tradeliste> alltradelist = new ArrayList<Tradeliste>();
		ArrayList<Tradeliste> alltradelist = null;
		
		if (portfolioflag == 0)
			alltradelist = tv_glob.buildAllTradeliste(tf, maxprofanz);
		else
			alltradelist = tv_glob.buildAllPortfolioliste();
		ShowAllProfitsConfig prof = new ShowAllProfitsConfig("Gewinnverlauf", tv_glob, alltradelist, brokerview_glob);
	}
	
	public void showallprofit2(Tradefilter tf, int portfolioflag, int maxprofanz, Profitliste profliste)
	{
		
		// hier wird eine Tradeliste gebildet welche alle ea´s beinhaltet
		// Also eine Liste der Tradelisten
		// portfolioflag=1, dann werden die ganzen portfolios zuzsammengefalls
		// ArrayList<Tradeliste> alltradelist = new ArrayList<Tradeliste>();
		ArrayList<Tradeliste> alltradelist = null;
		
		if (portfolioflag == 0)
			alltradelist = tv_glob.buildAllTradeliste(tf, maxprofanz);
		else
			alltradelist = tv_glob.buildAllPortfolioliste();
		ShowConfigAllP3 prof = new ShowConfigAllP3(display_glob, "Gewinnverlauf", tv_glob, alltradelist,
				brokerview_glob, profliste);
		
	}
	
	public void showUebersichtx(Tradefilter tf, int maxprofanz)
	{
		ArrayList<Tradeliste> alltradelist = tv_glob.buildAllTradeliste(tf, maxprofanz);
		ShowUebersicht sue = new ShowUebersicht("titel", tv_glob, alltradelist);
	}
	
	public void saveAndExit()
	{
		tv_glob.store();
		System.exit(0);
	}
	
	public void syncBroker(Table table2)
	{
		// hier werden die stati der ea mit dem wirklich installierten
		// abgeglichen
		tv_glob.syncBroker();
		tv_glob.ShowProfitTable();
		tv_glob.storeEaliste();
	}
	
	public void toggleOnOffBroker(Table table3)
	{
		brokerview_glob.toogleOnOffBroker(glob_selectedBroker);
		brokerview_glob.SaveBrokerTable();
		buildBrokerliste(table3, 0);
	}
	
	public void toggleOnOffEa()
	{
		
		// DisTool.waitCursor();
		tv_glob.toggleOnOffEas();
		tv_glob.ShowProfitTable();
		tv_glob.storeEaliste();
		// DisTool.arrowCursor();
		
	}
	
	public void toogleOnOffAutomatic()
	{
		// DisTool.waitCursor();
		tv_glob.toggleOnOffAutomatics();
		tv_glob.ShowProfitTable();
		tv_glob.storeEaliste();
		// DisTool.arrowCursor();
		
	}
	
	public int deleteEas()
	{
		return (tv_glob.deleteEas());
	}
	
	public void installAutoEa()
	{
		// installiert einen Einzigen EA
		Profit prof = selectedProfitelem_glob;
		String broker = prof.getBroker();
		
		Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(broker);
		if (meconf.getconnectedBroker() == null)
		{
			Mbox.Infobox("Info: no connected Broker");
			return;
		}
		
		if (meconf.getAccounttype() == 2)
		{
			Mbox.Infobox("please install from demoaccount");
			return;
		}
		Metaconfig meRealconf = brokerview_glob.getMetaconfigByBrokername(meconf.getconnectedBroker());
		
		if (meRealconf == null)
		{
			Mbox.Infobox("E: no Realbroker is connected to this demobroker !!");
			return;
		}
		
		Installer inst = new Installer();
		if (inst.InstalliereEinRealEaSystemFromDemobroker(tv_glob, prof, meconf, meRealconf,
				tv_glob.getEaliste()) == true)
		{
			Mbox.Infobox("EA magic<" + prof.getMagic() + "> installed on broker <" + meconf.getconnectedBroker() + ">");
		} else
		{
			Mbox.Infobox(
					"EA <" + prof.getMagic() + "> installation ERROR on broker<" + meconf.getconnectedBroker() + ">");
		}
	}
	
	public void showBroker(Display dis)
	{
		
		Viewer viewer = new Viewer();
		Shell shell = new Shell(dis);
		// shell.open();
		FileDialog dialog = new FileDialog(shell, SWT.SELECTED);
		
		dialog.setFilterPath(glob_selectedBrokerShare + "\\experts"); // Windows
																		// path
		dialog.open();
		
		// Ergebniss auswerten
		/*
		 * String fnam = new String(dialog.getFilterPath() + "\\" +
		 * dialog.getFileName());
		 * 
		 * SwtShowFile sf = new SwtShowFile(); sf.init(dis, fnam);
		 */
	}
	
	public void initTargetBrokerCombo(Combo targetbroker)
	{
		brokerview_glob.initTargetBrokerCombo(targetbroker);
	}
	
	public void copyEA(String targetbroker)
	{
		
		Metaconfig quell_meconf = brokerview_glob.getMetaconfigByBrokername(selectedProfitelem_glob.getBroker());
		String quellverz = quell_meconf.getMqlquellverz();
		Metaconfig ziel_meconf = brokerview_glob.getMetaconfigByBrokername(targetbroker);
		String zielverz = ziel_meconf.getMqlquellverz();
		
		if (quellverz.length() < 2)
		{
			Mbox.Infobox("Kein mql-Quellverzeichniss definiert");
			return;
		}
		if (zielverz.length() < 2)
		{
			Mbox.Infobox("Kein mql-Zielverzeichniss definiert");
			return;
		}
		Installer inst = new Installer();
		inst.copyEA(selectedProfitelem_glob.getMagic(), quellverz, zielverz);
		
	}
	
	public void lifecheck(Table table3)
	{
		int anz = brokerview_glob.getAnz();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mc = brokerview_glob.getElem(i);
			Tracer.WriteTrace(20, "Info:lifcheck Broker <" + mc.getBrokername() + ">");
			tv_glob.lifecheck(mc);
		}
		brokerview_glob.ShowBrokerTable(display_glob, table3, 0);
		brokerview_glob.SaveBrokerTable();
	}
	
	public void checkGd20_dep(Table table1, Table table2, Display dis, Text anzincommingtrades, Text anzeas,
			org.eclipse.swt.widgets.Label broker, int showflag)
	{
		// prüft bei allen ea´s ob der gd20 überschritten worden ist.
		// setze in der ea liste den wert.
		// 0=gd20 unterschritten
		// 1=gd20 einmal überschritten
		// 2=gd20 zweimal überschritten
		// showflag=0 dann wird nix auf dem display angezeigt
		AutomaticCheck_dep gd20 = new AutomaticCheck_dep(tv_glob, brokerview_glob, table1, dis, anzincommingtrades,
				anzeas);
		AutomaticCheck_dep.runAutomatic_dep(showflag, 0);
	}
	
	public void tooggleallprofits()
	{
		tv_glob.toogleallprofits();
	}
	
	public void showOnOffLog()
	{
		String fnam = Rootpath.getRootpath() + "\\EaOnOffLog.txt";
		SwtShowFile sf = new SwtShowFile();
		sf.init(display_glob, fnam);
		
	}
	
	public void searchSelId(String searchid)
	{
		tv_glob.searchSelId(searchid);
		
	}
	
	public void ConfigEa()
	{
		
		SwtConfigEa.startmain(display_glob, selectedProfitelem_glob, tv_glob, this,
				brokerview_glob.getAccounttype(glob_selectedBroker));
	}
	
	public void exportOneEaAndTradelist()
	{
		FileAccessDyn fd = new FileAccessDyn();
		Tradeliste eatradeliste = tv_glob.buildTradelisteAllSel(1);
		if (eatradeliste == null)
			return;
		
		// den Speicherort bestimmen
		String dirnam = MonDia.DirDialog(display_glob, Rootpath.getRootpath());
		
		// den ea suchen
		Trade trade = eatradeliste.getelem(0);
		Ea ea = tv_glob.getEaliste().getEa(trade.getMagic(), trade.getBroker());
		
		// den tradelistennamen bestimmen
		Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(trade.getBroker());
		// strname beinhaltet den ganzen Pfad
		String strname = ea.holeStrFilename(tv_glob.getEaliste(), meconf, trade.getComment(), 0);
		
		String tradelname = strname.substring(strname.lastIndexOf("\\") + 1).replace(".str", ".tradeliste");
		// das ist der zielname
		String fnam = dirnam + "\\" + tradelname;
		// die Tradeliste speichern
		eatradeliste.exportXmlTl(fnam);
		
		// den Eanamen bestimmen
		String mqlname = strname.substring(strname.lastIndexOf("\\") + 1).replace(".str", ".mq4");
		mqlname = mqlname.replace("[tp]", "");
		
		String eaquellname = meconf.getMqlquellverz() + "\\" + mqlname;
		String zielname = dirnam + "\\" + mqlname;
		// den Ea kopieren
		System.out.println("kopiere ea von <" + eaquellname + "> nach <" + zielname + ">");
		
		File equell = new File(eaquellname);
		if (equell.exists() == false)
			Mbox.Infobox("sourcefile <" + eaquellname + "> not exits");
		
		fd.copyFile2(eaquellname, zielname);
		
		DisTool.UpdateDisplay();
	}
	
	public void exportTradelist()
	{
		Tradeliste eatradeliste = tv_glob.buildTradelisteAllSel(1);
		if (eatradeliste == null)
			return;
		Viewer view = new Viewer();
		String fnam = view.fileRequester2(Display.getDefault(), "export tradelist", Rootpath.getRootpath(), SWT.SAVE);
		
		eatradeliste.exportXmlTl(fnam);
		DisTool.UpdateDisplay();
	}
	
	public void importTradelist()
	{
		// die Tradeliste in einem bestimmten "selektierten Broker" importieren
		MessageBox dialog = new MessageBox(display_glob.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to import new trades into broker <" + glob_selectedBroker + ">");
		if (dialog.open() != 32)
			return;
		
		/*
		 * Tradeliste eatradeliste = tv_glob.buildTradelisteAllSel(1); if (eatradeliste
		 * == null) return;
		 */
		Viewer view = new Viewer();
		String fnam = view.fileRequester2(Display.getDefault(), "import tradelist", Rootpath.getRootpath(),
				SWT.READ_ONLY);
		tv_glob.importTradeliste(fnam, glob_selectedBroker);
		DisTool.UpdateDisplay();
	}
	
	public void removeImported()
	{
		tv_glob.removeImported(glob_selectedBroker);
		DisTool.UpdateDisplay();
	}
	
	public void exportAllTradelistEncryped()
	{
		LockTradeliste lockTradeliste = new LockTradeliste();
		lockTradeliste.lockTradeliste();
		// die gesammtradeliste wird exportiert
		String fnam = Rootpath.getRootpath() + "\\data\\tra.txt";
		String fnamzip1 = Rootpath.getRootpath() + "\\data\\tra.txt.gzip";
		String fnamzip2 = Rootpath.getRootpath() + "\\data\\tra.txt.zip";
		String datenfilenamecr = fnamzip2 + ".cr";
		
		File fnamf = new File(fnam);
		File fnamzip1f = new File(fnamzip1);
		File fnamzip2f = new File(fnamzip2);
		
		if (fnamf.exists() == true)
			fnamf.delete();
		Tradeliste tl = tv_glob.getTradeliste();
		tl.exportTextTl(fnam);
		Archive.gzipFileFast(fnam);
		
		// das alte xml löschen
		if (fnamf.exists())
			fnamf.delete();
		
		// das .gzip in .zip umbenennen
		if (fnamzip1f.exists())
			fnamzip1f.renameTo(fnamzip2f);
		
		// verschlüsseln
		Crypto crypto = new Crypto();
		crypto.setProvider();
		try
		{
			crypto.encryptFile(fnamzip2, datenfilenamecr, "f15q8t93");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// die zips löschen
		if (fnamzip1f.exists())
			fnamzip1f.delete();
		if (fnamzip2f.exists())
			fnamzip2f.delete();
		
		// die Tradeliste freigeben
		lockTradeliste.delTradelock();
	}
	
	public void exportAllTradelist()
	{
		LockTradeliste lockTradeliste = new LockTradeliste();
		lockTradeliste.lockTradeliste();
		// die gesammtradeliste wird exportiert
		// den Namen abfragen
		String fpath = Rootpath.getRootpath() + "\\import_export";
		String fnam = MonDia.FileDialog(display_glob, fpath);
		if (fnam.contains(".gzip") == false)
			fnam = fnam.concat(".gzip");
		
		File fnamf = new File(fnam);
		
		if (fnamf.exists() == true)
			fnamf.delete();
		
		Tradeliste tl = tv_glob.getTradeliste();
		tl.exportTextTl(fnam);
		
		// die Tradeliste freigeben
		lockTradeliste.delTradelock();
		Mbox.Infobox("export of tradelist ok");
	}
	
	public void importAllTradelist()
	{
		// das file mit allen Tradelisten wird hier importiert
		// die EAs sind dann zwar nicht da, aber die Tradelisten drin
		// die Trades werden alle grün dargestellt
		MessageBox dialog = new MessageBox(display_glob.getActiveShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to import the Mastertradelist ??");
		if (dialog.open() != 32)
			return;
		
		Viewer view = new Viewer();
		String fnam = view.fileRequester2(Display.getDefault(), "import tradelist", Rootpath.getRootpath(),
				SWT.READ_ONLY);
		
		tv_glob.importAllTradelist(fnam);
		DisTool.UpdateDisplay();
	}
	
	public void showBacktest(boolean forceflag)
	{
		Profit pro = selectedProfitelem_glob;
		
		if (pro == null)
		{
			Tracer.WriteTrace(10, "E:no element selected");
			return;
		}
		
		Ea ea = tv_glob.getEaliste().getEa(pro.getMagic(), pro.getBroker());
		Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(pro.getBroker());
		String filename = meconf.getMqlquellverz() + "\\" + ea.getEafilename();
		
		if (filename.contains("null"))
		{
			filename = ea.holeStrFilename(tv_glob.getEaliste(), meconf, tv_glob.getComment(ea.getMagic()), 1);
		}
		
		filename = filename.replace(".mq4", ".jpg");
		filename = filename.replace(".str", ".jpg");
		
		// 1)falls jpg bild da ist
		if ((forceflag == false) && (FileAccess.FileAvailable(filename) == true))
			showBacktestJPG(filename);
		// 2)falls gif bild da ist
		else if ((forceflag == false) && (FileAccess.FileAvailable(filename.replace(".jpg", ".gif")) == true))
			showBacktestGif(filename.replace(".jpg", ".gif"));
		else
			// 3)kein bild dann schaue ob man es aus dem xml-file genieren kann
			showBacktestXml(filename);
	}
	
	private void showBacktestJPG(String fnam)
	{
		SwtPicture.mainx(fnam);
	}
	
	private void showBacktestGif(String fnam)
	{
		SwtPicture.mainx(fnam);
	}
	
	private boolean showBacktestXml(String storefilename)
	{
		// das bild wird anschliessend unter storefilename abgespeichtert
		
		// die tradeliste für einen bestimmten ea generieren und anzeigen
		Profit pro = selectedProfitelem_glob;
		
		if (pro == null)
		{
			Mbox.Infobox("please select a EA");
			return false;
		}
		int magic = pro.getMagic();
		String comment = tv_glob.getComment(magic);
		// hier wird der backtest aus dem xml geladen, da kein Bild dafür
		// vorliert
		Tradeliste eatradeliste = tv_glob.buildTradelisteBacktest(pro, comment);
		
		if (eatradeliste == null)
			return false;
		
		Ea ea = tv_glob.getEaliste().getEa(pro.getMagic(), pro.getBroker());
		String headline = pro.getBroker() + ":" + pro.getMagic() + ":" + ea.getInfo();
		
		Profitanzeige profanz = new Profitanzeige("Gewinnverlauf", eatradeliste, headline, storefilename);
		
		return true;
	}
	
	public void TradefilterConfig(Tradefilter tf)
	{
		TradeFilterConfig.showGUI(tf);
	}
	
	public void setSortkriteria()
	{
		SwtSetSortCriteria.mainx();
	}
	
	public void setInstfromDemobroker()
	{
		ArrayList<Profit> profitmenge = tv_glob.CalcSelectedProfitliste();
		SwtSetInstfrom.mainx(brokerview_glob, profitmenge, tv_glob.getEaliste());
	}
	
	public void brokerchangeposition(int direction, Table table3)
	{
		// direction=0: nach oben
		// direction=1: nach unten
		
		brokerview_glob.moveBroker(glob_selectedBroker, direction);
		brokerview_glob.ShowBrokerTable(display_glob, table3, 0);
	}
	
	public void restartAllMetatrader()
	{
		int anz = brokerview_glob.getAnz();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mc = brokerview_glob.getElem(i);
			Tracer.WriteTrace(20, "Info:Restart MT <" + mc.getBrokername() + ">");
			mc.Restart();
		}
	}
	
	public void autoconfig()
	{
		// hier werden alle Metatrader automatisch konfiguriert
		// die neuen werden als Demokonto hinzugefügt
		
		MetaStarter.KillAllMetatrader(1);
		// das metatraderrootverzeichniss holen
		Autoconfig autoconf = new Autoconfig();
		autoconf.configAllMetatrader(brokerview_glob);
		
	}
	
	public void startAllMt(int forceflag)
	{
		
		MetaStarter.StartAllMetatrader(brokerview_glob, forceflag);
		
	}
	
	public void stopAllMt(int forceflag)
	{
		
		Tracer.WriteTrace(20, "I:stop all Metatrader");
		MetaStarter.StopAllMetatrader(brokerview_glob, forceflag);
		
	}
	
	public void convertToPortable()
	{
		Installer inst = new Installer();
		inst.convertToPortable(Display.getDefault(), brokerview_glob);
	}
	
	public void replaceAllSymbols(int forceflag)
	{
		if ((GlobalVar.getMetatraderautostartstop() == 0) && (forceflag == 0))
		{
			Tracer.WriteTrace(20, "I:I stop all Metatrader because startstop=0 && forceflag=0");
			return;
			
		}
		MetaStarter.KillAllMetatrader(forceflag);
		
		SymbolReplaceList s = new SymbolReplaceList(brokerview_glob);
		s.ReplaceAllSymbols();
		s.ShowReplaceResults();
	}
	
	static public void checkBadBrokerconfig()
	{
		File fnam = new File(Rootpath.getRootpath() + "\\conf\\brokerconf.xml");
		File fnamold = new File(Rootpath.getRootpath() + "\\conf\\brokerconf.old");
		
		// wenn *.tmp < 0.5 *old ist, dann stimmt was nicht, breche ab.
		double lenold = fnamold.length();
		double lenfnam = fnam.length();
		
		if (lenfnam == 0)
			return;
		
		if (lenfnam > (lenold / 2))
		{
			// das neue ist länger, dann ist alles ok
		} else
		{
			// *.xml ist zu klein, dann restauriere
			Mbox.Infobox("found defekt <" + fnam.getAbsolutePath() + ">, I try to repair");
			FileAccess.copyFile(fnamold.getAbsolutePath(), fnam.getAbsolutePath());
			
		}
		
	}
}
