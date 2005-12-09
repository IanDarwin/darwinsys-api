package com.darwinsys.genericui;

public interface GenericUI {
	
	/** Display a popup to the user, given a Severity,
	 * a titlebar title, and a message.
	 * @param sev
	 * @param title
	 * @param message
	 */
	public void popup(Severity sev, String title, String message);
}
