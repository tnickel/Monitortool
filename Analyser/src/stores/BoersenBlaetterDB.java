package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import objects.BoersenblattDbObj;
import objects.KeyDbObj;
import objects.Keyword;
import db.DB;

public class BoersenBlaetterDB extends DB
{
	// Dies ist die PuscherDB die s�mtliche Puscher(B�rsenbl�tter enth�lt)
	// hier sind auch die keyw�rter f�r die einzelnen b�rsenbl�tter drin

	// Gesammte Symbolemenge: Dies ist die Menge aller bekannten keyw�rter. Also jedes keywort
	// hieraus kommt in mindestens einem b�rsenblatt vor
	private HashSet<String> symbol_foundgesmenge = new HashSet<String>();

	// Symbolemenge betrachtet nach Boersennbl�ttern
	// Bsp:Amazon, (keywort1, keywort2,keywort3)
	// Canon, (keywortx1,... keywortxy)
	//Hier werden Symbolmengen f�r einzelne B�rsenbl�tter betrachtet.
	//Es wird genau gepseichert werlche Symbolmengen in einem Bestimmten B�rsenblatt vorkommen
	//z.B. Boerse.de
	HashMap<String, HashSet<String>> boermenge = new HashMap<String, HashSet<String>>();

	public static String calcBoername(String fnam)
	{
		// Aus dem Fullfilenamen wird der boernamen extrahiert
		// Bsp: m:\Mail\text\DerGoldreport\2011-11-14_170609_Textbody.txt
		// Boername=DerGoldreport

		// hinten abschneiden
		fnam = fnam.substring(0, fnam.lastIndexOf("\\"));
		// vorne abschneiden
		fnam = fnam.substring(fnam.lastIndexOf("\\") + 1);
		return fnam;
	}

	public HashSet<String>getBoerGesMenge()
	{
		return symbol_foundgesmenge;
	}
	public HashMap<String, HashSet<String>> getBoerMenge()
	{
		return boermenge;
	}
	public BoersenBlaetterDB()
	{
		LoadDB("boersenblaetterdb", null, 0);

		//Bilde die 2 Symbolmengen f�r Boersenbl�tter von maximalen Alter von 20 tagen
		CalcSymbolmenge(20);
	}

