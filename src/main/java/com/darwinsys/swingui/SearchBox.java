package com.darwinsys.swingui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
 */
public class SearchBox extends JComponent {

	private static final long serialVersionUID = -21240072882607221L;

	private final JTextField text;

	private final JButton clearButton;

	private boolean imbed = true;

	/** Construct the Search Box including its GUI.
	 * @param borderStr The border title; if null, no titled border added
	 */
	public SearchBox(String borderStr) {
		super();
		setLayout(new FlowLayout()); // default
		
		if (borderStr != null) {
			this.setBorder(BorderFactory.createTitledBorder(borderStr));
		}

		add(text = new JTextField(10));
		clearButton = new JButton("X");
		if (imbed ) {
			text.setLayout(new FlowLayout(FlowLayout.RIGHT));
			Dimension d = text.getPreferredSize();
			d.height *= 1.7;
			text.setPreferredSize(d);
			final Font xFont = clearButton.getFont();
			Font newFont = xFont.deriveFont(xFont.getSize2D()*0.7f);
			clearButton.setFont(newFont);
			text.add(clearButton);
		} else {
			// Plain mode, add it beside the textfield
			add(clearButton);
		}
		clearButton.addActionListener(clearer);
	}

	/** Construct the Search Box including its GUI with a default title
	 */
	public SearchBox() {
		this("Search");
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

	public JButton getClearButton() {
		return clearButton;
	}
}
