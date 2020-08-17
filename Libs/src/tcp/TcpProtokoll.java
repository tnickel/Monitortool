package tcp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.JLibsProgressWin;
import hiflsklasse.SG;
import hiflsklasse.Tracer;

public class TcpProtokoll
{
	// dies ist die tcp klasse
	int var = 0;

	// int port = 3141;
	/*
	 * Packete um den Server zu simmulieren (fall a, ich möchte nur die
	 * Tradeliste) 00001000000013ok@sendTrades 00005000000002ok 00011000000002ok
	 * 
	 * Fall b) ich möchte die ea´s 00001000000022ok@sendEas@ThinkForex8
	 * 00005000000002ok 00011000000002ok
	 * 
	 * Packete um den Monitorclient zu simmulieren
	 * 00001000000035tnickel-PC@310@Forex Monitor V0.390@tnickel@tnickel#gmx.de
	 * 00005000000010SendTrades
	 */

	public TPacket ReadPacket(BufferedReader bufferedReader, Socket socket,int logflag)
	{
		// packetaufbau
		// kommando+datalen+data
		// 0000100000000512345
		// 00001000000000

		char[] cmdbuffer = new char[5];
		char[] lenbuffer = new char[9];
		// das tpacket über tcp lesen und aufbauen
		TPacket tp = new TPacket();

		int anzahlZeichen = 0;
		try
		{
			// das 5-bytes kommando lesen
			// cmdbuffer=leseBuffer(bufferedReader,5);
			anzahlZeichen = bufferedReader.read(cmdbuffer, 0, 5);
			if (anzahlZeichen < 5)
			{
				Tracer.WriteTrace(20,
						"error: commandlen should be 5 but I got<"
								+ anzahlZeichen + ">");
				return null;
			}
			String cmdnachricht = new String(cmdbuffer, 0, 5);
			tp.setCmdstring(cmdnachricht);
			tp.setCmd(SG.get_zahl(cmdnachricht));
			if(logflag==1)
				Tracer.WriteTrace(20, "I: got command=" + cmdnachricht);

			if (SG.get_zahl(cmdnachricht) > 20)
			{
				Tracer.WriteTrace(20, "illegal command <" + cmdnachricht + ">");
				return null;
			}

			// die 9-byte laenge des datenpaketes lesen
			anzahlZeichen = bufferedReader.read(lenbuffer, 0, 9);
			if (anzahlZeichen < 9)
			{
				Tracer.WriteTrace(20, "Tcp error dalalen<9 I got len<"+anzahlZeichen+"> ");
				return null;
			}
			String lennachricht = new String(lenbuffer, 0, 9);
			if(logflag==1)
				Tracer.WriteTrace(20, "I: got messagelength=" + lennachricht);
			tp.setDatalen(SG.get_zahl(lennachricht));

			// das Datenpaket einlesen falls lennachricht >0
			int datalen = tp.getDatalen();
			if (datalen > 0)
			{
				if ((tp.getCmd() != 10) && (tp.getCmd() != 13)
						&& (tp.getCmd() != 15))
				{
					// falls es ein textdatenpacket ist
					char[] databuffer = new char[datalen + 100];
					anzahlZeichen = bufferedReader.read(databuffer, 0, datalen);
					String datanachricht = new String(databuffer, 0,
							anzahlZeichen);
					if(logflag==1)
					Tracer.WriteTrace(20, "I: got message " + datanachricht);

					tp.setData(datanachricht);
				} else
				// cmd ==10,13,15
				{ // falls ein dateipacket
					// falls kommando ==10 dann lese das Packet binär
					// das datenpacket wird nur eingelesen
					// die speicherung findet später statt
					// 10 = trades, 15=update, 13=eas
					int progressflag = 0;
					String loopmessage = "";
					if (tp.getCmd() == 15)
					{
						progressflag = 1;
						loopmessage = "update";
					} else if (tp.getCmd() == 10)
						loopmessage = "get trades";
					else if (tp.getCmd() == 13)
						loopmessage = "get eas";

					receiveFile(tp, socket, tp.getDatalen(), progressflag,
							loopmessage);
				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tp;
	}

	private char[] leseBuffer_dep(BufferedReader bufferedReader, int minanz)
	{
		char[] buffer = new char[minanz];
		int anzahlZeichen = 0;

		while (5 == 5)
		{
			try
			{
				anzahlZeichen = bufferedReader.read(buffer, 0, 5);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Tracer.WriteTrace(20, "error 50103 <" + e.getMessage() + ">");
				return null;
			}

			// falls fehler
			if (anzahlZeichen <= 0)
				return null;

			if (anzahlZeichen == minanz)
				return buffer;
			else
				return null;

		}
	}

	public void TcpSendeNachricht(Socket socket, TPacket tpacket,
			String filename,int logflag)
	{
		// sende kommando
		// sende länge
		// sende nachricht
		

		PrintWriter printWriter;
		try
		{
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			// das kommando in den puffer, das commando hat immer länge 5
			printWriter
					.print(tpacket.calcCommandLengthstring(tpacket.getCmd()));

			// falls es ein Textdatenpacket ist
			if (filename == null)
			{
				// die datenlänge in den puffer
				printWriter
						.print(tpacket.calcLengthstring(tpacket.getDatalen()));
			} else
			// falls es ein filedatenpacket ist
			{
				File fnamf = new File(filename);
				// die datenlänge für das file bestimmen
				tpacket.setDatalen((int) (fnamf.length()));
				// die datenlänge in den Puffer
				printWriter
						.print(tpacket.calcLengthstring(tpacket.getDatalen()));

			}
			// die daten noch dranpacken

			// falls filedaten da sind
			if (filename != null)
			{
				// die längeninformation schon mal schicken
				printWriter.flush();
				// die daten binär senden
				sendFile(filename, socket,logflag);
				
				
			} else if (tpacket.getDatalen() > 0)
			{

				printWriter.print(tpacket.getData());

			}
			printWriter.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	public void receiveFile(TPacket tp, Socket socket, int datalen,
			int progressflag, String message)
	{
		// liest und speichert gleich das datenpacket was über inernet kommt
		int bytescnt = 0;
		int dataSize = 0;
		JLibsProgressWin prog = null;
		byte[] mybytearray = new byte[tp.getDatalen()];

		if (progressflag == 1)
			prog = new JLibsProgressWin(
					"receive Monitortool update do not stop", 0, datalen);

		try
		{
			{
				while ((dataSize = socket.getInputStream().read(mybytearray,
						bytescnt, (mybytearray.length - bytescnt))) > 0)
				{
					bytescnt = bytescnt + dataSize;
					System.out.println(message + "count<" + bytescnt + ">");
					if (progressflag == 1)
						prog.update(bytescnt);

					if (bytescnt >= tp.getDatalen())
						break;
				}
				if (progressflag == 1)
					prog.end();
				tp.setBytearray(mybytearray);
			}
		} catch (FileNotFoundException e)
		{
			if (progressflag == 1)
				prog.end();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			if (progressflag == 1)
				prog.end();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (progressflag == 1)
			prog.end();
	}

	public void WriteFile(TPacket tp, String fzipna)
	{
		// schreibt das datenpaket als file
		File f = new File(fzipna);
		if (f.exists())
			f.delete();

		try
		{
			FileOutputStream fos = new FileOutputStream(fzipna);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			bos.write(tp.getBytearray(), 0, tp.getDatalen());
			bos.flush();
			bos.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ReceiveStoreFile_dep(TPacket tp, Socket socket, String fzipna)
	{
		// liest und speichert gleich das datenpacket was über inernet kommt
		int bytescnt = 0;

		File fna = new File(fzipna);
		if (fna.exists() == true)
			fna.delete();

		int dataSize = 0;
		byte[] mybytearray = new byte[tp.getDatalen()];

		try
		{
			if (fna.createNewFile())
			{
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(fzipna));
				while ((dataSize = socket.getInputStream().read(mybytearray, 0,
						mybytearray.length - bytescnt)) > 0)
				{
					bytescnt = bytescnt + dataSize;
					bos.write(mybytearray, 0, dataSize);
					if (bytescnt >= tp.getDatalen())
						break;
				}
				bos.close();
			}
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

	private void sendFile(String filename, Socket socket,int logflag)
	{
		// Rootpath.getRootpath()+"\\data\\tra.txt.zip"

		try
		{

			File myFile = new File(filename);

			byte[] buffer = new byte[16384];
			InputStream inputStream = new FileInputStream(myFile);
			OutputStream outputStream = socket.getOutputStream();
			int len = 0, geslen = 0;
			while ((len = inputStream.read(buffer)) > 0)
			{
				geslen = geslen + len;
				outputStream.write(buffer, 0, len);
				outputStream.flush();
			}
			if(logflag==1)
				Tracer.WriteTrace(20, "sended updatefile len<" + geslen
					+ "> to client ready");
			outputStream.flush();
			inputStream.close();

		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
