import java.awt.*;
import java.awt.event.*;

/** Canonical font selection dialog. AWT version.
 * @author	Ian Darwin
 * @version $Id$
 */
public class FontChooser extends Frame {
	/** The font the user has chosen */
	protected Font resultFont;
	/** The resulting font name */
	protected String resultName;
	/** The resulting font size */
	protected int resultSize;
	/** The list of Fonts */
	protected String fontList[];
	/** The file name chooser */
	protected List fNameChoice;
	/** The file size chooser */
	protected List fSizeChoice;
	/** The list of font sizes */
	protected String fontSizes[] = {
		"8", "10", "11", "12", "14", "16", "18", "20", "24",
		"30", "36", "40", "48", "60", "72"
		};
	/** The display area */
	protected Label previewArea;

	/** Construct a FontChooser -- Sets title and gets 
	 * array of fonts on the system. Builds a GUI to let
	 * the user choose one font at one size.
	 */
	public FontChooser() {
		super("Font Chooser");

		Container cp = this;	// or getContentPane() in Swing

		Panel top = new Panel();
		top.setLayout(new FlowLayout());

		fNameChoice = new List();
		top.add(fNameChoice);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// For JDK 1.1: returns about 10 names (Serif, SansSerif, etc.)
		fontList = toolkit.getFontList();
		// For JDK 1.2: a much longer list; most of the names that come
		// with your OS (e.g., Arial, Lucida, Lucida Bright, Lucida Sans...)
		// fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().
		//	getAvailableFontFamilyNames();

		for (int i=0; i<fontList.length; i++)
			fNameChoice.addItem(fontList[i]);
		fNameChoice.select(0);

		fSizeChoice = new List();
		top.add(fSizeChoice);

		for (int i=0; i<fontSizes.length; i++)
			fSizeChoice.addItem(fontSizes[i]);
		fSizeChoice.select(0);

		cp.add(BorderLayout.NORTH, top);

		previewArea = new MyLabel("Qwerty Yuiop", Label.CENTER);
		previewArea.setSize(200, 50);
		cp.add(BorderLayout.CENTER, previewArea);

		Panel bot = new Panel();
		bot.setLayout(new FlowLayout());

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
				dispose();
				setVisible(false);
			}
		});

		cp.add(BorderLayout.SOUTH, bot);

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
		resultFont = new Font(resultName, Font.BOLD, resultSize);
		// System.out.println("resultName = " + resultName + "; " +
		//		"resultFont = " + resultFont);
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
		new FontChooser().setVisible(true);
	}

	/** This tiny inner class just extends Label to allow for
	 * setSize() to control the results of getPreferredSize().
	 */
	class MyLabel extends Label {
		protected int wid = 0, ht = 0;

		public MyLabel(String labStr, int align) {
			super(labStr, align);
		}

		public void setSize(int w, int h) {
			super.setSize(wid = w, ht = h);
		}

		public Dimension getPreferredSize() {
			if (wid == 0 || ht == 0)
				return super.getPreferredSize();
			return new Dimension(wid, ht);
		}
	}
}
