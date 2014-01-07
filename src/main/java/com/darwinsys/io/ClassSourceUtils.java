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

import com.darwinsys.util.Debug;

public class ClassSourceUtils extends SourceUtils {
		
	private static final String DEBUG_TAG = "sourceutils";
	private static List<Class<?>> result;
	
	/**
	 * Create a list of the Classes in a Source;
	 * each class that is successfully loaded as though
	 * by Class.forName() will be included in the list.
	 * <br>
	 * <b>N.B.</b>: This method is not thread-safe!
	 * @param name - the name of something that can be used
	 * as a Source, e.g., a Jar file, a class file or a directory.
	 * @return List&lt;Class&lt;?&gt;&gt; List of all classes found in the source
	 */
	public static List<Class<?>> classListFromSource(String name, List<String> classpath) {
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
			return classListFromJar(name, classpath);
		case DIRECTORY:
			return classListFromDirectory(name, classpath);
		default:
			throw new IllegalArgumentException(name);
		}
	}
	
	public static List<Class<?>> classListFromSource(String classesToTest) {
		return classListFromSource(classesToTest, null);
	}
	
	private static List<Class<?>> classListFromJar(final String name, List<String> classpath) {
		final List<Class<?>> results = new ArrayList<Class<?>>();
		try {
			final JarFile jf = new JarFile(name);
			final File jFile = new File(name);
			ClassLoader cl = 
				new URLClassLoader(new URL[]{makeFileURL(jFile.getAbsolutePath())});

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
						// Caught here so we go on to next one.
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(name, e);
		}
		return results;
	}
	
	private static List<Class<?>> classListFromDirectory(final String dirName, List<String> classpath) {
		result = new ArrayList<Class<?>>();		
		ClassLoader cl;
		try {
			final File fileDir = new File(dirName);
			final URL fileDirURL = makeFileURL(fileDir.getCanonicalPath());
			List<URL> urls = new ArrayList<URL>();
			urls.add(fileDirURL);
			if (classpath != null) {
				for (String s : classpath) {
					final URL anotherURL = makeFileURL(s);
					urls.add(anotherURL);
					Debug.println(DEBUG_TAG, "added " + anotherURL);
				}
			}
			final int extraElements = urls.size();
			Debug.println(DEBUG_TAG, "Creating URLClassLoader for " + fileDirURL +
					" with " + extraElements + " extra elements.");
			cl = new URLClassLoader(urls.toArray(new URL[extraElements]));
		} catch (Exception e) {
			throw new IllegalArgumentException(dirName, e);
		}
		startDir(dirName, cl);
		return result;
	}
	
	public static URL makeFileURL(String s) throws IOException {
		File f = new File(s);
		return new URL("file://" + f.getCanonicalPath() + (f.isDirectory() ? "/" : ""));
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
		Debug.println(DEBUG_TAG, "SourceUtils.doDir(): " + name);
		if (!f.exists()) {
			throw new IllegalStateException(name + " does not exist");
		}
		if (f.isFile()) {
			final String className = f.getPath().substring(1+startPath.length());
			try {
				result.add(doFile(f, cl, className));
			} catch (Exception e) {
				System.err.println("Warning: non-classifiable: " + f);
			}
		}
		else if (f.isDirectory()) {
			File objects[] = f.listFiles();

			for (int i=0; i<objects.length; i++)
				doDir(objects[i], cl);
		} else
			System.err.println("Unknown: " + name);
	}

	public static Class<?> doFile(File f, ClassLoader cl) {
		return doFile(f, cl, f.getName());
	}
	
	private static Class<?> doFile(File f, ClassLoader cl, String name) {
		
		if (name.endsWith(".class")) {
			String className = name.substring(0, name.length() - 6).replace("/", ".");
			Debug.println(DEBUG_TAG, "SourceUtils.doFile(): '" + className + '\'');
			try {
				Class<?> c = cl.loadClass(className);
				Debug.println(DEBUG_TAG, "Loaded OK");
				return c;
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}
		throw new IllegalArgumentException(f.getAbsolutePath());
	}
}
