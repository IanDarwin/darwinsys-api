package com.darwinsys.sql;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;

import com.darwinsys.util.Verbosity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultsDecoratorTextTest {

	private PrintWriter out;
	private StringWriter sWriter;
	private ResultsDecorator fixture;
	ResultSet rs;

	@BeforeEach
	void setUp() throws Exception {
		sWriter = new StringWriter();
		out = new PrintWriter(sWriter);
		fixture = new ResultsDecoratorText(out, Verbosity.QUIET);
		// XXX rs = create using jmock...
	}

	@Test
	void bogus() {
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
