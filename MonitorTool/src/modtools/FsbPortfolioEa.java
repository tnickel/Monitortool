package modtools;

import data.Metaconfig;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;
import mqlLibs.Eaclass;

public class FsbPortfolioEa
{
	public FsbPortfolioEa()
	{
	}
	
	static public boolean checkIsPortfolioEa(int magic, Metaconfig meconf)
	{
		// prüft ob dies ein portfolio ea is
		// hier ist die schwierigkeit das die magic länger als die basemagic ist
		// und er EA-name nur die basemagic beinhaltet
		// bsp: magic=21040112345
		// basemagic=210401
		// d.h. im mql4/experts-verzeichniss befindet sich nur
		// das file "P01 EURUSD M1 210401.mq4"
		
		// Vorgehen:
		// 1)Kein Portofolio EA
		// prüfe nach ob die volle magic in eines der Files ist,
		// ist dies der fall ist dies kein Portfolio EA
		// return false;
		if (isStandartEa( magic,  meconf)==true)
			return false;
			
		// 2)ein Portfolio EA
		// Fall1) ist nicht eingetreten, jetzt werden die ersten 6 Stellen genommen
		// und die Files nach diesem Praefix untersucht
		// Falls gefunden haben wird für diese magic das passende EA-file gefunden
		// => return true
		// nimm die ersten 6 stellen der magic und suche den EA
		
		//magic to short, this is not a portfolio ea
		if(String.valueOf(magic).length()<5)
			return false;
		
		String magicpref = String.valueOf(magic).substring(0, 6);

		//magic 8 is a magic from a realbroker channel
		if(magicpref.length()<3)
		  return false;
		
		if (searchEaMagic(Integer.valueOf(magicpref), meconf) != null)
			return true;
		
		Tracer.WriteTrace(20, "I: Ea with magic<" + magic + "> not on harddisk path<" + meconf.getExpertdata() + ">");
		return false;
		
	}
	static public Boolean isStandartEa(int magic, Metaconfig meconf)
	{
		if (searchEaMagic(magic, meconf) != null)
			return true;
		return false;
	}
		
	static public String searchEaMagic(int magic, Metaconfig meconf)
	{
		// sucht den portfolio ea auf platte und liefert den namen zurück
		String verz = meconf.getExpertdata();
		FileAccess.initFileSystemList(verz, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			int fnam_magic=Eaclass.calcMagicSiml(fnam);
			
			if (fnam_magic==magic)
				return fnam;
		}
		return null;
		
	}
	
	static public double getLotsize(int magic, Metaconfig meconf, String eaname)
	{
		MqlPatch mfx = new MqlPatch();
		mfx.setFilename(eaname);
		double lots=mfx.getFsbPortfolioLotsize();
		return lots;
	}
	
	static public void deletePortfolioEa(int magic, Metaconfig meconf, String eaname)
	{
		MqlPatch mfx = new MqlPatch();
		mfx.setFilename(eaname);
		mfx.delFsbPortfolioEa(magic);
		mfx.writeMemFile(eaname);
	}
}
