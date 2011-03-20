package com.darwinsys.diff;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiffTest {

	@Test
	public void allDifferent() {
		String a = "a,b,c,d,e,f,g,h,i,j,k,l".replace(',', '\n');
		String b = "0,1,2,3,4,5,6,7,8,9".replace(',', '\n');
		assertEquals("all-changes test", 
				diffsToString(Diff.diffText(a, b, false, false, false)), 
				"12.10.0.0*");
	}

	@Test
	public void allSame() {
		String a = "a,b,c,d,e,f,g,h,i,j,k,l".replace(',', '\n');
		String b = a;
		assertEquals("all-same test", 
				diffsToString(Diff.diffText(a, b, false, false, false)), 
				"");
	}

	@Test
	public void snake() {
		String a = "a,b,c,d,e,f".replace(',', '\n');
		String b = "b,c,d,e,f,x".replace(',', '\n');
		assertEquals("snake test", 
				diffsToString(Diff.diffText(a, b, false, false, false)), 
				"1.0.0.0*0.1.6.5*");
	}

	@Test
	public void repro() {
		String a = "c1,a,c2,b,c,d,e,g,h,i,j,c3,k,l".replace(',', '\n');
		String b = "C1,a,C2,b,c,d,e,I1,e,g,h,i,j,C3,k,I2,l".replace(',', '\n');
		assertEquals("repro20020920 test", 
				diffsToString(Diff.diffText(a, b, false, false, false)),
				"1.1.0.0*1.1.2.2*0.2.7.7*1.1.11.13*0.1.13.15*");
	}

	@Test
	public void repro20030207() {
		// 2003.02.07 - repro
		String a = "F".replace(',', '\n');
		String b = "0,F,1,2,3,4,5,6,7".replace(',', '\n');
		assertEquals("repro20030207 test",
				diffsToString(Diff.diffText(a, b, false, false, false)), 
				"0.1.0.0*0.7.1.2*");
	}

	// @Test() - XXX BROKEN
	public void muegelRepro() {
		String a = "HELLO\nWORLD";
		String b = "\n\nhello\n\n\n\nworld\n";
		assertEquals("repro20030409 test", 
				diffsToString(Diff.diffText(a, b,false, false, false)), 
				"2.8.0.0*");
	}

	@Test
	public void someDiffs() {
		String a = "a,b,-,c,d,e,f,f".replace(',', '\n');
		String b = "a,b,x,c,e,f".replace(',', '\n');
		assertEquals("some-changes test", 
				diffsToString(Diff.diffText(a, b, false, false, false)), 
				"1.1.2.2*1.0.4.4*1.0.7.6*");
	}

	@Test
	public void oneChangeWithinLongChainOfRepeats() {
		String a = "a,a,a,a,a,a,a,a,a,a".replace(',', '\n');
		String b = "a,a,a,a,-,a,a,a,a,a".replace(',', '\n');
		assertEquals("long chain of repeats test", 
				diffsToString(Diff.diffText(a, b, false, false, false)),
				"0.1.4.4*1.0.9.10*");
	}

	public String diffsToString(Diff.Item[] f) {
		StringBuilder ret = new StringBuilder();
		for (int n = 0; n < f.length; n++) {
			ret.append(f[n].deletedA + "." + f[n].insertedB + "." + f[n].StartA
					+ "." + f[n].StartB + "*");
		}
		return (ret.toString());
	}
}
