package com.darwinsys.io;

import java.io.File;
import java.io.IOException;

/** Visitor interface for Crawler.
 */
public interface FileHandler {
	public void init() throws IOException;
	public void visit(File f) throws IOException;
	public void destroy() throws IOException;
	public File getFile();
}
