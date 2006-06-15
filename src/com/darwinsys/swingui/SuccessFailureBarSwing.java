package com.darwinsys.swingui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.darwinsys.genericui.SuccessFailureUI;

/**
 * A Swing implementation of SuccessFailureUI; vaguely like a ProgressBar but
 * only shows Green (success), Red (failure), or neutral.
 */
public class SuccessFailureBarSwing 
	extends JComponent 
	implements SuccessFailureUI {

	/** Needed for Serializable */
	private static final long serialVersionUID = 4955244957447902337L;

	protected Color neutral;
	int width;
	int height;
	
	/**
	 * Construct a Swing SuccFailureUI.
	 * @param neutral The Color to use when reset, often parent.getBackground() is a good choice.
	 * @param width The preferred width for this Component
	 * @param height The preferred height for this Component
	 */
	public SuccessFailureBarSwing(Color neutral, int width, int height) {
		super();
		this.neutral = neutral;
		this.width = width;
		this.height = height;
	}

	public void showSuccess() {
		setBackground(Color.GREEN);
		repaint();
	}

	public void showFailure() {
		setBackground(Color.RED);
		repaint();
	}

	public void reset() {
		setBackground(neutral);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	

	@Override
	public void paintComponent(Graphics g) {
		Color oldColor = g.getColor();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        // "You must not make permanent changes to the Graphics object...
        g.setColor(oldColor);
    }
}
