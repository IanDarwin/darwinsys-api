package com.darwinsys.swingui;

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
@Deprecated
public class AppletSupport {

	/** Whether to display messages on the console or in a Swing component */
	public static enum StatusMode { 
		/** Display on the console */
		CONSOLE, 
		/** Display via Swing */
		SWING,
	}

	/** The display mode */
	private static StatusMode mode = StatusMode.SWING;

	/** Private constructor: all methods are static */
	private AppletSupport() {
		System.out.println("Warning: Constructing AppletSupport when all methods are static");
	}

	/** Set the display mode.
	 * @param mode The StatusMode enum value to set
	 */
	public static void setStatusMode(StatusMode mode){
		AppletSupport.mode = mode;
	}

	/** Get an image from a URL
	 * @param url The constructed URL
	 * @return the constructed Image
	 */
	public static Image getImage(URL url) {
		throw new IllegalStateException(
			"Sorry, you called AppletSupport.getImage() before anybody contributed it");
	}

	/** Display the given message.
	 * @param message The message to display
	 */
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
	
	/** Display a document.
	 * @param targetURL The URL of the document to display.
	 */
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
