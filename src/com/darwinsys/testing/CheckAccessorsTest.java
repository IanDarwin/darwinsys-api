package com.darwinsys.testing;

import org.junit.Test;

public class TestSettersGettersTest {

	// JUnit 4 assert*() throw java.lang.AssertionError
	@Test(expected=AssertionError.class)
	public final void testBadClass() throws Exception {
		TestSettersGetters.process(BadClass.class);
	}
	
	@Test
	public final void testGoodClass() throws Exception {
		TestSettersGetters.process(GoodClass.class);
	}
	
	/** test data class with various types */
	static class GoodClass {
		char ch;
		int i;
		double d;
		float f;
		public char getCh() {
			return ch;
		}
		public void setCh(char ch) {
			this.ch = ch;
		}
		public double getD() {
			return d;
		}
		public void setD(double d) {
			this.d = d;
		}
		public float getF() {
			return f;
		}
		public void setF(float f) {
			this.f = f;
		}
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}
	}

	/** test data class, contains deliberate error
	 */
	static class BadClass {
		int i,j;

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}

		public int getJ() {
			return i; // deliberate error DO NOT FIX
		}

		public void setJ(int j) {
			this.j = j; // FindBugs M P UrF - expected
		}		
	}
}
