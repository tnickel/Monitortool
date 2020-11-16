package network;

import hiflsklasse.Archive;
import hiflsklasse.Inf;
import hiflsklasse.Sys;
import hiflsklasse.Tools;
import hiflsklasse.Tracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import tcp.TPacket;
import tcp.TcpProtokoll;
import Metriklibs.FileAccessDyn;
import StartFrame.Brokerview;
import Sync.LockTradeliste;
import data.GlobalVar;
import data.Lic;
import data.Metaconfig;
import data.Rootpath;

public class MonitorClient
{
	/*
	 * Im 'worker' des Hauptprogramms wird wie folgt verfahren: o Bilde Instanz
	 * von 'FutureTask', gib ihr als Parameter eine Instanz von 'ClientHandler'
	 * mit, die das Interface 'Callable' (ähnlich 'Runnable') implementiert. o
	 * Übergib die 'FutureTask' an einen neuen Thread und starte diesen. Im
	 * Thread wird nun die 'call'-Methode aus dem Interface 'Callable' des
	 * ClientHandlers abgearbeitet. o Dabei wird die komplette Kommunikation mit
	 * dem Server durchgeführt. Die 'call'-Methode gibt nun das Ergebnis vom
	 * Server an die 'FutureTask' zurück, wo es im Hauptprogramm zur Verfügung
	 * steht. Hier kann beliebig oft und an beliebigen Stellen abgefragt werden,
	 * ob das Ergebnis bereits vorliegt.
	 */
	String werte;

	public MonitorClient()
	{
	}

