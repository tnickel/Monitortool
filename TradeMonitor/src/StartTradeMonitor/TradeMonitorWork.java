package StartTradeMonitor;

import filter.Tradefilter;
import gui.Mbox;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Tools;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import swtHilfsfenster.SwtCompareTradelist;
import swtHilfsfenster.SwtSetSortCriteria;
import swtHilfsfenster.SwtShowTradeliste;
import swtHilfsfenster.TradeFilterConfig;
import StartFrame.Tableview;
import charttool.Profitanzeige;
import charttool.ShowAllProfits;
import data.Ea;
import data.Profit;
import data.SMglobalConfig;
import data.ServerTradelisten;
import data.Tradeliste;

public class TradeMonitorWork
{
	static ServerTradelisten servertradelisten_glob=null;
	static Tableview tv_glob=null;
	private static Profit selectedProfitelem_glob = null;
	private static String table1name=null;
	private static String table2name=null; 
	static public Table LoadTradelisten(Table table)
	{
		//hier wird die Liste aller tradelisten aufgebaut
		servertradelisten_glob = new ServerTradelisten();
		servertradelisten_glob.loadUebersichtlisten();
		servertradelisten_glob.buildUebersichtsTable( table);
		return (table);
	}
	static public void LoadShowProfittable(Display dis, Tradefilter tf,Table table2,String tradelistenname)
	{
		//die tableview initialisieren
		tv_glob = new Tableview();
		tv_glob.init(dis,null,tf,table2,null,0);
		
		//die Tradeliste in die tableview aufnehmen
		tv_glob.addTradeliste(servertradelisten_glob.getTradeliste(tradelistenname));
		//die profittabelle berechnen
		tv_glob.CalcProfitTable(null, 1);
		// die profittabelle anzeigen
		tv_glob.ShowProfitTable();
	}
	static public void setTable1Name(String name)
	{
		table1name=name;
	}
	static public void setTable2Name(String name)
	{
		table2name=name;
	}
	static public String selectEa(int nr)
	{
		//das Tableitem der Zeile nr holen
		TableItem ti=tv_glob.getTableItemTable2(nr);
		String magicstring=ti.getText(1);
		
		Profit prof = tv_glob.getprofitelem(nr);
		selectedProfitelem_glob = prof;
		return magicstring;
	}
	

	private static Profit getProfEa(String table2name)
	{
		//holt den zum table2 item gehöhrigen profit
		
		int nr=SG.get_zahl(table2name);
		//das Tableitem der Zeile nr holen
		TableItem ti=tv_glob.getTableItemTable2(nr);
		String magicstring=ti.getText(1);
		
		Profit prof = tv_glob.getprofitelem(nr);
		selectedProfitelem_glob = prof;
		return prof;
		
	}
	
	
	static public void showTradelist()
	{
		String magic=String.valueOf(selectedProfitelem_glob.getMagic());
		String broker=selectedProfitelem_glob.getBroker();
		
		Tradeliste etl = tv_glob.buildTradeliste(magic,broker);
		SwtShowTradeliste st = new SwtShowTradeliste();
		st.init(Display.getDefault(), etl,tv_glob,magic,broker);
	}
	static public void showProfitGraphik()
	{
		// die tradeliste für einen bestimmten ea generieren und anzeigen
		Profit pro = selectedProfitelem_glob;

		// die aktuelle profitliste laden
		Tradeliste eatradeliste = tv_glob.buildTradeliste(String.valueOf(pro.getMagic()),
				pro.getBroker());

		if (eatradeliste == null)
			return;

		Ea ea = tv_glob.getEaliste().getEa(pro.getMagic(), pro.getBroker());
		String headline = pro.getBroker() + ":" + pro.getSymbol() + ":"
				+ pro.getMagic() + ":" + ea.getInfo() + ":" + pro.getComment();

		Profitanzeige profanz = new Profitanzeige("Gewinnverlauf",
				eatradeliste, headline, null);

		// das demopanel im hauptscreen unten rechtes anzeigen
		// JPanel jp = Profitanzeige.createDemoPanel(eatradeliste, headline);
	}
	static public void showallprofit(Tradefilter tf, int portfolioflag, int maxprofanz)
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
		ShowAllProfits prof = new ShowAllProfits("Gewinnverlauf", tv_glob,
				alltradelist);
	}
	static public void TradefilterConfig(Tradefilter tf)
	{
		TradeFilterConfig.showGUI(tf);

	}
	static public void SetSortCriteria()
	{
		SwtSetSortCriteria.mainx();
	}
	static public void CompareTradelist()
	{
		SwtCompareTradelist sc = new SwtCompareTradelist();
		ArrayList<Integer> maglist = tv_glob.CalcSelectedMagics();
		sc.init(Display.getDefault(), selectedProfitelem_glob, tv_glob, maglist);
	}
	static public void getMetatrader()
	{
		Profit profit=getProfEa(table2name);
		String broker=profit.getBroker();
		
		
		String cmddir = SMglobalConfig.getCmddir();
		String fnam=cmddir+"\\"+table1name.replace(".zip","")+"___"+broker+"___.start";
		File fnamf= new File(fnam);
		if(fnamf.exists()==true)
		{
			Mbox.Infobox("W:The command <"+fnam+"> already in progress");
			return;
		}
		Mbox.Infobox("I:You will get <"+fnam+"> in the next days");
		Inf inf= new Inf();
		inf.setFilename(fnam);
		inf.writezeile("started at"+Tools.get_aktdatetime_str());
		inf.close();
		
		if(fnamf.exists()==true)
		{
			Mbox.Infobox("I: all ok, please wait and look in the folder exportdir in the next days");
		}
		else
			Mbox.Infobox("ERROR: can´t generate <"+fnam+">");
		
	}
	static public void getAllMetatrader()
	{
		
		
		
		String cmddir = SMglobalConfig.getCmddir();
		String fnam=cmddir+"\\"+table1name.replace(".zip","")+"___CHECKALLSYSTEM___.start";
		File fnamf= new File(fnam);
		if(fnamf.exists()==true)
		{
			Mbox.Infobox("W:The command <"+fnam+"> already in progress");
			return;
		}
		Mbox.Infobox("I:You will get <"+fnam+"> in the next days");
		Inf inf= new Inf();
		inf.setFilename(fnam);
		inf.writezeile("started at"+Tools.get_aktdatetime_str());
		inf.close();
		
		if(fnamf.exists()==true)
		{
			Mbox.Infobox("I: all ok, please wait and look in the folder exportdir in the next days");
		}
		else
			Mbox.Infobox("ERROR: can´t generate <"+fnam+">");
		
	}
}
