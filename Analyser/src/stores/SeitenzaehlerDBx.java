package stores;

import hilfsklasse.Tools;
import db.DBlauf;

public class SeitenzaehlerDBx extends DBlauf
{

	public SeitenzaehlerDBx(String kennung)
	{
		LoadDB("Seitenzaehler", kennung);
	}

	public void UpdateLeserHeute(int anz)
	{
		String date = Tools.get_aktdatetime_str();
		this.UpdateZeile(date + " " + Tools.get_aktWochentag_str() + "#" + anz,
				8);
		this.WriteDB();
	}
}
