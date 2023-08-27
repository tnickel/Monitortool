package backtest;

import java.io.File;

import Metriklibs.FileAccessDyn;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class Mt4Backtester
{
	
	public void Install(String rootpath, String appdata)
	{
		//rootpath: ist der pfad wo das installationspacket ist
		//appdata ist das appdata des jeweiligen Metatrader
		
		if(appdata.contains("mt5"))
			return;
		
		FileAccessDyn fd = new FileAccessDyn();
		String backtest_ini = rootpath + "\\install\\MT4_backtests\\backtestconfig.ini";

		File btzieldir =new File(appdata+"\\mql4\\MT4_backtests");
		if(btzieldir.exists()==false)
			if(btzieldir.mkdir()==false)
				Tracer.WriteTrace(10, "E:copyBacktstEnvironment:cant create <"+btzieldir+"> -->stop");
		String zielfile=appdata+"\\mql4\\MT4_backtests\\backtestconfig.ini";
		
		Tracer.WriteTrace(20,
				"I:copy backtest.ini from<" + backtest_ini + "> to<" + zielfile + ">");
		
		// Kopiere die backtestini
		fd.copyFile3(backtest_ini, zielfile,1);
		
		//generate batchfile
		String fnam=appdata+"\\mql4\\MT4_backtests\\make_backtest.bat";
		new File(fnam).delete();
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String terminalexe=appdata+"\\terminal.exe";
		String inifile=appdata+"\\mql4\\MT4_backtests\\backtestconfig.ini";
		inf.writezeile("\""+terminalexe+"\" /portable /wait \""+inifile+"\"");
		inf.close();
	}
	public void setvalues(String eaname,String timeframe, String currency)
	{
		
		
	}
	public void makeBacktest()
	{
		
		
	}
	
	public void showResults()
	{
		
		
	}

}
