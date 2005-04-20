package com.darwinsys.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/** Simple directory crawler, using a Filename Filter to select files and
 * the Visitor pattern to process each chosen file.
 * @author Ian Darwin, http://www.darwinsys.com/
 * @version $Id$
 * @see regress.io.CrawlerTest for a concrete usage example.
 */
public class Crawler implements Checkpointer {
	
	/** The visitor to send all our chosen files to */
	private FileHandler visitor;
	/** The chooser for files by name; may be null! */
	private FilenameFilter chooser;
	/** An Error Handler that just prints the exception */
	public static final CrawlerCaller JUST_PRINT = new CrawlerCaller() {
		public void handleException(Throwable th) {
			System.err.println("Caught exception: " + th);
		}
	};
	/** The current Error Handler */
	private CrawlerCaller eHandler = JUST_PRINT;
	
	public Crawler(FilenameFilter chooser, FileHandler fileVisitor) {
		this.chooser = chooser;
		this.visitor = fileVisitor;
	}
	public Crawler(FileHandler fileVisitor) {
		this(null, fileVisitor);
	}
	
	/** Crawl one set of directories, starting at startDir
	 * @param startDir
	 * @throws IOException if File.getCanonicalPath() does so.
	 */
	public void crawl(File startDir) throws IOException {
		File[] dir = startDir.listFiles(); // Get list of names
		if (dir == null) {
			System.err.println("Warning: list of " + startDir + " returned null");
			return;							// head off NPE
		}
		//java.util.Arrays.sort(dir);		// Sort it (Data Structuring chapter))
		for (int i=0; i<dir.length; i++) {
			File next = dir[i];
			if (next.getName() == null) {
				System.err.println("Warning: " + startDir +" contains null filename(s)");
				continue;
			}
			if (next.isDirectory()  && !seen(next)) {
				checkpoint(next);
				crawl(next);			// Crawl the directory
			} else if (next.isFile()) {
				// Intentionally put try/catch around just one call, so we keep going,
				// assuming that it's something that only affects one file...
				try {
					if (chooser != null) {
						if (chooser.accept(startDir, next.getName())){
							visitor.visit(next); // Process file based on name.
						}
					} else {
						visitor.visit(next);	// Process file unconditionally
					}
				} catch (IOException e) {
					eHandler.handleException(e);
				}
			} else {
				System.err.println("Warning:" + next + " neither file nor directory");
			}
		}
	}

	private Set seen = new TreeSet();
	
	/**
	 * Keep track of whether we have seen this directory, to avoid looping
	 * when people get crazy with symbolic links.
	 * @param next
	 * @return True iff we have seen this directory before.
	 * @throws IOException 
	 */
	private boolean seen(File next) throws IOException {
		String path = next.getCanonicalPath();
		boolean seen = this.seen.contains(path);
		if (!seen) {
			this.seen.add(path);
		}
		return seen;
	}
	
	private void checkpoint(File next) {
		// TODO Auto-generated method stub		
	}
	public CrawlerCaller getEHandler() {
		return eHandler;
	}
	
	public void setEHandler(CrawlerCaller handler) {
		eHandler = handler;
	}
	
}
