package modtools;

import data.Ea;
import data.Metaconfig;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class MqlSqPatcher extends Patcher
{
	// zwischenklasse die zwischen 3.8.2 und 4.X trennt, hier sind also die
	// für den SQ spezifischen funktionen drin
	
	public MqlSqPatcher()
	{
		
	}
	
	public boolean patchLotsizeSq3(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "extern double Lots = ";
		double lotsize = meRealconf.getLotsize();
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(lotkeyword))
			{
				zeilenspeicher[i] = (lotkeyword + lotsize + ";");
				return true;
			}
		}
		
		Mbox.Infobox("Sq3attrib <" + lotkeyword + "> not found in file <" + ea.getEafilename() + ">");
		return false;
	}
	
	public boolean patchLotsizeSq4x(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "extern double mmLots = ";
		String lotkeyword2 = "extern double mmLotsIfNoMM = ";
		String lotkeyword3 = "input double mmLots = ";
		String lotreplace3 = "double mmLots = ";
		
		double lotsize = meRealconf.getLotsize();
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(lotkeyword))
			{
				zeilenspeicher[i] = (lotkeyword + lotsize + ";");
				return true;
			} else if (zeilenspeicher[i].contains(lotkeyword2))
			{
				Tracer.WriteTrace(10, "E: memory management not supported use Fixed size for lots");
				zeilenspeicher[i] = (lotkeyword2 + lotsize + ";");
				return true;
			} else if (zeilenspeicher[i].contains(lotkeyword3))
			{
				zeilenspeicher[i] = (lotreplace3 + lotsize + ";");
				return true;
			}
		}
		
		Mbox.Infobox("Sq4 attrib <" + lotkeyword + "> or <" + lotkeyword2 + ">not found in file <" + ea.getEafilename()
				+ ">");
		return false;
	}
	
	public boolean patchLotsizeExpertStudio(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "static input double Entry_Amount =";
		String lotkeywordrepl = "extern double Entry_Amount =";
		
		double lotsize = meRealconf.getLotsize();
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(lotkeyword))
			{
				zeilenspeicher[i] = (lotkeywordrepl + lotsize + "; //Entry lots");
				return true;
			}
			
		}
		
		Mbox.Infobox("EA Studio attrib <" + lotkeyword + "> or <" + lotkeyword + ">not found in file <"
				+ ea.getEafilename() + ">");
		return false;
	}
	
	public boolean patchBugsMt5()
	{
		// falls globale config gewünscht
		String lotkeyword = "input ENUM_ORDER_TYPE_FILLING preferredFillingType = ORDER_FILLING_FOK;";
		String lotkeywordrepl = "input ENUM_ORDER_TYPE_FILLING preferredFillingType = ORDER_FILLING_RETURN;";
		
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(lotkeyword))
			{
				zeilenspeicher[i] = lotkeywordrepl;
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean patchEodExitTimeMt5()
	{
		String keyword = "input string EODExitTime = ";
		
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(keyword))
			{
				String timestr = zeilenspeicher[i]
						.substring(zeilenspeicher[i].indexOf(keyword) + keyword.length());
				String newtime = modifyTime(timestr);
				zeilenspeicher[i] = "input string EODExitTime = " + "\"" + newtime + "\";";
				return true;
			}
			
		}
		
		return true;
	}
	
	public boolean patchEodExitTimeMt4()
	{
		//extern string EODExitTime = "15:00";
		String keyword = "extern string EODExitTime = ";
		
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(keyword))
			{
				String timestr = zeilenspeicher[i]
						.substring(zeilenspeicher[i].indexOf(keyword) + keyword.length());
				String newtime = modifyTime(timestr);
				zeilenspeicher[i] = "extern string EODExitTime = " + "\"" + newtime + "\";";
				return true;
			}
		}
	return true;
	}
	
	public boolean patchLotsizeExpertStudioPortfolio(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "static input double Entry_Amount";
		String lotkeywordrepl = "extern double Entry_Amount      =";
		
		double lotsize = meRealconf.getLotsize();
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if (zeilenspeicher[i].contains(lotkeyword))
			{
				zeilenspeicher[i] = (lotkeywordrepl + lotsize + "; //Entry lots");
				return true;
			}
			
		}
		
		Mbox.Infobox("Ea Studio Port attrib <" + lotkeyword + "> or <" + lotkeyword + ">not found in file <"
				+ ea.getEafilename() + ">");
		return false;
	}
	
	public boolean patchCommentSq4x(Ea ea,Metaconfig meconf)
	{
		String comment="";
		String eafilename = ea.getEafilename();
		eafilename = eafilename.replace(".mq4", "");
		// extern string CustomComment = "Q67_EURUSD_M15Strategy_4_35_155";
		String kw = "extern string CustomComment = ";
		String kw2 = "input string CustomComment =";
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			
			// Mt4
			if (zeilenspeicher[i].contains(kw))
			{
				String mem = zeilenspeicher[i];
				if(meconf.isCustomCommentFlag()==true)
					comment=meconf.getCustomCommentText();
				else
					comment = eafilename;
				
				comment = comment.replace("Strategy", "");
				
				zeilenspeicher[i] = "extern string CustomComment = " + "\"" + comment + "\";";
				return true;
			}
			// Mt5
			else if (zeilenspeicher[i].contains(kw2))
			{
				String mem = zeilenspeicher[i];
				if(meconf.isCustomCommentFlag()==true)
					comment=meconf.getCustomCommentText();
				else
					comment = eafilename;
				
				comment = comment.replace("Strategy", "");
				
				zeilenspeicher[i] = "input string CustomComment = " + "\"" + comment + "\";";
				return true;
			}
			
		}
		Mbox.Infobox("pcsq4 attrib <" + kw + "> not found in file <" + ea.getEafilename() + ">");
		return false;
		
	}
	
	public boolean patchCommentExpertStudio(Ea ea,Metaconfig meconf)
	{
		String comment="";
		String eafilename = ea.getEafilename();
		eafilename = eafilename.replace(".mq4", "");
		// extern string CustomComment = "Q67_EURUSD_M15Strategy_4_35_155";
		String kw = "string comment    = ";
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			
			if (zeilenspeicher[i].contains(kw))
			{
				String mem = zeilenspeicher[i];
				
				if(meconf.isCustomCommentFlag()==true)
					comment=meconf.getCustomCommentText();
				else
					comment = eafilename;
			
				comment = "\"" + comment.replace("Strategy", "") + "\";";
				
				zeilenspeicher[i] = "\t\tstring comment    = " + comment;
				return true;
			}
		}
		Mbox.Infobox("pcesPort attrib <" + kw + "> not found in file <" + ea.getEafilename() + ">");
		return false;
		
	}
	
	public boolean patchCommentFsbPortfolio(Ea ea,Metaconfig meconf)
	{
		// hier kann man leider keinen comment einbauen, da nur ein einzelner ea sehr
		// viele andere Eas beinhaltet
		// das comment wird im ea im array gesetzt
		return true;
	}
	
	public boolean patchCommentSq3(Ea ea,Metaconfig meconf)
	{
		return true;
	}
	
	protected void patchInitSq3(String mtype)
	{
		addIncludes(mtype);
		
		// falls der initpatch schon drin ist dann mache nix
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i].contains("MathSrand(TimeLocal()+(MagicNumber+4)*5);") == true))
				return;
		}
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if ((zeilenspeicher[i].contains("int init()")) && (zeilenspeicher[i].contains("{")))
			{
				// fall a) init sieht so aus "init () {"
				addInitpatch(i, "sq3");
				return;
			} else if ((zeilenspeicher[i].contains("int init()")) && (zeilenspeicher[i + 1].contains("{")))
			{
				// fall b) init sieht so aus "init ()
				// {" [klammer ist in nächster zeile]
				addInitpatch(i + 1, "sq3");
				return;
			}
		}
	}
	
	private void addInitpatch(int ind, String vers)
	{
		String mn = "";
		if (vers.contains("sq3") || vers.contains("sq4"))
			mn = "MagicNumber";
		else
			mn = "Magic_Number";
		
		// warte 0 bis 150 sekunden
		addnewline(ind + 1, "   MathSrand(TimeLocal()+(" + mn + "+4)*5);");
		addnewline(ind + 2, "   int rand_val=MathRandInt(1,20);");
		addnewline(ind + 3, "   Print(\"init wait \"+(rand_val)+\" secs\");");
		addnewline(ind + 4, "    Sleep(rand_val*1000);");
		addnewline(ind + 5, "//patch lotsize");
		addnewline(ind + 6, "if(lotconfig(IntegerToString(" + mn + ",0,0))==false)");
		addnewline(ind + 7, "Alert(\"Error cant ptch lotsize\");");
	}
	
	protected void patchInitSq4x(String mtype)
	{
		addIncludes(mtype);
		// falls der initpatch schon drin ist dann mache nix
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i].contains("MathSrand(TimeLocal()+(MagicNumber+4)*5);") == true))
				return;
		}
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if ((zeilenspeicher[i].contains("int init()")) && (zeilenspeicher[i].contains("{")))
			{
				// fall a) init sieht so aus "init () {"
				addInitpatch(i, "sq4");
				return;
			} else if ((zeilenspeicher[i].contains("int init()")) && (zeilenspeicher[i + 1].contains("{")))
			{
				// fall b) init sieht so aus "init ()
				// {" [klammer ist in nächster zeile]
				addInitpatch(i + 1, "sq4");
				return;
			} else if ((zeilenspeicher[i].contains("int OnInit()")) && (zeilenspeicher[i].contains("{")))
			{
				// fall c) init sieht so aus "int OnInit() {"
				// {" [klammer ist gleiche zeile]
				addInitpatch(i, "sq4");
				return;
			} else if ((zeilenspeicher[i].contains("int OnInit()")) && (zeilenspeicher[i + 1].contains("{")))
			{
				// fall c) init sieht so aus "int OnInit() {"
				// {" [klammer ist in folgezeile]
				addInitpatch(i + 1, "sq4");
				return;
			}
		}
		Tracer.WriteTrace(10, "E: patcher cant find init() in mq4-file--> stop");
		
	}
	
	protected void patchInitStudioBuilder(String mtype)
	{
		addIncludes(mtype);
		// falls der initpatch schon drin ist dann mache nix
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i].contains("MathSrand(TimeLocal()+(Magic_Number+4)*5);") == true))
				return;
		}
		
		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			
			if ((zeilenspeicher[i].contains("int OnInit()")) && (zeilenspeicher[i + 1].contains("{")))
			{
				// fall c) init sieht so aus "int OnInit() {"
				// {" [klammer ist in folgezeile]
				addInitpatch(i + 1, "studio");
				return;
			}
		}
		Tracer.WriteTrace(10, "E: patcher cant find init() in mq4-file--> stop");
		
	}
	
	protected void addIncludes(String mtype)
	{
		if ((checkKeyword("#include <monitorlib.mqh>") == false) && (mtype.toLowerCase().equals("mt5") == false))
		{
			addnewline(0, "#include <monitorlib.mqh>");
		}
		
		if ((checkKeyword("#include <monitorlib_mt5.mqh>") == false) && (mtype.toLowerCase().equals("mt5") == true))
		{
			addnewline(0, "#include <monitorlib_mt5.mqh>");
		}
		
		// 509er Metatrader version, in der 600+ Metatraderversion darf keine
		// stdlib mehr rein und auch nicht in mt5
		if ((checkKeyword("string ErrorDescription(int error_code)") == false)
				&& (mtype.toLowerCase().equals("mt5") == false))
		{
			if (checkKeyword("#include <stdlib.mqh>") == false)
			{
				addnewline(1, "#include <stdlib.mqh>");
			}
		}
		
	}
	
	protected void addAbschaltAutomaticSq3(String kennung, String expertname, String mtype)
	{
		addIncludes(mtype);
		
		// überprüft auf ###Tradecheck
		// falls das schlüsselwort vorkommt wird da die abschaltautomatik
		// eingebaut
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if (zeilenspeicher[i].toLowerCase().contains("###tradecheck") == true)
			{
				addnewline(i + 1, "  Lots=lotcheck(MagicNumber,Kennung,Lots);");
				addnewline(i + 2, " if( tradecheck(MagicNumber,Kennung)==0) return (0);");
				return; // gehe raus da die abschaltautomatik eingebaut ist
			}
		}
		
		// hier suche nach der start-funktion da kein Tradecheck eingebaut ist
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			
			if ((zeilenspeicher[i].contains("start()")) && (zeilenspeicher[i].contains(";") == false))
			{
				// Fall 1 : Klammer ist in der Startzeile
				if (zeilenspeicher[i].contains("{") == true)
				{
					
					addnewline(i + 1, "  Lots=lotcheck(MagicNumber,Kennung,Lots);");
					addnewline(i + 2, " if( tradecheck(MagicNumber,Kennung)==0) return (0);");
					return;
				} else
				{
					if ((zeilenspeicher[i + 1].contains("{") == true) && (zeilenspeicher[i + 1].length() < 10))
					// Fall 2: Klammer ist eine Zeile weiter
					{
						// Hier wird die Abschaltautomatik eingebaut
						addnewline(i + 2, " if( tradecheck(MagicNumber,Kennung)==0) return (0);");
						addnewline(i + 3, "  Lots=lotcheck(MagicNumber,Kennung,Lots);");
						return;
					}
					// fehler
					// da ist so eine Zeile drin
					// { bla bala...
					else
						Mbox.Infobox("Installation error startautomatic start() ea<" + expertname + ">");
				}
			}
		}
		
	}
	
	public void patchSleeptimemodSQ3()
	{
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if (zeilenspeicher[i].contains("bool sqIsBarOpen;") == true)
			{
				addnewline(i + 1, "////sleeptime for modulo modificacion");
				addnewline(i + 2, "int sleeptimemod =1000;");
				break;
			}
			if (i == 19999)
				Tracer.WriteTrace(10, "E:keyword <bool sqIsBarOpen;> not found");
		}
		
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i].contains(
					"if(openPrice == OrderOpenPrice() && stopLoss == OrderStopLoss() && profitTarget == OrderTakeProfit()"))
					&& (zeilenspeicher[i + 1].contains(" all values are the same"))
					&& (zeilenspeicher[i + 2].contains("return(true)")))
			{
				addnewline(i + 4, "sleeptimemod=(MagicNumber%10)*1000;");
				addnewline(i + 5, "Print(\"Sleeptime for mod=\"+sleeptimemod);");
				addnewline(i + 6, " Sleep(sleeptimemod);");
				break;
			}
			
		}
	}
	
	protected void addAbschaltAutomaticSq4x(String kennung, String expertname, String mtype)
	{
		
		// mt5
		if ((checkKeyword("#include <monitorlib_mt5.mqh>") == false) && (mtype.toLowerCase().equals("mt5") == true))
		{
			addnewline(0, "#include <monitorlib_mt5.mqh>");
			
		}
		
		// mt4
		if ((checkKeyword("#include <monitorlib.mqh>") == false) && (mtype.toLowerCase().equals("mt5") == false))
		{
			addnewline(0, "#include <monitorlib.mqh>");
		}
		
		// 509er Metatrader version, in der 600+ Metatraderversion darf keine
		// stdlib mehr rein und auch nicht in mt5
		if ((checkKeyword("string ErrorDescription(int error_code)") == false)
				&& (mtype.toLowerCase().equals("mt5") == false))
		{
			if (checkKeyword("#include <stdlib.mqh>") == false)
			{
				addnewline(1, "#include <stdlib.mqh>");
			}
		}
		
		// überprüft auf ###Tradecheck
		// falls das schlüsselwort vorkommt wird da die abschaltautomatik
		// eingebaut
		
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if (zeilenspeicher[i].toLowerCase().contains("###tradecheck") == true)
			{
				addnewline(i + 1, "  Lots=lotcheck(MagicNumber,Kennung,Lots);");
				addnewline(i + 2, " if( tradecheck(MagicNumber,Kennung)==0) return (0);");
				return; // gehe raus da die abschaltautomatik eingebaut ist
			}
		}
		
		// hier suche nach der start-funktion da kein Tradecheck eingebaut ist
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			
			if ((zeilenspeicher[i].contains("start()")) && (zeilenspeicher[i].contains(";") == false))
			{
				// Fall 1 : Klammer ist in der Startzeile
				if (zeilenspeicher[i].contains("{") == true)
				{
					
					addnewline(i + 1, "  Lots=lotcheck(MagicNumber,Kennung,Lots);");
					addnewline(i + 2, " if( tradecheck(MagicNumber,Kennung)==0) return (0);");
					return;
				} else
				{
					if ((zeilenspeicher[i + 1].contains("{") == true) && (zeilenspeicher[i + 1].length() < 10))
					// Fall 2: Klammer ist eine Zeile weiter
					{
						// Hier wird die Abschaltautomatik eingebaut
						addnewline(i + 2, " if( tradecheck(MagicNumber,Kennung)==0) return (0);");
						addnewline(i + 3, "  Lots=lotcheck(MagicNumber,Kennung,Lots);");
						return;
					}
					// fehler
					// da ist so eine Zeile drin
					// { bla bala...
					else
						Mbox.Infobox("Installation error startautomatic start() ea<" + expertname + ">");
				}
			}
		}
		
	}
	
	public void patchGBMeta600Sq3()
	{
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			
			// anpassung an den MT600+
			if ((zeilenspeicher[i].contains("int numberOfBars = 0;") == true)
					&& (zeilenspeicher[i + 1].contains("for(int i=i; i<expBarsPeriod") == true))
			{
				zeilenspeicher[i] = "   int numberOfBars = 0,i=0;";
				zeilenspeicher[i + 1] = zeilenspeicher[i + 1].replace("int", "");
			}
		}
	}
	
	protected void addPostcodePatch(String fname)
	{
		// das komplette fname wird am ende zum zeilenspeicher hinzugefügt
		int endpos = 0;
		
		if (isSq3Ea() == true)
			fname = fname + ".sq3";
		else if (isSq4Ea() == true)
			fname = fname + ".sq4";
		else if (isSq4x == 2)
			fname = fname + ".sea";
		else
			return;
		
		if (FileAccess.FileAvailable(fname) == false)
			Tracer.WriteTrace(10, "E:File missing <" + fname + "> --> stop");
		
		Inf inf = new Inf();
		inf.setFilename(fname);
		
		for (endpos = 19999; endpos > 0; endpos--)
		{
			if (zeilenspeicher[endpos] != null)
				break;
		}
		if (endpos == 0)
			Tracer.WriteTrace(10, "E:can´t find endpos on file <" + fname + ">");
		
		for (int i = 1; i < 1000; i++)
		{
			String str = inf.readZeile();
			if (str == null)
				break;
			zeilenspeicher[endpos + i] = new String(str);
		}
		inf.close();
		
	}
}