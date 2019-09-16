package com.darwinsys.swingui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Routines for working with Menus.
 * It's assumed that you have done something like
 * ResourceBundle b = ResourceBundle.getBundle("Menus");
 */
public class MenuUtils {

	/** Convenience routine to make a Menu */
	public static JMenu mkMenu(ResourceBundle b, String name) {
		String menuLabel;
		try { menuLabel = b.getString(name+".label"); }
		catch (MissingResourceException e) { menuLabel=name; }
		return new JMenu(menuLabel);
	}

	/** Convenience routine to make a JMenuItem */
	public static JMenuItem mkMenuItem(ResourceBundle b, String menu, String name) {
		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }
		String key = null;
		try { key = b.getString(menu + "." + name + ".key"); }
		catch (MissingResourceException e) { key=null; }

		if (key == null)
			return new JMenuItem(miLabel);
		else
			return new JMenuItem(miLabel/*, new MenuShortcut(key.charAt(0))*/);
	}

	/** Convenience routine to make a MenuItem */
	public static JCheckBoxMenuItem mkCheckboxMenuItem(ResourceBundle b, String menu, String name) {
		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }

		return new JCheckBoxMenuItem(miLabel);
	}
	
	/** Convenience routine to make a JMenuItem */
	public static JButton mkButton(ResourceBundle b, String menu, String name) {
		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }
		return new JButton(miLabel);
	}
}
