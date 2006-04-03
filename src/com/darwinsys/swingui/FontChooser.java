package com.darwinsys.swingui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/** A font selection dialog.
 * <p>Note: can take a while to start up on systems
 * with (literally) hundreds of fonts.
 * TODO change list to JList, add a SelectionChangedListener to preview.
 * @author	Ian Darwin
 * @version $Id$
 */
public class FontChooser extends JDialog {

	// Results:

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

	// Working fields

	/** Display text */
	protected String displayText = "Qwerty Yuiop";
	/** The list of Fonts */
	protected String fontList[];
	/** The font name chooser */
	protected List fontNameChoice;
	/** The font size chooser */
	protected List fontSizeChoice;
	/** The bold and italic choosers */
	Checkbox bold, italic;

	/** The list of font sizes */
	protected String fontSizes[] = {
		"8", "10", "11", "12", "14", "16", "18", "20", "24",
		"30", "36", "40", "48", "60", "72"
		};
	/** The index of the default size (e.g., 14 point == 4) */
	protected static final int DEFAULT_SIZE = 4;
	/** The display area. Use a JLabel as the AWT label doesn't always
	 * honor setFont() in a timely fashion :-)
	 */
	protected JLabel previewArea;

	/** Construct a FontChooser -- Sets title and gets 
	 * array of fonts on the system. Builds a GUI to let
	 * the user choose one font at one size.
	 */
	public FontChooser(Frame f) {
		super(f, "Font Chooser", true);

		Container cp = getContentPane();

		Panel top = new Panel();
		top.setLayout(new FlowLayout());

		fontNameChoice = new List(8);
		top.add(fontNameChoice);

		// This gives a longish list; most of the names that come
		// with your OS (e.g., Helvetica, Times), plus the Sun/Java ones (Lucida, 
		// Lucida Bright, Lucida Sans...)
		fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().
			getAvailableFontFamilyNames();

		for (int i=0; i<fontList.length; i++)
			fontNameChoice.add(fontList[i]);
		fontNameChoice.select(0);

		fontSizeChoice = new List(8);
		top.add(fontSizeChoice);

		for (int i=0; i<fontSizes.length; i++)
			fontSizeChoice.add(fontSizes[i]);
		fontSizeChoice.select(DEFAULT_SIZE);

		cp.add(top, BorderLayout.NORTH);

		Panel attrs = new Panel();
		top.add(attrs);
		attrs.setLayout(new GridLayout(0,1));
		attrs.add(bold  =new Checkbox("Bold", false));
		attrs.add(italic=new Checkbox("Italic", false));

		previewArea = new JLabel(displayText, JLabel.CENTER);
		previewArea.setSize(200, 50);
		cp.add(previewArea, BorderLayout.CENTER);

		Panel bot = new Panel();

		JButton okButton = new JButton("Apply");
		bot.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
				dispose();
				setVisible(false);
			}
		});

		JButton pvButton = new JButton("Preview");
		bot.add(pvButton);
		pvButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewFont();
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
		resultName = fontNameChoice.getSelectedItem();
		String resultSizeName = fontSizeChoice.getSelectedItem();
		int resultSize = Integer.parseInt(resultSizeName);
		isBold = bold.getState();
		isItalic = italic.getState();
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
}
