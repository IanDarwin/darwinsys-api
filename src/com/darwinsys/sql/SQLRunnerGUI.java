package com.darwinsys.sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import com.darwinsys.swingui.UtilGUI;

/**
 * A static GUI to run one test file.
 */
public class SQLRunnerGUI  {
	
	final Preferences p = Preferences.userNodeForPackage(SQLRunnerGUI.class);
	
	final JProgressBar bar = new JProgressBar();
	
	final JFrame jf;
	
	/**
	 * Main method; ignores arguments.
	 */
	public static void main(String[] args) {
		new SQLRunnerGUI();
	}
	
	SQLRunnerGUI() {
		jf = new JFrame("SQLRunner");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Container cp = new JPanel();
		jf.add(cp, BorderLayout.CENTER);
		
		cp.setLayout(new FlowLayout());
		cp.add(new JLabel("SQL Command"));
		final JTextArea tf = new JTextArea(5, 60);
		cp.add(tf);
		
		final JComboBox modeList = new JComboBox();
		for (SQLRunner.Mode mode : SQLRunner.Mode.values()) {
			modeList.addItem(mode);
		}
		cp.add(new JLabel("Format:"));
		cp.add(modeList);		

		final JButton testButton = new JButton("Run");
		cp.add(testButton);
		testButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				
				// Run this under a new Thread, so we don't block the EventDispatch thread...
				new Thread() {
					
					public void run() {
						String config = "lims";
						
						try {
							Connection conn =  ConnectionUtil.getConnection(config);
							
							SQLRunner prog = new SQLRunner(conn, null, "t");	
							prog.setOutputMode((SQLRunner.Mode) modeList.getSelectedItem());
							
							prog.runStatement(tf.getText());
							seeGreen();	// If no exception thrown
							
						} catch (Exception e) {
							seeRed();
							error("Error: " + e);
						}
						
					}
					
				}.start();
			}
		});
		
		jf.add(bar, BorderLayout.SOUTH);
		bar.setMaximum(1);
		bar.setValue(1);
		seeGreen();
		
		jf.pack();
		UtilGUI.monitorWindowPosition(jf, p);
		jf.setVisible(true);
	}
	
	/**
	 * Set the bar to green, used only at the beginning
	 */
	void seeGreen() {
		bar.setForeground(Color.GREEN);
	}
	/**
	 * Set the bar to red, used when a test fails or errors.
	 */
	void seeRed() {
		bar.setForeground(Color.RED);
	}
	
	void error(String mesg) {
		JOptionPane.showMessageDialog(jf, mesg, "Oops", JOptionPane.ERROR_MESSAGE);
		seeRed();
	}

}
