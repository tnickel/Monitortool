package ranking;

import java.util.ArrayList;
import java.util.List;

import objects.UserDbObj;
import objects.UserThreadPostingObj;

public class UserStatistik
{
	private List<UserDbObj> ul = new ArrayList<UserDbObj>();

	public UserStatistik()
	{
	}

	public void addUser(UserDbObj udbo)
	{
		ul.add(udbo);
	}

	public String printUserliste(int threadid, String username)
	// gibt eine liste aus welche anderen (ausser username)diesen
	// thread(threadid) beobachten
	{
		UserThreadPostingObj upost = null;
		List<String> unams = new ArrayList<String>();
		for (int i = 0; i < ul.size(); i++)
		{
			UserDbObj udbo = ul.get(i);
			udbo.ResetPostingObj();
			// baue eine liste mit den usernamen auf die den gleichen thread
			// beobachten

			if (udbo.get_username().equalsIgnoreCase(username) == false)
			{
				// der username ist anders
				// xxxx fehler, es muss die ganze Liste durchsucht werden ?
				while ((upost = udbo.getNextUserTheadPostingObj()) != null)
				{
					if (upost.getThreadid() == threadid)
					{
						// der andere user beobachtet auch den thread
						unams.add(udbo.get_username());
						continue;
					}
				}
			}
		}
		System.out.println("unams=" + unams.toString());

		return unams.toString();
	}
}
