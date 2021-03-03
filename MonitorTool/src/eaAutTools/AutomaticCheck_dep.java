package eaAutTools;

import hiflsklasse.Swttool;
import hiflsklasse.Tracer;
import mtools.Mlist;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import StartFrame.Brokerview;
import StartFrame.Tableview;
import data.GlobalVar;
import data.Metaconfig;

public class AutomaticCheck_dep
{
	private static Brokerview brokerview_glob = null;
	private static Tableview tv_glob = null;
	private static Table table1_glob = null;

	private static Display dis_glob = null;
	private static Text anzincommingtrades_glob = null;
	private static Text anzeas_glob = null;
	

	public AutomaticCheck_dep(Tableview tabv, Brokerview bv, Table table1,
			 Display dis, Text anzincommingtrades,
			Text anzeas)
	{
		tv_glob = tabv;
		brokerview_glob = bv;
		table1_glob = table1;
		
		dis_glob = dis;
		anzincommingtrades_glob = anzincommingtrades;
		anzeas_glob = anzeas;

	}

	public static void runAutomatic_dep(int showflag)
	{
		// prüft bei allen ea´s ob der gd20 überschritten worden ist.
		// setze in der ea liste den wert.
		// 0=gd20 unterschritten
		// 1=gd20 einmal überschritten
		// 2=gd20 zweimal überschritten
		
		Display dis = dis_glob;
		Text anzincommingtrades = anzincommingtrades_glob;
		Text anzeas = anzeas_glob;
		Table table1 = table1_glob;
		Mlist.add("check gd20 0:START");
		int anz = brokerview_glob.getAnz();

		// erst mal alle trades reinladen
		for (int i = 0; i < anz; i++)
		{
			//gehe durch alle broker
			Metaconfig mc = brokerview_glob.getElem(i);
			if (showflag == 1)
				Mlist.add("check gd20 " + (i + 1) + ":" + mc.getBrokername(), 1);
			
			Tracer.WriteTrace(20, "Info: lade Broker <" + mc.getBrokername()
					+ ">");

			tv_glob.LoadTradeTable(mc, dis, 1,showflag);

			if (showflag == 1)
				Swttool.wupdate(dis_glob);
		}
		// dann die profittable berechnen(ohne tradefilter)
		tv_glob.CalcProfitTable(null,1);
		// dann gd20 für alles schauen
		Tradelogic_dep.workProfitliste_dep(tv_glob,brokerview_glob);

		if (showflag == 1)
		{
			tv_glob.showCounter(anzincommingtrades, anzeas);

			// dann anzeigen
			tv_glob.ShowTradeTable(dis, table1, null,GlobalVar.getShowMaxTradetablesize() ,1);
			tv_glob.ShowProfitTable();

			Mlist.add("check gd20 X:READY", 1);
		}
		tv_glob.storeEaliste();
	}



	
	

}
