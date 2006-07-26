package com.darwinsys.swingui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;

/**
 * This class is used to create a single-enablement scope for a set of buttons. 
 * It is "Exclusion" in the sense that creating a set of buttons with the same 
 * ButtonExclusionGroup object means that sending setEnabled(true) to one of the
 * buttons will send setEnabled(false) to all other buttons in the group.
 * <p>
 * A ButtonExclusionGroup can be used with objects that inherit from AbstractButton. 
 * Typically a button group contains instances of JButton or JMenuItem,
 * but other types could be used.
 * 
 * Similar to a Swing "ButtonGroup" but for Enabledness rather than for Selection;
 * only one of the buttons can be enabled at a time.
 * Example:
 * <pre>
 * JButton goButton = new JButton("Start");
 * JButton stopButton = new JButton("Cancel");
 * ButtonExclusionGroup group - new ButtonExclusionGroup();
 * group.add(goButton);
 * group.add(stopButton);
 * </pre>
 * Because "two buttons" is the most common form of exclusion, as shown
 * in this example, there is a convenience Constructor; the last three lines
 * could be replaced with
 * <pre>ButtonExclusionGroup group - new ButtonExclusionGroup(goButton, stopButton);</pre>
 */
public class ButtonExclusionGroup {
	
	/** The list of buttons in this group */
	List<AbstractButton> buttons = new ArrayList<AbstractButton>();
	
	/** The one button that is currently enabled */
	AbstractButton enabled;

	/**
	 * Construct a ButtonExclusionGroup.
	 */
	public ButtonExclusionGroup() {
		// nothing to do
	}
	
	/**
	 * Construct a ButtonExclusionGroup with two buttons
	 * @param b1
	 * @param b2
	 */
	public ButtonExclusionGroup(AbstractButton b1, AbstractButton b2) {
		add(b1);
		add(b2);
	}
	
	/** 
	 * This PropertyChangeListener is added to every button that is in the
	 * group, and allows the Group to work correctly whether the user calls
	 * setEnabled() on the button directly OR calls thisGroup.setEnabled()
	 */
	private PropertyChangeListener listener = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent evt) {
			if (!"enabled".equals(evt.getPropertyName())) {
				return;
			}
			AbstractButton source = (AbstractButton) evt.getSource();
			if (buttons.contains(source)) {
				throw new IllegalArgumentException("Button not in group");
			}

			enabled = source;	// Don't call setEnabled() or you will loop...
			
			// disable other buttons in group.
			for (AbstractButton b : buttons) {
				if (b != source) {
					b.setEnabled(false);
				}
			}
		}
	};
	
	/** Returns the selected button.*/
	AbstractButton getEnabled() {
		return enabled;
	}
	
	/** Returns whether a Button is the enabled one.*/
	boolean isSelected(AbstractButton b) {
		return b == enabled;
	}

	/** Sets given Button to be the Enabled one.*/
	void setEnabled(AbstractButton b) {
		if (enabled != null) 
			enabled.setEnabled(false);
		enabled = b;
		b.setEnabled(true);
	}
	
	// Collection management.
	
	/** Adds the button to the group.*/
	void add(AbstractButton b) {
		buttons.add(b);
		b.addPropertyChangeListener(listener);
		
	}
	
	/** Removes the button from the group.*/
	void remove(AbstractButton b) {
		b.removePropertyChangeListener(listener);
		buttons.remove(b);
	}
	
	/** Returns the number of buttons in the group.*/
	int getButtonCount() {
		return buttons.size();
	}

	/** Returns all the buttons that are participating in this group.*/
	Iterator<AbstractButton> getElements() {
		return buttons.iterator();
	}
}
