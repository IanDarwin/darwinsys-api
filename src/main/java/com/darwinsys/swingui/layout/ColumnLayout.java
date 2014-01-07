package com.darwinsys.swingui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * <p>
 * ColumnLayout, a Single-Column Layout Manager for AWT/Swing.
 * Sort of a cross between BoxLayout and GridLayout(0, x).
 * Displays components in a single row or column based on the
 * alignment given to the constructor, with optional padding.</p>
 * <p>
 * There is a utility method for adding space between the previous
 * and next components, which (like Menu.addSeparator()) adds a
 * fixed-size non-visible component between two added components.</p>
 * <p>
 * Note: The current version of ColumnLayout doesn't resize.</p>
 *
 * @author Ian Darwin, http://www.darwinsys.com/
 */

public class ColumnLayout implements LayoutManager {
	/** Constant for X AXIS (horizontal column) alignment */
	public static final int X_AXIS = 'x';
	/** Constant for Y AXIS (vertical column) alignment */
	public static final int Y_AXIS = 'y';

	/** The alignment for this ColumnLayout */
	protected final int alignment;
	/** The X padding for this ColumnLayout */
	protected final int hPadding;	// blank final
	/** The Y padding for this ColumnLayout */
	protected final int vPadding;	// blank final
	/** The minimum width of each component */
	protected int minw;
	/** The minimum height of each component */
	protected int minh;
	/** The list of components */
	Component[] curComps;

	/** Construct a ColumnLayout given only an alignment. */
	public ColumnLayout(int dirn) {
		this(dirn, 0, 0);
	}

	/** Construct a ColumnLayout given an alignment and a padding amount. */
	public ColumnLayout(int dirn, int pad) {
		this(dirn, pad, pad);
	}

	/** Construct a ColumnLayout given an alignment and h,v padding amounts. */
	public ColumnLayout(int dirn, int hpad, int vpad) {
		alignment = dirn;
		hPadding  = hpad;
		vPadding  = vpad;
	}

	/**
	 * Called by AWT when the user uses the form add(name, Component).
	 * Adds the specified component with the specified name to the layout.
	 * Not necessary to use this form.
	 *
	 * @param	name	String with location for component c
	 * @param	c	Component to be added.
	 */
	public void addLayoutComponent(String name, Component c) {
		System.err.println("don't use add(component,name) with ColumnLayout");
	}

	/**
	 * Called by AWT to lay out the components
	 * in the target Container at its current size.
	 *
	 * @param	target	Container whose components are to be laid out.
	 */
	public void layoutContainer(Container target) {
		//System.out.println("ColumnLayout.layoutContainer() called.");
		doLayout(target);
	}

	/** Used internally: compute the layout and the maximal preferred
	 * width and height
	 * <br>
	 * TODO XXX NEED TO SCALE BY TARGSIZE?
	 */
	protected Dimension doLayout(Container target) {

		// Pass 1 - get preferred sizes
		minw = minh = 0;
		curComps = target.getComponents();
		for (int i = 0; i<curComps.length; i++) {
			Component tc = curComps[i];
			Dimension d = tc.getPreferredSize();
			minw = Math.max(minw, d.width);
			minh = Math.max(minh, d.height);
		}

		// Pass 2 - move & resize components
		int x = hPadding, y = vPadding;
		for (int i = 0; i<curComps.length; i++) {
			Component tc = curComps[i];
			Dimension d = tc.getPreferredSize();
			int cx, cy, cw, ch;
			switch (alignment) {
			case X_AXIS:
				if (tc instanceof Spacer)
					cw = d.width;
				else
					cw = minw;
				ch = minh; // d.height;
				cx = x;
				x += cw+hPadding;
				cy = vPadding;
				y  = ch;
				break;
			case Y_AXIS:
				cw = minw; // d.width;
				if (tc instanceof Spacer)
					ch = d.height;
				else
					ch = minh;
				cx = hPadding;
				x = cw;
				cy = y;
				y += ch+vPadding;
				break;
			default:
				throw new IllegalArgumentException("Invalid alignment");
			}
			tc.setBounds(cx, cy, cw, ch);
		}
		switch (alignment) {
			case X_AXIS:
				return new Dimension(x, y+2*vPadding);
			case Y_AXIS:
				return new Dimension(x+2*hPadding, y);
			default:
				throw new IllegalArgumentException("Invalid alignment");
		}
	}

	/**
	 * Called from AWT to calculate the minimum size dimensions
	 * for the target panel given the components in it.
	 * But we use our own list of named insertions, not the
	 * list of Components that the container keeps.
	 * @param	target	Container to calculate for
	 */
	public Dimension minimumLayoutSize(Container target) {
		// XXX should return doLayout(target, 0, 0); - add vpad, hpad args
		return doLayout(target);
	}

	/**
	 * Called by AWT to compute the preferred size for the target panel
	 * given our list of the components that it contains.
	 * @param	target	Container to calculate for
	 */
	public Dimension preferredLayoutSize(Container target) {
		System.out.println("preferredLayoutSize() called");
		return doLayout(target);
	}

	/** Class to represent a spacer, like Menubar.separator. */
	protected static class Spacer extends Component {
		private static final long serialVersionUID = -7583095138638433618L;

		public Dimension getPreferredSize() {
			return new Dimension(10, 10);
		}
	}

	/** Utility method to add a "spacer" */
	public void addSpacer(Container target) {
		target.add(new Spacer());
	}

	/**
	 * Called by AWT to remove a given component from the layout.
	 * @param	c	Component to be removed
	 */
	public void  removeLayoutComponent(Component c) {
		// nothing to do! user will (hopefully) invalidate() the target.
	}
}
