package stores;

import db.DB;

public class UsernameErsetzungsDB extends DB
{
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public UsernameErsetzungsDB()
	{
		LoadDB("usernameersetzungsdb", null, 0);

	}
	public void postprocess()
	{}
}
