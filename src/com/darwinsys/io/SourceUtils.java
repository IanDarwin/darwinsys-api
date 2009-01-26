package com.darwinsys.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.darwinsys.util.Debug;

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
	
	private static List<Class<?>> result;
	
	/**
	 * Create a list of the classes in a Source.
	 * <br/>
	 * <b>N.B.</b>: This method is not thread-safe!
	 * @param name
	 * @return List<Class<?>>
	 */
	public static List<Class<?>> classListFromSource(String name) {
		result = new ArrayList<Class<?>>();
		switch(classify(name)) {
		case CLASS:
			try {
					result.add(Class.forName(name));
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException(e);
				}
			break;
		case JAR:
			throw new IllegalStateException("code called before written");
			//break;
		case DIRECTORY:
			doDir(name);
			break;
		}
		return result;
	}
	
	private static String startPath;
	
	/** doDir - do one directory recursively */
	private static void doDir(String name) {
		final File file = new File(name);
		startPath = name;
		doDir(file);
	}
	
	/** doDir - do one directory recursively */
	private static void doDir(File f) {
		final String name = f.getPath();
		Debug.println("sourceutils", "SourceUtils.doDir(): " + name);
		if (!f.exists()) {
			throw new IllegalStateException(name + " does not exist");
		}
		if (f.isFile())
			doFile(f);
		else if (f.isDirectory()) {
			File objects[] = f.listFiles();

			for (int i=0; i<objects.length; i++)
				doDir(objects[i]);
		} else
			System.err.println("Unknown: " + name);
	}

	private static void doFile(File f) {
		final String name = f.getPath().substring(1+startPath.length());
		Debug.println("sourceutils", "SourceUtils.doFile(): " + name);
		if (name.endsWith(".class") && name.indexOf('$') == -1) {
			String className = name.substring(0, name.length() - 6).replace("/", ".");
			try {
				Class<?> c = Class.forName(className);
				result.add(c);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}
