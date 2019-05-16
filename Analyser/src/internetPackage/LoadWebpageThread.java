package internetPackage;

import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Inf;
import hilfsklasse.ToolsDyn;
import hilfsklasse.Tracer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;

import mainPackage.GC;

public class LoadWebpageThread extends Thread
{
	private BufferedWriter out = null;

	private String urlname1 = null;
	private String safefilename1 = null;
	private int umbruchflag1 = 0;
	private int timeout1 = 0;
	private int threadpos1 = 0;
	private int forceflag1 = 0;
	private int showflag1 = 0;
	private Timer timer1 = null;
	private int ret = 0;
	// 0: am anfang 1:geladen 10:ladefehler
	public int status = 0;
	public String startzeit = "Waiting for first start";
	private int errorcounter = 0;
	private int onlineflag = 0;

	private FileAccessDyn fa = new FileAccessDyn();
	private ToolsDyn to = new ToolsDyn();

	public LoadWebpageThread()
	{
	}

	// construktor
	public LoadWebpageThread(String url, String filename, int flag, int timeo,
			int threadpos, Timer timer, int forflag, int showflag)
	{
		// System.out.println("construktor LoadWebpageThread ****");
		urlname1 = url;
		safefilename1 = filename;
		umbruchflag1 = flag;
		timeout1 = timeo;
		threadpos1 = threadpos; // errorcounter
		forceflag1 = forflag;
		showflag1 = showflag;
		timer1 = timer;
	}

	public void putOnlineflag(int flag)
	{
		onlineflag = flag;
	}

	private int LoadWebpageFehlerausgang(HttpURLConnection uc)
	{
		if(uc!=null)
			uc.disconnect();
		
		
		
		try
		{
			System.out.println("Sleep 5sec");
			Thread.sleep(5000);
		} catch (InterruptedException e)
		{
			Tracer.WriteTrace(10, "Timerfehler");
		}

		
		
		status = 10;
		return 10;
	}

	private int LoadWebpagePic(String urln, String outfile, int umflag,
			int timeoutx, int showflag)
	{
		URL url;
		URLConnection connection=null;
		try
		{
			status = 0;
			url = new URL(urln);

			connection = url.openConnection();
			InputStream stream = connection.getInputStream();
			BufferedInputStream in = new BufferedInputStream(stream);
			FileOutputStream file = new FileOutputStream(outfile);
			BufferedOutputStream out = new BufferedOutputStream(file);

			int i;

			while ((i = in.read()) != -1)
			{
				out.write(i);
			}
			out.flush();
			out.close();
			stream.close();
			status = 1; // ready

		} catch (MalformedURLException e)
		{
			Tracer.WriteTrace(20, "Error:Webladefehler 1 url<" + urln + ">");
			e.printStackTrace();
			return (LoadWebpageFehlerausgang((HttpURLConnection)connection));// ladefehler

		} catch (IOException e)
		{
			
			if(e.getMessage().contains("Server returned HTTP response code: 500 for URL"))
				Tracer.WriteTrace(10, "Error: WO-Server Error '500 - Internal Server Error' ---> STOP ALL");
			
			Tracer.WriteTrace(20, "Error:Webladefehler 2 url<" + urln + ">");
			e.printStackTrace();
			return (LoadWebpageFehlerausgang((HttpURLConnection)connection));
		}
		return 0;
	}

