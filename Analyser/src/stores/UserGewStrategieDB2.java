package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.IsValid;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;
import java.util.HashSet;

import mainPackage.GC;
import objects.UserDbObjSort;
import objects.UserEinPostingGewinnObj;
import objects.UserGewStrategieObjII;
import objects_gewinnausgabe.Rankingliste;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import bank.Bank;
import db.DB;

public class UserGewStrategieDB2 extends DB
{
	// Diese Liste geht über alle usernamen, für jeden Usernamen werden
	// informationen gehalten wie
	// sich sein gewinn zusammenstellt.
	// Ausserdem wird für jeden User festgehalten welche Gewinnstrategie er
	// verfolgt

	// static Prop p = new Prop();
	private int dbopenflag = 0;
	private static HashMap<String, Integer> hmap = new HashMap<String, Integer>();

	// einige Definitionen für den Masteruser
	int[] maxfaktor_G =
	{ 5000, 12000, 20000 };
	int[] gefaelle_G =
	{ 5, 20, 40 };
	int[] startsumme_G =
	{ 100, 1000, 20000 };
	int[] maxdays_G =
	{ 10, 15, 20, 25, 30 };
	int[] minguteUser_G =
	{ 5, 20, 40 };
	float[] guteUserfaktor_G =
	{ 1.2f, 1.9f, 2.2f };
	float[] gutePostingsfaktor_G =
	{ 1.2f, 1.9f, 2.2f };
	int[] minaktivitaet_G =
	{ 3, 8, 15 };

	//rootpath für die Gewinnauswertung z.b. "UserThreadVirtualKonto\\Summengewinnedb"
	//oder UserThreadVirutalKonto\\Bank\\Gewinnverlaufdb
	private String rootpath_g=null;
	
	// maxfaktor_G: 5000-15000 [5]
	// gefaelle_G: 5-40 [5]
	// startsumme_G: 100-30000 [5]
	// maxdays_G: 10-30 [5]
	// minguteUser_G: 5-40 [5]
	// guteUserfaktor_G: 0.9-2.5 [5]
	// gutePostingsfaktor_G: 0.9-2.5 [5]
	// minaktivität_G: 3-15[5]
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	public UserGewStrategieDB2(String rootpath)
	{
		rootpath_g=new String(rootpath);
		Tracer.WriteTrace(20, "Info:Kontstruktor Usergewinne");
		LoadDB("usergewinne", null, 0);
		HmapInit();
		HmapKonsistenzcheckUserMengencheck();
		HmapKonsistenzcheckGrössencheck();
		// xxxx hier muss noch überprüft werden wieviele millisekunden der
		// Plausicheck kostet ??

	}

