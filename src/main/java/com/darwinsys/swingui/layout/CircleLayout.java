package com.darwinsys.swingui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import com.darwinsys.util.Debug;

/** A simplistic CircleLayout implementation of the LayoutManager interface.
 * Components are drawn at their preferred size.
 * <br>
 * Bugs:
 * <ul>
 * <li>Only works well if the container is approximately square.
 * </ul>
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class CircleLayout implements LayoutManager {

	/** True to start at 12:00 position; false to start 1/2 way */
	boolean startAtTop = false;

	/** Construct a CircleLayout
	 */
	public CircleLayout(boolean isTop) {
		startAtTop = isTop;
	}

	/** Construct a CircleLayout with default values.
	 */
	public CircleLayout() {
		// Nothing to do
	}

	/** Adds the specified component with the specified constraint 
	 * to the layout; required by LayoutManager but not used.
	 */
	public void addLayoutComponent(String name, Component comp) {
		throw new IllegalArgumentException(
			"Don't use add(component, constraint) with CircleLayout");
	}

	/** Removes the specified component from the layout;
	 * required by LayoutManager, but does nothing.
	 */
	public void removeLayoutComponent(Component comp)  {
		// nothing to do
	}

	/** Calculates the preferred size dimensions for the specified panel 
	 * given the components in the specified parent container. */
	public Dimension preferredLayoutSize(Container parent)  {
		// System.out.println("preferredLayoutSize");
		return computelayoutSize(parent);
	}

	/** Find the minimum Dimension for the 
	 * specified container given the components therein.
	 */
	public Dimension minimumLayoutSize(Container parent)  {
		return computelayoutSize(parent);
	}


	/** Compute the size of the whole mess. Serves as the guts of 
	 * preferredLayoutSize() and minimumLayoutSize().
	 */
	protected Dimension computelayoutSize(Container parent) {

		// Pass the sums back as the actual size.
		// XXX finish this!
		return new Dimension(300, 300); // width == height!
	}

	/** Lays out the container in the specified panel. */
	public void layoutContainer(Container parent) {
		Component[] components = parent.getComponents();
		Dimension totalSize = parent.getSize();

		int dx = totalSize.width / 2;
		int dy = totalSize.height / 2;

		// make one quick pass to get max(PreferredSize.width[1..n]
		int width = 0, height = 0;
		for (int i=0; i < components.length; i++) {
			width = Math.max(width, components[i].getPreferredSize().width);
			height = Math.max(height, components[i].getPreferredSize().height);
		}
		int componentPad = Math.max(width, height) / 2;

		int PAD = 10;
		// Assumed to be regular (circle, not ellipse).
		int radius = dx - componentPad - PAD;

		int degreesPer = 360 / components.length;
		int angle = startAtTop ? 0 : degreesPer/2;

		for (int i=0; i < components.length; i++, angle += degreesPer) {
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			double theta = Math.toRadians(angle);
			int x = (int)(Math.sin(theta) * radius);
			int y = (int)(Math.cos(theta) * radius);
			Debug.println("layout", c.getClass().getName() + 
				" " + angle + ", " + theta +
				", x=" + x + ", y=" + y);
			c.setBounds(dx + x - (d.width/2), dy + y - (d.height/2),
				d.width, d.height);
		}
	}
}
