package com.darwinsys.sql;

import java.io.BufferedReader;
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
				prog.runScript(new BufferedReader(
					new InputStreamReader(System.in)), "(standard input)");
			} else for (int i = go.getOptInd()-1; i < args.length; i++) {
				prog.runScript(args[i]);
			}
			prog.close();
		} catch (SQLException ex) {
			throw new DataBaseException(ex.toString());
		} catch (IOException ex) {
			throw new DataBaseException(ex.toString());
		}
		System.exit(0);
	}

}
