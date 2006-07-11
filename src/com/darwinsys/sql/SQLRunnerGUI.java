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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
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

import com.darwinsys.genericui.SuccessFailureUI;
import com.darwinsys.io.TextAreaWriter;
import com.darwinsys.swingui.SuccessFailureBarSwing;
import com.darwinsys.swingui.UtilGUI;
import com.darwinsys.util.Verbosity;

/**
 * A simple GUI to run one set of commands.
 */
public class SQLRunnerGUI  {
	
	private static final int DISPLAY_COLUMNS = 70;

	final Preferences p = Preferences.userNodeForPackage(SQLRunnerGUI.class);
	
	final SuccessFailureUI bar;
	
	final JFrame mainWindow;
	
	final JTextArea inputTextArea, outputTextArea;
	
	final JButton runButton;
	
	final PrintWriter out;
	
	private SQLRunnerErrorHandler eHandler = new SQLRunnerErrorHandler() {

		public void handleError(Exception e) {
			
				JOptionPane.showMessageDialog(mainWindow, 
					"<html><p>Error: <font color='red'>" + e,
					"Oops", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
		}
		
	};
	
	/**
	 * Allow the Application to provide its own error handler.
	 * @param eHandler
	 */
	public void setErrorHandler(SQLRunnerErrorHandler eHandler) {
		this.eHandler = eHandler;
	}
	
	/**
	 * Main method; ignores arguments.
	 */
	public static void main(String[] args) {
		String config = null;
		if (args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				if ("-c".equals(arg) && args.length > i) {
					config = args[i+1];
				}
			}
		}
		SQLRunnerGUI prog = new SQLRunnerGUI();
		if (config != null) {
			prog.setConfig(config);
		}
	}
	
	final List<Object> connections;
	final JComboBox connectionsList;
	
	/**
	 * Set the selected Configuration Object in the Connections chooser
	 * from a given Configuration Name passed as a String.
	 * @param config The chosen name.
	 */
	private void setConfig(String config) {
		if (config == null) {
			throw new NullPointerException("Configuration name may not be null");
		}
		Iterator<Object> it = connections.iterator();
		while (it.hasNext()) {
			Object configListItem = it.next();
			if (config.equals(configListItem.toString())) {
				connectionsList.setSelectedItem(configListItem);
				return;
			}
		}
		System.err.printf("Warning: Configuration %s not found", config);
	}

	/**
	 * Constructor
	 */
	public SQLRunnerGUI() {
		mainWindow = new JFrame("SQLRunner");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Container controlsArea = new JPanel();
		mainWindow.add(controlsArea, BorderLayout.NORTH);
		
		connections = ConnectionUtil.getInstance().getConfigurations();
		connectionsList = new JComboBox(connections.toArray(new String[connections.size()]));
		controlsArea.add(new JLabel("Connection"));
		controlsArea.add(connectionsList);
		
		final JComboBox inTemplateChoice = new JComboBox();
		// XXX Of course these should come from Properties and be editable...
		inTemplateChoice.addItem("Input Template:");
		inTemplateChoice.addItem("SELECT * from TABLE where x = y");
		inTemplateChoice.addItem("INSERT into TABLE(col,col) VALUES(val,val)");
		inTemplateChoice.addItem("UPDATE TABLE set x = y where x = y");
		controlsArea.add(inTemplateChoice);
		
		final JButton inTemplateButton = new JButton("Apply Template");
		controlsArea.add(inTemplateButton);
		
		final JComboBox modeList = new JComboBox();
		for (OutputMode mode : OutputMode.values()) {
			modeList.addItem(mode);
		}
		controlsArea.add(new JLabel("Output Format:"));
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
							String command = inputTextArea.getText().trim();
							if (command == null || command.length() == 0)
							runButton.setEnabled(false);
							conn =  ConnectionUtil.getInstance().getConnection((String)connectionsList.getSelectedItem());
							SQLRunner.setVerbosity(Verbosity.QUIET);
							SQLRunner prog = new SQLRunner(conn, null, "t");
							prog.setOutputFile(out);
							prog.setOutputMode((OutputMode) modeList.getSelectedItem());							
							bar.reset();							
							prog.runStatement(command);
							bar.showSuccess();	// If no exception thrown							
						} catch (Exception e) {
							bar.showFailure();
							eHandler.handleError(e);							
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
		
		
		outputTextArea = new JTextArea(20, DISPLAY_COLUMNS);
		JScrollPane outputAreaScrollPane = new JScrollPane(outputTextArea);
		outputAreaScrollPane.setBorder(BorderFactory.createTitledBorder("SQL Results"));
		
		inTemplateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (inTemplateChoice.getSelectedIndex() == 0) {
					return;
				}
				inputTextArea.setText((String)inTemplateChoice.getSelectedItem());				
			}			
		});
		
		JButton clearOutput = new JButton("Clear Output");
		clearOutput.addActionListener(new ActionListener() {		    
		    public void actionPerformed(ActionEvent e) {
		        outputTextArea.setText("");
                bar.reset();
		    }	    
		});
        controlsArea.add(clearOutput);
        
        mainWindow.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
					inputAreaScrollPane, 
					outputAreaScrollPane), BorderLayout.CENTER);
		

		out = new PrintWriter(new TextAreaWriter(outputTextArea));
        
		bar = new SuccessFailureBarSwing(mainWindow.getBackground(), 400, 20);
		bar.reset();
		mainWindow.add((JComponent)bar, BorderLayout.SOUTH);
		
		mainWindow.pack();
		UtilGUI.monitorWindowPosition(mainWindow, p);
		mainWindow.setVisible(true);
	}
	
}
