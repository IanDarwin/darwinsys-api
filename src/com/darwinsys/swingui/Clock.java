package com.darwinsys.swingui;

import java.awt.*;
import java.text.*;
import java.util.*;

/** A simple Clock */
public class Clock extends javax.swing.JComponent {
	protected DecimalFormat tflz, tf;
	protected boolean done = false;

	public Clock() {
		new Thread(new Runnable() {
			public void run() {
				while (!done) {
					Clock.this.repaint();	// request a redraw
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e){ /* do nothing*/ }
				}
			}
		}).start();
		tf = new DecimalFormat("#0");
		tflz = new DecimalFormat("00");
	}

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
