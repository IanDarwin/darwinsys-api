import java.util.*;
import javax.swing.*;

/** Set of convenience routines for internationalized code.
 * All convenience methods are static, for ease of use.
 * @version $Id$
 */
public class I18N {

	/** Convenience routine to make a JButton */
	public static JButton mkButton(ResourceBundle b, String name) {
		String label;
		try { label = b.getString(name+".label"); }
		catch (MissingResourceException e) { label=name; }
		return new JButton(label);
	}

	/** Convenience routine to make a JMenu */
	public static JMenu mkMenu(ResourceBundle b, String name) {
		String menuLabel;
		try { menuLabel = b.getString(name+".label"); }
		catch (MissingResourceException e) { menuLabel=name; }
		return new JMenu(menuLabel);
	}

	/** Convenience routine to make a JMenuItem */
	public static JMenuItem mkMenuItem(ResourceBundle b,
			String menu, String name) {

		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }
		String key = null;
		try { key = b.getString(menu + "." + name + ".key"); }
		catch (MissingResourceException e) { key=null; }

		// if (key == null)
			return new JMenuItem(miLabel);
		// else
			// return new JMenuItem(miLabel, new MenuShortcut(key.charAt(0)));
	}

	/** Show a JOptionPane message dialog */
	public static void mkDialog(JFrame parent,
		String dialogTag, String titleTag, int messageType) {
			JOptionPane.showMessageDialog(
				parent,
				rb.getString(dialogTag),
				rb.getString(titleTag),
				messageType);
	}
}
