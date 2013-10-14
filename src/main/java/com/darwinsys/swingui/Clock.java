package com.darwinsys.swingui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/** A simple Clock */
public class Clock extends javax.swing.JComponent {

	private static final long serialVersionUID = -3553831092792561909L;
	protected NumberFormat tflz = new DecimalFormat("00"),
		tf = new DecimalFormat("#0");;
	protected boolean done = false;
	Thread ticker;

	public Clock() {
		ticker = new Thread(new Runnable() {
			public void run() {
				while (!done) {
					Clock.this.repaint();	// request a redraw
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e){ 
						/* do nothing*/
					}
				}
			}
		});
	}

	/**
	 * Start the Clock running.
	 */
	public void start() {
		ticker.start();
	}

	/**
	 * Stop the clock (err, permanently).
	 */
	public void stop() {
		done = true;
	}
 
	/* paint() - get current time and draw (centered) in Component. */ 
	public void paint(Graphics g) {
		Calendar myCal = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(tf.format(myCal.get(Calendar.HOUR)));
		sb.append(':');
		sb.append(tflz.format(myCal.get(Calendar.MINUTE)));
		sb.append(':');
		sb.append(tflz.format(myCal.get(Calendar.SECOND)));
		String s = sb.toString();
		FontMetrics fm = getFontMetrics(getFont());
		int x = (getSize().width - fm.stringWidth(s))/2;
		// System.out.println("Size is " + getSize());
		g.drawString(s, x, 10);
	}

	public Dimension getPreferredSize() {
		return new Dimension(100, 30);
	}

	public Dimension getMinimumSize() {
		return new Dimension(50, 10);
	}
}
