import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Print a ResultSet in plain text.
 * @version $Id$
 */
class ResultsDecoratorText extends ResultsDecorator {
	
	ResultsDecoratorText(PrintWriter out) {
		super(out);
	}
	
	public void write(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int cols = md.getColumnCount();
		for (int i = 1; i <= cols; i++) {
			out.print(md.getColumnName(i) + "\t");
		}
		out.println();
		while (rs.next()) {
			for (int i = 1; i <= cols; i++) {
				out.print(rs.getString(i) + "\t");
			}
			out.println();
		}
		out.flush();
	}

	void write(int rowCount) throws SQLException {
		out.println("OK: " + rowCount);
		out.flush();
	}
}