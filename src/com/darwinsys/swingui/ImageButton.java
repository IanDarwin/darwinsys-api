package com.darwinsys.swingui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/** A JButton with just a graphic display.
 * Just a thin wrapper for JButton.
 * @author ian
 * @deprecated Use the appropriate JButton constructors directly.
 */
public class ImageButton extends JButton {
	private static final long serialVersionUID = 3904965248233714485L;

	@Deprecated
    public ImageButton(ImageIcon icon) {
        super(icon);
    }

	@Deprecated
	public ImageButton(ImageIcon icon, String text) {
		super(text, icon);
	}
}
