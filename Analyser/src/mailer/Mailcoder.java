package mailer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import mainPackage.GC;

import java.util.Base64;

public class Mailcoder
{
	// diese Klasse wertet die Mails von Mozilla Firefox aus
	// Hier findet auch die Umwandlung von PDF ins Textformat statt
	String rpath_glob = null;

	Mailcoder(String rootpath)
	{
		rpath_glob = rootpath;
	}

	 public static void schreibePdfDatei(String ausgabefile, Inf eingabefeld) {
	        String line;
	        try (FileOutputStream fos = new FileOutputStream(new File(ausgabefile))) {
	            while ((line = eingabefeld.readZeile()) != null) {
	                if (line.isEmpty()) {
	                    break;
	                }
	                byte[] bytes2 = Base64.getDecoder().decode(line);
	                fos.write(bytes2);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	static private Boolean endMailboxcheck(String line, String boundarray)
	{
		// Was für ein Ende wird hier gechecked ???

		if ((line == null) || (line.length() < 2))
			return false;

		if (line.contains("-----MIME delimiter") == true)
			return true;

		if (boundarray != null)
			if (boundarray.length() > 2)
				if (line.contains(boundarray) == true)
					return true;

		return false;
	}

	static public void schreibeHtmlDatei(String ausgabefile, Inf eingabefeld,
			String line, String boundarray)
	{
		// fnam: ausgabedatei
		// inf: eingabefeld
		// die erste Zeile wird mitgeschrieben

		BufferedWriter bw = null;
		try
		{
			bw = FileAccess.WriteFileOpenAppend(ausgabefile);
			// schreibe auch die erste zeile
			bw.write(line);
			bw.newLine();
			line = eingabefeld.readZeile();

			while ((line = eingabefeld.readZeile()) != null)
			{
				bw.write(line);
				bw.newLine();

				// Hier wird das Ende der Seite gesucht
				if ((line.toUpperCase().contains("</HTML>") == true)
						|| (line.contains(boundarray) == true))
					break;

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

		try
		{
			bw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public void schreibeTxtDatei(String ausgabefile, Inf eingabefeld,
			String trenner, String boundarray)
	{
		// fnam: ausgabedatei
		// inf: eingabefeld

		String line = null;
		BufferedWriter bw = null;
		try
		{
			bw = FileAccess.WriteFileOpenAppend(ausgabefile);
			line = eingabefeld.readZeile();

			while ((line = eingabefeld.readZeile()) != null)
			{
				// Tracer.WriteTrace(20, "Info: betrachte Mailzeile<"+line+">");
				bw.write(line);
				bw.newLine();
				if (endMailboxcheck(line, boundarray) == true)
					break;
				if (trenner.length() > 3)
					if (line.contains(trenner) == true)
						break;
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

		try
		{
			bw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public BStat extraktMailFoulder(String fnam)
	{
		// Hier wird eine ganzes Maildirektory extrahiert.
		// z.B. das 1000er Depot
		// Ex werden alle Texte und pdf´s extrahiert
		// fnam: filename
		// z.B. fnam = AAAInbox

		String quellfile = GC.mailbase + "\\" + fnam;
		String zielpfad = GC.pdfzielbase + "\\" + fnam;
		// Bei Multipart Mails werden boundarrys eingesetzt um die einzelnen
		// Sektionen zu trennen
		String boundarray = null;
		String teiler = "";
		int attachcounter = 1;
		BStat bstat = new BStat();

		String line = null, endung = null;
		String tmpdate = null;
		String tmptime = null;
		Inf inf = new Inf();
		inf.setFilename(quellfile);
		FileAccess.checkgenDirectory(zielpfad);

		Tracer.WriteTrace(20, "Info:extrahiere Mailfile<" + fnam + ">");

		while ((line = inf.readZeile()) != null)
		{
			//System.out.println("line<" + line + "> len<" + line.length() + ">");

			

			// leerzeilen und kurze Zeilen ignoieren
			if (line.length() == 0)
				continue;

			if (line.contains("boundary=\"---MIME") == true)
			{
				// jede Mail muss mindestens einen boundarry haben
				if ((line.contains("boundary=\"---MIME") == false)
						|| (line.contains("\"") == false))
					Tracer.WriteTrace(10, "Mailbox fehlerhaft mailbox<"
							+ quellfile + "> boundary or '' missing line<"
							+ line + ">");

				teiler = new String(line.substring(
						line.indexOf("boundary") + 28, line.lastIndexOf("\"")));
				System.out.println("teiler<" + teiler + ">");
			}
			// type 1 boundary
			if (line.contains("boundary=\"--") == true)
			{
				// Dies ist eine Multipart Mail, somit speichere boundarry
				boundarray = line.substring(line.indexOf("boundary=") + 9, line
						.lastIndexOf(("\"")));
			}
			// type 2 boundary
			if (line.contains("boundary=") == true)
			{
				int firstindex = 0, lastindex = line.length();

				// Dies ist eine Multipart Mail, somit speichere boundarry
				firstindex = line.indexOf("boundary=") + 9;

				// Es gibt unterschiedliche Arten von boundarys, bei der zweiten
				// gibt es keine ""
				if (line.contains("\""))
					lastindex = line.lastIndexOf(("\""));
				boundarray = line.substring(firstindex, lastindex);
			}

			// ermittelt das Datum der Mail
			if ((line.contains("Date:") && (line.contains("+0000")) && (line
					.length() > 30))
					&& (line.length() < 40))
			{
				// Date: Thu, 7 Jan 2010 12:42:04 +0000
				//System.out.println("D_line<" + line + ">");
				attachcounter = 1;
				// pos1:dateanfang
				int pos1 = 1 + line.indexOf(",");
				// pos2:dateende
				int pos2 = 19 + line.substring(19).indexOf(" ");
				// pos3:zeitende
				int pos3 = 19 + line.substring(19).indexOf("+");
				tmpdate = line.substring(pos1, pos2);
				tmpdate = tmpdate.substring(tmpdate.indexOf(" ") + 1);

				tmpdate = Tools.convDatumTextStrich(tmpdate);
				tmptime = line.substring(pos2, pos3);
				tmptime = tmptime.replaceAll(":", "");
				tmptime = tmptime.replaceAll(" ", "");
				continue;
			}

			// Dies ist eine Html-Seite
			//
			if ((line.contains("<!DOCTYPE HTML PUBLIC") == true)
					|| (line.contains("<html>")))
			// if (line.contains("Content-Type: text/html;"))
			{
				String zieldatei = zielpfad + "\\" + tmpdate + "_" + tmptime
						+ "_Htmlmail" + ".html";
				if (FileAccess.FileAvailable(zieldatei) == true)
					continue;
				bstat.setNeueTexte(bstat.getNeueTexte() + 1);
				schreibeHtmlDatei(zieldatei, inf, line, boundarray);
				continue;
			}

			// Textkopf der Mail
			if (line.contains("Content-Type: text/plain;"))
			{
				String zieldatei = zielpfad + "\\" + tmpdate + "_" + tmptime
						+ "_Textbody.txt";

				if (FileAccess.FileAvailable(zieldatei) == true)
					continue;
				bstat.setNeueTexte(bstat.getNeueTexte() + 1);
				schreibeTxtDatei(zieldatei, inf, teiler, boundarray);
				continue;
			}

			// Attachment in dieser Mail
			if (line.contains("Content-Disposition: attachment"))
			{
				// liest die nächste zeile also filename
				if (line.length() < 40)
					line = inf.readZeile();

				int pos1 = line.indexOf("filename=\"") + 10;
				String zieldatei = null;
				String fx = line.substring(pos1);
				// fx ist der filename
				fx = fx.replace("\"", "");

				// Endung bestimmen
				if (fx.contains(".pdf"))
					endung = ".pdf";
				else if (fx.contains(".html"))
					endung = ".html";
				else if (fx.contains(".txt"))
					endung = ".txt";

				// Punkt weg
				fx = fx.replace(".", "");

				// kürze Filenamen auf max 20 Zeichen
				int endindex = fx.length();
				if (endindex > 20)
					endindex = 20;

				zieldatei = zielpfad + "\\" + tmpdate + "_" + tmptime
						+ "_attach" + attachcounter + "_"
						+ fx.substring(0, endindex) + endung;

				// Falls schon da
				if (FileAccess.FileAvailable(zieldatei) == true)
					continue;
				// liest line = inf.readZeile();
				line = inf.readZeile();

				bstat.setNeuePdfs(bstat.getNeuePdfs() + 1);
				attachcounter++;
				System.out.println("extrahiere<" + fx + ">");
				schreibePdfDatei(zieldatei, inf);
				continue;
			}
		}
		return (bstat);
	}

	private static BStat getAllTextPdfs(Display dis, ProgressBar progressBar1)
	{
		BStat bstat = null;
		// wertet das Root-Pdf-Verzeichniss aus
		// Es wird hier durch alle Verzeichnisse gegangen
		FileAccess.initFileSystemList(GC.mailbase, 1);

		// holt alle Verzeichnisse
		int anz = FileAccess.holeFileAnz();

		progressBar1.setMinimum(0);
		progressBar1.setMaximum(anz);

		// gehe durch alle Verzeichnisse
		for (int i = 0; i < anz; i++)
		{

			progressBar1.setSelection(i);
			Swttool.wupdate(dis);

			String foulder = FileAccess.holeFileSystemName();

			if (foulder.contains("inbox"))
				continue;

			// msf-Dateien werden nicht ausgewertet da diese nur
			// konfigurationsdaten
			// enthalten
			if (foulder.contains(".msf") == true)
				continue;

			System.out.println("Betrachte BB <" + foulder + ">");

			// Hier werden alle Mails eines Foulders mit attachments extrahiert
			bstat = extraktMailFoulder(foulder);
		}
		return bstat;
	}

	static void Konvertiere(String quelle, String ziel)
	{
		// Konvertiert PDF nach text

		try
		{
			String cmd = GC.rootpath + "\\external_exe\\ptcmd.exe \"" + quelle
					+ "\"" + " \"" + ziel + "\" ";
			String line = null;

			// System.out.println("zeile<"+cmd+">");
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader lsOut = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			while ((line = lsOut.readLine()) != null)
			{
				System.out.println(line);
			}
		} catch (Exception e)
		{
			System.err.println("ls error " + e);
		}
	}

	static void convPdfToText(String verz)
	{
		FileAccessDyn fdyn2 = new FileAccessDyn();
		fdyn2.initFileSystemList(GC.pdfzielbase + "\\" + verz, 1);
		int anz = fdyn2.holeFileAnz();

		// Prüft ob Zielverzeichniss da
		FileAccess.checkgenDirectory(GC.textzielbase + "\\" + verz);

		for (int i = 0; i < anz; i++)
		{
			String pdfnam = fdyn2.holeFileSystemName();
			String fnam2 = GC.pdfzielbase + "\\" + verz + "\\" + pdfnam;
			String fnamb = GC.textzielbase + "\\" + verz + "\\" + pdfnam;

			if (fnamb.contains(".txt"))
			{
				// falls file schon da
				if (FileAccess.FileAvailable0(fnamb) == true)
					continue;

				// convert
				FileAccess.copyFile2_dep(fnam2, fnamb);
			}
			// pdf-file
			else if (fnamb.contains(".pdf"))
			{
				fnamb = fnamb.replace(".pdf", ".txt");

				// falls file schon da
				if (FileAccess.FileAvailable0(fnamb) == true)
					continue;

				// convert
				Konvertiere(fnam2, fnamb);
			} else if (fnamb.contains(".html"))
			{
				// falls file schon da
				if (FileAccess.FileAvailable0(fnamb) == true)
					continue;

				// kopiere
				FileAccess.copyFile2_dep(fnam2, fnamb);
			} else
				Tracer.WriteTrace(10, "Error: unbekannter dateitype fnam<"
						+ fnam2 + ">");
		}
	}

	static public void pdfToTextDurchlaufeVerzeichnisse(Display dis,
			ProgressBar progressBar1)
	{
		// Holt alle pdf-verzeichnise
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(GC.pdfzielbase, 0);

		int anz = fdyn.holeFileAnz();
		progressBar1.setMinimum(0);
		progressBar1.setMaximum(anz);

		for (int i = 0; i < anz; i++)
		{
			progressBar1.setSelection(i);
			Swttool.wupdate(dis);

			String fnam = fdyn.holeFileSystemName();

			convPdfToText(fnam);
		}

	}

	public static BStat convertiereAlleMails(Display dis,
			ProgressBar progressBar1_glob)
	{
		// Check Mailbase
		if (FileAccess.checkDirectory(GC.mailbase) == false)
			Tracer.WriteTrace(10, "Konfigurationsproblem: Directory<"
					+ GC.mailbase + "> nicht verfügbar");

		if (FileAccess.checkDirectory(GC.pdfzielbase) == false)
			Tracer.WriteTrace(10, "Konfigurationsproblem: Directory<"
					+ GC.pdfzielbase + "> nicht verfügbar");

		// Holt die PDFs aus dem Mailer und speichert sie in GC.pdfzielbase
		BStat bstat = getAllTextPdfs(dis, progressBar1_glob);

		// wandelt GC.pdfzielbase in Texte um und speichert die in
		// GC.textzielbase
		pdfToTextDurchlaufeVerzeichnisse(dis, progressBar1_glob);
		return bstat;
	}
}
