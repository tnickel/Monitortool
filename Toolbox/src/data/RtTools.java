package data;

import gui.Mbox;
import gui.Viewer;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import hiflsklasse.Tradestatistik;

import java.io.File;

import montool.MonDia;

import org.eclipse.swt.widgets.Display;

import work.JToolboxProgressWin;
import xml.TnParser;
import Metriklibs.FileAccessDyn;

public class RtTools
{
	private String quelldir_glob = null;
	private String zieldir_glob = null;
	private int maxlosses_glob = 0;
	private float maxdd_faktor_glob = 0;
	private float equityfaktor_glob = 0;
	private float Longshort_faktor_glob = 0;
	

	public float getLongshort_faktor_glob()
	{
		return Longshort_faktor_glob;
	}

	public void setLongshort_faktor_glob(float longshort_faktor_glob)
	{
		Longshort_faktor_glob = longshort_faktor_glob;
	}

	public float getEquityfaktor_glob()
	{
		return equityfaktor_glob;
	}

	public void setEquityfaktor_glob(float equityfaktor_glob)
	{
		this.equityfaktor_glob = equityfaktor_glob;
	}

	public String getQuelldir_glob()
	{
		return quelldir_glob;
	}

	public void setQuelldir_glob(String quelldir_glob)
	{
		this.quelldir_glob = quelldir_glob;
	}

	public String getZieldir_glob()
	{
		return zieldir_glob;
	}

	public void setZieldir_glob(String zieldir_glob)
	{
		this.zieldir_glob = zieldir_glob;
	}

	public float getMaxdrawdown_faktor_glob()
	{
		return maxdd_faktor_glob;
	}

	public void setMaxdrawdown_faktor_glob(float maxdrawdown_faktor_glob)
	{
		this.maxdd_faktor_glob = maxdrawdown_faktor_glob;
	}

	public int getMaxlosses_glob()
	{
		return maxlosses_glob;
	}

	public void setMaxlosses_glob(int maxlosses_glob)
	{
		this.maxlosses_glob = maxlosses_glob;
	}

	public String setQuelldirRequester(String defaultpath)
	{
		String dir = selectDir(defaultpath);
		this.quelldir_glob = dir;
		return dir;
	}

	public String setZieldirRequester(String defaultpath)
	{
		String dir = selectDir(defaultpath);
		this.zieldir_glob = dir;
		return dir;
	}

	private String selectDir(String defaultpath)
	{
		String fnam = MonDia.DirDialog(Display.getDefault(), defaultpath);
		return fnam;
	}

