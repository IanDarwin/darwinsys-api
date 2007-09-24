package com.darwinsys.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.darwinsys.database.DataBaseException;

/** Simple directory crawler, using a Filename Filter to select files and
 * the Visitor pattern to process each chosen file.
 * See the regression test CrawlerTest for a working example.
 * XXX Lots of things are static that should not be!!
 * @author Ian Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class Crawler implements Checkpointer {

	private static boolean debug = false;
	private boolean verbose = false;
	/** The visitor to send all our chosen files to */
	private static FileHandler visitor;
	/** The chooser for files by name; may be null! */
	private FilenameFilter chooser;
	/** A list of directories to exclude altogether (including sub-dirs). */
	private List<String> excludeDirs = new ArrayList<String>();

	/** An Error Handler that just prints the exception */
	public static final CrawlerCallback JUST_PRINT = new CrawlerCallback() {
		public void handleException(Throwable t) {
			System.err.printf("File %s caused exception (%s)\n",
					visitor.getFile().getAbsolutePath(), t);
			if (isDebug()) {
				t.printStackTrace();
			} else {
				Throwable t2 = t.getCause();
				if (t2 != null) {
					System.err.println("Cause: " + t2);
				}
			}
		}
	};
	/** The current Error Handler */
	private CrawlerCallback eHandler;

	public Crawler(FilenameFilter chooser, FileHandler fileVisitor) {
		if (chooser == null) {
			throw new NullPointerException("Chooser may not be null");
		}
		this.chooser = chooser;
		setVisitor(fileVisitor);
	}

	/** Crawl one set of directories, starting at startDir.
	 * Calls itself recursively.
	 * @param startDir
	 * @throws IOException if File.getCanonicalPath() does so.
	 */
	public void crawl(File startDir) throws IOException {

		File[] dir = startDir.listFiles(); // Get list of names in this directory
		if (dir == null) {
			System.err.println("Warning: list of " + startDir + " returned null");
			return;							// head off NPE
		}

		for (File next : dir) {
			String nextFileName = next.getName();
			if (nextFileName == null) {
				System.err.println("Warning: " + startDir +" contains null filename(s)");
				continue;
			}

			// If appropriate, open up this sub-directory and take a look inside.
			if (next.isDirectory()) {
				/*
				 * If the directory is not in our exclude list, can be read,
				 * and we haven't already visited it, visit it.
				 */
				if (excludeDirs.contains(next.getCanonicalPath()))  {
					continue;
				}
				if (seen(next)) {
					continue;
				}
				if (next.canRead()) {
					checkpoint(next);
					crawl(next);			// Crawl the directory
				} else {
					System.err.println("The directory " + next.getAbsolutePath() + " is not readable, ignored.");
				}
			} else {
				// See if we want file by name. Then, if isFile(), process, else ignore quietly
				// (this squelches lots of natterings about borked symlinks, which are not our worry).
				int nextFreeFD = -1;
				if (next.isFile()) {
					if (!next.canRead()) {
						System.err.println(nextFileName + " is not readable, ignored.");
						continue;
					}
					// Intentionally put try/catch around just one call, so we keep going,
					// assuming that it's something that only affects one file...
					try {
						if (chooser != null) {
							if (chooser.accept(startDir, nextFileName)){
								nextFreeFD = NextFD.getNextFD();
								visitFile(next); // Process file based on name.
							}
						} else {
							visitFile(next);	// Process file unconditionally
						}
					} catch (IOException e) {
						e.printStackTrace();
						if (eHandler != null) {
							eHandler.handleException(e);
						} else {
							throw (IOException)e;
						}
					} catch (DataBaseException e) {
						System.err.println("There was a database problem trying to visit: " + next.getAbsolutePath());
						System.err.println(e.getCause());
					} finally {
						if (nextFreeFD != -1 && NextFD.getNextFD() != nextFreeFD) {
							System.err.printf("Hey, processing %s lost a file descriptor!",
									next);
						}
					}
				}
			}
		}
	}

	/**
	 * @param next
	 * @throws IOException
	 */
	private void visitFile(File next) throws IOException {
		if (verbose) {
			System.out.printf("Starting file: %s%n", next.getAbsolutePath());
		}
		visitor.visit(next);
	}

	private Set<String> seenSet = new TreeSet<String>();

	/**
	 * Keep track of whether we have seen this directory, to avoid looping
	 * when people get crazy with symbolic links.
	 * @param next
	 * @return True iff we have seen this directory before.
	 * @throws IOException
	 */
	private boolean seen(File next) throws IOException {
		String path = next.getCanonicalPath();
		boolean seen = seenSet.contains(path);
		if (!seen) {
			seenSet.add(path);
		}
		return seen;
	}

	private void checkpoint(File next) {
		// TODO Need some functionality here...
	}

	public CrawlerCallback getEHandler() {
		return eHandler;
	}

	public void setEHandler(CrawlerCallback handler) {
		eHandler = handler;
		if (debug) {
			Thread.setDefaultUncaughtExceptionHandler(eHandler);
		}
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static FileHandler getVisitor() {
		return visitor;
	}

	public static void setVisitor(FileHandler visitor) {
		Crawler.visitor = visitor;
	}

	/**
	 * Add a list of directories that should be excluded in crawling.
	 * @param dirs
	 */
	public void addExcludeDirs(String ... dirs) {
		for (String dir : dirs) {
			try {
				String fullDir = new File(dir).getCanonicalPath();
				System.err.printf("Crawler.addExcludeDirs(): %s->%s%n", dir, fullDir);
				excludeDirs.add(fullDir);
			} catch (IOException e) {
				System.err.printf("Could not getCanonPath(%s), leaving AS-IS!!%n", dir);
				excludeDirs.add(dir);
			}
		}
	}
}
