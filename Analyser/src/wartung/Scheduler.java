package wartung;

import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import hilfsrepeattask.SammleIcons;
import hilfsrepeattask.SlidWorker;
import kurse.KurseDB;
import mailer.Mailcoder;
import mailer.MailsucherListe;
import mailer.Suchliste;
import mainPackage.GC;
import objects.Reporting;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import ranking.Rang;
import stores.AktDB;
import stores.KeyDB;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import stores.UserDB;

public class Scheduler
{
	private void Kurseladen(ProgressBar pb, Display dis,
			Boolean boerbestimmflag, ThreadsDB tdb, AktDB aktdb)
	{
		// lade alle Kurse

		KurseDB kdb = new KurseDB(tdb);
		// kdb.ladeAlleKurseUpdateKurseDB(tdb, 30, 1, 0);

		Boolean fastflag = false;
		Boolean forceflag = false;

		if (boerbestimmflag == false)
			fastflag = true;
		else
		{
			// die kürzel werden ja neu bestimmt
			forceflag = true;
		}

		// lade die kurse schnell
		kdb.ladeAlleKurseUpdateKurseDB(aktdb, tdb, 30, fastflag, forceflag, pb,
				dis);
	}

	private void Lade38200(ProgressBar pb, Display dis, ThreadsDB tdb)
	{
		KurseDB kdb = new KurseDB(tdb);
		kdb.suche38_200LimitsSortSteigung(0, pb, dis);
	}

	private void LadeObserveUserdaten(ProgressBar pb, Display dis,
			int threshold, boolean alluserflag,AktDB aktdb)
	{
		int nurobserveuserflag = 0;

		if (alluserflag == true)
			nurobserveuserflag = 0;
		else
			nurobserveuserflag = 1;

		//AktDB aktdb = new AktDB();

		// die Userdaten aktualisieren
		UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
		// udb.AktualliereAlleUserSchnell(0);

		// Bermerkung um den Rang optimal zu bestimmen ist es am besten alle
		// user zu aktualisieren, also nicht nur die User die auf observable
		// gesetzt sind !!
		udb.AktuallisiereAlleUserCalRang(aktdb, 1, nurobserveuserflag,
				threshold, pb, dis, null, null);
	}

	private void SchaueNeueThreads(ProgressBar pb, int observeuser2flag,
			AktDB aktdb, ThreadsDB tdb, UserDB udb, KeyDB keydb)
	{

		if (GC.ONLINEMODE == 0)
		{
			Tracer
					.WriteTrace(
							10,
							"Info: geht nur online da bei neuaufnahme in aktdb die grunddaten geladen werden müssen");
			return;
		}
		// UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
		udb.ImportNewThreadsFromUsers(GC.KEIN_USERGEWINNRANG, observeuser2flag,
				pb, null, aktdb, tdb, keydb);
	}

	private void Ranking(int rangmode, ProgressBar pb, Display dis, Text text,
			AktDB aktdb, ThreadsDB tdb,UserDB udb)
	{
		Rang.calcSchedulerRank(rangmode, pb, dis, text, null, "rangfile", tdb,
				aktdb,udb);
	}

	private void LadeSliderGenPrognosen(ProgressBar pb, ThreadsDB tdb,
			UserDB udb)
	{

		SlidWorker slider = new SlidWorker(tdb, udb);
		SlideruebersichtDB sldb = new SlideruebersichtDB();

		// wo werden die Prognosen generiert ???
		// "kaufe für"
		if (GC.TESTMODE == 1)
		{
			tdb.updateAllThreads(0, 1, 10, 10, 400, 1, pb, udb, sldb);
			slider.GenPrognosen(sldb, 0, GC.SUCHMODE_UEBERSPRINGE_FEHLER, 400,
					GC.KEINE_EINZELGEWINNE, 1, 10, 200, 1, 200, pb, null);
			return;
		}
		// Prio 1-5
		tdb.updateAllThreads(2, 1, 1, 5, 180, 1, pb, udb, sldb);
		slider.GenPrognosen(sldb, 2, GC.SUCHMODE_UEBERSPRINGE_FEHLER, 200,
				GC.KEINE_EINZELGEWINNE, 1, 5, 180, 1, 60, pb, null);
		// Prio 6-8
		tdb.updateAllThreads(4, 1, 6, 8, 50, 0, pb, udb, sldb);
		slider.GenPrognosen(sldb, 4, GC.SUCHMODE_UEBERSPRINGE_FEHLER, 200,
				GC.KEINE_EINZELGEWINNE, 6, 8, 60, 0, 60, pb, null);

		// Prio 9
		tdb.updateAllThreads(20, 1, 9, 9, 30, 0, pb, udb, sldb);
		slider.GenPrognosen(sldb, 20, GC.SUCHMODE_UEBERSPRINGE_FEHLER, 200,
				GC.KEINE_EINZELGEWINNE, 9, 9, 30, 0, 60, pb, null);

		// Prio 10
		tdb.updateAllThreads(20, 0, 10, 10, 20, 0, pb, udb, sldb);
		slider.GenPrognosen(sldb, 20, GC.SUCHMODE_UEBERSPRINGE_FEHLER, 200,
				GC.KEINE_EINZELGEWINNE, 10, 10, 30, 0, 60, pb, null);

		tdb.SortSliderWriteDB();
		sldb.SortWriteDB();
	}

