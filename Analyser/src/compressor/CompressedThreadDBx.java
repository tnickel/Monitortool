package compressor;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.BadObjectException;
import db.CompressedPostingObj;
import db.DBlauf;
import except.CompressedThreadException;

public class CompressedThreadDBx extends DBlauf
{
	static private int dbopenflag = 0;
	static private String kennung_threadid_g = null;
	private int lastthreadid_g = 0;

	public CompressedThreadDBx(String threadid) throws CompressedThreadException 
	{
		
		kennung_threadid_g = threadid;
		LoadDB("compressedthreads", threadid);

		if(this.getanz()==0)
			Tracer.WriteTrace(20,"W:Null elemente in compressed thread kennung<"+threadid+">");
		
		if (this.getanz() > 0)
			plausicheck_i();
		
		
	}

	private void plausicheck_i() throws CompressedThreadException
	{
		int anz = this.getanz();
		int tid = 0, postid = 0;

		CompressedPostingObj co = this.getObjectIDX(0);
		
		if (co == null)
		{
			String msg = "Error: internal compressed thread corruped kennung<"
					+ kennung_threadid_g + ">";
			Tracer.WriteTrace(20, msg);
			throw new CompressedThreadException(msg);
		}

		tid = co.getThreadid();
		postid = co.getPostid();
		for (int i = 1; i < anz; i++)
		{
			co = this.getObjectIDX(i);
			if (co == null)
			{
				String msg = "Error: internal compressed thread corruped kennung<"
						+ kennung_threadid_g + ">";
				Tracer.WriteTrace(20, msg);
				throw new CompressedThreadException(msg);
			}
			
			if (tid != co.getThreadid())
			{
				String msg = "Error: internal compressed thread corruped kennung<"
						+ kennung_threadid_g
						+ "> erwtid<"
						+ tid
						+ "> tidid<"
						+ co.getThreadid() + ">";
				Tracer.WriteTrace(20, msg);
				throw new CompressedThreadException(msg);
			}

			if (postid + 1 != co.getPostid())
			{
				Tracer.WriteTrace(20,
						"Warning: ein Posting fehlt<"
								+ kennung_threadid_g + "> Postidsequence<" + postid
								+ "> co.getpostid<" + co.getPostid() + ">");
			}
			postid = co.getPostid();
		}
		return;
	}

	@Override
	public void cleanMemDB()
	{
		super.cleanMemDB();
	}

	public void cleanfile()
	{
		char first=kennung_threadid_g.charAt(0);
		String speicherfile = GC.rootpath + "\\db\\compressedthreads\\@"+first+"\\"
				+ kennung_threadid_g + ".db";
		if (FileAccess.FileAvailable(speicherfile))
			FileAccess.FileDelete(speicherfile,0);
	}

	public void addElem(CompressedPostingObj co)
	{
		String zeile = new String(co.getDatetime() + "#" + co.getUsername()
				+ "#" + co.getThreadid() + "#" + co.getPostid() + "#"
				+ co.getKursvalue());
		
		UpdateZeile(zeile, 20);

		// plausicheck der threadid beim compressen
		if (lastthreadid_g != 0)
			if (lastthreadid_g != co.getThreadid())
				Tracer.WriteTrace(10,
						"Error: DB error, fehler beim Compressen des thread kennung<"
								+ kennung_threadid_g + "> tid<" + lastthreadid_g
								+ "> falsche tid<" + co.getThreadid() + ">");

		// die Threadid das erst mal setzen
		if (lastthreadid_g == 0)
			lastthreadid_g = co.getThreadid();
	}

	public int getanz()
	{
		//liefert die anzahl der postings
		return (super.getanzObj());
	}

	public CompressedPostingObj getObjectIDX(int index)
	{
			String zeile = super.getZeileIDX(index);
			try
			{
				CompressedPostingObj co = new CompressedPostingObj(zeile,
						kennung_threadid_g);
				return co;
			} catch (BadObjectException e)
			{
				// bei einer fehlerhaften Zeile versuche die nächste zu lesen
				e.printStackTrace();

				return null;
			}
		
	}
}
