package com.darwinsys.sql;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;

import junit.framework.TestCase;

import com.darwinsys.util.Verbosity;

public class ResultsDecoratorTextTest extends TestCase {

	private PrintWriter out;
	private StringWriter sWriter;
	private ResultsDecorator fixture;
	ResultSet rs;

	protected void setUp() throws Exception {
		super.setUp();
		sWriter = new StringWriter();
		out = new PrintWriter(sWriter);
		fixture = new ResultsDecoratorText(out, Verbosity.QUIET);
		// XXX rs = create using jmock...
	}
	
	public void testBogus() {
		System.out.println("Totally bogus test of " + fixture + " passed");
	}

//	public final void testWrite() {
//		fail("Not yet implemented");
//	}
//
//	public final void testPrintRowCount() {
//		fail("Not yet implemented");
//	}

}
