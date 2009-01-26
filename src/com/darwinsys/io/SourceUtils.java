package com.darwinsys.io;

import java.io.File;

public class SourceUtils {
	public static SourceType classify(String arg) {
	
		File f = new File(arg);
		
		if (f.isDirectory() && f.canRead()) {
			return SourceType.DIRECTORY;
		}
		
		if (arg.endsWith(".jar") && f.exists()) {
			// XXX might want to create a JarEntry & verify?
			return SourceType.JAR;
		}
		
		try {
			Class.forName(arg);
			return SourceType.CLASS;
		} catch (ClassNotFoundException e) {
			// nothing to do here, it's not a class
		}
		throw new IllegalArgumentException("Failed to classify " + arg);
	}
}
