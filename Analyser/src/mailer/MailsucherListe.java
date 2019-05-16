package mailer;

import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import mainPackage.GC;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import swtOberfläche.ExtFileEditor;

public class MailsucherListe extends Sucher_dep
{
	//Die Mailsucherliste dient zum Durchsuchen von Mails und Aufbau einer Suchliste
	
	private Boolean checkFileL(String fnamtext, Suchliste suchl, String mindat,int keineTeilwoerterflag,int aflag)
	{
		// Prüft ein Suchwort in dem file
		// speichere die Ergebnisse in der suchlliste "suchl"

		String filenam = fnamtext.substring(fnamtext.lastIndexOf("\\") + 1);
		String filedat = filenam.substring(0, filenam.indexOf("_"));

		// Step 1: schaue nur in den neuen falls,falls file zu alt dann nix
		if ((Tools.datum_ist_aelter_gleich(mindat, filedat)) == false)
			return false;

		// Step 2: gehe durch die Schlüsselwörter und aktualisiere die Suchliste
		suchl.checkSuchwoerter(fnamtext,keineTeilwoerterflag,aflag);
		return true;
	}

	public Suchliste erstelleSuchliste(Display dis, ProgressBar pb,
			String config, String mindat,int keineTeilwoerterflag,int ausflag,String aliste)
	{
		
		
		System.out.println("Erstelle Suchliste");
		Suchliste suchl = new Suchliste(config,aliste);
		
		
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
			
			checkVerzeichnisL(fnam, suchl, mindat,keineTeilwoerterflag,ausflag);
		}
		String fout = GC.rootpath + "\\result\\boersenblaetter\\keyliste.txt";

		FileAccess.FileDelete(fout, 0);
		suchl.gibSuchwortlisteAus(GC.rootpath
				+ "\\result\\boersenblaetter\\keyliste.txt");

		// gibt die erstellte suchliste zurück
		return suchl;
	}

	private void checkVerzeichnisL(String verznam, Suchliste suchl,
			String mindat,int keineTeilwoerterflag,int ausschlussflag)
	{
		// Hier wird ein Verzeichniss ausgewertet, also welche Files besitzen
		// welche
		// Schlüsselwörter. Das Ergebniss wird in der Suchliste festgehalten
		// ausschlussflag: falls 1 dann werden die suchbegriffe der ausliste ausgeklammert

		FileAccessDyn fdyn2 = new FileAccessDyn();
		fdyn2.initFileSystemList(GC.textzielbase + "\\" + verznam, 1);
		int anz = fdyn2.holeFileAnz();

		for (int i = 0; i < anz; i++)
		{
			String pdfnam = fdyn2.holeFileSystemName();
			String fnamtext = GC.textzielbase + "\\" + verznam + "\\" + pdfnam;
			System.out.println("untersuche file <"+fnamtext+">" );
			checkFileL(fnamtext, suchl, mindat,keineTeilwoerterflag,ausschlussflag);
		}
	}

	public void zeigeErgebnisse(Display dis)
	{
		ExtFileEditor ef = new ExtFileEditor();
		ef.init(dis, GC.rootpath + "\\result\\boersenblaetter\\keyliste.txt");
	}

	public void Lade()
	{}
	
	public void Speichere()
	{}
	
	public void Sortiere()
	{}
	
	
}