	public void filter(boolean rtcheck, boolean eqcheck, boolean lastyearsflag,
			boolean cut2yearsflag, boolean longshortflag,
			boolean showtableresultflag,boolean xauusdflag)
	{
		// cut2yearsflag==true, dann wird das erste und letzte Jahr nicht
		// gerechnet
		// 1)str werden eingelesen
		// 2)dann wird RetDD95% < (faktor *RetDD) gerechnet
		// 3)und ins Ausgabeverzeichniss gepeichert (falls diese strategie die
		// bedingung erfüllt)
		// 4)longshortflag: falls das gesetzt ist wird der longshortfilter
		// ausgeführt
		// 5) xauusdflag: falls das gesetzt ist wird die strategie nicht genommen
		// wenn retdd== retddrobust ist dies ist für den XAUUSD-strategien bug

		int copycount = 0;
		int strzaehler = 0;
		String respmessage1 = "", respmessage2 = "", respmessage3 = "";
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(quelldir_glob, 1);
		int anz = fdyn.holeFileAnz();
		String rp = GlobToolbox.getRootpath();

		String stratinfofilename = rp + "\\rt_cal_info.txt";
		File stratinfofilename_f = new File(stratinfofilename);
		if (stratinfofilename_f.exists() == true)
			stratinfofilename_f.delete();

		Inf stratinfo = new Inf();
		stratinfo.setFilename(stratinfofilename);
		stratinfo
				.writezeile("filename#result#RetDD | RetDD95#x#short% | long%#*** ");

		if (anz == 0)
		{
			Mbox.Infobox("I: no strfiles in <" + quelldir_glob + ">");
			return;
		}

		// progress slider
		JToolboxProgressWin jp = new JToolboxProgressWin("filter <0/" + anz
				+ ">", 0, (int) anz);
		jp.update(0);

		File zdirf = new File(zieldir_glob);
		FileAccess.deleteDirectoryContentPostfix(zdirf, ".str");

		// berechne die werte für die einzelen strs
		for (int i = 0; i < anz; i++)
		{
			String fnam1 = fdyn.holeFileSystemName();
			String fnam = quelldir_glob + "\\" + fnam1;

			jp.update(i);
			// only .str-files are allowed
			if (fnam.contains(".str") == false)
				continue;
			strzaehler++;
			// hier wird kopiert wenn die bedingung erfüllt ist

			if ((maxdd_faktor_glob < 0.1) || (maxdd_faktor_glob > 1.1))
			{
				Mbox.Infobox("I: RetDDfkator=" + maxdd_faktor_glob
						+ " out of range, It should be between 0.1---1.1");

				break;
			}
			if ((Longshort_faktor_glob < 0.01)
					|| (Longshort_faktor_glob >= 0.49))
			{
				Mbox.Infobox("I: LongShortfaktor=" + Longshort_faktor_glob
						+ " out of range, It should be between 0.01---0.49");

				break;
			}
			if ((equityfaktor_glob < 0.1) || (equityfaktor_glob > 10))
			{
				Mbox.Infobox("I: Equityfaktor=" + equityfaktor_glob
						+ " out of range, It should be between 0.1---10");
				stratinfo.writezeile(fnam1 + "#bad#missed rule3#");
				break;
			}

			if (rtcheck == true)
			{
				FilterResponse resp = check_Retdd95_Retdd(fnam,
						maxdd_faktor_glob,xauusdflag);
				if (resp.getResponseval() == false)
				{
					// bad case
					stratinfo.writezeile(fnam1 + "#bad#"
							+ resp.getResponsestring() + "###");
					continue;
				} else
					// ok case
					respmessage1 = resp.getResponsestring();
			}
			if (eqcheck == true)
			{
				FilterResponse resp = check_gewinnverteilung(fnam,
						lastyearsflag, cut2yearsflag);

				if (resp.getResponseval() == false)
				{
					// bad case
					stratinfo.writezeile(fnam1 + "#bad##"
							+ resp.getResponsestring() + "##");
					continue;
				} else
					// ok case
					respmessage2 = resp.getResponsestring();
			}

			if (longshortflag == true)
			{
				FilterResponse resp = check_longshortverteilung(fnam);
				if (resp.getResponseval() == false)
				{
					// bad case
					stratinfo.writezeile(fnam1 + "#bad" + "###"
							+ resp.getResponsestring() + "#");
					continue;
				} else
					// ok case
					respmessage3 = resp.getResponsestring();
			}
			
				
			// der wert ist innerhalb der schranken dann kopiere um
			Tracer.WriteTrace(20, "I:copy from<" + quelldir_glob + "\\" + fnam1
					+ "> to<" + zieldir_glob + "\\" + fnam1 + ">");
			kopiereStr(quelldir_glob, zieldir_glob, fnam1);

			// strategienamen nimm in tabelle auf
			stratinfo.writezeile(fnam1 + "#ok#" + respmessage1 + "#"
					+ respmessage2 + "#" + respmessage3 + "#");

			copycount++;

		}
		stratinfo.close();
		jp.end();
		Mbox.Infobox("#<" + copycount + ">/<" + strzaehler
				+ "> strfiles copied ready");

		// zeige tabelle an
		if (showtableresultflag == true)
		{
			Viewer v = new Viewer();
			v.viewTableExtFile(Display.getDefault(), stratinfofilename);
		}
	}

	private void kopiereStr(String quelldir, String zieldir, String fnam1)
	{
		FileAccess.copyFile(quelldir + "\\" + fnam1, zieldir + "\\" + fnam1);
	}

