package data;

import hiflsklasse.SG;
import hiflsklasse.ToolsException;

import java.text.DecimalFormat;
import java.util.Date;

import datefunkt.Mondate;


public class Profit implements Comparable<Profit>
{
	// magicnumber
	private int magic = 0;
	// gesamtanzahl der gespeicherten Trades
	private int gestrades = 0;
	// gesamttage die aufgezeichnet wurden
	private int gestage = 0;
	// gewinn der letzten 30 tage
	private float dreizigtragegewinn = 0;
	// gewinn der letzten 10 Tage
	private float zehntagegewinn = 0;
	// anz trades im letzten Monat
	private int anzmonatstrades = 0;
	// anz trades in den letzten 10 Tagen
	private int anztradeslastzehn = 0;
	// gesamtgewinn
	private float gesgewinn = 0;
	// gesamtpips
	private int pips = 0;

	// activated- sagt ob ea auf dem verknüpften realaccount aktiviert ist
	private int on = 0;
	private String comment = null;
	private String info1 = null;
	private String info2 = null;
	private String symbol = "???";
	private float profitfaktor = 0;
	private float drawdown = 0;
	private float pz1=0;
	

	private Tradeliste tradeliste_glob = null;
	// broker
	private String broker = "";

	// imported flag
	// dies flag zeigt an, das einige Trades hier importiert worden sind
	private int importedcolor = 0;

