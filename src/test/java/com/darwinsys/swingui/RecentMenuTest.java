package com.darwinsys.swingui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecentMenuTest {

	RecentMenu target;

	@BeforeEach
	void setup() {
		target = new RecentMenu(getClass()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void loadFile(String fileName) {
				System.out.println("Test would open " + fileName);
			}
		};
	}

	// @Test
	void testAddWorks() {
		target.add(".");
		assertEquals(1, target.recentItemNames.getList().size());
	}

	@Test
	void clearWorks() {
		target.add(".");
		target.clear();
		assertEquals(0, target.recentItemNames.getList().size());
	}
}
