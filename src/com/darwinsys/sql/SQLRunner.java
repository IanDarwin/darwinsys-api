import java.sql.*;
import java.io.*;
import java.util.*;

/** Class to run an SQL script, like psql(1) or similar programs.
 * Command line interface hard-codes sample driver and dburl,
 * expects script file name in argv[0].
 * Can be used from within servlet, etc.
 * @author	Ian Darwin, ian@darwinsys.com
 */
public class SQLRunner {

	/** The database driver */
	protected String db_driver;

	/** The database URL. */
	protected String db_url;

	protected String user, password;

	/** Database connection */
	protected Connection conn;

	/** SQL Statement */
	protected Statement stmt;

	public static void main(String[] args)
	throws ClassNotFoundException, SQLException, IOException {
		String DB_DRIVER;
		// DB_DRIVER = "jdbc.idbDriver";
		DB_DRIVER = "org.hsqldb.jdbcDriver";
		String DB_URL;
		//DB_URL = "jdbc:idb:orders.prp";
		DB_URL = "jdbc:hsqldb:ordersdb";

		try {
			SQLRunner prog = new SQLRunner(DB_DRIVER, DB_URL,
				"sa", "");
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
		this.user = user;
		this.password = password;

		// Load the database driver
		Class.forName(db_driver);

		conn = DriverManager.getConnection(
			db_url, user, password);

		stmt = conn.createStatement();
	}

	/** Run one script file. Called from cmd line main
	 * or from user code.
	 */
	public void runScript(String scriptFile)
	throws SQLException, IOException {

		BufferedReader is;

		// Load the script file first, it's the most likely error
		is = new BufferedReader(new FileReader(scriptFile));

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
	throws SQLException, IOException {
		System.out.println("Executing update : <<" + str + ">>");
		System.out.flush();
		int ret = stmt.executeUpdate(str);
		System.out.println("Return code: " + ret);
		System.out.println();
	}

	/** Extract one statement from the given Reader.
	 * Ignore comments and null lines.
	 */
	public static String getStatement(BufferedReader is)
	throws SQLException, IOException {
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
				ret = ret.substring(0, ret.length());
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
