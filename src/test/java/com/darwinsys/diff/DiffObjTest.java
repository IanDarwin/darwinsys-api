package com.darwinsys.diff;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darwinsys.diff.DiffObj.DiffField;

class DiffObjTest {

	class A {
		int id;
		String name;
	}
	A targetA;

	class B extends A {
		String addr;
	}
	B targetB;

	@BeforeEach
	void init() {
		targetA = new A();
		targetA.id = 1;
		targetA.name = "name a";
		targetB = new B();
		targetB.id = 2;
		targetB.name = "name b";
		targetB.addr = "address b";
	}

	@Test
	void differentClassesMustFail() {
		assertThrows(IllegalArgumentException.class, () ->
			DiffObj.diffObj(targetA, targetB));
	}

	@Test
	void noDiff() {
		assertEquals(0, DiffObj.diffObj(targetB, targetB).size());
	}

	@Test
	void oneDiff() {
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
