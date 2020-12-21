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

	String expertname_glob = null;
	
	public String getExpertname()
	{
		return expertname_glob;
	}

	public void setExpertname(String expertname)
	{
		this.expertname_glob = expertname;
	}

	public int getPeriod()
	{
		//expertname_glob=Q67 EURUSD M15 3.100.112.mq4
		
		String[] periodenzeichen =
		{ "M15", "H1", "M1", "M5", "M30", "H4", "D1" };
		Integer[] frame =
		{ 15, 60, 1, 5, 30, 240, 1440 };
		
		
		if(expertname_glob.endsWith(".mq4")==false)
		{
			Tracer.WriteTrace(10, "expertname should end with .mq4 but I got <"+expertname_glob+">");
			return 0;
		}
		
		String[] parts = expertname_glob.split(" ");
		int anzp=parts.length;
		if(anzp<3)
		{
			Tracer.WriteTrace(10, "wrong format expertname should have [Currencystring] [Timeframe] [Magic].mq4 \n for example 'Q67 EURUSD M15 3.100.112.mq4'");
		}
	
		String period_found=parts[anzp-2];
		
		if(period_found==null)
		{
			Tracer.WriteTrace(10, "problem with period in <"+expertname_glob+"> -> STOP");
			return 0;
		}
		
		int anz = periodenzeichen.length;
		for (int i = 0; i < anz; i++)
		{
			if (period_found.contains(periodenzeichen[i]))
				return frame[i];
		}
		
		Tracer.WriteTrace(10, "found no symbol in <"+expertname_glob+">");
		return 0;
	}

	public String getSymbol(Metaconfig meconfig)
	{
		//expertname_glob=Q67 EURUSD M15 3.100.112.mq4
		if(expertname_glob.endsWith(".mq4")==false)
		{
			Tracer.WriteTrace(10, "expertname should end with .mq4 but I got <"+expertname_glob+">");
			return null;
		}
		
		String[] parts = expertname_glob.split(" ");
		int anzp=parts.length;
		if(anzp<3)
		{
			Tracer.WriteTrace(10, "wrong format expertname should have [Currencystring] [Timeframe] [Magic].mq4 \n for example 'Q67 EURUSD M15 3.100.112.mq4'");
		}
	
		String currency=parts[anzp-3];
		
		if(currency!=null)
		{
			return currency;
		}
		
		Tracer.WriteTrace(10, "found no symbol in <"+expertname_glob+">");
		return null;
	}

	public boolean patchLotsize(Ea ea, Metaconfig meRealconf)
	{
		if(isSq4x==1)
			return(patchLotsizeSq4x( ea,  meRealconf));
		else
			return(patchLotsizeSq3(ea, meRealconf));
			
	}
	
	public boolean patchComment(Ea ea)
	{
		if(isSq4x==1)
			return(patchCommentSq4x(ea));
		else
			return(patchCommentSq3(ea));
		
		
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
		if (expertname_glob.contains("DAX"))
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
			addAbschaltAutomaticSq4x(kennung,expertname_glob);
		else
			addAbschaltAutomaticSq3(kennung,expertname_glob);
			
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
