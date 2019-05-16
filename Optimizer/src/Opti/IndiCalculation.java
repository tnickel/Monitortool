package Opti;

import gui.Mbox;
import hiflsklasse.Tracer;
import jhilf.JProgressWin;
import data.IndicatordataHashmap_0A;
import data.OptimizeResult;
import data.Timefilter;
import data.Trade;
import data.Tradeliste;

public class IndiCalculation
{
	// in der hashmap werden die Zeiten gespeichert und trades
	IndicatordataHashmap_0A indidat_glob = null;

	// diese klasse uebernimmt alle berechnungen mit den Indikatoren
	public IndiCalculation(IndicatordataHashmap_0A indidat)
	{
		indidat_glob = indidat;
	}

	public String getIndiName(int indiindex)
	{
		return (indidat_glob.calcIndexname(indiindex));
	}

	public OptimizeResult workIndi(Tradeliste tradeliste, int indiindex1,
			int indiindex2, String mode)
	{
		// mode=0=>dann erste index nur auf buy und der zweite index auf sell
		// anwenden
		// mode=1=>dann erste index nur auf sell und den zweiten index nur auf
		// buy anwenden

		
		int maxpos = tradeliste.getsize();

		// anzahl der marktierten positiven trades
		int postrades = 0;
		OptimizeResult os = new OptimizeResult();

		// dies ist das markerarray was die winner markiert
		int[] marker = new int[maxpos];

		//die winner werden markiert, d.h. es werden die Trades genommen wo der vergleichsindikator an der 
		//entsprechenden Position eine 1 hat
		markWinner(tradeliste, marker, maxpos, indiindex1, indiindex2, mode);

		// die gewinnsumme berechnen
		double gewinnsumme = 0;
		//betrachte jeden einzelnen Trade
		for (int pos = 1; pos < maxpos; pos = pos + 1)
		{
			
			if (marker[pos] == 1)
			{
				// das gewinnelement wird gezählt
				Trade trade = tradeliste.getelem(pos);
				gewinnsumme = gewinnsumme + trade.getProfit();
				postrades++;
			}
		}

		// erstelle hier eine neue Tradeliste die nur trades beinhaltet die
		// markiert sind
		// mit dieser neuen Tradeliste wird dann profitfaktor und prozdrawdown
		// berechnet

		System.out.println("gewinnsumme=" + gewinnsumme + "#trades="
				+ postrades);

		// erst marker setzen
		os.setMarker(marker);
		Tradeliste tradeliste_opt = tradeliste.getOptimizedTradelist(os);

		os.setGewinnsumme(gewinnsumme);
		os.setProfitfaktor((float) tradeliste_opt.calcProfitfactor());
		os.setDrawdownproz((float) tradeliste_opt.calcDrawdownProz());
		os.setAnzSelectTrades(postrades);
		os.setProfitfaktoranzahl((float) tradeliste_opt.calcProfitfactor()
				* postrades);
		os.setAnzGesTrades(maxpos);

		return os;

	}

	public OptimizeResult workIndiOracle(Tradeliste tradeliste)
	{
		// das orakel nimm nur die gewinnbringenden Trades

		int maxpos = tradeliste.getsize();
		JProgressWin jp = new JProgressWin("optimize oracle", 0, maxpos - 1);
		// anzahl der marktierten positiven trades
		int postrades = 0;
		OptimizeResult os = new OptimizeResult();

		int[] marker = new int[maxpos];

		// die gewinnsumme berechnen
		double gewinnsumme = 0;
		for (int pos = 0; pos < maxpos; pos++)
		{
			jp.update(pos);
			// das gewinnelement wird gezählt
			Trade trade = tradeliste.getelem(pos);

			if (trade.getProfit() > 0)
			{
				marker[pos] = 1;
				gewinnsumme = gewinnsumme + trade.getProfit();
				postrades++;
			}
		}
		System.out.println("gewinnsumme=" + gewinnsumme + "#trades="
				+ postrades);
		os.setGewinnsumme(gewinnsumme);
		os.setAnzSelectTrades(postrades);
		os.setAnzGesTrades(maxpos);
		os.setMarker(marker);
		jp.end();
		return os;
	}

