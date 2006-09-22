package com.darwinsys.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * Write a ransom-note-style string to an OutputStream as
 * an Image file (jpeg for now). Designed for use in
 * e.g., a servlet for verifying that a user is a person
 * and not a spambot.
 * But, deliberately, does not know about the Servlet API.
 */
public class JigglyTextImageWriter {

	int width, height;
	Font font;
	private int nc;

	public JigglyTextImageWriter(Font font, int width, int height) {
		super();
		this.font = font;
		this.width = width;
		this.height = height;
	}

	/** generate the image and output it. */
	public void write(String msg, OutputStream os) throws IOException{

		BufferedImage img =
			new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

		// Get the Image's Graphics context
		Graphics2D g = img.createGraphics();
		g.setFont(font);

		// Start with a clear screen
		g.setColor(Color.white);
		g.fillRect(0,0, width, height);

		// Fill the background with hashes
		// XXX maybe use fanOfLines here

		// Draw the text
		char[] msgChars = msg.toCharArray();
		int y = 100, x = 0;
		for (int i = 0; i < msgChars.length; i++) {
			g.setColor(randomColor());
			// XXX permute X a bit
			// XXX pick a rotation transform
			g.drawChars(msgChars, i, 1, x += 20, y);
		}

		// Write the output
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);

		if (!ImageIO.write(img, "jpeg", ios)) {
			throw new IOException("Write Failed");
		}
	}

	private static Color[] colors = {
			Color.BLUE, Color.RED, Color.BLACK, Color.GREEN
	};


	private Color randomColor() {
		return colors[nc++ % colors.length];
	}
}