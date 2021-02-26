package com.darwinsys.swingui;

import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * A simple Preferences Panel
 * @author Ian Darwin
 */
public class PrefsPanel extends JPanel implements Serializable {

	private static final long serialVersionUID = 8721512919301817567L;

	public enum PredefinedPanel {
		ONE_FONT,
		ONE_COLOR,
		FGBG_COLOR,
	};
	
	public static final JPanel OneFontPanel = new JPanel();
	public static final JPanel OneColorPanel = new JPanel();
	public static final JPanel FgBgColorPanel = new JPanel();
	
	private JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
	
	/**
	 * @param isDoubleBuffered True to enable double-buffering
	 */
	public PrefsPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		add(tabPane);
	}

	/**
	 * Construct a PrefsPanel
	 */
	public PrefsPanel() {
		this(false);
	}

	/**
	 * @param comp The component to add
	 * @param labelKey The labelkey with which to add it
	 * @param image The icon to display
	 */
	public void add(JComponent comp, String labelKey, Icon image) {
		tabPane.add(labelKey, comp);
	}
	
	/**
	 * @param which Which panel to use
	 */
	public void add(PredefinedPanel which) {
		switch (which) {
		case ONE_FONT:
			add(OneFontPanel, "font", null);
			break;
		case ONE_COLOR:
			add(OneColorPanel, "color", null);
			break;
		case FGBG_COLOR:
			add(FgBgColorPanel, "colors", null);
			break;
		}
	}
}
