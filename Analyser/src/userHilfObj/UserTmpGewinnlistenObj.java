package userHilfObj;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Infbuf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.HtmlEinzelgewinne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import mainPackage.GC;
import objects.UserEinPostingGewinnObj;
import objects.UserGewStrategieObjII;
import stores.PostDBx;

public class UserTmpGewinnlistenObj
{
	// Dieser Gewinnspeicher verwaltet die Gewinne für einen user <username>
	// private ArrayList<UserEinPostingGewinnObj> gewinnListe = null;
	private String username_glob = null; // für diesen User werden die
	// Gewinne
	// gespeichert

	//dies ist eine einfachliste mit den Gewinnobjekten
	private ArrayList<UserEinPostingGewinnObj>[] gewinnlisten = new ArrayList[1];

	// tidmenge beinhaltet die menge der tids im tmpspeicher
	private HashSet<Integer> tidmenge = new HashSet<Integer>();
	private int lasttidindex = 0;

	public void cleanGewinnSpeicher()
	{
		if (gewinnlisten[0] == null)
			return;
		else
		{
			gewinnlisten[0].clear();
			gewinnlisten[0] = null;
		}
		UserEinPostingGewinnObj.cleanObjektzaehler();
	}

	public int getSize(int index)
	{
		// gibt groesse der postingopbjekte aus
		// usereinzelgewinnliste[5].size();
		return (gewinnlisten[index].size());
	}

	public String getObjInfostring(int gewpos,
			UserGewStrategieObjII ugewstrategieII, float sum)
	{
		// bereitet einen infostring für einen index auf
		// index indiziert den Algorithmus, gewpos den x-ten Gewinn

		UserEinPostingGewinnObj ugewobj = gewinnlisten[0].get(gewpos);

		String datum = Tools.entferneZeit(ugewobj.getDatum());
		String retstring = new String(ermittleNameTid(ugewobj.getTid())
				+ ":ind<" + 0 + ">:" + datum + ":" + ugewobj.getTid() + ":"
				+ ugewobj.getPostid() + ":g1<" + ugewobj.getVal1() + ">g2<"
				+ ugewobj.getVal2() + ">Einzelgew=" + ugewobj.getGewinn()
				+ ":Sum=" + (sum + ugewobj.getGewinn()));

		return retstring;
	}

	public void addObj(String unam, UserEinPostingGewinnObj urp)
	{
		// prüft ob objekt schon der datenliste,
		// wenn noch nicht dann nimm es auf

		// plausicheck des usernamens,(alle usernamen müssen hier immer gleich
		// sein)
		if (username_glob == null)
			username_glob = new String(unam);
		else if (username_glob.equals(unam) == false)
			Tracer.WriteTrace(10, "Error: internal error unam<" + unam
					+ "> != username<" + username_glob + "> ");

		// Tracer.WriteTrace(20, "I: AddRankingpointInfo user <"+unam+">
		// tid<"+urp.getTid()+"> pid<"+urp.getPostid()+">
		// Gewinn<"+urp.getGewinn()+">");

		if (gewinnlisten[0] == null)
		{
			gewinnlisten[0] = new ArrayList<UserEinPostingGewinnObj>();
			gewinnlisten[0].add(urp);
			return;
		}

		if (gewinnlisten[0].contains(urp) == false)
			gewinnlisten[0].add(urp);
	}

	public int calcAktinMenge(int index)
	{
		// berechnet wieviele verschiedene Aktien in der Gewinnliste sind
		tidmenge.clear();
		int anz = gewinnlisten[index].size();
		int j = 0;

		for (j = 0; j < anz; j++)
		{
			UserEinPostingGewinnObj ugewobj = gewinnlisten[index].get(j);
			int tid = ugewobj.getTid();
			tidmenge.add(tid);
		}
		lasttidindex = 0;
		return tidmenge.size();
	}

