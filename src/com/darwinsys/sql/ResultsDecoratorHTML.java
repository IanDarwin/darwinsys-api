import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/** Print ResultSet in HTML
 */
class ResultsDecoratorHTML extends ResultsDecorator {
	
	ResultsDecoratorHTML(PrintWriter out) {
		super(out);
	}
	
	public void write(ResultSet rs) throws SQLException {

		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		out.println("<table border=1>");
		out.print("<tr>");
		for (int i=1; i<=count; i++) {
			out.print("<th>");
			out.print(md.getColumnName(i));
		}
		out.println("</tr>");
		while (rs.next()) {
			out.print("<tr>");
			for (int i=1; i<=count; i++) {
				out.print("<td>");
				out.print(rs.getString(i));
			}
			out.println("</tr>");
		}
		out.println("</table>");
		out.flush();
	}

	/* (non-Javadoc)
	 * @see ResultSetDecorator#write(int)
	 */
	void write(int updateCount) throws SQLException {
		out.println("<br/>RowCount: updateCount = <b>" + 
					updateCount + "</p>");
	}
}