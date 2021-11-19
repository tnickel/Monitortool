package data;

import hiflsklasse.SG;
import hiflsklasse.Tools;
import hiflsklasse.ToolsException;
import hiflsklasse.Tracer;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import datefunkt.Mondate;


public class Trade implements Comparable<Trade>
{
	private int magic = -9999;
	private int ordertype = 0;
	private float openprice = 0;
	private float closeprice = 0;
	private float commision = 0;
	private float orderswap = 0;
	private double profit = 0;
	private float lots = 0;
	private String opentime = null;
	private String closetime = null;
	private String comment = null;
	private int accountnumber = 0;
	private String broker = null;
	private int transactionnumber = 0;
	private int importedcolor = 0;
	private String importedfrom = null;
	private int direction = -99;
	private String symbol = "???";
	private static int count = 0;
	private String indikatorcheckdate = null;
	private static Hashmapper myfxbookhashmapper_g = null;
	
	
	public Trade()
	{
	}

	public Trade(String zeile, String brokeri, boolean normflag,
			int importedcolori)
	{
		// OrderMagicNumber(),OrderType(),OrderOpenPrice(),OrderClosePrice(),OrderCommission(),OrderSwap(),OrderProfit(),OrderLots(),opentime,closetime,
		//OrderComment(),AccountNumber(),OrderTicket(),Symbol(),
		// //0#1#1662.68#1663.37#0#0#-52.21#1#2012.12.27 18:22:32#2012.12.27
		// 18:24:13##499143#49648465
		// falls normflag ==true wird der gewinn auf lotsize 0.1 umgerechnet
		DecimalFormat f = new DecimalFormat("#.00");

		//Modifikation für Sebastians AutoCreator
		//if magic = 99999 than takes the numbers from comment as magic
		
		try
		{
			magic = SG.get_zahl(SG.nteilstring(zeile, "#", 1));
			ordertype = SG.get_zahl(SG.nteilstring(zeile, "#", 2));
			openprice = Float.parseFloat(SG.nteilstring(zeile, "#", 3));
			closeprice = Float.parseFloat(SG.nteilstring(zeile, "#", 4));
			commision = Float.parseFloat(SG.nteilstring(zeile, "#", 5));
			orderswap = Float.parseFloat(SG.nteilstring(zeile, "#", 6));
			profit = SG.get_float_zahl((SG.nteilstring(zeile, "#", 7)), 2);

			// nachkommastellen abscheiden
			// profit=Float.parseFloat(f.format(profit));

			lots = Float.parseFloat(SG.nteilstring(zeile, "#", 8));
			opentime = SG.nteilstring(zeile, "#", 9).replace(".", "-");
			closetime = SG.nteilstring(zeile, "#", 10).replace(".", "-");
			comment = SG.nteilstring(zeile, "#", 11);
			accountnumber = SG.get_zahl(SG.nteilstring(zeile, "#", 12));
			transactionnumber = SG.get_zahl(SG.nteilstring(zeile, "#", 13));
			broker = brokeri;// SG.nteilstring(zeile, "#", 14);

			// vom gewinn erst noch den Swap und die Commision abziehen
			profit = profit + commision + orderswap;

			//AutoCreator modification
			if(magic==99999)
			{
				magic=calMagicFromAutoCreator(comment);
			}
			
			if ((normflag == true) && (lots != 0.1) && (lots > 0))
			{
				// rechne
				// (gewinn/lotsize)*0.1
				double normprof = (profit / lots) * 0.1;
				double normcommision = (commision / lots) * 0.1;
				double normswap = (orderswap / lots) * 0.1;
				commision = (float) normcommision;
				orderswap = (float) normswap;

				profit = normprof;
				lots = (float) 0.1;

			}
			importedcolor = importedcolori;

			// falls neues format
			if (SG.countZeichen(zeile, "#") == 14)
			{
				symbol = SG.nteilstring(zeile, "#", 14);
				direction = SG.get_zahl(SG.nteilstring(zeile, "#", 15));
			}

			if (opentime.length() != 19)
			{
				Tracer.WriteTrace(20, "error dateformat<" + opentime + ">");
				magic = -9999;
				return;
			}
			if (closetime.length() != 19)
			{
				Tracer.WriteTrace(20, "error dateformat<" + closetime + ">");
				magic = -9999;
				return;
			}
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			magic = -9999;
			return;
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			magic = -9999;
			return;
		}
	}

