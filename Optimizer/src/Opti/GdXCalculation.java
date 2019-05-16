package Opti;

import data.OptimizeResult;
import data.Trade;
import data.Tradeliste;


public class GdXCalculation
{
	public OptimizeResult calcGdxProfit(Tradeliste tradeliste, int periodx)
	{
		// periodx: die gd abschaltung läuft mit der periode x
		// für die Abschaltung mit periode x wird er Gewinn ermittelt
		// Es wird also festgestellt welche Trades in der Tradeliste für
		// die berechnung herangezogen werden dürfen
		// Es werden alle trades n gezählt wo marker(n-1) ==true ist
		// der Marker markiert die winner

		// diese liste(die liste der marker) markiert wann sumx>gdx
		OptimizeResult os=new OptimizeResult();
		
	
		
		int maxpos = tradeliste.getsize();
		//anzahl der markierten positiven trades
		int postrades=0;
		int[] marker = new int[maxpos];
		
		
		markWinner(tradeliste, periodx, maxpos, marker);
		
		
		//jetzt wird der Gewinn für die Periode x bestimmt
		//Es werden also alle trades n gezählt wo marker(n-1) ==true ist
		double gewinnsumme=0;
		for(int pos=1; pos<maxpos; pos++)
		{
			if(marker[pos-1]==1)
			{
				//das gewinnelement wird gezählt
				Trade trade=tradeliste.getelem(pos);
				//gewinnsumme=gewinnsumme+trade.getProfit();
				postrades++;
			}
		}
		
		//die gewinne werden ermittelt wo die positionen 1 sind
		for(int pos=1; pos<maxpos; pos++)
		{
			if(marker[pos]==1)
			{
				//das gewinnelement wird gezählt
				Trade trade=tradeliste.getelem(pos);
				gewinnsumme=gewinnsumme+trade.getProfit();
			}
		}
		
		//erst marker setzen
		os.setMarker(marker);
		//dann die optimerte tradeliste
		Tradeliste tradeliste_opt=tradeliste.getOptimizedTradelist(os);
		System.out.println("gewinnsumme="+gewinnsumme+ "periodx="+periodx+"#trades="+postrades);
		os.setGewinnsumme(gewinnsumme);
		os.setAnzSelectTrades(postrades);
		os.setAnzGesTrades(maxpos);
	
		os.setgdx(periodx);
		os.setProfitfaktor((float)tradeliste_opt.calcProfitfactor());
		os.setDrawdownproz((float)tradeliste_opt.calcDrawdownProz());
		//tradeliste.setgdx(periodx);
		return os;
	}

	private void markWinner(Tradeliste tradeliste, int periodx, int maxpos,
			int[] marker)
	{
		//hier werden die Winner markiert
		
		for (int pos = 2; pos < maxpos; pos++)
		{
			double sumx = tradeliste.get_tsumx(pos - 1);
			double gdx = tradeliste.calc_gdx(pos - 1, periodx);
			
			//überprüfen ob der Trade in dem Timefilter ist
			Trade trade=tradeliste.getelem(pos);
		
			
			// die winner markieren, winner sind teilsummengewinne die über dem GDX sind
			// immer dann wird true markiert
			if (sumx > gdx)
			// dann markiere als ok
				marker[pos]=1;
				
			else
				marker[pos]=0;
		}
	}
}
