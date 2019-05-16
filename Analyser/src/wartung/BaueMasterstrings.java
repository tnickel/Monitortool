package wartung;

import hilfsklasse.Inf;
import mainPackage.GC;
import objects.ThreadDbObj;

import org.eclipse.swt.widgets.ProgressBar;

import stores.MidDB;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import stores.UserDB;

public class BaueMasterstrings
{
	public void start(int startid,ProgressBar pb)
	{
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\wartung.csv");
		SlideruebersichtDB sldb = new SlideruebersichtDB();

		ThreadsDB tdb = new ThreadsDB();
		MidDB middb=new MidDB(tdb);
		UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");

		//a) die primepage neu laden und middb mit den midstrings ergänzen
		tdb.updateAllPrimepagesParallel(40, 1, 10, sldb, 480, inf,pb);
		
		//b) nochmal alles checken
		int anz = tdb.GetanzObj();
		for (int i = startid; i < anz; i++)
		{
			ThreadDbObj obj = (ThreadDbObj) tdb.GetObjectIDX(i);

			// die Kompressung überprüfen
			obj.updateCompressedThread(middb,i, anz, tdb, udb, 1);

			System.out.println("gehe einen thread weiter");
			tdb.SetNextObject();
		}
	}
}