	public Trade(String zeile)
	{
		//hier wird eine Tradezeile aufgebaut
	
		//hier wird die Zeile in teile zerlegt 
		String[] sa = zeile.split("#");
		magic = Integer.valueOf(sa[0]);
		ordertype= Integer.valueOf(sa[1]);
		openprice= Float.valueOf(sa[2]);
		closeprice=Float.valueOf(sa[3]);
		commision=Float.valueOf(sa[4]);
		orderswap=Float.valueOf(sa[5]);
		profit=Double.valueOf(sa[6]);
		lots = Float.valueOf(sa[7]);
		opentime=String.valueOf(sa[8]);
		closetime=String.valueOf(sa[9]);
		comment=String.valueOf(sa[10]);
		accountnumber=Integer.valueOf(sa[11]);
		transactionnumber=Integer.valueOf(sa[12]);
		broker=String.valueOf(sa[13]);
	}
	
	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public int getMagic()
	{
		return magic;
	}

	public void setMagic(int magic)
	{
		this.magic = magic;
	}

	public double getProfit()
	{
		return profit;
	}

	public void setProfit(double profit)
	{
		this.profit = profit;
	}

	public float getLots()
	{
		return lots;
	}

	public void setLots(float lots)
	{
		this.lots = lots;
	}

	public boolean checkTimefilter(Timefilter timefilter)
	{
		boolean stundenflag = false, weekflag = false;
		Date opendate = getOpentimeDate();

		Calendar cal = Calendar.getInstance();
		cal.setTime(opendate);

		int stunde = cal.get(Calendar.HOUR_OF_DAY);
		int day = ((cal.get(Calendar.DAY_OF_WEEK)) + 5) % 7;
		stundenflag = timefilter.gettime(stunde);
		weekflag = timefilter.getday(day);

		if ((stundenflag == true) && (weekflag == true))
			return true;

		return false;

	}

