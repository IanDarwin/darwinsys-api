package com.darwinsys.swingui.layout;

import java.awt.*;
import java.awt.event.*;

/**
 * A TabLayoutButton is used by TabLayout to display the name, and 
 * tell the TabLayout to show the component when called.
 * @deprecated Use javax.swing.JTabbedPane instead of TabLayout.
 */
class TabLayoutButton extends Component implements MouseListener {
	/** The current text for the Tab */
	String label;
	/** The current image for the Tab (NOT USED) */
	Image image;
	/** The TabLayout that we are working with. */
	TabLayout parent;
	/** Minimum padding around text */
	protected final int MINPAD = 5;
	/** A better padding around text */
	protected final int PREFPAD = 10;

	/** Construct a TabLayoutButton with a String and a Component (Panel?) */
	TabLayoutButton(String s, TabLayout p){
		label = s;
		image = null;
		parent = p;
		// We catch mouse clicks, and call back the TabLayout
		addMouseListener(this);
	}

	/** Construct a TabLayoutButton with an Image (NOT WORKING YET).
	 * @param im	Image, from GetImage
	 */
	TabLayoutButton(Image im, TabLayout p){
		label = null;
		throw new IllegalArgumentException("Image not supported yet");
	}

	/** When get notice a mouseClicked event, we want the parent to
	 * show our corresponding Panel (or whatever).
	 */
	public void mouseClicked(MouseEvent e) {
		parent.show(label);
	}
	public void mouseEntered(MouseEvent e)	{ /* NOTUSED */ }
	public void mouseExited(MouseEvent e)	{ /* NOTUSED */ }
	public void mousePressed(MouseEvent e)	{ /* NOTUSED */ }
	public void mouseReleased(MouseEvent e)	{ /* NOTUSED */ }

	public void paint(Graphics g) {
		Dimension d = getSize();
		Dimension t = getStrSize(label);

		// If we are the active button, use a colored background
		// if (active)
		//	g.fillRect(white);

		// Draw the tab itself.
		// Use width-2 to leave a line between tabs.
		// Cheap trick: Use height+5 so the bottom is clipped.
		g.draw3DRect(0, 0, d.width-2, d.height+5, true);
		// In 1.1, the "3DRect" isn't, so fake it a bit.
		Color orig = g.getColor();
		Color shad = orig.brighter();
		g.setColor(shad);
		g.draw3DRect(1, 1, d.width-2, d.height+5, true);

		// Draw either the image or the text
		// if (image != null)
		// 	g.drawImage(image, 0, 0, this);
		// else
		if (label != null) {
			int drawx = (d.width  - t.width)/2;
			int drawy = (d.height - t.height)/2; // ??
			g.drawString(label, drawx, 20);
		}
	}

	/** Compute the size of a String */
	protected Dimension getStrSize(String l) {
		Font f = getFont();
		if (f == null) 
			throw new IllegalArgumentException("No Font!");
		FontMetrics fm = getFontMetrics(f);
		if (fm == null) 
			throw new IllegalArgumentException("No FontMetrics!");
		return new Dimension(fm.stringWidth(l), fm.getHeight());
	}

	/** Compute our minimumSize */
	public Dimension getMinimumSize() {
		Dimension d = getStrSize(label);
		return new Dimension(d.width+MINPAD, d.height+MINPAD);
	}

	/** Compute our maximumSize */
	public Dimension getPreferredSize() {
		Dimension d = getStrSize(label);
		return new Dimension(d.width+PREFPAD, d.height+PREFPAD);
	}
}
