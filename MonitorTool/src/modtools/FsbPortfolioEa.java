package modtools;

import data.Metaconfig;

public class FsbPortfolioEa
{
	public FsbPortfolioEa()
	{}
	public boolean checkIsPortfolioEa(int magic,Metaconfig meconf)
	{
		//prüft ob dies ein portfolio ea is
		//hier ist die schwierigkeit das die magic länger als die basemagic ist
		//und er EA-name nur die basemagic beinhaltet
		//bsp: magic=21040112345
		//basemagic=210401
		//d.h. im mql4/experts-verzeichniss befindet sich nur
		//das file "P01 EURUSD M1 210401.mq4"
		
		//Vorgehen:
		//1)Kein Portofolio EA
		//prüfe nach ob die volle magic in eines der Files ist,
		//ist dies der fall ist dies kein Portfolio EA
		//=> return false
		
		//2)ein Portfolio EA
		//Fall1) ist nicht eingetreten, jetzt werden die ersten 6 Stellen genommen
		//und die Files nach diesem Praefix untersucht
		//Falls gefunden haben wird für diese magic das passende EA-file gefunden
		//=> return true
		return true;
	}
	public String getPortfolioEaName(int magic, Metaconfig meconf)
	{
		//sucht den portfolio ea auf platte und liefert den namen zurück
		return null;
	}
	public void setLotsize(int magic,Metaconfig meconf, String eaname, double lotsize)
	{
		
		
	}
	public double getLotsize(int magic,Metaconfig meconf, String eaname)
	{
		
		return 0;
	}
	public void delete(int magic, Metaconfig meconf, String eaname)
	{
		
	}
}
