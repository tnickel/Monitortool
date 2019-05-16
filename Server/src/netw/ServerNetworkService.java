package netw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import mtools.Mlist;

public class ServerNetworkService implements Runnable
{
	// Thread bzw. Runnable zur Entgegennahme der Client-Anforderungen

	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	public ServerNetworkService(ExecutorService pool, ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
		this.pool = pool;
	}

	public void run()
	{ // run the service
		try
		{
			// Endlos-Schleife: warte auf Client-Anforderungen
			// Abbruch durch Strg+C oder Client-Anforderung 'Exit',
			// dadurch wird der ServerSocket beendet, was hier zu einer
			// IOException
			// führt und damit zum Ende der run-Methode mit vorheriger
			// Abarbeitung der
			// finally-Klausel.
			while (true)
			{
				/*
				 * Zunächst wird eine Client-Anforderung
				 * entgegengenommen(accept-Methode). Der ExecutorService pool
				 * liefert einen Thread, dessen run-Methode durch die
				 * run-Methode der Handler-Instanz realisiert wird. Dem Handler
				 * werden als Parameter übergeben: der ServerSocket und der
				 * Socket des anfordernden Clients.
				 */
				Socket cs = serverSocket.accept(); // warten auf
													// Client-Anforderung

				// starte den Handler-Thread zur Realisierung der
				// Client-Anforderung
				ServerHandler handler = new ServerHandler(serverSocket, cs);
				pool.execute(handler);
				System.out.println("warte auf client");
			}
		} catch (IOException ex)
		{

		} finally
		{
			System.out.println("--- Ende NetworkService(pool.shutdown)");
			pool.shutdown(); // keine Annahme von neuen Anforderungen
			try
			{
				// warte maximal 300 Sekunden auf Beendigung aller Anforderungen
				pool.awaitTermination(300L, TimeUnit.SECONDS);
				if (!serverSocket.isClosed())
				{
					Mlist.add("socked closed");
					System.out
							.println("--- Ende NetworkService:ServerSocket close");
					serverSocket.close();
				}
			} catch (IOException e)
			{
			} catch (InterruptedException ei)
			{
			}
		}
	}
}
