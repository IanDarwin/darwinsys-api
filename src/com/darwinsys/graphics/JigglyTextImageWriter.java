package com.darwinsys.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * Write a ransom-note-style string to an OutputStream as
 * an Image file (jpeg for now). Designed for use in
 * e.g., a servlet for verifying that a user is a person
 * and not a spambot.
 * But, deliberately, does not know about the Servlet API.
 */
public class JigglyTextImageWriter implements Serializable {

	private final int width, height;
	private final Font font;
	private int nextColor;
	private final Random random = new Random();

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
		g.setColor(Color.WHITE);
		g.fillRect(0,0, width, height);

		// Fill the background with hashes
		// XXX maybe use fanOfLines here
		g.setColor(Color.GRAY);
		g.drawLine(0, height / 2, width-1, height / 2);

		// Draw the text
		char[] msgChars = msg.toCharArray();
		int y = height / 2, x = 0;
		for (int i = 0; i < msgChars.length; i++) {
			g.setColor(randomColor());
			int xPermute = random.nextInt(5);
			int yPermute = random.nextInt(5);
			// XXX pick a rotation transform
			g.drawChars(msgChars, i, 1, (x += 20) + xPermute, y + yPermute);
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
		return colors[nextColor++ % colors.length];
	}
}
