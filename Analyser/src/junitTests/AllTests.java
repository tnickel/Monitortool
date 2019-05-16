package junitTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for test");
		// $JUnit-BEGIN$

		// suite.addTestSuite(Htmltest.class);
		suite.addTestSuite(Ziptest.class);
		// $JUnit-END$
		return suite;
	}

}
