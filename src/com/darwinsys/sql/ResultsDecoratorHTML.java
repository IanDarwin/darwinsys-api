import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/** Print ResultSet in HTML
 */
class ResultsDecoratorHTML extends ResultsDecorator {
	
	ResultsDecoratorHTML(ResultsDecoratorPrinter out) {
		super(out);
	}
	
	public void write(ResultSet rs) throws IOException, SQLException {

		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		println("<table border=1>");
		print("<tr>");
		for (int i=1; i<=count; i++) {
			print("<th>");
			print(md.getColumnLabel(i));
		}
		println("</tr>");
		while (rs.next()) {
			print("<tr>");
			for (int i=1; i<=count; i++) {
				print("<td>");
				print(rs.getString(i));
			}
			println("</tr>");
		}
		println("</table>");
	}

	/* (non-Javadoc)
	 * @see ResultSetDecorator#write(int)
	 */
	void write(int updateCount) throws IOException {
		println("<p>RowCount: updateCount = <b>" + 
					updateCount + "</p>");
	}

	/** Return a printable name for this decorator
	 * @see ResultsDecorator#getName()
	 */
	String getName() {
		return "HTML";
	}
}