	private void HmapKonsistenzcheckUserMengencheck()
	{
		HashSet<String> usermenge = new HashSet<String>();
		// prüft nach ob alle usernamen nur einmal vorkommen
		int anz = GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII uIIobj2 = (UserGewStrategieObjII) this
					.GetObjectIDX(i);
			String nam = uIIobj2.getUsername();

			if (usermenge.contains(nam) == true)
				Tracer.WriteTrace(10, "Error: plausicheck username<" + nam
						+ "> ist doppelt in usergewinne.db");
			else
				usermenge.add(nam);
		}
	}

	private void HmapKonsistenzcheckGrössencheck()
	{
		// Konsistenzcheck(Grössenscheck) der hmap
		int anzh = hmap.size();
		int anz = this.GetanzObj();

		if (anzh != anz)
			Tracer.WriteTrace(10,
					"Error: inkonistente Hmap bei userVerwaltungObjII anzhmap<"
							+ anzh + "> != anzobjekte<" + anz + ">");
	}

	public UserGewStrategieObjII sucheUser(String username)
	{
		HmapKonsistenzcheckGrössencheck();
		// sucht den User in der Liste
		if (hmap.containsKey(username) == true)
		{
			int pos = hmap.get(username);

			UserGewStrategieObjII uIIobj2 = (UserGewStrategieObjII) this
					.GetObjectIDX(pos);

			if (uIIobj2.getUsername().equalsIgnoreCase(username))
			{
				return uIIobj2;
			} else
				Tracer.WriteTrace(10, "Error: internal hmap indexfehler pos<"
						+ pos + "> erwusername<" + username + "> getusername<"
						+ uIIobj2.getUsername() + ">");
		}
		return null;
	}

	private void updateHmap(String username)
	{
		// der username 'username' wird in der hmap gespeichert

		int anz = GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII uIIobj2 = (UserGewStrategieObjII) this
					.GetObjectIDX(i);
			if (uIIobj2.getUsername().equalsIgnoreCase(username) == true)
			{
				if (hmap.containsKey(username) == false)
				{
					hmap.put(username, i);
					return;
				}
			}
		}
		Tracer.WriteTrace(10, "Error: internal username<" + username
				+ "> konnte nicht in der Userverwaltung gefunden werden");
	}

	private void HmapInit()
	{
		// Die Hmap wird initialisiert
		int anz = GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII uIIobj2 = (UserGewStrategieObjII) this
					.GetObjectIDX(i);
			String unam = uIIobj2.getUsername();

			if (hmap.containsKey(unam) == false)
				hmap.put(unam, i);
		}
	}

	private void updateGenMasteruserI(UserGewStrategieObjII uIIobj2, int ind)
	{
		// die Parameter des iten Masterusers werden upgedatet
		// username_G: wird vorgegeben
		// handelsstrategie_G: 1 (fix)
		// sliderindex_G: 3 (fix)
		// maxfaktor_G: 5000-15000 [5]
		// minfaktor_G: 0.1 (fix)
		// gefaelle_G: 5-40 [5]
		// startsumme_G: 100-30000 [5]
		// maxdays_G: 10-30 [5]
		// vertrauensfaktorAlgo_G: 1 (fix)
		// minguteUser_G: 5-40 [5]
		// guteUserfaktor_G: 0.9-2.5 [5]
		// gutePostingsfaktor_G: 0.9-2.5 [5]
		// minaktivitaet_G: 3-15[5]

		uIIobj2.setHandelsstrategie_G(1);
		float maxf = maxfaktor_G[ind % 3];
		uIIobj2.setMaxfaktor_G(maxf);
		uIIobj2.setMinfaktor_G(0.1f);
		int gef = gefaelle_G[(ind / 3) % 3];
		uIIobj2.setGefaelle_G(gef);
		int starts = startsumme_G[(ind / (3 * 3)) % 3];
		uIIobj2.setStartsumme_G(starts);
		int maxd = maxdays_G[(ind / (3 * 3 * 3)) % 3];
		uIIobj2.setMaxdays_G(maxd);
		uIIobj2.setVertrauensfaktorAlgo_G(1);
		int ming = minguteUser_G[(ind / (3 * 3 * 3 * 3)) % 3];
		uIIobj2.setMinguteUser_G(ming);
		float guf = guteUserfaktor_G[(ind / (3 * 3 * 3 * 3 * 3)) % 3];
		uIIobj2.setGuteUserfaktor_G(guf);
		float gup = gutePostingsfaktor_G[(ind / (3 * 3 * 3 * 3 * 3 * 3)) % 3];
		uIIobj2.setGutePostingsfaktor_G(gup);
		int minak = minaktivitaet_G[(ind / (3 * 3 * 3 * 3 * 3 * 3 * 3)) % 3];
		uIIobj2.setMinaktivität_G(minak);
		// definiere dies als Masteruser
		uIIobj2.setUsertype_G(1);
		// verwende den 20Tage Slider
		uIIobj2.setSliderindex_G(GC.SLIDERINDEX20TAGE);
	}

	public void initMasteruser()
	{
		// initialisiert die ganzen Masteruser und dessen Handelsstrategien
		int ind = 0;
		UserGewStrategieObjII uIIobj2 = null;

		for (ind = 0; ind < 6561; ind++)
		{
			String unam = "MasteruserI" + ind;
			// objekt noch nicht da, dann generiere
			if ((uIIobj2 = sucheUser(unam)) == null)
			{
				uIIobj2 = new UserGewStrategieObjII(unam);
				this.AddObject(uIIobj2);

			}
			if (ind % 1000 == 0)
				System.out.println("i<" + ind + ">");
			updateGenMasteruserI(uIIobj2, ind);
			this.HmapInit();
		}
		this.WriteDB();
	}

	public boolean addGewinnObj(int verfahrenindex, String username,
			UserEinPostingGewinnObj usergewinn)
	{
		// verfahrenindex: es gibt mehrere 15 Alorithmen nach denen Gewinne
		// berechnet werden
		// Ein Gewinnobjekt ist ein Aktienkauf der sofort gehandelt wird
		// Zum Zeitpunkt x wird gekauft und sogleich zu einem festen Zeitpunkt
		// verkauft
		// Beim Verkauf wird ein Gewinnobjekt generiert
		UserGewStrategieObjII uIIobj2 = null;

		// a)Falls der User schon bekannt
		if ((uIIobj2 = sucheUser(username)) != null)
		{
			// user gefunden dann store das Gewinnobjekt
			uIIobj2.storeEinzelgewinn(verfahrenindex, username, usergewinn,rootpath_g);
			return true;
		}

		// b)Falls der User neu ist
		// schaue nach ob der neue User einen korrekten Namen hat
		if (IsValid.checkFilesystemname(username) == false)
		{
			Tracer.WriteTrace(20, "Warning:username <" + username
					+ "> nicht erlaubt !!! ");
			return false;
		}

		// lege den neuen User an
		UserGewStrategieObjII uIIobj = new UserGewStrategieObjII(username);
		uIIobj.storeEinzelgewinn(verfahrenindex, username, usergewinn,rootpath_g);
		this.AddObject(uIIobj);
		this.updateHmap(username);
		this.WriteDB();
		return true;
	}

	public void GibZwischenGewinnListeAusCalcGewinnDelMem(int erstflag,
			int auswertungsflag, int threadmaxzaehler, Bank bank, int boniflag,String vprefix)
	{
		// gibt für alle user die Gewinne aus
		// erstflag:
		// falls das erstflag =1 ist werden die alten ergebnisse gelöscht
		// nach der Gewinnausgabe werden die internen speicher für die bereits
		// berechneten Gewinne gelöscht
		// auswertungsflag:

		if(vprefix == null)
			vprefix="";
		else
			vprefix=vprefix.concat("\\");
		
		Rankingliste rliste = new Rankingliste(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\"+vprefix+"ZwischenGewinnliste.txt", 0);

		String guteUser = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\"+vprefix+"guteUserBoni.txt";
		Inf infgut = new Inf();
		infgut.setFilename(guteUser);

		if (boniflag == 1)
			infgut.writezeile(Tools.get_aktdatetime_str()
					+ ":Bonuszahlung*******");

		// dies ist eine Referenzliste wo nur der 'einfache' alg. als
		// maximumgewinn gewertet wird

		if (erstflag == GC.LOESCHE)
			this.cleanVerzeichnisse();

		// prüft ob kein User doppelt
		HmapKonsistenzcheckUserMengencheck();

		int i = 0;
		int useranz = GetanzObj();
		for (i = 0; i < useranz; i++)
		{
			// moni.begin("Schleife");
			if (i % 1000 == 0)
				System.out.println("i<" + i + "> Zwischengewinnliste");
			if (i % 15 == 0)
				System.out.print(".");

			float points = 0;
			float bonus = 0;
			// hier wird genau der Gewinn für einen User ausgewertet
			UserGewStrategieObjII userGewStratObjII = (UserGewStrategieObjII) GetObjectIDX(i);

			// gesammtgewinn um den depotgewinn erhohen
			float gesgew = userGewStratObjII.getGesGewinn();
			gesgew = gesgew
					+ bank.getGesammtGewinn(userGewStratObjII.getUsername());
			userGewStratObjII.setGesGewinn(gesgew);

			if (erstflag == GC.LOESCHE)
			{
				userGewStratObjII.setGesGewinn(0);
				userGewStratObjII.setGewinnhandlungen_g(0);
				userGewStratObjII.setGewinnpostings_G(0);
				userGewStratObjII.setVerlustpostings_G(0);
				userGewStratObjII.setAnzneutralpostings_G(0);
			}

			userGewStratObjII.CalcReportUsergewinneDelMem(erstflag,
					auswertungsflag);

			// baue neues objekt und füge in die rankingliste ein
			UserDbObjSort udbo_s = new UserDbObjSort();
			udbo_s.setUserverwobj(userGewStratObjII);
			udbo_s.setName(userGewStratObjII.getUsername());

			// rechnet die gewinnpunkte aus
			// Setzt den gesammtgewinn für die Tabelle

			udbo_s.setGewinn(userGewStratObjII.getGesGewinn());
			points = userGewStratObjII.getGesGewinn();

			// holt Gewinn und Verlustpostings
			int gewp = userGewStratObjII.getGewinnpostings_G();
			int verlp = userGewStratObjII.getVerlustpostings_G();
			int neutrp = userGewStratObjII.getAnzneutralpostings_G();

			udbo_s.setAnzGewinne(gewp);
			udbo_s.setAnzVerluste(verlp);
			udbo_s.setAnzNeutral(neutrp);

			if ((boniflag == 1) && (gewp > 10))
			{
				bonus = boniZahlungen(infgut, points, bonus, userGewStratObjII,
						gewp, verlp, neutrp);
				points = points + bonus;
			}
			// nach den points wird sortiert
			udbo_s.setPoints(points);

			String gewinninf = new String("");
			gewinninf = gewinninf.concat(":"
					+ userGewStratObjII.getGewinnhandlungen_g());
			udbo_s.setGewinninfostring(gewinninf);

			rliste.add(udbo_s);

			// moni.end(".........end Schleife");
		}

		rliste.sort();
		// nach dem sortieren kann man die Ränge setzen

		rliste.gibListeAus(threadmaxzaehler);
		rliste.gibHtmlListeAus(threadmaxzaehler);

		this.WriteDB();

		// lösche den Speicher
		rliste = null;

		// hier wird nur der zaehler zurückgesetzt
		UserEinPostingGewinnObj.cleanObjektzaehler();

		System.out.println("Zwischengewinnlistenausgabe fertig ");
	}

	private float boniZahlungen(Inf infgut, float points, float bonus,
			UserGewStrategieObjII userGewStratObjII, int gewp, int verlp,
			int neutrp)
	{
		// Bonizahlungen gibt es nur bei mehr als 100Postings

		String boniprozent = "";
		if ((gewp > (verlp * 50)) && (gewp + verlp > 100))
		{// 1000% Bounus, falls gewpostings 50 mal so hoch wie
			// verlustpostings
			bonus = points * 10;
			boniprozent = "1000%";
		} else if (gewp > (verlp * 10))
		{// 200% Bounus, falls gewpostings 10 mal so hoch wie
			// verlustpostings
			bonus = points * 2;
			boniprozent = "200%";
		} else if (gewp > (verlp * 2))
		{// 50% Bonus, falls gewpostings doppelt so hoch wie
			// verlustpostings
			bonus = points * 0.5f;
			boniprozent = "50%";
		} else if (gewp > verlp)
		{// 20% Bonus, falls gewpostings grösser als verlustpostings
			bonus = points * 0.2f;
			boniprozent = "20%";
		}
		if (boniprozent.length() > 2)
			infgut.writezeile("Guter User usernam<"
					+ userGewStratObjII.getUsername() + "> get Bonus<"
					+ boniprozent + "> points<" + points + "> bonus<" + bonus
					+ "> #gewpost<" + gewp + "> #verlustpost<" + verlp
					+ "> #neutral<" + neutrp + ">");
		return points;
	}

	private void cleanVerzeichnisse()
	{
		int z = 0;
		String fnam = null, fullnam = null;
		// beim ersten Mal alle Files löschen
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Summengewinne", 1);
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Summengewinn<" + z + ">");
			z++;
			fullnam = GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\Summengewinne\\" + fnam;
			FileAccess.FileDelete(fullnam,0);
		}
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Einzelgewinne", 1);
		z = 0;
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Einzelgewinn<" + z + ">");
			z++;

			fullnam = GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\Einzelgewinne\\" + fnam;
			FileAccess.FileDelete(fullnam,0);
		}

		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Summengewinnedb", 1);
		z = 0;
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Summengewinnedb<" + z + ">");
			z++;
			fullnam = GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\Summengewinnedb\\" + fnam;
			FileAccess.FileDelete(fullnam,0);
		}
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Masteruserverlauf", 1);
		z = 0;
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Masteruserverlauf<" + z + ">");
			z++;
			fullnam = GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\Masteruserverlauf\\"
					+ fnam;
			FileAccess.FileDelete(fullnam,0);
		}
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Masteruserlisten", 1);
		z = 0;
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Masteruserlisten<" + z + ">");
			z++;
			fullnam = GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\Masteruserlisten\\" + fnam;
			FileAccess.FileDelete(fullnam,0);
		}
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Verlauf", 1);
		z = 0;
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Verlauf<" + z + ">");
			z++;
			fullnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\Verlauf\\"
					+ fnam;
			FileAccess.FileDelete(fullnam,0);
		}
		FileAccess.initFileSystemList(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb", 1);
		z = 0;
		while ((fnam = FileAccess.holeFileSystemName()) != null)
		{
			if (z % 1000 == 0)
				System.out.println("lösche Gewinnverlaufdb<" + z + ">");
			z++;
			fullnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb\\"
					+ fnam;
			FileAccess.FileDelete(fullnam,0);
		}
	}

	public void calcGewinnraenge(Text aktion, Display dis,ProgressBar pb)
	{
		// Aus den einzelen Usergewinnen wird ein zugehöriger Gewinnrank
		// berechnet.
		// Vorraussetzung für diese Funktion ist das die einzelen Usergewinn
		// zuvor berechnet wurden.

		String gfile = GC.rootpath + "\\db\\UserThreadVirtualKonto\\Gliste.txt";
		Rankingliste gliste = new Rankingliste(gfile, 0);
		this.ResetDB();

		
		
		// baue eine Liste auf die anschliessend nach der Gewinnsumme sortiert
		// wird
		int anz = this.GetanzObj();
		pb.setMinimum(0);
		pb.setMaximum(anz);
		if(aktion!=null)
		aktion.setText("1:Erstelle Gewinnsummenliste Gliste.txt");
		Swttool.wupdate(dis);
		for (int i = 0; i < anz; i++)
		{
			
			// holt das gewinnobj
			UserGewStrategieObjII userverwaltobj = (UserGewStrategieObjII) GetObjectIDX(i);

			// erstellt ein gwinnobjekt was sortiert wird
			UserDbObjSort gsort = new UserDbObjSort();
			gsort.setGewinn(userverwaltobj.getGesGewinn());
			gsort.setName(userverwaltobj.getUsername_G());
			gsort.setAnzGewinne(userverwaltobj.getGewinnpostings_G());
			gsort.setAnzVerluste(userverwaltobj.getVerlustpostings_G());
			gsort.setUserverwobj(userverwaltobj);
			gliste.add(gsort);

			// hier wird nur mal zum Test ausgegeben
			// gliste.gibListeAus(5);
		}
		gliste.sort();
		gliste.gibListeAus(5);

		// setzt die Raenge in der usergewinnliste
		// Skaliere alles auf 100000
		anz = gliste.getanzObj();
		if(aktion!=null)
		aktion.setText("2:Setzte Rang in usergewinne");
		float gfaktor = (100000f / anz);
		for (int i = 0; i < anz; i++)
		{
			pb.setSelection(i);
			Swttool.wupdate(dis);
			UserDbObjSort gsort = gliste.getObjektIDX(i);
			String unam = gsort.getName();

			// setzt den Rank in usergewinne
			UserGewStrategieObjII userverwaltobj = this.sucheUser(unam);
			float gf = i * gfaktor;
			userverwaltobj.setGewinnrank((int) gf);
		}
	}
	public void postprocess()
	{}
}
