package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecentItemsTest {

	private final boolean VERBOSE = false;

	Preferences prefs;
	RecentItems.Callback lister = new RecentItems.Callback() {
		// Override reload to just print items in list
		public void reload(List<String> items) {
			for (String string : items) {
				if (VERBOSE) {
					System.out.println("LIST contains " + string);
				}
			}
		}
	};
	RecentItems target;

	@BeforeEach
	void setup() throws BackingStoreException {
		prefs = Preferences.userNodeForPackage(getClass());
		prefs.clear();
		target = new RecentItems(prefs, lister);
	}

	@Test
	void adding() {
		System.out.println("RecentItemstest.test1()");
		
		target.putRecent("abc");
		target.putRecent("def");
		List<String>list = target.getList();
		assertEquals(2, list.size());
		assertEquals("def", list.get(0));
		assertEquals("abc", list.get(1));
		target.putRecent("abc");

		// List is modifiable by "target", check that re-adding item1 moves it to 0
		assertEquals("abc", list.get(0));	
		list = target.getList();
		assertEquals("abc", list.get(0));	// check it in current copy of list
	}

	@Test
	void listIsUnmodifiable() {
		target.putRecent("/a/b/c/d/e/f");
		assertThrows(UnsupportedOperationException.class, () ->
			target.getList().remove(0)); //should throw UOE
	}

	@Test
	void removing() {
		System.out.println("RecentItemstest.test2()");
		target.putRecent("abc");
		target.putRecent("def");
		List<String> list = target.getList();
		assertEquals(2, list.size());
		target.remove("abc");
		assertEquals("def", list.get(0));		
	}

	@Test
	void overflow() {
		target = new RecentItems(prefs, lister, 3);
		List<String>list = target.getList();
		target.putRecent("abc");
		target.putRecent("def");
		target.putRecent("ghi");
		target.putRecent("jkl");
		assertEquals(3, list.size());
		target.putRecent("mno");
		target.putRecent("pqr");
		assertEquals("pqr", list.get(0));
		assertEquals("jkl", list.get(2));
	}

	@Test
	void thatItLasts() {
		target.putRecent("Hello");
		target = new RecentItems(prefs, lister);
		assertEquals("Hello", target.getList().get(0));
	}
}
