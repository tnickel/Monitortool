package objects;

//Hier werden die Task reingepackt die auf den Basisklassen laufen.
import hilfsklasse.Swttool;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.util.HashSet;

import mainPackage.GC;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import ranking.UserStatistik;
import stores.AktDB;
import stores.MidDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;

public class Reporting
{
	// Dies ist die Beobachtungsklasse, hier werden user und Threads beobachtet

	private UserStatistik ustat = new UserStatistik();

	public void StartAktualisiereObserveUser(int alluserflag,
			String observeuser, int mindestzeit, int usergewinnrangflag,
			ProgressBar pb, Text textinfo, Display dis, String pref)
	{
		// alles in dem Verzeichnis ..db//userdata wird aktualisiert
		// D.h es wird geschaut ob ein neuer thread hinzugekommen ist. Wenn ja
		// nimm die info in aktdb und threaddb auf (+ aktuelles datum)
		int maxuser = 0;
		String rangfile = null;
		// alltimeflag= wenn 1 dann
		// mindestzeit= wurde ein thread upgedatet muss mindestens die
		// mindestzeit vergangen sein bis er wieder
		// upgedatet wird

		// a)gehe durch die Userliste (es werden die Userobjekte geladen)
		// b)aktualisiere für diesen User die Observerdaten
		// die Observerdaten werden in dieser Klasse gehalten
		// BAWorkUser...
		// Für den User wird die aktuelle Threadliste geladen, dann wird
		// geschaut
		// ob neuere Postings in den Threads sind, wenn ja wird der Thread
		// aktualisiert
		UserGewStrategieDB2 ugewinnedb = new UserGewStrategieDB2(
				"UserThreadVirtualKonto\\Summengewinnedb");

		Tracer.WriteTrace(30, this.getClass().getName());

		UserDB udb = new UserDB(observeuser, "boostraenge.txt");
		maxuser = udb.GetanzObj();
		UserDbObj userobj;
		ThreadsDB tdb = new ThreadsDB();
		MidDB middb= new MidDB(tdb);
		AktDB aktdb = new AktDB();
		udb.ResetDB(-1);
		DownloadManager dm = new DownloadManager(GC.MAXLOW);
		// geht durch die Userliste, hole nur die User die beobachtet werden
		// müssen

		if (alluserflag == 0)
			maxuser = udb.SetDurchlaufmode(GC.MODE_OBSERVE);
		else
			maxuser = udb.SetDurchlaufmode(GC.MODE_ALL);
		int i = 1;
		if (pb != null)
		{
			pb.setMinimum(0);
			pb.setMaximum(maxuser);
			pb.setSelection(0);
		}
		if (textinfo != null)
			textinfo.setText("1/2 Lade Userdaten von <" + maxuser + "> usern");
		while ((userobj = (UserDbObj) udb.GetNextObject()) != null)
		{
			if (pb != null)
				pb.setSelection(i);
			Swttool.wupdate(dis);

			System.out.println("Bearbeite user <" + i + "|" + maxuser + ">");
			// läd aktuelle Beiträge von Usern und speichert updates
			// in ../userdata/postingliste/<username>.db
			Tracer.WriteTrace(20, Reporting.class.getName() + "<" + i + "|"
					+ maxuser + ">user<" + userobj.get_username() + ">");

			// lade die Userinfo herunter (also wo hat der user gepostet)
			userobj.WebUpdateStep3(dm, mindestzeit);

			dm.waitEnd();
			Swttool.wupdate(dis);
			// ...postingliste/tnickel_de.db wird upgedatet
			userobj.WebUpdateStep4(ugewinnedb, mindestzeit, aktdb,
					usergewinnrangflag, pref, rangfile);

			// arbeite mit den daten (aktualisiere die notwendigen threads)
			userobj.WorkUserPostingListe(middb,dm,ugewinnedb, aktdb, tdb, udb,
					mindestzeit, GC.DEFAULT, usergewinnrangflag, pref, dis,
					rangfile);

			// nimm diesen User für die Statistik auf
			ustat.addUser(userobj);

			if(i%10==0)
			{
				aktdb.WriteDB();
				tdb.WriteDB();
			}
			i++;
		}
		aktdb.WriteDB();
		tdb.WriteDB();
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public void StartAktualisiereObserveThreads(UserDB udb, int threshold,ThreadsDB tdb)
	{
		// aktualisiert die threads der Threadsdb
		// es werden aber nur die threads aktualisiert die
		// beobachtet werden
		// Es werden die Observe-Thread auf jeden Fall aktualisiert.
		// Es findet keine Überprüfung statt ob die Thread vor kurzen
		// bereits aktualisiert worden sind.
		int count = 0;
		//ThreadsDB tdb = new ThreadsDB();
		ThreadDbObj tdbobj = new ThreadDbObj();
		MidDB middb = new MidDB(tdb);
		tdb.ResetDB();

		int anzobservelem = tdb.SetDurchlaufmode(GC.MODE_OBSERVE);
		tdbobj = (ThreadDbObj) tdb.GetAktObject();
		tdbobj.UpdateThread(middb,udb, GC.DEFAULT, threshold,tdb);

		while ((tdbobj = (ThreadDbObj) tdb.GetNextObject()) != null)
		{
			System.out.println("Aktualisiere <" + count + "|" + anzobservelem
					+ "> Thread<" + tdbobj.getThreadname() + ">");
			tdbobj.UpdateThread(middb,udb, GC.DEFAULT, threshold,tdb);
			count++;
		}
	}

	public void StartBaueTagesinfo(int flag, ProgressBar pb, Text text,
			Display dis, String pref, HashSet tidset, String rangfile)
	{
		InfoUserpost info = new InfoUserpost();
		info.BaueTagesinfo(flag, ustat, pb, text, dis, pref, tidset, rangfile);
	}

	public void StartBaueTagesinfo_allezeit(String config, String pref,
			HashSet tidset)
	{
		InfoUserpost info = new InfoUserpost();
		info.BaueTagesinfo_allezeit(config, ustat, pref, tidset);
	}
}
