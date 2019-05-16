package kurse;

import hilfsklasse.SG;
import hilfsklasse.TDate;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import html.Chart;
import internetPackage.DownloadManager;
import mainPackage.GC;
import objects.Obj;

public class KursIconObj extends Obj
{
	private int masterid = 0;
	private int marketid = -1;
	private String[] dateinfo = new String[6];

	// sagt mir wie lange das icon gültig ist
	private static int[] validdays =
	{ 1, 5, 30, 90, 365, 365 * 5 };
	private static String[] kuerzel =
	{ "1d", "5d", "1m", "3m", "1y", "5y" };
	private static String[] gb =
	{ "60m", "60m", "1d", "1d", "", "" };

	public KursIconObj(String zeile)
	{
		try
		{
			if (SG.countZeichen(zeile, "#") == 6)
			{
				masterid = Integer.valueOf(SG.nteilstring(zeile, "#", 1));

				for (int j = 0; j < 6; j++)
				{
					dateinfo[j] = new String(SG.nteilstring(zeile, "#", 2 + j));
				}
			} else if (SG.countZeichen(zeile, "#") == 7)
			{
				masterid = Integer.valueOf(SG.nteilstring(zeile, "#", 1));
				marketid = Integer.valueOf(SG.nteilstring(zeile, "#", 2));
				for (int j = 0; j < 6; j++)
				{
					dateinfo[j] = new String(SG.nteilstring(zeile, "#", 3 + j));
				}
			}

		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public KursIconObj(int masterid_l, DownloadManager Wpage)
	{
		// generiert das KursIconobj und läd alle icons
		masterid = masterid_l;
		downloadIcons(Wpage);

	}

	@Override
	public String GetSaveInfostring()
	{
		return "masterid#marketid#1d#5d#1m#3m#1y#5y";
	}

	@Override
	public String toString()
	{
		return (masterid + "#" + marketid + "#" + dateinfo[0] + "#"
				+ dateinfo[1] + "#" + dateinfo[2] + "#" + dateinfo[3] + "#"
				+ dateinfo[4] + "#" + dateinfo[5]);
	}

	public int getMasterid()
	{
		return masterid;
	}

	public void setMasterid(int masterid)
	{
		this.masterid = masterid;
	}

	public int getMarketid()
	{
		return (marketid);
	}

	public void setMarketid(int mid)
	{
		this.marketid = mid;
	}

	public String getDateinfo(int index)
	{
		return dateinfo[index];
	}

	public void setDateinfo(int index, String date)
	{
		dateinfo[index] = new String(date);
	}

	public boolean UpdateObject(DownloadManager Wpage)
	{
		// updatet das object, also schaut nach ob alle icons aktuell sind
		downloadIcons(Wpage);
		return true;
	}

	private String GenUrlString(TDate aktdate, int masterid, int markid,
			String kuerzel, String gb)
	{
		// String
		// urlstr="http://aktien.wallstreet-online.de/charts/instinformer.php?&inst_id="+masterid+"&market_id=2&spid=ws&tr=1d&ct=line&grid=on&gb=10m&log=0&redvol=0&gd1=0&gd2=0&size=tool";
		// http://aktien.wallstreet-online.de/charts/instinformer.php?inst_id=14723&market_id=8&spid=ws&edit=zeichnen&utr=3m&gb=1d&till_day=5&till_month=12&till_year=2008&ct=line&ind1=&tr=3m&ind2=&log=0&bench=&redvol=0&gd1=0&gd1typ=exp&benchstr=&gd2=0&gd2typ=exp&grid=on

		String urlstr = new String(
				"http://aktien.wallstreet-online.de/charts/instinformer.php?&inst_id="
						+ masterid
						+ "&market_id="
						+ markid
						+ "&spid=ws&edit=zeichnen&utr="
						+ kuerzel
						+ "&gb="
						+ gb
						+ "&till_day="
						+ aktdate.getVal("d")
						+ "&till_month="
						+ aktdate.getVal("m")
						+ "&till_year="
						+ aktdate.getVal("y")
						+ "&ct=line&ind1=&tr="
						+ kuerzel
						+ "&ind2=&log=0&bench=&redvol=0&gd1=0&gd1typ=exp&benchstr=&gd2=0&gd2typ=exp&grid=on");
		return urlstr;
	}

	private boolean downloadIcons(DownloadManager Wpage)
	{
		// prüft wann das objekt zum letzten Male geladen wurde
		// wenn die icons zu alt sind dann lade neu

		int anzkuerzel = kuerzel.length;

		if (marketid < 0)
		{
			String urlstr = new String("http://aktien.wallstreet-online.de/"
					+ masterid + "/chart.html");
			String fname = GC.rootpath + "\\tmp\\chart.html";
			System.out.println("Lade marketid mid=" + masterid);
			Wpage.DownloadHtmlPage(urlstr, fname, 0, 50000, 1, 1, 0);
			Chart charthtml = new Chart(fname);
			marketid = charthtml.GetMarketid();
			if (marketid < 0)
			{
				// hierfür gibt es keine marketid und somit keinen kurs
				marketid = 99;
				Tracer.WriteTrace(20, "I:no marketid für masterid <" + masterid
						+ "> set state 99");
				return false;
			}
		}
		// hier gibt es nix zu holen
		if (marketid == 99)
			return true;

		for (int j = 0; j < anzkuerzel; j++)
		{
			TDate icondate = new TDate(dateinfo[j]);
			TDate aktdate = new TDate();

			int maxdiffdays = (validdays[j] / 3);
			if ((icondate == null)
					|| (icondate.Tdiff(aktdate, "t") > maxdiffdays))
			{
				// wenn veraltet lade das icon neu und setzte das datum
				String urlstr = GenUrlString(aktdate, masterid, marketid,
						kuerzel[j], gb[j]);
				String fname = GC.rootpath + "\\db\\kursicons\\" + masterid
						+ "_" + kuerzel[j] + ".png";
				Wpage.DownloadHtmlPage(urlstr, fname, 0, 50000, 0, 1, 0);
				dateinfo[j] = aktdate.GetaktDatestring();
			}
		}
		return true;
	}

}
