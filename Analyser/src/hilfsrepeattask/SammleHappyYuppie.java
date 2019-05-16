package hilfsrepeattask;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import html.YuppiePageLang;
import html.YuppiePageMittel;
import internetPackage.DownloadManager;
import mainPackage.GC;
import objects.YuppieDbObj;
import stores.YuppieDB;
import stores.YuppieDBzeile;

public class SammleHappyYuppie
{
	static DownloadManager Wpage = new DownloadManager(GC.MAXLOW);

	static String yzeile[] = new String[30];

	public void start()
	{
		String zeile = null;
		YuppieDB ydb = new YuppieDB();
		if ((FileAccess.FileAvailable(ydb.GetDbFilename()) == false))
		{
			Tracer.WriteTrace(20, "I:DB file <" + ydb.GetDbFilename()
					+ "> not available, generate new out of masterfile");
			ydb.GenerateMasterliste();
		}
		YuppieDbObj ydbo = null;
		int anz = ydb.GetanzObj();
		ydb.SetDurchlaufmode(GC.MODE_ALL);
		int i = 1;

		ydb.ResetDB(i - 1);
		while ((ydbo = (YuppieDbObj) ydb.GetNextObject()) != null)
		{
			i++;
			System.out.println("(" + i + "|" + anz + ")");
			String tname = ydbo.getAktname();
			if (ydbo.getState() != 0)
			{
				Tracer.WriteTrace(20, "I: tname<" + tname + "> state<"
						+ ydbo.getState() + "> -> continue");
				continue;
			}

			String wkn = ydbo.getWkn();
			YuppieDBzeile yuplauf = new YuppieDBzeile(tname);

			String akttime = Tools.get_aktdate_str("dd.MM.yy");
			String lastprognosedatum = yuplauf.GetLastPrognoseDatum();

			if (akttime.equals(lastprognosedatum))
			{
				System.out.println("Prognose für <" + tname
						+ "> heute schon geladen");
				continue;
			}
			// lade mittleres signal
			String urlstr = "http://www.happyyuppie.com/cgi-bin/de/search.pl?string="
					+ wkn + "&x=13&y=7";
			String fname = GC.rootpath + "\\tmp\\yuppiechartsignalm.html";

			if (FileAccess.FileAvailable(fname))
				FileAccess.FileDelete(fname,0);

			Tracer.WriteTrace(50, "I:lade Webseite <" + urlstr + "> file<"
					+ fname + ">");
			Wpage.DownloadHtmlPage(urlstr, fname, 0, 50000, 1, 0, 0);
			YuppiePageMittel yuppiemittel = new YuppiePageMittel(fname);

			// hole die isin, wenn das nicht möglich ist die wkn falsch, somit
			// ist die yuppiedb-falsch
			String isin = yuppiemittel.GetIsin();

			if (isin == null)
			{ // ein Eintrag in der yuppiedb ist fehlerhaft, masterfile wohl
				// fehlerhaft
				Tracer.WriteTrace(20, "W: ISIN konnte nicht ermittelt werden <"
						+ urlstr + "> file<" + fname
						+ "> setzte yuppiedb state auf 99");
				ydbo.setState(99);
				ydb.WriteDB();
				continue;
			}

			yzeile[0] = new String(yuppiemittel.GetIsin());
			yzeile[1] = new String(yuppiemittel.GetAktKurs());
			String kursdatum = new String(yuppiemittel.GetKursDatum());
			if (kursdatum.equals("0") == true)
			{
				Tracer.WriteTrace(20, "W: Kursdatum nicht ermittelt werden <"
						+ urlstr + "> file<" + fname
						+ "> ueberspringe Prognose");
				continue;

			}
			yzeile[2] = kursdatum;
			yzeile[3] = new String(yuppiemittel.GetHandelssignal());
			yzeile[4] = new String(yuppiemittel.GetHandelsvalue());
			yzeile[5] = new String(yuppiemittel.GetHandelssignalSeit());
			yzeile[6] = new String(yuppiemittel.GetRisikoklasse());
			yzeile[7] = new String(yuppiemittel.GetVolatilität());
			yzeile[8] = new String(yuppiemittel.GetKursNachCrash());
			yzeile[9] = new String(yuppiemittel.GetVolumenHeute());
			yzeile[10] = new String(yuppiemittel.GetStärke());
			yzeile[11] = new String(yuppiemittel.GetPrognoseN(1));
			yzeile[12] = new String(yuppiemittel.GetPrognoseN(2));
			yzeile[13] = new String(yuppiemittel.GetPrognoseN(3));
			yzeile[14] = new String(yuppiemittel.GetPrognoseN(4));
			yzeile[15] = new String(yuppiemittel.GetPrognoseN(5));

			ydbo.setIsin(new String(yzeile[0]));
			ydbo.setBranchesektor(new String(yuppiemittel.GetBranchesektor()));
			ydbo.setLand(new String(yuppiemittel.GetLand()));
			ydbo.setBörse(new String(yuppiemittel.GetBörse()));
			ydbo.setBenchmark(new String(yuppiemittel.GetBenchmark()));
			ydbo.setSigdatmed(new String(yuppiemittel.GetHandelssignalSeit()));
			ydbo.setBetrachtungszeitraum(new String(yuppiemittel
					.GetBetrachtungszeitraum()));
			ydbo.setGewinnMittel(new String(yuppiemittel.GetGewinn()));
			ydbo.setÄnderungMittel(new String(yuppiemittel.GetÄnderung()));

			// lade langes signal
			urlstr = "http://www.happyyuppie.com/cgi-bin/de/search.pl?isin="
					+ yzeile[0] + "&typnr=4";
			fname = GC.rootpath + "\\tmp\\yuppiechartsignall.html";

			if (FileAccess.FileAvailable(fname))
				FileAccess.FileDelete(fname,0);

			Tracer.WriteTrace(50, "I:lade Webseite <" + urlstr + "> file<"
					+ fname + ">");
			Wpage.DownloadHtmlPage(urlstr, fname, 0, 50000, 1, 0, 0);
			YuppiePageLang yuppielang = new YuppiePageLang(fname);

			yzeile[16] = new String(yuppielang.GetHandelssignal());
			yzeile[17] = new String(yuppielang.GetHandelsvalue());
			yzeile[18] = new String(yuppielang.GetHandelssignalSeit());
			yzeile[19] = new String(yuppielang.GetPrognoseN(1));
			yzeile[20] = new String(yuppielang.GetPrognoseN(2));
			yzeile[21] = new String(yuppielang.GetPrognoseN(3));
			yzeile[22] = new String(yuppielang.GetPrognoseN(4));
			yzeile[23] = new String(yuppielang.GetPrognoseN(5));

			ydbo.setSigdatlong(new String(yuppielang.GetHandelssignalSeit()));
			ydbo.setGewinnLang(new String(yuppielang.GetGewinn()));
			ydbo.setÄnderungLang(new String(yuppielang.GetÄnderung()));

			zeile = "";
			for (int j = 0; j < 24; j++)
				zeile = zeile.concat("#" + yzeile[j]);
			yuplauf.UpdateYuppiePrognose(zeile);

			yuplauf.WriteDB();

			ydb.WriteDB();
		}
	}
}
