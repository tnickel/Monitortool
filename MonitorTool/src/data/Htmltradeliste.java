package data;

import gui.Mbox;
import hiflsklasse.Tracer;

import java.util.ArrayList;
import java.util.Random;


public class Htmltradeliste
{
	// hier drin werden die html-trades gehalten
	private ArrayList<Htmltrade> htmltradeliste = new ArrayList<Htmltrade>();

	// <tr bgcolor="#E0E0E0" align=right><td>2</td><td class=msdate>2011.01.03
	// 16:05</td><td>buy</td><td>2</td><td class=mspt>0.50</td><td
	// style="mso-number-format:0\.00000;">1.33640</td><td
	// style="mso-number-format:0\.00000;" align=right>0.00000</td><td
	// style="mso-number-format:0\.00000;" align=right>0.00000</td><td
	// colspan=2></td></tr>

	public Htmltradeliste(StringBuffer mem)
	{
		// baut intern die Htmltradeliste auf
		// Die Arraylist beinhaltet die trades

		// den anfang schon mal wegwerfen
		mem = new StringBuffer(mem.substring(mem.indexOf("</table>")));

		int poscounter = 0;
		while (5 == 5)
		{
			Htmltrade tr = null;
			// hole die nächste order
			int pos1 = mem.indexOf("align=right><td>", poscounter);
			if (pos1 == -1)
				break;
			int pos2 = mem.indexOf("</td></tr>", poscounter);
			// zeile rausschneiden

			if ((pos1 > mem.length()) || (pos2 > mem.length()))
				break;
			String tradezeile = mem.substring(pos1, pos2);

			// plausicheck
			if (tradezeile.contains("<td class=msdate>") == false)
			{
				// keine tradezeile dann gehe weiter
				poscounter = pos2 + 1;
				continue;
			}
			// plausicheck
			if (tradezeile.contains("mso-number-format") == false)
			{
				// keine tradezeile dann gehe weiter
				poscounter = pos2 + 1;
				continue;
			}
			if((tradezeile.contains("sell stop")==true)||(tradezeile.contains("modify")==true)||(tradezeile.contains("delete")==true)||(tradezeile.contains("buy stop")==true))
			{
				//rausfiltern
				// keine tradezeile dann gehe weiter
				poscounter = pos2 + 1;
				continue;
			}
			
			
			tr = new Htmltrade(tradezeile);

			// trade nur aufnehmen wenn kein modify und kein limit und kein
			// delete
			// Achtung einzelne Ordernummern können duch buylimit und delete limits wieder wegfallen
			
			if ((tr.getRichtung().contains("modify") == false)
					&& (tr.getRichtung().contains("limit") == false)
					&& (tr.getRichtung().contains("delete") == false))
				htmltradeliste.add(tr);

			// gehe weiter
			poscounter = pos2 + 1;

			// System.out.println("pos1=" + pos1 + " len=" + mem.length());
		}
		
	}

	public void Htmltradeliste_old(String mem)
	{

		mem = mem.substring(mem.indexOf("</table>"));

		while (5 == 5)
		{
			Htmltrade tr = null;
			// hole die nächste order
			int pos1 = mem.indexOf("align=right><td>");
			if (pos1 == -1)
				break;
			int pos2 = mem.indexOf("</td></tr>");
			// zeile rausschneiden

			if ((pos1 > mem.length()) || (pos2 > mem.length()))
				break;
			String tradezeile = mem.substring(pos1, pos2);

			// plausicheck
			if (tradezeile.contains("<td class=msdate>") == false)
			{
				mem = mem.substring(pos2 + 6);
				continue;
			}
			// plausicheck
			if (tradezeile.contains("mso-number-format") == false)
			{
				mem = mem.substring(pos2 + 6);
				continue;
			}
			tr = new Htmltrade(tradezeile);

			// trade nur aufnehmen wenn kein modify
			if (tr.getRichtung().contains("modify") == false)
				htmltradeliste.add(tr);

			mem = mem.substring(pos2 + 6);

			System.out.println("pos1=" + pos1 + " len=" + mem.length());
		}
		
	}

