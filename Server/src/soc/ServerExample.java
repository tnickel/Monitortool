package soc;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import netw.ServerNetworkService;
import data.GlobServer;

abstract public class ServerExample
{
	private static ExecutorService pool = null;
	private static ServerSocket serverSocket = null;
	private static Thread t1 = null;

	/*
	 * - Server benutzt Port 3141, liefert zu einem Datum selbiges mit dem
	 * Wochentag an den Client - jede Client-Anforderung wird von einem Thread
	 * des erzeugten Thread-Pools behandelt - Server-Socket kann mit Strg+C
	 * geschlossen werden oder vom Client mit dem Wert 'Exit'.
	 */

	public static void stopServer()
	{
		pool.shutdown();
		try
		{
			System.out.println("run");
			// warte maximal 4 Sekunden auf Beendigung aller
			// Anforderungen
			pool.awaitTermination(4L, TimeUnit.SECONDS);
			if (!serverSocket.isClosed())
			{
				System.out.println("ServerSocket close");
				serverSocket.close();
			}
		} catch (IOException e)
		{
		} catch (InterruptedException ei)
		{
		}

	}

	public static void main(String[] args) throws IOException
	{

		GlobServer.setServerport(8080);
		String var = "C";
		String zusatz;
		if (args != null)
			if (args.length > 0)
				var = args[0].toUpperCase();
		if (var == "C")
		{
			// Liefert einen Thread-Pool, dem bei Bedarf neue Threads
			// hinzugefügt
			// werden. Vorrangig werden jedoch vorhandene freie Threads benutzt.
			pool = Executors.newCachedThreadPool();
			zusatz = "CachedThreadPool";
		} else
		{
			int poolSize = 4;
			// Liefert einen Thread-Pool für maximal poolSize Threads
			pool = Executors.newFixedThreadPool(poolSize);
			zusatz = "poolsize=" + poolSize;
		}
		serverSocket = new ServerSocket(GlobServer.getServerport());
		// Thread zur Behandlung der Client-Server-Kommunikation, der Thread-
		// Parameter liefert das Runnable-Interface (also die run-Methode für
		// t1).
		t1 = new Thread(new ServerNetworkService(pool, serverSocket));
		System.out.println("Start NetworkService(Multiplikation), " + zusatz
				+ ", Thread: " + Thread.currentThread());
		// Start der run-Methode von NetworkService: warten auf Client-request
		t1.start();
		System.out.println("run1");
		//
		// reagiert auf Strg+C, der Thread(Parameter) darf nicht gestartet sein

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				pool.shutdown(); // keine Annahme von neuen Anforderungen
				try
				{

					// warte maximal 4 Sekunden auf Beendigung aller
					// Anforderungen
					pool.awaitTermination(4L, TimeUnit.SECONDS);
					if (!serverSocket.isClosed())
					{

						serverSocket.close();
					}
				} catch (IOException e)
				{
				} catch (InterruptedException ei)
				{
				}
			}
		});
		System.out.println("run2");
		//
	}

	private void Auswertung()
	{
	}

	String getWday(String s)
	{ // Datum mit Wochentag
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy");
		String res = "";
		try
		{
			// Parameter ist vom Typ Date
			res = sdf.format(DateFormat.getDateInstance().parse(s));
		} catch (ParseException p)
		{
		}
		return res;
	}
}
