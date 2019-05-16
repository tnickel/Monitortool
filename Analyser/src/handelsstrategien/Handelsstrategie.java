package handelsstrategien;

import hilfsklasse.Inf;
import hilfsklasse.Tracer;
import mainPackage.GC;
import ranking.Vertrauen;
import slider.PostSlider;
import slider.Sliderbewert;

public class Handelsstrategie
{
	private PostSlider postslider = null;
	private float udb_anzahluser = 0;

	public Handelsstrategie(PostSlider pl, float useranzahl)
	{
		// useranzahl=anzahl der user in der userdb
		postslider = pl;
		udb_anzahluser = useranzahl;
	}

	public Vertrauen VertrauensfaktorMasteruser(float gutUfaktor,
			float gutPfaktor, Sliderbewert slb, int sliderindex,
			int sliderpostingsanz, float maxfaktor, float gefaelle,
			int minguteuser, int minaktivitaet)
	{
		float vertrauensfaktor = 0;
		float durchschnittrank = slb.getMitlrank();
		// int sliderpostingsanz = sliderliste[sliderindex].size();
		String handelsabbruch = "fehler";
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\vertrauensfaktor4.csv");

		// float maxanzahluser = udb_glob.GetanzObj();
		float maxanzahluser = udb_anzahluser;

		if ((slb.getGuteU() * gutUfaktor) < slb.getSchlechteU())
		{
			handelsabbruch = new String("weil guteU(" + slb.getGuteU()
					* gutUfaktor + ")" + "< schlU(" + slb.getSchlechteU() + ")");
			vertrauensfaktor = 0;
		} else if (slb.getGuteU() < minguteuser)
		{
			handelsabbruch = new String("weil guteU(" + slb.getGuteU() + ")"
					+ "< (" + minguteuser + ")");
			vertrauensfaktor = 0;
		}

		else if (slb.getAnzgutePostings() * gutPfaktor < slb
				.getAnzschlechtePostings())
		{
			handelsabbruch = "weil guteP(" + slb.getAnzgutePostings()
					* gutPfaktor + ")<schlP(" + slb.getAnzschlechtePostings()
					+ ")";
			vertrauensfaktor = 0;

		}
		// bei zu wenig Postings oder zu wenigen usern ist auch nix los
		else if ((sliderpostingsanz < minaktivitaet)
				|| (slb.getUseranz() < minaktivitaet))
		{
			if ((sliderpostingsanz < minaktivitaet))
				handelsabbruch = "zu wenig postings min=" + minaktivitaet + "";
			else
				handelsabbruch = "zu wenig user min=" + minaktivitaet + "";

			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + 0.1f);
			inf.writezeile("+Postings<" + slb.getAnzgutePostings()
					+ ">; -Postings<" + slb.getAnzschlechtePostings()
					+ ">; anzUser<" + slb.getPostanz() + ">; guteU<"
					+ slb.getGuteU() + ">; schlU<" + slb.getSchlechteU()
					+ ">; maxfaktor<" + maxfaktor + ">; gefaelle<" + gefaelle
					+ ">; durchrank<" + durchschnittrank + ">; maxfaktor<"
					+ maxfaktor + ">; vertrauensfaktor<" + vertrauensfaktor
					+ ">");
			if (vertrauensfaktor < 0)
			{
				handelsabbruch = "neg. vertrauensfaktor(" + vertrauensfaktor
						+ ")";
				vertrauensfaktor = 0;
			}
		}

		int sumrp = (int) durchschnittrank * sliderpostingsanz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				sliderpostingsanz, durchschnittrank, 0.1f, maxfaktor, gefaelle,
				slb.getAnzgutePostings(), slb.getAnzschlechtePostings());

		// falls vertrauensfaktor > 0 ==> dann kaufe
		if (vertrauensfaktor > 0)
			slb.setHandelshinweis(new String("Kaufe fuer <" + vertrauensfaktor
					* 100 + ">Euro"));
		else
			slb.setHandelshinweis(new String(handelsabbruch));

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> sliderpostingsanz<"
					+ sliderpostingsanz + "> druchrank<" + durchschnittrank
					+ "> minfaktor<" + 0.1f + "> maxfaktor<" + maxfaktor
					+ "> gefaelle<" + gefaelle + ">");

		return vertrau;
	}
}
