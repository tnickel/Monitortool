package wartung;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;
import html.Htmlcompress;
import mainPackage.GC;
import objects.UserDbObj;
import stores.MidDB;
import stores.ThreadsDB;
import stores.UserDB;

public class CompressUserhtmlpages
{
	// komprimiert die Html-seiten in userhtmlpages
	// macht aus 4 Webseiten eine Webseite und legt die Infos ab
	//Htmlcompress hw = new Htmlcompress();

	public void CompressUerhtmlpages()
	{
	}

	private void deleteAllUserinfo(String username)
	{
		char first=username.charAt(0);
		
		if (FileAccess.FileAvailable(GC.rootpath
				+ "\\downloaded\\userhtmlpages\\" + username + ".html.gzip") == true)
			FileAccess.FileDelete(GC.rootpath + "\\downloaded\\userhtmlpages\\@"+first+"\\"
					+ username + ".html.gzip",0);

		if (FileAccess.FileAvailable(GC.rootpath
				+ "\\downloaded\\userhtmlpages\\@"+first+"\\" + username
				+ "_blogs.html.gzip") == true)
			FileAccess.FileDelete(GC.rootpath + "\\downloaded\\userhtmlpages\\@"+first+"\\"
					+ username + "_blogs.html.gzip",0);

		if (FileAccess.FileAvailable(GC.rootpath
				+ "\\downloaded\\userhtmlpages\\@"+first+"\\" + username
				+ "_postings.html.gzip") == true)
			FileAccess.FileDelete(GC.rootpath + "\\downloaded\\userhtmlpages\\@"+first+"\\"
					+ username + "_postings.html.gzip",0);

		if (FileAccess.FileAvailable(GC.rootpath
				+ "\\downloaded\\userhtmlpages\\@"+first+"\\" + username
				+ "_threads.html.gzip") == true)
			FileAccess.FileDelete(GC.rootpath + "\\downloaded\\userhtmlpages\\@"+first+"\\"
					+ username + "_threads.html.gzip",0);

	}

	public boolean CompressUser(MidDB middb,ThreadsDB tdb,String username)
	{
		Htmlcompress hw = new Htmlcompress(tdb);
		// komprimiert die 4 Userhtml-Seiten zu einer Seite und legt diese
		// im gzip-Format ab
		
		if(username.contains("pp-matrix"))
			System.out.println("found pp-matrix");
		
		char first=username.charAt(0);
		try
		{
			hw
					.cleanCompressHtmlFile(middb,GC.rootpath
							+ "\\downloaded\\userhtmlpages\\@"+first+"\\" + username
							+ ".html.gzip", GC.rootpath
							+ "\\downloaded\\userhtmlpages\\@"+first+"\\" + username
							+ ".html.gzip",0,"NO2");
			return true;

		} catch (NumberFormatException e)
		{
			Tracer.WriteTrace(20, "W: NumberFormat Exception <" + username
					+ ">");
			deleteAllUserinfo(username);
			return false;
		}

	}

	public void startCompressAll(MidDB middb,ThreadsDB tdb)
	{
		UserDB udb = new UserDB("observeuser.txt","boostraenge.txt");
		int anz = udb.GetanzObj();
		String uname = null;
		udb.ResetDB(-1);
		

		int defektuser=0;
		int defektcompress=0;
		
		for (int i = 0; i < anz; i++)
		{
			UserDbObj udbo = (UserDbObj) udb.GetNextObject();
			if (udbo == null)
				break;
			uname = udbo.get_username();

			if(defektcompress>3)
				Tracer.WriteTrace(10, "Error: 3 Defekte user hintereinander!! -> STOP");
			
			Tracer.WriteTrace(50, "I:<" + i + "|" + anz + ">komprimiere user <"
					+ uname + ">");
			if ((uname.contains("?") == true) || (uname.contains("&") == true))
			{
				Tracer.WriteTrace(20, "W: illegal username <" + uname + ">");
				continue;
			}
			// falls der User fehlerhaft ist
			if (udbo.getState() != 0)
			{
				defektuser++;
				Tracer.WriteTrace(20, "W: <" + i + "|" + anz + "> defzähl<"+defektuser+"> userstate=<" + udbo.getState()
						+ ">  username=<" + uname + "> -> überspringe");
				continue;
			}
			
			if (CompressUser(middb,tdb,uname) == false)
			{
				defektcompress++;
				udbo.setState(10);
				Tracer.WriteTrace(20, "W: user<" + uname
						+ " fehlerhaft setze state=10");
				udb.WriteDB();
			}
			else
				defektcompress=0;
		}
	}
}
