package com.darwinsys.swingui;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
 * @version $Id$
 */
public class ErrorUtil {

	/** The button options for the ultimate (or only) Excepton */
	final static String[] choicesNoMore = { "OK", "Details..." };

	/** The button options for the any non-ultimate) Excepton */
	final static String[] choicesMore = { "OK", "Details...", "Next" };

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

			/* Use the form:<br>
			 * showOptionDialog(Component parentComponent, Object message, 
			 * String title, int optionType, int messageType, Icon icon, 
			 * Object[] options, Object initialValue) 
			 */
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

			if (response == 0)
				return;
			// if (response == 1)
				// show a JDialog with a JTextArea of printStackTrace();
			// else resp = 2, let it fall through:

			theExc = next;

		} while (next != null);
	}
}
