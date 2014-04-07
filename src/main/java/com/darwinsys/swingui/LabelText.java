package com.darwinsys.swingui;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** A label and text combination, inspired by
 * the LabelText control in Guy Eddon's ActiveX Components book
 * (2nd Edition, page 203). But done more simply.
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
// BEGIN main
// package com.darwinsys.swingui;
public class LabelText extends JPanel implements java.io.Serializable {

	private static final long serialVersionUID = -8343040707105763298L;
	/** The label component */
	protected JLabel theLabel;
	/** The text field component */
	protected JTextField theTextField;
	/** The font to use */
	protected Font myFont;

	/** Construct the object with no initial values.
	 * To be usable as a JavaBean there must be a no-argument constructor.
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
		this(label, numChars, null);
	}

	/** Construct the object with given label, textfield size,
	 * and "Extra" component
	 * @param label The text to display
	 * @param numChars The size of the text area
	 * @param extra A third component such as a cancel button;
	 * may be null, in which case only the label and textfield exist.
	 */
	public LabelText(String label, int numChars, JComponent extra) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		theLabel = new JLabel(label);
		add(theLabel);
		theTextField = new JTextField(numChars);
		add(theTextField);
		if (extra != null) {
			add(extra);
		}
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
	public void setFont(Font f) {
		// This class' constructors call to super() can trigger
		// calls to setFont() (from Swing.LookAndFeel.installColorsAndFont),
		// before we create our components, so work around this.
		if (theLabel != null)
			theLabel.setFont(f);
		if (theTextField != null)
			theTextField.setFont(f);
	}

	/** Adds the ActionListener to receive action events from the textfield */
	public void addActionListener(ActionListener l) {
		theTextField.addActionListener(l);
	}

	/** Remove an ActionListener from the textfield. */
	public void removeActionListener(ActionListener l) {
		theTextField.removeActionListener(l);
	}
}
// END main
