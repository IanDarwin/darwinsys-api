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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

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
 * <p>This class can also be used from within programs such as servlets, etc.;
 * see SQLRunnerGUI for an example of how to call.
 * <p>For example, this command and input:
 * <pre>
 * SQLrunner -c testdb
 * \ms;
 * select * from person where person_key=4;
 * </pre>
 * might produce this output:
 * <pre>
 * Executing : select * from person where person_key=4
 * insert into PERSON(PERSON_KEY,  FIRST_NAME, INITIAL, LAST_NAME, ... )
 * values (4, 'Ian', 'F', 'Darwin', ...);
 * </pre>
 * <p>TODO Fix parsing so \\ escapes don't need to end with SQL semi-colon.
 * <p>TODO add a "Manual Commit" (or "Undoable") mode, in CLI and GUI
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
// BEGIN main
// package com.darwinsys.sql;
public class SQLRunner {

	OutputMode outputMode = OutputMode.t;

	private static boolean okToExit = false;

	public static void setOkToExit(final boolean setting) {
		okToExit = setting;
	}

	public static boolean isOkToExit() {
		return okToExit;
	}

	public static void exit(final int exitStatus) {
		if (okToExit) {
			System.exit(exitStatus);
		} else {
			// do nothing
		}
	}

	/** Database connection */
	private Connection conn;

	private DatabaseMetaData dbMeta;

	/** SQL Statement */
	private Statement statement;

	/** Where the output is going */
	private PrintWriter out;

	private ResultsDecorator currentDecorator;

	/** Must be set at beginning */
	private ResultsDecorator textDecorator =
		new ResultsDecoratorText(out, verbosity);

	private ResultsDecorator sqlDecorator;

	private ResultsDecorator htmlDecorator;

	private ResultsDecorator xmlDecorator;

	private ResultsDecorator jtableDecorator;

	private boolean debug;

	private boolean escape;

	/** DB2 is the only one I know of today that requires table names
	 * be given in upper case when getting table metadata
	 */
	private boolean upperCaseTableNames;

	private SQLRunnerGUI gui;