	public Profit(String zeile, Trade trade)
	{
		DecimalFormat f = new DecimalFormat("#.00");

		try
		{
			magic = SG.get_zahl(SG.nteilstring(zeile, "#", 1));
			gestrades = SG.get_zahl(SG.nteilstring(zeile, "#", 2));
			gestage = SG.get_zahl(SG.nteilstring(zeile, "#", 3));
			dreizigtragegewinn = Float
					.parseFloat(SG.nteilstring(zeile, "#", 4));
			zehntagegewinn = Float.parseFloat(SG.nteilstring(zeile, "#", 5));
			anzmonatstrades = SG.get_zahl(SG.nteilstring(zeile, "#", 6));
			anztradeslastzehn = SG.get_zahl(SG.nteilstring(zeile, "#", 7));
			gesgewinn = Float.parseFloat(SG.nteilstring(zeile, "#", 8));
			broker = new String(SG.nteilstring(zeile, "#", 9));
			pips = SG.get_zahl(SG.nteilstring(zeile, "#", 10));
			on = SG.get_zahl(SG.nteilstring(zeile, "#", 11));
			comment = new String(SG.nteilstring(zeile, "#", 12));
			info1 = new String(SG.nteilstring(zeile, "#", 13));
			info2 = new String(SG.nteilstring(zeile, "#", 14));
			if (SG.countZeichen(zeile, "#") >= 14)
			{
				symbol = new String(SG.nteilstring(zeile, "#", 15));

			}
			if (SG.countZeichen(zeile, "#") >= 15)
			{
				pz1 = SG.get_zahl(SG.nteilstring(zeile, "#", 16));

			}
			// den zugehörigen Trade speichern, beim ersten male
			//die tradeliste löschen
			tradeliste_glob = new Tradeliste(Rootpath.rootpath+ "\\data\\tradeliste.xml");
			tradeliste_glob.addTradeElem(trade);

		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addTrade(Trade trade)
	{
		tradeliste_glob.addTradeElem(trade);
	}
	
	public int getMagic()
	{
		return magic;
	}

	public void setMagic(int magic)
	{
		this.magic = magic;
	}

	public int getGestrades()
	{
		return gestrades;
	}

	public void setGestrades(int gestrades)
	{
		this.gestrades = gestrades;
	}

	public int getGestage()
	{
		return gestage;
	}

	public void setGestage(int gestage)
	{
		this.gestage = gestage;
	}

	public float getDreizigtragegewinn()
	{
		return dreizigtragegewinn;
	}

	public void setDreizigtragegewinn(float dreizigtragegewinn)
	{
		this.dreizigtragegewinn = dreizigtragegewinn;
	}

	public float getZehntagegewinn()
	{
		return zehntagegewinn;
	}

	public void setZehntagegewinn(float zehntagegewinn)
	{
		this.zehntagegewinn = zehntagegewinn;
	}

	public int getAnzmonatstrades()
	{
		return anzmonatstrades;
	}

	public void setAnzmonatstrades(int anzmonatstrades)
	{
		this.anzmonatstrades = anzmonatstrades;
	}

	public int getAnztradeslastzehn()
	{
		return anztradeslastzehn;
	}

	public void setAnztradeslastzehn(int anztradeslastzehn)
	{
		this.anztradeslastzehn = anztradeslastzehn;
	}

	public float getGesgewinn()
	{
		return gesgewinn;
	}

	public void setGesgewinn(float gesgewinn)
	{
		this.gesgewinn = gesgewinn;
	}

	public String getBroker()
	{
		return broker;
	}

	public void setBroker(String broker)
	{
		this.broker = broker;
	}

	public int getPips()
	{
		return pips;
	}

	public void setPips(int pips)
	{
		this.pips = pips;
	}

	public int getOn()
	{
		return on;
	}

	public void setOn(int on)
	{
		this.on = on;
	}

	public int getImportedColor()
	{
		return importedcolor;
	}

	public void setImportColor(int importedcol)
	{
		this.importedcolor = importedcol;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getInfo1()
	{
		return info1;
	}

	public void setInfo1(String info1)
	{
		this.info1 = info1;
	}

	public String getInfo2()
	{
		return info2;
	}

	public void setInfo2(String info2)
	{
		this.info2 = info2;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public float getProfitfaktor()
	{
		return profitfaktor;
	}

	public void setProfitfaktor(float profitfaktor)
	{
		this.profitfaktor = profitfaktor;
	}

	public float getDrawdown()
	{
		return drawdown;
	}

	public void setDrawdown(float drawdown)
	{
		this.drawdown = drawdown;
	}

	public void calcProfitfaktor()
	{
		// die profitliste wird bei der profitanzeige einmal aufgebaut
		// Dies wird in <CalcProfitTable> gemacht.
		// nachdem die Liste aufgebaut ist muss noch der Drawdown berechnet
		// werden.
		// die profitliste besitzt hierzu für jeden profit die zugehörige
		// Tradeliste
		this.profitfaktor=(float)tradeliste_glob.calcProfitfactor();
	}

	public void calcDrawdown()
	{
		// die profitliste wird bei der profitanzeige einmal aufgebaut
		// Dies wird in <CalcProfitTable> gemacht.
		// nachdem die Liste aufgebaut ist muss noch der Drawdown berechnet
		// werden.
		// die profitliste besitzt hierzu für jeden profit die zugehörige
		// Tradeliste
		this.drawdown=(float)tradeliste_glob.calcDrawdown();
	}
	public int calcDaysNeusterTrade()
	{
		//rechnet nach wie alt der neuste Trade ist
		//rückgabe in Tagen
		tradeliste_glob.calcMinMaxdate();
		Date lasttrade=tradeliste_glob.getMaxdate();
	
		long sek=Mondate.SekundenVergangen(lasttrade);
		//86400 sekunden hat ein Tag
		long tage=(sek/86400);
		
		return ((int)tage);
	}
	public int calcConsecLooses()
	{
		int cl=tradeliste_glob.calcConsecLooses();
		return cl;
		
	}
	
	public float getPz1()
	{
		float pfloat=Float.valueOf(pz1);
		double dpz1 = Math.round(pfloat * 10) / 10.0;
		return (float)dpz1;
	}

	public void setPz1(float pz1)
	{
		float pfloat=Float.valueOf(pz1);
		double dpz1 =(float)Math.round(pfloat * 10) / 10.0;
		
		this.pz1 = (float)dpz1;
	}

	public int compareTo(Profit vergleichsprofit)
	{
		int retval = 0;
		switch (GlobalVar.getSortcriteria())
		{
		case 1:// magic
			if (vergleichsprofit.getMagic() > this.getMagic())
				retval = 1;
			else if (vergleichsprofit.getMagic() < this.getMagic())
				retval = -1;
			else
				retval = 0;
			break;
		case 2:// comment
			String verglstr = vergleichsprofit.getComment();
			if (this.getComment().equals(verglstr))
				retval = 0;
			else
				retval = 1;
			break;
		case 3:// profit10
			float tage10vergl = vergleichsprofit.getZehntagegewinn();
			float tage10gew = this.getZehntagegewinn();

			if (tage10vergl > tage10gew)
				retval = 1;
			else if (tage10vergl < tage10gew)
				retval = -1;
			else
				retval = 0;
			break;
		case 4:// profit30
			float tage30vergl = vergleichsprofit.getDreizigtragegewinn();
			float tage30gew = this.getDreizigtragegewinn();

			if (tage30vergl > tage30gew)
				retval = 1;
			else if (tage30vergl < tage30gew)
				retval = -1;
			else
				retval = 0;
			break;
		case 5:// profitall
			float verglgew = vergleichsprofit.getGesgewinn();
			float gesgew = this.getGesgewinn();

			if (verglgew > gesgew)
				retval = 1;
			else if (verglgew < gesgew)
				retval = -1;
			else
				retval = 0;
			break;
		case 6:// anztrades
			if (this.gestrades > vergleichsprofit.gestrades)
				retval = -1;
			else if (this.gestrades < vergleichsprofit.gestrades)
				retval = 1;
			else
				retval = 0;
			break;
		case 7:// info1
			String info1 = vergleichsprofit.getInfo1();
			if ((info1.equals("0")) || ((this.getInfo1().equals("0"))))
				return 1;// ungleich

			if (this.getInfo1().equals(info1))
				retval = 0;
			else
				retval = 1;
			break;
		case 8:// info2
			String info2 = vergleichsprofit.getInfo2();
			if (this.getInfo2().equals(info2))
				retval = 0;
			else
				retval = 1;
			break;
		case 9:// profitfaktor
			float verglpf = vergleichsprofit.getProfitfaktor();
			float pf = this.getProfitfaktor();

			if (verglpf > pf)
				retval = 1;
			else if (verglpf < pf)
				retval = -1;
			else
				retval = 0;
			break;
		case 10:// drawdown
			float vergldd = vergleichsprofit.getDrawdown();
			float dd = this.getDrawdown();

			if (vergldd > dd)
				retval = -1;
			else if (vergldd < dd)
				retval = 1;
			else
				retval = 0;
			break;
		case 11://pz1
			float verglpz1=vergleichsprofit.getPz1();
			float pz1tmp=this.getPz1();
			if(verglpz1>pz1tmp)
				retval=1;
			else if (verglpz1<pz1tmp)
				retval=-1;
			else
				retval=0;
			break;
			
		}
		// System.out.println("vergleiche a<"+tage10vergl+"> mit b<"+tage10gew+"> retval<"+retval+">");
		return retval;
	}
}
