package com.darwinsys.util;

/**
 * Identify the Operating System.
 * XXX In 1.5, rewrite using enums.
 */
public class SystemType {
	/** A constant for "can't figure out this system type" */
	public final int SYS_UNKNOWN = -1;
	/** A constant for "System is UNIX or Linux" */
	public final int SYS_NIX = 1;
	/** A constant for "System is Mac OS X" */
	public final int SYS_MACOSX = 2;
	/** A constant for "System is Microsoft Windows" */
	public final int SYS_MSWIN = 3;
	/** A constant for being run under the J2ME */
	public final int SYS_J2ME = 4;
	
	String system = System.getProperty("os.name").toLowerCase();
			
	int type() {
		if (system.indexOf("mac") > 0) {
			return SYS_MACOSX;
		}
		if (system.indexOf("unix") > 0 || system.indexOf("linux") > 0) {
			return SYS_NIX;
		}
		if (system.indexOf("windows") > 0) {
			return SYS_MSWIN;
		}
		return SYS_UNKNOWN;
	}
}