	private void ListeWochengewinne(String enddatum, ProgressBar pb,
			Display dis, Text textinfo)
	{
		// Die Wochengewinne können aus der Slideruebersicht.db generiert werden
		// Vorraussetzung ist allerdings das die Slideruebersicht.db auch
		// aktuell ist
		SlideruebersichtDB sldb = new SlideruebersichtDB();
		textinfo.setText("1/3 calc Wochengewinne 50");
		Swttool.wupdate(dis);
		sldb.ListeWochengewinne(enddatum, 50, pb, dis);
		textinfo.setText("2/3 calc Wochengewinne 100");
		Swttool.wupdate(dis);
		sldb.ListeWochengewinne(enddatum, 100, pb, dis);
		textinfo.setText("3/3 calc Wochengewinne 200");
		sldb.ListeWochengewinne(enddatum, 200, pb, dis);
		Swttool.wupdate(dis);
	}

	private void ZeigeSliderPrios(ProgressBar pb, Display dis)
	{
		SlideruebersichtDB sldb = new SlideruebersichtDB();
		sldb.GibSliderPrioKlassenAus(pb, dis);

	}

	private void LadeIcons(ProgressBar pb)
	{

		// aktuallisiere die icons
		SammleIcons si = new SammleIcons();
		// aktuallisiere die icondb alle 48 Stunden
		si.start(30, pb);
	}

	private void BaueTagesinfo(ProgressBar pb, Text textinfo, Display dis,
			String pref)
	{
		Reporting rep = new Reporting();

		// a)schaue nach ob die User neue Threads besitzen
		// ausserdem ob die vorhandenen Threads neuer sind
		// wenn ja setze den Marker in der tdb
		System.out.println("Start a)");

		if (GC.ONLINEMODE == 1)
			rep.StartAktualisiereObserveUser(0, "observeuser.txt", 2,
					GC.KEIN_USERGEWINNRANG, pb, textinfo, dis, pref);

		// b)sucht userpostings
		System.out.println("Start b)");
		rep.StartBaueTagesinfo(0, pb, textinfo, dis, pref, null, "rangfile");
	}

