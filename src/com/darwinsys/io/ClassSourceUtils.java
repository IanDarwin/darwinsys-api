package com.darwinsys.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.darwinsys.lang.JarFileClassLoader;
import com.darwinsys.lang.DirectoryClassLoader;
import com.darwinsys.util.Debug;

public class ClassSourceUtils extends SourceUtils {
		
	private static List<Class<?>> result;
	
	/**
	 * Create a list of the Classes in a Source.
	 * <br/>
	 * <b>N.B.</b>: This method is not thread-safe!
	 * @param name
	 * @return List<Class<?>>
	 */
	public static List<Class<?>> classListFromSource(String name) {
		switch(classify(name)) {
		case CLASS:
			result = new ArrayList<Class<?>>();
			try {
					result.add(Class.forName(name));
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException(e);
				}
			break;
		case JAR:
			return classListFromJar(name);
		case DIRECTORY:
			result = new ArrayList<Class<?>>();
			doDir(name);
			break;
		}
		return result;
	}
	
	private static List<Class<?>> classListFromDirectory(final String dirName) {
		DirectoryClassLoader cl = new DirectoryClassLoader(dirName);
		throw new IllegalStateException("not written yet");
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
	
	private static List<Class<?>> classListFromJar(final String name) {
		final List<Class<?>> results = new ArrayList<Class<?>>();
		try {
			final JarFile jf = new JarFile(name);
			final JarFileClassLoader cl = new JarFileClassLoader(jf);
			final Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				final JarEntry jarEntry = entries.nextElement();
				String entName = jarEntry.getName();
				if (entName.endsWith(".class")) {
					int n = entName.length();
					try {
						results.add(
								cl.findClass(
									entName.substring(0, n - 6).replace('/','.'), jarEntry));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
		
}
