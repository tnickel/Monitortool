package objects;

import hilfsklasse.Prop2;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import mainPackage.GC;
import ranking.GewinnStatObj;
import stores.UserSummenGewinneDBI;

public class UserGewStrategieObjII extends Obj implements DBObject
{
	// Dieses Analyseobjekt verwaltet Gewinneigenschaften für einen User dies
	// sind z.B. Summengewinne, Stabilitäten etc...
	// Ein Vewaltungsobjekt kann aus mehreren Teilobjekten bestehen
	//
	// Ausserdem werden z.B. einige Werte temporär gehalten. Z.B. die
	// Einzelgewinne werden in den UserDBGewinnObjI temporär gehalten
	// Dies Verwaltungsobjekt besteht aus:
	// a)Den Usernamen
	// b)Den Userstabilitätsobjekt ../db/stabil/<username.db>(Dieses Objekt
	// beinhaltet Statistikdaten zur Bewertung der Handelsstrategie)
	// c)Temporäre Daten die zur Analyse verwendet werden

	// username
	String username_G = null;
	// usertype_G; 0:standartuser mit normalen usernamen 1:masteruser
	int usertype_G = 0;
	// handelsstrategie die der User verfolgt
	int handelsstrategie_G = 0;
	// sliderindex_g:sagt welcher Slider verwendet wird (20 Tage)
	int sliderindex_G = 0;
	// maxfaktor: sagt wie stark der user gewichtet ist, (Vertrauensbewertung)
	float maxfaktor_G = 0;
	float minfaktor_G = 0;
	float gefaelle_G = 0;
	int minguteUser_G = 0;
	// startsumme die angelegt wird
	float startsumme_G = 0;
	// maxdays: solange wird der einsatz gehalten bis verkauft wird
	int maxdays_G = 0;
	// vertrauensfaktorAlgo_G= gibt den Algorithmus für den Vertrauensfaktor an
	int vertrauensfaktorAlgo_G = 0;
	// minaktiviät, diese aktivität muss mindestens ueberschritten werden sonst
	// wird nicht
	// gehandelt

	float guteUserfaktor_G = 0;
	float gutePostingsfaktor_G = 0;
	// diese aktivitätsschwelle muss überschritten sein bis gehandelt wird
	int minaktivität_G = 0;

	// dies ist der maximale Gewinn der mit dieser Strategie ermittelt wurde
	// dieser Gewinn wird auch für das userranking herangezogen und permanent in
	// der
	// usergewinne.db gespeichert
	float gesgewinn_G = 0;
	// dies ist der Rank für diese Gewinnstrategie R1...R100000
	int gewinnrank_G = 0;

	// anzahl postings die zum gewinn beitragen
	int gewinnpostings_G = 0;
	int verlustpostings_G = 0;
	int anzneutralpostings_G = 0;
	int anzthreads_G = 0;

	private int intervalltage_glob = 0;
	private int seqnr_glob = 0;

	
	
	// Storeklasse GewinnlisteDBI (Diese Storeklasse hat die Liste der
	// Summengewinne 'eines' User )
	public UserSummenGewinneDBI userIntervallGewinne = null;
	private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");

