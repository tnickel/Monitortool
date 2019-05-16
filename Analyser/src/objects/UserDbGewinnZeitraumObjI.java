package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

public class UserDbGewinnZeitraumObjI extends Obj implements DBObject
{
	// Dieses objekt verwaltet die Gewinne für einen USER
	// 
	// Einzelgewinne
	// Ebenfalls werden hier auch die Einzelgewinne gesammelt, diese werden
	// allerdings nur als infofile unter
	// UserThreadVirtualKonto/Einzelgewinne abgespeichert
	// Summnengewinne
	// Die Summengewinne werden in UserThreadVirutalKonto/Summengewinnedb unter
	// der *.db struktur abgespeichert
	//
	// "seqnr#aktname#tid#startdatum#enddatum#gew0#gew1#gew2#gew3#gew4#gew5#gew6#gew7#gew8#gew9#gew10#gew11#gew12#gew13#gew14#gew15#dummy1#dummy2";

	private int seqnr = 0; // deqnr dient zur vereinfachung für die auswertung
	private int tid = 0;
	private String aktname = null;
	private String startdatum = null;
	private String enddatum = null;
	private float[] gewinn = new float[1];
	private int dummy1 = 0;
	private int dummy2 = 0;

	private String username = null;

	// Hier werden die 5Tage,10Tage... 720Tage gewinne gespeichert

	private int[] tage = new int[]
	{ 5, 10, 20, 60, 90, 160, 360, 720 };
	// gewinn der gewinnstrategie i...3

	protected float usergewinnsortierindex = 0;

	// diese Liste sammelt die gesammten Einzelgewinne, dies ist nur eine
	// temporäre Liste, die liste
	// wird öffter gelöscht
	public int compareTo(Obj argument)
	{
		return 0;
	}
	public UserDbGewinnZeitraumObjI(Datumsintervall inter, int ti)
	{
		startdatum = inter.getStartdatum();
		enddatum = inter.getEnddatum();
		tid = ti;
	}

	public UserDbGewinnZeitraumObjI(String zeile)
	{
		int i = 0;
		int anz = SG.countZeichen(zeile, "#");
		if (anz != 6)
		{
			System.out.println(UserDbGewinnZeitraumObjI.class.getName()
					+ ":ERROR:zeile fehlerhaft in usergewinne.db zeile=<"
					+ zeile + "> anz<" + anz + ">");
			Tracer.WriteTrace(10, UserDbGewinnZeitraumObjI.class.getName()
					+ ":ERROR:zeile fehlerhaft in usergewinne.db zeile=<"
					+ zeile + ">anz<" + anz + ">");
			return;
		}
		int pos = 1;
		try
		{

			seqnr = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			tid = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			startdatum = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			enddatum = new String(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			
				gewinn[0] = Float.valueOf(SG.nteilstring(zeile, "#", pos));
				pos = pos + 1;
			
			dummy1 = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			dummy2 = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;

		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, UserDbGewinnZeitraumObjI.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		String infostring = null;
		infostring = seqnr + "#" + tid + "#" + startdatum + "#" + enddatum;

		
		infostring = infostring.concat("#" + gewinn[0]);

		infostring = infostring.concat("#" + dummy1);
		infostring = infostring.concat("#" + dummy2);

		return (infostring);
	}

	@Override
	public String GetSaveInfostring()
	{
		return "seqnr#tid#startdatum#enddatum#gew0#dummy1#dummy2";
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public int getSeqnr()
	{
		return seqnr;
	}

	public void setSeqnr(int seqnr)
	{
		this.seqnr = seqnr;
	}

	public int getThreadid()
	{
		return tid;
	}

	public void setThreadid(int tid)
	{
		this.tid = tid;
	}

	public String getStartdatum()
	{
		return startdatum;
	}

	public void setStartdatum(String startdatum)
	{
		this.startdatum = startdatum;
	}

	public String getEnddatum()
	{
		return enddatum;
	}

	public void setEnddatum(String enddatum)
	{
		this.enddatum = enddatum;
	}

	public void setGewinn( float value)
	{
		gewinn[0] = value;
	}

	public float getGewinn()
	{
		return (gewinn[0]);
	}

	public int compareTo(UserDbGewinnZeitraumObjI argument)
	{
		if (usergewinnsortierindex > argument.usergewinnsortierindex)
			return -1;
		if (usergewinnsortierindex < argument.usergewinnsortierindex)
			return 1;
		return 0;
	}
	public void postprocess()
	{}
}