	public Date getOpentimeDate()
	{
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date date = dt.parse(opentime.replace(".", "-"));
			return date;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Date getClosetimeDate()
	{
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date date = dt.parse(closetime.replace(".", "-"));
			return date;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getOpentime()
	{
		return opentime.replace(".", "-");
	}

	public void setOpentime(String opentime)
	{
		this.opentime = opentime.replace(".", "-");
		;
	}

	public String getClosetime()
	{
		return closetime.replace(".", "-");
	}

	public void setClosetime(String closetime)
	{
		this.closetime = closetime.replace(".", "-");
		;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getBroker()
	{
		return broker;
	}

	public void setBroker(String broker)
	{
		this.broker = broker;
	}

	public int getAccountnumber()
	{
		return accountnumber;
	}

	public void setAccountnumber(int accountnumber)
	{
		this.accountnumber = accountnumber;
	}

	public int getTransactionnumber()
	{
		return transactionnumber;
	}

	public void setTransactionnumber(int transactionnumber)
	{
		this.transactionnumber = transactionnumber;
	}

	public int getOrdertype()
	{
		return ordertype;
	}

	public void setOrdertype(int ordertype)
	{
		this.ordertype = ordertype;
	}

	public float getOpenprice()
	{
		return openprice;
	}

	public void setOpenprice(float openprice)
	{
		this.openprice = openprice;
	}

	public float getCloseprice()
	{
		return closeprice;
	}

	public void setCloseprice(float closeprice)
	{
		this.closeprice = closeprice;
	}

	public float getCommision()
	{
		return commision;
	}

	public void setCommision(float commision)
	{
		this.commision = commision;
	}

	public float getOrderswap()
	{
		return orderswap;
	}

	public void setOrderswap(float orderswap)
	{
		this.orderswap = orderswap;
	}

	public int getImportedcolor()
	{
		return importedcolor;
	}

	public void setImportedcolor(int color)
	{
		this.importedcolor = color;
	}

	public String getImportedfrom()
	{
		return importedfrom;
	}

	public void setImportedfrom(String importedfrom)
	{
		this.importedfrom = importedfrom;
	}

	public String calcDirection()
	{
		if (direction == 0)
			return ("buy");
		else if (direction == 1)
			return ("sell");
		else
			return (String.valueOf(direction));

	}

	public String getIndikatorcheckdate()
	{
		return indikatorcheckdate;
	}

	public void setIndikatorcheckdate(String indikatorcheckdate)
	{
		this.indikatorcheckdate = indikatorcheckdate;
	}

	public String calcTradezeile()
	{
		String retstring=magic + "#" + ordertype + "#" + openprice + "#" + closeprice
				+ "#" + commision + "#" + orderswap + "#" + profit + "#" + lots
				+ "#" + opentime + "#" + closetime + "#" + comment + "#"
				+ accountnumber + "#" + transactionnumber + "#" + broker;
		return retstring;

	}


 
	
	public String calcDuration()
	{
		Date opendate = Mondate.convTradezeit(opentime);
		Date closedate = Mondate.convTradezeit(closetime);
		long mindiff = Math.abs(Mondate.Zeitdiff(opendate, closedate) / 60);
		return (String.valueOf(mindiff));
	}

	public int compareTo(Trade vergleichstrade)
	{
		int retval = 0;

		count++;
		String vglzeit = vergleichstrade.getClosetime();

		// //Zeitanpassung von
		// 2012.10.18 14:36:25 -> 2012-10-18 14:36:25
		String time1 = vglzeit.replace(".", "-");
		String time2 = closetime.replace(".", "-");

		if (time1.compareTo(time2) == 0)
			retval = 0; // gleich
		else if (Tools.datum_ist_aelter(time1, time2) == true)
			retval = -1;// closetime ist nicht älter
		else
			retval = 1;// closetime ist älter

		return retval;
	}

	public static String convMyfxbooktradezeile(String tradezeile)
	{
		
		//konvertiert eine myfxbookzeile in eine Tradezeile
		if(tradezeile.contains("Ticket")==true)
		{
			Tracer.WriteTrace(20, "I: got Headline<"+tradezeile+"> from myfxbook");
			myfxbookhashmapper_g= new Hashmapper(tradezeile);
			return null;
		}
		if((tradezeile.contains("Deposit")==true)||(tradezeile.contains("Withdrawal")==true))
		{
			Tracer.WriteTrace(20, "I: waste this tradezeile<"+tradezeile+"> from myfxbook");
			return null;
		}
		if(tradezeile.length()<100)
		{
			Tracer.WriteTrace(20, "I: waste this tradezeile<"+tradezeile+"> from myfxbook");
			return null;
		}
		
		// (magic + "#" + ordertype + "#" + openprice + "#" + closeprice
		//			+ "#" + commision + "#" + orderswap + "#" + profit + "#" + lots
		//			+ "#" + opentime + "#" + closetime + "#" + comment + "#"
		//			+ accountnumber + "#" + transactionnumber + "#" + broker);
		
		
		//baue die Tradezeile
		myfxbookhashmapper_g.addLine(tradezeile);
		
		
		//Ticket,Open Date,Close Date,Symbol,Action,Lots,SL,TP,Open Price,Close Price,Commission,Swap,Pips,Profit,Gain,
		//Comment,Magic Number,Duration (DD:HH:MM:SS),Profitable(%),Profitable(time duration),Drawdown,Risk:Reward,Max(pips),
		//Max(EUR),Min(pips),Min(EUR),Entry Accuracy(%),Exit Accuracy(%),ProfitMissed(pips),ProfitMissed(EUR)

			
		String magic=myfxbookhashmapper_g.getAttribute(tradezeile,"Magic Number");
		String action=myfxbookhashmapper_g.getAttribute(tradezeile,"Action");
		if(action.equals("Buy")==true)
			action="0";
		else if(action.equals("Sell")==true)
				action="1";
		else
			Tracer.WriteTrace(10, "E:wrong direction <"+action+">");
		
		String openprice=myfxbookhashmapper_g.getAttribute(tradezeile,"Open Price");
		String closeprice=myfxbookhashmapper_g.getAttribute(tradezeile,"Close Price");
		String commision=myfxbookhashmapper_g.getAttribute(tradezeile,"Commission");
		String orderswap=myfxbookhashmapper_g.getAttribute(tradezeile,"Swap");
		String profit=myfxbookhashmapper_g.getAttribute(tradezeile,"Profit");
		String lots=myfxbookhashmapper_g.getAttribute(tradezeile,"Lots");

		//datumskonvertierung
		//von "07/08/2014 23:59" -> nach "2013.04.23 10:35:26"
		String opentime=convMyfxbookdate(myfxbookhashmapper_g.getAttribute(tradezeile,"Open Date"));
		String closetime=convMyfxbookdate(myfxbookhashmapper_g.getAttribute(tradezeile,"Close Date"));
		String comment=myfxbookhashmapper_g.getAttribute(tradezeile,"Comment");
		String accountnumber="0";
		String transactionnumber=myfxbookhashmapper_g.getAttribute(tradezeile,"Ticket");
		String symbol=myfxbookhashmapper_g.getAttribute(tradezeile,"Symbol");
		
		String retstring=magic + "#" + action + "#" + openprice + "#" + closeprice
				+ "#" + commision + "#" + orderswap + "#" + profit + "#" + lots
				+ "#" + opentime + "#" + closetime + "#" + comment + "#"
				+ accountnumber + "#" + transactionnumber+"#"+symbol+"#0";
		
		
		return retstring;
	}
	private static String convMyfxbookdate(String input)
	{
		//datumskonvertierung
		//von "07/08/2014 23:59" -> nach "2013.04.23 10:35:26"

		
		String year=input.substring(6,10);
		String month=input.substring(0, 2);
		String day=input.substring(3,5);
		String time=input.substring(11);
		
		String output=year+"."+month+"."+day+" "+time+":00";
		return output;
		
	}
	private int calMagicFromAutoCreator(String comment)
	{
		//take the number out of the comment
		String zahlstring= comment.replaceAll("[^0-9]", "");

		//shortend to max 9 digits
		if(zahlstring.length()>9)
			zahlstring=zahlstring.substring(0,9);
		int zahl=Integer.valueOf(zahlstring);
		return zahl;
		
	}
}
