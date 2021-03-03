package eaAutTools;

import gui.Mbox;
import hiflsklasse.Inf;
import hiflsklasse.Tools;

import java.util.Date;

import modtools.Networker_dep;
import mtools.Mlist;
import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.Ea;
import data.Ealiste;
import data.GlobalVar;
import data.Metaconfig;
import data.Profit;
import data.Rootpath;
import data.Tradeliste;
import datefunkt.Mondate;


public class Tradelogic_dep
{
	static private Brokerview brokerview_glob = null;
	private static Networker_dep networker = new Networker_dep();
	static private int swOnCounter = 0;
	static private int swOffCounter = 0;

	static public void workProfitliste_dep(Tableview tv_glob, Brokerview brokerview)
	{
		brokerview_glob = brokerview;
		// 3 Dinge werden hier erledigt
		// Step a)LATE lateswitchon (wenn ea.auto=2 und gd unterschritten dann
		// automatik einschalten)
		// Step b)MARK hier wird markiert (ob gd20 überschritten wurde)
		// Step c)SWITCH EA- gemäss seiner Automatik Ein/Ausgeschaltet

		// work heisst markieren und Ein/Ausschalten wenn die Automatik
		// aktiviert ist

		swOnCounter = 0;
		swOffCounter = 0;
		int i = 0;
		// gehe durch die profitliste

		Profit prof = null;
		while ((prof = tv_glob.getprofitelem(i)) != null)
		{
			i++;
			// für jeden profit wird die tradeliste aufgebaut
			Tradeliste eatradeliste = tv_glob.buildTradeliste(String.valueOf(prof.getMagic()),
					prof.getBroker());

			// dies ist der broker vom trade
			String demobrokername = prof.getBroker();

			// jetzt wird der realbroker gesucht
			String realbrokername = brokerview.getConBroker(demobrokername);

			if (realbrokername == null)
				continue;

			// jetzt wird der realbrokershare ermittelt
			Metaconfig meconf = brokerview
					.getMetaconfigByBrokername(realbrokername);
			if (meconf == null)
				continue;

			String realbrokerroot = meconf.getMqldata();
			if (realbrokerroot == null)
				continue;

			int pos = eatradeliste.getsize();

			Ealiste eal = tv_glob.getEaliste();
			Ea ea = eal.getEa(prof.getMagic(), prof.getBroker());

			//step a) late switchon
			//falls lateswitchon und equity unter gd20, dann wird die automatik eigeschaltet
			if((ea.getInst()==1)&&(ea.getLateswitchonflag()==1) && (ea.getGd20flag()==0))
				ea.setAuto(1);
			
			//step c) tradelogik
			// default tradelogik
			if (ea.getTradelogiktype() == 0)
				tradelogicDefault_dep(prof, eatradeliste, pos, eal, ea,
						realbrokerroot);
			// gdx tradelogik
			else if (ea.getTradelogiktype() == 1)
				tradelogicGdX_dep(prof, eatradeliste, pos, eal, ea, realbrokerroot);
			// line tradelogik
			else if (ea.getTradelogiktype() == 2)
				tradelogicLine_dep(prof, eatradeliste, pos, eal, ea, realbrokerroot);
			else
				Mbox.Infobox("tradelogik unknown");
		}
		Mlist.add("I: #switched on=" + swOnCounter + " #switched off="
				+ swOffCounter);
	}

	private static void tradelogicLine_dep(Profit prof, Tradeliste eatradeliste,
			int pos, Ealiste eal, Ea ex, String borkerroot)
	{
		int onval = ex.getSwitchonval();
		int offval = ex.getSwitchoffval();

		double sumx = eatradeliste.get_tsumx(pos - 1);

		
		//  anz der letzten Gewinnertrades muss > als ein minimum sein
		if (sumx > onval)
		{
			if (workWinner_dep(prof, eal, ex, sumx, 0, borkerroot) == 100)
				swOnCounter++;
		} else if (sumx < offval)
		// looser
		{
			if (workLooser_dep(prof, eal, ex, sumx, 0, borkerroot) == 100)
				swOffCounter++;
		}
	}

	private static void tradelogicGdX_dep(Profit prof, Tradeliste eatradeliste,
			int pos, Ealiste eal, Ea ex, String brokerroot)
	{
		int gdXperiod = ex.getGdx();
		double sumx = eatradeliste.get_tsumx(pos - 1);
		double gdX = eatradeliste.calc_gdx(pos - 1, gdXperiod);

		// falls aktueller gewinn >gd5 (winner)
		// und die anz der letzten Gewinnertrades muss > als ein minimum sein
		if (sumx > gdX)
		{
			if (workWinner_dep(prof, eal, ex, sumx, gdX, brokerroot) == 100)
				swOnCounter++;
		} else
		// looser
		{
			if (workLooser_dep(prof, eal, ex, sumx, gdX, brokerroot) == 100)
				swOffCounter++;
		}
	}

