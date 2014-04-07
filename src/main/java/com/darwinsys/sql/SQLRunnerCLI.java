/* Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 2004-2006.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.darwinsys.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.lang.GetOpt;
import com.darwinsys.util.Verbosity;

/** CLI front-end for SQL Runner
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
public class SQLRunnerCLI {

	/** print help; called from several places in main */
	private static void doHelp(int i) {
		System.out.println(
		"Usage: SQLRunner [-f configFile] [-c config] [SQLscript[ ...]");
		System.exit(i);
	}

	/**
	 * main - parse arguments, construct SQLRunner object, open file(s), run scripts.
	 */
	public static void main(final String[] args) {
		String config = "default";
		String outputModeName = "t";
		String outputFile = null;
		final GetOpt go = new GetOpt("dvf:c:m:o:");
		char c;
		while ((c = go.getopt(args)) != GetOpt.DONE) {
			switch(c) {
			case 'h':
				doHelp(0);
				break;
			case 'd':
				SQLRunner.setVerbosity(Verbosity.DEBUG);
				break;
			case 'v':
				SQLRunner.setVerbosity(Verbosity.VERBOSE);
				break;
			case 'f':
				ConnectionUtil.setConfigFileName(go.optarg());
				break;
			case 'c':
				config = go.optarg();
				break;
			case 'm':
				outputModeName = go.optarg();
				break;
			case 'o':
				outputFile = go.optarg();
				break;
			default:
				System.err.println("Unknown option character " + c);
				doHelp(1);
			}
		}

		try {

			Configuration conf = ConnectionUtil.getConfiguration(config);
			if (!conf.hasPassword()) {
				System.err.printf("Enter password for connection %s: ", config);
				System.err.flush();
				Scanner sc = new Scanner(System.in);      // Requires J2SE 1.5
	            String newPass = sc.next();
				conf.setPassword(newPass);
			}
			Connection conn = ConnectionUtil.getConnection(conf);


			SQLRunner prog = new SQLRunner(conn, outputFile, outputModeName);

			if (go.getOptInd() == args.length) {
				runScript(prog, new BufferedReader(
					new InputStreamReader(System.in)), "(standard input)");
			} else for (int i = go.getOptInd()-1; i < args.length; i++) {
				runScript(prog, args[i]);
			}
			prog.close();
		} catch (SQLException ex) {
			throw new DataBaseException(ex.toString());
		} catch (IOException ex) {
			throw new DataBaseException(ex.toString());
		} catch (ClassNotFoundException ex) {
			throw new DataBaseException(ex.toString());
		}
		System.exit(0);
	}

	static void runScript(SQLRunner prog, String scriptFile)
	throws IOException, SQLException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(prog, is, scriptFile);
	}

	static void runScript(SQLRunner prog, BufferedReader is, String name) throws IOException {
		String stmt;

		System.out.printf("SQLRunner: starting %s%n", name);
		System.out.flush();
		while ((stmt = SQLRunner.getStatement(is)) != null) {
			stmt = stmt.trim();
			try {
				prog.runStatement(stmt);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		System.out.printf("SQLRunner: %s done.%n", name);
		System.out.flush();
	}
}
