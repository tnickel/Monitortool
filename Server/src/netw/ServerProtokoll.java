package netw;

import hilfsklasse.FileAccessDyn;
import hilfsklasse.SG;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

import network.Crypto;
import tcp.TPacket;
import tcp.TcpProtokoll;
import data.Client;
import data.Clientlist;
import data.ConfigServer;
import datefunkt.Mondate;

public class ServerProtokoll
{
	// Hier findet die Protokollverarbeitung für den Server statt
	// hier werden die empfangenen Packete ausgewertet

	PrintWriter out_glob = null;
	Clientlist clist_glob = null;
	private String hostname_glob = null;
	private int serial_glob = 0;
	private String username_glob = null;
	private String email_glob = null;
	private String metaname_glob = null;
	private TcpProtokoll tcpprot = new TcpProtokoll();
	private ServerWork swork = new ServerWork();

	public ServerProtokoll()
	{
		clist_glob = new Clientlist();
		clist_glob.init();
	}

	public void setOut(PrintWriter out)
	{
		out_glob = out;
	}

	static int binaerflag = 0;

	public int WorkIncomming(TPacket tp, BufferedReader bufferedReader,
			Socket socket, TcpProtokoll tcpprot)
	{
		// für das debug für den Sockettest
		// 00001000000002ok
		// 00005000000002ok

		// kennung erhalten
		if (tp.getCmd() == 1)
		{
			// 00001000000042@hostname@12345@Monitortool version
			// 7.3123@username@email
			Client cl = workAnmeldung(tp);

			if (swork.checkTaskHoleEas(cl) == true)
			{
				metaname_glob = swork.taskHoleEas(tcpprot, socket, cl);

				Tracer.WriteTrace(20, "server send request for metatrader <"
						+ hostname_glob + "___" + serial_glob + "|"
						+ metaname_glob + ">");
				binaerflag = 1;
				return (0);// alles ok
			} // falls es Zeit ist die Tradeliste zu holen
			else if (swork.checkTaskTradeliste(cl) == false)
			{
				Tracer.WriteTrace(20, "server send request for tradelist <"
						+ hostname_glob + "___" + serial_glob + ">");
				swork.taskHoleTradeliste(tcpprot, socket, cl);

				return 0;
			} else if (swork.checkUpdate(tp, hostname_glob, serial_glob) == true)
			{
				Tracer.WriteTrace(20, "server send new update to client<"
						+ hostname_glob + "___" + serial_glob + ">");
				swork.taskSendUpdate(tp, tcpprot, socket, cl, hostname_glob,
						serial_glob);

				// Ein EndePacket schicken
				swork.taskEndCommunication(tcpprot, socket, cl);

				return 0;
			} else
			{ // es ist nix zu tun
				Tracer.WriteTrace(20, "server nothing to to for client <"
						+ hostname_glob + "___" + serial_glob + ">");
				swork.taskEndCommunication(tcpprot, socket, cl);

			}
			return 0;
		}
		// die tradeanforderung kommt rein
		else if (tp.getCmd() == 5)
		{
			// 00005000000000
			TPacket tps = new TPacket();
			tps.setCmd(5);
			tps.setDatalen(2);
			tps.setData("ok");
			// ok senden
			// out("0000500000002ok");
			tcpprot.TcpSendeNachricht(socket, tps, null,1);
			binaerflag = 1;
			return 0;

		} else if ((tp.getCmd() == 10) && (binaerflag == 1))
		{

			// die tradeliste wurde empfangen dann speichern
			String fzipna = ConfigServer.getTradelistenDir() + "\\"
					+ username_glob + "___" + hostname_glob + "___"
					+ serial_glob + ".zip";
			//verschlüsseltes file
			String fzipnacr = ConfigServer.getTradelistenDir() + "\\"
					+ username_glob + "___" + hostname_glob + "___"
					+ serial_glob + ".zip.cr";
			
			tcpprot.WriteFile(tp, fzipnacr);
			
			//Entschlüsseln
			Crypto crypto= new Crypto();
			crypto.setProvider();
			try
			{
				crypto.decryptFile(fzipnacr, fzipna, "f15q8t93");
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			File crfile=new File(fzipnacr);
			if(crfile.exists())
				crfile.delete();
			
			Tracer.WriteTrace(20, "server got new tradelist<" + hostname_glob
					+ "___" + serial_glob + "> --> store file <" + fzipna + ">");
			// ok melden
			// out("00011000000002ok");
			TPacket tps = new TPacket();
			tps.setCmd(11);
			tps.setDatalen(2);
			tps.setData("ok");
			// ok senden
			// out("0001100000002ok");
			tcpprot.TcpSendeNachricht(socket, tps, null,1);
			binaerflag = 00;
			return 99;
		} else if ((tp.getCmd() == 13) && (binaerflag == 1))
		{
			String kennung = username_glob + "___" + hostname_glob + "___"
					+ serial_glob + "___" + metaname_glob + "___";

			// die metatraderdatei empfangen und speichern
			String fzipna = ConfigServer.getEaDir() + "\\" + kennung + ".zip";
			String fzipnacr = ConfigServer.getEaDir() + "\\" + kennung + ".zip.cr";
			String fzipnadrop = ConfigServer.getExportDir() + "\\" + kennung
					+ ".zip";
			String steuercommandStart = ConfigServer.getCmdDir() + "\\"
					+ kennung + ".start";
			String steuercommandOk = ConfigServer.getCmdDir() + "\\" + kennung
					+ ".ok";

			// in den internen speicher speichern
			tcpprot.WriteFile(tp, fzipnacr);

			//Entschlüsseln
			Crypto crypto= new Crypto();
			crypto.setProvider();
			try
			{
				crypto.decryptFile(fzipnacr, fzipna, "f15q8t93");
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//das verschlüsselte löschen
			File crfile=new File(fzipnacr);
			if(crfile.exists())
				crfile.delete();
			
			//das entschlüsselte in der Dropbox kopieren
			FileAccessDyn fdyn=new FileAccessDyn();
			fdyn.copyFile2(fzipna, fzipnadrop);
			
			Tracer.WriteTrace(20, "server got new metatrader<" + hostname_glob
					+ "___" + serial_glob + "> --> store file <" + fzipna + ">");

			// das steuerkommando umbenennen
			File f1 = new File(steuercommandStart);
			f1.renameTo(new File(steuercommandOk));

			// ok melden
			// out("00011000000002ok");
			TPacket tps = new TPacket();
			// sende als 14 ok zurück da 10 und 13 für binär reserviert sind
			tps.setCmd(14);
			tps.setDatalen(2);
			tps.setData("ok");
			// ok senden
			// out("0001300000002ok");
			tcpprot.TcpSendeNachricht(socket, tps, null,1);
			binaerflag = 00;
			return 99;
		} else
			return 99;
	}

	private Client workAnmeldung(TPacket tp)
	{
		// ein neuer client hat sich angemdeldet
		// "0001datenlänge@hostname@12345@Monitortool version 7.3123"
		//
		if (tp.getCmd() == 1)
		{
			// das datenpacket auswerten
			Tracer.WriteTrace(20,
					"I: New Monitorclient connected <" + tp.getData() + ">");

			if(tp.getData().contains("@")==false)
			{
				Tracer.WriteTrace(20, "Not expected packet @ is missing --> close connection");
				return null;
			}
			String[] splittArray = tp.getData().split("@");
			hostname_glob = splittArray[0];
			serial_glob = SG.get_zahl(splittArray[1]);
			String versionsstring = splittArray[2];
			username_glob = splittArray[3];
			email_glob = splittArray[4];

			// den client in der datenbasis suchen
			Client cl = clist_glob.getClient(username_glob, hostname_glob,
					serial_glob);
			// falls der Client noch nicht in der Datenbasis, dann lege an
			if (cl == null)
			{

				Client clx = new Client();
				clx.setClientname(hostname_glob);
				clx.setSerial(serial_glob);
				clx.setVersionsnummer(versionsstring);
				clx.setLastloaded(Mondate.getAktDate());
				clx.setUsername(username_glob);
				clx.setEmail(email_glob);
				clist_glob.addClient(clx);
				// die clientlist speichern
				clist_glob.store(0);
				return clx;
			}
			return cl;
		}
		return null;
	}

}
