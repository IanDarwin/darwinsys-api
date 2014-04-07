package com.darwinsys.swingui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

/** Display one of those standard Calendar Page icons with
 * Weekday, Day and Month. Can be used as the Icon in a
 * JButton. Can include or exclude an updating Clock at the top
 * (invoke constructor with value of true to include).
 * However, it should be excluded when using as an Icon, and 
 * true when using as a Component.
 * @author	Ian Darwin, http://www.darwinsys.com
 */
public class CalIcon extends JComponent implements Icon {

	private static final long serialVersionUID = -75900943452653673L;
	/** The size shalle be 64x64. */
	protected final int SIZE = 64;
	protected final Dimension d = new Dimension(SIZE, SIZE);
	/** The size of the inner white box */
	protected final int RBW=40, RBH=40;
	/** The x location of the inner box */
	protected final int RBX;
	/** The y location of the inner box */
	protected final int RBY;
	/** Our Calendar */
	protected Calendar myCal;
	/** True if user wants the time shown */
	protected boolean showTime = true;
	/** The Clock to show the time, if showTime */
	protected Clock clock;
	/** Font for displaying the time */
	protected Font dayNumbFont;
	/** FontMetrics for displaying the time */
	protected FontMetrics dayNumbFM;
	/** Font for displaying the time */
	protected Font dayNameFont;
	/** FontMetrics for displaying the time */
	protected FontMetrics dayNameFM;
	/** Font for displaying the time */
	protected Font monNameFont;
	/** FontMetrics for displaying the time */
	protected FontMetrics monNameFM;

	/** Construct the object with default arguments */
	public CalIcon(boolean showT) {
		this(Calendar.getInstance(), showT);
	}

	/** Construct the object with a Calendar object */
	public CalIcon(Calendar c, boolean showT) {
		super();
		showTime = showT;
		myCal = c;

		setLayout(null);			// we don't need another layout, ...

		if (showTime) {
			// System.err.println("Constructing and adding Clock");
			clock = new Clock();
			add(clock);
			clock.setBounds(0, 2, SIZE, 10);
			// clock.setBackground(Color.black);
			// clock.setForeground(Color.green);
			RBY = d.height - (RBH+(showTime?12:0)/2);
		} else {
			RBY = 6;
		}
		RBX = 12;	// raised box x offset
		// System.err.println("RBX, RBY = " + RBX + "," + RBY);

		dayNumbFont = new Font("Serif",     Font.BOLD, 20);
		dayNumbFM = getFontMetrics(dayNumbFont);
		dayNameFont = new Font("SansSerif", Font.PLAIN, 10);
		dayNameFM = getFontMetrics(dayNameFont);
		monNameFont = new Font("SansSerif", Font.ITALIC, 10);
		monNameFM = getFontMetrics(monNameFont);
	}

	/** Days of the week */
	public String[] days = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
	public String[] mons = { 
		"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
		"JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
	};

	/** Paint: draw the calendar page in the JComponent.
	 * Delegates most work to paintIcon().
	 */
	public void paint(Graphics g) {

		paintIcon(this, g, 0, 0);
	}

	/** paintIcon: draw the calendar page. */
	public void paintIcon(Component c, Graphics g, int x, int y) {

		// Allow clock to get painted (voodoo magic)
		if (showTime)
			super.paint(g);

		// Outline it.
		g.setColor(Color.black);
		g.draw3DRect(x, y, d.width-2, d.height-2, true);

		// Show the date: First, a white page with a drop shadow.
		g.setColor(Color.gray);
		g.fillRect(x + RBX+3, y + RBY+3, RBW, RBH);
		g.setColor(Color.white);
		g.fillRect(x + RBX, y + RBY, RBW, RBH);

		// g.setColor(getForeground());
		g.setColor(Color.black);

		String s = days[myCal.get(Calendar.DAY_OF_WEEK)-1];
		g.setFont(dayNameFont);
		int w = dayNameFM.stringWidth(s);
		g.drawString(s, x + RBX+((RBW-w)/2), y + RBY+10);

		s = Integer.toString(myCal.get(Calendar.DAY_OF_MONTH));
		g.setFont(dayNumbFont);
		w = dayNumbFM.stringWidth(s);
		g.drawString(s, x + RBX+((RBW-w)/2), y + RBY+25);

		s = mons[myCal.get(Calendar.MONTH)];
		g.setFont(monNameFont);
		w = monNameFM.stringWidth(s);
		g.drawString(s, x + RBX+((RBW-w)/2), y + RBY+35);
	}

	public int getIconWidth() { return SIZE; }
	public int getIconHeight() { return SIZE; }

	public Dimension getPreferredSize() {
		return d;
	}
	public Dimension getMinimumSize() {
		return d;
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("Calendar");
		Container cp = jf.getContentPane();
		cp.setLayout(new GridLayout(0,1,5,5));
		CalIcon c = new CalIcon(true);
		cp.add(c);
		JButton j = new JButton("As Icon", new CalIcon(false));
		cp.add(j);
		jf.pack();
		jf.setVisible(true);
	}
}