	public OptimizeResult workIndiHour(Tradeliste tradeliste,
			Timefilter timefilter)
	{
		// dies ist der Indikator der nur die Orginal-Strategie anzeigt
		int maxpos = tradeliste.getsize();
		JProgressWin jp = new JProgressWin("optimize hour/weekday", 0,
				maxpos - 1);
		// anzahl der marktierten positiven trades
		int postrades = 0;
		OptimizeResult os = new OptimizeResult();

		int[] marker = new int[maxpos];

		// die gewinnsumme berechnen
		double gewinnsumme = 0;
		for (int pos = 0; pos < maxpos; pos++)
		{
			jp.update(pos);
			// jedes gewinnelement wird gezählt
			Trade trade = tradeliste.getelem(pos);

			// prüfe den den Timefilter
			if (timefilter != null)
				if (trade.checkTimefilter(timefilter) == false)
					marker[pos] = 0;
				else
				{

					marker[pos] = 1;
					gewinnsumme = gewinnsumme + trade.getProfit();
					postrades++;
				}
		}

		System.out.println("gewinnsumme=" + gewinnsumme + "#trades="
				+ postrades);
		os.setGewinnsumme(gewinnsumme);
		os.setAnzSelectTrades(postrades);
		os.setAnzGesTrades(maxpos);
		os.setMarker(marker);
		jp.end();
		return os;
	}

	private void markWinner(Tradeliste tradeliste, int[] marker, int maxpos,
			int indiindex1, int indiindex2, String mode)
	{
		//gehe durch die einzelnen trades
		//tradeliste= die tradeliste beinhaltet die einzelnen trades
		//indiindex1= der indikator für die buy-seite(sell)
		//indiindex2= der indikator für die sellseite(buy)
		//marker[i]= marker array, merkt sich die markierten bars
		//indiindex1=gibt mir den index des indikators der buy-seite an
		//indiindex2=gibt mir den index des indikators der sell-seite an
		//maxpos=so viele indikatorpositionen gibt es (durch 2teilen da long und shortseite jeweils einen indikator bilden)
		
		if (mode.contains("buy/sell"))
		{
			//d.h.index1=buy, index2=sell
			for (int pos = 0; pos < maxpos; pos++)
			{
				Trade tr = tradeliste.getelem(pos);
				int direction = tr.getDirection();
				String tradedat = tr.getOpentime();

				if ((direction != 0) && (direction != 1))
					Mbox.Infobox("internal error only direction 0 and 1 allowed dir="
							+ direction);

				int ismarked = 0;
				if (direction == 0)// buy-trade
					ismarked = indidat_glob.isMarked(tr,tradedat, indiindex1);
				if (direction == 1)// sell-trade
					ismarked = indidat_glob.isMarked(tr,tradedat, indiindex2);
				
				marker[pos] = ismarked;
			}
		} else if (mode.contains("sell/buy"))
		{
			//d.h.index1=sell, index2=buy
			for (int pos = 0; pos < maxpos; pos++)
			{
				Trade tr = tradeliste.getelem(pos);
				int direction = tr.getDirection();
				String tradedat = tr.getOpentime();

				if ((direction != 0) && (direction != 1))
					Mbox.Infobox("internal error only direction 0 and 1 allowed dir="
							+ direction);

				int ismarked = 0;
				if (direction == 0)// buy-trade
					ismarked = indidat_glob.isMarked(tr,tradedat, indiindex2);
				if (direction == 1)// sell-trade
					ismarked = indidat_glob.isMarked(tr,tradedat, indiindex1);
				
				marker[pos] = ismarked;
			}
		} else
			Tracer.WriteTrace(10, "error unknown indimode");
	}

	
}