	// Verwaltungsvariablen für den Stabilitätsfaktor
	float stabilfaktor_g = 0;
	// zaehler der die kauf-verkaufaktionen zählt
	long gewinnhandlungen_g = 0;

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public UserGewStrategieObjII(String zeile)
	{
		
		//System.out.println("x1 zeile<"+zeile+">");
		if(zeile.length()>200)
		{
			Tracer.WriteTrace(10, "UserGewSrategieObjII zu lang<"+zeile.length()+"> zeile<"+zeile+">->ignore");
			return;
		}
		
		int anz = SG.countZeichen(zeile, "#");
		if ((anz != 0) && (anz != 18))
		{

			Tracer.WriteTrace(10, UserGewStrategieObjII.class.getName()
					+ ":ERROR:zeile fehlerhaft in userdbgewinn.db zeile=<"
					+ zeile + "> anz<" + anz + ">");
		}
		int pos = 1;
		try
		{ // 1
			username_G = new String(SG.nteilstring(zeile, "#", pos));
			pos++;

			if ((anz == 17) || (anz == 18))
			{ // 2
				gesgewinn_G = Float.valueOf(SG.nteilstring(zeile, "#", pos));
				pos++;

				// 3
				gewinnrank_G = Integer.valueOf(SG.nteilstring(zeile, "#",
						pos));
				pos++;
				// 4
				handelsstrategie_G = Integer.valueOf(SG.nteilstring(zeile,
						"#", pos));
				pos++;

				// 5
				sliderindex_G = Integer.valueOf(SG.nteilstring(zeile, "#",
						pos));
				pos++;

				// 6
				maxfaktor_G = Float.valueOf(SG.nteilstring(zeile, "#", pos));
				pos++;

				minfaktor_G = Float.valueOf(SG.nteilstring(zeile, "#", pos));
				pos++;

				gefaelle_G = Float.valueOf(SG.nteilstring(zeile, "#", pos));
				pos++;

				// 9
				startsumme_G = Float
						.valueOf(SG.nteilstring(zeile, "#", pos));
				pos++;

				// 10
				maxdays_G = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
				pos++;

				// 11
				vertrauensfaktorAlgo_G = Integer.valueOf(SG.nteilstring(
						zeile, "#", pos));
				pos++;
				// 12
				minguteUser_G = Integer.valueOf(SG.nteilstring(zeile, "#",
						pos));
				pos++;

				// 13
				guteUserfaktor_G = Float.valueOf(SG.nteilstring(zeile, "#",
						pos));
				pos++;
				// 14
				gutePostingsfaktor_G = Float.valueOf(SG.nteilstring(zeile,
						"#", pos));
				pos++;
				// 15
				minaktivität_G = Integer.valueOf(SG.nteilstring(zeile, "#",
						pos));
				pos++;
				// 16
				gewinnpostings_G = Integer.valueOf(SG.nteilstring(zeile,
						"#", pos));
				pos++;

				// 17
				verlustpostings_G = Integer.valueOf(SG.nteilstring(zeile,
						"#", pos));
				pos++;
				// 18
				anzthreads_G = Integer.valueOf(SG.nteilstring(zeile, "#",
						pos));
				pos++;

				if (anz == 18)
				{
					// 19
					anzneutralpostings_G = Integer.valueOf(SG.nteilstring(
							zeile, "#", pos));
				}
			}
		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, UserDbGewinnZeitraumObjI.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getconfig();
	}

	private void getconfig()
	{
		if (seqnr_glob != 0)
			return;
		// intervalltage setzten
		String laststring = Prop2.getprop("usergewinnintervall");
		intervalltage_glob = Integer.parseInt(laststring);
		String seqstring = Prop2.getprop("seqnummer");
		seqnr_glob = Integer.parseInt(seqstring);
	}

	public int getThreadid()
	{
		return 0;
	}

	@Override
	public String toString()
	{
		String infostring = null;
		infostring = username_G;

		return (username_G + "#" + gesgewinn_G + "#" + gewinnrank_G + "#"
				+ handelsstrategie_G + "#" + sliderindex_G + "#" + maxfaktor_G
				+ "#" + minfaktor_G + "#" + gefaelle_G + "#" + startsumme_G
				+ "#" + maxdays_G + "#" + vertrauensfaktorAlgo_G + "#"
				+ minguteUser_G + "#" + guteUserfaktor_G + "#"
				+ gutePostingsfaktor_G + "#" + minaktivität_G + "#"
				+ gewinnpostings_G + "#" + verlustpostings_G + "#"
				+ anzthreads_G + "#" + anzneutralpostings_G);
	}

	public String getAlgoBeschreibungsstring()
	// Gibt einen String zurück der den Algorithmus beschreibt
	{
		String info = "maxgew<" + gesgewinn_G + "> maxfaktor<" + maxfaktor_G
				+ "> minfaktor<" + minfaktor_G + "> gefaelle<" + gefaelle_G
				+ "> startsumme<" + startsumme_G + "> maxdays<" + maxdays_G
				+ "> minguteU<" + minguteUser_G + "> guteUserfakt<"
				+ guteUserfaktor_G + "> gutePostingsfakt<"
				+ gutePostingsfaktor_G + "> minaktiviät<" + minaktivität_G
				+ "gewinnpostings<" + gewinnpostings_G + "> verlustpostings<"
				+ verlustpostings_G + " neutralpostings<"
				+ anzneutralpostings_G + "> anzthreads<" + anzthreads_G + ">";
		return info;
	}

	@Override
	public String GetSaveInfostring()
	{
		return ("username#maxgewinn#gewinnrank#handelsstrategie#sliderindex#maxfaktor#minfaktor#gefaelle#startsumme#maxdays#vertrauensfaktorAlgo#minguteUser#guteUserfaktor#gutePostingsfaktor#minaktivität#gewinnpostings#verlustpostings#anzthreads#neutralpostings");
	}

	public String getUsername()
	{
		return username_G;
	}

	public void setUsername(String username)
	{
		this.username_G = username;
	}

	public String getUsername_G()
	{
		return username_G;
	}

	public void setUsername_G(String username_G)
	{
		this.username_G = username_G;
	}

	public int getUsertype_G()
	{
		
		if(username_G.contains("Masteruser"))
			return GC.UsertypeMasteruser;
		else
			return GC.UsertypeStandart;
	}

	public void setUsertype_G(int usertype_G)
	{
		this.usertype_G = usertype_G;
	}

	public int getHandelsstrategie_G()
	{
		return handelsstrategie_G;
	}

	public void setHandelsstrategie_G(int handelsstrategie_G)
	{
		this.handelsstrategie_G = handelsstrategie_G;
	}

	public int getSliderindex_G()
	{
		return sliderindex_G;
	}

	public void setSliderindex_G(int sliderindex_G)
	{
		this.sliderindex_G = sliderindex_G;
	}

	public float getMaxfaktor_G()
	{
		return maxfaktor_G;
	}

	public void setMaxfaktor_G(float maxfaktor_G)
	{
		this.maxfaktor_G = maxfaktor_G;
	}

	public float getMinfaktor_G()
	{
		return minfaktor_G;
	}

	public void setMinfaktor_G(float minfaktor_G)
	{
		this.minfaktor_G = minfaktor_G;
	}

	public float getGefaelle_G()
	{
		return gefaelle_G;
	}

	public void setGefaelle_G(float gefaelle_G)
	{
		this.gefaelle_G = gefaelle_G;
	}

	public float getStartsumme_G()
	{
		return startsumme_G;
	}

	public void setStartsumme_G(float startsumme_G)
	{
		this.startsumme_G = startsumme_G;
	}

	public int getMaxdays_G()
	{
		return maxdays_G;
	}

	public void setMaxdays_G(int maxdays_G)
	{
		this.maxdays_G = maxdays_G;
	}

	public long getGewinnhandlungen_g()
	{
		return gewinnhandlungen_g;
	}

	public void setGewinnhandlungen_g(long gewinnhandlungen)
	{
		this.gewinnhandlungen_g = gewinnhandlungen;
	}

	public int getVertrauensfaktorAlgo_G()
	{
		return vertrauensfaktorAlgo_G;
	}

	public void setVertrauensfaktorAlgo_G(int vertrauensfaktorAlgo_G)
	{
		this.vertrauensfaktorAlgo_G = vertrauensfaktorAlgo_G;
	}

	public int getMinguteUser_G()
	{
		return minguteUser_G;
	}

	public void setMinguteUser_G(int minguteUser_G)
	{
		this.minguteUser_G = minguteUser_G;
	}

	public float getGuteUserfaktor_G()
	{
		return guteUserfaktor_G;
	}

	public void setGuteUserfaktor_G(float guteUserfaktor_G)
	{
		this.guteUserfaktor_G = guteUserfaktor_G;
	}

	public float getGutePostingsfaktor_G()
	{
		return gutePostingsfaktor_G;
	}

	public void setGutePostingsfaktor_G(float gutePostingsfaktor_G)
	{
		this.gutePostingsfaktor_G = gutePostingsfaktor_G;
	}

	public int getMinaktivität_G()
	{
		return minaktivität_G;
	}

	public void setMinaktivität_G(int minaktivität_G)
	{
		this.minaktivität_G = minaktivität_G;
	}

	public float getGesGewinn()
	{
		return gesgewinn_G;
	}

	public void setGesGewinn(float maxgewinn)
	{
		this.gesgewinn_G = maxgewinn;
	}

	public int getGewinnrank()
	{
		return gewinnrank_G;
	}

	public void setGewinnrank(int gewinnrank)
	{
		this.gewinnrank_G = gewinnrank;
	}

	public float getStabilfaktor_g()
	{
		return stabilfaktor_g;
	}

	public void setStabilfaktor_g(float stabilfaktor_g)
	{
		this.stabilfaktor_g = stabilfaktor_g;
	}

	public int getGewinnpostings_G()
	{
		return gewinnpostings_G;
	}

	public void setGewinnpostings_G(int gewinnpostings_G)
	{
		this.gewinnpostings_G = gewinnpostings_G;
	}

	public int getVerlustpostings_G()
	{
		return verlustpostings_G;
	}

	public void setVerlustpostings_G(int verlustpostings_G)
	{
		this.verlustpostings_G = verlustpostings_G;
	}

	public int getAnzneutralpostings_G()
	{
		return anzneutralpostings_G;
	}

	public void setAnzneutralpostings_G(int anzneutralpostings_G)
	{
		this.anzneutralpostings_G = anzneutralpostings_G;
	}

	public int getAnzthreads_G()
	{
		return anzthreads_G;
	}

	public void setAnzthreads_G(int anzthreads_G)
	{
		this.anzthreads_G = anzthreads_G;
	}

	public boolean storeEinzelgewinn(int index, String unam,
			UserEinPostingGewinnObj ueingew,String rootpath)
	{
		// rootpath ="UserThreadVirtualKonto\\Summengewinnedb"
		// hier wird für den user "username" ein einzelgewinn in der Gewinnliste
		// für den User abgelegt
		// ausserdem werden gleich statistikdaten für den User gespeichert

		if (unam.equalsIgnoreCase(username_G) == false)
			Tracer.WriteTrace(10, "Error: internal error username<"
					+ username_G + "> != unam<" + unam + ">");

		// passe Statistikdaten an
		float gew = ueingew.getGewinn();
		gesgewinn_G = gesgewinn_G + gew;
		gewinnhandlungen_g++;

		if (gew > 0)
			gewinnpostings_G++;
		if (gew < 0)
			verlustpostings_G++;
		if(gew==0)
			anzneutralpostings_G++;

		// die intervallstruktur aufbauen wenn noch nicht da
		if (userIntervallGewinne == null)
		{
			userIntervallGewinne = new UserSummenGewinneDBI(rootpath,username_G,
					intervalltage_glob, seqnr_glob);
		}
		// der Einzelgewinn wird hier für den user abgespeichert
		userIntervallGewinne.addEinzelgewinn(unam, ueingew);
		return true;
	}

	public void CalcReportUsergewinneDelMem(int erstflag, int auswertungsflag)
	{
		// hier werden die Usergewinne in den datenfilen ("summengewinne" und
		// "einzelgewinne") reportet
		// auswertungsflag:
		
		if (userIntervallGewinne != null)
			userIntervallGewinne.reportUsergewinneDelMem(erstflag, 0,
					auswertungsflag, this);

		// lösche den Zeitraumspeicher komplett(der Intervallspeicher wird erst
		// wieder aufgebaut
		// (wird eingelesen von platte) wenn für den user gewinne verzeichnet
		// werden)
		userIntervallGewinne = null;
	}

	public int getanzElementeZeitraumspeicher()
	{
		if (userIntervallGewinne == null)
			return 0;

		return (userIntervallGewinne.GetanzObj());
	}

	public void storeStabilitätsfaktor(String usernam, int seqnr,
			int algorithmus, GewinnStatObj gstat)
	{
		// Hier wird die stabilität für den user gespeichert
		if (usernam.equalsIgnoreCase(username_G) == false)
			Tracer.WriteTrace(10, "error internal error unam<" + usernam
					+ "> username<" + username_G + "> muss gleich sein");

		if (seqnr != 0)
			Tracer.WriteTrace(10,
					"error internal error nur seqnr=0 wird unterstuetzt seqnr<"
							+ seqnr + ">");

		if (algorithmus != 1)
			Tracer.WriteTrace(10,
					"error internal error nur index=1 wird unterstuetzt seqnr<"
							+ algorithmus + ">");

		stabilfaktor_g = gstat.getGewabweichungsSumme();
	}

	public String zeigeStabilitätsinfo()
	{
		// gibt einen Infostring zurück der die Stabilitätsinfo aufzeigt
		String retstrg = username_G + ":" + stabilfaktor_g;
		return retstrg;
	}
	public void postprocess()
	{}
}
