package data;

import hilfsklasse.SG;
import hilfsklasse.Tracer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigServer
{
	// hier werden die tradelisten gespeichert, dies sollte ein dropboxdirectory
	// sein
	private static String TradelistenDir = null;

	// hier kommen die steuerkommandos rein, dies sollte ein dropboxdirectory
	// sein
	private static String CmdDir = null;

	// dies ist das exportverzeichniss wo die ea´s etc exportiert werden, sollte
	// auch dropbox sein
	private static String ExportDir = null;

	// dies ist das ea dir, hier werden allen eas abgelegt
	private static String EaDir = null;

	// update verzeichniss
	private static String UpdateDir = null;

	//gibt an wann der server neue tradelisten holt. Also nach wieviel stunden die alten zu alt sind
	private static int Maxhour = 0;

	private static Properties prop = new Properties();

	private static int ServerMinLen= 25000000;
	
	public ConfigServer()
	{
		try
		{
			prop.load(new FileInputStream(GlobServer.rootdir
					+ "\\conf\\conf.prop"));
			TradelistenDir = prop.getProperty("TradelistenDir");
			CmdDir = prop.getProperty("CmdDir");
			ExportDir = prop.getProperty("ExportDir");
			EaDir = prop.getProperty("EaDir");
			Maxhour = SG.get_zahl(prop.getProperty("Maxhour"));
			UpdateDir = prop.getProperty("UpdateDir");
			ServerMinLen=Integer.valueOf(prop.getProperty("ServerMinLen"));
			if (Maxhour == -1)
				Tracer.WriteTrace(10,
						"configuration error plese set Maxhour in <"
								+ GlobServer.rootdir + "\\conf\\conf.prop"
								+ "> ");

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getTradelistenDir()
	{
		return TradelistenDir;
	}

	public static void setTradelistenDir(String tradelistenDir)
	{
		TradelistenDir = tradelistenDir;
	}

	public static String getCmdDir()
	{
		return CmdDir;
	}

	public static void setCmdDir(String cmdDir)
	{
		CmdDir = cmdDir;
	}

	public static String getExportDir()
	{
		return ExportDir;
	}

	public static void setExportDir(String exportDir)
	{
		ExportDir = exportDir;
	}

	public static String getEaDir()
	{
		return EaDir;
	}

	public static void setEaDir(String eaDir)
	{
		EaDir = eaDir;
	}

	public static int getMaxhour()
	{
		return Maxhour;
	}

	public static void setMaxhour(int maxhour)
	{
		Maxhour = maxhour;
	}

	public static String getUpdateDir()
	{
		return UpdateDir;
	}

	public static void setUpdateDir(String updateDir)
	{
		UpdateDir = updateDir;
	}

	public static int getServerMinLen()
	{
		return ServerMinLen;
	}

	public static void setServerMinLen(int serverMinLen)
	{
		ServerMinLen = serverMinLen;
	}

}
