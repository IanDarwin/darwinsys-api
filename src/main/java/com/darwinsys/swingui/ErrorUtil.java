package com.darwinsys.swingui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Convenience class for fielding Exceptions in a Swing App.
 * Displays exceptions in a JOptionPane, and follows chained
 * exceptions, both the 1.x SQLException.getNextExeption() and
 * the new 1.4 Exception.getCause().
 * <p>
 * The user (will be able to) press a Details... button to see the
 * traceback in a dialog; tracebacks are <b>not</b> displayed unless
 * the user requests them.
 * @author Ian Darwin
 */
public class ErrorUtil {

	// static { System.out.println("ErrorUtil loaded"); }

	/** The button options for the ultimate (or only) Excepton */
	final static String[] choicesNoMore = { "OK", "Details..." };

	/** The button options for any non-ultimate Exception */
	final static String[] choicesMore = { "OK", "Details...", "Next" };

	/** Secondary dialog for the "Details..." button */
	static DetailsDialog detailsDialog;

	/** Public no-arg constructor for those who like simple instantiation. */
	public ErrorUtil() {
		// Nothing to do
	}

	/** Convenience routine for use with AWT's dispatch thread; this is the old,
	 * never-supported and now often-doesn't-work method, but the code is still here.
	 * Usage:
	 * <pre>
	 * System.setProperty("sun.awt.exception.handler", "com.darwinsys.swingui.ErrorUtil");
	 * </pre>
	 */
	public void handle(Throwable th) {
		//System.out.println("handle() called with " + th.getClass().getName());
		showExceptions(null, th);
	}
		
	// BEGIN main
	/** Show the given Exception (and any nested Exceptions) in JOptionPane(s).
	 */
	public static void showExceptions(Component parent, Throwable theExc) {

		Throwable next = null;

		do {
			String className = theExc.getClass().getName();
			String message = className;

			if (theExc instanceof SQLException) {
				SQLException sexc = (SQLException)theExc;
				message += "; code=" + sexc.getErrorCode();
				next = sexc.getNextException();
			}
				else next = theExc.getCause();   // Comment out if < JDK 1.4

			String[] choices = next != null ? choicesMore : choicesNoMore;

			/* Show the Dialog! */
			int response = JOptionPane.showOptionDialog(
				parent,
				message,
				className,	 						// title
				JOptionPane.YES_NO_CANCEL_OPTION,	// icontType
				JOptionPane.ERROR_MESSAGE,			// messageType
				null,								// icon
				choices,							// options
				choices[0]							// default
				);

			if (response == 0)			// "OK"
				return;
			if (response == 1) {		// "Details"
				// show ANOTHER JDialog with a JTextArea of printStackTrace();
				if (detailsDialog == null) // first time, lazy creation
					detailsDialog = new DetailsDialog((JFrame)parent);
				detailsDialog.showStackTrace(theExc);
			}
			// else resp = 2, "Next", let it fall through:

			theExc = next;

		} while (next != null);
	}

	/** JDialog class to display the details of an Exception */
	protected static class DetailsDialog extends JDialog {

		private static final long serialVersionUID = -4779441441693785664L;
		JButton ok;
		JTextArea text;
		/** Construct a DetailsDialog given a parent (Frame/JFrame) */
		DetailsDialog(JFrame parent) {
			super(parent);
			Container cp = getContentPane();
			text = new JTextArea(40, 40);
			cp.add(text, BorderLayout.CENTER);
			ok = new JButton("Close");
			cp.add(ok, BorderLayout.SOUTH);
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					dispose();
				}
			});
			pack();
		}

		/** Display the stackTrace from the given Throwable in this Dialog. */
		void showStackTrace(Throwable exc) {
			CharArrayWriter buff = new CharArrayWriter();
			PrintWriter pw = new PrintWriter(buff);
			exc.printStackTrace(pw);
			pw.close();
			text.setText(buff.toString());
			setVisible(true);
		}
	}
	// END main
}
