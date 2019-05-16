package ComData;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.lowagie.text.Table;

public class Tradeliste
{
	private static int isloaded = 0;
	private static String tradelistenam = Rootpath.rootpath
			+ "\\data\\tradeliste.xml";
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

	public Tradeliste()
	{
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
	public void initTL(Brokerview bview)
	{
		if (bview == null)
			return;
		if (isloaded == 0)
		{
			brokerview_glob = bview;
			Tracer.WriteTrace(20, "I: Tradeliste von Platte lesen");
			Mlist.add("Info: read tradelist from database", 1);
			if (FileAccess.FileAvailable(tradelistenam) == false)
				return;

			tradeliste.clear();
			// tradeliste laden
			Inf inf = new Inf();
			inf.setFilename(tradelistenam);

			// tradeliste einlesen
			tradeliste = (ArrayList<Trade>) inf.loadXST();
			// Tracer.WriteTrace(10, "anz trades geladen="+tradeliste.size());
			inf.close();
			isloaded = 1;

			// nach dem Einladen die open trades entfernen
			this.entferneTrades(bview, tradeliste);
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

	public void initBacktest_slow(String fnam)
	{
		// HIer muss ich anders vorgehen
		// beim html, muss alles in einer riesen liste eingelesen werden und
		// dann die open und closetrades
		// aus der liste einzeln rausgesucht werden.

		Inf inf = new Inf();
		inf.setFilename(fnam);
		tradeliste.clear();

		String mem = null;
		mem = inf.readMemFile();

		if (fnam.contains(".str"))
			baueTradelisteStr(mem);
		else if (fnam.contains(".htm"))
			baueTradelisteHtml(mem);
		else
			Tracer.WriteTrace(10, "unbekanntes format 454545");

		calcMinMaxdate();
		
	}

	private void baueTradelisteHtml(String mem)
	{
		Htmltradeliste htradel = new Htmltradeliste(mem);
		tradeliste = htradel.generateTradeliste();
		calcMinMaxdate();
	
	}

	private void baueTradelisteStr(String mem)
	{
		while (5 == 5)
		{
			Trade tr = null;
			// hole die nächste order
			int pos1 = mem.indexOf("<Order>");
			if (pos1 == -1)
				break;
			int pos2 = mem.indexOf("</Order>");
			// zeile rausschneiden

			//order zu klein, dann das ende erreicht
			if((pos2-pos1)<50)
				break;
			
			if ((pos1 > mem.length()) || (pos2 > mem.length()))
				break;
			String tradezeile = mem.substring(pos1, pos2);

			tr = calcStrTrade(tradezeile);

			if (tr.getProfit() != 0.0)
				tradeliste.add(tr);

			mem = mem.substring(pos2 + 6);

			System.out.println("pos1=" + pos1 + " len=" + mem.length());
		}
		System.out.println("fertig");
		calcMinMaxdate();

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
		System.out.println("fertig");
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

		float profit = 0.0f;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		//System.out.println("lese zeile<"+tradezeile+">");
		
		// den profit holen
		String profitstring = tradezeile.substring(
				tradezeile.indexOf("<pl>") + 4, tradezeile.indexOf("</pl>"));
		profit = Float.valueOf(profitstring);

		// die opentime holen
		String opentimestring = tradezeile.substring(
				tradezeile.indexOf("<openTime>") + 10,
				tradezeile.indexOf("</openTime>"));
		Long ot = Long.valueOf(opentimestring) * 1000 - (7200 * 1000);
		Date opentime = new Date(ot);

		// die closetime holen
		String closetimestring = tradezeile.substring(
				tradezeile.indexOf("<closeTime>") + 11,
				tradezeile.indexOf("</closeTime>"));
		Long ct = Long.valueOf(closetimestring) * 1000 - (7200 * 1000);
		Date closetime = new Date(ct);

		// den open-Price holen
		String openpricestring = tradezeile.substring(
				tradezeile.indexOf("<openPrice>") + 11,
				tradezeile.indexOf("</openPrice>"));

		// den close-Price holen
		String closepricestring = tradezeile.substring(
				tradezeile.indexOf("<closePrice>") + 12,
				tradezeile.indexOf("</closePrice>"));

		Trade tr = new Trade("0#1#" + openpricestring + "#" + closepricestring
				+ "#0#0#" + profit + "#1#" + sdf.format(opentime) + "#"
				+ sdf.format(closetime) + "##499143#49648465", "Alpari", false,
				false);
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
				+ "#" + parts[7] + "#0#" + transnumber, broker, false, false);
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
			ArrayList<Trade> tradelistetmp)
	{
		// die open Trades entfernen
		// die nostore Trades entfernen
		// s.h. nur die Trades die das store-flag haben bleiben erhalten
		// und die Trades die das imported flag haben bleiben ebenfalls erhalten

		// hole Hashmap welche broker denn gespeichert werden dürfen
		HashSet<String> brokermap = bview.calcAllowedsaveBroker();

		// Niemals die open Trades speichern
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
		}

		anz = tradelistetmp.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradelistetmp.get(i);

			// prüfe nach ob man den Broker speichern darf
			// importedflag==1, dann bleibt der Trade immer bestehen
			String broker = tr.getBroker();
			if (tr.getImportedflag() == false)
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
			// updateElem(elem);
			// suche den trade und nimm neu auf
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

	public Trade getelem(int index)
	{
		return (tradeliste.get(index));
	}

	public void sortliste()
	{
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

	public void store()
	// Hier wird die Tradeliste auf platte gespeichert
	{
		// Im Prinzip reicht es einmal in der Woche zu speichern

		ArrayList<Trade> tradeliste2 = (ArrayList<Trade>) tradeliste.clone();

		String tradeliste_tmp = tradelistenam + ".tmp";
		String tradeliste_old = tradelistenam + ".old";

		// das tmp löschen
		if (FileAccess.FileAvailable(tradeliste_tmp))
			FileAccess.FileDelete(tradeliste_tmp, 0);

		tradeliste2 = entferneTrades(brokerview_glob, tradeliste2);

		// speichern
		Inf inf = new Inf();
		inf.setFilename(tradeliste_tmp);
		inf.saveXST(tradeliste2);
		inf.close();

		// längenvergleich, es darf nicht kürzer werden
		File f1 = new File(tradelistenam);
		File f2 = new File(tradeliste_tmp);
		if (f2.length() < f1.length())
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

	public void exportTL(String fnam)
	{
		// speichern
		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.saveXST(tradeliste);
		inf.close();

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
			tr.setImportedflag(true);

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
			if ((tr.getImportedflag() == true)
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
			int maxentrys, Tradeliste tl, Tradefilter tf)
	{
		// brokername: falls gesetzt wird nur ein bestimmter broker angezeigt
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
		tl.sortliste();

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

	public void calcMinMaxdate()
	{
		// 2013.01.22 08:16:03

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		

		int anz = tradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tradeliste.get(i);

			
			
			try
			{
				String closetimestring=tr.getClosetime();
				date = format.parse( closetimestring );
				//System.out.println("dati closetime="+closetimestring+" date="+date.toString());
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//init
			if(i==0)
			{
				mindate_glob=date;
				maxdate_glob=date;
			}
			
			// falls kleineres datum gefunden
			if (date.compareTo(mindate_glob) < 0)
				mindate_glob = date;

			// falls grösseres datum gefunden
			if (date.compareTo(maxdate_glob) > 0)
				maxdate_glob = date;
		}
		System.out.println("minmaxdate fertig");
	}

}
