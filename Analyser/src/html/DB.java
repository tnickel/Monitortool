package html;

import hilfsklasse.Tracer;

import java.util.ArrayList;

import mainPackage.GC;

public class DB<T> extends HtmlCore
{
	// wollsauklasse von listenelementen
	// diese Seite dient zur Auswertung von HTLM-Listenelementen
	// dies sind z.b. die einzelnen Postings eines Threads(HTMLPostElem) oder
	// die last userpostings(HTML-PostElem) den Userprofilen
	// Welche Listenelemente diese Klasse beinhaltet das wird aus den
	// Filenamen beim Öffnen der Klasse festgelegt
	// Dies ist eine DB-Klasse, deswegen auch Reset und Next-Element

	private ArrayList<T> dbliste = new ArrayList<T>();
	private int counter = -1;
	private int Klasse = 0;
	private boolean add;
	private String fnamk = null;

	public DB(String filename)
	{
		// nur wirkliche postings werden in der dbliste aufgenommen
		if (filename.contains("downloaded\\threads") == true)
			Klasse = GC.HtmlPostElem;
		if (filename.contains("_postings") == true)
			Klasse = GC.HtmlUserElem;

		// läd die postings einer html-seite
		ReadHtmlPage(filename);
		fnamk = filename;

		if(filename.contains("INTERNET INIT.JAP_20.")==true)
			Tracer.WriteTrace(20, "found INTERNET INIT.JAP_20.");
		
		
		//System.out.println("betrachte file x123<"+filename+">");
		
		Builddbliste();
	}

	@SuppressWarnings("unchecked")
	private void Builddbliste()
	{
		String postline = null;

		if (Klasse == GC.HtmlPostElem)
		{
			while ((postline = GetFullHtmlPosting()).length() > 200)
			{
				ThreadsPostElem opo = new ThreadsPostElem(htmlseite_str,
						postline);
				if (opo == null)
					Tracer
							.WriteTrace(10,
									"Error: fehler beim generieren eines postingobjektes ");

				add = dbliste.add((T) opo);
			}
		}
	}

	public void ResetDB(int n)
	{
		counter = n;
	}

	public T GetNextPosting()
	{
		counter++;
		if (counter >= dbliste.size())
			return null;
		T opo = dbliste.get(counter);
		return opo;
	}
}