	private int LoadWebpageHtml(String urln, String f, int umflag,
			int timeoutx, int showflag)
	{
		// LoadWebpage wird vom run() aufgerufen, oder direkt wenn im Singlemode

		// falls umbruchflag ==1 dann wird auch der Zeilenumbruch gespeichert
		// rückgabe 0 ok, seite vom Internet geladen,
		// 1 ok, seite schon vorhanden
		// 10 ladefehler
		// 99 offline-mode

		// Prüfe ob </html> sich in der HTML-Seite befindet, wenn nein dann ist
		// das keine HTML-Seite oder
		// es ist ein Ladefehler aufgetreten.

		int htmlendflag = 0;
		int loadlen = 0;

		java.util.Random random = new java.util.Random();
		int randsleep = random.nextInt(GC.RANDOMSLEEP) + 50; // 50 <= bar < 550

		try
		{
			Thread.currentThread().sleep(randsleep);
		} catch (InterruptedException ie)
		{
			Tracer.WriteTrace(10, "Error: Sleep Interrupt error 05");
		}

		out = fa.WriteFileOpen(f, null);
		if (out == null)
		{
			Tracer.WriteTrace(20, "Warning: kann file<" + f
					+ "> nicht schreiben");
			return (LoadWebpageFehlerausgang(null));
		}
		HttpURLConnection uc = null;
		try
		{
			status = 0;
			URL url = new URL(urln);
			uc = (HttpURLConnection) url.openConnection();
			uc.setReadTimeout(timeoutx);
			uc.setConnectTimeout(timeoutx);

			try
			{
				uc.getContent();
			} catch (FileNotFoundException e0)
			{
				Tracer.WriteTrace(20, "Webfehler 434,File Not found");
				// e0.printStackTrace();
				return (LoadWebpageFehlerausgang(uc));
			} catch (IOException e)
			{
				Inf inf = new Inf();
				inf.appendzeile(GC.rootpath + "\\log\\downloaderror.txt",
						e.getMessage(), true);

				if(e.getMessage().contains("Server returned HTTP response code: 500 for URL"))
					Tracer.WriteTrace(10, "Error: WO-Server Error '500 - Internal Server Error' ---> STOP ALL");

				Tracer.WriteTrace(20,
						"Webladefehler IOException x1 connect timeout ");
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (LoadWebpageFehlerausgang(uc));
			}
			InputStream is = url.openStream();
			Reader urlReader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(urlReader);

			String zeile = "";
			while ((zeile = br.readLine()) != null)
			{
				loadlen = loadlen + zeile.length();
				if (zeile.contains("</html>") == true)
					htmlendflag = 1;

				if (zeile
						.contains("Es ist ein Fehler aufgetreten. Der Seitenbetreiber wurde informiert. Bitte versuchen Sie es") == true)
				{
					Tracer.WriteTrace(20,
							"Warning: Webladefehler Seitenbetreiberfehler, lade neu");
					return (LoadWebpageFehlerausgang(uc));
				}
				
				

				// System.out.println(zeile);
				out.write(zeile);
				if (umflag == 1)
					out.newLine();
			}
			br.close();
			is.close();
			out.close();
			uc.disconnect();

			// prüfe ob </html> geladen wurde
			// Wenn nein Fehlerausgang !!
			if (htmlendflag == 0)
			{
				Tracer.WriteTrace(20,
						"Info:HtlmSeite fehlerhaft </html> missing");
				return (LoadWebpageFehlerausgang(uc));
			}
			if (loadlen < GC.HTMLLENGTHMIN)
			{
				FileAccess.FileDelete(f, 0);
				Tracer.WriteTrace(20, "Info:HtmlSeite fehlerhaft len<"
						+ loadlen + "> zu kurz");
				return (LoadWebpageFehlerausgang(uc));
			}
			if (showflag == 1)
				Tracer.WriteTrace(20, "Webseite korrekt geladen");
			status = 1;

			try
			{
				Thread.currentThread().sleep(GC.DOWNLOADDELAY);
			} catch (InterruptedException ie)
			{
				Tracer.WriteTrace(10, "Error: Sleep Interrupt error 05");
			}

			return (0); // geladen, alles ok

		} catch (MalformedURLException e1)
		{
			Inf inf = new Inf();
			inf.appendzeile(GC.rootpath + "\\log\\downloaderror.txt",
					e1.getMessage(), true);

			Tracer.WriteTrace(20, "Webladefehler x123");

			// TODO Auto-generated catch block
			e1.printStackTrace();
			return (LoadWebpageFehlerausgang(uc));
		} catch (IOException e)
		{
			Inf inf = new Inf();
			inf.appendzeile(GC.rootpath + "\\log\\downloaderror.txt",
					e.getMessage(), true);

			if(e.getMessage().contains("Server returned HTTP response code: 500 for URL"))
				Tracer.WriteTrace(10, "Error: WO-Server Error '500 - Internal Server Error' ---> STOP ALL");
			
			Tracer.WriteTrace(20, "Webladefehler 222 IOException");
			// TODO Auto-generated catch block
			e.printStackTrace();

			try
			{
				if (out != null)
					out.close();

				String ResponseM = uc.getResponseMessage();
				System.out.println("RM=" + ResponseM);

			} catch (IOException e2)
			{

				inf.appendzeile(GC.rootpath + "\\log\\downloaderror.txt",
						e2.getMessage(), true);

				if(e2.getMessage().contains("Server returned HTTP response code: 500 for URL"))
					Tracer.WriteTrace(10, "Error: WO-Server Error '500 - Internal Server Error' ---> STOP ALL");
				
				// TODO Auto-generated catch block
				e2.printStackTrace();
				Tracer.WriteTrace(30, "Error ResponseM");
			}
			return (LoadWebpageFehlerausgang(uc));
		}

	}

