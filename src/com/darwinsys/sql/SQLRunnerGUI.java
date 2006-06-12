package com.darwinsys.sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.darwinsys.io.TextAreaWriter;
import com.darwinsys.swingui.UtilGUI;
import com.darwinsys.util.Verbosity;

/**
 * A simple GUI to run one set of commands.
 */
public class SQLRunnerGUI  {
	
	private static final int DISPLAY_COLUMNS = 70;

	final Preferences p = Preferences.userNodeForPackage(SQLRunnerGUI.class);
	
	final JProgressBar bar = new JProgressBar();
	
	final JFrame mainWindow;
	
	final JTextArea inputTextArea;
	
	final PrintWriter out;
	
	/**
	 * Main method; ignores arguments.
	 */
	public static void main(String[] args) {
		new SQLRunnerGUI();
	}
	
	public SQLRunnerGUI() {
		mainWindow = new JFrame("SQLRunner");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Container controlsArea = new JPanel();
		mainWindow.add(controlsArea, BorderLayout.NORTH);
		
		Set<String> connections = ConnectionUtil.getConfigurations();
		final JComboBox connectionsList = new JComboBox(connections.toArray(new String[connections.size()]));
		controlsArea.add(new JLabel("Connection"));
		controlsArea.add(connectionsList);
		
		controlsArea.setLayout(new FlowLayout());
		
		final JComboBox modeList = new JComboBox();
		for (OutputMode mode : OutputMode.values()) {
			modeList.addItem(mode);
		}
		controlsArea.add(new JLabel("Format:"));
		controlsArea.add(modeList);		

		final JButton testButton = new JButton("Run");
		controlsArea.add(testButton);
		testButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				
				// Run this under a its own Thread, so we don't block the EventDispatch thread...
				new Thread() {
					
					public void run() {
						try {
							Connection conn =  ConnectionUtil.getConnection((String)connectionsList.getSelectedItem());
							SQLRunner.setVerbosity(Verbosity.QUIET);
							SQLRunner prog = new SQLRunner(conn, null, "t");
							prog.setOutputFile(out);
							prog.setOutputMode((OutputMode) modeList.getSelectedItem());
							setActive();
							prog.runStatement(inputTextArea.getText());
							setSuccess();	// If no exception thrown
							
						} catch (Exception e) {
							setFailure();
							error("Error: " + e);
							e.printStackTrace();
						}						
					}
					
				}.start();
			}
		});

		inputTextArea = new JTextArea(6, DISPLAY_COLUMNS);
		inputTextArea.setBorder(BorderFactory.createTitledBorder("SQL Command"));
		
		setActive();
		
		JTextArea outputTextArea = new JTextArea(20, DISPLAY_COLUMNS);
		outputTextArea.setBorder(BorderFactory.createTitledBorder("SQL Results"));
		
		mainWindow.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
					new JScrollPane(inputTextArea), 
					new JScrollPane(outputTextArea)), BorderLayout.CENTER);
		
		mainWindow.add(bar, BorderLayout.SOUTH);

		out = new PrintWriter(new TextAreaWriter(outputTextArea));
		
		mainWindow.pack();
		UtilGUI.monitorWindowPosition(mainWindow, p);
		mainWindow.setVisible(true);
	}
	
	/**
	 * Set the bar to green, used only at the beginning
	 */
	void setSuccess() {
		bar.setValue(bar.getMaximum());
		bar.setForeground(Color.GREEN);
		bar.repaint();
	}
	/**
	 * Set the bar to red, used when a test fails or errors.
	 */
	void setFailure() {
		bar.setValue(bar.getMaximum());
		bar.setForeground(Color.RED);
		bar.repaint();
	}
	/**
	 * Set the bar to neutral
	 */
	void setActive() {
		bar.setValue(bar.getMaximum());
		bar.setForeground(mainWindow.getBackground());
		bar.repaint();
	}
	
	/**
	 * The obvious error handling.
	 * @param mesg
	 */
	void error(String mesg) {
		setFailure();
		JOptionPane.showMessageDialog(mainWindow, mesg, "Oops", JOptionPane.ERROR_MESSAGE);
	}

}
