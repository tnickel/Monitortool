package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Swttool;
import hilfsklasse.Tracer;
import html.DB;
import html.Htmlcompress;
import html.Threads;
import html.ThreadsPostElem;
import internetPackage.DownloadManager;
import mainPackage.GC;
import objects.BadObjectException;
import objects.Htmlpage;
import objects.ThreadDbObj;
import objects.UserDbObj;
import objects.UserPostingverhaltenDbObj;

import org.eclipse.swt.widgets.Display;

import stores.MidDB;
import stores.PostDBx;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserPostingverhaltenDB;
import swtOberfläche.Rangstat;

public class AnalyseUserpostings
{
	static PostDBx pdb = new PostDBx();
	static UserDB udb_glob = null;
	static UserPostingverhaltenDB upostverh_glob = new UserPostingverhaltenDB();
	static Inf infMaster2 = new Inf();
	String postinfname = null;
	int lastpostid_glob = 0, breakindex = 0, aktpostanz = 0;
	String lastdate_g = null, lastdate2_g = null;
	String froot = GC.rootpath;
	String lastcreatedatetime = null, aktdate = null;
	String startzeit = null;
	String lasterrorsymbol_glob = null, lastthreadname_glob = null;
	UserDbObj userdbobj = null;
	Htmlpage htmlZahlen = null, htmlTexte = null, htmlUserinfo = null;
	static DownloadManager Wpage = new DownloadManager(GC.MAXLOW);

