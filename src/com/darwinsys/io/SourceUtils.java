package com.darwinsys.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	
	public static List<Class<?>> classListFromSource(String name) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		switch(classify(name)) {
		case CLASS:
			try {
					result.add(Class.forName(name));
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException(e);
				}
			break;
		case JAR:
			break;
		case DIRECTORY:
			break;
		}
		return result;
	}
}
