// BEGIN main
package com.darwinsys.swingui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.*;

/** Convenience routines for internationalized code.
 * All methods are static, for ease of use.
 */
public class I18N {

	/**
	 * Convenience/Factory routine to make a L10N'd JButton
	 * @param b The resources
	 * @param name The name for the button
	 * @return the constructed JButton
	 */
	public static JButton mkButton(ResourceBundle b, String name) {
		String label;
		try { label = b.getString(name+".label"); }
		catch (MissingResourceException e) { label=name; }
		return new JButton(label);
	}

	/** Convenience/factory routine to make a L10N'd JMenu
	 * @param b The resources
	 * @param name The name for the button
	 * @return the constructed JMenu
	 */
	public static JMenu mkMenu(ResourceBundle b, String name) {
		String menuLabel;
		try { menuLabel = b.getString(name+".label"); }
		catch (MissingResourceException e) { menuLabel=name; }
		return new JMenu(menuLabel);
	}

	/** Convenience/factory routine to make a L10N'd JMenuItem
	 * @param b The resources
	 * @param menu The name for the menu
	 * @param name The name for the menuItem
	 * @return the constructed JMenuItem
	 */
	public static JMenuItem mkMenuItem(ResourceBundle b,
			String menu, String name) {

		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }
		String key = null;
		try { key = b.getString(menu + "." + name + ".key"); }
		catch (MissingResourceException e) { key=null; }

		if (key == null)
			return new JMenuItem(miLabel);
		else
			return new JMenuItem(miLabel, key.charAt(0));
	}

	/** Show a L10N'd JOptionPane message dialog
	 * @param b The resources
	 * @param parent The owning JFrame for the dialog
	 * @param dialogTag The dialog name
	 * @param titleTag The title
	 * @param messageType the JOptionPane messageType
	 */
	public static void mkDialog(ResourceBundle b, JFrame parent,
		String dialogTag, String titleTag, int messageType) {
			JOptionPane.showMessageDialog(
				parent,
				getString(b, dialogTag, "DIALOG TEXT MISSING: " + dialogTag),
				getString(b, titleTag, "DIALOG TITLE MISSING: "  + titleTag),
				messageType);
	}

	/** Just find a L10N'd String (for dialogs, labels, etc.)
	 * @param b The resources
	 * @param name The name for the menuItem
	 * @param dflt A default string
	 * @return the localized sting if found, else the default string
	 */
	public static String getString(ResourceBundle b, String name, String dflt) {
		String result;
		try {
			result = b.getString(name);
		} catch (MissingResourceException e) {
			result = dflt;
		}
		return result;
	}
}
// END main
