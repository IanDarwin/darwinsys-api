/** Display one of those standard Calendar Page icons with
 * Weekday, Day and Month.
 * <P>TODO:
 * <BR>1) Test with/without showTime.
 * <BR>2) MouseListener to call a callback on single click.
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */

import java.awt.*;
import java.util.*;

public class CalIcon extends javax.swing.JComponent {
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
	public CalIcon() {
		super();
		setLayout(null);			// we don't need another layout, ...
		myCal = Calendar.getInstance();

		if (showTime) {
			// System.err.println("Constructing and adding Clock");
			clock = new Clock();
			add(clock);
			clock.setBounds(0, 2, SIZE, 10);
			// clock.setBackground(Color.black);
			// clock.setForeground(Color.green);
		}
		dayNumbFont = new Font("Serif",     Font.BOLD, 20);
		dayNumbFM = getFontMetrics(dayNumbFont);
		dayNameFont = new Font("SansSerif", Font.PLAIN, 10);
		dayNameFM = getFontMetrics(dayNameFont);
		monNameFont = new Font("SansSerif", Font.ITALIC, 10);
		monNameFM = getFontMetrics(monNameFont);
		RBX = 12;	// raised box x offset
		RBY = d.height - (RBH+(showTime?12:0)/2);
		// System.err.println("RBX, RBY = " + RBX + "," + RBY);
	}

	/** Construct the object with a Calendar object */
	public CalIcon(Calendar c) {
		this();
		myCal = c;
	}

	/** Days of the week */
	public String[] days = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
	public String[] mons = { 
		"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
		"JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
	};

	/** Paint: draw the calendar page. */
	public void paint(Graphics g) {
		String s;
		int w;

		// Allow clock to get painted (voodoo magic)
		if (showTime)
			super.paint(g);

		// Outline it.
		g.setColor(Color.black);
		g.draw3DRect(0, 0, d.width-2, d.height-2, true);

		// Show the date: First, a white page with a drop shadow.
		g.setColor(Color.gray);
		g.fillRect(RBX+3, RBY+3, RBW, RBH);
		g.setColor(Color.white);
		g.fillRect(RBX, RBY, RBW, RBH);

		g.setColor(getForeground());

		s = days[myCal.get(Calendar.DAY_OF_WEEK)-1];
		g.setFont(dayNameFont);
		w = dayNameFM.stringWidth(s);
		g.drawString(s, RBX+((RBW-w)/2), RBY+10);

		s = Integer.toString(myCal.get(Calendar.DAY_OF_MONTH));
		g.setFont(dayNumbFont);
		w = dayNumbFM.stringWidth(s);
		g.drawString(s, RBX+((RBW-w)/2), RBY+25);

		s = mons[myCal.get(Calendar.MONTH)];
		g.setFont(monNameFont);
		w = monNameFM.stringWidth(s);
		g.drawString(s, RBX+((RBW-w)/2), RBY+35);
	}

	public Dimension getPreferredSize() {
		return d;
	}
	public Dimension getMinimumSize() {
		return d;
	}
}
