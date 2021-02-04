package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Metriklibs.FileAccessDyn;
import StartFrame.Brokerview;
import datefunkt.Mondate;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Tracer;
import modtools.ChrFile;
import mtools.Mlist;

public class Ealiste
{
	private static int isloaded = 0;
	private static String ealistenam = Rootpath.rootpath
			+ "\\data\\ealiste.xml";
	private ArrayList<Ea> ealiste = new ArrayList<Ea>();

	public void add(Ea ea)
	{
		// wenn der ea noch nicht in der liste ist dann nimm auf
		if (getInst(ea.getMagic(), ea.getBroker()) == 0)
			ealiste.add(ea);

	}

	public void expand(Profitliste pl)
	{
		// die ealiste wird aus der Profitliste erweitert
		System.out.println("Ealiste erweitern");
		int anzpl = pl.getsize();

		for (int i = 0; i < anzpl; i++)
		{
			Profit prof = pl.getelem(i);
			// System.out.println("work profit i="+i);
			int magic = prof.getMagic();
			String broker = prof.getBroker();

			Ea ea = this.getEa(magic, broker);
			if (ea != null)
				continue;

			// profit noch nicht als ea in der ealiste
			Ea nea = new Ea();
			nea.setBroker(broker);
			nea.setMagic(magic);
			nea.setInst(0);
			nea.setAuto(0);
			nea.setOn(0);
			ealiste.add(nea);

		}
		// speichern
		this.store(0);

	}

	public void setEafilename(int magic, String broker, String filename)
	{
		Ea ea = this.getEa(magic, broker);

		if (ea == null)
			ea = new Ea();
		ea.setEafilename(filename);
	}

