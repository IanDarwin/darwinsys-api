package com.darwinsys.lang;

/**
 * Very simple StringHolder, just enough to replace my uses of CORBA StringHolder class.
 */
public final class StringHolder {
	public String value;
	public StringHolder() {
		// empty
	}
	public StringHolder(String value) {
		this.value = value;
	}
}