	private HashSet<Integer> getTidmengeClone()
	{
		// liefert die Menge der ganzen Tid´s
		HashSet<Integer> tidmengeclone = null;
		tidmengeclone = (HashSet<Integer>) tidmenge.clone();
		return tidmengeclone;
	}

	private float ermittleGewinnTid(int index, int tid)
	{
		// ermittelt den Gewinn für eine tid
		float teilgew = 0;
		int anz = gewinnlisten[index].size();
		for (int j = 0; j < anz; j++)
		{
			UserEinPostingGewinnObj ugewobj = gewinnlisten[index].get(j);
			if (ugewobj.getTid() != tid)
				continue;
			teilgew = teilgew + ugewobj.getGewinn();
		}
		return teilgew;
	}

	private String ermittleNameTid(int tid)
	{
		// ermittelt threadnamen für eine tid
		float teilgew = 0;
		int anz = gewinnlisten[0].size();
		for (int j = 0; j < anz; j++)
		{
			UserEinPostingGewinnObj ugewobj = gewinnlisten[0].get(j);
			if (ugewobj.getTid() != tid)
				continue;
			return UserEinPostingGewinnObj.getThreadname(tid);
		}
		Tracer
				.WriteTrace(10,
						"Error: fatal error, tid ist in der gewinnliste hat aber keinen namem");
		return null;
	}

	public String getNextTidGewinnStr()
	{
		Integer[] tidarray = tidmenge.toArray(new Integer[0]);
		int tid = tidarray[lasttidindex];
		float gew = ermittleGewinnTid(0, tid);
		lasttidindex++;
		String str = "ind<" + 0 + "> tid<"+tid+"> Aktname<" + this.ermittleNameTid(tid)
				+ "> Gewinn <" + this.ermittleGewinnTid(0, tid) + ">";
		return str;
	}

	private HashSet<Integer> sammlePostidmenge(int index, int tid)
	{
		// ermittelt die Menge der Postid´s für eine tid in der gewinnliste
		// gibt die Menge der Postid´s zurück
		HashSet<Integer> postidmenge = new HashSet<Integer>();

		int anz = gewinnlisten[index].size();
		for (int j = 0; j < anz; j++)
		{
			UserEinPostingGewinnObj gewobj = gewinnlisten[index].get(j);
			if (gewobj.getTid() == tid)
				postidmenge.add(gewobj.getPostid());
		}
		return postidmenge;
	}

	private float calcGesGewinn()
	{
		// ermittelt den Gesammtgewinn der in der gewinnliste ist
		int gewanz = getSize(0);
		float sum = 0;
		for (int i = 0; i < gewanz; i++)
		{
			UserEinPostingGewinnObj gewobj = gewinnlisten[0].get(i);
			sum = sum + gewobj.getGewinn();
		}
		return sum;
	}

