import java.awt.*;
import java.awt.event.*;

/** Canonical font selection dialog. AWT version.
 * @author	Ian Darwin
 * @version $Id$
 */
public class FontChooser extends Frame {
	/** The list of Fonts */
	protected String fontList[];
	/** The file name chooser */
	protected Choice fNameChoice;
	/** The file size chooser */
	protected Choice fSizeChoice;
	/** The list of font sizes */
	protected String fontSizes[] = {
		"6", "8", "10", "11", "12", "14", "16", "18", "20", "24",
		"30", "36", "40", "48", "60", "72"
		};
	protected Label show;

	/** Construct a FontChooser -- Sets title and gets 
	 * array of fonts on the system
	 */
	public FontChooser() {
		super("Font Chooser");

		Container cp = this;	// or getContentPane() in Swing
		cp.setLayout(new FlowLayout());

		fNameChoice = new Choice();
		cp.add(fNameChoice);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// For JDK 1.1: returns about 10 names (Serif, SansSerif, etc.)
		fontList = toolkit.getFontList();
		// For JDK 1.2: a much longer list; most of the names that come
		// with your OS (e.g., Arial, Lucida, Lucida Bright, Lucida Sans...)
		// fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().
		//	getAvailableFontFamilyNames();

		for (int i=0; i<fontList.length; i++)
			fNameChoice.add(fontList[i]);

		fSizeChoice = new Choice();
		cp.add(fSizeChoice);

		for (int i=0; i<fontSizes.length; i++)
			fSizeChoice.add(fontSizes[i]);

		show = new Label("Qwerty Yuiop");
	}

	public void actionPerformed(actionEvent e) {
	}

	/** Simple main program to start it running */
	public static void main(String args[]) {
		new FontChooser().setVisible(true);
	}
}
