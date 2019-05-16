package stores;

import db.DB;


public class UserCharakterDB extends DB
{
	public UserCharakterDB()
	{
		LoadDB("usercharakter", null, 0);
	}
	
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public void postprocess()
	{}
}
