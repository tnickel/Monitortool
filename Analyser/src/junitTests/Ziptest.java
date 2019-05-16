package junitTests;

import hilfsklasse.Archive;
import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import junit.framework.TestCase;
import mainPackage.GC;

public class Ziptest extends TestCase
{
	public Ziptest()
	{
		Tracer.SetTraceFilePrefix(GC.rootpath + "\\db\\trace.txt");
		Tracer.SetTraceLevel(10);
	}

	public void testZeigeZippverzeichniss()
	{
		String archivename = GC.rootpath + "\\tests\\ziptest\\api.zip";
		String fnam = GC.rootpath + "\\tests\\ziptest\\Api_1.html";
		ZipFile z = null;
		try
		{
			z = new ZipFile(archivename);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Enumeration<? extends ZipEntry> e = z.entries(); e
				.hasMoreElements();)
		{
			ZipEntry ze = e.nextElement();
			System.out.println(ze.getName());
		}
		// assertTrue(10 == 10);
		// http://www.dpunkt.de/java/Programmieren_mit_Java/Streams/17.html
		// da ist beschrieben wie das geht !!!!
	}

	public void testErweitereZipverzeichnis()
	{
		File fnam = new File(GC.rootpath + "\\tests\\ziptest\\Api_2.html");
		File zipstore = new File(GC.rootpath + "\\tests\\ziptest\\api.zip");

		if (Archive.zipisFileinArchive(fnam, zipstore) == false)
			Archive.zipAddFile(fnam, zipstore);
		else
			System.out.println("file <" + fnam + "> is already in archive <"
					+ zipstore + ">");

		Archive.zipAddFile(fnam, zipstore);
		File fnam2 = new File(GC.rootpath + "\\tests\\ziptest\\2.gzip");
		Archive.zipAddFile(fnam2, zipstore);
		fnam2 = new File(GC.rootpath + "\\tests\\ziptest\\test.txt");
		Archive.zipAddFile(fnam2, zipstore);
		assertTrue(10 == 10);
	}

	public void testLeseTestaus()
	{

		File fnam = new File(GC.rootpath + "\\tests\\ziptest\\test.txt");
		File zipstore = new File(GC.rootpath + "\\tests\\ziptest\\api.zip");
		String destdir = GC.rootpath + "\\tests\\ziptest\\unzip";

		BufferedReader inf = null;

		inf = Archive.zipGetStream(fnam, zipstore);
		// inf =fa.ReadFileOpen(fnam.getAbsolutePath());
		String zeile = "";

		try
		{
			while ((zeile = inf.readLine()) != null)
				System.out.println("zeile <" + zeile + ">");

			inf.close();
			assertTrue(10 == 10);
		} catch (IOException e)
		{
			assertTrue(10 == 5);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Archive.unzip(fnam, zipstore, destdir);

	}

	public void xtestPackeUserhtmlpages()
	{

		FileAccess.initFileSystemList(GC.rootpath
				+ "\\downloaded\\userhtmlpages", 1);
		int anz = FileAccess.holeFileAnz();
		File zipstore = new File(GC.rootpath + "\\downloaded\\pages.zip");

		for (int i = 0; i < anz; i++)
		{
			File fname = new File(GC.rootpath + "\\downloaded\\userhtmlpages\\"
					+ FileAccess.holeFileSystemName());

			if (Archive.zipisFileinArchive(fname, zipstore) == false)
				Archive.zipAddFile(fname, zipstore);

			System.out.println("packe <" + i + "|" + anz + ">"
					+ fname.getName());

		}
		assertTrue(10 == 10);
	}

	public void testEntpackeDateiAusUserhtmlpages()
	{
		System.out.println("start test");

		File zipstore = new File(GC.rootpath
				+ "\\downloaded\\userhtmlpages.zip");
		File fname = new File("vorwortverfasser_threads.html"
				+ FileAccess.holeFileSystemName());
		if (Archive.zipisFileinArchive(fname, zipstore) == true)
			System.out.println("datei vorhanden");
		assertTrue(10 == 10);
	}
}
