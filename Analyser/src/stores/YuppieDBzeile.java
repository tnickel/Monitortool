package stores;

import hilfsklasse.Tools;
import db.DBlauf;

public class YuppieDBzeile extends DBlauf
{

	public YuppieDBzeile(String kennung)
	{
		LoadDB("Yuppie", kennung);
	}

	public void UpdateYuppiePrognose(String yuppiezeile)
	{
		String date = Tools.get_aktdatetime_str();
		this.UpdateZeile(date + yuppiezeile, 8);
		this.WriteDB();
	}

	public String GetLastPrognoseDatum()
	{
		// return
		// null: wenn da nix da ist
		// last Prognosedatum
		String z = this.GetLastZeile();
		if (z == null)
			return null;

		z = z.substring(0, 8);
		return z;
	}
}
