package hilfsrepeattask;

import internetPackage.DownloadManager;
import kurse.KursIconObj;
import kurse.KursiconDB;
import mainPackage.GC;
import objects.ThreadDbObj;

import org.eclipse.swt.widgets.ProgressBar;

import stores.ThreadsDB;

public class SammleIcons
{
	// sammelt alle icons
	private static DownloadManager Wpage = new DownloadManager(GC.MAXLOW);

	public void start(int thresholdstunden,ProgressBar pb)
	{
		ThreadsDB tdb = new ThreadsDB();
		ThreadDbObj tdbo = null;
		int anz = tdb.GetanzObj();
		int i = 1, j = 0;
		KursiconDB kidb = new KursiconDB();
		tdb.ResetDB(-1);
		pb.setMinimum(0);
		pb.setMaximum(anz);
		if (kidb.calcHoursOfSpeicherfile() > thresholdstunden)
			while ((tdbo = (ThreadDbObj) tdb.GetNextObject()) != null)
			{
				System.out.println("Icon(" + i + "|" + anz + ")");
				i++;
				pb.setSelection(i);
				
				int masterid = tdbo.getMasterid();

				// holt das iconobjekt mit der masterid
				KursIconObj kio = kidb.GetKursIconObj(masterid);

				// falls es da ist dann schaue nach ob aktuell
				if (kio != null)
					kio.UpdateObject(Wpage);
				else
				{
					// noch kein kio vorhanden dann generiere es das erste mal
					kio = new KursIconObj(masterid, Wpage);
					kidb.AddObject(kio);
				}
				kidb.WriteDB();
			}

	}
}
