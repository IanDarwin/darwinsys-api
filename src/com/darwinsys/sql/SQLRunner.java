import java.sql.*;
import java.io.*;
import java.util.*;

/** Class to run an SQL script, like psql(1), SQL*Plus, or similar programs.
 * Command line interface hard-codes sample driver and dburl,
 * expects script file name in argv[0].
 * Can be used from within servlet, etc.
 * @author	Ian Darwin, ian@darwinsys.com
 */
public class SQLRunner {

	/** The database driver */
	protected static String db_driver;

	/** The database URL. */
	protected static String db_url;

	protected static String db_user, db_password;

	/** Database connection */
	protected Connection conn;

	/** SQL Statement */
	protected Statement stmt;

	public static void main(String[] args)
	throws ClassNotFoundException, SQLException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream("db.properties"));
		db_driver = p.getProperty("db.driver");
		db_url = p.getProperty("db.url");
		db_user = p.getProperty("db.user");
		db_password = p.getProperty("db.password");

		try {
			SQLRunner prog = new SQLRunner(db_driver, db_url,
				db_user, db_password);
			if (args.length == 0)
				prog.runScript(new BufferedReader(
					new InputStreamReader(System.in)));
			else
				prog.runScript(args[0]);
			prog.close();
		} catch (Exception ex) {
			System.out.println("** ERROR **");
			System.out.println(ex.toString());
			System.exit(1);
		}
	}

	public SQLRunner(String driver, String dbUrl,
		String user, String password)
	throws ClassNotFoundException, SQLException {
		db_driver = driver;
		db_url = dbUrl;
		db_user = user;
		db_password = password;

		// Load the database driver
		Class.forName(db_driver);

		conn = DriverManager.getConnection(
			db_url, user, password);

		DatabaseMetaData dbm = conn.getMetaData();
		String dbName = dbm.getDatabaseProductName();
		System.out.println("SQLRunner: Connected to " + dbName);

		stmt = conn.createStatement();
	}

	/** Run one script file. Called from cmd line main
	 * or from user code.
	 */
	public void runScript(String scriptFile)
	throws IOException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

		runScript(is);
	}
	public void runScript(BufferedReader is)
	throws IOException {

		String str;
		int i = 0;
		System.out.println("SQLRunner: ready.");
		while ((str = getStatement(is)) != null) {
			runStatement(str);
		}
	}

	/** Run one Statement as an Update.
	 * Called from runScript or from user code.
	 */
	public void runStatement(String str)
	throws IOException {
		System.out.println("Executing : <<" + str.trim() + ">>");
		System.out.flush();
		try {
			boolean ret = stmt.execute(str);
			if (!ret)
				System.out.println("OK: " + stmt.getUpdateCount());
			else {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					System.out.println(rs.getString(1));
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
			if (!line.startsWith("#")) {
				ret += ' ' + line;
				found = true;
			}
			if (line.endsWith(";")) {
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
