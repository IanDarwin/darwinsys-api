import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.lang.GetOpt;

/** Class to run an SQL script, like psql(1), SQL*Plus, or similar programs.
 * Command line interface hard-codes sample driver and dburl,
 * expects script file name in argv[0].
 * Can be used from within servlet, etc.
 * TODO: knobs to set debug mode (interactively & from getopt!)
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
public class SQLRunner {

	/** The default configuration file */
	public static String DEFAULT_FILE = "db.properties";

	/** The database driver */
	protected static String db_driver;

	/** The database URL. */
	protected static String db_url;

	protected static String db_user, db_password;
	
	// TODO: This is an OBVIOUS candidate for a 1.5 "enum"
	
	/** The mode for textual output */
	public static final String MODE_TXT = "t";
	/** The mode for HTML output */
	public static final String MODE_HTML = "h";
	/** The mode for SQL output */
	public static final String MODE_SQL = "s";

	/** Database connection */
	protected Connection conn;

	/** SQL Statement */
	protected Statement stmt;
	
	private ResultsDecorator currentDecorator;

	private ResultsDecorator textDecorator;

	private ResultsDecorator sqlDecorator;
	
	private ResultsDecorator htmlDecorator;
	
	boolean debug = true;

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
	public static void main(String[] args) throws SQLException {
		String configFileName = System.getProperty("user.home", "/") +
			File.separator + "." + DEFAULT_FILE;
		String config = "default";
		String outputMode = MODE_TXT;
		GetOpt go = new GetOpt("f:c:m:");
		char c;
		while ((c = go.getopt(args)) != GetOpt.DONE) {
			switch(c) {
			case 'h':
				doHelp(0);
				break;
			case 'f':
				configFileName = go.optarg();
				break;
			case 'c':
				config = go.optarg();
				break;
			case 'm':
				outputMode = go.optarg();
				break;
			default:
				System.err.println("Unknown option character " + c);
				doHelp(1);
			}
		}

		try {
			Properties p = new Properties();
			p.load(new FileInputStream(configFileName));
			db_driver = p.getProperty(config  + "." + "DBDriver");
			db_url = p.getProperty(config  + "." + "DBURL");
			db_user = p.getProperty(config  + "." + "DBUser");
			db_password = p.getProperty(config  + "." + "DBPassword");
			if (db_driver == null || db_url == null) {
				throw new IllegalStateException("Driver or URL null: " + config);
			}

			SQLRunner prog = new SQLRunner(db_driver, db_url,
				db_user, db_password, outputMode);
			
			if (go.getOptInd() == args.length) {
				prog.runScript(new BufferedReader(
					new InputStreamReader(System.in)));
			} else for (int i = go.getOptInd(); i < args.length; i++) {
				prog.runScript(args[i]);
			}
			prog.close();
		// } catch (SQLException ex) {
		// 	throw new DataBaseException(ex.toString());
		} catch (ClassNotFoundException ex) {
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
			String outputMode)
			throws ClassNotFoundException, SQLException {
		conn = createConnection(driver, dbUrl, user, password);
		finishSetup(outputMode);
	}
	
	public SQLRunner(Connection c, String outputMode) throws SQLException {
		// set up the SQL input
		conn = c;
	}
	
	void finishSetup(String outputMode) throws SQLException {
		DatabaseMetaData dbm = conn.getMetaData();
		String dbName = dbm.getDatabaseProductName();
		System.out.println("SQLRunner: Connected to " + dbName);
		stmt = conn.createStatement();
		
		// Set up the output modes.
		textDecorator = new ResultsDecoratorText(new PrintWriter(System.out));
		sqlDecorator = new ResultsDecoratorSQL(new PrintWriter(System.out));
		htmlDecorator = new ResultsDecoratorHTML(new PrintWriter(System.out));
		setMode(outputMode);
	}
	
	/** Set the output mode.
	 * @param outputMode Must be a value equal to one of the MODE_XXX values.
	 * @throws IllegalArgumentException if the mode is not valid.
	 */
	void setMode(String outputMode) {
		if (outputMode.length() == 0) {
			throw new IllegalArgumentException(
					"invalid mode: " + outputMode + "; must be t, h or s");
		}
		switch( outputMode.charAt(0)) {
			case 't':
				currentDecorator = textDecorator;
				break;
			case 'h':
				currentDecorator = htmlDecorator;
				break;
			case 's':
				currentDecorator = sqlDecorator;
				break;
			default: throw new IllegalArgumentException(
					"invalid mode: " + outputMode + "; must be t, h or s");
		}
	}
	
	public Connection createConnection(String driver, String dbUrl, String user, String password)
			throws ClassNotFoundException, SQLException {
		db_driver = driver;
		db_url = dbUrl;
		db_user = user;
		db_password = password;

		// Load the database driver
		System.out.println("SQLRunner: Loading driver " + db_driver);
		Class.forName(db_driver);

		System.out.println("SQLRunner: Connecting to DB " + db_url);
		return DriverManager.getConnection(
			db_url, user, password);
	}
	

	/** Run one script file, by name. Called from cmd line main
	 * or from user code.
	 */
	public void runScript(String scriptFile)
	throws IOException, SQLException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(is);
	}

	/** Run one script, by name, given a BufferedReader. */
	public void runScript(BufferedReader is)
	throws IOException, SQLException {

		String stmt;
		int i = 0;
		System.out.println("SQLRunner: ready.");
		while ((stmt = getStatement(is)) != null) {
			stmt = stmt.trim();
			if (stmt.startsWith("\\")) {
				doEscape(stmt);
			} else {
				runStatement(stmt);
			}
		}
	}

	/**
	 * Process an escape like \ms; for mode=sql.
	 */
	private void doEscape(String str) {
		String rest = null;
		if (str.length() > 2) {
			rest = str.substring(2);
		}
		if (str.startsWith("\\m")) {	// MODE
			if (rest == null){
				throw new IllegalArgumentException("\\m needs arg");
			}
			setMode(rest);
		}
		
	}

	/** Run one Statement, and format results as per Update or Query.
	 * Called from runScript or from user code.
	 */
	public void runStatement(String str) throws SQLException {
		
		System.out.println("Executing : <<" + str.trim() + ">>");
		System.out.flush();
		try {
			boolean hasResultSet = stmt.execute(str);
			if (!hasResultSet)
				currentDecorator.write(stmt.getUpdateCount());
			else {
				ResultSet rs = stmt.getResultSet();
				currentDecorator.write(rs);
			}
		} catch (SQLException ex) {
			if (debug){
				throw ex;
			} else {
				System.out.println("ERROR: " + ex.toString());
			}
		}
		System.out.println();
	}
	
	/** Extract one statement from the given Reader.
	 * Ignore comments and null lines.
	 * @return The SQL statement, up to but not including the ';' character.
	 * May be null if not statement found.
	 */
	public static String getStatement(BufferedReader is)
	throws IOException {
		String ret="";
		String line;
		boolean found = false;
		while ((line = is.readLine()) != null) {
			if (line == null || line.length() == 0) {
				continue;
			}
			if (!(line.startsWith("#") || line.startsWith("--"))) {
				ret += ' ' + line;
				found = true;
			}
			if (line.endsWith(";")) {
				// Kludge, kill off empty statements (";") by itself, continue scanning.
				if (line.length() == 1)
					line = "";
				ret = ret.substring(0, ret.length()-1);
				return ret;
			}
		}
		return null;
	}

	public void close() throws SQLException {
		stmt.close();
		conn.close();
	}
}
