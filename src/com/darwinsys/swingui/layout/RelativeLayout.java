package com.darwinsys.swingui.layout;

import java.awt.*;
import java.util.*;

/** 
 * <p>
 * RelativeLayout, a Relative Layout Manager for Java J2SE.
 * Mainly for porting tired old code that uses x,y locations.
 * You really can't just assign x,y locations to components 
 * in Java Java J2SE - it breaks badly when the user resizes (and you can
 * <em>not</em> mandate that the user can't resize you -- see any book
 * on UI design for <em>that</em> little discussion -- and can also look
 * bad due to resolution independance.  Symantec Cafe 1.x, for example,
 * used to spit out unfortunate (and unmaintainable) code like this:
 * <pre>
 *       setLayout(null);
 *       setSize(331,241);
 *       label1=new Label("Info Applet", Label.CENTER);
 *       add(label1);
 *       label1.setBounds(91,19,107,15);
 * </pre>
 * </p> <p>
 * <em>Bleaarrgghh!!!</em>
 * To make it work properly at all resolutions and survive
 * user-initiated resize actions, change it to
 * <pre>
 *	setLayout(new RelativeLayout(331,241,false);
 *	label1=new Label("Info Applet", Label.CENTER);
 *	add("91,19", label1);
 * </pre>
 * Note that it's actually <EM>less</EM> work to get it right.
 * Symantec, Microsoft, and others, please take note!</p>
 * @author Ian Darwin, http://www.darwinsys.com/
 */

public class RelativeLayout implements LayoutManager {
	/** requested absolute width of canvas */
	protected int reqWid;
	/** requested absolute height of canvas */
	protected int reqHgt;

	/** actual size width when laid out */
	protected int curWid;
	/** actual size height when laid out */
	protected int curHgt;

	/** to track Components added by named add form. */
	protected Vector curComps = new Vector();

	/**
	 * Constructs an RelativeLayout, given original hard-coded size of panel.
	 */
	public RelativeLayout(int wid, int ht) {
		this.reqWid = wid;
		this.reqHgt = ht;
	}

	/**
	 * Called by AWT when the user uses the form add(name, Component).
	 * Adds the specified component with the specified name to the layout. 
	 *
	 * @param	name	String with location for component c
	 * <EM>Note</EM>: the "name" <EM>must</EM> contain x, y location, ie.,
	 * <BR>add("" + 320 + "," + 100, new Button("Quit"));
	 * <BR>or
	 * <BR>add("320,100", new Button("Quit").
	 * <BR>This adds the Button at x=320, y=100 when the Panel is 
	 * at its original full size.
	 * @param	c	Component to be added.
	 */
	public void addLayoutComponent(String name, Component c) {
		int x, y;
		StringTokenizer st = new StringTokenizer(name, ",");
		x = Integer.parseInt(st.nextToken());
		y = Integer.parseInt(st.nextToken());
		// System.out.println("Adding: Name " + name +"; obj " + c
		//	 + "; x " + x + "; y " + y);
		Tracker t = new Tracker(x, y, c);
		curComps.addElement(t);
	}

	/**
	 * Called by AWT to lay out the components 
	 * in the target Container at its current size.
	 *
	 * @param	target	Container whose components are to be laid out.
	 */
	public void layoutContainer(Container target) {
		Dimension targSize = target.getSize();
		Insets ins = target.getInsets();
		// System.out.println("layoutContainer: size " + targSize);
		curWid = targSize.width;
		curHgt = targSize.height;
		float widRatio = (float)curWid / (float)reqWid;
		float hgtRatio = (float)curHgt / (float)reqHgt;
		for (int i = 0; i<curComps.size(); i++) {
			int px, py, pw, ph;
			Tracker t = (Tracker)curComps.elementAt(i);
			Component tc = t.getComponent();
			Dimension d = tc.getPreferredSize();
			px = ins.right+(int)(t.getRequestedLoc().x * widRatio);
			py = ins.top + (int)(t.getRequestedLoc().y * hgtRatio);
			pw = d.width;
			ph = d.height;
			// System.out.println("layoutContainer["+i+"]: move " +
			// tc + " to " + px + ", " + py);
			tc.setBounds(px, py, pw, ph);
		}
	}

	/**
	 * Called from AWT to calculate the minimum size dimensions 
	 * for the target panel given the components in it.
	 * But we use our own list of named insertions, not the
	 * list of Components that the container keeps.
	 *
	 * @param	target	Container to calculate for
	 */
	public Dimension minimumLayoutSize(Container target) {
		int minw = 0, minh = 0;
		for (int i = 0; i<curComps.size(); i++) {
			Tracker t = (Tracker)curComps.elementAt(i);
			Component tc = t.getComponent();
			Dimension d = tc.getMinimumSize();
			Point rl = t.getRequestedLoc();
			minw = Math.max(minw, rl.x +d.width);
			minh = Math.max(minh, rl.y +d.height);
			// System.out.println("minLay, minw = " + minw 
			// + "; minh = " + minh);
		}
		return new Dimension(minw, minw);
	}

	/**
	 * Called by AWT to compute the preferred size for the target panel
	 * given our list of the components that it contains.
	 *
	 * @param	target	Container to calculate for
	 */
	public Dimension preferredLayoutSize(Container target) {
		int prefw = 0, prefh = 0;
		for (int i = 0; i<curComps.size(); i++) {
			Tracker t = (Tracker)curComps.elementAt(i);
			Component tc = t.getComponent();
			Dimension d = tc.getMinimumSize();
			Point rl = t.getRequestedLoc();
			prefw = Math.max(prefw, rl.x+d.width);
			prefh = Math.max(prefh, rl.y+d.height);
			// System.out.println("prefLay, prefw = " + 
			// prefw + "; prefh = " + prefh);
		}
		return new Dimension(prefw, prefh);
	}

	/**
	 * Called by AWT to remove a given component from the layout. 
	 *
	 * @param	c	Component to be removed
	 */
	public void  removeLayoutComponent(Component c) {
		curComps.removeElement(c);
	}

	/**
	 * Tracker is a class used only by RelativeLayout,
	 * to track the original "requested" (hard-coded) x,y locations of
	 * each Component.
	 */
	class Tracker {
		int absx, absy;
		Component theComp;

		/** Construct a tracker item given its location and Component. */
		Tracker(int x, int y, Component c) {
			this.absx = x;
			this.absy = y;
			this.theComp = c;
		}

		/** Extract the location as a Point. */
		public Point getRequestedLoc() {
			return new Point(absx, absy);
		}

		/** Extract the Component from this Tracker. */
		public Component getComponent() {
			return theComp;
		}
	}
}
