import java.awt.*;
import java.awt.event.*;

/* Show an example of closing a Window.
 * @author Ian Darwin
 * @version $Id$
 */
public class WinClose extends Frame {
	/* Constructor */
	public WinClose() {
		setSize(200, 100);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// Fancier: popup a "confirm quit" dialog here.
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
	}

	/* Main method */
	public static void main(String argv[]) {
		new WinClose().setVisible(true);
	}
}
