package com.darwinsys.io;

import java.io.File;

/** Visitor interface for Crawler.
 * @version $Id$
 */
public interface FileHandler {
	public void visit(File f);
}
