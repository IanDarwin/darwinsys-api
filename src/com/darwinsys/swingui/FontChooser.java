import java.awt.*; import java.awt.event.*;
import javax.swing.*;

/** Canonical font selection dialog. AWT version.
 * @author	Ian Darwin
 * @version $Id$
 */
public class FontChooser extends Dialog {
	/** The font the user has chosen */
	protected Font resultFont;
	/** The resulting font name */
	protected String resultName;
	/** The resulting font size */
	protected int resultSize;
	/** The resulting boldness */
	protected boolean isBold;
	/** The resulting italicness */
	protected boolean isItalic;

	/** The list of Fonts */
	protected String fontList[];
	/** The file name chooser */
	protected List fNameChoice;
	/** The file size chooser */
	protected List fSizeChoice;
	/** The bold and italic choosers */
	Checkbox bold, italic;
	/** The list of font sizes */
	protected String fontSizes[] = {
		"8", "10", "11", "12", "14", "16", "18", "20", "24",
		"30", "36", "40", "48", "60", "72"
		};
	/** The display area. Use a JLabel as the AWT label doesn't always
	 * honor setFont() in a timely fashion :-)
	 */
	protected JLabel previewArea;

	/** Construct a FontChooser -- Sets title and gets 
	 * array of fonts on the system. Builds a GUI to let
	 * the user choose one font at one size.
	 */
	public FontChooser(JFrame f) {
		super(f, "Font Chooser", true);

		Container cp = this;	// or getContentPane() in Swing

		Panel top = new Panel();
		top.setLayout(new FlowLayout());

		fNameChoice = new List(8);
		top.add(fNameChoice);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// For JDK 1.1: returns about 10 names (Serif, SansSerif, etc.)
		// fontList = toolkit.getFontList();
		// For JDK 1.2: a much longer list; most of the names that come
		// with your OS (e.g., Arial), plus the Sun/Java ones (Lucida, 
		// Lucida Bright, Lucida Sans...)
		fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().
			getAvailableFontFamilyNames();

		for (int i=0; i<fontList.length; i++)
			fNameChoice.add(fontList[i]);
		fNameChoice.select(0);

		fSizeChoice = new List(8);
		top.add(fSizeChoice);

		for (int i=0; i<fontSizes.length; i++)
			fSizeChoice.add(fontSizes[i]);
		fSizeChoice.select(5);

		cp.add(BorderLayout.NORTH, top);

		Panel attrs = new Panel();
		top.add(attrs);
		attrs.setLayout(new GridLayout(0,1));
		attrs.add(bold  =new Checkbox("Bold", false));
		attrs.add(italic=new Checkbox("Italic", false));

		previewArea = new JLabel("Qwerty Yuiop", JLabel.CENTER);
		previewArea.setSize(200, 50);
		cp.add(BorderLayout.CENTER, previewArea);

		Panel bot = new Panel();

		Button okButton = new Button("Apply");
		bot.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
				dispose();
				setVisible(false);
			}
		});

		Button pvButton = new Button("Preview");
		bot.add(pvButton);
		pvButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
			}
		});

		Button canButton = new Button("Cancel");
		bot.add(canButton);
		canButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set all values to null. Better: restore previous.
				resultFont = null;
				resultName = null;
				resultSize = 0;
				isBold = false;
				isItalic = false;

				dispose();
				setVisible(false);
			}
		});

		cp.add(BorderLayout.SOUTH, bot);

		previewFont(); // ensure view is up to date!

		pack();
		setLocation(100, 100);
	}

	/** Called from the action handlers to get the font info,
	 * build a font, and set it.
	 */
	protected void previewFont() {
		resultName = fNameChoice.getSelectedItem();
		String resultSizeName = fSizeChoice.getSelectedItem();
		int resultSize = Integer.parseInt(resultSizeName);
		isBold = bold.getState();
		isItalic = italic.getState();
		int attrs = Font.PLAIN;
		if (isBold) attrs = Font.BOLD;
		if (isItalic) attrs |= Font.ITALIC;
		resultFont = new Font(resultName, attrs, resultSize);
		System.out.println("resultName = " + resultName + "; " +
				"resultFont = " + resultFont);
		previewArea.setFont(resultFont);
		previewArea.repaint();
	}

	/** Retrieve the selected font name. */
	public String getSelectedName() {
		return resultName;
	}
	/** Retrieve the selected size */
	public int getSelectedSize() {
		return resultSize;
	}

	/** Retrieve the selected font, or null */
	public Font getSelectedFont() {
		return resultFont;
	}

	/** Simple main program to start it running */
	public static void main(String args[]) {
		JFrame f = new JFrame("Dummy");
		FontChooser fc = new FontChooser(f);
		fc.setVisible(true);
		System.out.println("You chose " + fc.getSelectedFont());
		System.exit(0);
	}
}
