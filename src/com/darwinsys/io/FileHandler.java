package com.darwinsys.io;

import java.io.File;
import java.io.IOException;

/** Visitor interface for Crawler.
 * @version $Id$
 */
public interface FileHandler {
	public void init() throws IOException;
	public void visit(File f) throws IOException;
	public void destroy() throws IOException;
}
