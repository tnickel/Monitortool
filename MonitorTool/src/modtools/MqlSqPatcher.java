package modtools;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import data.Ea;
import data.Metaconfig;

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

		Mbox.Infobox("attrib <" + lotkeyword + "> not found in file <"
				+ ea.getEafilename() + ">");
		return false;
	}

	public boolean patchLotsizeSq4x(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "extern double mmLots = ";
		String lotkeyword2= "extern double mmLotsIfNoMM = ";
							 
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
			else if (zeilenspeicher[i].contains(lotkeyword2))
			{
				Tracer.WriteTrace(10, "E: memory management not supported use Fixed size for lots");
				zeilenspeicher[i] = (lotkeyword2 + lotsize + ";");
				return true;
			}
		}

		Mbox.Infobox("attrib <" + lotkeyword + "> or <"+lotkeyword2+">not found in file <"
				+ ea.getEafilename() + ">");
		return false;
	}
	public boolean patchLotsizeExpertStudio(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "static input double Entry_Amount =";
		String lotkeywordrepl="extern double Entry_Amount =";
		
							 
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

		Mbox.Infobox("attrib <" + lotkeyword + "> or <"+lotkeyword+">not found in file <"
				+ ea.getEafilename() + ">");
		return false;
	}
	public boolean patchLotsizeExpertStudioPortfolio(Ea ea, Metaconfig meRealconf)
	{
		// falls globale config gewünscht
		String lotkeyword = "static input double Entry_Amount      =";
		String lotkeywordrepl="extern double Entry_Amount      =";
		
							 
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

		Mbox.Infobox("attrib <" + lotkeyword + "> or <"+lotkeyword+">not found in file <"
				+ ea.getEafilename() + ">");
		return false;
	}
	public boolean patchCommentSq4x(Ea ea)
	{
		String eafilename=ea.getEafilename();
		eafilename=eafilename.replace(".mq4","");
		//extern string CustomComment = "Q67_EURUSD_M15Strategy_4_35_155";
		String kw = "extern string CustomComment = ";
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			
			if (zeilenspeicher[i].contains(kw))
			{
				String mem=zeilenspeicher[i];
				String comment=eafilename;
				comment=comment.replace("Strategy", "");
				
				zeilenspeicher[i] = "extern string CustomComment = "+"\""+comment+"\";";
				return true;
			}
		}
		Mbox.Infobox("attrib <" + kw + "> not found in file <"
				+ ea.getEafilename() + ">");
		return false;
		
	}
	public boolean patchCommentExpertStudio(Ea ea)
	{
		String eafilename=ea.getEafilename();
		eafilename=eafilename.replace(".mq4","");
		//extern string CustomComment = "Q67_EURUSD_M15Strategy_4_35_155";
		String kw = "string comment    = ";
		for (int i = 0; i < 20000; i++)
		{
			
			if (zeilenspeicher[i] == null)
				continue;
			
			if (zeilenspeicher[i].contains(kw))
			{
				String mem=zeilenspeicher[i];
				String comment=eafilename;
				comment="\""+comment.replace("Strategy", "")+"\";";
				
				
				zeilenspeicher[i] = "\t\tstring comment    = "+comment;
				return true;
			}
		}
		Mbox.Infobox("attrib <" + kw + "> not found in file <"
				+ ea.getEafilename() + ">");
		return false;
		
		
		
		
		
	}
	public boolean patchCommentSq3(Ea ea)
	{
		return true;
	}
	
	protected void patchInitSq3()
	{
		addIncludes();
		
		// falls der initpatch schon drin ist dann mache nix
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i]
					.contains("MathSrand(TimeLocal()+(MagicNumber+4)*5);") == true))
				return;
		}

		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if ((zeilenspeicher[i].contains("int init()"))
					&& (zeilenspeicher[i].contains("{")))
			{
				// fall a) init sieht so aus "init () {"
				addInitpatch(i,"sq3");
				return;
			} else if ((zeilenspeicher[i].contains("int init()"))
					&& (zeilenspeicher[i + 1].contains("{")))
			{
				// fall b) init sieht so aus "init ()
				// {" [klammer ist in nächster zeile]
				addInitpatch(i + 1,"sq3");
				return;
			}
		}
	}

	private void addInitpatch(int ind,String vers)
	{
		String mn="";
		if(vers.contains("sq3")||vers.contains("sq4"))
			mn="MagicNumber";
		else
			mn="Magic_Number";
		
		// warte 0 bis 150 sekunden
		addnewline(ind + 1, "   MathSrand(TimeLocal()+("+mn+"+4)*5);");
		addnewline(ind + 2, "   int rand_val=MathRand();");
		addnewline(ind + 3,
				"   Print(\"init wait \"+((rand_val)/1000)*10+\" secs\");");
		addnewline(ind + 4, "   Sleep(rand_val*10);");
		addnewline(ind + 5, "//patch lotsize");
		addnewline(ind + 6, "if(lotconfig(IntegerToString("+mn+",0,0))==false)");
		addnewline(ind + 7, "Alert(\"Error cant ptch lotsize\");");
	}

	protected void patchInitSq4x()
	{
		addIncludes();
		// falls der initpatch schon drin ist dann mache nix
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i]
					.contains("MathSrand(TimeLocal()+(MagicNumber+4)*5);") == true))
				return;
		}

		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			if ((zeilenspeicher[i].contains("int init()"))
					&& (zeilenspeicher[i].contains("{")))
			{
				// fall a) init sieht so aus "init () {"
				addInitpatch(i,"sq4");
				return;
			} else if ((zeilenspeicher[i].contains("int init()"))
					&& (zeilenspeicher[i + 1].contains("{")))
			{
				// fall b) init sieht so aus "init ()
				// {" [klammer ist in nächster zeile]
				addInitpatch(i + 1,"sq4");
				return;
			}else if ((zeilenspeicher[i].contains("int OnInit()"))
					&& (zeilenspeicher[i ].contains("{")))
			{
				// fall c)  init sieht so aus "int OnInit() {"
				// {" [klammer ist gleiche zeile]
				addInitpatch(i,"sq4" );
				return;
			}else if ((zeilenspeicher[i].contains("int OnInit()"))
					&& (zeilenspeicher[i+1 ].contains("{")))
			{
				// fall c)  init sieht so aus "int OnInit() {"
				// {" [klammer ist in folgezeile]
				addInitpatch(i+1 ,"sq4");
				return;
			}
		}	
		Tracer.WriteTrace(10, "E: patcher cant find init() in mq4-file--> stop"); 
		
	}
	
	protected void patchInitStudioBuilder()
	{
		addIncludes();
		// falls der initpatch schon drin ist dann mache nix
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i]
					.contains("MathSrand(TimeLocal()+(Magic_Number+4)*5);") == true))
				return;
		}

		for (int i = 0; i < 20000; i++)
		{
			// System.out.println("i="+i+" zeile<"+zeilenspeicher[i]+">");
			if (zeilenspeicher[i] == null)
				continue;
			// include einbauen
			
			if ((zeilenspeicher[i].contains("int OnInit()"))
					&& (zeilenspeicher[i+1 ].contains("{")))
			{
				// fall c)  init sieht so aus "int OnInit() {"
				// {" [klammer ist in folgezeile]
				addInitpatch(i+1 ,"studio");
				return;
			}
		}	
		Tracer.WriteTrace(10, "E: patcher cant find init() in mq4-file--> stop"); 
		
	}
	
	protected void addIncludes()
	{
		if (checkKeyword("#include <monitorlib.mqh>") == false)
		{
			addnewline(0, "#include <monitorlib.mqh>");
		}

		// 509er Metatrader version, in der 600+ Metatraderversion darf keine
		// stdlib mehr rein
		if (checkKeyword("string ErrorDescription(int error_code)") == false)
		{
			if (checkKeyword("#include <stdlib.mqh>") == false)
			{
				addnewline(1, "#include <stdlib.mqh>");
			}
		}
		if (checkKeyword("extern string Kennung=") == false)
		{
			addnewline(2, "extern string Kennung=\"\";");
		}

		
	}
	
	
	protected void addAbschaltAutomaticSq3(String kennung, String expertname)
	{
		addIncludes();
	
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
				addnewline(i + 2,
						" if( tradecheck(MagicNumber,Kennung)==0) return (0);");
				return; // gehe raus da die abschaltautomatik eingebaut ist
			}
		}

		// hier suche nach der start-funktion da kein Tradecheck eingebaut ist
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if ((zeilenspeicher[i].contains("start()"))
					&& (zeilenspeicher[i].contains(";") == false))
			{
				// Fall 1 : Klammer ist in der Startzeile
				if (zeilenspeicher[i].contains("{") == true)
				{

					addnewline(i + 1,
							"  Lots=lotcheck(MagicNumber,Kennung,Lots);");
					addnewline(i + 2,
							" if( tradecheck(MagicNumber,Kennung)==0) return (0);");
					return;
				} else
				{
					if ((zeilenspeicher[i + 1].contains("{") == true)
							&& (zeilenspeicher[i + 1].length() < 10))
					// Fall 2: Klammer ist eine Zeile weiter
					{
						// Hier wird die Abschaltautomatik eingebaut
						addnewline(i + 2,
								" if( tradecheck(MagicNumber,Kennung)==0) return (0);");
						addnewline(i + 3,
								"  Lots=lotcheck(MagicNumber,Kennung,Lots);");
						return;
					}
					// fehler
					// da ist so eine Zeile drin
					// { bla bala...
					else
						Mbox.Infobox("Installation error startautomatic start() ea<"
								+ expertname + ">");
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
			if (zeilenspeicher[i].contains("bool sqIsBarOpen;")==true)
				{
					addnewline(i +1, "////sleeptime for modulo modificacion");
					addnewline(i+2, "int sleeptimemod =1000;");
					break;
				}
			if(i==19999)
				Tracer.WriteTrace(10, "E:keyword <bool sqIsBarOpen;> not found");
		}		
		
		for ( int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;
			if ((zeilenspeicher[i]
					.contains("if(openPrice == OrderOpenPrice() && stopLoss == OrderStopLoss() && profitTarget == OrderTakeProfit()"))
					&& (zeilenspeicher[i + 1]
							.contains(" all values are the same"))
					&& (zeilenspeicher[i + 2].contains("return(true)")))
			{
				addnewline(i + 4, "sleeptimemod=(MagicNumber%10)*1000;");
				addnewline(i + 5, "Print(\"Sleeptime for mod=\"+sleeptimemod);");
				addnewline(i + 6, " Sleep(sleeptimemod);");
				break;
			}

		}
	}

	protected void addAbschaltAutomaticSq4x(String kennung, String expertname)
	{

		if (checkKeyword("#include <monitorlib.mqh>") == false)
		{
			addnewline(0, "#include <monitorlib.mqh>");
		}

		// 509er Metatrader version, in der 600+ Metatraderversion darf keine
		// stdlib mehr rein
		if (checkKeyword("string ErrorDescription(int error_code)") == false)
		{
			if (checkKeyword("#include <stdlib.mqh>") == false)
			{
				addnewline(1, "#include <stdlib.mqh>");
			}
		}
		if (checkKeyword("extern string Kennung=") == false)
		{
			addnewline(2, "extern string Kennung=\"\";");
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
				addnewline(i + 2,
						" if( tradecheck(MagicNumber,Kennung)==0) return (0);");
				return; // gehe raus da die abschaltautomatik eingebaut ist
			}
		}

		// hier suche nach der start-funktion da kein Tradecheck eingebaut ist
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if ((zeilenspeicher[i].contains("start()"))
					&& (zeilenspeicher[i].contains(";") == false))
			{
				// Fall 1 : Klammer ist in der Startzeile
				if (zeilenspeicher[i].contains("{") == true)
				{

					addnewline(i + 1,
							"  Lots=lotcheck(MagicNumber,Kennung,Lots);");
					addnewline(i + 2,
							" if( tradecheck(MagicNumber,Kennung)==0) return (0);");
					return;
				} else
				{
					if ((zeilenspeicher[i + 1].contains("{") == true)
							&& (zeilenspeicher[i + 1].length() < 10))
					// Fall 2: Klammer ist eine Zeile weiter
					{
						// Hier wird die Abschaltautomatik eingebaut
						addnewline(i + 2,
								" if( tradecheck(MagicNumber,Kennung)==0) return (0);");
						addnewline(i + 3,
								"  Lots=lotcheck(MagicNumber,Kennung,Lots);");
						return;
					}
					// fehler
					// da ist so eine Zeile drin
					// { bla bala...
					else
						Mbox.Infobox("Installation error startautomatic start() ea<"
								+ expertname + ">");
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
					&& (zeilenspeicher[i + 1]
							.contains("for(int i=i; i<expBarsPeriod") == true))
			{
				zeilenspeicher[i] = "   int numberOfBars = 0,i=0;";
				zeilenspeicher[i + 1] = zeilenspeicher[i + 1]
						.replace("int", "");
			}
		}
	}
	protected void addPostcodePatch(String fname)
	{
		//das komplette fname wird am ende zum zeilenspeicher hinzugefügt
		int endpos=0;
		
		if(isSq3Ea()==true)
			fname=fname+".sq3";
		else
			if (isSq4Ea()==true)
			fname=fname+".sq4";
		else if (isSq4x==2)
			fname=fname+".sea";
		else
			return;
		
		if(FileAccess.FileAvailable(fname)==false)
			Tracer.WriteTrace(10, "E:File missing <"+fname+"> --> stop");
		
		Inf inf= new Inf();
		inf.setFilename(fname);
		
		
		for (endpos=19999; endpos>0; endpos--)
		{
			if(zeilenspeicher[endpos]!=null)
			   break;
		}
		if(endpos==0)
			Tracer.WriteTrace(10, "E:can´t find endpos on file <"+fname+">");
					
		for(int i=1; i<1000; i++)
		{
			String str=inf.readZeile();
			if(str==null)
				break;
			zeilenspeicher[endpos+i]=new String(str);
		}
		inf.close();
		
	}
}