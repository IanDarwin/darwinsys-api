package com.darwinsys.io;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.darwinsys.util.Debug;

public class ClassSourceUtils extends SourceUtils {
		
	private static List<Class<?>> result;
	
	/**
	 * Create a list of the Classes in a Source;
	 * each class that is successfully loaded as though
	 * by Class.forName() will be included in the list.
	 * <br/>
	 * <b>N.B.</b>: This method is not thread-safe!
	 * @param name - the name of something that can be used
	 * as a Source, e.g., a Jar file, a class file or a directory.
	 * @return List<Class<?>> List of all classes found in the source
	 */
	public static List<Class<?>> classListFromSource(String name) {
		switch(classify(name)) {
		case CLASS:
			try {
				result = new ArrayList<Class<?>>();
				result.add(Class.forName(name));
				return result;
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		case JAR:
			return classListFromJar(name);
		case DIRECTORY:
			return classListFromDirectory(name);
		default:
			throw new IllegalArgumentException(name);
		}
	}
	
	private static List<Class<?>> classListFromJar(final String name) {
		final List<Class<?>> results = new ArrayList<Class<?>>();
		try {
			final JarFile jf = new JarFile(name);
			final File jFile = new File(name);
			ClassLoader cl = 
				new URLClassLoader(new URL[]{new URL("file://" + jFile.getAbsolutePath())});

			final Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				final JarEntry jarEntry = entries.nextElement();
				String entName = jarEntry.getName();
				if (entName.endsWith(".class")) {
					int n = entName.length();
					try {
						results.add(
								cl.loadClass(
									entName.substring(0, n - 6).replace('/','.')));
					} catch (ClassNotFoundException e) {
						System.err.println(e);
						// But caught here so we go on to next one.
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(name, e);
		}
		return results;
	}
	
	private static List<Class<?>> classListFromDirectory(final String dirName) {
		result = new ArrayList<Class<?>>();		
		ClassLoader cl;
		try {
			final File fileDir = new File(dirName);
			final URL fileDirURL = new URL("file://" + fileDir.getCanonicalPath() + "/");
			Debug.println("source", "Creating URLClassLoader for " + fileDirURL);
			cl = new URLClassLoader(new URL[]{fileDirURL});
		} catch (Exception e) {
			throw new IllegalArgumentException(dirName, e);
		}
		startDir(dirName, cl);
		return result;
	}
	
	private static String startPath;
	
	/** doDir - do one directory recursively */
	private static void startDir(String name, ClassLoader cl) {
		final File file = new File(name);
		startPath = name;
		doDir(file, cl);
	}
	
	/** doDir - do one directory recursively */
	private static void doDir(File f, ClassLoader cl) {
		final String name = f.getPath();
		Debug.println("sourceutils", "SourceUtils.doDir(): " + name);
		if (!f.exists()) {
			throw new IllegalStateException(name + " does not exist");
		}
		if (f.isFile())
			doFile(f, cl);
		else if (f.isDirectory()) {
			File objects[] = f.listFiles();

			for (int i=0; i<objects.length; i++)
				doDir(objects[i], cl);
		} else
			System.err.println("Unknown: " + name);
	}

	private static void doFile(File f, ClassLoader cl) {
		final String name = f.getPath().substring(1+startPath.length());
		if (name.endsWith(".class")) {
			String className = name.substring(0, name.length() - 6).replace("/", ".");
			Debug.println("sourceutils", "SourceUtils.doFile(): '" + className + '\'');
			try {
				Class<?> c = cl.loadClass(className);
				result.add(c);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}
