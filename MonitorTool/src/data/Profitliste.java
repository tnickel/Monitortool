package data;

import hiflsklasse.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class Profitliste
{
	// Diese liste beinhaltet die liste der Profite
	ArrayList<Profit> profitliste = new ArrayList<Profit>();
	private static int isloaded = 0;
	private static String profitlistenam = Rootpath.rootpath
			+ "\\data\\profitliste.xml";
	private Tradeliste tradeliste_glob = new Tradeliste(Rootpath.rootpath+ "\\data\\tradeliste.xml");

	public int getsize()
	{
		return profitliste.size();
	}

	public Profit getelem(int index)
	{
		return (profitliste.get(index));
	}

	public void delelem(int index)
	{
		profitliste.remove(index);
	}

	public void init()
	{
		profitliste.clear();

	}

	public void addelem(Trade trade)
	{
		// ein Trade wird in der gesammtprofitliste gespeichert

		int magic = trade.getMagic();
		String aktdatetime = Tools.get_aktdatetime_str();
		String tradebroker = trade.getBroker();
		String comment = trade.getComment();
		int plsize = this.getsize();
		int importedcolor = trade.getImportedcolor();

		// falls liste leer
		if (this.checkelem(magic, tradebroker) == false)
		{
			Profit p = new Profit(magic + "#0#0#0#0#0#0#0#" + tradebroker
					+ "#0#0#" + comment + "#0#0" + "#" + trade.getSymbol()
					+ "#0#0", trade);
			profitliste.add(p);
		}

		//falls die liste schon existiert
		plsize = this.getsize();
		for (int i = 0; i < plsize; i++)
		{
			Profit p = profitliste.get(i);
			if ((p.getMagic() == magic) && (p.getBroker().equals(tradebroker)))
			{
				// magic mit passenden broker gefunden
				String closetime = trade.getClosetime();

				//den trade aufnehmen
				p.addTrade(trade);
				
				if (importedcolor != 0)
					p.setImportColor(importedcolor);

				// gesamttrades erhöhen
				int gestrades = p.getGestrades();
				p.setGestrades(gestrades + 1);
				float gesgewinn = p.getGesgewinn();
				p.setGesgewinn(gesgewinn + (float) trade.getProfit());

				p.setBroker(trade.getBroker());

				// ermittle das alter in Tagen des Closetrades
				int gestage = p.getGestage();
				int tagediff = Tools.getDateInt(aktdatetime, closetime);
				// setze neues maximales alter
				if (tagediff > gestage)
					p.setGestage(tagediff);

				// Ist closetrade maximal 30 Tage alt
				if (Tools.zeitdifferenz_tage(aktdatetime, closetime) < 30)
				{
					int trades30 = p.getAnzmonatstrades();
					float gewinn30 = p.getDreizigtragegewinn();
					p.setAnzmonatstrades(trades30 + 1);
					p.setDreizigtragegewinn((float) gewinn30
							+ (float) trade.getProfit());
				}
				// ist closetrade maximal 7 Tage alt
				if (Tools.zeitdifferenz_tage(aktdatetime, closetime) < 7)
				{
					int trades7 = p.getAnztradeslastzehn();
					float gewinn7 = p.getZehntagegewinn();
					p.setAnztradeslastzehn(trades7 + 1);
					p.setZehntagegewinn((float) gewinn7
							+ (float) trade.getProfit());
				}

			}

		}

	}

	private boolean checkelem(int magic, String broker)
	// prüft ob das element schon in der Profitliste ist
	{
		int plsize = this.getsize();

		for (int i = 0; i < plsize; i++)
		{
			Profit p = profitliste.get(i);
			if ((p.getMagic() == magic) && (p.getBroker().equals(broker)))
				return true;
		}
		return false;
	}

	public void sortliste()
	{
		Collections.sort(profitliste);
	}

	public void Reverse()
	{
		Collections.reverse(profitliste);
	}

	public void calcDrawdowns()
	{
		// die profitliste wird bei der profitanzeige einmal aufgebaut
		// Dies wird in <CalcProfitTable> gemacht.
		// nachdem die Liste aufgebaut ist muss noch der Drawdown berechnet
		// werden.
		// die profitliste besitzt hierzu für jeden profit die zugehörige
		// Tradeliste
		
		System.out.println("calcdd start");
		int plsize = this.getsize();

		for (int i = 0; i < plsize; i++)
		{
			Profit p = profitliste.get(i);
			p.calcDrawdown();
			
		}
		System.out.println("calcdd end");
	}

	public void calcProfitfaktoren()
	{
		// die profitliste wird bei der profitanzeige einmal aufgebaut
		// Dies wird in <CalcProfitTable> gemacht.
		// nachdem die Liste aufgebaut ist muss noch der Drawdown berechnet
		// werden.
		// die profitliste besitzt hierzu für jeden profit die zugehörige
		// Tradeliste
		int plsize = this.getsize();
		System.out.println("calcpf start");
		for (int i = 0; i < plsize; i++)
		{
			Profit p = profitliste.get(i);
			p.calcProfitfaktor();
		}
		System.out.println("calcpf end");
	}

}