	private FilterResponse check_longshortverteilung(String fname)
	{
		FilterResponse resp = new FilterResponse();

		// lese alles zur sicherheit, rt liegen eh am ende
		float longshortfaktor = Longshort_faktor_glob;
		if (longshortfaktor >= 0.48)
			Tracer.WriteTrace(10, "I:longshortfaktor is too high <"
					+ longshortfaktor + ">");

		Tradeliste eatl = new Tradeliste(null);
		eatl.initBacktest(fname);

		// dies ist der faktor für die long/short verteilung

		Tradestatistik tradestat = eatl.calcTradestatistik();

		float longanz = (float) tradestat.getAnzlong();
		float shortanz = (float) tradestat.getAnzshort();
		float shortproz = (100 / (longanz + shortanz)) * shortanz;
		float longproz = (100 / (longanz + shortanz)) * longanz;
		float minproz = longshortfaktor * 100;

		if ((shortproz > minproz) && (longproz > minproz))
		{
			resp.setResponsestring("s=" + shortproz + "% | l=" + longproz + "%");
			resp.setResponseval(true);
			return resp;
		} else
		{
			resp.setResponsestring("s=" + shortproz + "% | l=" + longproz + "%");
			resp.setResponseval(false);
			return resp;
		}
	}

	private FilterResponse check_gewinnverteilung(String fname,
			boolean lasttwoyearsflag, boolean cut2yearsflag)
	{
		// fname= name of the strategy
		// lastyearsflag: true=strategy have to win in the last 2 years
		// cut2yearsflag: true, than the first and last year will be cutted

		// if (profit)<jahresgewinn ==> looses
		// if (profit)>jahresgewinn ==> gewinn
		// maxlosses=this amount of years can be in loose

		FilterResponse resp = new FilterResponse();

		// lese alles zur sicherheit, rt liegen eh am ende
		TnParser parser = new TnParser();
		parser.loadFile(fname, 0);
		float maxdd = parser.getMaxDrawdown_IS();

		Tradeliste eatl = new Tradeliste(null);
		eatl.initBacktest(fname);
		Gewinnverteilung gv = eatl.calcGewinnjahrType3();
		boolean result = gv.checkMaxlosses(maxlosses_glob, maxdd, fname,
				lasttwoyearsflag, cut2yearsflag, equityfaktor_glob);

		resp.setResponseval(result);

		if (result == true)
			resp.setResponsestring("true");
		else
			resp.setResponsestring("false");
		return (resp);

	}

	private FilterResponse check_Retdd95_Retdd(String fnam, float faktor,boolean xauusdflag)
	{
		TnParser parser = new TnParser();
		FilterResponse resp = new FilterResponse();
		// lese alles zur sicherheit, rt liegen eh am ende
		// Yes, "RetDDrobust95>RetDD*faktor" with MaxRobust faktor of 0.5 or
		// 0.33

		parser.loadFile(fnam, 0);

		// retdd auslesen
		float retdd95 = parser.getRetDD_Robust(95);
		float retdd = parser.getRetDD_IS();

		if(xauusdflag==true)
			if(Math.abs(retdd-retdd95)<0.01)
			{
				Tracer.WriteTrace(20, "I:XAUUSDFLAG retdd<"+retdd+"> retdd95<"+retdd95+">");
				resp.setResponseval(false);
				resp.setResponsestring(retdd + " | " + retdd95 +" XAUUSDFLAG");
				return resp;
			}
		
		// wähle faktor = 2
		// retDD95 * faktor muss grösser als retdd sein
		Tracer.WriteTrace(20, "I:check strategy <" + fnam + "> retdd<" + retdd
				+ "> retdd95rob<" + retdd95 + ">");
		if ((retdd95) > retdd * faktor)
		{
			// ok case
			Tracer.WriteTrace(20, "Debug Test1:ok: <" + fnam
					+ ">: reason [RetDD differ]: RetDDrobust95(" + retdd95
					+ ")  >  RetDD_fakt(" + retdd * faktor + ")");
			resp.setResponsestring(retdd + " | " + retdd95);
			resp.setResponseval(true);
			return resp;
		} else
		{
			// bad case
			Tracer.WriteTrace(20, "Debug Test1:looses :<" + fnam
					+ "> looses: reason [RetDD differ]: REtDDrobust95("
					+ retdd95 + ")  <  RetDD_fakt(" + retdd * faktor + ")");
			resp.setResponseval(false);
			resp.setResponsestring(retdd + " | " + retdd95);
			return resp;
		}
	}
	

}
