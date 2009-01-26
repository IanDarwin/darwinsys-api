package com.darwinsys.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.darwinsys.util.Debug;

/** ClassLoader to load classes directly from a Jar 
 * (or Zip) File, assuming that
 * the Zip or Jar file is not on your classpath
 */
public class JarFileClassLoader extends ClassLoader {

	private JarFile zipFile;

	public JarFileClassLoader(JarFile zf) {
		zipFile = zf;
	}

	/** Override of standard findClass method. Not used by the application,
	 * but will be called by JVM for referenced classes so must work. :-)
	 */
	@Override
	public Class<?> findClass(String className) throws ClassNotFoundException {
		Debug.println("classloader", "JarClassLoader: In standard findClass");
		String entryName = className.replace('.', '/') + ".class";
		JarEntry ze = (JarEntry) zipFile.getEntry(entryName);
		if (ze == null) {
			throw new ClassNotFoundException(
			"findClass(string) can't find " + className + " in ZipFile");
		}
		return findClass(className, ze);
	}

	/* load the Class given by the parameters - overload, not override */
	public Class findClass(String className, ZipEntry zipEntry)
	throws ClassNotFoundException {
		Debug.println("classloader", "JarClassLoader: In overloaded findClass");
		byte[] bytes;
		try {
			InputStream is = zipFile.getInputStream(zipEntry);
			int size = (int)zipEntry.getSize();
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
		System.out.println("ZipClassLoader: Defining " + className);
		return defineClass(className, bytes, 0, bytes.length);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		Debug.println("classloader", "JarClassLoader: In loadClass, calling super(" +
			className + ")");
		return super.loadClass(className);
	}
	
}
