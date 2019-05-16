package stores;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;

import objects.BadObjectException;
import objects.MidDbObj;
import objects.ThreadDbObj;
import db.DB;

public class MidDB extends DB
{
	// translator mid<->masterstring
	private static HashMap<Integer, String> translateMid = new HashMap<Integer, String>();
	private static HashMap<String, Integer> translateMidstr = new HashMap<String, Integer>();
	private static ThreadsDB tdb_glob = null;//new ThreadsDB();
	
	
	
	public MidDB(ThreadsDB tdb)
	{
		tdb_glob=tdb;
		Tracer.WriteTrace(20, "Info:konstruktor von middb");
		LoadDB("middb", null, 0);
		
		initSuchstrukturen();
	}
	
	
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	private void initSuchstrukturen()
	{
		int tdbsaveflag = 0;
		String mstring = null;

		// schaue ob in der threads.db neue tid´s sind und ergänze die middb um
		// diese
		int anz = tdb_glob.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			if(i%100==0)
				System.out.println("init suchstruktur i<"+i+">");
			ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObjectIDX(i);
			int tid = tdbo.getThreadid();
			MidDbObj mo = this.holeTidObj(tid);

		
			if (mo == null)
			{
				// tid noch nicht gespeichert, dann speichere mo obj
				mo = new MidDbObj();
				mo.setTid(tid);
				mo.setMid(tdbo.getMasterid());
				mo.setAdat(Tools.entferneZeit(Tools.get_aktdatetime_str()));
				this.AddObject(mo);
			}
			
			// plausi
			mstring = mo.getMasterstring();
			if ((mstring!=null)&&(mstring.contains("Ã") || (mstring.contains("¤"))))
			{
				Tracer.WriteTrace(20, "Warning: Mstring<" + mstring
						+ "> hat unerlaubte Sonderzeichen, tid<" + tid
						+ "> -> lösche string (klick)");
				tdbo.setMasterstring(null);
				mo.setMasterstring(null);
				mstring = null;
				tdbsaveflag = 1;
			}
			if ((mstring!=null)&&SG.is_zahl(mstring) == true)
			{
				Tracer.WriteTrace(20, "Warning: Mstring<" + mstring
						+ "> ist fehlerhaft, tid<" + tid
						+ "> -> lösche string (klick)");
				tdbo.setMasterstring(null);
				mo.setMasterstring(null);
				mstring = null;
				tdbsaveflag = 1;
			}
			
		}
		if (tdbsaveflag == 1)
			tdb_glob.WriteDB();
		this.WriteDB();

		// init suchstrukturen
		// in der tdb sind mid<->tid gespeichert, diese werte kann man im
		// initialen ja schon mal in der middb ablegen
		// prüft nach ob alle Aktien-objekte mit tid auch in der middb sind !!!,
		// wenn nicht werden die angelegt
		anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			MidDbObj mi = (MidDbObj) this.GetObjectIDX(i);

			// turbospeicher refreshen
			translateMid.put(mi.getMid(), mi.getMasterstring());
			translateMidstr.put(mi.getMasterstring(), mi.getMid());

		}

	}

	public MidDbObj holeTidObj(int tid)
	{
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			MidDbObj mo = (MidDbObj) this.GetObjectIDX(i);

			if (mo.getTid() == tid)
				return mo;
		}
		return null;
	}

	public String holeMasterstring(int mid)
	{
		// wandelt eine masterid in einen masterstring um
		return (translateMid.get(mid));
	}
	
	
	
	public int holeMasterid(String midstring)
	{
		// wandelt einen masterstring in eine masterid um
		return (translateMidstr.get(midstring));
	}

	public Boolean checkMid(int mid)
	{
		return (translateMid.containsKey(mid));
	}

	public int addMstringGenMid(String masterstring, int tid)
	{
		// hier wird ein neues objekt in der middb aufgenommen
		// bekannt ist ein Masterstring und eine tid
		int mid = 0;

		// sucht in der tdb die mid
		ThreadDbObj tdbo = (ThreadDbObj) tdb_glob.GetObject(tid);

		// die tid, besitzt schon eine eindeutige Masterid
		if (tdbo != null)
			mid = tdbo.getMasterid();
		else
		{
			// die tid ist nicht in der tdb
			// generiere alternativ eine mid
			mid = calcNextMid();
		}

		// hier wird das objekt aufgenommen
		addNewObj(masterstring, mid, tid);
		this.WriteDB();

		return mid;
	}

	private int calcNextMid()
	{
		// fange bei 50 Mio an

		int maxid = 0;
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			MidDbObj mid = (MidDbObj) this.GetObjectIDX(i);
			if (mid.getMid() > maxid)
				maxid = mid.getMid();
		}
		// einen grösser
		maxid = maxid + 1;

		// maxid´s fangen bei 50Mio an
		if (maxid < 50000000)
			maxid = 50000000;
		return maxid;
	}

	private Boolean addNewObj(String midstring, int mid, int tid)
	{
		// erweitert die mid um ein neues objekt

		// prüft midstring
		if (translateMidstr.containsKey(midstring) == true)
		{
			Tracer.WriteTrace(10,
					"Warning: add new midstr translator fails midstr<"
							+ midstring + "> schon vorhanden 'midstring'");
			return false;
		}
		// prüft mid
		if (translateMid.containsKey(mid) == true)
		{
			Tracer.WriteTrace(20, "Warning: add new mid translator fails mid<"
					+ mid + "> schon vorhanden 'mid'");
			return false;
		}

		// das objekt generieren und aufnehmen
		try
		{
			String t1 = new String(mid + "#" + midstring + "#" + tid + "#"
					+ (Tools.entferneZeit(Tools.get_aktdatetime_str())));
			MidDbObj mi;
			mi = new MidDbObj(t1);
			this.AddObject(mi);

			// turbospeicher refreshen
			translateMid.put(mid, midstring);
			translateMidstr.put(midstring, mid);
			return true;
		} catch (BadObjectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void updateMidDbObj(int tid, int mid, String mstring, MidDbObj midobj)
	{
		// ein objekt mit der tid was sich schon in der middb befindet wird
		// upgedatet
		// im Prinzip gibt es nur updates

		// a) tid ist schon in der middb=> dann füge ggf. daten hinzu
		if (midobj != null)
		{
			// falls mid in webseite vorhanden dann versuche die unterzubringen
			if (mid != 0)
			{
				// falls in der datenbasis nix ist, dann setze
				if (midobj.getMid() == 0)
				{
					midobj.setMid(mid);
				} else
				{
					// da ist schon was also mache plausi
					if (midobj.getMid() != mid)
						Tracer.WriteTrace(20,
								"Error: plausi middb inkonsistent midneu<"
										+ mid + ">  midbekannt<"
										+ midobj.getMid() + "> tid<" + tid
										+ ">");
					return;
				}
			}

			// falls mstring in webseite vorhanden dann versuche den
			// unterzubringen
			if ((mstring != null) && (mstring.equals("0") == false))
			{
				// falls in der datenbasis nix ist, dann setze
				if ((midobj.getMasterstring() == null)
						|| (midobj.getMasterstring().equals("0")))
				{
					midobj.setMasterstring(mstring);
				} else
				{
					// da ist schon was also mache plausi
					if (midobj.getMasterstring().equals(mstring) == false)
						Tracer.WriteTrace(10,
								"Error: plausi middb inkonsistent midstringneu<"
										+ mstring + ">  midbekannt<"
										+ midobj.getMasterstring() + "> tid<"
										+ tid + ">");
				}
			}
		}
	}
	public void postprocess()
	{}
}
