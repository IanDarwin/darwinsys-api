import java.awt.*;
import java.util.*;

/** Intended to become a CircleLayout layout manager.
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class CircleLayout implements LayoutManager {
	/** Construct a CircleLayout 
	 */
	public CircleLayout() {
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
		Dimension contSize = parent.getSize();

		int degreesPer = 360 / numComps;
		int centreX = contSize.width / 2;
		int centreY = contSize.height / 2;
		int PAD = 10;
		int radius = centreX - PAD;

		int i;
		for (i=0; i<numComps; i++) {
			Component c = components[i];
			Dimension d = c.getPreferredSize();
			int angle = i * degreesPer;
			double angleRad = Math.toRadians(angle);
			double tan = Math.tan(angleRad);
			System.out.println(c.getClass() + " " + angle + ", " + tan);
		}
	}
}
