package wartung;

import html.ThreadsPostElem;
import mainPackage.GC;
import objects.ThreadDbObj;
import stores.MidDB;
import stores.ThreadsDB;
import stores.UserDB;
import basis.BAWorkThreads;

public class VergibHitpoints extends BAWorkThreads
{
	UserDB udb = new UserDB("observeuser.txt","boostraenge.txt");
	String username = null, threadname = null;
	int tid = 0, postid = 0, breakindex;

	@Override
	public void BAworkOnePostingAbstract(ThreadsPostElem obj)
	{// hier wird ein posting verarbeitet

		breakindex = BAgetaktBreakid();
		username = obj.get_username();
		postid = obj.get_postid();
		threadname = BAgetThreadname();

		// System.out.println("name="+username+" postid="+obj.get_postid()+"
		// bi="+breakindex);
		System.out.println("postid<" + obj.get_postid() + "> bi<" + breakindex
				+ ">");
		// nur hitpoints vergeben wenn postid noch kleiner als breakindex
		if (postid < breakindex)
			udb.hit_user(threadname, username, tid);
		else
			// geht einen Thread weiter da der breakindex überschritten
			BAsetNextThread();
	}

	@Override
	public void BAworkOneThreadPageAbstract(String Threadname, String fnam,
			int count, int max, int pcount, int maxpagecount, ThreadDbObj tdbo,
			ThreadsDB tdb,MidDB middb)
	{// hier wird eine HTML-Seite verarbeitet
		BAsetHtml(fnam);
		System.out.println("Suche Hitpoints in<" + fnam + ">");
		tid = BAgetaktThreadId();
		BAdurchlaufHtml(middb,tdb,tdbo, pcount);

	}

	public void StartDurchwandereThreads()
	{// hier werden alle threads verarbeitet
		BAsetDurchlaufmode(GC.MODE_ONLYBREAKID, "");
		BAdurchlaufThreads();
		udb.writeHitpointstore();
	}
}
