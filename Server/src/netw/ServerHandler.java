package netw;

import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import tcp.TPacket;
import tcp.TcpProtokoll;

public class ServerHandler implements Runnable
{
	ServerProtokoll prot = new ServerProtokoll();

	// Thread bzw. Runnable zur Realisierung der Client-Anforderungen
	// oder 'extends Thread'
	private final Socket client;
	private final ServerSocket serverSocket;

	ServerHandler(ServerSocket serverSocket, Socket client)
	{ // Server/Client-Socket
		this.client = client;
		this.serverSocket = serverSocket;
	}

	public void run()
	{

		PrintWriter out = null;
		TcpProtokoll tcpprot = new TcpProtokoll();
		try
		{
			int muellzaehler = 0;
			// read and service request on client
			System.out.println("running service, " + Thread.currentThread());

			out = new PrintWriter(client.getOutputStream(), true);
			prot.setOut(out);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(client.getInputStream()));

			while (5 == 5)
			{
				// das packet einlesen
				TPacket tp = tcpprot.ReadPacket(bufferedReader, client,1);

				// falls müllpacket
				if (tp == null)
					break;

				if (tp.getCmdstring().contains("Exit") == true)
				{

					Tracer.WriteTrace(20, "W: Got Server Termination command");
					if (!serverSocket.isClosed())
					{

						Tracer.WriteTrace(20, "Got exit, Server endet");
						try
						{
							serverSocket.close();
							break;
						} catch (IOException e)
						{
						}
					}
				} else
				{ // Auswertung der Antwort vom Server

					int ret = prot.WorkIncomming(tp, bufferedReader, client,
							tcpprot);

					if (ret == 99)
					{
						Tracer.WriteTrace(20, "E:  close connection");
						if (!client.isClosed())
						{
							try
							{
								Tracer.WriteTrace(20,
										"W:Handler:Client transfer ok");
								client.close();
							} catch (IOException e)
							{
							}
						}
					}
				}
			}

		} catch (IOException e)
		{
			System.out.println("IOException, Handler-run" + e.getMessage());
		} finally
		{
			if (!client.isClosed())
			{
				Tracer.WriteTrace(20, "I1:Handler closed");
				try
				{
					Tracer.WriteTrace(20, "I2:Handler closed");
					client.close();
				} catch (IOException e)
				{
				}
			}
		}
	} // Ende run
}
