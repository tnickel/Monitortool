package stores;

import db.DBlauf;

public class SignaleDBzeile extends DBlauf
{

	public SignaleDBzeile(String kennung)
	{
		LoadDB("signale", kennung);
	}

	public void UpdateSignale(String zeile)
	{

		this.UpdateZeile(zeile, zeile.length());
		this.WriteDB();
	}
}
