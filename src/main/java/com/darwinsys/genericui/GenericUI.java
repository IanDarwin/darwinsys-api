package com.darwinsys.genericui;

public interface GenericUI {
	
	/** Display a popup to the user, given a Severity,
	 * a titlebar title, and a message.
	 * @param sev The severity
	 * @param title The dialog title
	 * @param message The message text
	 */
	public void popup(Severity sev, String title, String message);
}
