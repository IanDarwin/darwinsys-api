import java.awt.*;
import java.util.*;

/** Intended to become a CircleLayout layout manager.
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class CircleLayout implements LayoutManager {

	/** True to start at 12:00 position; false to start 1/2 way */
	boolean startAtTop = false;

	/** Construct a CircleLayout
	 */
	public CircleLayout(boolean startAtTop) {
		this.startAtTop = startAtTop;
	}

	/** Construct a CircleLayout with default valuse.
	 */
	public CircleLayout() {
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
		// System.out.println("minimumLayoutSize");
		return computelayoutSize(parent);
	}

	Point[] points;

	/** Compute the size of the whole mess. Serves as the guts of 
	 * preferredLayoutSize() and minimumLayoutSize().
	 */
	protected Dimension computelayoutSize(Container parent) {

		// Pass the sums back as the actual size.
		return new Dimension(200, 200); // width == height!
	}

	/** Lays out the container in the specified panel. */
	public void layoutContainer(Container parent) {
		// System.out.println("layoutContainer:");
		Component[] components = parent.getComponents();
		int numComps = components.length;
		points = new Point[numComps];
		Dimension totalSize = parent.getSize();

		int dx = totalSize.width / 2;
		int dy = totalSize.height / 2;
		int PAD = 10;
		int radius = dx - PAD;


		int degreesPer = 360 / numComps;
		int angle = startAtTop ? 0 : degreesPer/2;

		for (int i=0; i<numComps; i++, angle += degreesPer) {
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			double theta = Math.toRadians(angle);
			int x = (int)(Math.sin(theta) * radius);
			int y = (int)(Math.cos(theta) * radius);
			System.out.println(c.getClass().getName() + 
				" " + angle + ", " + theta +
				", x=" + x + ", y=" + y);
			c.setBounds(dx + x + (d.width/2), dy + y + (d.height/2),
				d.width, d.height);
		}
	}
}
