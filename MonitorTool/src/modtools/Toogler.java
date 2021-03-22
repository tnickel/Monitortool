package modtools;

import StartFrame.Brokerview;
import data.Ealiste;
import data.Metaconfig;
import gui.Mbox;
import hiflsklasse.Tracer;

public class Toogler
{
	public void ToggleOnOffEa(Brokerview brokerview, Ealiste eal, int magic, String selbroker)
	{
		
		// selbroker = selektierter broker
		// der selektierte broker ist ein Realbroker
		if (brokerview.getAccounttype(selbroker) == 2)
		{
			Mbox.Infobox(
					"Only on demoaccounts you can swith on/off eas magic<" + magic + "> broker<" + selbroker + ">");
			return;
			
		}
		//nur beim demobroker kann der EA ein/aus geschaltet werden
		if (brokerview.getAccounttype(selbroker) == 1)
		{
			// Broker ist ein demobroker, dann hole den realbroker
			Metaconfig mc = brokerview.getMetaconfigByBrokername(selbroker);
			// den broker gibt es nicht mehr
			if (mc == null)
				Tracer.WriteTrace(10, "E:unknown broker<" + selbroker + ">");
			
			//check
			String realbroker = mc.getconnectedBroker();
			if((realbroker==null)||(realbroker.length()==0))
			{
				Tracer.WriteTrace(10, "E:This demobroker <"+selbroker+"> dont have a connected realbroker, please connect to realbroker with 'Edit Broker'");
				return;	
			}
			
			//hier wird getoogled
			int on=eal.getOn(magic, selbroker);
			if(on==1)
			{
				//ausschalten
				eal.setOn(magic, selbroker, 0);
				//und die offenen trades mit der bestimmten magic abschalten
				//aber nur einmal, der EA bleibt bestehen sonst
				mc.SetCloseAllTradesOnce(magic);
			}
			else
			{
				//einschalten
				eal.setOn(magic, selbroker, 1);
				
				//remove
				//hier wird ein bestehender RemoveCloseAllTrades für eine Magic wieder entfernt
				mc.RemoveCloseAllTradesOnce(magic);
			}
			
			
			TradeCopy trc=new TradeCopy();
			//beim init werden die *.chr-files kopiert
			trc.init(selbroker,realbroker,brokerview);

			//trage die verbindungsdaten und die magics ein
			trc.configProfiles(brokerview,eal);
		}
		
	}
}
