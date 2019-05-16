package hilfsrepeattask;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Prop2;

import java.util.ArrayList;
import java.util.Collections;

import mainPackage.GC;
import objects.UserGewStrategieObjII;
import ranking.GewinnStatObj;
import stores.UserGewStrategieDB2;
import stores.UserSummenGewinneDBI;

import comperatoren.StabilComparator;

public class Stabilitaet
{
	// Es werden alle UserThreadVirtualKonto/../Summengewinne.db/<username.db>
	// untersucht
	// Hier wird geschaut welcher User hat ein stabiles Handelsverhalten und
	// kann stabil Gewinne
	// erzeugen.
	static UserGewStrategieDB2 ugewstrategieII = new UserGewStrategieDB2("UserThreadVirtualKonto\\Summengewinnedb");
	static UserSummenGewinneDBI userZeitraumDbi = null;
	private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");
	private int gewintervall_glob = 0;

	ArrayList<GewinnStatObj> rankingliste = new ArrayList<GewinnStatObj>();

	public void CalcStabilitaet()
	{
		// holt das Gewinnintervall z.B. 120 Tage werden hier betrachtet
		gewintervall_glob = Integer.parseInt(Prop2
				.getprop("usergewinnintervall"));

		
		int anz = ugewstrategieII.GetanzObj();
		// betrachte für jeden User dieses Intervall und stelle Gewinne fest
		for (int i = 0; i < anz; i++)
		{

			UserGewStrategieObjII uobj2 = (UserGewStrategieObjII) ugewstrategieII
					.GetObjectIDX(i);
			String unam = uobj2.getUsername();

			// lade für diesen User die DBI-Daten (Summengewinne.db rein)
			userZeitraumDbi = new UserSummenGewinneDBI("UserThreadVirtualKonto\\Summengewinnedb",unam, gewintervall_glob,
					0);
			// Stabilität (seqnr, index)
			GewinnStatObj gstat = userZeitraumDbi.calcStabilitätsfaktor(0, 1);
			gstat.setUname(unam);

			// dieser Stabilitätsfaktor muss noch in uobj2 gespeichert werden
			// uobj2.storeStabilitätsfaktor(seqnr,index,algorithmus,f);
			uobj2.storeStabilitätsfaktor(unam, 0, 1, gstat);
			System.out.println(i + "<" + unam + "> gsum<"
					+ gstat.getGewinnsumme() + "> stabil<"
					+ gstat.getGewabweichungsSumme() + ">");

			// Es werden nur User mit positiven Gewinnen betrachtet !!
			if (gstat.getGewinnsumme() > 0)
				rankingliste.add(gstat);
		}
		Collections.sort(rankingliste);
		showliste();
		writeliste();
	}

	public void showGewinne()
	{
		String fna=GC.rootpath
		+ "\\db\\UserThreadVirtualKonto\\stabilitaeten.txt";
		Inf inf = new Inf();
		inf.setFilename(fna);

		if(FileAccess.FileAvailable(fna))
			FileAccess.FileDelete(fna,0);
		
		int anz = ugewstrategieII.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII uobj2 = (UserGewStrategieObjII) ugewstrategieII
					.GetObjectIDX(i);
			String unam = uobj2.getUsername();

			// Schaut nach ob für diesen Gewinnalgorithmus überhaupt eine db da
			// ist
			if (FileAccess.FileAvailable(GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\SummengewinneDB\\" + unam
					+ ".db") == false)
				continue;

			// lade für diesen User die DBI-Daten (Summengewinne.db rein)
			userZeitraumDbi = new UserSummenGewinneDBI("UserThreadVirtualKonto\\Summengewinnedb",unam, gewintervall_glob,
					0);

			userZeitraumDbi.sortiereDatum();
			userZeitraumDbi.protoGewinnintervalle(0);
			float stabil = userZeitraumDbi.calcStabilitaet();
			if(stabil>0)
				uobj2.setStabilfaktor_g(stabil);
		}

		// sortiere die Strategieobjekte nach den stabilitätsfaktoren
		// sortiere nach datum
		StabilComparator c = new StabilComparator();
		Collections.sort(ugewstrategieII.dbliste, c);
		// speichere
		ugewstrategieII.WriteDB();

		// erzeuge eine Liste der Stabilitätsfaktoren
		anz = ugewstrategieII.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII uobj2 = (UserGewStrategieObjII) ugewstrategieII
					.GetObjectIDX(i);
			if(uobj2.getStabilfaktor_g()>0)
			{
				String msg="Algo<"+uobj2.getUsername_G()+"> stabilfaktor<"+uobj2.getStabilfaktor_g()+">";
				inf.writezeile(msg);
			}
		}

	}

	private void showliste()
	{
		int anz = rankingliste.size();
		for (int i = 0; i < anz; i++)
		{
			GewinnStatObj gstat = rankingliste.get(i);
			String outstr = i + "<" + gstat.getUname() + "> gsum<"
					+ gstat.getGewinnsumme() + "> stabil<"
					+ gstat.getGewabweichungsSumme() + ">";
			System.out.println(outstr);
		}
	}

	private void writeliste()
	{
		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Stabilliste.txt";

		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam,0);

		inf.setFilename(fnam);
		int anz = rankingliste.size();
		for (int i = 0; i < anz; i++)
		{
			GewinnStatObj gstat = rankingliste.get(i);
			String outstr = i + "<" + gstat.getUname() + "> gsum<"
					+ gstat.getGewinnsumme() + "> stabil<"
					+ gstat.getGewabweichungsSumme() + ">";
			inf.writezeile(outstr);
		}
	}
}
