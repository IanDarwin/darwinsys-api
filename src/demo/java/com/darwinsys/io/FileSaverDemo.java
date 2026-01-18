package com.darwinsys.io;

import java.io.*;
import java.nio.file.*;

public class FileSaverDemo {

	static Path file = Path.of("/tmp/id");

	public static void main(String[] args) {
		try (
				FileSaver saver = new FileSaver(file);
				final Writer writer = saver.getWriter();
				PrintWriter out = new PrintWriter(writer);) {
			myWriteOutputFile(out);
			out.close();
			saver.finish();
			System.out.println("Saved OK");
		} catch (IOException e) {
			System.out.println("Save FAILED");
		}
	}

	static void myWriteOutputFile(PrintWriter out) {
		out.println("<!-- This is a demo file. Not all that useful. -->");
	}
}