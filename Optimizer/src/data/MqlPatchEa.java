package data;

import gui.Mbox;
import hiflsklasse.Inf;

import java.io.File;

import modtools.Patcher;

public class MqlPatchEa extends Patcher
{
	Inf inf_glob = null;

	public void modifyAddFuntion(String action, String patchstring,
			String patcharray[], int timeframe)
	{
		// die funktion wird am ende des mqls eingefügt

		// patchstring ist z.B. 0045b
		// patcharray sind die Zeilen die einzufügen sind
		// action = sell oder buy
		// int timeframe=x

		patchTimeframe(timeframe, patcharray);

		// den ea mit den indikatoren string1 und string2 modifzieren
		int anz = zeilenspeicher.length;
		int endindex = 0;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile == null)
			{
				endindex = i;
				break;
			}
		}
		if (endindex == 0)
			Mbox.Infobox("Error end not found");

		// am ende den indikator einfügen
		int patchlen = patcharray.length;
		for (int k = 0, j = endindex; j < 20000; j++, k++)
		{
			// falls ende vom patcharray erreicht
			if (k == patchlen)
				break;

			zeilenspeicher[j] = patcharray[k];
		}
		String suchentrystring = "";
		// dann die eine rule einfügen

		// suchstring bestimmen
		if (action.equals("buy"))
			suchentrystring = "if(TradeLong)";
		else if (action.equals("sell"))
			suchentrystring = "if(TradeShort)";
		else
			Mbox.Infobox("internal error 55kdkdkfjf");

		// die bedingung am anfang in der if-condition einfügen
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile.contains(suchentrystring))
			{
				this.addnewline(i - 1, "if(Filter_" + patchstring + " ())");
				break;
			}
		}

	}

	private void patchTimeframe(int timeframe, String patcharray[])
	{
		// der timeframe wird gesetzt
		int anz = patcharray.length;
		for (int i = 0; i < anz; i++)
		{
			if(patcharray[i].contains("iRSI(NULL,0,"))
				patcharray[i]=patcharray[i].replace("iRSI(NULL,0,", "iRSI(NULL,"+timeframe+",");
			
			if(patcharray[i].contains("iHigh(NULL, 0,"))
				patcharray[i]=patcharray[i].replaceAll("iHigh(NULL, 0,", "iHigh(NULL,"+timeframe+",");
			
			if(patcharray[i].contains("iLow(NULL, 0,"))
				patcharray[i]=patcharray[i].replaceAll("iLow(NULL, 0,", "iLow(NULL,"+timeframe+",");
			
			if(patcharray[i].contains("iOpen(NULL, 0,"))
				patcharray[i]=patcharray[i].replaceAll("iOpen(NULL, 0,", "iOpen(NULL,"+timeframe+",");
			
			if(patcharray[i].contains("iClose(NULL, 0,"))
				patcharray[i]=patcharray[i].replaceAll("iClose(NULL, 0,", "iClose(NULL,"+timeframe+",");
			
			while(patcharray[i].contains("Period()"))
				patcharray[i]=patcharray[i].replace("Period()",""+timeframe);
		}
	}

	public void saveEa(String filename)
	{
		File fx= new File(filename);
		if(fx.exists())
			fx.delete();
		
		// abspeichern
		writeMemFile(filename);
	}
}
