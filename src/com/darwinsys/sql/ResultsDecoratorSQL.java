import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Print an SQL ResultSet in SQL-import format.
 * TODO: Don't quote numeric fields.
 * TODO: Check all escaped characters needed! Test on PGSQL and DB2 at least...
 * @version $Id$
 */
public class ResultsDecoratorSQL extends ResultsDecorator {
	ResultsDecoratorSQL(PrintWriter out) {
		super(out);
	}
	public void write(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		// This assumes you're not using a Join!!
		String tableName = md.getTableName(1);
		int cols = md.getColumnCount();
		StringBuffer sb = new StringBuffer("insert into ").append(tableName).append("(");
		for (int i = 1; i <= cols; i++) {
			sb.append(md.getColumnName(i));
			if (i != cols) {
				sb.append(", ");
			}
		}
		sb.append(") values (");
		String insertCommand = sb.toString();
		while (rs.next()) {
			out.println(insertCommand);		
			for (int i = 1; i <= cols; i++) {
				String tmp = rs.getString(i);
				if (rs.wasNull()) {
					out.print("null");
				} else {
					tmp = tmp.replaceAll("'", "''");
					out.print("'" + tmp + "'");
				}
				if (i != cols) {
					out.print( ", ");
				}
			}
			out.println(");");
		}
		out.flush();
	}

	void write(int rowCount) throws SQLException {
		System.out.println("RowCount: " + rowCount);
		
	}
}