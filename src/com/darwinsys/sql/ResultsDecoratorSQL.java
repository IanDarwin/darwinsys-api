package com.darwinsys.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.io.*;
import java.sql.*;

import com.darwinsys.util.Verbosity;

/**
 * Print an SQL ResultSet in SQL-import format.
 * TODO: check all escaped characters needed! Test on PGSQL and DB2 at least...
 * @version $Id$
 */
public class ResultsDecoratorSQL extends ResultsDecorator {
	ResultsDecoratorSQL(ResultsDecoratorPrinter out, Verbosity v) {
		super(out, v);
	}
	public void write(ResultSet rs) throws IOException, SQLException {
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
			println(insertCommand);		
			for (int i = 1; i <= cols; i++) {
				String tmp = rs.getString(i);
				if (rs.wasNull()) {
					print("null");
				} else {
					int type = md.getColumnType(i);
					// Don't quote numeric types; quote all others for now.
					switch (type) {
						case Types.BIGINT:
						case Types.DECIMAL:
						case Types.DOUBLE:
						case Types.FLOAT:
						case Types.INTEGER:
							print(tmp);
							break;
						default:	
							tmp = tmp.replaceAll("'", "''");
							print("'" + tmp + "'");
					}
				}
				if (i != cols) {
					print( ", ");
				}
			}
			println(");");
		}
	}

	void write(int rowCount) throws IOException {
		println("RowCount: " + rowCount);
		
	}
	/* (non-Javadoc)
	 * @see ResultsDecorator#getName()
	 */
	String getName() {
		return "SQL";
	}
}
