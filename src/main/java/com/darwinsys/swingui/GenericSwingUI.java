package com.darwinsys.swingui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.darwinsys.genericui.GenericUI;
import com.darwinsys.genericui.Severity;

public class GenericSwingUI implements GenericUI {

	private JFrame parent;
	
	public GenericSwingUI(JFrame jf) {
		parent = jf;
	}

	public void popup(Severity sev, String title, String message) {
		int severity;
		switch (sev) {
		case INFO: severity = JOptionPane.INFORMATION_MESSAGE; break;
		case WARNING: severity = JOptionPane.WARNING_MESSAGE; break;
		case ERROR: severity = JOptionPane.ERROR_MESSAGE; break;
		default: throw new IllegalStateException("Undefined Severity " + sev);
		}
		JOptionPane.showMessageDialog(parent, message, title, severity);
	}

}
