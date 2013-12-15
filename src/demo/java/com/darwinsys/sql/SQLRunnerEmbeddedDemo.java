package com.darwinsys.sql;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

/**
 * Show use as an imbedded application
 *
 */
public class SQLRunnerEmbeddedDemo {

	/**
	 * @param args Not used.
	 */
	public static void main(final String[] args) {
		new SQLRunnerEmbeddedDemo();
	}

	final JFrame jf = new JFrame("SQLRunner Imbedded Demo");
	final JCheckBox toggleSetting;

	@SuppressWarnings("serial")
	SQLRunnerEmbeddedDemo() {
		jf.setLayout(new FlowLayout());
		final ConfigurationManager mgr = new DefaultConfigurationManager();
		final Action toggleAction = new AbstractAction("Enable exit") {
			public void actionPerformed(ActionEvent e) {
				SQLRunner.setOkToExit(toggleSetting.isEnabled());
			}
		};
		jf.add(toggleSetting = new JCheckBox(toggleAction));
		final Action goAction = new AbstractAction("Tools->SQLRunner") {
			public void actionPerformed(ActionEvent e) {
				new SQLRunnerGUI(mgr);
			}
		};
		jf.add(new JButton(goAction));
		jf.pack();
		jf.setVisible(true);
	}
}
