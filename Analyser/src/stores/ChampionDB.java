package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Keywortliste;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import kurse.KursDbObj;
import kurse.KursValueDB;
import kurse.KurseDB;
import kurse.Kursvalue;
import mainPackage.GC;
import objects.AktDbObj;
import objects.ChampionDbObj;
import objects.Obj;
import championAnalyser.BoerseDe;
import championAnalyser.BoerseMasterliste;
import championAnalyser.Championaktie;
import db.DB;

public class ChampionDB extends DB
{
	public ChampionDB()
	{
		LoadDB("championdb", null, 0);
		exportHanddata();

	}

	private void exportHanddata()
	{
		// Die Symbole und Boersenplätze die von Hand
		// Eingegeben wurden, werden zu Backupzwecken exportiert
		// hier werden die Handdaten in ein datenfile ausgegeben
		// dies dient nur zur Kontrolle !!!
		String fnam = GC.rootpath + "\\db\\champion_handdata.txt";
		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam, 0);

		Inf inf= new Inf();
		inf.setFilename(fnam);
		
		int anz = this.GetanzObj();
		for(int i=0; i<anz; i++)
		{
			ChampionDbObj cobj=(ChampionDbObj)this.GetObjectIDX(i);
			inf.writezeile((i+1)+":"+cobj.getName()+":"+cobj.getSymbol()+":"+cobj.getPressBoerse());
		}
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public ChampionDbObj getChampion(String wkn)
	{
		int maxzaehler = GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			ChampionDbObj campObj = (ChampionDbObj) GetObjectIDX(i);
			String wkno = campObj.getWkn();
			if (wkno.equalsIgnoreCase(wkn) == true)
				return campObj;
		}
		return null;
	}

	public void Init(BoerseDe bde, String filterfile, String fnamout)
	{
		// a) Erstellen der Suchliste
		// b) Erstellen der Boersenplaetze
		// Die suchliste wird erstellt wenn auf einen Button geklickt wird
		// Das wird neu gemacht
		// Autogen wird nach dem folgenden Format erstellt
		// File:AUTOGEN_BoerseDE_Suchliste.txt
		// Suchwort:Church & Dwight
		// Church & Dwight
		// 864371
		// Suchwort:Swedish Match
		// Swedish Match
		// 900439

		Inf inf = new Inf();
		inf.setFilename(fnamout);

		if (FileAccess.FileAvailable(fnamout) == true)
			FileAccess.FileDelete(fnamout, 1);

		// keyliste mit den Suchwörtern einlesen
		Keywortliste keyl = new Keywortliste(",");
		keyl.addFile(filterfile);

		// Das aktuelle Börsenblatt wird betrachtet
		int anzlines = bde.getAnzLines();
		for (int i = 0; i < anzlines; i++)
		{
			// Hole die ite Championaktie aus dem aktuellen Boerblatt
			Championaktie ca = bde.getChampionaktie(i);
			String wkn = ca.getWkn();

			// Hole das passenden ChampDbObj aus der ChampDB
			ChampionDbObj co = (ChampionDbObj) this.getChampion(wkn);

			// prüfe ob die Suchwoerter mit der iten Championaktie korrelieren
			if (ca.checkKeywortliste(keyl) == false)
				continue;
			else
			{// ja eines der suchwörter passt, dann baue das Autogensuchfile
				// passend auf
				inf.writezeile("Suchwort:" + co.getName());
				Keywortliste keysl = new Keywortliste(",");
				keysl.addElem(co.getSuchwoerter());

				int anz = keysl.getanz();
				for (int j = 0; j < anz; j++)
				{
					inf.writezeile(keysl.getObjIdx(j));
				}
			}

		}

		inf.close();
		this.WriteDB();
	}

	public void expand(BoerseMasterliste boerl, AktDB aktdb, ThreadsDB tdb)
	{
		// hier wird die Masterliste erweitert
		// es werden neue WKN´s aufgenommen und Kurse und Börsenplätze angepasst
		// wenn die Wkns nicht in der AKTdb dann wird in der championdb ??
		// gesetzt
		// dies muss später von hand angepasst werden

		String symbol = "???";
		String boerse = "?";
		int status = 0;
		int masterid = 0;
		String adatum = Tools.get_aktdatetime_str();

		int anzblaetter = boerl.getanz();
		for (int i = 0; i < anzblaetter; i++)
		{
			// wertet das Boerblatt i aus
			BoerseDe blatt = boerl.getBoerblattIdx(i);

			int lin = blatt.getAnzLines();
			for (int j = 0; j < lin; j++)
			{
				// Geht durch die einzelnen Aktien eines Blattes
				Championaktie ca = blatt.getChampionaktie(j);
				String wkn = ca.getWkn();

				// schaut nach ob in der Championdb die wkn schon drin ist
				ChampionDbObj cdbo = this.getChampion(wkn);

				if (cdbo == null)
				{
					// die Masterid ermitteln
					AktDbObj aktobj = aktdb.getWknIDX(wkn);

					if (aktobj == null)
					{

						Tracer.WriteTrace(20, "Info: wkn<" + wkn
								+ "> nicht in aktdb bekannt");
						// trace diese unbekannten wkns
						Inf inf = new Inf();
						inf.setFilename(GC.rootpath
								+ "\\db\\reporting\\ChampionUnbekannteWkns.txt");
						inf.writezeile("Aktname<"
								+ ca.getAktnam()
								+ "> wkn<??> symbol<??> bitte in champion.db ergänzen");
						inf.close();

						symbol = "???";
						boerse = "?";

						status = 99;
					} else
					{ // werte in aktdb vorhanden
						symbol = aktobj.getSymbol();
						masterid = aktobj.getMasterid();
						boerse = aktobj.getBoerse();
						status = 0;
					}

					// Neuaufnahme in die Champion db
					ChampionDbObj cdbo1 = new ChampionDbObj();
					cdbo1.setMasterid(masterid);
					cdbo1.setName(ca.getAktnam());
					cdbo1.setStatus(status);
					cdbo1.setSymbol(symbol);
					cdbo1.setWkn(wkn);
					cdbo1.setPressBoerse(boerse);
					cdbo1.setAdatum(adatum);
					cdbo1.setSuchwoerter(ca.getAktnam() + "," + wkn);
					this.AddObject(cdbo1);
				}
			}
		}
		this.WriteDB();

		// Hier wird kurse.db erweitert
		// evtl. sind neue symbole durch champion.db hinzugekommen,
		// diese müssen in kurse.db erweitert werden damit diese
		// dann auch beim Laden der kurse berücksichtigt werden

		KurseDB kdb = new KurseDB(tdb);

		// gehe durch alle champions und schaue nach ob die symbole der
		// champioins auch in
		// kurse.db vorhanden sind. Wenn nicht vorhanden werden diese Symbole
		// dort aufgenommen
		int maxzaehler = GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			ChampionDbObj campObj = (ChampionDbObj) GetObjectIDX(i);
			String symb = campObj.getSymbol();

			// Falls symbol schon in kdb ist
			if (kdb.GetSymbolIDX(symb) > 0)
				continue;
			else
			{
				// Symbol ist noch nicht in kdb
				if (symb.contains("?") == true)
					continue;

				// symbol von champion.db noch nicht in kurse.db
				// dann nimm dies auf.
				Tracer.WriteTrace(20, "Info: symb<" + symb + "> boerse<"
						+ "???" + "> zur kurse.db aufgenommen");
				kdb.addKurs(symb, "???von Champdb");
				kdb.WriteDB();
			}
		}

	}

	public void pressSymboleKdb(KurseDB kdb)
	{
		// Hier werden die Symbole nach Kurse.db durchgepresst
		// d.h. wenn in kurse.db das symbol noch nicht vorhanden
		// dann wird dies aus champion.db genommen
		int anz = this.GetanzObj();

		if (anz == 0)
			Tracer.WriteTrace(10, "Error: champdb nicht vorhanden");

		for (int i = 0; i < anz; i++)
		{
			System.out.println("i="+i+"pressing champdb in kurse");
			
			ChampionDbObj champobj = (ChampionDbObj) this.GetObjectIDX(i);
			String symb = champobj.getSymbol();
			String boer = champobj.getPressBoerse();

			if (boer == null)
				boer = new String("null");

			if ((symb == null) || (symb.contains("?")) || (boer.contains("?")))
				continue;

			KursDbObj kobj = kdb.holeKursobj(symb);
			String ksymb = kobj.getSymbol();
			String kboer = kobj.getBoerse();

			if ((symb.equalsIgnoreCase(ksymb) == false)
					|| (boer.equalsIgnoreCase(kboer) == false))
			{
				// daten von champion.db und boerse.db ungleich, dann setze neu
				kobj.setSymbol(symb);
				kobj.setBoerse(boer);

				Tracer.WriteTrace(20, "Info: pos<" + i + "> Symbol<" + symb
						+ "> Boerse<" + boer + "> neu in KurseDB gesetzt");
			}
		}
		kdb.WriteDB();

	}

	public float HoleKurs(String wkn, String datum)
	{
		int maxzaehler = GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			ChampionDbObj campObj = (ChampionDbObj) GetObjectIDX(i);
			String wkno = campObj.getWkn();
			if (wkno.equalsIgnoreCase(wkn) == true)
			{
				// wkn gefunden
				String symb = campObj.getSymbol();
				KursValueDB kvdbc_g = new KursValueDB(symb, 0, null);
				Kursvalue k = kvdbc_g.holeKurswert(datum, 0);
				return k.getKv();
			}
			;
		}

		Tracer.WriteTrace(10, "Error: wkn<" + wkn + "> nicht in champion.db");
		return -1000;

	}

	public String HoleSymbol(String wkn)
	{
		int maxzaehler = GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			ChampionDbObj campObj = (ChampionDbObj) GetObjectIDX(i);
			String wkno = campObj.getWkn();
			if (wkno.equalsIgnoreCase(wkn) == true)
			{
				// wkn gefunden
				String symb = campObj.getSymbol();
				return symb;
			}
			;
		}

		Tracer.WriteTrace(10, "Error: wkn<" + wkn + "> nicht in champion.db");
		return "??xx??";

	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public void postprocess()
	{
	}
}
