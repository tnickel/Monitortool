package swtOberfläche;

import hilfsklasse.DateExcecption;
import hilfsklasse.FileAccess;
import hilfsklasse.Keywortliste;
import hilfsklasse.SG;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import mailer.Found;
import mailer.Foundelem;
import mailer.Suchliste;
import mainPackage.GC;
import objects.EventDbObj;
import objects.ThreadDbObj;
import objects.UeberwachungDbObj;
import objects.UserDbObj;
import objects.UserThreadPostingObj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ranking.Rangparameter;
import stores.AktDB;
import stores.BoersenBlaetterDB;
import stores.ChampionDB;
import stores.EventsDB;
import stores.ThreadsDB;
import stores.UeberwachungDB;
import stores.UserDB;
import userHilfObj.UserPostingListe;
import attribute.ThreadAttribObjI;
import attribute.ThreadAttribStoreI;
import championAnalyser.BoerseDe;
import championAnalyser.BoerseMasterliste;
import championAnalyser.Championaktie;

import comperatoren.FnamComperator;

public class SwtTabelle
{
	static public void baueTabelleChampionBlaetterListe(Table table,
			Display dis, String textroot, HashMap<Integer, String> boermap)
	{
		//
		ArrayList<String> boerblattnamlist = new ArrayList<String>();

		table.removeAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Boerblatt");
		column = new TableColumn(table, SWT.NONE);

		// Filenamen holen
		FileAccess.initFileSystemList(textroot, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			// sortiere erst richtig und dann putte alles sortiert in die
			// boermap

			// nimm nur pdfs
			if (fnam.contains("pdf") == false)
				continue;

			boerblattnamlist.add(fnam);
		}
		FnamComperator c = new FnamComperator();
		Collections.sort(boerblattnamlist, c);

		// Hashmap setzen
		anz = boerblattnamlist.size();
		for (int i = 0; i < anz; i++)
		{
			String fnam = boerblattnamlist.get(i);
			boermap.put(i, fnam);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, fnam);

		}
		for (int i = 0; i < 1; i++)
		{
			table.getColumn(i).pack();
		}
	}

	static public void baueTabelle100Championliste(Display dis, Table table,
			BoerseDe boersenblatt, int extendedflag,
			BoerseMasterliste boersenliste, AktDB aktdb, ChampionDB champdb,
			HashMap<Integer, String> selwknmapLinks, int markerflag,
			Keywortliste keyl)
	{
		String[] extkopfliste =
		{ "Rang", "Info", "Aktie", "Wkn", "Symb", "Branche", "Sterne",
				"Währung", "GeoPac", "GK", "cGewinn", "VerlustRatio", "ATH",
				"CRisiko", "Crang", "aktKurs", "aktYhKurs", "GD200", "Trend",
				"Seit", "KursDam", "KursYahoo", "Ergeb", "Strat" };

		Color red = dis.getSystemColor(SWT.COLOR_RED);
		Color blue = dis.getSystemColor(SWT.COLOR_BLUE);

		// Hier wird die Championliste aufgebaut
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		TableColumn column = new TableColumn(table, SWT.NONE);
		int anz = boersenblatt.getAnzLines();
		if (anz != 100)
			Tracer.WriteTrace(10, "Error: muss 100 sein!!!");

		if (extendedflag == 0)
		{
			Swttool.baueTabellenkopfDispose(table, "Rang#Aktie#Trend#Strategie");

			for (int i = 0; i < anz; i++)
			{
				Championaktie ca = boersenblatt.getChampionaktie(i);

				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, String.valueOf(ca.getRang_i()));
				item.setText(1, ca.getAktnam());
				item.setText(2, ca.getTrend());
				item.setText(3, ca.getStrategie());
			}
			for (int i = 0; i < 3; i++)
			{
				table.getColumn(i).pack();
			}
		} else if (extendedflag == 1)
		{
			String kopfstring = "";
			// Setze die Kopfleiste
			for (int i = 0; i < extkopfliste.length; i++)
			{
				kopfstring = kopfstring.concat(extkopfliste[i].toString());
				if (i < extkopfliste.length - 1)
					kopfstring = kopfstring.concat("#");

			}
			Swttool.baueTabellenkopfDispose(table, kopfstring);

			selwknmapLinks.clear();

			for (int i = 0; i < 100; i++)
			{
				Championaktie ca = boersenblatt.getChampionaktie(i);

				// "Rang", "Aktie", "Wkn", "Branche", "Sterne", "Währung",
				// "GeoPac",
				// "GK", "cGewinn", "VerlustRatio", "ATH", "CRisiko", "Crang",
				// "aktKurs", "GD200", "Trend", "Seit", "KursDam", "Ergeb",
				// "Strat"

				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, String.valueOf(ca.getRang_i()));
				item.setText(1, "Info");
				item.setText(2, ca.getAktnam());
				String wkn = ca.getWkn();
				item.setText(3, wkn);
				// speichere die position und die zugehörige wkn
				selwknmapLinks.put(i + 1, wkn);

				String symb = champdb.HoleSymbol(wkn);
				item.setText(4, symb);

				item.setText(5, ca.getBranche());
				item.setText(6, String.valueOf(ca.getSternanz_i()));
				item.setText(7, ca.getWaehrung());
				item.setText(8, String.valueOf(ca.getGeopak10_i()));
				item.setText(9, String.valueOf(ca.getGk_f()));
				item.setText(10, String.valueOf(ca.getCgewinn_f()));
				item.setText(11, String.valueOf(ca.getVerlustratio_f()));
				item.setText(12, String.valueOf(ca.getAthv_f()));
				item.setText(13, String.valueOf(ca.getCrisiko_f()));
				item.setText(14, String.valueOf(ca.getCrang_i()));
				item.setText(15, String.valueOf(ca.getKurs_f()));

				// akt.yahookurs
				float kursyahoo = champdb.HoleKurs(wkn, Tools
						.get_aktdatetime_str().substring(0, 8));
				item.setText(16, String.valueOf(kursyahoo));

				item.setText(17, String.valueOf(ca.getGd200_f()));

				String aktTrend = ca.getTrend();
				String vorgTrend = boersenliste.getVorgaengerTrend(
						boersenblatt, i);

				// falls dies eine zeile ist die den suchwörtern entspricht
				// blau übersteuert den rot-Mechanismus
				if ((markerflag == 1)
						&& (ca.checkKeywortlisteFull(keyl) == true))
					item.setForeground(blue);
				else if ((aktTrend.endsWith(vorgTrend)) == false)
					// Setze farbe rot
					item.setForeground(red);

				item.setText(18, String.valueOf(ca.getTrend()));

				String seitdatum = ca.getSeit();
				item.setText(19, String.valueOf(seitdatum));
				item.setText(20, String.valueOf(ca.getKursdamals_f()));

				if (seitdatum.length() == 8)
				{
					kursyahoo = champdb.HoleKurs(wkn, seitdatum);
					item.setText(21, String.valueOf(kursyahoo));
				} else
					// datumsfehler
					item.setText(21, "datlen8");

				item.setText(22, String.valueOf(ca.getErgebniss_i()) + "%");
				item.setText(23, String.valueOf(ca.getStrategie()));

			}
			for (int i = 0; i < 24; i++)
			{
				table.getColumn(i).pack();
			}
		}
	}

	static public void baueTabelleWknZeitverlauf(Table table, String wkn,
			BoerseMasterliste bmaster)
	{
		// hier wird nur eine einzige Aktie in einer Tabelle dargestellt
		// Es wird hier der Zeitverlauf der Aktie über alle
		// Börsenblätterausgaben dargestellt
		String[] extkopfliste =
		{ "Datum", "Rang", "Info", "Aktie", "Wkn", "aktKurs", "GD200", "Trend",
				"Seit", "KursDam", "Ergeb", "Strat" };

		// Hier wird die Championliste aufgebaut
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		TableColumn column = new TableColumn(table, SWT.NONE);
		{
			String kopfstring = "";
			// Setze die Kopfleiste
			for (int i = 0; i < extkopfliste.length; i++)
			{
				kopfstring = kopfstring.concat(extkopfliste[i].toString());
				if (i < extkopfliste.length - 1)
					kopfstring = kopfstring.concat("#");

			}
			Swttool.baueTabellenkopfDispose(table, kopfstring);

			int anzmaster = bmaster.getanz();
			for (int i = 0; i < anzmaster; i++)
			{
				BoerseDe bde = bmaster.getBoerblattIdx(i);
				Championaktie ca = bde.getChampionaktieWkn(wkn);

				// in dieser Börsenausgabe wurde nicht die passende wkn gefunden
				if (ca == null)
					continue;

				// ca in die Börsenliste aufnehmen
				TableItem item = new TableItem(table, SWT.NONE);

				item.setText(0, bde.getDatum());
				item.setText(1, String.valueOf(ca.getRang_i()));
				item.setText(2, "Info");
				item.setText(3, ca.getAktnam());
				String wkn2 = ca.getWkn();
				item.setText(4, wkn2);
				/*
				 * item.setText(5, String.valueOf(ca.getSternanz_i()));
				 * item.setText(6, String.valueOf(ca.getGeopak10_i()));
				 * item.setText(7, String.valueOf(ca.getGk_f()));
				 * item.setText(8, String.valueOf(ca.getCgewinn_f()));
				 * item.setText(9, String.valueOf(ca.getVerlustratio_f()));
				 * item.setText(10, String.valueOf(ca.getAthv_f()));
				 * item.setText(11, String.valueOf(ca.getCrisiko_f()));
				 * item.setText(12, String.valueOf(ca.getCrang_i()));
				 */
				item.setText(5, String.valueOf(ca.getKurs_f()));
				item.setText(6, String.valueOf(ca.getGd200_f()));

				item.setText(7, String.valueOf(ca.getTrend()));
				String seitdatum = ca.getSeit();
				item.setText(8, String.valueOf(seitdatum));
				item.setText(9, String.valueOf(ca.getKursdamals_f()));
				item.setText(10, String.valueOf(ca.getErgebniss_i()) + "%");
				item.setText(11, String.valueOf(ca.getStrategie()));
			}

			for (int i = 0; i < 12; i++)
			{
				table.getColumn(i).pack();
			}
		}
	}

	static public void baueTabelleRankingconfig(Table table, Rangparameter rp)
	{
		/*
		 * double rankingpoints = ((float) anmeldetage * 3) + ((float) themenanz
		 * f* 100) + antwortanz + ((float) postinglisteanz * 100) + ((float)
		 * neueAktThreads * 1000) + ((float) neueSofaThreads * 100) +
		 * (durchschnbeitraege * 2000) + gewinnrangpunkte;
		 */
		table.removeAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Konfigurationsparameter");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Value");
		column = new TableColumn(table, SWT.NONE);

		int anz = rp.getAnz();

		for (int i = 0; i < anz; i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, rp.getAttribut(i));
			item.setText(1, String.valueOf(rp.getVal(i)));
		}

		for (int i = 0; i < 2; i++)
		{
			table.getColumn(i).pack();
		}

	}

	static public void baueTabelleUserPostings(Table table, String unam,
			UserDbObj udbo, boolean[] uplliste_g)
	{
		UserPostingListe upl = new UserPostingListe();
		UserThreadPostingObj upobj = null;
		upl.ReadUserInfoListe(udbo);
		int uplanz = upl.getSize();
		// Diese liste verwaltet die häckchen
		uplliste_g = new boolean[uplanz];
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Pos");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Threadname");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Tid");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Aufnahmedatum");
		column = new TableColumn(table, SWT.NONE);
		column.setText("LastPosting");

		upobj = upl.getFirstPostingObj();
		int anz = upl.getSize();
		for (int i = 0; i < anz - 1; i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, String.valueOf(i));
			item.setText(1, upobj.getThreadname());
			item.setText(2, String.valueOf(upobj.getThreadid()));
			item.setText(3, upobj.getAufnahmeDatum());
			item.setText(4, upobj.getLastposting());
			upobj = upl.getNextPostingObj();
		}

		for (int i = 0; i < 5; i++)
		{
			table.getColumn(i).pack();
		}
		// table.setBounds(20, 20, 800, 250);
	}

	static public void baueTabelleObserveuser(Table table, UserDB udb,
			HashMap<Integer, String> usermap, Display dis, int minprio,
			int maxprio)
	{
		// Kopf aufbauen
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Username");
		Color red = dis.getSystemColor(SWT.COLOR_RED);

		// Diese Tabelle baut die Liste der User die beobachtet werden auf
		int anz = udb.GetanzObj();
		int count = 0;
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) udb.GetObjectIDX(i);
			if ((udbo.getMode() == 8000) && (udbo.getPrio() >= minprio)
					&& (udbo.getPrio() <= maxprio))
			{
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, udbo.get_username());
				usermap.put(count, udbo.get_username());
				count++;
			}
		}
		// Ausserdem kommen hier noch einige speczialelemente hinzu
		TableItem item = new TableItem(table, SWT.NONE);
		item.setForeground(red);
		item.setText(0, "AlleObserveuser");
		usermap.put(count, "AlleObserveuser");
		count++;

		table.getColumn(0).pack();
	}

	static private void setZeileObserverthread(ThreadDbObj tdbo, Table table,
			int index, UserThreadPostingObj upobj, ThreadsDB tdb,
			HashMap<Integer, Integer> map, Display dis,
			List<Integer> prognosenliste)
	{
		// hier wird eine Zeile der Observerthreads generiert
		int tid = upobj.getThreadid();
		int neuflag = 0;
		String lastpostdate = "?";

		Color red = dis.getSystemColor(SWT.COLOR_RED);
		Color black = dis.getSystemColor(SWT.COLOR_BLACK);
		Color gruen = dis.getSystemColor(SWT.COLOR_DARK_GREEN);

		// "Pos#Threadname#Info#Tid#BBlastPusch#Aufnahmedatum#LastPostDate#Pageanzahl#Upostanz#Info"

		// Color orange = dis.getSystemColor(SWT.COLOR_DARK_MAGENTA);

		if (tdbo != null)
			lastpostdate = tdbo.getLastThreadPosttime();

		// mache grün wenn nicht älter als 90 Tage
		TableItem item = new TableItem(table, SWT.NONE);
		if (Tools.zeitdifferenz_tage(lastpostdate, Tools.get_aktdatetime_str()) < 90)
			item.setForeground(gruen);

		// mache rot wenn thread in prognose
		if (prognosenliste.contains(tid))
		{
			item.setForeground(red);
		}
		// neuflag=1 wenn thread neu aufgespürt wurde
		String aufdatum = upobj.getAufnahmeDatum();
		if (Tools.zeitdifferenz_tage(aufdatum, Tools.get_aktdatetime_str()) < 30)
		{
			neuflag = 1;
		}

		String postext = (String.valueOf(index));
		if (neuflag == 1)
			postext = postext.concat("*");

		if ((tdbo != null) && (tdbo.getPuscherflag() == 1))
			postext = postext.concat("P");

		item.setText(0, postext);
		item.setText(1, upobj.getThreadname());

		String fna = "";
		String progstr = "...";
		if (tdbo != null)
		{
			fna = GC.rootpath + "\\db\\prognosenliste\\" + tdbo.getMasterid()
					+ ".txt";
			if (FileAccess.FileAvailable(fna) == true)
				progstr = "PROG";
		}

		item.setText(2, progstr);
		item.setText(3, String.valueOf(upobj.getThreadid()));
		if (tdbo == null)
			item.setText("??");
		else
			item.setText(4, tdbo.getBBlastPuschdate());
		item.setText(5, upobj.getAufnahmeDatum());
		item.setText(6, lastpostdate);

		map.put(index, tid);

		if (tdbo == null)
		{
			item.setText(7, "??");
			item.setText(8, "??");
		} else
		{
			item.setText(7, String.valueOf(tdbo.getPageanz()));

			item.setText(8, String.valueOf(tdbo.calcUserpostanzahl(upobj
					.getUsername_glob())));
		}
		item.setText(9, Tools.calcTargetnameSimple(tid));
	}

	static private boolean checkFilter(boolean nurakten, boolean nurneue,
			boolean nurprognosen, boolean nurthreadsmitpages, ThreadDbObj tdbo,
			UserThreadPostingObj upobj, List<Integer> prognosenliste,
			boolean gelesAusblenden, boolean pusched, int maxpostaltertage)
	{
		// true: filter sagt alles ok
		// false: filter sagt bedingung nicht erfüllt
		// nuraktien: es werden nur aktienthreads angzeigt
		// nurneue: alte threads (letztes ist zu alt) werden nicht mehr
		// angezeigt
		// pusched: es werden nur diskussionen angezeigt die gepusched sind
		// maxpostaltertage: in diesem thread ist das letzte posting max "tage"
		// alt
		// hier werden pages erwartet, falls tdbo ==null dann sind keine da
		if ((tdbo == null) && (nurthreadsmitpages == true))
		{
			Tracer.WriteTrace(20, "tid<??> wird rausgefiltert PAGEMISSING");
			return false;
		}
		if (tdbo == null)
			return true;

		// prognosencheck
		if (nurprognosen == true)
		{
			int tid = tdbo.getThreadid();
			if (prognosenliste.contains(tid) == false)
				return false;
		}

		// Prüft auf aktienthread
		if ((tdbo.isValidAktienthread(0) == false) && (nurakten == true))
		{
			Tracer.WriteTrace(20, "tid<" + tdbo.getThreadid()
					+ "> wird rausgefiltert AKTIENTHREAD");
			return false;
		}
		// prüfe auf alter des letztes postings
		if ((Tools.zeitdifferenz_tage(tdbo.getLastThreadPosttime(),
				Tools.get_aktdatetime_str()) > maxpostaltertage)
				&& (nurneue == true))
		{
			Tracer.WriteTrace(20, "tid<" + tdbo.getThreadid()
					+ "> wird rausgefiltert ALTER");
			return false;
		}

		if ((pusched == true) && (tdbo.getPuscherflag() == 0))
			return false;

		// prüfe ob der thread neue postings hat
		if ((gelesAusblenden == true) && (tdbo.hatNeuePostings() == false))
		{
			Tracer.WriteTrace(20, "tid<" + tdbo.getThreadid()
					+ "> wird rausgefiltert da schon GELESEN");
			return false;
		}

		return true;
	}

	static private int baueObserverzeilen(Table table, UserDbObj udbo,
			ThreadsDB tdb, HashMap<Integer, Integer> threadmap, Display dis,
			int fuellstand, HashSet<Integer> tidmenge,
			List<Integer> prognosenliste, boolean nurakten, boolean nurneue,
			boolean nurprognosen, boolean nurthreadsmitpages,
			boolean gelesAusblenden, boolean pusched, int maxpostaltertage)
	{
		// hier werden für einen User die Zeileneinträge gemacht
		// es werden aber keine Tids doppelt aufgenommen
		// fuellstand: gibt an wieviele Zeilen schon in der Tabelle sind,
		// fuellstand fängt bei 0 an
		// nuraktien: falls true werden nur aktienthreads angezeigt
		// nurneue: falls true werden nur threads angezeigt in denen den letzten
		// 90 tagen gepostet wurde
		// gelesAusblenden: falls true, dann werden nur ThreadsPostings
		// angezeigt die
		// noch nicht gelesen wurden
		// return int: anzahl der neuen threads, die neu hinzugekommen sind

		UserPostingListe upl = new UserPostingListe();
		UserThreadPostingObj upobj = null;
		upl.ReadUserInfoListe(udbo);

		upobj = upl.getFirstPostingObj();
		int neuhinzu = 0;

		if (upobj == null)
		{
			Tracer.WriteTrace(10,
					"Info: Fuer user<> sind keine postings in userpostings->userdaten erst laden");
			return 0;
		}

		int tid = upobj.getThreadid();
		ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObject(tid);

		if (tdbo == null)
			Tracer.WriteTrace(10, "Info: tdbo ==null tid<" + tid + ">");

		if (checkFilter(nurakten, nurneue, nurprognosen, nurthreadsmitpages,
				tdbo, upobj, prognosenliste, gelesAusblenden, pusched,
				maxpostaltertage) == true)
		{
			setZeileObserverthread(tdbo, table, fuellstand, upobj, tdb,
					threadmap, dis, prognosenliste);
			fuellstand++;
			neuhinzu++;
		}

		int anz = upl.getSize();
		// baut die einzelnen zeilen der Tabelle auf
		for (int i = 1; i < anz; i++)
		{
			upobj = upl.getNextPostingObj();
			tid = upobj.getThreadid();

			if (tidmenge.contains(tid) == false)
			{
				tdbo = (ThreadDbObj) tdb.GetObject(tid);

				if (checkFilter(nurakten, nurneue, nurprognosen,
						nurthreadsmitpages, tdbo, upobj, prognosenliste,
						gelesAusblenden, pusched, maxpostaltertage) == false)
					continue;

				// zeile aufnehmen und füllstand erhöhen
				setZeileObserverthread(tdbo, table, fuellstand, upobj, tdb,
						threadmap, dis, prognosenliste);
				tidmenge.add(tid);
				neuhinzu++;
				fuellstand++;
			}
		}
		return neuhinzu;
	}

	static public void baueTabelleObservethreads(Group group, Table table,
			UserDB udb, HashMap<Integer, Integer> threadmap,
			List<String> seluserlist, ThreadsDB tdb, Display dis,
			List<Integer> prognosenliste, boolean nuraktien, boolean nurneue,
			boolean nurprognosen, boolean nurthreadsmitpages,
			boolean gelAusblenden, boolean pusched, int maxpostaltertage)
	{
		HashSet<Integer> tidset = new HashSet<Integer>();
		table.removeAll();
		table.clearAll();
		threadmap.clear();
		int tableanz = table.getItemCount();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);
		Swttool.baueTabellenkopf(
				table,
				"Pos#Threadname#Info#Tid#BBlastPusch#Aufnahmedatum#LastPostDate#Pageanz#UserPostanz#Info");

		// hier wird für die usermenge die posting liste aufgebaut
		// hier werden also alle threads gesammelt und die in der gesammtliste
		// dargestellt
		int anzuser = seluserlist.size();
		int fuellstand = 0;
		for (int i = 0; i < anzuser; i++)
		{
			String unam = seluserlist.get(i);
			UserDbObj udbo = (UserDbObj) udb.getUserobj(unam);

			if (udbo == null)
			{
				Tracer.WriteTrace(10, "Info: keine Info für user<" + unam
						+ "> in userstore.db");
				continue;
			}
			int neuhinzu = baueObserverzeilen(table, udbo, tdb, threadmap, dis,
					fuellstand, tidset, prognosenliste, nuraktien, nurneue,
					nurprognosen, nurthreadsmitpages, gelAusblenden, pusched,
					maxpostaltertage);
			Tracer.WriteTrace(20, "Info: unam<" + unam + "> neuhinzu<"
					+ neuhinzu + ">");
			fuellstand = fuellstand + neuhinzu;
		}

		for (int i = 0; i < 10; i++)
		{
			table.getColumn(i).pack();
		}
	}

	static public void baueTabelleEvents(Table table, EventsDB evdb,
			UeberwachungDB uebdb, HashMap<Integer, Integer> hmap,
			Tab12selFilter filter)
	{
		// Baut eine Tabelle der eingetretenen Ereignisse auf
		// table: diese tablle wird aufgebaut
		// eventdb: dies ist die grundlage für die tablle
		// uebdb: wird benötigt da nicht alles in evdb steht
		// hmap: dient als zuordnung von zeile zur nr

		table.removeAll();
		table.clearAll();

		hmap.clear();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		Swttool.baueTabellenkopfDispose(table,
				"Pos#uebmid#tid#name#type#symb#ausloesedate#min#max#val#Puschertext");

		int anz = evdb.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			EventDbObj eobj = (EventDbObj) evdb.GetObjectIDX(i);
			UeberwachungDbObj uebobj = (UeberwachungDbObj) uebdb
					.getObjektIDXid(eobj.getId());

			if (filter.check(eobj) == false)
				continue;

			TableItem item = new TableItem(table, SWT.NONE);
			// pos
			item.setText(0, String.valueOf(i));
			// uebmid
			int uebmid = eobj.getId();
			item.setText(1, String.valueOf(uebmid));
			// tid
			item.setText(2, String.valueOf(eobj.getThreadid()));
			// name
			item.setText(3, eobj.getAnzeigename());
			// type
			item.setText(4, String.valueOf(eobj.getType()));
			// symb
			if (uebobj != null)
				item.setText(5, uebobj.getSymbol());
			// auslösedate
			item.setText(6, eobj.getAusloesedate());
			// min
			if (uebobj != null)
				item.setText(7, String.valueOf(uebobj.getMinval()));
			// max
			if (uebobj != null)
				item.setText(8, String.valueOf(uebobj.getMaxval()));
			// val
			item.setText(9, String.valueOf(eobj.getVal()));
			// puschertext
			if (uebobj != null)
				item.setText(10, uebobj.getPuschertext());
			else
				item.setText(10, "Nicht mehr in Überwachung !!");

			hmap.put(i, uebmid);
		}

		for (int i = 0; i < 11; i++)
		{
			table.getColumn(i).pack();

		}

	}

	static public void baueTabelleUeberwachung(Display dis, Table table,
			UeberwachungDB uebdb, HashMap<Integer, Integer> hmap,
			HashMap<String, String> kursmenge, ArrayList<String> puscherliste,
			HashMap<String, Boolean> selmap, Tab12typefilter tab12typefilter)
	{
		// puscherliste: gibt eine Liste der Puscher an
		// selmap: gibt an ob die Puscher selektiert oder inaktiv sind
		//
		// bsp: smallscap#0
		// hmap: tabellenzeile <-> uebmid
		// Baut eine Tabelle für Uberwachung
		// Baut eine Tabelle für Observe Ereignisse auf
		table.removeAll();
		hmap.clear();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		Color black = dis.getSystemColor(SWT.COLOR_BLACK);
		Color red = dis.getSystemColor(SWT.COLOR_RED);
		Color dred = dis.getSystemColor(SWT.COLOR_MAGENTA);
		Swttool.baueTabellenkopfDispose(
				table,
				"Pos#uebmid#tid#name#type#symb#erstelldat#lastload#min#max#ist#wkn#isin#filepath#Pushertext");
		int anz = uebdb.GetanzObj();

		// puscherliste aufbauen, dies ist die liste die unten rechts angezeigt
		// wird
		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj u = (UeberwachungDbObj) uebdb.GetObjectIDX(i);
			String puschertext = u.getPuschertext();
			if (puscherliste.contains(puschertext) == false)
				puscherliste.add(puschertext);
		}

		// beim ersten mal alles aufbauen und initialisieren
		if (selmap.isEmpty() == true)
			for (int i = 0; i < anz; i++)
			{
				UeberwachungDbObj u = (UeberwachungDbObj) uebdb.GetObjectIDX(i);
				String puschertext = u.getPuschertext();
				selmap.put(puschertext, true);
			}

		// i+1 da der kursvalue aus den Kursen geholt wird
		int ipos = 0;
		for (int i = 0; i < anz; i++)
		{
			UeberwachungDbObj u = (UeberwachungDbObj) uebdb.GetObjectIDX(i);
			String puschertext = u.getPuschertext();

			// nur selektierte Puscher anzeigen
			Boolean bool = selmap.get(puschertext);
			if (bool == false)
				continue;

			// nur selektierte Typen anzeigen
			int type = u.getType();
			int marker = u.getMarker();
			if (tab12typefilter.checkType(type, marker) == false)
				continue;

			TableItem item = new TableItem(table, SWT.NONE);
			item.setForeground(black);

			String symb = u.getSymbol();
			float mink = u.getMinval();
			float maxk = u.getMaxval();
			float kurs = 0;
			if ((kursmenge != null) && (kursmenge.containsKey(symb)))
				kurs = Float.valueOf(kursmenge.get(symb));

			// mache Zeile rot falls der kurs über der grenze ist
			if ((kurs <= mink) || (kurs >= maxk))
			{
				if (u.getMarker() == 1)
					item.setForeground(dred);
				else
					item.setForeground(red);
			}
			// pos
			String posadd = "";
			if (u.getMarker() == 1)
				posadd = "M";
			item.setText(0, String.valueOf(ipos) + posadd);
			// id
			int uebid = u.getUebmid();
			item.setText(1, String.valueOf(uebid));
			// tid
			item.setText(2, String.valueOf(u.getThreadid()));
			// name
			item.setText(3, u.getAnzeigename());
			// type
			item.setText(4, String.valueOf(u.getType()));
			// symb
			item.setText(5, symb);
			// erstelldat
			item.setText(6, u.getErstelldat());
			// lastload
			item.setText(7, u.getLastload());
			// min
			item.setText(8, String.valueOf(mink));
			// max
			item.setText(9, String.valueOf(maxk));
			// akt Kurs
			item.setText(10, String.valueOf(kurs));
			// wkn
			item.setText(11, u.getWkn());
			// isin
			item.setText(12, u.getIsin());
			// filepath
			item.setText(13, u.getFilepath());
			// puschertext
			String ptext = u.getPuschertext();
			item.setText(14, ptext);

			if (puscherliste.contains(ptext) == false)
				puscherliste.add(ptext);

			hmap.put(ipos, uebid);
			ipos++;
		}

		for (int i = 0; i < 15; i++)
		{
			table.getColumn(i).pack();

		}
	}

	static public void baueTabellePuscherFiles(Display dis, Table table,
			Found found, HashMap<Integer, String> hmap, BoersenBlaetterDB boerdb)
	{
		// Baut eine Tabelle mit gefunden Files auf
		// Diese Files beinhalten also das Suchwort
		// sl: Fileliste mit den gefundenen Files <name#anz>
		// hmap: position in der tabelle/filename

		table.removeAll();
		hmap.clear();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		Color black = dis.getSystemColor(SWT.COLOR_BLACK);
		Color green = dis.getSystemColor(SWT.COLOR_GREEN);
		Color red = dis.getSystemColor(SWT.COLOR_RED);

		Swttool.baueTabellenkopfDispose(table, found.getAufbau());
		if (found.getAnz() == 0)
		{
			Tracer.WriteTrace(20, "Nix gefunden");
			return;

		}

		int anz = found.getAnz();

		int ipos = 0;
		String aktdatum = Tools.entferneZeit(Tools.get_aktdatetime_str());

		for (int i = 0; i < anz; i++)
		{
			Swttool.wupdate(dis);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setForeground(black);
			// pos
			item.setText(0, String.valueOf(ipos));

			// Boerblatt
			Foundelem fe = found.getElem(i);

			String boerblatt = fe.getBoerblattname();

			item.setText(1, boerblatt);

			// Datum
			String dat = fe.getDatum();

			item.setText(2, dat);

			// Filename
			String fnam = fe.getFilename();
			item.setText(3, fnam);

			// falls aktuelles datum
			if ((boerdb.isNew(fnam) == true)
					&& (Tools.datum_ist_aelter(Tools.entferneZeit(dat),
							aktdatum) == false))
			{
				item.setForeground(red);
			} else if (boerdb.isNew(fnam) == true)
				item.setForeground(green);
			else
				item.setForeground(black);

			hmap.put(ipos, "\\" + boerblatt + "\\" + fnam);
			ipos++;
		}

		for (int i = 0; i < 4; i++)
		{
			table.getColumn(i).pack();

		}
	}

	static public void baueTabelleTidBewertung(Group group, Table table,
			Display dis, int tid)
	{

		ThreadAttribStoreI tstore_g = null;
		table.removeAll();

		int tableanz = table.getItemCount();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		Swttool.baueTabellenkopf(
				table,
				"Pos#Datum#Lastpostid#Usreanz#mittlRang#gUser#sUser#badUser#-Postings#+Postings#neuGutUser#neuSchlUser#neuBadUser#Bemerkung");

		String attribpfad = "\\db\\userthreadvirtualkonto\\einzelbewertung\\attribute";
		tstore_g = new ThreadAttribStoreI(tid, "\\db\\Attribute\\Threads");

		int anz = tstore_g.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			ThreadAttribObjI tobj = (ThreadAttribObjI) tstore_g.GetObjectIDX(i);
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < 14; j++)
			{
				if (j == 0)
					item.setText(j, String.valueOf(i + 1));
				else if (j == 1)
					item.setText(j, tobj.getDatum());
				else if (j == 13)
					item.setText(j, tobj.getHandelshinweis());
				else if (j > 1)
					item.setText(j, String.valueOf(tobj.getAttribvalue(j - 2)));

			}
		}

		for (int i = 0; i < 14; i++)
		{
			table.getColumn(i).pack();
		}

	}

	private static void setTableline(TableItem item, String line, String name,
			int rang)
	{
		// setzte eine Tabellenzeile
		// die elemente sind mit # getrennt

		int n = Tools.countZeichen(line, "#");

		// setzt die ersten beiden spalten

		// name
		item.setText(0, name);
		// rang
		item.setText(1, String.valueOf(rang));

		for (int index = 2; index < n + 2; index++)
		{
			try
			{
				String linex = new String(SG.nteilstring(line, "#", index - 1));
				item.setText(index, linex);
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static public void baueTabelleRangliste_ext(UserDB udb, Display dis)
	{
		Shell sh = new Shell(dis);
		sh.setLayout(new GridLayout());
		Table table = new Table(sh, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Name");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Rang");
		column = new TableColumn(table, SWT.NONE);
		column.setText("RangPoints");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Anmeldetage");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Themenanz");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Antwortanz");
		column = new TableColumn(table, SWT.NONE);
		column.setText("Postinglisteanz");
		column = new TableColumn(table, SWT.NONE);
		column.setText("NeueAktThreads");
		column = new TableColumn(table, SWT.NONE);
		column.setText("NeueSofaThreads");
		column = new TableColumn(table, SWT.NONE);
		column.setText("durchschnbeitraege");
		column = new TableColumn(table, SWT.NONE);
		column.setText("gewinnrang");
		column = new TableColumn(table, SWT.NONE);
		column.setText("gewinnrangpunkte");

		int anz = udb.GetanzObj();

		// baut die einzelnen zeilen der Tabelle auf
		for (int i = 0; i < anz; i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			UserDbObj uobj = (UserDbObj) udb.GetObjectIDX(i);
			setTableline(item, uobj.getRankinginfostring2(),
					uobj.getUsername(), uobj.getRang());
		}

		for (int i = 0; i < 11; i++)
		{
			table.getColumn(i).pack();
		}
		sh.pack();
		sh.open();
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
	}

	static public void baueTabellePuschertexte(Display dis, Table table,
			ArrayList<String> puscherliste, HashMap<String, Boolean> selmap)
	{

		table.removeAll();
		table.clearAll();

		Swttool.baueTabellenkopfDispose(table, "Puschertext");

		// Diese Tabelle baut die Liste der User die beobachtet werden auf
		int anz = puscherliste.size();

		for (int i = 0; i < anz; i++)
		{
			String ptext = puscherliste.get(i);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, ptext);

			if (selmap.containsKey(ptext))
			{
				// schon da dann nimm wert
				Boolean selektor = selmap.get(ptext);
				item.setChecked(selektor);
			} else
			{
				// nicht da dann setze neu auf true
				item.setChecked(true);
				selmap.put(ptext, true);
			}
		}

		table.getColumn(0).pack();

	}

	static public void baueTabellePuscherFilesMulti(Display dis,
			Suchliste suchliste, Table table, HashMap<Integer, String> hmap,
			BoersenBlaetterDB boerdb)
	{
		// Baut eine Tabelle mit gefunden Files auf
		// Diese Files beinhalten also das Suchwort
		// sl: Fileliste mit den gefundenen Files <name>
		// hmap: position in der tabelle/filename
		// ausserdem die suchpatterns die gefunden wurden

		table.removeAll();
		hmap.clear();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);
		String aktdat = Tools.entferneZeit(Tools.get_aktdatetime_str());

		Color black = dis.getSystemColor(SWT.COLOR_BLACK);
		Color green = dis.getSystemColor(SWT.COLOR_GREEN);
		Color magenta =dis.getSystemColor(SWT.COLOR_DARK_MAGENTA);
		Color red = dis.getSystemColor(SWT.COLOR_RED);

		Swttool.baueTabellenkopfDispose(table,
				"Pos#Boerblatt#Datum#Filename#Suchwoerter#Patterns");

		int hanz = suchliste.getHauptkeyanz();
		int ipos = 0;

		// gehe durch alle hauptkeys, hauptkeys sind z.B. commerzbank, allianz
		for (int i = 0; i < hanz; i++)
		{
			// Anzahl der Subindexes holen
			int ianz = suchliste.getResultanz(i);

			if (ianz == 0)
				continue;

			// gehe durch die subkeys, subkeys sind z.b. wkn, keywort1, keywort2
			for (int j = 0; j < ianz; j++)
			{

				Swttool.wupdate(dis);

				TableItem item = new TableItem(table, SWT.NONE);

				// pos
				item.setText(0, String.valueOf(ipos));

				// Boerblatt
				String such = suchliste.getResult(i, j);
				String boerblatt = SG.nteElementHinten(such, "\\", 2);
				item.setText(1, boerblatt);

				// Datum
				String fnam = such.substring(such.lastIndexOf("\\") + 1);
				String fdat = fnam.substring(0, fnam.indexOf("_"));
				item.setText(2, fdat);

				String anfdatum=null;
				try
				{
					anfdatum = Tools.subTimeHours(aktdat, 72);
				} catch (DateExcecption e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//aktuelles datum rot
				if (Tools.datum_ist_aelter_gleich(aktdat, fdat) == true)
					item.setForeground(red);
				else if(Tools.datum_im_intervall(fdat, anfdatum, aktdat)==true)
					item.setForeground(magenta);
				//wenn schon gesehen dann black, ansonsten green
				else if (boerdb.isNew(fnam) == true)
					item.setForeground(green);
				else
					item.setForeground(black);

				// Filename
				item.setText(3, fnam.substring(fnam.indexOf("_")));

				// Suchwort
				item.setText(4, suchliste.getIDXHauptkey(i));

				hmap.put(ipos, "\\" + boerblatt + "\\" + fnam);

				// Hier müsste man die Patterns einfügen, also die foundpatterns
				// für ein
				// bestimmtes file zu einem Hauptindex i werden ausgegeben
				item.setText(5, suchliste.getFoundpattern(i, j));

				ipos++;

			}
		}

		for (int i = 0; i < 6; i++)
		{
			table.getColumn(i).pack();
		}
	}

}