	private void BewertePosting(Rangstat rs,ThreadsPostElem einposting)
	{
		//return: anz neulinks
		//das posting wird bzgl. externer links bewertet und das Ergebniss wird in der db festgehalten
		String zeile = einposting.get_postingzeile();
		String unam=einposting.get_username();
		int lanz = SG.countExternalWeblinks(zeile);
		int anzicons= SG.countIcons(zeile);
		
		rs.setGesSeenPostings(rs.getGesSeenPostings()+1);
		
		//sucht den passenen usernamen in der bewertungs.db
		UserPostingverhaltenDbObj o=upostverh_glob.sucheUser(unam);

		if(unam.contains("pinguin"))
			return;
		
		if(o==null)
		{
			//generiere das objekt und speichere es ab
			try
			{
				UserPostingverhaltenDbObj ox = new UserPostingverhaltenDbObj(unam+"#1#"+lanz+"#0#0#0#0#0#0#0#0#0#0#");
				upostverh_glob.AddObject(ox);
			} catch (BadObjectException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "BadObj Username <"+unam+"> invalid, user wird nicht gespeichert -> go on");
				return;
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "NumberFormat Username <"+unam+"> invalid, user wird nicht gespeichert -> go on");
				return;
			}
			rs.setGesSeenLinks(rs.getGesSeenLinks()+lanz);
			rs.setGesSeenUser(rs.getGesSeenUser()+1);
			return ;
		}
		//statistik erhöhen
		rs.setGesSeenLinks(rs.getGesSeenLinks()+lanz);
		rs.setGesSeenIcons(rs.getGesSeenIcons()+anzicons);
		
		int oanz=o.getAnzausgewpostings();
		int olinkanz=o.getPostanzExtLink();
		int ianz=o.getPostanzIcons();
		
		//zaehle die neuen postings zu den alten hinzu
		oanz=oanz+1;
		o.setAnzausgewpostings(oanz);
		
		olinkanz=olinkanz+lanz;
		o.setPostanzExtLink(olinkanz);
		//iconzahl für den user erhöhen
		o.setPostanzIcons(ianz+anzicons);
	
		return ;
	}

	private void korrektFilename(String fnamz)
	{
		System.out.println("found ._ <" + fnamz + ">");
		String fnamez2 = fnamz.replaceAll("._", "_");
		if (FileAccess.FileAvailable(fnamz))
		{
			System.out.println("rename <" + fnamz + "> to <" + fnamez2 + ">");
			FileAccess.Rename(fnamz, fnamez2);
		}
	}

	private void DurchlaufeTdbo(MidDB middb,ThreadsDB tdb,Rangstat rs,ThreadDbObj tdbo)
	{
		
		Htmlcompress hw = new Htmlcompress(tdb);
		// stelle fest welches die letzte Htmlseite in der compressed db ist
		int lastpagetdbo = tdbo.getPageanz();

		// gehe durch die Htmlseiten und baue die compressed.db weiter aus
		for (int j = 1; j <= lastpagetdbo; j++)
		{
			String urlnam = tdbo.GetUrlName(j);
			String fname = tdbo.GetSavePageName_genDir(j);
			// falls was ungezipptes da ist lösche es
			if ((FileAccess.FileAvailable(fname)) == true)
				FileAccess.FileDelete(fname, 0);

			String fnamz = fname + ".gzip";

			if (FileAccess.FileAvailable(fnamz) == false)
			{
				Tracer.WriteTrace(20, "Warning: Htmlseite verschwunden <"
						+ fnamz + ">??, lade neu");
				Wpage.DownloadHtmlPage(urlnam, fnamz, 0, 50000, 1, 1, 0);
				hw.cleanCompressHtmlFile(middb,fnamz, fnamz, tdbo.getThreadid(),urlnam);

				String fnamez2 = fnamz.replaceAll("_", "._");
				if (FileAccess.FileAvailable(fnamez2) == true)
				{
					System.out.println("fnamez2 <" + fnamez2
							+ ">ist aber vorhanden");
					System.out.println("möchte umbenennen von <" + fnamez2
							+ "> nach <" + fnamz + ">");
					FileAccess.Rename(fnamez2, fnamz);
				}
			}

			if (fnamz.contains("._"))
				korrektFilename(fnamz);

			
			System.out.print(".");
			hw.cleanCompressHtmlFile(middb,fnamz, fnamz, tdbo.getThreadid(),urlnam);
			DB<ThreadsPostElem> posting = new DB<ThreadsPostElem>(fnamz);

			// zaehlt die Postings
			Threads html = new Threads(middb,tdb,fnamz, tdbo.getThreadid());
			int anz = html.GetBeitragsanzahl();
			for (int i = 0; i < anz; i++)
			{
				ThreadsPostElem einposting = new ThreadsPostElem();
				while ((einposting = posting.GetNextPosting()) != null)
				{
					BewertePosting(rs,einposting);
					
				}
			}
			
		}
		System.out.println(".");
		return;
	}

	private void DurchlaufeAlleThreads(MidDB middb,Rangstat rs,ThreadsDB tdb)
	{
		int anz = tdb.GetanzObj();
		
		
		if(rs.getEndpos()>0)
			anz=rs.getEndpos();
		
		for (int i = rs.getStartpos(); i < anz; i=i+rs.getStepweite())
		{
			
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			DurchlaufeTdbo(middb,tdb,rs,tdbo);
			
			if(tdbo.getThreadname().equals("0279"))
				continue;
			
			System.out.println("Analyse User Thread <"+i+" von "+anz+"> seitenanz<"+tdbo.getPageanz()+"> seenpostings<"+rs.getGesSeenPostings()+"> seenuser<"+rs.getGesSeenUser()+"> seenllinks<"+rs.getGesSeenLinks()+"> seensmileys<"+rs.getGesSeenIcons()+">");
			if(i%10==0)
				upostverh_glob.WriteDB();
		}
		
	}

	public void StartAnalyseSt1(MidDB middb,Rangstat rs,Display dis, UserDB udb,
			UserPostingverhaltenDB upostverh, ThreadsDB tdb)
	{
		//return: linksumm, die summe aller links für alle user
		udb_glob = udb;
		upostverh_glob=upostverh;
	
		Swttool.wupdate(dis);
		DurchlaufeAlleThreads(middb,rs,tdb);
		return;
	}
}
