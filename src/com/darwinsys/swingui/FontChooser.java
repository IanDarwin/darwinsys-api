import java.awt.*;
import java.awt.event.*;

/** Canonical font selection dialog. AWT version.
 * @author	Ian Darwin
 * @version $Id$
 */
public class FontChooser extends Frame {
	/** The font the user has chosen */
	Font resultFont;
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
	protected Label previewArea;

	/** Construct a FontChooser -- Sets title and gets 
	 * array of fonts on the system. Builds a GUI to let
	 * the user choose one font at one size.
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

		previewArea = new Label("Qwerty Yuiop");
		cp.add(previewArea);
		previewArea.setSize(200, 50);

		Button okButton = new Button("Apply");
		cp.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
				dispose();
				setVisible(false);
			}
		});

		Button pvButton = new Button("Preview");
		cp.add(pvButton);
		pvButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
			}
		});

		Button canButton = new Button("Cancel");
		cp.add(canButton);
		canButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
			}
		});

		pack();
		setLocation(100, 100);
	}

	/** Called from the action handlers to get the font info,
	 * build a font, and set it.
	 */
	protected void previewFont() {
		String resultName = fNameChoice.getSelectedItem();
		String resultSizeName = fSizeChoice.getSelectedItem();
		int resultSize = Integer.parseInt(resultSizeName);
		resultFont = new Font(resultName, Font.BOLD, resultSize);
		// System.out.println("previewFont(): resultFont = " + resultFont);
		previewArea.setFont(resultFont);
		previewArea.repaint();
	}

	/** Retrieve the selected font, or null */
	public Font getSelectedFont() {
		return resultFont;
	}

	/** Simple main program to start it running */
	public static void main(String args[]) {
		new FontChooser().setVisible(true);
	}
}