	public void start(Button button1, Button button2, Button button3,
			Button button4, Button button5, Button button6, Button button7,
			Button button8, Button button9, Button button10, Button button11,
			ProgressBar progressBar1, ProgressBar progressBar2,
			ProgressBar progressBar3, ProgressBar progressBar4,
			ProgressBar progressBar5, ProgressBar progressBar6,
			ProgressBar progressBar7, ProgressBar progressBar8,
			ProgressBar progressBar9, ProgressBar progressBar10,
			ProgressBar progressBar11, Display dis, Text textinfo,
			Button loopbutton, Text letzterstart, String end,
			Text threshold_userdaten, boolean alluser,
			boolean observeuser2flag, boolean boerbestimmflag, ThreadsDB tdb,
			AktDB aktdb, UserDB udb, KeyDB kdb)
	{
		// boerbestimmflag: true, dann wird die boerse neu bestimmt

		Inf inf = new Inf();
		int loopflag = 0;

		String enddatum = Tools.convDatumStrichPunkt(end);

		inf.setFilename(GC.rootpath + "\\db\\reporting\\scheduler.txt");
		if (loopbutton.getSelection() == true)
			loopflag = 1;

		while (5 == 5)
		{
			String startzeit = Tools.get_aktdatetime_str();
			// inf.writezeile(Tools.get_aktdatetime_str() + ":ThreadAquise");
			// ThreadAquise
			// ThreadAquise ta = new ThreadAquise();
			// ta.start();
			letzterstart.clearSelection();
			letzterstart.setText(startzeit);
			Swttool.wupdate(dis);

			progressBar1.setSelection(0);
			progressBar2.setSelection(0);
			progressBar3.setSelection(0);
			progressBar4.setSelection(0);
			progressBar5.setSelection(0);
			progressBar6.setSelection(0);
			progressBar7.setSelection(0);
			progressBar8.setSelection(0);
			progressBar9.setSelection(0);
			progressBar10.setSelection(0);
			progressBar11.setSelection(0);
			Swttool.wupdate(dis);

			if (button1.getSelection() == true)
			{
				inf.writezeile(Tools.get_aktdatetime_str() + ":KurseLaden");
				Kurseladen(progressBar1, dis, boerbestimmflag, tdb, aktdb);
			}

			if (button2.getSelection() == true)
			{
				inf.writezeile(Tools.get_aktdatetime_str() + ":LadeUserdaten");
				LadeObserveUserdaten(progressBar2, dis, Integer
						.valueOf(threshold_userdaten.getText()), alluser,aktdb);
			}

			if (button3.getSelection() == true)
			{
				int obflag = 0;
				if (observeuser2flag == true)
					obflag = 1;
				inf.writezeile(Tools.get_aktdatetime_str()
						+ ":SchaueNeueThreads");
				SchaueNeueThreads(progressBar3, obflag, aktdb, tdb, udb, kdb);
			}

			if (button4.getSelection() == true)
			{
				// Ranking
				inf.writezeile(Tools.get_aktdatetime_str() + ":Ranking");
				Ranking(0, progressBar4, dis, null, aktdb, tdb, udb);
			}

			if (button5.getSelection() == true)
			{

				inf.writezeile(Tools.get_aktdatetime_str()
						+ ":LadeThreadsSlider");
				LadeSliderGenPrognosen(progressBar5, tdb, udb);
			}

			if (button6.getSelection() == true)
			{
				inf.writezeile(Tools.get_aktdatetime_str()
						+ ":ListeWochengewinne");
				ListeWochengewinne(enddatum, progressBar6, dis, textinfo);
			}

			if (button7.getSelection() == true)
			{
				inf.writezeile(Tools.get_aktdatetime_str()
						+ ":ZeigeSliderPrios");
				ZeigeSliderPrios(progressBar7, dis);
			}

			if (button8.getSelection() == true)
			{
				// lade 38200
				inf.writezeile(Tools.get_aktdatetime_str() + ":Lade38200");
				Lade38200(progressBar8, dis, tdb);
			}

			if (button9.getSelection() == true)
			{
				inf.writezeile(Tools.get_aktdatetime_str() + ":LadeIcons");
				LadeIcons(progressBar9);
			}

			if (button10.getSelection() == true)
			{
				inf.writezeile(Tools.get_aktdatetime_str() + ":BaueTagesinfo");
				// pref =null da hier die tagesinfo im standartverzeichniss
				// aufgebaut wird
				BaueTagesinfo(progressBar10, textinfo, dis, null);
				String endzeit = Tools.get_aktdatetime_str();
				inf.writezeile(Tools.get_aktdatetime_str()
						+ ":Durchlauf fertig dauer<"
						+ Tools.zeitdifferenz_minuten(startzeit, endzeit)
						+ "> **********");
			}
			if (button11.getSelection() == true)
			{
				// hole die neuen Mails
				Mailcoder.convertiereAlleMails(dis, progressBar11);

				// Erstelle Sucheliste
				MailsucherListe ms = new MailsucherListe();
				String config = GC.rootpath
						+ "\\conf\\boersenblaetter\\ConfigSuchliste.txt";
				String ausschluss =GC.rootpath+"\\conf\\boersenblaetter\\configausliste.txt";

				String dat = Tools.entferneZeit(Tools.get_aktdatetime_str());
				String dat2 = Tools.entferneZeit(Tools.modifziereDatum(dat, 0,
						0, -10, 0));

				Suchliste such13 = ms.erstelleSuchliste(dis, progressBar11,
						config, dat2,1,1,ausschluss);
				// SwtTabelle.baueTabellePuscherFilesMulti(dis, such13,null,
				// null);

			}
			inf.writezeile(Tools.get_aktdatetime_str() + ":Fertig");
			if (loopflag == 0)
				break;
		}
	}
}
