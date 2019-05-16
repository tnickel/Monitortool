package modtools;

import gui.Mbox;
import hiflsklasse.Tracer;
import data.Ea;
import data.GlobalVar;
import data.Metaconfig;

public class MqlPatch extends MqlSqPatcher
{
	// klasse die das mql-file patched
	// zb. magicnumber setzten etc...

	String expertname = null;
	
	public String getExpertname()
	{
		return expertname;
	}

	public void setExpertname(String expertname)
	{
		this.expertname = expertname;
	}

	public int getPeriod()
	{
		String[] periodenzeichen =
		{ "M15", "H1", "M1", "M5", "M30", "H4", "D1" };
		Integer[] frame =
		{ 15, 60, 1, 5, 30, 240, 1440 };

		int anz = periodenzeichen.length;
		for (int i = 0; i < anz; i++)
		{
			if (expertname.contains(periodenzeichen[i]))
				return frame[i];
		}
		
		Mbox.Infobox("Unbekannte Periode");
		Tracer.WriteTrace(10, "Error:unbekannte periode");
		return 0;
	}

	public String getSymbol(Metaconfig meconfig)
	{
		String daxsymbol = meconfig.getDaxname();

		String[] symbol =
		{ "USDJPY", "EURUSD", "GBPUSD", "DAX", "USDCHF", "AUDUSD", "USDCAD",
				"EURGBP", "EURAUD", "EURCHF", "EURJPY", "GBPCHF", "AUDCAD",
				"EURCAD", "NZDUSD", "CADCHF", "GBPCAD", "CADJPY", "AUDNZD",
				"AUDCHF", "GOLD", "SILVER", "XAGUSD", "XAUUSD","AUDCHF","AUDJPY","AUDNZD","CADCHF","CHFJPY","EURNZD","GBPAUD",
				"GBPCAD","GBPCHF","GBPJPY","GBPNZD","NZDCAD","NZDCHF","NZDJPY"};

		int anz = symbol.length;

		for (int i = 0; i < anz; i++)
		{
			String suchsymbol = symbol[i];
			if (expertname.contains(suchsymbol))
			{
				// falls der dax configuriert ist wird für den Future das
				// brokerspzifische zeichen verwendet
				if (suchsymbol.equalsIgnoreCase("DAX"))
					return daxsymbol;

				return symbol[i];
			}
		}
		
		Tracer.WriteTrace(10, "Kein symbol gefunden");
		return null;
	}

	public boolean patchLotsize(Ea ea, Metaconfig meRealconf)
	{
		if(isSq4x==1)
			return(patchLotsizeSq4x( ea,  meRealconf));
		else
			return(patchLotsizeSq3(ea, meRealconf));
			
	}
	public boolean patchInit()
	{
		if(isSq4x==1)
			patchInitSq4x();
		else
			patchInitSq3();
			
		return true;
	}

	public boolean patchVariables()
	{
		if(isSq4x==1)
			addVariablesSq4();
		else
			addVariablesSq3();
			
		return true;
		
	}
	public boolean isDaxEA()
	{
		if (expertname.contains("DAX"))
			// if (zeilenspeicher[1].contains("DAX"))
			return true;
		else
			return false;

	}

	
	
	public void patchGBMeta600()
	{
		if(isSq4x==1)
			return;
		else
			patchGBMeta600Sq3();
			
	}

	public void addAbschaltAutomatic(String kennung)
	{
		if(isSq4x==1)
			addAbschaltAutomaticSq4x(kennung,expertname);
		else
			addAbschaltAutomaticSq3(kennung,expertname);
			
	}
	protected void addPostcode(String postfilename)
	{
		
			addPostcodePatch(postfilename);
		
		
	}
	

	public void patchMqlProtection()
	{
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("gi_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("gi_", "Gi_");
			if (zeilenspeicher[i].contains("li_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("li_", "Li_");
			if (zeilenspeicher[i].contains("gs_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("gs_", "Gs_");
			if (zeilenspeicher[i].contains("gd_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("gd_", "Gd_");
			if (zeilenspeicher[i].contains("lsa_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("lsa_", "Lsa_");
			if (zeilenspeicher[i].contains("g_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("g_", "G_");
			if (zeilenspeicher[i].contains("l_") == true)
				zeilenspeicher[i] = zeilenspeicher[i].replace("l_", "L_");

		}
		for (int i = 0; i < 100; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if (zeilenspeicher[i].contains("EX4-TO-MQ4 decompiler") == true)
				zeilenspeicher[i] = "//*****";
			if (zeilenspeicher[i].contains("purebeam") == true)
				zeilenspeicher[i] = "//*****";

		}
	}

	

	public void patchDaxEA()
	{
		//gelöscht
		
		
	}

	private boolean isTradeEobEA()
	{
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] != null)
				if (zeilenspeicher[i].contains("// skips the rest of ticks"))
					return true;
		}
		return false;
	}

	
	
	public void patchMyFxbookEa()
	{
		int foundflag = 0;

		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("<inputs>"))
			{
				foundflag = 1;

				addnewline(i + 1,
						"Myfxbook_Email=" + GlobalVar.getMyfxbookemail());
				addnewline(i + 2,
						"Myfxbook_Password=" + GlobalVar.getMyfxbookpassword());
				addnewline(
						i + 3,
						"Publish_Interval_Minutes="
								+ GlobalVar.getMyfxbookintervall());
				break;
			}
		}
		if (foundflag == 0)
			Mbox.Infobox("Error not found <inputs> in master *.cfg file");

		foundflag = 0;

		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("name=Historyexporter"))
			{
				zeilenspeicher[i] = "name=myfxbook";
				foundflag = 1;
				break;
			}
		}
		if (foundflag == 0)
			Mbox.Infobox("Error not found 'name=Historyexporter' in master *.cfg file");

	}
	
	public void patchSleeptimemod()
	{
		if(isSq4x==1)
			return;
		else
			patchSleeptimemodSQ3();
		
	}
}