	public void reportSpeichereUsergewinne(int erstflag, int auswertungsflag,
			UserGewStrategieObjII userGewStratObj)
	{
		// gibt für diesen user die Gewinne in eine Datei als Einzelgewinne aus
		// falls das erstflag =1 ist werden die alten ergebnisse(und files)
		// gelöscht
		// auswertungsflag: default, es wird alles reportet
		// GC.KEINE_EINZELGEWINNE, die einzelgewinne werden nicht reportet
		// return:

		int j = 0;
		int gewanz = 0, aktanz = 0;
		String gfnameinzel = null, gfnamesum = null;
		Infbuf infeinzel = null, infsumm = null;
		HtmlEinzelgewinne heinzel=null;

		// falls keine Gewinne da sind
		if (gewinnlisten[0] == null)
		{
			gewanz = 0;
			aktanz = 0;
		} else
		{
			// falls doch gewinne da sind
			gewanz = getSize(0);
			aktanz = calcAktinMenge(0);
		}

		gfnameinzel = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Einzelgewinne\\"
				+ username_glob + ".txt";
		gfnamesum = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Summengewinne\\"
				+ username_glob + ".txt";

		if (auswertungsflag == GC.DEFAULT)
		{
			String htmleinzel = GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\Einzelgewinne\\"
					+ username_glob + ".html";

			heinzel = new HtmlEinzelgewinne(htmleinzel);
			int len = FileAccess.FileLength(htmleinzel);

			if (len == 0)
				heinzel
						.addKopf("Einzelgewinne",
								"Aktname@Symbol@Kursinfo@Datum@Tid@k1@k2@EinzelGew@Summe");
		}

		if (auswertungsflag == GC.DEFAULT)
			infeinzel = new Infbuf(gfnameinzel);

		infsumm = new Infbuf(gfnamesum);

		// ermittle die lastsum neu
		// tempsum = Gesammtgewinn-(der neue Gewinn der in der templiste ist)
		float tempsum = calcGesGewinn();
		float stratsum = userGewStratObj.getGesGewinn();

		float sum = stratsum - tempsum;

		// zeige wie sich der gesammtgewinn aufsplitet
		for (j = 0; j < gewanz; j++)
		{
			String gewinnzeile = this.getObjInfostring(j, userGewStratObj, sum);
			// hier kann die einzelgewinnauswertung abgeschaltet werden
			if (auswertungsflag == GC.DEFAULT)
				infeinzel.writezeile(gewinnzeile);

			UserEinPostingGewinnObj gewobj = gewinnlisten[0].get(j);
			sum = sum + gewobj.getGewinn();

			// nullgewinne werden nicht gespeichert
			if (gewobj.getGewinn() == 0)
				continue;

			//Speichere den Einzelgewinn in Gewinnverlaufdb
			userGewStratObj.userIntervallGewinne.addEinzelgewinn(username_glob, gewobj);
			
			if (auswertungsflag == GC.DEFAULT)
				heinzel.addZeile(gewobj, gewinnzeile);

		}
		//Speichere den Zeitraumspeicher
		userGewStratObj.userIntervallGewinne.WriteDB();
		// Schliesse das einzelfile
		if (auswertungsflag == GC.DEFAULT)
			heinzel.closeFile();

		for (j = 0; j < aktanz; j++)
		{
			String gewstr = this.getNextTidGewinnStr();
			infsumm.writezeile(":" + gewstr);
		}
		if (auswertungsflag == GC.DEFAULT)
		  infeinzel.close();

		infsumm.close();
		return;
	}

	public void delMem()
	{
		// lösche den Gewinnspeicher
		if (gewinnlisten[0] != null)
			gewinnlisten[0].clear();
		gewinnlisten[0] = null;
	}

