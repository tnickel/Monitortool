package ComData;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trade  implements Comparable<Trade> 
{
	private int magic = 0;
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
	private boolean importedflag=false;
	private String importedfrom=null;
	private int direction=-99;
	private String symbol="???";
	
	public Trade()
	{}
	
	public Trade(String zeile, String brokeri,boolean normflag,boolean importedflagi)
	{
		// OrderMagicNumber(),OrderType(),OrderOpenPrice(),OrderClosePrice(),OrderCommission(),OrderSwap(),OrderProfit(),OrderLots(),opentime,closetime,OrderComment(),AccountNumber(),OrderTicket()
		// //0#1#1662.68#1663.37#0#0#-52.21#1#2012.12.27 18:22:32#2012.12.27
		// 18:24:13##499143#49648465
		// falls normflag ==true wird der gewinn auf lotsize 0.1 umgerechnet
		DecimalFormat f = new DecimalFormat("#.00");

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
			broker = brokeri;//SG.nteilstring(zeile, "#", 14);

			//vom gewinn erst noch den Swap und die Commision abziehen
			profit=profit+commision+orderswap;
			
			
			if ((normflag == true) && (lots != 0.1) && (lots > 0))
			{
				// rechne
				// (gewinn/lotsize)*0.1
				double normprof = (profit / lots) * 0.1;
				double normcommision=(commision/lots)*0.1;
				double normswap=(orderswap/lots)*0.1;
				commision=(float)normcommision;
				orderswap=(float)normswap;
				
				profit = normprof;
				lots = (float) 0.1;

			}
			importedflag=importedflagi;
			
			//falls neues format
			if(SG.countZeichen(zeile, "#")==14)
			{
				symbol=SG.nteilstring(zeile, "#", 14);
				direction = SG.get_zahl(SG.nteilstring(zeile, "#", 15));
			}
				
				
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public boolean getImportedflag()
	{
		return importedflag;
	}

	public void setImportedflag(boolean importedflag)
	{
		this.importedflag = importedflag;
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
		if(direction==0)
			return ("buy");
		else if(direction==1)
			return ("sell");
		else 
			return ("?");
		/*
		if((openprice<closeprice)&&(profit>0))
			return ("buy");
		else if((openprice<closeprice)&&(profit<0))
			return ("buy");
		else if((openprice>closeprice)&&(profit<0))
			return ("sell");
		else if((openprice>closeprice)&&(profit>0))
			return ("sell");
		else
		*/
		
			
	}
	public String calcDuration()
	{
		Date opendate= Mondate.convTradezeit(opentime);
		Date closedate=Mondate.convTradezeit(closetime);
		long mindiff=Math.abs(Mondate.Zeitdiff(opendate, closedate)/60);
		return(String.valueOf(mindiff));
	}
	
	public int compareTo(Trade vergleichstrade)
	{
		int retval = 0;

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
}
