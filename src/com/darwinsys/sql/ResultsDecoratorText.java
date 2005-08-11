package com.darwinsys.sql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Print a ResultSet in plain text.
 * @version $Id$
 */
class ResultsDecoratorText extends ResultsDecorator {
	
	ResultsDecoratorText(ResultsDecoratorPrinter pt) {
		super(pt);
	}
	
	public void write(ResultSet rs) throws IOException,SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int cols = md.getColumnCount();
		for (int i = 1; i <= cols; i++) {
			print(md.getColumnName(i) + "\t");
		}
		println();
		while (rs.next()) {
			for (int i = 1; i <= cols; i++) {
				print(rs.getString(i) + "\t");
			}
			println();
		}
	}

	void write(int rowCount) throws IOException {
		println("OK: " + rowCount);
	}

	/* (non-Javadoc)
	 * @see ResultsDecorator#getName()
	 */
	String getName() {
		return "Plain text";
	}
}
