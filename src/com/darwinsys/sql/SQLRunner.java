import java.sql.*;
import java.io.*;
import java.util.*;
import com.darwinsys.lang.*;	// for getopt

/** Class to run an SQL script, like psql(1), SQL*Plus, or similar programs.
 * Command line interface hard-codes sample driver and dburl,
 * expects script file name in argv[0].
 * Can be used from within servlet, etc.
 * @author	Ian Darwin, ian@darwinsys.com
 */
public class SQLRunner {

	/** The default configuration file */
	public static String DEFAULT_FILE = "db.properties";

	/** The database driver */
	protected static String db_driver;

	/** The database URL. */
	protected static String db_url;

	protected static String db_user, db_password;

	/** Database connection */
	protected Connection conn;

	/** SQL Statement */
	protected Statement stmt;

	private static void doHelp(int i) {
		System.out.println(
		"Usage: SQLRunner [-f configFile] [-c config] [SQLscript[ ...]");
		System.exit(i);
	}

	public static void main(String[] args)
	throws ClassNotFoundException, SQLException, IOException {
		String fileName = DEFAULT_FILE;
		String config = "default";
		GetOpt go = new GetOpt("f:c:");
		char c;
		while ((c = go.getopt(args)) != GetOpt.DONE) {
			switch(c) {
			case 'h':
				doHelp(0);
				break;
			case 'f':
				fileName = go.optarg();
				break;
			case 'c':
				config = go.optarg();
				break;
			default:
				System.err.println("Unknown option character " + c);
				doHelp(1);
			}
		}

		Properties p = new Properties();
		p.load(new FileInputStream(fileName));
		db_driver = p.getProperty(config  + "." + "db.driver");
		db_url = p.getProperty(config  + "." + "db.url");
		db_user = p.getProperty(config  + "." + "db.user");
		db_password = p.getProperty(config  + "." + "db.password");
		if (db_driver == null || db_url == null) {
			throw new IllegalStateException("Driver or URL null: " + config);
		}


		try {
			SQLRunner prog = new SQLRunner(db_driver, db_url,
				db_user, db_password);
			if (go.getOptInd() == args.length) {
				prog.runScript(new BufferedReader(
					new InputStreamReader(System.in)));
			} else for (int i = go.getOptInd(); i < args.length; i++) {
				prog.runScript(args[i]);
			}
			prog.close();
		} catch (SQLException ex) {
			System.out.println("** ERROR **");
			System.out.println(ex.toString());
			System.exit(1);
		} catch (Throwable ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	public SQLRunner(String driver, String dbUrl,
		String user, String password)
	throws ClassNotFoundException, SQLException {
		db_driver = driver;
		db_url = dbUrl;
		db_user = user;
		db_password = password;

		// Load the database driver
		System.out.println("SQLRunner: Loading driver " + db_driver);
		Class.forName(db_driver);

		System.out.println("SQLRunner: Connecting to DB " + db_url);
		conn = DriverManager.getConnection(
			db_url, user, password);

		DatabaseMetaData dbm = conn.getMetaData();
		String dbName = dbm.getDatabaseProductName();
		System.out.println("SQLRunner: Connected to " + dbName);

		stmt = conn.createStatement();
	}

	/** Run one script file, by name. Called from cmd line main
	 * or from user code.
	 */
	public void runScript(String scriptFile)
	throws IOException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(is);
	}

	/** Run one script, by name, given a BufferedReader. */
	public void runScript(BufferedReader is)
	throws IOException {

		String str;
		int i = 0;
		System.out.println("SQLRunner: ready.");
		while ((str = getStatement(is)) != null) {
			runStatement(str);
		}
	}

	/** Run one Statement, and format results as per Update or Query.
	 * Called from runScript or from user code.
	 */
	public void runStatement(String str)
	throws IOException {
		System.out.println("Executing : <<" + str.trim() + ">>");
		System.out.flush();
		try {
			boolean hasResults = stmt.execute(str);
			if (!hasResults)
				System.out.println("OK: " + stmt.getUpdateCount());
			else {
				ResultSet rs = stmt.getResultSet();
				ResultSetMetaData md = rs.getMetaData();
				int cols = md.getColumnCount();
				for (int i=1; i <= cols; i++) {
					System.out.print(md.getColumnName(i) + "\t");
				}
				System.out.println();

				while (rs.next()) {
					for (int i=1; i <= cols; i++) {
						System.out.print(rs.getString(i) + "\t");
					}
					System.out.println();
				}
			}
		} catch (SQLException ex) {
			System.out.println("ERROR: " + ex.toString());
		}
		System.out.println();
	}

	/** Extract one statement from the given Reader.
	 * Ignore comments and null lines.
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
