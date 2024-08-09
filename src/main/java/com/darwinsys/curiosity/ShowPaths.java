package com.darwinsys.curiosity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Debug-only module to get path information
 */
public class ShowPaths {

	private record PathEntry(String printable, String property){}

	private final static PathEntry[] ENTRIES = {
		new PathEntry("Class path", "java.class.path"),
		new PathEntry("Module path", "jdk.module.path"),
		new PathEntry("Module Upgrade path", "jdk.module.upgrade.path"),
		new PathEntry("Main Module", "jdk.module.main"),
		new PathEntry("Main class", "jdk.module.main.class"),
	};

	/* Save the runtime CLASSPATH and MODULEPATH to temp files.
	 * @param prefix A string to prepend to the displays of
	 * the classpath and modulepath.
	 */
	public static void showPaths(String prefix) throws IOException {
		for (PathEntry pe : ENTRIES) {
			var prop = System.getProperty(pe.property());
			System.err.printf("%s: %s is %s\n",
				prefix, pe.printable(),
				prop != null ? prop.replace(':','\n') : "null");
		}
	}
}
