// package com.darwinsys.entrylayout;

import java.awt.*;
import java.util.*;

/** A simple layout manager, for Entry areas like:
 * <PRE>
 *    Login: _______________
 * Password: _______________
 * </PRE>
 * Basically two (or more) columns of different, but constant, widths.
 * <b>Note: all columns must be the same height!</b>.
 * <P>
 * Construct instances by passing an array of the column width percentages
 * (as doubles, fractions from 0.1 to 0.9, so 40%,60% would be {0.4, 0.6}).
 * The length of this array uniquely determines the number of columns.
 * Columns are forced to be the relevant widths.
 * <b>Note:</b> As with GridLayout, the number of items 
 * added <B>must</B> be an even
 * multiple of the number of columns. If not, exceptions may be thrown!
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class EntryLayout implements LayoutManager {
	/** The array of widths, as decimal fractions (0.4 == 40%, etc.). */
	protected final double[] widthPercentages;

	/** The number of columns. */
	protected final int COLUMNS;

	/** The default padding */
	protected final static int HPAD = 5, VPAD = 5;
	/** The actual padding */
	protected final int hpad, vpad;

	/** True if the list of widths was valid. */
	protected boolean validWidths = false;

	/** Construct an EntryLayout with widths and padding specified.
	 * @param widths	Array of doubles specifying column widths.
	 * @param h			Horizontal padding between items
	 * @param v			Vertical padding between items
	 */
	public EntryLayout(double[] widths, int h, int v) {
		COLUMNS = widths.length;
		widthPercentages = new double[COLUMNS];
		for (int i=0; i<widths.length; i++) {
			if (widths[i] >= 1.0)
				throw new IllegalArgumentException(
					"EntryLayout: widths must be fractions < 1");
			widthPercentages[i] = widths[i];
		}
		validWidths = true;
		hpad = h;
		vpad = v;
	}
	/** Construct an EntryLayout with widths and with default padding amounts.
	 * @param widths	Array of doubles specifying column widths.
	 */
	public EntryLayout(double[] widths) {
		this(widths, HPAD, VPAD);
	}

	/** Adds the specified component with the specified constraint 
	 * to the layout; required by LayoutManager but not used.
	 */
	public void addLayoutComponent(String name, Component comp) {
		// nothing to do
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
		return computeLayoutSize(parent, hpad, vpad);
	}

	/** Find the minimum Dimension for the 
	 * specified container given the components therein.
	 */
	public Dimension minimumLayoutSize(Container parent)  {
		// System.out.println("minimumLayoutSize");
		return computeLayoutSize(parent, 0, 0);
	}

	/** The width of each column, as found by computLayoutSize(). */
	int[] widths;
	/** The height of each row, as found by computLayoutSize(). */
	int[] heights;

	/** Compute the size of the whole mess. Serves as the guts of 
	 * preferredLayoutSize() and minimumLayoutSize().
	 */
	protected Dimension computeLayoutSize(Container parent, int hpad, int vpad) {
		if (!validWidths)
			return null;
		Component[] components = parent.getComponents();
		Dimension contSize = parent.getSize();
		int preferredWidth = 0, preferredHeight = 0;
		widths = new int[COLUMNS];
		heights = new int[components.length / COLUMNS];
		// System.out.println("Grid: " + widths.length + ", " + heights.length);

		int i;
		// Pass One: Compute largest widths and heights.
		for (i=0; i<components.length; i++) {
			int row = i / widthPercentages.length;
			int col = i % widthPercentages.length;
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			widths[col] = Math.max(widths[col], d.width);
			heights[row] = Math.max(heights[row], d.height);
		}

		// Pass two: agregate them.
		for (i=0; i<widths.length; i++)
			preferredWidth += widths[i] + hpad;
		for (i=0; i<heights.length; i++)
			preferredHeight += heights[i] + vpad;

		// Finally, pass the sums back as the actual size.
		return new Dimension(preferredWidth, preferredHeight);
	}

	/** Lays out the container in the specified panel. */
	public void layoutContainer(Container parent) {
		// System.out.println("layoutContainer:");
		if (!validWidths)
			return;
		Component[] components = parent.getComponents();
		Dimension contSize = parent.getSize();
		for (int i=0; i<components.length; i++) {
			int row = i / COLUMNS;
			int col = i % COLUMNS;
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			int colWidth = (int)(contSize.width * widthPercentages[col]);
			Rectangle r = new Rectangle(
				col == 0 ? 0 :
				hpad * (col-1) + (int)(contSize.width * widthPercentages[col-1]),
				vpad * (row) + (row * heights[row]) + (heights[row]-d.height),
				colWidth, d.height);
			// System.out.println(c.getClass() + "-->" + r);
			c.setBounds(r);
		}
	}

}
