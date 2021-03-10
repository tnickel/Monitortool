package data;

import filter.Tradefilter;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.InfFast;
import hiflsklasse.Infbuf;
import hiflsklasse.SG;
import hiflsklasse.Swttool;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;
import hiflsklasse.Tradestatistik;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import mtools.Mlist;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import StartFrame.Brokerview;

public class Tradeliste
{
	private static int isloaded = 0;
	private static String tradelistenam = null;//
	// Rootpath.rootpath+ "\\data\\tradeliste.xml";
	// hashmenge der ordertickets, dient zum überprüfen ob der trade schon
	// gespeichert
	// hier wird broker+magic gespeichert
	// dürfen pro broker also nur einmal vorkommen
	private Set<String> transactionmenge = new HashSet<String>();

	// Diese Liste beinhaltet alle getätigten Trades
	private ArrayList<Trade> tradeliste = new ArrayList<Trade>();
	private Brokerview brokerview_glob = null;

	// wird für den GD5 benötigt
	// liste der summen bsp: sum0= gewinn 0
	// sum1=gewinn0+gewinn1; sum2= gewinn0+gewinn1+gewinn2
	private ArrayList<Double> summengewinne = new ArrayList<Double>();
	private Double sumGewinn = 0.0;
	int gdx = 0;

	private Date mindate_glob = null;
	private Date maxdate_glob = null;

	// ermittle die Zeitenmenge für die open/close Listen
	// die open-close listen sind die auf geweicht also eine Minutentolleranz drin
	private HashSet<Date> openzeitenmenge = null;
	private HashSet<Date> closezeitenmenge = null;
	
	
	public Tradeliste(String tradelistennamx)
	{
		if (tradelistennamx == null)
			tradelistenam = Rootpath.rootpath + "\\data\\tradeliste.xml";
		
	}

	public static String getTradelistenam()
	{
		return tradelistenam;
	}

	public static void setTradelistenam(String tradelistenam)
	{
		Tradeliste.tradelistenam = tradelistenam;
	}

	public Date getMindate()
	{
		return mindate_glob;
	}

	public void setMindate(Date mindate_glob)
	{
		this.mindate_glob = mindate_glob;
	}

	public Date getMaxdate()
	{
		return maxdate_glob;
	}

	public void setMaxdate(Date maxdate_glob)
	{
		this.maxdate_glob = maxdate_glob;
	}

	public int getgdx()
	{
		return gdx;
	}

	public void setgdx(int gdx)
	{
		this.gdx = gdx;
	}

	@SuppressWarnings("unchecked")
	public void initTL(Brokerview bview,int forceflag,Tradefilter tf)
	{
		//falls forceflag=1 ist wird die Tradeliste auf jeden Fall geladen
		
		if (bview == null)
			return;
		if ((isloaded == 0)||(forceflag==1))
		{
			brokerview_glob = bview;
			Tracer.WriteTrace(20, "I: Tradeliste von Platte<"+tradelistenam+"> lesen");
			Mlist.add("Info: read tradelist from database", 1);
			if (FileAccess.FileAvailable(tradelistenam) == false)
				return;

			tradeliste.clear();
			transactionmenge.clear();
			// tradeliste laden
			Inf inf = new Inf();
			inf.setFilename(tradelistenam);

			// tradeliste einlesen
			tradeliste = (ArrayList<Trade>) inf.loadXST();
			// Tracer.WriteTrace(10, "anz trades geladen="+tradeliste.size());
			inf.close();
			isloaded = 1;

			// nach dem Einladen die open trades entfernen
			this.entferneTrades(bview, tradeliste,tf);
			Tracer.WriteTrace(20, "Info: Tradeliste von Platte lesen fertig");
			Mlist.add("I:tradelist ready");
		}
		// hashset aufbauen
		int anz = tradeliste.size();

		for (int i = 0; i < anz; i++)
		{
			Trade trade = tradeliste.get(i);
			int trans = trade.getTransactionnumber();
			transactionmenge.add(trade.getBroker() + trans);
		}
		calcMinMaxdate();

	}

	public ArrayList<String> calcBrokerliste(int magic)
	{
		// für eine magic werden alle verfügbaren boker ermittelt
		ArrayList<String> brokerliste = new ArrayList<String>();
		Set<String> brokermenge = new HashSet<String>();

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);
			if (tr.getMagic() != magic)
				continue;