	public String getEafilename(int magic, String broker)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea == null)
			Tracer.WriteTrace(10, "Error: ea magic<" + magic + "> broker<"
					+ broker + "> nicht bekannt");

		return (ea.getEafilename());
	}

	public String getInstFrom(int magic, String broker)
	{

		// holt installiert von
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			return ea.getInstFrom();

		return null;
	}

	public void setInstFrom(int magic, String broker, String instfrombroker)
	{
		// es wurde ein demoea auf einen realbroker installiert
		// a) instfrom setzten
		// b) installiertflag auf realbroker setzen
		// c) installiertflag auf demobroker setzen

		// sucht den EA auf den Realbroker
		Ea eareal = this.getEa(magic, broker);

		if (eareal == null)
		{
			// noch nicht drin dann erzeuge den Realea
			eareal = new Ea();
			eareal.setMagic(magic);
			eareal.setBroker(broker);
			ealiste.add(eareal);
		}

		// setzte den instfrom
		eareal.setInstFrom(instfrombroker);
		eareal.setInst(1);

		// sucht den EA auf dem Demobroker
		Ea eademo = this.getEa(magic, instfrombroker);
		if (eademo == null)
			eademo = new Ea();
		// und setzten
		eademo.setInst(1);

	}

	public int getInst(int magic, String broker)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			return ea.getInst();

		return 0;
	}

	public int setInst(int magic, String broker, int flag)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			ea.setInst(flag);
		else
			Tracer.WriteTrace(10, "Error: magic<" + magic + "> broker<"
					+ broker + "> nicht in der ealiste");
		return 0;
	}

	public int getAuto(int magic, String broker)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			return ea.getAuto();

		return 0;

	}

	public int getGd20Flag(int magic, String broker)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			return (ea.getGd20flag());
		return 0;
	}

	public int setGd20Flag(int magic, String broker)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			ea.setGd20flag(1);
		return 0;
	}

	public int getOn(int magic, String broker)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			return ea.getOn();

		return 0;

	}

	public void setOn(int magic, String broker, int flag)
	{
		Ea ea = this.getEa(magic, broker);
		if (ea != null)
			ea.setOn(flag);
		else
			Tracer.WriteTrace(10, "Error: magic<" + magic + "> broker<"
					+ broker + "> nicht in der ealiste");
		return;

	}

	public void toggleAuto(int magic, String broker, String realbroker)
	{

		int val = getAuto(magic, broker);

		// falls ausgeschaltet ist dann setze 1 oder 2(late)
		if (val == 0)
		{// EA ist ausgeschaltet

			// die ea-konfiguration holen
			Ea ea = this.getEa(magic, broker);
			if (ea == null)
				Tracer.WriteTrace(20, "Error: ea magic<" + magic + "> broker<"
						+ broker + "> nicht bekannt");

			// falls ea nicht bekannt dann schalte auf jeden fall auf 1
			if (ea == null)
				val = 1;
			else
			{
				// konfig bekannt, dann schaue dir genauer die konfig an
				if (ea.getLateswitchonflag() == 1)
					val = 2;
				else
					val = 1;
			}
		} else
			// falls eingeschaltet ist ist das neue val auf jeden fall 0
			val = 0;

		setAuto(magic, broker, realbroker, val);

	}

	public void setAuto(int magic, String broker, String realbroker, int state)
	{

		Ea ea = this.getEa(magic, broker);
		if (ea == null)
		{
			Tracer.WriteTrace(20, "Error: ea magic<" + magic + "> broker<"
					+ broker + "> nicht bekannt");
			return;
		}
		ea.setAuto(state);

		if (realbroker != null)
		{
			ea = this.getEa(magic, realbroker);
			if (ea == null)
			{
				Tracer.WriteTrace(20, "Error: ea magic<" + magic
						+ "> realbroker<" + broker + "> nicht bekannt");
				return;
			}
			ea.setAuto(state);

		}

	}

	public int toggleOn(int magic, String broker)
	{
		int val = getOn(magic, broker);
		if (val == 0)
			val = 1;
		else
			val = 0;
		this.setOn(magic, broker, val);
		return val;

	}

	public Ea getEa(int magic, String broker)
	{
		if (ealiste == null)
			return null;

		int anz = ealiste.size();
		for (int i = 0; i < anz; i++)
		{
			Ea ea = ealiste.get(i);
			if ((ea.getMagic() == magic)
					&& (ea.getBroker().equalsIgnoreCase(broker)))
				return (ea);
		}
		return null;
	}

	public Ea delEa(int magic, String broker)
	{
		if (ealiste == null)
			return null;

		int anz = ealiste.size();
		for (int i = 0; i < anz; i++)
		{
			Ea ea = ealiste.get(i);
			if ((ea.getMagic() == magic)
					&& (ea.getBroker().equalsIgnoreCase(broker)))
			{
				ealiste.remove(i);
				anz--;
				Mlist.add("deleted ea magic<" + magic + "> broker<" + broker
						+ ">");
			}
			;
		}
		return null;
	}

	public void deleteEaFilesystem(Brokerview brokerview, int magic,
			String broker)
	{
		//hier wird der ea mit der magic gelöscht
		Metaconfig meconf = brokerview.getMetaconfigByBrokername(broker);

		// 1) installations verzeichnisse holen
		String quellverz = meconf.getMqlquellverz();
		String mqldata = meconf.getMqldata();
		String appdata = meconf.getAppdata();
		String expertdata=meconf.getExpertdata();

		//1.5) check
		if(quellverz==null)
			Tracer.WriteTrace(10, "E:1215 quellverz=null");
		if(mqldata==null)
			Tracer.WriteTrace(10, "E:1215 mqldata=null");
		if(appdata==null)
			Tracer.WriteTrace(10, "E:1215 appdata=null");
		if(expertdata==null)
			Tracer.WriteTrace(10, "E:1215 expertdata=null");
		
		// 2) Ea auf zielsystem löschen
		ArrayList<String> alist = genFileliste(expertdata, magic);
		FilesLoeschen(expertdata, expertdata, alist, magic);

		// 3) Ea auf quellverz löschen
		ArrayList<String> qlist = genFileliste(quellverz, magic);
		FilesLoeschen(quellverz, quellverz, qlist, magic);

		// 3) Ea aus der EAliste löschen
		delEa(magic, broker);
		this.store(1);

		// 4) profiles mit einer bestimmten magic löschen
		if (meconf.getMetaversion().contains("509"))
		{
			ArrayList<String> plist = genFileliste(appdata
					+ "\\profiles\\default", magic);
			FilesLoeschen(appdata + "\\profiles\\default", appdata, plist,
					magic);
		}
		else //metaversion ist 600+
		{
			//hier wird anders vorgegangen.
			//hier wird geschaut in welchen char-files die magic vorkommt und die files wo die magic vorkommt
			//werden gelöscht
			
			ArrayList<String> plist = genFileliste600plus(appdata
					+ "\\profiles\\default", magic);
			FilesLoeschen(appdata + "\\profiles\\default", appdata, plist,
					magic);
		}
		// 5) den <magic>.del eintrag im files verzeichniss machen
		String fnam=appdata+"\\mql4\\files\\"+magic+".del";
		Inf inf= new Inf();
		inf.setFilename(fnam);
		inf.writezeile("deleted on"+Mondate.getAktDate());
		inf.close();
	
	}

	private void FilesLoeschen(String workverz, String zielverz,
			ArrayList<String> alist, int magic)
	{
		// files löschen, und bei dropbox noch was für die dropbox anlegen
		// die files aber zuvor in deleted-dir ablegen
		FileAccessDyn fd = new FileAccessDyn();
		String deleteddir = zielverz + "\\deleted";

		// das deleted verzeichniss anlegen falls nicht da
		File deldirfile = new File(deleteddir);
		if (deldirfile.exists() == false)
			deldirfile.mkdirs();

		int anz = alist.size();
		for (int i = 0; i < anz; i++)
		{
			String fnam = alist.get(i);
			File fnamf = new File(fnam);
			if (fnamf.exists())
			{
				// den backup in deleted anlegen
				String deletedirfnam = deleteddir + "\\" + fnamf.getName();
				fd.copyFile2(fnam, deletedirfnam);

				// das file löschen
				if(fnamf.delete()==false)
					Tracer.WriteTrace(10, "E:can´t delete file <"+fnamf+">");

				Mlist.add("deleted <" + fnam + "> ");
			}
		}

		// 3) bei dropbox noch .del file anlegen
		if (zielverz.toLowerCase().contains("dropbox"))
		{
			File fnamfdel = new File(workverz + "\\" + magic + ".del");
			try
			{
				fnamfdel.createNewFile();
				Mlist.add("deleted <" + magic + ">  in dropbbox");
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private ArrayList<String> genFileliste(String path, int magic)
	{
		// holt die filenamen mit einer bestimmten magic

		ArrayList<String> flist = new ArrayList<String>();
		FileAccess.initFileSystemList(path, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			int fmagic = calcMagic(fnam);
			if (fmagic == magic)
			{
				flist.add(path + "\\" + fnam);
			}

		}
		return flist;
	}

	private ArrayList<String> genFileliste600plus(String path, int magic)
	{
		// holt die filenamen mit einer bestimmten magic und speichert die in der fileliste

		ArrayList<String> flist = new ArrayList<String>();
		FileAccess.initFileSystemList(path, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			
			if(fnam==null)
				continue;
			
			if((fnam.endsWith(".chr"))==false)
				continue;
			
			//jetzt wird geprüft ob die magic in dem chr file ist
			//dies wird mit einer installerklasse gemcht
			ChrFile chrfile = new ChrFile();
			chrfile.setFilename(path+"\\"+fnam);
			
			if(chrfile.hatMagic(magic)==true)
			{
				Tracer.WriteTrace(20, "I:found magicnumber <"+magic+"> in file <"+path+"\\"+fnam+">");
				flist.add(path + "\\" + fnam);
			}

		}
		return flist;
	}
	
	private int calcMagic(String quellnam)
	{
		// B23 EURUSD M5_Strategy 454.6035.mq4
		// die Magic muss am ende stehen

		if (quellnam.contains(" ") == false)
			return -99;

		String keyword = quellnam.substring(quellnam.lastIndexOf(" "),
				quellnam.lastIndexOf("."));

		keyword = keyword.replace(".", "");

		// die führenden nullen entfernen
		while ((keyword.startsWith("0")) || (keyword.startsWith(" ")))
			keyword = keyword.substring(1, keyword.length());

		if (keyword.length() > 9)
			Mbox.Infobox("Magic<" + keyword + "> too long max 9 digits");
		return SG.get_zahl(keyword);
	}

	public void init()
	{
		// semaphore on
		if (isloaded == 0)
		{
			ealiste.clear();

			if (FileAccess.FileAvailable0(ealistenam) == false)
				return;

			// tradeliste laden
			Inf inf = new Inf();
			inf.setFilename(ealistenam);

			// tradeliste einlesen
			ealiste = (ArrayList<Ea>) inf.loadXST();
			// Tracer.WriteTrace(10, "anz trades geladen="+tradeliste.size());
			inf.close();
			isloaded = 1;
		}

	}

	public void store(int nolengthprotectionflag)
	{
		if (ealiste.size() == 0)
			return;

		String tradeliste_tmp = ealistenam + ".tmp";
		String tradeliste_old = ealistenam + ".old";

		// das tmp löschen
		if (FileAccess.FileAvailable(tradeliste_tmp))
			FileAccess.FileDelete(tradeliste_tmp, 0);

		// speichern
		Inf inf = new Inf();
		inf.setFilename(tradeliste_tmp);
		inf.saveXST(ealiste);
		inf.close();

		// längenvergleich, es darf nicht kürzer werden
		File f1 = new File(ealistenam);
		File f2 = new File(tradeliste_tmp);
		if (nolengthprotectionflag == 0)
			if (f2.length() < f1.length() - 10)
				Tracer.WriteTrace(20, "ERROR: Length protection error ealiste<"
						+ f1.length() + ">ealiste_new<" + f2.length() + ">");

		// wenn alles ok dann nach old speichern
		if (FileAccess.FileAvailable(tradeliste_old))
			FileAccess.FileDelete(tradeliste_old, 0);

		if (FileAccess.FileAvailable(ealistenam) == true)
			FileAccess.FileMove(ealistenam, tradeliste_old);
		// und tmp umbenennen
		FileAccess.FileMove(tradeliste_tmp, ealistenam);

	}

}
