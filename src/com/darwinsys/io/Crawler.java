package com.darwinsys.io;

import java.io.File;
import java.io.FilenameFilter;

/** Simple directory crawler, using a Filename Filter to select files and
 * the Visitor pattern to process each chosen file.
 * @author Ian Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class Crawler implements Checkpointer {
	
	/** The visitor to send all or chosen files to */
	private FileHandler visitor;
	/** The chooser for files by name; may be null! */
	private FilenameFilter chooser;
	
	public Crawler(FilenameFilter chooser, FileHandler fileVisitor) {
		this.chooser = chooser;
		this.visitor = fileVisitor;
	}
	public Crawler(FileHandler fileVisitor) {
		this(null, fileVisitor);
	}
	
	public void crawl(File startDir) {
		File[] dir = startDir.listFiles(); // Get list of names
		java.util.Arrays.sort(dir);		// Sort it (Data Structuring chapter))
		for (int i=0; i<dir.length; i++) {
			File next = dir[i];
			if (next.isDirectory()) {
				checkpoint(next);
				crawl(next);			// Crawl the directory
			} else if (next.isFile()) {
				if (chooser != null) {
					if (chooser.accept(startDir, next.getName())){
						visitor.visit(next); // Process file based on name.
					}
				} else {
					visitor.visit(next);	// Process file unconditionally
				}
			} else {
				System.err.println("Warning:" + next + " neither file nor directory");
			}
		}
	}

	private void checkpoint(File next) {
		// TODO Auto-generated method stub		
	}
}
