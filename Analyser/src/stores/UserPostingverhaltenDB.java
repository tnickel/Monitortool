package stores;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Prop2;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.Collections;

import objects.UserDbObj;
import objects.UserPostingverhaltenDbObj;

import org.eclipse.swt.widgets.Display;

import ranking.AnalyseUserpostings;
import swtOberfläche.Rangstat;
import db.DB;

public class UserPostingverhaltenDB extends DB
{
	public UserPostingverhaltenDB()
	{

		LoadDB("userpostingverhaltendb", null, 0);

	}

	public void postprocess()
	{
	}

	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	// ******************************* Allegmeine Funktionen **************//

	// ******************************* hier geht es los ***************** *//
	public UserPostingverhaltenDbObj sucheUser(String unam)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserPostingverhaltenDbObj uo = (UserPostingverhaltenDbObj) GetObjectIDX(i);
			if (uo.getUname().equals(unam) == true)
				return uo;
		}
		return null;
	}

	private void CalcRangSt1(MidDB middb,Rangstat rs, Display dis, UserDB udb, ThreadsDB tdb)
	{
		rs.setStartpos(0);
		rs.setEndpos(72445);
		rs.setStepweite(1);
		String startstr = Prop2.getprop("Verhaltensrang_startpos");

		if (startstr == null)
		{
			rs.setStartpos(0);
			this.clearAll();
		} else
			rs.setStartpos(Integer.parseInt(startstr));

		// Step 1: analysiere die User
		AnalyseUserpostings ap = new AnalyseUserpostings();
		ap.StartAnalyseSt1(middb,rs, dis, udb, this, tdb);
	}

	private int CalcRangSt2(UserDB udb,
			ArrayList<UserPostingverhaltenDbObj> rankingliste)
	{
		// Step 2: jetzt wird ein ranking gemacht
		// Das Ranking wird auf 100000 Skaliert. 1=bester, 100000=schlechtester
		int useranz = GetanzObj();
		this.ResetDB();

		int links = 0;
		for (int i = 0; i < useranz; i++)
		{
			UserPostingverhaltenDbObj upo = (UserPostingverhaltenDbObj) this
					.GetObjectIDX(i);
			String unam = upo.getUname();

			UserDbObj udbo = (UserDbObj) udb.getUserobj(unam);
			if (udbo == null)
			{
				Tracer.WriteTrace(20, "Warning: user<" + unam
						+ "> nicht in userdb");
				continue;
			}

			// falls der user nicht valid ist gehe weiter
			if (udbo.getState() != 0)
				continue;

			if ((udbo.getRegistriert().contains("?"))
					|| (udbo.getRegistriert().contains("null")))
			{
				Tracer.WriteTrace(20, "W:kein user aufnahmedatum user<"
						+ udbo.get_username() + "> datum<"
						+ udbo.getRegistriert() + "> no ranking!");
				continue;
			}

			System.out.println(" <" + i + "|" + useranz + "> Add user <"
					+ udbo.get_username() + "> to Rankingliste");

			int rp=upo.calcRankingpoints();
			links = links + upo.getPostanzExtLink();
			upo.setRangpoints(rp);
			
			System.out.println("sumrp<" + links + ">");

			// Mindestens 20 mal muss ein user was gepostet haben
			if (upo.getAnzausgewpostings() > 20)
				rankingliste.add(upo);
		}
		this.WriteDB();
		return links;
	}

	public void CalcRangingProto(MidDB middb,Rangstat rs, Display dis, UserDB udb,
			ThreadsDB tdb, String fnamrankliste, Boolean step1flag,
			Boolean step2flag)
	{
		// geht durch alle userpostings und beurteile das Userverhalten
		// zähle Links, smileys ...
		int links = 0;
		ArrayList<UserPostingverhaltenDbObj> rankingliste = new ArrayList<UserPostingverhaltenDbObj>();

		/* STEP1: Berechnung des Ranges in UserPostingVerhalten*/
		if (step1flag == true)
			CalcRangSt1(middb,rs, dis, udb, tdb);

		/* STEP2: Skalierung der userpostingverhalten.db auf 100000*/
		// Die Userverhaltensliste wird protokolliert
		if (step2flag == true)
		{
			links = CalcRangSt2(udb, rankingliste);
			System.out.println("summe links<" + links+">");

			Collections.sort(rankingliste);

			int ranklistengroesse = rankingliste.size();
			float rankfaktor = (100000 / ranklistengroesse);
			for (int i = 0; i < ranklistengroesse; i++)
			{
				// setzt den Rank;
				int rangmod = (int) (i * rankfaktor);
				rankingliste.get(i).setRang(rangmod);
			}

			FileAccess.FileDelete(fnamrankliste, 0);
			Inf inf = new Inf();
			inf.setFilename(fnamrankliste);
			inf.writezeile("******\\db\\stores\\Userpostingverh_rang.txt="
					+ ranklistengroesse);
			inf
					.writezeile("*****name#postanz#anzlinks#%links#Rang aus Verhalten#Rang aus Userdb#anz smileys#%smileys");

			// rang aus der userdb
			int ruserdb = -99;
			for (int i = 0; i < ranklistengroesse; i++)
			{
				UserPostingverhaltenDbObj ph = rankingliste.get(i);
				String unam = ph.getUname();

				//hier wird das udbo aus der userdb geholt,
				//dies wird aber nur für das protokoll verwendet
				UserDbObj udbo = (UserDbObj) udb.getUserobj(unam);
				if (udbo == null)
					ruserdb = -99;
				else
					ruserdb = udbo.getRang();

				String line = ph.getUname() + "#" + ph.getAnzausgewpostings()
						+ "#" + ph.getPostanzExtLink() + "#"
						+ ph.calcProzLinks() + "#" + ph.getRang() + "#"
						+ ruserdb + "#" + ph.getPostanzIcons() + "#"
						+ ph.calcProzIcons();
				inf.writezeile(line);
			}
			inf.close();

			this.WriteDB();
		}
	}
}