	public ArrayList<Trade> generateTradeliste()
	{
		// baue aus der Htmltradeliste eine Tradeliste
		// die Liste wird nach den Auftragsnummern sortiert
		ArrayList<Trade> tradeliste = new ArrayList<Trade>();

		int anz = htmltradeliste.size();

		int groessteauftragsnummer = calcGroessteAuftragsnummer();

		// suche alle auftragsnummern
		for (int i = 1; i <= groessteauftragsnummer; i++)
		{

			Htmltrade ht1 = holeTradeAuftragsnummer(i);
			
			//falls auftragsnummer nicht da ist dann gehe weiter, es dürfen also lücken in den
			//auftragsnummern sein
			if(ht1==null)
				continue;
			
			int auftragsnummer1 = ht1.getAuftragsnummer();

			// Nimm das Päärchen mit Auftragsnummer i1 auf
			if (auftragsnummer1 > 0)
				addAuftragspaaerchen(auftragsnummer1, tradeliste);
		}
		return tradeliste;
	}

	private Htmltrade holeTradeAuftragsnummer(int auftragsnummer)
	{
		// holt den ersten Trade mit der auftragsnimmer
		int anz = htmltradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Htmltrade ht1 = htmltradeliste.get(i);
			if (ht1.getAuftragsnummer() == auftragsnummer)
				return ht1;
		}
	
		return null;
	}

	private int calcGroessteAuftragsnummer()
	{
		// errechnet in der htmlliste die grösste auftragsnummer
		int bignr = -99;
		int anz = htmltradeliste.size();
		for (int i = 0; i < anz; i++)
		{
			Htmltrade ht1 = htmltradeliste.get(i);
			if (ht1.getAuftragsnummer() > bignr)
				bignr = ht1.getAuftragsnummer();
		}
		return bignr;
	}

	private void addAuftragspaaerchen(int suchauftragsnummer,
			ArrayList<Trade> tradeliste)
	{
		// die beiden Auftragsnummern finden

		// zufallszahl für die Transaktionsnummer
		Random ran = new Random();

		int maxpos = htmltradeliste.size();
		int auftragsnummer1 = -99;
		int auftragsnummer2 = -99;
		Htmltrade ht1 = null;
		int i1 = 0;

		// die erste Auftragsnummer finden
		for (i1 = 0; i1 < maxpos; i1++)
		{
			ht1 = htmltradeliste.get(i1);
			auftragsnummer1 = ht1.getAuftragsnummer();
			if (suchauftragsnummer == auftragsnummer1)
				break;
		}
		if (suchauftragsnummer != auftragsnummer1)
			Mbox.Infobox("autragsnummer1 <" + suchauftragsnummer
					+ "> not found");

		// i+1 suche die gleiche nummer nochmal aber eine position weiter
		// die zweite gleiche Auftragsnummer finden
		for (int j = i1 + 1; j < maxpos; j++)
		{
			Htmltrade ht2 = htmltradeliste.get(j);
			auftragsnummer2 = ht2.getAuftragsnummer();

			if (auftragsnummer2 == suchauftragsnummer)
			{
				// die zweite auftragsnummer gefunden

				Trade tr = new Trade();
				tr.setAccountnumber(0);
				tr.setBroker("");
				tr.setLots(ht2.getLotsize());
				tr.setOpentime(ht1.getDate());
				tr.setOpenprice(ht1.getPrize());
				tr.setClosetime(ht2.getDate());
				tr.setCloseprice(ht2.getPrize());
				tr.setProfit(ht1.getGewinn() + ht2.getGewinn());
				tr.setTransactionnumber(ran.nextInt(10000000));

				if (ht1.getRichtung().contains("buy"))
					tr.setDirection(0);
				else if (ht1.getRichtung().contains("sell"))
					tr.setDirection(1);
				else
					tr.setDirection(-99);

				tradeliste.add(tr);

				// beide pärchen löschen
				ht1.setAuftragsnummer(-99);
				ht2.setAuftragsnummer(-99);

				return;
			}
		}

		Tracer.WriteTrace(10, "error: Auftragnumer <" + suchauftragsnummer
				+ "> nicht komplett2");
	}
}
