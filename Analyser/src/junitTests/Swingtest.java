package junitTests;

import hilfsklasse.Tracer;
import junit.framework.TestCase;
import mainPackage.GC;
import swingdemo.Swlearning;
import swingtest.UI;

public class Swingtest extends TestCase
{
	public Swingtest()
	{
		Tracer.SetTraceFilePrefix(GC.rootpath + "\\db\\trace.txt");
		Tracer.SetTraceLevel(10);
	}

	public void test1()
	{
		Swlearning sw = new Swlearning();
		sw.showframe();

		UI ui = new UI();
		ui.FehlermeldungStop("Soll ich beenden");

		assertTrue(10 == 10);
	}
}
