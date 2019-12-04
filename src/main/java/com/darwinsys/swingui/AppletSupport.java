package swingui;

import java.net.URL;
import java.awt.Image;
import java.io.IOException;

import javax.swing.*;

/**
 * APPLET SUPPORT - provide some of the functionality of the
 * old Applet class, for Applet code that's being converted
 * to use Swing. All methods are static.
 * Typical usage:
 * import static com.darwinsys.swingui.*;
 * then in main():
 *   AppletSupport.setStatusMode(StatusMode.CONSOLE); or StatusMode.SWING
 * Change 'getAppletContext().showDocument()' to just 'showDocument()'.
 */
public class AppletSupport {

	public static enum StatusMode { CONSOLE, SWING }

	private static StatusMode mode = StatusMode.SWING;

	public static void setStatusMode(StatusMode mode){
		AppletSupport.mode = mode;
	}

	private static Image getImage(URL url) {
		throw new IllegalStateException(
			"Sorry, you got to AppletSupport.getImage() before I wrote it");
	}

	public static void showStatus(String message) {
		switch(mode) {
			case CONSOLE:
				System.out.println(message);
				break;
			case SWING:
				JOptionPane.showMessageDialog(null, message);
				break;
			default:
				throw new IllegalArgumentException("Unknown StatusMode value " + mode);
		}
	}
	
	public static void showDocument(URL targetURL) {
		JTextArea ta = new JTextArea();
		try {
			ta.setText(targetURL.getContent().toString());
		} catch (IOException e) {
			ta.setText("Failed to load " + targetURL + " due to " + e);
		}
		JFrame jf = new JFrame(targetURL.toString());
		jf.show();
	}
}
