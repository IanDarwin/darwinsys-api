package com.darwinsys.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

/**
 * A TextField that looks like a label until you click on it
 */
public class ClickToActivateTextField extends JTextField {

	/** Constructor */
	public ClickToActivateTextField() {
		this(null);
	}

	/** Constructor
	 * @param label The String to display
	 */
	public ClickToActivateTextField(String label) {
		super(label);
		setEnabled(false);

		// clicking enables
		addMouseListener(new MyMouseListener());

		// hitting return disables
		addActionListener(new MyActionListener());
	}

	/** The MouseListener */
	class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			setEnabled(true);
		}
	}

	/** The ActionListener */
	class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			setEnabled(false);
		}
	}
}
