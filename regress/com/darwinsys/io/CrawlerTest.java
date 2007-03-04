package com.darwinsys.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.darwinsys.io.Crawler;
import com.darwinsys.io.FileHandler;

import junit.framework.TestCase;

public class CrawlerTest extends TestCase {
	
	boolean seenAnyFiles = false;
	
	public void testPubCrawl() throws Exception {
		String dir =  "." ;
				
		FilenameFilter javaFileFilter = new FilenameFilter() {
			public boolean accept(File dir, String s) {
				if (s.endsWith(".java") || s.endsWith(".class") || s.endsWith(".jar"))
					return true;
				// others: projects, ... ?
				return false;
			}
		};
		FileHandler dummyVisitorJustPrints = new FileHandler() {
			private File file;
			public void visit(File f) {
				this.file = f;
				seenAnyFiles = true;
				System.out.println(f.getAbsolutePath());
			}

			public void init() throws IOException {
			}

			public void destroy() throws IOException {
			}

			public File getFile() {
				return file;
			}
			
		};
		new Crawler(javaFileFilter, dummyVisitorJustPrints).crawl(new File(dir));
		
		assertTrue("crawler found at least one file in .", seenAnyFiles);
	}
	
	public void testErrors() throws Exception {
		try {
			new Crawler(null, null);
			fail("Did not throw expected NPE");
		} catch (NullPointerException e) {
			// OK
		} catch (Exception t) {
			fail("Caught UNexcpeted exception " + t);
		}
	}
}
