package com.darwinsys.util;

import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.darwinsys.util.RecentItems;

import org.junit.*;
import static org.junit.Assert.*;

public class RecentItemsTest {

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
	
	@Before public void setup() throws BackingStoreException {
		prefs = Preferences.userNodeForPackage(getClass());
		prefs.clear();
		target = new RecentItems(prefs, lister);
	}
	
	@Test
	public void testAdding() {
		System.out.println("RecentItemstest.test1()");
		
		target.putRecent("abc");
		target.putRecent("def");
		List<String>list = target.getList();
		assertEquals(2, list.size());
		assertEquals("def", list.get(0));
		assertEquals("abc", list.get(1));
		target.putRecent("abc");
		try {
			list.remove(0);
			fail("Did not return unmodifiableList");
		} catch (UnsupportedOperationException e) {
			System.out.println("Caught expected exception");
		}
		assertEquals("abc", list.get(0));	// List is modifiable by "r", although not by us.
		list = target.getList();
		assertEquals("abc", list.get(0));	// check it in current copy of list
	}

	@Test
	public void testRemoving() {
		System.out.println("RecentItemstest.test2()");
		target.putRecent("abc");
		target.putRecent("def");
		List<String> list = target.getList();
		assertEquals(2, list.size());
		target.remove("abc");
		assertEquals("def", list.get(0));		
	}

	@Test
	public void testOverflow() {
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
	public void testThatItLasts() {
		target.putRecent("Hello");
		target = new RecentItems(prefs, lister);
		assertEquals("Hello", target.getList().get(0));
	}
}
