package com.darwinsys.util;

import java.util.List;
import java.util.prefs.Preferences;

import com.darwinsys.util.RecentItems;

import junit.framework.TestCase;

public class RecentItemstest extends TestCase {
	
	Preferences p = Preferences.userNodeForPackage(getClass());
	RecentItems.Callback lister = new RecentItems.Callback() {

		public void reload(List<String> items) {
			for (String string : items) {
				System.out.println("LIST contains " + string);
			}
		}
		
	};
	RecentItems r;
	
	public void testAdding() {
		System.out.println("RecentItemstest.test1()");
		r = new RecentItems(p, lister);
		r.putRecent("abc");
		r.putRecent("def");
		List<String>list = r.getList();
		assertEquals(2, list.size());
		assertEquals("def", list.get(0));
		assertEquals("abc", list.get(1));
		r.putRecent("abc");
		try {
			list.remove(0);
			fail("Did not return unmodifiableList");
		} catch (UnsupportedOperationException e) {
			System.out.println("Caught expected exception");
		}
		assertEquals("abc", list.get(0));	// List is modifiable by "r", although not by us.
		list = r.getList();
		assertEquals("abc", list.get(0));	// check it in current copy of list
	}
	
	public void testRemoving() {
		System.out.println("RecentItemstest.test2()");
		r = new RecentItems(p, lister);
		r.putRecent("abc");
		r.putRecent("def");
		List<String> list = r.getList();
		assertEquals(2, list.size());
		r.remove("abc");
		assertEquals("def", list.get(0));		
	}
	
	public void testOverflow() {
		r = new RecentItems(p, lister, 3);
		List<String>list = r.getList();
		r.putRecent("abc");
		r.putRecent("def");
		r.putRecent("ghi");
		r.putRecent("jkl");
		assertEquals(3, list.size());
		r.putRecent("mno");
		r.putRecent("pqr");
		assertEquals("pqr", list.get(0));
		assertEquals("jkl", list.get(2));
		
	}
}
