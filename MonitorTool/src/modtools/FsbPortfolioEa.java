package modtools;

import java.io.File;

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
		
		String magicpref = Eaclass.calcMagicFsbPortBaseMagic(magic);

		//magic 8 is a magic from a realbroker channel
		if(magicpref.length()<3)
		  return false;
		
		//sucht den filenamen der den magicprefix beinhaltet, also es wird ein portfolio ea gesucht, da
		//dies nur beim portfolio ea zutrifft
		if (searchEaFilenameBase(Integer.valueOf(magicpref), meconf) != null)
			return true;
		
		Tracer.WriteTrace(20, "I: Ea with magic<" + magic + "> not on harddisk path<" + meconf.getExpertdata() + ">");
		return false;
		
	}
	static public Boolean isStandartEa(int magic, Metaconfig meconf)
	{
		if (searchEaFilename(magic, meconf) != null)
			return true;
		return false;
	}
		
	static public String searchEaFilenameBase(int basemagic, Metaconfig meconf)
	{
		
		
		// sucht den portfolio ea auf platte und liefert den namen zurück
		String verz = meconf.getExpertdata();
		FileAccess.initFileSystemList(verz, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			//holt den filenamen um hierraus die magic zu extrahieren
			String fnam = FileAccess.holeFileSystemName();
			//extrahiert aus dem filnamen die magic ohne die zusatzzeichen wie . etc.
			int fnam_magic=Eaclass.calcMagicSimple(fnam);
			
			if (fnam_magic==basemagic)
				return fnam;
		}
		return null;
		
	}
	static public String searchEaFilename(int magic, Metaconfig meconf)
	{
	
		
		// sucht den portfolio ea auf platte und liefert den namen zurück
		String verz = meconf.getExpertdata();
		FileAccess.initFileSystemList(verz, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			//holt den filenamen um hierraus die magic zu extrahieren
			String fnam = FileAccess.holeFileSystemName();
			//extrahiert aus dem filnamen die magic ohne die zusatzzeichen wie . etc.
			int fnam_magic=Eaclass.calcMagicSimple(fnam);
			
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
	
	static public void modifyPortfolioEa(String postmagicstr, Metaconfig meconf, String eaname)
	{
		String fnam=meconf.getExpertdata()+"\\"+eaname;
		MqlPatch mfx = new MqlPatch();
		mfx.setFilename(fnam);
		mfx.delFsbPortfolioEa(postmagicstr);
		mfx.writeMemFile(fnam);
		
		//das *.ex4 auf platte löschen
		String fnam_ex4=fnam.replace(".mq4", ".ex4");
		File fnam_ex4_f=new File(fnam_ex4);
		if(fnam_ex4_f.exists())
			if(fnam_ex4_f.delete()==false)
				Tracer.WriteTrace(10, "E:delete PortfolioEA cant delete file<"+fnam_ex4+">");
		
	}
	static public String PortfolioEaGetTpSl(int magic, Metaconfig meconf)
	{
		//magic=
		//hier wird er TP/Sl aus dem portfolio ea rausgelesen
		//0) hole die basemagic raus, die basemagic sind die ersten 6 Stellen
		String basemagic=Eaclass.calcMagicFsbPortBaseMagic(magic);
		
		
		//1) sucht den portfolio EA der für diese Magic verantwortlich ist
		//sucht den filenamen auf platte, (der ea beinhaltet die basemagic im Namen), Es wird also der Ea
		//mit diesem Namen auf Platte gesucht
		String eaname=FsbPortfolioEa.searchEaFilenameBase(Integer.valueOf(basemagic), meconf);
		
		//2) es wird der poststring dieser magic extrahiert, der postmagicteil sind die letzten Stellen
		String postmagicstr=Eaclass.calcMagicFsbPortPostMagic(Integer.valueOf(magic));
		
		//3) dann wird aus dem gefundenen EA sl und tp extrahiert, hierzu wird der eaname und der endteil der magic benötigt
		String fnam=meconf.getExpertdata()+"\\"+eaname;
		MqlPatch mfx = new MqlPatch();
		mfx.setFilename(fnam);
		String tpsl=mfx.getFsbPortfolioEaTpSl(postmagicstr);
		mfx.close();
		return tpsl;
	}
}
