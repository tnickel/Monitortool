package mailer;

import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.ArrayList;

import mainPackage.GC;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class MailsucherSchluesselwortFile extends Sucher
{
	// Hier wird ein Schlüsselwort in allen Files gesucht
	// zurückgegeben wird eine Liste von files wo das drin vorkommmt

	public Found SucheSchluesselwort(Display dis, ProgressBar pb,
			String mindat, String schluesselwort, Found found,int maxfilelength,int keineTeilwoerterflag)
	{
		// Main: durchsucht alle Verzeichnisse nach 'einem' Schlüsselwort

		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(GC.pdfzielbase, 0);

		// gehe durch alle Verzeichnisse
		int anz = fdyn.holeFileAnz();
		pb.setMinimum(0);
		pb.setMaximum(anz);
		for (int i = 0; i < anz; i++)
		{
			pb.setSelection(i);
			Swttool.wupdate(dis);

			String fnam = fdyn.holeFileSystemName();
			sucheVerzeichnis(schluesselwort, fnam, mindat, found,maxfilelength,keineTeilwoerterflag);
		}

		return found;
	}

	private void sucheVerzeichnis(String schluesselwort, String verz,
			String mindat, Found found, int maxfilegroesse,int keineTeilwoerterflag)
	{
		// Durchsucht ein Verzeichniss nach einem Schlüsselwort
		// return: Liste mit Dateinamen die dieses Schlüsselwort enthalten
		//
		// found: rückgabestruktur
		// maxfilegroesse: maximale filegroesse in kb, typisch maximal 1000kb
		FileAccessDyn fdyn2 = new FileAccessDyn();
		fdyn2.initFileSystemList(GC.textzielbase + "\\" + verz, 1);
		int anz = fdyn2.holeFileAnz();

		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn2.holeFileSystemName();

			// Aus dem Filenamen werden die zugehörigen Daten extrahiert
			String fnamtext = GC.textzielbase + "\\" + verz + "\\" + fnam;
			System.out.println("betrachte file<" + fnam + ">");

			int len = FileAccess.FileLength(fnamtext);
			if (len > maxfilegroesse * 1000)
			{
				Tracer.WriteTrace(20,
						"Info: File<"+fnamtext+"> wird nicht durchsucht da File zu lang aktlen<"
								+ len + ">  >>> maxlen<" + maxfilegroesse
								+ "> ist");
				continue;
			}

			if (SucheSchluesselwortFile(fnamtext, schluesselwort, mindat,keineTeilwoerterflag) == true)
			{
				// fliste.add(fnamtext);
				found.add(mindat, schluesselwort, verz, fnam);
			}
		}
	}

}
