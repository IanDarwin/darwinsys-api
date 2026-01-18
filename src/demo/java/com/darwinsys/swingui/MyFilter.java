package com.darwinsys.swingui;

/** A trivial filter baseclass used in testing FilterGUI */
public abstract class MyFilter {
	protected MyFilter next;
	public abstract void write(byte[] data) throws MyFilterException;
	public void setNext(MyFilter n) {
		next = n;
	}
}