	public int LoadWebpage(String urln, String f, int umflag, int timeoutx,
			int forceflag, int showflag)
	{
		// falls showflag =0 wird nix ausgegeben
		if (showflag == 1)
			Tracer.WriteTrace(30, "Lade Webseite URL=<" + urln + "> File <" + f
					+ ">");

		if (onlineflag == 0)
		{
			Tracer.WriteTrace(30, "offlinemode Lade Webpage <" + f
					+ "> threadpos=" + threadpos1);

			return (0);
		}

		if (forceflag == 0)
		{
			if (fa.FileAvailable(f))
			{
				Tracer.WriteTrace(30, "Seite schon vorhanden <" + f + ">");
				return (1); // Seite schon vorhanden
			}
			if (fa.FileAvailable(f))
			{
				if (showflag == 1)
					Tracer.WriteTrace(30, "Seite schon vorhanden <" + f + ">");

				return (1); // Seite schon vorhanden
			}
		}
		// setze Startzeit, nach 10 Minuten wird der Thread gelöscht
		startzeit = new String(to.get_aktdatetime_str());
		Tracer.WriteTrace(40, "Setze Startzeit <" + startzeit + "> für <"
				+ this.getName() + ">");

		if (f.contains(".png") == false)
			ret = LoadWebpageHtml(urln, f, umflag, timeoutx, showflag);
		else
			ret = LoadWebpagePic(urln, f, umflag, timeoutx, showflag);

		// loesche die startzeit da mit dem laden fertig

		Tracer.WriteTrace(40, "Lösche Startzeit da fertig<" + startzeit
				+ "> für <" + this.getName() + ">");
		startzeit = "Beendet";
		return (ret);

		// dann lösche jetzt den tpointer

	}

	@Override
	public void run()
	{
		// Hier ist der Einstiegspunkt für den Thread
		// mit tpointer[threadpos].start(); wird LoadWebpage aufgerufen

		// System.out.println("der run wurde ausgelöst !!");

		while ((status = LoadWebpage(urlname1, safefilename1, umbruchflag1,
				timeout1, forceflag1, showflag1)) == 10)
		{// lade solange fehler
			timeout1 = timeout1 * 4;
			errorcounter++;
			if (errorcounter == 3)
				break;
			Tracer.WriteTrace(20, "errorc=" + errorcounter + "Ladefehler url<"
					+ urlname1 + ">");
		}

		// lösche den Timer für den Prozess, da der Prozess fertig ist
		if (timer1 != null)
		{
			// System.out.println("timer=<"+timer1.toString()+"> wird
			// gelöscht");
			timer1.cancel();
		}
	}
}
