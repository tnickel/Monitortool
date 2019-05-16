package data;

import gui.Mbox;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GewinnjahrStategienSum extends GewinnjahrStrategie
{
	// das ist die gesammtgewinnsumme über alle strategien
	private int firstMonatsIndex = -99;
	private int firstJahresIndex = -99;

	private int gesanzstrategien = 0;
	private int gesanzposstrategien = 0;
	private int gesanznegstrategien = 0;

	public void addStrategie(GewinnjahrStrategie gstr)
	{
		// die Strategie die alles gewinne aller trades beinhaltet wird in der
		// summenklasse mit aufgenommen
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
			{
				this.monatsgewinn[j][i] += gstr.monatsgewinn[j][i];
				this.postradezaehler[j][i] += gstr.postradezaehler[j][i];
				this.negtradezaehler[j][i] += gstr.negtradezaehler[j][i];
				this.grossprofit[j][i] += gstr.grossprofit[j][i];
				this.grossloss[j][i] += gstr.grossloss[j][i];
				this.gewanz[j][i] += gstr.gewanz[j][i];
			}
		}
		if (firstMonatsIndex < 0)
			firstMonatsIndex = gstr.getFmonatsindex();
		if (firstJahresIndex < 0)
			firstJahresIndex = gstr.getFjahresindex();

		// den zähler für die gesammtstrategien erhöhen
		gesanzstrategien++;
		if (gstr.calcGesammtgewinn() > 0)
			gesanzposstrategien++;
		else if (gstr.calcGesammtgewinn() <= 0)
			gesanznegstrategien++;

	}

	public String getZeitraumname()
	{
		String firstdate=null;
		String lastdate=null;
		
		
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
			{
				if(firstdate==null)
					if(monatsgewinn[j][i]>0)
						firstdate=i+"|"+j;
			}
		}
		
		
		for (int j = 19; j >0; j--)
		{
			for (int i = 11; i >0; i--)
			{
				if(lastdate==null)
					if(monatsgewinn[j][i]>0)
						lastdate=i+"|"+j;
			}
		}
		
		return ("von "+ firstdate+" bis "+lastdate);
	}
	
	private int calcPostrades()
	{
		// zähle für jeden Monat die positiven Trades
		int trades = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
				trades += postradezaehler[j][i];
		}
		return trades;
	}

	private int calcNegtrades()
	{
		// zähle für jeden Monat die negativen Trades
		int trades = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
				trades += negtradezaehler[j][i];
		}
		return trades;
	}

	private float calcGrossprofit()
	{
		// zähle für jeden Monat den Grossprofit
		float sum = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
				sum += grossprofit[j][i];
		}
		return sum;
	}

	private float calcGrossloss()
	{
		// zähle für jeden Monat den Grossloss
		float sum = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
				sum += grossloss[j][i];
		}
		return sum;
	}

	private int calcGewanz()
	{
		int trades = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
				trades += gewanz[j][i];
		}
		return trades;
	}

	public int getFirstMonatsIndex()
	{
		return firstMonatsIndex;
	}

	public int getFirstJahresIndex()
	{
		return firstJahresIndex;
	}

	public int calcLastJahresIndex()
	{
		//ermittelt das letzte jahr wo gewinn erwirtschaftet wurde
		int lastjahresindex = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
			{
				float mongew = monatsgewinn[j][i];
				if (mongew > 0)
				{
					if (j > lastjahresindex)
						lastjahresindex = j;
				}
			}
		}
		return lastjahresindex;
	}

	public float calcNettprofit()
	{
		float sum = 0;
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
				sum += monatsgewinn[j][i];
		}
		return sum;
	}

	public void showResult()
	{
		float pf = (Math.abs(calcGrossprofit()) / Math.abs(calcGrossloss()));

		NumberFormat numberFormat = new DecimalFormat("0.00");
		Mbox.Infobox("result: #trades+<" + calcPostrades() + "> #trades-<"
				+ calcNegtrades() + "> ges<" + calcGewanz() + "> Grprof<"
				+ calcGrossprofit() + "> Grloss<" + calcGrossloss() + "> sum<"
				+ calcNettprofit() + "> pf(gross)<" + numberFormat.format(pf)
				+ "> #strategien+<" + gesanzposstrategien + "> #strategien-<"
				+ gesanznegstrategien + ">");

	}

}
