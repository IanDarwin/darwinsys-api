/* Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 2004-2006.
 * $Id$
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.darwinsys.sql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.darwinsys.io.TextAreaWriter;
import com.darwinsys.swingui.UtilGUI;
import com.darwinsys.util.Verbosity;

/**
 * A simple GUI to run one set of commands.
 */
public class SQLRunnerGUI  {
	
	private static final int DISPLAY_COLUMNS = 70;

	final Preferences p = Preferences.userNodeForPackage(SQLRunnerGUI.class);
	
	@SuppressWarnings("serial")
	final JComponent bar = new JComponent() {
	    public void paint(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    };
	
	final JFrame mainWindow;
	
	final JTextArea inputTextArea, outputTextArea;
	
	final JButton runButton;
	
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

		runButton = new JButton("Run");
		controlsArea.add(runButton);
		runButton.addActionListener(new ActionListener() {
			
            /** Called each time the user presses the Run button */
			public void actionPerformed(ActionEvent evt) {
				
				// Run this under a its own Thread, so we don't block the EventDispatch thread...
				new Thread() {
                    Connection conn;
					public void run() {
						try {
							runButton.setEnabled(false);
							conn =  ConnectionUtil.getConnection((String)connectionsList.getSelectedItem());
							SQLRunner.setVerbosity(Verbosity.QUIET);
							SQLRunner prog = new SQLRunner(conn, null, "t");
							prog.setOutputFile(out);
							prog.setOutputMode((OutputMode) modeList.getSelectedItem());
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									setNeutral();
								}
							});
							prog.runStatement(inputTextArea.getText());
							setSuccess();	// If no exception thrown							
						} catch (Exception e) {
							setFailure();
							error("<html><p>Error: <font color='red'>" + e);
							e.printStackTrace();
						} finally {
							if (conn != null) {
							    try {
							        conn.close();
							    } catch (SQLException e) {
							        // We just don't care at this point....
							    }                     
                            }
							runButton.setEnabled(true);
						}
					}
					
				}.start();
			}
		});
        

		inputTextArea = new JTextArea(6, DISPLAY_COLUMNS);
		JScrollPane inputAreaScrollPane = new JScrollPane(inputTextArea);
		inputAreaScrollPane.setBorder(BorderFactory.createTitledBorder("SQL Command"));
		
		setNeutral();
		
		outputTextArea = new JTextArea(20, DISPLAY_COLUMNS);
		JScrollPane outputAreaScrollPane = new JScrollPane(outputTextArea);
		outputAreaScrollPane.setBorder(BorderFactory.createTitledBorder("SQL Results"));
		
		JButton clearOutput = new JButton("Clear Output");
		clearOutput.addActionListener(new ActionListener() {		    
		    public void actionPerformed(ActionEvent e) {
		        outputTextArea.setText("");
                setNeutral();
		    }	    
		});
        controlsArea.add(clearOutput);
        
        mainWindow.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
					inputAreaScrollPane, 
					outputAreaScrollPane), BorderLayout.CENTER);
		
		mainWindow.add(bar, BorderLayout.SOUTH);

		out = new PrintWriter(new TextAreaWriter(outputTextArea));
        
        bar.setPreferredSize(new Dimension(400, 20));
		
		mainWindow.pack();
		UtilGUI.monitorWindowPosition(mainWindow, p);
		mainWindow.setVisible(true);
	}
	
	/**
	 * Set the bar to green
	 */
	void setSuccess() {
		bar.setBackground(Color.GREEN);
		bar.repaint();
	}
	
	/**
	 * Set the bar to red, used when a test fails or errors.
	 */
	void setFailure() {
		bar.setBackground(Color.RED);
		bar.repaint();
	}
	
	/**
	 * Set the bar to neutral
	 */
	void setNeutral() {
		bar.setBackground(mainWindow.getBackground());
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
