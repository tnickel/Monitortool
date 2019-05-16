package hilfsklasse;

public class PostingObj_dep
{
	private static String username = null;
	private static int threadid = 0;
	private static String date = null;
	private static int postid = 0;

	public PostingObj_dep()
	{
	}

	public void set_date(String d)
	{
		date = d;
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

	public String get_akt_date()
	{
		return (date);
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
