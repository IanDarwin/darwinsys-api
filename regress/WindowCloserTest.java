package regress;

import com.darwinsys.swingui.*;

import java.awt.*;
import java.awt.event.*;

/* Show an example of closing a Window.
 * @author Ian Darwin
 * @version $Id$
 */
public class WindowCloserTest {

	/* Main method */
	public static void main(String[] argv) {
		Frame f = new Frame("Close Me");
		f.add(new Label("Try Titlebar Close", Label.CENTER));
		f.setSize(100, 100);
		f.setVisible(true);
		f.addWindowListener(new WindowCloser(f, true));
	}
}
