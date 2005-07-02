package com.darwinsys.swingui;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/** A JButton with just a graphic display.
 * Adapted from the highly-recommended O'Reilly book Swing Hacks (ISBN 0-596-00907-0).
 * @author ian
 * @version $Id$
 */
public class ImageButton extends JButton {
	private static final long serialVersionUID = 3904965248233714485L;

    public ImageButton(ImageIcon icon) {
        this(icon, null);
    }
    
	public ImageButton(ImageIcon icon, String text) {
		setIcon(icon);
		setIconTextGap(0);
		int width = icon.getImage().getWidth(null);
		int height = icon.getImage().getHeight(null);
		setSize(width, height);
		setMargin(new Insets(0,0,0,0));
		setBorderPainted(false);
		setBorder(null);
		setText(text);
	}
}