			String brokernam = tr.getBroker();
			if (brokermenge.contains(brokernam) == false)
			{
				brokermenge.add(brokernam);
				brokerliste.add(brokernam);
			}
		}
		return brokerliste;
	}

	public int initBacktest(String fnam)
	{
		// HIer muss ich anders vorgehen
		// beim html, muss alles in einer riesen liste eingelesen werden und
		// dann die open und closetrades
		// aus der liste einzeln rausgesucht werden.

		InfFast inf = new InfFast();
		inf.setFilename(fnam);
		tradeliste.clear();

		StringBuffer mem = null;
		mem = inf.readMemFile();

		if (fnam.contains(".str"))
		{
			int anztrades=baueTradelisteStr(mem);
			if(anztrades==0)
				return 0;
		}
		else if (fnam.contains(".htm"))
			baueTradelisteHtml(mem);
		else
			Tracer.WriteTrace(10, "unknown format only .str or .html allowed");

		calcMinMaxdate();
		int anztrades=tradeliste.size();
		return anztrades;
	}

	private void baueTradelisteHtml(StringBuffer mem)
	{
		Htmltradeliste htradel = new Htmltradeliste(mem);
		tradeliste = htradel.generateTradeliste();
		calcMinMaxdate();
	}

	private int baueTradelisteStr(StringBuffer mem)
	{
		int poscounter = 0;
		while (5 == 5)
		{
			Trade tr = null;
			// hole die nächste order
			int pos1 = mem.indexOf("<Order>", poscounter);
			if (pos1 == -1)
				break;
			int pos2 = mem.indexOf("</Order>", poscounter);
			// zeile rausschneiden

			if ((pos1 > mem.length()) || (pos2 > mem.length()))
				break;

			String tradezeile = mem.substring(pos1, pos2);

			// ende erreicht
			if (tradezeile.length() < 50)
				break;

			tr = calcStrTrade(tradezeile);

			if (tr.getProfit() != 0.0)
				tradeliste.add(tr);

			poscounter = pos2 + 1;

			// System.out.println("pos1=" + pos1 + " len=" + mem.length());
		}
		if(tradeliste.size()==0)
			return 0;
		calcMinMaxdate();
		return (tradeliste.size());
	}

	public void initGBcsvBacktest(String fnam)
	{
		Inf inf = new Inf();
		inf.setFilename(fnam);
		tradeliste.clear();
		int count = 0;
		int lauf = 0;
		String line = null;

		while ((line = inf.readZeile()) != null)
		{
			if (line.contains("Order,Type,Open time"))
				continue;

			count++;
			Trade tr = calcCsvTrade(line, count, "tmp");
			tradeliste.add(tr);
		}
		calcMinMaxdate();
	}

	private Trade calcStrTrade(String tradezeile)
	{
		// aus der Tradezeile wird der Trade gewonnen
		// <Order> (schon gelesen)
		// <type>2</type>
		// <originalType>2</originalType>
		// <volume>1.0</volume>
		// <openPrice>6740.5</openPrice>
		// <closePrice>6772.5</closePrice>
		// <stoploss>6772.5</stoploss>
		// <takeprofit>6447.5</takeprofit>
		// <openTime>1342771259</openTime>
		// <closeTime>1342778226</closeTime>
		// <closeType>2</closeType>
		// <barsInTrade>1</barsInTrade>
		// <pl>-32.0</pl>
		// <outOfSample>false</outOfSample>
		// <status>1</status>
		// </Order>

		int direction = 0;
		float profit = 0.0f;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		// den ordertype holen
		int type = SG
				.get_zahl(tradezeile.substring(
						tradezeile.indexOf("<type>") + 6,
						tradezeile.indexOf("</type>")));

		if (type == 1)
			direction = 0;
		else if (type == 2)
			direction = 1;
		else
			// der Trade hat keine Richtung
			direction = -99;

		// den profit holen
		String profitstring = tradezeile.substring(
				tradezeile.indexOf("<pl>") + 4, tradezeile.indexOf("</pl>"));
		profit = Float.valueOf(profitstring);

		// die opentime holen
		String opentimestring = tradezeile.substring(
				tradezeile.indexOf("<openTime>") + 10,
				tradezeile.indexOf("</openTime>"));
		Long ot = Long.valueOf(opentimestring) * 1000 - (7200 * 1000)
				+ (3600 * 1000);
		Date opentime = new Date(ot);

		// die closetime holen
		String closetimestring = tradezeile.substring(
				tradezeile.indexOf("<closeTime>") + 11,
				tradezeile.indexOf("</closeTime>"));
		Long ct = Long.valueOf(closetimestring) * 1000 - (7200 * 1000)
				+ (3600 * 1000);
		Date closetime = new Date(ct);

		// den open-Price holen
		String openpricestring = tradezeile.substring(
				tradezeile.indexOf("<openPrice>") + 11,
				tradezeile.indexOf("</openPrice>"));

		// den close-Price holen
		String closepricestring = tradezeile.substring(
				tradezeile.indexOf("<closePrice>") + 12,
				tradezeile.indexOf("</closePrice>"));

		// symbol holen
		String symbol = "???";

		Trade tr = new Trade("0#" + type + "#" + openpricestring + "#"
				+ closepricestring + "#0#0#" + profit + "#1#"
				+ sdf.format(opentime) + "#" + sdf.format(closetime)
				+ "##499143#49648465" + "#" + symbol + "#" + direction,
				"Alpari", false, 0);

		// System.out.println("tradezeile="+tradezeile);
		return (tr);
	}

	private Trade calcCsvTrade(String line, int transnumber, String broker)
	{
		// die CSV-Tradezeile wird entziffernt
		// Order,Type,Open time,Open price,Close time,Close
		// price,Profit/Loss,Comment,Out of sample
		// 1,SHORT Stop - 2013.04.22 04:17:03,2013.04.22
		// 04:18:29,1.30621,2013.04.22 09:53:45,1.30621,0.0,SL,false

		// 0/1,
		// 1/SHORT Stop - 2013.04.22 04:17:03,2
		// 2/2013.04.22 04:18:29,
		// 3/1.30621,
		// 4/2013.04.22 09:53:45,
		// 5/1.30621,
		// 6/0.0,
		// 7/SL,
		// 8/false

		String eingabe = line;
		String[] parts = eingabe.split(Pattern.quote(","));

		int ordertype = 0;
		if (parts[1].contains("LONG") == true)
			ordertype = 1;

		Trade tr = new Trade("0#" + ordertype + "#" + parts[3] + "#" + parts[5]
				+ "#0#0#" + parts[6] + "#0.1#" + parts[2] + "#" + parts[4]
				+ "#" + parts[7] + "#0#" + transnumber, broker, false, 0);
		return tr;
	}

	public int calcAnzLastPosTrades()
	{
		// berechnet wie lang die aktuelle gewinnserie ist
		// 0 heisst das der letzte Trade keinen gewinn gemacht hat
		int anz = tradeliste.size();
		int poscount = 0;
		this.sortliste();

		// in position 0 ist der neuste Gewinn
		for (int i = 0; i < anz; i++)
		{
			Trade trade = tradeliste.get(i);
			if (trade.getProfit() > 0)
				poscount++;
			else
				break;
		}
		return poscount;
	}

	public void calcSummengewinne()
	{
		int anz = tradeliste.size();
		summengewinne = new ArrayList<Double>();

		this.sortliste();// pos 0 ist neuste gewinn
		this.Reverse();// pos 0 ist der älteste gewinn
		// in position 0 ist der älteste Gewinn

		for (int i = 0; i < anz; i++)
		{
			Trade trade = tradeliste.get(i);
			sumGewinn = sumGewinn + trade.getProfit();
			summengewinne.add(sumGewinn);
		}
	}

	private ArrayList<Trade> entferneTrades(Brokerview bview,
			ArrayList<Trade> tradelistetmp,Tradefilter tf)
	{
		// die open Trades entfernen
		// die nostore Trades entfernen
		// s.h. nur die Trades die das store-flag haben bleiben erhalten
		// und die Trades die das imported flag haben bleiben ebenfalls erhalten

		// hole Hashmap welche broker denn gespeichert werden dürfen
		HashSet<String> brokermap = bview.calcAllowedsaveBroker();

		// Niemals die open Trades speichern, und die trades mit farbe 2 auch
		// nicht
		int anz = tradelistetmp.size();
		for (int opentradezaehl = 0, i = 0; i < anz; i++)
		{
			Trade tr = tradelistetmp.get(i);
			if (tr.getClosetime().contains("2050-01-01") == true)
			{
				opentradezaehl++;
				Tracer.WriteTrace(
						20,
						"Info: <" + opentradezaehl
								+ "> Offener Trade entfernt <"
								+ tr.getComment() + "> open<"
								+ tr.getOpentime() + "> close<"
								+ tr.getClosetime() + ">");
				tradelistetmp.remove(i);
				i = i - 1;
				anz--;
			}
			if (tr.getImportedcolor() == 2)
			{
				tradelistetmp.remove(i);
				i = i - 1;
				anz--;
			}
			if(tf.checkTradeIsToOld(tr)==true)
			{
				tradelistetmp.remove(i);
				i = i - 1;
				anz--;
			}
			
		}

		anz = tradelistetmp.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradelistetmp.get(i);
			// prüfe nach ob man den Broker speichern darf
			// importedflag==1, dann bleibt der Trade immer bestehen
			String broker = tr.getBroker();
			if (tr.getImportedcolor() == 0)
				if (brokermap.contains(broker) == false)
				{
					Tracer.WriteTrace(20, "Info: Trade wird entfernt broker<"
							+ broker + ">");
					tradelistetmp.remove(i);
					i = i - 1;
					anz--;
				}
		}
		Tracer.WriteTrace(20, "ende");
		return tradelistetmp;
	}

	public boolean addTradeElem(Trade elem)
	{
		// erst wird geprüft ob der trade schon in der Speicherliste ist
		int trans = elem.getTransactionnumber();
		String broker = elem.getBroker();

		if (transactionmenge.contains(broker + trans) == false)
		{
			/*
			 * Tracer.WriteTrace(20, "Info: neuen Trade aufnehmen broker=" +
			 * elem.getBroker() + "  transaktion=" + elem.getTransactionnumber()
			 * + "  closetime=" + elem.getClosetime());
			 */
			transactionmenge.add(broker + trans);
			tradeliste.add(elem);
			return true;
		} else
		{
			//Tracer.WriteTrace(20, "I:broker<"+broker+"> trade schon drin trans<"+trans+">");
			return false;
		}
	}

	public boolean remTradeElem(Trade elem)
	{
		int trans = elem.getTransactionnumber();
		String broker = elem.getBroker();
		if (transactionmenge.contains(broker + trans) == true)
		{
			transactionmenge.remove(broker + trans);
			tradeliste.remove(elem);
			return true;
		}
		return false;
	}

	public boolean deleteMagic(int magic,String brokername)
	{
		//löscht eine magic aus der tradeliste für einen broker
		int anz=tradeliste.size();
		
		
		for(int i=0; i<anz; i++)
		{
			Trade tr=tradeliste.get(i);
			if((tr.getMagic()==magic) && (tr.getBroker().equals(brokername)))
			{
				tradeliste.remove(i);
				anz--;
				i--;
			}
			
		}
		
		return true;
	}
	
	public Trade getelem(int index)
	{
		return (tradeliste.get(index));
	}

	public void sortliste()
	{
		// die liste wird nach den closezeiten sortiert
		Collections.sort(tradeliste);
	}

	public void Reverse()
	{
		Collections.reverse(tradeliste);
	}

	public int getsize()
	{
		return (tradeliste.size());
	}

	public void store(Tradefilter tf)
	// Hier wird die Tradeliste auf platte gespeichert
	{
		// Im Prinzip reicht es einmal in der Woche zu speichern

		if (tradeliste.size() == 0)
			return;

		ArrayList<Trade> tradeliste2 = (ArrayList<Trade>) tradeliste.clone();

		String tradeliste_tmp = tradelistenam + ".tmp";
		String tradeliste_old = tradelistenam + ".old";

		// das tmp löschen
		if (FileAccess.FileAvailable(tradeliste_tmp))
			FileAccess.FileDelete(tradeliste_tmp, 0);

		if(tf!=null)
			tradeliste2 = entferneTrades(brokerview_glob, tradeliste2,tf);

		// speichern
		Inf inf = new Inf();
		inf.setFilename(tradeliste_tmp);
		inf.saveXST(tradeliste2);
		inf.close();

		// längenvergleich, es darf nicht kürzer werden
		File f1 = new File(tradelistenam);
		File f2 = new File(tradeliste_tmp);
		if (f2.length()*(1.2) < f1.length())
			Tracer.WriteTrace(20, "ERROR: Length protection error tradeliste<"
					+ f1.length() + "> tradeliste_new<" + f2.length() + ">");

		// wenn alles ok dann nach old speichern
		if (FileAccess.FileAvailable(tradeliste_old))
			FileAccess.FileDelete(tradeliste_old, 0);

		if (FileAccess.FileAvailable(tradelistenam) == true)
			FileAccess.FileMove(tradelistenam, tradeliste_old);
		// und tmp umbenennen
		FileAccess.FileMove(tradeliste_tmp, tradelistenam);

	}

	public String getComment(int magic)
	{
		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{

			Trade tr = tradeliste.get(i);
			if (tr.getMagic() == magic)
			{
				return tr.getComment();
			}
		}
		Tracer.WriteTrace(10, "error internal magic nicht bekannt");
		return null;
	}

	public Double calc_gdx(int pos, int intervall)
	{
		// es wird der gleitende durchschnitt der summengewinne für die position
		// x berechnet
		// pos= position für die der gleitende durchschnitt berechnet wird
		// intervall=wieviele punkte in der vergangenheit betrachtet werden
		// beispiel: intervall =5
		// pos =10
		// dann rechne (sum6+sum7+sum8+sum9+sum10)/5=sumGD5
		Double sum = 0.0;

		if (pos > summengewinne.size())
			Tracer.WriteTrace(10, "internal error pos=" + pos
					+ "  > summenlistesize=" + summengewinne.size() + "");

		if (summengewinne.size() < intervall)
			intervall = summengewinne.size();

		int start = pos;

		// falls noch zu wenige positionen drin sind dann wähle kleineres
		// intervall
		if (pos < intervall)
		{
			intervall = pos;
		}

		for (int i = start; i > pos - intervall; i--)
		{
			double val = summengewinne.get(i);
			sum = sum + val;
		}
		sum = sum / intervall;
		return sum;

	}

	public double get_tsumx(int pos)
	{
		// es wird die Tradesumme für die position pos berechnet
		// Bsp: Tradesum5=profit0+profit1+profit2+profit3+profit4+profit5

		double sgewinn = summengewinne.get(pos);
		return sgewinn;
	}

	public void kontrollausgabe()
	{
		String fnam = Rootpath.getRootpath() + "\\tradeliste.inf";
		Inf inf = new Inf();
		inf.setFilename(fnam);

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade trade = tradeliste.get(i);
			inf.writezeile("i=" + i + " magic=" + trade.getMagic()
					+ " closetime=" + trade.getClosetime() + " profit="
					+ trade.getProfit() + " sum=" + summengewinne.get(i));
		}
		inf.close();
	}

	public void exportXmlTl(String fnam)
	{
		// speichern
		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.saveXST(tradeliste);
		inf.close();

	}

	public void exportTextTl(String fnam)
	{
		if(fnam==null)
		{
			Tracer.WriteTrace(20, "E:tradelistennam=null");
			return;
		}
		
		try{
		
		// eigene routine um die tradeliste als textfile abzuspeichern
		// also nicht als xml
		Infbuf inf = new Infbuf(fnam);

		if(tradeliste==null)
		{
			Tracer.WriteTrace(20, "I: no trades in tradeliste");
			return;
		}	
		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade trade = tradeliste.get(i);
			
			String broker=trade.getBroker();
			
				
			Metaconfig meconf=brokerview_glob.getMetaconfigByBrokername(broker);
			if((meconf!=null)&&(meconf.getDesciption().contains("nosend")==true))
				continue;
			
			// die trades die mit farbe 2 importiert wurden werden nicht zum
			// Server übertragen
			if (trade.getImportedcolor() == 2)
				continue;
			inf.writezeile(trade.calcTradezeile());
		}
		inf.close();
		}catch (Exception e)
		{
			Tracer.WriteTrace(10, "E:exception caught");
		}
	}

	public void importTextTl(String fnam)
	{
		// hier wird die TextTradeliste importiert
		Inf inf = new Inf();
		inf.setFilename(fnam);

		String zeile = null;
		// inf.readMemFile();
		while ((zeile = inf.readZeile("UTF-8")) != null)
		{
			try
			{
				Trade tr = new Trade(zeile);
				tradeliste.add(tr);
			} catch (NumberFormatException e)
			{
				Tracer.WriteTrace(10, "Fehler aufgetreten! <" + e.getMessage()
						+ ">");
			}

		}
		inf.close();

	}

	public void expandAllTl(String fnam, int tradecolor)
	{
		// erst wird aus dem gezippten eine Tradeliste aufgebaut
		// siehe hierzu den servermonitor wie das gemacht wird

		// die Tradeliste aus dem gezippten einlesen
		Tradeliste imptrl = new Tradeliste(null);
		imptrl.importTextTl(fnam);

		// dann wird diese Tradeliste in der haupttradeliste eingeladen
		int anz = imptrl.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = imptrl.getelem(i);
			tr.setImportedcolor(tradecolor);

			// merke woher der trade her kommt
			tr.setImportedfrom(fnam);

			// den neuen broker setzen wo der trade angezeigt wird
			tr.setBroker(fnam);

			this.addTradeElem(tr);

		}

		// das neue wird grün markiert

	}

	public void expandTL(String fnam, String broker)
	{
		int badcounter = 0;
		int impcounter = 0;

		Inf inf = new Inf();
		inf.setFilename(fnam);
		// tradeliste einlesen
		ArrayList<Trade> imptradeliste = (ArrayList<Trade>) inf.loadXST();
		inf.close();

		int anz = imptradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = imptradeliste.get(i);
			tr.setImportedcolor(1);

			// merke woher der trade her kommt
			tr.setImportedfrom(tr.getBroker());

			// den neuen broker setzen wo der trade angezeigt wird
			tr.setBroker(broker);

			if (this.addTradeElem(tr))
			{
				Mlist.add("trade<" + tr.getBroker() + "><" + tr.getMagic()
						+ "><" + tr.getOpentime() + ">imported");
				impcounter++;
			} else
			{
				Mlist.add("ImportError:<" + tr.getBroker() + "><"
						+ tr.getMagic() + "><" + tr.getOpentime() + ">");
				badcounter++;
			}
		}
		// validation of tradelist
		// es dürfen nur trades vom "broker" drin sein

		Mbox.Infobox("Imported trades into broker<" + broker + "> ok<"
				+ impcounter + "> bad<" + badcounter + ">");

	}

	public void removeImported(String broker)
	{
		int remcounter = 0;
		int badcounter = 0;
		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);
			if ((tr.getImportedcolor() != 0)
					&& (tr.getBroker().equalsIgnoreCase(broker)))
			{

				if (this.remTradeElem(tr))
				{
					Mlist.add("trade<" + tr.getBroker() + "><" + tr.getMagic()
							+ "><" + tr.getOpentime() + ">removed");
					remcounter++;
					i = i - 1;
					anz = anz - 1;

				} else
				{
					Mlist.add("RemoveError:<" + tr.getBroker() + "><"
							+ tr.getMagic() + "><" + tr.getOpentime() + ">");
					badcounter++;
				}
			}
		}
		Mbox.Infobox("Removed trades from broker<" + broker + "> ok<"
				+ remcounter + "> bad<" + badcounter + ">");
	}

	public void ShowTradeTable(Display dis, Table table, String brokername,
			int maxentrys, Tradeliste tl, Tradefilter tf, int forcesortflag)
	{
		//Hier wird eine Tabelle aufgebaut
		//
		// brokername: falls gesetzt wird nur ein bestimmter broker angezeigt
		// forceflag: if 1 than table will be sorted
		org.eclipse.swt.graphics.Color red = dis.getSystemColor(SWT.COLOR_RED);
		org.eclipse.swt.graphics.Color green = dis
				.getSystemColor(SWT.COLOR_GREEN);
		org.eclipse.swt.graphics.Color magenta = dis
				.getSystemColor(SWT.COLOR_MAGENTA);
		org.eclipse.swt.graphics.Color gray = dis
				.getSystemColor(SWT.COLOR_GRAY);
		boolean showopenorders = false;

		if (tf != null)
			showopenorders = tf.isShowopenorders();

		String zeil = null;
		int countadd = 0;
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		Swttool.baueTabellenkopfDispose(table,
				"Ind#Magic#Symb#Profit#Lots#Opentime#Closetime#Comment#Broker#Account");

		// absteigend sortieren

		if (forcesortflag == 1)
		{
			Tracer.WriteTrace(20, "sorting table");
			tl.sortliste();
		}
		int zeilcount = tl.getsize();

		int tablepos = 0;
		for (int i = 0; i < zeilcount; i++)
		{

			Trade tr = tl.getelem(i);
			if (showopenorders == false)
				if (tr.getClosetime().contains("2050-") == true)
					continue;

			if (brokername != null)
				if (tr.getBroker().equals(brokername) == false)
					continue;

			TableItem item = new TableItem(table, SWT.NONE);

			item.setText(0, String.valueOf(tablepos));
			tablepos++;
			item.setText(1, String.valueOf(tr.getMagic()));

			String symb = tr.getSymbol();
			if (symb != null)
				item.setText(2, tr.getSymbol());

			Double profit = tr.getProfit();
			if (profit < 0)
				item.setForeground(3, red);
			if (profit > 0)
				item.setForeground(3, green);

			String closetime = tr.getClosetime();

			if (Tools.zeitdifferenz_stunden(Tools.get_aktdatetime_str(),
					closetime) < 16)
				item.setForeground(5, magenta);

			item.setText(3,
					SG.kuerzeFloatstring(String.valueOf(tr.getProfit()), 2));
			item.setText(4, String.valueOf(tr.getLots()));
			item.setText(5, tr.getOpentime());

			// noch offene Trades werden gelb angezeigt
			if (closetime.contains("2050"))
			{
				item.setForeground(5, gray);
				item.setText(6, "open");
			} else
				item.setText(6, tr.getClosetime());

			item.setText(7, tr.getComment());
			item.setText(8, tr.getBroker());
			item.setText(9, String.valueOf(tr.getAccountnumber()));

			countadd++;
			if (countadd > maxentrys)
				break;
		}

		for (int i = 0; i < 10; i++)
		{
			table.getColumn(i).pack();
		}
	}

	public   HashSet<Date> calcTradezeitenmenge(int gmtdiff,int opentimeflag,int minutentolleranz)
	{
		//die Tradezeitenmenge beinhaltet die menge der open trades
		//die Sekunden werden abgeschnitten
		//+/- minutenfenster werden hinzugenommen
		//hier wird eine menge von open-zeiten in einer menge gesammelt
		//man möchte von einer trademenge wissen wann die openzeiten sind
		//ausserdem möchte man einge gewisse tolleranz erlauben, d.h. man
		//richtet ein zeitfenster von +/- minuten ein
		//die menge wird also um dieses minutenfenster vergrössert
		//opentimeflag=1 es wird die openzeit betrachtet
		//closetimeflag=0 es wird die closezeit betrachtet
		
		HashSet<Date> zeitenmenge = new HashSet<Date>();
		Date tradedatum=null;
		
		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			//hole die opentime
			Trade tr = tradeliste.get(i);
			
			if(opentimeflag==1)
				tradedatum=tr.getOpentimeDate();
			else
				tradedatum=tr.getClosetimeDate();
			
			//2 minuten vor und danach geht auch noch
			for(int j=-minutentolleranz; j<=minutentolleranz; j++)
			{
				Date newdate=calcNewtradezeit(tradedatum,j,gmtdiff);
				zeitenmenge.add(newdate);
			}
		}
		return zeitenmenge;
	}

	private Date calcNewtradezeit(Date tradezeit,int minutendiff,int gmtdiff)
	{
		//rechnet eine neue Zeit aus und glättet die Sekunden
		//nutze die Calendar funktion
		Calendar cal = Calendar.getInstance();
		cal.setTime(tradezeit);
		//lösche die sekunden
		cal.set(Calendar.SECOND,0);
		cal.add(Calendar.MINUTE,minutendiff);
		cal.add(Calendar.HOUR,gmtdiff);
		Date tradetime=cal.getTime();
		return tradetime;
	}
	
	public double calcDrawdownProz()
	{
		// berechnet den maximalen prozentualen Drawdown

		double sumgewinn = 1000;
		double maxdrawdownprozent = 0;

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);

			if (tr.getProfit() > 0)
				sumgewinn = sumgewinn + tr.getProfit();
			if (tr.getProfit() < 0)
			{
				double verlusttrade = tr.getProfit();

				// den drawdown berechnen
				// summgewinn=100%
				// verlusttrade=x%

				double verlustproz = Math.abs((100 / sumgewinn) * verlusttrade);

				// falls grösseren verlustprozent gefunden
				if (verlustproz > maxdrawdownprozent)
					maxdrawdownprozent = verlustproz;
			}

		}
		return maxdrawdownprozent;
	}

	private double calcGrossProfit()
	{
		double grossprofit = 0;

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);

			if (tr.getProfit() > 0)
				grossprofit = grossprofit + tr.getProfit();
		}
		return grossprofit;
	}

	private double calcGrossLoose()
	{
		double grossloose = 0;

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);

			if (tr.getProfit() < 0)
				grossloose = grossloose + tr.getProfit();
		}
		return grossloose;
	}

	public double calcProfitfactor()
	{
		double gloose = 0, gprofit = 0;

		gprofit = calcGrossProfit();
		gloose = Math.abs(calcGrossLoose());

		if (gloose == 0)
			return (gprofit);

		else
			return (gprofit / gloose);

	}

	public void calcMinMaxdate()
	{
		// 2013.01.22 08:16:03

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;

		int anz = tradeliste.size();

		if (anz == 0)
		{
			//Mbox.Infobox("Error: tradelist empty 04");
			return;
		}
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);

			try
			{
				String closetimestring = tr.getClosetime();
				date = format.parse(closetimestring);
				// System.out.println("dati closetime="+closetimestring+" date="+date.toString());
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// init
			if (i == 0)
			{
				mindate_glob = date;
				maxdate_glob = date;
			}

			// falls kleineres datum gefunden
			if (date.compareTo(mindate_glob) < 0)
				mindate_glob = date;

			// falls grösseres datum gefunden
			if (date.compareTo(maxdate_glob) > 0)
				maxdate_glob = date;
		}

	}
	public void calcMinMaxdateOpentime()
	{
		// 2013.01.22 08:16:03

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;

		int anz = tradeliste.size();

		if (anz == 0)
			Mbox.Infobox("Error: tradelist empty 05");

		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);

			try
			{
				String opentimestring = tr.getOpentime();
				date = format.parse(opentimestring);
				// System.out.println("dati opentimetime="+opentimestring+" date="+date.toString());
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// init
			if (i == 0)
			{
				mindate_glob = date;
				maxdate_glob = date;
			}

			// falls kleineres datum gefunden
			if (date.compareTo(mindate_glob) < 0)
				mindate_glob = date;

			// falls grösseres datum gefunden
			if (date.compareTo(maxdate_glob) > 0)
				maxdate_glob = date;
		}

	}
	public Tradeliste getOptimizedTradelist(OptimizeResult or)
	{

		// es wird die optimierte Tradesliste errechnet
		// hier werden alle trades gelöscht die nicht im optimalen result
		// vorkommen
		Tradeliste tradelisteopt = new Tradeliste(Rootpath.rootpath
				+ "\\data\\tradeliste.xml");
		int[] marker = or.getMarker();

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			// falls das element markiert ist dann übernehme
			if (marker[i] == 1)
			{
				Trade tr1 = tradeliste.get(i);
				tr1.setTransactionnumber(1000 + i);
				tradelisteopt.addTradeElem(tr1);
			}
		}
		tradelisteopt.calcSummengewinne();

		return tradelisteopt;
	}

	public GewinnjahrStrategie calcGewinnjahrType1(int maxdd, int maxddproz,
			int maxddfromstart, int trailflag, int maxddtrail, int ntraildays)
	{
		// maxdd= maximaler drawdown vom maximum der der erreicht werden darf
		// maxddproz= maximaler %drawdown vom maximum das erreicht werden darf
		// falls maxdd=0 oder maxddproz=0 oder maxddfromstart =0 findet der wert
		// keien bachtung
		// für jeden Monat wird er summengewinn ermittelt
		// diese Datenstrunktur enthält 12 floatgewinne
		GewinnjahrStrategie gewjahr = new GewinnjahrStrategie();
		float maxgewin = 0;
		float aktgewin = 0;
		// speichert die letzen n gewinnstände
		Trailingdata trdata = new Trailingdata(ntraildays);
		float maxverlusttrail = (-1) * maxddtrail;

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr1 = tradeliste.get(i);
			String datum = tr1.getClosetime();

			int monatsindex = calcmonat(datum);
			int jahrindex=calcjahr(datum);
			if(jahrindex>2000)
				jahrindex=jahrindex-2000;
			
			float tradegewin = (float) tr1.getProfit();
			// den aktuellen gewinn ermitteln
			aktgewin = aktgewin + tradegewin;

			// falls ein neues maximum erreicht ist
			if (aktgewin > maxgewin)
				maxgewin = aktgewin;

			// summiere weiter auf falls alles ok ist
			gewjahr.addGewinn(jahrindex,monatsindex, tradegewin);

			// trailing
			if (trailflag == 1)
			{
				trdata.setAktGewinn(i, aktgewin);

				// die ersten 10 Steps nimm die feste verlustschranke von -50
				// Euro hin
				if (i < 9)
				{
					// maximal 50Euro verlust
					if (aktgewin < maxverlusttrail)
					{
						Tracer.WriteTrace(20, "I:Trailing erreicht aktgewin="
								+ aktgewin + " tradeanz(" + i + "|" + anz
								+ ")=");
						return (gewjahr);
					}
				}
				// ab dem 10ten trade ziehe das minimum der letzten n equity mit
				else
				{
					maxverlusttrail = trdata.getMinX();
					if (aktgewin < maxverlusttrail)
					{
						Tracer.WriteTrace(20, "I:Trailing erreicht aktgewin="
								+ aktgewin + " maxverlust=" + maxverlusttrail
								+ " tradeanz(" + i + "|" + anz + ")=");
						return (gewjahr);
					}
				}
			}

			// falls maxddfrom start gewünscht ist
			if (maxddfromstart > 0)
			{
				if (aktgewin < ((-1) * maxddfromstart))
				{
					Tracer.WriteTrace(20, "I:MaxddFromStart erreicht aktgewin="
							+ aktgewin + " tradeanz(" + i + "|" + anz + ")=");
					return (gewjahr);
				}
			}

			// falls eine maxdd abschaltung geünscht ist dann höre auf falls der
			// maximale drawdown zu hoch ist
			if (maxdd > 0)
			{
				float dd = maxgewin - aktgewin;
				if (dd > maxdd)
				{
					Tracer.WriteTrace(20, "I:Maxdd erreicht dd=" + dd
							+ " tradeanz(" + i + "|" + anz + ")=");
					return (gewjahr);
				}
			}
			if (maxddproz > 0)
			{
				float dd = maxgewin - aktgewin;
				// maxgewinn=100%
				// dd=wieviel ?
				float ddproz = (100 / maxgewin) * dd;

				if (ddproz > maxddproz)
				{
					Tracer.WriteTrace(20, "I:Maxdd proz erreicht dd%=" + ddproz
							+ " tradeanz(" + i + "|" + anz + ")=");
					return (gewjahr);
				}
			}
		}
		return (gewjahr);
	}

	public GewinnjahrStrategie calcGewinnjahrType2(int budget, int maxddproz,int anzstrategien)
	{
		/*
		 * In der Toolbox wird die Accountgröße angegeben, sagen wir mal
		 * 10.000€. Dann wird ein maximaler %DD festgelegt, der für das gesamte
		 * Portfolio gilt. Danijel hat diesen DD mit 30% festgelegt, er sollte
		 * aber in der Toolbox weiterhin variabel sein. Dann werden die 30%
		 * durch die Anzahl der Strategien geteilt und jeder EA bekommt sein
		 * persönliches Budget zum verpulvern.
		 * 
		 * 10.000€ * 30% = 3.000€ 3.000€/Anzahl der installierten EA´s (z.B. 100
		 * Stück) = 30€ pro EA .
		 * 
		 * Diese 30€ gelten zu Anfang gesehen vom Startbudget aus und ab dem
		 * ersten Trade vom Allzeithoch der Equity-Kurve des EA. Sprich:
		 * 
		 * Trade 1. Der EA fängt bei Null an darf also bis -30€ fallen bis er
		 * abgeschaltet wird. Trade 2. Wenn der erste Trade +10€ gebracht hat
		 * dann steigt die Abschaltlinie von -30 auf -20€, es wird also immer
		 * das Allzeithoch -30€ (in diesem Beispiel) zum Abschalten genommen.
		 * 
		 * Ganz wichtig, die 30% werden nur einmal beim Start des Portfolio
		 * berechnet und das Budget an alle EA´s vergeben. Es gibt keine weitere
		 * prozentuale Anpassung wenn z.B. die Equity angestiegen ist oder EA´s
		 * abgeschaltet wurden!
		 */
		GewinnjahrStrategie gewjahr = new GewinnjahrStrategie();
		float maxgewin = 0;
		float aktgewin = 0;
		
		float maxverlust100=(float)(budget/anzstrategien);
		float maxdd=(maxverlust100/100)*(float)(maxddproz);
		
		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr1 = tradeliste.get(i);
			String datum = tr1.getClosetime();

			int monatsindex = calcmonat(datum);
			int jahrindex=calcjahr(datum);
			if(jahrindex>2000)
				jahrindex=jahrindex-2000;
			float tradegewin = (float) tr1.getProfit();
			// den aktuellen gewinn ermitteln
			aktgewin = aktgewin + tradegewin;

			// falls ein neues maximum erreicht ist
			if (aktgewin > maxgewin)
				maxgewin = aktgewin;

			// summiere weiter auf falls alles ok ist
			gewjahr.addGewinn(jahrindex,monatsindex, tradegewin);

			if (maxdd > 0)
			{
				float dd = maxgewin - aktgewin;
				if (dd > maxdd)
				{
					Tracer.WriteTrace(20, "I:Maxdd erreicht dd=" + dd
							+ " tradeanz(" + i + "|" + anz + ")=");
					return (gewjahr);
				}
			}
		}
		return (gewjahr);
	}
	public Gewinnverteilung calcGewinnjahrType3()
	{
		//für jedes jahr werden die gewinne aufsummiert und in einer liste gehalten
		//diese liste nennt sich gewinnverteilung
		Gewinnverteilung gew = new Gewinnverteilung();
	
		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr1 = tradeliste.get(i);
			String datum = tr1.getClosetime();
			int jahrindex=calcjahr(datum)-1970;
			
			float tradegewin = (float) tr1.getProfit();
			gew.addGewinn(jahrindex, tradegewin);
		}
		return (gew);
	}
	private int calcmonat(String datum)
	{
		// 0= januar, 1=feburar, 11=dezember
		// System.out.println("datum="+datum);
		String monzahl = datum.substring(datum.indexOf("-") + 1,
				datum.lastIndexOf("-"));
		int mz = SG.get_zahl(monzahl) - 1;
		return mz;
	}
	private int calcjahr(String datum)
	{
		// 0=2000, 1=2001,.. 14=2014
		// System.out.println("datum="+datum);
		String jahrzahl = datum.substring(0,datum.indexOf("-") );
				
		int jz = SG.get_zahl(jahrzahl) ;
		return jz;
	}
	public Tradestatistik calcTradestatistik()
	{
		Tradestatistik trstat= new Tradestatistik();
		int anz=tradeliste.size();
		for(int i=0; i<anz; i++)
		{
			Trade tr=tradeliste.get(i);
			if(tr.getDirection()==0)
				trstat.inclong();
			else if(tr.getDirection()==1)
				trstat.incshort();
		}
		return trstat;
	}
	public void buildTolleranzlisten(int gmtdiff, int minutentolleranz)
	{
	 openzeitenmenge = calcTradezeitenmenge(gmtdiff, 1,
			minutentolleranz);
	 closezeitenmenge =calcTradezeitenmenge(gmtdiff, 0,
			minutentolleranz);
	}
	public int countHitTrades(Trade tr)
	{
			//prüft ob der trade in der aufgeweichten menge ist
			//return 0: weder open noch closetrade sind drin
			//return 1: ein trade ist drin
			//return 2: der open und der closetrade ist drin
			int count=0;
			Date opentime = tr.getOpentimeDate();
			Date closetime = tr.getClosetimeDate();

			// sekunden auf 0 setzen
			Calendar cal = Calendar.getInstance();
			cal.setTime(opentime);
			cal.set(Calendar.SECOND, 0);
			opentime = cal.getTime();

			cal = Calendar.getInstance();
			cal.setTime(closetime);
			cal.set(Calendar.SECOND, 0);
			closetime = cal.getTime();
			
			if (openzeitenmenge.contains(opentime) == true)
				count++;
			if(closezeitenmenge.contains(closetime) == true)
				count++;
			
			return count;
	}
	public int calcConsecLooses()
	{
		int clanz=0;
		
		int anz=tradeliste.size();
		for(int i=0; i<anz; i++)
		{
			Trade tr=tradeliste.get(i);
			double prof=tr.getProfit();
			if(prof<0)
				clanz++;
			if(prof==0)
				continue;
			
			if((prof>0)&&(clanz>0))
			{   //falls schon losses da sind und jetzt wird es plötzlich positiv
				//dann sind wir fertig
				return clanz;
			}
			if(prof>0)
			{
				
				clanz=0;
				return clanz;
			}
		}
		return clanz;
		
	}
}
