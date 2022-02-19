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
		
		Tracer.WriteTrace(10, "found no period in <"+expertname_glob+">");
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
		if(isSq4x==2)
			return(patchLotsizeExpertStudio(ea,meRealconf));
		else if(isSq4x==4)
			return(patchLotsizeExpertStudioPortfolio(ea,meRealconf));
		else if(isSq4x==1)
			return(patchLotsizeSq4x( ea,  meRealconf));
		else if(isSq4x==3) //in fsb we don´t patch lotsize
		{
			Tracer.WriteTrace(20, "I:This is an FSB ea we don´t patch lotsize"+ea.getEafilename());
			return true;
		}
		else
			return(patchLotsizeSq3(ea, meRealconf));
			
	}
	
	public boolean patchComment(Ea ea)
	{
		if(isSq4x==2)
			return(patchCommentExpertStudio(ea));
		else if(isSq4x==1)
			return(patchCommentSq4x(ea));
		else if(isSq4x==4)
			return(patchCommentFsbPortfolio(ea));
		else
			return(patchCommentSq3(ea));
		
		
	}
	public boolean patchInit()
	{
		//sq4 and Ea Studio same inits
		if(isSq4x==1)
			patchInitSq4x();
		else if(isSq4x==0)
			patchInitSq3();
		else if(isSq4x==2)
			patchInitStudioBuilder();
		return true;
	}

	public boolean patchVariables()
	{
		if(isSq4x==4)
			addVariablesExpertStudioPortfolio();
		else if(isSq4x==2)
			addVariablesExpertStudio();
		else if(isSq4x==1)
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
	public void delFsbPortfolioEa(int magic)
	{
		//check base magic
		String basemagic=String.valueOf(magic).substring(0,5);
		if(checkFsbPortfolioBaseMagicNumber(basemagic)==false)
			Tracer.WriteTrace(10, "E:2 can´t find base magic number<"+basemagic+"> in <"+expertname_glob+">");
		
		if(delFsbPortfolioEa(String.valueOf(magic).substring(5))==false)
			Tracer.WriteTrace(10, "E:3 can´t find magic subnumber<"+magic+"> in <"+expertname_glob+">");
		
	}
	private Boolean checkFsbPortfolioBaseMagicNumber(String basemagic)
	{
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("Base_Magic_Number"))
			{
				if(zeilenspeicher[i].contains(basemagic)==false)
					Tracer.WriteTrace(10, "E: can´t find base magic number<"+basemagic+"> in <"+expertname_glob+">");
				else
					return true;
			}
		}
		Tracer.WriteTrace(10, "E:2 can´t find base magic number<"+basemagic+"> in <"+expertname_glob+">");
		return false;
	}
	public Boolean delFsbPortfolioEa(String magicstr)
	{
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("GetEntrySignaL_"+magicstr))
			{
				zeilenspeicher[i]="//signalList[i++] = GetEntrySignaL_"+magicstr+"();";
				return true;
			}
		}
		
		return false;
	}
	public String getFsbPortfolioEaTpSl(String magicstr)
	{
		//Signal signal = CreateEntrySignal(9, ind0long && ind1long, ind0short && ind1short, 14, 12, false);
		//Signal signal = CreateEntrySignal(11, ind0long, ind0short, 40, 20, false);
		int magic_subnr=Integer.valueOf(magicstr);
		
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("Signal signal = CreateEntrySignal("+magic_subnr))
			{
				String[] parts = zeilenspeicher[i].split(",");
				int anz=parts.length;
				String part1=parts[anz-2].replace(" ", "");
				String part2=parts[anz-3].replace(" ", "");;
				return ("tp="+part1+" sl="+part2);
			}
		}
		
		return null;
	}
	public double getFsbPortfolioLotsize()
	{
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains("double Entry_Amount"))
			{
				String lotstring=zeilenspeicher[i].substring(zeilenspeicher[i].indexOf("=")+1);
				lotstring=lotstring.replace(";", "");
				return Double.valueOf(lotstring);
			}
		}
		return 0;
	}
}
