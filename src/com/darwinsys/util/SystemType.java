package com.darwinsys.util;

/**
 * Identify the Operating System.
 */
public enum SystemType {
	/** A constant for "can't figure out this system type" */
	SYS_UNKNOWN,
	/** A constant for "System is UNIX or Linux" */
	SYS_NIX,
	/** A constant for "System is Mac OS X" */
	SYS_MACOSX,
	/** A constant for "System is Microsoft Windows" */
	SYS_MSWIN,
	/** A constant for being run under the J2ME */
	SYS_J2ME;
	
	String system = System.getProperty("os.name").toLowerCase();
			
	SystemType type() {
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


