import java.awt.Window;
import java.awt.event.*;

/** A WindowCloser - watch for Window Closing events, and
 * follow them up with setVisible(false) and dispose().
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class WindowCloser extends WindowAdapter {
	Window win;
	public WindowCloser(Window w) {
		win = w;
	}
	public void windowClosing(WindowEvent e) {
		win.setVisible(false);
		win.dispose();
	}
}
