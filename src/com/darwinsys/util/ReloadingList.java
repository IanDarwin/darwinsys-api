package com.darwinsys.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * A Reloading List - checks every n seconds if
 * the file's timestamp has changed, and rereads if so.
 * Based on Vector since all its methods are synchronized.
 * X is a Y.
 */
public class ReloadingList extends Vector<String>{

	private static final long serialVersionUID = -2116296360428588971L;
	/** the file we read from */
	private File file;
	/** numer of seconds to sleep before checking mtime */
	private int seconds;
	/** Default number of seconds for "seconds" */
	private static final int DEFAULT_INTERVAL_SECONDS = 120;
	/** time file last modified */
	private long mtime;
	
	public ReloadingList(String fileName) {
		this(fileName, DEFAULT_INTERVAL_SECONDS);
	}
	
	public ReloadingList(String fileName, int seconds) {
		this.file = new File(fileName);
		if (!(file.exists() && file.isFile() && file.canRead()) ) {
			throw new IllegalArgumentException("Not a readable file: " + fileName);
		}
		this.seconds = seconds;
		final Thread watcher = new Thread(new Watcher());
		// Don't keep main() running just for me!
		watcher.setDaemon(true);
		watcher.start();
	}

	protected synchronized void read() {
		try {
			BufferedReader is = new BufferedReader(new FileReader(file));
			clear();
			String line;
			while ((line = is.readLine()) != null) {
				add(line);
			}
			is.close();
			mtime = file.lastModified();
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	class Watcher implements Runnable {

		public void run() {
			while (true) {
				if (mtime != file.lastModified()) {
					read();
				}
				try {
					Thread.sleep(1000 * seconds);
				} catch (InterruptedException e) {
					return;
				}
			}
			
		}
		
	}
}
