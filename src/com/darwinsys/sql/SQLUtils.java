package com.darwinsys.sql;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/** Miscellaneous utilities for dealing with SQL.
 * @author ian
 */
public class SQLUtils {

	/** Process resultset, formatting it as HTML
	 * @param rs The valid ResultSet, which will be closed.
	 * @param out A PrintWriter to generate HTML to.
	 * @param style1 CSS style name for title and data rows 2, 4, 6, ...
	 * @param style2 CSS style name for data rows 1, 3, 5, ...
	 * @throws SQLException
	 */
	public static void resultSetToHTML(ResultSet rs, PrintWriter out,
			String style1, String style2) 
	throws SQLException {

		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		out.println("<table border=1>");
		
		// Print a table row with column headings.
		out.print("<tr>");
		for (int i=1; i<=count; i++) {
			out.printf("<th>%s</td>%n", md.getColumnName(i));
		}
		out.println("</tr>");
		
		// Print one table row of data for each row in the resultset.
		while (rs.next()) {
			out.print("<tr>");
			for (int i=1; i<=count; i++) {
				out.printf("<td id='%s'>%s</td>%n",
						i%2 == 0 ? style1 : style2, rs.getString(i));
			}
			out.println("</tr>");
		}
		
		// All done.
		out.println("</table>");
		rs.close();
	}
}
