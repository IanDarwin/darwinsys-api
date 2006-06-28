package com.darwinsys.swingui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PrefsPanelTest  {
	public static void main(String[] args) {
		JFrame jf = new JFrame("PrefsPanelTest");
		PrefsPanel p = new PrefsPanel();
		jf.add(p);
		p.add(PrefsPanel.PredefinedPanel.ONE_COLOR);
		p.add(PrefsPanel.PredefinedPanel.ONE_FONT);
		p.add(PrefsPanel.PredefinedPanel.FGBG_COLOR);
		JPanel test = new JPanel();
		p.add(test, "Test", null);
		jf.pack();
		jf.setVisible(true);
	}
}
