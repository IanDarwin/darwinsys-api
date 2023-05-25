package com.darwinsys.workflow;

import javax.swing.JOptionPane;
import java.util.Optional;

public class BreakNagger implements Runnable {

	public static void main(String[] args) {
		new BreakNagger(args.length > 0 ? args[0] : DEFAULT_MESG).run();
	}

	private int intervalMinutes = 60; // XXX Should be configurable
	private static String DEFAULT_MESG = "Take a break!";
	private String message;
	private boolean done;

	BreakNagger(String msg) {
		this.message = msg;
	}

	public void run() {
		while (!done) {
			try {
				Thread.sleep(intervalMinutes * 60 * 1000);
				nag();
			} catch (InterruptedException e) {
				/*CANTHAPPEN*/
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private void nag() {
		int ret = JOptionPane.showOptionDialog(null, message,
				"BreakNagger",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, new String[] { "OK", "Quit"}, "OK");
		if (ret == 1) {
			System.exit(0);
		}
	}
}
