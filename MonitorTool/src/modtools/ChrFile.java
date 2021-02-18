package modtools;

import java.util.ArrayList;

import data.Ealiste;
import gui.Mbox;

public class ChrFile extends Patcher
{
	// klasse die die passende metatraderconfig bearbeitet

	public void setSymbol(String symbol)
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile.contains("symbol=") == true)
			{
				// falls noch nix gesetzt ist
				if (zeilenspeicher[i].endsWith(symbol) == false)
					zeilenspeicher[i] = "symbol=" + symbol;

				return;
			}
		}
	}

	public void setPeriod(int period)
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile.contains("period=1") == true)
			{
				zeilenspeicher[i] = "period=" + period;
				return;
			}
		}
	}

	public void setExpertname(String expertname)
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile0 = zeilenspeicher[i];
			String zeile1 = zeilenspeicher[i + 1];
			if (((zeile0.contains("<expert>") == true) && (zeile1
					.contains("name=") == true)))
			{
				zeilenspeicher[i + 1] = "name=" + expertname;

				return;
			}
		}
	}

	public String getOrginalSymbol()
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz-1; i++)
		{
			String zeile0 = zeilenspeicher[i];
			
			if((zeile0!=null)&&(zeile0.length()>10)&&(zeile0.contains("description=") == true))
			{   
				//description=Q67 GBPUSD H1 4.62.126
				String part=zeile0.substring(zeile0.indexOf("description=")+12);
				String[] parts = zeile0.split(" ");
	
				if(parts.length<3)
					return null;
				
				int pos=parts.length-3;
				
				return(parts[pos]);
				
			}
		}
		//symbol not found
		return null;
		
	}
	
	
	public boolean setAttribute(String attribvorher, String attribnachher)
	{
		System.out.println("attribvorh<" + attribvorher + "> nachher<"
				+ attribnachher + ">");
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz - 1; i++)
		{
			if (zeilenspeicher[i] == null)
				break;
			if (zeilenspeicher[i].contains(attribvorher))
			{
				zeilenspeicher[i] = attribnachher;
				return (true);
			}
		}
		Mbox.Infobox("attrib <" + attribvorher + "> not found");
		return (false);
	}

	public boolean hatMagic(int magic)
	{
		// prüft ob die magic im *.cfg file ist
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz - 1; i++)
		{
			if (zeilenspeicher[i] == null)
				break;
			// MagicNumber=11846
			if ((zeilenspeicher[i].contains("MagicNumber=" + magic))||
			    (zeilenspeicher[i].contains("Magic_Number=" + magic)))
			{	
				
				return (true);
			}
		}
		return false;
	}

	public void setYellowmessage(String expertname)
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile0 = zeilenspeicher[i];
			String zeile1 = zeilenspeicher[i + 1];
			if (((zeile0.contains("<indicator>") == true) && (zeile1
					.contains("name=") == true)))
			{
				addnewline(i + 2, "<object>");
				addnewline(i + 3, "type=23");
				addnewline(i + 4, "object_name=Label" + expertname);
				addnewline(i + 5, "period_flags=0");
				addnewline(i + 6, "create_time=1375773897");
				addnewline(i + 7, "description=" + expertname);
				addnewline(i + 8, "color=65535");
				addnewline(i + 9, "font=Arial");
				addnewline(i + 10, "fontsize=10");
				addnewline(i + 11, "angle=0");
				addnewline(i + 12, "background=0");
				addnewline(i + 13, "corner=0");
				addnewline(i + 14, "x_distance=500");
				addnewline(i + 15, "y_distance=2");
				addnewline(i + 16, "</object>");
				return;
			}

		}
		Mbox.Infobox("Can´t set yellowmessage ea=" + expertname);
	}
	
	public void patchTradecopyMagics(String broker,Ealiste eal)
	{
		String patchstring="";
		ArrayList<Integer> mlist=eal.getMagiclist(broker);
		for(int i=0; i<mlist.size(); i++)
		{
			int magic=mlist.get(i);
			if(eal.getOn(magic, broker)==1)
			  patchstring=patchstring+mlist.get(i)+",";
		}
		//patch magicnumbers
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile0 = zeilenspeicher[i];
			if (zeile0.contains("IncludeMagicNumbers=") == true) 
			{
				zeilenspeicher[i] = "IncludeMagicNumbers=" + patchstring;
				return;
			}
		}
	}
	public void patchReceiverMagic(int magic)
	{
		//patch magicnumber
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile0 = zeilenspeicher[i];
			if (zeile0.contains("MagicNumber=") == true) 
			{
				zeilenspeicher[i] = "MagicNumber=" + magic;
				return;
			}
		}
	}
	public void patchCurPair(String currencypair)
	{
		//patch magicnumber
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile0 = zeilenspeicher[i];
			if (zeile0.contains("symbol=") == true) 
			{
				zeilenspeicher[i] = "symbol=" + currencypair;
				return;
			}
		}
	}
	public void patchTradecopyChannel(String broker)
	{
		
		//patch channel
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile0 = zeilenspeicher[i];
			if (zeile0.contains("Channel=") == true) 
			{
				zeilenspeicher[i] = "Channel=" + broker;
				return;
			}
		}
		
		
	}
}
