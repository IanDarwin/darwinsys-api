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
	/** There must be a folder of this name in the web app
	 * that is writable by the app server OS account.
	 */
	private static String imgDir = "tmp";
	private String text;

	/** Invoked at the end tag boundary, does the work */
	public int doStartTag() throws JspException  {
		OutputStream os = null;
		ImageOutputStream ios = null;
		try {
			try {
				JspWriter out = pageContext.getOut();
				if (text == null) {
					out.println("<b>Error: text not set");
				}

				// XXX Should save filename in (static) Map(text,filename).
				File fileForDir = new File(imgDir);
				File file = File.createTempFile("img", "img", fileForDir);

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
				System.out.println(
						"TextImageTag.doStartTag(): Creating output file " + file);
				os = new FileOutputStream(file);
				ios = ImageIO.createImageOutputStream(os);

				if (!ImageIO.write(img, "jpeg", ios)) {
					throw new IOException("Failed to write JPEG to " + file);
				}

				out.println("<img src='" + file.getName() + "' alt='text image/>");
				out.flush();
			} finally {
				if (ios != null)
					ios.close();
				if (os != null)
					os.close();
			}
		} catch (IOException ex) {
			System.err.println("TextImageServleg.doStartTag: caught " + ex);
		}
		return EVAL_BODY_INCLUDE;
	}

	// Attribute set/get

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
