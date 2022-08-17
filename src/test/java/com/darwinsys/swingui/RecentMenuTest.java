package com.darwinsys.swingui;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecentMenuTest {

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
		assertEquals(1, target.recentFileNames.getList().size());
	}

	@Test
	void testClearWorks() {
		target.add(".");
		target.clear();
		assertEquals(0, target.recentFileNames.getList().size());
	}
}
