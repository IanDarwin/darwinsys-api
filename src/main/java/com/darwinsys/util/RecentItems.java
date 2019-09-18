package com.darwinsys.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A list of recent items, such as a Recent File Menu, a Recent Choices dropdown, etc.
 * The items are kept in java's Preferences, and can be fetched with a call to a getter
 * and/or with a notification Callback.
 */
public class RecentItems {
	
	public interface Callback {
		public void reload(List<String> items);
	}
	
	/** The List of recent files */
	private List<String> recentNames = new ArrayList<>();
	public final static int DEFAULT_MAX_RECENT_FILES = 5;
	private final int maxRecentFiles;
	private final static String RECENT_ITEMS_KEY = "recent_items";
	private final Preferences prefsNode;
	private Callback callback;
	
	public RecentItems(Preferences prefs, Callback cb, int max) {
		this.prefsNode = prefs;
		this.callback = cb;
		maxRecentFiles = max;
		int i = 0;
		String s = null;
		do {
			s = prefsNode.get(makeName(i++), null);
			if (s != null)
				recentNames.add(s);
		} while (s != null);
	}
	
	public RecentItems(Preferences prefs, Callback cb) {
		this(prefs, cb, DEFAULT_MAX_RECENT_FILES);
	}
	
	/**
	 * Add the given filename to the top of the recent list in Prefs and in menu.
	 * It is generally <b>not</b> necessary for user code to call this method!
	 * @param f The name to add
	 */
	public void putRecent(String f) {
		// Trim from back end if too long
		while (recentNames.size() > maxRecentFiles - 1) {
			recentNames.remove(recentNames.size()-1);
		}
		// Move filename to front: Remove if present, add at front.
		if (recentNames.contains(f)) {
			recentNames.remove(f);
		}
		recentNames.add(0, f);

		// Now save from List into Prefs
		int i = 0;
		for (String t : recentNames) {
			prefsNode.put(makeName(i++), t);
		}
		try {
			prefsNode.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException("Failed to flush Prefs Node", e);
		}

		// Finally, load menu again.
		callCallBack();
	}

	private void callCallBack() {
		if (callback != null) {
			callback.reload(getList());
		}
	}
	
	String makeName(int i) {
		return RECENT_ITEMS_KEY + "." + i;
	}

	public List<String> getList() {
		return Collections.unmodifiableList(recentNames);
	}
	
	/**
	 * Clear all saved Recent Items from Preferences, from memory, and from the Menu.
	 * There is NO UNDO for this so call with care. DOES invoke your callback.
	 */
	public void clear() {
		for (int i = 0; i < maxRecentFiles; i++) {
			prefsNode.remove(makeName(i));
		}
		callCallBack();
	}

	/**
	 * Remove the given name from the list; does NOT invoke your callback
	 * since it might have been invoked in your callback...
	 * @param f The name to remove
	 */
	public void remove(String f) {
		recentNames.remove(f);
	}
}
