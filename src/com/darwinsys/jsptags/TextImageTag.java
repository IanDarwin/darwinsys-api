package com.darwinsys.jsptags;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/** TextImageServlet - draw text as a graphic, so spam twerps can't robotically
 * harvest it, and can also be used in web forms to prevent robots from submitting
 * the form.
 * <p><b>Must</b> be used as a JSP Tag: if you made this a servlet, the parameters
 * to it would be visible in the HTML (think about it...).
 */
public class TextImageTag extends TagSupport {

	private static final long serialVersionUID = 3257567299946231088L;
	private static int W = 300, H = 200;
	private static String imgDir = "imagetmp";
	private String text;

	/** Invoked at the end tag boundary, does the work */
	public int doStartTag() throws JspException  {
		try {
			JspWriter out = pageContext.getOut();
			if (text == null) {
				out.println("<b>Error: text not set");
			}
	
			// pre-alpha: use the text string as the file name: presto, simple caching.
			// XXX Useless!! must create random filename, save in (static) Map(text,filename).

			File dir = new File(imgDir);
			dir.mkdir();
			File file = new File(dir, text);
			if (!file.exists()) {
				// Create an Image
				BufferedImage img =
					new BufferedImage(W, H,
					BufferedImage.TYPE_INT_RGB);
		
				// Get the Image's Graphics, and draw.
				Graphics2D g = img.createGraphics();
		
				// In real life this would call some charting software...
				g.setColor(Color.white);
				g.fillRect(0,0, W, H);
				g.setColor(Color.green);
				g.fillOval(100, 75, 50, 50);
				g.drawString(text, 10, 25);
		
				// Write the output
				System.out.println("Creating output file " + file);
				OutputStream os = new FileOutputStream(file);
				ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		
				if (!ImageIO.write(img, "jpeg", ios)) {
					System.err.println("Boo hoo, failed to write JPEG");
				}
				ios.close();
				os.close();
			}
			out.println("<img src='" + file.getName() + "' alt='text image/>");
			out.flush();
		} catch (IOException ex) {
			System.err.println("TextImageServleg.doEndTag: caught " + ex);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
