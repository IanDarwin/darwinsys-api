import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** A label and text combination, inspired by
 * the LabelText control in Guy Eddon's ActiveX Components book
 * (2nd Edition, p. 203). But done honestly, without using a GUI builder
 * just to create a (captiveX) project and then typing 100 lines of code.
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class LabelText extends JComponent implements java.io.Serializable {
	/** The label component */
	protected JLabel theLabel;
	/** The label component */
	protected JTextField theTextField;

	/** Construct the object with the label and a default textfield size */
	public LabelText(String label) {
		this(label, 10);
	}

	/** Construct the object with given label and textfield size */
	public LabelText(String label, int nch) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		theLabel = new JLabel(label);
		add(theLabel);
		theTextField = new JTextField(nch);
		add(theTextField);
	}

	/** Get the label's horizontal alignment */
	public int getLabelAlignment() {
		return theLabel.getHorizontalAlignment();
	}

	/** Set the label's horizontal alignment */
	public void setLabelAlignment(int align) {
		switch (align) {
		case JLabel.LEFT:
		case JLabel.CENTER:
		case JLabel.RIGHT:
			theLabel.setHorizontalAlignment(align);
			break;
		default:
			throw new IllegalArgumentException(
				"setLabelAlignment argument must be one of JLabel aligners");
		}
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
		theLabel.setFont(f);
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
