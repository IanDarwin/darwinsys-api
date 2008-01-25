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
