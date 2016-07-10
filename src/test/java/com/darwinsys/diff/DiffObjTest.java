package com.darwinsys.diff;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import com.darwinsys.diff.DiffObj.DiffField;

public class DiffObjTest {

	class A {
		int id;
		String name;
	}
	A targetA;
	class B extends A {
		String addr;
	}
	B targetB;

	@Before
	public void init() {
		targetA = new A();
		targetA.id = 1;
		targetA.name = "name a";
		targetB = new B();
		targetB.id = 2;
		targetB.name = "name b";
		targetB.addr = "address b";
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDifferentClassesMustFail() {
		DiffObj.diffObj(targetA, targetB);
	}
	
	@Test
	public void testNoDiff() {
		assertEquals(0, DiffObj.diffObj(targetB, targetB).size());
	}

	@Test
	public void testOneDiff() {
		B newB = new B();
		newB.id = 3;
		newB.name=targetB.name;
		final String newAddr = "here am i, alone on a hill!";
		newB.addr = newAddr;
		int expected = 2; // id and addr differ
		final List<DiffField> diffs = DiffObj.diffObj(targetB, newB);
		assertEquals(expected, diffs.size());
		// Cannot depend on order
		boolean found = false;
		for (DiffObj.DiffField d : diffs) {
			if (d.name.equals("addr") && d.newVal.equals(newAddr)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
