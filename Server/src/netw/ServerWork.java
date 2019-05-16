package netw;

import hilfsklasse.FileAccessDyn;
import hilfsklasse.SG;
import hilfsklasse.Tracer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import tcp.TPacket;
import tcp.TcpProtokoll;
import data.Client;
import data.ConfigServer;
import data.GlobServer;
import data.Rootpath;
import datefunkt.Mondate;

public class ServerWork
{
	public void taskHoleTradeliste(TcpProtokoll tcpprot, Socket socket,
			Client cl)
	{
		// die tradeliste wird vom monitorclient angefordert
		TPacket tps = new TPacket();
		tps.setCmd(1);
		tps.setDatalen(13);
		tps.setData("ok@sendTrades");
		tcpprot.TcpSendeNachricht(socket, tps, null,1);
		// die Kennung wurde gesendet, dann ok zurück senden
		// out("0000100000002ok@sendTrades");
	}

	public void taskEndCommunication(TcpProtokoll tcpprot, Socket socket,
			Client cl)
	{
		TPacket tps = new TPacket();
		tps.setCmd(1);
		tps.setDatalen(6);
		tps.setData("ok@end");
		tcpprot.TcpSendeNachricht(socket, tps, null,1);
		
		// die Kennung wurde gesendet, dann ok zurück senden
		// out("0000100000006ok@end");
	}

	public String taskHoleEas(TcpProtokoll tcpprot, Socket socket, Client cl)
	{
		// return metatradername
		String metatradername = null;

		// die eas werden vom client angefordert
		TPacket tps = new TPacket();
		tps.setCmd(1);
		tps.setDatalen(21);

		// steuerkommando= tnickel___tnickel-pc___1234___Thinkforex8___.start
		String steuerkommando = holeSteuerkommando(cl);

		// posibility check
		if (steuerkommando == null)
			Tracer.WriteTrace(10,
					"internal error zuordnungsfehler Steuerkommando clientname<"
							+ cl.getClientname() + "> serial<" + cl.getSerial()
							+ ">");

		String[] segs = steuerkommando.split("___");

		// posibility check
		if ((cl.getClientname().equalsIgnoreCase(segs[1]) == false)
				|| (cl.getSerial() != SG.get_zahl(segs[2])))
		{
			Tracer.WriteTrace(10, "internal error zuordnungsfehler clientname<"
					+ cl.getClientname() + "> serial<" + cl.getSerial() + ">");
		}

		// tps.setData("ok@sendMt@Thinkforex8");
		tps.setData("ok@sendMt@" + segs[3]);
		metatradername = segs[3];

		// die datenlänge setzen
		tps.setDatalen(tps.getData().length());

		tcpprot.TcpSendeNachricht(socket, tps, null,1);
		// die Kennung wurde gesendet, dann ok zurück senden und metatrader
		// anfordern
		return metatradername;
	}

	public boolean checkTaskTradeliste(Client cl)
	{
		// false: wenn die tradeliste zu alt ist, oder nicht vorhanden
		// true: wenn alles ok ist

		// prüft wie alt die Tradeliste ist
		String cmdverz = ConfigServer.getTradelistenDir();
		int id = cl.getSerial();
		String hostname = cl.getClientname();

		// den tradelistennamen berechnen
		// tnickel___tnickel-PC___310.txt.zip
		String tradelistenname = cmdverz + "\\" + cl.getUsername() + "___"
				+ hostname + "___" + id + ".zip";

		// dies ist das tradelistenfile
		File trfile = new File(tradelistenname);

		// wenn das file nicht da ist dann false zurück
		if (trfile.exists() == false)
		{
			Tracer.WriteTrace(20, "Tradelist <" + tradelistenname
					+ "> not available, I need first Tradelist");
			return false;
		}
		// holt das filealter in stunden
		int filealter_stunden = Mondate.Filealterhour(tradelistenname);

		// falls das file zu alt ist, oder nicht vorhanden
		if (filealter_stunden > ConfigServer.getMaxhour())
		{
			Tracer.WriteTrace(20, "Tradelist<" + tradelistenname
					+ "> to old age is<" + filealter_stunden
					+ "> hours, I need new Tradelist");
			return false;
		} else
		{
			Tracer.WriteTrace(20, "I:Tradelist<" + tradelistenname
					+ "> ok, age=<"+filealter_stunden+">hours");
			return true;
		}
	}

