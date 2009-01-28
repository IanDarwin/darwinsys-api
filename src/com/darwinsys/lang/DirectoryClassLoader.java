package com.darwinsys.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.darwinsys.util.Debug;

/** ClassLoader to load classes from a local directory 
 * file that is not on your classpath
 */
public class DirectoryClassLoader extends ClassLoader {

	private File dirFile;

	public DirectoryClassLoader(String dirName) {
		dirFile = new File(dirName);
	}

	/** Override of standard findClass method. Not used by the application,
	 * but will be called by JVM for referenced classes so must work. :-)
	 */
	@Override
	public Class<?> findClass(String className) throws ClassNotFoundException {
		Debug.println("classloader", "JarClassLoader: In standard findClass");
		String entryName = className.replace('.', '/') + ".class";
		byte[] bytes;
		try {
			InputStream is = new FileInputStream(entryName);
			int size = (int)dirFile.length();
			bytes = new byte[size];
			int n = 0, total = 0;
			while (total < size) {
				n = is.read(bytes, total, size-total);
				total += n;
			}
			is.close();
		} catch (IOException ex) {
			throw new ClassNotFoundException(ex.toString());
		}
		System.out.println("DirectoryClassLoader: Defining " + className);
		return defineClass(className, bytes, 0, bytes.length);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		Debug.println("classloader", "DirectoryClassLoader: In loadClass, calling super(" +
			className + ")");
		return super.loadClass(className);
	}
	
}
