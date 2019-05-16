package userHilfObj;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mainPackage.GC;
import objects.AktDbObj;
import objects.UserDbObj;
import objects.UserThreadPostingObj;
import stores.AktDB;

public class UserPostingListe
{
	// diese Liste verwaltet die verschiedenen Threads eines users
	// in welchen Threads hat er schon gepostet.

	private List<UserThreadPostingObj> UserPostingListe = new ArrayList<UserThreadPostingObj>();

	int tidnextcounter = 0;

	public void addUserObj(UserThreadPostingObj uipo)
	{
		// prüft ob objekt schon der datenliste,
		// wenn noch nicht dann nimm es auf
		if (UserPostingListe == null)
		{
			UserPostingListe = new ArrayList<UserThreadPostingObj>();
			UserPostingListe.add(uipo);
			return;
		}

		if (UserPostingListe.contains(uipo) == false)
			UserPostingListe.add(uipo);
		else
			Tracer.WriteTrace(20, "Warning: unam<" + uipo.getUsername_glob()
					+ "> tid<" + uipo.getThreadid() + "> Aufn-dat<"
					+ uipo.getAufnahmeDatum() + "> tnam<"
					+ uipo.getThreadname() + "> doppelt in userpostings");
	}

	public int getSize()
	{
		// gibt groesse der postingopbjekte aus
		if (UserPostingListe == null)
			UserPostingListe = new ArrayList<UserThreadPostingObj>();

		return (UserPostingListe.size());
	}

	public int getAnzneuerThreadsSeit(String altdatum, int flag,
			UserDbObj udbo, AktDB aktdb)
	{
		// ermittelt die anzahl der neuen Threads die der User besucht hat seit
		// dem datum
		// flag:0 nur diskussionsthreads werden betrachtet
		// flag:1 nur Aktienthreads werden betrachtet
		UserThreadPostingObj uipo = new UserThreadPostingObj(udbo
				.get_username());
		int tid = 0;
		int aktienzaehler = 0, sofazaehler = 0;

		int anz = UserPostingListe.size();
		for (int i = 0; i < anz; i++)
		{
			uipo = UserPostingListe.get(i);
			tid = uipo.getThreadid();

			if (tid == 0)
			{
				Tracer.WriteTrace(20, this.getClass().getName()
						+ "ERROR: fileerror tid==0 uname<"
						+ udbo.get_username() + ">");
				return 0;
			}

			// sucht das objekt in der aktdb

			AktDbObj aktdbo = (AktDbObj) aktdb.GetObject(tid);

			if (aktdbo == null)
			{
				/*
				 * Tracer.WriteTrace(20, this.getClass().getName() +
				 * "Warning: tid<" + tid + "> nicht in der aktdb uname<" +
				 * udbo.get_username() + ">");
				 */
				continue;
			}
			// prüft ob das aufnahmedatum älter als 0,5 Jahre ist, wenn ja ist
			// es zu alt
			// für die zaehlung
			if (Tools
					.datum_ist_aelter_gleich(altdatum, uipo.getAufnahmeDatum()) == false)
				continue;

			// prüft ob Aktie
			if ((aktdbo.getMasterid() > 0) && (aktdbo.getSymbol().length() > 2))

			{
				aktienzaehler++;
			}
			// prüft ob Sofathread
			else if ((aktdbo.getMasterid() == 0)
					&& (aktdbo.getSymbol().length() == 0))
			{
				sofazaehler++;
			}

		}

		if (flag == 1)
			return aktienzaehler;
		else
			return sofazaehler;

	}

	public void updateUserObj(UserThreadPostingObj uipo)
	{
		// holt das vorhandene Objekt
		UserThreadPostingObj uivorh = getUserObj(uipo.getThreadid());

		uivorh.setAufnahmeDatum(uipo.getAufnahmeDatum());
		uivorh.setLastposting(uipo.getLastposting());
		uivorh.setThreadname(uipo.getThreadname());
	}

	public void deleteUserPostingListe()
	{
		UserPostingListe = null;
	}

	public UserThreadPostingObj getUserObj(int tid)
	{
		// prüft nach ob ein Thread schon gespeichert ist oder ob der neu ist
		// wenn der thread schon da ist gib ihn zurück
		UserThreadPostingObj uipo = new UserThreadPostingObj(null);

		// UserLastPostings;
		int anz = UserPostingListe.size();
		for (int i = 0; i < anz; i++)
		{
			uipo = UserPostingListe.get(i);
			if (uipo.getThreadid() == tid)
				return uipo;
		}
		return null;
	}

	public boolean threadidAvailable(int tid)
	{
		// prüft nach ob ein Thread schon beim user gespeichert ist oder ob der
		// neu ist
		UserThreadPostingObj uipo = new UserThreadPostingObj(null);

		// UserLastPostings;
		int anz = UserPostingListe.size();
		for (int i = 0; i < anz; i++)
		{
			uipo = UserPostingListe.get(i);
			if (uipo.getThreadid() == tid)
				return true;
		}
		return false;
	}

