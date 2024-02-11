package com.darwinsys.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * A set of utility methods for Class files.
 */
public class ClassSourceUtils extends SourceUtils {
	
	private final static Logger logger = 
		Logger.getLogger(ClassSourceUtils.class.getName());
		
	private static List<Class<?>> result;

	/** No-argument constructor but all fields are static */
	public ClassSourceUtils() {
		System.out.println("Warning: Constructing ClassSourceUtils but all methods are static");
	}
	
	/**
	 * Create a list of the Classes in a Source;
	 * each class that is successfully loaded as though
	 * by Class.forName() will be included in the list.
	 * <br>
	 * <b>N.B.</b>: This method is not thread-safe!
	 * @param name - the name of something that can be used
	 * as a Source, e.g., a Jar file, a class file or a directory.
	 * @param classpath List of classpath entries
	 * @return List&lt;Class&lt;?&gt;&gt; List of classes found in the source
	 * which were able to be loaded.
	 */
	public static List<Class<?>> classListFromSource(String name, List<String> classpath) {
		switch(classify(name)) {
		case CLASS:
			result = new ArrayList<Class<?>>();
			try {
				result.add(Class.forName(name));
			} catch (Exception e) {
				// Caught here so we go on to next one.
				logger.warning(
					String.format("Class %s failed to load: %s", name, e));
			}
			return result;
		case JAR:
			return classListFromJar(name, classpath);
		case DIRECTORY:
			return classListFromDirectory(name, classpath);
		default:
			throw new IllegalStateException(
				String.format("Could not classify %s: Unhandled type", name));
		}
	}
	
	/**
	 * Make a list of Class descripters from a source
	 * @param classesToTest The names of the classes
	 * @return The list of Class descriptors.
	 */
	public static List<Class<?>> classListFromSource(String classesToTest) {
		return classListFromSource(classesToTest, null);
	}
	
	/**
	 * Make a list of Class descripters from a Jar file
	 * @param name The name of the jar file
	 * @param classpath The members of the classpath.
	 * @return The list of Class descriptors.
	 */
	private static List<Class<?>> classListFromJar(final String name, List<String> classpath) {
		final List<Class<?>> results = new ArrayList<Class<?>>();
		final File jFile = new File(name);
		ClassLoader cl = null;
		try (final JarFile jf = new JarFile(name)) {
			cl = new URLClassLoader(new URL[]{makeFileURL(jFile.getAbsolutePath())});

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
					} catch (Exception e) {
						logger.warning(
							String.format("Class %s failed to load: %s",
								entName, e));
						// Caught here so we go on to next one.
					}
				}
			}
		} catch (Exception e) {
			logger.warning("Caught unknown exception: " + e);
		}
		return results;
	}
	
	/**
	 * Tries to find all the classes in the given directory and set of classpath elements
	 * @param dirName Starting directory
	 * @param classpath List of classpath elements
	 * @return List of classes
	 */
	private static List<Class<?>> classListFromDirectory(final String dirName, List<String> classpath) {
		result = new ArrayList<Class<?>>();		
		ClassLoader cl = null;
		try {
			final File fileDir = new File(dirName);
			final URL fileDirURL = makeFileURL(fileDir.getCanonicalPath());
			List<URL> urls = new ArrayList<URL>();
			urls.add(fileDirURL);
			if (classpath != null) {
				for (String s : classpath) {
					final URL anotherURL = makeFileURL(s);
					urls.add(anotherURL);
					logger.fine("added " + anotherURL);
				}
			}
			final int extraElements = urls.size();
			logger.fine("Creating URLClassLoader for " + fileDirURL +
					" with " + extraElements + " extra elements.");
			cl = new URLClassLoader(urls.toArray(new URL[extraElements]));
		} catch (Exception e) {
			logger.warning("Failed to list directory " + dirName);
		}
		startDir(dirName, cl);
		return result;
	}
	
	/** Whip up a URL for a filename
	 * @param filename The file name
	 * @return The URL
	 * @throws IOException Something wrong in IO land.
	 */
	public static URL makeFileURL(String filename) throws IOException {
		File f = new File(filename);
		return new URL("file://" + f.getCanonicalPath() + (f.isDirectory() ? "/" : ""));
	}

	private static String startPath;
	
	/** startDir - do one directory recursively
	 * @param name The input
	 * @param cl The classloader
	 */
	private static void startDir(String name, ClassLoader cl) {
		final File file = new File(name);
		startPath = name;
		doDir(file, cl);
	}
	
	/** doDir - do one directory recursively
	 * @param f The input file
	 * @param cl The ClassLoader
	 */
	private static void doDir(File f, ClassLoader cl) {
		final String name = f.getPath();
		System.out.println("SourceUtils.doDir(): " + name);
		if (!f.exists()) {
			logger.warning(name + " does not exist");
			return;
		}
		if (f.isFile()) {
			final String className = f.getPath().substring(1+startPath.length());
			try {
				final Class<?> clazz = doFile(f, cl, className);
				if (clazz != null)
					result.add(clazz);
			} catch (Exception e) {
				logger.warning("Warning: non-classifiable: " + f);
				return;
			}
		}
		else if (f.isDirectory()) {
			File objects[] = f.listFiles();

			for (int i=0; i<objects.length; i++)
				doDir(objects[i], cl);
		} else
			logger.warning("Unknown: " + name);
	}

	/**
	 * Process one file.
	 * @param f The File to process.
	 * @param cl The ClassLoader to use.
	 * @return The Class descriptor
	 */
	public static Class<?> doFile(File f, ClassLoader cl) {
		return doFile(f, cl, f.getName());
	}

	/**
	 * Process one file.
	 * @param f The File to process.
	 * @param cl The ClassLoader to use.
	 * @param name A classfile name.
	 * @return The Class descriptor
	 */
	private static Class<?> doFile(File f, ClassLoader cl, String name) {
		
		if (name.endsWith(".class")) {
			String className = name.substring(0, name.length() - 6).replace("/", ".");
			logger.fine("SourceUtils.doFile(): '" + className + '\'');
			try {
				Class<?> c = cl.loadClass(className);
				logger.fine("Loaded OK");
				return c;
			} catch (Exception e) {
				logger.warning(
					String.format("Class %s failed to load: %s", name, e));
				return null;
			}
		}
		throw new IllegalStateException("Not a class! " + f.getAbsolutePath());
	}
}
