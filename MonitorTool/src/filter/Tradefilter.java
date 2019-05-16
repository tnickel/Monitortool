package filter;

import hiflsklasse.Inf;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;
import data.GlobalVar;
import data.Profit;
import data.Rootpath;
import data.Trade;

public class Tradefilter
{
	// hier wird configuriert welche Trades angezeigt werden
	boolean profitnormalisierung = false;
	boolean nocancel = true;
	boolean showopenorders = false;
	boolean forgetoldeas = false;
	boolean loadexpired = true;
	boolean commentselection = false;
	boolean mintradesselection = false;
	boolean tradefiltermainselection = false;
	boolean minprofitselection = false;
	boolean info1selection = false;
	boolean info2selection = false;
	boolean automaticselection = false;
	boolean profitfaktorselection = false;
	boolean drawdownselection = false;
	boolean lasttradedayselection = false;
	boolean tradefilterbutton=false;

	int minfavorit = 0;
	int maxfavorit = 10;
	String comment = null;
	int mintrades = 0;
	int mintradesAll = 0;
	int minprofit10 = 0;
	int minprofit30 = 0;
	int minprofitall = 0;
	String info1 = null;
	String info2 = null;
	float profitfaktor = 0;
	float drawdown = 0;
	int lasttradesdays = 0;
	String tradestartdate="1.1.2012";

	private static int isloaded = 0;

	public Tradefilter()
	{
		init();
	}

	public boolean isCommentselection()
	{
		return commentselection;
	}

	public void setCommentselection(boolean commentselection)
	{
		this.commentselection = commentselection;
	}

	public boolean isProfitnormalisierung()
	{
		return profitnormalisierung;
	}

	public void setProfitnormalisierung(boolean profitnormalisierung)
	{
		this.profitnormalisierung = profitnormalisierung;
	}

	public boolean isNocancel()
	{
		return nocancel;
	}

	public void setNocancel(boolean nocancel)
	{
		this.nocancel = nocancel;
	}

	public boolean isShowopenorders()
	{
		return showopenorders;
	}

	public void setShowopenorders(boolean showopenorders)
	{
		this.showopenorders = showopenorders;
	}

	public boolean isForgetoldeas()
	{
		return forgetoldeas;
	}

	public void setForgetoldeas(boolean forgetoldeas)
	{
		this.forgetoldeas = forgetoldeas;
	}

	public boolean isLoadexpired()
	{
		return loadexpired;
	}

	public void setLoadexpired(boolean loadexpired)
	{
		this.loadexpired = loadexpired;
	}

	public int getMinfavorit()
	{
		return minfavorit;
	}

	public void setMinfavorit(int minfavorit)
	{
		this.minfavorit = minfavorit;
	}

	public int getMaxfavorit()
	{
		return maxfavorit;
	}