	public void plausicheck(int index)
	{
		// mache einen Plausicheck für die Userposings (die Postid wird hier
		// überprüft)
		// die postingid´s von offline/db/threaddata muss die gleichen
		// postingid´s in der tmpgewinnliste haben
		// Bsp: <threadname>.db #"Procera" 97 [4, 6, 9, 15, 18, 23, 25, 28, 30,
		// 32, 36, 39, 40, 50, 53, 55, 59, 66, 68, 10896, 18470, 18474, 18483,
		// 18583, 18625, 18628, 18632, 18661, 18663, 18665, 18681, 18684, 18688,
		// 18710, 18713, 18719, 18744, 18746, 18748, 18750, 18752, 18755, 18757,
		// 18759, 18761, 18765, 18766, 18783, 18790, 18798, 18826, 18833, 18845,
		// 18847, 18849, 18852, 18858, 18865, 18867, 18892, 18895, 18901, 18906,
		// 18909, 18913, 18915, 18917, 18949, 18952, 18954, 18956, 18969, 18980,
		// 18982, 18994, 18996, 19000, 19003, 19011, 19021, 19028, 19030, 19048,
		// 19050, 19092, 19099, 19107, 19111, 19116, 19168, 19174, 19180, 19267,
		// 19268, 19272, 19290, 19373]
		// => in der tmpliste sollten für den User Procera auch sämtliche diese
		// Nummern wiederzufinden sein. Ist dies nicht der Fall ist die Tmpliste
		// kaputt oder nicht vollständig
		PostDBx pdb = new PostDBx();

		int j = 0;
		int gewanz = 0;

		if (gewinnlisten[index] == null)
		{
			gewanz = 0;
		} else
		{
			gewanz = getSize(index);
		}

		String gfplausierror = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\plausierror" + ".txt";

		Inf infplausi = new Inf();
		infplausi.setFilename(gfplausierror);

		// holt sich die Menge der Tid´s aus den gewinnlisten
		HashSet<Integer> tidmengeclone = getTidmengeClone();

		// Hier wird überprüft ob die postid´s der gewinne auch in den
		// entsprechenden postdb´s sind
		// postdb´s und id´s in den Gewinnlisten müssen exakt übereinstimmen,
		// sonst stimmt was nicht
		j = 0;
		while ((tidmengeclone.size() > 0) && (j < gewanz))
		{
			// holt sich ein gewinnobjekt
			UserEinPostingGewinnObj gewobj = gewinnlisten[index].get(j);
			int tid = gewobj.getTid();

			if (tidmengeclone.contains(tid) == false)
			{
				j++;
				continue;
			} else
				// entferne diese tid, da die schon betrachtet wurde
				tidmengeclone.remove(tid);

			// jetzt haben wir eine tid und jetzt werden hierzu die postings mit
			// der postdb verglichen
			// diese postings müssen exakt übereinstimmen

			String filedbnam = GC.rootpath + "\\db\\threaddata\\"
					+ UserEinPostingGewinnObj.getThreadname(tid) + "_" + tid
					+ ".db";
			filedbnam = filedbnam.replace(".gzip", "");
			pdb.ReadPostDB(filedbnam);

			// liefert die Menge der ganzen Postid´s für eine Tid
			HashSet<Integer> Postidmenge_inPostingliste = pdb.getPostidMenge(
					 username_glob);
			if (Postidmenge_inPostingliste == null)
			{
				Tracer.WriteTrace(20, "Warning postidmenge ==null filedbnam<"
						+ filedbnam + ">");
				return;
			}

			// ermittelt die Postid´s für eine Tid in der gewinnliste
			HashSet<Integer> Postidmenge_inGewinnliste = sammlePostidmenge(0,
					tid);

			HashSet<Integer> diffmenge = Tools.MengenDifferenz(
					Postidmenge_inPostingliste, Postidmenge_inGewinnliste);

			if (diffmenge.size() == 0)
			{
			} else
			{
				String msg = "inkonsistenz tnam<" + filedbnam + "> usernam<"
						+ username_glob + "> #postdb<"
						+ Postidmenge_inPostingliste.size()
						+ "> #postinggewinne<"
						+ Postidmenge_inGewinnliste.size() + ">";
				System.out.println(msg);
				Tracer.WriteTrace(20, "error:" + msg);
				// inkosistentz in Postingliste und Gewinnliste
				// logge die Unterschiede
				for (Iterator<Integer> it = diffmenge.iterator(); it.hasNext();)
				{
					String mistext = null;
					int missingpostid = it.next();
					System.out.print(missingpostid + " ");

					if (Postidmenge_inPostingliste.contains(missingpostid) == false)
						mistext = filedbnam;
					else if (Postidmenge_inGewinnliste.contains(missingpostid) == false)
						mistext = "Gewinnliste";
					else
						Tracer.WriteTrace(10, "error: internal error 13454545");

					Tracer.WriteTrace(20, "warning: missing-postid<"
							+ missingpostid + "> in <" + mistext + ">");
				}
				System.out.println();
			}
		}
	}

	public void MengenAusgabe(HashSet<Integer> menge)
	{
		String ausgabe = new String("");
		for (Iterator it = menge.iterator(); it.hasNext();)
		{
			ausgabe = ausgabe.concat(it.next() + " ");
		}
		Tracer.WriteTrace(20, ausgabe);
	}

}
