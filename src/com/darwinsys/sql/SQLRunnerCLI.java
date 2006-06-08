package com.darwinsys.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

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
	 * @throws SQLException if anything goes wrong.
	 * @throws DatabaseException if anything goes wrong.
	 */
	public static void main(String[] args)  {
		String config = "default";
		String outputModeName = "t";
		String outputFile = null;
		GetOpt go = new GetOpt("dvf:c:m:o:");
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

			Connection conn = ConnectionUtil.getConnection(config);

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

	static void runScript(SQLRunner prog, BufferedReader is, String name)
	throws IOException, SQLException {
		String stmt;
		
		System.out.printf("SQLRunner: starting %s%n", name);
		while ((stmt = SQLRunner.getStatement(is)) != null) {
			stmt = stmt.trim();
			prog.runStatement(stmt);			
		}
		System.out.printf("SQLRunner: %s done.%n", name);
	}
}