	public void setMaxfavorit(int maxfavorit)
	{
		this.maxfavorit = maxfavorit;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public int getMintrades()
	{
		return mintrades;
	}

	public void setMintrades(int mintrades)
	{
		this.mintrades = mintrades;
	}

	public int getMintradesAll()
	{
		return mintradesAll;
	}

	public void setMintradesAll(int mintradesAll)
	{
		this.mintradesAll = mintradesAll;
	}

	public boolean isMintradesselection()
	{
		return mintradesselection;
	}

	public void setMintradesselection(boolean mintradesselection)
	{
		this.mintradesselection = mintradesselection;
	}

	public boolean isTradefiltermainselection()
	{
		return tradefiltermainselection;
	}

	public void setTradefiltermainselection(boolean tradefiltermainselection)
	{
		this.tradefiltermainselection = tradefiltermainselection;
	}

	public int getMinprofit10()
	{
		return minprofit10;
	}

	public void setMinprofit10(int minprofit10)
	{
		this.minprofit10 = minprofit10;
	}

	public int getMinprofit30()
	{
		return minprofit30;
	}

	public void setMinprofit30(int minprofit30)
	{
		this.minprofit30 = minprofit30;
	}

	public int getMinprofitall()
	{
		return minprofitall;
	}

	public void setMinprofitall(int minprofitall)
	{
		this.minprofitall = minprofitall;
	}

	public boolean isProfitfaktorselection()
	{
		return profitfaktorselection;
	}

	public void setProfitfaktorselection(boolean profitfaktorselection)
	{
		this.profitfaktorselection = profitfaktorselection;
	}

	public boolean isDrawdownselection()
	{
		return drawdownselection;
	}

	public void setDrawdownselection(boolean drawdownselection)
	{
		this.drawdownselection = drawdownselection;
	}

	public boolean isLasttradedayselection()
	{
		return lasttradedayselection;
	}

	public void setLasttradedayselection(boolean lasttradedayselection)
	{
		this.lasttradedayselection = lasttradedayselection;
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

	public int getLasttradesdays()
	{
		return lasttradesdays;
	}

	public void setLasttradesdays(int lasttradesdays)
	{
		this.lasttradesdays = lasttradesdays;
	}

	public boolean checkConditions(Profit prof)
	{
		// vergesse die alten trades, zeige nix an wenn er x=forgetdays nicht
		// mehr getraded hat
		if ((isForgetoldeas() == true)
				&& (prof.calcDaysNeusterTrade() > GlobalVar.getForgetdays()))
			return false;

		// falls der Tradefilter garnicht aktiviert ist wird auf jeden Fall
		// angezeigt
		if (isTradefiltermainselection() == false)
			return true;

		if (isMintradesselection() == true)
		{
			if (prof.getAnzmonatstrades() < mintrades)
				return false;

			if (prof.getGestrades() < mintradesAll)
				return false;
		}

		if (isCommentselection() == true)
			if (prof.getComment().toLowerCase().contains(comment.toLowerCase()) == false)
				return false;

		if (isMinprofitselection() == true)
		{
			if (prof.getZehntagegewinn() < ((float) minprofit10))
				return false;
			if (prof.getDreizigtragegewinn() < ((float) minprofit30))
				return false;
			if (prof.getGesgewinn() < ((float) minprofitall))
				return false;
		}
		if (isInfo1selection() == true)
		{
			if (prof.getInfo1() == null)
				return false;
			if (prof.getInfo1().toLowerCase().contains(info1) == false)
				return false;
		}
		if (isInfo2selection() == true)
		{
			if (prof.getInfo2() == null)
				return false;
			if (prof.getInfo2().toLowerCase().contains(info2) == false)
				return false;
		}

		if (isProfitfaktorselection() == true)
		{
			if (profitfaktor > prof.getProfitfaktor())
				return false;
		}
		if (isDrawdownselection() == true)
		{
			if (drawdown < prof.getDrawdown())
				return false;
		}
		if (isLasttradedayselection() == true)
		{
			if (lasttradesdays < prof.calcDaysNeusterTrade())
				return false;
		}
		return true;
	}

	public boolean isMinprofitselection()
	{
		return minprofitselection;
	}

	public void setMinprofitselection(boolean minprofitselection)
	{
		this.minprofitselection = minprofitselection;
	}

	public boolean isInfo1selection()
	{
		return info1selection;
	}

	public void setInfo1selection(boolean info1selection)
	{
		this.info1selection = info1selection;
	}

	public boolean isInfo2selection()
	{
		return info2selection;
	}

	public void setInfo2selection(boolean info2selection)
	{
		this.info2selection = info2selection;
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

	public boolean isAutomaticselection()
	{
		return automaticselection;
	}

	public void setAutomaticselection(boolean automaticselection)
	{
		this.automaticselection = automaticselection;
	}

	public boolean isTradefilterbutton()
	{
		return tradefilterbutton;
	}

	public void setTradefilterbutton(boolean tradefilterbutton)
	{
		this.tradefilterbutton = tradefilterbutton;
	}

	public String getTradestartdate()
	{
		return tradestartdate;
	}

	public void setTradestartdate(String tradestartdate)
	{
		this.tradestartdate = tradestartdate;
	}

	public void init()
	{
		String fnam = Rootpath.getRootpath() + "\\conf\\tradefilter.xml";
		// semaphore on
		/*
		 * if (isloaded == 0) {
		 * 
		 * 
		 * 
		 * if(FileAccess.FileAvailable0(fnam)==false) return;
		 * 
		 * File filen=new File(fnam); if(filen.length()<10) return;
		 * 
		 * // tradeliste laden Inf inf = new Inf(); inf.setFilename(fnam);
		 * 
		 * // tradeliste einlesen commentselection= (Boolean)inf.loadXST();
		 * profitnormalisierung = (Boolean) inf.loadXST(); nocancel= (Boolean)
		 * inf.loadXST(); showopenorders=(Boolean) inf.loadXST();
		 * forgetoldeas=(Boolean) inf.loadXST(); loadexpired=(Boolean)
		 * inf.loadXST(); minfavorit=(Integer) inf.loadXST();
		 * maxfavorit=(Integer) inf.loadXST(); comment=new String((String)
		 * inf.loadXST());
		 * 
		 * inf.close(); isloaded = 1; }
		 */

	}
	public boolean checkTradeIsToOld(Trade tr)
	{
		
		if(tradefilterbutton==false)
			return false;
		else
		{
			String closetime=tr.getClosetime();
			if(Tools.CheckDateIsOlder(closetime,tr.getClosetime())==true)
					return true;
			else
				return false;
			
		}
	}
	public void store_depricated()
	{
		String fnam = Rootpath.getRootpath() + "\\conf\\tradefilter.xml";
		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.saveXST(commentselection);
		inf.saveXST(profitnormalisierung);
		inf.saveXST(nocancel);
		inf.saveXST(showopenorders);
		inf.saveXST(forgetoldeas);
		inf.saveXST(loadexpired);
		inf.saveXST(minfavorit);
		inf.saveXST(maxfavorit);
		inf.saveXST((String) comment);
		inf.saveXST(info1);
		inf.saveXST(info2);
		inf.close();
	}

}