	public static void main(String[] args)
	{

		if (args.length == 0)
		{
			System.out.println("Datum-Parameter fehlen !");
			System.exit(1);
		}

		if ((GlobalVar.getUsername() == null)
				|| (GlobalVar.getUsername().length() < 2))
		{
			GlobalVar.setIpmessage("in config");
			return;
		}

		if (GlobalVar.getIpthreadflag() == 1)
		{
			// Mbox.Infobox("Ipthread already running, do nothing");
			return;
		}
		GlobalVar.setIpthreadflag(1);

		StringBuffer sb = new StringBuffer();
		// alle Parameter zusammenfassen, getrennt durch Leerzeichen
		for (int i = 0; i < args.length; i++)
		{
			sb.append(args[i] + ' ');
		}
		String werte = sb.toString().trim();

		/* try to connect to server
		try
		{
			Thread t1 = new Thread(new MonitorClientThread());
			t1.start();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
	}

	void worker() throws Exception
	{
		System.out.println("worker:" + Thread.currentThread());
		// Klasse die 'Callable' implementiert
		ClientHandler ch = new ClientHandler("werte");

		// kennung des Montortools setzen

		int j = 0;
		// call-Methode 'ch' von ClientHandler wird mit 'FutureTask'
		// asynchron
		// abgearbeitet, das Ergebnis kann dann von der 'FutureTask'
		// abgeholt
		// werden.
		FutureTask<String> ft = new FutureTask<String>(ch);
		Thread tft = new Thread(ft);
		tft.start();

		// prüfe ob der Thread seine Arbeit getan hat
		while (!ft.isDone())
		{
			j++; // zähle die Thread-Wechsel
			Thread.yield(); // andere Threads (AndererThread) können
							// drankommen
			if (GlobalVar.getIpthreadflag() == 0)
			{
				Tracer.WriteTrace(20, "I: ipthradflag is off");
				break;
			}
			Thread.sleep(1000);
			// System.out.println("worker ip thread running");
		}
		System.out.println("all Done:" + j);

		ft.cancel(true);
		System.out.println("threadnamen ausgeben");
		System.out.println(Thread.currentThread().getName());
		GlobalVar.setIpthreadflag(0);
	}
}

// Enthält die call-Methode für die FutureTask (entspricht run eines Threads)
class ClientHandler implements Callable<String>
{
	String ip1 = GlobalVar.getServerip1();// "85.214.73.81"; // Server Strato2
	String ip2 = GlobalVar.getServerip2();
	int port = 8080;
	String werte;

	public ClientHandler(String wertex)
	{
		werte = wertex;
	}

	public String call() throws Exception
	{ // run the service
		System.out.println("ClientHandler:" + Thread.currentThread());
		// verlängere künstlich die Bearbeitung der Anforderung, um das
		// Wechselspiel
		// der Threads zu verdeutlichen
		Thread.sleep(2000);
		return (RequestServer());
	}

	// Socket öffnen, Anforderung senden, Ergebnis empfangen, Socket schliessen
	String RequestServer() throws IOException
	{
		LockTradeliste lockTradeliste = new LockTradeliste();
		Socket socket = null;
		// das geht immer so, der client stellt anforderung an den Server und
		// der server liefert dann

		TcpProtokoll tcpprot = new TcpProtokoll();
		String zuSendendeNachricht = null;

		try
		{
			// versuche ersten server
			socket = new Socket(ip1, port); // verbindet sich mit Server
			socket.setSoTimeout(15000);

		} catch (Exception e)
		{
			// versuche den zweiten server
			try
			{
				Tracer.WriteTrace(20, "connect exception1 ip<" + ip1 + "> <"
						+ e.getMessage() + ">");
				socket = new Socket(ip2, port); // verbindet sich mit zweiten
												// Server
				socket.setSoTimeout(15000);
			} catch (Exception ex)
			{
				Tracer.WriteTrace(20, "connect exception2 ip<" + ip2 + "> <"
						+ e.getMessage() + ">");
				GlobalVar.setIpmessage("no serverconnection");
				return "no socket";
			}
		}

		try
		{
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			TPacket tps = new TPacket();

			// 1) Sende Kennung
			zuSendendeNachricht = Sys.getHostname() + "@"
					+ GlobalVar.getSernumber() + "@"
					+ GlobalVar.calcVersionstring() + "@"
					+ GlobalVar.getUsername() + "@"
					+ GlobalVar.getEmail().replace("@", "#") + "@"
					+ GlobalVar.getUpdatechannel() + "@"
					+ Updater.GetVersionFilelength();
			tps.setCmd(1);
			tps.setDatalen(zuSendendeNachricht.length());
			tps.setData(zuSendendeNachricht);
			// Anforderung senden
			tcpprot.TcpSendeNachricht(socket, tps, null, 0);

			// Ergebnis empfangen
			TPacket tpr = tcpprot.ReadPacket(inputStream, socket, 0);
			{
				if (tpr == null)
				{
					Tracer.WriteTrace(20, "error: socket connection error");
					GlobalVar.setIpmessage("no connection to server");
					socket.close();
				}
			}

			Tracer.WriteTrace(20, "es wurde empfangen1<" + tpr.getCmdstring()
					+ ">");

			// 2) server hat gemeldet ok,und es wird die Tradeliste gefordert
			String rueckmeldung = tpr.getData();
			if ((tpr.getCmd() == 1) && (rueckmeldung.contains("ok@sendTrades")))
			{
				// sende die Trades zum Server

				lockTradeliste.waitTradelisteReady();
				protokollSendTrades(socket, tps, tcpprot, tpr, inputStream);
				if (Lic.getlic() != 0)
					GlobalVar.setIpmessage("all ok " + Tools.get_akttime_str());
				else
					GlobalVar.setIpmessage("all ok ");

			}
			// 3) falls mt, vom server gefordert wurde
			else if ((tpr.getCmd() == 1)
					&& (tpr.getData().contains("ok@sendMt")))
			{
				// es kam ja die anfrage "ok@sendMt@Thinkforex8"
				// sende die Mt zum Server
				protokollSendMetatrader(socket, tps, tcpprot, tpr, inputStream);
				if (Lic.getlic() != 0)
					GlobalVar.setIpmessage("all ok " + Tools.get_akttime_str());
				else
					GlobalVar.setIpmessage("all ok ");

			}
			// 4) falls ein update ansteht
			else if (tpr.getCmd() == 15)
			{
				protokollWerteUpdateAus(socket, tps, tcpprot, tpr, inputStream);
				GlobalVar.setIpmessage("got update please restart");
				socket.close();

			} else if ((tpr.getCmd() == 1)
					&& (tpr.getData().contains("ok@end")))
			{
				// es gibt nix zu tun
				if (Lic.getlic() != 0)
					GlobalVar.setIpmessage("all ok " + Tools.get_akttime_str());
				else
					GlobalVar.setIpmessage("all ok ");
				socket.close();
			}

		} catch (Exception e)
		{
			GlobalVar.setIpmessage("no connection to server");
			System.out.println("exception keine socket verbindung"
					+ e.getMessage());
		}
		return ("ok");
	}

	private void protokollWerteUpdateAus(Socket socket, TPacket tps,
			TcpProtokoll tcpprot, TPacket tpr, BufferedReader inputStream)
	{
		Tracer.WriteTrace(20,
				"I:es wurde update empfangen<" + tpr.getCmdstring() + ">");
		// das update auf platte speichern

		String fzipna = Rootpath.getRootpath()
				+ "\\bin\\monitortool_update.jar";

		tcpprot.WriteFile(tpr, fzipna);
		Tracer.WriteTrace(20, "I:speichere update <" + fzipna + ">");
		Tracer.WriteTrace(20, "I:update wurde gespeichert<" + fzipna + ">");

	}

	private void protokollSendTrades(Socket socket, TPacket tps,
			TcpProtokoll tcpprot, TPacket tpr, BufferedReader inputStream)
	{
		Tracer.WriteTrace(20, "I:es wurde cmd-empfangen<" + tpr.getCmdstring()
				+ ">");

		// 3) sende die Tradeanforderung
		String zuSendendeNachricht = "SendTrades";
		tps.setCmd(5);
		tps.setDatalen(zuSendendeNachricht.length());
		tps.setData(zuSendendeNachricht);
		tcpprot.TcpSendeNachricht(socket, tps, null, 0);

		// 4) auf das ok warten von send Trades warten
		tpr = tcpprot.ReadPacket(inputStream, socket, 0);

		if ((tpr.getCmd() == 5) && (tpr.getData().contains("ok")))
		{
			// das datenfile mit vorgestellten header senden
			tps.setCmd(10);
			tps.setData("");

			String datenfilenamecr = Rootpath.getRootpath()
					+ "\\data\\tra.txt.zip.cr";

			// senden
			tcpprot.TcpSendeNachricht(socket, tps, datenfilenamecr, 0);

			Tracer.WriteTrace(20, "I:cmd 05 alles ok");
		}
		// 5) rückmeldung abwarten
		tpr = tcpprot.ReadPacket(inputStream, socket, 0);

		if ((tpr.getCmd() == 11) && (tpr.getData().contains("ok")))
			;
		System.out.println("I:cmd 11 alles ok");

	}

	private void protokollSendMetatrader(Socket socket, TPacket tps,
			TcpProtokoll tcpprot, TPacket tpr, BufferedReader inputStream)
	{
		Tracer.WriteTrace(20, "es wurde empfangen2<" + tpr.getCmdstring() + ">");

		// 3) sende die Metatrader
		// es wurde empfangen "ok@sendMt@Pepperstone25"
		String[] splittArray = tpr.getData().split("@");
		String brokername = splittArray[2];

		// das metatraderverzeichniss zippen und ins zipfile ablegen
		// das datenfile mit vorgestellten header senden
		tps.setCmd(13);
		tps.setData("");

		// erst mal das Metatraderverzeichniss zippen
		packeMetatrader(brokername);

		tcpprot.TcpSendeNachricht(socket, tps, Rootpath.getRootpath()
				+ "\\data\\met.zip.cr", 0);

		File metcr = new File(Rootpath.getRootpath() + "\\data\\met.zip.cr");
		// das gesendete met wieder löschen
		metcr.delete();

		Tracer.WriteTrace(20, "I:me snd alles ok");

		// 4) rückmeldung abwarten
		tpr = tcpprot.ReadPacket(inputStream, socket, 0);

		if ((tpr.getCmd() == 13) && (tpr.getData().contains("ok")))
			;
		System.out.println("I:cmd 13 alles ok");
	}

	private void packeMetatrader(String brokername)
	{
		Metaconfig me = null;

		// erst mal das Metatraderverzeichniss suchen
		Brokerview bv = new Brokerview();
		bv.LoadBrokerTable();

		if (brokername.contains("CHECKALLSYSTEM") == false)
		{
			// Nur einen Broker zippen
			me = bv.getMetaconfigByBrokername(brokername);
			// dann einen metatrader zippen
			zipMetatrader(me, Rootpath.getRootpath() + "\\data", "met.zip");
		} else
		{
			// Alle Broker zippen
			zipAllMetatrader(bv, Rootpath.getRootpath() + "\\data", "met.zip");
		}
	}

	private void zipMetatrader(Metaconfig me, String zipverz, String zipfilename)
	{
		ArrayList<String> sourcesFilenames = new ArrayList<String>();

		// falls metatrader nicht vorhanden
		if (me == null)
		{
			Inf inf = new Inf();
			String fnam = Rootpath.getRootpath() + "\\data\\met.txt";
			inf.setFilename(fnam);
			inf.writezeile("not available");
			inf.close();
			sourcesFilenames.add(fnam);
		} else
		{// falls verzeichnisse da sind
			buildMetatraderFileliste(me, sourcesFilenames);
		}
		// Schritt2: dann alles zippen
		Archive.generateZipFile2(sourcesFilenames, zipverz, zipfilename);

		// Schritt3: dann verschlüsseln
		Crypto crypto = new Crypto();
		crypto.setProvider();
		try
		{
			crypto.encryptFile(zipverz + "\\" + zipfilename, zipverz + "\\"
					+ zipfilename + ".cr", "f15q8t93");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (me == null)
		{ // den dreck wieder wegmachen
			File fmettxt = new File(Rootpath.getRootpath() + "\\data\\met.txt");
			if (fmettxt.exists())
				fmettxt.delete();
		}

		return;
	}

	private void buildMetatraderFileliste(Metaconfig me,
			ArrayList<String> sourcesFilenames)
	{
		// sammelt alle files für einen Metatrader in einer Liste

		String mqldata = me.getMqldata();
		String expertdata = me.getExpertdata();
		String appdata = me.getAppdata();

		// sammle die experten
		addZipfilesListe(expertdata, ".mq4", sourcesFilenames);

		// sammle die indikatoren
		addZipfilesListe(mqldata + "\\indicators", ".mq4", sourcesFilenames);

		// sammle die profiles
		addZipfilesListe(appdata + "\\profiles\\default", ".chr",
				sourcesFilenames);

		// sammle die txt-files(origin.txt)
		addZipfilesListe(appdata, ".txt", sourcesFilenames);

		// sammle die dlls
		addZipfilesListe(mqldata + "\\libraries", ".mq4", sourcesFilenames);
		addZipfilesListe(mqldata + "\\libraries", ".dll", sourcesFilenames);
		addZipfilesListe(mqldata + "\\libraries", ".ex4", sourcesFilenames);

		// sammle die includes
		addZipfilesListe(mqldata + "\\include", ".mqh", sourcesFilenames);
	}

	private void zipAllMetatrader(Brokerview bv, String zipverz,
			String zipfilename)
	{
		ArrayList<String> sourcesFilenames = new ArrayList<String>();
		int anz = bv.getAnz();
		Metaconfig me = null;
		// gehe durch alle metatrader
		for (int i = 0; i < anz; i++)
		{
			me = bv.getElem(i);

			// falls metatrader nicht vorhanden
			if (me == null)
			{
				Inf inf = new Inf();
				String fnam = Rootpath.getRootpath() + "\\data\\met.txt";
				inf.setFilename(fnam);
				inf.writezeile("not available");
				inf.close();
				sourcesFilenames.add(fnam);
			} else
			{// falls verzeichnisse da sind
				if (me.getDesciption().contains("nosend") == true)
					continue;
				buildMetatraderFileliste(me, sourcesFilenames);
			}
		}
		// Schritt2: dann alles zippen
		Archive.generateZipFile2(sourcesFilenames, zipverz, zipfilename);

		// Schritt3: dann verschlüsseln
		Crypto crypto = new Crypto();
		crypto.setProvider();
		try
		{
			crypto.encryptFile(zipverz + "\\" + zipfilename, zipverz + "\\"
					+ zipfilename + ".cr", "f15q8t93");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (me == null)
		{ // den dreck wieder wegmachen
			File fmettxt = new File(Rootpath.getRootpath() + "\\data\\met.txt");
			if (fmettxt.exists())
				fmettxt.delete();
		}

		return;
	}

	private void addZipfilesListe(String quellverz, String endung,
			ArrayList<String> sourcesFilenames)
	{
		// initialisiere das Verzeichniss was gezippt werden soll
		FileAccessDyn fad = new FileAccessDyn();
		fad.initFileSystemList(quellverz, 1);
		int anz = fad.holeFileAnz();

		// Schritt 1: erst alle Files in einer liste sammeln
		for (int i = 0; i < anz; i++)
		{
			File fnam = new File(quellverz + "\\" + fad.holeFileSystemName());
			if (fnam.getName().endsWith(endung) == true)
			{
				// nimm die mq4 quellnamen in einer liste auf
				sourcesFilenames.add(fnam.getAbsolutePath());
			}
		}
	}

	void schreibeTextNachricht(Socket socket, String nachricht)
			throws IOException
	{
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		printWriter.print(nachricht);
		printWriter.flush();
	}

	String leseNachricht(Socket socket) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[100];
		// blockiert bis Nachricht empfangen
		int anzahlZeichen = bufferedReader.read(buffer, 0, 100);
		String nachricht = new String(buffer, 0, anzahlZeichen);
		return nachricht;
	}

}

class MonitorClientThread implements Runnable
{
	public void run()
	{
		MonitorClient monitorClient = new MonitorClient();
		try
		{

			monitorClient.worker();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}