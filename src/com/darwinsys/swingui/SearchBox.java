package com.darwinsys.swingui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

/** SearchBox is a configurable text search box; options control
 * whether the Cancel (X) button is inside or outside the textfield.
 * <p>
 * Actually, the options aren't available yet.
 * <p>
 * There is no facility to add a "Search" button, since today only
 * dynamic searchboxes are in fashion. The normal way to use this is
 * to create it, get the TextField from it, and add a KeyAdapter
 * whose keyReleased method calls a setMatch method in your ListModel.
 * Here is an example of a setMatch() method and the corresponding
 * getElementAt() method from the ListModel.
 * <pre>
 	// Set visible only the items that match the given text. no
	// need to special-case for null str as String.contains("")
	// is always true.	
	public void setMatch(String match) {
		for (ToDoItem t : list) {
			final boolean vis = t.getText().contains(match);
			t.setVisible(vis);
		}
		notifyListenersChanged(
				new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, list.size()));
	}
	
	// Almost all of the complexity of the visible list vs
	// the full data list is hidden here.
	// Specified-by: ListModel
	public Object getElementAt(int virtualIndex) {
		int visibles = 0;
		for (ToDoItem t : list) {
			if (t.isVisible()) {
				if (visibles == virtualIndex) {
					return t;
				}
				++visibles;
			}
		}
		throw new IllegalStateException("Cant Happen");
	}
 * </pre>
 * <p>The implementation of getSize() in the list model is parallel
 * but simpler; it must of course count only elements that are visible.
 * @see javax.swing.ListModel
 * @version $Id$
 */
public class SearchBox extends JComponent {

	private static final long serialVersionUID = -21240072882607221L;

	private final JTextField text;

	private final JButton cancelButton;

	/** Construct the object including its GUI */
	public SearchBox() {
		super();
		setLayout(new FlowLayout()); // default
		
		this.setBorder(BorderFactory.createTitledBorder("Search"));

		add(text = new JTextField(10));

		add(cancelButton = new JButton("X"));

		cancelButton.addActionListener(clearer);
	}
	
	// Fake up an Icon for the JButton instead of "X"

	// Make this an Action
	private ActionListener clearer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			text.setText("");
			// Send a fake KeyEvent to notify the client
			final long now = System.currentTimeMillis();
			final KeyEvent fakeKeyEvent = 
				new KeyEvent(text, KeyEvent.KEY_RELEASED, now, '\0',
										KeyEvent.VK_UNDEFINED, '\0');
			Runnable r = new Runnable() {
				public void run() {
					text.dispatchEvent(fakeKeyEvent);
				}
			};
			new Thread(r).start();
		}
	};

	public JTextField getTextField() {
		return text;
	}

	public String getText() {
		return text.getText();
	}

	public void setText(String text) {
		this.text.setText(text);
	}
}
