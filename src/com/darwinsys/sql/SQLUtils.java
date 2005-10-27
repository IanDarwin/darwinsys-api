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
	public static void resultSetToHTML(
			final ResultSet rs, final PrintWriter out,
			String style1, String style2,
			String keyColName, String link) 
	throws SQLException {

		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		out.println("<table border=1>");
		
		// Print a table row with column headings.
		out.printf("<tr id='%s'>", style1);
		for (int i=1; i<=count; i++) {
			out.printf("<th>%s</td>%n", md.getColumnName(i));
		}
		out.println("</tr>");
		
		// Print one table row of data for each row in the resultset.
		int rowNum = 0;
		while (rs.next()) {
			out.printf("<tr id='%s'>", rowNum++ % 2 == 1 ? style1 : style2);
			for (int i=1; i<=count; i++) {
				String linkText = rs.getString(i);
				if (md.getColumnName(i).equals(keyColName)) {
					linkText = String.format(
						"<a href='%s%s'>%s</a>", link, linkText, linkText);
				}					
				out.printf("<td>%s</td>", linkText);
			}
			out.println("</tr>");
		}
		
		// All done.
		out.println("</table>");
		rs.close();
	}
}
