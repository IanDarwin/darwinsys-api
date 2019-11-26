package com.darwinsys.saw;	// "Sharpen the Saw", e.g., look after yourself

// Move every half hour - don't get sedentary

import javax.swing.*;

public class MoveTimer {
	public static void main(String[] args) throws Exception {
		while (true) {
			Thread.sleep (30*60 * 1000);
			JOptionPane.showMessageDialog(null, "Move it");
		}
	}
}