	public boolean checkTaskHoleEas(Client cl)
	{
		// falls von dem client was abzuholen ist
		// dann wird das Kommando übergeben
		// false: falls nix zu holen ist

		if (holeSteuerkommando(cl) != null)
			return true; // es wurde etwas gefunden
		else
			return false;// nix wird vom client gewollt.
	}

	private String holeSteuerkommando(Client cl)
	{
		// prüft ob eas zu holen sind
		// return: das steuerkommando was für den client zutrifft
		String cmdverz = ConfigServer.getCmdDir();
		int id = cl.getSerial();
		String hostname = cl.getClientname();

		// von dem angemeldeten client möchten wir etwas
		String suchstring = cl.getUsername()+"___"+hostname + "___" + id;
		// bsp: tnickel___tnickel_pc___1234___Thinkforex8

		// alle files des verzeichnisses betrachten
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(cmdverz, 1);
		int anz = fdyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn.holeFileSystemName();
			if ((fnam.contains(suchstring) == true)
					&& (fnam.contains(".start") == true))
				return fnam;
		}
		return null;
	}

	public boolean checkUpdate(TPacket tp, String hostname, int serial)
	{
		// prüft ob ein neues update ansteht
		String[] splittArray = tp.getData().split("@");

		String channel = splittArray[5];
		int filelen = SG.get_zahl(splittArray[6]);

		// vergleicht die Filelänge
		String updatefile = null;

		if (channel.equalsIgnoreCase("release"))
			updatefile = ConfigServer.getUpdateDir()
					+ "\\release\\monitortool.jar";
		else if (channel.equalsIgnoreCase("development"))
			updatefile = ConfigServer.getUpdateDir()
					+ "\\development\\monitortool.jar";
		else
			updatefile = ConfigServer.getUpdateDir()
			+ "\\freeware\\monitortool.jar";

		File quellfile = new File(updatefile);

		if (quellfile.exists() == false)
		{
			Tracer.WriteTrace(10, "E:fatal error updatefile <" + updatefile
					+ "> not available on server");
			
		}
		//serverminlen=30000000
		int serverminlen=ConfigServer.getServerMinLen();
		if (quellfile.length() < serverminlen)
		{
			Tracer.WriteTrace(20, "W:updatefile on server to short <"
					+ updatefile + "> length <" + quellfile.length() + ">");
			return false;
		}
		if (quellfile.length() != filelen)
		{
			Tracer.WriteTrace(
					20,
					"I:Client need update version is different<" + hostname
							+ "___" + serial + "> versServer<"
							+ quellfile.length() + "> versClient<" + filelen
							+ ">");
			return true;
		} else
		{
			Tracer.WriteTrace(20, "I:Client version is up to date<" + hostname
					+ "___" + serial + "> versServer<" + quellfile.length()
					+ "> versClient<" + filelen + ">");
			return false;
		}
	}

	public boolean taskSendUpdate(TPacket tps, TcpProtokoll tcpprot,
			Socket socket, Client cl, String hostname, int serial)
	{
		String sendfilename = null;

		String[] splittArray = tps.getData().split("@");
		String channel = splittArray[5];

		if (channel.equalsIgnoreCase("release"))
			sendfilename = ConfigServer.getUpdateDir()
					+ "\\release\\monitortool.jar";
		else if (channel.equalsIgnoreCase("development"))
			sendfilename = ConfigServer.getUpdateDir()
					+ "\\development\\monitortool.jar";
		else 
			sendfilename = ConfigServer.getUpdateDir()
			+ "\\freeware\\monitortool.jar";

		Tracer.WriteTrace(20, "I:Server send update<" + hostname + "___"
				+ serial + "> updatefile<" + sendfilename + ">");

		// dann alles senden
		tps.setCmd(15);
		tps.setData("");
		tcpprot.TcpSendeNachricht(socket, tps, sendfilename,1);
		Tracer.WriteTrace(20, "I:Server sended update to client ok<" + hostname
				+ "___" + serial + "> updatefile<" + sendfilename
				+ "> transfer ok");

		return true;
	}

}
