import java.awt.Window;
import java.awt.event.*;

/** A WindowCloser - watch for Window Closing events, and
 * follow them up with setVisible(false) and dispose().
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class WindowCloser extends WindowAdapter {
	/** The window we close */
	Window win;
	/** True if we are to exit as well. */
	boolean doExit = false;
	public WindowCloser(Window w) {
		this(w, false);
	}
	public WindowCloser(Window w, boolean exit) {
		win = w;
		doExit = exit;
	}
	public void windowClosing(WindowEvent e) {
		win.setVisible(false);
		win.dispose();
		if (doExit)
			System.exit(0);
	}
}