	private void CalcSymbolmenge(int maxtage)
	{
		//Hier werden die Symbolmengen �ber alle Boersenbl�tter gebildet
		//a)gesammtmenge b) mit betrachtung des boersenblattes
		// maxtage: Betrachte nur boersenbl�tter die maximal 20 Tage alt sind

		String aktdate = Tools.get_aktdatetime_str();
		aktdate = Tools.entferneZeit(aktdate);
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			BoersenblattDbObj bo = (BoersenblattDbObj) this.GetObjectIDX(i);
			String boerblattdate=bo.calcMsgDate();
			
			//Alte boersenblaetter werden nicht betrachtet
			if(Tools.zeitdifferenz_tage(aktdate, boerblattdate)>maxtage)
				continue;
			
			//keylist ist die liste mit Suchw�rtern (jedes Boerblatt hat eine liste
			//mit bereits gefundenen Keyw�rtern
			ArrayList<Keyword> keylist = bo.getKeylist();
			String boerblattname = bo.getBoerblattname();

			
			
			
			int anzk = keylist.size();
			for (int j = 0; j < anzk; j++)
			{
				String key = keylist.get(j).getKeyword();

				// Nimm symbol in der Gesammtmenge auf
				symbol_foundgesmenge.add(key);

				// Hier werden die Schl�sselmengen f�r das jeweilige
				// boersenblatt erweitert
				HashSet<String> h1 = boermenge.get(boerblattname);
				if (h1 == null)
				{
					//falls boerblatt noch nicht aufgenommen
					HashSet<String> h2 = new HashSet<String>();
					h2.add(key);
					boermenge.put(boerblattname, h2);
				} else
					//schon da, dann nimm key auf
					h1.add(key);
			}
		}
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public void postprocess()
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			BoersenblattDbObj bo = (BoersenblattDbObj) this.GetObjectIDX(i);
			bo.postprocess();
		}
	}

	public Boolean isNew(String fnamsuch)
	{

		BoersenblattDbObj bo = this.getBoerblatt(fnamsuch);
		if (bo == null)
			return true;
		else
			return false;

	}

	public BoersenblattDbObj getBoerblatt(String fnamsuch)
	{
		fnamsuch = fnamsuch.toLowerCase();
		fnamsuch = fnamsuch.replace(".txt", "");
		fnamsuch = fnamsuch.replace(".pdf", "");

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			BoersenblattDbObj bo = (BoersenblattDbObj) this.GetObjectIDX(i);

			String fnam = bo.getFname().toLowerCase();

			// holt den letzten Part des Namens \\bbbb\\cccccc\\name.txt
			if (fnamsuch.contains("\\"))
				fnamsuch = fnamsuch.substring(fnamsuch.lastIndexOf("\\") + 1)
						.toLowerCase();

			if (fnam.contains(fnamsuch) == true)
			{
				return bo;
			}

		}
		// nix gefunden
		return null;
	}

	public boolean AddKeyword(String boerblattname, String fnamtext,
			KeyDbObj keyobj)
	{
		// Es wurde ein Schl�sselwort gefunden und dies wird
		// im entsprechenen B�rsenblatt gespeichert
		// return: true, wenn keyword noch nicht drin und aufgenommen wurde
		// false, wenn keyword schon drin

		String keyword = keyobj.getKeyword();

		int anz = this.GetanzObj();
		// Gehe durch alle B�rsenbl�tter
		for (int i = 0; i < anz; i++)
		{
			BoersenblattDbObj bbobj = (BoersenblattDbObj) this.GetObjectIDX(i);
			String fnam = bbobj.getFname();

			// Suche den B�rsenblatteintrag in der BoersenblattDB
			if (fnam.equals(fnamtext))
			{
				// Wenn das keyword noch nicht drin
				if (bbobj.checkKeyword(keyword) == false)
				{
					bbobj.addKeyword(keyobj);
					return true;
				} else
					// keyword schon drin
					return false;
			}
		}

		// Falls das B�rsenblatt �berhaupt noch nicht angelegt ist dann lege es
		// an
		BoersenblattDbObj bbobj = new BoersenblattDbObj(boerblattname + "#"
				+ fnamtext + "#" + Tools.get_aktdatetime_str());
		this.AddObject(bbobj);

		return true;
	}

	// Erstellt f�r die Schluessenmenge eine Liste von BB wo �berall diese
	// Keyw�rter vorkommen
	public void genBBvorkommen(ArrayList<String> sm, String outnam)
	{
		// Erstellt f�r die Schluesselliste eine Liste von BB wo �berall diese
		// Keyw�rter vorkommen
		int anz = this.GetanzObj();
		int sanz = sm.size();
		String ftext = "";

		FileAccess.FileDelete(outnam, 0);

		Inf inf = new Inf();
		inf.setFilename(outnam);
		// Gehe durch alle B�rsenbl�tter
		for (int i = 0; i < anz; i++)
		{
			BoersenblattDbObj bbobj = (BoersenblattDbObj) this.GetObjectIDX(i);
			int foundflag = 0;
			for (int j = 0; j < sanz; j++)
			{
				String k1 = sm.get(j);
				if (bbobj.checkKeyword(k1) == true)
				{
					foundflag = 1;
					ftext = ftext.concat(k1 + ";");
				}
			}
			// Keywoerter in BBlatt gefunden
			if (foundflag == 1)
			{

				inf.writezeile(bbobj.getBoerblattname() + " "
						+ bbobj.calcMsgDate());
				inf.writezeile(ftext);
				inf.writezeile("..................................................................");
			} else
				inf.writezeile("nix gefunden......................................................");
		}
	}
}
