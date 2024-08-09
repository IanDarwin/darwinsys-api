package com.darwinsys.curiosity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Debug-only module to get path information
 */
public class ShowPaths {

	/* Save the runtime CLASSPATH and MODULEPATH to temp files.
	 * @param filenameBase A path to which "_cp" and "_mp" will be
	 * appended, for the classpath and modulepath respectively.
	 */
	public void saveCpMp(String filenameBase) throws IOException {
		Files.writeString(Path.of(filenameBase + "_cp"),
		"Test classpath is:\n" + System.getProperty("java.class.path").replace(':','\n') + "\n");
		Files.writeString(Path.of(filenameBase + "_mp"),
		"Test classpath is " + System.getProperty("jdk.module.path") + "\n");
	}
}
