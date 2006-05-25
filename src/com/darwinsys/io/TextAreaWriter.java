package com.darwinsys.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Packages up the code to let you "print" to a JTextArea.
 */
public class WriterToJTextArea {

	private PipedInputStream is;
	private PipedOutputStream os;
	private PrintWriter ps;

	public WriterToJTextArea(final JTextArea results) throws IOException {
		// Create a pair of Piped Streams.
		is = new PipedInputStream();
		os = new PipedOutputStream(is);

		final BufferedReader iis = new BufferedReader(new InputStreamReader(is, "ISO8859_1"));
		ps = new PrintWriter(os);

		// Construct and start a Thread to copy data from "is" to "os".
		new Thread() {
			public void run() {
				try {
					String line;
					while ((line = iis.readLine()) != null) {
						results.append(line);
						results.append("\n");
					}
				} catch(IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(results,
						"*** Input or Output error ***\n" + e,
						"Error",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		}.start();
	}
	
	public PrintWriter getWriter() {
		return ps;
	}
}