	public UserThreadPostingObj getNextPostingObj()
	{
		// holt das nächste threadidobj aus der liste

		UserThreadPostingObj uipo = new UserThreadPostingObj(null);
		tidnextcounter++;
		// UserLastPostings;
		int anz = UserPostingListe.size();
		if (tidnextcounter >= anz)
			return null;
		uipo = UserPostingListe.get(tidnextcounter);

		return uipo;
	}

	public UserThreadPostingObj getFirstPostingObj()
	{
		// holt das erste userinfpostingobj aus der postingliste
		tidnextcounter = 0;
		UserThreadPostingObj uipo = new UserThreadPostingObj(null);

		// UserLastPostings;
		int anz = UserPostingListe.size();
		if (anz == 0)
			return null;
		// holt sich das erste posting
		uipo = UserPostingListe.get(0);
		return uipo;
	}

	public UserThreadPostingObj getObjIDX(int index)
	{
		UserThreadPostingObj uipo = new UserThreadPostingObj(null);

		// UserLastPostings;
		int anz = UserPostingListe.size();
		if (anz == 0)
			return null;
		// holt sich das erste posting
		uipo = UserPostingListe.get(index);
		return uipo;
	}

	public void WriteUserInfoListe(UserDbObj udbo)
	{
		// schreibt die WebBetragsliste in db\\userdate\\postingliste_<x>.db.
		// Die Beitragsliste beinhaltet die Threads die ein user in
		// letzter Zeit besucht hatte

		String filename = GC.rootpath + "\\db\\postingliste\\"
				+ udbo.get_username() + ".db";
		String filenametmp = GC.rootpath + "\\db\\postingliste\\"
				+ udbo.get_username() + ".tmp";
		String zeile = null;
		int i = 0, zeilenanz = 0;
		UserThreadPostingObj uipo = new UserThreadPostingObj(null);

		zeilenanz = UserPostingListe.size();
		BufferedWriter ouf;
		try
		{
			ouf = FileAccess.WriteFileOpenUnziped(filenametmp);
			zeile = "********userdata postinganz=  " + zeilenanz;
			ouf.write(zeile);
			ouf.newLine();

			for (i = 0; i < zeilenanz; i++)
			{

				// holt sich das nächste Postingobjekt
				uipo = UserPostingListe.get(i);
				uipo.Write(ouf);
				ouf.newLine();
			}
			ouf.close();

			// length protection, compare lenght
			int oldlen = FileAccess.FileLength(filename);
			int newlen = FileAccess.FileLength(filenametmp);

			if ((newlen < oldlen - 5)
					&& (filename.contains("compressed") == false))
			{
				// lenght error, new is shorter then the oldfile
				Tracer.WriteTrace(10, this.getClass().getName()
						+ "ERROR: length protection error<" + filename + "> <"
						+ filenametmp + "> oldlen<" + oldlen + "> newlen<"
						+ newlen + ">");
				
			}
			FileAccess.Rename(filenametmp, filename);

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean CheckPostingListeAvailable(UserDbObj udbo)
	{
		String filename = GC.rootpath + "\\db\\postingliste\\"
				+ udbo.get_username() + ".db";
		return (FileAccess.FileAvailable(filename));
	}

	public int ReadUserInfoListe(UserDbObj udbo)
	{

		// liest die Beitragsliste ein die schon da ist
		// Die Beitragsliste beinhaltet die Threads die ein user in letzter Zeit
		// besucht hatte
		// return: anz elemente in dieser Liste
		BufferedReader inf;
		int anz = 0;

		// falls das gesetzt ist kommt man in die charaktersektion
		String unam = udbo.get_username();
		String filename = GC.rootpath + "\\db\\postingliste\\" + unam + ".db";
		if (FileAccess.FileAvailable(filename) == false)
			return 0;

		inf = FileAccess.ReadFileOpen(filename);
		String zeile = null;

		try
		{
			while ((zeile = inf.readLine()) != null)
			{
				UserThreadPostingObj uip = new UserThreadPostingObj(unam);

				// falls jetzt kein Charakterobjekt, dann ist das ein
				// threadobjekt

				// Dies ist ein postingpart
				// falls noch nix gepostet wurde
				if (zeile.contains("#postings") == true)
					continue;
				if (zeile.contains("postinganz=  0") == true)
					continue;
				if (zeile.contains("*****userdata") == true)
					continue;

				// fall die kennung für die userdata gefunden wurden
				if (zeile.contains(uip.Kennung()) == true)
					continue;// erste zeile

				if (uip.Read(inf, zeile) == true)
				{
					this.addUserObj(uip);
					anz++;
				}
			}
			inf.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return anz;
	}

}
