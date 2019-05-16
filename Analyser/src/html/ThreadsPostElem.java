package html;

import hilfsklasse.Tracer;

public class ThreadsPostElem
{
	// Dies ist ein einzelnes Posting einer HTML-Seite

	private String username = null;
	private int threadid = 0;
	private String datetime = null;
	private int postid = 0;

	// Die Postingzeile beinhaltet die Zeile des Postings
	private String Postingzeile = null;

	public ThreadsPostElem()
	{
	}

	public ThreadsPostElem(String d, int pid, String uname, int id)
	{
		datetime = d;
		postid = pid;
		username = uname;
		threadid = id;
	}

	public ThreadsPostElem(String alles, String postline)
	{
		//postline ausgeben
		//Tracer.WriteTrace(20, "Info: betrachte postline<"+postline+">");
		
		
		

		int postid = Integer.parseInt(Keyword.GetBetragsnummer(postline));
		Tracer.WriteTrace(20, "Info: postid<"+postid+">");
		String username = new String(Keyword.GetBenutzername(postline));
		String date = new String(Keyword.GetDateTime(postline));
		String threadid_str = new String(Keyword.GetThreadIdFromMem(alles));

		set_postid(postid);
		set_username(username);
		set_datetime(date);
		set_postingzeile(postline);
		set_threadid(Integer.parseInt(threadid_str));
		if(plausicheck()==false)
			Tracer.WriteTrace(10, "Error: plausicheck, webseite falscher aufbau postline<"+postline+">");
	}

	public boolean plausicheck()
	{
		while (5 == 5)
		{
			if (datetime.length() != 17)
			{
				Tracer.WriteTrace(20, "Error:datetime plausi datetime<"+datetime+">");
				break;
			}
			if (postid < 0)
			{
				Tracer.WriteTrace(20, "Error: postid<"+postid+">");
				break;
			}
			if (username.length() == 0)
			{
				username=new String("**SET**EMPTY**");
				Tracer.WriteTrace(20, "Error: username<"+username+">");
				return true;
			}
			if (threadid == 0)
			{
				Tracer.WriteTrace(20, "Error: threadid<"+threadid+">");
				break;
			}
			return true;
		}

		Tracer.WriteTrace(20, "W: OnePostingObj Plausicheck datetime<"
				+ datetime + "> postid<" + postid + "> username<" + username
				+ "> threadid<" + threadid + "> zeilelen<"
				+ Postingzeile.length() + ">");

		return false;
	}

	public void set_datetime(String d)
	{
		datetime = d;
	}

	public void set_postid(int pid)
	{
		postid = pid;
	}

	public void set_username(String uname)
	{
		username = uname;

	}

	public void set_threadid(int id)
	{
		threadid = id;

	}

	public void set_postingzeile(String pz)
	{
		Postingzeile = new String(pz);
	}

	public String get_datetime()
	{
		return (datetime);
	}

	public String get_postingzeile()
	{
		return Postingzeile;
	}

	public String get_username()
	{
		return (username);
	}

	public int get_threadid()
	{
		return threadid;
	}

	public int get_postid()
	{
		return (postid);
	}

}
