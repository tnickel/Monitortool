package StartFrame;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import Sync.Lock;
import charttool.Profitanzeige;
import data.Ea;
import data.EaStatus;
import data.Ealiste;
import data.GlobalVar;
import data.HistoryExpired;
import data.Historyexporter;
import data.Marklines;
import data.Metaconfig;
import data.Profit;
import data.Profitliste;
import data.Rootpath;
import data.TableViewBasic;
import data.Trade;
import data.Tradeanzahl;
import data.Tradeliste;
import filter.Tradefilter;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Swttool;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;
import modtools.AutoCreator;
import modtools.FsbPortfolioEa;
import modtools.Networker_dep;
import modtools.Toogler;
import montool.MonDia;
import mtools.DisTool;
import mtools.Mlist;

public class Tableview extends TableViewBasic
{
	// die Tradeliste beinhaltet alle trades aller EA´s
	// Die Tradeliste wird ebenfalls auf platte gespeichert und ggf. ergänzt
	// wenn neue Trades hinzukommen.
	
	private Tradeliste tl = new Tradeliste(null);
	private Profitliste pl_glob = new Profitliste();
	private HashSet<String> profitmenge = new HashSet<String>();
	private Ealiste eal_glob = new Ealiste();
	private Brokerview brokerview_glob = new Brokerview();
	private Networker_dep networker_glob = new Networker_dep();
	private Table table2_glob = null;
	private Tradefilter tf_glob = null;
	private Display display_glob = null;
	private ProgressBar progressbar_glob = null;
	static private boolean table2lastchecked = false;
	static private int lastfoundpos = 0;
	
	public void init(Display dis, Brokerview bview, Tradefilter tf, Table table, ProgressBar pb, int forceflag)
	{
		progressbar_glob = pb;
		table2_glob = table;
		display_glob = dis;
		brokerview_glob = bview;
		tf_glob = tf;
		tl.initTL(bview, forceflag, tf);
		pl_glob.init();
		eal_glob.init();
	}
	
	public ArrayList<String> calcBrokerliste(int magic)
	{
		// bestimmt die brokerliste für eine bestimmte magic
		return (tl.calcBrokerliste(magic));
	}
	
	public Tradeanzahl calcTradeanzahl()
	{
		Tradeanzahl tr = new Tradeanzahl();
		tr.setAnztrades(tl.getsize());
		tr.setAnzprofits(pl_glob.getsize());
		return tr;
	}
	
	public void setLotsize(String broker, int magic, float lotsize)
	{
		Ea ea = eal_glob.getEa(magic, broker);
		ea.setReallotsize(brokerview_glob, lotsize);
	}
	
	public String getLotsize_str(String broker, int magic)
	{
		Ea ea = eal_glob.getEa(magic, broker);
		if (ea == null)
			return "empty";
		String lotstr = ea.getlotsize_str(brokerview_glob);
		return (lotstr);
	}
	
	public void store()
	{
		tl.store(tf_glob);
		eal_glob.store(0);
	}
	
	public Ealiste getEaliste()
	{
		return eal_glob;
	}
	
	public void storeEaliste()
	{
		eal_glob.store(0);
	}
	
	public Tradeliste getTradeliste()
	{
		return tl;
	}
	