	private static void tradelogicDefault_dep(Profit prof, Tradeliste eatradeliste,
			int pos, Ealiste eal, Ea ex, String brokerroot)
	{
		int gd5period = GlobalVar.getDefaultGd();
		double sumx = eatradeliste.get_tsumx(pos - 1);
		double gd5 = eatradeliste.calc_gdx(pos - 1, gd5period);

		// falls aktueller gewinn >gd5 (winner)
		// und die anz der letzten Gewinnertrades muss > als ein minimum sein
		if (sumx > gd5)
		{
			int errcode = workWinner_dep(prof, eal, ex, sumx, gd5, brokerroot);
			if (errcode == 100)
				swOnCounter++;
			else if (errcode != 0)
				Mlist.add("error switch ON magic<" + prof.getMagic()
						+ "> broker<" + prof.getBroker() + ">");
		} else
		// looser
		{
			int errcode = workLooser_dep(prof, eal, ex, sumx, gd5, brokerroot);
			if (errcode == 100)
				swOffCounter++;
			else if (errcode != 0)
				Mlist.add("error switch OFF magic<" + prof.getMagic()
						+ "> broker<" + prof.getBroker() + ">");
		}
	}

	private static int workLooser_dep(Profit prof, Ealiste eal, Ea ex, double sum,
			double gd5, String brokerroot)
	{
		// return !=100 ==> fehler
		// return 0, ok aber nicht markiert
		// return 100 , und markiert

		// profit black, da loosing modus
		ex.setGd20flag(0);

		// sumx war <gd5 und wird jetzt ausgeschaltet
		// falls installiert und automatik = 1 && DEMOBROKER
		if ((ex.checkOn(brokerroot) == 1) && (ex.getInst() == 1)
				&& (ex.getAuto() == 1) && (ex.getInstFrom() == null))
		{
			String demobroker = prof.getBroker();
			logAction_dep(prof.getMagic(), demobroker,
					"switched off on Realaccount", "by automatic", sum, gd5);

			String realbroker = brokerview_glob.getConBroker(demobroker);

			if (realbroker == null)
			{
				Mlist.add("demobroker <" + demobroker
						+ ">hat noch keinen realbroker", 1);
				return 5;
			}

			String message = prof.getMagic() + " " + demobroker
					+ "*** switched Off ***";
			if (networker.RealBroSwitchOffEa(brokerview_glob, prof.getMagic(),
					realbroker) != 0)
				return 5;

			System.out.println(message);
			Mlist.add(message);
			// setze off auf demobroker
			ex.setOn(0);
			// setze off auf realbroker
			Ea exreal = eal.getEa(prof.getMagic(), realbroker);

			// Zeit holen und speichern
			Date dt = new Date();
			exreal.setLastofftime(dt);

			if (exreal != null)
				exreal.setOn(0);

			return 100;// ok und marked
		}
		return 0;// ok
	}

	private static int workWinner_dep(Profit prof, Ealiste eal, Ea ex, double sum,
			double gd5, String brokerroot)
	{
		// return !=100 ==> fehler
		// return 0, ok aber nicht markiert
		// return 100 , und markiert

		if (ex.getInstFrom() == null)
			// demobroker=green
			ex.setGd20flag(1);
		else
			// realbroker = blue
			ex.setGd20flag(2);

		int minutes = ex.getWaitAfterOffTime();

		// falls nach einer ausschaltung gewartet werden soll
		if (minutes > 0)
		{
			// hole zeit des letzten ausschaltens
			Date lastofftime = ex.getLastofftime();
			if (lastofftime != null)
			{
				long sekundenvergangen = Mondate.SekundenVergangen(lastofftime);

				// falls noch nicht genug zeit vergangen
				if ((sekundenvergangen * 60) < minutes)
				{
					Mlist.add("Trade to early minutes<" + sekundenvergangen
							* 60 + "> minimumwait<" + minutes + ">", 1);
					return 0; // ok
				}
			}
		}

		// falls installiert und automatik = 1 und DEMOBROKER und
		// switchonlyoff=false
		if (ex.getSwitchoffval() == 0)
			if ((ex.checkOn(brokerroot) == 0) && (ex.getInst() == 1)
					&& (ex.getAuto() == 1) && (ex.getInstFrom() == null))
			{
				String demobroker = prof.getBroker();
				String realbroker = brokerview_glob.getConBroker(demobroker);
				logAction_dep(prof.getMagic(), prof.getBroker(),
						"*** switched on Realaccount***", "by automatic", sum,
						gd5);

				if (realbroker == null)
				{
					Mlist.add("demobroker <" + demobroker
							+ ">hat noch keinen realbroker", 1);
					return 5;// fehler
				}
				String message = prof.getMagic() + " " + demobroker
						+ "*** switched on ***";
				if (networker.RealBroSwitchOnEa(brokerview_glob,
						prof.getMagic(), realbroker) != 0)
					return 5;// fehler

				System.out.println(message);
				Mlist.add(message, 1);
				// setze on auf demobroker
				ex.setOn(1);
				// setze on auf realborker
				Ea exreal = eal.getEa(prof.getMagic(), realbroker);
				if (exreal != null)
					exreal.setOn(1);
				return 100; // ok und marked
			}
		return 0;
	}

	static private void logAction_dep(int magic, String broker, String action,
			String who, double sum, double gd5)
	{
		Inf inf = new Inf();
		String fnam = Rootpath.getRootpath() + "\\log\\" + broker + " " + magic
				+ ".txt";
		inf.appendzeile(fnam, Tools.get_aktdatetime_str() + ": EA" + action,
				true);
		inf.close();

		inf = new Inf();
		fnam = Rootpath.getRootpath() + "\\EaOnOffLog.txt";
		inf.appendzeile(fnam, Tools.get_aktdatetime_str() + ": Broker<"
				+ broker + "> Magic<" + magic + "> " + action + "sum<" + sum
				+ "> gd5<" + gd5 + "> by <" + who + ">", true);

		inf.close();
	}
}
