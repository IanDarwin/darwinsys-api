package com.darwinsys.swingui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** A Swing-based Font Selection JDialog, to be created and
 * setVisible(true) in the usual way.
 * <p>
 * Uses Listeners to ensure that Preview button isn't actually needed
 * @author	Ian Darwin
 */
// BEGIN main
// package com.darwinsys.swingui;
public class FontChooser extends JDialog {

	private static final long serialVersionUID = 5363471384675038069L;

	public static final String DEFAULT_TEXT = "Lorem ipsem dolor";

	// Results:

	/** The font the user has chosen */
	protected Font resultFont = new Font("Serif", Font.PLAIN, 12);
	/** The resulting font name */
	protected String resultName;
	/** The resulting font size */
	protected int resultSize;
	/** The resulting boldness */
	protected boolean isBold;
	/** The resulting italicness */
	protected boolean isItalic;

	// Working fields

	/** Display text */
	protected String displayText = DEFAULT_TEXT;
	/** The font name chooser */
	protected JList fontNameChoice;
	/** The font size chooser */
	protected JList fontSizeChoice;
	/** The bold and italic choosers */
	JCheckBox bold, italic;

	/** The list of font sizes */
	protected Integer fontSizes[] = {
			8, 9, 10, 11, 12, 14, 16, 18, 20, 24, 30, 36, 40, 48, 60, 72
	};
	/** The index of the default size (e.g., 14 point == 4) */
	protected static final int DEFAULT_SIZE = 4;
	/** The font display area.
	 */
	protected JLabel previewArea;

	/** Construct a FontChooser -- Sets title and gets
	 * array of fonts on the system. Builds a GUI to let
	 * the user choose one font at one size.
	 */
	public FontChooser(JFrame f) {
		super(f, "Font Chooser", true);

		Container cp = getContentPane();

		JPanel top = new JPanel();
		top.setBorder(new TitledBorder(new EtchedBorder(), "Font"));
		top.setLayout(new FlowLayout());

		// This gives a longish list; most of the names that come
		// with your OS (e.g., Helvetica, Times), plus the Sun/Java ones (Lucida,
		// Lucida Bright, Lucida Sans...)
		String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().
			getAvailableFontFamilyNames();

		fontNameChoice = new JList(fontList);
		top.add(new JScrollPane(fontNameChoice));

		fontNameChoice.setVisibleRowCount(fontSizes.length);
		fontNameChoice.setSelectedValue("Serif", true);

		fontSizeChoice = new JList(fontSizes);
		top.add(fontSizeChoice);

		fontSizeChoice.setSelectedIndex(fontSizes.length * 3 / 4);

		cp.add(top, BorderLayout.NORTH);

		JPanel attrs = new JPanel();
		top.add(attrs);
		attrs.setLayout(new GridLayout(0,1));
		attrs.add(bold  =new JCheckBox("Bold", false));
		attrs.add(italic=new JCheckBox("Italic", false));

		// Make sure that any change to the GUI will trigger a font preview.
		ListSelectionListener waker = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				previewFont();
			}
		};
		fontSizeChoice.addListSelectionListener(waker);
		fontNameChoice.addListSelectionListener(waker);
		ItemListener waker2 = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				previewFont();
			}
		};
		bold.addItemListener(waker2);
		italic.addItemListener(waker2);

		previewArea = new JLabel(displayText, JLabel.CENTER);
		previewArea.setSize(200, 50);
		cp.add(previewArea, BorderLayout.CENTER);

		JPanel bot = new JPanel();

		JButton okButton = new JButton("Apply");
		bot.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
				dispose();
				setVisible(false);
			}
		});

		JButton canButton = new JButton("Cancel");
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

		cp.add(bot, BorderLayout.SOUTH);

		previewFont(); // ensure view is up to date!

		pack();
		setLocation(100, 100);
	}

	/** Called from the action handlers to get the font info,
	 * build a font, and set it.
	 */
	protected void previewFont() {
		resultName = (String)fontNameChoice.getSelectedValue();
		String resultSizeName = fontSizeChoice.getSelectedValue().toString();
		int resultSize = Integer.parseInt(resultSizeName);
		isBold = bold.isSelected();
		isItalic = italic.isSelected();
		int attrs = Font.PLAIN;
		if (isBold) attrs = Font.BOLD;
		if (isItalic) attrs |= Font.ITALIC;
		resultFont = new Font(resultName, attrs, resultSize);
		// System.out.println("resultName = " + resultName + "; " +
		//		 "resultFont = " + resultFont);
		previewArea.setFont(resultFont);
		pack();				// ensure Dialog is big enough.
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

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
		previewArea.setText(displayText);
		previewFont();
	}

	public JList getFontNameChoice() {
		return fontNameChoice;
	}

	public JList getFontSizeChoice() {
		return fontSizeChoice;
	}

	public boolean isBold() {
		return isBold;
	}

	public boolean isItalic() {
		return isItalic;
	}
}
// END main
