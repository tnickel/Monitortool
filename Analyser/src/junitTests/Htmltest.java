package junitTests;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;
import junit.framework.TestCase;
import mainPackage.GC;

/**
 * @author tnickel
 * 
 */
public class Htmltest extends TestCase
{

	public Htmltest()
	{
		Tracer.SetTraceFilePrefix(GC.rootpath + "\\db\\trace.txt");
		Tracer.SetTraceLevel(10);
	}

	public void testHtmlThreadComprTest1()
	{
		// Daten vorbereiten
		String fnam0 = GC.rootpath + "\\tests\\htmltest\\API_1_orginal.html";
		String fnam1 = GC.rootpath + "\\tests\\htmltest\\API_1.html";
		String fnam2 = GC.rootpath + "\\tests\\htmltest\\API_1short.html";
		FileAccess.copyFile(fnam0, fnam1);

		// testet ob die headerelemente noch passen
		//Htmlcompress htmlw = new Htmlcompress();
		/*htmlw.cleanCompressHtmlFile(fnam1, fnam2);
		Threads html = new Threads(fnam1); // selektiert dieses File
		Threads html2 = new Threads(fnam2); // selektiert dieses File
		System.out.println("obj1orginal   =<" + html.GetHtmlElements() + ">");
		System.out.println("obj2compressed=<" + html2.GetHtmlElements() + ">");
		assertEquals(html.GetHtmlElements(), html2.GetHtmlElements());*/
	}

	public void testHtmlThreadComprTest2()
	{
		int i;
		String str1 = null, str2 = null;
		// Daten vorbereiten
		String fnam0 = GC.rootpath + "\\tests\\htmltest\\API_1_orginal.html";
		String fnam1 = GC.rootpath + "\\tests\\htmltest\\API_1.html";
		String fnam2 = GC.rootpath + "\\tests\\htmltest\\API_1short.html";
		FileAccess.copyFile(fnam0, fnam1);
		// testet ob die postings noch passen
		//Htmlcompress htmlw = new Htmlcompress();
	/*	htmlw.cleanCompressHtmlFile(fnam1, fnam2);
		Threads html = new Threads(fnam1); // selektiert dieses File
		Threads html2 = new Threads(fnam2); // selektiert dieses File
*/		for (i = 0; i < 10; i++)
		{
			/*str1 = html.GetNextPostingString();
			str2 = html2.GetNextPostingString();*/

			if (str1.equals(str2) == false)
				break;

		}
		assertTrue(i == 10);
	}
}