	private static Verbosity verbosity = Verbosity.QUIET;

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
		commonSetup(outputFile, outputMode);
	}

	public SQLRunner(Connection c, String outputFile, String outputModeName) throws IOException, SQLException {
		// set up the SQL input
		conn = c;
		commonSetup(outputFile, outputModeName);
	}

	private void commonSetup(String outputFileName, String outputModeName) throws IOException, SQLException {
		dbMeta = conn.getMetaData();
		upperCaseTableNames =
			dbMeta.getDatabaseProductName().indexOf("DB2") >= 0;
		String dbName = dbMeta.getDatabaseProductName();
		System.out.println("SQLRunner: Connected to " + dbName);
		statement = conn.createStatement();
		
		if (outputFileName == null) {
			out = new PrintWriter(System.out);
		} else {
			out = new PrintWriter(new FileWriter(outputFileName));
		}

		setOutputMode(outputModeName);
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

		outputMode = OutputMode.valueOf(outputModeName);
		setOutputMode(outputMode);
	}

	/** Assign the correct ResultsDecorator, creating them on the fly
	 * using lazy evaluation.
	 */
	void setOutputMode(OutputMode outputMode) {
		ResultsDecorator newDecorator = null;
		switch (outputMode) {
			case t:
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
				newDecorator = xmlDecorator;
				break;
			case j:
				if (jtableDecorator == null) {
					if (gui == null) {
						throw new IllegalArgumentException("Can't set mode to JTable before calling setGUI()");
					}
					jtableDecorator = new ResultsDecoratorJTable(gui.getJTable(), out, verbosity);
				}
				newDecorator = jtableDecorator;
				break;
			default:
				System.err.println("invalid mode: "
								+ outputMode + "; must be one of: ");
				for (OutputMode t : OutputMode.values()) {
					out.print(t); out.print(' ');
				}
				out.println();
		}
		if (currentDecorator != newDecorator) {
			currentDecorator = newDecorator;
			if (debug)
				System.out.println("Mode set to  " + outputMode);
		}
		currentDecorator.setWriter(out);
	}

	/** Run one script file, by name. Called from cmd line main
	 * or from user code. Deprecated because of the poor capability
	 * for error handling; it would be better for the user interface
	 * code to create a Reader and then say:
	 * <pre>while ((stmt = SQLRunner.getStatement(is)) != null) {
			stmt = stmt.trim();
			try {
				myRunner.runStatement(stmt);
			} catch (Exception e) {
				// Display the message to the user ...
			}
		}
	 * </pre>
	 * @throws SyntaxException
	 */
	@Deprecated
	public void runScript(String scriptFile)
	throws IOException, SQLException, SyntaxException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(is, scriptFile);
	}

	/** Run one script, by name, given a BufferedReader.
	 * Deprecated because of the poor capability
	 * for error handling; it would be better for the
	 * user interface code to do:
	 * <pre>while ((stmt = SQLRunner.getStatement(is)) != null) {
			stmt = stmt.trim();
			try {
				myRunner.runStatement(stmt);
			} catch (Exception e) {
				// Display the message to the user ...
			}
		}
	 * </pre>
	 * @throws SyntaxException
	 */
	@Deprecated
	public void runScript(BufferedReader is, String name)
	throws IOException, SQLException, SyntaxException {
		String stmt;

		while ((stmt = getStatement(is)) != null) {
			stmt = stmt.trim();
			runStatement(stmt);
		}
	}

	/**
	 * Process an escape, like "\ms;" for mode=sql.
	 * @throws SyntaxException
	 */
	private void doEscape(String str) throws IOException, SQLException, SyntaxException  {
		String rest = null;
		if (str.length() > 2) {
			rest = str.substring(2);
		}

		if (str.startsWith("\\d")) {	// Display
			if (rest == null){
				throw new SyntaxException("\\d needs display arg");
			}
			display(rest);
		} else if (str.startsWith("\\m")) {	// MODE
			if (rest == null){
				throw new SyntaxException("\\m needs output mode arg");
			}
			setOutputMode(rest);
		} else if (str.startsWith("\\o")){
			if (rest == null){
				throw new SyntaxException("\\o needs output file arg");
			}
			setOutputFile(rest);
		} else if (str.startsWith("\\q")){
			exit(0);
		} else {
			throw new SyntaxException("Unknown escape: " + str);
		}
	}

	/**
	 * Display - generate output for \dt and similar escapes
	 * @param rest - what to display - the argument with the \d stripped off
	 * XXX: Move more formatting to ResultsDecorator: listTables(rs), listColumns(rs)
	 * @throws SyntaxException
	 */
	private void display(String rest) throws IOException, SQLException, SyntaxException {
		// setOutputMode(OutputMode.t);
		if (rest.equals("t")) {
			// Display list of tables
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", new String[]{"TABLE","VIEW"});
			textDecorator.setWriter(out);
			textDecorator.write(rs);
			textDecorator.flush();
		} else if (rest.startsWith("t")) {
			// Display one table. Some DatabaseMetaData implementations
			// don't do ignorecase so, for now, convert to UPPER CASE.
			String tableName = rest.substring(1).trim();
			if (upperCaseTableNames) {
				tableName = tableName.toUpperCase();
			}
			System.out.println("-- Display table " + tableName);
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getColumns(null, null, tableName, "%");
			currentDecorator.displayTable(tableName, rs);
			textDecorator.flush();
		} else
			throw new SyntaxException("\\d"  + rest + " invalid");
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static CachedRowSet cacheResultSet(ResultSet rs) throws SQLException {
		CachedRowSet rows = null;//new com.sun.rowset.WebRowSetImpl();
		rows.populate(rs);
		return rows;
	}

	/** Set the output to the given filename.
	 * @param fileName
	 */
	public void setOutputFile(String fileName) throws IOException {
		if (fileName == null) {
			/* Set the output file back to System.out */
			setOutputFile(new PrintWriter(System.out, true));
		} else {
			File file = new File(fileName);
			setOutputFile(new PrintWriter(new FileWriter(file), true));
			System.out.println("Output set to " + file.getCanonicalPath());
		}
	}

	/** Set the output to the given Writer; immediately update the textDecorator so \dt works...
	 * @param writer
	 */
	public void setOutputFile(PrintWriter writer) {
		out = writer;
		currentDecorator.setWriter(out);
	}

	/** Run one Statement, and format results as per Update or Query.
	 * Called from runScript or from user code.
	 * @throws SyntaxException
	 */
	public void runStatement(final String rawString) throws IOException, SQLException, SyntaxException {

		final String inString = rawString.trim();

		if (verbosity != Verbosity.QUIET) {
			out.println("Executing : <<" + inString + ">>");
			out.flush();
		}
		currentDecorator.println(String.format("-- output from command -- \"%s\"%n", inString));

		escape = false;
		if (inString.startsWith("\\")) {
			escape = true;
			doEscape(inString);
			return;
		}

		boolean hasResultSet = statement.execute(inString);			// DO IT - call the database.

		if (!hasResultSet) {
			currentDecorator.printRowCount(statement.getUpdateCount());
		} else {
			int n = currentDecorator.write(cacheResultSet(statement.getResultSet()));
			if (verbosity == Verbosity.VERBOSE || verbosity == Verbosity.DEBUG) {
				currentDecorator.printRowCount(n);
			}
		}
		currentDecorator.flush();
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
			line = line.trim();
			if (line.startsWith("#") || line.startsWith("--")) {
				continue;
			}
			if (line.startsWith("\\")) {
				if (sb.length() != 0) {
					throw new IllegalArgumentException("Escape command found inside statement");
				}
			}
			sb.append(line);
			int nb = sb.length();

			// If the buffer currently ends with ';', return it.
			if (nb > 0 && sb.charAt(nb-1) == ';') {
				if (nb == 1) {
					return null;
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
		if (statement != null) {
			statement.close();
		}
		if (conn != null) {
			conn.close();
		}
		out.flush();
		out.close();
	}

	public static Verbosity getVerbosity() {
		return verbosity;
	}

	public static void setVerbosity(Verbosity verbosity) {
		SQLRunner.verbosity = verbosity;
	}

	public void setErrorHandler(SQLRunnerErrorHandler eHandler) {
		gui.setErrorHandler(eHandler);
	}

	public void setGUI(SQLRunnerGUI gui) {
		this.gui = gui;
	}

	public String toString() {
		return "sqlrunner";
	}

	public boolean isEscape() {
		return escape;
	}


}
// END main
