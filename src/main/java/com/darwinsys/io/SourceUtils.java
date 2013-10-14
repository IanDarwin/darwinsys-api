package com.darwinsys.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SourceUtils {

	public static SourceType classify(String arg) {

		return classify(new File(arg));
	}

	public static SourceType classify(File f) {
		
		if (!f.exists()) {
			throw new IllegalArgumentException("Failed to classify non-existent " + f);
		}

		if (f.isDirectory() && f.canRead()) {
			return SourceType.DIRECTORY;
		}

		InputStream is = null;
		try {
			is = new FileInputStream(f);
			long magic = is.read();
			String name = f.getName();
			// The annoying magic numbers here are borrowed
			// from my file(1) command, see /etc/magic or
			// http://www.darwinsys.com/file/
			if (magic == 0xCAFEBABE && name.endsWith(".class")) {
				return SourceType.CLASS;
			}
			if (magic == ('P' << 24 | 'K' << 16 | 3 << 8 | 4)
					&& name.endsWith(".jar")) {
				return SourceType.JAR;
			}
			throw new IllegalArgumentException("Failed to classify "
					+ f.getAbsolutePath());
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to classify "
					+ f.getAbsolutePath(), e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					System.err.println("Annoying close: " + e);
				}
			}
		}
	}
}