	public String getComment(int magic)
	{
		int anz = tl.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tl.getelem(i);
			if (tr.getMagic() == magic)
			{
				String comment = tr.getComment();
				comment = comment.replace("[tp]", "");
				comment = comment.replace("[sl]", "");
				return comment;
			}
		}
		return ("id not found");
	}
	
	public String getFiledata(Metaconfig mc)
	{
		String filedata = mc.getFiledata();
		
		// hack falls filedata nicht initialisiert
		if (filedata == null)
			filedata = mc.getMqldata() + "\\files";
		return filedata;
	}
	
	public void LoadTradeTable(Metaconfig mc, Display dis, int nostopflag, int showflag, int onlyopentrades)
	{
		// Hier werden die Trades für einen Broker eingelesen
		
		// nocanceledflag: falls dies flag gesetzt ist werden keine canceled
		// orders angezeigt
		// normflag:falls normflag =true werden die gewinne auf 0.1 Lot
		// umgerechnet
		// fnam: verzeichnissname das geshared wurde
		// broker: brokername der angezeigt wird
		// showopenorders: falls open orders aktiviert ist dann werden auch die
		// open orders geladen
		// falls nostopflag ==1 ist dann wird nicht gestoppt
		// showflag=0; wenn 0 dann wird nix auf dem display ausgegeben
		
		// Hier wird eine Tabelle in einer Struktur eingeladen
		
		boolean nocanceledflag = tf_glob.isNocancel();
		boolean showopenorders = tf_glob.isShowopenorders();
		boolean normflag = tf_glob.isProfitnormalisierung();
		boolean tradefilterstartflag = tf_glob.isTradefilterbutton();
		String startdate = tf_glob.getTradestartdate();
		
		String filedata = getFiledata(mc);
		
		String broker = mc.getBrokername();
		String maxdate = "";
		
		Lock.waitFreeLock(filedata + "/exporter.lock");
		Lock.waitFreeLock(filedata + "/monitor.lock");
		Lock.lock(filedata + "/monitor.lock", Rootpath.getRootpath());
		
		String fnam1 = filedata + "/history_small.txt";
		String fnam2 = filedata + "/history.txt";
		String fnam3 = filedata + "/history_open.txt";
		String fnam5 = filedata + "/history_transfer.txt";
		String dirnam6 = mc.getAppdata() + "\\tester\\files\\Ac_Entwicklungen";
		
		if ((new File(fnam1).exists() == true) && (GlobalVar.getAutocreatormode() == 0) && (onlyopentrades == 0))
			readTradesFile(tl, fnam1, nocanceledflag, showopenorders, normflag, mc, tf_glob);
		// wenn kein history.txt und kein history_transfer.txt
		// dann gibt es nix zu lesen
		if ((new File(fnam2).exists() == false) && (new File(fnam5).exists() == false)
				&& (new File(dirnam6).exists() == false) && (onlyopentrades == 0))
		{
			Lock.unlock(filedata + "/monitor.lock");
			Tracer.WriteTrace(20, "W:no file <" + fnam2 + "> and no file<" + fnam5 + ">");
			return;
		}
		if ((new File(fnam2).exists() == true) && (GlobalVar.getAutocreatormode() == 0) && (onlyopentrades == 0))
		{
			Tracer.WriteTrace(20, "I: read histroy.txt  <"+fnam2+">");
			maxdate = readTradesFile(tl, fnam2, nocanceledflag, showopenorders, normflag, mc, tf_glob);
		}
		if ((new File(fnam3).exists() == true) && (onlyopentrades == 1))
		{
			Tracer.WriteTrace(20, "I: read histroy.txt  <"+fnam3+">");
			readTradesFile(tl, fnam3, nocanceledflag, true, normflag, mc, tf_glob);
		}	
		// a)fasse die expired zu einem datenfile zusammen
		// b)und lade dann das Datenfile
		if ((tf_glob.isLoadexpired() == true) && (GlobalVar.getAutocreatormode() == 0) && (onlyopentrades == 0))
		{
			String fna = filedata + "\\history_expset_";
			File hist_exp_f = new File(fna);
			
			HistoryExpired hi = new HistoryExpired(fna);
			hi.CompressExpiredDatabase(filedata);
			
			if (hist_exp_f.exists() == true)
			{
				Tracer.WriteTrace(20, "I: read histroy.txt  <"+fna+">");
				Mlist.add("Load expired<" + broker + "><" + filedata + ">");
				readTradesFile(tl, fna, nocanceledflag, showopenorders, normflag, mc, tf_glob);
			}
		}
		
		// lade die transfer
		if ((new File(fnam5).exists() == true) && (GlobalVar.getAutocreatormode() == 0) && (onlyopentrades == 0))
		{	
			Tracer.WriteTrace(20, "I: read histroy.txt  <"+fnam5+">");
			readTradesFile(tl, fnam5, nocanceledflag, showopenorders, normflag, mc, tf_glob);
		}
		// lade die Daten vom Auto Creator
		if ((new File(dirnam6).exists() == true) && (GlobalVar.getAutocreatormode() == 1) && (onlyopentrades == 0))
		{
			Tracer.WriteTrace(20, "I: read histroy.txt  <"+dirnam6+">");
			readTradesAutoCreator(tl, dirnam6, nocanceledflag, showopenorders, normflag, mc, tf_glob);
		}
		Lock.unlock(filedata + "/monitor.lock");
		
		if (maxdate.length() < 2)
		{
			// Mbox.Infobox("Warning metatrader<"+mc.getBrokername()+"> hat noch keine EA´s
			// -->Stop");
			return;
		}
		
		// das erste (älteste datum in der mc speichern)
		if (GlobalVar.getAutocreatormode() == 1)
			mc.setDatumDesErstenTrades(maxdate);
		
	}
	
	public void ShowTradeTable(Display dis, Table table, String brokername, int maxentrys, int forcesortflag)
	{
		tl.ShowTradeTable(dis, table, brokername, maxentrys, tl, tf_glob, forcesortflag);
	}
	
	public void CalcProfitTable(String brokername, int tradefilterflag)
	{
		// falls brokername != null wird nur ein bestimmter broker betrachtet
		pl_glob.init();
		int anz = tl.getsize();
		
		Tracer.WriteTrace(20, "build profittable");
		// erst die profitliste aufbauen
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tl.getelem(i);
			
			// falls nur ein bestimmter broker gewünscht
			if (brokername != null)
				if (tr.getBroker().equals(brokername) == false)
					continue;
				
			// das element wird in die profitliste eingruppiert
			pl_glob.addelem(tr);
		}
		
		// dann den profitfaktor und drawdown berechnen
		pl_glob.calcDrawdowns();
		pl_glob.calcProfitfaktoren();
		pl_glob.calcOnoff(eal_glob);
		pl_glob.calcPz1(brokerview_glob);
		
		// die unerwünschten Profite aus der Profitliste wieder rausfiltern
		if (tradefilterflag == 1)
		{
			anz = pl_glob.getsize();
			for (int i = 0; i < anz; i++)
			{
				Profit prof = pl_glob.getelem(i);
				
				if (tf_glob.checkConditions(prof) == false)
				{
					pl_glob.delelem(i);
					i--;
					anz--;
				}
			}
		}
		
		// falls nur ea´s angezeigt werden sollen wo die automatic an ist
		
		if ((tradefilterflag == 1) && (tf_glob.isAutomaticselection() == true))
		{
			anz = pl_glob.getsize();
			for (int i = 0; i < anz; i++)
			{
				Profit prof = pl_glob.getelem(i);
				Ea ea = eal_glob.getEa(prof.getMagic(), prof.getBroker());
				if (ea.getAuto() == 0)
				{
					// lösche eintrag da automatic ==0
					pl_glob.delelem(i);
					i--;
					anz--;
				}
			}
		}
		
		// dann die Infos setzen
		anz = pl_glob.getsize();
		for (int i = 0; i < anz; i++)
		{
			Profit prof = pl_glob.getelem(i);
			Ea ea = eal_glob.getEa(prof.getMagic(), prof.getBroker());
			
			if (ea == null)
				continue;
			
			if (ea.getInfo() != null)
				prof.setInfo1(ea.getInfo());
			
			if (ea.getInfo2() != null)
				prof.setInfo2(ea.getInfo2());
		}
		
		// show only EAs which are installed
		anz = pl_glob.getsize();
		for (int i = 0; i < anz; i++)
		{
			Profit prof = pl_glob.getelem(i);
			Ea ea = eal_glob.getEa(prof.getMagic(), prof.getBroker());
			
			brokername = prof.getBroker();
			Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(brokername);
			if (meconf.getShowOnlyInstalledEas() == 1)
				if ((ea != null) && (ea.checkIfInstalled(meconf) == false))
				{
					// lösche eintrag da ea nicht installiert
					
					pl_glob.delelem(i);
					i--;
					anz--;
				}
			
		}
		
		// die EAliste um die neuen EAs erweitern erweiten
		eal_glob.expand(pl_glob);
		
		// ganz zum Schluss die Profitfaktoren und drawdowns berechnen
		
	}
	
	public void checkProfitliste()
	{
		
		Brokerview bv = brokerview_glob;
		
		int profanzahl = pl_glob.getsize();
		for (int j = 0; j < profanzahl; j++)
		{
			
			Profit prof = pl_glob.getelem(j);
			// falls realbroker, dann überprüfe ob es einen zugehörigen demobroker gibt
			if ((bv != null) && (bv.getAccounttype(prof.getBroker()) == 2))
			{
				Tracer.WriteTrace(20, "CheckProfitliste: broker <"+prof.getBroker()+"> ist ein Realbroker und wird überprüft");
				
				// Dann hole für dies profitelement die tradeliste
				// das müssen wir so machen da ein channel mehrere Trades beinhalten kann.
				// d.h. z.B. unter kanal 8 können mehrere offene Trades sein und wir müssen
				// jetzt checken ob diese
				// trades auch im demokonto zugehörige Eas haben die eingeschaltet sind.
				
				// beim realbroker ist die magic der chanel
				
				String broker = prof.getBroker();
				int magic=prof.getMagic();
				String comment=prof.getComment();
				String channel = String.valueOf(magic);
				
				if(magic==0)
				{
					continue;
					//because this is a trade by hand, this should not be checked
					
				}
				
				// jetzt muss jeder Trade dieser Tradeliste,des Realaccount überprüft werden
				Tradeliste tl = buildTradeliste(channel, broker, Integer.valueOf(channel),comment);
				
				int anztrades = tl.getsize();
				for (int i = 0; i < anztrades; i++)
				{
					
					// überprüfe den nächsten Trades des Realbrokers
					Trade tr = tl.getelem(i);
					String realbroker = tr.getBroker();
					int channelRealbroker = tr.getMagic();
					String closetime=tr.getClosetime();
					
					if(closetime.equals("2050-01-01 00:00:00")==false)
					{
						Tracer.WriteTrace(20, "W: I will check only Trades on realbroker witch are open this trade is not open because closetime is<"+closetime+">");
						continue;
					}
					
					Tracer.WriteTrace(20, "I:Check Trade on Realbroker <"+realbroker+"> comment<"+tr.getComment()+">");
					// suche den Trade im Demobroker
					Profit pldemo = pl_glob.searchProfitOnDemobroker(brokerview_glob,realbroker, tr.getComment(), channelRealbroker);
					if(pldemo==null)
					{
						String em="E: can´t find Trade with comment <"+tr.getComment()+"> on channel<"+channelRealbroker+"> on demobroker (possible broker is down or switched off)";
						Mlist.add(em);
						Tracer.WriteTrace(10, em);
						continue;
					}
					int magdemo = pldemo.getMagic();
					String demobroker = pldemo.getBroker();
					
					// hier muss noch geprüft weren ob dieser Ea = On ist, da ein trade auf dem
					// Realaccount offen ist
					int status = eal_glob.getOn(magdemo, demobroker);
					if (status == 0)
					{
						String em="E:Error: Found Trade on Demobroker but this EA was off: Realaccount <" + realbroker + "> channel<"
								+ channelRealbroker + "> comment<" + tr.getComment() + "> but NOT ON on Demoaccount <" + demobroker
								+ "> channel <" + channelRealbroker + "> comment<" + tr.getComment() + "> magic<" + magdemo + ">";
						Mlist.add(em);
						Tracer.WriteTrace(10, em);
						continue;
					}
					
				}
				
			}
		}
	}
	
	public void dumpProfitliste(String name)
	{
		String outfile="c:\\tmp\\"+name+".txt";
		File outfile_f=new File(outfile);
		if(outfile_f.exists())
				outfile_f.delete();
		Inf inf=new Inf();
		inf.setFilename(outfile);
		int profanzahl = pl_glob.getsize();
		for(int j=0; j<profanzahl; j++)
		{
		
		
			Profit prof = pl_glob.getelem(j);
			inf.writezeile("Magic="+prof.getMagic()+" Comment="+prof.getComment()+" broker=="+prof.getBroker() +"#trades="+prof.getGestrades());
		}
		inf.close();
	}
	
	
	
	public void ShowProfitTable()
	{
		// Die Profittable ist die grosse Tabelle im Hauptscreen in der Mitte
		Tracer.WriteTrace(50, "Show Profittable for debug");
		ProgressBar pb = progressbar_glob;
		Table table = table2_glob;
		Display dis = display_glob;
		Brokerview bv = brokerview_glob;
		Tradefilter tf = tf_glob;
		Color magenta = dis.getSystemColor(SWT.COLOR_MAGENTA);
		Color green = dis.getSystemColor(SWT.COLOR_GREEN);
		Color blue = dis.getSystemColor(SWT.COLOR_BLUE);
		Device device = Display.getCurrent();
		Color lightblue = new Color(device, 0, 0, 80);
		Color dmagenta = dis.getSystemColor(SWT.COLOR_DARK_MAGENTA);
		Color red = dis.getSystemColor(SWT.COLOR_RED);
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		float gew10 = 0, gew30 = 0, gewall = 0;
		
		if (pb != null)
			pb.setMinimum(0);
		
		Swttool.baueTabellenkopfDispose(table,
				"Ind#Magic#Symb#F#Info1#LOT#AUT#On#cL#pz1#tr7#prof7#tr30#prof30#trALL#profALL#PF#DD#comment#info2#broker#RealBroker#inst from");
		
		Tracer.WriteTrace(20, "show profittable");
		pl_glob.sortliste();
		
		int profanzahl = pl_glob.getsize();
		
		if (pb != null)
			pb.setMaximum(profanzahl + 1);
		for (int j = 0; j < profanzahl; j++)
		{
			
			Profit prof = pl_glob.getelem(j);
			if (pb != null)
				pb.setSelection(j);
			
			gew10 = prof.getZehntagegewinn();
			gew30 = prof.getDreizigtragegewinn();
			gewall = prof.getGesgewinn();
			Ea ea = eal_glob.getEa(prof.getMagic(), prof.getBroker());
			
			String comment = prof.getComment();// tl.getComment(prof.getMagic());
			
			TableItem item = new TableItem(table, SWT.CHECK);
			
			int gd20fl = ea.getGd20flag();
			
			if (gd20fl == 1)
				item.setForeground(0, green);
			else if (gd20fl == 2)
				item.setForeground(0, blue);
				
			// falls automatic an ist und >5 trades und alles im minus dann rote
			// zeile
			if ((gew10 < 0) && (gew30 < 0) && (gewall < 0) && (prof.getGestrades() > 5) && (ea.getAuto() == 1))
			{
				item.setBackground(magenta);
			}
			// index
			item.setText(0, String.valueOf(j));
			// magic
			item.setText(1, String.valueOf(prof.getMagic()));
			
			// symb
			String symb = prof.getSymbol();
			item.setText(2, symb);
			
			// info
			String info = ea.getTradelogikinfo();
			if (prof.getImportedColor() != 0)
				info = info + "I";
			item.setText(3, info);
			// info1
			if (ea.getInfo() != null)
				item.setText(4, ea.getInfo());
			
			if (brokerview_glob != null)
				item.setText(5, ea.getlotsize_str(brokerview_glob));
			
			// auto
			item.setText(6, String.valueOf(ea.getAuto()));
			// on
			if (ea.getOn() == 1)
				item.setBackground(7, green);
			item.setText(7, String.valueOf(ea.getOn()));
			
			int cl = prof.calcConsecLooses();
			if (cl >= 2)
				item.setBackground(8, dmagenta);
			if (cl >= 3)
				item.setBackground(8, red);
			item.setText(8, String.valueOf(prof.calcConsecLooses()));
			
			// pz1
			Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(prof.getBroker());
			if (meconf.getAccounttype() == 4)
				item.setText(9, AutoCreator.getPkz1(meconf, comment));
			else
				item.setText(9, "0");
			// last7trades
			item.setText(10, String.valueOf(prof.getAnztradeslastzehn()));
			item.setText(11, SG.kuerzeFloatstring(String.valueOf(gew10), 2));
			// last30trades
			item.setText(12, String.valueOf(prof.getAnzmonatstrades()));
			item.setText(13, SG.kuerzeFloatstring(String.valueOf(gew30), 2));
			// gestrades
			item.setText(14, String.valueOf(prof.getGestrades()));
			// gesgewinn
			item.setText(15, SG.kuerzeFloatstring(String.valueOf(gewall), 2));
			// profitfaktor
			item.setText(16, SG.kuerzeFloatstring(String.valueOf(prof.getProfitfaktor()), 2));
			// drawdown
			item.setText(17, SG.kuerzeFloatstring(String.valueOf(prof.getDrawdown()), 2));
			// comment
			item.setText(18, comment);
			// info2
			if (ea.getInfo2() != null)
				item.setText(19, ea.getInfo2());
			
			// broker
			item.setText(20, String.valueOf(prof.getBroker()));
			// conbroker
			if ((prof.getBroker() != null) && (bv != null))
				item.setText(21, bv.getConBroker(prof.getBroker()));
				
			// instfrom
			// falls das ein Realaccount ist
			if ((bv != null) && (bv.getAccounttype(prof.getBroker()) == 2))
			{
				String instfrom = ea.getInstFrom();
				
				if (instfrom != null)
					item.setText(22, instfrom);
			} else
				item.setText(22, "-");
			
		}
		
		if (profanzahl > 0)
			for (int i = 0; i < 23; i++)
			{
				table.getColumn(i).pack();
			}
		
	}
	
	public void CalcSelectedProfits()
	{
		// Jedes Profitelement was in der Tabelle selektiert worden ist wird in
		// dieser Menge aufgenommen
		// Menge beinhaltet "Broker"+Magic
		// Z.B. Alpari:13454545
		
		profitmenge.clear();
		
		// DAs ist in der Profitliste
		int profanzahl = pl_glob.getsize();
		
		// Dast zeigt die Tablle an
		int anz = table2_glob.getItemCount();
		
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			if (item.getChecked() == true)
			{
				Profit prof = pl_glob.getelem(i);
				profitmenge.add(prof.getBroker() + ":" + prof.getMagic());
				
			}
		}
	}
	
	public ArrayList<Integer> CalcSelectedMagics()
	{
		ArrayList<Integer> maglist = new ArrayList<Integer>();
		int anz = table2_glob.getItemCount();
		
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			if (item.getChecked() == true)
			{
				Profit prof = pl_glob.getelem(i);
				maglist.add(prof.getMagic());
			}
		}
		return maglist;
	}
	
	public ArrayList<Profit> CalcSelectedProfitliste()
	{
		// Jedes Profitelement was in der Tabelle selektiert worden ist wird in
		// dieser Liste aufgenommen
		ArrayList<Profit> profitliste = new ArrayList<Profit>();
		
		// Das zeigt die Tablle an
		int anz = table2_glob.getItemCount();
		
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			if (item.getChecked() == true)
			{
				Profit prof = pl_glob.getelem(i);
				profitliste.add(prof);
				
			}
		}
		return profitliste;
	}
	
	public Tradeliste showEinzelTradeliste(Tradeliste einzeltradeliste, Display dis, Table table, Marklines marklines,
			int sideflag, Text pftext, Text ddtext)
	
	{
		// hier wird die einzeltradeliste in einer tabelle dargestellt
		// ausserdem wird der profitfaktor und der drawdown ausgegeben
		// einzeltradeliste: dies ist die liste mit den Trades
		// dis: display
		// table: dies ist die table die aufgebaut wird
		// marklines: dies ist die marklinestruktur die die farben berechenet
		// sideflag: 0, dann ist dies die linke seite der tabelle, 1 = rechte seite der
		// tabelle
		// gelben Hintergrund dargestellt
		// pftext:profitfaktor anzeigen wenn !=null
		// ddtext:drawdown anzeigen wenn !=null
		// farbanzeige:
		// gemeinsammer bereich farblich anzeigen (grau) marklines=1
		// gleiche Trades farblich anzeigen (dunkelgrün) marklines=2
		// prozentanzeige wie die beiden Strategien harmonieren
		
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		Color yellow = dis.getSystemColor(SWT.COLOR_DARK_YELLOW);
		Color grey = dis.getSystemColor(SWT.COLOR_GRAY);
		Color green = dis.getSystemColor(SWT.COLOR_GREEN);
		Color blue = dis.getSystemColor(SWT.COLOR_BLUE);
		Swttool.baueTabellenkopfDispose(table,
				"Ind#Magic#Transac#Broker#dir#Open#Openprice#Close#Closeprice#Profit#Pips#Sum#Gd5#dur(min)#Comment#Comment2");
		
		// hier werden für eine selekte magic/broker sämtliche Trades
		// aufgelistet
		
		// Tradeliste einzeltradeliste = buildTradeliste(magic, broker);
		einzeltradeliste.sortliste();
		einzeltradeliste.Reverse();
		
		int zeilcount = einzeltradeliste.getsize();
		for (int i = 0, j = 0; i < zeilcount; i++)
		{
			Trade tr = einzeltradeliste.getelem(i);
			// if ((tr.getBroker().equals(broker)) && (tr.getMagic() == magic))
			{
				// der trade passt
				TableItem item = new TableItem(table, SWT.NONE);
				
				if (marklines != null)
				{
					// hole die markierung
					int markline = marklines.getFarbe(sideflag, i);
					if (markline == 1)
						item.setBackground(grey);
					else if (markline == 2)
						item.setBackground(green);
				}
				item.setText(0, String.valueOf(j));
				j++;
				item.setText(1, String.valueOf(tr.getMagic()));
				item.setText(2, String.valueOf(tr.getTransactionnumber()));
				item.setText(3, tr.getBroker());
				item.setText(4, tr.calcDirection());
				item.setText(5, tr.getOpentime());
				item.setText(6, SG.kuerzeFloatstring(String.valueOf(tr.getOpenprice()), 5));
				item.setText(7, tr.getClosetime());
				item.setText(8, SG.kuerzeFloatstring(String.valueOf(tr.getCloseprice()), 5));
				item.setText(9, SG.kuerzeFloatstring(String.valueOf(tr.getProfit()), 2));
				
				String pipval = String.valueOf(Math.abs(tr.getCloseprice() - tr.getOpenprice()) * 10000);
				// die nachkommastellen entfernen
				if (pipval.contains("."))
					pipval = pipval.substring(0, pipval.indexOf("."));
				
				item.setText(10, pipval);
				item.setText(11, SG.kuerzeFloatstring(String.valueOf(einzeltradeliste.get_tsumx(i)), 2));
				item.setText(12, SG.kuerzeFloatstring(String.valueOf(einzeltradeliste.calc_gdx(i, 5)), 2));
				item.setText(13, tr.calcDuration());
				String comment = tr.getComment();
				item.setText(14, comment);
				
				if (tr.getImportedcolor() != 0)
				{
					item.setForeground(blue);
					item.setText(15, "imported from:" + tr.getImportedfrom());
				}
			}
		}
		for (int i = 0; i < 16; i++)
		{
			table.getColumn(i).pack();
		}
		
		// drawdown anzeigen
		if (ddtext != null)
			ddtext.setText(String.valueOf(einzeltradeliste.calcDrawdown()));
		
		// profitfaktor anzeigen
		if (pftext != null)
			pftext.setText(String.valueOf(einzeltradeliste.calcProfitfactor()));
		
		return einzeltradeliste;
	}
	
	public Profit getprofitelem(int index)
	{
		if (index < pl_glob.getsize())
			return pl_glob.getelem(index);
		else
			return null;
	}
	
	public Trade gettradeelem(int index)
	{
		return tl.getelem(index);
	}
	
	public int getChannel(String broker)
	{
		Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(broker);
		int channel = meconf.getTradecopymagic();
		return channel;
	}
	
	public Tradeliste buildTradelisteBacktest(Profit profelem, String tradecomment)
	{
		// Hier wird die Tradeliste für ein Profitelement aufgebaut
		
		if (profelem == null)
		{
			Tracer.WriteTrace(10, "Info: please select EA");
			return null;
		}
		
		int magic = profelem.getMagic();
		String broker = profelem.getBroker();
		
		// baut eine Profitliste auf wo nur ein bestimmter Ea vorkommt
		Tradeliste eatl = new Tradeliste(null);
		
		Ea ea = eal_glob.getEa(magic, broker);
		Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(broker);
		
		// den str-filenamen holen
		String filename = ea.holeStrFilename(eal_glob, meconf, tradecomment, 1);
		
		if (FileAccess.FileAvailable(filename) == false)
		{
			Mbox.Infobox("Problem with file <" + filename + ">");
			return null;
		}
		
		// Die Tradeliste für den Backtest einlesen
		
		int anztrades = eatl.initBacktest(filename);
		
		eatl.sortliste();
		eatl.Reverse();
		eatl.calcSummengewinne();
		// tl=eatl;
		return eatl;
	}
	
	public Tradeliste buildTradeliste(String magic, String broker, int channel,String comment)
	{
		// Hier wird die Tradeliste für ein Profitelement aufgebaut
		
		// 1) Fall 1 Backtest, dann hole die daten aus dem file
		if ((broker != null) && (broker.toLowerCase().contains("backtest")))
		{
			// dann steht in der magic der name des files das geladen werden soll
			
			// loadfile und baue Tradeliste auf
			String filename = MonDia.FileDialog(display_glob, "");
			// text1tradelist.setText(filename);
			
			// jetzt die Tradeliste noch einlesen und anzeigen
			Tradeliste trl = new Tradeliste(null);
			Tradeliste.setTradelistenam(filename);
			// den Backtest aus dem GB laden
			int anztrades = trl.initBacktest(filename);
			trl.calcSummengewinne();
			
			return trl;
			
		}
		
		// 2) Fall 2, Broker ist ein demobroker
		// baut eine Profitliste auf wo nur ein bestimmter Ea vorkommt, die Trades
		// werden aus der globalen Tradeliste geholt
		Tradeliste eatl = new Tradeliste(null);
		int zeilcount = tl.getsize();
		for (int i = 0, j = 0; i < zeilcount; i++)
		{
			Trade tr = tl.getelem(i);
			
			if ((tr.getBroker().equals(broker)) && (tr.getMagic() == Long.valueOf(magic)))
			{
				eatl.addTradeElem(tr);
			}
		}
		
		// falls eatl.size==0, dann wurde nix gefunden, dann schaue in den comments nach
		// der magic
		
		// 3) Fall3, Broker ist ein Realbroker, dann wird man die magic auf dem
		// realbroker möglicherweise nicht
		// finden, dann schaue zusätzlich in den comment nach und übernimm die trades
		// wenn dort ein teil der magic vor
		// kommt.
		// die Funktion sieht ungefähr wie bei fall 2 aus nur das hier noch der comment
		// als oder abgefragt wird
		// ermittle erste den channel der angezeigt werden soll
		if ((eatl.getsize() == 0)&&(comment!=null))
		{  //3.1 autocreator ea on realaccount
			zeilcount = tl.getsize();
			for (int i = 0, j = 0; i < zeilcount; i++)
			{
				Trade tr = tl.getelem(i);
				
				if ((tr.getBroker().equals(broker)) && tr.getComment().equals(comment) && (channel == tr.getMagic()))
				{
					Tracer.WriteTrace(20, "Lifecheck:, I add ea comment<"+comment+"> to checklist for realbroker <"+broker+"> channel<"+tr.getMagic()+">");
					eatl.addTradeElem(tr);
				}
			}
		}
		else if((eatl.getsize() == 0)&&(comment==null))
		{ //3.2 normal ea on realaccount
			zeilcount = tl.getsize();
			for (int i = 0, j = 0; i < zeilcount; i++)
			{
				Trade tr = tl.getelem(i);
				
				if ((tr.getBroker().equals(broker)) && tr.getComment().contains(magic) && (channel == tr.getMagic()))
				{
					
					eatl.addTradeElem(tr);
				}
			}
		}
		
		eatl.sortliste();
		eatl.Reverse();
		eatl.calcSummengewinne();
		// tl=eatl;
		return eatl;
	}
	
	public Tradeliste buildTradelisteAllSel(int ausgabeflag)
	{
		// Hier wird die Tradeliste für alle selektierten EA´s in einer Liste
		// aufgebaut
		CalcSelectedProfits();
		
		// baut eine Profitliste auf wo nur ein bestimmte Eas vorkommen
		Tradeliste eatl = new Tradeliste(null);
		
		int zeilcount = tl.getsize();
		for (int i = 0, j = 0; i < zeilcount; i++)
		{
			Trade tr = tl.getelem(i);
			if (profitmenge.contains(tr.getBroker() + ":" + tr.getMagic()))
			{
				eatl.addTradeElem(tr);
			}
		}
		if (ausgabeflag == 1)
			Mbox.Infobox("Export" + profitmenge.toString());
		
		eatl.sortliste();
		eatl.Reverse();
		eatl.calcSummengewinne();
		// tl=eatl;
		return eatl;
	}
	
	public ArrayList<Tradeliste> buildAllTradeliste(Tradefilter tf, int maxprofanz)
	{
		// baut eine Arrayliste von listen auf
		// man möchte für jeden EA eine tradeliste haben
		// diese Information sammelt man in der alltradeliste
		ArrayList<Tradeliste> alltradel = new ArrayList<Tradeliste>();
		
		int profanz = pl_glob.getsize();
		if (profanz > maxprofanz)
			profanz = maxprofanz;
		for (int k = 0; k < profanz; k++)
		{
			Profit profelem = pl_glob.getelem(k);
			int magic = profelem.getMagic();
			String broker = profelem.getBroker();
			
			// baut eine Profitliste auf, wo nur ein bestimmter Ea vorkommt
			Tradeliste eatl = new Tradeliste(null);
			
			// die liste der Trades für den EA aufbauen
			int zeilcount = tl.getsize();
			for (int i = 0, j = 0; i < zeilcount; i++)
			{
				Trade tr = tl.getelem(i);
				
				// beim portfolio muss nur der broker stimmen
				if ((tr.getBroker().equals(broker)) && (tr.getMagic() == magic))
				{
					eatl.addTradeElem(tr);
				}
			}
			
			// die liste der trades in der globalen liste ablegen, aber nur wenn
			// der tradefilter dies erlaubt
			
			if (tf == null)
				alltradel.add(eatl);
			else if (tf.checkConditions(profelem) == true)
				alltradel.add(eatl);
			
		}
		return alltradel;
	}
	
	public ArrayList<Tradeliste> buildAllPortfolioliste()
	{
		
		ArrayList<Tradeliste> allportfolio = new ArrayList<Tradeliste>();
		HashSet<String> brokermenge = new HashSet<String>();
		
		int profanz = pl_glob.getsize();
		
		for (int k = 0; k < profanz; k++)
		{
			Profit profelem = pl_glob.getelem(k);
			String broker = profelem.getBroker();
			
			// broker wurde schon betrachtet
			if (brokermenge.contains(broker) == true)
				continue;
			else
				// nimm den broker auf
				brokermenge.add(broker);
			
			// baue für diesen Broker eine profitliste auf
			Tradeliste eatl = new Tradeliste(null);
			
			// gehe hierzu durch alle trades
			int zeilcount = tl.getsize();
			for (int i = 0, j = 0; i < zeilcount; i++)
			{
				Trade tr = tl.getelem(i);
				
				if (tr.getBroker().equals(broker))
					eatl.addTradeElem(tr);
			}
			
			// baue für jeden broker eine eatl-liste auf
			allportfolio.add(eatl);
			
		}
		return allportfolio;
	}
	
	public void showCounter(Text anztrades, Text anzeas)
	{
		anztrades.setText(String.valueOf(tl.getsize()));
		anzeas.setText(String.valueOf(pl_glob.getsize()));
	}
	
	public void syncBroker()
	{
		// gehe durch die profitliste und schaue welche ea´s beim realbroker
		// installiert sind
		int anz = pl_glob.getsize();
		for (int i = 0; i < anz; i++)
		{
			Profit prof = pl_glob.getelem(i);
			
			int magic = prof.getMagic();
			String broker = prof.getBroker();
			Networker_dep net = new Networker_dep();
			
			// holt den Status des Eas
			EaStatus eastat = net.getEaStatus(brokerview_glob, magic, broker);
			
			// falls für das Profitelement kein Ea gespeichert ist dann gehe
			// weiter
			if (eastat == null)
				continue;
			
			Ea ea = eal_glob.getEa(magic, broker);
			if (ea != null)
			{
				int installiertflag = eastat.getInstalliert();
				if (installiertflag == 0)
				{
					// falls nix installiert ist dann ist die automatic auf 0
					ea.setInst(0);
					ea.setAuto(0);
				} else
				{
					// ea in der eal
					ea.setOn(eastat.getOn());
					ea.setInst(eastat.getInstalliert());
				}
			} else
				Tracer.WriteTrace(10, "Inernal error expanded error4545 ");
		}
		
		// gehe noch mal durch die Liste und setze instfrom beim Realbroker
		anz = pl_glob.getsize();
		for (int i = 0; i < anz; i++)
		{
			Profit prof = pl_glob.getelem(i);
			int magic = prof.getMagic();
			String broker = prof.getBroker();
			
		}
		
	}
	
	private void ToggleOnOffEa_dep(int magic, String selbroker)
	{
		
		// selbroker = selektierter broker
		int ret = -1;
		// den realbroker toggeln
		
		String realbroker = null, demobroker = null;
		
		// der selektierte broker ist ein demobroker
		if (brokerview_glob.getAccounttype(selbroker) != 2)
		{
			realbroker = brokerview_glob.getConBroker(selbroker);
			if (realbroker == null)
			{
				// einen demobroker kann man nicht ein ausschalten der muss schon mit einem
				// realbroker verbunden sein
				Mbox.Infobox("no connected Realbroker for magic<" + magic + "> broker<" + selbroker + ">");
				return;
			}
			demobroker = selbroker;
		} else
		{// selektierter broker ist ein realbroker
			realbroker = selbroker;
			demobroker = eal_glob.getInstFrom(magic, realbroker);
		}
		
		// Ab hier sind real und demobroker separiert !!!
		// den demobroker toggln
		
		// das flag auf demobroker umdrehen
		eal_glob.toggleOn(magic, demobroker);
		
		// es gibt den falls das es für den realbroker keinen demobroker gibt
		// Hier kann man trotzdem den realbroker ein/ausschalten
		if (demobroker == null)
		{
			Mlist.add("W: realbroker<" + realbroker + "> don´t have a con. demobroker", 1);
			eal_glob.toggleOn(magic, demobroker);
		}
		
		// hier wird im Netzwerk auf dem Realaccount geschaltet
		if (ret == 1)
		{
			Mlist.add("switch ON EA<" + magic + "> on realbroker<" + realbroker + ">", 1);
			// EA wurde eingeschaltet
			networker_glob.RealBroSwitchOnEa(brokerview_glob, magic, realbroker);
		} else if (ret == 0)
		{
			Mlist.add("switch OFF EA<" + magic + "> on realbroker<" + realbroker + ">", 1);
			// EA wurde ausgeschaltet
			networker_glob.RealBroSwitchOffEa(brokerview_glob, magic, realbroker);
		} else
			Tracer.WriteTrace(10, "internal error 3004545");
		
	}
	
	public String getSourcedir(String selbroker)
	{
		// das mql-sourcedir zurückmelden
		return (brokerview_glob.getMetaconfigByBrokername(selbroker).getMqlquellverz());
		
	}
	
	private void ToggleOnOffAutomatic(int magic, String selbroker)
	{
		// selbroker (selektierter broker) kann sowohl realaccount wie auch
		// demoaccount sein
		String demobroker, realbroker;
		
		if (brokerview_glob.getAccounttype(selbroker) == 2)
		{
			// es handelt sich um einen realbroker
			realbroker = selbroker;
			demobroker = eal_glob.getInstFrom(magic, realbroker);
			
		} else
		{
			// es handelt sich um einen demobroker
			demobroker = selbroker;
			realbroker = brokerview_glob.getConBroker(demobroker);
		}
		
		eal_glob.toggleAuto(magic, demobroker, realbroker);
		
	}
	
	public void lifecheck(Metaconfig mc)
	{
		String expertpath = mc.getMqldata();
		
		File histnam = new File((expertpath + "/experts/files/history.txt"));
		
		// String akttime=Tools.get_aktdatetime_str();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		
		if (Tools.CheckDateIsOlder(sdf.format(histnam.lastModified()), 0, 15, 0))
			Mbox.Infobox(
					"Broker" + mc.getBrokername() + "not working last access=" + sdf.format(histnam.lastModified()));
			
		// System.out.println("After Format : " +
		// sdf.format(histnam.lastModified()));
		
	}
	
	public void toogleallprofits()
	{
		// bei allen profits wird die checkbox an oder aus geschaltet
		if (table2lastchecked == false)
			table2lastchecked = true;
		else
			table2lastchecked = false;
		
		int anz = table2_glob.getItemCount();
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			item.setChecked(table2lastchecked);
			
		}
		DisTool.UpdateDisplay();
	}
	
	public void toggleOnOffEas()
	{
		Toogler tog = new Toogler();
		
		// hier werden die EA ein/ausgeschaltet
		int anz = table2_glob.getItemCount();
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			Profit prof = pl_glob.getelem(i);
			
			if (item.getChecked() == true)
			{
				// hole die magic
				int magic = SG.get_zahl(item.getText(1));
				if (magic != prof.getMagic())
					Tracer.WriteTrace(10, "internal magic<" + magic + "> != prof.magic<" + prof.getMagic() + ">");
				tog.ToggleOnOffEa(tl, brokerview_glob, eal_glob, magic, prof.getComment(), prof.getBroker(), pl_glob);
				
			}
		}
	}
	
	public void setAllInstfrom()
	{
		// hier wird für den Realbroker abgefragt woher die demoEa´s herkommen
		// anschliessend wird instfrom gesetzt
		
		// Abfrage welches der Quellbroker ist,
		// hierbei eine Combobox mit Text anbieten
		
		// Anschliessend wrd in der tabelle gesetzt
		int anz = table2_glob.getItemCount();
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			Profit prof = pl_glob.getelem(i);
			
			if (item.getChecked() == true)
			{
				// hole die magic
				int magic = SG.get_zahl(item.getText(1));
				if (magic != prof.getMagic())
					Tracer.WriteTrace(10, "internal magic<" + magic + "> != prof.magic<" + prof.getMagic() + ">");
				
				// prof.
			}
		}
		
	}
	
	public void toggleOnOffAutomatics()
	{
		int anz = table2_glob.getItemCount();
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			Profit prof = pl_glob.getelem(i);
			
			// falls dieser EA in der globalen liste selektiert wurde
			if (item.getChecked() == true)
			{
				// hole die magic
				int magic = SG.get_zahl(item.getText(1));
				if (magic != prof.getMagic())
					Tracer.WriteTrace(10, "internal magic<" + magic + "> != prof.magic<" + prof.getMagic() + ">");
				
				ToggleOnOffAutomatic(magic, prof.getBroker());
			}
		}
	}
	
	public int deleteEas()
	{
		
		int delcounter = 0;
		
		int anz = table2_glob.getItemCount();
		for (int i = 0; i < anz; i++)
		{
			TableItem item = table2_glob.getItem(i);
			Profit prof = pl_glob.getelem(i);
			
			// falls dieser EA in der globalen liste selektiert wurde
			if (item.getChecked() == true)
			{
				// hole die magic,comment, broker
				int magic = SG.get_zahl(item.getText(1));
				String comment = prof.getComment();
				String brokername = prof.getBroker();
				Metaconfig meconf = brokerview_glob.getMetaconfigByBrokername(brokername);
				
				if (meconf.getAccounttype() != 4)
				{
					if (deleteSingleEa(brokerview_glob, tl, prof.getBroker(), magic) == true)
						delcounter++;
					
				} else // if AutocreatorEA
				{
					Tracer.WriteTrace(10, "E:Can´t delete AutoCreator EA in this list");
					// if (deleteSingleEaAC(brokerview_glob, tl, prof.getBroker(), magic,comment) ==
					// true)
					// delcounter++;
					
				}
			}
		}
		// speichere die verkleinerte Tradeliste
		tl.store(null);
		return delcounter;
	}
	
	public Boolean deleteSingleEa(Brokerview bv, Tradeliste tl, String broker, int magic)
	{
		Metaconfig meconf = bv.getMetaconfigByBrokername(broker);
		// check if we can delete this ea
		if (checkIfDeleateAbleEa(bv, broker, magic) == false)
			return false;
			
		// delete eas from filesystem in the installdir and in the metatrader
		// falls das ein normaler EA ist
		
		// normaler ea
		if ((FsbPortfolioEa.checkIsPortfolioEa(magic, meconf)) == false)
		{
			deleteEaFilesystem(bv, magic, broker);
		} else
		{ // ist ein portfolio ea
			deletePortfolioEaFilesystem(bv, magic, broker);
		}
		
		// delete eas from tradelist
		tl.deleteEaMagic(magic, broker);
		
		// delete ea-trades in historyexporter.txt
		// die metatrader dürfen hier nicht laufen!!!
		String historytxt = bv.getMqlData(broker) + "\\files\\history.txt";
		File hf = new File(historytxt);
		if (hf.exists())
		{
			Historyexporter h = new Historyexporter(historytxt);
			h.deleteEa(String.valueOf(magic));
			h.storeHistoryTxt();
		}
		historytxt = bv.getMqlData(broker) + "\\files\\history_small.txt";
		hf = new File(historytxt);
		if (hf.exists() && (hf.length() > 0))
		{
			Historyexporter h = new Historyexporter(historytxt);
			h.deleteEa(String.valueOf(magic));
			h.storeHistoryTxt();
		}
		
		return true;
	}
	
	public Boolean deleteSingleEaAC(Brokerview bv, Tradeliste tl, String broker, int magic, String comment)
	{
		//Ein einzelner EA Autocreator Ea wird hier gelöst
		
		
		// check if we can delete this ea
		if (checkIfDeleateAbleEa(bv, broker, magic) == false)
			return false;
		
		// delete eas from tradelist
		tl.deleteEaMagic(magic, broker);
		
		// delete ea-trades in historyexporter.txt
		// die metatrader dürfen hier nicht laufen!!!
		String historytxt = bv.getMqlData(broker) + "\\files\\history.txt";
		File hf = new File(historytxt);
		if (hf.exists())
		{
			Historyexporter h = new Historyexporter(historytxt);
			h.deleteEaMagicComment("99999", comment);
			h.storeHistoryTxt();
		}
		historytxt = bv.getMqlData(broker) + "\\files\\history_small.txt";
		hf = new File(historytxt);
		if (hf.exists() && (hf.length() > 0))
		{
			Historyexporter h = new Historyexporter(historytxt);
			h.deleteEaMagicComment("99999", comment);
			h.storeHistoryTxt();
		}
		
		//generate .delcom
		String delcom=bv.getMqlData(broker) + "\\files\\"+comment+".delcom";
		if(new File(delcom).exists()==false)
			FileAccess.genFile(delcom);
		
		return true;
	}
	
	private Boolean checkIfDeleateAbleEa(Brokerview bv, String broker, int magic)
	{
		// schaue nach ob der EA auf einem Realbroker ist
		// auf Realbroker sind nur Tradecopierer die nicht gelöscht werden können
		Metaconfig meconf = bv.getMetaconfigByBrokername(broker);
		if (meconf.isRealbroker() == true)
		{
			Mbox.Infobox("Error: Can´t delete magic<" + magic + "> because broker <" + broker + ">is realbroker");
			return false;
		}
		
		// schaue nach ob der EA gelocked ist, wenn ja dann meldung ausgeben und nicht
		// den EA löschen
		if (meconf.getAccountlocked() == 1)
		{
			Mbox.Infobox("Error: Can´t delete magic<" + magic + "> because broker <" + broker + ">is locked");
			return false;
		}
		return true;
		
	}
	
	private void deleteEaFilesystem(Brokerview bv, int magic, String broker)
	{
		eal_glob.deleteEaFilesystem(bv, magic, broker);
		
	}
	
	private void deletePortfolioEaFilesystem(Brokerview bv, int magic, String broker)
	{
		eal_glob.deletePortfolioEaFilesystem(bv, magic, broker);
		
	}
	
	public void refresh()
	{
		ShowProfitTable();
		
	}
	
	public void exportTradeliste(String fnam)
	{
		// sicherheitsabfrage ob man wirklich so viele EA´s exportieren möchte
		
		tl.exportXmlTl(fnam);
	}
	
	public void importTradeliste(String fnam, String broker)
	{
		
		// das alte erst mal laden damit alles zusammen ist
		int anz = brokerview_glob.getAnz();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mc = brokerview_glob.getElem(i);
			this.LoadTradeTable(mc, Display.getDefault(), 1, 0, 0);
		}
		
		// das neue in den selektierten Broker importieren
		tl.expandTL(fnam, broker);
		tl.store(tf_glob);
	}
	
	public void importAllTradelist(String fnam)
	{
		// Die mastertradeliste die ein anderer user exportiert hat wird hier
		// importiert
		
		// das neue in den selektierten Broker importieren
		tl.expandAllTl(fnam, 2);
		// tl.store();
	}
	
	public void removeImported(String broker)
	{
		tl.removeImported(broker);
		tl.store(tf_glob);
	}
	
	public void searchSelId(String searchid)
	{
		//es wird nur nach zahlen gesucht
		int id = SG.get_zahl(searchid);
		
		int anz = pl_glob.getsize();
		for (int i = lastfoundpos + 1; i < anz; i++)
		{
			Profit prof = pl_glob.getelem(i);
			String magstring=String.valueOf(prof.getMagic());
			String comment=prof.getComment();
			
			if ((magstring.contains(searchid))||(comment.contains(searchid)))
			{
				table2_glob.setSelection(i);
				lastfoundpos = i;
				
				return;
			}
		}
		
		for (int i = 0; i < anz; i++)
		{
			Profit prof = pl_glob.getelem(i);
			String magstring=String.valueOf(prof.getMagic());
			String comment=prof.getComment();
			
			if ((magstring.contains(searchid))||(comment.contains(searchid)))
			{
				table2_glob.setSelection(i);
				lastfoundpos = i;
				
				return;
			}
		}
	}
	
	public String calcTradepicture(String headline, int forceflag, Tradeliste tlx)
	{
		String fnam = Profitanzeige.createProfitpicture(tlx, headline, forceflag);
		return (fnam);
	}
	
	public void addTradeliste(Tradeliste tlneu)
	{
		// Hier wird eine Tradeliste zur Gesammtradeliste hinzugefügt
		int anz = tlneu.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade trade = tlneu.getelem(i);
			tl.addTradeElem(trade);
		}
	}
	
	public TableItem getTableItemTable2(int nr)
	{
		TableItem item = table2_glob.getItem(nr);
		return item;
	}
	
	public Profitliste getAktProfitliste()
	{
		return pl_glob;
	}
}
