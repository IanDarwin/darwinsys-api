package com.darwinsys.swingui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/** Test the "sun.awt.exception.handler" trick to invoke swingui.ErrorUtil
 */
public class ErrorUtilCatchTest extends JFrame {

	private static final long serialVersionUID = -3417837022502498981L;

	public ErrorUtilCatchTest() {
		super("GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cp = getContentPane();
		JButton bx = new JButton("Throw!");
		bx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				throw new IllegalArgumentException("foo");
			}
		});
		cp.add(bx, BorderLayout.CENTER);
		JButton cl = new JButton("Close");
		cl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		cp.add(cl, BorderLayout.SOUTH);
		setBounds(200, 200, 200, 100);
	}

	public static void main(String[] args) {
		System.setProperty("sun.awt.exception.handler",
						   "com.darwinsys.swingui.ErrorUtil");
		new ErrorUtilCatchTest().setVisible(true);
	}
}


