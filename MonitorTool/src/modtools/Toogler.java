package modtools;

import StartFrame.Brokerview;
import data.Ealiste;
import data.Metaconfig;
import data.Profitliste;
import data.Tradeliste;
import gui.Mbox;
import hiflsklasse.Tracer;

public class Toogler
{
	public void ToggleOnOffEa(Tradeliste tl,Brokerview brokerview, Ealiste eal, int magic,String comment, String selbroker,Profitliste pl)
	{
		//The ea will be switched on/off for the tradecopier
		// selbroker = selektierter broker		// der selektierte broker ist ein Realbroker, we can´t switch realbrokers

		int atype=	brokerview.getAccounttype(selbroker);
		
		if (atype == 2)
		{
			Mbox.Infobox(
					"Only on demoaccounts you can swith on/off eas magic<" + magic + "> broker<" + selbroker + ">");
			return;
		}
		//nur beim demobroker kann der EA ein/aus geschaltet werden
		if ((atype==1)||(atype==4))
		{
			//type1= demobroker, type4=autocreator broker
			
			// Broker ist ein demobroker, dann hole den realbroker
			Metaconfig mc = brokerview.getMetaconfigByBrokername(selbroker);
			// den broker gibt es nicht mehr
			if (mc == null)
				Tracer.WriteTrace(10, "E:unknown broker<" + selbroker + ">");
			
			//check
			String realbroker = mc.getconnectedBroker();
			if((realbroker==null)||(realbroker.length()==0)||(realbroker.contains("select realaccount")))
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
				if(atype==1)mc.SetCloseAllTradesOnce(magic);
				else if (atype==4)
					mc.SetCloseAllTradesOnceAC(comment);
				else
					Tracer.WriteTrace(10, "Error: wrong accountype="+atype);
			}
			else
			{
				//einschalten
				eal.setOn(magic, selbroker, 1);
				
				//remove
				//hier wird ein bestehender RemoveCloseAllTrades für eine Magic wieder entfernt
				if(atype==1)mc.RemoveCloseAllTradesOnce(magic);
				else if (atype==4)
					mc.RemoveCloseAllTradesOnceAC(comment);
				else
					Tracer.WriteTrace(10, "Error: wrong accountype="+atype);
			}
			
			
			TradeCopy trc=new TradeCopy();
			//beim init werden die *.chr-files kopiert
			trc.init(selbroker,realbroker,brokerview);

			//trage die verbindungsdaten und die magics ein
			trc.configProfiles(brokerview,eal,pl);
			
			//konsistenzprüfung, hier wird geprüft ob jeder channel nur ein einziges Mal vorkommt
			//eine channelid darf beim realbroker nicht zweimal vorkommen. Es wird channel 1-255 überprüft
			if(trc.checkdoubleChannel(realbroker,brokerview,255)==false)
			{
				Tracer.WriteTrace(10, "E:error double channel for broker<"+realbroker+">--> stop");
				System.exit(99);
			}
			System.out.println("go on\n ");
	
		}
		
	}
	
}
