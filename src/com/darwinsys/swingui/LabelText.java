package com.darwinsys.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** A label and text combination, inspired by
 * the LabelText control in Guy Eddon's ActiveX Components book
 * (2nd Edition, p. 203). But done more simply, without using a GUI builder
 * just to create a (captiveX) project and then typing 100 lines of code.
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class LabelText extends JPanel implements java.io.Serializable {
	/** The label component */
	protected JLabel theLabel;
	/** The label component */
	protected JTextField theTextField;
	/** The font to use */
	protected Font myFont;

	/** Construct the object with no initial values.
	 * To be usable as a JavaBean there MUST be a no-argument constructor.
	 */
	public LabelText() {
		this("(LabelText)",  12);
	}

	/** Construct the object with the label and a default textfield size */
	public LabelText(String label) {
		this(label, 12);
	}

	/** Construct the object with given label and textfield size */
	public LabelText(String label, int numChars) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		theLabel = new JLabel(label);
		add(theLabel);
		theTextField = new JTextField(numChars);
		add(theTextField);
		//if (myFont != null) {
		//	// See setFont() below!
		//	theLabel.setFont(myFont);
		//	theTextField.setFont(myFont);
		//}
	}

	/** Get the label's horizontal alignment */
	public int getLabelAlignment() {
		return theLabel.getHorizontalAlignment();
	}

	/** Set the label's horizontal alignment */
	public void setLabelAlignment(int align) {
		theLabel.setHorizontalAlignment(align);
	}

	/** Get the text displayed in the text field */
	public String getText() {
		return theTextField.getText();
	}

	/** Set the text displayed in the text field */
	public void setText(String text) {
		theTextField.setText(text);
	}

	/** Get the text displayed in the label */
	public String getLabel() {
		return theLabel.getText();
	}

	/** Set the text displayed in the label */
	public void setLabel(String text) {
		theLabel.setText(text);
	}

	/** Set the font used in both subcomponents. */
	// public void setFont(Font f) {
		// myFont = f;
		// Since this class' constructors call to super() can trigger
		// calls to setFont() (from Swing.LookAndFeel.installColorsAndFont),
		// have to cache the font here.
		// if (theLabel != null)
			// theLabel.setFont(f);
		// if (theTextField != null)
			// theTextField.setFont(f);
	// }

	/** Adds the ActionListener to receive action events from the textfield */
	public void addActionListener(ActionListener l) {
		theTextField.addActionListener(l);
	}

	/** Remove an ActionListener from the textfield. */
	public void removeActionListener(ActionListener l) {
		theTextField.removeActionListener(l);
	}
}
