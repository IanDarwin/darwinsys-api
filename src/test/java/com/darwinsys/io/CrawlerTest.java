package com.darwinsys.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.jupiter.api.*;

public class CrawlerTest {

	protected static final boolean debug = false;

	boolean seenAnyFiles = false;

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

	@Test
	public void testPubCrawl() throws Exception {
		String dir =  "." ;

		new Crawler(javaFileFilter, dummyVisitorJustPrints).crawl(new File(dir));

		assertTrue("crawler found at least one file in .", seenAnyFiles);
	}

	@Test
	public void testWithDirFilter() throws Exception {
		String dir =  "." ;
		final File tmpDir = File.createTempFile("tmp", ".dir", new File(dir));
		tmpDir.delete(); tmpDir.mkdirs();
		assertTrue(tmpDir.exists());
		tmpDir.deleteOnExit();
		File badFile = new File(tmpDir, "xxx");
		badFile.createNewFile();
		badFile.deleteOnExit();
		FileHandler checksForBannedFile = new FileHandler() {
			private File file;
			public void visit(File f) {
				this.file = f;
				if (file.getAbsolutePath().startsWith(tmpDir.getAbsolutePath())) {
						throw new IllegalStateException("FOUND");
				}
				if (debug)
					System.out.println("crawler: " + f);
			}

			public void init() throws IOException {
			}

			public void destroy() throws IOException {
			}

			public File getFile() {
				return file;
			}

		};
		final FilenameFilter alwaysAcceptFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return true;
			}
			
		};
		final FilenameFilter dirFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (dir.equals(tmpDir))
					return false;
				return true;
			}
			
		};
		final Crawler crawler = new Crawler(alwaysAcceptFilter, dirFilter, checksForBannedFile);
		new File(tmpDir, "xxx").createNewFile();
		crawler.crawl(new File(dir));

	}

	@Test
	public void testErrors() throws Exception {
		try {
			new Crawler(null, null);
			fail("Did not throw expected NPE");
		} catch (NullPointerException e) {
			// OK
		} catch (Throwable t) {
			fail("Caught UNexpected exception " + t);
		}
	}
}
