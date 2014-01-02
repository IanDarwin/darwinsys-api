package com.darwinsys.swingui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/** Class for creating an Internationalized Swing Action.
 * See also
 <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/action.html">
 * How to use Actions"</a>.
 */
public abstract class IntlAction extends AbstractAction {

	/** Convenience routing to make a Swing Action.
	 * @param b - ResourceBundle containting strings
	 * @param key - name such as "edit.cut" identifiying this action;
	 *		should have at least "label", "descr" and "key" values
	 * @param icon - an ImageIcon (24x24?)
	 */
	public IntlAction(
		ResourceBundle b, String key, ImageIcon icon) {

		String text =     b.getString(key + ".label");
		putValue(NAME, text);
		putValue(SMALL_ICON, icon);

		// May be a Mnemonic under ".key".
		try {
			String mnemonic = b.getString(key + ".key");
			putValue(MNEMONIC_KEY, Integer.valueOf(mnemonic.charAt(0)));
		} catch (MissingResourceException ex) {
			// Nothing to do.
		}

		// May be a ToolTip Description under ".descr".
		try {
			String ttDesc =   b.getString(key + ".descr");
			putValue(SHORT_DESCRIPTION, ttDesc);
		} catch (MissingResourceException ex) {
			putValue(SHORT_DESCRIPTION, text);
		}

	}
}

