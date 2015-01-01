package com.darwinsys.lang;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.darwinsys.io.FileIO;

/**
 * ExecAndPrint runs a program using Runtime.exec(),
 * read the program's output, and returns its exit status.
 */
public class ExecAndPrint {

	// BEGIN main
	/** Need a Runtime object for any of these methods */
	protected final static Runtime r = Runtime.getRuntime();

	/** Run the command given as a String, output to System.out
	 * @param cmd The command
	 * @return The command's exit status
	 * @throws IOException if the command isn't found
	 */
	public static int run(String cmd) throws IOException { 
		return run(cmd, new OutputStreamWriter(System.out));
	}

	/** Run the command given as a String, output to "out"
	 * @param cmd The command and list of arguments
	 * @param out The output file
	 * @return The command's exit status
	 * @throws IOException if the command isn't found
	 */
	public static int run(String cmd, Writer out) throws IOException {
		
		Process p = r.exec(cmd);

		FileIO.copyFile(new InputStreamReader(p.getInputStream()), out, true);
		try {
			p.waitFor();	// wait for process to complete
		} catch (InterruptedException e) {
			return -1;
		}
		return p.exitValue();
	}
	// END main

	/** Run the command given as a String[], output to System.out
	 * @param cmd The command and list of arguments
	 * @return The command's exit status
	 * @throws IOException if the command isn't found
	 */
	public static int run(String[] cmd) throws IOException { 
		return run(cmd, new OutputStreamWriter(System.out));
	}

	/** Run the command given as a String[], print its output to "out"
	 * @param cmd The command and list of arguments
	 * @param out The output file
	 * @return The command's exit status
	 * @throws IOException if the command isn't found
	 */
	public static int run(String[] cmd, Writer out) throws IOException {
		
		Process p = r.exec(cmd);

		FileIO.copyFile(new InputStreamReader(p.getInputStream()), out, true);

		try {
			p.waitFor();	// wait for process to complete
		} catch (InterruptedException e) {
			return -1;
		}
		return p.exitValue();
	}
}
