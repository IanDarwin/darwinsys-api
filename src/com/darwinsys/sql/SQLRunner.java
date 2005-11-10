package com.darwinsys.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.lang.GetOpt;
import com.darwinsys.sql.ConnectionUtil;
import com.darwinsys.util.Verbosity;

/** Class to run an SQL script, like psql(1), SQL*Plus, or similar programs.
 * Command line interface accepts options -c config [-f configFile] [scriptFile].
 * <p>Input language is: escape commands (begin with \ and MUST end with semi-colon), or
 * standard SQL statements which must also end with semi-colon);
 * <p>Escape sequences: 
 * <ul>
 * <li> \m (output-mode), takes character t for text,
 * h for html, s for sql, x for xml (not in this version)
 * (the SQL output is intended to be usable to re-insert the data into another identical table,
 * but this has not been extensively tested!).
 * <li> \o output-file, redirects output.
 * <li> \q quit the program
 * </ul>
 * <p>This class can also be used from within programs such as servlets, etc.
  * <p>For example, this command and input:</pre>
 * SQLrunner -c testdb
 * \ms;
 * select * from person where person_key=4;
 * </pre>might produce this output:<pre>
 * Executing : <<select * from person where person_key=4>>
 *  insert into PERSON(PERSON_KEY,  FIRST_NAME, INITIAL, LAST_NAME, ... ) 
 * values (4, 'Ian', 'F', 'Darwin', ...);
 * </pre>
 * <p>TODO: Fix parsing so \\ escapes don't need to end with SQL semi-colon.
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
public class SQLRunner {
	
	/** The set of all valid modes. Short, lowercase names were used
	 * for simple use in \mX where X is one of the names.
	 */
	enum Mode {
		/** Mode for Text */
		t,
		/** Mode for HTML output */
		h,
		/** Mode for SQL output */
		s,
		/** Mode for XML output */
		x;
	};
	Mode outputMode = Mode.t;

	/** Database connection */
	protected Connection conn;

	/** SQL Statement */
	protected Statement statement;
	
	/** Where the output is going */
	protected PrintWriter out;
	
	private ResultsDecorator currentDecorator;

	private ResultsDecorator textDecorator;

	private ResultsDecorator sqlDecorator;
	
	private ResultsDecorator htmlDecorator;
	
	private ResultsDecorator xmlDecorator;
	
	private static Verbosity verbosity = Verbosity.QUIET;

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
				setVerbosity(Verbosity.DEBUG);
				break;
			case 'v':
				setVerbosity(Verbosity.VERBOSE);
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

	/** Construct a SQLRunner object
	 * @param driver String for the JDBC driver
	 * @param dbUrl String for the JDBC URL
	 * @param user String for the username
	 * @param password String for the password, normally in cleartext
	 * @param outputMode One of the MODE_XXX constants.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SQLRunner(String driver, String dbUrl, String user, String password,
			String outputFile, String outputMode)
			throws IOException, ClassNotFoundException, SQLException {
		conn = ConnectionUtil.getConnection(driver, dbUrl, user, password);
		finishSetup(outputFile, outputMode);
	}
	
	public SQLRunner(Connection c, String outputFile, String outputModeName) throws IOException, SQLException {
		// set up the SQL input
		conn = c;
		finishSetup(outputFile, outputModeName);
	}
	
	void finishSetup(String outputFileName, String outputModeName) throws IOException, SQLException {
		DatabaseMetaData dbm = conn.getMetaData();
		String dbName = dbm.getDatabaseProductName();
		System.out.println("SQLRunner: Connected to " + dbName);
		statement = conn.createStatement();
		
		if (outputFileName == null) {
			out = new PrintWriter(System.out);
		} else {
			out = new PrintWriter(new FileWriter(outputFileName));
		}
		
		setOutputMode(outputMode);
	}
	
	/** Set the output mode.
	 * @param outputMode Must be a value equal to one of the MODE_XXX values.
	 * @throws IllegalArgumentException if the mode is not valid.
	 */
	void setOutputMode(String outputModeName) {
		if (outputModeName == null || 
			outputModeName.length() == 0) { 
			System.err.println(
			"invalid mode: " + outputMode + "; must be t, h or s"); }
		
		outputMode = Mode.valueOf(outputModeName);
		setOutputMode(outputMode);
	}
	
	void setOutputMode(Mode outputMode) {
		// Assign the correct ResultsDecorator, creating them on the fly
		// using lazy evaluation.
		ResultsDecorator newDecorator = null;
		switch (outputMode) {
			case t:
				if (textDecorator == null) {
					textDecorator = new ResultsDecoratorText(out, verbosity);
				}
				newDecorator = textDecorator;
				break;
			case h:
				if (htmlDecorator == null) {
					htmlDecorator = new ResultsDecoratorHTML(out, verbosity);
				}
				newDecorator = htmlDecorator;
				break;
			case s:
				if (sqlDecorator == null) {
					sqlDecorator = new ResultsDecoratorSQL(out, verbosity);
				}
				newDecorator = sqlDecorator;
				break;
			case x:
				if (xmlDecorator == null) {
					xmlDecorator = new ResultsDecoratorXML(out, verbosity);
				}
				newDecorator = sqlDecorator;
				break;
			default:
				String values = Mode.values().toString();
				System.err.println("invalid mode: "
								+ outputMode + "; must be " + values);
		}
		if (currentDecorator != newDecorator) {
			currentDecorator = newDecorator;
			System.out.println("Mode set to  " + outputMode);
		}

	}
	
	/** Run one script file, by name. Called from cmd line main
	 * or from user code.
	 */
	public void runScript(String scriptFile)
	throws IOException, SQLException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(is, scriptFile);
	}

	/** Run one script, by name, given a BufferedReader. */
	public void runScript(BufferedReader is, String name)
	throws IOException, SQLException {
		String stmt;
		
		System.out.printf("SQLRunner: starting %s%n", name);
		while ((stmt = getStatement(is)) != null) {
			stmt = stmt.trim();
			if (stmt.startsWith("\\")) {
				doEscape(stmt);
			} else {
				runStatement(stmt);
			}
		}
		System.out.printf("SQLRunner: %s done.%n", name);
	}

	/**
	 * Process an escape like \ms; for mode=sql.
	 */
	private void doEscape(String str) throws IOException, SQLException  {
		String rest = null;
		if (str.length() > 2) {
			rest = str.substring(2);
		}
		
		if (str.startsWith("\\d")) {	// Display
			if (rest == null){
				System.err.println("\\d needs display arg");
			}
			display(rest);
		} else if (str.startsWith("\\m")) {	// MODE
			if (rest == null){
				System.err.println("\\m needs output mode arg");
			}
			setOutputMode(rest);
		} else if (str.startsWith("\\o")){
			if (rest == null){
				System.err.println("\\o needs output file arg");
			}
			setOutputFile(rest);
		} else if (str.startsWith("\\q")){
			System.exit(0);
		} else {
			System.err.println("Unknown escape: " + str);
		}
		
	}

	/**
	 * Display - something
	 * @param rest - what to display
	 * XXX: Move more formatting to ResultsDecorator: listTables(rs), listColumns(rs)
	 */
	private void display(String rest) throws SQLException {
		if (rest.equals("t")) {
			// Display list of tables
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
				System.out.println(rs.getString(3));
			}
		} else if (rest.startsWith("t")) {
			// Display one table. Some DatabaseMetaData implementations
			// don't do ignorecase so, for now, convert to UPPER CASE.
			String tableName = rest.substring(1).trim().toUpperCase();
			System.out.println("# Display table " + tableName);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getColumns(null, null, tableName, "%");
			while (rs.next()) {
				System.out.println(rs.getString(4));
			}
		} else
			System.err.println("\\d"  + rest + " invalid");
	}

	/** Set the output to the given filename.
	 * @param fileName
	 */
	private void setOutputFile(String fileName) throws IOException {
		if (fileName == null) {
			/* Set the output file back to System.out */
			out = new PrintWriter(System.out, true);
		} else {
			File file = new File(fileName);
			out = new PrintWriter(new FileWriter(file), true);
			System.out.println("Output set to " + file.getCanonicalPath());
		}
	}

	/** Run one Statement, and format results as per Update or Query.
	 * Called from runScript or from user code.
	 */
	public void runStatement(String str) throws IOException, SQLException {
		
		if (verbosity != Verbosity.QUIET) {
			System.out.println("Executing : <<" + str.trim() + ">>");		
			System.out.flush();
		}
		try {
			boolean hasResultSet = statement.execute(str);
			if (!hasResultSet)
				currentDecorator.printRowCount(statement.getUpdateCount());
			else {
				ResultSet rs = statement.getResultSet();
				int n = currentDecorator.write(rs);
				currentDecorator.printRowCount(n);
			}
		} catch (SQLException ex) {
			if (verbosity == Verbosity.QUIET) {
				System.err.println("Failure in : <<" + str.trim() + ">>");
			}
			if (verbosity == Verbosity.DEBUG){
				throw ex;
			} else {
				System.err.println("ERROR: " + ex.toString());
			}
		}
		if (verbosity != Verbosity.QUIET)
			System.out.println();
	}
	
	/** Extract one statement from the given Reader.
	 * Ignore comments and null lines.
	 * @return The SQL statement, up to but not including the ';' character.
	 * May be null if not statement found.
	 */
	public static String getStatement(BufferedReader is)
	throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = is.readLine()) != null) {
			if (verbosity == Verbosity.DEBUG) {
				System.out.println("SQLRunner.getStatement(): LINE " + line);
			}
			if (line == null || line.length() == 0) {
				continue;
			}
			if (line.startsWith("#") || line.startsWith("--")) {
				continue;
			}
			sb.append(line);
			int nb = sb.length();
			if (sb.charAt(nb-1) == ';') {
				if (nb == 1) {
					return "";
				}
				sb.setLength(nb-1);
				return sb.toString();
			}
			// Add a space in case the sql is generated by a tool
			// that doesn't remember to add spaces (hopefully this won't
			// break tools that output newlines inside quoted strings!).
			sb.append(' ');
		}
		return null;
	}

	public void close() throws SQLException {
		statement.close();
		conn.close();
		out.flush();
		out.close();
	}

	public static Verbosity getVerbosity() {
		return verbosity;
	}

	public static void setVerbosity(Verbosity verbosity) {
		SQLRunner.verbosity = verbosity;
	}

}
